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
    private static final String MA = "mysticalagriculture:";
    private static final String IE = "immersiveengineering:";
    private static final String AE = "ae2:";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("magitech_workshop/workshop.nbt"), magitechWorkshop());
        writeIfAbsent(base.resolve("essence_farm/farm.nbt"), automatedEssenceFarm());
        writeIfAbsent(base.resolve("substation/substation.nbt"), feToMeSubstation());
        writeIfAbsent(base.resolve("distillery/distillery.nbt"), alchemistsDistillery());
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

        // -- Caved roof: one surviving beam rests wall-to-wall (both ends carried on the side-wall tops at z3, so it
        //    doesn't float); the rest is open sky. A soul lantern hangs from its centre. ------------------------------
        for (int x = 0; x <= max; x++) {
            m.put(new BlockPos(x, 4, 3), Blocks.STRIPPED_OAK_LOG.defaultBlockState()
                    .setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        }
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

    /**
     * The <b>Automated Essence Farm</b> (B27, <b>Create + Mystical Agriculture</b>) — a mechanised inferium field seized
     * up mid-harvest. <em>Not a box</em>: a derelict essence plot (rows of inferium farmland, some trampled back to coarse
     * dirt, crops at mixed growth) straddled by a stalled harvester <b>gantry</b> — a vanilla oak frame (six posts carry two
     * side-rails, and a cross-bridge rides the rails) from which a <b>Create</b> {@code mechanical_drill} husk hangs over
     * the rows, its {@code andesite_casing} body and a cogwheel drive beside it, all frozen. A hanging lantern lights the
     * gantry; at the west end a control station (an {@code andesite_casing} panel + the scrap chest) faces the field, and a
     * derelict inferium {@code growth_accelerator} + a coolant cauldron sit at the east. A broken oak-fence rail runs the
     * edges. Fits Meadow; the theme {@code mobs} pack (1–2 zombies) shambles the rows.
     *
     * <p><b>D4 (both mods).</b> Create: only casing / cogwheel and a {@code mechanical_drill} husk — no working
     * contraption; loot tops out at {@code andesite_alloy}. Mystical Agriculture: <b>tier-1 inferium only</b> — the
     * farmland/crops and a broken tier-1 growth accelerator, shown derelict; loot is a little {@code inferium_essence}
     * (the mineable base tier) — never a seed, a higher-tier essence, a prosperity block or an infusion component.
     */
    private static Built automatedEssenceFarm() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final int xMax = 8, zMax = 4; // 9×5

        // -- Ground: grass, with the inferium-farmland field carved into the centre (some rows trampled to coarse dirt). --
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        for (int x = 1; x <= 7; x++) {
            for (int z = 1; z <= 3; z++) {
                set(m, mods, new BlockPos(x, 0, z), Blocks.FARMLAND.defaultBlockState(), MA + "inferium_farmland");
            }
        }
        for (final int[] c : new int[][]{{5, 1}, {2, 2}, {6, 3}}) { // trampled rows — clear the farmland id (set...,null)
            set(m, mods, new BlockPos(c[0], 0, c[1]), Blocks.COARSE_DIRT.defaultBlockState(), null);
        }
        // Essence crops (tier-1 inferium) at mixed growth; the cells under the drill + over the anchor stay bare/harvested.
        maCrop(m, mods, 1, 1, 7); maCrop(m, mods, 3, 1, 5); maCrop(m, mods, 6, 1, 3); maCrop(m, mods, 7, 1, 7);
        maCrop(m, mods, 1, 2, 7); maCrop(m, mods, 3, 2, 7); maCrop(m, mods, 5, 2, 5); maCrop(m, mods, 7, 2, 3);
        maCrop(m, mods, 2, 3, 7); maCrop(m, mods, 3, 3, 7); maCrop(m, mods, 5, 3, 7); maCrop(m, mods, 7, 3, 7);

        // -- The harvester gantry (vanilla oak): six posts, two side-rails resting on them, and a cross-bridge on the rails.
        for (final int px : new int[]{0, 4, 8}) {
            for (final int pz : new int[]{0, zMax}) {
                m.put(new BlockPos(px, 1, pz), log);
                m.put(new BlockPos(px, 2, pz), log);
            }
        }
        for (int x = 0; x <= xMax; x++) {                                   // the two side-rails (rest on the posts)
            m.put(new BlockPos(x, 3, 0), log.setValue(BlockStateProperties.AXIS, Direction.Axis.X));
            m.put(new BlockPos(x, 3, zMax), log.setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        }
        for (int z = 1; z <= 3; z++) {                                      // the cross-bridge (carried on the 4,_ posts)
            m.put(new BlockPos(4, 3, z), log.setValue(BlockStateProperties.AXIS, Direction.Axis.Z));
        }

        // -- The stalled Create harvester head hanging under the bridge + its drive. --------------------------------
        set(m, mods, new BlockPos(4, 2, 1), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), CREATE + "mechanical_drill"); // the drill, pointing down
        set(m, mods, new BlockPos(4, 2, 3), cube(), CREATE + "andesite_casing");                     // the harvester body
        set(m, mods, new BlockPos(4, 4, 2), shaft(Direction.Axis.X), CREATE + "cogwheel");            // the drive cog atop the bridge
        m.put(new BlockPos(6, 2, 0), Blocks.LANTERN.defaultBlockState()
                .setValue(BlockStateProperties.HANGING, true));                                       // hung from the side-rail (6,3,0)

        // -- The west control station: an andesite-casing panel + the scrap chest facing the field. ----------------
        set(m, mods, new BlockPos(0, 1, 1), cube(), CREATE + "andesite_casing");
        m.put(new BlockPos(0, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(0, 1, 2), StructureParts.lootChest("skyseed:chests/essence_farm_scrap"));
        m.put(new BlockPos(0, 2, 1), Blocks.COBWEB.defaultBlockState());

        // -- The east end: a derelict tier-1 growth accelerator + a coolant cauldron. -------------------------------
        set(m, mods, new BlockPos(8, 1, 2), cube(), MA + "inferium_growth_accelerator");
        m.put(new BlockPos(8, 1, 3), Blocks.WATER_CAULDRON.defaultBlockState()
                .setValue(BlockStateProperties.LEVEL_CAULDRON, 3));
        m.put(new BlockPos(8, 2, 2), Blocks.COBWEB.defaultBlockState());

        // -- A broken oak-fence rail along the edges. ---------------------------------------------------------------
        for (final int[] c : new int[][]{{2, 0}, {6, 0}, {2, zMax}, {6, zMax}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.OAK_FENCE.defaultBlockState());
        }

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(4, 0, 2), "minecraft:grass_block");
        return built(m, bes, mods);
    }

    /** An inferium essence crop at {@code age} (0-7) — a WHEAT analog whose palette Name is swapped to the MA crop. */
    private static void maCrop(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, int age) {
        set(m, mods, new BlockPos(x, 1, z),
                Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, age), MA + "inferium_crop");
    }

    /**
     * The <b>FE→ME Substation</b> (B28, <b>Immersive Engineering + AE2</b>) — the sharpest D4 case in the catalog: a
     * derelict power-conversion station where IE high-voltage power once fed an AE2 ME network. <em>Not a box</em>: a
     * gutted, roofless hall — an IE concrete-and-sheetmetal west end (a wire-mast rising past the wall, a post-transformer
     * husk + a capacitor), a certus-quartz/fluix AE2 east end, and between them a scorched <b>conversion column</b>
     * (deepslate topped by a broken conduit antenna). The centrepiece is the <b>empty controller pit</b> — a scorched
     * deepslate mount where the ME Controller was torn out, with a couple of fluix remnants beside it.
     *
     * <p><b>D4 — the AE2 gate is the sharpest (AE2PLAN / [[skyseed-ae2-curated-set]]).</b> There is <b>NO</b>
     * {@code sky_stone_*} (harvestable ⇒ a Controller), <b>NO</b> {@code ae2:controller}, and <b>NO</b> inscriber
     * press/processor anywhere — the pit is deliberately <em>empty</em>. Built only from certus/fluix cubes (mid-game,
     * renewable, not gating) + IE concrete/sheetmetal husks. Loot is fluix + a copper wire coil — never sky stone, a press,
     * a processor or a working multiblock. The two mods' machinery resolves to air without them, so the vanilla shell (the
     * deepslate column + pit, the conduit antenna, the chest, a lantern) is the assertable gametest anchor.
     * Fits Rocky; the theme {@code mobs} pack (a creeper) lurks.
     */
    private static Built feToMeSubstation() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 6, zMax = 4; // 7×5

        // Floor: IE concrete (west) transitioning to AE2 certus-quartz (east).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                if (x <= 2) {
                    set(m, mods, new BlockPos(x, 0, z), cube(), IE + ((x + z) % 3 == 0 ? "concrete_tile" : "concrete"));
                } else {
                    set(m, mods, new BlockPos(x, 0, z), cube(), AE + ((x + z) % 3 == 0 ? "cut_quartz_block" : "quartz_block"));
                }
            }
        }
        // The EMPTY CONTROLLER PIT (D4): a scorched deepslate mount where the Controller was torn out — nothing above it.
        set(m, mods, new BlockPos(5, 0, 2), Blocks.DEEPSLATE.defaultBlockState(), null);
        set(m, mods, new BlockPos(5, 0, 3), Blocks.DEEPSLATE.defaultBlockState(), null);

        // Perimeter walls: IE sheetmetal (west) / AE2 quartz (east), two courses (corners three), ruined — a front doorway
        // + two breaches.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                if (!(x == 0 || x == xMax || z == 0 || z == zMax)) {
                    continue;
                }
                if (x == 3 && z == 0) {
                    continue; // front doorway
                }
                if ((x == 0 && z == 2) || (x == xMax && z == 3)) {
                    continue; // breaches
                }
                final boolean corner = (x == 0 || x == xMax) && (z == 0 || z == zMax);
                for (int y = 1; y <= (corner ? 3 : 2); y++) {
                    if (x <= 2) {
                        set(m, mods, new BlockPos(x, y, z), cube(), IE + (y == 1 ? "sheetmetal_steel" : "treated_wood_horizontal"));
                    } else {
                        set(m, mods, new BlockPos(x, y, z), cube(), AE + (y == 1 ? "quartz_block" : "quartz_bricks"));
                    }
                }
            }
        }
        set(m, mods, new BlockPos(xMax, 2, 2), Blocks.GLASS.defaultBlockState(), AE + "quartz_glass"); // an AE2 window

        // IE power-in (west): a wire-mast rising past the wall, a post-transformer husk + a capacitor.
        for (int y = 1; y <= 3; y++) {
            set(m, mods, new BlockPos(1, y, 1), cube(), IE + "alu_post");
        }
        set(m, mods, new BlockPos(1, 4, 1), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.UP), IE + "connector_hv"); // the HV line coming in
        set(m, mods, new BlockPos(1, 1, 3), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), IE + "post_transformer"); // FE->ME transformer husk
        set(m, mods, new BlockPos(2, 1, 3), cube(), IE + "capacitor_hv");

        // The scorched conversion column (vanilla): deepslate topped by a broken conduit antenna — the anti-box vertical,
        // standing right on the IE/AE2 boundary. All stacked on the floor, so nothing floats.
        m.put(new BlockPos(3, 1, 2), Blocks.DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(3, 2, 2), Blocks.DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(3, 3, 2), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
        m.put(new BlockPos(3, 4, 2), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        // AE2 side: the fluix coupling beside the column + fluix remnants around the empty pit (the pit itself stays open).
        set(m, mods, new BlockPos(4, 1, 2), cube(), AE + "fluix_block");
        set(m, mods, new BlockPos(6, 1, 2), cube(), AE + "fluix_block");
        set(m, mods, new BlockPos(4, 1, 3), cube(), AE + "chiseled_quartz_block");

        // The scrap chest on the interior AE2 floor (air above at (4,2,1) so it opens); a floor lantern; cobwebs. Both sit
        // on INTERIOR cells, not the perimeter — a wall cell already carries a mod id, so a plain overwrite there would
        // emit the chest/lantern as that mod block (the stale-id gotcha).
        m.put(new BlockPos(4, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 1), StructureParts.lootChest("skyseed:chests/substation_scrap"));
        m.put(new BlockPos(2, 1, 2), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(1, 2, 3), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(5, 2, 2), Blocks.COBWEB.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 3), "minecraft:deepslate");
        return built(m, bes, mods);
    }

    /**
     * The <b>Alchemist's Distillery</b> (B29, <b>Immersive Engineering + Iron's Spells</b>) — an IE refinery repurposed as
     * an arcane still, gone cold in the badlands. <em>Not a box</em>: a roofless sheetmetal shed (breached walls, broken
     * corners) around a still — a lava-cauldron fire under a weathered-copper still-head with its brewing stand, a
     * weathered-copper <b>condenser column</b> topped by a lightning-rod vent (the anti-box vertical), a water-cauldron
     * receiver, and IE fluid tanks (metal barrels) + a capacitor to the side; an amethyst focus, a bookshelf study with a
     * lectern of alchemy notes, and the essence-scrap chest. Iron's Spells is a mob/item mod, so the arcane fittings are
     * all vanilla (and the assertable gametest shell); the "Iron's" flavour is the mage of the theme {@code mobs} pack and
     * the loot. Fits Badlands.
     *
     * <p><b>D4 (both mods).</b> IE: only metal-barrel / sheetmetal / capacitor husks — no working multiblock; loot is an
     * iron plate. Iron's Spells: loot is {@code arcane_essence} (the common crafting mat) — never a spellbook, a named
     * scroll or an Upgrade Orb.
     */
    private static Built alchemistsDistillery() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 6, zMax = 4; // 7×5

        // IE concrete floor (the refinery pad).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                set(m, mods, new BlockPos(x, 0, z), cube(), IE + ((x + z) % 3 == 0 ? "concrete_tile" : "concrete"));
            }
        }

        // Perimeter walls: rusty IE sheetmetal + treated wood, two courses (corners three), ruined — a front doorway
        // + two breaches.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                if (!(x == 0 || x == xMax || z == 0 || z == zMax)) {
                    continue;
                }
                if (x == 3 && z == 0) {
                    continue; // front doorway
                }
                if ((x == 0 && z == 3) || (x == xMax && z == 2)) {
                    continue; // breaches
                }
                final boolean corner = (x == 0 || x == xMax) && (z == 0 || z == zMax);
                final boolean brokenTop = (x == 0 && z == 0) || (x == xMax && z == zMax);
                for (int y = 1; y <= (brokenTop ? 2 : (corner ? 3 : 2)); y++) {
                    set(m, mods, new BlockPos(x, y, z), cube(), IE + (y == 1 ? "sheetmetal_steel" : "treated_wood_horizontal"));
                }
            }
        }

        // -- The still (LEFT, x1-x2) — all-vanilla so it stands on its own: a copper condenser column topped by a
        //    lightning-rod vent, a lava-cauldron fire, a copper still-head + brewing stand. The central x3 axis (the
        //    doorway line) is left CLEAR as a walkway to the back, so the still never bars the room. -------------------
        m.put(new BlockPos(1, 1, 1), Blocks.WEATHERED_COPPER.defaultBlockState()); // condenser column
        m.put(new BlockPos(1, 2, 1), Blocks.WEATHERED_COPPER.defaultBlockState());
        m.put(new BlockPos(1, 3, 1), Blocks.WEATHERED_COPPER.defaultBlockState());
        m.put(new BlockPos(1, 4, 1), Blocks.LIGHTNING_ROD.defaultBlockState());    // the vent (anti-box vertical)
        m.put(new BlockPos(2, 1, 1), Blocks.LAVA_CAULDRON.defaultBlockState());    // the still fire
        m.put(new BlockPos(2, 1, 2), Blocks.WEATHERED_COPPER.defaultBlockState()); // the still base (vanilla)
        m.put(new BlockPos(2, 2, 2), Blocks.BREWING_STAND.defaultBlockState());    // the still head, on the copper base
        set(m, mods, new BlockPos(1, 1, 2), cube(), IE + "metal_barrel");          // an IE fluid tank
        set(m, mods, new BlockPos(1, 1, 3), cube(), IE + "capacitor_hv");
        m.put(new BlockPos(2, 1, 3), Blocks.GLOWSTONE.defaultBlockState());

        // -- The right side (x4-x5): a water receiver + an IE tank, the amethyst focus, a bookshelf study + lectern,
        //    the essence-scrap chest — all reachable straight off the x3 walkway. --------------------------------------
        m.put(new BlockPos(4, 1, 1), Blocks.WATER_CAULDRON.defaultBlockState()
                .setValue(BlockStateProperties.LEVEL_CAULDRON, 3));               // the receiver
        set(m, mods, new BlockPos(5, 1, 1), cube(), IE + "metal_barrel");
        m.put(new BlockPos(4, 1, 2), Blocks.AMETHYST_BLOCK.defaultBlockState());   // the arcane focus
        m.put(new BlockPos(4, 2, 2), Blocks.AMETHYST_CLUSTER.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.UP));
        m.put(new BlockPos(5, 1, 2), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(5, 2, 2), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(5, 1, 3), Blocks.LECTERN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        m.put(new BlockPos(2, 3, 2), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(5, 3, 3), Blocks.COBWEB.defaultBlockState());
        // The chest faces the walkway (west); air above (4,2,3) so it opens.
        m.put(new BlockPos(4, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 3), StructureParts.lootChest("skyseed:chests/distillery_scrap"));

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 3), "minecraft:cobblestone");
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
