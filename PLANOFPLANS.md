# PLANOFPLANS — Skyseed prioritized backlog

**A single prioritized view across every planning doc in the repo — only what is LEFT.** Each item links back
to the plan that owns the detail. Item numbers are stable (they carry over from the previous backlog; closed
numbers are simply gone — the shipped history lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md` and git).

> **Cleaned 2026-07-02 at mod 0.191.0** (refreshed after the round of in-game sign-offs). All
> fully-shipped-and-signed-off items were removed, and the three fully-complete BWG plans were **retired** into the
> changelogs + git — `BWGPLAN.md` (wood/flowers), `BWGSWAMPVILLAGEPLAN.md` (cypress bayou), and `BWGVILLAGEPLAN.md`
> (all six village styles, now that #14 biome-reachability is signed off). Their still-governing decisions were
> carried into the Decisions log below, and the villages' reusable string-id structure engine into STRUCTUREPLAN.
> The trial-chamber vanilla redesign shipped end-to-end (#24 + #25, v0.189–v0.191). Shipped sections inside the
> still-open plans were trimmed to changelog pointers.

## Headline

- The **BWG arc is done end-to-end** — woods, flowers, planks, guide, quests, and all six village styles × three
  tiers, plus the door-flush fix (**#72**) and the cypress overwater bayou (**#73**), all shipped **and signed off
  in-game** (v0.170.0–v0.188.0). Its dedicated plans are retired.
- The **Trial Chamber** shipped its vanilla redesign (**#24** aged-copper palette/lighting/cornice + multi-story-downward
  layout, **#25** greebling, v0.189–v0.191) and then a **corridor-WARREN restructure** (**#61**, v0.194.0) so it reads
  like a real smaller vanilla trial dungeon — atrium → winding passages → junctions → chambers-on-spurs, not rooms
  bolted on. Both nodes green; the in-game feel/fit is the pending sign-off.
- **In-game sign-offs on shipped content are cleared** (2026-07-02): #3/#10/#66/#64/#65 earlier, plus **#14** (village
  biomes reachable) and **#15** (Quark smoke pass). **#71** Quark island stones — Y-band bug **fixed v0.192.0** +
  limestone/jasper/shale made abundant across all tiers **v0.193.0**; awaiting an in-game re-verify.
- What's meaningfully **left**: two **in-game re-verifies** (#71 Quark stones at all depths, #61 the trial warren's
  feel/fit); the **next content-mod wave** (Farmer's Delight, Immersive Engineering + AE2, each with its quest chapter
  + gated island tier); and a **standing-rule / contingency tail** (visuals, further version nodes, engineering debt).
- Best value next: throw-test #71 + #61, then open the **content-mod wave** (Farmer's Delight is the highest-ROI mod).

## Decisions log (standing decisions that govern the open work)

- **BWG bands (Q2): DISTRIBUTE** across typed seed families, priority-ordered per seed — the same biome may appear
  in several families with different emphasis. *(BWG arc shipped; retained as the convention for any future biome-band work.)*
- **BWG dedicated-seed bar (Q3):** only non-growable AND farm-worthy content earns a dedicated seed — nothing
  currently qualifies. Governs **#60**. *(BWG arc shipped.)*
- **fir is the documented non-growable BWG plank** (no configured tree feature in 2.6.0) → the 24/25 planks are
  island-obtainable, gametest-guarded. *(BWG arc shipped.)*
- **Tech backbone: Immersive Engineering; Mekanism DROPPED** (aesthetic call). Cascades: **#34** promoted, the
  Excavator fix **#35** required, Mekanism integration + quest chapter dead. [CONTENTPLAN §7](Modpack-growyourownworld/CONTENTPLAN.md).
- **Quark: SHIPPED for NeoForge 1.21.1** — 4 jars in (Quark 4.1-481, Zeta 1.1-40, Quark Oddities marker,
  QuarkPonders 1.5.1) + modules curated + the void-death **Totem of Holding** relocated to a lit island shrine
  (v0.182.0). **#15 smoke pass signed off** 2026-07-02; **#43** quest sketch is queued; island integration (**#71**)
  is shipped but has a Y-band bug (Tier 2). Plans: [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) /
  [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md).
- **Structure scope: FULL** — all 6 villages (✅ shipped) + aspen manor + bog trial; prairie/fossil stay optional.
  [STRUCTUREPLAN § Scope](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **Structure vehicle: our OWN jigsaw set in themed palettes** (hermetic string-id `.nbt` engine — no BWG on any
  classpath). The manor/trial must make the same route choice (adapt pools vs author own).
  [STRUCTUREPLAN § Mechanism](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **FTB Quests tag-tasks trap:** smart-filter item tasks don't expand tags — use an advancement task.
  [QUESTPLAN § Approach](Modpack-growyourownworld/QUESTPLAN.md).

## Snapshot by plan

| Plan | What it covers | Open items |
|---|---|---|
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration | #34 #35 #18 #16 #31 #32 #36 #37 #38 #39 #52 + rolling #19 #20 |
| [IEPLAN.md](Modpack-growyourownworld/IEPLAN.md) | Immersive Engineering + flight + petroleum (child of CONTENTPLAN) | jars in; **shipped:** #34 ore island + crude-oil pocket, #45 quest chapter, #35 excavator pinned (defaultconfigs, chance 0.7); FD×IE compat verified fine; **left:** in-game verifies · #39 FE proof · add Quark Engineering jar |
| [AE2PLAN.md](Modpack-growyourownworld/AE2PLAN.md) | Applied Energistics 2 + Create/IE bridges (child of CONTENTPLAN) | jars in; **integration SHIPPED** — #18a meteorite island theme+seed, #18d certus deposit, #18c IE+certus seed gate, **#18b presses from the tiered Meteorite Core** (sky-stone recipes dropped; MA press+sky_stone essence bypasses removed; custom `skyseed:meteorite_core` block, small→1/medium→2-distinct/huge→4, iron-tier harvest), **#41 quest chapter (16 quests in Storage `B404`–`B419`)**, **#39 FE-flow proven on paper**. Meteorite worldgen already suppressed. Immersive Energistics + MA certus seed both confirmed present. **#18e meteor redesign SHIPPED — Phase 1 (visual) + Phase 2 (compat/wild meteors) + Phase 3 (tiered core); all both-nodes-green** → [METEORPLAN.md](Modpack-growyourownworld/METEORPLAN.md). **#41 quest chapter VERIFIED in-game 2026-07-04** (renders + deps resolve; user repositioned nodes so lines don't cross). **Left (in-game human verify only, can't run in dev env):** #39 power-chain sign-off, meteor throw-tests/tuning |
| [METEORPLAN.md](Modpack-growyourownworld/METEORPLAN.md) | Meteor island redesign (child of AE2PLAN #18e) | **Phase 1 SHIPPED 2026-07-03 (both nodes green):** overworld body + crater + sky-stone globe + Mysterious Cube, `MeteorPlacer` + codec-slot refactor, gametest `meteorIslandFormsCrater`. **Phase 2 SHIPPED 2026-07-04 (both nodes green):** AE2 compat layer (`Ae2Compat` conditional seed reg + AE2-gated jar default recipe of 8 sky stone + certus + tag-based advancements so nothing hard-refs AE2) and a **1% wild meteor** (no cube, sky stone only) on natural overworld islands as the standalone bootstrap — position-RNG roll (no gen disruption), toggled off in the pack via the new `wildMeteorChance` common config (`skyseed-common.toml = 0.0`). **Phase 3 SHIPPED 2026-07-04 (both nodes green):** tiered press drops — a custom `skyseed:meteorite_core` block (skyseed's first block) replaces the Mysterious Cube so the drop scales by tier (small→1 random / medium→2 distinct, uniform over 6 `expand:false` pair-tags / huge→all 4); loot is tag-based → inert without AE2. In-game throw-test/tune pending |
| [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) | Quark integration (child of CONTENTPLAN) | #43 quest sketch (+ partner-gated add-ons) |
| [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md) | Quark × island integration (child of QUARKPLAN) | #71 (Y-band fix shipped v0.192.0 — in-game re-verify) |
| [FARMERSDELIGHTPLAN.md](Modpack-growyourownworld/FARMERSDELIGHTPLAN.md) | Farmer's Delight integration (child of CONTENTPLAN) | #16 (jars in; crops shipped — dry crops + rice + chorus + nether, 20 overrides + gametests), #42 quest SHIPPED (A008). Pending: in-game throw-test + quest-book load |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | structures long tail | #26 #27 #28 #29 #30 #49 #60 #68 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | future chapters #41–#47 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture (shipped) | #50 #51 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visuals (shipped) | #21 #55 (+ optional revivals #53 #54) |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version build (shipped) | #56 #59 (+ contingencies #57 #58) |
| [plannednotes.md](plannednotes.md) | Trial Chamber tail + misc | #33 #61 #70 |
| Engineering debt (ex-CODE_REVIEW) | crash-robustness follow-ups | 5.2, 5.3, #67 |

---

## Priority tiers

### Tier 1 — in-game sign-offs on shipped content

**✅ Cleared (2026-07-02):** #3/#10/#66/#64/#65 earlier, plus **#14** (all village biomes reachable) and **#15**
(Quark smoke pass — all works).

- [ ] **(#71)** **Quark island stones — re-verify after the fix (v0.192.0).** The Y-band bug is fixed (the veins now
  merge into every Rocky/Ancient Y-band, so they no longer vanish on low/high throws). Confirm in-game: throw Rocky +
  Ancient islands at **low, mid, and high** Y and **mine into the core** — you should find limestone/jasper (rocky),
  jasper/shale (ancient) + the occasional deep blue-corundum geode at every depth. Then tune vein weights, and sign
  off the blossom/Ancient-Tome loot half of #71. *(QUARKISLANDPLAN)*

*(**#61** trial rooms — grander/less-square — needs dev work first; it's in Tier 3.)*

### Tier 2 — the content-mod wave *(in ROI order; each is followed by its quest chapter #19 and gated tier #20)*

- **#16** Farmer's Delight — **curated 8-mod set is in `overrides/mods/` and boot-verified** (all load clean; one
  bounded netherwood cutting-recipe casualty + two cosmetic warns). **Wild-crop island injection** is the open work.
  *(CONTENTPLAN → [FARMERSDELIGHTPLAN.md](Modpack-growyourownworld/FARMERSDELIGHTPLAN.md) · medium)*
- **#34** Immersive Engineering — the tech backbone (bauxite/aluminum island + FE), **gated on #35** the Excavator
  fix (island-aware ore mix preferred, else disable + hide in JEI). *(CONTENTPLAN · large + medium)*
- **#18** Applied Energistics 2 — **curated jar set + Create/IE bridges landed**; open work is the sky-stone island
  bootstrap and the **inscriber-press** blocker (presses are meteorite-loot-only and uncraftable → needs a bespoke
  source before the chapter). *(→ [AE2PLAN.md](Modpack-growyourownworld/AE2PLAN.md) · medium)*
- Then their quest chapters: **#45** IE, **#41** AE2, **#43** Quark. *(**#42** FD chapter ✅ shipped — A008.)* *(QUESTPLAN)*
- *(**#39** FE-flow proof is blocked until the first FE consumer lands — see "Not yet testable" below.)*

### Tier 3 — structures & trial-chamber polish

- **#61** Trial Chamber — **corridor-warren first pass SHIPPED v0.194.0.** Reworked the jigsaw flow so it reads like a
  smaller vanilla trial dungeon (atrium → winding passages → T-junctions → chambers on spurs + a small cell + descents),
  not chambers bolted onto the atrium. Both nodes green. **In-game feel/fit is yours to confirm** (does it wind/branch,
  chambers open off junctions, stays inside the island); tune hall/junction weights + jigsaw `depth` after the look.
- **#33** Trial Chamber more variants — partly done by the warren (corner/junction/cell/descent); further options:
  cross-intersections, alcove-corridors, bigger multi-cell chambers, vaulted ceilings. *(plannednotes)*
- **#26** aspen manor + **#27** bog trial — first decision: vehicle (adapt pools vs author own set like the
  villages); each carries **#28** (vertical-jigsaw bounding-box mitigation + placement gametest) and **#29** (on-pad
  assembly check); **#30** release hygiene rides every step. *(STRUCTUREPLAN · medium each)*

### Tier 4 — long tail / optional / future

- Flavor mods: **#31** Critters & Companions (small), **#32** Productive Bees (medium), **#36** Iron's Spells (large;
  decide scope **#37** first). *(CONTENTPLAN)*
- Optional visuals: **#21** Distant Horizons (unblocked), **#53** Vanilla Tweaks revival, **#54** standalone resource
  pack. *(BEAUTIFYPLAN)*
- Standing rules (work only when triggered): **#55** shaderPack pin refresh on Complementary/Euphoria updates;
  **#30** per-structure-step hygiene; **#38** per-future-mod ore-island-vs-MA call.
- Refactor tail: **#59** further version nodes (discretionary — the recipe is ready), **#56** route gametest suites
  through compat (deprioritized), **#57**/**#58** contingencies (shared-suite fingerprint map / per-version data
  variant — build only when needed). *(REFACTORPLAN)*
- **#52** verify The Factory Must Grow + Extended Cogwheels for 1.21.1, or drop them. *(CONTENTPLAN · small)*
- **#47** clarify the "BYG content" future-chapter scope (BWG's branch already shipped — deeper BWG coverage, or the
  separate BYG mod?). *(QUESTPLAN · decision)*
- **#49** prairie houses / rugged fossil, **#60** dedicated structure seeds (fallback), **#68** net-new bespoke
  structures (Create sheds, abandoned Inferium farmlands, … — future want). *(STRUCTUREPLAN)*
- **#50** optional dedicated Prosperity island. *(MYSTICALPLAN · medium)*
- **#70** waystone drop-compat idea (decision + build, unscoped). *(plannednotes)*
- **#51** MA balance — an **ongoing watch** (Growth Accelerator stacking + mob Inferium drop rates). Deferred out of
  Tier 1: revisit only if a normal playthrough surfaces a problem, once there's enough feedback to act on. *(MYSTICALPLAN)*

---

## Not yet testable (blocked until a dependency lands)

Parked here so Tier 1 stays a list of things that can actually be done now. Move an item up to its testable tier the
moment its blocker lands.

- **#39** Prove FE flows Create → IE/AE2 across islands — **unblocked** (IE + AE2 both landed) and **proven on paper**
  (C&A → Flux → IE/AE2 Energy Acceptor, all standard NeoForge FE — AE2PLAN #39). Only the one-time **in-game
  sign-off** remains (a Tier-1 verify), not a blocker. *(CONTENTPLAN / AE2PLAN)*

---

## Full ranked backlog (open items only)

| # | Item | Plan | Priority | Effort | Status |
|---|---|---|---|---|---|
| 71 | Quark island stones — Y-band bug **fixed v0.192.0** (veins now merge into every overworld Y-band across the 6 `quark_{rocky,ancient}{,_large,_huge}` files; ids verified; gametest-guarded). Remaining: in-game re-verify at low/mid/high throws + tune weights + blossom/Ancient-Tome loot sign-off | QUARKISLANDPLAN | medium | medium | fix shipped (in-game re-verify) |
| 16 | Farmer's Delight — curated 8-mod set in `overrides/mods/` (base FD 1.3.2 + End's/My Nether's/Ocean's + Autochef's/Chef's/Chopper's/FD Extended), **boot-verified all load clean**. Wild-crop island injection = open | FARMERSDELIGHTPLAN | medium | medium | jars in + boot-verified; injection pending |
| 34 | Immersive Engineering — bauxite/aluminum island + FE (the tech backbone) | CONTENTPLAN | medium | large | open (gated on 35) |
| 35 | IE Excavator fix — island-aware ore mix (preferred) else disable + hide in JEI | CONTENTPLAN | medium | medium | open |
| 18 | AE2 — jars in; **integration SHIPPED 2026-07-03 (both nodes green):** #18a meteorite island theme+seed, #18d rocky/ancient certus deposit, #18c IE+certus seed gate, #18b sky-stone press recipes. Left: meteorite-disable config, #41 quest, #39 FE proof, in-game throw-tests | AE2PLAN | medium | medium | built; in-game verify pending |
| 19 | Rolling: quest chapter per newly-landed mod | QUESTPLAN / CONTENTPLAN | medium | rolling | partial |
| 20 | Rolling: gated island tier per newly-landed mod | CONTENTPLAN | medium | rolling | partial |
| 61 | Trial Chamber corridor-WARREN — atrium → passages → junctions → chambers-on-spurs + cell + descents (reads like a real vanilla trial dungeon). First pass shipped v0.194.0, both nodes green | plannednotes | medium | medium | shipped (in-game feel/fit) |
| 33 | Trial Chamber more variants — partly done by the warren (corner/junction/cell); further: cross-intersections, alcove-corridors, multi-cell/vaulted chambers | plannednotes | low | large | open |
| 26 | Resurrect aspen manor (vehicle decision first) | STRUCTUREPLAN | low | medium | open |
| 27 | Resurrect bog trial (vehicle decision first) | STRUCTUREPLAN | low | medium | open |
| 28 | Vertical-jigsaw bounding-box mitigation + placement gametest (rides 26/27) | STRUCTUREPLAN | low | medium | rider |
| 29 | On-pad assembly verification (rides 26/27; done for villages) | STRUCTUREPLAN | low | medium | rider |
| 30 | Per-structure-step release hygiene (standing rule) | STRUCTUREPLAN | low | small | standing rule |
| 31 | Critters and Companions — spawn verification on biome islands | CONTENTPLAN | low | small | open |
| 32 | Productive Bees — starter bees/hives | CONTENTPLAN | low | medium | open |
| 36 | Iron's Spells — loot/mob injection | CONTENTPLAN | low | large | open (decide 37 first) |
| 37 | Decide Iron's Spells scope (full discovery loop vs crafted-only) | CONTENTPLAN | low | small | decision |
| 38 | Per-future-mod call: bespoke ore island vs MA seeds | CONTENTPLAN | low | small | standing rule |
| 39 | Prove FE flows Create → IE/AE2 across islands — **proven on paper** (C&A `alternator` → Flux `plug`/`point` → IE native / AE2 `energy_acceptor`, all standard NeoForge FE; see AE2PLAN #39). Left: one-time in-game sign-off | CONTENTPLAN / AE2PLAN | low | small | proven; in-game sign-off pending |
| 41 | AE2 quest chapter — **SHIPPED** (16 quests `B404`–`B419` in the **Storage** chapter, not a new chapter; gated off IE steel `B908`). In-game book-load pending | QUESTPLAN / AE2PLAN | low | small | shipped (in-game load pending) |
| 42 | Farmer's Delight quest chapter — **SHIPPED** (`chapters/farmersdelight.snbt`, A008, B801–B808 + lang). In-game quest-book load pending | QUESTPLAN | low | small | shipped (in-game load pending) |
| 43 | Future chapter: Quark (quest — minimal 3-quest sketch in QUARKPLAN) | QUESTPLAN | low | unknown | unblocked (#15 ✅) — build last |
| 44 | Future chapter: Productive Bees (quest) | QUESTPLAN | low | unknown | gated on 32 |
| 45 | Future chapter: Immersive Engineering (quest — promoted with the backbone decision) | QUESTPLAN | medium | unknown | gated on 34 |
| 46 | Future chapter: Iron's Spells (quest) | QUESTPLAN | low | unknown | gated on 36 |
| 47 | Clarify "BYG content" chapter scope (BWG branch already shipped) | QUESTPLAN | low | small | decision |
| 49 | Prairie houses / rugged fossil (optional polish) | STRUCTUREPLAN | low | small | open |
| 50 | Optional dedicated Prosperity island | MYSTICALPLAN | low | medium | open |
| 51 | MA balance watch (Growth Accelerator stacking, Inferium drops) | MYSTICALPLAN | low | small | ongoing watch |
| 52 | Verify Create addons: The Factory Must Grow, Extended Cogwheels (Deco ✅ shipped) | CONTENTPLAN | low | small | partial |
| 53 | Vanilla Tweaks 16× pack (deliberately dropped; revival recipe in BEAUTIFYPLAN §2) | BEAUTIFYPLAN | low | medium | dropped/revivable |
| 54 | Standalone Skyseed resource pack (dropped; re-scaffold recipe in BEAUTIFYPLAN §3) | BEAUTIFYPLAN | low | medium | dropped/revivable |
| 55 | Refresh shaderPack pin when Complementary/Euphoria update (pin currently correct) | BEAUTIFYPLAN | low | small | standing rule |
| 56 | Route gametest suites' direct API calls through compat | REFACTORPLAN | low | medium | open (deprioritized) |
| 57 | Version-keyed golden-master map for a future SHARED suite | REFACTORPLAN | low | small | contingency |
| 58 | Per-version data variant for a future vanilla block-id rename | REFACTORPLAN | low | small | contingency |
| 59 | Add further Minecraft/NeoForge version nodes (recipe ready) | REFACTORPLAN | low | large | discretionary |
| 60 | Dedicated structure seeds instead of biome adaptation | STRUCTUREPLAN | low | medium | deferred fallback |
| 68 | Net-new bespoke structures beyond BWG's 17 (Create sheds, abandoned Inferium farmlands, …) | STRUCTUREPLAN | low | large | future want |
| 70 | Waystone drop-compat idea (decide + build) | plannednotes | low | unknown | idea |
| 21 | Distant Horizons (LOD) — optional, unblocked | BEAUTIFYPLAN | low | medium | open |

---

## Engineering debt (ex-CODE_REVIEW.md)

All **21 code-review findings were fixed and merged via PR #15** (CI green both nodes; the 5.1/5.2/5.3 in-game
smoke tests passed 2026-07-01) — the review doc itself is retired. These are the deferred follow-ups it left,
each self-documented in the code:

| Item | Priority | Effort | What & why |
|---|---|---|---|
| **5.2 follow-up — persist/resume of in-progress GenerationJobs.** The shipped fix drains jobs synchronously on `ServerStoppingEvent` (`IslandGrowth`, `MAX_DRAIN_TICKS`); a crash (not a clean stop) still loses unfinished island content after the seed was consumed. Upgrade: persist the `IslandPlan` + progress indices in `SkyseedWorldData` and re-enqueue on server start. Code note: `IslandGrowth.java:48`. | medium | large | crash-robustness |
| **5.3 follow-up — force-load ticket leak reconciliation.** `GenerationJob` force-loads its chunk region for the job's lifetime via raw `setChunkForced`; a hard crash mid-grow leaves the region permanently force-loaded (recoverable only with `/forceload remove`). Fix: track forced regions in `SkyseedWorldData`, clear stale ones on `ServerStartedEvent`. Code note: `GenerationJob.java:170`. | medium | medium | crash-robustness |
| **#67 — crash-fix observability.** The 2026-07-01 smoke tests passed but the user could not verify `MAX_DRAIN_TICKS` adequacy or ticket release — nothing is surfaced. Add a LOGGER warning when a shutdown drain hits `MAX_DRAIN_TICKS` unfinished, and log force-load ticket acquire/release (or document `/forceload query`). | low | small | makes 5.2/5.3 sign-offs checkable |
