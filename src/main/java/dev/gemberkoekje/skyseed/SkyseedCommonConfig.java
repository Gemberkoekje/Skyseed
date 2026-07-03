package dev.gemberkoekje.skyseed;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Common config (both sides), persisted in {@code config/skyseed-common.toml}. Holds {@link #WILD_METEOR_CHANCE} —
 * the odds that a natural overworld island also carries a small "wild" meteor (a sky-stone globe with NO Meteorite
 * Core), the standalone way to find your first sky stone for the Meteorite Skyseed (METEORPLAN Phase 2). The modpack
 * ships this at {@code 0.0} (wild meteors off; the meteor comes only from the dedicated seed).
 */
public final class SkyseedCommonConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue WILD_METEOR_CHANCE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        WILD_METEOR_CHANCE = builder
                .comment("Chance [0..1] that a NATURAL overworld island (forest / desert / meadow / rocky / … — not a",
                        "structure, village, ladder or the dedicated meteorite) grown IN THE OVERWORLD also carries a",
                        "small wild meteor: a sky-stone globe with NO Meteorite Core (no free presses). It's the",
                        "standalone way to find your first sky stone for the Meteorite Skyseed. Set 0 to disable — the",
                        "modpack does, gating the meteor behind the dedicated seed instead.")
                .defineInRange("wildMeteorChance", 0.01, 0.0, 1.0);
        SPEC = builder.build();
    }

    private SkyseedCommonConfig() {}

    /** The configured wild-meteor chance, or {@code 0.0} if the config hasn't loaded yet (e.g. early worldgen/tests). */
    public static double wildMeteorChance() {
        try {
            return WILD_METEOR_CHANCE.get();
        } catch (IllegalStateException notLoaded) {
            return 0.0;
        }
    }
}
