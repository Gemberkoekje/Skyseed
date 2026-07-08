package dev.gemberkoekje.skyseed.worldgen;

import dev.gemberkoekje.skyseed.Skyseed;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Server-side scheduler for tick-budgeted island placement. Germinating seeds enqueue a
 * {@link GenerationJob}; each server tick every active job places its next budget of blocks until done.
 */
@EventBusSubscriber(modid = Skyseed.MODID)
public final class IslandGrowth {
    private static final List<GenerationJob> JOBS = new ArrayList<>();
    /** Safety cap on the per-job shutdown drain so a never-completing job can't hang the stop (far above any real
     *  island: ~hundreds of ticks even for a huge structure island). */
    private static final int MAX_DRAIN_TICKS = 100_000;

    private IslandGrowth() {}

    public static void enqueue(GenerationJob job) {
        JOBS.add(job);
    }

    @SubscribeEvent
    static void onServerTick(ServerTickEvent.Post event) {
        if (JOBS.isEmpty()) {
            return;
        }
        Iterator<GenerationJob> it = JOBS.iterator();
        while (it.hasNext()) {
            if (it.next().tick()) {
                it.remove();
            }
        }
    }

    @SubscribeEvent
    static void onServerStopping(ServerStoppingEvent event) {
        // Finish any island still growing before the world's final save, so a clean save/quit (or server stop) mid-grow
        // completes the island now rather than deferring it. A hard crash is already covered separately: a real island's
        // progress is persisted every tick (GenerationJob.tick -> SkyseedWorldData) and resumed on the next load by
        // CrashRecovery (5.2). This drain (bounded by MAX_DRAIN_TICKS) is the clean-stop optimisation — it finishes each
        // descriptor-backed job immediately (a completed job clears its own pending record) so the next load has nothing
        // left to resume.
        int unfinished = 0;
        for (GenerationJob job : new ArrayList<>(JOBS)) {
            int guard = 0;
            boolean done = false;
            while (guard++ < MAX_DRAIN_TICKS) {
                if (job.tick()) { // drain the job's remaining budgeted steps to completion
                    done = true;
                    break;
                }
            }
            if (!done) {
                unfinished++;
            }
        }
        if (unfinished > 0) {
            // Observability (engineering-debt #67): the safety cap fired before an island finished. A real (descriptor-
            // backed) island is NOT lost — its last-saved progress persists (onServerStopping clears only the in-memory
            // JOBS + force-ref-counts, not the pending-island records), so CrashRecovery resumes it on the next load;
            // the content is deferred, not dropped. If this ever shows up, MAX_DRAIN_TICKS is too low for some island,
            // or a job is wedged and never completing.
            Skyseed.LOGGER.warn("[skyseed] {} growing island(s) did not finish within MAX_DRAIN_TICKS ({}) on shutdown "
                    + "— they will be resumed on the next load; consider raising the cap", unfinished, MAX_DRAIN_TICKS);
        }
        JOBS.clear(); // and don't carry jobs into a later server (e.g. another singleplayer world)
        GenerationJob.forgetForcedRegions(); // clear the in-memory force-load ref-counts alongside the jobs
    }
}
