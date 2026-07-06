package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.theme.IslandTheme;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Grows a cross-dimension twin island at the vanilla 8:1 portal-linked coordinate, so a Ruined Portal seed pairs an
 * overworld frame with a Nether one that links with no extra code (SKYNETHERPLAN → Ruined Portal twins). Pulled out of
 * {@link dev.gemberkoekje.skyseed.entity.IslandSeedEntity} so the thrown seed keeps a single responsibility.
 */
public final class TwinPlacer {
    private TwinPlacer() {}

    /**
     * Grow a twin island at the 8:1 dimension-linked coordinate in the paired dimension (overworld &harr; nether), so a
     * repaired-and-lit frame on both sides connects with no linking code. The twin germinates the same theme — the
     * dimension itself decides the form (goodies in the overworld, a bare frame in the Nether). Placement stays close to
     * the linked spot (small steps only) so the portals still link.
     */
    public static void spawnTwin(ServerLevel origin, BlockPos center, IslandTheme theme, Id themeId) {
        final ResourceKey<Level> to;
        if (origin.dimension() == Level.OVERWORLD) {
            to = Level.NETHER;
        } else if (origin.dimension() == Level.NETHER) {
            to = Level.OVERWORLD;
        } else {
            return; // twins only pair the overworld and the Nether
        }
        final ServerLevel other = origin.getServer().getLevel(to);
        if (other == null) {
            return;
        }
        final BlockPos linked = linkedPortalPos(center, to, other);
        if (!IslandGenerator.formValidFor(theme, other.getBiome(linked), linked.getY(), Lookup.dimensionId(other.dimension()))) {
            return; // the theme doesn't implement the other dimension — no twin
        }
        final TwinResult twin = placeTwinNear(other, theme, linked);
        // SIGNOFFPLAN B4 diagnostic — twin-alignment probe. origin = the source island's centre; linked = the exact 8:1
        // target; grewAt = where the twin actually planted. If grewAt != linked, placeTwinNear nudged it (the frames then
        // sit off the link); if grewAt == linked but the in-game frames still don't line up 8:1, the opening isn't seating
        // on the centre anchor. Remove once B4 is fixed.
        Skyseed.LOGGER.info("[skyseed] twin B4: origin={} linked={} grewAt={} nudge=({},{},{})",
                center, linked, twin.center(),
                twin.center().getX() - linked.getX(), twin.center().getY() - linked.getY(),
                twin.center().getZ() - linked.getZ());
        // Crash-resume the twin as its own pending island (5.2) — the highest-value case: a twin grown in the
        // player-less Nether has no other chunk ticket, so a crash mid-grow was the likeliest place to lose content.
        final PendingIsland descriptor = themeId == null ? null : PendingIsland.fresh(
                Lookup.dimensionId(other.dimension()), themeId.value(), twin.center(), "", -1, false);
        IslandGrowth.enqueue(new GenerationJob(other, twin.plan(), descriptor));
    }

    /** The vanilla 8:1 cross-dimension coordinate (overworld/8 &harr; nether*8), Y kept and clamped to {@code to}. */
    public static BlockPos linkedPortalPos(BlockPos c, ResourceKey<Level> to, ServerLevel toLevel) {
        final int x;
        final int z;
        if (to == Level.NETHER) {
            x = Math.floorDiv(c.getX(), 8);
            z = Math.floorDiv(c.getZ(), 8);
        } else {
            x = c.getX() * 8;
            z = c.getZ() * 8;
        }
        int y = c.getY();
        if (to == Level.NETHER) {
            y = Mth.clamp(y, 16, 110); // above the lava sea, below the ceiling
        } else {
            //? if >=26.1.2 {
            /*y = Mth.clamp(y, toLevel.getMinY() + 8, toLevel.getMaxY() - 16);*/
            //?} else {
            y = Mth.clamp(y, toLevel.getMinBuildHeight() + 8, toLevel.getMaxBuildHeight() - 16);
            //?}
        }
        return new BlockPos(x, y, z);
    }

    /** Plan the twin on {@code linked}'s exact XZ column (never nudged sideways — see {@link #twinSearchSpots}), so the
     *  portal opening lands on the 8:1 coordinate and the pair links; only a small vertical lift dodges an obstruction. */
    private static TwinResult placeTwinNear(ServerLevel level, IslandTheme theme, BlockPos linked) {
        final List<Vec3> players = level.players().stream().map(p -> p.position()).toList();
        final BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();
        final IslandPlacement.Occupancy occupied = (x, y, z) -> {
            final BlockState s = level.getBlockState(probe.set(x, y, z));
            return !s.isAir() && !s.canBeReplaced();
        };
        for (BlockPos c : twinSearchSpots(linked)) {
            final IslandPlan candidate = planTwinAt(level, theme, c);
            if (IslandPlacement.check(candidate, players, occupied).ok()) {
                return new TwinResult(candidate, c);
            }
        }
        // No clear spot close by — grow it at the linked coordinate anyway; sitting on the link is the whole point.
        return new TwinResult(planTwinAt(level, theme, linked), linked);
    }

    /** A planned twin and the centre it grows at (kept so a crash can re-plan the identical twin — 5.2). */
    private record TwinResult(IslandPlan plan, BlockPos center) {}

    /** Candidate spots for the twin — all on the SAME XZ column as {@code linked}. The portal opening MUST land at the
     *  exact 8:1 coordinate for the pair to link, so we NEVER nudge horizontally: a horizontal step desyncs the two
     *  frames (the B4 bug — an occupied link spot nudged the twin +6 X, so traversal emerged 6 blocks off the frame).
     *  Only small vertical lifts are tried, to dodge a vertical obstruction (vanilla's portal search spans the Y column,
     *  so a small Y shift still links); if none is clear the caller grows right on the link anyway. */
    private static List<BlockPos> twinSearchSpots(BlockPos linked) {
        final List<BlockPos> spots = new ArrayList<>();
        spots.add(linked);
        for (int lift : new int[] { 4, -4, 8, -8, 12, -12 }) {
            spots.add(linked.above(lift));
        }
        return spots;
    }

    /** Plan the twin at {@code c} with the linked dimension's own biome, RNG keyed by the centre (deterministic). */
    private static IslandPlan planTwinAt(ServerLevel level, IslandTheme theme, BlockPos c) {
        final RandomSource random = RandomSource.create(level.getSeed() ^ c.asLong());
        return IslandGenerator.planIsland(level, c, theme, level.getBiome(c), random);
    }
}
