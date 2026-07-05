package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Band 2</b> Mystical Agriculture derelict of {@code VARIETYSTRUCTUREPLAN.md} §3 (B23) — a single-mod "rare"
 * surprise building that appears on an ordinary island only when <b>Mystical Agriculture</b> is installed: the
 * <b>Abandoned Inferium Plot</b>, an overgrown essence farm gone to seed. Gated {@code requires: ["mysticalagriculture"]}
 * so it is filtered before any RNG and its modded {@code .nbt} never parses without the mod.
 *
 * <p>Unlike the Create/IE/AE2 batches this is a mostly-vanilla farmstead — the only modded blocks are the essence
 * farmland, the growing essence crops, and one toppled growth accelerator, authored through the {@code modNames} side-map
 * (a vanilla analog carries the property serialisation; the emitted {@code .nbt} palette {@code Name} is swapped to the
 * real {@code mysticalagriculture:} id — see {@link StructureWriter}). The farmland/crops/accelerator therefore resolve
 * to <b>air</b> without the mod, so — like the AE2 lab — the assertable gametest anchor is the deliberate <b>vanilla
 * shell</b>: the leaning scarecrow (a carved pumpkin on a fence post), the caved tool lean-to, the water-cauldron trough,
 * the broken oak-fence perimeter, hay-bale straw, and the scrap chest.
 *
 * <p><b>D4 gate-safety (§5).</b> Mystical Agriculture's tier climb (inferium → prudentium → tertium → imperium →
 * supremium) is the whole progression. So this plot uses only <b>tier-1 inferium</b> — the cheapest, renewable,
 * mob-droppable base tier — and shows it <em>derelict</em>: trampled beds reverted to coarse dirt, bare rows, a
 * <em>broken</em> inferium growth accelerator (not a working one), and only a handful of crops. It places <b>NO</b>
 * prosperity block/ore, higher-tier farmland, infusion altar/pedestal, seed reprocessor or essence vessel (the
 * gate-keys), and the loot carries <b>NO</b> seeds or tier-4/5 essence — just a little tier-1 {@code inferium_essence}
 * and {@code prosperity_shard} (the two mineable base materials) via inert {@code add_drop} GLMs.
 *
 * <p><b>Verified Mystical Agriculture ids (mysticalagriculture 8.0.27).</b> {@code inferium_farmland} (property
 * {@code moisture} 0-7) → a {@code FARMLAND} analog; {@code inferium_crop} (property {@code age} 0-7) → a {@code WHEAT}
 * analog; {@code inferium_growth_accelerator} (propertyless) → a {@code STONE} analog.
 */
public final class MysticalRuinsTemplates {
    private MysticalRuinsTemplates() {}

