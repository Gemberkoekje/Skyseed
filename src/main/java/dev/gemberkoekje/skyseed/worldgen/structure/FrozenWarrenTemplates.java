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
 * The <b>Frozen Warren</b> — skyseed's home-curated stand-in for Iron's Spells' <b>Ice Spider Den</b>, whose own jigsaw
 * sprawls a deep cavern that hangs out under a floating island. This is one deliberate, self-contained piece with a
 * <em>known</em> footprint and depth, so it always fits inside a Huge Frozen island's body: a surface ice-fort mouth
 * (echoing the mod's entrance), a ladder shaft down, and a single bounded ice cavern below — carved into the island
 * (explicit air) and lined with an ice shell (so the interior reads as an ice den and is enclosed even where the island
 * body thins). A cave-spider spawner + cobweb nests fill it (the mod's cryomancer/ice-spiders come from the theme's
 * {@code mobs} pack); an underwater-ruin chest on an ice dais is the reward. No boxes: the cavern is an irregular blob
 * with ice pillars, and the fort is an organic ice mound with spikes. Centred on the island by its central anchor.
 *
 * <p>The piece is ~13×13 and ~15 tall; the host rare {@code sink}s it 10 so the fort floor (structure y=10) sits at the
 * island surface, the cavern (y=1–9) buried below, the fort + spikes (y=11–17) above. See {@code IRONSTRUCTUREREBUILDPLAN.md}.
 */
public final class FrozenWarrenTemplates {
    private FrozenWarrenTemplates() {}

    private static final int MID = 6;           // 13×13 footprint, anchor at the centre
    private static final int SURFACE_Y = 10;    // the fort floor; sink 10 seats this at the island surface

    public static void generateInto(Path dir) throws IOException {
        writeIfAbsent(dir.resolve("warren.nbt"), warren());
    }

    private static Built warren() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState packed = Blocks.PACKED_ICE.defaultBlockState();
        final BlockState blue = Blocks.BLUE_ICE.defaultBlockState();
        final BlockState air = Blocks.AIR.defaultBlockState();

        // ===== BURIED ICE CAVERN (y0 floor, y1-8 hollow, y9 ceiling) — carved into the island body =====
        // Air radius per level: narrow at the floor, a bulge through the middle, doming shut toward the ceiling. Each
        // column's radius is jittered so the walls read organic, not a cylinder. Interior = air (carves the island);
        // a 1-ring ice shell rings it (encloses the cavern even where the island body thins); solid ice floor/ceiling.
        final int[] airR2 = {0, 15, 24, 25, 25, 23, 18, 14, 8}; // index by y (0..8)
        m.put(new BlockPos(MID, 0, MID), packed); // floor centre (anchor overwrites it last)
        for (int x = 0; x <= 12; x++) {
            for (int z = 0; z <= 12; z++) {
                final int d2 = sq(x - MID) + sq(z - MID);
                if (d2 <= 40) {
                    m.put(new BlockPos(x, 0, z), iceMix(x, 0, z));    // solid ice floor disc (r≈6)
                    m.put(new BlockPos(x, 9, z), iceMix(x, 9, z));    // solid ice ceiling disc
                }
                for (int y = 1; y <= 8; y++) {
                    final int jitter = Math.floorMod(x * 13 + z * 7 + y * 5, 5) - 2; // -2..+2
                    final int r2 = airR2[y] + jitter;
                    if (d2 <= r2) {
                        m.put(new BlockPos(x, y, z), air);            // carve the hollow
                    } else if (d2 <= r2 + 9) {
                        m.put(new BlockPos(x, y, z), iceMix(x, y, z)); // ice shell wall
                    }
                }
            }
        }
        // Three ice pillars floor→ceiling break the room up (no empty box) and read as a frozen cavern's columns.
        for (final int[] c : new int[][]{{MID - 3, MID + 2}, {MID + 3, MID - 2}, {MID + 2, MID + 3}}) {
            for (int y = 1; y <= 8; y++) {
                m.put(new BlockPos(c[0], y, c[1]), (y % 3 == 0) ? blue : packed);
            }
        }
        // Cobweb spider nests slung in the upper corners of the cavern.
        for (final BlockPos w : new BlockPos[]{
                new BlockPos(MID - 3, 7, MID - 3), new BlockPos(MID + 3, 7, MID + 3),
                new BlockPos(MID - 2, 8, MID + 2), new BlockPos(MID + 3, 6, MID - 3)}) {
            m.put(w, Blocks.COBWEB.defaultBlockState());
        }
        // A cave-spider spawner (base-mod spider action; the mod's ice-spiders/cryomancer arrive via the theme mobs pack).
        m.put(new BlockPos(MID - 3, 1, MID), Blocks.SPAWNER.defaultBlockState());
        bes.put(new BlockPos(MID - 3, 1, MID), StructureParts.mobSpawner("minecraft:cave_spider"));
        // The reward: an ice-tomb dais with a chest (Iron's essence reaches it via the underwater_ruin_big GLM).
        for (final int[] c : new int[][]{{MID + 3, MID}, {MID + 4, MID}, {MID + 3, MID - 1}, {MID + 3, MID + 1}}) {
            m.put(new BlockPos(c[0], 1, c[1]), blue);
        }
        m.put(new BlockPos(MID + 3, 2, MID), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(MID + 3, 2, MID), StructureParts.lootChest("minecraft:chests/underwater_ruin_big"));
        m.put(new BlockPos(MID + 4, 2, MID), Blocks.SOUL_LANTERN.defaultBlockState()); // cold light over the tomb

        // ===== LADDER SHAFT (cavern floor y1 → surface y10) =====
        // A backing ice column with ladders on its south face, and the shaft cut up through the ceiling to the fort.
        for (int y = 1; y <= SURFACE_Y; y++) {
            m.put(new BlockPos(MID, y, MID - 1), iceMix(MID, y, MID - 1)); // backing wall
            m.put(new BlockPos(MID, y, MID), Blocks.LADDER.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
            m.put(new BlockPos(MID, y, MID + 1), Blocks.AIR.defaultBlockState()); // climb space
        }
        m.put(new BlockPos(MID, 9, MID), Blocks.LADDER.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)); // through the ceiling disc

        // ===== SURFACE FORT (y10 floor → y14 cap) + ice spikes — the entrance mouth, echoing the mod =====
        // Floor: a frozen platform flush with the pad; the shaft mouth stays open.
        for (int x = 1; x <= 11; x++) {
            for (int z = 1; z <= 11; z++) {
                if (sq(x - MID) + sq(z - MID) <= 30) {
                    m.put(new BlockPos(x, SURFACE_Y, z), iceMix(x, SURFACE_Y, z));
                }
            }
        }
        m.put(new BlockPos(MID, SURFACE_Y, MID), air);       // shaft mouth
        m.put(new BlockPos(MID, SURFACE_Y, MID + 1), air);
        // Fort wall ring (y11-13): an irregular ice rampart with a doorway on the +Z side leading in to the shaft.
        for (int y = SURFACE_Y + 1; y <= SURFACE_Y + 3; y++) {
            for (int x = 1; x <= 11; x++) {
                for (int z = 1; z <= 11; z++) {
                    final int d2 = sq(x - MID) + sq(z - MID);
                    final int jitter = Math.floorMod(x * 5 + z * 11 + y * 3, 4); // ragged top edge
                    if (d2 >= 18 && d2 <= 30 - (y - SURFACE_Y) * 3 && jitter != 0) {
                        m.put(new BlockPos(x, y, z), iceMix(x, y, z));
                    }
                }
            }
        }
        // Punch a doorway (the mouth) through the +Z wall.
        for (int y = SURFACE_Y + 1; y <= SURFACE_Y + 2; y++) {
            m.remove(new BlockPos(MID, y, MID + 4));
            m.remove(new BlockPos(MID, y, MID + 5));
            m.remove(new BlockPos(MID - 1, y, MID + 5));
            m.remove(new BlockPos(MID + 1, y, MID + 5));
        }
        // Ice spikes clawing up from the rampart — the frozen-fort silhouette (tapering packed-ice fangs).
        iceSpike(m, MID - 4, MID - 3, SURFACE_Y + 1, 5, packed, blue);
        iceSpike(m, MID + 4, MID + 3, SURFACE_Y + 1, 6, packed, blue);
        iceSpike(m, MID + 3, MID - 4, SURFACE_Y + 1, 4, packed, blue);
        iceSpike(m, MID - 3, MID + 4, SURFACE_Y, 4, packed, blue);

        StructureParts.anchor(m, bes, new BlockPos(MID, 0, MID), "minecraft:packed_ice");
        return new Built(m, bes);
    }

    /** A tapering vertical ice fang at {@code (x,z)}: {@code height} blocks from {@code y0}, blue-ice core, packed tip. */
    private static void iceSpike(Map<BlockPos, BlockState> m, int x, int z, int y0, int height,
                                 BlockState packed, BlockState blue) {
        for (int i = 0; i < height; i++) {
            m.put(new BlockPos(x, y0 + i, z), i < height - 2 ? blue : packed);
        }
    }

    /** A deterministic frozen mix for the warren: mostly packed ice, blue-ice veins, a little clear ice. */
    private static BlockState iceMix(int a, int b, int c) {
        return switch (Math.floorMod(a * 7 + b * 13 + c * 5, 7)) {
            case 5 -> Blocks.BLUE_ICE.defaultBlockState();
            case 6 -> Blocks.ICE.defaultBlockState();
            default -> Blocks.PACKED_ICE.defaultBlockState();
        };
    }

    private static int sq(int n) {
        return n * n;
    }
}
