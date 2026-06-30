package dev.gemberkoekje.skyseed.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Ids;
import net.neoforged.fml.ModList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Scans the shipped {@code skyseed:theme} JSON at mod-construction time and derives the full set of debug seeds so we
 * never hand-maintain them: one per theme {@code biome_overrides} entry (germinate that theme forced to a representative
 * biome) and one per {@code rare_structures} entry (germinate that theme forcing the otherwise chance-gated structure).
 * Add an override or a rare structure to any theme and its debug seed appears next launch — no list, model, or lang
 * edits. {@link ModItems} registers a {@link dev.gemberkoekje.skyseed.item.IslandSeedItem} per spec; the names are
 * composed at runtime and the icons reuse the base theme's seed texture (a client model hook), so no per-seed files.
 */
public final class ThemeScanner {
    private ThemeScanner() {}

    /**
     * One auto debug seed. {@code id} is the registered name (no {@code _skyseed} suffix); {@code baseTheme} is the
     * theme it germinates; exactly one forcing drives what it shows — {@code forcedBiome} (force this biome),
     * {@code forcedRare} &ge; 0 (force the rare structure at that index into {@code rare_structures}), or
     * {@code forcedWaterfall} (force the ladder shaft's waterfall variant).
     */
    public record DebugSeedSpec(String id, String baseTheme, String label, Id forcedBiome,
                                int forcedRare, boolean forcedWaterfall) {}

    public static List<DebugSeedSpec> scan() {
        final List<DebugSeedSpec> out = new ArrayList<>();
        final Set<String> ids = new HashSet<>();
        try {
            // Base themes: a debug seed's baseTheme is the file's own theme id (no rare-index offset — base
            // rare_structures sit at indices 0..n-1 in the resolved theme).
            final java.util.Map<String, String> baseThemes = gather("theme");
            baseThemes.forEach((theme, json) -> scanTheme(theme, json, out, ids, 0));
            // First-party theme_overrides (Create / MA / BWG ...): the biome_overrides (and rare_structures) a patch
            // ADDS get debug seeds too, attributed to the override's `target` theme — so e.g. the BWG wood bands, which
            // live in theme_override and not the base forest theme, still appear as debug seeds. The base themes are
            // passed in so an override's rare_structures can be indexed past the base's (see scanOverride). Scanned after
            // the base themes so a base id wins a collision (the override's then gets a numeric suffix).
            gather("theme_override").forEach((file, json) -> scanOverride(json, baseThemes, out, ids));
        } catch (Exception e) {
            Skyseed.LOGGER.warn("[skyseed] debug-seed scan skipped: {}", e.toString());
        }
        Skyseed.LOGGER.info("[skyseed] auto debug seeds: {}", out.size());
        return out;
    }

