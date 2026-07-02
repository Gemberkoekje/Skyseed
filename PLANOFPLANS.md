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
  × three tiers, v0.170.0–v0.181.0) — only its in-game sign-offs (#3, #10, #14 Phase 5, #66) remain.
- Best value first: the **Tier 1 sign-offs** (they de-risk everything shipped) and the two **wet-wood quality
  fixes** (#64/#65) that in-game testing already flagged.

## Decisions log (standing decisions that govern the open work)

- **Q2 (BWG bands): DISTRIBUTE** across typed seed families, priority-ordered per seed — same biome may appear
  in several families with different emphasis. File convention in [BWGPLAN § Q2](Modpack-growyourownworld/BWGPLAN.md).
- **Tech backbone: Immersive Engineering; Mekanism DROPPED** (aesthetic call). Cascades: #34 promoted, the
  Excavator fix #35 required, Mekanism integration + quest chapter dead. [CONTENTPLAN §7](Modpack-growyourownworld/CONTENTPLAN.md).
- **Structure scope: FULL** — all 6 villages (✅ since shipped) + aspen manor + bog trial; prairie/fossil stay
  optional. [STRUCTUREPLAN § Scope decision](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **Village vehicle: our OWN jigsaw set in BWG palettes** (hermetic string-id `.nbt` engine — no BWG on any
  classpath). The manor/trial must make the same route choice (adapt BWG pools vs author own). [STRUCTUREPLAN § Mechanism](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **BWG dedicated-seed bar (Q3):** only non-growable AND farm-worthy content earns a dedicated seed — nothing
  currently qualifies. [BWGPLAN § Q3](Modpack-growyourownworld/BWGPLAN.md).
- **fir is the documented non-growable BWG plank** (no configured tree feature in 2.6.0) → 24/25 planks
  island-obtainable, gametest-guarded.
- **#66 (spirit band "failure") is a diagnosed test mislabel**, not a defect — re-test only, no code change.
- **FTB Quests tag-tasks trap:** smart-filter item tasks don't expand tags — use an advancement task
  ([QUESTPLAN § Approach](Modpack-growyourownworld/QUESTPLAN.md)).

## Snapshot by plan

| Plan | What it covers | Open items |
|---|---|---|
| [BWGPLAN.md](Modpack-growyourownworld/BWGPLAN.md) | BWG woods/flowers integration | #64 #65 #66 #22 #23 + sign-offs #3 #10 |
| [BWGVILLAGEPLAN.md](Modpack-growyourownworld/BWGVILLAGEPLAN.md) | BWG villages (shipped) | #14 Phase 5 (in-game sign-off) only |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration | #34 #35 #18 #15 #16 #31 #32 #36 #37 #38 #39 #52 + rolling #19 #20 |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | BWG structures long tail | #26 #27 #28 #29 #30 #49 #60 #68 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | #3 + future chapters #41–#47 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture (shipped) | #50 #51 #69 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visuals (shipped) | #21 #55 (+ optional revivals #53 #54) |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version build (shipped) | #56 #59 (+ contingencies #57 #58) |
| [plannednotes.md](plannednotes.md) | Trial Chamber polish, misc | #24 #25 #33 #61 #70 |
| Engineering debt (ex-CODE_REVIEW) | crash-robustness follow-ups | 5.2, 5.3, #67 |

---

## Priority tiers

### Tier 1 — sign-offs & small fixes on shipped content *(do these first)*

- **#3 / #10 / #14-Phase-5 / #66** — the four in-game sign-offs (see the [checklist](#-in-game-verification-checklist-for-you-to-test) below). Cheap, and any of them could surface a real bug before more content lands on top.
- **#64** — rework the wet-wood water feature (deep pond → broad shallow swamp/marsh, ×3 Aquatic tier files). *(BWGPLAN · medium)*
- **#65** — fix the wet-wood zero-tree floor (guarantee ≥1 tree on Small; fix Huge Bayou 0-willow; root cause = silent `feature.place` failure, levers documented in BWGPLAN). *(BWGPLAN · small-medium)*
- **#69** — refresh quest **B602 "Prosperity Found"** text (still says MA ores "come from one place"/Ancient; v0.172.0 added the accessible Lush stone source). *(MYSTICALPLAN · tiny)*
- **#22** — lift the held wet/semi forest biome densities (small numeric `tries` edits ×3 tiers). *(BWGPLAN · small)*
- **#23** — BWG config coherence pass + `mods.txt` regen (bundled skyseed jar is 0.174.0 vs mod 0.181.0). *(BWGPLAN · small)*

### Tier 2 — the content-mod wave *(in ROI order; each is followed by its quest chapter #19 and gated tier #20)*

- **#15** Quark + Zeta — cheap building/QoL breadth. *(CONTENTPLAN · small)*
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

**Shipped-feature sign-offs (do soon; all need BWG installed):**

- [ ] **(#3)** The **BWG quest branch** (Tools & Travel) loads in the quest book; B701's advancement task auto-completes on obtaining an exotic plank; B703's Skyfarer's Cache reward rolls.
- [ ] **(#10)** The **"Exotic Woods" guide entry** is hidden with BWG absent, and appears (Patchouli book AND Modonomicon Almanac) once BWG is installed and a BWG plank is obtained. *(While there: its flavour text still says "Eleven wood families" — now 19; fix during the check.)*
- [ ] **(#14 Phase 5a)** **Village-biome reachability:** confirm `pumpkin_valley`, `red_rock_valley`, `weeping_witch_forest`, `cypress_wetlands` actually occur in the void overworld (F3/Debug tab). If one never places, re-key that style's bands (cf. #66 precedent).
- [ ] **(#14 Phase 5b)** **Throw-a-seed per village style** (all 6): real BWG blocks render, roofs/fences/doors correct (this finally proves the BWG property-serialisation assumption); judge whether the **swamp** style needs its deferred water channels/stilts.
- [ ] **(#66)** **Spirit re-test:** stand on a **confirmed** `biomeswevegone:pale_bog` (F3 first!) and throw a Forest seed — spirit trees must grow (a matched band can only emit `spirit_trees`). Also confirm `pale_bog` is reachable at all; if not, spirit needs re-keying.

**After the #64/#65 fixes ship:** re-throw the wet-wood seeds (cypress/bayou/white-mangrove/palm) — water reads as swamp/marsh, Small tiers grow ≥1 tree, Huge Bayou grows willows.

**Balance & polish — observe during a normal playthrough:**

- [ ] **(#51)** MA balance — watch Growth Accelerator stacking + mob Inferium drop rates; tune configs only if needed.
- [ ] **(#61)** Trial Chamber — after the #24/#25/#33 polish lands: clean rebuild + visual comparison to a vanilla trial chamber (stale-NBT Stonecutter trap).

**Blocked until the mod lands:**

- [ ] **(#39)** FE flows Create → IE/AE2 across islands (needs the first FE consumer installed).

---

## Full ranked backlog (open items only)

| # | Item | Plan | Priority | Effort | Status |
|---|---|---|---|---|---|
| 3 | In-game quest-book test-load of the BWG branch (B701–B703) | BWGPLAN / QUESTPLAN | high | small | sign-off |
| 10 | In-game "Exotic Woods" guide reveal check (+ fix the stale "Eleven wood families" entry text) | BWGPLAN | high | small | sign-off |
| 14 | BWG villages Phase 5 — biome reachability (4 biomes) + per-style assembly sign-off + swamp-water read | BWGVILLAGEPLAN | high | small | sign-off |
| 66 | Spirit band re-test over a confirmed `pale_bog` (+ reachability) — diagnosed mislabel, no code change expected | BWGPLAN | high | small | sign-off |
| 64 | Rework the wet-wood water feature: broad shallow swamp/marsh instead of the deep round pond (×3 Aquatic tiers) | BWGPLAN | medium | medium | open |
| 65 | Wet-wood tree density: guarantee ≥1 tree on Small tier; fix Huge Bayou 0 willows (placement-failure root cause; levers a/b/c in BWGPLAN) | BWGPLAN | medium | small | open |
| 69 | Refresh quest B602 "Prosperity Found" text/gating for the Lush stone-ore source (v0.172.0) | MYSTICALPLAN | medium | small | open |
| 22 | Density follow-up: lift held wet/semi forest biomes to the agreed level (`tries` edits ×3 tiers) | BWGPLAN | low | small | open |
| 23 | BWG config coherence pass + `mods.txt` regen (bundled skyseed jar stale at 0.174.0) | BWGPLAN | low | small | partial |
| 15 | Curate Quark modules + add Zeta | CONTENTPLAN | medium | small | open |
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
| 43 | Future chapter: Quark (quest) | QUESTPLAN | low | unknown | gated on 15 |
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
