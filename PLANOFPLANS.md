# PLANOFPLANS — Skyseed prioritized backlog

**A single prioritized view across every planning doc in the repo — only what is LEFT.** Each item links back
to the plan that owns the detail. Item numbers are stable (they carry over from the previous backlog; closed
numbers are simply gone — the shipped history lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md` and git).

> **Regenerated 2026-07-01 at mod 0.181.0** by a full plan-vs-repo audit: every plan doc was re-verified
> item-by-item against the code, the datapack, the gametests, both changelogs, `mods.txt`, and the modpack
> configs. All fully-done items were removed from the plans; `CODE_REVIEW.md` was retired entirely (all 21
> findings fixed + merged via PR #15 — its three open follow-ups are carried in
> [Engineering debt](#engineering-debt-ex-code_reviewmd) below).

## Headline

- **~40 open items** remain — about half are **in-game sign-offs and small fixes** on already-shipped content;
  the other half is the **next content-mod wave** (IE / AE2 / Quark / Farmer's Delight / …) and the
  **structure long tail** (manor, bog trial, trial-chamber polish).
- The BWG arc is **done end-to-end** (woods, flowers, planks 24/25, guide, quests, and all six village styles
  × three tiers, v0.170.0–v0.181.0). Its in-game sign-offs are **done** (#3, #10, #66; #14 Phase 5 assembly +
  reachability for red_rock/cypress), leaving only two follow-ups they surfaced — **#72** (village doors) and **#73**
  (cypress overwater bayou) — plus two biome reachability spot-checks.
- The two **wet-wood quality fixes** in-game testing flagged (**#64** broad shallow marsh, **#65** the ≥1-tree
  guarantee) and the **#69** quest-text refresh all **shipped v0.186.0** (both nodes green); #64/#65 now just want an
  in-game re-throw sign-off. Best value next: the remaining **Tier 1 sign-offs** (they de-risk everything shipped).

## Decisions log (standing decisions that govern the open work)

- **Q2 (BWG bands): DISTRIBUTE** across typed seed families, priority-ordered per seed — same biome may appear
  in several families with different emphasis. File convention in [BWGPLAN § Q2](Modpack-growyourownworld/BWGPLAN.md).
- **Tech backbone: Immersive Engineering; Mekanism DROPPED** (aesthetic call). Cascades: #34 promoted, the
  Excavator fix #35 required, Mekanism integration + quest chapter dead. [CONTENTPLAN §7](Modpack-growyourownworld/CONTENTPLAN.md).
- **Quark: SHIPPED for NeoForge 1.21.1** — the **4 jars are in** (Quark 4.1-481, Zeta 1.1-40, Quark Oddities marker,
  QuarkPonders 1.5.1) and the modules are **curated** (overlaps + void-dead worldgen off). The **Totem of Holding now
  works in the void**: Skyseed relocates a void-death totem to a lit shrine at the island band (v0.182.0 — the totem is
  otherwise unreachable). #15 is now in progress (only the in-game smoke-pass sign-off remains); #43's minimal quest
  sketch is queued. **Island integration is committed as #71** ([QUARKISLANDPLAN](Modpack-growyourownworld/QUARKISLANDPLAN.md)).
  Own plan: [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md).
- **Structure scope: FULL** — all 6 villages (✅ since shipped) + aspen manor + bog trial; prairie/fossil stay
  optional. [STRUCTUREPLAN § Scope decision](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **Village vehicle: our OWN jigsaw set in BWG palettes** (hermetic string-id `.nbt` engine — no BWG on any
  classpath). The manor/trial must make the same route choice (adapt BWG pools vs author own). [STRUCTUREPLAN § Mechanism](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **BWG dedicated-seed bar (Q3):** only non-growable AND farm-worthy content earns a dedicated seed — nothing
  currently qualifies. [BWGPLAN § Q3](Modpack-growyourownworld/BWGPLAN.md).
- **fir is the documented non-growable BWG plank** (no configured tree feature in 2.6.0) → 24/25 planks
  island-obtainable, gametest-guarded.
- **FTB Quests tag-tasks trap:** smart-filter item tasks don't expand tags — use an advancement task
  ([QUESTPLAN § Approach](Modpack-growyourownworld/QUESTPLAN.md)).

## Snapshot by plan

| Plan | What it covers | Open items |
|---|---|---|
| [BWGPLAN.md](Modpack-growyourownworld/BWGPLAN.md) | BWG woods/flowers integration | #64/#65 in-game sign-off (fixes shipped) |
| [BWGVILLAGEPLAN.md](Modpack-growyourownworld/BWGVILLAGEPLAN.md) | BWG villages (shipped) | #14 Phase-5 spot-checks + #72 door fix |
| [BWGSWAMPVILLAGEPLAN.md](Modpack-growyourownworld/BWGSWAMPVILLAGEPLAN.md) | Cypress swampland villages overwater rework (child of BWGVILLAGEPLAN) | #73 |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration | #34 #35 #18 #16 #31 #32 #36 #37 #38 #39 #52 + rolling #19 #20 |
| [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) | Quark integration (child of CONTENTPLAN) | #15 (smoke-pass sign-off) (+ #43 quest sketch) |
| [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md) | Quark × island integration (child of QUARKPLAN) | #71 |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | BWG structures long tail | #26 #27 #28 #29 #30 #49 #60 #68 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | future chapters #41–#47 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture (shipped) | #50 #51 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visuals (shipped) | #21 #55 (+ optional revivals #53 #54) |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version build (shipped) | #56 #59 (+ contingencies #57 #58) |
| [plannednotes.md](plannednotes.md) | Trial Chamber polish, misc | #24 #25 #33 #61 #70 |
| Engineering debt (ex-CODE_REVIEW) | crash-robustness follow-ups | 5.2, 5.3, #67 |

---

## Priority tiers

### Tier 1 — sign-offs & small fixes on shipped content *(do these first)*

- ✅ **#3 / #10 / #66 — DONE** (2026-07-02 in-game sign-off); **#14 Phase 5** largely signed off (assembly ✅ + reachability for red_rock/cypress), leaving two biome spot-checks + the follow-ups it surfaced: **#72** (village doors face inward) and **#73** (cypress → stilted overwater bayou). See the [checklist](#-in-game-verification-checklist-for-you-to-test).
- ✅ **#64 — SHIPPED (v0.186.0):** the wet-wood water feature is now a broad shallow swamp/marsh (`depth: 2`, `slope`, raised `extent` 0.6/0.62/0.68) across all ×3 Aquatic tiers; gametest-guarded. **Needs an in-game re-throw sign-off** (water reads as marsh) — see the [checklist](#-in-game-verification-checklist-for-you-to-test). *(BWGPLAN)*
- ✅ **#65 — SHIPPED (v0.186.0):** the `forceOneTree` guarantee now grades a real 5×5 planting clearing so big BWG NBT trees (willow/cypress) can't silently fail to zero on a cramped pad; base-tier `tries` 4→6. **Needs an in-game re-throw sign-off** (Small grows ≥1 tree, Huge Bayou grows willows). *(BWGPLAN)*
- ✅ **#69 — DONE (v0.186.0):** quest **B602 "Prosperity Found"** text refreshed — leads with the accessible Lush stone ores, Ancient framed as the richer deepslate option. *(MYSTICALPLAN)*
- ✅ **#22 — DONE (v0.186.0):** the held wet/semi forest biomes (flower/cherry/grove/mangrove/swamp/river/mushroom/bamboo) lifted to the canonical per-tier forest density (7/40/120 base/large/huge); both nodes green. *(BWGPLAN)*
- ✅ **#23 — DONE (v0.186.0):** BWG config coherence pass complete (biome injection ON; all 35 targeted biomes enabled; `eroded_borealis: false` confirmed harmless — holly reachable via `dacite_ridges`); `mods.txt` skyseed jar bumped 0.179.0 → 0.186.0. *(BWGPLAN)*

### Tier 2 — the content-mod wave *(in ROI order; each is followed by its quest chapter #19 and gated tier #20)*

- **#15** Quark + Zeta — cheap building/QoL breadth. **Availability verified 2026-07-02** (Quark 4.1-481 +
  Zeta 1.1-40, NeoForge 1.21.1); jar list, module curation, smoke pass, and follow-ons (Quark Engineering ← #34,
  Farmer's Cutting: Quark ← #16) in [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md). *(QUARKPLAN · small)*
- **#16** Farmer's Delight — wild crops on biome islands. *(CONTENTPLAN · medium)*
- **#34** Immersive Engineering — the tech backbone (bauxite/aluminum island + FE), **gated on #35** the Excavator fix (island-aware ore mix preferred, else disable + hide in JEI). *(CONTENTPLAN · large + medium)*
- **#18** Applied Energistics 2 — certus/sky-stone bootstrap. *(CONTENTPLAN · medium)*
- Then their quest chapters: **#45** IE, **#41** AE2, **#42** FD, **#43** Quark. *(QUESTPLAN)*
- **#39** FE-flow proof rides the first consumer (IE or AE2) landing.

### Tier 3 — structures & trial-chamber polish

- **#26** aspen manor + **#27** bog trial — first decision: vehicle (adapt BWG pools vs author own set like the villages); each carries **#28** (vertical-jigsaw bounding-box mitigation + placement gametest) and **#29** (on-pad assembly check); **#30** release hygiene rides every step. *(STRUCTUREPLAN · medium each)*
- **#24** Trial Chamber palette + lighting pass (copper bulbs / grate, mud bricks, stairs-slabs-walls shapes) → **#25** atmosphere/greebling half (vaults already ✅) → **#33** more room/corridor variants → **#61** clean rebuild + vanilla comparison (mind the NBT staging trap). *(plannednotes · medium/medium/large/small)*

### Tier 4 — long tail / optional / future

- Flavor mods: **#31** Critters & Companions (small), **#32** Productive Bees (medium), **#36** Iron's Spells (large; decide scope **#37** first). *(CONTENTPLAN)*
- Optional visuals: **#21** Distant Horizons (unblocked), **#53** Vanilla Tweaks revival, **#54** standalone resource pack. *(BEAUTIFYPLAN)*
- Standing rules (work only when triggered): **#55** shaderPack pin refresh on Complementary/Euphoria updates; **#30** per-structure-step hygiene; **#38** per-future-mod ore-island-vs-MA call.
- Refactor tail: **#59** further version nodes (discretionary — the recipe is ready), **#56** route gametest suites through compat (deprioritized), **#57**/**#58** contingencies (shared-suite fingerprint map / per-version data variant — build only when needed). *(REFACTORPLAN)*
- **#52** verify The Factory Must Grow + Extended Cogwheels for 1.21.1, or drop them. *(CONTENTPLAN · small)*
- **#47** clarify the "BYG content" future-chapter scope (BWG's branch already shipped — deeper BWG coverage, or the separate BYG mod?). *(QUESTPLAN · decision)*
- **#49** prairie houses / rugged fossil, **#60** dedicated structure seeds (fallback), **#68** net-new bespoke structures (Create sheds, abandoned Inferium farmlands, … — future want). *(STRUCTUREPLAN)*
- **#50** optional dedicated Prosperity island. *(MYSTICALPLAN · medium)*
- **#70** waystone drop-compat idea (decision + build, unscoped). *(plannednotes)*

---

## 🎮 In-game verification checklist (for you to test)

Items that need a running client/server — one list to work through. Several could surface a real bug, so
they're worth doing before the next content lands on top.

**Shipped-feature sign-offs (all need BWG installed) — ✅ largely cleared 2026-07-02:**

- [x] **(#3)** The **BWG quest branch** (Tools & Travel) — ✅ loads and works as intended (tasks/rewards resolve).
- [x] **(#10)** The **"Exotic Woods" guide entry** — ✅ hidden without BWG, appears (Patchouli AND Modonomicon) once installed; and the stale "Eleven wood families" flavour text was corrected to **twenty** (v0.184.0).
- [ ] **(#14 Phase 5a)** **Village-biome reachability** — PARTIAL: ✅ `red_rock_valley` (the default village seed grew a red-rock village) + `cypress_swamplands`/`cypress_wetlands` confirmed; still to spot-check **`pumpkin_valley`** and **`weeping_witch_forest`** (re-key to a reachable sibling if either never places, cf. #66).
- [x] **(#14 Phase 5b)** **Throw-a-seed per village style** — ✅ villages assemble with the right BWG blocks (property-serialisation proven). Two findings spun off: **#72** (doors face inward) and **#73** (cypress → stilted overwater bayou).
- [x] **(#66)** **Spirit re-test** — ✅ a Forest seed grew spirit trees; band confirmed working (the earlier "no spirit" was the mislabelled-biome test).

- [ ] **(#64/#65)** **Wet-wood re-throw sign-off** (fixes SHIPPED v0.186.0; needs BWG installed) — re-throw the wet-wood seeds (cypress/bayou/white-mangrove/palm) at all three tiers and confirm: the water now reads as a **broad shallow marsh** (not a deep round pond), **Small tiers grow ≥1 tree**, and **Huge Bayou grows willows**. If Huge Bayou willows are still 0 even with the clearing, the willow feature likely can't place on the Aquatic pad at all → fall back to BWGPLAN lever (c) (smaller willow variant / water-adjacent mud).

**Balance & polish — observe during a normal playthrough:**

- [ ] **(#71)** Quark island extras (needs Quark installed): a Forest seed over savanna/plains/swamp/snowy/badlands sometimes grows a **blossom grove** (saplings obtainable); Rocky/Ancient islands vein Quark stones + a deep corundum geode, End form veins myalite; **Ancient Tomes** appear in the dungeon/mansion/bastion/ancient-city/trial/fortress island chests. Tune weights after the read.
- [ ] **(#51)** MA balance — watch Growth Accelerator stacking + mob Inferium drop rates; tune configs only if needed.
- [ ] **(#61)** Trial Chamber — after the #24/#25/#33 polish lands: clean rebuild + visual comparison to a vanilla trial chamber (stale-NBT Stonecutter trap).

**Blocked until the mod lands:**

- [ ] **(#39)** FE flows Create → IE/AE2 across islands (needs the first FE consumer installed).

---

## Full ranked backlog (open items only)

| # | Item | Plan | Priority | Effort | Status |
|---|---|---|---|---|---|
| 14 | BWG villages Phase 5 — reachability ✅ (red_rock_valley + cypress confirmed; spot-check pumpkin_valley/weeping_witch_forest) + per-style assembly ✅; two follow-ups spun off (#72, #73), then retire the plan | BWGVILLAGEPLAN | high | small | mostly done |
| 72 | Village doors face inward — flip z=0-wall front-door FACING (likely NORTH→SOUTH) so it's flush outside, then regen. Confirmed in BWG villages (`BwgVillageTemplates.door()`/porch); the SAME `FACING=NORTH` pattern is in TradePost/VillageCenter/Hamlet/RareStructure templates → verify + fix in one pass (not a blind global replace — some doors may be correctly oriented) | BWGVILLAGEPLAN | high | small | open |
| 73 | Cypress swampland villages → single swamp-water island with stilts + wooden bridges (all 3 tiers; de-cluster village_center) | BWGSWAMPVILLAGEPLAN | medium | large | open (planned) |
| 64 | Wet-wood water feature reworked to a broad shallow swamp/marsh (`depth:2`/`slope`/`extent` 0.6–0.68) ×3 Aquatic tiers — SHIPPED v0.186.0, gametest-guarded | BWGPLAN | medium | medium | shipped (sign-off) |
| 65 | Wet-wood zero-tree floor: `forceOneTree` grades a real 5×5 clearing so big NBT trees can't fail to zero; base `tries` 4→6 — SHIPPED v0.186.0 (Huge-Bayou-willow floor still wants in-game confirm) | BWGPLAN | medium | small | shipped (sign-off) |
| 15 | Quark + Zeta: 4 jars in + modules curated + void-death Totem shrine shipped (v0.182.0) | QUARKPLAN | medium | small | in progress (smoke-pass sign-off) |
| 71 | Quark island integrations (extras only — NO new seeds/tiers): stones+corundum (Rocky/Ancient) + myalite (End) + blossoms (all 3 Forest tiers) + Ancient-Tome loot — ✅ ALL PHASES SHIPPED v0.184–0.185, both nodes green; only the in-game look/loot sign-off remains | QUARKISLANDPLAN | medium | medium | shipped (sign-off) |
| 16 | Farmer's Delight — wild crops on biome islands | CONTENTPLAN | medium | medium | open |
| 34 | Immersive Engineering — bauxite/aluminum island + FE (the tech backbone) | CONTENTPLAN | medium | large | open (gated on 35) |
| 35 | IE Excavator fix — island-aware ore mix (preferred) else disable + hide in JEI | CONTENTPLAN | medium | medium | open |
| 18 | AE2 — certus + sky-stone bootstrap | CONTENTPLAN | medium | medium | open |
| 19 | Rolling: quest chapter per newly-landed mod | QUESTPLAN / CONTENTPLAN | medium | rolling | partial |
| 20 | Rolling: gated island tier per newly-landed mod | CONTENTPLAN | medium | rolling | partial |
| 24 | Trial Chamber palette + lighting pass (7 NBTs) | plannednotes | medium | medium | open |
| 25 | Trial Chamber atmosphere/greebling (vault half ✅ done) | plannednotes | low | medium | partial |
| 33 | Trial Chamber more room/corridor variants | plannednotes | low | large | open |
| 61 | Trial Chamber clean rebuild + vanilla comparison | plannednotes | low | small | gated on 24/25/33 |
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
| 39 | Prove FE flows Create → IE/AE2 across islands | CONTENTPLAN | low | small | blocked (needs a consumer) |
| 41 | Future chapter: AE2 (quest) | QUESTPLAN | low | unknown | gated on 18 |
| 42 | Future chapter: Farmer's Delight (quest) | QUESTPLAN | low | unknown | gated on 16 |
| 43 | Future chapter: Quark (quest — minimal 3-quest sketch in QUARKPLAN) | QUESTPLAN | low | unknown | gated on 15 |
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
