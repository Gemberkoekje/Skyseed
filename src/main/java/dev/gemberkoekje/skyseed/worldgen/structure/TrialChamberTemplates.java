package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.SlabBlock;
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
 * The grand <b>Trial Chamber</b> — a buried tuff/copper complex, assembled by the jigsaw from a central
 * <b>hub</b> (the breeze boss + ominous vault + the ladder entrance) and a pool of <b>room</b> pieces (each a
 * trial spawner and a vault, or a twin-vault treasure room) that attach to the hub's four edge connectors. The
 * whole assembly is sunk deep via the theme {@code sink}; connectors sit at floor level and each piece carries
 * a pre-carved doorway, so the rooms open off the hub. Self-contained: clear the spawners for Trial Keys, open
 * the vaults; with Bad Omen the spawners turn ominous and feed the centre ominous vault (heavy core → mace).
 * (v2 — split from the original single 11×11 template into a modular pool for layout variety.)
 */
public final class TrialChamberTemplates {
    private TrialChamberTemplates() {}

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final String TUFF = "minecraft:tuff_bricks";

    public static void generateInto(Path dir) throws IOException {
        writeIfAbsent(dir.resolve("hub.nbt"), hub());
        writeIfAbsent(dir.resolve("room_zombie.nbt"), room("minecraft:zombie", 1));
        writeIfAbsent(dir.resolve("room_skeleton.nbt"), room("minecraft:skeleton", 1));
        writeIfAbsent(dir.resolve("room_spider.nbt"), room("minecraft:spider", 1));
        writeIfAbsent(dir.resolve("room_breeze.nbt"), room("minecraft:breeze", 1));
        writeIfAbsent(dir.resolve("room_treasure.nbt"), room(null, 2));
        writeIfAbsent(dir.resolve("room_small.nbt"), roomSmall("minecraft:zombie")); // #61 — chamber-size variety (a cell)
        // Passages — the warren's connective tissue (#61): the atrium feeds these, they wind + branch, chambers hang off.
        writeIfAbsent(dir.resolve("corridor.nbt"), corridor());   // straight passage (was gallery)
        writeIfAbsent(dir.resolve("corner.nbt"), corner());       // 90° turn
        writeIfAbsent(dir.resolve("junction.nbt"), junction());   // T-branch with a chamber spur
        writeIfAbsent(dir.resolve("descent.nbt"), descent());     // staircase down a level (multi-story, downward)
        writeIfAbsent(dir.resolve("end.nbt"), endRoom());         // a climactic ominous-vault chamber
    }