    /** Every {@code .json} under {@code data/skyseed/skyseed/<subdir>/} from the mod's own file, as a sorted
     *  (relative-name -&gt; raw JSON) map so the derived debug-seed ids stay stable across runs. */
    private static java.util.Map<String, String> gather(String subdir) throws java.io.IOException {
        final java.util.Map<String, String> files = new java.util.TreeMap<>();
        final String prefix = "data/skyseed/skyseed/" + subdir + "/";
        //? if >=26.1.2 {
        /*ModList.get().getModFileById(Skyseed.MODID).getFile().getContents().visitContent(
                prefix.substring(0, prefix.length() - 1), (path, resource) -> {
                    String name = path.replace('\\', '/');
                    if (!name.endsWith(".json")) {
                        return;
                    }
                    final int at = name.indexOf(prefix);
                    if (at >= 0) {
                        name = name.substring(at + prefix.length());
                    } else if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    try {
                        files.put(name.substring(0, name.length() - ".json".length()),
                                new String(resource.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        Skyseed.LOGGER.warn("[skyseed] debug-seed scan could not read {}: {}", path, e.toString());
                    }
                });*/
        //?} else {
        final Path dir = ModList.get().getModFileById(Skyseed.MODID).getFile()
                .findResource("data", "skyseed", "skyseed", subdir);
        if (dir != null && Files.isDirectory(dir)) {
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.filter(p -> p.getFileName().toString().endsWith(".json")).forEach(p -> {
                    try {
                        files.put(relName(dir, p), Files.readString(p));
                    } catch (Exception e) {
                        Skyseed.LOGGER.warn("[skyseed] debug-seed scan could not read {}: {}", p, e.toString());
                    }
                });
            }
        }
        //?}
        return files;
    }

    /** A {@code theme_override} patch: scan the content it ADDS (biome_overrides / rare_structures) as debug seeds
     *  attributed to its {@code target} theme (so a patch's biome bands get debug seeds without editing a base theme). */
    private static void scanOverride(String json, java.util.Map<String, String> baseThemes,
                                     List<DebugSeedSpec> out, Set<String> ids) {
        try {
            final JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (!root.has("target")) {
                return;
            }
            final String target = root.get("target").getAsString();
            final int colon = target.indexOf(':');
            final String theme = colon < 0 ? target : target.substring(colon + 1);
            // Themes.resolve concatenates base ++ override rare_structures (ThemeOverride.applyTo), so an override's
            // rare structure at override-index j lives at resolved index baseRareCount + j. Offset the forced index by
            // the base theme's rare count so the debug seed germinates the override's added structure, not the base's.
            scanTheme(theme, json, out, ids, rareCount(baseThemes.get(theme)));
        } catch (Exception e) {
            Skyseed.LOGGER.warn("[skyseed] debug-seed override scan failed: {}", e.toString());
        }
    }

    /** The number of {@code rare_structures} a base theme JSON declares (0 if absent / unparseable / not found). */
    private static int rareCount(String json) {
        if (json == null) {
            return 0;
        }
        try {
            final JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            return root.has("rare_structures") ? root.getAsJsonArray("rare_structures").size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private static void scanTheme(String theme, String json, List<DebugSeedSpec> out, Set<String> ids, int rareOffset) {
        // Skip the gametest/* scaffolding themes: they're test-only (used directly by the gametests), have no
        // island_seed_<theme> icon texture, and shouldn't appear as creative-tab debug seeds — generating one only
        // yields a model the client can't resolve. Keeps the auto debug seeds to real content.
        if (theme.startsWith("gametest/")) {
            return;
        }
        try {
            final JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (root.has("biome_overrides")) {
                for (JsonElement el : root.getAsJsonArray("biome_overrides")) {
                    final JsonObject ov = el.getAsJsonObject();
                    if (!ov.has("biomes")) {
                        continue;
                    }
                    final Id biome = firstConcreteBiome(ov.getAsJsonArray("biomes"));
                    if (biome == null) {
                        continue;
                    }
                    final String id = unique("debug_" + theme + "_" + biome.path(), ids);
                    out.add(new DebugSeedSpec(id, theme, theme + " [" + biome.path() + "]", biome, -1, false));
                }
            }
            if (root.has("rare_structures")) {
                final JsonArray arr = root.getAsJsonArray("rare_structures");
                for (int i = 0; i < arr.size(); i++) {
                    final String tag = rareTag(arr.get(i).getAsJsonObject(), i);
                    final String id = unique("debug_" + theme + "_" + tag, ids);
                    // forcedRare indexes the RESOLVED theme's rare_structures list; for an override patch that is the
                    // base list with this patch's entries appended, hence the rareOffset (0 for a base theme).
                    out.add(new DebugSeedSpec(id, theme, theme + " (" + tag + ")", null, rareOffset + i, false));
                }
            }
            // A ladder shaft with a non-zero waterfall_chance is a rare roll of the ladder island — cover its variant.
            if (root.has("ladder_shaft")) {
                final JsonObject shaft = root.getAsJsonObject("ladder_shaft");
                final float chance = shaft.has("waterfall_chance") ? shaft.get("waterfall_chance").getAsFloat() : 0.05f;
                if (chance > 0f) {
                    final String id = unique("debug_" + theme + "_waterfall", ids);
                    out.add(new DebugSeedSpec(id, theme, theme + " (waterfall)", null, -1, true));
                }
            }
        } catch (Exception e) {
            Skyseed.LOGGER.warn("[skyseed] debug-seed scan failed for theme {}: {}", theme, e.toString());
        }
    }

    /** Theme id from the file path: the path under {@code dir} with the {@code .json} suffix dropped. */
    private static String relName(Path dir, Path file) {
        final String rel = dir.relativize(file).toString().replace('\\', '/');
        return rel.substring(0, rel.length() - ".json".length());
    }

    /**
     * Resolve the first usable biome in an override's {@code biomes} list to a concrete biome id: a plain id verbatim,
     * or a {@code #ns:is_X} tag mapped to {@code ns:X} (every override tag follows that shape, and {@code X} is a real
     * biome). An odd tag we can't map to a single biome is skipped.
     */
    private static Id firstConcreteBiome(JsonArray biomes) {
        for (JsonElement b : biomes) {
            String s = b.getAsString();
            if (s.startsWith("#")) {
                s = s.substring(1);
                final int colon = s.indexOf(':');
                final String ns = colon < 0 ? "minecraft" : s.substring(0, colon);
                final String path = colon < 0 ? s : s.substring(colon + 1);
                if (!path.startsWith("is_")) {
                    continue;
                }
                s = ns + ":" + path.substring("is_".length());
            }
            if (Ids.parse(s) != null) {
                return Id.of(s);
            }
        }
        return null;
    }

    /** A short tag for a rare structure: its jigsaw pool's first path segment ({@code ancient_city/plaza} →
     *  {@code ancient_city}), else {@code rare<index>}. */
    private static String rareTag(JsonObject rare, int index) {
        if (rare.has("jigsaw")) {
            final JsonObject jig = rare.getAsJsonObject("jigsaw");
            if (jig.has("pool")) {
                final String poolStr = jig.get("pool").getAsString();
                if (Ids.parse(poolStr) != null) {
                    final String path = Id.of(poolStr).path();
                    final int slash = path.indexOf('/');
                    return slash < 0 ? path : path.substring(0, slash);
                }
            }
        }
        return "rare" + index;
    }

    /** A registry-safe id, made unique against {@code ids} by suffixing a counter on collision. */
    private static String unique(String base, Set<String> ids) {
        final String safe = base.replaceAll("[^a-z0-9_]", "_");
        String id = safe;
        int n = 2;
        while (!ids.add(id)) {
            id = safe + "_" + (n++);
        }
        return id;
    }
}
