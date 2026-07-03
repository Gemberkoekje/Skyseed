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
| [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) | Quark integration (child of CONTENTPLAN) | #43 quest sketch (+ partner-gated add-ons) |
| [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md) | Quark × island integration (child of QUARKPLAN) | #71 (Y-band fix shipped v0.192.0 — in-game re-verify) |
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

- **#16** Farmer's Delight — wild crops on biome islands. *(CONTENTPLAN · medium)*
- **#34** Immersive Engineering — the tech backbone (bauxite/aluminum island + FE), **gated on #35** the Excavator
  fix (island-aware ore mix preferred, else disable + hide in JEI). *(CONTENTPLAN · large + medium)*
- **#18** Applied Energistics 2 — certus/sky-stone bootstrap. *(CONTENTPLAN · medium)*
- Then their quest chapters: **#45** IE, **#41** AE2, **#42** FD, **#43** Quark. *(QUESTPLAN)*
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

- **#39** Prove FE flows Create → IE/AE2 across islands — **blocked**: needs the first FE **consumer** (IE #34 or
  AE2 #18) installed. Promote to a Tier-1 sign-off the moment either lands. *(CONTENTPLAN)*

---

## Full ranked backlog (open items only)

| # | Item | Plan | Priority | Effort | Status |
|---|---|---|---|---|---|
| 71 | Quark island stones — Y-band bug **fixed v0.192.0** (veins now merge into every overworld Y-band across the 6 `quark_{rocky,ancient}{,_large,_huge}` files; ids verified; gametest-guarded). Remaining: in-game re-verify at low/mid/high throws + tune weights + blossom/Ancient-Tome loot sign-off | QUARKISLANDPLAN | medium | medium | fix shipped (in-game re-verify) |
| 16 | Farmer's Delight — wild crops on biome islands | CONTENTPLAN | medium | medium | open |
| 34 | Immersive Engineering — bauxite/aluminum island + FE (the tech backbone) | CONTENTPLAN | medium | large | open (gated on 35) |
| 35 | IE Excavator fix — island-aware ore mix (preferred) else disable + hide in JEI | CONTENTPLAN | medium | medium | open |
| 18 | AE2 — certus + sky-stone bootstrap | CONTENTPLAN | medium | medium | open |
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
| 39 | Prove FE flows Create → IE/AE2 across islands | CONTENTPLAN | low | small | not yet testable (needs a consumer) |
| 41 | Future chapter: AE2 (quest) | QUESTPLAN | low | unknown | gated on 18 |
| 42 | Future chapter: Farmer's Delight (quest) | QUESTPLAN | low | unknown | gated on 16 |
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
