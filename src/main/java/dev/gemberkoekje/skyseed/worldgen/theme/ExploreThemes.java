package dev.gemberkoekje.skyseed.worldgen.theme;

import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

/**
 * The Explore Skyseed's biome→theme resolver. Unlike the fixed per-biome seeds, the single {@code explore_skyseed}
 * (whose item theme is the {@link #MARKER} sentinel) has no island of its own: on germination the entity reads the
 * biome it landed in, resolves the dedicated base theme that biome's own seed would grow (so a desert throw grows a
 * Desert island, a snowy throw a Frozen one), and then forces a biome-appropriate rare structure to germinate on it —
 * an on-purpose version of the usual ~5% chance roll ({@link RareStructure}). See {@code IslandSeedEntity}.
 *
 * <p>Rules are matched in order (specific → general) using the same id/{@code #tag} biome syntax as
 * {@link BiomeOverride}; the first hit wins. A biome no rule matches falls to {@link #MARKER} itself — the modest
 * {@code explore.json} fallback theme — so the seed always grows something (e.g. thrown in an unusual/modded biome).
 */
public final class ExploreThemes {
    private ExploreThemes() {}

    /** The sentinel theme id carried by the {@code explore_skyseed} item. No fixed island — the entity intercepts it
     *  and resolves a dedicated base theme from the germination biome. {@code explore.json} backs it as the fallback. */
    public static final Id MARKER = Id.of("skyseed:explore");

    /** The large-tier Explore sentinel ({@code explore_large_skyseed}) — resolves biome → the {@code <family>_large}
     *  theme, which is where the bigger structures (incl. most Iron's ones) live. Forest-large is the unmapped fallback. */
    public static final Id MARKER_LARGE = Id.of("skyseed:explore_large");

    /** The huge-tier Explore sentinel ({@code huge_explore_skyseed}) — resolves biome → the {@code huge_<family>} theme
     *  (the largest structures). Huge-forest is the unmapped fallback. */
    public static final Id MARKER_HUGE = Id.of("skyseed:huge_explore");

    private record Rule(String biome, String theme) {}

    // Prefer the dedicated biome theme that biome's own seed grows, so the Explore island's terrain matches exactly
    // (no palette duplication). Order matters: specific ids and narrower tags precede the broad #is_forest catch-all.
    private static final List<Rule> RULES = List.of(
            new Rule("#minecraft:is_ocean", "aquatic"),
            new Rule("#minecraft:is_deep_ocean", "aquatic"),
            new Rule("#minecraft:is_river", "aquatic"),
            new Rule("#minecraft:is_beach", "aquatic"),
            new Rule("minecraft:desert", "desert"),
            new Rule("#minecraft:is_badlands", "badlands"),
            new Rule("minecraft:mushroom_fields", "mushroom"),
            new Rule("minecraft:swamp", "lush"),
            new Rule("minecraft:mangrove_swamp", "lush"),
            new Rule("minecraft:snowy_plains", "frozen"),
            new Rule("minecraft:snowy_taiga", "frozen"),
            new Rule("minecraft:ice_spikes", "frozen"),
            new Rule("minecraft:snowy_slopes", "frozen"),
            new Rule("minecraft:frozen_peaks", "frozen"),
            new Rule("minecraft:jagged_peaks", "frozen"),
            new Rule("minecraft:snowy_beach", "frozen"),
            new Rule("minecraft:grove", "frozen"),
            new Rule("#minecraft:is_mountain", "rocky"),
            new Rule("minecraft:windswept_hills", "rocky"),
            new Rule("minecraft:windswept_gravelly_hills", "rocky"),
            new Rule("minecraft:windswept_forest", "rocky"),
            new Rule("minecraft:stony_peaks", "rocky"),
            new Rule("minecraft:plains", "meadow"),
            new Rule("minecraft:sunflower_plains", "meadow"),
            new Rule("minecraft:meadow", "meadow"),
            new Rule("minecraft:cherry_grove", "meadow"),
            new Rule("#minecraft:is_savanna", "meadow"),
            // Everything wooded/temperate (forest, birch, dark, flower, taiga, jungle, …) grows the Forest theme, which
            // already carries overrides for that whole range.
            new Rule("#minecraft:is_forest", "forest"),
            new Rule("#minecraft:is_taiga", "forest"),
            new Rule("#minecraft:is_jungle", "forest")
    );

    /** The biome-family name for {@code biome} (e.g. {@code "desert"}, {@code "forest"}), or {@code null} if no rule
     *  matches — the base tier then falls to {@code explore.json}, the large/huge tiers to Forest. */
    private static String family(Holder<Biome> biome) {
        for (final Rule r : RULES) {
            if (Lookup.biomeMatches(biome, r.biome())) {
                return r.theme();
            }
        }
        return null;
    }

    /** The dedicated BASE theme the Explore seed should grow at {@code biome}, or {@link #MARKER} (the
     *  {@code explore.json} fallback) when no rule matches. */
    public static Id resolve(Holder<Biome> biome) {
        final String fam = family(biome);
        return fam == null ? MARKER : Id.of("skyseed:" + fam);
    }

    /** The {@code <family>_large} theme the Large Explore seed grows (Forest large as the unmapped fallback). */
    public static Id resolveLarge(Holder<Biome> biome) {
        final String fam = family(biome);
        return Id.of("skyseed:" + (fam == null ? "forest" : fam) + "_large");
    }

    /** The {@code huge_<family>} theme the Huge Explore seed grows (Huge Forest as the unmapped fallback). */
    public static Id resolveHuge(Holder<Biome> biome) {
        final String fam = family(biome);
        return Id.of("skyseed:huge_" + (fam == null ? "forest" : fam));
    }

    /** Whether {@code theme} is any Explore-seed sentinel (base / large / huge). */
    public static boolean isAdaptive(Id theme) {
        if (theme == null) {
            return false;
        }
        final String v = theme.value();
        return MARKER.value().equals(v) || MARKER_LARGE.value().equals(v) || MARKER_HUGE.value().equals(v);
    }

    /** Resolve the theme for an Explore sentinel {@code marker} at {@code biome} — dispatching to the right tier. */
    public static Id resolveFor(Id marker, Holder<Biome> biome) {
        final String v = marker.value();
        if (MARKER_LARGE.value().equals(v)) {
            return resolveLarge(biome);
        }
        if (MARKER_HUGE.value().equals(v)) {
            return resolveHuge(biome);
        }
        return resolve(biome);
    }
}
