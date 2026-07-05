package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Band 3</b> multi-mod <em>epic</em> set-pieces of {@code VARIETYSTRUCTUREPLAN.md} §3 (B26–B30) — the rarest
 * surprise buildings, each gated on <b>two</b> mods ({@code requires: [modA, modB]}) so they only germinate where the
 * whole combo is installed. Like the Band-2 batches, any modded machinery is authored with the {@code modNames} side-map
 * (a vanilla analog carries the property serialisation; the emitted {@code .nbt} palette {@code Name} is swapped to the
 * real mod id — see {@link StructureWriter} / {@link CreateRuinsTemplates}). Because a mod-heavy build resolves to AIR
 * without its mods, each carries a deliberate <b>vanilla shell</b> as the assertable gametest anchor.
 *
 * <p><b>D4 — the compounding gate (§5).</b> A two-mod build must be gate-safe against <em>both</em> mods' progressions:
 * every machine/forge/controller is an empty <b>husk</b> and the loot is scrap + flavour from each mod (never a working
 * setup, never a gate-key). Per build, the loot is audited against both deny-lists.
 */
public final class MultiModRuinsTemplates {
    private MultiModRuinsTemplates() {}

    private static final String CREATE = "create:";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("magitech_workshop/workshop.nbt"), magitechWorkshop());
    }

    /**
     * The <b>Magitech Workshop</b> (B26, <b>Create + Iron's Spells</b>) — an arcane-forge workshop where a mage once
     * married cogwork to spellcraft, long abandoned. <em>Not a box</em>: an andesite-and-stone workshop with a caved-in
     * roof (open to the sky, fallen rubble), a breached east wall, and a broken brick forge-flue jutting above the ridge.
     * Inside, a <b>Create</b> machine husk (an {@code andesite_casing} gearbox driving a cogwheel + shaft into a
     * {@code mechanical_press} over its bed) stands beside the <b>arcane</b> half — an enchanting table ringed with
     * amethyst and candles, a bookshelf study with a lectern, and a brewing nook. Iron's Spells is a mob/item mod, so the
     * arcane fittings are all vanilla (and the assertable gametest shell); the "Iron's" flavour is the two mages of the
     * theme {@code mobs} pack and the loot. Fits Rocky.
     *
     * <p><b>D4.</b> Create side: only {@code andesite_casing} / {@code cogwheel} / {@code shaft} and a {@code mechanical_press}
     * <em>husk</em> — no working contraption; loot tops out at {@code andesite_alloy} (never brass / precision mechanisms).
     * Iron's side: loot is {@code arcane_essence} + a {@code blank_rune} (an uninscribed base rune, inert without a gated
     * Upgrade Orb) — never a spellbook, a named scroll or an Upgrade Orb.
     */
    private static Built magitechWorkshop() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 6; // 7×7

        // -- Industrial floor: an andesite / polished-andesite / stone mix. -----------------------------------------
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorStone(x, z));
            }
        }

        // -- Andesite-casing corner posts (Create), three courses. -------------------------------------------------
        for (final int[] c : new int[][]{{0, 0}, {max, 0}, {0, max}, {max, max}}) {
            for (int y = 1; y <= 3; y++) {
                set(m, mods, new BlockPos(c[0], y, c[1]), cube(), CREATE + "andesite_casing");
            }
        }

        // -- Perimeter walls (weathered stone-brick / andesite), ruined: a front doorway, an east breach, broken tops. -
        for (final int[] c : new int[][]{
                {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {1, max}, {2, max}, {3, max}, {4, max}, {5, max},
                {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {max, 1}, {max, 2}, {max, 3}, {max, 4}, {max, 5}}) {
            final int x = c[0], z = c[1];
            if (x == 3 && z == max) {                       // front doorway
                m.put(new BlockPos(x, 3, z), Blocks.STONE_BRICKS.defaultBlockState()); // lintel; y1/y2 open
                continue;
            }
            if (x == max && z == 2) {                       // east breach — fully open
                continue;
            }
            final boolean brokenTop = (x == 0 && z == 4) || (x == max && z == 4) || (x == 1 && z == 0);
            for (int y = 1; y <= (brokenTop ? 2 : 3); y++) {
                m.put(new BlockPos(x, y, z), wallStone(x + y, z));
            }
        }

        // -- The broken brick forge-flue rising above the back wall (behind the press) — the anti-box vertical feature. -
        for (int y = 1; y <= 5; y++) {
            m.put(new BlockPos(2, y, 0), Blocks.BRICKS.defaultBlockState());
        }
        m.put(new BlockPos(2, 6, 0), Blocks.BRICK_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM));

        // -- The Create machine husk along the back wall: a gearbox driving a cogwheel + shaft into a mechanical press. -
        set(m, mods, new BlockPos(1, 1, 1), cube(), CREATE + "andesite_casing");         // gearbox base
        set(m, mods, new BlockPos(1, 2, 1), shaft(Direction.Axis.X), CREATE + "cogwheel"); // the cog
        set(m, mods, new BlockPos(2, 2, 1), shaft(Direction.Axis.X), CREATE + "shaft");    // drive shaft
        set(m, mods, new BlockPos(2, 1, 1), cube(), CREATE + "andesite_casing");           // the press bed
        set(m, mods, new BlockPos(2, 3, 1), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), CREATE + "mechanical_press"); // press husk
        set(m, mods, new BlockPos(3, 1, 1), shaft(Direction.Axis.X), CREATE + "shaft");
        m.put(new BlockPos(3, 2, 1), Blocks.COBWEB.defaultBlockState());

        // -- The arcane half: an enchanting table ringed with amethyst and candles. ---------------------------------
        m.put(new BlockPos(4, 1, 1), Blocks.ENCHANTING_TABLE.defaultBlockState());
        m.put(new BlockPos(4, 2, 1), Blocks.CANDLE.defaultBlockState()
                .setValue(BlockStateProperties.CANDLES, 3).setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(5, 1, 1), Blocks.AMETHYST_BLOCK.defaultBlockState());
        m.put(new BlockPos(5, 2, 1), Blocks.AMETHYST_CLUSTER.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.UP));
        m.put(new BlockPos(4, 1, 2), Blocks.CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, false));

        // -- The bookshelf study (right wall) with a lectern. -------------------------------------------------------
        m.put(new BlockPos(5, 1, 4), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(5, 2, 4), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(5, 1, 5), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(5, 1, 3), Blocks.LECTERN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));

        // -- The brewing nook (left/front) + a stash of glowstone. --------------------------------------------------
        m.put(new BlockPos(1, 1, 5), Blocks.BREWING_STAND.defaultBlockState());
        m.put(new BlockPos(1, 1, 4), Blocks.CAULDRON.defaultBlockState());
        m.put(new BlockPos(1, 2, 5), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(2, 1, 5), Blocks.GLOWSTONE.defaultBlockState());

        // -- Caved roof: a couple of surviving beams + a soul lantern over the workshop floor; the rest open sky. ----
        m.put(new BlockPos(3, 4, 3), Blocks.STRIPPED_OAK_LOG.defaultBlockState()
                .setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(3, 3, 3), Blocks.SOUL_LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true));
        for (final int[] r : new int[][]{{4, 1, 4}, {2, 1, 3}}) {          // fallen rubble
            m.put(new BlockPos(r[0], r[1], r[2]), Blocks.COBBLESTONE.defaultBlockState());
        }

        // -- The scrap chest on the open floor — air above (3,2,4) so it opens ([[skyseed-structure-chest-openable]]). -
        m.put(new BlockPos(3, 1, 4), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        bes.put(new BlockPos(3, 1, 4), StructureParts.lootChest("skyseed:chests/magitech_scrap"));

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 3), "minecraft:polished_andesite");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers (mirror of CreateRuinsTemplates).
    // ------------------------------------------------------------------------------------------------------------

    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p);
        }
    }

    /** A propertyless full-cube analog (Create's casing blocks — no blockstate properties). */
    private static BlockState cube() {
        return Blocks.STONE.defaultBlockState();
    }

    /** An {@code axis}-carrying log analog (Create's {@code shaft} / {@code cogwheel}). */
    private static BlockState shaft(Direction.Axis axis) {
        return Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, axis);
    }

    /** A deterministic industrial floor mix (andesite / polished andesite / stone). */
    private static BlockState floorStone(int a, int b) {
        final int k = Math.floorMod(a * 3 + b, 4);
        if (k == 0) {
            return Blocks.POLISHED_ANDESITE.defaultBlockState();
        }
        if (k == 1) {
            return Blocks.STONE.defaultBlockState();
        }
        return Blocks.ANDESITE.defaultBlockState();
    }

    /** A weathered wall mix (stone brick / mossy / andesite). */
    private static BlockState wallStone(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 5)) {
            case 0 -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            case 1 -> Blocks.ANDESITE.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }

    /** Finish a build: a jigsaw cell must never carry a mod palette name, so strip mod ids from every jigsaw cell. */
    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
