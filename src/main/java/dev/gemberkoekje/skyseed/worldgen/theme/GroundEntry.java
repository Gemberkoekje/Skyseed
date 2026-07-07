package dev.gemberkoekje.skyseed.worldgen.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Id;

import java.util.Optional;

/**
 * A ground-cover plant placed on a surface column with per-column probability {@code chance}. {@code grow}, when set, is
 * a range of bonemeal applications a placed {@link net.minecraft.world.level.block.BonemealableBlock} receives after the
 * island lands (via {@code GenerationJob}), so a crop lands at a VARIED growth stage instead of all as age-0 stubs —
 * used by the Nether powdery cane, whose configured feature won't place through Skyseed's direct call.
 */
public record GroundEntry(Id block, float chance, Optional<IntRange> grow) {
    public static final Codec<GroundEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
            Id.CODEC.fieldOf("block").forGetter(GroundEntry::block),
            Codec.FLOAT.fieldOf("chance").forGetter(GroundEntry::chance),
            IntRange.CODEC.optionalFieldOf("grow").forGetter(GroundEntry::grow)
    ).apply(i, GroundEntry::new));
}
