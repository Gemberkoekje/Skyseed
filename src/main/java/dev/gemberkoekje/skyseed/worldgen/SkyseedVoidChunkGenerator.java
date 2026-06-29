package dev.gemberkoekje.skyseed.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
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

    // The structure- and decoration-suppression overrides below use the 1.21.1 ChunkGenerator signatures. On 26.1.2
    // those signatures changed (GenerationStep.Carving removed; createStructures/applyBiomeDecoration params differ),
    // so they're scoped to <26.1.2 for now — see the TODO. Fully-qualified types keep imports clean on both nodes.
    //? if >=26.1.2 {
    /*// TODO(26.1.2, REFACTORPLAN Stage 2): re-add createStructures + applyBiomeDecoration with the 26.1.2 signatures
    // so the void is enforced there too. Until then skyseed:void behaves like a plain noise generator on 26.1.2
    // (same as the pre-existing minecraft:noise behaviour — no regression).*/
    //?} else {
    /** No structure starts ⇒ nothing places, any dimension, any mod — players grow structures from Skyseed seeds. */
    @Override
    public void createStructures(net.minecraft.core.RegistryAccess registryAccess,
                                 net.minecraft.world.level.chunk.ChunkGeneratorStructureState structureState,
                                 net.minecraft.world.level.StructureManager structureManager,
                                 net.minecraft.world.level.chunk.ChunkAccess chunk,
                                 net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager templateManager) {
        // intentionally empty — see class doc
    }

    /** Skip biome-feature decoration in the void dims that don't use it (overworld/nether); the End keeps it. */
    @Override
    public void applyBiomeDecoration(net.minecraft.world.level.WorldGenLevel level,
                                     net.minecraft.world.level.chunk.ChunkAccess chunk,
                                     net.minecraft.world.level.StructureManager structureManager) {
        if (skipDecoration) {
            return;
        }
        super.applyBiomeDecoration(level, chunk, structureManager);
    }
    //?}
}
