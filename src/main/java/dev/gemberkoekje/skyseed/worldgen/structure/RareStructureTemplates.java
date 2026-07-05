package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Code-authored templates for the {@code rare_structures} surprises that occasionally germinate in place of an
 * ordinary island: the snowy {@link #igloo()} (a rounded snow dome with a zombie villager to cure and an
 * igloo-loot chest), the {@link #abandonedCottage()} (a cobwebbed ruin of a Hamlet home, haunted by a zombie
 * villager), the {@link #oceanRuin()} (a flooded stone-brick basin with suspicious sand and a sunken chest), and the
 * {@link #impaledBoat()} (a wrecked longboat beset in sea ice and impaled on a blue-ice spike, on cold water isles).
 * The mobs themselves come from each rare structure's {@code mobs} pack (spawned at the island centre); these
 * templates are the architecture. Written to {@code .nbt} at dev time — see {@link DevStructureGenerator}.
 */
public final class RareStructureTemplates {
    private RareStructureTemplates() {}

    public static void generateInto(Path base) throws IOException {
        writeIfAbsent(base.resolve("igloo/igloo.nbt"), igloo());
        writeIfAbsent(base.resolve("abandoned/cottage.nbt"), abandonedCottage());
        writeIfAbsent(base.resolve("ocean_ruin/ruin.nbt"), oceanRuin());
        writeIfAbsent(base.resolve("evoker_cell/cell.nbt"), evokerCell());
        writeIfAbsent(base.resolve("vault_cell/cell.nbt"), vaultCell());
        writeIfAbsent(base.resolve("impaled_boat/wreck.nbt"), impaledBoat());
        writeIfAbsent(base.resolve("ruined_chapel/chapel.nbt"), ruinedChapel()); // VARIETYSTRUCTUREPLAN Band 3 (B25)
    }

    /**
     * The <b>Impaled Boat</b> — our small, hand-built answer to Iron's Spells' oversized Impaled Icebreaker (which
     * clips smaller islands and now lives only on huge_aquatic). A wrecked spruce longboat sits <em>beset in a floe of
     * sea ice</em> (a radius-5 disc of packed/blue ice with a few open water leads), a blue-ice spike punched up
     * <em>through</em> its shattered hull (the "impaled" motif), a snapped mast with a tattered sail, a soul-lantern on
     * the sternpost, and an underwater-ruin chest in the hold. Self-contained — it brings its own ice + water, so it
     * reads as a frozen wreck on a cold Aquatic isle or a Frozen one alike, regardless of the theme's own pond. Built on
     * an 11×11 footprint (anchor at the centre); all vanilla blocks, so it ships in the base mod. Iron's exploration
     * loot reaches the chest through the {@code underwater_ruin_big} global loot modifier. See {@code IRONSPELLSPLAN.md}.
     */
    private static Built impaledBoat() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.SPRUCE_PLANKS.defaultBlockState();
        final BlockState log = Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState();
        final BlockState blueIce = Blocks.BLUE_ICE.defaultBlockState();
        final BlockState packedIce = Blocks.PACKED_ICE.defaultBlockState();
        final int mid = 5; // 11×11 disc; anchor at the centre

        // The sea-ice floe (y=0): an irregular radius-5 disc of packed/blue ice, snow and clear ice — the frozen sea
        // the boat is beset in. A handful of interior cells are open water "leads" (cracks in the ice); all leads sit
        // well inside the rim so the water is walled by ice and never spills into the void.
        for (int x = 0; x <= 10; x++) {
            for (int z = 0; z <= 10; z++) {
                if (sq(x - mid) + sq(z - mid) <= 25) {
                    m.put(new BlockPos(x, 0, z), seaIce(x, z));
                }
            }
        }
        for (final BlockPos lead : new BlockPos[]{
                new BlockPos(5, 0, 2), new BlockPos(4, 0, 2), new BlockPos(6, 0, 8),
                new BlockPos(5, 0, 8), new BlockPos(3, 0, 3), new BlockPos(7, 0, 7)}) {
            m.put(lead, Blocks.WATER.defaultBlockState());
        }
        // A couple of ridged ice hummocks shoved up against the hull, for silhouette.
        for (final BlockPos berg : new BlockPos[]{new BlockPos(2, 1, 3), new BlockPos(8, 1, 7)}) {
            m.put(berg, packedIce);
        }

        // The wrecked longboat: keel along X (bow at x=2, square stern at x=8), beam z=4..6. The keel (y=1) sits on the
        // floe — a ship stuck ON the ice, not floating. Two planks are stove in (holes onto the ice below).
        for (int x = 2; x <= 8; x++) {
            for (int z = 4; z <= 6; z++) {
                if (x == 2 && z != 5) {
                    continue; // bow tapers to a point
                }
                m.put(new BlockPos(x, 1, z), plank);
            }
        }
        m.remove(new BlockPos(7, 1, 4));  // a stove-in hull plank
        m.remove(new BlockPos(3, 1, 6));

        // Gunwales (y=2): spruce stairs raked inward (tumblehome) down each side; a square plank transom at the stern.
        for (int x = 3; x <= 7; x++) {
            m.put(new BlockPos(x, 2, 4), rakedStair(Direction.SOUTH)); // port rail, faces inward
            m.put(new BlockPos(x, 2, 6), rakedStair(Direction.NORTH)); // starboard rail
        }
        for (int z = 4; z <= 6; z++) {
            m.put(new BlockPos(8, 2, z), plank);                       // stern transom
        }
        m.remove(new BlockPos(5, 2, 4)); // a broken length of rail
        // Prow: a raised stripped-log stem, with a stair stepping up to it.
        m.put(new BlockPos(2, 1, 5), log);
        m.put(new BlockPos(2, 2, 5), log);
        m.put(new BlockPos(2, 3, 5), log);
        m.put(new BlockPos(3, 2, 5), rakedStair(Direction.WEST));
        // Sternposts + a soul-lantern (cold blue glow) sitting on the port sternpost.
        m.put(new BlockPos(8, 3, 4), log);
        m.put(new BlockPos(8, 3, 6), log);
        m.put(new BlockPos(8, 4, 4), Blocks.SOUL_LANTERN.defaultBlockState());

        // The blue-ice spike, punched up THROUGH the hull just aft of centre (x=6, z=5): it stoves the keel and rail,
        // splays ice across the deck, and tapers to a packed-ice tip. This is the "impaled" motif.
        m.put(new BlockPos(6, 1, 5), blueIce);   // through the keel
        m.put(new BlockPos(6, 2, 5), blueIce);   // deck level — the hole
        m.put(new BlockPos(6, 2, 4), packedIce);  // ice splayed where it broke through
        m.put(new BlockPos(6, 2, 6), packedIce);
        m.put(new BlockPos(6, 3, 5), blueIce);
        m.put(new BlockPos(6, 4, 5), blueIce);
        m.put(new BlockPos(6, 5, 5), packedIce);
        m.put(new BlockPos(6, 6, 5), packedIce);  // tip

        // A snapped mast stub forward of the spike, with a tattered sail and a cobweb.
        m.put(new BlockPos(4, 2, 5), log);
        m.put(new BlockPos(4, 3, 5), log);
        m.put(new BlockPos(4, 3, 4), Blocks.WHITE_WOOL.defaultBlockState()); // sail scrap
        m.put(new BlockPos(4, 4, 5), Blocks.COBWEB.defaultBlockState());

        // The hold: a decorative supply barrel and the ruin chest (Iron's loot reaches it via the GLM).
        m.put(new BlockPos(7, 2, 5), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(5, 2, 5), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 2, 5), StructureParts.lootChest("minecraft:chests/underwater_ruin_big"));

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:packed_ice");
        return new Built(m, bes);
    }

    /** A spruce hull-rail stair raked inward (bottom half, straight) toward {@code facing}. */
    private static BlockState rakedStair(Direction facing) {
        return Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    /** A deterministic frozen-sea ice mix for the floe: mostly packed ice, some blue ice, a little clear ice / snow. */
    private static BlockState seaIce(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 13, 8)) {
            case 4, 5 -> Blocks.BLUE_ICE.defaultBlockState();
            case 6 -> Blocks.ICE.defaultBlockState();
            case 7 -> Blocks.SNOW_BLOCK.defaultBlockState();
            default -> Blocks.PACKED_ICE.defaultBlockState();
        };
    }

    /**
     * A rounded snow-block igloo (not an ice box): an octagonal 7×7 footprint, two wall courses, then a shoulder
     * that steps inward and a small cap — the silhouette curves in toward the top. A 1×2 doorway lets the player
     * in (the shoulder arches over it as a lintel, so the interior — and the zombie villager spawned in from the
     * mobs pack — still can't see the sky). A hearth, a workbench, the cleric's brewing stand + water cauldron, an
     * igloo-loot chest, a dim redstone torch and red carpet furnish it.
     */
    private static Built igloo() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState snow = Blocks.SNOW_BLOCK.defaultBlockState();
        final int mid = 3;

        // Dome by squared horizontal distance from the centre: floor disc, wall ring, an inset shoulder, a cap.
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 6; z++) {
                final int d2 = sq(x - mid) + sq(z - mid);
                if (d2 <= 13) {
                    m.put(new BlockPos(x, 0, z), snow);                 // floor (octagonal disc, corners cut)
                }
                if (d2 >= 9 && d2 <= 13) {
                    m.put(new BlockPos(x, 1, z), snow);                 // wall ring
                    m.put(new BlockPos(x, 2, z), snow);
                }
                if (d2 >= 2 && d2 <= 10) {
                    m.put(new BlockPos(x, 3, z), snow);                 // shoulder (steps in, arches over the door)
                }
                if (d2 <= 5) {
                    m.put(new BlockPos(x, 4, z), snow);                 // cap
                }
            }
        }
        // Doorway through the front wall; the y=3 shoulder already covers it from above.
        m.remove(new BlockPos(mid, 1, 0));
        m.remove(new BlockPos(mid, 2, 0));

        // The kit around the edges (centre kept clear for the zombie villager). Redstone torch = the dim igloo glow
        // (light 7 won't melt the snow), and it's block-light only, so the sealed interior still burns no undead.
        m.put(new BlockPos(1, 1, 1), Blocks.FURNACE.defaultBlockState());
        m.put(new BlockPos(1, 1, 2), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(5, 1, 1), Blocks.BREWING_STAND.defaultBlockState());
        m.put(new BlockPos(5, 1, 2), Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
        m.put(new BlockPos(5, 1, 5), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 5), StructureParts.lootChest("minecraft:chests/igloo_chest"));
        m.put(new BlockPos(1, 1, 5), Blocks.REDSTONE_TORCH.defaultBlockState());
        m.put(new BlockPos(2, 1, 4), Blocks.RED_CARPET.defaultBlockState());
        m.put(new BlockPos(3, 1, 4), Blocks.RED_CARPET.defaultBlockState());
        m.put(new BlockPos(4, 1, 4), Blocks.RED_CARPET.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:snow_block");
        return new Built(m, bes);
    }

    /**
     * A 7×7 oak cottage gone to ruin: cobwebs inside, gaps punched in the walls and roof, an open doorway, a
     * village-house chest — and, pointedly, no bed (the spawned zombie villager is the last "resident").
     */
    private static Built abandonedCottage() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.OAK_PLANKS.defaultBlockState();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState web = Blocks.COBWEB.defaultBlockState();
        final int max = 6, mid = 3;

        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), plank); // floor
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                if (perim) {
                    for (int h = 1; h <= 3; h++) {
                        m.put(new BlockPos(x, h, z), corner ? log : plank);
                    }
                }
                m.put(new BlockPos(x, 4, z), plank); // roof
            }
        }
        // Punch the ruin: missing wall blocks and roof holes let the weather (and the light) in.
        for (final BlockPos gap : new BlockPos[]{
                new BlockPos(max, 2, 2), new BlockPos(0, 3, 4), new BlockPos(4, 3, max), new BlockPos(2, 2, 0),
                new BlockPos(2, 4, 2), new BlockPos(4, 4, 4), new BlockPos(3, 4, 5), new BlockPos(5, 4, 2)}) {
            m.remove(gap);
        }
        // Open doorway in the front (z=0) wall — the door long since gone.
        m.remove(new BlockPos(mid, 1, 0));
        m.remove(new BlockPos(mid, 2, 0));

        // Cobwebs strung through the corners (never the centre, where the zombie villager spawns).
        for (final BlockPos w : new BlockPos[]{
                new BlockPos(1, 1, 1), new BlockPos(1, 2, 1), new BlockPos(5, 1, 5),
                new BlockPos(5, 3, 1), new BlockPos(1, 1, 5), new BlockPos(5, 2, 4)}) {
            m.put(w, web);
        }
        // A looted-but-not-empty chest, and a chunk of fallen masonry.
        m.put(new BlockPos(5, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
        bes.put(new BlockPos(5, 1, 1), StructureParts.lootChest("minecraft:chests/village/village_plains_house"));
        m.put(new BlockPos(1, 1, 4), Blocks.CRACKED_STONE_BRICKS.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:oak_planks");
        return new Built(m, bes);
    }

    /**
     * A 7×7 flooded stone-brick basin standing where the Aquatic pond would be: weathered walls hold a 2-deep
     * pool, suspicious sand hides archaeology finds on the floor, and a sunken chest carries underwater-ruin
     * loot. The lower two wall courses are kept intact to contain the water; the top course is broken for ruin.
     */
    private static Built oceanRuin() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState water = Blocks.WATER.defaultBlockState();
        final int max = 6, mid = 3;

        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), weathered(x, z)); // floor
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (perim) {
                    // Two intact lower courses contain the pool; a broken (sometimes-missing) top course.
                    m.put(new BlockPos(x, 1, z), weathered(x, z + 1));
                    m.put(new BlockPos(x, 2, z), weathered(x + 1, z));
                    if ((x * 5 + z) % 3 != 0) {
                        m.put(new BlockPos(x, 3, z), weathered(x, z));
                    }
                } else {
                    // Interior: a 2-deep pool over the floor.
                    m.put(new BlockPos(x, 1, z), water);
                    m.put(new BlockPos(x, 2, z), water);
                }
            }
        }
        // Suspicious sand/gravel sunk in the floor — brush them for ocean-ruin archaeology.
        for (final BlockPos s : new BlockPos[]{new BlockPos(2, 0, 2), new BlockPos(4, 0, 4), new BlockPos(2, 0, 4)}) {
            m.put(s, Blocks.SUSPICIOUS_SAND.defaultBlockState());
            bes.put(s, StructureParts.suspicious("minecraft:archaeology/ocean_ruin_warm"));
        }
        m.put(new BlockPos(4, 0, 2), Blocks.SUSPICIOUS_GRAVEL.defaultBlockState());
        bes.put(new BlockPos(4, 0, 2), StructureParts.suspicious("minecraft:archaeology/ocean_ruin_cold"));

        // A sunken chest in the pool corner.
        m.put(new BlockPos(1, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(1, 1, 1), StructureParts.lootChest("minecraft:chests/underwater_ruin_big"));
        // A couple of broken pillars rising clear of the water.
        for (int h = 1; h <= 4; h++) {
            m.put(new BlockPos(5, h, 5), weathered(5, h));
        }
        m.put(new BlockPos(5, 1, 2), weathered(0, 0));
        m.put(new BlockPos(5, 2, 2), Blocks.MOSSY_STONE_BRICKS.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:stone_bricks");
        return new Built(m, bes);
    }

    /**
     * The <b>Evoker Cell</b> — a mini woodland mansion (not a wooden box) holding one evoker, spawned via the rare
     * structure's {@code mobs} pack into the centre; on its death it drops the bootstrap <b>Totem of Undying</b>.
     * Cobblestone corner pillars and a foundation course, dark-oak plank walls with white-framed glass windows
     * (and the illagers' red windows flanking a dark-oak front door), a pitched dark-oak stair roof, and a
     * woodland-mansion chest with bookshelves inside. Rare on a Forest grown in a {@code dark_forest} biome.
     */
    private static Built evokerCell() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState plank = Blocks.DARK_OAK_PLANKS.defaultBlockState();
        final BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        final BlockState glass = Blocks.GLASS_PANE.defaultBlockState();
        final BlockState white = Blocks.WHITE_WOOL.defaultBlockState();
        final BlockState red = Blocks.RED_WOOL.defaultBlockState();
        final int max = 6, mid = 3; // 7×7, 5×5 interior

        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), plank); // floor
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (corner) {
                    for (int h = 1; h <= 4; h++) {
                        m.put(new BlockPos(x, h, z), cobble); // cobblestone corner pillars
                    }
                } else if (perim) {
                    m.put(new BlockPos(x, 1, z), cobble); // cobblestone foundation course
                    m.put(new BlockPos(x, 2, z), plank);  // dark-oak wall
                    m.put(new BlockPos(x, 3, z), plank);
                }
            }
        }
        // White-framed glass windows in the middle of the back and both side walls.
        window(m, new BlockPos(2, 0, max), new BlockPos(mid, 0, max), new BlockPos(4, 0, max), glass, white);
        window(m, new BlockPos(0, 0, 2), new BlockPos(0, 0, mid), new BlockPos(0, 0, 4), glass, white);
        window(m, new BlockPos(max, 0, 2), new BlockPos(max, 0, mid), new BlockPos(max, 0, 4), glass, white);
        // Front wall: a dark-oak door framed by wood, with the illagers' red windows out at the edges (next to
        // the cobblestone pillars) rather than crowding the door.
        // FACING=SOUTH sits the closed door flush with the −Z (front) face; FACING=NORTH recessed it inward (#72).
        m.put(new BlockPos(mid, 1, 0), Blocks.DARK_OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.FACING, Direction.SOUTH));
        m.put(new BlockPos(mid, 2, 0), Blocks.DARK_OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER).setValue(DoorBlock.FACING, Direction.SOUTH));
        for (final int wx : new int[]{1, 5}) {
            m.put(new BlockPos(wx, 2, 0), red);
            m.put(new BlockPos(wx, 3, 0), glass);
        }

        // Pitched dark-oak roof: an eaves course of stairs around the rim over a sealed plank ceiling, then a
        // smaller stepped course capped with planks — a hip roof, not a flat lid. Corner pillars show through.
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                final boolean edge = x == 0 || x == max || z == 0 || z == max;
                if (corner) {
                    // leave the cobblestone pillar top
                } else if (edge) {
                    m.put(new BlockPos(x, 4, z), roofStair(x, z, 0, max)); // eaves
                } else {
                    m.put(new BlockPos(x, 4, z), plank);                   // sealed ceiling
                }
                final boolean ring2 = (x == 1 || x == max - 1 || z == 1 || z == max - 1)
                        && (x >= 1 && x <= max - 1 && z >= 1 && z <= max - 1);
                if (ring2) {
                    m.put(new BlockPos(x, 5, z), roofStair(x, z, 1, max - 1)); // second step
                } else if (x >= 2 && x <= max - 2 && z >= 2 && z <= max - 2) {
                    m.put(new BlockPos(x, 5, z), plank);                       // ridge cap
                }
            }
        }
        // Inside — a red-carpet runner, bookshelves and a woodland-mansion chest; the centre stays clear for the evoker.
        for (final int[] c : new int[][]{{3, 1}, {3, 2}, {3, 4}, {3, 5}, {2, 5}, {4, 5}}) {
            m.put(new BlockPos(c[0], 1, c[1]), Blocks.RED_CARPET.defaultBlockState());
        }
        m.put(new BlockPos(1, 1, 5), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(1, 2, 5), Blocks.BOOKSHELF.defaultBlockState());
        m.put(new BlockPos(1, 1, 1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
        bes.put(new BlockPos(1, 1, 1), StructureParts.lootChest("minecraft:chests/woodland_mansion"));

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:dark_oak_planks");
        return new Built(m, bes);
    }

    /**
     * The <b>Ruined Chapel</b> (VARIETYSTRUCTUREPLAN Band 3, B25) — a vanilla <em>epic</em> showpiece so a mod-light pack
     * still occasionally gets a "wow" build, and the shared reward-bearing {@code explorable} floor for the thin themes
     * (Lush/Mushroom). A small stone-brick chapel fallen to ruin, <em>not a box</em>: a long nave whose roof has caved
     * in almost entirely (a couple of surviving eave rafters, the rest open to the sky, fallen blocks strewn across the
     * floor), arched side windows with shattered glass, a tall front bell-cote gable holding a hanging <b>bell</b> over a
     * broken doorway, and — at the raised chancel — a chiseled altar with a soul-lantern, candles and the reliquary chest
     * behind a broken rose window. The 2–3 undead of the theme's {@code mobs} pack haunt the open nave. All vanilla.
     */
    private static Built ruinedChapel() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState sb = Blocks.STONE_BRICKS.defaultBlockState();
        final BlockState glassRemnant = Blocks.GLASS_PANE.defaultBlockState();
        final int xMax = 6, zMax = 8; // 7 wide × 9 long

        // -- Floor: a weathered stone-brick mix (cracked / mossy patches, cobblestone rubble). ----------------------
        for (int x = 0; x <= xMax; x++) {
            for (int z = 0; z <= zMax; z++) {
                m.put(new BlockPos(x, 0, z), chapelStone(x, z));
            }
        }

        // -- Side walls (x0 / x6), three courses, ruined: arched windows at z2 (a glass remnant) and z5 (blown open),
        //    and a few blocks missing off the top for a jagged silhouette. -----------------------------------------
        for (final int wx : new int[]{0, xMax}) {
            for (int z = 1; z <= zMax - 1; z++) {
                m.put(new BlockPos(wx, 1, z), chapelStone(wx, z));
                if (z == 2) {
                    m.put(new BlockPos(wx, 2, z), glassRemnant);          // a surviving window pane
                } else if (z == 5) {
                    // blown-open window — leave y2 empty
                } else {
                    m.put(new BlockPos(wx, 2, z), chapelStone(wx, z + 1));
                }
                final boolean brokenTop = (wx == 0 && (z == 3 || z == 7)) || (wx == xMax && z == 6);
                if (!brokenTop) {
                    m.put(new BlockPos(wx, 3, z), chapelStone(wx + z, z));
                }
            }
        }

        // -- Back (chancel) wall z8 with a broken rose window of stained glass. -------------------------------------
        for (int x = 0; x <= xMax; x++) {
            m.put(new BlockPos(x, 1, zMax), chapelStone(x, zMax));
            if (x < 2 || x > 4) {
                m.put(new BlockPos(x, 2, zMax), chapelStone(x, zMax));
                m.put(new BlockPos(x, 3, zMax), chapelStone(x, zMax));
            }
        }
        m.put(new BlockPos(2, 2, zMax), Blocks.PURPLE_STAINED_GLASS_PANE.defaultBlockState());
        m.put(new BlockPos(3, 2, zMax), Blocks.RED_STAINED_GLASS_PANE.defaultBlockState());
        m.put(new BlockPos(4, 2, zMax), Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState());
        m.put(new BlockPos(3, 3, zMax), Blocks.ORANGE_STAINED_GLASS_PANE.defaultBlockState()); // (2,3)/(4,3) blown out

        // -- Front wall z0: a broken doorway at the centre, flanked by wall, rising into a bell-cote gable. ---------
        for (int x = 0; x <= xMax; x++) {
            if (x == 3) {
                m.put(new BlockPos(x, 3, 0), sb); // door lintel (y1/y2 are the broken opening)
            } else {
                for (int y = 1; y <= 3; y++) {
                    m.put(new BlockPos(x, y, 0), chapelStone(x, y));
                }
            }
        }
        // The bell-cote: two pillars (x2 / x4) up to y5 with a lintel, a hanging bell in the arch between them.
        for (int y = 4; y <= 5; y++) {
            m.put(new BlockPos(2, y, 0), sb);
            m.put(new BlockPos(4, y, 0), sb);
        }
        m.put(new BlockPos(3, 5, 0), sb); // cote lintel over the arch
        m.put(new BlockPos(3, 4, 0), Blocks.BELL.defaultBlockState()
                .setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.CEILING)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));

        // -- The caved roof: only a couple of eave rafters survive over the front bay + one cross-beam; the rest is
        //    open sky, with fallen blocks strewn across the nave. --------------------------------------------------
        for (int z = 1; z <= 2; z++) {
            m.put(new BlockPos(1, 4, z), chapelRafter(Direction.WEST));
            m.put(new BlockPos(5, 4, z), chapelRafter(Direction.EAST));
        }
        m.put(new BlockPos(3, 4, 3), sb); // a surviving cross-beam (the soul-lantern hangs from it)
        m.put(new BlockPos(3, 3, 3), Blocks.SOUL_LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true));
        for (final int[] r : new int[][]{{1, 1, 4}, {5, 1, 3}, {2, 1, 6}}) {          // fallen roof rubble
            m.put(new BlockPos(r[0], r[1], r[2]), Blocks.COBBLESTONE.defaultBlockState());
        }
        m.put(new BlockPos(4, 1, 5), Blocks.STONE_BRICK_STAIRS.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.HALF, net.minecraft.world.level.block.state.properties.Half.TOP)); // a fallen rafter

        // -- The chancel altar (raised step at the back) + the reliquary chest. ------------------------------------
        for (int x = 2; x <= 4; x++) {
            m.put(new BlockPos(x, 1, 7), Blocks.STONE_BRICK_SLAB.defaultBlockState()); // the dais step
        }
        m.put(new BlockPos(3, 2, 7), Blocks.CHISELED_STONE_BRICKS.defaultBlockState()); // the altar block
        m.put(new BlockPos(3, 3, 7), Blocks.CANDLE.defaultBlockState().setValue(BlockStateProperties.LIT, false));
        m.put(new BlockPos(2, 2, 7), Blocks.CANDLE.defaultBlockState()
                .setValue(BlockStateProperties.CANDLES, 2).setValue(BlockStateProperties.LIT, false));
        // The reliquary chest in front of the altar — air above (3,2,6) so it opens ([[skyseed-structure-chest-openable]]).
        m.put(new BlockPos(3, 1, 6), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        bes.put(new BlockPos(3, 1, 6), StructureParts.lootChest("skyseed:chests/ruined_chapel"));

        // -- Decay: cobwebs in the surviving corners. --------------------------------------------------------------
        for (final int[] c : new int[][]{{1, 3, 1}, {5, 2, 7}, {1, 2, 7}}) {
            m.put(new BlockPos(c[0], c[1], c[2]), Blocks.COBWEB.defaultBlockState());
        }

        StructureParts.anchor(m, bes, new BlockPos(3, 0, 3), "minecraft:stone_bricks");
        return new Built(m, bes);
    }

    /** A weathered chapel stone: mostly stone brick, with cracked + mossy patches keyed off the position. */
    private static BlockState chapelStone(int a, int b) {
        final int k = Math.floorMod(a * 3 + b * 5, 7);
        if (k == 0) {
            return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        }
        if (k == 1 || k == 4) {
            return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        }
        return Blocks.STONE_BRICKS.defaultBlockState();
    }

    /** A stone-brick eave rafter stair facing outward from the nave (so the surviving roof slopes up toward the ridge). */
    private static BlockState chapelRafter(Direction facing) {
        return Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    /** A 2-tall framed window: {@code b} is the central glass pane, {@code a}/{@code c} the wool frame (y2–y3). */
    private static void window(Map<BlockPos, BlockState> m, BlockPos a, BlockPos b, BlockPos c,
                               BlockState glass, BlockState frame) {
        for (int y = 2; y <= 3; y++) {
            m.put(a.above(y), frame);
            m.put(b.above(y), glass);
            m.put(c.above(y), frame);
        }
    }

    /** A dark-oak roof stair on a rim block, facing outward from the rim so the roof slopes up toward the centre. */
    private static BlockState roofStair(int x, int z, int lo, int hi) {
        final Direction facing;
        if (z == lo) {
            facing = Direction.NORTH;
        } else if (z == hi) {
            facing = Direction.SOUTH;
        } else if (x == lo) {
            facing = Direction.WEST;
        } else {
            facing = Direction.EAST;
        }
        return Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    /** Squared integer distance helper for the igloo dome. */
    private static int sq(int n) {
        return n * n;
    }

    /**
     * The <b>Vault Cell</b> — a small tuff/copper room buried (theme {@code sink}) in an Ancient island: two
     * {@code trial_spawner}s and a {@code vault}. Dig in, clear the spawners for <b>Trial Keys</b>, open the
     * vault for the reward — a self-contained mini trial-chamber and the reliable key source. The trial mechanics
     * are native 1.21 block-entity behaviour; the default vault already requires a trial key. See plan.
     */
    private static Built vaultCell() {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState air = Blocks.AIR.defaultBlockState();
        final int max = 6, mid = 3; // 7×7, 5×5×3 interior — carved out of solid fill when buried

        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), tuffMix(x, z));   // floor
                m.put(new BlockPos(x, 4, z), tuffMix(x, z));   // ceiling
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                for (int h = 1; h <= 3; h++) {
                    m.put(new BlockPos(x, h, z), perim ? tuffMix(x, h + z) : air); // walls / hollow interior
                }
            }
        }
        // Two trial spawners (a zombie wave and a skeleton wave) and a vault. Completing a spawner yields a
        // Trial Key (native); the default vault consumes a key and ejects the trial-chamber reward.
        m.put(new BlockPos(2, 1, 2), Blocks.TRIAL_SPAWNER.defaultBlockState());
        bes.put(new BlockPos(2, 1, 2), trialSpawner("minecraft:zombie"));
        m.put(new BlockPos(4, 1, 4), Blocks.TRIAL_SPAWNER.defaultBlockState());
        bes.put(new BlockPos(4, 1, 4), trialSpawner("minecraft:skeleton"));
        m.put(new BlockPos(1, 1, 3), Blocks.VAULT.defaultBlockState());

        StructureParts.anchor(m, bes, new BlockPos(mid, 0, mid), "minecraft:tuff_bricks");
        return new Built(m, bes);
    }

    /** A trial-spawner block entity configured to spawn waves of {@code mobId} (schema verified in-game). */
    private static CompoundTag trialSpawner(String mobId) {
        final CompoundTag entity = new CompoundTag();
        entity.putString("id", mobId);
        final CompoundTag spawnData = new CompoundTag();
        spawnData.put("entity", entity.copy());
        final CompoundTag potData = new CompoundTag();
        potData.put("entity", entity.copy());
        final CompoundTag potential = new CompoundTag();
        potential.put("data", potData);
        potential.putInt("weight", 1);
        final ListTag potentials = new ListTag();
        potentials.add(potential);
        final CompoundTag normal = new CompoundTag();
        normal.put("spawn_potentials", potentials);
        normal.putFloat("total_mobs", 4.0f);
        normal.putFloat("simultaneous_mobs", 2.0f);
        final CompoundTag be = new CompoundTag();
        be.putString("id", "minecraft:trial_spawner");
        be.put("spawn_data", spawnData);
        be.put("normal_config", normal);
        return be;
    }

    /** A deterministic tuff/copper mix for the trial-cell masonry (mostly tuff bricks). */
    private static BlockState tuffMix(int a, int b) {
        return switch (Math.floorMod(a * 7 + b * 5, 6)) {
            case 0 -> Blocks.CUT_COPPER.defaultBlockState();
            case 1 -> Blocks.CHISELED_TUFF.defaultBlockState();
            default -> Blocks.TUFF_BRICKS.defaultBlockState();
        };
    }

    /** A deterministic weathered stone-brick mix (plain / mossy / cracked) for a ruined, varied look. */
    private static BlockState weathered(int a, int b) {
        final int h = Math.floorMod(a * 7 + b * 13, 5);
        if (h == 0) {
            return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        }
        if (h == 1) {
            return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        }
        return Blocks.STONE_BRICKS.defaultBlockState();
    }
}
