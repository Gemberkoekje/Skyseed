# PLANOFPLANS — Skyseed prioritized backlog

**A single prioritized view across every planning doc in the repo — only what is LEFT.** Each item links back
to the plan that owns the detail. Item numbers are stable (they carry over from the previous backlog; closed
numbers are simply gone — the shipped history lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md` and git).

> **Cleaned 2026-07-03 at mod 0.206.0.** The whole **content-mod wave shipped end-to-end** since the last clean —
> Farmer's Delight (crops + quest), Immersive Engineering + Petroleum + flight (island + quest + configs), Applied
> Energistics 2 (certus/sky-stone bootstrap, meteorite island Phases 1–3, the tiered Meteorite-Core presses, quest
> chapter), and the Quark island tie-ins — all both-nodes-green. The fully-complete **`METEORPLAN.md` was retired**
> into the changelogs + git (all three meteor phases shipped; its only residual is the in-game throw-test, tracked
> under AE2 below). Shipped sections inside the still-open plans are trimmed to changelog pointers; what remains here
> is what's **LEFT** — almost entirely **in-game sign-offs** plus a tail of unbuilt/optional work.
> *(Earlier clean: 2026-07-02 at 0.191.0 retired the three BWG plans + the trial vanilla redesign; their still-governing
> decisions live in the Decisions log below.)*

## Headline

- **The content-mod wave is shipped end-to-end** (v0.196.0–v0.206.0, both nodes green): **Farmer's Delight** (#16 wild
  crops on biome islands + rice ponds + Nether/End delights, #42 quest A008), **Immersive Engineering + Petroleum +
  flight** (#34 ore island + crude-oil pocket, #35 Excavator config pinned, #45 quest A009, IE×FD compat), and **AE2**
  (#18 the whole certus/sky-stone bootstrap, the meteorite island Phases 1–3, the tiered **Meteorite-Core** presses,
  #41 quest B404–B419). What's left on all three is **in-game sign-off only** — throw-tests, tuning, pump/feature
  verifies — none of it runnable in the headless dev env.
- The earlier arcs remain done: the **BWG arc** (woods/flowers/planks/guide/quests + all six village styles × three
  tiers + #72/#73, plans retired) and the **Trial Chamber** (vanilla redesign #24/#25 + corridor-warren #61 + the
  framed-panel mosaic rollout, v0.189–v0.203). **Quark** shipped (#15 + island tie-ins #71).
- What's meaningfully **left** is three buckets: **(a) in-game sign-offs/re-verifies** on shipped content (#71 Quark
  stones at depth, #61 trial feel/fit, #39 FE power-chain, the IE/FD/AE2 throw-tests + quest-book loads); **(b) unbuilt
  content** — the aspen manor (#26) + bog trial (#27), the flavor mods (#31/#32/#36), the per-biome **Nether/End seed
  adaptation** (SKYNETHERENDBIOMEPLAN — plan-first, awaiting a design-fork sign-off), and the future quest chapters;
  **(c) a standing-rule / contingency / engineering-debt tail** (visuals, further version nodes, crash-robustness).
- Best value next: clear the **in-game sign-off queue** (Tier 1), then decide the SKYNETHERENDBIOMEPLAN design fork or
  pick up the manor/bog-trial (#26/#27).

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
  is shipped — the Y-band bug was **fixed v0.192.0**, in-game re-verify pending (Tier 1). Plans:
  [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) / [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md).
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
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration | flavor mods **#31 #32 #36 #37**, Create-addon check **#52**, standing call **#38** · rolling **#19 #20** · shipped, in-game-verify only: #16 #18 #34 #35 #39 |
| [IEPLAN.md](Modpack-growyourownworld/IEPLAN.md) | Immersive Engineering + flight + petroleum (child of CONTENTPLAN) | **all built** (#34 ore island + crude-oil pocket, #35 Excavator config pinned @0.7, #45 quest A009, IE×FD compat, Quark-Engineering jar in). **Left: in-game verifies only** — Excavator yields veins over void, oil pump-extractable, nether-cane feature grows, diesel aircraft consumes fuel · #39 FE sign-off |
| [AE2PLAN.md](Modpack-growyourownworld/AE2PLAN.md) | Applied Energistics 2 + the meteorite island (child of CONTENTPLAN; absorbed the retired METEORPLAN) | **all built & both-nodes-green** — certus deposit (#18d) + IE-gated seed (#18c) + meteorite island (overworld body + crater + sky-stone globe, Phases 1–3), the tiered **`skyseed:meteorite_core`** presses (#18b small→1/med→2-distinct/huge→4), the #41 quest (B404–B419, in-game-verified). **Left: in-game only** — #39 FE power-chain sign-off, meteor throw-test/tuning (sizes, crater palette) |
| [QUARKPLAN.md](Modpack-growyourownworld/QUARKPLAN.md) | Quark integration (child of CONTENTPLAN) | #43 quest sketch (+ partner add-ons: Farmer's Cutting: Quark still to verify) |
| [QUARKISLANDPLAN.md](Modpack-growyourownworld/QUARKISLANDPLAN.md) | Quark × island integration (child of QUARKPLAN) | #71 (Y-band fix shipped v0.192.0 — in-game re-verify + large-End myalite follow-up) |
| [FARMERSDELIGHTPLAN.md](Modpack-growyourownworld/FARMERSDELIGHTPLAN.md) | Farmer's Delight integration (child of CONTENTPLAN) | **all built** (#16 crops — dry + rice ponds + chorus + nether cane, 20 overrides + gametests; #42 quest A008). **Left: in-game only** — throw-test crops, nether-cane feature grows, quest-book load |
| [IRONSPELLSPLAN.md](Modpack-growyourownworld/IRONSPELLSPLAN.md) | Iron's Spells + Artifacts + Relics — the magic/exploration pillar (child of CONTENTPLAN; **#36/#37**) | **Largely built in-branch (unmerged; both nodes green):** the adaptive **Explore seed** (3 tiers) + loot layer (mod auto-inject + inert-safe `add_drop` GLMs) + biome-theme rares on every tier + magic-mob guardians + all Iron's own structures re-homed 1:1 (empty-target + `hasTemplatePool` inert-guard + **auto-centre**) + custom **Impaled Boat** + debug-seed coverage. **Left:** the 3 oversized-structure rebuilds (IRONSTRUCTUREREBUILDPLAN) + in-game fit tests + #46 quest |
| [IRONSTRUCTUREREBUILDPLAN.md](Modpack-growyourownworld/IRONSTRUCTUREREBUILDPLAN.md) | Island-friendly rebuilds of the oversized Iron's structures (child of IRONSPELLSPLAN; from the 2026-07-04 throw-test) | ✅ done: icebreaker→huge + custom boat; wizard→huge_rocky; mangrove→lush_large; guardians on 5 keepers; **auto-centre** for reused mod structures. 🔨 rebuilds (trial-chamber pattern, no-boxes + centred): **Ice Spider Den** (Frozen Warren), **Battleground** (War Barrow), **Citadel** (Mage's Sanctum — flagship) |
| [PORTALTWINPLAN.md](Modpack-growyourownworld/PORTALTWINPLAN.md) | Ruined-Portal twin alignment (**separate PR**; child of IRONSPELLSPLAN's germination path) | **option C** — derive the twin from the real portal block via vanilla linking + matched rotation, post-assembly; fallback B = center the frame + fix rotation. Cross-dim link is in-game-verify-only |
| [SKYNETHERENDBIOMEPLAN.md](SKYNETHERENDBIOMEPLAN.md) | Per-biome Nether/End seed adaptation | **entirely unbuilt — plan-first.** Gated on the §3 design-fork + §8 decisions sign-off; then ~36 data-only theme edits (5 Nether "biome kits" + a light End pass, base + `_large`) |
| [WILDSEEDPLAN.md](WILDSEEDPLAN.md) | New **Wild Skyseed** = adaptive default starter (biome-matching + ~5% builds); reuses the Explore engine | **plan-first, unbuilt; D1–D7 all signed off 2026-07-04 — ready to build.** Small code (ExploreThemes markers + a `forcesRare` gate) + recipes (Wild takes Forest's old planks+dirt; Forest→logs+dirt; nested Large/Huge, no-iron kelp token) + advancements + Patchouli/Modonomicon + FTB seed-swap (no new nodes) + self-drawn icons |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | structures long tail | #26 #27 #28 #29 #30 #49 #60 #68 |
| [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md) | Weighted 5%-per-seed surprise buildings — dilute the citadel (child of STRUCTUREPLAN; scopes **#68**) | **Phase 0 engine shipped** (opt-in weighted gate, both nodes green); left: 3 content batches (vanilla→1-mod→multi-mod) each migrating its themes + tuning; D1–D4 decided |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | future chapters **#43 #44 #46**, scope decision **#47** (#41/#42/#45 shipped) · rolling #19 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture (shipped) | #50 #51 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visuals (shipped) | #21 #55 (+ optional revivals #53 #54) |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version build (shipped) | #56 #59 (+ contingencies #57 #58) |
| [TRIALCHAMBERPLAN.md](TRIALCHAMBERPLAN.md) / [plannednotes.md](plannednotes.md) | Trial Chamber feel + misc | #33 #61 (mosaic rolled out; in-game tune/feel left) #70 |
| [CRASHRESUMEPLAN.md](CRASHRESUMEPLAN.md) + engineering debt | crash-robustness follow-ups | **all implemented in-branch 2026-07-05** (7 findings + #67 + 5.2 persist/resume + persistent 5.3, both nodes green); in-game hard-crash sign-off left |
| [ICONAUDITPLAN.md](ICONAUDITPLAN.md) | item-icon readability/consistency audit (93 textures) | **audit done 2026-07-05, art unbuilt.** Band A = 4 clear-cut redraws (farm-seed 4-way legibility, nether_lava_large colour, nether_forest/rocky twins, forest_large's missing dedicated art) — ready to build; Band B/C gated on §5 Q1/Q2 (structure-seed convention, Explore/Wild tier encoding) |

---

## Priority tiers

### Tier 1 — in-game sign-offs on shipped content *(the biggest bucket of "left")*

Everything here is **BUILT and both-nodes-green**; it needs a human throw-test in a real client (nothing below runs in
the headless dev env). Clearing this queue is the highest-value next work.

**✅ Cleared:** #3/#10/#66/#64/#65, **#14** (village biomes reachable), **#15** (Quark smoke pass), **#41** (AE2 quest
renders + deps resolve, 2026-07-04).

- [ ] **(#71)** **Quark island stones** — re-verify at low/mid/high throws (mine into the core): limestone/jasper
  (rocky), jasper/shale (ancient) + the odd deep corundum geode at every depth; then tune vein weights + sign off the
  blossom/Ancient-Tome loot. *(QUARKISLANDPLAN)*
- [ ] **(#16)** **Farmer's Delight** — throw Forest/Meadow/Desert/Aquatic/Lush, confirm crops appear + the harvest→
  replant loop + rice in ponds; confirm the nether powdery-cane **feature** grows a harvestable cane; quest-book load. *(FARMERSDELIGHTPLAN)*
- [ ] **(#34/#35)** **Immersive Engineering** — Excavator yields veins over the void (tune `chance`); the crude-oil
  source survives grow-in + is IE-Fluid-Pump-extractable; a diesel-fuelled aircraft consumes fuel; IE quest load. *(IEPLAN)*
- [ ] **(#18/#39)** **AE2 / meteorite** — throw-test the meteor island per tier (crater/globe/core read right; tune
  sizes + crater palette); **#39** the FE power-chain (generator island → Flux → IE/AE2 Energy Acceptor → a Controller powers up). *(AE2PLAN)*
- [ ] **(#61)** **Trial Chamber** feel/fit — the framed-panel mosaic rolled out to every piece (v0.203); the in-game
  vanilla compare + tune is the sign-off (details in Tier 3). *(TRIALCHAMBERPLAN)*

### Tier 2 — the content-mod wave — ✅ SHIPPED

The FD / IE / AE2 / Quark wave is **built end-to-end** (v0.196–v0.206, both nodes green), each with its island
integration **and** quest chapter. Its only remaining work is the **in-game verifies in Tier 1**. The next *unbuilt*
integrations — the flavor mods (**#31** Critters, **#32** Productive Bees) — are lower-ROI and live in **Tier 4**.
**Iron's Spells (#36/#37)** is further along: **largely built in-branch** (unmerged) — only the 3 structure rebuilds +
in-game fit tests + #46 quest are left (IRONSPELLSPLAN / IRONSTRUCTUREREBUILDPLAN). The per-biome **Nether/End seed
adaptation** (SKYNETHERENDBIOMEPLAN) is the other
sizeable unbuilt content block — plan-first, awaiting its design-fork sign-off (Tier 3).

### Tier 3 — structures, trial-chamber polish & the Nether/End biome pass

- **(new content) Per-biome Nether/End seed adaptation** — SKYNETHERENDBIOMEPLAN, **entirely unbuilt, plan-first.**
  First step is the user sign-off on the §3 design fork (A: seed-identity + biome-flavor [recommended] vs B) and the
  §8 decisions (End scope, Forest normalization, nether trees); then ~36 data-only theme edits (5 Nether "biome kits"
  + a light End pass, base + `_large`). No code changes expected. *(SKYNETHERENDBIOMEPLAN)*
- **#61** Trial Chamber — structure (corridor warren, v0.194) **and** the framed-panel wall MOSAIC + laid-floor tiler
  + corner posts **rolled out to every piece** (v0.196–v0.203, both nodes green). **In-game feel/fit is the sign-off**
  (does it wind/branch + read as a vanilla trial chamber); then tune module size / motif frequency / patina mix / hall
  weights. *(TRIALCHAMBERPLAN)*
- **#33** Trial Chamber more variants — partly done by the warren (corner/junction/cell/descent); further options:
  cross-intersections, alcove-corridors, bigger multi-cell chambers, vaulted ceilings. *(plannednotes)*
- **#26** aspen manor + **#27** bog trial — first decision: vehicle (adapt pools vs author own set like the
  villages); each carries **#28** (vertical-jigsaw bounding-box mitigation + placement gametest) and **#29** (on-pad
  assembly check); **#30** release hygiene rides every step. *(STRUCTUREPLAN · medium each)*

### Tier 4 — long tail / optional / future

- Flavor mods: **#31** Critters & Companions (small), **#32** Productive Bees (medium). **#36** Iron's Spells is
  **largely built in-branch** (Explore seed + loot + re-homed structures + guardians + auto-centre; scope **#37** ✅
  decided) — left: the 3 oversized-structure rebuilds (IRONSTRUCTUREREBUILDPLAN) + in-game fit tests + #46 quest.
  *(CONTENTPLAN / IRONSPELLSPLAN)*
- Optional visuals: **#21** Distant Horizons (unblocked), **#53** Vanilla Tweaks revival, **#54** standalone resource
  pack. *(BEAUTIFYPLAN)*
- Item-icon audit follow-through: **Band A** (4 redraws — farm-seed legibility, nether_lava_large colour,
  nether_forest/rocky twins, forest_large dedicated art) is a ready-to-build readability bugfix; **Band B/C** await the
  §5 design calls (structure-seed convention, Explore/Wild tier encoding). *(ICONAUDITPLAN)*
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

## Full ranked backlog (open items only)

| # | Item | Plan | Priority | Effort | Status |
|---|---|---|---|---|---|
| 71 | Quark island stones — Y-band bug **fixed v0.192.0** (veins now merge into every overworld Y-band across the 6 `quark_{rocky,ancient}{,_large,_huge}` files; ids verified; gametest-guarded). Remaining: in-game re-verify at low/mid/high throws + tune weights + blossom/Ancient-Tome loot sign-off | QUARKISLANDPLAN | medium | medium | fix shipped (in-game re-verify) |
| 16 | Farmer's Delight — **crops SHIPPED** (dry crops on Forest/Meadow/Desert + rice on Lush/Aquatic ponds + chorus succulent + nether powdery-cane feature; 20 `theme_override` files, gametest-guarded, both nodes green). Remaining: in-game throw-test + nether-cane feature grows + quest-book load | FARMERSDELIGHTPLAN | medium | medium | built; in-game verify |
| 34 | Immersive Engineering — **ore island SHIPPED** (6 `immersiveengineering_*` overrides: aluminum/lead/nickel/silver/uranium + a deep crude-oil pocket, inert-safe, gametest-guarded). Remaining: in-game verify all metals appear at low/mid/high + oil is pump-extractable | CONTENTPLAN / IEPLAN | medium | large | built; in-game verify |
| 35 | IE Excavator — **config pinned** (`overrides/defaultconfigs/immersiveengineering-server.toml`, `chance` 0.9→0.7). Remaining: in-game confirm it yields veins over the void, then fine-tune | CONTENTPLAN / IEPLAN | medium | medium | built; in-game verify |
| 18 | AE2 — **integration SHIPPED end-to-end (both nodes green):** certus deposit (#18d) + IE-gated seed (#18c) + meteorite island Phases 1–3 (overworld body + crater + sky-stone globe) + the tiered `skyseed:meteorite_core` presses (#18b) + #41 quest. Remaining: #39 FE sign-off + meteor throw-test/tuning | AE2PLAN | medium | medium | built; in-game verify |
| 19 | Rolling: quest chapter per newly-landed mod — kept pace (FD/IE/AE2 all authored); re-arms per future integration | QUESTPLAN / CONTENTPLAN | medium | rolling | caught up |
| 20 | Rolling: gated island tier per newly-landed mod — done for every installed mod; re-arms per future integration | CONTENTPLAN | medium | rolling | caught up |
| 61 | Trial Chamber corridor-warren (v0.194) **+ the framed-panel wall MOSAIC + laid-floor tiler rolled out to every piece** (v0.196–v0.203, both nodes green). Remaining: in-game vanilla compare + tune module/motif/patina/hall weights | TRIALCHAMBERPLAN / plannednotes | medium | medium | built; in-game feel/fit |
| 33 | Trial Chamber more variants — partly done by the warren (corner/junction/cell); further: cross-intersections, alcove-corridors, multi-cell/vaulted chambers | plannednotes | low | large | open |
| 26 | Resurrect aspen manor (vehicle decision first) | STRUCTUREPLAN | low | medium | open |
| 27 | Resurrect bog trial (vehicle decision first) | STRUCTUREPLAN | low | medium | open |
| 28 | Vertical-jigsaw bounding-box mitigation + placement gametest (rides 26/27) | STRUCTUREPLAN | low | medium | rider |
| 29 | On-pad assembly verification (rides 26/27; done for villages) | STRUCTUREPLAN | low | medium | rider |
| 30 | Per-structure-step release hygiene (standing rule) | STRUCTUREPLAN | low | small | standing rule |
| 31 | Critters and Companions — spawn verification on biome islands | CONTENTPLAN | low | small | open |
| 32 | Productive Bees — starter bees/hives | CONTENTPLAN | low | medium | open |
| 36 | Iron's Spells + Artifacts + Relics — the magic/exploration pillar. **Largely BUILT in-branch (unmerged; both nodes green):** Explore seed (3 tiers, full onboarding) + loot layer (mod auto-inject + inert-safe `add_drop` GLMs) + biome-theme rares every tier + magic-mob guardians + all Iron's own structures re-homed 1:1 (empty-target + `hasTemplatePool` guard + auto-centre) + custom **Impaled Boat** + debug-seed coverage. **Left:** the 3 oversized-structure rebuilds (Citadel/Battleground/Ice Spider Den — IRONSTRUCTUREREBUILDPLAN) + in-game fit throw-tests + #46 quest | CONTENTPLAN / IRONSPELLSPLAN | low | large | built in-branch; rebuilds + in-game left |
| 37 | Iron's Spells scope — **DECIDED: the full exploration loop** (throw seed → grow island → rare building → special loot; mod worldgen inert, re-homed via themes + loot GLMs) | CONTENTPLAN / IRONSPELLSPLAN | low | small | ✅ decided |
| 38 | Per-future-mod call: bespoke ore island vs MA seeds | CONTENTPLAN | low | small | standing rule |
| 39 | Prove FE flows Create → IE/AE2 across islands — **proven on paper** (C&A `alternator` → Flux `plug`/`point` → IE native / AE2 `energy_acceptor`, all standard NeoForge FE; see AE2PLAN #39). Left: one-time in-game sign-off | CONTENTPLAN / AE2PLAN | low | small | proven; in-game sign-off pending |
| 41 | AE2 quest chapter — **SHIPPED + in-game-verified 2026-07-04** (16 quests `B404`–`B419` in the **Storage** chapter; renders + deps resolve; user repositioned nodes so lines don't cross — do not revert) | QUESTPLAN / AE2PLAN | low | small | ✅ shipped + verified |
| 42 | Farmer's Delight quest chapter — **SHIPPED** (`chapters/farmersdelight.snbt`, A008, B801–B808 + lang). In-game quest-book load pending | QUESTPLAN | low | small | shipped (in-game load pending) |
| 43 | Future chapter: Quark (quest — minimal 3-quest sketch in QUARKPLAN) | QUESTPLAN | low | unknown | unblocked (#15 ✅) — build last |
| 44 | Future chapter: Productive Bees (quest) | QUESTPLAN | low | unknown | gated on 32 |
| 45 | Immersive Engineering quest chapter — **SHIPPED** (`chapters/immersiveengineering.snbt`, A009, 15 quests B9xx + the flight line moved to Tools). In-game quest-book load pending | QUESTPLAN / IEPLAN | medium | small | shipped (in-game load pending) |
| 46 | Iron's Spells quest chapter — **Magic & Exploration** — ✅ **SHIPPED** (`chapters/ironsspells.snbt`, A00A, 11 quests BB01–BB0B, new E004 sidebar group; Explore-seed tiers → scroll/essence loot → spellbook/scroll-forge/arcane-anvil → Upgrade Orb; Relic + Artifact checkmark side-finds; gated off the Skyseed structure-seed quest B108). In-game book-load pending | QUESTPLAN / IRONSPELLSPLAN | low | small | shipped (in-game load pending) |
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
| 68 | Net-new bespoke structures beyond BWG's 17 — **now scoped as [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)**: a weighted **5%-per-seed** surprise-building budget (D1 single-gate + weighted pick; D2 balanced 50/35/15 mix; D3 Explore-seed reward floor — the premium seed never forces a loot-less build; D4 mod builds stay derelict — loot never leaks a gate-key like AE2 sky stone/presses; all signed off 2026-07-04) to dilute the citadel/flagships. **Phase 0 engine SHIPPED** (2026-07-04, both nodes green — 175/177): `rollRare` weighted-gate + `weight`/`requires`/`explorable` fields + `Lookup.modLoaded` + Explore reward-floor filter, landed as a **backward-compatible opt-in** (`rare_structure_chance` present → new model, absent → legacy byte-identical) so no churn. Left: 3 content batches (vanilla common → single-mod rare → multi-mod epic), each migrating its themes to the new model + tuning pass; **open: the large/huge rate** (flat 5% vs keep bigger tiers hotter) | STRUCTUREPLAN / VARIETYSTRUCTUREPLAN | low | large | engine shipped; content next |
| 70 | Waystone drop-compat idea (decide + build) | plannednotes | low | unknown | idea |
| 21 | Distant Horizons (LOD) — optional, unblocked | BEAUTIFYPLAN | low | medium | open |

---

## Engineering debt (ex-CODE_REVIEW.md)

> **Update 2026-07-05 — the whole crash-robustness backlog is now implemented in-branch, both nodes green.** The
> **7 confirmed 2026-07-04 findings** (both `MobPlanner` inert-safety fixes, the `Traps` opt-in `traps` gate,
> `findClearSpot` per-candidate re-validation, the seed-derived `StartIsland` oak, the double dimension-reset backup
> guard, and the concurrent force-load un-force → an in-memory **ref-count**), **#67** (drain-cap warning + force-load
> logging), **and** the persistent parts — **5.2** (persist/resume in-progress grows) + persistent **5.3** (un-force
> stale forced chunks on restart), via a dual-version `SkyseedWorldData` schema + a `CrashRecovery` `ServerStarted`
> handler — are all in the working tree, both nodes green (**1.21.1 202/202, 26.1.2 204/204**, incl. a schema
> round-trip + resume gametest), pending commit. **Left:** the in-game hard-crash sign-off (kill mid-grow → restart
> finishes it, `/forceload query` clean; repeat with a Nether twin). Full design + test plan in
> **[CRASHRESUMEPLAN.md](CRASHRESUMEPLAN.md)**.

All **21 code-review findings were fixed and merged via PR #15** (CI green both nodes; the 5.1/5.2/5.3 in-game
smoke tests passed 2026-07-01) — the review doc itself is retired. These are the deferred follow-ups it left,
each self-documented in the code:

| Item | Priority | Effort | What & why |
|---|---|---|---|
| ✅ **IMPLEMENTED in-branch (CRASHRESUMEPLAN) — 5.2, persist/resume of in-progress grows.** A `PendingIsland` descriptor (re-plan inputs + progress) is built at germination + for twins, persisted every tick, removed on completion; `CrashRecovery` re-plans + re-enqueues each at its saved progress on `ServerStarted`. Both nodes green. Left: in-game hard-crash sign-off. | medium | large | crash-robustness |
| ✅ **IMPLEMENTED in-branch (CRASHRESUMEPLAN) — 5.3, force-load ticket leak reconciliation.** `GenerationJob` records/removes forced chunks in `SkyseedWorldData` at the ref-count transitions; `CrashRecovery` un-forces every leftover on `ServerStarted`, before the resume. | medium | medium | crash-robustness |
| ✅ **IMPLEMENTED in-branch — #67, crash-fix observability.** A drain-cap warning in `IslandGrowth.onServerStopping` + per-region force-load acquire/release debug logging in `GenerationJob`. | low | small | makes 5.2/5.3 sign-offs checkable |

### 2026-07-04 code review (adversarially verified — ✅ implemented in-branch 2026-07-05, pending commit)

A fresh fan-out review (both nodes' shared code) surfaced 7 confirmed findings. **All 7 are now implemented in-branch
(2026-07-05, both nodes green — 1.21.1 200/200, 26.1.2 202/202), pending commit.** No committed golden-master broke:
the two `MobPlanner` fixes only move the robust `bad.json` fixture's RNG stream (its lone test asserts non-empty +
grass), and single-source keeps the two nodes in parity. The table below is retained as the record of what the
findings were.

| Item | Priority | Effort | What & why |
|---|---|---|---|
| **Concurrent force-load un-force** (`GenerationJob.java:198`, extends 5.3). `level.setChunkForced` is a plain boolean (not ref-counted). Two islands germinating at once whose bounding boxes share a chunk column (a cluster/twin/flush case) both force it; the first job to finish un-forces the shared chunk while the other is still draining — silently dropping that job's later structure/mob/snow content, especially for a **twin grown in a player-less dimension** (no other ticket masks it). Fix: ref-count forced regions (fold into the 5.3 `SkyseedWorldData` tracking). | medium | medium | resource-leak |
| **`MobPlanner.planMobs` inert-safety** (`MobPlanner.java:72`). Rolls the per-entry `chance` `nextFloat` *before* `resolveEntity` (the modded-id `Lookup` guard, line 75), so an absent modded mob still consumes a shared-stream roll → the ladder-shaft carve + every downstream RNG consumer shift solely because the mod is absent (breaks the inert-without-the-mod / determinism-parity invariant). Fix: resolve-then-skip before the roll, exactly like `OrePlanner`/`rollAnimals`. Latent today (no shipped theme lists a modded mob except the `bad.json` test fixture). | medium | small | inert-safety |
| **`MobPlanner.planPondMobs` inert-safety** (`MobPlanner.java:100`). Same defect for pond `water_mobs`: `chance` roll before `resolveEntity` (line 103). Same fix. | medium | small | inert-safety |
| **`Traps.applyAfterJigsaw` wool sentinels** (`Traps.java:26`). Uses vanilla `RED_WOOL`/`LIME_WOOL`/`YELLOW_WOOL` as un-namespaced global markers and runs on **every** jigsaw structure, so it clobbers any *decorative* red/lime/yellow wool a structure legitimately places near its origin. Fix: use a namespaced sentinel (or gate the trap-rewrite to the structures that opt in). | medium | medium | correctness |
| **`findClearSpot` skips re-validation** (`IslandSeedEntity.java:311`). Re-plans + grows the island at nudged positions without re-checking `IslandGenerator.formValidFor` / `theme.fizzlesIn`, so the dimension/biome validity gate only covers the *original* rest point — a nudge can land (and grow) an island where the theme should have fizzled. Fix: re-run the validity gate per candidate spot. **Behaviour change — decide intent first.** | medium | medium | correctness |
| **Start-island oak non-deterministic** (`StartIsland.java:94`). The curated oak is grown by the vanilla `ConfiguredFeature` on `level.getRandom()` (an unseeded per-JVM `RandomSource`), so the start tree isn't a function of the world seed — contradicts the file's "deliberately not procedural" contract. Harmless (an oak always yields wood; persisted once). Fix: drive it from a seed-derived `RandomSource`. | low | small | determinism |
| **Double dimension-reset clobbers backup** (`SkyseedCommands.java:252`). Arming both `/emptynether` + `/emptyend` in one session makes the second `applyReset` overwrite the recovery backup with an already-modified `level.dat`, so "restore original" no longer restores vanilla. Low reach (these legacy rescue commands are slated for pre-1.0 removal — README Roadmap). Fix: don't overwrite an existing backup. | low | small | correctness |
