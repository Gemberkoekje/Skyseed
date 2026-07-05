package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Band 2</b> Iron's Spells & Spellbooks buildings of {@code VARIETYSTRUCTUREPLAN.md} §3 (B20–B22) — three
 * {@code requires}-gated "rare" surprise builds that germinate on an ordinary island only when <b>Iron's Spells</b> is
 * installed: a small <b>Wizard's Hut</b>, a <b>Wizard's Tower</b>, and a <b>Cursed Obelisk</b>.
 *
 * <p><b>Unlike the Create/IE/AE2 batches, these are all-vanilla architecture</b> (stone/wood/bookshelves/candles/
 * blackstone). Iron's Spells is a mob-and-item mod, not a block mod, so the "Iron's" flavour comes from (1) the
 * {@code requires: ["irons_spellbooks"]} gate — they only appear with the mod — (2) the theme {@code mobs} pack (mages,
 * resolve-then-skipped without the mod), and (3) the loot: {@code skyseed:chests/irons_wizard} (vanilla arcane scrap) +
 * inert {@code add_drop} GLMs layering a token <b>Arcane Essence</b> / <b>Common Ink</b>. So there is no {@code modNames}
 * side-map here, and the assembly gametests assert the vanilla structure directly.
 *
 * <p><b>D4 gate-safety (IRONSPELLSPLAN / §5).</b> Loot is the low tier only — arcane essence + common ink (common crafting
 * mats) — never an <b>Upgrade Orb</b>, a top-tier ink, or a high-tier/named scroll (those stay the reward of the mod's
 * own deadly explore structures via the existing {@code irons_upgrade_orb} GLM).
 */
public final class IronsRuinsTemplates {
    private IronsRuinsTemplates() {}

