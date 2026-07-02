package dev.gemberkoekje.skyseed.event;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Entities;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.SkyseedWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/**
 * Makes Quark's Totem of Holding usable in a void skyblock. On a normal death the totem spawns at the death spot and
 * hangs there; but when a player falls into the void, that spot is at the bottom of the world (~y-64, lower in the
 * End) — floating in empty space, unreachable without flight. So when a freshly-spawned {@code quark:totem} lands
 * below {@link #VOID_DEATH_MAX_Y} we treat it as a void death: keep its x/z (so it stays where the player fell, and
 * Xaero's death waypoint still points at it), raise its y to the island band — rising to the first clear cell above
 * if that spot happens to sit inside an island directly under the death point, so the totem never lands embedded —
 * and build a small lit shrine under it. The shrine only ever ADDS blocks (fills air cells); it never overwrites an
 * island it happens to land beside.
 *
 * <p>Decoupled from Quark: the totem is matched by its registry id via {@link Lookup}, so with Quark Oddities absent
 * the id simply doesn't resolve and this no-ops. Only runs on Skyseed worlds (a curated start island exists).
 */
@EventBusSubscriber(modid = Skyseed.MODID)
public final class TotemShrineEvents {
    /** Quark registers the Totem of Holding entity as {@code quark:totem} (lang key {@code entity.quark.totem}). */
    private static final Id QUARK_TOTEM = Id.of("quark:totem");
    /** Below this y a totem must have come from a void plunge, not a death on/near an island (band ≈ y100). */
    private static final int VOID_DEATH_MAX_Y = 50;
    private static final int FLAGS = Block.UPDATE_CLIENTS;

    /** Resolved once from the frozen entity registry; {@code null} when Quark Oddities isn't installed. */
    private static EntityType<?> totemType;
    private static boolean totemResolved;

    private TotemShrineEvents() {}

    @SubscribeEvent
    static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk()) {
            return; // only act on the fresh totem spawned at death, not on every chunk reload
        }
        Entity entity = event.getEntity();
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return; // server side only
        }
        EntityType<?> totem = totemType();
        if (totem == null || entity.getType() != totem) {
            return; // not a Quark totem (or Quark Oddities absent)
        }
        if (entity.getY() >= VOID_DEATH_MAX_Y) {
            return; // died somewhere already reachable — leave the totem where Quark put it
        }

        BlockPos band = islandBand(level);
        if (band == null) {
            return; // not a Skyseed world — don't relocate totems in a vanilla-generated world
        }

        final BlockPos death = entity.blockPosition();
        final int cx = death.getX();
        final int cz = death.getZ();
        // The band height may sit inside an island if the player fell straight down one — rise to the first cell
        // where the totem (and the block above it) is clear, so it's never embedded.
        final int totemY = firstClearY(level, cx, band.getY(), cz);
        buildShrine(level, cx, totemY, cz);
        // Sit the totem on the shrine floor, keeping its facing.
        Entities.place(entity, cx + 0.5, totemY, cz + 0.5, entity.getYRot(), entity.getXRot());
        Skyseed.LOGGER.info("[skyseed] void death: raised Totem of Holding from y={} to a shrine at ({}, {}, {})",
                death.getY(), cx, totemY, cz);
    }

    /**
     * The island-band position (its y is the target height) for the world this {@code level} belongs to, or
     * {@code null} if it isn't a Skyseed world. The flag lives on the overworld's saved data (mirrors PlayerEvents).
     */
    private static BlockPos islandBand(ServerLevel level) {
        MinecraftServer server = level.getServer();
        ServerLevel overworld = server.overworld();
        //? if >=26.1.2 {
        /*SkyseedWorldData world = overworld.getDataStorage().computeIfAbsent(SkyseedWorldData.TYPE);*/
        //?} else {
        SkyseedWorldData world = overworld.getDataStorage()
                .computeIfAbsent(SkyseedWorldData.factory(), SkyseedWorldData.NAME);
        //?}
        return world.getStartSpawn(); // null on existing/non-Skyseed worlds
    }

    /**
     * A tiny shrine centred on {@code (cx, cz)} whose floor top sits one below {@code spawnY} (the totem rests on it):
     * a 3×3 stone-brick platform with a glowing centre, four short corner posts capped with soul lanterns, and a clear
     * centre column for the totem and the collecting player. Everything is lit so nothing spawns on the platform.
     */
    private static void buildShrine(ServerLevel level, int cx, int totemY, int cz) {
        final int floorY = totemY - 1;
        final BlockState bricks = Blocks.STONE_BRICKS.defaultBlockState();
        final BlockState glow = Blocks.SEA_LANTERN.defaultBlockState();
        final BlockState wall = Blocks.STONE_BRICK_WALL.defaultBlockState();
        final BlockState lantern = Blocks.SOUL_LANTERN.defaultBlockState();
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        // 3×3 floor: a glowing centre (the totem stands on it) ringed by stone bricks.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                addBlock(level, pos, cx + dx, floorY, cz + dz, (dx == 0 && dz == 0) ? glow : bricks);
            }
        }
        // Four corner posts (two tall) capped with soul lanterns — a lit, beacon-like frame.
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dz = -1; dz <= 1; dz += 2) {
                addBlock(level, pos, cx + dx, floorY + 1, cz + dz, wall);
                addBlock(level, pos, cx + dx, floorY + 2, cz + dz, wall);
                addBlock(level, pos, cx + dx, floorY + 3, cz + dz, lantern);
            }
        }
    }

    /** Place {@code state} only where the target cell is currently air — so the shrine only ever ADDS to the world
     *  and never overwrites an island it happens to grow beside. (The centre column needs no explicit clearing:
     *  {@link #firstClearY} already guaranteed the totem's cell + the one above are air.) */
    private static void addBlock(ServerLevel level, BlockPos.MutableBlockPos pos, int x, int y, int z, BlockState state) {
        if (level.getBlockState(pos.set(x, y, z)).isAir()) {
            level.setBlock(pos, state, FLAGS);
        }
    }

    /** The lowest y at/above {@code startY} where both the totem's cell and the block above it are air, so a moved
     *  totem is never embedded in an island sitting directly under the death spot. Capped so a freak column can't
     *  send the search runaway (in the void this hits {@code startY} immediately). */
    private static int firstClearY(ServerLevel level, int cx, int startY, int cz) {
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        final int capY = startY + 64;
        for (int y = startY; y <= capY; y++) {
            if (level.getBlockState(pos.set(cx, y, cz)).isAir()
                    && level.getBlockState(pos.set(cx, y + 1, cz)).isAir()) {
                return y;
            }
        }
        return startY;
    }

    private static EntityType<?> totemType() {
        if (!totemResolved) {
            totemType = Lookup.hasEntityType(QUARK_TOTEM) ? Lookup.entityType(QUARK_TOTEM) : null;
            totemResolved = true;
        }
        return totemType;
    }
}
