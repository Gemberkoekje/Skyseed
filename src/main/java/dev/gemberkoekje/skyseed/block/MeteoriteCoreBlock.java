package dev.gemberkoekje.skyseed.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * The core buried at the centre of a dedicated meteorite island's sky-stone globe (METEORPLAN Phase 3) — the AE2
 * inscriber-press source. Replaces AE2's property-less Mysterious Cube so the drop can <b>scale with the meteor
 * tier</b>: a single block can't tier its own loot, so this one carries a {@link #TIER} blockstate (0/1/2) that its
 * loot table branches on — 1 random press (small) / 2 distinct (medium) / all 4 (huge). Placed only inside the
 * sky-stone globe ({@code MeteorPlacer}), i.e. only when AE2 is present; the block is always registered but its loot is
 * <b>tag-based and inert without AE2</b>. Worldgen-only — no BlockItem; the player mines it for the presses.
 */
public class MeteoriteCoreBlock extends Block {
    /** Meteor tier: 0 = small (1 press), 1 = medium (2 distinct), 2 = huge (all 4). Read by the block loot table. */
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 0, 2);

    public MeteoriteCoreBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TIER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIER);
    }
}
