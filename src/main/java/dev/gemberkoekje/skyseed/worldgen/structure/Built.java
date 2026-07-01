package dev.gemberkoekje.skyseed.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

/**
 * A code-authored structure template: its block states, per-position block-entity NBT, (optionally) baked entities
 * keyed by their cell (e.g. a chest minecart on a mineshaft rail), and (optionally) per-position <b>mod block ids</b>
 * that override the serialised palette {@code Name} (see {@link StructureWriter}). Most pieces have neither entities nor
 * mod ids — the two-arg form defaults them to none.
 */
public record Built(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities,
                    Map<BlockPos, CompoundTag> entities, Map<BlockPos, String> modNames) {
    public Built(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities,
                 Map<BlockPos, CompoundTag> entities) {
        this(blocks, blockEntities, entities, Map.of());
    }

    public Built(Map<BlockPos, BlockState> blocks, Map<BlockPos, CompoundTag> blockEntities) {
        this(blocks, blockEntities, Map.of(), Map.of());
    }
}
