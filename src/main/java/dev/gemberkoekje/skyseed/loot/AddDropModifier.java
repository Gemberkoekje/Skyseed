package dev.gemberkoekje.skyseed.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * A global loot modifier that adds {@code item} to a rolled loot table's drops with probability {@code chance}.
 * The End-chapter collect-a-thon (SKYENDPLAN Phase 1) uses it to seed the Portal Frame Shard + the structure relics
 * into the vanilla structure loot tables, gated to one table each by a {@code neoforge:loot_table_id} condition in the
 * data file — so a re-grown structure island is a re-rollable source ("rare but farmable").
 *
 * <p>The {@code item} is stored as a raw {@link Id} and resolved at apply time through {@link Lookup#hasItem}: an id
 * whose mod isn't installed is silently skipped (no drop) instead of failing the datapack load, so a GLM can name a
 * modded item — an Iron's Spells scroll, an Artifact — and stay <em>inert</em> in a pack without that mod, the same
 * tolerance the theme system gives modded block ids.
 */
public class AddDropModifier extends LootModifier {
    public static final MapCodec<AddDropModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(inst.group(
                    Id.CODEC.fieldOf("item").forGetter(m -> m.item),
                    Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
            )).apply(inst, AddDropModifier::new));

    private final Id item;
    private final float chance;

    // 26.1.2 added a priority to LootModifier (the ctor + codecStart now carry an int); the CODEC's apply(::new)
    // resolves to whichever ctor arity matches the active version, so only the constructor needs the directive.
    //? if >=26.1.2 {
    /*public AddDropModifier(LootItemCondition[] conditions, int priority, Id item, float chance) {
        super(conditions, priority);
        this.item = item;
        this.chance = chance;
    }*/
    //?} else {
    public AddDropModifier(LootItemCondition[] conditions, Id item, float chance) {
        super(conditions);
        this.item = item;
        this.chance = chance;
    }
    //?}

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Inert-safe: an id whose mod isn't installed resolves to nothing, so the GLM adds no drop (and doesn't touch
        // the RNG) instead of crashing the pack — see the class note. A present id keeps the original roll exactly.
        if (Lookup.hasItem(item) && context.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(Lookup.item(item)));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
