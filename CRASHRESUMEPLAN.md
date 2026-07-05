# CRASHRESUMEPLAN — persist/resume in-progress island grows + reconcile leaked force-load tickets

> Child of the **Engineering debt** section in [PLANOFPLANS.md](PLANOFPLANS.md) (items **5.2 / 5.3 / #67**, ex-CODE_REVIEW).
> Grew out of the 2026-07 crash-robustness review. This plan is the design for the two parts that **touch the on-disk
> save schema** and so were deliberately **not** built in the 2026-07-05 pass — they can only be verified by a real
> hard-crash test, and the `SkyseedWorldData` schema carries a hard invariant (a divergent 1.21.1↔26.1.2 encoding
> silently drops data across a version upgrade — no DataFixer). Build this as its own focused change with a crash-test.

## Status (2026-07-05)

**Already shipped in-branch (both nodes green — no schema change):**
- **#1 concurrent force-load un-force** — force-loads are ref-counted in
  [`GenerationJob`](src/main/java/dev/gemberkoekje/skyseed/worldgen/GenerationJob.java) (`FORCED_REFS` +
  `acquireChunk`/`releaseChunk`), so two islands sharing a chunk column don't have the first-to-finish job un-force a
  chunk the other still needs. In-memory only; cleared on stop via `GenerationJob.forgetForcedRegions()`.
- **#67 observability** — a shutdown-drain warning when `MAX_DRAIN_TICKS` fires with an island unfinished
  ([`IslandGrowth.onServerStopping`](src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGrowth.java)), plus
  per-region force-load acquire/release debug logging (`GenerationJob.setRegionForced`).

**Now implemented in-branch (both nodes green — 1.21.1 202/202, 26.1.2 204/204), pending an in-game crash sign-off:**
- **5.2** — persist/resume in-progress grows. A [`PendingIsland`](src/main/java/dev/gemberkoekje/skyseed/worldgen/PendingIsland.java)
  descriptor (re-plan inputs + progress) is built at germination ([`IslandSeedEntity`](src/main/java/dev/gemberkoekje/skyseed/entity/IslandSeedEntity.java))
  and for twins ([`TwinPlacer`](src/main/java/dev/gemberkoekje/skyseed/worldgen/TwinPlacer.java)), persisted every tick by
  `GenerationJob.tick()`, removed on completion, and on start [`CrashRecovery`](src/main/java/dev/gemberkoekje/skyseed/worldgen/CrashRecovery.java)
  re-plans + re-enqueues each one at its saved progress (`GenerationJob.resume`).
- **5.3 (persistent half)** — `GenerationJob` records/removes forced chunks in `SkyseedWorldData` at the ref-count
  transitions (so the record is the union of forced chunks), and `CrashRecovery` un-forces every leftover on start,
  before the resume.
- **Schema** — two backward-compatible optional collections on `SkyseedWorldData`, mirrored in the 1.21.1 NBT and the
  26.1.2 Codec; a `crashResumeStateRoundTrips` gametest in both suites guards the parity.

**What still needs an in-game sign-off** (can't be simulated headless): kill the process mid-grow → restart → the
island finishes and `/forceload query` is clean; repeat with a Ruined-Portal twin in the Nether. The automated tests
cover the serialization round-trip and the descriptor persist/remove lifecycle (resume from progress 0), not a real
kill-mid-grow.

## Why these two were held back

- **Save-schema risk.** [`SkyseedWorldData`](src/main/java/dev/gemberkoekje/skyseed/worldgen/SkyseedWorldData.java) has
  two serialization paths that must stay byte-identical: the 1.21.1 `save`/`load` NBT and the 26.1.2 `CODEC`. Its own
  header warns that a divergent schema silently drops the start-spawn + per-player join flags on a 1.21.1→26.1.2
  upgrade. Any new field has to be added to **both** paths, as **optional** (absent → default), mirroring the existing
  `UUID_STRING_SET` string-encoding trick.
- **Un-verifiable headless.** The payoff is only after a *hard* crash; a clean stop is already drained by
  `IslandGrowth.onServerStopping`. There's no headless way to simulate a kill-mid-grow → restart, so this needs an
  in-game sign-off before merge.

## Shared foundation — `SkyseedWorldData` schema extension

Two new **backward-compatible optional** collections on the existing overworld-scoped `SkyseedWorldData` (the single
per-world SavedData), each entry keyed by **dimension id** so twins grown in the Nether/End are covered.

Add each to **both** paths, mirroring `UUID_STRING_SET` (string lists keep NBT and Codec trivially identical — never
`BlockPos.CODEC` / `UUIDUtil.CODEC_SET`, which encode differently across the two versions):
- **1.21.1** `save`/`load`: a `ListTag`, absent key → empty (old saves load).
- **26.1.2** Codec: `optionalFieldOf(..., empty)` + a slot in the private canonical constructor.

Encodings:
- **Forced chunks (5.3):** a `ListTag` of strings `"<dimId>;<cx>;<cz>"`.
- **Pending islands (5.2):** a `ListTag` of compounds (see the `PendingIsland` fields below); on 26.1.2 a small
  `PendingIsland` record with its own Codec + `.listOf()`.

Add a static accessor `SkyseedWorldData.get(MinecraftServer)` (computes-if-absent on the overworld, handling the
dual-version `factory()` vs `TYPE` split — the same branch `WorldSetupEvents.onServerStarted` already uses) so
`GenerationJob` and the resume handler can reach it off any level.

**Guard the whole schema with a round-trip gametest in both suites:** populate both collections, save→load (1.21.1) /
encode→decode (26.1.2), assert equality. This catches schema drift headlessly — it is the single most important test.

## 5.3 — persistent force-load reconciliation

Vanilla persists forced chunks with **no owner tag** (one global `ForcedChunksSavedData`), so Skyseed must track its
own to know which to release on restart.

1. In `GenerationJob.acquireChunk`, on the `0→1` transition, also record `(dimId, cx, cz)` in `SkyseedWorldData`
   (`setDirty`). In `releaseChunk`, on `→0`, remove it. Fold this into the existing ref-count so the persisted set and
   the in-memory `FORCED_REFS` stay consistent.
2. A **clean** stop drains every job → ref-counts hit 0 → the persisted set ends **empty**. So any entry still present
   at startup is, by construction, a **crash leak**.
3. On `ServerStartedEvent`, reconcile: for every recorded `(dimId, cx, cz)`, `level.setChunkForced(cx, cz, false)` and
   clear the record. Runs **before** the 5.2 re-enqueue (though ref-counting makes a subsequent re-force safe anyway).

This half is self-contained and could ship independently of 5.2.

## 5.2 — persist/resume in-progress jobs (re-plan-from-inputs + progress)

Persist a small **`PendingIsland`** per active job — enough to *deterministically re-plan the identical island*, not
the whole `IslandPlan` (no feature/`RandomSource` serialization):

- `dimId`
- **resolved** `themeId` — the concrete theme, so an adaptive Explore/Wild seed resumes as what it resolved to, not
  re-resolved against a possibly-changed biome
- `center` (BlockPos) — the **final** post-`findClearSpot` centre; it keys the plan RNG in
  [`IslandSeedEntity.planAt`](src/main/java/dev/gemberkoekje/skyseed/entity/IslandSeedEntity.java) via
  `worldSeed ^ center.asLong()`
- `forcedBiome` id (nullable), `forcedRare` index, `forcedWaterfall` bool — the `DebugForce` inputs
- `twinTheme` id (nullable) — informational; the twin resumes as its **own** `PendingIsland` (it is enqueued as its
  own job), so no twin re-derivation is needed
- progress indices: `blockIndex`, `treeIndex`, `treesPlaced`, `scatterIndex`, `finalizeStep`

**Lifecycle:**
- Write on enqueue (in `IslandSeedEntity.germinate`, right after `IslandGrowth.enqueue`), progress 0.
- Checkpoint the indices at each finalize-step boundary and every N block-ticks — **not** every tick (`setDirty` I/O).
- Remove on completion (when `GenerationJob.tick()` returns true).

**Resume** (`ServerStartedEvent`, after the 5.3 reconcile): for each `PendingIsland`, rebuild the plan via
`IslandGenerator.planIsland(level, center, theme, biome, RandomSource.create(worldSeed ^ center.asLong()), force)`,
construct a `GenerationJob` **seeded to the checkpointed indices** (add a package-private resume constructor / index
setters), and `IslandGrowth.enqueue` it. It re-forces its region fresh and finishes the remaining blocks/mobs/snow.

**The one real tradeoff — entity duplication.** Blocks/trees/scatter are idempotent (`setBlock` re-places
identically); the finalize entity step (`finalizeStep == 1`: mobs/villagers/animals/hive bees/golems) is **not**.
Because `finalizeStep` is checkpointed *after* each step completes, a crash landing *inside* the ~1-tick entity step
re-runs it on resume → a few duplicate mobs.
- **Recommended:** accept the narrow window and document it (a couple of extra mobs, vs. today's *lose all unfinished
  content*). The alternative — scan the footprint and remove Skyseed-spawned persistent mobs before respawning — is
  fragile (no reliable "ours" marker) and not worth it.

**ServerStarted ordering** (one handler, in order — sits next to the existing `WorldSetupEvents.onServerStarted`):
1. **5.3 reconcile** — un-force stale chunks + clear their records.
2. **5.2 resume** — re-plan + enqueue each `PendingIsland` at its checkpointed progress.

(1 before 2 so we never un-force a chunk a resumed job is about to re-force — belt-and-braces, since the re-force is
ref-counted anyway.)

## Risks

- **Schema divergence** between the NBT and Codec paths → silent data loss on a version upgrade. Mitigate: identical
  string/int encodings + the round-trip gametest above.
- **Determinism drift on re-plan.** Re-planning reproduces the identical island only if the resolved theme + centre +
  biome + force + world seed are all captured **and** the theme datapack hasn't changed between crash and restart. A
  datapack change in that window could diverge from already-placed blocks (rare, low-impact — a slightly inconsistent
  island). Note it; don't guard it.
- **Entity duplication** in the narrow finalize window (above).

## Test plan

Headless-runnable (add to both gametest suites):
- `SkyseedWorldData` NBT↔Codec round-trip equality with forced-chunks **and** pending-islands populated.
- A `GenerationJob` seeded to a mid-progress index drains to completion and lands the full island (construct a plan,
  set the indices, drain, assert the terrain/mobs are complete) — covers the resume mechanics without a real crash.

In-game sign-off (the part that can't be automated):
- Throw a huge structure island, kill the process mid-grow, restart → the island finishes; `/forceload query` shows no
  leaked chunks. Repeat with a Ruined-Portal twin grown in the Nether (the player-less-dimension case).

## Sequencing

1. **Schema + round-trip gametest** (the foundation; ship the gametest with it).
2. **5.3 persistent reconcile** (self-contained; the smaller, lower-risk half).
3. **5.2 persist/resume** (descriptor + checkpointing + the resume constructor + the `ServerStarted` handler).
4. **In-game crash sign-off**, then per the standing #30 rule a version bump + CHANGELOG entry, both nodes green.
