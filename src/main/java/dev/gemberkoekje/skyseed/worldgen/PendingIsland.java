package dev.gemberkoekje.skyseed.worldgen;

import net.minecraft.core.BlockPos;

/**
 * A resumable descriptor for an in-progress island grow (engineering-debt 5.2 — see {@code PLANOFPLANS.md}). It
 * carries the deterministic re-plan <em>inputs</em> — dimension, the RESOLVED theme id, the chosen centre (which keys
 * the plan RNG as {@code worldSeed ^ centre}), and the debug forced-biome + {@link DebugForce} flags — plus the live
 * <em>progress</em> indices. So after a hard crash mid-grow, {@code CrashRecovery} can re-plan the identical island and
 * resume it exactly where the last world-save left off, instead of losing the (already-consumed) seed's island.
 *
 * <p>Kept a plain record: the version-specific persistence lives in {@link SkyseedWorldData} (a 1.21.1 {@code CompoundTag}
 * path and a 26.1.2 {@code Codec}), because the {@code CompoundTag} get/put API differs across the two nodes.
 */
public record PendingIsland(String dimId, String themeId, int centerX, int centerY, int centerZ,
                            String forcedBiome, int forcedRare, boolean forcedWaterfall,
                            int blockIndex, int treeIndex, int treesPlaced, int scatterIndex, int finalizeStep) {

    /** A fresh descriptor at progress 0. {@code forcedBiome} is {@code ""} for the normal planting biome. */
    public static PendingIsland fresh(String dimId, String themeId, BlockPos center, String forcedBiome,
                                      int forcedRare, boolean forcedWaterfall) {
        return new PendingIsland(dimId, themeId, center.getX(), center.getY(), center.getZ(),
                forcedBiome, forcedRare, forcedWaterfall, 0, 0, 0, 0, 0);
    }

    /** A copy with updated progress indices, every input field preserved — written each tick as the grow advances. */
    public PendingIsland withProgress(int blockIndex, int treeIndex, int treesPlaced, int scatterIndex, int finalizeStep) {
        return new PendingIsland(dimId, themeId, centerX, centerY, centerZ, forcedBiome, forcedRare, forcedWaterfall,
                blockIndex, treeIndex, treesPlaced, scatterIndex, finalizeStep);
    }

    public BlockPos center() {
        return new BlockPos(centerX, centerY, centerZ);
    }

    /** Stable identity — one growing island per (dimension, centre) — used to replace/remove the persisted entry. */
    public String key() {
        return dimId + ";" + centerX + ";" + centerY + ";" + centerZ;
    }
}
