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
 * The <b>Band 2</b> Applied Energistics 2 derelict of {@code VARIETYSTRUCTUREPLAN.md} §3 (B19) — a single-mod "rare"
 * surprise building that appears on an ordinary island only when <b>Applied Energistics 2</b> is installed: the
 * <b>Gutted AE2 Lab</b>. Like the Create/IE batches ({@link CreateRuinsTemplates} / {@link IeRuinsTemplates}), the modded
 * blocks are authored with the {@code modNames} side-map (a vanilla analog carries the property serialisation; the
 * emitted {@code .nbt} palette {@code Name} is swapped to the real {@code ae2:} id — see {@link StructureWriter}). Gated
 * {@code requires: ["ae2"]} so it is filtered before any RNG and its modded {@code .nbt} never parses without AE2.
 *
 * <p><b>D4 gate-safety — the sharpest in the catalog.</b> AE2's endgame is gated on <em>sky stone</em> (→ the ME
 * Controller) and the four <em>inscriber presses</em> (→ processors), both effectively meteorite-island-only (see
 * {@code PLANOFPLANS.md} / [[skyseed-ae2-curated-set]]). So this build places <b>NO</b> {@code sky_stone_*} block (harvestable
 * ⇒ sky stone ⇒ a Controller, breaking the gate), <b>NO</b> {@code ae2:controller}, and its loot carries <b>NO</b>
 * press/processor/sky-stone. It is built only from <em>certus-quartz / fluix</em> cubes (mid-game, renewable, not gating)
 * and shows an <em>empty</em> controller pit — the valuable core ripped out — the literal "all the interesting bits are
 * missing" D4 aesthetic. Loot = the inert-safe {@code skyseed:chests/ae2_scrap} vanilla table + {@code add_drop} GLMs
 * layering a token certus/fluix crystal.
 *
 * <p><b>Verified AE2 ids (appliedenergistics2 19.2.17).</b> Propertyless full cubes (single {@code ""} blockstate variant)
 * → a {@code STONE} analog: {@code quartz_block}, {@code chiseled_quartz_block}, {@code cut_quartz_block},
 * {@code quartz_bricks}, {@code fluix_block}; {@code quartz_glass} → a {@code GLASS} analog. The "cables strung across the
 * ceiling" are vanilla {@code END_ROD} conduit stand-ins (AE2's real cable is a {@code cable_bus} multipart, not a plain
 * blockstate). AE2 builds are mostly mod blocks (air without AE2), so a deliberate <b>vanilla ruin shell</b> — cobblestone
 * rubble, a lantern, cobwebs, the scorched-deepslate controller mount, the chest — is the assertable gametest anchor.
 */
public final class AeRuinsTemplates {
    private AeRuinsTemplates() {}

