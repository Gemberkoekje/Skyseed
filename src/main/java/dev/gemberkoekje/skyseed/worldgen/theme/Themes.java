package dev.gemberkoekje.skyseed.worldgen.theme;

import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.registry.SkyseedRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;

/**
 * Resolves an {@link IslandTheme} by id, applying any {@link ThemeOverride} patches that {@code target} it. This is the
 * single entry point the generator uses instead of a raw registry lookup, so the modpack / other mods can extend
 * islands purely by dropping {@code skyseed:theme_override} datapack files. With no patches loaded it returns the base
 * theme unchanged, so generation is byte-identical to a no-override build (the golden master is unaffected).
 */
public final class Themes {
    private Themes() {}

    /** @return the base theme {@code id} with all targeting {@link ThemeOverride}s merged in, or {@code null} if no such base theme is loaded. */
    public static IslandTheme resolve(RegistryAccess access, Id id) {
        Registry<IslandTheme> themes = Lookup.registry(access, SkyseedRegistries.THEME);
        IslandTheme base = Lookup.byId(themes, id);
        if (base == null) {
            return null;
        }
        // Apply every patch targeting this theme, in datapack-load order (registry iteration). The common case is one
        // patch per theme; with several, lists append in load order and a later patch's present scalar wins.
        Registry<ThemeOverride> overrides = Lookup.registry(access, SkyseedRegistries.THEME_OVERRIDE);
        IslandTheme result = base;
        for (ThemeOverride ov : overrides) {
            if (ov.target().equals(id)) {
                result = ov.applyTo(result);
            }
        }
        return result;
    }
}
