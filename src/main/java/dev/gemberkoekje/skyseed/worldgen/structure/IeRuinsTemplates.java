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
 * The <b>Band 2</b> Immersive Engineering derelicts of {@code VARIETYSTRUCTUREPLAN.md} §3 (B17–B18) — single-mod "rare"
 * surprise buildings that appear on an ordinary island only when <b>Immersive Engineering</b> is installed: a
 * dilapidated factory and a fallen powerline pylon. Like the Create batch ({@link CreateRuinsTemplates}), the modded
 * machinery is authored with the {@code modNames} side-map (a vanilla analog carries the property serialisation; the
 * emitted {@code .nbt} palette {@code Name} is swapped to the real {@code immersiveengineering:} id — see
 * {@link StructureWriter}). Gated {@code requires: ["immersiveengineering"]} so it is filtered before any RNG and its
 * modded {@code .nbt} never parses without IE.
 *
 * <p><b>Testable vanilla shell.</b> IE builds are mostly mod blocks, which resolve to AIR without IE (the gametest
 * classpath). So each build deliberately carries a <em>vanilla</em> ruin shell — cobblestone rubble/footings, a lantern,
 * cobwebs, the loot chest — both as derelict clutter and as the anchors the assembly gametests assert on. Loot is the
 * inert-safe {@code skyseed:chests/ie_scrap} vanilla table + an {@code add_drop} GLM layering a token IE scrap
 * (component/plate/wire) — reward-bearing (D3) yet gate-safe (D4: never a working multiblock or high-tier blueprint).
 *
 * <p><b>Verified IE ids (immersiveengineering 12.4.2).</b> Propertyless full cubes ({@code concrete*},
 * {@code sheetmetal_*}, {@code storage_*}, {@code crate}, {@code reinforced_crate}, {@code treated_wood_*},
 * {@code alu_post}, {@code alu_scaffolding_standard}, {@code capacitor_hv}, {@code metal_barrel}) → a {@code STONE}
 * analog; {@code alu_fence}/{@code treated_fence} → an {@code OAK_FENCE} (vanilla {@code FenceBlock}) analog; the six-way
 * {@code facing} of {@code connector_hv} → {@code END_ROD}; the horizontal {@code facing} of {@code post_transformer} /
 * {@code razor_wire} → {@code CARVED_PUMPKIN}.
 */
public final class IeRuinsTemplates {
    private IeRuinsTemplates() {}

