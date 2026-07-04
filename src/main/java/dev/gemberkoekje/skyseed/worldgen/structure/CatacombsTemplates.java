package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The <b>Catacombs</b> — skyseed's home-curated grand crypt, standing in for Iron's Spells' own <b>Catacombs</b> (whose
 * jigsaw digs a straight vertical shaft, unfit for a floating island) and, crucially, restoring its flagship boss: the
 * <b>Dead King</b>. One deliberate, self-contained piece that descends into a Huge Ancient island's deep deepslate body:
 * a broken surface <b>mausoleum</b> (with a ruined side <b>wing</b>) → three <em>stacked, rounded</em> chambers — an
 * <b>antechamber</b>, a bone-lined <b>ossuary</b>, and the deep <b>Crypt of the Dead King</b> — linked by a winding
 * stair, with a distinct <b>side crypt</b> bulging off two of them into the footprint corners (burial pockets to find).
 *
 * <p><b>No boxes, inside or out.</b> Every chamber is a jitter-walled rounded blob (never a cube); the side crypts sit in
 * the corners with their centres <em>outside</em> the parent room, so they read as genuine pockets joined by a doorway
 * (not just a fatter room); the mausoleum is a half-collapsed ruin with an off-axis wing. Rooms are carved in two passes
 * — all interiors first, then the deepslate-brick shells (which skip any cell a neighbouring room already opened) — so a
 * side crypt joins its chamber through a natural doorway and every room stays enclosed even where the island body thins.
 *
 * <p><b>Mobs.</b> The side crypts carry {@code catacombs_zombie} spawners, kept deliberately <em>dark</em> (block light
 * suppresses a spawner — the {@link WarBarrowTemplates} lesson) and <em>off the stairways</em>. The <b>Dead King</b> is
 * baked as a dormant {@code dead_king_corpse} <em>entity</em> in the boss chamber (it wakes into the fight when a player
 * descends — so it must sit down here, not spawn at the surface like a theme mob-pack would), flanked by two animated
 * {@code cursed_armor_stand} honour guards. The mod's necromancer + cultists guard the surface mouth via the theme
 * {@code mobs} pack. All inert-safe: unknown ids simply don't spawn, the crypt still builds. All blocks are vanilla →
 * ships in the base mod. Centred by its central anchor. See {@code IRONSCONTENTGAPPLAN.md}.
 *
 * <p>Geometry: 21×21 footprint (anchor at the centre {@link #MID}). Boss floor y0, ossuary floor y7, antechamber floor
 * y12, mausoleum floor y16 = {@link #SURFACE_Y}; the host rare {@code sink}s it 16 so the mouth sits at the surface.
 */
public final class CatacombsTemplates {
    private CatacombsTemplates() {}

    private static final int MID = 10;       // 21×21 footprint (0..20), anchor at the centre
    private static final int MAX = 20;       // footprint upper bound
    private static final int SURFACE_Y = 16; // the mausoleum floor; host sink 16 seats this at the island surface

    public static void generateInto(Path dir) throws IOException {
        writeIfAbsent(dir.resolve("crypt.nbt"), crypt());
    }

    private static Built crypt() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final Map<BlockPos, CompoundTag> entities = new HashMap<>();

        // ---- carve the voids (all interiors first, so overlaps become doorways when the shells go in) ----
        interior(m, MID, MID, 0, 6, 6);      // Crypt of the Dead King (deepest): floor y0 · ceiling y6
        interior(m, MID, MID, 7, 11, 6);     // Ossuary (mid):                    floor y7 · ceiling y11
        interior(m, MID, MID, 12, 16, 6);    // Antechamber (upper):              floor y12 · ceiling y16
        interior(m, 15, 15, 7, 11, 3);       // ossuary's SE side crypt — a distinct corner pocket (centre outside the room)
        interior(m, 5, 5, 12, 16, 3);        // antechamber's NW side crypt

        // ---- wrap every void in its masonry shell (skips cells a neighbour opened → doorways) ----
        shell(m, MID, MID, 0, 6, 6);
        shell(m, MID, MID, 7, 11, 6);
        shell(m, MID, MID, 12, 16, 6);
        shell(m, 15, 15, 7, 11, 3);
        shell(m, 5, 5, 12, 16, 3);

        // ---- winding descent (3-wide mouth aligned with the north door, then two internal flights) ----
        // Landings are stamped FIRST (they clear each stair foot); the flights then lay their treads on top, so the
        // bottom treads are never carved away and the step-off stays roomy.
        landing(m, MID, 12, MID - 1);   // entrance stair foot (10,12,9)
        landing(m, MID + 2, 7, MID);    // 2nd stair foot (12,7,10)
        landing(m, MID + 4, 0, MID);    // grand-stair foot in the boss chamber (14,0,10)
        stairFlight(m, MID, MID - 5, SURFACE_Y, 4, 0, 1, 3);  // north door (y16) → antechamber (y12): descend +Z INTO the crypt
        stairFlight(m, MID - 3, MID, 12, 5, 1, 0, 2);         // antechamber (y12) → ossuary (y7): lands central (roomy)
        stairFlight(m, MID - 3, MID, 7, 7, 1, 0, 2);          // ossuary (y7) → Crypt of the Dead King (y0): cross, then dive

        // ---- dress the rooms ----
        antechamber(m, bes);
        ossuary(m, bes);
        sideCrypt(m, bes, 15, 15, 7);   // ossuary wing: spawner + coffins + loot
        sideCrypt(m, bes, 5, 5, 12);    // antechamber wing: spawner + coffins + loot
        deadKingCrypt(m, bes, entities);
        mausoleum(m, bes);

        StructureParts.linkFences(m);
        StructureParts.anchor(m, bes, new BlockPos(MID, 0, MID), "minecraft:deepslate_tiles");
        return new Built(m, bes, entities);
    }

    /**
     * Carve one rounded chamber centred on {@code (cx,cz)}: a tiled floor + brick ceiling across a per-column jittered
     * radius (organic, never a cylinder) and an air hollow between {@code floorY+1..ceilY-1}. Interiors are laid for
     * every room before any shell, so where two rooms overlap the shared air survives as a doorway.
     */
    private static void interior(Map<BlockPos, BlockState> m, int cx, int cz, int floorY, int ceilY, int rBase) {
        for (int x = 0; x <= MAX; x++) {
            for (int z = 0; z <= MAX; z++) {
                final int r2 = rBase * rBase + jitter(x, z);
                if (sq(x - cx) + sq(z - cz) <= r2) {
                    m.put(new BlockPos(x, floorY, z), floorMix(x, z));
                    m.put(new BlockPos(x, ceilY, z), deepslateMix(x, ceilY, z));
                    for (int y = floorY + 1; y < ceilY; y++) {
                        m.put(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    /** Wrap a chamber in a solid deepslate-brick shell ring, but leave any cell a neighbouring room already opened as air
     *  (so overlaps read as doorways, not walls) — keeping the room enclosed even where the island body thins. */
    private static void shell(Map<BlockPos, BlockState> m, int cx, int cz, int floorY, int ceilY, int rBase) {
        for (int x = 0; x <= MAX; x++) {
            for (int z = 0; z <= MAX; z++) {
                final int r2 = rBase * rBase + jitter(x, z);
                final int d2 = sq(x - cx) + sq(z - cz);
                if (d2 > r2 && d2 <= r2 + 12) {
                    for (int y = floorY; y <= ceilY; y++) {
                        final BlockPos p = new BlockPos(x, y, z);
                        final BlockState cur = m.get(p);
                        if (cur != null && cur.isAir()) {
                            continue; // a neighbour room carved through here → keep the doorway
                        }
                        m.put(p, deepslateMix(x, y, z));
                    }
                }
            }
        }
    }

    /**
     * A {@code width}-wide descending stair flight from {@code (x0,z0)} at {@code yTop}, dropping {@code steps} blocks
     * while heading in {@code (dx,dz)}: each tread is a deepslate-brick stair whose raised side faces uphill, with solid
     * rock tucked beneath (no floating steps) and three blocks of headroom carved above (punches through the floor/ceiling
     * between levels). {@code width} 3 gives the walk-in mausoleum mouth; 2 the internal flights.
     */
    private static void stairFlight(Map<BlockPos, BlockState> m, int x0, int z0, int yTop, int steps, int dx, int dz, int width) {
        final Direction uphill = dirOf(-dx, -dz);
        final int px = dz != 0 ? 1 : 0; // perpendicular unit for the extra width columns
        final int pz = dx != 0 ? 1 : 0;
        final int baseW = (width - 1) / 2; // centre the columns for odd widths, bias one side for even
        for (int i = 0; i <= steps; i++) {
            final int x = x0 + dx * i, z = z0 + dz * i, y = yTop - i;
            for (int w = 0; w < width; w++) {
                final int off = w - baseW;
                final int wx = x + px * off, wz = z + pz * off;
                m.put(new BlockPos(wx, y, wz), Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                        .setValue(StairBlock.FACING, uphill));
                m.put(new BlockPos(wx, y - 1, wz), deepslateMix(wx, y - 1, wz)); // solid tread underside
                m.put(new BlockPos(wx, y + 1, wz), Blocks.AIR.defaultBlockState());
                m.put(new BlockPos(wx, y + 2, wz), Blocks.AIR.defaultBlockState());
                m.put(new BlockPos(wx, y + 3, wz), Blocks.AIR.defaultBlockState());
            }
        }
    }

    /** Clear a 3×3 pocket (solid floor + 3 headroom) at a stair foot, so stepping off is never cramped against a wall. */
    private static void landing(Map<BlockPos, BlockState> m, int cx, int floorY, int cz) {
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int z = cz - 1; z <= cz + 1; z++) {
                m.put(new BlockPos(x, floorY, z), floorMix(x, z));
                for (int y = floorY + 1; y <= floorY + 3; y++) {
                    m.put(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    /** Antechamber (floor y12): a couple of stone coffins, cobwebbed corners, and a pair of pillars (no spawner — its
     *  spawner lives in the adjoining NW side crypt, off the stairway). */
    private static void antechamber(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        coffin(m, 13, 13, 13, Direction.WEST);
        coffin(m, 7, 13, 13, Direction.EAST);
        cobwebs(m, 13, 15, 13, 8, 15, 7);
        pillar(m, 13, 7, 13, 15);
        pillar(m, 7, 7, 13, 15);
    }

    /** Ossuary (floor y7): stacked bone-block bays, skull plinths, sculk creep, a coffin, a dungeon chest — placed off
     *  the z=MID stair lane; its spawner also lives in the adjoining SE side crypt. */
    private static void ossuary(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        for (final int[] b : new int[][]{{7, 6}, {13, 6}, {13, 14}}) {
            m.put(new BlockPos(b[0], 8, b[1]), Blocks.BONE_BLOCK.defaultBlockState());
            m.put(new BlockPos(b[0], 9, b[1]), Blocks.BONE_BLOCK.defaultBlockState());
        }
        for (final int[] s : new int[][]{{7, 14}, {13, 7}}) {
            m.put(new BlockPos(s[0], 8, s[1]), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
            m.put(new BlockPos(s[0], 9, s[1]), Blocks.SKELETON_SKULL.defaultBlockState());
        }
        for (final int[] s : new int[][]{{9, 6}, {11, 14}}) {
            m.put(new BlockPos(s[0], 7, s[1]), Blocks.SCULK.defaultBlockState());
        }
        coffin(m, 10, 8, 14, Direction.SOUTH);
        cobwebs(m, 6, 10, 6, 14, 10, 14);
        lootAt(m, bes, 6, 8, 8, Direction.EAST, "minecraft:chests/simple_dungeon");
    }

    /** A side crypt (its interior/shell already carved as a corner pocket): a dark catacombs-zombie spawner, a couple of
     *  coffins, cobwebs, and a modest dungeon chest. {@code (cx,cz)} is the pocket centre, {@code floorY} its floor. */
    private static void sideCrypt(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, int cx, int cz, int floorY) {
        darkSpawner(m, bes, cx, floorY + 1, cz, "irons_spellbooks:catacombs_zombie");
        coffin(m, cx - 1, floorY + 1, cz + 1, Direction.EAST);
        lootAt(m, bes, cx + 1, floorY + 1, cz - 1, Direction.NORTH, "minecraft:chests/simple_dungeon");
        m.put(new BlockPos(cx, floorY + 3, cz + 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(cx + 1, floorY + 3, cz), Blocks.COBWEB.defaultBlockState());
    }

    /**
     * The Crypt of the Dead King (floor y0): a raised sarcophagus dais at the centre with the dormant Dead King baked
     * atop it, two animated-armour honour guards flanking, a chiseled-deepslate throne backdrop with a wither skull,
     * ceremonial black candles + soul lanterns (safe here — no spawner to suppress), corner pillars, and the grand
     * reward chest. The lights make the boss chamber the one bright room at the bottom of the dark descent.
     */
    private static void deadKingCrypt(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes,
                                      Map<BlockPos, CompoundTag> entities) {
        for (int x = MID - 1; x <= MID + 1; x++) {
            for (int z = MID - 1; z <= MID + 1; z++) {
                m.put(new BlockPos(x, 1, z), Blocks.POLISHED_DEEPSLATE.defaultBlockState()); // sarcophagus dais
            }
        }
        m.put(new BlockPos(MID, 1, MID), Blocks.CHISELED_DEEPSLATE.defaultBlockState()); // the tomb lid
        entities.put(new BlockPos(MID, 2, MID), mob("irons_spellbooks:dead_king_corpse"));
        entities.put(new BlockPos(MID - 2, 1, MID - 2), mob("irons_spellbooks:cursed_armor_stand"));
        entities.put(new BlockPos(MID + 2, 1, MID - 2), mob("irons_spellbooks:cursed_armor_stand"));
        // Throne backdrop against the +Z wall.
        m.put(new BlockPos(MID, 1, MID + 3), Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.NORTH));
        m.put(new BlockPos(MID, 2, MID + 3), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(MID, 3, MID + 3), Blocks.WITHER_SKELETON_SKULL.defaultBlockState());
        m.put(new BlockPos(MID - 1, 2, MID + 3), litCandle());
        m.put(new BlockPos(MID + 1, 2, MID + 3), litCandle());
        // Soul-lantern sconces over the tomb.
        for (final int[] c : new int[][]{{MID - 4, MID}, {MID, MID + 4}, {MID, MID - 4}}) {
            m.put(new BlockPos(c[0], 4, c[1]), Blocks.SOUL_LANTERN.defaultBlockState());
        }
        // Corner pillars.
        pillar(m, MID - 3, MID - 3, 1, 5);
        pillar(m, MID + 3, MID - 3, 1, 5);
        pillar(m, MID - 3, MID + 3, 1, 5);
        pillar(m, MID + 3, MID + 3, 1, 5);
        lootAt(m, bes, MID - 3, 1, MID, Direction.EAST, "minecraft:chests/ancient_city"); // west wall, clear of the stair
    }

    /**
     * The surface mausoleum (centred at (MID, MID−2)) — a half-collapsed deepslate ruin poking from the mossy ground
     * (y16 floor → y19), with a wide, walk-in −Z doorway over the 3-block descent mouth, two guardian pillars with soul
     * lanterns, and a broken ruined <b>wing</b> annex off the +X side (its own sarcophagus + loot). Roofless, asymmetric.
     */
    private static void mausoleum(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        final int xlo = MID - 3, xhi = MID + 3;          // 7..13
        final int zlo = MID - 5, zhi = MID + 1;          // 5..11 (pushed north over the descent mouth)
        for (int y = SURFACE_Y + 1; y <= SURFACE_Y + 3; y++) {
            for (int x = xlo; x <= xhi; x++) {
                for (int z = zlo; z <= zhi; z++) {
                    if (!(x == xlo || x == xhi || z == zlo || z == zhi)) {
                        continue;
                    }
                    if (z == zlo && x >= MID - 1 && x <= MID + 1) {
                        continue; // the walk-in front (−Z) doorway (3 wide)
                    }
                    final int jitter = Math.floorMod(x * 5 + z * 11 + y * 3, 4);
                    if (y <= SURFACE_Y + 3 - (jitter == 0 ? 1 : 0)) {
                        m.put(new BlockPos(x, y, z), mossyMix(x, z));
                    }
                }
            }
        }
        // Arched front doorway + lantern-topped guardian pillars flanking it.
        m.put(new BlockPos(MID - 1, SURFACE_Y + 3, zlo), Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.TOP));
        m.put(new BlockPos(MID + 1, SURFACE_Y + 3, zlo), Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP));
        for (final int gx : new int[]{xlo, xhi}) {
            m.put(new BlockPos(gx, SURFACE_Y + 1, zlo), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
            m.put(new BlockPos(gx, SURFACE_Y + 2, zlo), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
            m.put(new BlockPos(gx, SURFACE_Y + 3, zlo), Blocks.SOUL_LANTERN.defaultBlockState());
        }
        // A broken ruined WING off the +X side: a roofless annex with a sarcophagus lid + a loot chest.
        final int wz = MID - 2; // annex centre-line matches the mausoleum body
        for (int x = xhi; x <= xhi + 3; x++) {
            for (int z = wz - 1; z <= wz + 1; z++) {
                m.put(new BlockPos(x, SURFACE_Y, z), mossyMix(x, z)); // annex floor
                final boolean edge = x == xhi + 3 || z == wz - 1 || z == wz + 1;
                final int jitter = Math.floorMod(x * 7 + z * 5, 4);
                if (edge && jitter != 0) {
                    m.put(new BlockPos(x, SURFACE_Y + 1, z), mossyMix(x, z)); // low ragged wall
                    if (jitter == 2) {
                        m.put(new BlockPos(x, SURFACE_Y + 2, z), mossyMix(x, z)); // the odd taller stub
                    }
                }
            }
        }
        m.remove(new BlockPos(xhi, SURFACE_Y + 1, wz)); // a doorway from the mausoleum into the wing
        m.put(new BlockPos(xhi + 1, SURFACE_Y, wz), Blocks.DEEPSLATE_TILE_SLAB.defaultBlockState()); // broken lid
        lootAt(m, bes, xhi + 2, SURFACE_Y, wz, Direction.WEST, "minecraft:chests/simple_dungeon");
        m.put(new BlockPos(xhi + 2, SURFACE_Y + 1, wz + 1), Blocks.COBWEB.defaultBlockState());
    }

    // ---- small shared parts ---------------------------------------------------------------------------------------

    /** A low stone coffin: a tiled slab body with a skeleton skull at the head, facing {@code head}. */
    private static void coffin(Map<BlockPos, BlockState> m, int x, int y, int z, Direction head) {
        m.put(new BlockPos(x, y, z), Blocks.DEEPSLATE_TILE_SLAB.defaultBlockState());
        m.put(new BlockPos(x + head.getStepX(), y, z + head.getStepZ()), Blocks.CHISELED_DEEPSLATE.defaultBlockState());
        m.put(new BlockPos(x + head.getStepX(), y + 1, z + head.getStepZ()), Blocks.SKELETON_SKULL.defaultBlockState());
    }

    /** A deepslate-brick pillar from {@code y0} to {@code y1}, polished base + cap. */
    private static void pillar(Map<BlockPos, BlockState> m, int x, int z, int y0, int y1) {
        for (int y = y0; y <= y1; y++) {
            m.put(new BlockPos(x, y, z), (y == y0 || y == y1)
                    ? Blocks.POLISHED_DEEPSLATE.defaultBlockState() : Blocks.DEEPSLATE_BRICKS.defaultBlockState());
        }
    }

    /** Sparse cobwebs at the two given corners of a room (age + spider dread). */
    private static void cobwebs(Map<BlockPos, BlockState> m, int x0, int y0, int z0, int x1, int y1, int z1) {
        m.put(new BlockPos(x0, y0, z0), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(x1, y1, z1), Blocks.COBWEB.defaultBlockState());
    }

    /** A vanilla mob spawner set into the floor with NO light nearby (block light suppresses spawns). */
    private static void darkSpawner(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, int x, int y, int z, String mob) {
        m.put(new BlockPos(x, y, z), Blocks.SPAWNER.defaultBlockState());
        bes.put(new BlockPos(x, y, z), StructureParts.mobSpawner(mob));
    }

    /** A loot chest facing {@code face}, bound to a vanilla loot table (Iron's injects its scroll/essence loot into these). */
    private static void lootAt(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, int x, int y, int z, Direction face, String table) {
        m.put(new BlockPos(x, y, z), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, face));
        bes.put(new BlockPos(x, y, z), StructureParts.lootChest(table));
    }

    private static BlockState litCandle() {
        return Blocks.BLACK_CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true);
    }

    /** A dormant/baked entity NBT — just its id; unknown ids simply fail to spawn (inert-safe). */
    private static CompoundTag mob(String id) {
        final CompoundTag t = new CompoundTag();
        t.putString("id", id);
        return t;
    }

    private static Direction dirOf(int dx, int dz) {
        if (dx > 0) return Direction.EAST;
        if (dx < 0) return Direction.WEST;
        if (dz > 0) return Direction.SOUTH;
        return Direction.NORTH;
    }

    /** A per-column radius wobble (−2..+2), used by both interior + shell so a room's ragged wall lines up. */
    private static int jitter(int x, int z) {
        return Math.floorMod(x * 13 + z * 7, 5) - 2;
    }

    /** Crypt masonry: mostly deepslate bricks, veined with cracked bricks, cobbled + tiles (deterministic, organic). */
    private static BlockState deepslateMix(int a, int b, int c) {
        return switch (Math.floorMod(a * 7 + b * 13 + c * 5, 8)) {
            case 2 -> Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
            case 3 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            case 4 -> Blocks.DEEPSLATE_TILES.defaultBlockState();
            case 5 -> Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState();
            default -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        };
    }

    /** Crypt floor: deepslate tiles worn with cracks, cobble rubble and the odd polished slab. */
    private static BlockState floorMix(int a, int c) {
        return switch (Math.floorMod(a * 5 + c * 11, 6)) {
            case 1 -> Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState();
            case 2 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            case 3 -> Blocks.POLISHED_DEEPSLATE.defaultBlockState();
            default -> Blocks.DEEPSLATE_TILES.defaultBlockState();
        };
    }

    /** Weathered surface masonry: deepslate bricks gone mossy, cracked, with cobbled patches. */
    private static BlockState mossyMix(int a, int c) {
        return switch (Math.floorMod(a * 7 + c * 13, 6)) {
            case 0 -> Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
            case 1 -> Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            case 2 -> Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            default -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        };
    }

    private static int sq(int n) {
        return n * n;
    }
}
