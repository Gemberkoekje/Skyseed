package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Entities;
import dev.gemberkoekje.skyseed.compat.Jigsaw;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.structure.PathSurfacer;
import dev.gemberkoekje.skyseed.worldgen.structure.Traps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
//? if >=26.1.2 {
/*import static net.minecraft.world.entity.EntitySpawnReason.SPAWNER;*/
//?} else {
import static net.minecraft.world.entity.MobSpawnType.SPAWNER;
//?}
//? if >=26.1.2 {
/*import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerType;*/
//?} else {
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
//?}
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Drains an {@link IslandPlan} into the world a bounded number of blocks per tick, so an island never
 * places thousands of blocks in a single tick (README → Generation algorithm). The gradual fill doubles as a "grows in"
 * animation. Trees (configured features) are heavier than a setBlock, so they go a couple per tick
 * once the solid blocks have landed; mobs are spawned last, once the whole island exists.
 */
public final class GenerationJob {
    private static final int BLOCKS_PER_TICK = 512;
    private static final int TREES_PER_TICK = 2;
    // Box (around a jigsaw origin) the connection-relink pass scans — generous enough for our biggest templates.
    private static final int LINK_RADIUS = 16;
    private static final int LINK_DOWN = 2;
    private static final int LINK_UP = 28;
    // forceOneTree's last-resort planting clearing (see #65): a small dirt pad under a tall air column, big enough for a
    // multi-block NBT tree (BWG willow/cypress) that couldn't fit against the packed island. Radius 2 = a 5x5 pad.
    private static final int FORCE_TREE_PAD_RADIUS = 2;
    private static final int FORCE_TREE_CLEAR_HEIGHT = 24;

    private final ServerLevel level;
    private final IslandPlan plan;
    private int blockIndex = 0;
    private int treeIndex = 0;
    private int treesPlaced = 0;
    private int scatterIndex = 0;
    /** Finalization sub-step (0 = structures, 1 = mobs/animals/hives/fluids, 2 = snow + done), spread across ticks. */
    private int finalizeStep = 0;
    /** Whether this job has force-loaded its region (released again on completion). */
    private boolean chunksForced = false;

    public GenerationJob(ServerLevel level, IslandPlan plan) {
        this.level = level;
        this.plan = plan;
    }

