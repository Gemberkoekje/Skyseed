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
 * The <b>Band 2</b> Create derelicts of {@code VARIETYSTRUCTUREPLAN.md} §3 — the single-mod "rare" surprise buildings
 * that appear on an ordinary island only when <b>Create</b> is installed: a broken windmill, a derelict watermill, an
 * abandoned train shed, a rusted drill rig. Each is a small no-box single-piece jigsaw (see [[skyseed-no-boxes]]) whose
 * <em>machinery</em> is authored with the {@code modNames} side-map: a vanilla <b>analog</b> {@link BlockState} carries
 * the shape/property serialisation while the emitted {@code .nbt} palette {@code Name} is swapped to the real
 * {@code create:} id (the same trick the BWG villages use — see {@link BwgVillageTemplates} / {@link StructureWriter}).
 *
 * <p><b>Inert without Create.</b> The theme entry gates each build on {@code requires: ["create"]}
 * ({@link dev.gemberkoekje.skyseed.worldgen.theme.RareStructure}), so it is filtered out before any RNG when Create is
 * absent and its {@code create:}-id {@code .nbt} is never parsed. If it ever were, an unknown block id resolves to AIR,
 * so the assembly gametests assert on the <em>vanilla</em> shell (stone, wool, rail, scaffolding, chests) — the Create
 * blocks vanish without the mod. Loot is a curated vanilla-scrap table ({@code skyseed:chests/create_scrap}) plus an
 * inert-safe {@code add_drop} GLM injecting a token Create item, so the reward is real (D3) but gate-safe (D4).
 *
 * <p><b>Verified Create ids (create 6.0.10).</b> Propertyless full cubes ({@code andesite_casing}, {@code brass_casing},
 * {@code copper_casing}, {@code millstone}) map to a {@code STONE} analog; the six-way {@code facing} of
 * {@code water_wheel} / {@code mechanical_drill} to {@code END_ROD}; the horizontal {@code facing} of
 * {@code mechanical_press} to {@code CARVED_PUMPKIN}; the {@code axis} of {@code shaft} / {@code cogwheel} to a log. A
 * subset of properties is fine — the drill omits {@code waterlogged}, which defaults to false on load.
 */
public final class CreateRuinsTemplates {
    private CreateRuinsTemplates() {}

