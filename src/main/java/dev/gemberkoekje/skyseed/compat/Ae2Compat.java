package dev.gemberkoekje.skyseed.compat;

import net.neoforged.fml.ModList;

/**
 * Applied Energistics 2 optional-dependency gate (METEORPLAN Phase 2). Skyseed has <b>no hard AE2 dependency</b> —
 * the Meteorite Skyseed (its seed item + jar recipe) is a compat feature, present only when AE2 is installed. The
 * meteorite <em>theme</em> JSON still ships (harmless dead data without AE2, since {@code MeteorPlacer} skips the
 * sky-stone globe + Meteorite Core via {@code Lookup.hasBlock}); only the craftable seed is gated on this flag.
 * Mirrors the {@code ModList.get().isLoaded(...)} pattern used by {@link PatchouliCompat} / {@code ModonomiconCompat}.
 */
public final class Ae2Compat {
    private Ae2Compat() {}

    /** True when Applied Energistics 2 is installed. Evaluated once at mod construction. */
    public static final boolean LOADED = ModList.get().isLoaded("ae2");
}
