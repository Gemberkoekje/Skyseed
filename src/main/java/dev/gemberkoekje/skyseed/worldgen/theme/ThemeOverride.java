package dev.gemberkoekje.skyseed.worldgen.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A data-driven <b>patch</b> applied on top of a base {@link IslandTheme} at resolution time (see {@link Themes#resolve}).
 * Loaded as the {@code skyseed:theme_override} datapack registry, so the modpack OR any third-party mod can extend a
 * Skyseed island without editing Skyseed's own themes — e.g. a Create-compat datapack adds {@code create:zinc_ore} to
 * the rocky island. {@code target} names the theme this patches; the {@link Patch} fields use the same names as
 * {@link IslandTheme}, so a patch reads like a theme with only the bits you want to add. Multiple patches may target
 * one theme (applied in id order — see {@link Themes#resolve}).
 *
 * <p>Merge rules ({@link Patch#applyTo}): the <b>list</b> fields (ores / variants / biome_overrides / mobs / animals /
 * rare_structures / dimensions) are <b>appended</b> to the base; the <b>optional scalar</b> fields (shape / palette /
 * pond / jigsaw / lava / twin / ladder_shaft / fizzle / caves) <b>replace</b> the base's value only when the patch
 * specifies them. With no patch targeting a theme the resolver returns the base unchanged, so generation (and the
 * golden master) is byte-identical to a build with no overrides.
 */
public record ThemeOverride(Id target, Patch patch) {

    public static final Codec<ThemeOverride> CODEC = RecordCodecBuilder.create(i -> i.group(
            Id.CODEC.fieldOf("target").forGetter(ThemeOverride::target),
            Patch.MAP_CODEC.forGetter(ThemeOverride::patch)
    ).apply(i, ThemeOverride::new));

    /** Merge this override onto {@code base} (delegates to {@link Patch#applyTo}). */
    public IslandTheme applyTo(IslandTheme base) {
        return patch.applyTo(base);
    }

    /**
     * The patchable fields. A {@link MapCodec} (not a {@code fieldOf}) so it <b>flattens</b> into the same JSON object as
     * {@code target} — a patch reads exactly like a partial theme. Kept on a sub-record because 16 fields is DFU's
     * {@code group} limit, and {@code target} needs the 17th slot.
     */
    public record Patch(Optional<Shape> shape, Optional<Palette> palette,
                        List<OreEntry> ores, List<Variant> variants, List<BiomeOverride> biomeOverrides,
                        Optional<Pond> pond, List<MobEntry> mobs, Optional<JigsawConfig> jigsaw,
                        List<AnimalPack> animals, List<RareStructure> rareStructures, Optional<Lava> lava,
                        List<String> dimensions, Optional<Id> twin, Optional<LadderShaft> ladderShaft,
                        Optional<FizzleRule> fizzle, Optional<Caves> caves) {

        public static final MapCodec<Patch> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Shape.CODEC.optionalFieldOf("shape").forGetter(Patch::shape),
                Palette.CODEC.optionalFieldOf("palette").forGetter(Patch::palette),
                OreEntry.CODEC.listOf().optionalFieldOf("ores", List.of()).forGetter(Patch::ores),
                Variant.CODEC.listOf().optionalFieldOf("variants", List.of()).forGetter(Patch::variants),
                BiomeOverride.CODEC.listOf().optionalFieldOf("biome_overrides", List.of()).forGetter(Patch::biomeOverrides),
                Pond.CODEC.optionalFieldOf("pond").forGetter(Patch::pond),
                MobEntry.CODEC.listOf().optionalFieldOf("mobs", List.of()).forGetter(Patch::mobs),
                JigsawConfig.CODEC.optionalFieldOf("jigsaw").forGetter(Patch::jigsaw),
                AnimalPack.CODEC.listOf().optionalFieldOf("animals", List.of()).forGetter(Patch::animals),
                RareStructure.CODEC.listOf().optionalFieldOf("rare_structures", List.of()).forGetter(Patch::rareStructures),
                Lava.CODEC.optionalFieldOf("lava").forGetter(Patch::lava),
                Codec.STRING.listOf().optionalFieldOf("dimensions", List.of()).forGetter(Patch::dimensions),
                Id.CODEC.optionalFieldOf("twin").forGetter(Patch::twin),
                LadderShaft.CODEC.optionalFieldOf("ladder_shaft").forGetter(Patch::ladderShaft),
                FizzleRule.CODEC.optionalFieldOf("fizzle").forGetter(Patch::fizzle),
                Caves.CODEC.optionalFieldOf("caves").forGetter(Patch::caves)
        ).apply(i, Patch::new));

        /** Lists appended to the base, present optionals replacing it (see {@link ThemeOverride} class doc). */
        public IslandTheme applyTo(IslandTheme base) {
            return new IslandTheme(
                    shape.orElse(base.shape()),
                    palette.orElse(base.palette()),
                    concat(base.ores(), ores),
                    concat(base.variants(), variants),
                    mergeBands(base.biomeOverrides(), biomeOverrides),
                    pond.isPresent() ? pond : base.pond(),
                    concat(base.mobs(), mobs),
                    jigsaw.isPresent() ? jigsaw : base.jigsaw(),
                    concat(base.animals(), animals),
                    concat(base.rareStructures(), rareStructures),
                    lava.isPresent() ? lava : base.lava(),
                    concat(base.dimensions(), dimensions),
                    twin.isPresent() ? twin : base.twin(),
                    ladderShaft.isPresent() ? ladderShaft : base.ladderShaft(),
                    fizzle.isPresent() ? fizzle : base.fizzle(),
                    caves.isPresent() ? caves : base.caves());
        }

        private static <T> List<T> concat(List<T> a, List<T> b) {
            return b.isEmpty() ? a : Stream.concat(a.stream(), b.stream()).toList();
        }

        /**
         * Merge each patch band into the base band with the same selector (so e.g. a {@code max_y: 8} patch injects into
         * the existing deep band instead of appending a band that would lose the first-match). A patch band whose
         * selector matches no base band is appended as a new band (the way a mod adds a whole new dimension/biome band).
         */
        private static List<BiomeOverride> mergeBands(List<BiomeOverride> base, List<BiomeOverride> patches) {
            if (patches.isEmpty()) {
                return base;
            }
            final List<BiomeOverride> result = new ArrayList<>(base);
            for (BiomeOverride patch : patches) {
                boolean merged = false;
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).sameSelectorAs(patch)) {
                        result.set(i, result.get(i).mergedWith(patch));
                        merged = true;
                    }
                }
                if (!merged) {
                    result.add(patch);
                }
            }
            return result;
        }
    }
}
