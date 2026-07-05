package dev.gemberkoekje.skyseed.worldgen.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

/**
 * A rare structure that occasionally germinates on an otherwise ordinary island — the surprise igloo on a
 * Frozen isle, the haunted cottage on a Hamlet, the flooded ruin on an Aquatic isle, the buried temple on a
 * Large Desert. When one is chosen, its {@code jigsaw} replaces the theme's normal jigsaw (or becomes the only
 * one, if the theme has none) and its {@code mobs} replace the theme's animal packs. At most one rare structure
 * is chosen per island.
 *
 * <p><b>Two selection models</b> (VARIETYSTRUCTUREPLAN D1): a theme that declares a {@code rare_structure_chance}
 * uses the <b>weighted-gate</b> model — one per-seed gate roll, then a pick among the eligible candidates weighted
 * by {@code weight} (so "common vs rare" is a weight, and the per-seed spawn rate is a flat gate no matter how many
 * fit). A theme with no {@code rare_structure_chance} uses the <b>legacy</b> per-entry model — the first whose own
 * {@code chance} rolls true (kept for the unmigrated Nether/End themes, byte-identical). {@code weight} is read only
 * in the first model, {@code chance} only in the second.
 *
 * <p>{@code suppress_pond} skips the theme's pond carve so a self-contained flooded ruin can stand in its place.
 * {@code biomes} (empty = any) gates to matching germination biomes — same id/{@code #tag} syntax as
 * {@link BiomeOverride}, e.g. a jungle temple only on a Large Forest grown in {@code #minecraft:is_jungle}.
 * {@code requires} (empty = none) gates on mod presence: a structure whose required mod(s) are absent is filtered
 * out before any RNG (inert-without-the-mod), so a Create/AE2 build can be listed on a plain terrain theme yet stay
 * byte-identical without its mod. {@code explorable} (default true) — set false on a rewardless filler build to bar
 * it from the Explore seed's guaranteed pick (D3) while it still shows as an ordinary 5% surprise. See
 * {@code VARIETYSTRUCTUREPLAN.md} / {@code SKYSTRUCTURESPLAN.md}.
 */
public record RareStructure(float chance, int weight, JigsawConfig jigsaw, List<AnimalPack> mobs,
                            boolean suppressPond, List<String> biomes, Optional<Id> twin, Optional<String> dimension,
                            List<String> requires, boolean explorable) {
    public static final Codec<RareStructure> CODEC = RecordCodecBuilder.create(i -> i.group(
            // Legacy-model spawn chance (per-entry). Optional now: the weighted-gate model ignores it, and a migrated
            // entry omits it. Default keeps an old { "jigsaw": … } with no chance parseable.
            Codec.FLOAT.optionalFieldOf("chance", 0.05f).forGetter(RareStructure::chance),
            // Weighted-model relative weight (common ≈ 6, rare ≈ 3, epic ≈ 1). Ignored by the legacy model.
            Codec.INT.optionalFieldOf("weight", 1).forGetter(RareStructure::weight),
            JigsawConfig.CODEC.fieldOf("jigsaw").forGetter(RareStructure::jigsaw),
            AnimalPack.CODEC.listOf().optionalFieldOf("mobs", List.of()).forGetter(RareStructure::mobs),
            Codec.BOOL.optionalFieldOf("suppress_pond", false).forGetter(RareStructure::suppressPond),
            Codec.STRING.listOf().optionalFieldOf("biomes", List.of()).forGetter(RareStructure::biomes),
            // If present, a roll of this rare structure also grows the named theme at the dimension-linked
            // coordinate in the other dimension — so a ruined portal rolled on a big island gets its twin too.
            Id.CODEC.optionalFieldOf("twin").forGetter(RareStructure::twin),
            // A roll gate by dimension: when set, this rare structure rolls only in that dimension — so a Nether-native
            // seed can carry an overworld easter egg (the Trading Post growing the abandoned cottage thrown topside).
            // Unset, it follows the theme's home dimension. See rollsIn.
            Codec.STRING.optionalFieldOf("dimension").forGetter(RareStructure::dimension),
            // Mod-presence gate (VARIETYSTRUCTUREPLAN A3): every id here must be loaded or the structure is skipped
            // before any RNG, so a modded build listed on a vanilla theme stays inert + byte-identical without the mod.
            Codec.STRING.listOf().optionalFieldOf("requires", List.of()).forGetter(RareStructure::requires),
            // Explore-seed reward floor (VARIETYSTRUCTUREPLAN D3): false = rewardless filler, barred from the Explore
            // seed's guaranteed pick but still eligible for the ordinary per-seed roll.
            Codec.BOOL.optionalFieldOf("explorable", true).forGetter(RareStructure::explorable)
    ).apply(i, RareStructure::new));

    /** True if every mod this rare structure {@code requires} is loaded (empty = always true). Checked BEFORE any RNG
     *  so an absent-mod structure never consumes a roll — the inert-without-the-mod / determinism-parity invariant. */
    public boolean requiresPresent() {
        for (final String modId : requires) {
            if (!Lookup.modLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

    /** Whether this rare structure may roll in {@code dim}: only its own {@code dimension} when set, else the theme's
     * home dimension ({@code baseValidHere}). */
    public boolean rollsIn(String dim, boolean baseValidHere) {
        return dimension.isPresent() ? dimension.get().equals(dim) : baseValidHere;
    }

    /** True if this rare structure may roll in {@code biome} (no {@code biomes} filter set = any biome). */
    public boolean matchesBiome(Holder<Biome> biome) {
        if (biomes.isEmpty()) {
            return true;
        }
        for (final String entry : biomes) {
            if (Lookup.biomeMatches(biome, entry)) {
                return true;
            }
        }
        return false;
    }
}
