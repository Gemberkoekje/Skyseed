package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import dev.gemberkoekje.skyseed.compat.Id;
import dev.gemberkoekje.skyseed.compat.Ids;
import dev.gemberkoekje.skyseed.compat.Lookup;
import dev.gemberkoekje.skyseed.worldgen.theme.IslandTheme;
import dev.gemberkoekje.skyseed.worldgen.theme.Themes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/**
 * Recovers the crash-robustness state a HARD crash (not a clean stop, which drains) can leave behind, using the records
 * {@link SkyseedWorldData} persisted at the last world-save (engineering-debt 5.2 / 5.3 — see {@code CRASHRESUMEPLAN.md}).
 * Runs once, on {@link ServerStartedEvent}, in two ordered steps:
 *
 * <ol>
 *   <li><b>Reconcile leaked force-loads (5.3)</b> — un-force every chunk still recorded as force-loaded. A clean stop
 *       drains all jobs (so the record ends empty), so anything here is a chunk left permanently forced by a crash.</li>
 *   <li><b>Resume in-progress grows (5.2)</b> — re-plan each pending island deterministically (same theme id, centre,
 *       biome, force → same plan) and re-enqueue it fast-forwarded to the last saved progress, so the (already-consumed)
 *       seed's island finishes instead of staying half-built.</li>
 * </ol>
 *
 * Step 1 runs before step 2 so a resumed job's fresh force-load isn't immediately un-forced. A descriptor that can no
 * longer be re-planned (its dimension/theme/biome went away, or the theme now fizzles here — e.g. a datapack changed) is
 * dropped rather than retried forever.
 */
@EventBusSubscriber(modid = Skyseed.MODID)
public final class CrashRecovery {
    private CrashRecovery() {}

    @SubscribeEvent
    static void onServerStarted(ServerStartedEvent event) {
        final MinecraftServer server = event.getServer();
        final SkyseedWorldData data = SkyseedWorldData.get(server);
        reconcileForcedChunks(server, data);
        resumePendingIslands(server, data);
    }

    /** Step 1 (5.3): un-force every recorded chunk (a crash leak) and clear the record. */
    private static void reconcileForcedChunks(MinecraftServer server, SkyseedWorldData data) {
        int unforced = 0;
        for (String entry : data.forcedChunks()) {
            final String[] parts = entry.split(";");
            if (parts.length != 3) {
                continue;
            }
            final ServerLevel level = levelFor(server, parts[0]);
            if (level == null) {
                continue; // dimension gone (removed by a mod) — nothing to un-force
            }
            try {
                level.setChunkForced(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), false);
                unforced++;
            } catch (NumberFormatException ignored) {
                // malformed record — just drop it with the clear below
            }
        }
        data.clearForcedChunks();
        if (unforced > 0) {
            Skyseed.LOGGER.warn("[skyseed] un-forced {} chunk(s) left force-loaded by a previous hard crash", unforced);
        }
    }

    /** Step 2 (5.2): re-plan + re-enqueue each pending island at its last saved progress; drop any that can't re-plan. */
    private static void resumePendingIslands(MinecraftServer server, SkyseedWorldData data) {
        int resumed = 0;
        for (PendingIsland p : data.pendingIslands()) {
            try {
                if (resumeOne(server, p)) {
                    resumed++;
                } else {
                    data.removePendingIsland(p.key());
                }
            } catch (Exception e) {
                Skyseed.LOGGER.warn("[skyseed] could not resume a crashed island grow at {} — dropping it", p.key(), e);
                data.removePendingIsland(p.key());
            }
        }
        if (resumed > 0) {
            Skyseed.LOGGER.info("[skyseed] resuming {} island grow(s) left unfinished by a previous hard crash", resumed);
        }
    }

    /** @return true if the island was re-planned and re-enqueued; false if it can no longer be grown (drop it). */
    private static boolean resumeOne(MinecraftServer server, PendingIsland p) {
        final ServerLevel level = levelFor(server, p.dimId());
        if (level == null) {
            return false;
        }
        final IslandTheme theme = Themes.resolve(level.registryAccess(), Id.of(p.themeId()));
        if (theme == null) {
            return false; // the theme id went away (datapack changed)
        }
        final BlockPos center = p.center();
        final Holder<Biome> biome = p.forcedBiome().isEmpty()
                ? level.getBiome(center)
                : Lookup.biomeHolder(level.registryAccess(), Id.of(p.forcedBiome()));
        if (biome == null) {
            return false; // a debug forced biome that no longer exists
        }
        // Re-validate: a datapack change between the crash and restart could make the theme fizzle here now — don't
        // grow an island the live gate would reject. Matches germinate + findClearSpot's per-spot validity check.
        if (!IslandGenerator.formValidFor(theme, biome, center.getY(), Lookup.dimensionId(level.dimension()))) {
            return false;
        }
        // Deterministic re-plan — identical inputs (worldSeed ^ centre RNG, theme, biome, force) → identical plan, so the
        // saved progress indices line up with the blocks already on disk from the last world-save.
        final RandomSource random = RandomSource.create(level.getSeed() ^ center.asLong());
        final IslandPlan plan = IslandGenerator.planIsland(level, center, theme, biome, random,
                new DebugForce(p.forcedRare(), p.forcedWaterfall()));
        IslandGrowth.enqueue(GenerationJob.resume(level, plan, p));
        return true;
    }

    /** The {@link ServerLevel} for a persisted dimension id string, or null if that dimension is absent. */
    private static ServerLevel levelFor(MinecraftServer server, String dimId) {
        final var location = Ids.parse(dimId);
        if (location == null) {
            return null;
        }
        return server.getLevel(ResourceKey.create(Registries.DIMENSION, location));
    }
}