    private static final String MA = "mysticalagriculture:";
    private static final String SCRAP = "skyseed:chests/inferium_plot";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("inferium_plot/plot.nbt"), abandonedPlot());
    }

    /**
     * The <b>Abandoned Inferium Plot</b> (B23) — an essence farm gone to seed, <em>not a box</em>: an irregular tilled
     * bed of inferium farmland (rows trampled back to coarse dirt, several harvested bare) around a water-cauldron
     * trough, a scatter of tier-1 essence crops at mixed growth, a lop-sided scarecrow (one arm snapped off), a caved
     * tool lean-to sheltering the scrap chest, a toppled inferium growth accelerator, spilled hay straw and a broken
     * oak-fence perimeter with a swung-open gate. Fits Meadow / Hamlet; the theme's {@code mobs} pack (a zombie) haunts it.
     */
    private static Built abandonedPlot() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 6; // 7×7 footprint

        // -- Ground surface (grass), then the tilled bed carved into it. --------------------------------------------
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        // The irregular inferium-farmland bed (a blob, not a rectangle). A few cells are decayed back to coarse dirt.
        final int[][] farmland = {
                {2, 1}, {3, 1}, {4, 1},
                {1, 2}, {2, 2}, {4, 2}, {5, 2},
                {1, 3}, {2, 3}, {4, 3}, {5, 3},
                {2, 4}, {3, 4}, {4, 4}, {5, 4},
                {3, 5}, {4, 5},
        };
        for (final int[] c : farmland) {
            set(m, mods, new BlockPos(c[0], 0, c[1]), Blocks.FARMLAND.defaultBlockState(), MA + "inferium_farmland");
        }
        // Trampled / dried-out patches (reverted to coarse dirt) — clear any farmland id underneath with set(...,null).
        for (final int[] c : new int[][]{{4, 2}, {2, 4}, {5, 3}, {3, 3}}) {
            set(m, mods, new BlockPos(c[0], 0, c[1]), Blocks.COARSE_DIRT.defaultBlockState(), null);
        }
        // A trodden dirt path from the gate up to the trough.
        m.put(new BlockPos(3, 0, 6), Blocks.DIRT_PATH.defaultBlockState());
        m.put(new BlockPos(3, 0, 2), Blocks.DIRT_PATH.defaultBlockState());

        // -- The water trough (a filled cauldron on the coarse-dirt mound) — the farm's vanilla water source. --------
        m.put(new BlockPos(3, 1, 3), Blocks.WATER_CAULDRON.defaultBlockState()
                .setValue(BlockStateProperties.LEVEL_CAULDRON, 3));

        // -- Essence crops (tier-1 inferium) at mixed growth; several beds left bare (harvested/trampled). ----------
        crop(m, mods, 2, 1, 7); crop(m, mods, 1, 2, 7); crop(m, mods, 4, 3, 7); crop(m, mods, 2, 3, 7); crop(m, mods, 4, 4, 7);
        crop(m, mods, 5, 2, 3); crop(m, mods, 1, 3, 1); crop(m, mods, 4, 5, 5);
        // Dead bushes on the dried patches (decay).
        m.put(new BlockPos(4, 1, 2), Blocks.DEAD_BUSH.defaultBlockState());
        m.put(new BlockPos(5, 1, 3), Blocks.DEAD_BUSH.defaultBlockState());
        // Grass tufts creeping back over the abandoned rim.
        for (final int[] c : new int[][]{{0, 0}, {6, 0}, {6, 6}, {1, 0}, {0, 3}, {2, 0}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.SHORT_GRASS.defaultBlockState());
        }

        // -- The lop-sided scarecrow standing in the field (vanilla; the assertable centrepiece). One arm has snapped
        //    off, a carved-pumpkin head, straw slumped at its foot. Stands on the grass rim at (5, z1). ---------------
        m.put(new BlockPos(5, 1, 1), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(5, 2, 1), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(4, 2, 1), Blocks.OAK_FENCE.defaultBlockState());   // the one surviving arm (the other snapped off)
        m.put(new BlockPos(5, 3, 1), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        m.put(new BlockPos(4, 1, 1), Blocks.HAY_BLOCK.defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.X));       // straw slumped at the foot

        // -- The caved tool lean-to at the SW corner: a leaning oak-log post, a broken slab roof (a hole where it
        //    caved), the scrap chest sheltered beneath (air above it so it opens), a composter and a toppled inferium
        //    growth accelerator (the "salvaged part"). ----------------------------------------------------------------
        m.put(new BlockPos(0, 1, 5), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
        m.put(new BlockPos(0, 2, 5), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
        // A mono-pitch plank roof leaning off the post, half caved in (the (1,3,6) slope is missing).
        m.put(new BlockPos(0, 3, 5), Blocks.OAK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM));
        m.put(new BlockPos(0, 3, 6), Blocks.OAK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM));
        m.put(new BlockPos(1, 2, 6), Blocks.OAK_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE,
                net.minecraft.world.level.block.state.properties.SlabType.BOTTOM));   // the surviving lower eave board
        m.put(new BlockPos(0, 2, 6), Blocks.COBWEB.defaultBlockState());              // decay in the rafters
        // The scrap chest, sheltered under the lean-to. (1,2,5) is air so it opens ([[skyseed-structure-chest-openable]]).
        m.put(new BlockPos(1, 1, 5), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(1, 1, 5), StructureParts.lootChest(SCRAP));
        m.put(new BlockPos(0, 1, 4), Blocks.COMPOSTER.defaultBlockState()
                .setValue(BlockStateProperties.LEVEL_COMPOSTER, 4));                  // a part-full composter
        m.put(new BlockPos(1, 1, 6), Blocks.HAY_BLOCK.defaultBlockState());          // a stored straw bale
        // The toppled inferium growth accelerator (tier-1, broken) lying in the open by the nook.
        set(m, mods, new BlockPos(0, 1, 6), Blocks.STONE.defaultBlockState(), MA + "inferium_growth_accelerator");

        // -- Broken oak-fence perimeter, with whole runs missing and a swung-open gate at the front (south). --------
        for (final int[] c : new int[][]{{1, 6}, {2, 6}, {4, 6},                       // south run (gate at x=3)
                {6, 5}, {6, 4}, {6, 2}, {6, 1},                                         // east run (a gap at z=3)
                {5, 0}, {6, 0},                                                         // north stub
                {0, 4}}) {                                                              // west stub
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.OAK_FENCE.defaultBlockState());
        }
        m.put(new BlockPos(3, 1, 6), Blocks.OAK_FENCE_GATE.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)   // NORTH connects to the E/W fence run
                .setValue(BlockStateProperties.OPEN, true));

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 4), "minecraft:grass_block");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers (mirror of AeRuinsTemplates).
    // ------------------------------------------------------------------------------------------------------------

    /** An inferium essence crop at {@code age} (0-7) — a WHEAT analog whose palette Name is swapped to the MA crop. */
    private static void crop(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, int age) {
        set(m, mods, new BlockPos(x, 1, z),
                Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, age), MA + "inferium_crop");
    }

    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p);
        }
    }

    /** Finish a build: a jigsaw cell must never carry a mod palette name, so strip mod ids from every jigsaw cell. */
    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
