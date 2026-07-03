package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.theme.OreDepth;
import dev.gemberkoekje.skyseed.worldgen.theme.OreEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Plants ore veins through an island's core columns (README → ore algorithm): each {@link OreEntry} rolls a number
 * of veins, each grown by a short random walk from a depth-appropriate seed block, replacing core blocks in place.
 */
final class OrePlanner {
    private OrePlanner() {}

    /** Fraction of the core Y-range (from the bottom) that counts as "deep core" for {@code deep_core} ores. */
    private static final double DEEP_CORE_FRACTION = 0.4;
    /** Chance a vein step grows to a face neighbour (compact veins) vs. a rarer diagonal. */
    private static final float FACE_GROW_CHANCE = 0.80f;
    /** Attempts to find a valid (depth-appropriate, not-yet-ore) seed block before giving up on a vein. */
    private static final int SEED_TRIES = 16;

    // --- Ore scales with island size (so every island — any tier, any Y-band, any theme — carries volume-appropriate
    // ore). A theme's {@code count} describes a NORMAL-tier island (~REF_CORE core blocks); a bigger island gets
    // proportionally MORE veins of the authored size. Sub-linear (EXP<1) so the biggest islands sit a touch leaner than
    // a normal one (~0.7×); never scales BELOW the authored baseline. The EXTRA veins are placed from a SEPARATE,
    // deterministic RNG so scaling never shifts the main generation stream — a normal-size island stays byte-identical,
    // and every island's downstream rolls (lava / pond / decoration / mobs) are unaffected by how much ore it gets.
    private static final double REF_CORE = 550.0;   // ~a normal-tier island's core volume — the baseline the JSON counts describe
    private static final double SCALE_EXP = 0.92;   // <1 → huge ≈ 0.7×, large ≈ 0.85× a normal island's ore density

    static void planOres(Map<BlockPos, BlockState> blockMap, List<OreEntry> ores, List<BlockPos> coreList,
                         int minCoreY, int maxCoreY, RandomSource random) {
        final Set<Long> coreSet = new HashSet<>(coreList.size() * 2);
        for (BlockPos p : coreList) {
            coreSet.add(p.asLong());
        }
        final int deepMaxY = minCoreY + (int) Math.round((maxCoreY - minCoreY) * DEEP_CORE_FRACTION); // lower 40% = deep_core
        final double volScale = Math.pow(Math.max(1.0, coreList.size() / REF_CORE), SCALE_EXP);
        // A side RNG for the size-scaled extra veins — deterministic per island (its core span), never touches `random`.
        final RandomSource extra = RandomSource.create(coreList.size() * 2654435761L + minCoreY * 31L + maxCoreY);

        for (OreEntry ore : ores) {
            if (!Lookup.hasBlock(ore.block())) {
                Skyseed.LOGGER.warn("[skyseed] theme ore references unknown block '{}' — skipping", ore.block());
                continue;
            }
            if (random.nextFloat() >= ore.chance()) {
                continue;
            }
            final BlockState state = Lookup.blockState(ore.block());
            // The baseline veins on the MAIN stream (byte-identical to a no-scaling island)…
            final int baseVeins = ore.count().sample(random);
            for (int v = 0; v < baseVeins; v++) {
                final BlockPos seed = pickSeed(coreList, coreSet, ore.depth(), deepMaxY, random);
                if (seed != null) {
                    growVein(blockMap, seed, state, ore.veinSize().sample(random), coreSet, random);
                }
            }
            // …then the size-appropriate EXTRA veins on the side RNG (adds ore only; doesn't move anything else).
            final int extraVeins = (int) Math.round(baseVeins * volScale) - baseVeins;
            for (int v = 0; v < extraVeins; v++) {
                final BlockPos seed = pickSeed(coreList, coreSet, ore.depth(), deepMaxY, extra);
                if (seed != null) {
                    growVein(blockMap, seed, state, ore.veinSize().sample(extra), coreSet, extra);
                }
            }
        }
    }

    private static BlockPos pickSeed(List<BlockPos> coreList, Set<Long> coreSet, OreDepth depth,
                                     int deepMaxY, RandomSource random) {
        for (int tries = 0; tries < SEED_TRIES; tries++) {
            final BlockPos c = coreList.get(random.nextInt(coreList.size()));
            if (!coreSet.contains(c.asLong())) {
                continue;
            }
            if (depth == OreDepth.DEEP_CORE && c.getY() > deepMaxY) {
                continue;
            }
            return c;
        }
        return null;
    }

    private static void growVein(Map<BlockPos, BlockState> blockMap, BlockPos seed, BlockState ore, int size,
                                 Set<Long> coreSet, RandomSource random) {
        final List<BlockPos> placed = new ArrayList<>();
        blockMap.put(seed, ore);
        coreSet.remove(seed.asLong());
        placed.add(seed);

        int attempts = 0;
        while (placed.size() < size && attempts < size * 10) {
            attempts++;
            final BlockPos from = placed.get(random.nextInt(placed.size()));
            // Favour growing to a face neighbour (compact, vanilla-looking veins); allow diagonals, but rarely.
            final BlockPos nb;
            if (random.nextFloat() < FACE_GROW_CHANCE) {
                nb = from.relative(Direction.values()[random.nextInt(Direction.values().length)]);
            } else {
                int dx, dy, dz; // a real diagonal step: at least two axes off
                do {
                    dx = random.nextInt(3) - 1;
                    dy = random.nextInt(3) - 1;
                    dz = random.nextInt(3) - 1;
                } while (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) < 2);
                nb = from.offset(dx, dy, dz);
            }
            final long key = nb.asLong();
            if (coreSet.contains(key)) {
                blockMap.put(nb, ore);
                coreSet.remove(key);
                placed.add(nb);
            }
        }
    }
}
