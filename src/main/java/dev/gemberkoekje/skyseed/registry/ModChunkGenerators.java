package dev.gemberkoekje.skyseed.registry;

import com.mojang.serialization.MapCodec;
import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.worldgen.SkyseedVoidChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom chunk generators. {@code skyseed:void} keeps vanilla noise terrain (void via the noise settings) but
 * suppresses natural structures everywhere and biome decoration in the void overworld/nether — see
 * {@link SkyseedVoidChunkGenerator}. Referenced from {@code data/skyseed/worldgen/world_preset/skyblock.json}.
 */
public final class ModChunkGenerators {
    private ModChunkGenerators() {}

    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, Skyseed.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<SkyseedVoidChunkGenerator>> VOID =
            CHUNK_GENERATORS.register("void", () -> SkyseedVoidChunkGenerator.CODEC);

    public static void register(IEventBus modEventBus) {
        CHUNK_GENERATORS.register(modEventBus);
    }
}
