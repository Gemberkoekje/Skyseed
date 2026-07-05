# PLANOFPLANS — Skyseed prioritized backlog

**A single prioritized view across every planning doc in the repo — only what is LEFT.** Each item links back to
the plan that owns the detail. Shipped history lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md` and git.

> **Cleaned 2026-07-05 at mod 0.228.0.** A full **code-review + documentation + plan-cleanup pass**. Since the last
> clean, everything that was "built in-branch / pending commit" **merged to main** — so the stale "pending commit"
> language is gone. **Eleven fully-shipped plans were retired** into the changelogs + git (AE2, Farmer's Delight,
> Biome-Coverage, Crash-Resume, Icon-Audit, Iron's-Content-Gap, Mystical, Portal-Twin, Structure-Long-Tail, Wild-Seed,
> and the parent Structure plan). **Thirteen still-open plans were trimmed to their open items only.** What survives:
> `EPICSTRUCTUREPLAN` (plan-first, unbuilt) and the trimmed `CONTENTPLAN` · `IEPLAN` · `IRONSPELLSPLAN` ·
> `IRONSTRUCTUREREBUILDPLAN` · `QUARK(ISLAND)PLAN` · `QUESTPLAN` · `VARIETYSTRUCTUREPLAN` · `BEAUTIFYPLAN` ·
> `REFACTORPLAN` · `SKYNETHERENDBIOMEPLAN` · `TRIALCHAMBERPLAN` · `plannednotes`.

## Headline — what's meaningfully LEFT

- **(a) In-game sign-offs on shipped content** — the biggest bucket. Everything is built and both-nodes-green; none
  runs in the headless dev env. Clear this queue first (Tier 1).
- **(b) Unbuilt content** — the **grand epic structures** (#74, `EPICSTRUCTUREPLAN`, plan-first), the flavor mods
  (#31/#32), the remaining **quest chapters** (#43/#44), the **End light pass** (`SKYNETHERENDBIOMEPLAN`, deferred),
  and an optional Prosperity island (#50).
- **(c) Standing rules / contingencies / debt tail** — per-mod calls, shader-pin refresh, further version nodes, and
  a handful of code-review follow-ups (see **Engineering debt**).

## Decisions log (standing decisions that govern the open work)

- **BWG bands (Q2): DISTRIBUTE** across typed seed families, priority-ordered per seed. *(BWG arc shipped; retained as
  the convention for future biome-band work.)*
- **Dedicated-seed bar (Q3):** only non-growable AND farm-worthy content earns a dedicated seed. Governs #60 (now
  superseded by biome-`theme_override`s). *(BWG arc shipped.)*
- **Tech backbone: Immersive Engineering; Mekanism DROPPED** (aesthetic). Cascades: #34 promoted, the Excavator fix
  #35 required, Mekanism integration + quest dead.
- **Structure scope: FULL** — all 6 villages + aspen manor + bog trial (all shipped); prairie/fossil optional (prairie
  shipped, fossil covered by `fossil_dig`).
- **Structure long tail route 1 LOCKED** — adapt BWG's own pools via a biome-keyed `theme_override` on the existing
  seed (the village pattern). No new seed items; #60 superseded. *(All shipped.)*
- **Structure Variety D1–D5** — single weighted 5% gate; 50/35/15 mix; Explore reward floor; mod builds stay derelict
  (loot never leaks a gate-key); **D5 large/huge rate = flat 5%**. *(B1–B30 shipped; #68 closed.)*
- **Iron's Spells scope (#37): the full exploration loop** (throw → grow → rare building → special loot; mod worldgen
  inert, re-homed via themes + loot GLMs).
- **FTB Quests tag-task trap:** smart-filter item tasks don't expand tags — use an advancement task.

## Snapshot by plan (survivors — open items only)

| Plan | Open items |
|---|---|
| [EPICSTRUCTUREPLAN.md](EPICSTRUCTUREPLAN.md) | **#74** grand epic structures — plan-first, unbuilt (6 grand builds + cross-wiring, 3 batches; D1–D2 locked, OD-1/2/3 open) |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | flavor mods **#31 #32**, Create-addon check **#52**, standing call **#38**, rolling **#19 #20**, **#39** FE sign-off |
| [IEPLAN.md](Modpack-growyourownworld/IEPLAN.md) | in-game verifies only (Excavator/oil/metals/aircraft) · **#39** |
| [IRONSPELLSPLAN.md](Modpack-growyourownworld/IRONSPELLSPLAN.md) | in-game fit + loot + #46 book load; `createrelics` keep/drop |
| [IRONSTRUCTUREREBUILDPLAN.md](Modpack-growyourownworld/IRONSTRUCTUREREBUILDPLAN.md) | **open:** `huge_citadel` giant tier vs keep the Mage's Sanctum rebuild; in-game fit tests |
| [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md) | **#71** in-game re-verify stones at depth + tune; optional myalite band on large/huge End forms |
| [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) | **#43** Quark quest chapter; Farmer's Cutting: Quark verify+add |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | **#43 #44** chapters, **#47** BYG scope decision, in-game book-load sign-offs |
| [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md) | in-game tuning; **Band 4 → EPICSTRUCTUREPLAN**. Engine + B1–B30 shipped (#68 closed) |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | **#21** Distant Horizons (optional), **#55** shader-pin (standing), #53/#54 dropped-revivable |
| [REFACTORPLAN.md](REFACTORPLAN.md) | **#56** route gametests through compat (deprioritized), **#59** further version nodes (discretionary), #57/#58 contingencies |
| [SKYNETHERENDBIOMEPLAN.md](SKYNETHERENDBIOMEPLAN.md) | End light pass (§5, deferred); End Wild dropped; Nether in-game throw-test |
| [TRIALCHAMBERPLAN.md](TRIALCHAMBERPLAN.md) | **#61** in-game vanilla feel/fit tune (module/motif/patina/grate/hall weights); plinth unbuilt |
| [plannednotes.md](plannednotes.md) | **#70** waystone drop-compat (unscoped); #61/#33 pointers |

*Retired 2026-07-05 (fully shipped — history in the changelogs + git): AE2PLAN, FARMERSDELIGHTPLAN, BIOMECOVERAGEPLAN,
CRASHRESUMEPLAN, ICONAUDITPLAN, IRONSCONTENTGAPPLAN, MYSTICALPLAN, PORTALTWINPLAN, STRUCTURELONGTAILPLAN, WILDSEEDPLAN,
STRUCTUREPLAN.*

---

## Priority tiers

### Tier 1 — in-game sign-offs on shipped content *(the biggest "left" bucket)*

Everything here is **BUILT and both-nodes-green**; it needs a human throw-test in a real client (nothing below runs
headless). Clearing this queue is the highest-value next work.

- [ ] **(#71)** **Quark island stones** — re-verify at low/mid/high throws (mine into the core); tune vein weights +
  the blossom / Ancient-Tome loot. *(QUARKISLANDPLAN)*
- [ ] **(#16)** **Farmer's Delight** — throw the biome islands, confirm crops + harvest→replant + rice ponds + the
  nether powdery-cane feature; quest-book load. *(retired plan — see changelog)*
- [ ] **(#34/#35)** **Immersive Engineering** — Excavator veins over the void, crude-oil pump-extractable, diesel
  aircraft consumes fuel, IE quest load. *(IEPLAN)*
- [ ] **(#18/#39)** **AE2 / meteorite** — throw-test the meteor island per tier (crater/globe/core read right; tune
  sizes + crater palette); **#39** the FE power-chain sign-off. *(retired AE2 plan — see changelog)*
- [ ] **(#61)** **Trial Chamber** feel/fit — the in-game vanilla compare + tune. *(TRIALCHAMBERPLAN)*
- [ ] **Nether biomes** — throw every `nether_*` seed + Nether Wild across all 5 Nether biomes. *(SKYNETHERENDBIOMEPLAN)*
- [ ] **Wild / Explore adaptive seeds** — throw over desert/mountains/snow/meadow/beach, confirm the right dedicated
  theme resolves (the **ExploreThemes ordering fix, 2026-07-05**, made meadow/cherry_grove→Meadow and
  snowy_beach→Frozen; re-run the gametest suites to lock it in). *(retired WildSeed plan — see changelog)*
- [ ] **Iron's Spells** — structure/rebuild fit + loot + #46 book load. *(IRONSPELLSPLAN)*
- [ ] **Crash-resume** — hard-crash sign-off (kill mid-grow → restart finishes it; `/forceload query` clean; repeat
  with a Nether twin). *(retired CrashResume plan — see changelog)*
- [ ] **Portal twins** — cross-dimension traversal lands in the paired repaired frame (option B shipped v0.225.0).
  *(retired PortalTwin plan — see changelog)*

### Tier 2 — unbuilt content

- **#74 grand epic structures** — plan-first, D1–D2 locked; 3 batches (Chapel → 3 Create movers → 2 IE verticals) +
  a cross-wiring pass. *(EPICSTRUCTUREPLAN)*
- **End light pass** — per-end-biome accents on each seed's bare end-stone form (~20 theme edits). Deferred, not
  dropped. End Wild dropped. *(SKYNETHERENDBIOMEPLAN)*
- **#31 Critters & Companions** (small) / **#32 Productive Bees** (medium) — jars not installed. *(CONTENTPLAN)*
- **#43 Quark quest chapter** (3-quest sketch ready) / **#44 Productive Bees chapter** (gated on #32). *(QUESTPLAN)*
- **#50 optional dedicated Prosperity island** (bootstrap already solved; polish only). *(retired Mystical plan)*
- **#70 waystone drop-compat** — unscoped; decide + build. *(plannednotes)*

### Tier 3 — long tail / optional / standing rules / decisions

- **Decisions:** **#47** BYG-content chapter scope · Iron's `huge_citadel` giant tier vs keep the rebuild ·
  `createrelics` keep/drop · **#52** verify Factory-Must-Grow / Extended-Cogwheels or drop.
- **Standing rules:** **#30** per-structure-step hygiene · **#38** per-mod ore-island-vs-MA call · **#55** shader-pin
  refresh · **#51** MA balance watch · **#19/#20** rolling quest-chapter + island-tier per new mod.
- **Optional visuals:** **#21** Distant Horizons · **#53/#54** dropped-revivable resource packs. *(BEAUTIFYPLAN)*
- **Icon audit:** Band A redraws shipped; Band B/C were resolved/dropped. *(retired IconAudit plan)*
- **Refactor tail:** **#56** route gametests through compat (deprioritized) · **#59** further version nodes
  (discretionary) · **#57/#58** contingencies. *(REFACTORPLAN)*

---

## Engineering debt (code review)

### 2026-07-05 code review — adversarial fan-out (this pass)

A subsystem fan-out over the shared worldgen source, findings adjudicated by re-reading the code (the automated
skeptic panel was cut short by a session limit, so the surviving findings were verified by hand).

**Fixed in-branch this pass (safe, no golden-master risk):**

| Fix | File | What & why |
|---|---|---|
| ✅ Adaptive-seed biome resolution | `ExploreThemes.java` | `meadow`/`cherry_grove` (both in `#minecraft:is_mountain`) and `snowy_beach` (in `#minecraft:is_beach`) sat *after* their broad tags, so the Wild/Explore seed grew a Rocky/Aquatic island over them and the dedicated rules were dead. Moved the specific ids ahead of the broad tags. **Behaviour change — run the gametest suites to lock it in.** |
| ✅ Doc: phantom `@link` | `PathSurfacer.java` | `supportStilts` javadoc referenced a non-existent `STILT_STUB` and claimed a stub over void; `stiltDown` actually places nothing over void. Corrected. |
| ✅ Comment accuracy | `OrePlanner.java` | The size-scaled "extra" veins run on a side RNG but still remove cells from the shared `coreSet`, so a later ore's main-stream `pickSeed` can retry more — ore volume *does* couple into the main stream (tiers above `REF_CORE`). Corrected the "doesn't move anything else" comment. |

