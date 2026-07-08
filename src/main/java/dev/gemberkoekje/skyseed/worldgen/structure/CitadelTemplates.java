package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Mage's Sanctum</b> — skyseed's home-curated rebuild of Iron's Spells' <b>Citadel</b> (its flagship fortress,
 * far too large to reuse 1:1). Grand at a fraction of the scale, and it uses the huge island's room: a 21×21 keep with a
 * crenellated <b>rampart skirt</b> + corner turrets, over four tall levels — an <b>entrance hall</b> (pillars, a grand
 * gate), the <b>double-height library</b> (bookshelf walls, a two-wide <b>mezzanine</b> balcony, four hanging
 * chandeliers, lecterns — the showpiece), a set-back <b>keeper's chamber</b> (throne, an ominous-reward chest), a
 * tapering <b>deepslate spire</b>, and a buried <b>vault</b> (a trial Vault + a mansion chest). A single ladder core
 * links every floor and climbs out into the keeper's chamber with real headroom. No boxes: buttressed + windowed +
 * set-back massing outside, multi-level carved interiors within. Centred by its central anchor; {@code sink 5} seats the
 * hall floor at the surface and buries the vault below.
 *
 * <p>The boss is a single Iron's <b>citadel_keeper</b> (via the theme {@code mobs} pack — no mixed factions that fight
 * each other, no oversized mob that shatters the floor). Inert-safe; all vanilla blocks. See {@code IRONSTRUCTUREREBUILDPLAN.md} §Citadel.
 */
public final class CitadelTemplates {
    private CitadelTemplates() {}

    private static final int MID = 10;          // 21×21 footprint (0..20), anchor at the centre
    private static final int LO = 3, HI = 17;   // keep wall square (15×15)
    private static final int ILO = 4, IHI = 16; // keep interior (13×13)
    private static final int LX = 6, LZ = 6;    // ladder cell; backing pillar one to the west

    public static void generateInto(Path dir) throws IOException {
        writeIfAbsent(dir.resolve("keep.nbt"), keep());
    }

    private static Built keep() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        vault(m, bes);          // y0-4 (buried)
        hall(m);                // y5-10
        library(m, bes);        // y10-19 (double height)
        keeperChamber(m, bes);  // y19-24
        spire(m);               // y24-30
        ramparts(m);            // y5-7 skirt + turrets
        circulation(m);         // ladder core through every floor (placed last → cuts its own floor holes)

