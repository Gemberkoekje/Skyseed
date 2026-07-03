package dev.gemberkoekje.skyseed.registry;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.block.MeteoriteCoreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Mod blocks. The mod is otherwise worldgen-only, so this holds just the {@link MeteoriteCoreBlock} — the tiered press
 * source at a meteorite island's centre (METEORPLAN Phase 3). The 2-arg {@code registerBlock(name, factory)} is shared
 * by both nodes (21.1 / 26.1), so the block properties are applied inside the factory — no Stonecutter split. No
 * BlockItem: the core is placed by generation and mined for its presses, never held as an item.
 */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Skyseed.MODID);

    public static final DeferredBlock<MeteoriteCoreBlock> METEORITE_CORE = BLOCKS.registerBlock(
            "meteorite_core",
            props -> new MeteoriteCoreBlock(props
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops() // iron-tier (see needs_iron_tool tag) — matches the sky-stone globe
                    .sound(SoundType.STONE)));

    private ModBlocks() {}

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