    /** Advance one tick. @return true once the whole island (blocks + trees + mobs) has been placed. */
    public boolean tick() {
        if (!chunksForced) {
            // Keep the island's chunks loaded for the whole (multi-tick) grow. Critical for a twin grown in a
            // player-less dimension, where nothing else tickets the region: a chunk written on an early tick could
            // otherwise unload before a later pass (structures/mobs/snow) reads it, silently dropping content there.
            setRegionForced(true);
            chunksForced = true;
        }
        final int blockCount = plan.blocks().size();
        int budget = BLOCKS_PER_TICK;
        while (budget-- > 0 && blockIndex < blockCount) {
            IslandPlan.BlockPlacement bp = plan.blocks().get(blockIndex++);
            if (plan.scatterPositions().contains(bp.pos())) {
                continue; // ground cover — deferred until after the trees so it can't block them
            }
            // UPDATE_CLIENTS only: show the block without neighbour/physics cascades.
            level.setBlock(bp.pos(), bp.state(), Block.UPDATE_CLIENTS);
        }
        if (blockIndex < blockCount) {
            return false;
        }

        final int treeCount = plan.trees().size();
        if (treeIndex < treeCount) {
            final ChunkGenerator generator = level.getChunkSource().getGenerator();
            int treeBudget = TREES_PER_TICK;
            while (treeBudget-- > 0 && treeIndex < treeCount) {
                IslandPlan.TreeSite ts = plan.trees().get(treeIndex++);
                if (ts.feature().place(level, generator, plan.random(), ts.pos())) {
                    treesPlaced++;
                }
            }
            if (treeIndex < treeCount) {
                return false;
            }
            // Guarantee at least one tree: a vanilla tree feature returns false if its spot is blocked (a snow layer,
            // a sandy surface, the rim), so an island that asked for trees could come up bare. If every site failed,
            // force one onto a cleared grass+air spot.
            if (treesPlaced == 0) {
                forceOneTree(generator);
            }
        }

        // Ground cover, placed now (after the trees) onto the bare terrain, skipping any spot a tree has taken — so
        // ground cover never makes a tree fail. Same blocks() list, re-scanned for the deferred scatter positions.
        if (scatterIndex < blockCount) {
            int scatterBudget = BLOCKS_PER_TICK;
            while (scatterBudget-- > 0 && scatterIndex < blockCount) {
                IslandPlan.BlockPlacement bp = plan.blocks().get(scatterIndex++);
                if (!plan.scatterPositions().contains(bp.pos())) {
                    continue; // terrain, already placed
                }
                if (level.getBlockState(bp.pos()).isAir()) {
                    level.setBlock(bp.pos(), bp.state(), Block.UPDATE_CLIENTS);
                }
            }
            if (scatterIndex < blockCount) {
                return false;
            }
        }

        // Finalize over several ticks instead of one: a big structure island would otherwise run jigsaw assembly +
        // connection-linking + villagers, then mobs/animals/hives/fluids, then a full-island snow scan all in the SAME
        // server tick — a multi-hundred-ms spike. One step per tick keeps each finalize tick bounded.
        switch (finalizeStep) {
            case 0 -> {
                placeStructures();
                finalizeStep++;
                return false;
            }
            case 1 -> {
                spawnMobs();
                spawnEnclosureAnimals();
                populateHives();
                kickFluids();
                finalizeStep++;
                return false;
            }
            default -> {
                if (plan.snow() > 0.0f) {
                    snowIsland(); // final step: snow-cap the whole island — ground, roofs and tree tops
                }
                setRegionForced(false); // island complete — release the chunk-load tickets taken on the first tick
                return true;
            }
        }
    }

    /**
     * Force-load (or release) every chunk column spanning the island's blocks plus any structure reach, so the whole
     * multi-tick job sees a stable, fully-loaded region (see {@link #tick()}'s first-tick call). Forced on the first
     * tick, released on completion; a hard crash mid-grow would leave them forced (recoverable with {@code /forceload
     * remove}). A no-op for a plan with no blocks.
     */
    private void setRegionForced(boolean add) {
        if (plan.blocks().isEmpty()) {
            return;
        }
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (IslandPlan.BlockPlacement bp : plan.blocks()) {
            final BlockPos p = bp.pos();
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minZ = Math.min(minZ, p.getZ());
            maxZ = Math.max(maxZ, p.getZ());
        }
        for (IslandPlan.JigsawSite js : plan.jigsaws()) {
            minX = Math.min(minX, js.origin().getX() - js.reach());
            maxX = Math.max(maxX, js.origin().getX() + js.reach());
            minZ = Math.min(minZ, js.origin().getZ() - js.reach());
            maxZ = Math.max(maxZ, js.origin().getZ() + js.reach());
        }
        for (int cx = minX >> 4; cx <= maxX >> 4; cx++) {
            for (int cz = minZ >> 4; cz <= maxZ >> 4; cz++) {
                level.setChunkForced(cx, cz, add);
            }
        }
    }

    /**
     * Last resort so a tree-bearing island is never bare: grade a real planting clearing at each candidate site and
     * place its feature there, stopping at the first success. The old single-block clear (one grass + one air) was too
     * cramped for the big multi-block NBT trees BWG uses — a wet-wood island (cypress / willow / white-mangrove) whose
     * every normal site was blocked by the packed rim came up with zero trees (#65). Now each site gets a small dirt pad
     * under a tall air column, which those features can actually grow into. Only ever runs when every normal placement
     * already failed ({@code treesPlaced == 0}), so a healthy island is untouched.
     */
    private void forceOneTree(ChunkGenerator generator) {
        for (IslandPlan.TreeSite ts : plan.trees()) {
            clearPlantingSpot(ts.pos());
            if (ts.feature().place(level, generator, plan.random(), ts.pos())) {
                treesPlaced++;
                return;
            }
        }
    }