    private static final String CREATE = "create:";
    private static final String SCRAP = "skyseed:chests/create_scrap";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("windmill/mill.nbt"), windmill());
        writeIfAbsent(base.resolve("watermill/mill.nbt"), watermill());
        writeIfAbsent(base.resolve("train_shed/shed.nbt"), trainShed());
        writeIfAbsent(base.resolve("drill_rig/rig.nbt"), drillRig());
    }

    /**
     * The <b>Broken Windmill</b> (B13) — a grain mill fallen still, <em>not a box</em>: a stone-brick mill house whose
     * open front face carries a great four-armed sail (radiating {@code create:shaft} spokes hung with wool cloth), one
     * arm snapped short so the silhouette reads asymmetric and ruined. A {@code create:millstone} + an
     * {@code andesite_casing} machine sit at the base linked by a {@code cogwheel}, moss and cobwebs creep over it, and a
     * kinetic-scrap chest waits inside. Fits the grass themes (Meadow / Hamlet); the theme's {@code mobs} pack (a zombie)
     * haunts it.
     */
    private static Built windmill() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 4, zMax = 4;

        // A 5×5 stone-brick apron, weathered (mossy/cracked mix), with a little rubble spilling onto it.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), weatheredBrick(x, z));
            }
        }
        // The mill house: a 3-wide × 2-deep tower at the back (z 3..4), stone-brick walls two-and-a-bit courses tall,
        // andesite-casing corner posts, and a broken-open top course (ruin).
        for (int x = 1; x <= 3; x++) {
            for (int z = 3; z <= 4; z++) {
                final boolean corner = (x == 1 || x == 3) && z == 4;
                for (int y = 1; y <= 3; y++) {
                    if (corner) {
                        set(m, mods, new BlockPos(x, y, z), cube(), CREATE + "andesite_casing");
                    } else if (x == 2 && z == 3) {
                        continue; // the open front doorway of the mill house
                    } else {
                        m.put(new BlockPos(x, y, z), weatheredBrick(x + y, z));
                    }
                }
            }
        }
        m.remove(new BlockPos(1, 3, 3)); // caved-in front-top corners of the mill house
        m.remove(new BlockPos(3, 3, 4));

        // The machinery at the mill floor: a millstone + an andesite-casing machine, linked by a horizontal cogwheel.
        set(m, mods, new BlockPos(1, 1, 4), cube(), CREATE + "millstone");
        set(m, mods, new BlockPos(2, 1, 4), shaft(Direction.Axis.X), CREATE + "cogwheel");
        set(m, mods, new BlockPos(3, 1, 4), cube(), CREATE + "andesite_casing");
        m.put(new BlockPos(2, 1, 3), Blocks.COBWEB.defaultBlockState());

        // The kinetic-scrap chest tucked in the mill house, facing out the doorway; the wall above it is caved open so
        // the loot stays reachable (an opaque block on top would jam the chest) and the ruin reads harder.
        m.put(new BlockPos(3, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(3, 1, 3), StructureParts.lootChest(SCRAP));
        m.remove(new BlockPos(3, 2, 3));
        m.remove(new BlockPos(3, 3, 3));

        // The great sail on the mill's front face: a hub of andesite casing at (2,3,2), four shaft arms radiating in the
        // X–Y plane hung with white-wool cloth. The +Y arm is snapped short (one wool, no tip) — the ruin that breaks
        // the box. Arms sit one block proud of the house (z=2), clear of the walls.
        set(m, mods, new BlockPos(2, 3, 2), cube(), CREATE + "andesite_casing"); // sail hub
        // down arm
        set(m, mods, new BlockPos(2, 2, 2), shaft(Direction.Axis.Y), CREATE + "shaft");
        set(m, mods, new BlockPos(2, 1, 2), shaft(Direction.Axis.Y), CREATE + "shaft");
        m.put(new BlockPos(1, 1, 2), Blocks.WHITE_WOOL.defaultBlockState());
        m.put(new BlockPos(3, 1, 2), Blocks.WHITE_WOOL.defaultBlockState());
        // up arm — SNAPPED short (ruin): a single shaft, cloth torn away
        set(m, mods, new BlockPos(2, 4, 2), shaft(Direction.Axis.Y), CREATE + "shaft");
        // west arm
        set(m, mods, new BlockPos(1, 3, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        set(m, mods, new BlockPos(0, 3, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        m.put(new BlockPos(0, 4, 2), Blocks.WHITE_WOOL.defaultBlockState());
        m.put(new BlockPos(0, 2, 2), Blocks.WHITE_WOOL.defaultBlockState());
        // east arm
        set(m, mods, new BlockPos(3, 3, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        set(m, mods, new BlockPos(4, 3, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        m.put(new BlockPos(4, 4, 2), Blocks.WHITE_WOOL.defaultBlockState());
        m.put(new BlockPos(4, 2, 2), Blocks.WHITE_WOOL.defaultBlockState());

        // Yard clutter: a rubble heap + a dead bush breaking the apron edge.
        m.put(new BlockPos(0, 1, 0), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(1, 1, 0), Blocks.ANDESITE.defaultBlockState());
        m.put(new BlockPos(0, 2, 0), Blocks.COBBLESTONE_SLAB.defaultBlockState());
        m.put(new BlockPos(4, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:stone_bricks");
        return built(m, bes, mods);
    }

    /**
     * The <b>Derelict Watermill</b> (B14) — a mill on a dead stream, <em>not a box</em>: a great {@code water_wheel}
     * stands over a self-contained walled water channel (it brings its own water — set {@code suppress_pond}), an axle
     * {@code shaft} runs into a ruined spruce mill housing where a {@code mechanical_press} "doohicky" hangs on the end
     * over an {@code andesite_casing} block. The housing's front and part of its roof have fallen in; moss, cobwebs and a
     * scrap chest sit within. Fits Aquatic; the theme's {@code mobs} pack (a drowned) washes about the channel.
     */
    private static Built watermill() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.SPRUCE_PLANKS.defaultBlockState();
        final int xMax = 6, zMax = 4;

        // A cobblestone apron (7×5).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), (x + z) % 4 == 0
                        ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState());
            }
        }
        // A self-contained walled water channel running along X at z=2 (west end, x 1..3): a cobble tub holding water.
        for (int x = 1; x <= 3; x++) {
            m.put(new BlockPos(x, 1, 2), Blocks.WATER.defaultBlockState());
            m.put(new BlockPos(x, 1, 1), Blocks.COBBLESTONE.defaultBlockState()); // north curb
            m.put(new BlockPos(x, 1, 3), Blocks.COBBLESTONE.defaultBlockState()); // south curb
        }
        m.put(new BlockPos(0, 1, 2), Blocks.MOSSY_COBBLESTONE.defaultBlockState()); // west end wall of the tub
        m.put(new BlockPos(0, 1, 1), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(0, 1, 3), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(4, 1, 2), Blocks.COBBLESTONE.defaultBlockState()); // east end wall — seals the tub so the water can't flow out under the wheel
        m.put(new BlockPos(1, 2, 1), Blocks.COBWEB.defaultBlockState());

        // The water wheel standing over the channel (facing along Z), an axle shaft running east into the mill housing.
        set(m, mods, new BlockPos(3, 2, 2), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.SOUTH), CREATE + "water_wheel");
        set(m, mods, new BlockPos(4, 2, 2), shaft(Direction.Axis.X), CREATE + "shaft");

        // The ruined spruce mill housing at the east end (x 5..6, z 1..3): spruce-plank walls with a caved-in front and
        // a broken roof, an andesite-casing machine bed with a mechanical-press husk hung above it on the end.
        for (int x = 5; x <= 6; x++) {
            for (int z = 1; z <= 3; z++) {
                final boolean perim = x == 6 || z == 1 || z == 3;
                if (perim && !(x == 5 && z == 2)) { // leave the west face (x=5, z=2) open to the axle
                    for (int y = 1; y <= 3; y++) {
                        m.put(new BlockPos(x, y, z), plank);
                    }
                }
            }
        }
        m.remove(new BlockPos(6, 3, 1)); // broken-in roofline corners (ruin)
        m.remove(new BlockPos(5, 3, 3));
        for (int x = 5; x <= 6; x++) {
            for (int z = 1; z <= 3; z++) {
                m.put(new BlockPos(x, 4, z), Blocks.SPRUCE_SLAB.defaultBlockState()); // a low flat roof
            }
        }
        m.remove(new BlockPos(6, 4, 2)); // ... with a hole caved through it
        set(m, mods, new BlockPos(6, 1, 2), cube(), CREATE + "andesite_casing"); // the machine bed
        set(m, mods, new BlockPos(6, 2, 2), Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), CREATE + "mechanical_press"); // the press doohicky
        // The scrap chest on the open west-face threshold (air above at (5,2,2), so it opens) — the plank wall cell
        // (5,1,3) would have jammed it with the wall above.
        m.put(new BlockPos(5, 1, 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 2), StructureParts.lootChest(SCRAP));
        m.put(new BlockPos(6, 2, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(0, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:cobblestone");
        return built(m, bes, mods);
    }

    /**
     * The <b>Abandoned Train Shed</b> (B15) — a half-built locomotive left on a spur, <em>not a box</em>: an open-sided
     * timber shed (four oak-log posts, a gabled oak roof with a caved-in patch — no walls) straddles a stub of vanilla
     * rail, on which sits a half-finished train: {@code shaft} bogies under {@code brass_casing} / {@code copper_casing}
     * car bodies. Brass/copper scrap heaps, cobwebs and a scrap chest fill it out. Fits Rocky; the theme's {@code mobs}
     * pack (a spider) nests in the rafters.
     */
    private static Built trainShed() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final int xMax = 6, zMax = 4;

        // A gravel rail-yard bed (7×5), a little cobble ballast worked through it.
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), (x * 3 + z) % 5 == 0
                        ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.GRAVEL.defaultBlockState());
            }
        }
        // A stub of rail down the centre (along X), the train sitting mid-track.
        for (final int rx : new int[]{0, 1, 4, 5, 6}) {
            m.put(new BlockPos(rx, 1, 2), Blocks.RAIL.defaultBlockState());
        }
        // The half-finished train: shaft bogies at (2,1,2)+(3,1,2), brass/copper casing car bodies above.
        set(m, mods, new BlockPos(2, 1, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        set(m, mods, new BlockPos(3, 1, 2), shaft(Direction.Axis.X), CREATE + "shaft");
        set(m, mods, new BlockPos(2, 2, 2), cube(), CREATE + "brass_casing");
        set(m, mods, new BlockPos(3, 2, 2), cube(), CREATE + "copper_casing");

        // The open-sided shed: four log posts (3 tall) + a gabled oak roof, no walls (anti-box), a caved-in roof patch.
        for (final int[] leg : new int[][]{{1, 1}, {5, 1}, {1, 3}, {5, 3}}) {
            for (int y = 1; y <= 3; y++) {
                m.put(new BlockPos(leg[0], y, leg[1]), log);
            }
        }
        gableRoof(m, 1, 5, 1, 3, 4, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_STAIRS, Blocks.OAK_SLAB, 0);
        m.remove(new BlockPos(2, 5, 1)); // caved-in roof patch (the x=2 roof course sits at y=5, not the y=4 eave)
        m.remove(new BlockPos(2, 5, 2));

        // Brass/copper scrap heaps, cobwebs, and the scrap chest.
        set(m, mods, new BlockPos(1, 1, 0), cube(), CREATE + "brass_casing");
        m.put(new BlockPos(2, 1, 0), Blocks.COPPER_BLOCK.defaultBlockState());
        m.put(new BlockPos(5, 2, 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(1, 2, 3), Blocks.COBWEB.defaultBlockState());
        // The scrap chest on open floor inside the shed (air above at (4,2,3)); the {5,3} corner-post log would have
        // sat on top of it at (5,1,3) and jammed it.
        m.put(new BlockPos(4, 1, 3), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(4, 1, 3), StructureParts.lootChest(SCRAP));
        m.put(new BlockPos(6, 1, 0), Blocks.DEAD_BUSH.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:gravel");
        return built(m, bes, mods);
    }

    /**
     * The <b>Rusted Drill Rig</b> (B16) — a mining contraption seized up over its own pit, <em>not a box</em>: a
     * scaffold derrick (an irregular tower silhouette) rises over a dug shaft, a {@code mechanical_drill} pointing down
     * it, driven by a {@code shaft} axle off an {@code andesite_casing} gearbox; a {@code copper_casing} tank + a
     * cauldron stand in for the ruptured fluid works. Spoil heaps of gravel and coal ore spill to the sides, cobwebs
     * hang, and a scrap chest sits at the rig foot. Fits the arid themes (Desert / Badlands); the {@code mobs} pack (a
     * husk) lurks.
     */
    private static Built drillRig() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 4, zMax = 4;

        // A sandy dig floor (5×5) with a lined shaft dug at the centre (deepslate collar, dark opening).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                if (x == 2 && z == 2) {
                    m.put(new BlockPos(x, 0, z), Blocks.DEEPSLATE.defaultBlockState()); // the drilled shaft, going nowhere
                } else {
                    m.put(new BlockPos(x, 0, z), (x * 3 + z) % 4 == 0
                            ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState());
                }
            }
        }
        for (final int[] c : new int[][]{{1, 2}, {3, 2}, {2, 1}, {2, 3}}) {
            m.put(new BlockPos(c[0], 0, c[1]), Blocks.COBBLED_DEEPSLATE.defaultBlockState()); // the shaft collar
        }

        // The scaffold derrick over the shaft: four scaffolding legs rising 3–4 tall (uneven = ruined), a lantern up top.
        for (int y = 1; y <= 4; y++) {
            m.put(new BlockPos(1, y, 1), Blocks.SCAFFOLDING.defaultBlockState());
            m.put(new BlockPos(3, y, 3), Blocks.SCAFFOLDING.defaultBlockState());
        }
        for (int y = 1; y <= 3; y++) {
            m.put(new BlockPos(3, y, 1), Blocks.SCAFFOLDING.defaultBlockState());
            m.put(new BlockPos(1, y, 3), Blocks.SCAFFOLDING.defaultBlockState());
        }
        m.put(new BlockPos(1, 5, 1), Blocks.LANTERN.defaultBlockState());
        // A cross-beam of scaffolding over the shaft, hanging the drill + gearbox down the middle.
        m.put(new BlockPos(2, 4, 1), Blocks.SCAFFOLDING.defaultBlockState());
        m.put(new BlockPos(2, 4, 3), Blocks.SCAFFOLDING.defaultBlockState());
        set(m, mods, new BlockPos(2, 4, 2), cube(), CREATE + "andesite_casing"); // the gearbox
        set(m, mods, new BlockPos(2, 3, 2), shaft(Direction.Axis.Y), CREATE + "shaft"); // the drive axle
        set(m, mods, new BlockPos(2, 2, 2), Blocks.END_ROD.defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.DOWN), CREATE + "mechanical_drill"); // the drill, pointing down the shaft

        // The ruptured fluid works: a copper-casing tank + a cauldron, off to one side.
        set(m, mods, new BlockPos(4, 1, 1), cube(), CREATE + "copper_casing");
        m.put(new BlockPos(4, 2, 1), Blocks.CAULDRON.defaultBlockState());
        m.put(new BlockPos(4, 1, 2), Blocks.COBWEB.defaultBlockState());

        // Spoil heaps (gravel, coal ore), cobwebs, and the scrap chest at the rig foot.
        m.put(new BlockPos(0, 1, 0), Blocks.GRAVEL.defaultBlockState());
        m.put(new BlockPos(1, 1, 0), Blocks.COAL_ORE.defaultBlockState());
        m.put(new BlockPos(0, 2, 0), Blocks.GRAVEL.defaultBlockState());
        m.put(new BlockPos(4, 1, 4), Blocks.GRAVEL.defaultBlockState());
        m.put(new BlockPos(0, 1, 4), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(0, 1, 4), StructureParts.lootChest(SCRAP));
        m.put(new BlockPos(3, 2, 3), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(0, 1, 2), Blocks.DEAD_BUSH.defaultBlockState());

        // final_state is deepslate (not sand) so the anchor cell stays the dark drilled-shaft mouth inside the collar.
        StructureParts.anchor(m, bes, new BlockPos(2, 0, 2), "minecraft:deepslate");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // modNames-aware placement helpers (mirror of BwgVillageTemplates.set / built).
    // ------------------------------------------------------------------------------------------------------------

    /** Place {@code analog} at {@code p}, recording the {@code create:} palette-{@code Name} override in {@code mods}
     *  (a null id clears any stale override at that cell, so a vanilla overwrite stays vanilla). */
    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p);
        }
    }

    /** A propertyless full-cube analog (for Create's casing/millstone blocks — no blockstate properties to carry). */
    private static BlockState cube() {
        return Blocks.STONE.defaultBlockState();
    }

    /** An {@code axis}-carrying log analog (for Create's {@code shaft} / {@code cogwheel}). */
    private static BlockState shaft(Direction.Axis axis) {
        return Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, axis);
    }

    /** A deterministic weathered stone-brick mix (plain / mossy / cracked) for a ruined look — mirrors CommonRuins. */
    private static BlockState weatheredBrick(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 5)) {
            case 0 -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            case 1 -> Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }

    /** Finish a build: a jigsaw cell must never carry a {@code create:} palette name (else without Create the anchor
     *  isn't a jigsaw and assembly fails), so strip mod ids from every jigsaw cell centrally — as the BWG villages do. */
    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