    private static final String IE = "immersiveengineering:";
    private static final String SCRAP = "skyseed:chests/ie_scrap";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("ie_factory/factory.nbt"), factory());
        writeIfAbsent(base.resolve("powerline/pylon.nbt"), powerline());
    }

    /**
     * The <b>Dilapidated IE Factory</b> (B17) — an industrial hall gone to rust, <em>not a box</em>: a concrete-slab
     * floor on four <em>uneven, part-collapsed</em> concrete pillars, treated-wood + sheetmetal walls breached in
     * several places, a sheetmetal roof caved in over one bay. Inside, a crusher/kiln <em>husk</em> (reinforced concrete
     * + a capacitor + a barrel), spilled crates, and a scrap chest; an external HV wire-post juts up past the roofline
     * (the anti-box silhouette). Cobblestone rubble, a lantern and cobwebs are the vanilla ruin clutter. Fits Rocky /
     * Badlands; the theme's {@code mobs} pack (a pair of zombies) shambles the floor.
     */
    private static Built factory() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 6, zMax = 5;

        // Concrete-slab floor (a plain/tile mix).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                set(m, mods, new BlockPos(x, 0, z), cube(), IE + ((x + z) % 3 == 0 ? "concrete_tile" : "concrete"));
            }
        }
        // Four concrete pillars at the corners, deliberately uneven — one tall, one snapped short (ruin).
        pillar(m, mods, 0, 0, 4);
        pillar(m, mods, xMax, 0, 3);
        pillar(m, mods, 0, zMax, 4);
        pillar(m, mods, xMax, zMax, 2);

        // Perimeter walls (treated-wood + sheetmetal), two courses, breached — a doorway at the front centre + gaps.
        for (int x = 1; x <= xMax - 1; x++) {
            wall(m, mods, x, 0, x == 3);           // front (z0): gap/doorway at x=3
            wall(m, mods, x, zMax, x == 4);        // back (z5): a breach at x=4
        }
        for (int z = 1; z <= zMax - 1; z++) {
            wall(m, mods, 0, z, z == 2);           // west (x0): a breach at z=2
            wall(m, mods, xMax, z, z == 3 || z == 4); // east (x6): torn open
        }
        // A sheetmetal roof over the front bay, with a caved-in patch (ruin).
        for (int x = 1; x <= xMax - 1; x++) {
            for (int z = 1; z <= 3; z++) {
                if (!((x == 2 || x == 3) && z == 2)) { // the caved-in hole
                    set(m, mods, new BlockPos(x, 3, z), cube(), IE + "sheetmetal_steel");
                }
            }
        }

        // The crusher/kiln HUSK in the back-east bay: reinforced concrete + a capacitor + a barrel + a treated frame.
        set(m, mods, new BlockPos(5, 1, 4), cube(), IE + "concrete_reinforced");
        set(m, mods, new BlockPos(5, 2, 4), cube(), IE + "sheetmetal_iron");
        set(m, mods, new BlockPos(4, 1, 4), cube(), IE + "capacitor_hv");
        set(m, mods, new BlockPos(5, 1, 3), cube(), IE + "metal_barrel");
        set(m, mods, new BlockPos(4, 2, 4), cube(), IE + "treated_wood_horizontal");

        // Spilled crates by the west wall.
        set(m, mods, new BlockPos(1, 1, 1), cube(), IE + "crate");
        set(m, mods, new BlockPos(1, 1, 2), cube(), IE + "reinforced_crate");
        set(m, mods, new BlockPos(1, 2, 1), cube(), IE + "crate");
        set(m, mods, new BlockPos(2, 1, 1), cube(), IE + "crate");

        // The external HV wire-post off the east wall — an alu post topped with an HV connector, jutting past the roof.
        set(m, mods, new BlockPos(6, 1, 2), cube(), IE + "alu_post");
        set(m, mods, new BlockPos(6, 2, 2), cube(), IE + "alu_post");
        set(m, mods, new BlockPos(6, 3, 2), cube(), IE + "alu_post");
        set(m, mods, new BlockPos(6, 4, 2), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), IE + "connector_hv"); // DOWN = mounted on the block below, dish up (facing=up would flip it upside-down)
        // A treated-fence railing along part of the front (breaks the wall line further).
        set(m, mods, new BlockPos(4, 1, 0), Blocks.OAK_FENCE.defaultBlockState(), IE + "treated_fence");

        // Vanilla ruin clutter (+ the gametest's assertable shell): cobblestone rubble, a floor lantern, cobwebs.
        m.put(new BlockPos(5, 1, 1), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(5, 2, 1), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(2, 1, 4), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(2, 1, 3), Blocks.LANTERN.defaultBlockState());
        // (0,2,1) is a west-wall top cell that wall() gave an IE id — use set(...,null) so this vanilla cobweb clears
        // the stale mod id (a plain m.put would leave it, emitting the cobweb as sheetmetal_iron).
        set(m, mods, new BlockPos(0, 2, 1), Blocks.COBWEB.defaultBlockState(), null);
        m.put(new BlockPos(5, 2, 3), Blocks.COBWEB.defaultBlockState());

        // The scrap chest on open interior floor (air above at (4,2,1) so it opens).
        m.put(new BlockPos(4, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 1), StructureParts.lootChest(SCRAP));

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:cobblestone");
        return built(m, bes, mods);
    }

    /**
     * The <b>Fallen Powerline</b> (B18) — a toppled transmission pylon, <em>not a box</em> by nature: a lattice
     * alu-scaffolding mast on a cobble footing, cross-arms of alu-fence hung with HV connectors + razor wire, a
     * transformer husk at its foot — and a whole span <em>snapped off and lying across the ground</em> (a run of
     * scaffolding, a toppled capacitor "transformer", scattered connectors + wire). A warning lantern, cobblestone
     * rubble and cobwebs are the vanilla clutter; the scrap chest holds wire/redstone. Fits any open theme (Meadow /
     * Rocky); the theme's {@code mobs} pack (a creeper) lurks in the wreck.
     */
    private static Built powerline() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        // Cobble footings under the mast (vanilla base).
        for (final int[] c : new int[][]{{1, 1}, {2, 1}, {3, 1}}) {
            m.put(new BlockPos(c[0], 0, c[1]), Blocks.COBBLESTONE.defaultBlockState());
        }
        // The lattice mast: alu-scaffolding stacked five tall at (2, *, 1).
        for (int y = 1; y <= 5; y++) {
            set(m, mods, new BlockPos(2, y, 1), cube(), IE + "alu_scaffolding_standard");
        }
        // Two cross-arms of alu-fence: a lower arm (y3) and a wide upper arm (y5), the mast top splitting the upper arm.
        for (final int[] a : new int[][]{{1, 3}, {3, 3}}) {
            set(m, mods, new BlockPos(a[0], a[1], 1), Blocks.OAK_FENCE.defaultBlockState(), IE + "alu_fence");
        }
        for (final int ax : new int[]{0, 1, 3, 4}) {
            set(m, mods, new BlockPos(ax, 5, 1), Blocks.OAK_FENCE.defaultBlockState(), IE + "alu_fence");
        }
        // HV connectors (the insulators) on the arm tips + razor wire draped over the top.
        set(m, mods, new BlockPos(0, 6, 1), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), IE + "connector_hv"); // DOWN = mounted on the block below, dish up (facing=up would flip it upside-down)
        set(m, mods, new BlockPos(4, 6, 1), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), IE + "connector_hv"); // DOWN = mounted on the block below, dish up (facing=up would flip it upside-down)
        set(m, mods, new BlockPos(1, 6, 1), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), IE + "razor_wire");
        set(m, mods, new BlockPos(3, 6, 1), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), IE + "razor_wire");
        // The transformer at the mast foot — a post-transformer husk + a capacitor.
        set(m, mods, new BlockPos(3, 1, 1), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), IE + "post_transformer");
        set(m, mods, new BlockPos(1, 1, 1), cube(), IE + "capacitor_hv");

        // THE FALLEN SPAN, snapped off and lying across the ground toward z4: a run of scaffolding, a toppled
        // capacitor "transformer", scattered connectors + razor wire.
        set(m, mods, new BlockPos(2, 1, 3), cube(), IE + "alu_scaffolding_standard");
        set(m, mods, new BlockPos(2, 1, 4), cube(), IE + "alu_scaffolding_standard");
        set(m, mods, new BlockPos(1, 1, 4), cube(), IE + "capacitor_hv"); // the toppled transformer
        set(m, mods, new BlockPos(1, 1, 3), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), IE + "connector_hv"); // DOWN = mounted on the block below, dish up (facing=up would flip it upside-down)
        set(m, mods, new BlockPos(3, 1, 4), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), IE + "razor_wire");

        // Vanilla ruin clutter (+ the gametest's assertable shell): a warning lantern atop the mast, cobblestone rubble,
        // cobwebs, and the wire/redstone scrap chest on open ground (air above at (4,2,4)).
        m.put(new BlockPos(2, 6, 1), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(0, 1, 4), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(0, 1, 3), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(2, 2, 4), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(4, 1, 4), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 4), StructureParts.lootChest(SCRAP));

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:cobblestone");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers (mirror of CreateRuinsTemplates).
    // ------------------------------------------------------------------------------------------------------------

    /** A concrete pillar of {@code height} at {@code (x, z)} — its base capital a plain concrete cube, the shaft
     *  {@code concrete_pillar}. */
    private static void pillar(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, int height) {
        for (int y = 1; y <= height; y++) {
            set(m, mods, new BlockPos(x, y, z), cube(), IE + (y == 1 ? "concrete" : "concrete_pillar"));
        }
    }

    /** A two-course perimeter wall cell at {@code (x, z)} — treated-wood over sheetmetal — unless {@code breach} (a gap
     *  in the wall, ruin). */
    private static void wall(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, boolean breach) {
        if (breach) {
            return;
        }
        set(m, mods, new BlockPos(x, 1, z), cube(), IE + "sheetmetal_steel");
        set(m, mods, new BlockPos(x, 2, z), cube(), IE + ((x + z) % 2 == 0 ? "treated_wood_horizontal" : "sheetmetal_iron"));
    }

    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p);
        }
    }

    /** A propertyless full-cube analog (for IE's concrete/sheetmetal/storage/crate/post blocks — no properties). */
    private static BlockState cube() {
        return Blocks.STONE.defaultBlockState();
    }

    /** Finish a build: a jigsaw cell must never carry a mod palette name, so strip mod ids from every jigsaw cell. */
    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
