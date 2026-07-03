package dev.gemberkoekje.skyseed.worldgen.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * An impact meteor stamped onto the top-centre of an island (METEORPLAN, AE2PLAN #18e): a crater — obsidian + scorched
 * glass / magma / basalt — holding a <b>sky-stone globe</b> with a <b>Meteorite Core</b> at its centre (the AE2
 * inscriber-press source). {@code radius} is the sky-stone globe radius; the crater scales from it. {@code coreTier}
 * (0/1/2) is the core's press payout — small→1, medium→2 distinct, huge→all 4 (METEORPLAN Phase 3, read by the core's
 * loot table). Only the meteorite island family sets this, tuned per tier (~2 base → ~5-6 huge). See
 * {@code worldgen/MeteorPlacer}. <b>Inert without AE2</b> — the globe + core skip ({@code Lookup.hasBlock}), leaving
 * just a vanilla impact crater on a normal island.
 */
public record Meteor(IntRange radius, int coreTier) {
    public static final Codec<Meteor> CODEC = RecordCodecBuilder.create(i -> i.group(
            IntRange.CODEC.optionalFieldOf("radius", new IntRange(2, 2)).forGetter(Meteor::radius),
            Codec.intRange(0, 2).optionalFieldOf("core_tier", 0).forGetter(Meteor::coreTier)
    ).apply(i, Meteor::new));
}