    /**
     * Grade a small dirt pad under a tall air column at {@code base} (the tree's root cell), so a large tree feature has
     * the vertical + lateral room it needs. The pad is a {@link #FORCE_TREE_PAD_RADIUS}-radius dirt disc one block down
     * with a grassy centre; air is only ever cleared <em>above</em> the pad ({@link #FORCE_TREE_CLEAR_HEIGHT} tall), so
     * it can never punch a hole through the island body below.
     */
    private void clearPlantingSpot(BlockPos base) {
        for (int dx = -FORCE_TREE_PAD_RADIUS; dx <= FORCE_TREE_PAD_RADIUS; dx++) {
            for (int dz = -FORCE_TREE_PAD_RADIUS; dz <= FORCE_TREE_PAD_RADIUS; dz++) {
                level.setBlock(base.offset(dx, -1, dz), Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
                for (int dy = 0; dy < FORCE_TREE_CLEAR_HEIGHT; dy++) {
                    level.setBlock(base.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
        level.setBlock(base.below(), Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS); // grassy root cell
    }


    /**
     * Nudge planned water sources (a Ladder Island waterfall) into flowing. The block fill places everything with
     * {@code UPDATE_CLIENTS} only, so a source would otherwise sit inert until something disturbs it — scheduling a
     * fluid tick lets physics carry it down the open shaft.
     */
    private void kickFluids() {
        for (final BlockPos pos : plan.fluidTicks()) {
            level.scheduleTick(pos, Fluids.WATER, 1);
        }
    }

    /** Spawn an Animal Island's guaranteed pack inside its enclosure — babies aged down, sheep dyed, fish submerged. */
    private void spawnEnclosureAnimals() {
        for (IslandPlan.AnimalSpawn as : plan.animals()) {
            if (as.inWater()) {
                final BlockPos wet = submergedSpot(level, as.pos());
                if (wet == null) {
                    continue; // no tank water near this spot — skip rather than beach the animal
                }
                final Entity e = Entities.create(as.type(), level);
                if (e instanceof Mob mob) {
                    Entities.place(mob, wet.getX() + 0.5, wet.getY() + 0.5, wet.getZ() + 0.5,
                            plan.random().nextFloat() * 360.0F, 0.0F);
                    applyTraits(mob, as.baby());
                    EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(wet), SPAWNER, null);
                    level.addFreshEntity(mob);
                }
                continue;
            }
            final Entity e = as.type().spawn(level, freeStandSpot(as.pos()), SPAWNER);
            if (e != null) {
                applyTraits(e, as.baby());
            }
        }
    }

    /**
     * Finds a submerged spot at or below {@code from} for an aquatic mob. The rolled position assumes the tank water
     * sits a block above the pad (a raised surface pond), but a <em>sunk</em> tank — e.g. the flush Ocean Monument,
     * whose water now sits at island level rather than in a raised pool — puts the water lower, so the nominal spot is
     * air and the mob would beach. We walk down to the first water block and one block into it (so the mob spawns
     * inside the water, not bobbing at the surface). Returns {@code null} if there's no water within reach.
     */
    public static BlockPos submergedSpot(ServerLevel level, BlockPos from) {
        for (int dy = 0; dy <= 14; dy++) {
            final BlockPos p = from.below(dy);
            if (!level.getFluidState(p).isEmpty()) {
                final BlockPos deeper = p.below();
                return level.getFluidState(deeper).isEmpty() ? p : deeper;
            }
        }
        return null;
    }

    /**
     * The position a land mob should spawn at (its feet), given the floor block {@code base} it was planned on.
     * If that column is blocked by a kit block (a cauldron, hay, composter), search outward for a clear column
     * so the mob doesn't spawn inside furniture — e.g. a Witch Hut witch cooking herself in her own cauldron.
     */
    private BlockPos freeStandSpot(BlockPos base) {
        if (standClear(base)) {
            return base.above();
        }
        for (int r = 1; r <= 2; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    final BlockPos p = base.offset(dx, 0, dz);
                    if (standClear(p)) {
                        return p.above();
                    }
                }
            }
        }
        return base.above();
    }

    /** True if {@code base} is solid footing with two non-colliding blocks above it (room for a standing mob). */
    private boolean standClear(BlockPos base) {
        final BlockPos foot = base.above();
        final BlockPos head = base.above(2);
        return !level.getBlockState(base).getCollisionShape(level, base).isEmpty()
                && level.getBlockState(foot).getCollisionShape(level, foot).isEmpty()
                && level.getBlockState(head).getCollisionShape(level, head).isEmpty();
    }

    private void applyTraits(Entity e, boolean baby) {
        if (baby && e instanceof AgeableMob ageable) {
            ageable.setBaby(true);
        }
        if (e instanceof Sheep sheep) {
            sheep.setColor(DyeColor.byId(plan.random().nextInt(DyeColor.values().length)));
        }
        // Persist so curated spawns stay put — harmless for farm animals (they don't despawn), and it keeps
        // structure-island mobs (a Witch Hut's witch, an Outpost's pillagers) from despawning when unattended.
        if (e instanceof Mob mob) {
            mob.setPersistenceRequired();
        }
    }

    /** A stilted build's willow wood block ({@code biomeswevegone:willow_<suffix>}), falling back to the vanilla
     *  {@code fallback} when BWG isn't installed — so the stilt/boardwalk pass places plain-oak legs off-BWG (and in CI),
     *  and the swamp village (which only assembles with BWG present) gets its willow set. */
    private static BlockState swampWood(String suffix, Block fallback) {
        final Id id = Id.of("biomeswevegone:willow_" + suffix);
        return Lookup.hasBlock(id) ? Lookup.blockState(id) : fallback.defaultBlockState();
    }

    /**
     * Assemble the planned jigsaw structures, then spawn a villager at every bed they placed.
     *
     * <p>Trade-off: unlike the per-block fill, {@link Jigsaw#place} (vanilla {@code JigsawPlacement.generateJigsaw})
     * runs the whole structure
     * in one tick, un-budgeted — a possible frame spike for the big assemblies (Woodland Mansion + wings,
     * Village Center). It's a single vanilla call so it can't be chunked across ticks without reimplementing
     * jigsaw placement; in practice it fires once per island and the spike is acceptable. If it ever becomes a
     * problem, defer large structures by a tick (place them on a later {@link #tick()} than the blocks/trees).
     */
    private void placeStructures() {
        for (IslandPlan.JigsawSite js : plan.jigsaws()) {
            // Gate the data-driven pool id before Lookup.templatePool (which getOrThrows) so a theme referencing a pool
            // from an uninstalled structure mod — or a typo'd id — skips this structure instead of crashing the whole
            // grow job mid-island. Matches the hasTemplatePool gating every other resolve path already uses.
            if (!Lookup.hasTemplatePool(level.registryAccess(), js.pool())) {
                Skyseed.LOGGER.warn("[skyseed] jigsaw pool '{}' is not registered — skipping this structure", js.pool().value());
                continue;
            }
            final Holder<StructureTemplatePool> pool = Lookup.templatePool(level.registryAccess(), js.pool());
            final Holder<StructureTemplatePool> fillerPool = (js.capFiller().isEmpty()
                    || !Lookup.hasTemplatePool(level.registryAccess(), js.capFiller())) ? null
                    : Lookup.templatePool(level.registryAccess(), js.capFiller());
            Jigsaw.placeCapped(level, pool, js.target(), js.depth(), js.origin(), false,
                    js.capPrefix(), js.capCount(), fillerPool);
            // Re-add any support-dependent trap blocks the jigsaw path would have popped (plate / tripwire).
            Traps.applyAfterJigsaw(level, js.origin());
            // Foundation any solid lot floor left over the void FIRST (while the connective lanes are still markers
            // with empty decks, so they're skipped), THEN resolve the lane markers into terrain-aware paths and
            // over-void bridges — the lanes stay floating bridges, only buildings/fields/gardens get a foundation (§3a).
            if (js.reach() > 0) {
                if (js.stiltHeight() > 0) {
                    // A stilted bayou village: hang wooden legs down through the swamp water to the bed under the lots,
                    // and render the street lanes as plank boardwalks over the water (BWGSWAMPVILLAGEPLAN #73).
                    final BlockState post = swampWood("log", Blocks.OAK_LOG);
                    PathSurfacer.supportStilts(level, js.origin(), js.reach(), post);
                    PathSurfacer.resolveStilted(level, js.origin(), js.reach(),
                            swampWood("slab", Blocks.OAK_SLAB), swampWood("planks", Blocks.OAK_PLANKS),
                            swampWood("fence", Blocks.OAK_FENCE), post);
                } else {
                    if (js.trestles()) {
                        PathSurfacer.supportTrestles(level, js.origin(), js.reach()); // mineshaft: wooden legs over the void
                    } else {
                        PathSurfacer.supportFloatingFloors(level, js.origin(), js.reach());
                    }
                    PathSurfacer.resolve(level, js.origin(), js.reach());
                }
            }
            // Link up any fences / panes / walls (incl. the bridge railings just placed) in their default state.
            linkConnections(level, js.origin(), Math.max(LINK_RADIUS, js.reach()));
            spawnVillagersAtBeds(js.origin(), Math.max(js.pad(), js.reach()));
            // A capstone block at the very centre, if the theme set one. The start square's centre tile — its lantern —
            // sits exactly at the origin (the jigsaw seats the floor at origin.y - 1, so the block one above the floor
            // lands at origin), and the anvil replaces it, resting on that floor block so a falling block won't drop.
            final boolean hasCenterpiece = js.centerpiece().filter(Lookup::hasBlock).isPresent();
            js.centerpiece().filter(Lookup::hasBlock).ifPresent(cp ->
                    level.setBlock(js.origin(), Lookup.blockState(cp), 3));
            // Guard golems spawn at the centre — unless a centerpiece holds it, in which case post them a couple of
            // blocks aside (the start square is 7 wide) so they stand on the floor beside the capstone, not on it.
            final BlockPos[] guardSpots = {
                    js.origin().offset(2, 0, 0), js.origin().offset(-2, 0, 0),
                    js.origin().offset(0, 0, 2), js.origin().offset(0, 0, -2)};
            for (int i = 0; i < js.ironGolems(); i++) {
                final IronGolem golem = Entities.create(EntityType.IRON_GOLEM, level);
                if (golem != null) {
                    final BlockPos base = hasCenterpiece ? guardSpots[i % guardSpots.length] : js.origin();
                    final BlockPos spot = golemSpot(base);
                    Entities.place(golem, spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, 0.0F, 0.0F);
                    golem.setPersistenceRequired();
                    level.addFreshEntity(golem);
                }
            }
        }
    }

    /**
     * Snow-cap a cold-biome island once everything else is down: a snow layer on the highest block of every column,
     * from the island terrain out to wherever a town's streets/bridges sprawled (the jigsaw reach). The terrain bounds
     * come from the planned blocks; the scan covers a band near the top, deep enough for any roof/canopy and the
     * sloped island edges, so open-void columns inside the box stay cheap.
     */
    private void snowIsland() {
        if (plan.blocks().isEmpty()) {
            return;
        }
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (IslandPlan.BlockPlacement bp : plan.blocks()) {
            final BlockPos p = bp.pos();
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minZ = Math.min(minZ, p.getZ());
            maxZ = Math.max(maxZ, p.getZ());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }
        for (IslandPlan.JigsawSite js : plan.jigsaws()) {
            minX = Math.min(minX, js.origin().getX() - js.reach());
            maxX = Math.max(maxX, js.origin().getX() + js.reach());
            minZ = Math.min(minZ, js.origin().getZ() - js.reach());
            maxZ = Math.max(maxZ, js.origin().getZ() + js.reach());
            maxY = Math.max(maxY, js.origin().getY());
        }
        final int top = maxY + 16;                      // headroom above the tallest roof / tree canopy
        final int bottom = Math.max(minY, maxY - 24);   // a band near the top — covers sloped edges, skips the deep void
        PathSurfacer.snowCover(level, new BlockPos(minX, bottom, minZ), new BlockPos(maxX, top, maxZ),
                plan.snow(), plan.random());
    }

    /**
     * Re-derive the connection state of any fences, panes/bars or walls in a just-placed structure's footprint.
     * Jigsaw (structure) placement copies each stored blockstate verbatim with no neighbour update, so a connecting
     * block written in its default state renders as an unconnected post; re-running the vanilla shape update links it
     * to its real in-world neighbours (including the island terrain it sits against). Applied to every structure —
     * a no-op wherever there is nothing to link. Scans a box around the jigsaw origin large enough for our templates.
     */
    public static void linkConnections(ServerLevel level, BlockPos origin) {
        linkConnections(level, origin, LINK_RADIUS);
    }

    /** As {@link #linkConnections(ServerLevel, BlockPos)} but scanning {@code radius} out — sized to the structure. */
    public static void linkConnections(ServerLevel level, BlockPos origin, int radius) {
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        // Column-outer so an unloaded column is skipped with ONE isLoaded check instead of paying a getBlockState for
        // every cell in it. A wide structure's reach box is mostly empty void; without this guard (the other passes all
        // have it) the scan did ~(2·radius+1)^2·(LINK_DOWN+LINK_UP+1) unconditional reads — and getBlockState on a cold
        // column can force-load it. Linking only reads neighbour block *types* (not their link flags), so the per-column
        // order is equivalent to the old layer-by-layer order.
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                p.set(origin.getX() + dx, origin.getY(), origin.getZ() + dz);
                if (!level.isLoaded(p)) {
                    continue;
                }
                for (int dy = -LINK_DOWN; dy <= LINK_UP; dy++) {
                    p.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    final BlockState state = level.getBlockState(p);
                    if (state.getBlock() instanceof CrossCollisionBlock || state.getBlock() instanceof WallBlock) {
                        final BlockState linked = Block.updateFromNeighbourShapes(state, level, p);
                        if (linked != state) {
                            level.setBlock(p.immutable(), linked, Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
    }

    /**
     * Feet position for a structure's iron golem. The structure floor sits a block below the jigsaw origin, so
     * the golem stands at the origin itself; but it's ~2.7 tall, so spawning it on the floor of a tight pen (the
     * Outpost cage, a 3-tall box) jams its head into the ceiling and suffocates it. Find the lowest level at/above
     * the floor with three clear blocks, dropping the golem to the floor where there's room and only nudging up
     * when the floor column is blocked.
     */
    private BlockPos golemSpot(BlockPos origin) {
        for (int dy = 0; dy <= 4; dy++) {
            final BlockPos feet = origin.above(dy);
            if (clearForGolem(feet)) {
                return feet;
            }
        }
        return origin.above(); // nothing clear nearby — fall back to the old one-up spawn
    }

    /** True if {@code feet} and the two blocks above it have no collision (room for a ~2.7-tall iron golem). */
    private boolean clearForGolem(BlockPos feet) {
        for (int h = 0; h < 3; h++) {
            final BlockPos p = feet.above(h);
            if (!level.getBlockState(p).getCollisionShape(level, p).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * One villager per bed in the assembled structure — adult, dressed for the biome, persistent. They
     * arrive unemployed; a villager beside an unclaimed job-site block (the shops carry them) takes up
     * that profession on its own, exactly as in a natural village.
     */
    private void spawnVillagersAtBeds(BlockPos origin, int radius) {
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                p.set(origin.getX() + dx, origin.getY(), origin.getZ() + dz);
                if (!level.isLoaded(p)) {
                    continue; // a village's reach is wide — don't force-load the empty void around it
                }
                for (int dy = -1; dy <= 6; dy++) {
                    p.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    final BlockState state = level.getBlockState(p);
                    if (!state.is(Blocks.RED_BED) || state.getValue(BedBlock.PART) != BedPart.FOOT) {
                        continue;
                    }
                    final Villager villager = Entities.create(EntityType.VILLAGER, level);
                    if (villager != null) {
                        Entities.place(villager, p.getX() + 0.5, p.getY(), p.getZ() + 0.5, 0.0F, 0.0F);
                        // Assign the biome-appropriate villager type. 1.21.1: VillagerType.byBiome directly. 26.1.2:
                        // VillagerType became a Holder/registry and byBiome was removed, so run vanilla's finalizeMobSpawn
                        // (as the mob-spawn paths above/below do) which sets the type from the spawn biome — addFreshEntity
                        // alone does NOT call it, so without this the villager would keep the default (plains) variant.
                        //? if >=26.1.2 {
                        /*EventHooks.finalizeMobSpawn(villager, level, level.getCurrentDifficultyAt(p), SPAWNER, null);*/
                        //?} else {
                        villager.setVillagerData(villager.getVillagerData().setType(VillagerType.byBiome(level.getBiome(p))));
                        //?}
                        villager.setPersistenceRequired();
                        level.addFreshEntity(villager);
                    }
                }
            }
        }
    }

    /** Fill any bee nests on the island with a few bees (they emerge to pollinate, then return home). */
    private void populateHives() {
        for (BlockPos hive : plan.hives()) {
            if (!(level.getBlockEntity(hive) instanceof BeehiveBlockEntity beehive)) {
                continue;
            }
            for (int i = 0; i < 3; i++) {
                Bee bee = Entities.create(EntityType.BEE, level);
                if (bee != null) {
                    Entities.place(bee, hive.getX() + 0.5, hive.getY() + 0.5, hive.getZ() + 0.5, 0.0F, 0.0F);
                    beehive.addOccupant(bee);
                }
            }
        }
    }

    /** Spawn the planned mobs: land mobs on top of their surface block, water mobs inside the pond. */
    private void spawnMobs() {
        for (IslandPlan.MobSpawn ms : plan.mobs()) {
            if (ms.inWater()) {
                final BlockPos wp = ms.pos();
                if (level.getFluidState(wp).isEmpty()) {
                    continue; // the pond water at this spot got displaced (e.g. by a plant block)
                }
                // Place submerged, centred in the water block — EntityType#spawn aligns to the block top,
                // which would leave a squid/axolotl out of the water and they'd promptly die.
                final Entity e = Entities.create(ms.type(), level);
                if (e instanceof Mob mob) {
                    Entities.place(mob, wp.getX() + 0.5, wp.getY() + 0.5, wp.getZ() + 0.5, plan.random().nextFloat() * 360.0F, 0.0F);
                    EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(wp), SPAWNER, null);
                    level.addFreshEntity(mob);
                }
                continue;
            }
            final BlockPos surface = ms.pos();
            final BlockPos spawnPos = surface.above();
            // Need solid ground and two blocks of standing room — but plants (flowers, grass, mushrooms)
            // don't block a mob, so allow those; only skip on no ground or a real obstruction (trunk, leaves).
            if (level.getBlockState(surface).isAir() || blocked(spawnPos) || blocked(spawnPos.above())) {
                continue;
            }
            ms.type().spawn(level, spawnPos, SPAWNER);
        }
    }

    /** True if a mob can't occupy {@code p}: the block there has a collision shape (air, plants and water don't). */
    private boolean blocked(BlockPos p) {
        return !level.getBlockState(p).getCollisionShape(level, p).isEmpty();
    }
}