    private static final String SCRAP = "skyseed:chests/irons_wizard";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("wizard_hut/hut.nbt"), wizardHut());
        writeIfAbsent(base.resolve("wizard_tower/tower.nbt"), wizardTower());
        writeIfAbsent(base.resolve("cursed_obelisk/obelisk.nbt"), cursedObelisk());
    }

    /**
     * The <b>Small Wizard's Hut</b> (B20) — a hedge-wizard's cottage, <em>not a box</em>: a 5×5 cobble-and-oak hut under
     * a tall <em>pointed witch-hat</em> spruce roof capped by a lantern beacon (the anti-box silhouette). Inside, a
     * bookshelf wall, a brewing stand over a water cauldron, a lectern, an amethyst focus, lit candles and cobwebs, and
     * the wizard's chest. All vanilla. Fits Forest / Meadow; the theme's {@code mobs} pack (an apprentice mage) keeps it.
     */
    private static Built wizardHut() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        final BlockState plank = Blocks.SPRUCE_PLANKS.defaultBlockState();
        final int max = 4, mid = 2;

        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.STONE_BRICKS.defaultBlockState());
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (perim) {
                    m.put(new BlockPos(x, 1, z), corner ? Blocks.OAK_LOG.defaultBlockState() : cobble);
                    m.put(new BlockPos(x, 2, z), corner ? Blocks.OAK_LOG.defaultBlockState() : cobble);
                }
            }
        }
        m.remove(new BlockPos(mid, 1, 0));
        m.remove(new BlockPos(mid, 2, 0)); // doorway
        m.put(new BlockPos(0, 2, 2), Blocks.GLASS_PANE.defaultBlockState());
        m.put(new BlockPos(max, 2, 2), Blocks.GLASS_PANE.defaultBlockState());

        // The pointed witch-hat roof: two converging spruce-stair rings to a lantern-topped spire.
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 3, z), roofRing(x, z, 0, max) ? spruceRoofStair(x, z, 0, max) : plank);
            }
        }
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 4, z), roofRing(x, z, 1, 3) ? spruceRoofStair(x, z, 1, 3) : plank);
            }
        }
        m.put(new BlockPos(mid, 5, mid), plank);
        m.put(new BlockPos(mid, 6, mid), Blocks.OAK_FENCE.defaultBlockState()); // spire pole
        m.put(new BlockPos(mid, 7, mid), Blocks.LANTERN.defaultBlockState());   // beacon

        // Arcane interior (3×3), centre kept clear for the mage.
        m.put(new BlockPos(1, 1, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(1, 2, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(3, 1, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(1, 1, 1), Blocks.BREWING_STAND.defaultBlockState());
        m.put(new BlockPos(1, 1, 2), Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
        m.put(new BlockPos(3, 1, 1), Blocks.LECTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        m.put(new BlockPos(2, 1, 1), litCandle(Blocks.CANDLE));
        m.put(new BlockPos(1, 2, 1), Blocks.AMETHYST_BLOCK.defaultBlockState()); // arcane focus
        m.put(new BlockPos(3, 2, 3), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(3, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(3, 1, 2), StructureParts.lootChest(SCRAP)); // air above at (3,2,2)

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:stone_bricks");
        return new Built(m, bes);
    }

    /**
     * The <b>Wizard's Tower</b> (B21) — a mage's study risen over the land, <em>not a box</em> by nature: a 5×5
     * stone-brick tower six courses tall (two storeys split by a plank floor, joined by a ladder), arrow-slit + glass
     * windows, a small jutting balcony, and a pointed spruce-stair spire crowned with a lantern. The lower floor holds a
     * bookshelf study; the upper the wizard's alchemy — a brewing stand, an amethyst focus, candles, and the chest. All
     * vanilla, weathered with moss + cobwebs. Fits Rocky / Ancient; the theme's {@code mobs} pack (two mages) holds it.
     */
    private static Built wizardTower() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;

        // The shaft: 5×5 weathered stone-brick walls, six courses (y1..6), with a doorway and windows.
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.STONE_BRICKS.defaultBlockState());
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (perim) {
                    for (int y = 1; y <= 6; y++) {
                        m.put(new BlockPos(x, y, z), weathered(x, y + z));
                    }
                }
            }
        }
        m.remove(new BlockPos(mid, 1, 0));
        m.remove(new BlockPos(mid, 2, 0)); // doorway
        for (final BlockPos slit : new BlockPos[]{
                new BlockPos(0, 3, 2), new BlockPos(max, 3, 2), new BlockPos(2, 4, max), new BlockPos(0, 5, 2)}) {
            m.put(slit, Blocks.GLASS_PANE.defaultBlockState());
        }
        // A mid floor at y3 (splits the two storeys) with a ladder hatch, and a ladder up the west wall.
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                if (!(x == 1 && z == 1)) { // the ladder hatch
                    m.put(new BlockPos(x, 3, z), Blocks.SPRUCE_PLANKS.defaultBlockState());
                }
            }
        }
        for (int y = 1; y <= 5; y++) {
            m.put(new BlockPos(1, y, 1), Blocks.LADDER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
        }
        // A small jutting balcony off the east wall (the anti-box protrusion): a 2-tall doorway in the wall, a slab
        // platform out past it, and a fence rail.
        m.remove(new BlockPos(max, 4, 2));
        m.remove(new BlockPos(max, 5, 2)); // the balcony doorway
        m.put(new BlockPos(max + 1, 4, 2), Blocks.STONE_BRICK_SLAB.defaultBlockState());
        m.put(new BlockPos(max + 1, 5, 2), Blocks.OAK_FENCE.defaultBlockState());

        // A pointed spruce-stair roof + lantern spire on top (y7+).
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 7, z), roofRing(x, z, 0, max) ? spruceRoofStair(x, z, 0, max) : Blocks.SPRUCE_PLANKS.defaultBlockState());
            }
        }
        m.put(new BlockPos(mid, 8, mid), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(mid, 9, mid), Blocks.LANTERN.defaultBlockState());

        // Lower floor — the study (bookshelves + a lectern); upper floor — the alchemy (brewing + amethyst + the chest).
        m.put(new BlockPos(1, 1, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(1, 2, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(3, 1, 3), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(3, 1, 1), Blocks.LECTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        m.put(new BlockPos(1, 1, 2), litCandle(Blocks.CANDLE));
        m.put(new BlockPos(3, 4, 3), Blocks.BREWING_STAND.defaultBlockState());
        m.put(new BlockPos(3, 4, 2), Blocks.AMETHYST_BLOCK.defaultBlockState());
        m.put(new BlockPos(2, 4, 3), litCandle(Blocks.CANDLE));
        m.put(new BlockPos(2, 5, 2), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(3, 4, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(3, 4, 1), StructureParts.lootChest(SCRAP)); // air above at (3,5,1)

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:stone_bricks");
        return new Built(m, bes);
    }

    /**
     * The <b>Cursed Obelisk</b> (B22) — a necromancer's dark monument, <em>not a box</em> by nature: a tapering
     * blackstone/deepslate/basalt spire (a 5×5 stepped base narrowing to a broken point ~8 up) over a cursed altar — a
     * soul-lantern-lit basalt slab strung with black candles, bone piles, a wither rose, and the grave-goods chest.
     * All vanilla. Fits Ancient / Badlands; the theme's {@code mobs} pack (a necromancer) presides.
     */
    private static Built cursedObelisk() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int mid = 2;

        // A cracked-stone plinth (5×5) the spire rises from.
        for (int x = 0; x <= 4; x++) {
            for (int z = 0; z <= 4; z++) {
                m.put(new BlockPos(x, 0, z), dark(x, z));
            }
        }
        // The tapering spire: a solid 3×3 core (y1..3), a 1×1 shaft (y4..7), a broken point.
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                for (int y = 1; y <= 3; y++) {
                    m.put(new BlockPos(x, y, z), dark(x + y, z));
                }
            }
        }
        for (int y = 4; y <= 7; y++) {
            m.put(new BlockPos(mid, y, mid), dark(mid, y));
        }
        m.put(new BlockPos(mid, 8, mid), Blocks.SOUL_LANTERN.defaultBlockState()); // the cursed beacon on the broken tip
        // The corners stepped back so the silhouette tapers (ruin/irregular), AND a front-facing base niche carved out of
        // the core (2,1..2,1..2) so the presiding necromancer has an open cell to spawn in, facing the altar.
        for (final BlockPos cut : new BlockPos[]{
                new BlockPos(1, 3, 1), new BlockPos(3, 3, 1), new BlockPos(1, 3, 3), new BlockPos(3, 3, 3),
                new BlockPos(3, 2, 3),
                new BlockPos(2, 1, 1), new BlockPos(2, 1, 2), new BlockPos(2, 2, 1), new BlockPos(2, 2, 2)}) {
            m.remove(cut);
        }

        // The cursed altar at the foot (front, z=0 side): a basalt slab, black candles, soul lantern, bones, a wither rose.
        m.put(new BlockPos(mid, 1, 0), Blocks.POLISHED_BLACKSTONE_SLAB.defaultBlockState());
        m.put(new BlockPos(mid, 2, 0), litCandle(Blocks.BLACK_CANDLE));
        m.put(new BlockPos(1, 1, 0), Blocks.SOUL_LANTERN.defaultBlockState());
        m.put(new BlockPos(3, 1, 0), Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Y));
        m.put(new BlockPos(0, 1, 1), Blocks.WITHER_ROSE.defaultBlockState());
        m.put(new BlockPos(4, 1, 1), Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(0, 1, 3), Blocks.COBWEB.defaultBlockState());
        // The grave-goods chest on the open plinth ring beside the altar (NOT inside the core) — air above at (4,2,2).
        m.put(new BlockPos(4, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 2), StructureParts.lootChest(SCRAP));

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:blackstone");
        return new Built(m, bes);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------------------------------------------

    /** Whether {@code (x, z)} is on the rim of the {@code [lo, hi]} square (a roof-ring cell, i.e. a sloped stair). */
    private static boolean roofRing(int x, int z, int lo, int hi) {
        return x == lo || x == hi || z == lo || z == hi;
    }

    /** A spruce roof stair on a rim cell, facing the rim so the roof slopes up toward the centre (a pyramid/hat). */
    private static BlockState spruceRoofStair(int x, int z, int lo, int hi) {
        final Direction f;
        if (z == lo) {
            f = Direction.NORTH;
        } else if (z == hi) {
            f = Direction.SOUTH;
        } else if (x == lo) {
            f = Direction.WEST;
        } else {
            f = Direction.EAST;
        }
        return Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, f);
    }

    /** A lit single candle of the given candle block. */
    private static BlockState litCandle(net.minecraft.world.level.block.Block candle) {
        return candle.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    }

    /** A deterministic weathered stone-brick mix (plain / mossy / cracked). */
    private static BlockState weathered(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 5)) {
            case 0 -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            case 1 -> Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }

    /** A deterministic dark-stone mix (blackstone / deepslate / basalt) for the cursed obelisk. */
    private static BlockState dark(int a, int b) {
        return switch (Math.floorMod(a * 5 + b * 11, 4)) {
            case 0 -> Blocks.DEEPSLATE.defaultBlockState();
            case 1 -> Blocks.BASALT.defaultBlockState();
            case 2 -> Blocks.POLISHED_BLACKSTONE.defaultBlockState();
            default -> Blocks.BLACKSTONE.defaultBlockState();
        };
    }
}
