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

    /** The Wild Skyseed sentinels ({@code wild_skyseed} / {@code wild_large_skyseed} / {@code huge_wild_skyseed}) — the
     *  <em>default</em> adaptive seed. Like the Explore markers they resolve the germination biome to that biome's
     *  dedicated theme, but the entity does <b>not</b> force a rare structure on them ({@link #forcesRare}) — the theme's
     *  ordinary ~5% roll applies. The base tier's unmapped fallback is {@code forest} (a plain wooded island), not the
     *  build-forcing {@code explore.json}. See {@code IslandSeedEntity}. */
    public static final Id WILD = Id.of("skyseed:wild");
    public static final Id WILD_LARGE = Id.of("skyseed:wild_large");
    public static final Id WILD_HUGE = Id.of("skyseed:huge_wild");

    /** The Nether Wild Skyseed sentinels ({@code wild_nether_skyseed} / {@code wild_nether_large_skyseed}) — the
     *  adaptive Nether starter (SKYNETHERENDBIOMEPLAN §9b). Like the overworld Wild they resolve the germination
     *  biome to that biome's dedicated seed, but over the <b>Nether</b> biome map ({@link #resolveNether}); no huge
     *  tier, and {@link #forcesRare} is false (the resolved theme's ordinary ~5% roll applies — only the {@code _large}
     *  nether themes carry rare structures). */
    public static final Id WILD_NETHER = Id.of("skyseed:wild_nether");
    public static final Id WILD_NETHER_LARGE = Id.of("skyseed:wild_nether_large");

    private record Rule(String biome, String theme) {}

    // Prefer the dedicated biome theme that biome's own seed grows, so the Explore island's terrain matches exactly
    // (no palette duplication). Order matters: specific ids and narrower tags precede the broad #is_forest catch-all.
    private static final List<Rule> RULES = List.of(
            new Rule("#minecraft:is_ocean", "aquatic"),
            new Rule("#minecraft:is_deep_ocean", "aquatic"),
            new Rule("#minecraft:is_river", "aquatic"),
            // snowy_beach is a member of #minecraft:is_beach, so route it to Frozen BEFORE the broad beach→aquatic
            // catch-all — otherwise a snowy beach grows an Aquatic (lake) island instead of a Frozen one, and the
            // dedicated snowy_beach→frozen rule further down is dead code.
            new Rule("minecraft:snowy_beach", "frozen"),
            new Rule("#minecraft:is_beach", "aquatic"),
            new Rule("minecraft:desert", "desert"),
            // BIOMECOVERAGEPLAN F1 — vanilla has no #is_desert tag, so modded deserts (BWG's atacama_outback /
            // mojave_desert / windswept_desert, all in #biomeswevegone:desert) never matched the exact minecraft:desert
            // id and fell through to the Forest fallback (a green island on a desert). Route the BWG desert tag to the
            // Desert theme. Inert without BWG: the tag is empty/unknown, so this rule simply never fires.
            new Rule("#biomeswevegone:desert", "desert"),
            new Rule("#minecraft:is_badlands", "badlands"),
            new Rule("minecraft:mushroom_fields", "mushroom"),
            new Rule("minecraft:swamp", "lush"),
            new Rule("minecraft:mangrove_swamp", "lush"),
            // BIOMECOVERAGEPLAN F7 — Quark's glimmering_weald is an underground glow biome in no surface tag, so the
            // adaptive seed fell to the Forest fallback. Route it to Lush (the glow-berry / moss family). Inert without Quark.
            new Rule("quark:glimmering_weald", "lush"),
            new Rule("minecraft:snowy_plains", "frozen"),
            new Rule("minecraft:snowy_taiga", "frozen"),
            new Rule("minecraft:ice_spikes", "frozen"),
            new Rule("minecraft:snowy_slopes", "frozen"),
            new Rule("minecraft:frozen_peaks", "frozen"),
            new Rule("minecraft:jagged_peaks", "frozen"),
            new Rule("minecraft:grove", "frozen"),
            // meadow + cherry_grove are members of #minecraft:is_mountain, so route them to Meadow BEFORE the broad
            // mountain→rocky catch-all — otherwise both grow a Rocky island and the dedicated meadow/cherry_grove
            // rules further down are dead code (they're specific ids, so they can't shadow anything else here).
            new Rule("minecraft:meadow", "meadow"),
            new Rule("minecraft:cherry_grove", "meadow"),
            new Rule("#minecraft:is_mountain", "rocky"),
            new Rule("minecraft:windswept_hills", "rocky"),
            new Rule("minecraft:windswept_gravelly_hills", "rocky"),
            new Rule("minecraft:windswept_forest", "rocky"),
            new Rule("minecraft:stony_peaks", "rocky"),
            // BIOMECOVERAGEPLAN F2 — vanilla has no #is_snowy tag either, so BWG's genuinely-frozen biomes (temperature
            // -0.5) never matched the explicit snowy-id list the Frozen theme resolves and fell through to Forest (a
            // green island on a glacier). Route BWG's own snowy/icy tags to the Frozen theme. Placed AFTER #is_mountain
            // on purpose: howling_peaks is both snowy and a peak and we keep it Rocky (F6 snow-caps it there) —
            // eroded_borealis (the other #biomeswevegone:snowy member) is not a mountain, so it reaches this rule.
            // #biomeswevegone:icy = shattered_glacier. NB crimson_tundra is deliberately NOT here — despite the name
            // it's a temperate grassland (temp 0.75), so it falls to the F4 plains→Meadow rule below. Inert without BWG.
            new Rule("#biomeswevegone:snowy", "frozen"),
            new Rule("#biomeswevegone:icy", "frozen"),
            new Rule("minecraft:plains", "meadow"),
            new Rule("minecraft:sunflower_plains", "meadow"),
            // (meadow + cherry_grove are routed to Meadow above, before #minecraft:is_mountain, which contains them.)
            new Rule("#minecraft:is_savanna", "meadow"),
            // BIOMECOVERAGEPLAN F4 — BWG's grassland/plains biomes sit only in #biomeswevegone:plains (no vanilla
            // is_plains tag), so the adaptive seed fell to the Forest fallback even though the Meadow theme already
            // carries dedicated flower-field bands for them (biomeswevegone_meadow). Route the whole BWG plains family to
            // Meadow so those bands fire (and pumpkin_valley gets its new pumpkin-patch band). This also correctly catches
            // crimson_tundra — despite the name it's a temperate grassland (temp 0.75), not frozen. Inert without BWG.
            new Rule("#biomeswevegone:plains", "meadow"),
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

    /** The dedicated BASE theme the Wild seed grows at {@code biome}, or {@code skyseed:forest} (a plain wooded island,
     *  <em>not</em> the build-forcing {@code explore.json}) when no rule matches — so a Wild throw in an unmapped/modded
     *  biome still grows something ordinary with only the usual ~5% building roll. */
    public static Id resolveWildBase(Holder<Biome> biome) {
        final String fam = family(biome);
        return Id.of("skyseed:" + (fam == null ? "forest" : fam));
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

    // The Nether Wild biome→family map. crimson and warped share nether_forest (it carries both looks — crimson base +
    // a warped override); the nether_wastes / unmapped fallback is nether_rocky (a plain netherrack mining rock).
    // nether_lava has no biome of its own (a lava lagoon isn't a vanilla biome), so it isn't Wild-reachable — craft it
    // directly, the same way overworld families like Ancient aren't reachable from the overworld Wild.
    private static final List<Rule> NETHER_RULES = List.of(
            new Rule("minecraft:crimson_forest", "nether_forest"),
            new Rule("minecraft:warped_forest", "nether_forest"),
            new Rule("minecraft:soul_sand_valley", "nether_soul"),
            new Rule("minecraft:basalt_deltas", "nether_basalt"));

    /** The dedicated {@code nether_<family>} name for a Nether {@code biome}, or {@code nether_rocky} (a plain
     *  netherrack rock) for {@code nether_wastes} and any unmapped biome. Never null — the Nether Wild always grows. */
    private static String netherFamily(Holder<Biome> biome) {
        for (final Rule r : NETHER_RULES) {
            if (Lookup.biomeMatches(biome, r.biome())) {
                return r.theme();
            }
        }
        return "nether_rocky";
    }

    /** The dedicated BASE theme the Nether Wild seed grows at {@code biome}. */
    public static Id resolveNether(Holder<Biome> biome) {
        return Id.of("skyseed:" + netherFamily(biome));
    }

    /** The {@code nether_<family>_large} theme the Large Nether Wild seed grows. */
    public static Id resolveNetherLarge(Holder<Biome> biome) {
        return Id.of("skyseed:" + netherFamily(biome) + "_large");
    }

    /** Whether {@code theme} is any adaptive-seed sentinel — an Explore (base/large/huge) or Wild (base/large/huge) marker. */
    public static boolean isAdaptive(Id theme) {
        if (theme == null) {
            return false;
        }
        final String v = theme.value();
        return MARKER.value().equals(v) || MARKER_LARGE.value().equals(v) || MARKER_HUGE.value().equals(v)
                || WILD.value().equals(v) || WILD_LARGE.value().equals(v) || WILD_HUGE.value().equals(v)
                || WILD_NETHER.value().equals(v) || WILD_NETHER_LARGE.value().equals(v);
    }

    /** Resolve the theme for an adaptive sentinel {@code marker} at {@code biome} — dispatching to the right tier.
     *  Large/Huge Wild share the Explore large/huge resolution (Forest as the unmapped fallback); base Wild uses
     *  {@link #resolveWildBase} (Forest fallback), where base Explore uses the build-forcing {@code explore.json}. */
    public static Id resolveFor(Id marker, Holder<Biome> biome) {
        final String v = marker.value();
        if (MARKER_LARGE.value().equals(v) || WILD_LARGE.value().equals(v)) {
            return resolveLarge(biome);
        }
        if (MARKER_HUGE.value().equals(v) || WILD_HUGE.value().equals(v)) {
            return resolveHuge(biome);
        }
        if (WILD.value().equals(v)) {
            return resolveWildBase(biome);
        }
        if (WILD_NETHER.value().equals(v)) {
            return resolveNether(biome);
        }
        if (WILD_NETHER_LARGE.value().equals(v)) {
            return resolveNetherLarge(biome);
        }
        return resolve(biome);
    }

    /** Whether the entity should <b>force</b> a rare structure for {@code marker}: {@code true} for the Explore markers
     *  (the seed's guaranteed build), {@code false} for the Wild markers (ordinary ~5% roll) and anything else. */
    public static boolean forcesRare(Id marker) {
        if (marker == null) {
            return false;
        }
        final String v = marker.value();
        return MARKER.value().equals(v) || MARKER_LARGE.value().equals(v) || MARKER_HUGE.value().equals(v);
    }
}
