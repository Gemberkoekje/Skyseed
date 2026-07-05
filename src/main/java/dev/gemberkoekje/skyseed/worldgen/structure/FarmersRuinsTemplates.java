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
import net.minecraft.world.level.block.state.properties.SlabType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Band 2</b> Farmer's Delight derelict of {@code VARIETYSTRUCTUREPLAN.md} §3 (B24), and the <b>last</b> single-mod
 * Band 2 build — a "rare" surprise building that appears on an ordinary island only when <b>Farmer's Delight</b> is
 * installed: the <b>Overgrown Cook's Homestead</b>, an abandoned kitchen cottage with a garden gone to weeds. Gated
 * {@code requires: ["farmersdelight"]} so it is filtered before any RNG and its modded {@code .nbt} never parses without
 * the mod.
 *
 * <p>Like the Mystical Agriculture plot ({@link MysticalRuinsTemplates}), the shell is vanilla and only the kitchen
 * fittings are modded — a cold stove with its cooking pot, a skillet, a cutting board, wall cabinets, produce crates and
 * the garden crops (cabbages / tomatoes / onions / rice), authored through the {@code modNames} side-map: a vanilla
 * {@code SMOKER} analog carries the {@code facing} for every appliance/cabinet (Farmer's Delight ignores the extra
 * {@code lit}, defaults its own {@code support}/{@code open}); a {@code STONE} analog for the propertyless crates; a
 * {@code WHEAT} analog for the {@code age}-0..7 crops. Those resolve to <b>air</b> without the mod, so — like the AE2 lab —
 * the assertable gametest anchor is the deliberate <b>vanilla shell</b>: the oak-log posts, the cobblestone hearth-chimney
 * jutting broken above the caved roof, the cold campfire hearth, the crafting table and the scrap chest.
 *
 * <p><b>D4 gate-safety (§5).</b> Farmer's Delight is a cooking/quality-of-life mod with <em>no progression gate</em>, so
 * there is nothing to leak — the loot is pure flavour: raw produce, seeds, canvas and rope (a "rotting harvest"), layered
 * via inert {@code add_drop} GLMs, never a rich cooked meal (those need the mod's own cookware anyway).
 *
 * <p><b>Verified Farmer's Delight ids (farmersdelight 1.3.2).</b> {@code stove} (facing+lit), {@code cooking_pot}
 * (facing+support), {@code skillet} (facing+support), {@code cutting_board} (facing), {@code oak_cabinet} (facing+open) →
 * a {@code SMOKER} facing analog; {@code cabbage_crate}/{@code tomato_crate}/{@code onion_crate} (propertyless) → a
 * {@code STONE} analog; {@code cabbages}/{@code onions} (age 0-7) and {@code tomatoes}/{@code rice} (age 0-3) → a
 * {@code WHEAT} analog (ages kept ≤3 for tomatoes/rice so the swapped state parses).
 */
public final class FarmersRuinsTemplates {
    private FarmersRuinsTemplates() {}

    private static final String FD = "farmersdelight:";
    private static final String SCRAP = "skyseed:chests/cook_homestead";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("cook_homestead/homestead.nbt"), cooksHomestead());
    }

    /**
     * The <b>Overgrown Cook's Homestead</b> (B24) — a kitchen cottage abandoned to the weeds, <em>not a box</em>: a
     * 5×5 log-and-cobble cabin with a caved-in roof, a breached east wall where the cold hearth vents up an external
     * cobblestone chimney jutting broken above the ridge, a doorway and a shattered window; inside, the Farmer's Delight
     * kitchen (stove + cooking pot, skillet, cutting board, cabinets, the scrap chest); out front, a weed-choked garden
     * of FD crops at mixed growth with a rotting stack of produce crates and a water butt. No mobs — a peaceful, empty
     * homestead.
     */
    private static Built cooksHomestead() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        // -- Ground: grass across the whole plot; the cabin interior gets a plank floor. -----------------------------
        for (int x = 0; x <= 5; x++) {
            for (int z = 0; z <= 6; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState());
            }
        }

        // -- Cabin corner posts (oak log). --------------------------------------------------------------------------
        for (final int[] c : new int[][]{{0, 0}, {4, 0}, {0, 4}, {4, 4}}) {
            for (int y = 1; y <= 3; y++) {
                m.put(new BlockPos(c[0], y, c[1]), Blocks.OAK_LOG.defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            }
        }

        // -- Perimeter walls (cobble course + two plank courses), with a front doorway, a shattered west window and a
        //    caved east breach where the chimney vents. ------------------------------------------------------------
        for (final int[] c : new int[][]{
                {1, 0}, {2, 0}, {3, 0}, {1, 4}, {2, 4}, {3, 4},   // north / south (front) runs
                {0, 1}, {0, 2}, {0, 3}, {4, 1}, {4, 2}, {4, 3}}) {  // west / east runs
            final int x = c[0], z = c[1];
            if (x == 2 && z == 4) {                       // front doorway — open below the plank lintel
                m.put(new BlockPos(x, 3, z), Blocks.OAK_PLANKS.defaultBlockState());
                continue;
            }
            if (x == 0 && z == 2) {                       // shattered west window
                m.put(new BlockPos(x, 1, z), Blocks.COBBLESTONE.defaultBlockState());
                m.put(new BlockPos(x, 2, z), Blocks.GLASS_PANE.defaultBlockState());
                m.put(new BlockPos(x, 3, z), Blocks.OAK_PLANKS.defaultBlockState());
                continue;
            }
            if (x == 4 && z == 1) {                       // caved breach (the hearth vents here) — fully open
                continue;
            }
            if (x == 4 && z == 2) {                       // jagged breach edge — just the cobble footing survives
                m.put(new BlockPos(x, 1, z), Blocks.COBBLESTONE.defaultBlockState());
                continue;
            }
            m.put(new BlockPos(x, 1, z), Blocks.COBBLESTONE.defaultBlockState());
            m.put(new BlockPos(x, 2, z), Blocks.OAK_PLANKS.defaultBlockState());
            m.put(new BlockPos(x, 3, z), Blocks.OAK_PLANKS.defaultBlockState());
        }

        // -- The cold hearth in the breach + the external cobblestone chimney jutting broken above the roofline. -----
        m.put(new BlockPos(4, 1, 1), Blocks.CAMPFIRE.defaultBlockState()
                .setValue(BlockStateProperties.LIT, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        for (int y = 1; y <= 5; y++) {
            m.put(new BlockPos(5, y, 1), Blocks.COBBLESTONE.defaultBlockState());
        }
        m.put(new BlockPos(5, 6, 1), Blocks.COBBLESTONE_SLAB.defaultBlockState()
                .setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM));   // the broken chimney cap

        // -- Gable roof, then caved in (two holes) with a fallen rafter stair inside. --------------------------------
        StructureParts.gableRoof(m, 0, 4, 0, 4, 3, Blocks.OAK_PLANKS.defaultBlockState(),
                Blocks.OAK_STAIRS, Blocks.OAK_SLAB, 0);
        m.remove(new BlockPos(3, 4, 3));   // caved hole
        m.remove(new BlockPos(1, 4, 2));   // caved hole
        m.put(new BlockPos(3, 1, 2), Blocks.OAK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP));   // a fallen rafter

        // -- The Farmer's Delight kitchen along the back (north) wall — appliances face south into the room. ---------
        fd(m, mods, new BlockPos(1, 1, 1), "stove", Direction.SOUTH);
        fd(m, mods, new BlockPos(1, 2, 1), "cooking_pot", Direction.SOUTH);   // the pot sits on the stove
        fd(m, mods, new BlockPos(2, 1, 1), "skillet", Direction.SOUTH);
        fd(m, mods, new BlockPos(3, 1, 1), "cutting_board", Direction.SOUTH);
        fd(m, mods, new BlockPos(1, 1, 3), "oak_cabinet", Direction.NORTH);
        m.put(new BlockPos(1, 1, 2), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(2, 4, 2), Blocks.LANTERN.defaultBlockState()
                .setValue(BlockStateProperties.HANGING, true));               // hung directly from the ridge slab (2,5,2)
        m.put(new BlockPos(3, 2, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(1, 3, 2), Blocks.COBWEB.defaultBlockState());
        // The scrap chest on the interior floor — air above (3,2,3) so it opens ([[skyseed-structure-chest-openable]]).
        m.put(new BlockPos(3, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(3, 1, 3), StructureParts.lootChest(SCRAP));

        // -- The weed-choked garden out front (z5-6): FD crops on vanilla farmland, a couple trampled to coarse dirt. -
        for (final int[] c : new int[][]{{1, 5}, {2, 5}, {3, 5}, {1, 6}, {3, 6}}) {
            m.put(new BlockPos(c[0], 0, c[1]), Blocks.FARMLAND.defaultBlockState());
        }
        m.put(new BlockPos(2, 0, 6), Blocks.COARSE_DIRT.defaultBlockState());   // a trampled row
        crop(m, mods, 1, 5, "cabbages", 7);
        crop(m, mods, 2, 5, "tomatoes", 3);
        crop(m, mods, 3, 5, "onions", 7);
        crop(m, mods, 1, 6, "rice", 3);
        crop(m, mods, 3, 6, "cabbages", 5);
        // A rotting stack of produce crates by the garden edge, and a water butt.
        cube(m, mods, new BlockPos(0, 1, 5), "cabbage_crate");
        cube(m, mods, new BlockPos(0, 2, 5), "tomato_crate");
        cube(m, mods, new BlockPos(0, 1, 6), "onion_crate");
        m.put(new BlockPos(4, 1, 5), Blocks.WATER_CAULDRON.defaultBlockState()
                .setValue(BlockStateProperties.LEVEL_CAULDRON, 3));
        // Weeds creeping back in (kept clear of the cabin's corner posts).
        for (final int[] c : new int[][]{{4, 6}, {5, 4}, {5, 5}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.SHORT_GRASS.defaultBlockState());
        }
        m.put(new BlockPos(5, 1, 6), Blocks.BROWN_MUSHROOM.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:oak_planks");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers (mirror of AeRuinsTemplates / MysticalRuinsTemplates).
    // ------------------------------------------------------------------------------------------------------------

    /** A facing Farmer's Delight appliance/cabinet — a SMOKER analog (carries {@code facing}; FD ignores the extra
     *  {@code lit} and defaults its own {@code support}/{@code open}) whose palette Name is swapped to the FD block. */
    private static void fd(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, String id, Direction facing) {
        set(m, mods, p, Blocks.SMOKER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing), FD + id);
    }

    /** A propertyless FD full-cube (a produce crate) — a STONE analog whose palette Name is swapped to the FD block. */
    private static void cube(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, String id) {
        set(m, mods, p, Blocks.STONE.defaultBlockState(), FD + id);
    }

    /** An FD garden crop at {@code age} — a WHEAT analog whose palette Name is swapped to the FD crop. */
    private static void crop(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, String id, int age) {
        set(m, mods, new BlockPos(x, 1, z),
                Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, age), FD + id);
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
