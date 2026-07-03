package dev.gemberkoekje.skyseed.worldgen.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Id;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

/**
 * A datapack-defined island theme: shape, palette, ore table, and weighted decoration variants.
 * This codec is the keystone (README → Configuration) — recipes, the {@code skyseed:theme} component, and the
 * generator all key off the same theme ids. Loaded as the {@code skyseed:theme} datapack registry.
 * {@code jigsaw} optionally assembles a building (or cluster) on the surface from a jigsaw template pool,
 * like a vanilla village — e.g. a villager island's cottage — see {@code SKYVILLAGESPLAN.md}.
 * {@code animals} optionally rolls one weighted pack of farm animals into the jigsaw enclosure's centre,
 * for the dedicated Animal Islands — see {@code SKYANIMALSPLAN.md}.
 * {@code rare_structures} optionally lets a chance-gated structure (igloo, haunted cottage, flooded ruin)
 * germinate in place of the usual island — see {@code SKYSTRUCTURESPLAN.md}.
 * {@code dimensions} declares which dimensions the <em>base</em> config is an implementation for (default
 * {@code [minecraft:overworld]}). A seed thrown in a dimension that is neither in {@code dimensions} nor covered by
 * a dimension-keyed {@link BiomeOverride} fizzles instead of growing the foreign base form — see SKYNETHERPLAN.
 */
public record IslandTheme(Shape shape, Palette palette, List<OreEntry> ores, List<Variant> variants,
                          List<BiomeOverride> biomeOverrides, Optional<Pond> pond, List<MobEntry> mobs,
                          Optional<JigsawConfig> jigsaw, List<AnimalPack> animals, List<RareStructure> rareStructures,
                          Optional<Lava> lava, List<String> dimensions, Optional<Id> twin,
                          Optional<LadderShaft> ladderShaft, Optional<FizzleRule> fizzle, Optional<Caves> caves,
                          Optional<Meteor> meteor) {

    /** True if this theme's base config is an implementation for {@code dim} (its declared {@code dimensions}). */
    public boolean baseValidIn(String dim) {
        return dimensions.contains(dim);
    }

    /** True if a {@code fizzle} rule excludes {@code biome} — the seed should fizzle there regardless of dimension. */
    public boolean fizzlesIn(Holder<Biome> biome) {
        return fizzle.map(f -> f.matches(biome)).orElse(false);
    }

    public static final Codec<IslandTheme> CODEC = RecordCodecBuilder.create(i -> i.group(
            Shape.CODEC.fieldOf("shape").forGetter(IslandTheme::shape),
            Palette.CODEC.fieldOf("palette").forGetter(IslandTheme::palette),
            OreEntry.CODEC.listOf().optionalFieldOf("ores", List.of()).forGetter(IslandTheme::ores),
            Variant.CODEC.listOf().optionalFieldOf("variants", List.of()).forGetter(IslandTheme::variants),
            BiomeOverride.CODEC.listOf().optionalFieldOf("biome_overrides", List.of()).forGetter(IslandTheme::biomeOverrides),
            Pond.CODEC.optionalFieldOf("pond").forGetter(IslandTheme::pond),
            MobEntry.CODEC.listOf().optionalFieldOf("mobs", List.of()).forGetter(IslandTheme::mobs),
            JigsawConfig.CODEC.optionalFieldOf("jigsaw").forGetter(IslandTheme::jigsaw),
            AnimalPack.CODEC.listOf().optionalFieldOf("animals", List.of()).forGetter(IslandTheme::animals),
            RareStructure.CODEC.listOf().optionalFieldOf("rare_structures", List.of()).forGetter(IslandTheme::rareStructures),
            Lava.CODEC.optionalFieldOf("lava").forGetter(IslandTheme::lava),
            Codec.STRING.listOf().optionalFieldOf("dimensions", List.of("minecraft:overworld")).forGetter(IslandTheme::dimensions),
            // If present, germinating this island also grows the named theme at the vanilla 8:1 dimension-linked
            // coordinate in the other dimension (overworld <-> nether). The Ruined Portal names itself — see
            // SKYNETHERPLAN. (RareStructure has the same field, so a rolled ruined portal on a big island pairs too.)
            Id.CODEC.optionalFieldOf("twin").forGetter(IslandTheme::twin),
            // Optional "way down": a ladder shaft (or, rarely, a water column) punched through the island centre to a
            // landing far below, so you can reach mining level without bridging out. See LadderShaft / ShaftPlanner.
            LadderShaft.CODEC.optionalFieldOf("ladder_shaft").forGetter(IslandTheme::ladderShaft),
            // A hard biome exclusion: fizzle (with a message) when thrown into these biomes even in a dimension this
            // theme implements — the Bastion never forms in the basalt deltas. See IslandGenerator.formValidFor.
            FizzleRule.CODEC.optionalFieldOf("fizzle").forGetter(IslandTheme::fizzle),
            // Two terrain-carving optionals folded into ONE codec slot so the group stays within DFU's 16-field limit
            // (the theme JSON keeps `caves` + `meteor` both top-level). caves = internal cave systems (Caves /
            // CaveCarver, SKYHUGEPLAN Phase 2); meteor = an impact crater + sky-stone globe + Meteorite Core (Meteor /
            // MeteorPlacer, METEORPLAN, AE2PLAN #18e).
            Carve.CODEC.forGetter(t -> new Carve(t.caves(), t.meteor()))
    ).apply(i, IslandTheme::from));

    /** Reassembles the record from the codec's 16 group values (the last being the combined {@link Carve} slot). */
    private static IslandTheme from(Shape shape, Palette palette, List<OreEntry> ores, List<Variant> variants,
                                    List<BiomeOverride> biomeOverrides, Optional<Pond> pond, List<MobEntry> mobs,
                                    Optional<JigsawConfig> jigsaw, List<AnimalPack> animals,
                                    List<RareStructure> rareStructures, Optional<Lava> lava, List<String> dimensions,
                                    Optional<Id> twin, Optional<LadderShaft> ladderShaft, Optional<FizzleRule> fizzle,
                                    Carve carve) {
        return new IslandTheme(shape, palette, ores, variants, biomeOverrides, pond, mobs, jigsaw, animals,
                rareStructures, lava, dimensions, twin, ladderShaft, fizzle, carve.caves(), carve.meteor());
    }

    /** The two terrain-carving optionals ({@code caves} + {@code meteor}), combined into one {@link #CODEC} slot to
     *  keep the group within DFU's 16-field limit. Both remain top-level fields in the theme JSON. */
    private record Carve(Optional<Caves> caves, Optional<Meteor> meteor) {
        static final MapCodec<Carve> CODEC = RecordCodecBuilder.mapCodec(c -> c.group(
                Caves.CODEC.optionalFieldOf("caves").forGetter(Carve::caves),
                Meteor.CODEC.optionalFieldOf("meteor").forGetter(Carve::meteor)
        ).apply(c, Carve::new));
    }
}
