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
 * The <b>War Barrow</b> — skyseed's home-curated stand-in for Iron's Spells' <b>Ancient Battleground</b>, whose own
 * jigsaw sprawls a surface graveyard/camp far too wide for a floating island. This is one deliberate ~19×19 surface
 * piece for a Huge Badlands mesa: a central earthen <b>barrow mound</b> with a hollow <b>crypt</b> (a skeleton spawner +
 * a coffin chest — the "inside"), a stepped <b>necromancer's altar</b> on top (soul-lantern posts, candles, a skull, the
 * reward chest), a ragged ring of <b>gravestones</b>, two tattered <b>A-frame war tents</b> (one caved in), a broken
 * <b>palisade siege line</b>, bone piles, skull pikes and dead bushes over trampled ground. No boxes: nothing is a
 * cube — the mound is a rounded dome, the altar is stepped-with-posts, the tents are A-frames, and the whole thing is a
 * scattered open-air composition. Centred by its central anchor. The mod's necromancer + cultists come from the theme
 * {@code mobs} pack (inert-safe). All vanilla blocks → ships in the base mod. See {@code IRONSTRUCTUREREBUILDPLAN.md}.
 */
public final class WarBarrowTemplates {
    private WarBarrowTemplates() {}

    private static final int MID = 9; // 19×19 footprint, anchor at the centre

    public static void generateInto(Path dir) throws IOException {
        writeIfAbsent(dir.resolve("barrow.nbt"), barrow());
    }

    private static Built barrow() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();

        ground(m);
        moundAndCrypt(m, bes);
        altar(m, bes);
        graveyard(m);
        tent(m, MID - 7, MID - 5, false);
        tent(m, MID + 6, MID + 5, true); // a caved-in tent
        palisade(m);
        details(m);

