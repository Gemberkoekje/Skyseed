package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.theme.AnimalPack;
import dev.gemberkoekje.skyseed.worldgen.theme.MobEntry;
import dev.gemberkoekje.skyseed.worldgen.theme.Pond;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Plans an island's entity spawns: theme/variant mob sprinkles ({@link #planMobs}), Animal-Island / rare-structure
 * packs ({@link #rollAnimals}), and submerged pond water mobs ({@link #planPondMobs}). Returns spawn descriptors;
 * {@link GenerationJob} actually summons them once the island has landed.
 */
final class MobPlanner {
    private MobPlanner() {}

    /** Entity types that must be spawned submerged (Aquarium) — they die if placed on dry land. */
    private static final Set<EntityType<?>> AQUATIC = Set.of(
            EntityType.AXOLOTL, EntityType.SQUID, EntityType.GLOW_SQUID, EntityType.TURTLE,
            EntityType.TROPICAL_FISH, EntityType.COD, EntityType.SALMON, EntityType.PUFFERFISH, EntityType.DOLPHIN,
            EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN);

    /** Pick one weighted pack and expand it into concrete animal spawns jittered around the enclosure centre. */
    static void rollAnimals(List<AnimalPack> packs, BlockPos center, List<IslandPlan.AnimalSpawn> out, RandomSource random) {
        final int total = packs.stream().mapToInt(AnimalPack::weight).sum();
        if (total <= 0) {
            return;
        }
        int roll = random.nextInt(total);
        AnimalPack chosen = packs.get(0);
        for (AnimalPack p : packs) {
            if (roll < p.weight()) {
                chosen = p;
                break;
            }
            roll -= p.weight();
        }
        for (AnimalPack.Entry e : chosen.entries()) {
            final EntityType<?> type = resolveEntity(e.entity());
            if (type == null) {
                continue;
            }
            final boolean water = AQUATIC.contains(type);
            for (int i = 0; i < e.adults() + e.babies(); i++) {
                final int dx = random.nextInt(3) - 1;
                final int dz = random.nextInt(3) - 1;
                // Land: stand on the pad (pos = surface, spawn goes on top). Water: a block up, inside the tank.
                final BlockPos pos = new BlockPos(center.getX() + dx, center.getY() + (water ? 1 : 0), center.getZ() + dz);
                out.add(new IslandPlan.AnimalSpawn(type, pos, i >= e.adults(), water));
            }
        }
    }

    /** Roll each configured mob and pick random surface columns to spawn them on (after generation). */
    static List<IslandPlan.MobSpawn> planMobs(List<MobEntry> cfg, List<BlockPos> surfaceList, RandomSource random) {
        final List<IslandPlan.MobSpawn> out = new ArrayList<>();
        if (cfg.isEmpty() || surfaceList.isEmpty()) {
            return out;
        }
        for (MobEntry m : cfg) {
            if (random.nextFloat() >= m.chance()) {
                continue;
            }
            final EntityType<?> type = resolveEntity(m.entity());
            if (type == null) {
                continue;
            }
            final int n = m.count().sample(random);
            for (int i = 0; i < n; i++) {
                out.add(new IslandPlan.MobSpawn(type, surfaceList.get(random.nextInt(surfaceList.size())), false));
            }
        }
        return out;
    }

    /** Roll each pond water mob and pick random submerged positions inside the carved water columns. */
    static List<IslandPlan.MobSpawn> planPondMobs(Map<BlockPos, BlockState> blockMap, BlockPos center, int waterY,
                                                  Pond pond, Set<Long> carved, RandomSource random) {
        final List<IslandPlan.MobSpawn> out = new ArrayList<>();
        if (pond.waterMobs().isEmpty() || carved.isEmpty()) {
            return out;
        }
        final int bottomY = waterY - Math.max(0, pond.depth() - 1);
        final List<int[]> cols = new ArrayList<>();
        for (long k : carved) {
            cols.add(new int[]{(int) (k >> 32), (int) k});
        }
        for (MobEntry m : pond.waterMobs()) {
            if (random.nextFloat() >= m.chance()) {
                continue;
            }
            final EntityType<?> type = resolveEntity(m.entity());
            if (type == null) {
                continue;
            }
            final int n = m.count().sample(random);
            for (int i = 0; i < n; i++) {
                final int[] c = cols.get(random.nextInt(cols.size()));
                final int wx = center.getX() + c[0];
                final int wz = center.getZ() + c[1];
                // Spawn below the surface (water above them) so squid/glow squid stay submerged. Roll against the GLOBAL
                // deepest floor (same RNG draw as before, so generation stays byte-identical), then clamp UP to this
                // column's actual water floor: a sloped basin shallows toward the rim, so a rim column's water spans only
                // its top blocks. An unclamped roll near bottomY would land in solid body below the water, and
                // GenerationJob.spawnMobs would silently drop the mob — leaving the pond with fewer mobs than configured.
                final int rolled = bottomY + random.nextInt(Math.max(1, waterY - bottomY));
                final int y = Math.min(waterY, Math.max(rolled, waterFloorIn(blockMap, wx, wz, waterY, bottomY)));
                out.add(new IslandPlan.MobSpawn(type, new BlockPos(wx, y, wz), true));
            }
        }
        return out;
    }

    /** The lowest carved-water Y in a column (its floor), scanned down from {@code waterY}, bounded by {@code deepest}. */
    private static int waterFloorIn(Map<BlockPos, BlockState> blockMap, int wx, int wz, int waterY, int deepest) {
        int f = waterY;
        while (f - 1 >= deepest) {
            final BlockState s = blockMap.get(new BlockPos(wx, f - 1, wz));
            if (s == null || !s.getFluidState().is(FluidTags.WATER)) {
                break;
            }
            f--;
        }
        return f;
    }

    /** @return the entity type for {@code id}, or {@code null} (logged) if it isn't a registered entity. */
    private static EntityType<?> resolveEntity(Id id) {
        if (Lookup.hasEntityType(id)) {
            return Lookup.entityType(id);
        }
        Skyseed.LOGGER.warn("[skyseed] theme references unknown entity '{}' — skipping", id);
        return null;
    }
}