    private static final String AE = "ae2:";
    private static final String SCRAP = "skyseed:chests/ae2_scrap";

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("ae2_lab/lab.nbt"), guttedLab());
    }

    /**
     * The <b>Gutted AE2 Lab</b> (B19) — a networking room stripped for parts, <em>not a box</em>: certus-quartz/fluix
     * walls breached in several places on a quartz floor, a caved-in ceiling over the back bay, conduit/cable stubs still
     * strung under the ceiling, and — the centrepiece — an <em>empty controller pit</em> (a scorched deepslate mount
     * ringed with fluix where the ME Controller was torn out). A few spilled certus-quartz chunks, cobblestone rubble, a
     * lantern, cobwebs and the scrap chest. Fits Rocky / Ancient; the theme's {@code mobs} pack (a zombie) shambles it.
     */
    private static Built guttedLab() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int xMax = 6, zMax = 5;

        // Certus-quartz lab floor (a quartz / cut-quartz mix).
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                set(m, mods, new BlockPos(x, 0, z), cube(), AE + ((x + z) % 3 == 0 ? "cut_quartz_block" : "quartz_block"));
            }
        }
        // The EMPTY CONTROLLER PIT (back-east): a scorched deepslate mount (the ME Controller torn out) with fluix
        // remnants — gate-safe: NO ae2:controller / sky_stone. set(...,null) clears the floor's AE id under the vanilla mount.
        for (final int[] c : new int[][]{{4, 2}, {5, 2}, {4, 3}, {5, 3}}) {
            set(m, mods, new BlockPos(c[0], 0, c[1]), Blocks.DEEPSLATE.defaultBlockState(), null);
        }
        set(m, mods, new BlockPos(6, 0, 2), cube(), AE + "fluix_block");
        set(m, mods, new BlockPos(3, 0, 3), cube(), AE + "fluix_block");

        // Walls (quartz mix), two courses, breached — a doorway at the front centre + gaps; a quartz-glass window.
        for (int x = 1; x <= xMax - 1; x++) {
            wall(m, mods, x, 0, x == 3);       // front (z0): doorway at x=3
            wall(m, mods, x, zMax, x == 4);    // back (z5): a breach at x=4
        }
        for (int z = 1; z <= zMax - 1; z++) {
            wall(m, mods, 0, z, z == 2);       // west (x0): a breach at z=2
            wall(m, mods, xMax, z, z == 3);    // east (x6): a breach at z=3
        }
        set(m, mods, new BlockPos(0, 2, 3), Blocks.GLASS.defaultBlockState(), AE + "quartz_glass"); // window

        // Ceiling (quartz) over the front bay, caved-in over the back-east.
        for (int x = 1; x <= xMax - 1; x++) {
            for (int z = 1; z <= 3; z++) {
                if (!((x == 4 || x == 5) && z == 3)) {
                    set(m, mods, new BlockPos(x, 3, z), cube(), AE + "quartz_block");
                }
            }
        }
        // Conduit/cable stubs still strung under the ceiling (vanilla END_ROD stand-ins — AE2's cable is a multipart).
        // Kept off the centre column (3,2,2) so the spawned mob has headroom; one branch trails out over the open pit.
        for (int z = 1; z <= 3; z++) {
            m.put(new BlockPos(2, 2, z), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));
        }
        m.put(new BlockPos(4, 2, 2), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
        // A snapped conduit antenna juts up off the roof (the anti-box silhouette, like the IE factory's wire-post).
        m.put(new BlockPos(2, 4, 2), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
        m.put(new BlockPos(2, 5, 2), Blocks.END_ROD.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        // A few spilled certus-quartz / fluix chunks (the "certus scraps").
        set(m, mods, new BlockPos(1, 1, 1), cube(), AE + "quartz_block");
        set(m, mods, new BlockPos(1, 1, 2), cube(), AE + "chiseled_quartz_block");
        set(m, mods, new BlockPos(2, 1, 1), cube(), AE + "fluix_block");

        // Vanilla ruin clutter (+ the gametest's assertable shell): cobblestone rubble, a lantern, cobwebs.
        m.put(new BlockPos(5, 1, 1), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(5, 2, 1), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(1, 1, 4), Blocks.COBBLESTONE.defaultBlockState());
        m.put(new BlockPos(2, 1, 3), Blocks.LANTERN.defaultBlockState());
        m.put(new BlockPos(1, 2, 4), Blocks.COBWEB.defaultBlockState());
        // (0,2,1) is a west-wall cell that wall() gave an AE id — set(...,null) so this vanilla cobweb clears the stale id.
        set(m, mods, new BlockPos(0, 2, 1), Blocks.COBWEB.defaultBlockState(), null);

        // The scrap chest on open interior floor — air above at (5,2,4) so it opens ([[skyseed-structure-chest-openable]]).
        m.put(new BlockPos(5, 1, 4), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 4), StructureParts.lootChest(SCRAP));

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 2), "minecraft:cobblestone");
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Helpers (mirror of Create/IeRuinsTemplates).
    // ------------------------------------------------------------------------------------------------------------

    /** A two-course perimeter wall cell at {@code (x, z)} — quartz over a quartz/chiseled mix — unless {@code breach}. */
    private static void wall(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x, int z, boolean breach) {
        if (breach) {
            return;
        }
        set(m, mods, new BlockPos(x, 1, z), cube(), AE + "quartz_block");
        set(m, mods, new BlockPos(x, 2, z), cube(), AE + ((x + z) % 2 == 0 ? "chiseled_quartz_block" : "quartz_bricks"));
    }

    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p);
        }
    }

    /** A propertyless full-cube analog (for AE2's quartz/fluix blocks — no blockstate properties). */
    private static BlockState cube() {
        return Blocks.STONE.defaultBlockState();
    }

    /** Finish a build: a jigsaw cell must never carry a mod palette name, so strip mod ids from every jigsaw cell. */
    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
