package dev.gemberkoekje.skyseed.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

/**
 * Void-dimension chunk generator ({@code skyseed:void}). It is an ordinary noise generator — terrain is whatever the
 * noise settings produce, which for Skyseed is {@code final_density: 0.0} (pure void) — but it <b>suppresses natural
 * structures everywhere</b> and biome-feature <b>decoration where {@code skipDecoration} is set</b>.
 *
 * <p>Why a custom generator: Skyseed keeps the vanilla {@code multi_noise} overworld biome source (island theming
 * reads the biome a seed lands in — see {@code IslandGenerator}), and that is exactly what TerraBlender biome mods
 * (BYG/BWG/Terralith…) hook to inject biomes. Their biome <i>features</i> then decorate at the void floor (~y=-64),
 * and "Generate Structures" would scatter villages across the void. Suppressing both at the generator makes the void
 * immune to any biome/structure mod while leaving the biome source intact for theming. See {@code plannednotes.md}.
 *
 * <ul>
 *   <li><b>Overworld / Nether</b> — {@code skip_decoration: true}: no biome features (Skyseed content is
 *       seed-driven, not decoration), no structures. (Carvers aren't overridden — a void has nothing to carve.)</li>
 *   <li><b>End</b> — {@code skip_decoration: false}: decoration kept so the {@code central_end_island} feature still
 *       lays the dragon's arena, while natural End cities are still suppressed.</li>
 * </ul>
 *
 * <p>Structures are off regardless of the world-creation "Generate Structures" toggle — Skyseed structures are grown
 * from seeds instead. Only affects worlds created with this generator (it is inlined into {@code level.dat}).
 */
public final class SkyseedVoidChunkGenerator extends NoiseBasedChunkGenerator {

    public static final MapCodec<SkyseedVoidChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
                    NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
                    Codec.BOOL.optionalFieldOf("skip_decoration", false).forGetter(g -> g.skipDecoration)
            ).apply(instance, SkyseedVoidChunkGenerator::new));

    private final boolean skipDecoration;

    public SkyseedVoidChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, boolean skipDecoration) {
        super(biomeSource, settings);
        this.skipDecoration = skipDecoration;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    /** Skip biome-feature decoration in the void dims that don't use it (overworld/Nether); the End keeps it. */
    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        if (skipDecoration) {
            return;
        }
        super.applyBiomeDecoration(level, chunk, structureManager);
    }

    // No structure starts ⇒ nothing places, any dimension, any mod — players grow structures from Skyseed seeds.
    // createStructures gained a 6th param (ResourceKey<Level> level) on 26.1.2, so it's the one signature that needs a
    // per-node form. Fully-qualified types keep the guarded branches self-contained (no version-divergent imports).
    //? if >=26.1.2 {
    /*@Override
    public void createStructures(net.minecraft.core.RegistryAccess registryAccess,
                                 net.minecraft.world.level.chunk.ChunkGeneratorStructureState state,
                                 net.minecraft.world.level.StructureManager structureManager,
                                 net.minecraft.world.level.chunk.ChunkAccess centerChunk,
                                 net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureTemplateManager,
                                 net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level) {
    }*/
    //?} else {
    @Override
    public void createStructures(net.minecraft.core.RegistryAccess registryAccess,
                                 net.minecraft.world.level.chunk.ChunkGeneratorStructureState state,
                                 net.minecraft.world.level.StructureManager structureManager,
                                 net.minecraft.world.level.chunk.ChunkAccess centerChunk,
                                 net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureTemplateManager) {
    }
    //?}
}