        StructureParts.linkFences(m); // mezzanine + rampart railings
        StructureParts.anchor(m, bes, new BlockPos(MID, 0, MID), "minecraft:deepslate_bricks");
        return new Built(m, bes);
    }

    // ===================================================================================================== VAULT (y0-4)
    /** A buried deepslate vault under the keep (11×11): a trial Vault + a mansion chest, sculk-lit, candle sconces set
     *  into the walls so nothing blocks the floor. Carved into the island (explicit air), reached by the ladder core. */
    private static void vault(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        for (int x = 5; x <= 15; x++) {
            for (int z = 5; z <= 15; z++) {
                m.put(new BlockPos(x, 0, z), darkMix(x, z));       // floor
                m.put(new BlockPos(x, 4, z), darkMix(x, z + 1));   // ceiling (hall floor sits above)
                final boolean perim = x == 5 || x == 15 || z == 5 || z == 15;
                for (int y = 1; y <= 3; y++) {
                    m.put(new BlockPos(x, y, z), perim ? darkMix(x, y + z) : Blocks.AIR.defaultBlockState());
                }
            }
        }
        m.put(new BlockPos(MID, 1, MID), Blocks.VAULT.defaultBlockState());
        m.put(new BlockPos(MID, 1, MID - 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        bes.put(new BlockPos(MID, 1, MID - 2), StructureParts.lootChest("minecraft:chests/woodland_mansion"));
        // Candle sconces at the far corners (a candle atop a chiseled plinth) — off the ladder drop (6,6) and clear of
        // the walk from the ladder to the central Vault, so nothing blocks the way down.
        for (final int[] c : new int[][]{{13, 7}, {7, 13}, {13, 13}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
            m.put(new BlockPos(c[0], 2, c[1]), candle());
        }
        m.put(new BlockPos(MID + 3, 1, MID + 3), Blocks.SCULK.defaultBlockState()); // a touch of dread, off the path
    }

    // ================================================================================================= ENTRANCE HALL (y5-10)
    /** The ground floor (surface): a pillared entrance hall with a grand arched gate and a hearth. */
    private static void hall(Map<BlockPos, BlockState> m) {
        for (int x = LO; x <= HI; x++) {
            for (int z = LO; z <= HI; z++) {
                m.put(new BlockPos(x, 5, z), floorMix(x, z)); // hall floor (at the island surface)
            }
        }
        keepWalls(m, 6, 9);
        // Grand front gate (−Z), 3 wide × 4 tall, arched.
        for (int x = MID - 1; x <= MID + 1; x++) {
            for (int y = 6; y <= 9; y++) {
                m.remove(new BlockPos(x, y, LO));
            }
        }
        m.put(new BlockPos(MID - 1, 9, LO), archStair(Direction.EAST));
        m.put(new BlockPos(MID + 1, 9, LO), archStair(Direction.WEST));
        // Four chiseled pillars carrying the library above, and a central hearth (clear of the ladder core at 6,6).
        for (final int[] p : new int[][]{{7, 7}, {13, 7}, {7, 13}, {13, 13}}) {
            for (int y = 6; y <= 9; y++) {
                m.put(new BlockPos(p[0], y, p[1]), Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
            }
        }
        m.put(new BlockPos(MID, 6, MID), Blocks.CAMPFIRE.defaultBlockState());
        windowRow(m, 8);
    }

    // ==================================================================================================== LIBRARY (y10-19)
    /** The showpiece: a big double-height library — bookshelf-lined walls both storeys, a two-wide mezzanine balcony
     *  with a railing, four hanging chandeliers, and lecterns that actually sit on a floor. */
    private static void library(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        for (int x = ILO; x <= IHI; x++) {
            for (int z = ILO; z <= IHI; z++) {
                m.put(new BlockPos(x, 10, z), Blocks.DARK_OAK_PLANKS.defaultBlockState()); // library floor
            }
        }
        keepWalls(m, 11, 18);
        // Bookshelf-line the interior wall faces, both storeys (skip the mezzanine band and the gate approach).
        for (int y = 11; y <= 17; y++) {
            if (y == 14) {
                continue; // mezzanine walkway level
            }
            for (int t = ILO; t <= IHI; t++) {
                shelf(m, ILO, y, t);
                shelf(m, IHI, y, t);
                shelf(m, t, y, ILO);
                shelf(m, t, y, IHI);
            }
        }
        windowRow(m, 12);
        windowRow(m, 16);
        // Two-wide mezzanine balcony at y14, railing at y15 on the inner edge, around a central double-height void.
        for (int x = ILO + 1; x <= IHI - 1; x++) {
            for (int z = ILO + 1; z <= IHI - 1; z++) {
                final boolean ring = x <= ILO + 2 || x >= IHI - 2 || z <= ILO + 2 || z >= IHI - 2;
                if (!ring) {
                    continue; // the open central void
                }
                m.put(new BlockPos(x, 14, z), Blocks.DARK_OAK_PLANKS.defaultBlockState());
                // Railing only along the void boundary (the inner rectangle) — never across the outer walkway — with a
                // gap where the ladder lands so you can step off onto the balcony and walk the whole loop.
                final boolean voidEdge = x >= ILO + 2 && x <= IHI - 2 && z >= ILO + 2 && z <= IHI - 2
                        && (x == ILO + 2 || x == IHI - 2 || z == ILO + 2 || z == IHI - 2);
                final boolean byLadder = Math.abs(x - LX) <= 1 && Math.abs(z - LZ) <= 1;
                if (voidEdge && !byLadder) {
                    m.put(new BlockPos(x, 15, z), Blocks.DARK_OAK_FENCE.defaultBlockState());
                }
            }
        }
        // Four chandeliers hanging into the void from the ceiling, and warm lecterns on the ground + mezzanine floors.
        chandelier(m, MID - 3, MID - 3, 18);
        chandelier(m, MID + 3, MID - 3, 18);
        chandelier(m, MID - 3, MID + 3, 18);
        chandelier(m, MID + 3, MID + 3, 18);
        m.put(new BlockPos(MID, 11, MID), Blocks.LECTERN.defaultBlockState());
        m.put(new BlockPos(MID - 1, 11, MID + 1), Blocks.LECTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        m.put(new BlockPos(MID, 11, MID - 1), Blocks.RED_CARPET.defaultBlockState());
        m.put(new BlockPos(MID, 11, MID + 1), Blocks.RED_CARPET.defaultBlockState());
        // A reading desk on the mezzanine — a lectern on an actual balcony cell (ILO+2 is mezzanine floor, y14).
        m.put(new BlockPos(ILO + 2, 15, MID), Blocks.LECTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
        m.put(new BlockPos(IHI - 2, 15, MID), Blocks.LECTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
    }

    // =============================================================================================== KEEPER'S CHAMBER (y19-24)
    /** The set-back top chamber: a throne dais, an ominous-reward chest, banners. The ladder climbs out here with headroom. */
    private static void keeperChamber(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        for (int x = ILO; x <= IHI; x++) {
            for (int z = ILO; z <= IHI; z++) {
                m.put(new BlockPos(x, 19, z), ((x + z) % 2 == 0) ? Blocks.DEEPSLATE_TILES.defaultBlockState()
                        : Blocks.POLISHED_DEEPSLATE.defaultBlockState()); // coffered floor over the library
            }
        }
        final int lo = LO + 2, hi = HI - 2; // set-back turret room; wide enough that the ladder corner (6,6) opens into it
        for (int y = 20; y <= 23; y++) {
            for (int t = lo; t <= hi; t++) {
                m.put(new BlockPos(t, y, lo), weathered(t, y, lo));
                m.put(new BlockPos(t, y, hi), weathered(t, y, hi));
                m.put(new BlockPos(lo, y, t), weathered(lo, y, t));
                m.put(new BlockPos(hi, y, t), weathered(hi, y, t));
            }
        }
        for (final int[] w : new int[][]{{MID, 21, lo}, {MID, 21, hi}, {lo, 21, MID}, {hi, 21, MID},
                {MID, 22, lo}, {MID, 22, hi}, {lo, 22, MID}, {hi, 22, MID}}) {
            m.put(new BlockPos(w[0], w[1], w[2]), Blocks.GLASS_PANE.defaultBlockState()); // tall arched windows
        }
        m.put(new BlockPos(MID, 20, MID + 1), Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        m.put(new BlockPos(MID, 20, MID), Blocks.POLISHED_DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(MID, 21, MID), Blocks.SOUL_LANTERN.defaultBlockState());
        m.put(new BlockPos(MID - 1, 20, MID - 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(MID - 1, 20, MID - 1), StructureParts.lootChest("minecraft:chests/trial_chambers/reward_ominous"));
        m.put(new BlockPos(MID - 1, 23, lo), Blocks.RED_WALL_BANNER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        m.put(new BlockPos(MID + 1, 23, lo), Blocks.RED_WALL_BANNER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        for (int x = lo; x <= hi; x++) {
            for (int z = lo; z <= hi; z++) {
                m.put(new BlockPos(x, 24, z), Blocks.DEEPSLATE_TILES.defaultBlockState()); // ceiling; the spire rises here
            }
        }
    }

    // ===================================================================================================== SPIRE (y25-31)
    private static void spire(Map<BlockPos, BlockState> m) {
        final int[] r = {4, 4, 3, 2, 1, 0}; // radius by course above y24
        for (int i = 0; i < r.length; i++) {
            final int y = 25 + i;
            final int rr = r[i];
            for (int x = MID - rr; x <= MID + rr; x++) {
                for (int z = MID - rr; z <= MID + rr; z++) {
                    final boolean edge = x == MID - rr || x == MID + rr || z == MID - rr || z == MID + rr;
                    if (edge || rr == 0) {
                        m.put(new BlockPos(x, y, z), (i % 2 == 0) ? Blocks.COBBLED_DEEPSLATE.defaultBlockState()
                                : Blocks.DEEPSLATE_TILES.defaultBlockState());
                    }
                }
            }
        }
        m.put(new BlockPos(MID, 31, MID), Blocks.SOUL_LANTERN.defaultBlockState()); // the beacon finial
    }

    // ================================================================================================= RAMPARTS (y5-7 skirt)
    private static void ramparts(Map<BlockPos, BlockState> m) {
        final int lo = 0, hi = 20;
        for (int t = lo; t <= hi; t++) {
            rampartCol(m, t, lo);
            rampartCol(m, t, hi);
            rampartCol(m, lo, t);
            rampartCol(m, hi, t);
        }
        for (final int[] c : new int[][]{{lo, lo}, {lo, hi}, {hi, lo}, {hi, hi}}) {
            for (int y = 2; y <= 9; y++) {
                m.put(new BlockPos(c[0], y, c[1]), weathered(c[0], y, c[1])); // corner turrets, footed 3 into the island
            }
        }
        for (int x = MID - 1; x <= MID + 1; x++) {
            m.remove(new BlockPos(x, 6, lo)); // gate gap aligned with the keep door
            m.remove(new BlockPos(x, 7, lo));
        }
    }

    private static void rampartCol(Map<BlockPos, BlockState> m, int x, int z) {
        for (int y = 2; y <= 6; y++) { // y2-4 a buried foundation skirt (roots the base into the island), y5-6 the wall
            m.put(new BlockPos(x, y, z), weathered(x, y, z));
        }
        if ((x + z) % 2 == 0) {
            m.put(new BlockPos(x, 7, z), weathered(x, 7, z)); // crenellations
        }
    }

    // ================================================================================================= CIRCULATION (ladder core)
    /** A single ladder core (a backing pillar with ladders on its east face) from the vault up into the keeper's
     *  chamber. Placed last, so it overwrites each floor it crosses — cutting the hole it passes through — and it runs
     *  one course above the keeper floor so the climb ends with a full 2-block headroom to step off (not a 1-block crawl). */
    private static void circulation(Map<BlockPos, BlockState> m) {
        for (int y = 1; y <= 20; y++) {
            m.put(new BlockPos(LX - 1, y, LZ), Blocks.DEEPSLATE_BRICKS.defaultBlockState()); // backing pillar (west)
            m.put(new BlockPos(LX, y, LZ), Blocks.LADDER.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
        }
        // Guarantee the step-off cell in the keeper's chamber is clear (floor below at y19, two blocks of air above).
        m.put(new BlockPos(LX + 1, 20, LZ), Blocks.AIR.defaultBlockState());
        m.put(new BlockPos(LX + 1, 21, LZ), Blocks.AIR.defaultBlockState());
    }

    // ===================================================================================================== helpers
    /** The keep's square wall (perimeter of [LO,HI]) for y in [y0,y1], with chiseled inner corner buttresses. */
    private static void keepWalls(Map<BlockPos, BlockState> m, int y0, int y1) {
        for (int y = y0; y <= y1; y++) {
            for (int t = LO; t <= HI; t++) {
                m.put(new BlockPos(t, y, LO), weathered(t, y, LO));
                m.put(new BlockPos(t, y, HI), weathered(t, y, HI));
                m.put(new BlockPos(LO, y, t), weathered(LO, y, t));
                m.put(new BlockPos(HI, y, t), weathered(HI, y, t));
            }
            for (final int[] c : new int[][]{{LO + 1, LO + 1}, {LO + 1, HI - 1}, {HI - 1, LO + 1}, {HI - 1, HI - 1}}) {
                m.put(new BlockPos(c[0], y, c[1]), Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
            }
        }
    }

    /** Glass-pane windows in the middle + flanks of each keep face at height {@code y}. */
    private static void windowRow(Map<BlockPos, BlockState> m, int y) {
        for (final int[] w : new int[][]{{MID, y, LO}, {MID, y, HI}, {LO, y, MID}, {HI, y, MID},
                {MID - 3, y, LO}, {MID + 3, y, LO}, {MID - 3, y, HI}, {MID + 3, y, HI},
                {LO, y, MID - 3}, {LO, y, MID + 3}, {HI, y, MID - 3}, {HI, y, MID + 3}}) {
            m.put(new BlockPos(w[0], w[1], w[2]), Blocks.GLASS_PANE.defaultBlockState());
        }
    }

    /** A bookshelf on an interior-edge cell (some chiseled), keeping the front-gate approach clear. */
    private static void shelf(Map<BlockPos, BlockState> m, int x, int y, int z) {
        if (z == ILO && x >= MID - 1 && x <= MID + 1) {
            return; // clear the path in from the front gate
        }
        m.put(new BlockPos(x, y, z), Math.floorMod(x * 7 + y * 13 + z * 5, 4) == 0
                ? Blocks.CHISELED_BOOKSHELF.defaultBlockState() : Blocks.BOOKSHELF.defaultBlockState());
    }

    /** A hanging chandelier: two chains from {@code ceilY}, a hanging lantern, ringed by four more. */
    private static void chandelier(Map<BlockPos, BlockState> m, int x, int z, int ceilY) {
        m.put(new BlockPos(x, ceilY, z), chain());
        m.put(new BlockPos(x, ceilY - 1, z), chain());
        m.put(new BlockPos(x, ceilY - 2, z), hangLantern());
        for (final int[] d : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
            m.put(new BlockPos(x + d[0], ceilY - 2, z + d[1]), hangLantern());
        }
    }

    private static BlockState hangLantern() {
        return Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true);
    }

    /** The chain block — renamed {@code IRON_CHAIN} on the 26.1.2 node. */
    private static BlockState chain() {
        //? if >=26.1.2 {
        /*return Blocks.IRON_CHAIN.defaultBlockState();*/
        //?} else {
        return Blocks.CHAIN.defaultBlockState();
        //?}
    }

    private static BlockState archStair(Direction facing) {
        return Blocks.STONE_BRICK_STAIRS.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(BlockStateProperties.HALF, Half.TOP);
    }

    private static BlockState candle() {
        return Blocks.CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    }

    /** Weathered fortress stone: stone bricks, mossy + cracked, cobbled-deepslate accents. */
    private static BlockState weathered(int a, int b, int c) {
        return switch (Math.floorMod(a * 5 + b * 7 + c * 11, 7)) {
            case 0 -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            case 1 -> Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
            case 2 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }

    /** Deepslate vault mix. */
    private static BlockState darkMix(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 5)) {
            case 0 -> Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
            case 1 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            default -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        };
    }

    /** Hall floor mix: stone bricks + polished andesite. */
    private static BlockState floorMix(int a, int b) {
        return (Math.floorMod(a * 3 + b * 5, 4) == 0)
                ? Blocks.POLISHED_ANDESITE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState();
    }
}