    /**
     * The deterministic vanilla trial-chamber masonry: tuff-brick dominant (~5/12) with the AGED copper family woven in
     * — waxed weathered/oxidized copper, cut copper, the chiseled variant, and copper grate (the vent look). Waxed so it
     * keeps its green-teal patina forever. Deterministic on {@code (a,b)} so a piece regenerates byte-stable. (#24)
     */
    private static BlockState mix(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 5, 12)) {
            case 0 -> Blocks.POLISHED_TUFF.defaultBlockState();
            case 1 -> Blocks.CHISELED_TUFF.defaultBlockState();
            case 2 -> Blocks.WAXED_OXIDIZED_COPPER.defaultBlockState();
            case 3 -> Blocks.WAXED_WEATHERED_COPPER.defaultBlockState();
            case 4 -> Blocks.WAXED_OXIDIZED_CUT_COPPER.defaultBlockState();
            case 5 -> Blocks.WAXED_OXIDIZED_COPPER_GRATE.defaultBlockState();
            case 6 -> Blocks.WAXED_OXIDIZED_CHISELED_COPPER.defaultBlockState();
            default -> Blocks.TUFF_BRICKS.defaultBlockState();
        };
    }

    // --- Wall/floor TEXTURER (#61): a deliberate tiled MOSAIC, not the old per-block speckle. Tuff-brick frames grid
    // each wall into panels; every panel is a patch of ONE copper patina with a chiseled/grate MOTIF at its centre —
    // the vanilla trial-chamber "decorated temple" read (see TRIALCHAMBERPLAN.md).

    /** A framed-panel WALL tile at in-wall coords ({@code u} across, {@code v} up): 4-block module — tuff-brick frame
     *  lines, each 3×3 panel a single copper tone with a chiseled-copper / chiseled-tuff / grate motif at its centre. */
    private static BlockState wall(int u, int v) {
        final int fu = Math.floorMod(u, 4), fv = Math.floorMod(v, 4);
        if (fu == 0 || fv == 0) {
            return Blocks.TUFF_BRICKS.defaultBlockState(); // the frame grout gridding the wall into panels
        }
        final int panel = Math.floorMod(Math.floorDiv(u, 4) * 2 + Math.floorDiv(v, 4), 6);
        if (fu == 2 && fv == 2) { // panel centre → a decorative motif, cycled per panel
            return switch (panel % 4) {
                case 0 -> Blocks.WAXED_OXIDIZED_CHISELED_COPPER.defaultBlockState(); // oxidized "cross" motif
                case 1 -> Blocks.CHISELED_TUFF.defaultBlockState();                  // the "spiral/maze" motif
                case 2 -> Blocks.WAXED_OXIDIZED_COPPER_GRATE.defaultBlockState();    // a lattice vent-window
                default -> Blocks.WAXED_CHISELED_COPPER.defaultBlockState();         // NON-weathered (warm) cross motif
            };
        }
        // The panel field → a patch of ONE copper tone. A MIX of oxidized/weathered (green-teal) AND non-weathered
        // (warm brown/orange) copper, so the wall reads as banded aged metal, not a uniform green speckle.
        return switch (panel) {
            case 0 -> Blocks.WAXED_OXIDIZED_COPPER.defaultBlockState();       // green
            case 1 -> Blocks.WAXED_WEATHERED_COPPER.defaultBlockState();      // green-brown
            case 2 -> Blocks.WAXED_EXPOSED_COPPER.defaultBlockState();        // brown (non-weathered)
            case 3 -> Blocks.WAXED_COPPER_BLOCK.defaultBlockState();          // orange (non-weathered)
            case 4 -> Blocks.WAXED_OXIDIZED_CUT_COPPER.defaultBlockState();   // green cut
            default -> Blocks.WAXED_CUT_COPPER.defaultBlockState();           // orange cut (non-weathered)
        };
    }

    /** The perimeter WALL block at (x,y,z) for a room of interior span [0..maxX]×[0..maxZ]: a cut-copper corner post at
     *  the four vertical corners, else the framed-panel {@link #wall} tile (u runs along whichever wall this cell is on). */
    private static BlockState wallBlock(int x, int y, int z, int maxX, int maxZ) {
        final boolean edgeX = x == 0 || x == maxX, edgeZ = z == 0 || z == maxZ;
        if (edgeX && edgeZ) {
            return Blocks.WAXED_OXIDIZED_CUT_COPPER.defaultBlockState(); // corner post (structure without free-standing pillars)
        }
        return wall(edgeZ ? x : z, y);
    }

    /** A laid FLOOR/ceiling tile (not speckle): a tuff-brick / polished-tuff checker with a chiseled-copper stud at the
     *  grid nodes ({@code x,z} both on the 4-grid). */
    private static BlockState floorTile(int x, int z) {
        if (Math.floorMod(x, 4) == 0 && Math.floorMod(z, 4) == 0) {
            return Blocks.WAXED_OXIDIZED_CHISELED_COPPER.defaultBlockState();
        }
        return Math.floorMod(x + z, 2) == 0
                ? Blocks.TUFF_BRICKS.defaultBlockState() : Blocks.POLISHED_TUFF.defaultBlockState();
    }

    /** The vanilla trial-chamber light: a lit waxed copper bulb (stays lit — placed with no block update → light 15). */
    private static final BlockState COPPER_BULB_LIT = Blocks.WAXED_COPPER_BULB.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    //? if >=26.1.2 {
    /*private static final BlockState CHAIN = Blocks.IRON_CHAIN.defaultBlockState();*/
    //?} else {
    private static final BlockState CHAIN = Blocks.CHAIN.defaultBlockState();
    //?}

    /** A ceiling lamp: a lit copper bulb flush in the ceiling ({@code y = ceil}) with a chain dangling one block below. */
    private static void ceilingLamp(Map<BlockPos, BlockState> m, int x, int ceil, int z) {
        m.put(new BlockPos(x, ceil, z), COPPER_BULB_LIT);
        m.put(new BlockPos(x, ceil - 1, z), CHAIN);
    }

    /** A crown cornice: top-half waxed-copper stairs ringing the interior wall-top, each facing in toward the room
     *  centre — the vanilla trial-chamber ceiling trim. {@code lo}/{@code hi} are the INTERIOR bounds; skips {@code keep}
     *  columns (packed x,z via {@link BlockPos#asLong}) so it never buries the ladder or a doorway. */
    private static void crownCornice(Map<BlockPos, BlockState> m, int lo, int hi, int y, java.util.Set<Long> keep) {
        for (int x = lo; x <= hi; x++) {
            for (int z = lo; z <= hi; z++) {
                if (x != lo && x != hi && z != lo && z != hi) {
                    continue; // interior perimeter ring only
                }
                if (keep.contains(new BlockPos(x, 0, z).asLong())) {
                    continue;
                }
                final Direction face = x == lo ? Direction.EAST : x == hi ? Direction.WEST
                        : z == lo ? Direction.SOUTH : Direction.NORTH;
                m.put(new BlockPos(x, y, z), Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS.defaultBlockState()
                        .setValue(StairBlock.FACING, face).setValue(StairBlock.HALF, Half.TOP));
            }
        }
    }

    // --- Greebling (#25): the vanilla trial-chamber atmosphere layer ---
    private static final BlockState COBWEB = Blocks.COBWEB.defaultBlockState();
    private static final BlockState CANDLE_LIT = Blocks.CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    private static final BlockState SUS_GRAVEL = Blocks.SUSPICIOUS_GRAVEL.defaultBlockState();
    private static final BlockState DECORATED_POT = Blocks.DECORATED_POT.defaultBlockState();
    private static final BlockState MOSS = Blocks.MOSS_BLOCK.defaultBlockState();

    /** A minimal (empty, four-brick) decorated-pot block entity. */
    private static CompoundTag potBE() {
        final CompoundTag be = new CompoundTag();
        be.putString("id", "minecraft:decorated_pot");
        return be;
    }

    /**
     * Sprinkle deterministic greebling into a chamber whose INTERIOR spans [{@code lo}..{@code hi}] in x/z, floor at y0
     * and ceiling at {@code ceilY}: cobwebs tucked under two front ceiling corners, a lit candle + a decorated pot on the
     * floor, a brushable suspicious-gravel dig (trial-chamber archaeology loot) set into the floor, and a moss patch —
     * the vanilla atmosphere layer (#25). Every placement is guarded ({@link #greebleAt}) so it can never bury a
     * functional/feature block (spawner, vault, lamp, chain, ladder, connector or cornice).
     */
    private static void greeble(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, int lo, int hi, int ceilY) {
        greebleAt(m, new BlockPos(lo, ceilY - 2, lo), COBWEB);
        greebleAt(m, new BlockPos(hi, ceilY - 2, lo), COBWEB);
        greebleAt(m, new BlockPos(lo, 1, hi - 1), CANDLE_LIT);
        if (greebleAt(m, new BlockPos(hi, 1, hi - 1), DECORATED_POT)) {
            bes.put(new BlockPos(hi, 1, hi - 1), potBE());
        }
        if (greebleAt(m, new BlockPos(lo, 0, lo + 1), SUS_GRAVEL)) {
            bes.put(new BlockPos(lo, 0, lo + 1), suspicious("minecraft:archaeology/trial_chambers"));
        }
        greebleAt(m, new BlockPos(hi, 0, lo + 1), MOSS);
    }

    /** Place {@code s} unless the cell already holds a functional/feature block (never bury a spawner/vault/lamp/chain/
     *  ladder/jigsaw/cornice-stair); returns whether it placed, so the caller can attach a matching block entity. */
    private static boolean greebleAt(Map<BlockPos, BlockState> m, BlockPos p, BlockState s) {
        final BlockState cur = m.get(p);
        if (cur != null) {
            final var b = cur.getBlock();
            if (b == Blocks.TRIAL_SPAWNER || b == Blocks.VAULT || b == Blocks.JIGSAW || b == Blocks.LADDER
                    || b == Blocks.WAXED_COPPER_BULB || b == CHAIN.getBlock() || b instanceof StairBlock) {
                return false;
            }
        }
        m.put(p, s);
        return true;
    }

    /** A rectangular belt of top-half {@code stair}s facing inward along the interior wall ring at height {@code y} — a
     *  wall-top cornice OR a mid-wall ridge that breaks up a tall blank wall. Skips {@code keep} columns (packed x,z). */
    private static void beltRect(Map<BlockPos, BlockState> m, int loX, int hiX, int loZ, int hiZ, int y,
                                 BlockState stair, BlockState corner, java.util.Set<Long> keep) {
        for (int x = loX; x <= hiX; x++) {
            for (int z = loZ; z <= hiZ; z++) {
                if (x != loX && x != hiX && z != loZ && z != hiZ) {
                    continue; // interior perimeter ring only
                }
                if (keep.contains(new BlockPos(x, 0, z).asLong())) {
                    continue;
                }
                if ((x == loX || x == hiX) && (z == loZ || z == hiZ)) {
                    m.put(new BlockPos(x, y, z), corner); // a top slab: stairs can't hook cleanly at the corner
                    continue;
                }
                final Direction face = x == loX ? Direction.WEST : x == hiX ? Direction.EAST
                        : z == loZ ? Direction.NORTH : Direction.SOUTH; // face OUTWARD (toward the wall) so the lip overhangs into the room
                m.put(new BlockPos(x, y, z), stair.setValue(StairBlock.FACING, face).setValue(StairBlock.HALF, Half.TOP));
            }
        }
    }

    /**
     * The start piece: a grand rectangular ATRIUM (11×13). The back third is a platform RAISED onto a stair step, so the
     * room reads on two floor levels — the breeze boss spawner on the lower arena, the ominous vault up on the dais. A
     * corner ladder climbs to the surface; copper-bulb chandeliers hang on chains; a crown cornice + a warmer mid-wall
     * RIDGE break up the tall walls; and four edge connectors feed the passages — three at the lower level and one
     * ELEVATED off the dais, so exits leave at different heights (the jigsaw reuses the same warren set). (#24/#61)
     */
    private static Built hub() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int maxX = 10, midX = 5, maxZ = 12, ceil = 9; // rectangular 11 wide × 13 long, interior y1-8
        final int daisY = 2;                                // back platform (z >= 8) raised to y2, reached by a 2-step stair (keeps the exit under the ridge)
        for (int x = 0; x <= maxX; x++) {
            for (int z = 0; z <= maxZ; z++) {
                m.put(new BlockPos(x, ceil, z), floorTile(x, z)); // ceiling
                if (x == 0 || x == maxX || z == 0 || z == maxZ) { // a perimeter wall column (full height, doorways carved)
                    for (int y = 0; y < ceil; y++) {
                        m.put(new BlockPos(x, y, z),
                                hubDoorway(x, y, z, maxX, midX, maxZ, daisY) ? AIR : wallBlock(x, y, z, maxX, maxZ));
                    }
                } else { // interior column: lower arena (z<=5) → a 3-step staircase (z6-8) → raised dais (z>=9)
                    final int surf = z <= 5 ? 0 : z >= 8 ? daisY : z - 5; // walkable top of this column
                    final boolean step = z >= 6 && z <= 7;                // a staircase tread (2 steps up to the dais)
                    for (int y = 0; y < surf; y++) {
                        m.put(new BlockPos(x, y, z), mix(x, y + z));      // substructure below the tread/dais
                    }
                    m.put(new BlockPos(x, surf, z), step
                            ? Blocks.TUFF_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)
                            : floorTile(x, z));
                    for (int y = surf + 1; y < ceil; y++) {
                        m.put(new BlockPos(x, y, z), AIR);
                    }
                }
            }
        }
        // Boss: breeze spawner on the lower arena; the ominous vault up on the raised dais.
        m.put(new BlockPos(midX, 1, 3), Blocks.TRIAL_SPAWNER.defaultBlockState());
        bes.put(new BlockPos(midX, 1, 3), trialSpawner("minecraft:breeze"));
        m.put(new BlockPos(midX, daisY + 1, maxZ - 2), Blocks.VAULT.defaultBlockState().setValue(BlockStateProperties.OMINOUS, true));
        bes.put(new BlockPos(midX, daisY + 1, maxZ - 2), ominousVault());
        // Four edge connectors: three at the lower level + one ELEVATED off the dais (exits leave at different heights).
        connector(m, bes, new BlockPos(midX, 0, 0), FrontAndTop.NORTH_UP);        // front (lower)
        connector(m, bes, new BlockPos(0, 0, 3), FrontAndTop.WEST_UP);            // left (lower)
        connector(m, bes, new BlockPos(maxX, 0, 3), FrontAndTop.EAST_UP);         // right (lower)
        connector(m, bes, new BlockPos(midX, daisY, maxZ), FrontAndTop.SOUTH_UP); // back (raised — an elevated exit)
        // Ladder against the −X wall (off the corner, so it climbs cleanly), punched up through the ceiling to the
        // surface (nbt y10 = surface at sink 10). FACING EAST → its back sits on the x0 wall.
        final BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
        for (int y = 1; y <= 10; y++) {
            m.put(new BlockPos(1, y, 2), ladder);
        }
        // Copper-bulb chandeliers on chains over both levels.
        for (final int[] l : new int[][]{{midX, 3}, {3, 6}, {maxX - 3, 6}, {midX, maxZ - 2}}) {
            ceilingLamp(m, l[0], ceil, l[1]);
        }
        // A crown cornice at the wall-top + a warmer (non-weathered) mid-wall RIDGE — both break up the tall blank walls.
        final java.util.Set<Long> keep = java.util.Set.of(new BlockPos(1, 0, 2).asLong()); // the ladder column
        beltRect(m, 1, maxX - 1, 1, maxZ - 1, ceil - 1, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS.defaultBlockState(),
                Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), keep);
        beltRect(m, 1, maxX - 1, 1, maxZ - 1, 5, Blocks.WAXED_CUT_COPPER_STAIRS.defaultBlockState(),
                Blocks.WAXED_CUT_COPPER_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), keep);
        // Greebling (#25): decorated pots on each level + a cobweb + a brushable dig.
        m.put(new BlockPos(3, 1, 2), DECORATED_POT); bes.put(new BlockPos(3, 1, 2), potBE());
        m.put(new BlockPos(maxX - 2, daisY + 1, maxZ - 1), DECORATED_POT); bes.put(new BlockPos(maxX - 2, daisY + 1, maxZ - 1), potBE());
        m.put(new BlockPos(maxX - 2, 7, 2), COBWEB);
        m.put(new BlockPos(3, 0, 4), SUS_GRAVEL); bes.put(new BlockPos(3, 0, 4), suspicious("minecraft:archaeology/trial_chambers"));
        // The "bottom" anchor seats the hub on the island; placed last so the floor loop doesn't overwrite it.
        StructureParts.anchor(m, bes, new BlockPos(midX, 0, 5), TUFF);
        return new Built(m, bes);
    }

    /** The atrium's carved doorways: three lower-level passages (front + left/right) and one ELEVATED back passage off
     *  the raised dais (its opening sits {@code daisY} higher, at y {@code daisY+1..daisY+3}). */
    private static boolean hubDoorway(int x, int y, int z, int maxX, int midX, int maxZ, int daisY) {
        final boolean front = z == 0 && x == midX && y >= 1 && y <= 3;
        final boolean left = x == 0 && z == 3 && y >= 1 && y <= 3;
        final boolean right = x == maxX && z == 3 && y >= 1 && y <= 3;
        final boolean back = z == maxZ && x == midX && y >= daisY + 1 && y <= daisY + 3;
        return front || left || right || back;
    }

    // --- Warren connectors (#61): the atrium feeds PASSAGES (the halls pool), passages wind through junctions/turns,
    // and CHAMBERS (the rooms pool) hang off junction spurs — so you walk corridors between rooms, like a real vanilla
    // trial chamber, instead of chambers bolted straight onto the atrium. Two connector pairs:
    //   passages: a `hall` source draws a piece whose entrance jigsaw is named `hall_end` (from the halls pool);
    //   chambers: a `chamber_edge` source draws a piece whose entrance jigsaw is named `room_door` (from the rooms pool).

    /** A source jigsaw that draws the HALLS pool (a passage) — used by the atrium edges + passage exits. */
    private static void drawHall(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos p, FrontAndTop facing) {
        m.put(p, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, facing));
        bes.put(p, jig("skyseed:hall", "skyseed:hall_end", "skyseed:trial_chamber/halls", TUFF));
    }

    /** A passage's entrance jigsaw — mates a {@code hall} source and draws nothing further (pool empty). */
    private static void hallEntrance(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos p, FrontAndTop facing) {
        m.put(p, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, facing));
        bes.put(p, jig("skyseed:hall_end", "skyseed:hall", "minecraft:empty", TUFF));
    }

    /** A source jigsaw that draws the ROOMS pool (a chamber) — used by junction spurs. */
    private static void drawChamber(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos p, FrontAndTop facing) {
        m.put(p, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, facing));
        bes.put(p, jig("skyseed:chamber_edge", "skyseed:room_door", "skyseed:trial_chamber/rooms", TUFF));
    }

    private static void connector(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos p, FrontAndTop facing) {
        drawHall(m, bes, p, facing); // atrium edges now open onto passages, not chambers
    }

    /**
     * A 7×7 chamber: a connector + 3-tall doorway on the −Z wall (faces the parent after the jigsaw rotates it in), a
     * trial spawner ({@code mob}, or none for a treasure room), {@code vaults} vault(s) along the back wall, a
     * copper-bulb lamp and a crown cornice — the aged-copper vanilla look.
     */
    private static Built room(String mob, int vaults) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 6, mid = 3, ceil = 6; // 7×7, interior y1-5 (5 tall)
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = x == mid && z == 0 && yy <= 3; // −Z wall doorway (3 tall)
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, max, max) : AIR);
                }
            }
        }
        // Connector + doorway on the −Z wall.
        m.put(new BlockPos(mid, 0, 0), Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.NORTH_UP));
        bes.put(new BlockPos(mid, 0, 0), jig("skyseed:room_door", "skyseed:chamber_edge", "minecraft:empty", TUFF));

        if (mob != null) {
            m.put(new BlockPos(mid, 1, mid), Blocks.TRIAL_SPAWNER.defaultBlockState());
            bes.put(new BlockPos(mid, 1, mid), trialSpawner(mob));
        }
        // Vaults along the back wall (away from the doorway).
        m.put(new BlockPos(2, 1, max - 1), Blocks.VAULT.defaultBlockState());
        if (vaults > 1) {
            m.put(new BlockPos(max - 2, 1, max - 1), Blocks.VAULT.defaultBlockState());
        }
        ceilingLamp(m, mid, ceil, mid);
        crownCornice(m, 1, max - 1, ceil - 1, java.util.Set.of());
        greeble(m, bes, 1, max - 1, ceil);
        return new Built(m, bes);
    }

    /**
     * A small spawner CELL — chamber-size variety (the vanilla "1-size" room): a compact 5×5 with a {@code mob} trial
     * spawner + one vault and a room_door entrance on the −Z wall, so a tight closet punctuates the big chambers. (#61)
     */
    private static Built roomSmall(String mob) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2, ceil = 4; // 5×5, interior y1-3
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = x == mid && z == 0 && yy <= 3;
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, max, max) : AIR);
                }
            }
        }
        hallRoomEntrance(m, bes, new BlockPos(mid, 0, 0));
        m.put(new BlockPos(mid, 1, mid), Blocks.TRIAL_SPAWNER.defaultBlockState());
        bes.put(new BlockPos(mid, 1, mid), trialSpawner(mob));
        m.put(new BlockPos(mid, 1, max - 1), Blocks.VAULT.defaultBlockState());
        ceilingLamp(m, mid, ceil, mid);
        crownCornice(m, 1, max - 1, ceil - 1, java.util.Set.of()); // match the big chambers' aged-copper trim
        greeble(m, bes, 1, max - 1, ceil);                          // cobweb/candle/pot atmosphere in the cell too
        return new Built(m, bes);
    }

    /** A chamber's entrance jigsaw on its −Z wall (name {@code room_door}) — mates a {@code chamber_edge} spur. */
    private static void hallRoomEntrance(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos p) {
        m.put(p, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.NORTH_UP));
        bes.put(p, jig("skyseed:room_door", "skyseed:chamber_edge", "minecraft:empty", TUFF));
    }

    /**
     * A straight PASSAGE (the warren's connective tissue): mates a hall source on the −Z wall and draws another passage
     * from the halls pool on the +Z wall — so corridors chain into corridors/junctions/turns. 5 wide × 7 long,
     * copper-bulb lit, aged-copper palette. (#61)
     */
    private static Built corridor() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int maxX = 4, midX = 2, maxZ = 6, ceil = 5; // 5 wide × 7 long, interior y1-4 (4 tall)
        for (int x = 0; x <= maxX; x++) {
            for (int z = 0; z <= maxZ; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == maxX || z == 0 || z == maxZ;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = x == midX && (z == 0 || z == maxZ) && yy <= 3; // both Z ends open through (3 tall)
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, maxX, maxZ) : AIR);
                }
            }
        }
        hallEntrance(m, bes, new BlockPos(midX, 0, 0), FrontAndTop.NORTH_UP);           // in (mates a hall)
        drawHall(m, bes, new BlockPos(midX, 0, maxZ), FrontAndTop.SOUTH_UP);            // out → next passage
        ceilingLamp(m, midX, ceil, 2);
        ceilingLamp(m, midX, ceil, maxZ - 2);
        return new Built(m, bes);
    }

    /**
     * A 90° corner PASSAGE (the winding): mates a hall on the −Z wall and turns to draw the next passage from the +X
     * wall, so the warren bends rather than running dead straight. 5×5 open node, copper-bulb lit. (#61)
     */
    private static Built corner() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2, ceil = 5; // 5×5, interior y1-4
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = yy <= 3 && ((x == mid && z == 0) || (x == max && z == mid)); // −Z in + +X out
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, max, max) : AIR);
                }
            }
        }
        hallEntrance(m, bes, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP);            // in
        drawHall(m, bes, new BlockPos(max, 0, mid), FrontAndTop.EAST_UP);               // turn → next passage on +X
        ceilingLamp(m, mid, ceil, mid);
        return new Built(m, bes);
    }

    /**
     * A T-JUNCTION node (the warren's branch points, where CHAMBERS hang off): mates a hall on the −Z wall, continues
     * passages out the ±X walls, and opens a chamber doorway on the +Z wall that draws the rooms pool — so a spawner/
     * treasure chamber sits off the junction. 5×5 open node, copper-bulb lit. (#61)
     */
    private static Built junction() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2, ceil = 5; // 5×5, interior y1-4
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = yy <= 3 && ((x == mid && (z == 0 || z == max)) || ((x == 0 || x == max) && z == mid));
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, max, max) : AIR);
                }
            }
        }
        hallEntrance(m, bes, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP);            // in
        drawHall(m, bes, new BlockPos(0, 0, mid), FrontAndTop.WEST_UP);                 // branch −X → passage
        drawHall(m, bes, new BlockPos(max, 0, mid), FrontAndTop.EAST_UP);              // branch +X → passage
        drawChamber(m, bes, new BlockPos(mid, 0, max), FrontAndTop.SOUTH_UP);          // spur +Z → a chamber
        ceilingLamp(m, mid, ceil, mid);
        return new Built(m, bes);
    }

    /**
     * A descending staircase corridor (phase 2 — the multi-story mechanism): its entrance ({@code room_door}) is at the
     * TOP floor and mates the parent a level up; its exit ({@code chamber_edge}, redrawing the rooms pool) is one level
     * DOWN at the far end — so the jigsaw seats the next piece a storey lower and the chamber leans downward into the
     * deepened island. 5 wide × 7 long, dropping {@code drop} blocks; copper-bulb lit, aged-copper palette. The exit box
     * sits beyond the parent's footprint (different X/Z), so the descent never overlaps the parent (cf. STRUCTUREPLAN #28).
     */
    private static Built descent() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int maxX = 4, midX = 2, maxZ = 6, drop = 5, wallH = 5; // entrance floor = drop; exit floor = 0 (wallH 5 = roomier, less cramped)
        for (int z = 0; z <= maxZ; z++) {
            final int fy = Math.max(0, drop - z);   // the floor steps down 1 per z (drop..0)
            final int ceil = fy + wallH;            // ceiling follows the descent (constant interior height)
            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y < fy; y++) {
                    m.put(new BlockPos(x, y, z), mix(x, y + z)); // solid substructure under the tread (plain fill)
                }
                m.put(new BlockPos(x, fy, z), floorTile(x, z));  // the walkable stepped tread → laid floor
                m.put(new BlockPos(x, ceil, z), floorTile(x, z)); // ceiling
                final boolean wall = x == 0 || x == maxX || z == 0 || z == maxZ;
                for (int y = fy + 1; y < ceil; y++) {
                    m.put(new BlockPos(x, y, z), wall ? wallBlock(x, y, z, maxX, maxZ) : AIR);
                }
            }
        }
        // Entrance (mates a hall a level UP): a 3-tall doorway + hall_end connector on the −Z wall at the top floor.
        for (int y = drop + 1; y <= drop + 3; y++) {
            m.put(new BlockPos(midX, y, 0), AIR);
        }
        hallEntrance(m, bes, new BlockPos(midX, drop, 0), FrontAndTop.NORTH_UP);
        // Exit (draws the ROOMS pool a level DOWN): a 3-tall doorway + chamber_edge connector on the +Z wall at the
        // bottom — so a staircase down always lands you in a trial/treasure room, never a dead end.
        for (int y = 1; y <= 3; y++) {
            m.put(new BlockPos(midX, y, maxZ), AIR);
        }
        drawChamber(m, bes, new BlockPos(midX, 0, maxZ), FrontAndTop.SOUTH_UP);
        ceilingLamp(m, midX, (drop - 1) + wallH, 1);
        ceilingLamp(m, midX, wallH, maxZ - 1);
        return new Built(m, bes);
    }

    /**
     * The climactic END chamber (phase 2): a 7×7 room like {@link #room} but capped with an OMINOUS vault + a breeze
     * spawner, terminal (a {@code room_door} entrance only), so a branch of the chamber ends on a big reward. Copper-bulb
     * lit, aged-copper palette. (Vanilla puts ominous vaults in both the atrium and the end.)
     */
    private static Built endRoom() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 6, mid = 3, ceil = 6; // 7×7, interior y1-5 (5 tall)
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), floorTile(x, z));
                m.put(new BlockPos(x, ceil, z), floorTile(x, z));
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int yy = 1; yy < ceil; yy++) {
                    final boolean door = x == mid && z == 0 && yy <= 3; // −Z wall doorway (3 tall)
                    m.put(new BlockPos(x, yy, z), (perim && !door) ? wallBlock(x, yy, z, max, max) : AIR);
                }
            }
        }
        m.put(new BlockPos(mid, 0, 0), Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.NORTH_UP));
        bes.put(new BlockPos(mid, 0, 0), jig("skyseed:room_door", "skyseed:chamber_edge", "minecraft:empty", TUFF));
        m.put(new BlockPos(mid, 1, mid), Blocks.TRIAL_SPAWNER.defaultBlockState());
        bes.put(new BlockPos(mid, 1, mid), trialSpawner("minecraft:breeze"));
        m.put(new BlockPos(mid, 1, max - 1), Blocks.VAULT.defaultBlockState().setValue(BlockStateProperties.OMINOUS, true));
        bes.put(new BlockPos(mid, 1, max - 1), ominousVault());
        ceilingLamp(m, mid, ceil, mid);
        crownCornice(m, 1, max - 1, ceil - 1, java.util.Set.of());
        greeble(m, bes, 1, max - 1, ceil);
        return new Built(m, bes);
    }

    /** A trial-spawner BE that spawns waves of {@code mob} in both normal and ominous modes. */
    private static CompoundTag trialSpawner(String mob) {
        final CompoundTag entity = new CompoundTag();
        entity.putString("id", mob);
        final CompoundTag spawnData = new CompoundTag();
        spawnData.put("entity", entity.copy());
        final CompoundTag be = new CompoundTag();
        be.putString("id", "minecraft:trial_spawner");
        be.put("spawn_data", spawnData);
        be.put("normal_config", spawnerConfig(mob, 5, 2));
        be.put("ominous_config", spawnerConfig(mob, 8, 3));
        return be;
    }

    private static CompoundTag spawnerConfig(String mob, int total, int simultaneous) {
        final CompoundTag entity = new CompoundTag();
        entity.putString("id", mob);
        final CompoundTag potData = new CompoundTag();
        potData.put("entity", entity.copy());
        final CompoundTag potential = new CompoundTag();
        potential.put("data", potData);
        potential.putInt("weight", 1);
        final ListTag potentials = new ListTag();
        potentials.add(potential);
        final CompoundTag cfg = new CompoundTag();
        cfg.put("spawn_potentials", potentials);
        cfg.putFloat("total_mobs", total);
        cfg.putFloat("simultaneous_mobs", simultaneous);
        return cfg;
    }

    /** An ominous vault BE — requires an ominous trial key and ejects the ominous reward (heavy core, etc.). */
    private static CompoundTag ominousVault() {
        final CompoundTag key = new CompoundTag();
        key.putString("id", "minecraft:ominous_trial_key");
        key.putInt("count", 1);
        final CompoundTag config = new CompoundTag();
        config.putString("loot_table", "minecraft:chests/trial_chambers/reward_ominous");
        config.put("key_item", key);
        final CompoundTag be = new CompoundTag();
        be.putString("id", "minecraft:vault");
        be.put("config", config);
        return be;
    }
}