        StructureParts.linkFences(m); // join the tent poles / palisade / pikes
        StructureParts.anchor(m, bes, new BlockPos(MID, 0, MID), "minecraft:coarse_dirt");
        return new Built(m, bes);
    }

    /** Trampled battlefield ground (y=0): coarse dirt + worn paths + gravel/red-sand patches over the mesa, a disc r≈9. */
    private static void ground(Map<BlockPos, BlockState> m) {
        for (int x = 0; x <= 18; x++) {
            for (int z = 0; z <= 18; z++) {
                if (sq(x - MID) + sq(z - MID) <= 84) {
                    m.put(new BlockPos(x, 0, z), groundMix(x, z));
                }
            }
        }
    }

    /** The barrow: a rounded earthen dome (y1-4) with a hollow crypt niche opening to the −Z front, holding a skeleton
     *  spawner and a coffin chest (the buried "inside"). The mound's cap (y3-4 centre) carries the altar above. */
    private static void moundAndCrypt(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        for (int y = 1; y <= 4; y++) {
            final int r = switch (y) { case 1 -> 5; case 2 -> 5; case 3 -> 4; default -> 3; };
            for (int x = 0; x <= 18; x++) {
                for (int z = 0; z <= 18; z++) {
                    if (sq(x - MID) + sq(z - MID) <= r * r) {
                        m.put(new BlockPos(x, y, z), moundMix(x, y, z));
                    }
                }
            }
        }
        // Hollow the crypt (3 wide × 2 tall × 3 deep) toward the front, and cut a doorway through the mound face.
        for (int x = MID - 1; x <= MID + 1; x++) {
            for (int z = MID - 3; z <= MID - 1; z++) {
                m.put(new BlockPos(x, 1, z), Blocks.AIR.defaultBlockState());
                m.put(new BlockPos(x, 2, z), Blocks.AIR.defaultBlockState());
            }
        }
        m.put(new BlockPos(MID, 1, MID - 4), Blocks.AIR.defaultBlockState()); // doorway through the face
        m.put(new BlockPos(MID, 2, MID - 4), Blocks.AIR.defaultBlockState());
        m.put(new BlockPos(MID, 1, MID - 5), Blocks.AIR.defaultBlockState());
        // A packed-mud crypt floor + a couple of soul lights, the spawner and the coffin.
        for (int x = MID - 1; x <= MID + 1; x++) {
            for (int z = MID - 3; z <= MID - 1; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.MUD_BRICKS.defaultBlockState());
            }
        }
        m.put(new BlockPos(MID - 1, 1, MID - 2), Blocks.SPAWNER.defaultBlockState());
        bes.put(new BlockPos(MID - 1, 1, MID - 2), StructureParts.mobSpawner("minecraft:skeleton"));
        m.put(new BlockPos(MID + 1, 1, MID - 2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        bes.put(new BlockPos(MID + 1, 1, MID - 2), StructureParts.lootChest("minecraft:chests/buried_treasure"));
        // Keep the crypt DARK (no light block): a soul fire here would suppress the skeleton spawner exactly like a
        // torch in a dungeon. Soul soil + cobwebs give the tomb its dread without lighting it, so the dead actually rise.
        m.put(new BlockPos(MID, 0, MID - 3), Blocks.SOUL_SOIL.defaultBlockState());
        m.put(new BlockPos(MID, 2, MID - 1), Blocks.COBWEB.defaultBlockState());
        m.put(new BlockPos(MID + 1, 2, MID - 2), Blocks.COBWEB.defaultBlockState());
    }

    /** The necromancer's altar on the mound cap (y5-7): a polished-blackstone dais, soul-lantern corner posts, a candled
     *  plinth topped with a skeleton skull, and the reward chest. Stepped + posted, deliberately not a block. */
    private static void altar(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes) {
        final BlockState black = Blocks.POLISHED_BLACKSTONE.defaultBlockState();
        for (int x = MID - 1; x <= MID + 1; x++) {
            for (int z = MID - 1; z <= MID + 1; z++) {
                m.put(new BlockPos(x, 5, z), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState()); // dais
            }
        }
        for (final int[] c : new int[][]{{MID - 1, MID - 1}, {MID + 1, MID - 1}, {MID - 1, MID + 1}, {MID + 1, MID + 1}}) {
            m.put(new BlockPos(c[0], 6, c[1]), Blocks.POLISHED_BLACKSTONE_WALL.defaultBlockState());
            m.put(new BlockPos(c[0], 7, c[1]), Blocks.SOUL_LANTERN.defaultBlockState());
        }
        m.put(new BlockPos(MID, 6, MID), Blocks.CHISELED_POLISHED_BLACKSTONE.defaultBlockState()); // plinth
        m.put(new BlockPos(MID, 7, MID), Blocks.SKELETON_SKULL.defaultBlockState());
        m.put(new BlockPos(MID - 1, 6, MID), Blocks.BLACK_CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true));
        m.put(new BlockPos(MID + 1, 6, MID), Blocks.BLACK_CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, true));
        m.put(new BlockPos(MID, 6, MID + 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        bes.put(new BlockPos(MID, 6, MID + 1), StructureParts.lootChest("minecraft:chests/pillager_outpost"));
        m.put(new BlockPos(MID, 6, MID - 1), black); // a step down off the front
    }

    /** A ragged ring of gravestones (a raised grave + a headstone with a sloped cap), materials/rotation varied. */
    private static void graveyard(Map<BlockPos, BlockState> m) {
        final int[][] spots = {{MID - 7, MID}, {MID + 7, MID}, {MID, MID - 7}, {MID, MID + 7},
                {MID - 6, MID - 5}, {MID + 6, MID - 4}, {MID - 5, MID + 6}, {MID + 5, MID + 6}, {MID - 7, MID + 3}};
        for (final int[] s : spots) {
            graveStone(m, s[0], s[1]);
        }
    }

    private static void graveStone(Map<BlockPos, BlockState> m, int x, int z) {
        final int h = Math.floorMod(x * 7 + z * 13, 4);
        final BlockState wall = switch (h) {
            case 0 -> Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState();
            case 1 -> Blocks.COBBLED_DEEPSLATE_WALL.defaultBlockState();
            case 2 -> Blocks.STONE_BRICK_WALL.defaultBlockState();
            default -> Blocks.COBBLESTONE_WALL.defaultBlockState();
        };
        m.put(new BlockPos(x, 1, z), Blocks.COARSE_DIRT.defaultBlockState()); // the grave mound in front
        m.put(new BlockPos(x, 1, z - 1), wall);                                // the headstone
        m.put(new BlockPos(x, 2, z - 1), (h % 2 == 0)
                ? Blocks.STONE_BRICK_SLAB.defaultBlockState() : Blocks.COBBLESTONE_SLAB.defaultBlockState()); // cap
    }

    /** An A-frame war tent over a 3×3 footprint: fence poles + a wool ridge sloping to stairs; {@code caved} drops the roof. */
    private static void tent(Map<BlockPos, BlockState> m, int cx, int cz, boolean caved) {
        final BlockState wool = caved ? Blocks.GRAY_WOOL.defaultBlockState() : Blocks.BROWN_WOOL.defaultBlockState();
        // four corner poles
        for (final int[] c : new int[][]{{cx - 1, cz - 1}, {cx + 1, cz - 1}, {cx - 1, cz + 1}, {cx + 1, cz + 1}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.OAK_FENCE.defaultBlockState());
            if (!caved) {
                m.put(new BlockPos(c[0], 2, c[1]), Blocks.OAK_FENCE.defaultBlockState());
            }
        }
        if (caved) {
            // collapsed: the roof lies over the frame at y=1-2, a heap of wool.
            for (int x = cx - 1; x <= cx + 1; x++) {
                m.put(new BlockPos(x, 2, cz), wool);
                m.put(new BlockPos(x, 1, cz + 1), wool);
            }
            return;
        }
        // ridge beam along X at cz, y=3; sloped wool sides down to the eaves.
        for (int x = cx - 1; x <= cx + 1; x++) {
            m.put(new BlockPos(x, 3, cz), wool);
            m.put(new BlockPos(x, 2, cz - 1), Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
            m.put(new BlockPos(x, 2, cz + 1), Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        }
        m.put(new BlockPos(cx, 1, cz), Blocks.CAMPFIRE.defaultBlockState()); // a hearth inside
    }

    /** A broken defensive palisade — a run of spruce fences along the −Z front with gaps, backed by fallen logs. */
    private static void palisade(Map<BlockPos, BlockState> m) {
        final int z = MID - 8;
        for (int x = MID - 5; x <= MID + 5; x++) {
            if (Math.floorMod(x * 5 + 3, 4) == 0) {
                continue; // a smashed gap in the line
            }
            m.put(new BlockPos(x, 1, z), Blocks.SPRUCE_FENCE.defaultBlockState());
            if (Math.floorMod(x, 3) != 0) {
                m.put(new BlockPos(x, 2, z), Blocks.SPRUCE_FENCE.defaultBlockState());
            }
        }
        // a couple of felled logs behind the line
        for (int x = MID - 2; x <= MID; x++) {
            m.put(new BlockPos(x, 1, z + 1), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, net.minecraft.core.Direction.Axis.X));
        }
    }

    /** Scattered aftermath: bone piles, skull pikes, dead bushes and a doused soul-campfire. */
    private static void details(Map<BlockPos, BlockState> m) {
        for (final int[] b : new int[][]{{MID - 4, MID + 3}, {MID + 4, MID - 2}, {MID - 6, MID - 2}}) {
            m.put(new BlockPos(b[0], 1, b[1]), Blocks.BONE_BLOCK.defaultBlockState());
        }
        // skull pikes: a fence topped with a skeleton skull
        for (final int[] p : new int[][]{{MID - 5, MID + 2}, {MID + 5, MID + 1}, {MID + 2, MID - 6}}) {
            m.put(new BlockPos(p[0], 1, p[1]), Blocks.DARK_OAK_FENCE.defaultBlockState());
            m.put(new BlockPos(p[0], 2, p[1]), Blocks.DARK_OAK_FENCE.defaultBlockState());
            m.put(new BlockPos(p[0], 3, p[1]), Blocks.SKELETON_SKULL.defaultBlockState());
        }
        m.put(new BlockPos(MID - 3, 1, MID + 5), Blocks.SOUL_CAMPFIRE.defaultBlockState());
        for (final int[] d : new int[][]{{MID + 3, MID + 4}, {MID - 2, MID - 6}, {MID + 6, MID - 1}, {MID - 6, MID + 4}, {MID + 4, MID + 6}}) {
            m.put(new BlockPos(d[0], 1, d[1]), Blocks.DEAD_BUSH.defaultBlockState());
        }
    }

    /** Trampled ground mix: mostly coarse dirt, worn dirt-path, gravel + red-sand patches. */
    private static BlockState groundMix(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 8)) {
            case 0, 1 -> Blocks.DIRT_PATH.defaultBlockState();
            case 2 -> Blocks.GRAVEL.defaultBlockState();
            case 3 -> Blocks.RED_SAND.defaultBlockState();
            default -> Blocks.COARSE_DIRT.defaultBlockState();
        };
    }

    /** Earthen barrow mix: coarse dirt, packed mud, mud bricks. */
    private static BlockState moundMix(int a, int b, int c) {
        return switch (Math.floorMod(a * 5 + b * 7 + c * 11, 6)) {
            case 0, 1 -> Blocks.PACKED_MUD.defaultBlockState();
            case 2 -> Blocks.MUD_BRICKS.defaultBlockState();
            default -> Blocks.COARSE_DIRT.defaultBlockState();
        };
    }

    private static int sq(int n) {
        return n * n;
    }
}