**Recorded (behaviour-changing or low-value — need a golden-master recapture or explicit accept; do in a dedicated pass):**

| Item | Priority | What & why |
|---|---|---|
| Decouple OrePlanner size-scaling from the main stream | low | Give the "extra" veins a separate exclusion set so ore volume can't shift downstream planners. Deterministic today, so it's a latent-change hazard, not a live bug. Needs a golden-master recapture on large/huge tiers. |
| MeteorPlacer central plug without AE2 | low | A "wild meteor" (no AE2) carves the bowl but leaves the central globe volume uncarved (no sky stone fills it) → a terrain plug in the crater. Aesthetic inert-safety. |
| Crash-resume RNG desync | low | Tree/snow features consume `plan.random()` during the tick drain; a re-planned resume restarts that stream, so post-resume decoration can diverge from an uncrashed island. Within the documented best-effort-resume tradeoff. |
| `java.lang.Math` in worldgen geometry | low | `ShapeBuilder`/`RimNoise` use `Math.atan2/pow/sin` (not `StrictMath`) — deterministic per node (each has its own golden master), only a cross-platform seed-portability nit. Do NOT "fix" blindly (StrictMath is slower and would shift output). |
| `HashSet` iteration drives RNG-consuming placement | low | `PondCarver`/`CaveCarver` iterate a `HashSet` while consuming RNG. Deterministic per node (value-based hashCodes, fixed JDK; golden masters pass); a portability nit only. Changing to a sorted set would shift output. |

### Earlier reviews — ✅ shipped

- The **21 findings** from the original CODE_REVIEW (fixed + merged via PR #15) and the **2026-07-04 review** (7
  findings — both `MobPlanner` inert-safety fixes, the `Traps` gate, `findClearSpot` re-validation, the seed-derived
  `StartIsland` oak, the double-reset backup guard, the concurrent force-load ref-count) are **all merged** (0.214.0,
  PR #38), along with **5.2** persist/resume, persistent **5.3** force-load reconciliation, and **#67** observability.
  Verified present in the committed source this pass. *(One residual: `Traps` still uses vanilla wool sentinels —
  latent, since no shipped structure places decorative red/lime/yellow wool near its origin.)*
