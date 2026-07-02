package dev.gemberkoekje.skyseed.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves the path markers a connective jigsaw piece leaves behind (SKYJIGSAWPLAN §3a) into a terrain-aware
 * surface — a worn dirt path where the deck sits on ground, a self-railing wooden bridge where it runs out over
 * the void. Connective pieces bake no floor; they place a reserved {@link #MARKER} one block <em>above</em> each
 * path tile, and this pass reads what is under each marker and fills it in:
 * <ul>
 *   <li>solid ground under the deck → a worn {@code dirt_path};</li>
 *   <li>void under the deck → a wooden-slab deck, and for every side that is itself an open drop (a neighbour
 *       that is not a path tile and is also over void) a full-block edge beam + a fence railing — a free,
 *       barebones bridge that rails its exposed sides and caps its dead-ends, scaling with path width.</li>
 * </ul>
 * Two-phase: it snapshots every marker first, resolves decks + edges from that snapshot, then clears the markers
 * <em>last</em> — so reading a neighbour can never be fooled by an already-cleared marker. Whether a tile is over
 * void is decided by the block <em>under the deck</em> (not the deck itself), so it is robust to the deck tile
 * having been cleared to air by a connector. Called once per assembled structure by {@code GenerationJob}; a
 * no-op wherever there are no markers.
 */
public final class PathSurfacer {
    private PathSurfacer() {}

    /** The reserved sentinel a connective piece places one block above each path tile. Never used decoratively. */
    public static final Block MARKER = Blocks.PURPLE_WOOL;

    private static final int SCAN_DOWN = 2;
    private static final int SCAN_UP = 4;
    private static final int FLAGS = Block.UPDATE_CLIENTS; // no physics — fences are linked separately afterwards
    private static final int SUPPORT_SEARCH = 8; // how far below a floating floor to look for ground to anchor onto
    private static final int SUPPORT_STUB = 2;   // over pure void (no ground found within the search): a short stub only
    private static final int TRESTLE_STUB = 4;   // a mineshaft trestle leg hangs a little longer over pure void
    private static final int CLEAR_ABOVE = 8;    // how far above a laid path tile to strip a tree trunk/canopy that grew there

    /** The four woods a bridge/boardwalk lays: {@code deck} slab, {@code beam} edge, {@code fence} railing, {@code post}
     *  leg. Void bridges use {@link #OAK}; a stilted swamp build passes its willow/cypress set via {@link #resolveStilted}. */
    private record Wood(BlockState deck, BlockState beam, BlockState fence, BlockState post) {}

    private static final Wood OAK = new Wood(Blocks.OAK_SLAB.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(),
            Blocks.OAK_FENCE.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState());

    /** Resolve every path marker within {@code reach} (horizontal half-extent) of {@code origin}. Oak, void-only. */
    public static void resolve(ServerLevel level, BlockPos origin, int reach) {
        resolve(level, origin, reach, OAK, false);
    }

    /**
     * As {@link #resolve}, but for a STILTED bayou build: lanes over WATER (as well as void) become plank bridges in the
     * given {@code deck}/{@code beam}/{@code fence} wood, each railed edge dropping a {@code post} leg down to the bed —
     * a boardwalk over the swamp. (BWGSWAMPVILLAGEPLAN #73.)
     */
    public static void resolveStilted(ServerLevel level, BlockPos origin, int reach,
                                      BlockState deck, BlockState beam, BlockState fence, BlockState post) {
        resolve(level, origin, reach, new Wood(deck, beam, fence, post), true);
    }

    /** Resolve every path marker within {@code reach} (horizontal half-extent) of {@code origin}. */
    private static void resolve(ServerLevel level, BlockPos origin, int reach, Wood wood, boolean overWater) {
        // Phase A — snapshot the markers and the set of deck (path-tile) columns they cover.
        final List<BlockPos> markers = new ArrayList<>();
        final Set<Long> deckTiles = new HashSet<>();
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                p.set(origin.getX() + dx, origin.getY(), origin.getZ() + dz);
                if (!level.isLoaded(p)) {
                    continue; // never force-load chunks the structure didn't reach — a wide reach stays cheap
                }
                for (int dy = -SCAN_DOWN; dy <= SCAN_UP; dy++) {
                    p.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (level.getBlockState(p).is(MARKER)) {
                        markers.add(p.immutable());
                        deckTiles.add(p.below().asLong());
                    }
                }
            }
        }
        if (markers.isEmpty()) {
            return;
        }
        // Phase B — resolve each deck tile. A STILTED build lays a plank boardwalk on posts for every lane (it is
        // lifted over the swamp, so its tiles are always over water/air); a normal build reads the void/ground test.
        for (final BlockPos marker : markers) {
            final BlockPos deck = marker.below();
            if (overWater) {
                boardwalk(level, deck, deckTiles, wood); // a plank walkway on posts, railed only along open-water edges
            } else if (level.getBlockState(deck.below()).isAir()) {
                bridge(level, deck, deckTiles, wood); // over the void — a self-railing wooden bridge
            } else {
                level.setBlock(deck, Blocks.DIRT_PATH.defaultBlockState(), FLAGS); // a uniform worn path (no stripes)
            }
            clearCanopyAbove(level, deck); // trees are placed BEFORE structures, so a lane laid across a tree column
                                           // leaves its trunk floating on the road — strip it (keeps trees in the gaps).
        }
        // Phase C — clear the markers (after every neighbour has been read).
        for (final BlockPos marker : markers) {
            level.setBlock(marker, Blocks.AIR.defaultBlockState(), FLAGS);
        }
    }

    /**
     * A stilted-boardwalk deck tile: a plank deck on a sparse (even/even) post, with a fence rail run along each side
     * that opens onto water/void. Crucially the rail is placed ONLY on a neighbour that is open water or void — never on
     * another lane tile (kept walkable) and never on a solid block (a house floor/wall or the plaza), so it can neither
     * overwrite a building nor fence a road shut (both were the earlier bugs). The rail sits in the adjacent open cell at
     * deck height + a sparse post drops to the bed under it, so a walkway edge reads as a railed pier while the lanes and
     * doorways themselves stay clear. Water is left visible between the sparse posts. (BWGSWAMPVILLAGEPLAN #73.)
     */
    private static void boardwalk(ServerLevel level, BlockPos deck, Set<Long> deckTiles, Wood wood) {
        level.setBlock(deck, wood.deck(), FLAGS);
        if (evenGrid(deck)) {
            stiltDown(level, deck, wood.post()); // a pier post under the walkway (a no-op where it already rests on ground)
        }
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final BlockPos nb = deck.relative(dir);
            final BlockState nbState = level.getBlockState(nb);
            // Never rail a walkable neighbour: skip another lane tile and skip any SOLID block (house/plaza edge, a door
            // threshold). Only an open cell (air or the swamp water itself) gets a rail.
            if (deckTiles.contains(nb.asLong()) || (!nbState.isAir() && nbState.getFluidState().isEmpty())) {
                continue;
            }
            // ...and only where that cell opens onto a drop (water/void below), not where it rests on the shore.
            final BlockState nbUnder = level.getBlockState(nb.below());
            if (!nbUnder.isAir() && nbUnder.getFluidState().isEmpty()) {
                continue;
            }
            level.setBlock(nb.above(), wood.fence(), FLAGS); // the rail, along the open-water edge
            if (evenGrid(nb)) {
                level.setBlock(nb, wood.post(), FLAGS);
                stiltDown(level, nb, wood.post()); // a sparse rail post down into the water
            }
        }
    }

    /** The even/even world-grid that the sparse pier posts land on — shared so lot legs, deck posts and rail posts align. */
    private static boolean evenGrid(BlockPos p) {
        return (p.getX() & 1) == 0 && (p.getZ() & 1) == 0;
    }

    /** A void deck tile: a slab deck, plus an edge beam + fence railing on each side that is itself an open drop. */
    private static void bridge(ServerLevel level, BlockPos deck, Set<Long> deckTiles, Wood wood) {
        level.setBlock(deck, wood.deck(), FLAGS);
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final BlockPos nb = deck.relative(dir);
            if (!deckTiles.contains(nb.asLong()) && level.getBlockState(nb.below()).isAir()) {
                level.setBlock(nb, wood.beam(), FLAGS);        // edge beam
                level.setBlock(nb.above(), wood.fence(), FLAGS); // railing
            }
        }
    }

    /**
     * Drop a short dirt foundation under any building / field / garden floor left hanging over the void, so a lot
     * that ran off the island edge reads as anchored rather than floating in mid-air. Scans the structure's deck
     * level (one below {@code origin}) for any solid (non-air) block sitting over an open drop and stilts it down a
     * few blocks. Run this BEFORE {@link #resolve}: a connective lane is still just a marker with an empty (air)
     * deck at that point, so the lanes are skipped and stay floating bridges — only the solid lot floors get a
     * foundation. Material-agnostic, so it supports every biome's wood/stone/farmland floors, not just oak.
     */
    public static void supportFloatingFloors(ServerLevel level, BlockPos origin, int reach) {
        final int deckY = origin.getY() - 1; // the structure floor sits one below the jigsaw origin
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                p.set(origin.getX() + dx, deckY, origin.getZ() + dz);
                if (!level.isLoaded(p) || level.getBlockState(p).isAir()
                        || !level.getBlockState(p.below()).isAir()) {
                    continue;
                }
                // Look for ground below to rest the foundation on: connect down to it, or — over pure void, where
                // nothing's found within the search — drop only a short stub instead of a long pillar into nothing.
                int gap = SUPPORT_STUB;
                for (int d = 2; d <= SUPPORT_SEARCH; d++) {
                    if (!level.getBlockState(new BlockPos(p.getX(), deckY - d, p.getZ())).isAir()) {
                        gap = d - 1;
                        break;
                    }
                }
                for (int d = 1; d <= gap; d++) {
                    level.setBlock(new BlockPos(p.getX(), deckY - d, p.getZ()), Blocks.DIRT.defaultBlockState(), FLAGS);
                }
            }
        }
    }

    /**
     * The wooden-trestle variant of {@link #supportFloatingFloors}, for a mineshaft that ran off the island edge: under
     * a sparse (every-other-column) grid of the over-void floor tiles, hang an oak-fence leg down to the ground (within
     * {@link #SUPPORT_SEARCH}) or — over pure void — a short {@link #TRESTLE_STUB} stub, like a vanilla mine support
     * trestle. Sparse so it reads as legs rather than a curtain; the {@code linkConnections} pass run afterwards joins
     * adjacent legs into a lattice. Selected over the dirt version by the jigsaw's {@code trestles} flag.
     */
    public static void supportTrestles(ServerLevel level, BlockPos origin, int reach) {
        final int deckY = origin.getY() - 1; // the structure floor sits one below the jigsaw origin
        final BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                final int wx = origin.getX() + dx;
                final int wz = origin.getZ() + dz;
                if ((wx & 1) != 0 || (wz & 1) != 0) {
                    continue; // a leg only on the even/even grid — a trestle, not a wall of fences
                }
                p.set(wx, deckY, wz);
                if (!level.isLoaded(p) || level.getBlockState(p).isAir() || !level.getBlockState(p.below()).isAir()) {
                    continue; // not a floor tile, or already grounded
                }
                int gap = TRESTLE_STUB;
                for (int d = 2; d <= SUPPORT_SEARCH; d++) {
                    if (!level.getBlockState(new BlockPos(wx, deckY - d, wz)).isAir()) {
                        gap = d - 1;
                        break;
                    }
                }
                for (int d = 1; d <= gap; d++) {
                    level.setBlock(new BlockPos(wx, deckY - d, wz), fence, FLAGS);
                }
            }
        }
    }

    /**
     * The bayou-stilt variant of {@link #supportFloatingFloors}: under a sparse (every-other-column) grid of the
     * over-water/void floor tiles of a stilted build, hang a wooden {@code post} leg down — through any WATER — to the
     * first solid block (the swamp bed), or a short {@link #STILT_STUB} stub over pure void. Unlike the dirt/trestle
     * passes, which stop at the first non-air block (the water surface), this descends through the fluid so a house
     * standing over the marsh rests on legs planted in the bed, not on the water. Sparse so it reads as legs, not a
     * wall; {@code linkConnections} joins adjacent legs afterwards. Run BEFORE {@link #resolveStilted} (while the
     * connective lanes are still empty-deck markers, so only the solid lot floors get legs). (BWGSWAMPVILLAGEPLAN #73.)
     */
    public static void supportStilts(ServerLevel level, BlockPos origin, int reach, BlockState post) {
        final int deckY = origin.getY() - 1; // the structure floor sits one below the jigsaw origin
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                final int wx = origin.getX() + dx;
                final int wz = origin.getZ() + dz;
                if ((wx & 1) != 0 || (wz & 1) != 0) {
                    continue; // a leg only on the even/even grid — stilts, not a wall of posts
                }
                p.set(wx, deckY, wz);
                if (!level.isLoaded(p) || level.getBlockState(p).isAir()) {
                    continue; // not a floor tile
                }
                final BlockState below = level.getBlockState(new BlockPos(wx, deckY - 1, wz));
                if (!below.isAir() && below.getFluidState().isEmpty()) {
                    continue; // already resting on solid ground (a dry stretch) — no leg needed
                }
                stiltDown(level, p.immutable(), post);
            }
        }
    }

    /** Drop a wooden leg from just under {@code floor} down through air/water to the first solid block (the bed) within
     *  {@link #SUPPORT_SEARCH}. Over PURE void (no bed found in range) it places NOTHING — a stilt only ever rests on the
     *  island it belongs to; it never dangles a stub into the void or reaches down to a different island below (the
     *  boardwalk/floor simply floats there instead). Shared by the over-water rail edges and {@link #supportStilts}. */
    private static void stiltDown(ServerLevel level, BlockPos floor, BlockState post) {
        int gap = 0; // pure void ⇒ no leg at all
        for (int d = 1; d <= SUPPORT_SEARCH; d++) {
            final BlockState s = level.getBlockState(new BlockPos(floor.getX(), floor.getY() - d, floor.getZ()));
            if (!s.isAir() && s.getFluidState().isEmpty()) {
                gap = d - 1; // the first SOLID block below is the bed; the leg fills the water/air above it
                break;
            }
        }
        for (int d = 1; d <= gap; d++) {
            level.setBlock(new BlockPos(floor.getX(), floor.getY() - d, floor.getZ()), post, FLAGS);
        }
    }

    /** Strip a tree trunk/canopy left floating over a just-laid path/boardwalk tile. Trees are placed BEFORE structures,
     *  so a lane run across a tree's column overwrites only the ground block and leaves the trunk standing on the road;
     *  clear the tree blocks above it. Trees in the gaps (not under a lane) are untouched. */
    private static void clearCanopyAbove(ServerLevel level, BlockPos deck) {
        for (int dy = 1; dy <= CLEAR_ABOVE; dy++) {
            final BlockPos p = deck.above(dy);
            final BlockState s = level.getBlockState(p);
            if (s.is(BlockTags.LOGS) || s.is(BlockTags.LEAVES) || s.is(BlockTags.SAPLINGS)) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), FLAGS);
            }
        }
    }

    /**
     * Snow post-pass over the finished island — the single place a snow layer is laid (every snowy theme funnels
     * through here). Drops a snow layer on the highest block of each column in the {@code [min, max]} box with
     * probability {@code chance} (1.0 caps every column; a lower value leaves icy patches, the {@code random} deciding
     * per column) — so it lands on ground, building roofs and tree tops alike, the way a player expects "snow on the
     * highest block" to behave. Scans each column from {@code max.y} down to {@code min.y} for the first non-air block;
     * the caller bounds {@code min.y} to a band near the top so open-void columns stay cheap. Only full blocks, stairs,
     * slabs and leaves receive it (skips fences, lanterns, crops, fluids, lamp posts); placed no-physics so it stays put.
     */
    public static void snowCover(ServerLevel level, BlockPos min, BlockPos max, float chance, RandomSource random) {
        final BlockState snow = Blocks.SNOW.defaultBlockState();
        final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                if (!level.isLoaded(new BlockPos(x, max.getY(), z))) {
                    continue;
                }
                for (int y = max.getY(); y >= min.getY(); y--) {
                    p.set(x, y, z);
                    final BlockState s = level.getBlockState(p);
                    if (s.isAir()) {
                        continue;
                    }
                    final boolean receives = s.isCollisionShapeFullBlock(level, p)
                            || s.is(BlockTags.STAIRS) || s.is(BlockTags.SLABS) || s.is(BlockTags.LEAVES);
                    if (receives && level.getBlockState(p.above()).isAir()
                            && (chance >= 1.0f || random.nextFloat() < chance)) {
                        level.setBlock(p.above(), snow, FLAGS);
                    }
                    break; // only the highest block of the column
                }
            }
        }
    }
}
