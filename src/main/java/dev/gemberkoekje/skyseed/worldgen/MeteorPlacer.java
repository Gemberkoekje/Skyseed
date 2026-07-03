package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.block.MeteoriteCoreBlock;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stamps an impact meteor onto an island's top-centre (METEORPLAN, AE2PLAN #18e): a paraboloid <b>crater</b> carved
 * into the surface and skinned with obsidian + scorched glass / magma / basalt, a sky-stone <b>globe</b> seated in it
 * (top exposed), and a <b>Meteorite Core</b> buried at the globe's centre (the press source). Mutates the shared
 * block map + surface list in place, like {@link CaveCarver}, and is run before decoration so nothing grows in the
 * crater. <b>Inert without AE2</b> — the globe + core skip ({@link Lookup#hasBlock}), leaving a plain vanilla impact
 * crater on an otherwise-normal overworld island. The globe/crater scale from {@code globeRadius}; {@code coreTier}
 * (0/1/2) seats a {@link MeteoriteCoreBlock} at the globe's centre with that press tier (dedicated meteorite island),
 * or {@code -1} omits it (a small "wild" meteor — sky stone only).
 */
final class MeteorPlacer {
    private MeteorPlacer() {}

    private static final Id SKY_STONE = Id.of("ae2:sky_stone_block");

    private static long key(int dx, int dz) {
        return (((long) dx) << 32) | (dz & 0xffffffffL);
    }

    static void place(Map<BlockPos, BlockState> blockMap, List<BlockPos> surfaceList,
                      BlockPos center, int baseRadius, int globeRadius, int coreTier, RandomSource random) {
        // Per-column surface Y (the island top), so the bowl sinks from the real surface at each column.
        final Map<Long, Integer> topY = new HashMap<>();
        for (BlockPos p : surfaceList) topY.merge(key(p.getX() - center.getX(), p.getZ() - center.getZ()), p.getY(), Math::max);
        final Integer surfaceY = topY.get(key(0, 0));
        if (surfaceY == null) return; // no surface at the centre — nothing to crater

        final int globeR = Math.max(1, Math.min(globeRadius, baseRadius - 1));
        final int craterR = (int) Math.round(globeR * 2.0);
        final int craterDepth = globeR;
        final int globeCY = surfaceY - (int) Math.floor(globeR * 0.4); // seated so the globe's top pokes out of the bowl
        final BlockPos globeCenter = new BlockPos(center.getX(), globeCY, center.getZ());
        final int gr2 = globeR * globeR;

        final Set<Long> breached = new HashSet<>();

        // 1. Carve a paraboloid bowl (deepest at centre) and skin it with scorched rock — everywhere the globe won't fill.
        for (int dx = -craterR; dx <= craterR; dx++) {
            for (int dz = -craterR; dz <= craterR; dz++) {
                final double d = Math.sqrt(dx * dx + dz * dz);
                if (d > craterR) continue;
                final Integer colTop = topY.get(key(dx, dz));
                if (colTop == null) continue;
                final double t = d / craterR;
                final int bowlDepth = (int) Math.round((1.0 - t * t) * craterDepth);
                if (bowlDepth <= 0) continue; // the outer rim stays natural surface
                final int floorY = colTop - bowlDepth;
                for (int y = colTop; y > floorY; y--) {
                    final BlockPos p = new BlockPos(center.getX() + dx, y, center.getZ() + dz);
                    if (!insideGlobe(p, globeCenter, gr2)) blockMap.remove(p);
                }
                final BlockPos floor = new BlockPos(center.getX() + dx, floorY, center.getZ() + dz);
                if (!insideGlobe(floor, globeCenter, gr2) && blockMap.containsKey(floor)) {
                    blockMap.put(floor, scorched(random));
                }
                breached.add(key(dx, dz)); // dressed as crater — suppress decoration on these columns
            }
        }

        // 2. The sky-stone globe + the Meteorite Core at its centre (inert-safe: the globe skips without AE2, so the
        //    core — placed only here — never appears without AE2 either; a wild meteor passes coreTier -1: no core).
        if (Lookup.hasBlock(SKY_STONE)) {
            final BlockState skyStone = Lookup.blockState(SKY_STONE);
            for (int dx = -globeR; dx <= globeR; dx++) {
                for (int dy = -globeR; dy <= globeR; dy++) {
                    for (int dz = -globeR; dz <= globeR; dz++) {
                        if (dx * dx + dy * dy + dz * dz <= gr2) {
                            blockMap.put(globeCenter.offset(dx, dy, dz), skyStone);
                        }
                    }
                }
            }
            if (coreTier >= 0) {
                blockMap.put(globeCenter, ModBlocks.METEORITE_CORE.get().defaultBlockState()
                        .setValue(MeteoriteCoreBlock.TIER, coreTier));
            }
        }

        // 3. Crater columns are scorched rock, not soil — drop them so trees/grass don't decorate the impact.
        surfaceList.removeIf(p -> breached.contains(key(p.getX() - center.getX(), p.getZ() - center.getZ())));
    }

    private static boolean insideGlobe(BlockPos p, BlockPos c, int r2) {
        final int dx = p.getX() - c.getX(), dy = p.getY() - c.getY(), dz = p.getZ() - c.getZ();
        return dx * dx + dy * dy + dz * dz <= r2;
    }

    /** A weighted scorched-melt block for the crater skin: mostly obsidian, some glass / magma / basalt. */
    private static BlockState scorched(RandomSource random) {
        final float r = random.nextFloat();
        if (r < 0.62f) return Blocks.OBSIDIAN.defaultBlockState();
        if (r < 0.77f) return Blocks.BLACK_STAINED_GLASS.defaultBlockState();
        if (r < 0.87f) return Blocks.GLASS.defaultBlockState();
        if (r < 0.94f) return Blocks.MAGMA_BLOCK.defaultBlockState();
        if (r < 0.99f) return Blocks.BASALT.defaultBlockState();
        return Blocks.CRYING_OBSIDIAN.defaultBlockState();
    }
}
