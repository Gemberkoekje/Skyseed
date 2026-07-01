package dev.gemberkoekje.skyseed.worldgen.structure;

import static dev.gemberkoekje.skyseed.worldgen.structure.StructureParts.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * BWG-styled village piece sets — Skyseed's own jigsaw village (the {@link TradePostTemplates} mechanics: a solid
 * {@code square} start, a marker-deck {@code streets} pool rendered by {@link PathSurfacer}, {@code lot}s that hang the
 * shops/houses, the {@code shop_} cap, bed→villager spawning) rebuilt in the block palettes of Oh The Biomes We've
 * Gone's six village styles (BWGVILLAGEPLAN). The buildings are <em>inspired by</em> BWG's villages, not copies.
 *
 * <p><b>Hermetic BWG blocks (no BWG on the classpath).</b> A {@link Mat} is a vanilla <b>analog</b> {@link BlockState}
 * (used for the shape math, adjacency and property serialisation — BWG blocks subclass the vanilla
 * {@code StairBlock}/{@code SlabBlock}/{@code FenceBlock}/{@code DoorBlock}/…) plus an optional
 * {@code biomeswevegone:} id. {@link #set} records that id in a {@code modNames} side-map; {@link StructureWriter}
 * then serialises the analog's palette compound but swaps in the BWG {@code Name}. The emitted {@code .nbt} carry
 * BWG ids and are only ever loaded when a village assembles over a BWG biome (impossible without BWG) — so the whole
 * set is inert without the mod, exactly like the wood/flower bands.
 */
public final class BwgVillageTemplates {
    private BwgVillageTemplates() {}

    /** A FLOOR-standing grindstone (the weaponsmith's job site). The default grindstone state is WALL-attached, which
     *  pops free when placed free-standing with no wall behind it. */
    private static final BlockState FLOOR_GRINDSTONE =
            Blocks.GRINDSTONE.defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);

    /** A material: a vanilla analog state for shape/property work + an optional BWG id overriding the palette Name. */
    public record Mat(BlockState analog, String id) {
        public static Mat v(Block b) { return new Mat(b.defaultBlockState(), null); }
        public static Mat v(BlockState s) { return new Mat(s, null); }
        public static Mat bwg(String id, Block analog) { return new Mat(analog.defaultBlockState(), "biomeswevegone:" + id); }
        Block block() { return analog.getBlock(); }
    }

    /**
     * A village style's pool namespace + material set. Slots mirror the vanilla {@link TradePostTemplates.Palette}:
     * {@code wall} body on a {@code base} masonry course with an {@code accent} course above and {@code post} corner
     * logs, a {@code stairs}/{@code slab} roof, {@code door}s, {@code glass} windows, {@code fence}s, a
     * {@code foundation}/floor, a {@code bookshelf} for the librarian, a themed {@code lantern}, and a signature
     * {@code flora} block for gardens/plots. Each {@link Mat} is either a vanilla block ({@code Mat.v}) or a BWG block
     * via a vanilla analog ({@code Mat.bwg}); a style is any mix. All ids are verified against the BWG 2.6.0 jar.
     */
    public record Style(String pool, Mat wall, Mat post, Mat stairs, Mat slab, Mat door, Mat foundation, Mat glass,
                        Mat fence, Mat accent, Mat base, Mat bookshelf, Mat lantern, Mat flora) {}

    /** Skyris — an elegant elevated masonry town: skyris (teal) wood over white-dacite brick on polished andesite,
     *  dark-prismarine trim and light-blue glass. */
    public static final Style SKYRIS = new Style("skyseed:village_skyris",
            Mat.bwg("skyris_planks", Blocks.OAK_PLANKS), Mat.bwg("stripped_skyris_log", Blocks.STRIPPED_OAK_LOG),
            Mat.bwg("skyris_stairs", Blocks.OAK_STAIRS), Mat.bwg("skyris_slab", Blocks.OAK_SLAB),
            Mat.bwg("skyris_door", Blocks.OAK_DOOR), Mat.v(Blocks.POLISHED_ANDESITE),
            Mat.v(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE), Mat.bwg("skyris_fence", Blocks.OAK_FENCE),
            Mat.v(Blocks.DARK_PRISMARINE), Mat.bwg("white_dacite_bricks", Blocks.STONE),
            Mat.bwg("skyris_bookshelf", Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.v(Blocks.LILY_OF_THE_VALLEY));

    /** Forgotten — an overgrown ruin reclaimed by nature: vanilla mossy-stone-brick masonry with florus (yellow-bloom)
     *  wood accents, moss and terracotta. Mostly vanilla + a few BWG florus pieces. */
    public static final Style FORGOTTEN = new Style("skyseed:village_forgotten",
            Mat.v(Blocks.MOSSY_STONE_BRICKS), Mat.bwg("stripped_florus_wood", Blocks.STRIPPED_OAK_LOG),
            Mat.v(Blocks.MOSSY_STONE_BRICK_STAIRS), Mat.v(Blocks.MOSSY_STONE_BRICK_SLAB),
            Mat.bwg("florus_door", Blocks.OAK_DOOR), Mat.v(Blocks.GRAVEL),
            Mat.v(Blocks.WHITE_STAINED_GLASS_PANE), Mat.bwg("florus_fence", Blocks.OAK_FENCE),
            Mat.v(Blocks.LIME_TERRACOTTA), Mat.v(Blocks.COBBLESTONE),
            Mat.bwg("florus_bookshelf", Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.v(Blocks.MOSS_CARPET));

    /** Pumpkin Patch — an autumnal dark-oak & dacite village. Vanilla dark oak + BWG dacite masonry; pumpkins are the
     *  signature flora. */
    public static final Style PUMPKIN_PATCH = new Style("skyseed:village_pumpkin_patch",
            Mat.v(Blocks.DARK_OAK_PLANKS), Mat.v(Blocks.DARK_OAK_LOG),
            Mat.v(Blocks.DARK_OAK_STAIRS), Mat.v(Blocks.DARK_OAK_SLAB),
            Mat.v(Blocks.DARK_OAK_DOOR), Mat.bwg("dacite_cobblestone", Blocks.STONE),
            Mat.v(Blocks.GLASS_PANE), Mat.v(Blocks.DARK_OAK_FENCE),
            Mat.v(Blocks.ORANGE_TERRACOTTA), Mat.bwg("dacite_bricks", Blocks.STONE),
            Mat.v(Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.v(Blocks.PUMPKIN));

    /** Red Rock — a southwestern adobe pueblo: red-rock brick masonry, baobab timber, and a cattail-thatch roof. */
    public static final Style RED_ROCK = new Style("skyseed:village_red_rock",
            Mat.bwg("red_rock_bricks", Blocks.STONE), Mat.bwg("baobab_log", Blocks.OAK_LOG),
            Mat.bwg("cattail_thatch_stairs", Blocks.OAK_STAIRS), Mat.bwg("cattail_thatch_slab", Blocks.OAK_SLAB),
            Mat.bwg("baobab_door", Blocks.OAK_DOOR), Mat.v(Blocks.RED_SANDSTONE),
            Mat.v(Blocks.GLASS_PANE), Mat.bwg("baobab_fence", Blocks.OAK_FENCE),
            Mat.bwg("chiseled_red_rock_bricks", Blocks.STONE), Mat.bwg("red_rock", Blocks.STONE),
            Mat.bwg("baobab_bookshelf", Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.bwg("blooming_aloe_vera", Blocks.DEAD_BUSH));

    /** Salem — a colonial / gothic New-England town: witch-hazel timber over stone, gray glass. */
    public static final Style SALEM = new Style("skyseed:village_salem",
            Mat.bwg("witch_hazel_planks", Blocks.OAK_PLANKS), Mat.bwg("witch_hazel_log", Blocks.OAK_LOG),
            Mat.bwg("witch_hazel_stairs", Blocks.OAK_STAIRS), Mat.bwg("witch_hazel_slab", Blocks.OAK_SLAB),
            Mat.bwg("witch_hazel_door", Blocks.OAK_DOOR), Mat.v(Blocks.STONE),
            Mat.v(Blocks.GRAY_STAINED_GLASS_PANE), Mat.bwg("witch_hazel_fence", Blocks.OAK_FENCE),
            Mat.v(Blocks.OAK_PLANKS), Mat.v(Blocks.COBBLESTONE),
            Mat.bwg("witch_hazel_bookshelf", Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.bwg("white_anemone", Blocks.AZURE_BLUET));

    /** Swamp — a stilted bayou village on willow & cypress with a cattail-thatch roof and mushroom accents. */
    public static final Style SWAMP = new Style("skyseed:village_swamp",
            Mat.bwg("willow_planks", Blocks.OAK_PLANKS), Mat.bwg("willow_log", Blocks.OAK_LOG),
            Mat.bwg("cattail_thatch_stairs", Blocks.OAK_STAIRS), Mat.bwg("cattail_thatch_slab", Blocks.OAK_SLAB),
            Mat.bwg("willow_door", Blocks.OAK_DOOR), Mat.v(Blocks.TUFF),
            Mat.v(Blocks.GLASS_PANE), Mat.bwg("willow_fence", Blocks.OAK_FENCE),
            Mat.bwg("cypress_planks", Blocks.OAK_PLANKS), Mat.v(Blocks.MUD),
            Mat.bwg("willow_bookshelf", Blocks.BOOKSHELF), Mat.v(Blocks.LANTERN), Mat.bwg("green_mushroom_block", Blocks.MOSS_BLOCK));

    public static void generateInto(Path dir, Style s) throws IOException {
        // The two street skeletons (normal + a "_dense" set weighting the big-building section), exactly as the
        // trade post: the village_center tier points at start_dense for more landmarks.
        writeStreetSkeleton(dir, s, s.pool() + "/streets", "");
        writeStreetSkeleton(dir, s, s.pool() + "/streets_dense", "_dense");
        // Profession shops — a distinct job-site + roof + feature on the shared shell so each reads differently.
        writeIfAbsent(dir.resolve("shop_farmer.nbt"), shop(s, new ShopDesign(Blocks.COMPOSTER.defaultBlockState(), Roof.GABLE, Feature.NONE)));
        writeIfAbsent(dir.resolve("shop_librarian.nbt"), shop(s, new ShopDesign(Blocks.LECTERN.defaultBlockState(), Roof.GABLE, Feature.BOOKS)));
        writeIfAbsent(dir.resolve("shop_fisherman.nbt"), shop(s, new ShopDesign(Blocks.BARREL.defaultBlockState(), Roof.HIP, Feature.NONE)));
        writeIfAbsent(dir.resolve("shop_fletcher.nbt"), shop(s, new ShopDesign(Blocks.FLETCHING_TABLE.defaultBlockState(), Roof.FLAT, Feature.WOOL)));
        writeIfAbsent(dir.resolve("shop_mason.nbt"), shop(s, new ShopDesign(Blocks.STONECUTTER.defaultBlockState(), Roof.STEPPED, Feature.NONE)));
        writeIfAbsent(dir.resolve("shop_cartographer.nbt"), shop(s, new ShopDesign(Blocks.CARTOGRAPHY_TABLE.defaultBlockState(), Roof.GABLE, Feature.BOOKS)));
        writeIfAbsent(dir.resolve("shop_shepherd.nbt"), shop(s, new ShopDesign(Blocks.LOOM.defaultBlockState(), Roof.FLAT, Feature.WOOL)));
        writeIfAbsent(dir.resolve("shop_butcher.nbt"), shop(s, new ShopDesign(Blocks.SMOKER.defaultBlockState(), Roof.HIP, Feature.NONE)));
        // The four professions the shop roster was missing — with these + the forge (toolsmith) every vanilla villager
        // profession is available in principle (armorer=blast_furnace, cleric=brewing_stand, weaponsmith=grindstone,
        // leatherworker=cauldron — the cauldron is free now that the animal pen's trough is a water basin).
        writeIfAbsent(dir.resolve("shop_armorer.nbt"), shop(s, new ShopDesign(Blocks.BLAST_FURNACE.defaultBlockState(), Roof.STEPPED, Feature.NONE)));
        writeIfAbsent(dir.resolve("shop_cleric.nbt"), shop(s, new ShopDesign(Blocks.BREWING_STAND.defaultBlockState(), Roof.STEPPED, Feature.BOOKS)));
        writeIfAbsent(dir.resolve("shop_weaponsmith.nbt"), shop(s, new ShopDesign(FLOOR_GRINDSTONE, Roof.FLAT, Feature.NONE)));
        writeIfAbsent(dir.resolve("shop_leatherworker.nbt"), shop(s, new ShopDesign(Blocks.CAULDRON.defaultBlockState(), Roof.GABLE, Feature.NONE)));
        // Plain homes with distinct footprints/roofs (no job site) — the "small_house" variety BWG leans on.
        writeIfAbsent(dir.resolve("house_cottage.nbt"), cottage(s));
        writeIfAbsent(dir.resolve("house_tower.nbt"), tower(s));
        writeIfAbsent(dir.resolve("house_porch.nbt"), porchHouse(s));
        writeIfAbsent(dir.resolve("house_long.nbt"), longHouse(s));
        // Big-section landmarks: the smithy (kept "forge" so the shop cap exempts it) and an open shrine hall.
        writeIfAbsent(dir.resolve("forge.nbt"), forge(s));
        writeIfAbsent(dir.resolve("shrine_hall.nbt"), shrineHall(s));
        // Surplus-lot fillers + the over-void pier.
        writeIfAbsent(dir.resolve("wheat_field.nbt"), field(s, Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7)));
        writeIfAbsent(dir.resolve("carrot_field.nbt"), field(s, Blocks.CARROTS.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7)));
        writeIfAbsent(dir.resolve("garden.nbt"), garden(s));
        writeIfAbsent(dir.resolve("grove.nbt"), grove(s));
        writeIfAbsent(dir.resolve("well.nbt"), well(s));
        writeIfAbsent(dir.resolve("animal_pen.nbt"), animalPen(s));
        writeIfAbsent(dir.resolve("market_stall.nbt"), marketStall(s));
        writeIfAbsent(dir.resolve("terminator.nbt"), terminator(s));
        writeIfAbsent(dir.resolve("pier.nbt"), pier(s));
        // The tiny green the Hamlet tier starts from — reuses this style's lots (1–2 shops in miniature).
        writeIfAbsent(dir.resolve("hamlet_hub.nbt"), hamletHub(s));
    }

    // ------------------------------------------------------------------------------------------------------------
    // Streets (marker decks — palette-independent apart from the square floor and the pool they recurse into).
    // ------------------------------------------------------------------------------------------------------------

    private static void writeStreetSkeleton(Path dir, Style s, String streetsPool, String suffix) throws IOException {
        writeIfAbsent(dir.resolve("square" + suffix + ".nbt"), square(s, streetsPool));
        writeIfAbsent(dir.resolve("street_straight" + suffix + ".nbt"), straightStreet(s, streetsPool));
        writeIfAbsent(dir.resolve("street_corner" + suffix + ".nbt"), streetCorner(s, streetsPool));
        writeIfAbsent(dir.resolve("street_large" + suffix + ".nbt"), largeStreet(s, streetsPool));
    }

    /** 7×7 solid village square: the island anchor + a lantern, four outward street connectors into {@code streetsPool}. */
    private static Built square(Style s, String streetsPool) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 6; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
            }
        }
        final String floor = finalState(s.foundation());
        streetConn(m, bes, new BlockPos(3, 0, 0), FrontAndTop.NORTH_UP, streetsPool, floor);
        streetConn(m, bes, new BlockPos(3, 0, 6), FrontAndTop.SOUTH_UP, streetsPool, floor);
        streetConn(m, bes, new BlockPos(0, 0, 3), FrontAndTop.WEST_UP, streetsPool, floor);
        streetConn(m, bes, new BlockPos(6, 0, 3), FrontAndTop.EAST_UP, streetsPool, floor);
        set(m, mods, new BlockPos(3, 1, 3), s.lantern());
        anchor(m, bes, new BlockPos(3, 0, 3), floor);
        return built(m, bes, mods);
    }

    /** A 7-long straight lane (markers) with a lot connector on each long side. */
    private static Built straightStreet(Style s, String streetsPool) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState wool = PathSurfacer.MARKER.defaultBlockState();
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 2; z++) {
                m.put(new BlockPos(x, 1, z), wool);
            }
        }
        streetConn(m, bes, new BlockPos(0, 0, 1), FrontAndTop.WEST_UP, streetsPool, "minecraft:air");
        streetConn(m, bes, new BlockPos(6, 0, 1), FrontAndTop.EAST_UP, streetsPool, "minecraft:air");
        lotConn(m, bes, s, new BlockPos(3, 0, 0), FrontAndTop.NORTH_UP, "minecraft:air");
        lotConn(m, bes, s, new BlockPos(3, 0, 2), FrontAndTop.SOUTH_UP, "minecraft:air");
        return new Built(m, bes);
    }

    /** A 3×3 corner turn (markers) — no lot (a 7×7 house won't fit a 3×3 corner). */
    private static Built streetCorner(Style s, String streetsPool) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState wool = PathSurfacer.MARKER.defaultBlockState();
        for (int x = 0; x <= 2; x++) {
            for (int z = 0; z <= 2; z++) {
                m.put(new BlockPos(x, 1, z), wool);
            }
        }
        streetConn(m, bes, new BlockPos(0, 0, 1), FrontAndTop.WEST_UP, streetsPool, "minecraft:air");
        streetConn(m, bes, new BlockPos(1, 0, 2), FrontAndTop.SOUTH_UP, streetsPool, "minecraft:air");
        return new Built(m, bes);
    }

    /** A 3×7 "large" section: the lane runs through its ends, one lot connector at the isolated middle drawing the
     *  {@code large_lots} pool (room for a bigger landmark building). */
    private static Built largeStreet(Style s, String streetsPool) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState wool = PathSurfacer.MARKER.defaultBlockState();
        for (int x = 0; x <= 2; x++) {
            for (int z = 0; z <= 6; z++) {
                m.put(new BlockPos(x, 1, z), wool);
            }
        }
        streetConn(m, bes, new BlockPos(1, 0, 0), FrontAndTop.NORTH_UP, streetsPool, "minecraft:air");
        streetConn(m, bes, new BlockPos(1, 0, 6), FrontAndTop.SOUTH_UP, streetsPool, "minecraft:air");
        conn(m, bes, new BlockPos(2, 0, 3), FrontAndTop.EAST_UP, "skyseed:lot", "skyseed:lot_door",
                s.pool() + "/large_lots", "minecraft:air");
        return new Built(m, bes);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Buildings.
    // ------------------------------------------------------------------------------------------------------------

    private enum Roof { GABLE, FLAT, STEPPED, HIP }
    private enum Feature { NONE, BOOKS, WOOL }
    private record ShopDesign(BlockState jobSite, Roof roof, Feature feature) {}

    /** A 7×7 profession house: a white-dacite base course, skyris-plank walls with a dark-prismarine upper course and
     *  log corner posts, a per-design roof + feature, glass windows, a bed and the job site facing the street. */
    private static Built shop(Style s, ShopDesign d) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int n = 7, max = 6, mid = 3;
        for (int x = 0; x < n; x++) {
            for (int z = 0; z < n; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                if (perim) {
                    for (int h = 1; h <= 3; h++) {
                        set(m, mods, new BlockPos(x, h, z), corner ? s.post() : (h == 1 ? s.base() : h == 3 ? s.accent() : s.wall()));
                    }
                }
                set(m, mods, new BlockPos(x, 4, z), s.wall()); // flat ceiling; the roof builds on top
            }
        }
        door(m, mods, s, mid, 0);
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        set(m, mods, new BlockPos(0, 2, mid), s.glass());
        set(m, mods, new BlockPos(max, 2, mid), s.glass());
        set(m, mods, new BlockPos(mid, 2, max), s.glass());
        bed(m, new BlockPos(1, 1, max - 2), Direction.SOUTH);
        m.put(new BlockPos(max - 1, 1, max - 1), d.jobSite());
        m.put(new BlockPos(1, 1, 1), Blocks.TORCH.defaultBlockState());
        roof(m, mods, s, d.roof(), max);
        feature(m, mods, s, d.feature(), max);
        return built(m, bes, mods);
    }

    /** Build the per-design roof on top of the y4 ceiling of a {@code max+1}-wide shell. */
    private static void roof(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, Style s, Roof roof, int max) {
        switch (roof) {
            case GABLE -> bwgGableRoof(m, mods, 0, max, 0, max, 4, s.wall(), s.stairs(), s.slab(), 0);
            case FLAT -> {
                for (int x = 0; x <= max; x++) {
                    for (int z = 0; z <= max; z++) {
                        if (x == 0 || x == max || z == 0 || z == max) {
                            putSlab(m, mods, new BlockPos(x, 5, z), s.slab(), false); // a low parapet
                        }
                    }
                }
            }
            case STEPPED -> {
                for (int lo = 1, hi = max - 1, y = 5; lo <= hi; lo++, hi--, y++) {
                    for (int x = lo; x <= hi; x++) {
                        for (int z = lo; z <= hi; z++) {
                            set(m, mods, new BlockPos(x, y, z), s.accent());
                        }
                    }
                }
            }
            case HIP -> hipRoof(m, mods, s, max);
        }
    }

    /** A four-sided (hip) roof of inward-facing stairs, ringing up to a peak — corners are solid, the apex a slab. */
    private static void hipRoof(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, Style s, int max) {
        final int mid = max / 2;
        for (int i = 0, y = 5; i <= mid; i++, y++) {
            final int lo = i, hi = max - i;
            if (lo > hi) {
                break;
            }
            for (int x = lo; x <= hi; x++) {
                for (int z = lo; z <= hi; z++) {
                    if (!(x == lo || x == hi || z == lo || z == hi)) {
                        continue; // only the ring
                    }
                    if (lo == hi) { // the apex column
                        putSlab(m, mods, new BlockPos(x, y, z), s.slab(), false);
                        continue;
                    }
                    Direction face = null; // inward-facing stair on each edge; corners stay solid
                    if (x == lo && z != lo && z != hi) {
                        face = Direction.EAST;
                    } else if (x == hi && z != lo && z != hi) {
                        face = Direction.WEST;
                    } else if (z == lo && x != lo && x != hi) {
                        face = Direction.SOUTH;
                    } else if (z == hi && x != lo && x != hi) {
                        face = Direction.NORTH;
                    }
                    if (face != null) {
                        set(m, mods, new BlockPos(x, y, z),
                                s.stairs().block().defaultBlockState().setValue(StairBlock.FACING, face), s.stairs().id());
                    } else {
                        set(m, mods, new BlockPos(x, y, z), s.wall()); // corner
                    }
                }
            }
        }
    }

    /** A per-profession interior feature against the back-right wall. */
    private static void feature(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, Style s, Feature feature, int max) {
        switch (feature) {
            case BOOKS -> {
                set(m, mods, new BlockPos(max - 1, 1, 2), s.bookshelf());
                set(m, mods, new BlockPos(max - 1, 2, 2), s.bookshelf());
                set(m, mods, new BlockPos(max - 1, 1, 3), s.bookshelf());
            }
            case WOOL -> {
                m.put(new BlockPos(max - 1, 1, 1), Blocks.WHITE_WOOL.defaultBlockState());
                m.put(new BlockPos(max - 1, 2, 1), Blocks.WHITE_WOOL.defaultBlockState());
                m.put(new BlockPos(max - 1, 1, 2), Blocks.LIGHT_GRAY_WOOL.defaultBlockState());
            }
            case NONE -> { }
        }
    }

    /** A small 5×5 gabled cottage (no job site) — a plain home for street variety. */
    private static Built cottage(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                if (perim) {
                    for (int h = 1; h <= 3; h++) {
                        set(m, mods, new BlockPos(x, h, z), corner ? s.post() : (h == 1 ? s.base() : s.wall()));
                    }
                }
                set(m, mods, new BlockPos(x, 4, z), s.wall());
            }
        }
        door(m, mods, s, mid, 0);
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        set(m, mods, new BlockPos(0, 2, mid), s.glass());
        set(m, mods, new BlockPos(max, 2, mid), s.glass());
        set(m, mods, new BlockPos(mid, 2, max), s.glass());
        bed(m, new BlockPos(1, 1, max - 1), Direction.NORTH);
        m.put(new BlockPos(max - 1, 1, max - 1), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(max - 1, 1, 1), Blocks.TORCH.defaultBlockState());
        bwgGableRoof(m, mods, 0, max, 0, max, 4, s.wall(), s.stairs(), s.slab(), 0);
        return built(m, bes, mods);
    }

    /** A slender two-storey 5×5 tower house — a taller silhouette for skyline variety, dacite-brick base, prismarine
     *  cap. (No job site; a home.) */
    private static Built tower(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                final boolean corner = (x == 0 || x == max) && (z == 0 || z == max);
                if (perim) {
                    for (int h = 1; h <= 7; h++) {
                        set(m, mods, new BlockPos(x, h, z), corner ? s.post() : (h == 1 ? s.base() : s.wall()));
                    }
                }
            }
        }
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                if (!(x == mid && z == max - 1)) {                 // leave the ladder shaft open through the mid floor
                    set(m, mods, new BlockPos(x, 4, z), s.wall());  // mid floor
                }
                set(m, mods, new BlockPos(x, 8, z), s.accent());   // flat prismarine roof
            }
        }
        // a parapet of slabs ringing the roof
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                if (x == 0 || x == max || z == 0 || z == max) {
                    putSlab(m, mods, new BlockPos(x, 9, z), s.slab(), false);
                }
            }
        }
        door(m, mods, s, mid, 0);
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        // Stacked windows on the two SIDE walls only. The back-centre column (mid, y, max) is kept SOLID because the
        // ladder mounts against it — a ladder on a glass pane can't attach and renders broken.
        for (int y = 2; y <= 6; y += 2) {
            set(m, mods, new BlockPos(0, y, mid), s.glass());
            set(m, mods, new BlockPos(max, y, mid), s.glass());
        }
        bed(m, new BlockPos(1, 1, max - 1), Direction.NORTH);
        m.put(new BlockPos(max - 1, 5, max - 1), Blocks.BELL.defaultBlockState()); // an upper belfry
        m.put(new BlockPos(1, 1, 1), Blocks.TORCH.defaultBlockState());
        // A ladder up the SOLID back wall to the mid-floor shaft (facing north = mounted on the +Z wall behind it).
        for (int y = 1; y <= 4; y++) {
            m.put(new BlockPos(mid, y, max - 1), Blocks.LADDER.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        }
        return built(m, bes, mods);
    }

    /** The smithy — a bigger 5×7 building for the large sections: a stone-faced forge with a furnace + prismarine
     *  chimney and an open patio with an anvil. Named "forge" so the {@code shop_} cap exempts it. */
    private static Built forge(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        for (int x = 0; x <= 4; x++) {
            for (int z = 0; z <= 6; z++) {
                if (!forgeRoom(x, z)) {
                    set(m, mods, new BlockPos(x, 0, z), s.base()); // open patio
                    continue;
                }
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
                set(m, mods, new BlockPos(x, 4, z), s.wall());
                boolean edge = false;
                for (final Direction d : Direction.Plane.HORIZONTAL) {
                    if (!forgeRoom(x + d.getStepX(), z + d.getStepZ())) {
                        edge = true;
                        break;
                    }
                }
                if (edge) {
                    set(m, mods, new BlockPos(x, 1, z), s.base());
                    set(m, mods, new BlockPos(x, 2, z), s.wall());
                    set(m, mods, new BlockPos(x, 3, z), s.wall());
                    putSlab(m, mods, new BlockPos(x, 5, z), s.slab(), false);
                }
            }
        }
        door(m, mods, s, 2, 0);
        lotConn(m, bes, s, new BlockPos(2, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        set(m, mods, new BlockPos(0, 2, 1), s.glass());
        set(m, mods, new BlockPos(0, 2, 4), s.glass());
        m.put(new BlockPos(3, 1, 1), Blocks.FURNACE.defaultBlockState());
        for (int y = 4; y <= 6; y++) {
            set(m, mods, new BlockPos(3, y, 1), s.accent()); // chimney
        }
        m.put(new BlockPos(1, 2, 1), Blocks.TORCH.defaultBlockState());
        bed(m, new BlockPos(1, 1, 4), Direction.SOUTH);
        m.put(new BlockPos(2, 1, 2), Blocks.SMITHING_TABLE.defaultBlockState()); // in the x=2 column, clear of the x=1 bed
        m.put(new BlockPos(3, 1, 4), Blocks.ANVIL.defaultBlockState());
        set(m, mods, new BlockPos(4, 1, 4), s.fence());
        set(m, mods, new BlockPos(4, 1, 5), s.fence());
        set(m, mods, new BlockPos(4, 1, 6), s.fence());
        set(m, mods, new BlockPos(3, 1, 6), s.fence());
        set(m, mods, new BlockPos(3, 2, 6), s.lantern());
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    private static boolean forgeRoom(int x, int z) {
        return x >= 0 && x <= 4 && z >= 0 && z <= 6 && (x <= 2 || z <= 2);
    }

    /** An open pillared shrine hall (5 wide × 9 deep) — the skyris landmark: dacite-brick floor, prismarine-capped
     *  skyris-log colonnade, a bell at the head, a lantern-lit aisle. A second large-section option beside the forge. */
    private static Built shrineHall(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int mx = 4, mz = 8, mid = 2;
        for (int x = 0; x <= mx; x++) {
            for (int z = 0; z <= mz; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.base()); // dacite-brick platform
            }
        }
        // colonnade: log posts down each long side (paired with the gable ends), a prismarine architrave along the top
        for (int z = 0; z <= mz; z += 2) {
            for (final int x : new int[]{0, mx}) {
                for (int h = 1; h <= 4; h++) {
                    set(m, mods, new BlockPos(x, h, z), s.post());
                }
            }
        }
        for (int x = 0; x <= mx; x++) {
            set(m, mods, new BlockPos(x, 5, 0), s.accent());  // architrave across the two gable ends
            set(m, mods, new BlockPos(x, 5, mz), s.accent());
        }
        for (int z = 0; z <= mz; z++) {
            set(m, mods, new BlockPos(0, 5, z), s.accent());  // architrave down the long sides
            set(m, mods, new BlockPos(mx, 5, z), s.accent());
        }
        // a gabled skyris roof over the colonnade
        bwgGableRoof(m, mods, 0, mx, 0, mz, 5, s.wall(), s.stairs(), s.slab(), 0);
        // No door: the shrine is an OPEN colonnade (pillars, no walls), so its front is an open temple entrance —
        // a freestanding door in the aisle reads wrong. The lot connector alone marks the entrance.
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        // altar + bell at the head, lanterns lining the aisle
        m.put(new BlockPos(mid, 1, mz - 1), Blocks.BELL.defaultBlockState());
        set(m, mods, new BlockPos(1, 1, mz - 1), s.lantern());
        set(m, mods, new BlockPos(mx - 1, 1, mz - 1), s.lantern());
        for (int z = 2; z <= mz - 2; z += 2) {
            set(m, mods, new BlockPos(mid, 1, z), s.lantern());
        }
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Fillers + decorations.
    // ------------------------------------------------------------------------------------------------------------

    /** A 5×5 fenced, watered, grown field with a gate onto the street. */
    private static Built field(Style s, BlockState crop) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final BlockState farmland = Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE, 7);
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (perim) {
                    set(m, mods, new BlockPos(x, 0, z), s.foundation());
                } else {
                    m.put(new BlockPos(x, 0, z), farmland);
                }
            }
        }
        m.put(new BlockPos(mid, 0, mid), Blocks.WATER.defaultBlockState());
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                if (!(x == mid && z == mid)) {
                    m.put(new BlockPos(x, 1, z), crop);
                }
            }
        }
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                final boolean perim = x == 0 || x == max || z == 0 || z == max;
                if (perim && !(x == mid && z == 0)) {
                    set(m, mods, new BlockPos(x, 1, z), s.fence());
                }
            }
        }
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    /** A 5×5 garden plot: the style's signature {@code flora} at the corners + a little short grass, around a lamp post. */
    private static Built garden(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        set(m, mods, new BlockPos(1, 1, 1), s.flora());
        set(m, mods, new BlockPos(3, 1, 1), s.flora());
        set(m, mods, new BlockPos(1, 1, 3), s.flora());
        m.put(new BlockPos(3, 1, 3), Blocks.SHORT_GRASS.defaultBlockState());
        set(m, mods, new BlockPos(mid, 1, mid), s.fence()); // lamp post
        set(m, mods, new BlockPos(mid, 2, mid), s.lantern());
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, "minecraft:grass_block");
        return built(m, bes, mods);
    }

    /** A 5×5 covered well: a dacite-brick apron + rim around a water pool, four posts, a slab roof, a hanging lantern. */
    private static Built well(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.base());
            }
        }
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                if (x == mid && z == mid) {
                    m.put(new BlockPos(x, 1, z), Blocks.WATER.defaultBlockState());
                } else {
                    set(m, mods, new BlockPos(x, 1, z), s.base());
                }
            }
        }
        for (final int[] c : new int[][]{{1, 1}, {3, 1}, {1, 3}, {3, 3}}) {
            set(m, mods, new BlockPos(c[0], 2, c[1]), s.fence());
            set(m, mods, new BlockPos(c[0], 3, c[1]), s.fence());
        }
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                putSlab(m, mods, new BlockPos(x, 4, z), s.slab(), false);
            }
        }
        m.put(new BlockPos(mid, 3, mid), Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true));
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    /** A tiny 3×3 lamp-post terminator — the lots' fallback for a plot too tight for a building. */
    private static Built terminator(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        for (int x = 0; x <= 2; x++) {
            for (int z = 0; z <= 2; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        set(m, mods, new BlockPos(0, 1, 0), s.flora());
        m.put(new BlockPos(2, 1, 2), Blocks.SHORT_GRASS.defaultBlockState());
        set(m, mods, new BlockPos(1, 1, 1), s.fence());
        set(m, mods, new BlockPos(1, 2, 1), s.lantern());
        lotConn(m, bes, s, new BlockPos(1, 0, 0), FrontAndTop.NORTH_UP, "minecraft:grass_block");
        return built(m, bes, mods);
    }

    /** A 5×5 plank pier for lots over the void — matches the bridges, with a fence railing, lamp and barrels. */
    private static Built pier(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.wall()); // plank deck
                final boolean edge = x == 0 || x == max || z == 0 || z == max;
                if (edge && !(x == mid && z == 0)) {
                    set(m, mods, new BlockPos(x, 1, z), s.fence());
                }
            }
        }
        set(m, mods, new BlockPos(1, 1, 2), s.fence());
        set(m, mods, new BlockPos(1, 2, 2), s.lantern());
        m.put(new BlockPos(3, 1, 1), Blocks.BARREL.defaultBlockState());
        m.put(new BlockPos(3, 1, 3), Blocks.BARREL.defaultBlockState());
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    /** A 3×3 green with three lot connectors — the Hamlet tier's start piece; pulls this style's {@code lots}. */
    private static Built hamletHub(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        for (int x = 0; x <= 2; x++) {
            for (int z = 0; z <= 2; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        conn(m, bes, new BlockPos(1, 0, 0), FrontAndTop.NORTH_UP, "skyseed:lot", "skyseed:lot_door", s.pool() + "/lots", "minecraft:grass_block");
        conn(m, bes, new BlockPos(0, 0, 1), FrontAndTop.WEST_UP, "skyseed:lot", "skyseed:lot_door", s.pool() + "/lots", "minecraft:grass_block");
        conn(m, bes, new BlockPos(2, 0, 1), FrontAndTop.EAST_UP, "skyseed:lot", "skyseed:lot_door", s.pool() + "/lots", "minecraft:grass_block");
        set(m, mods, new BlockPos(1, 1, 1), s.fence());
        set(m, mods, new BlockPos(1, 2, 1), s.lantern());
        anchor(m, bes, new BlockPos(1, 0, 1), "minecraft:grass_block");
        return built(m, bes, mods);
    }

    /** A 5-wide × 6-deep cottage with a covered front porch (a slab awning on two posts over the door) — a distinct
     *  silhouette from the plain cottage. The house occupies z 1..5; the porch is the z=0 strip. No job site. */
    private static Built porchHouse(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int mx = 4, mid = 2, zlo = 1, zhi = 5;
        for (int x = 0; x <= mx; x++) {
            for (int z = 0; z <= zhi; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation()); // floor incl. the porch strip at z=0
            }
        }
        for (int x = 0; x <= mx; x++) {
            for (int z = zlo; z <= zhi; z++) {
                final boolean perim = x == 0 || x == mx || z == zlo || z == zhi;
                final boolean corner = (x == 0 || x == mx) && (z == zlo || z == zhi);
                if (perim) {
                    for (int h = 1; h <= 3; h++) {
                        set(m, mods, new BlockPos(x, h, z), corner ? s.post() : (h == 1 ? s.base() : s.wall()));
                    }
                }
                set(m, mods, new BlockPos(x, 4, z), s.wall());
            }
        }
        bwgGableRoof(m, mods, 0, mx, zlo, zhi, 4, s.wall(), s.stairs(), s.slab(), 0);
        // Door in the front wall (z=zlo), opening onto the porch; the porch awning is a slab roof on two posts.
        set(m, mods, new BlockPos(mid, 1, zlo), s.door().block().defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.FACING, Direction.NORTH), s.door().id());
        set(m, mods, new BlockPos(mid, 2, zlo), s.door().block().defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER).setValue(DoorBlock.FACING, Direction.NORTH), s.door().id());
        set(m, mods, new BlockPos(1, 1, 0), s.fence());
        set(m, mods, new BlockPos(mx - 1, 1, 0), s.fence());
        set(m, mods, new BlockPos(1, 2, 0), s.fence());
        set(m, mods, new BlockPos(mx - 1, 2, 0), s.fence());
        for (int x = 0; x <= mx; x++) {
            putSlab(m, mods, new BlockPos(x, 3, 0), s.slab(), false); // the porch awning
        }
        set(m, mods, new BlockPos(0, 2, 3), s.glass());
        set(m, mods, new BlockPos(mx, 2, 3), s.glass());
        set(m, mods, new BlockPos(mid, 2, zhi), s.glass());
        bed(m, new BlockPos(1, 1, zhi - 1), Direction.NORTH);
        m.put(new BlockPos(mx - 1, 1, zhi - 1), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(mx - 1, 1, zlo + 1), Blocks.TORCH.defaultBlockState());
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    /** A 7-wide × 5-deep longhouse: a bigger family home with a gable roof, two beds and windows down the long walls.
     *  No job site — a "large house" for street variety. */
    private static Built longHouse(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int mx = 6, mz = 4, mid = 3;
        for (int x = 0; x <= mx; x++) {
            for (int z = 0; z <= mz; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
                final boolean perim = x == 0 || x == mx || z == 0 || z == mz;
                final boolean corner = (x == 0 || x == mx) && (z == 0 || z == mz);
                if (perim) {
                    for (int h = 1; h <= 3; h++) {
                        set(m, mods, new BlockPos(x, h, z), corner ? s.post() : (h == 1 ? s.base() : h == 3 ? s.accent() : s.wall()));
                    }
                }
                set(m, mods, new BlockPos(x, 4, z), s.wall());
            }
        }
        bwgGableRoof(m, mods, 0, mx, 0, mz, 4, s.wall(), s.stairs(), s.slab(), 0);
        door(m, mods, s, mid, 0);
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        for (final int wx : new int[]{1, mx - 1}) {
            set(m, mods, new BlockPos(wx, 2, 0), s.glass());
            set(m, mods, new BlockPos(wx, 2, mz), s.glass());
        }
        set(m, mods, new BlockPos(0, 2, 2), s.glass());
        set(m, mods, new BlockPos(mx, 2, 2), s.glass());
        bed(m, new BlockPos(1, 1, mz - 1), Direction.NORTH);
        bed(m, new BlockPos(mx - 1, 1, mz - 1), Direction.NORTH);
        m.put(new BlockPos(mid, 1, mz - 1), Blocks.CRAFTING_TABLE.defaultBlockState());
        m.put(new BlockPos(1, 1, 1), Blocks.TORCH.defaultBlockState());
        m.put(new BlockPos(mx - 1, 1, 1), Blocks.TORCH.defaultBlockState());
        return built(m, bes, mods);
    }

    /** A 5×5 botanical grove: a dense bed of the style's signature {@code flora} (pumpkins / mushrooms / aloe /
     *  anemones / …) with short grass and a lamp post — the per-style "garden" cranked up for flavour. */
    private static Built grove(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                if (x == mid && z == mid) {
                    continue; // the lamp post tile
                }
                if ((x + z) % 2 == 0) {
                    set(m, mods, new BlockPos(x, 1, z), s.flora());
                } else if (!(x == mid && z == 0)) {
                    m.put(new BlockPos(x, 1, z), Blocks.SHORT_GRASS.defaultBlockState());
                }
            }
        }
        set(m, mods, new BlockPos(mid, 1, mid), s.fence()); // lamp post
        set(m, mods, new BlockPos(mid, 2, mid), s.lantern());
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, "minecraft:grass_block");
        return built(m, bes, mods);
    }

    /** A 7×7 fenced animal paddock: a grass ring of fence (open at the front gate), hay, a water trough and a lamp. */
    private static Built animalPen(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 6, mid = 3;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
                final boolean edge = x == 0 || x == max || z == 0 || z == max;
                if (edge && !(x == mid && z == 0)) { // fence ring, open at the front-centre gate
                    set(m, mods, new BlockPos(x, 1, z), s.fence());
                }
            }
        }
        m.put(new BlockPos(1, 1, max - 1), Blocks.HAY_BLOCK.defaultBlockState());
        m.put(new BlockPos(2, 1, max - 1), Blocks.HAY_BLOCK.defaultBlockState());
        m.put(new BlockPos(max - 1, 0, max - 1), Blocks.WATER.defaultBlockState()); // a flush water trough (not a
        // cauldron, so it can't be misread as the leatherworker's job site)
        set(m, mods, new BlockPos(max - 1, 1, 1), s.fence()); // a lamp post
        set(m, mods, new BlockPos(max - 1, 2, 1), s.lantern());
        m.put(new BlockPos(mid, 1, mid), Blocks.SHORT_GRASS.defaultBlockState());
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, "minecraft:grass_block");
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    /** A 5×5 open-air market stall: a striped wool awning on fence posts over a counter of produce. No villager. */
    private static Built marketStall(Style s) {
        final Map<BlockPos, BlockState> m = new HashMap<>();
        final Map<BlockPos, String> mods = new HashMap<>();
        final Map<BlockPos, CompoundTag> bes = new HashMap<>();
        final int max = 4, mid = 2;
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                set(m, mods, new BlockPos(x, 0, z), s.foundation());
            }
        }
        for (final int[] c : new int[][]{{0, 0}, {max, 0}, {0, max}, {max, max}}) {
            set(m, mods, new BlockPos(c[0], 1, c[1]), s.fence());
            set(m, mods, new BlockPos(c[0], 2, c[1]), s.fence());
        }
        for (int x = 0; x <= max; x++) {
            for (int z = 0; z <= max; z++) {
                m.put(new BlockPos(x, 3, z), (x + z) % 2 == 0 ? Blocks.WHITE_WOOL.defaultBlockState()
                        : Blocks.RED_WOOL.defaultBlockState());
            }
        }
        m.put(new BlockPos(1, 1, max - 1), Blocks.HAY_BLOCK.defaultBlockState());
        m.put(new BlockPos(2, 1, max - 1), Blocks.MELON.defaultBlockState());
        m.put(new BlockPos(3, 1, max - 1), Blocks.PUMPKIN.defaultBlockState());
        m.put(new BlockPos(mid, 2, mid), Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true));
        lotConn(m, bes, s, new BlockPos(mid, 0, 0), FrontAndTop.NORTH_UP, finalState(s.foundation()));
        StructureParts.linkFences(m);
        return built(m, bes, mods);
    }

    // ------------------------------------------------------------------------------------------------------------
    // Mat-aware placement + jigsaw helpers.
    // ------------------------------------------------------------------------------------------------------------

    /** Place a material at {@code p}: the analog state into {@code m}, and (if it's a BWG block) its id into {@code mods}. */
    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, Mat mat) {
        set(m, mods, p, mat.analog(), mat.id());
    }

    private static void set(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, BlockState analog, String id) {
        m.put(p, analog);
        if (id != null) {
            mods.put(p, id);
        } else {
            mods.remove(p); // a vanilla overwrite must clear any stale mod id left by an earlier block at this cell
        }
    }

    /** A bottom slab (or top slab) state from a slab {@link Mat}. */
    private static BlockState slabState(Mat slab, boolean top) {
        return slab.block().defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE,
                top ? net.minecraft.world.level.block.state.properties.SlabType.TOP
                        : net.minecraft.world.level.block.state.properties.SlabType.BOTTOM);
    }

    /** Place a bottom/top slab of a slab {@link Mat} (carrying its BWG id). */
    private static void putSlab(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, BlockPos p, Mat slab, boolean top) {
        set(m, mods, p, slabState(slab, top), slab.id());
    }

    /** A two-block door on the −Z wall, facing into the house (opens onto the street once the piece rotates). */
    private static void door(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, Style s, int x, int z) {
        set(m, mods, new BlockPos(x, 1, z), s.door().block().defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.FACING, Direction.NORTH), s.door().id());
        set(m, mods, new BlockPos(x, 2, z), s.door().block().defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER).setValue(DoorBlock.FACING, Direction.NORTH), s.door().id());
    }

    /** A red bed at {@code foot}, its head one block toward {@code facing}. */
    private static void bed(Map<BlockPos, BlockState> m, BlockPos foot, Direction facing) {
        m.put(foot, Blocks.RED_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.FOOT).setValue(BedBlock.FACING, facing));
        m.put(foot.relative(facing), Blocks.RED_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD).setValue(BedBlock.FACING, facing));
    }

    /** A Mat-aware gable roof (mirror of {@link StructureParts#gableRoof} carrying BWG ids for the roof blocks). */
    private static void bwgGableRoof(Map<BlockPos, BlockState> m, Map<BlockPos, String> mods, int x0, int x1, int z0, int z1,
                                     int eaveY, Mat planks, Mat stairs, Mat slab, int ov) {
        final int mid = (x0 + x1) / 2;
        final int ridgeY = eaveY + (mid - x0) + ov;
        for (int x = x0 - ov; x <= x1 + ov; x++) {
            final int ry = ridgeY - Math.abs(x - mid);
            final boolean ridge = x == mid;
            final BlockState roof = ridge ? slabState(slab, false)
                    : stairs.block().defaultBlockState().setValue(StairBlock.FACING, x < mid ? Direction.EAST : Direction.WEST);
            final String rid = ridge ? slab.id() : stairs.id();
            for (int z = z0 - ov; z <= z1 + ov; z++) {
                set(m, mods, new BlockPos(x, ry, z), roof, rid);
            }
        }
        for (int x = x0; x <= x1; x++) {
            final int top = ridgeY - Math.abs(x - mid);
            for (int y = eaveY + 1; y < top; y++) {
                set(m, mods, new BlockPos(x, y, z0), planks);
                set(m, mods, new BlockPos(x, y, z1), planks);
            }
        }
        if (ov > 0) {
            for (int x = x0; x <= x1; x++) {
                if (x == mid) {
                    continue;
                }
                final int ry = ridgeY - Math.abs(x - mid);
                final BlockState rake = stairs.block().defaultBlockState()
                        .setValue(StairBlock.FACING, x < mid ? Direction.WEST : Direction.EAST)
                        .setValue(StairBlock.HALF, Half.TOP);
                set(m, mods, new BlockPos(x, ry - 1, z0 - ov), rake, stairs.id());
                set(m, mods, new BlockPos(x, ry - 1, z1 + ov), rake, stairs.id());
            }
            set(m, mods, new BlockPos(mid, ridgeY - 1, z0 - ov), planks);
            set(m, mods, new BlockPos(mid, ridgeY - 1, z1 + ov), planks);
        }
    }

    /** A lot connector (a building hangs off it) at {@code pos} facing {@code dir}, drawing this style's lots pool. */
    private static void lotConn(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Style s, BlockPos pos,
                                FrontAndTop dir, String finalState) {
        conn(m, bes, pos, dir, "skyseed:lot_door", "skyseed:lot", "minecraft:empty", finalState);
    }

    /** A self-linking street connector at {@code pos} facing {@code dir}, drawing {@code streetsPool}. */
    private static void streetConn(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos pos,
                                   FrontAndTop dir, String streetsPool, String finalState) {
        conn(m, bes, pos, dir, "skyseed:street", "skyseed:street", streetsPool, finalState);
    }

    /** A jigsaw connector block-entity at {@code pos}. */
    private static void conn(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, BlockPos pos,
                             FrontAndTop dir, String name, String target, String pool, String finalState) {
        m.put(pos, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, dir));
        bes.put(pos, jig(name, target, pool, finalState));
    }

    /**
     * The final (replacement) block-state id a jigsaw connector cell becomes after assembly. Always the material's
     * <b>vanilla analog</b>, never the BWG id: a connector's {@code final_state} is parsed by the vanilla
     * {@code JigsawReplacementProcessor} at assembly time, and a {@code biomeswevegone:} id there logs a parse ERROR
     * when the pool is assembled without BWG (the gametests, which force-assemble on a flat pad). These cells are
     * hidden thresholds/floor under the door, so the analog is a fine, log-clean choice on both the with- and
     * without-BWG paths.
     */
    private static String finalState(Mat mat) {
        return BuiltInRegistries.BLOCK.getKey(mat.block()).toString();
    }

    private static Built built(Map<BlockPos, BlockState> m, Map<BlockPos, CompoundTag> bes, Map<BlockPos, String> mods) {
        // A jigsaw block must never carry a mod name: connectors/anchors are placed with m.put over a cell the floor
        // loop may have given a BWG id (e.g. a dacite/white-dacite foundation), and a stale id there would rename the
        // jigsaw block's palette entry to that BWG block — so without BWG the cell isn't a jigsaw and assembly fails
        // ("no starting jigsaw bottom found"). Strip mod names from every jigsaw cell centrally.
        mods.keySet().removeIf(p -> m.get(p) != null && m.get(p).is(Blocks.JIGSAW));
        return new Built(m, bes, Map.of(), mods);
    }
}
