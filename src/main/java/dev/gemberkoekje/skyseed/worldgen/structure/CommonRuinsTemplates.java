package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Band 1</b> common, all-vanilla ruins of {@code VARIETYSTRUCTUREPLAN.md} §3 — the humble surprise buildings
 * that dilute the flagships: a derelict shed, a collapsed cabin, a ruined farmstead, and so on. Each is a small,
 * self-contained single-piece jigsaw (all vanilla blocks, so it ships in the base mod), authored in code and written
 * to {@code .nbt} at dev time — see {@link DevStructureGenerator}. They ride the same {@code rare_structures} table as
 * the older rare structures; a theme lists one at a {@code weight} (common ≈ 6) under its {@code rare_structure_chance}
 * gate. Reward-bearing (a loot chest/barrel) so they are {@code explorable}.
 */
public final class CommonRuinsTemplates {
    private CommonRuinsTemplates() {}

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("tool_shed/shed.nbt"), toolShed());
        writeIfAbsent(base.resolve("cairn/shrine.nbt"), cairn());
        writeIfAbsent(base.resolve("graveyard/plot.nbt"), graveyard());
        writeIfAbsent(base.resolve("collapsed_cabin/cabin.nbt"), collapsedCabin());
        writeIfAbsent(base.resolve("fossil_dig/dig.nbt"), fossilDig());
        writeIfAbsent(base.resolve("overgrown_well/well.nbt"), overgrownWell());
        writeIfAbsent(base.resolve("fishing_camp/camp.nbt"), fishingCamp());
        writeIfAbsent(base.resolve("farmstead/plot.nbt"), ruinedFarmstead());
        writeIfAbsent(base.resolve("hunters_blind/blind.nbt"), huntersBlind());
        writeIfAbsent(base.resolve("mine_head/adit.nbt"), mineHead());
        writeIfAbsent(base.resolve("prospectors_camp/camp.nbt"), prospectorsCamp());
        writeIfAbsent(base.resolve("bandit_camp/camp.nbt"), banditCamp());
    }

    /**
     * The <b>Tool Shed</b> (B1) — a derelict homestead outbuilding, deliberately <em>not a box</em> (see
     * [[skyseed-no-boxes]]): a 5×4 oak shed with an irregular silhouette (an open lean-to woodshed juts off the east
     * wall, a chopping stump sits out front), a pitched oak-stair gable that has <em>caved in</em>, planks missing
     * from the walls and a half-collapsed back corner post. Inside, light falls through the roof hole onto a
     * toolsmith's chest, a crafting table, a barrel and a dead potted plant, with a floor lantern and cobwebs in the
     * corners. All vanilla, so it ships in the base mod. Anchored at the shed's floor centre; the theme's {@code mobs}
     * pack (a lone spider) haunts it.
     */
    private static Built toolShed() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.OAK_PLANKS.defaultBlockState();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState logX = Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        final int xMax = 4, zMax = 3; // the shed proper is 5×4; the lean-to extends it to x=6

        // Shed floor + walls: oak-plank walls, oak-log corner posts, two courses tall.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), plank); // floor
                final boolean perim = x == 0 || x == xMax || z == 0 || z == zMax;
                final boolean corner = (x == 0 || x == xMax) && (z == 0 || z == zMax);
                if (perim) {
                    m.put(new BlockPos(x, 1, z), corner ? log : plank);
                    m.put(new BlockPos(x, 2, z), corner ? log : plank);
                }
            }
        }
        // Pitched oak gable roof (ridge along Z at the centre column), flush eaves.
        gableRoof(m, 0, xMax, 0, zMax, 3, plank, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, 0);

        // Break the box — RUIN: an open doorway (front, z=0), a glass-pane window (west, x=0), planks fallen from the
        // walls, a half-collapsed back-east corner post, and a caved-in patch of roof that lets the weather in.
        m.remove(new BlockPos(2, 1, 0));
        m.remove(new BlockPos(2, 2, 0));                              // doorway
        m.put(new BlockPos(0, 2, 1), Blocks.GLASS_PANE.defaultBlockState()); // grimy window
        for (final BlockPos gap : new BlockPos[]{new BlockPos(1, 2, 0), new BlockPos(4, 2, 2), new BlockPos(2, 2, 3)}) {
            m.remove(gap);                                           // fallen wall planks
        }
        m.remove(new BlockPos(xMax, 2, zMax));                       // the back-east corner post, snapped short
        m.remove(new BlockPos(1, 4, 1));
        m.remove(new BlockPos(1, 4, 2));                             // caved-in roof patch

        // Interior kit (centre kept clear for the spider): toolsmith's chest, crafting table, barrel, a dead potted
        // plant, a knocked-over floor lantern, and cobwebs strung in the corners under the broken roof.
        m.put(new BlockPos(3, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(3, 1, 1), StructureParts.lootChest("minecraft:chests/village/village_toolsmith"));
        m.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(3, 1, 2), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(1, 1, 2), Blocks.POTTED_DEAD_BUSH.defaultBlockState());
        m.put(new BlockPos(2, 1, 1), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(1, 2, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(3, 2, 2), Blocks.COBWEB.defaultBlockState());

        // The lean-to WOODSHED off the east wall (x=5..6, z=1..2): a worn dirt patch, two oak-fence corner posts, a
        // flat oak-slab awning resting on them and the shed eave, and a stacked woodpile sheltering beneath. This is
        // what breaks the rectangular silhouette.
        for (final int[] c : new int[][]{{5, 1}, {5, 2}, {6, 1}, {6, 2}}) {
            m.put(new BlockPos(c[0], 0, c[1]), Blocks.COARSE_DIRT.defaultBlockState());
        }
        for (final int[] c : new int[][]{{6, 1}, {6, 2}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.OAK_FENCE.defaultBlockState());
            m.put(new BlockPos(c[0], 2, c[1]), Blocks.OAK_FENCE.defaultBlockState());
        }
        for (final int[] c : new int[][]{{5, 1}, {5, 2}, {6, 1}, {6, 2}}) {
            m.put(new BlockPos(c[0], 3, c[1]), Blocks.OAK_SLAB.defaultBlockState()); // awning
        }
        m.put(new BlockPos(5, 1, 1), logX);
        m.put(new BlockPos(5, 1, 2), logX);
        m.put(new BlockPos(5, 2, 1), Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        // A chopping stump out front of the woodshed — a last bit of yard clutter past the walls.
        m.put(new BlockPos(6, 1, 0), Blocks.STRIPPED_OAK_LOG.defaultBlockState());

        StructureParts.linkFences(m); // join the lean-to posts before the anchor (jigsaw placement skips neighbour updates)
        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:oak_planks");
        return new Built(m, bes);
    }

    /**
     * The <b>Roadside Shrine / Cairn</b> (B6) — a wayside shrine gone to ruin, <em>not a box</em>: a worn mossy-cobble
     * apron with stepped approaches, a lantern-topped altar plinth flanked by lit candles, and four broken stone-brick
     * columns of <em>uneven, ruined heights</em> (a tall capped one, a snapped one, a stump) that give it an irregular
     * silhouette. Moss carpets creep over it and cobwebs hang between the pillars; a temple offering-chest sits at the
     * foot of the altar. All vanilla. Fits the stone themes (Rocky / Ancient). Peaceful — no guardian.
     */
    private static Built cairn() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        // A rough 3×3 mossy-cobble apron with a stepped-slab approach on each side; corners left as island surface.
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 0, z), pavers(x, z));
            }
        }
        for (final BlockPos step : new BlockPos[]{
                new BlockPos(0, 0, 2), new BlockPos(4, 0, 2), new BlockPos(2, 0, 0), new BlockPos(2, 0, 4)}) {
            m.put(step, Blocks.COBBLESTONE_SLAB.defaultBlockState());
        }
        // Four broken columns at the apron corners, deliberately uneven — a tall capped pillar, a snapped one, a stump.
        column(m, 1, 1, 3);
        column(m, 3, 3, 3);
        column(m, 3, 1, 2);
        column(m, 1, 3, 1);
        m.put(new BlockPos(1, 4, 1), Blocks.LANTERN.defaultBlockState()); // a lantern crowns the tallest pillar
        m.put(new BlockPos(3, 2, 1), Blocks.COBWEB.defaultBlockState());   // strung between the snapped columns
        m.put(new BlockPos(1, 2, 3), Blocks.COBWEB.defaultBlockState());

        // The altar: a mossy plinth, a wall post, a lantern; two lit candles flanking; moss creeping over the apron.
        m.put(new BlockPos(2, 1, 2), Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
        m.put(new BlockPos(2, 2, 2), Blocks.STONE_BRICK_WALL.defaultBlockState());
        m.put(new BlockPos(2, 3, 2), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(1, 1, 2), litCandle());
        m.put(new BlockPos(3, 1, 2), litCandle());
        m.put(new BlockPos(2, 1, 3), Blocks.MOSS_CARPET.defaultBlockState());
        m.put(new BlockPos(2, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        bes.put(new BlockPos(2, 1, 1), StructureParts.lootChest("minecraft:chests/village/village_temple"));

        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:mossy_cobblestone");
        return new Built(m, bes);
    }

    /**
     * The <b>Graveyard Corner</b> (B10) — a small overgrown boneyard, <em>not a box</em>: a scatter of leaning
     * headstones (cobblestone/andesite wall posts) over podzol grave-mounds, a broken oak-fence rail with a gap, a
     * bare dead tree in the corner, a wither rose and cobwebs — and, at the back, a little stone-brick crypt with a
     * slab lid hiding the coffin chest. Nothing lines up; the silhouette is all irregular. Vanilla. Fits any spooky
     * theme (wired to Forest); the theme's {@code mobs} pack (a zombie or two) rises from the graves.
     */
    private static Built graveyard() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        // Grave mounds: scattered podzol/coarse-dirt patches (not a solid floor — bare island grass shows between).
        for (final BlockPos mound : new BlockPos[]{
                new BlockPos(1, 0, 1), new BlockPos(1, 0, 2), new BlockPos(3, 0, 1), new BlockPos(3, 0, 2),
                new BlockPos(5, 0, 1), new BlockPos(2, 0, 3), new BlockPos(4, 0, 3)}) {
            m.put(mound, ((mound.getX() + mound.getZ()) % 2 == 0 ? Blocks.PODZOL : Blocks.COARSE_DIRT).defaultBlockState());
        }
        // Leaning headstones — wall posts of varied stone, each over its mound; one is a knocked-over slab.
        m.put(new BlockPos(1, 1, 1), Blocks.COBBLESTONE_WALL.defaultBlockState());
        m.put(new BlockPos(3, 1, 1), Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState());
        m.put(new BlockPos(5, 1, 1), Blocks.ANDESITE_WALL.defaultBlockState());
        m.put(new BlockPos(2, 0, 3), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState()); // a fallen headstone slab
        m.put(new BlockPos(3, 1, 2), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(1, 1, 2), Blocks.WITHER_ROSE.defaultBlockState());

        // A broken oak-fence rail with a gap (the graveyard gate long gone), along the front (z=0).
        for (final int fx : new int[]{0, 1, 3, 4, 6}) { // note: x=2 and x=5 left open — the broken gaps
            m.put(new BlockPos(fx, 1, 0), Blocks.OAK_FENCE.defaultBlockState());
        }
        // A bare dead tree leaning in the corner — a stripped trunk with a couple of snag branches, no leaves.
        m.put(new BlockPos(0, 1, 4), Blocks.OAK_LOG.defaultBlockState());
        m.put(new BlockPos(0, 2, 4), Blocks.OAK_LOG.defaultBlockState());
        m.put(new BlockPos(0, 3, 4), Blocks.OAK_LOG.defaultBlockState());
        m.put(new BlockPos(1, 3, 4), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(0, 3, 3), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z));

        // The crypt (back-right): a small stone-brick tomb with a slab lid over the coffin chest.
        for (final int[] c : new int[][]{{5, 0, 3}, {6, 0, 3}, {5, 0, 4}, {6, 0, 4}}) {
            m.put(new BlockPos(c[0], c[1], c[2]), weatheredBrick(c[0], c[2]));
        }
        m.put(new BlockPos(6, 1, 4), weatheredBrick(6, 4));
        m.put(new BlockPos(6, 1, 3), weatheredBrick(3, 6));
        m.put(new BlockPos(5, 1, 4), weatheredBrick(5, 4));
        m.put(new BlockPos(5, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 3), StructureParts.lootChest("minecraft:chests/village/village_temple"));
        m.put(new BlockPos(5, 2, 3), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState()); // the shoved-aside lid

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:coarse_dirt");
        return new Built(m, bes);
    }

    /**
     * The <b>Collapsed Cabin</b> (B2) — a frozen homestead fallen to ruin, <em>not a box</em>: a 7×6 spruce cabin whose
     * back-east corner has <em>caved in</em> (walls and roof breached, fallen timbers and snow spilling inside), with a
     * cobblestone chimney breast whose stack punches up past the low eave and has lost its cap. Frosted-glass windows,
     * an open doorway, a hearth furnace, a snowy-village chest, a barrel and cobwebs within; snow has drifted in through
     * the breach. All vanilla. Fits Frozen; the theme's {@code mobs} pack (a stray) shelters in it.
     */
    private static Built collapsedCabin() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.SPRUCE_PLANKS.defaultBlockState();
        final BlockState log = Blocks.SPRUCE_LOG.defaultBlockState();
        final BlockState logX = Blocks.SPRUCE_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        final int xMax = 6, zMax = 5;

        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), plank);
                final boolean perim = x == 0 || x == xMax || z == 0 || z == zMax;
                final boolean corner = (x == 0 || x == xMax) && (z == 0 || z == zMax);
                if (perim) {
                    m.put(new BlockPos(x, 1, z), corner ? log : plank);
                    m.put(new BlockPos(x, 2, z), corner ? log : plank);
                }
            }
        }
        gableRoof(m, 0, xMax, 0, zMax, 3, plank, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, 0);

        // Doorway (front) + two frosted windows.
        m.remove(new BlockPos(3, 1, 0));
        m.remove(new BlockPos(3, 2, 0));
        m.put(new BlockPos(0, 2, 2), Blocks.GLASS_PANE.defaultBlockState());
        m.put(new BlockPos(xMax, 2, 2), Blocks.GLASS_PANE.defaultBlockState());

        // A cobblestone chimney breast on the west wall, its stack punched up past the low eave, the cap fallen away.
        for (int y = 1; y <= 5; y++) {
            m.put(new BlockPos(0, y, 3), cobble);
        }
        m.put(new BlockPos(0, 6, 3), Blocks.COBBLESTONE_SLAB.defaultBlockState());
        m.put(new BlockPos(1, 1, 3), Blocks.FURNACE.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)); // the hearth

        // THE COLLAPSE (back-east corner): walls + roof caved in, spilling fallen timbers and snow inside — the ruin
        // that breaks the rectangular silhouette.
        for (final BlockPos gone : new BlockPos[]{
                new BlockPos(6, 1, 4), new BlockPos(6, 2, 4), new BlockPos(6, 2, 5), new BlockPos(5, 2, 5),
                new BlockPos(4, 2, 5), new BlockPos(4, 5, 5), new BlockPos(5, 4, 5), new BlockPos(5, 4, 4),
                new BlockPos(6, 3, 5), new BlockPos(6, 3, 4)}) {
            m.remove(gone);
        }
        m.put(new BlockPos(5, 1, 5), logX);           // fallen timbers
        m.put(new BlockPos(5, 1, 4), logX);
        m.put(new BlockPos(6, 1, 5), log);
        for (final BlockPos drift : new BlockPos[]{
                new BlockPos(4, 1, 4), new BlockPos(3, 1, 4), new BlockPos(4, 1, 3), new BlockPos(2, 1, 4)}) {
            m.put(drift, Blocks.SNOW.defaultBlockState());  // snow blown in through the breach
        }

        // Interior kit (centre clear for the spawned stray): a snowy-village chest, a barrel, a crafting table, cobwebs.
        m.put(new BlockPos(5, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 1), StructureParts.lootChest("minecraft:chests/village/village_snowy_house"));
        m.put(new BlockPos(1, 1, 2), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(1, 2, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(5, 2, 2), Blocks.COBWEB.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:spruce_planks");
        return new Built(m, bes);
    }

    /**
     * The <b>Fossil Dig</b> (B12) — a half-excavated paleontology site, <em>not a box</em>: a sandy patch (with exposed
     * sandstone bedrock) where a fossil skeleton lies part-unearthed — a broken spine with ribs arcing up and a skull —
     * beside brushable suspicious sand/gravel (archaeology finds). A scaffold tower with a lantern, a short scaffold
     * walkway, a mason's tools chest, a fallen support beam and dead bushes make the excavation clutter. All vanilla.
     * Fits the arid themes (Desert / Badlands); the theme's {@code mobs} pack (a husk) lurks.
     */
    private static Built fossilDig() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState boneX = Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        final BlockState boneZ = Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
        final int xMax = 6, zMax = 4;

        // Sandy dig floor with a little exposed sandstone bedrock.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), (x * 3 + z) % 5 == 0
                        ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState());
            }
        }
        // The fossil: a broken spine (a gap at x=3 where the centre mob spawns), grounded rib pairs arcing up, a skull.
        for (final int sx : new int[]{1, 2, 4, 5}) {
            m.put(new BlockPos(sx, 1, 2), boneX);
        }
        for (final BlockPos rib : new BlockPos[]{
                new BlockPos(2, 1, 1), new BlockPos(2, 2, 1), new BlockPos(2, 3, 1),
                new BlockPos(2, 1, 3), new BlockPos(2, 2, 3), new BlockPos(2, 3, 3),
                new BlockPos(4, 1, 1), new BlockPos(4, 2, 1), new BlockPos(4, 1, 3), new BlockPos(4, 2, 3)}) {
            m.put(rib, boneZ);
        }
        m.put(new BlockPos(5, 2, 2), boneX); // skull, on the last vertebra

        // Brushable finds (archaeology) sunk in the sand.
        m.put(new BlockPos(1, 1, 1), Blocks.SUSPICIOUS_SAND.defaultBlockState());
        bes.put(new BlockPos(1, 1, 1), StructureParts.suspicious("minecraft:archaeology/desert_pyramid"));
        m.put(new BlockPos(3, 1, 3), Blocks.SUSPICIOUS_SAND.defaultBlockState());
        bes.put(new BlockPos(3, 1, 3), StructureParts.suspicious("minecraft:archaeology/desert_well"));
        m.put(new BlockPos(5, 1, 3), Blocks.SUSPICIOUS_GRAVEL.defaultBlockState());
        bes.put(new BlockPos(5, 1, 3), StructureParts.suspicious("minecraft:archaeology/desert_well"));

        // The dig frame: a scaffold tower + lantern, a short scaffold walkway, a mason's chest, a fallen beam, dead bushes.
        for (int y = 1; y <= 3; y++) {
            m.put(new BlockPos(6, y, 0), Blocks.SCAFFOLDING.defaultBlockState());
        }
        m.put(new BlockPos(6, 4, 0), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(5, 1, 0), Blocks.SCAFFOLDING.defaultBlockState());
        m.put(new BlockPos(4, 1, 0), Blocks.SCAFFOLDING.defaultBlockState());
        m.put(new BlockPos(0, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(0, 1, 2), StructureParts.lootChest("minecraft:chests/village/village_mason"));
        m.put(new BlockPos(1, 1, 4), Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(2, 1, 4), Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(0, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());
        m.put(new BlockPos(6, 1, 4), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:sand");
        return new Built(m, bes);
    }

    /**
     * The <b>Overgrown Well</b> (B5) — a mossy old draw-well, <em>not a box</em>: a raised two-course cobble curb
     * holding a pool of water, roofed by a stripped-log beam and slab canopy on two posts, with a chain winch hanging
     * down the shaft. Moss creeps over the curb, cobwebs hang from the canopy, and a traveller's chest sits on a worn
     * cobble step beside it. Self-contained (it brings its own walled water). All vanilla. Fits the damp themes
     * (Lush / Mushroom); the theme's {@code mobs} pack (a drowned or two) haunts the shaft.
     */
    private static Built overgrownWell() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        // Mossy-cobble apron under the curb + a worn cobble approach step on each side.
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 0, z), pavers(x, z));
            }
        }
        for (final BlockPos step : new BlockPos[]{
                new BlockPos(0, 0, 2), new BlockPos(4, 0, 2), new BlockPos(2, 0, 0), new BlockPos(2, 0, 4)}) {
            m.put(step, Blocks.COBBLESTONE.defaultBlockState());
        }
        // The raised two-course curb, its water pooled inside and walled on every side.
        for (int y = 1; y <= 2; y++) {
            for (int x = 1; x <= 3; x++) {
                for (int z = 1; z <= 3; z++) {
                    if (x == 2 && z == 2) {
                        continue; // the shaft
                    }
                    m.put(new BlockPos(x, y, z), (x + z + y) % 3 == 0
                            ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState());
                }
            }
        }
        m.put(new BlockPos(2, 1, 2), Blocks.WATER.defaultBlockState());
        m.put(new BlockPos(2, 2, 2), Blocks.WATER.defaultBlockState());
        // Two posts hold a stripped-log beam + slab canopy; a chain winch hangs down over the water.
        for (final int px : new int[]{1, 3}) {
            m.put(new BlockPos(px, 3, 2), Blocks.OAK_FENCE.defaultBlockState());
            m.put(new BlockPos(px, 4, 2), Blocks.OAK_FENCE.defaultBlockState());
        }
        for (int x = 1; x <= 3; x++) {
            m.put(new BlockPos(x, 5, 2), Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
            m.put(new BlockPos(x, 5, 1), Blocks.OAK_SLAB.defaultBlockState());
            m.put(new BlockPos(x, 5, 3), Blocks.OAK_SLAB.defaultBlockState());
        }
        m.put(new BlockPos(2, 4, 2), Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true)); // a winch lantern over the shaft

        // Overgrowth, a couple of cobwebs, moss on the approach, and a traveller's chest on the east step.
        m.put(new BlockPos(1, 3, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(3, 3, 3), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(2, 1, 0), Blocks.MOSS_CARPET.defaultBlockState());
        m.put(new BlockPos(2, 1, 4), Blocks.MOSS_CARPET.defaultBlockState());
        m.put(new BlockPos(4, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 2), StructureParts.lootChest("minecraft:chests/village/village_plains_house"));

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:cobblestone");
        return new Built(m, bes);
    }

    /**
     * The <b>Beached Fishing Camp</b> (B7) — a fisher's spot gone quiet, <em>not a box</em>: a small spruce rowboat
     * hauled up on a sandy-and-gravel beach (raked gunwale stairs, a prow post, a thwart seat), with a dead campfire
     * ringed by log seats, a lantern post, a bale of dried kelp, a decorative barrel and the fisher's chest. Nothing
     * encloses it — all open, scattered clutter. All vanilla. Fits Aquatic; the theme's {@code mobs} pack (a drowned)
     * washed up beside it.
     */
    private static Built fishingCamp() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.SPRUCE_PLANKS.defaultBlockState();
        final int xMax = 6, zMax = 4;

        // A sandy beach with a little gravel washed up.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), (x * 5 + z) % 4 == 0
                        ? Blocks.GRAVEL.defaultBlockState() : Blocks.SAND.defaultBlockState());
            }
        }
        // The beached rowboat: keel along X (bow at x=1, stern x=5), gunwale stairs raked inward, a prow post + a seat.
        for (int x = 1; x <= 5; x++) {
            m.put(new BlockPos(x, 1, 2), plank);
        }
        for (int x = 2; x <= 4; x++) {
            m.put(new BlockPos(x, 1, 1), Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
            m.put(new BlockPos(x, 1, 3), Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        }
        m.put(new BlockPos(1, 2, 2), Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState()); // prow post
        m.put(new BlockPos(3, 2, 2), Blocks.SPRUCE_SLAB.defaultBlockState());          // a thwart seat

        // The camp clutter: the fisher's chest + a decorative barrel, a dead campfire ringed by log-stump seats, a
        // lantern post, a bale of dried kelp and a washed-up dead bush.
        m.put(new BlockPos(0, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(0, 1, 2), StructureParts.lootChest("minecraft:chests/village/village_fisher"));
        m.put(new BlockPos(0, 1, 3), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(5, 1, 0), Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(4, 1, 0), Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState());
        m.put(new BlockPos(6, 1, 0), Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState());
        m.put(new BlockPos(6, 1, 4), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(6, 2, 4), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(6, 3, 4), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(1, 1, 4), Blocks.DRIED_KELP_BLOCK.defaultBlockState());
        m.put(new BlockPos(0, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:sand");
        return new Built(m, bes);
    }

    /**
     * The <b>Ruined Farmstead</b> (B3) — a homestead's plot gone back to the wild, <em>not a box</em>: a broken
     * split-rail fence pen (gaps where the rails have fallen), overgrown crop rows (young wheat on farmland, dead
     * coarse-dirt patches), a leaning scarecrow (a carved-pumpkin head on a fence-post body with fence arms), a
     * toppled hay-bale stack and a stores chest. All open, all irregular. All vanilla. Fits Meadow; the theme's
     * {@code mobs} pack (a lone surviving cow) grazes it.
     */
    private static Built ruinedFarmstead() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        // A broken split-rail pen: front (z=0) with gaps at x=2,4; the west side (x=0) with a gap at z=3.
        for (final int fx : new int[]{0, 1, 3, 5, 6}) {
            m.put(new BlockPos(fx, 1, 0), fence);
        }
        for (final int fz : new int[]{1, 2, 4}) {
            m.put(new BlockPos(0, 1, fz), fence);
        }
        // Overgrown crop rows: farmland with young wheat, dead coarse-dirt patches worked back in.
        for (int x = 1; x <= 5; x++) {
            for (int z = 2; z <= 4; z++) {
                if (Math.floorMod(x * 3 + z, 4) == 0) {
                    m.put(new BlockPos(x, 0, z), Blocks.COARSE_DIRT.defaultBlockState());
                } else {
                    m.put(new BlockPos(x, 0, z), Blocks.FARMLAND.defaultBlockState());
                    if (Math.floorMod(x * 3 + z, 4) == 1) {
                        m.put(new BlockPos(x, 1, z), Blocks.WHEAT.defaultBlockState());
                    }
                }
            }
        }
        // A leaning scarecrow (front-centre), a toppled hay stack, and the stores chest.
        m.put(new BlockPos(3, 1, 5), fence);
        m.put(new BlockPos(3, 2, 5), fence);
        m.put(new BlockPos(2, 2, 5), fence); // arm
        m.put(new BlockPos(4, 2, 5), fence); // arm
        m.put(new BlockPos(3, 3, 5), Blocks.CARVED_PUMPKIN.defaultBlockState());
        m.put(new BlockPos(6, 1, 4), Blocks.HAY_BLOCK.defaultBlockState());
        m.put(new BlockPos(6, 1, 3), Blocks.HAY_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(6, 2, 4), Blocks.HAY_BLOCK.defaultBlockState());
        m.put(new BlockPos(1, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(1, 1, 1), StructureParts.lootChest("minecraft:chests/village/village_plains_house"));
        m.put(new BlockPos(2, 1, 1), Blocks.BARREL.defaultBlockState());

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:farmland");
        return new Built(m, bes);
    }

    /**
     * The <b>Hunter's Blind</b> (B4) — an abandoned hunting stand, <em>not a box</em>: a raised plank platform up on
     * four oak-log legs (the whole thing elevated is the anti-box), reached by a ladder up one leg, ringed by a fence
     * railing with an open access gap, part-shaded by a slab lean-to canopy. A fletcher's chest sits on the deck; a
     * cold campfire and a barrel wait on the ground below. All vanilla. Fits Forest; the theme's {@code mobs} pack (a
     * skeleton) keeps the stand.
     */
    private static Built huntersBlind() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState fence = Blocks.OAK_FENCE.defaultBlockState();

        // Four log legs (3 tall) + a 3×3 plank deck at y4.
        for (final int[] leg : new int[][]{{1, 1}, {3, 1}, {1, 3}, {3, 3}}) {
            for (int y = 1; y <= 3; y++) {
                m.put(new BlockPos(leg[0], y, leg[1]), log);
            }
        }
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 4, z), Blocks.OAK_PLANKS.defaultBlockState());
            }
        }
        // Fence railing around the deck (y5), with the front-left corner left open for the ladder access.
        for (final int[] r : new int[][]{{2, 1}, {3, 1}, {1, 2}, {3, 2}, {1, 3}, {2, 3}, {3, 3}}) {
            m.put(new BlockPos(r[0], 5, r[1]), fence);
        }
        // A ladder up the west face of the front-left leg.
        for (int y = 1; y <= 4; y++) {
            m.put(new BlockPos(0, y, 1), Blocks.LADDER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        }
        // A slab lean-to canopy shading the back of the deck (held on the back railing).
        for (int x = 1; x <= 3; x++) {
            m.put(new BlockPos(x, 6, 3), Blocks.OAK_SLAB.defaultBlockState());
        }
        // The fletcher's chest on the deck; a cold campfire + a barrel on the ground below.
        m.put(new BlockPos(2, 5, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        bes.put(new BlockPos(2, 5, 2), StructureParts.lootChest("minecraft:chests/village/village_fletcher"));
        m.put(new BlockPos(2, 1, 2), Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(1, 1, 2), Blocks.BARREL.defaultBlockState());

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:grass_block");
        return new Built(m, bes);
    }

    /**
     * The <b>Collapsed Mine Head</b> (B11) — a played-out mine's entrance, <em>not a box</em>: a timbered adit mouth
     * (oak-log frame + lintel over a dark deepslate opening) set into the ground, a stub of rail running out of it to
     * a derelict chest-minecart, spoil heaps of cobble/coal/gravel tumbled to the sides, and a lantern on a support
     * post. All open, irregular. All vanilla. Fits the stone themes (Rocky); the theme's {@code mobs} pack (a cave
     * spider) nests in the shaft.
     */
    private static Built mineHead() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final Map<BlockPos, CompoundTag> entities = new HashMap<>();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();

        // The timbered adit mouth at the back (z=4): two log posts + a lintel over a dark deepslate opening.
        m.put(new BlockPos(2, 1, 4), log);
        m.put(new BlockPos(2, 2, 4), log);
        m.put(new BlockPos(4, 1, 4), log);
        m.put(new BlockPos(4, 2, 4), log);
        for (int x = 2; x <= 4; x++) {
            m.put(new BlockPos(x, 3, 4), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X)); // lintel
        }
        m.put(new BlockPos(3, 1, 4), Blocks.DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(3, 2, 4), Blocks.DEEPSLATE.defaultBlockState()); // the dark tunnel, going nowhere
        m.put(new BlockPos(3, 3, 4), Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true));

        // A rail stub running out of the mouth (along Z, the default shape) to a derelict chest-minecart.
        for (int z = 0; z <= 3; z++) {
            m.put(new BlockPos(3, 0, z), Blocks.RAIL.defaultBlockState());
        }
        m.put(new BlockPos(3, 0, 4), cobble); // the rail bed at the mouth
        entities.put(new BlockPos(3, 0, 1), StructureParts.chestMinecart("minecraft:chests/abandoned_mineshaft"));

        // Spoil heaps tumbled to the sides: cobble, a chunk of coal ore, gravel.
        m.put(new BlockPos(1, 1, 3), cobble);
        m.put(new BlockPos(1, 1, 2), Blocks.COAL_ORE.defaultBlockState());
        m.put(new BlockPos(2, 1, 2), Blocks.GRAVEL.defaultBlockState());
        m.put(new BlockPos(5, 1, 3), Blocks.GRAVEL.defaultBlockState());
        m.put(new BlockPos(5, 1, 2), cobble);
        m.put(new BlockPos(1, 1, 0), Blocks.COBWEB.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 4), "minecraft:cobblestone");
        return new Built(m, bes, entities);
    }

    /**
     * The <b>Prospector's Camp</b> (B8) — a lone digger's camp gone cold, <em>not a box</em>: a bedroll (a wool
     * sleeping bag + pillow), a dead campfire ringed with cobble and log-stump seats, a spoil heap of cobble, a
     * coal-ore chunk and gravel, a lantern on a fence post, a tools chest, a stores barrel and a couple of dead
     * bushes. All open, all scattered. All vanilla. Fits the arid themes (Desert); the theme's {@code mobs} pack (a
     * husk) lurks.
     */
    private static Built prospectorsCamp() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        final BlockState stump = Blocks.STRIPPED_OAK_LOG.defaultBlockState();

        // A bedroll (a light-blue sleeping bag + white pillow) tucked to one side.
        m.put(new BlockPos(1, 1, 1), Blocks.LIGHT_BLUE_WOOL.defaultBlockState());
        m.put(new BlockPos(2, 1, 1), Blocks.LIGHT_BLUE_WOOL.defaultBlockState());
        m.put(new BlockPos(0, 1, 1), Blocks.WHITE_WOOL.defaultBlockState());
        // A cold campfire ringed with cobble + log-stump seats (centre kept clear for the husk).
        m.put(new BlockPos(4, 1, 2), Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(4, 1, 1), cobble);
        m.put(new BlockPos(4, 1, 3), cobble);
        m.put(new BlockPos(5, 1, 2), stump);
        m.put(new BlockPos(4, 1, 0), stump);
        // A prospector's spoil heap: cobble, a coal-ore chunk, gravel.
        m.put(new BlockPos(6, 1, 0), cobble);
        m.put(new BlockPos(6, 2, 0), cobble);
        m.put(new BlockPos(6, 1, 1), Blocks.COAL_ORE.defaultBlockState());
        m.put(new BlockPos(5, 1, 0), Blocks.GRAVEL.defaultBlockState());
        // A lantern post, the tools chest, a stores barrel, dead bushes.
        m.put(new BlockPos(0, 1, 4), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(0, 2, 4), Blocks.OAK_FENCE.defaultBlockState());
        m.put(new BlockPos(0, 3, 4), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(0, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(0, 1, 3), StructureParts.lootChest("minecraft:chests/village/village_mason"));
        m.put(new BlockPos(6, 1, 4), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(3, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());
        m.put(new BlockPos(6, 1, 3), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:sand");
        return new Built(m, bes);
    }

    /**
     * The <b>Bandit Camp</b> (B9) — an outlaws' hideout, <em>not a box</em>: two wool bedrolls, a cold campfire ringed
     * with cobble and log seats, a crude banner (black-and-red wool on a fence pole), the picked-clean bones of a past
     * victim strung with cobwebs, a stores barrel, and a spoils chest of pillager-outpost loot. All open, all menace,
     * no walls. All vanilla. Fits the arid/rugged themes (Badlands); the theme's {@code mobs} pack (a pair of
     * pillagers) holds it.
     */
    private static Built banditCamp() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        final BlockState stump = Blocks.STRIPPED_OAK_LOG.defaultBlockState();

        // Two bedrolls at opposite corners.
        m.put(new BlockPos(0, 1, 0), Blocks.RED_WOOL.defaultBlockState());
        m.put(new BlockPos(1, 1, 0), Blocks.RED_WOOL.defaultBlockState());
        m.put(new BlockPos(5, 1, 4), Blocks.GREEN_WOOL.defaultBlockState());
        m.put(new BlockPos(6, 1, 4), Blocks.GREEN_WOOL.defaultBlockState());
        // A cold campfire ringed with cobble + log seats (centre clear for the pillagers).
        m.put(new BlockPos(3, 1, 1), Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(2, 1, 1), cobble);
        m.put(new BlockPos(4, 1, 1), cobble);
        m.put(new BlockPos(2, 1, 0), stump);
        m.put(new BlockPos(4, 1, 0), stump);
        // A crude banner on a fence pole.
        for (int y = 1; y <= 3; y++) {
            m.put(new BlockPos(5, y, 0), Blocks.OAK_FENCE.defaultBlockState());
        }
        m.put(new BlockPos(5, 4, 0), Blocks.BLACK_WOOL.defaultBlockState());
        m.put(new BlockPos(4, 4, 0), Blocks.RED_WOOL.defaultBlockState());
        // The picked-clean bones of a past victim, strung with cobwebs.
        m.put(new BlockPos(1, 1, 4), Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(2, 1, 4), Blocks.BONE_BLOCK.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        m.put(new BlockPos(2, 2, 4), Blocks.COBWEB.defaultBlockState());
        // A stores barrel + the spoils chest.
        m.put(new BlockPos(0, 1, 4), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(6, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(6, 1, 2), StructureParts.lootChest("minecraft:chests/pillager_outpost"));

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:coarse_dirt");
        return new Built(m, bes);
    }

    /** A broken stone-brick column of {@code height} rising from the apron at {@code (x, z)}: plain shaft, a weathered
     *  (cracked/mossy) broken top. */
    private static void column(Map<BlockPos, BlockState> m, int x, int z, int height) {
        for (int y = 1; y <= height; y++) {
            m.put(new BlockPos(x, y, z), y == height ? weatheredBrick(x + y, z) : Blocks.STONE_BRICKS.defaultBlockState());
        }
    }

    /** A lit candle (a single taper) for a shrine. */
    private static BlockState litCandle() {
        return Blocks.CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    }

    /** A deterministic mossy-cobble / cobble paver mix for a worn apron. */
    private static BlockState pavers(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 5, 3)) {
            case 0 -> Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            case 1 -> Blocks.COBBLESTONE_SLAB.defaultBlockState();
            default -> Blocks.COBBLESTONE.defaultBlockState();
        };
    }

    /** A deterministic weathered stone-brick mix (plain / mossy / cracked) for a ruined look. */
    private static BlockState weatheredBrick(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 5)) {
            case 0 -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            case 1 -> Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }
}
