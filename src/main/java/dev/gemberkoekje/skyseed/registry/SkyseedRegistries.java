package dev.gemberkoekje.skyseed.registry;

import dev.gemberkoekje.skyseed.compat.Ids;
import dev.gemberkoekje.skyseed.worldgen.theme.IslandTheme;
import dev.gemberkoekje.skyseed.worldgen.theme.ThemeOverride;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * Custom datapack registries. {@link #THEME} holds {@link IslandTheme}s loaded from
 * {@code data/<namespace>/skyseed/theme/*.json}. Server-side only (generation is server-side — see README → Design decisions),
 * so no network codec is supplied.
 */
public final class SkyseedRegistries {
    public static final ResourceKey<Registry<IslandTheme>> THEME =
            ResourceKey.createRegistryKey(Ids.mod("theme"));

    /**
     * Patches merged onto {@link #THEME} themes at resolution time (see {@code Themes}/{@code ThemeOverride}). A
     * separate registry so the modpack or third-party mods can extend Skyseed islands by dropping a datapack file —
     * e.g. a Create-compat patch adding zinc ore to the rocky island.
     */
    public static final ResourceKey<Registry<ThemeOverride>> THEME_OVERRIDE =
            ResourceKey.createRegistryKey(Ids.mod("theme_override"));

    private SkyseedRegistries() {}

    public static void onNewDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(THEME, IslandTheme.CODEC);
        event.dataPackRegistry(THEME_OVERRIDE, ThemeOverride.CODEC);
    }
}
