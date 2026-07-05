# STRUCTURELONGTAILPLAN — Tier D: the structure long tail (#26 #27 #28 #29 #30 #33 #49 #60 #68)

> **Status: Phases B (manor #26 + bog trial #27, v0.226.0), C (trial variants #33, v0.227.0) and D (prairie house #49,
> v0.228.0) BUILT 2026-07-05, both nodes green (1.21.1 = 218, 26.1.2 = 220). Phase E (#60) is superseded (route 1
> shipped); Phase F (#68) is recorded-only. So the plan is effectively COMPLETE — the debug-seed spike cleanup is done;
> all remaining work is the in-game throw-tests (human/creative, not runnable headless).** This is the working
> plan for PLANOFPLANS **Tier D — structure long tail**: the aspen manor (**#26**) + bog trial (**#27**) with their
> riders (**#28** vertical-jigsaw mitigation, **#29** on-pad assembly, **#30** release hygiene), the **#33** trial-chamber
> variant tail, the optional **#49** prairie houses / rugged fossil polish, the recorded **#60** dedicated-seed fallback,
> and the **#68** net-new bespoke ambition. It is the active successor to the still-open tail of
> [`Modpack-growyourownworld/STRUCTUREPLAN.md`](Modpack-growyourownworld/STRUCTUREPLAN.md) and the
> [`TRIALCHAMBERPLAN.md`](TRIALCHAMBERPLAN.md) / [`plannednotes.md`](plannednotes.md) #33 note. Priorities in
> [`PLANOFPLANS.md`](PLANOFPLANS.md).

## Decisions taken (2026-07-05)

1. **Vehicle for #26/#27 — LOCKED to route 1 (adapt BWG's own pools).** The spike-first plan ran: three `debug_*`
   seeds assembled BWG's own `aspen_manor_1/2` + `bog_trial` pools on a huge island, and the throw-test verdict was
   **both fit as-is** — "the aspen manors look exactly as intended," the bog trial needs only a **1-block sink** tune.
   So **no authored Skyseed set (route 3) is needed** — we adapt the foreign pools directly. (Route 3 remains the
   recorded fallback had they read as boxes.)
2. **Delivery — a biome-keyed `theme_override` on the existing seed (no new seed items).** Confirmed against the
   shipped village precedent ([`biomeswevegone_hamlet.json`](src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_hamlet.json))
   and the resolver ([IslandGenerator.java:474](src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGenerator.java)):
   a `biome_overrides` entry whose `jigsaw` **replaces** the base seed's structure when the seed is grown over the host
   BWG biome. The biome gate itself guarantees inertness (no BWG → the host biome doesn't exist → the override never
   fires → the vanilla mansion/trial grows, byte-identical). **#60** (dedicated seeds) stays the recorded fallback.

   | Structure | Base seed (theme) | Host BWG biome | BWG start_pool | `sink` | `pad` |
   |---|---|---|---|---|---|
   | **Aspen manor** (#26) | `skyseed:woodland_mansion` | `biomeswevegone:aspen_boreal` | `aspen_manor_1` + `aspen_manor_2` (50/50) | 0 | 16 |
   | **Bog trial** (#27) | `skyseed:trial_chamber` | `biomeswevegone:pale_bog` | `bog_trial` | **1** | 14 |

   Island size: the manor (28×20, height 24) **fits the woodland-mansion seed's own island** (radius 20–23 → Ø 40–46,
   hosts a pad-16 build) — no huge tier needed, matching the throw-test note that it "might even fit on a large island."
3. **Scope — full: items 8 + 9, plus record #68.** #26/#27 (+#28/#29/#30), #33, #49, #60-as-fallback, and the #68
   net-new want captured at the end.
4. **#33 — all three new trial pieces**, under a hard **"no square boxes"** rule ([[skyseed-no-boxes]]):
   **cross-intersection**, **alcove-corridor spur**, **multi-cell vaulted chamber**.

## Grounding — every mechanism this needs already ships (why it's low-risk)

| Need | Already-shipping mechanism | Reference |
|---|---|---|
| Reference a **foreign mod pool** from a theme, inert without the mod | Iron's Spells re-homing: `"pool": "irons_spellbooks:…/start_pool", "target": "", "pad": …, "sink": …, "excavate": …`; unknown pool is skipped | [huge_desert.json:176](src/main/resources/data/skyseed/skyseed/theme/huge_desert.json), [lush_large.json:22](src/main/resources/data/skyseed/skyseed/theme/lush_large.json) |
| **Mod-gate** a chance-based bit so it's inert-safe / deterministic-parity | `Lookup.modLoaded` + the `requires` weighted-gate (`rollRare`) from the VARIETYSTRUCTUREPLAN #68 engine | [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md), PLANOFPLANS #68 |
| A **throwaway debug seed** for a jigsaw spike | `DEBUG_SEED_THEMES` hand-list → auto item in the "Skyseed Debug" creative tab; `debug_streets` is the exact precedent (SKYJIGSAWPLAN Phase 0) | [ModItems.java:84](src/main/java/dev/gemberkoekje/skyseed/registry/ModItems.java), [debug_streets.json](src/main/resources/data/skyseed/skyseed/theme/debug_streets.json) |
| A **weighted variant** of a seed that carries its own jigsaw + palette | `variants[]` with a per-variant `jigsaw` override — `dark_forest` on the mansion, `rocky` on the trial | [woodland_mansion.json:13](src/main/resources/data/skyseed/skyseed/theme/woodland_mansion.json), [trial_chamber.json:13](src/main/resources/data/skyseed/skyseed/theme/trial_chamber.json) |
| **Author our own** structure in a themed palette (route 3) | The hermetic string-id `.nbt` engine — `*Templates.generateInto` → `writeIfAbsent`, real mod ids substituted at write time; the villages/mansion/trial all took this | [WoodlandMansionTemplates.java](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/WoodlandMansionTemplates.java), [DevStructureGenerator.java:74](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/DevStructureGenerator.java) |
| **On-pad assembly** gametest of a jigsaw structure | The `big_region` 48×24×48 dirt pad + the villages' `bwg_*_assemble*` tests | [DevStructureGenerator.java:123](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/DevStructureGenerator.java) |

Standing discipline that governs every build step below: the **regen dance** + per-node JDK + the cross-version
`Blocks.*` gotcha ([[skyseed-structure-staging]]), the **no-boxes** rule ([[skyseed-no-boxes]]), the chest-openable
air-cell rule ([[skyseed-structure-chest-openable]]), and both-nodes-green before any commit.

---

## Phase A — the spike (the #26/#27 vehicle decision gate) — ✅ BUILT, awaiting throw-test

**Goal: one look each at BWG's aspen manor (both starts) + bog trial, grown on a Skyseed huge island, so the
route-1-vs-route-3 call is made by eye, not on paper.** Nothing here ships — all debug-tab, no recipe/tag/guide,
inert without BWG, and slated for deletion once the call is made.

**✅ Built (2026-07-05) — three throwable debug seeds in the "Skyseed Debug" creative tab:**

1. **Pool ids + footprints read from the BWG 2.6.0 jar** (`start_pool` field of each `worldgen/structure/*.json`; NBT
   `size` tag) — no guessing:

   | Debug seed | BWG `start_pool` | footprint (x·z) | height | pad used |
   |---|---|---|---|---|
   | `debug_aspen_manor_1` | `biomeswevegone:aspen_manor_1` | 28 × 20 | 24 | 16 |
   | `debug_aspen_manor_2` | `biomeswevegone:aspen_manor_2` | 28 × 19 | 24 | 16 |
   | `debug_bog_trial` | `biomeswevegone:bog_trial` | 24 × 23 | 19 | 14 |

   **Key finding — all three are SINGLE-piece jigsaws** (one `single_pool_element`, `size: 1`, no expansion, each its
   own BWG processor list). So the dreaded **#28 vertical-multi-piece bounding-box risk is much smaller than feared** —
   these are single ~26 KB surface `.nbt` placements, not sprawling recursive jigsaws.

2. **Three debug themes** ([debug_aspen_manor_1.json](src/main/resources/data/skyseed/skyseed/theme/debug_aspen_manor_1.json),
   `_2`, [debug_bog_trial.json](src/main/resources/data/skyseed/skyseed/theme/debug_bog_trial.json)): a huge island
   (`radius` 22–30, teardrop) with the foreign pool wired `"jigsaw": { "pool": "<bwg start_pool>", "target": "",
   "depth": 1, "pad": <16/14>, "sink": 0 }`. The **blank `target`** is load-bearing: `Jigsaw.placeCapped` reads it as
   "no start jigsaw" → reuses BWG's own start element (its pool has no `minecraft:bottom` anchor), **and** it triggers
   `recentreOnOrigin` so the corner-anchored BWG footprint is auto-centred on the island (the same fix the Iron's
   towers needed).
3. **Registered** in [`ModItems.DEBUG_SEED_THEMES`](src/main/java/dev/gemberkoekje/skyseed/registry/ModItems.java) +
   lang + item models (icons reuse the mansion / trial seed textures). Compiles green on the 1.21.1 node; all data
   parses.

**✅ Thrown + judged (2026-07-05):**
- **Aspen manor 1 & 2** — "look exactly as intended." Read as real manors, exterior + interior; seat cleanly on the
  pad at `sink 0`; no dirt scars from BWG's processors. Both designs kept (50/50). Note: they fit comfortably — "might
  even fit on a large island instead of a huge one" (confirms no huge tier needed; the woodland-mansion seed's own
  island hosts them).
- **Bog trial** — fits, but **needs to drop 1 block** → `sink 1`. Applied to the debug theme; baked into the Phase B spec.
- **Verdict: route 1 for both.** The `debug_*` seeds served their purpose and have been **deleted** now the real
  biome-overrides shipped (they were debug-tab throwaways, never shipped with a recipe/tag).

---

## Phase B — build the manor (#26) + bog trial (#27) — route 1 — ✅ BUILT (v0.226.0, both nodes green)

The vehicle is locked; this is the build. Both structures ship as a **biome-keyed `theme_override`** on the existing
seed (the `biomeswevegone_hamlet.json` village pattern) — no new seed items, no authored templates, no regen dance.

**✅ Shipped 2026-07-05 (both nodes green — 1.21.1 214, 26.1.2 216):**
- [`worldgen/template_pool/aspen_manor/start.json`](src/main/resources/data/skyseed/worldgen/template_pool/aspen_manor/start.json)
  — wrapper pool, both BWG manor designs 50/50, `processors: minecraft:empty` (keeps it loadable without BWG; the only
  loss is a cosmetic berry-bush swap).
- [`theme_override/biomeswevegone_woodland_mansion.json`](src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_woodland_mansion.json)
  — aspen_boreal band → `skyseed:aspen_manor/start`, aspen-tree decoration, inherits the evoker→Totem garrison.
- [`theme_override/biomeswevegone_trial_chamber.json`](src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_trial_chamber.json)
  — pale_bog band → `biomeswevegone:bog_trial` direct (keeps its aging processor), **`sink 1`**, boggy surface + mushrooms.
- Golden-master gametests in **both** suites (`aspenManorOverrideTargetsWoodlandMansion` /
  `bogTrialOverrideTargetsTrialChamber`) assert each band resolves onto its base seed with the right pool + sink.
- v0.226.0 bump + CHANGELOG (both files).
- **Left:** the in-game throw-test (below) + deleting the three `debug_*` spike seeds once you've signed off (kept for
  now so you can re-verify the bog `sink 1` + both manors on a clean flat island via the Skyseed Debug tab).

Spec, as built:

### B1 — the aspen manor (#26)
- **New wrapper pool** `skyseed:worldgen/template_pool/aspen_manor/start.json` listing **both** BWG designs as elements
  (`location: biomeswevegone:aspen_manor_1` + `_2`, weight 1 each, `processors: biomeswevegone:aspen_manor`,
  `projection: rigid`, `fallback: minecraft:empty`). Wrapping both lets one seed roll either design (the throw-test
  liked both); a blank `target` picks a random start element + auto-centres it. *(Fallback if the wrapper misbehaves:
  reference `biomeswevegone:aspen_manor_1` directly and drop design 2.)*
- **New override** `skyseed:theme_override/biomeswevegone_woodland_mansion.json`: `target: skyseed:woodland_mansion`,
  one `biome_overrides` entry `{ "biomes": ["biomeswevegone:aspen_boreal"], … }` with
  `"jigsaw": { "pool": "skyseed:aspen_manor/start", "target": "", "depth": 1, "pad": 16, "sink": 0 }`, an aspen surface
  palette (grass + podzol/coarse scatter), an aspen `decoration` (BWG `aspen_trees` feature + ferns), and the manor's
  `animals`/mob pack (keep BWG's baked 2–4 **vindicators**, or reuse the vanilla mansion's evoker→Totem garrison — a
  design call for the build; leaning **evoker garrison** so the aspen manor keeps the mansion seed's Totem reward).

### B2 — the bog trial (#27)
- **New override** `skyseed:theme_override/biomeswevegone_trial_chamber.json`: `target: skyseed:trial_chamber`, one
  `biome_overrides` entry `{ "biomes": ["biomeswevegone:pale_bog"], … }` with
  `"jigsaw": { "pool": "biomeswevegone:bog_trial", "target": "", "depth": 1, "pad": 14, "sink": 1 }` (the **`sink 1`**
  from the throw-test), a boggy surface palette (grass + mud/moss scatter) and pale-bog decoration. Single BWG design,
  so reference the BWG pool directly. The override **replaces** the deep-warren jigsaw only over `pale_bog`; the normal
  trial-chamber warren still grows everywhere else.

### Shared tail (both)
- **Inertness is free here** (simpler than the old variant plan feared): the override is gated on a **BWG biome** that
  only exists when BWG is loaded, so without BWG the entry never matches and the vanilla mansion/trial grows unchanged —
  byte-identical. No `requires:` mod-gate needed. Still add the **#30 golden-master gametest** asserting the base
  mansion/trial seed is byte-identical without BWG (both suites `gametest/SkyseedGameTests` + `gametest_26_1_2/SkyseedTests`).
- **#28 — placement gametest.** Lower risk than feared (all three pools are single-piece surface builds), but still add
  a placement gametest on the `big_region` pad confirming the manor / bog trial assemble + seat without floating,
  burying, or clipping the pad edge at the locked `pad`/`sink`.
- **#29 — on-pad assembly gametest.** Assert characteristic BWG blocks land (aspen planks / the bog-trial vault) with no
  dirt scars, in both suites (this is the foreign-processor check — verified by eye in the spike, now guarded in CI).
- **#30 — release hygiene.** `mod_version` bump + CHANGELOG per structure step; the three `debug_*` spike themes +
  models + lang + the `DEBUG_SEED_THEMES` registration have been **deleted** now the real overrides shipped.
- **Both nodes green**, then an in-game throw-test over `aspen_boreal` / `pale_bog` is the human sign-off (throw the
  woodland-mansion seed over an aspen-boreal biome column; the trial-chamber seed over a pale-bog column). Watch the
  [[skyseed-structure-chest-openable]] air-cell rule on any BWG chest that lands under an opaque block.

*Note — no tier mirror, no huge tier:* the mansion/trial seeds are **single-tier** (no `_large`/`_huge`), so
[[skyseed-mirror-island-tiers]] doesn't apply, and the throw-test confirmed the manor needs no huge island. (Optional
later: also surface the aspen manor as a *rare* inside `huge_forest`, where the vanilla mansion already appears as a
rare roll — a separate add, not scheduled.)

---

## Phase C — #33: more trial-chamber variants (the "no square boxes" tail) — ✅ BUILT (v0.227.0, both nodes green)

Three new pieces in [TrialChamberTemplates.java](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/TrialChamberTemplates.java),
all sharing the shipped wall/floor **texturer** (framed-panel mosaic + laid-floor tiler + corner posts) so the warren
stays coherent, obeying the v0.203 learnings (roomy passages, no free-standing pillars, branch-ends land in a room):

1. **`crossing`** — a 4-way node so halls *cross* (a hall in + three passages out), turning the warren from a tree into
   a possible grid. A chiseled-copper cross inlaid in the floor + a crown cornice. → the `halls` pool (weight 2).
2. **`alcove_corridor`** — a straight through-passage (hall in / hall out) that ALSO spurs a chamber (`rooms` pool) off
   an **L-shaped side alcove** — so a room can open off a hall, not only off a junction. The bump-out breaks the box. →
   the `halls` pool (weight 2).
3. **`vaulted_chamber`** — a grand 9×9 climactic room: four corner spawner/vault **cells** around a raised central
   ominous-vault **sanctum**, under a **stepped groin vault** (y6 eave → y8 apex, island fill forming the risers — not
   a flat lid). Two spawners feed the corner normal vaults; under Bad Omen they feed the centre ominous vault. → the
   `rooms` pool (weight 1), so it can also cap a descent.

**Shipped:** the three pieces + the `halls`/`rooms` pool wiring + assembly/mosaic gametests in **both** suites
(`trialCrossingCrossesFourWays` / `trialAlcoveCorridorSpursAChamber` / `trialVaultedChamberIsAMultiCellVault`). The
regen dance ran (new `.nbt` generated on 1.21.1, needed the 2-build pass; 26.1.2 loads them via DFU), no `Blocks.*`
cross-version issue (all trial blocks are version-stable). Both nodes green (217/219). **Left:** the in-game feel/fit —
does the warren wind + read right with the new pieces — folded into the standing **#61** trial tune pass.

---

## Phase D — #49: prairie houses (BUILT) / rugged fossil (recorded)

Same route-1 biome-override machinery — split by value:

- **Prairie house — ✅ BUILT (v0.228.0, both nodes green 218/220).** A wrapper pool
  [prairie_house/start.json](src/main/resources/data/skyseed/worldgen/template_pool/prairie_house/start.json) (BWG's
  intact `prairie_house` + `abandoned_prairie_house` ruin, 50/50, `minecraft:empty` processors) + a second override
  [biomeswevegone_hamlet_prairie.json](src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_hamlet_prairie.json)
  on the **hamlet** seed over `biomeswevegone:prairie`. Because the house is 15×16 but the hamlet island is only radius
  9–11, the band carries a **`shape` override** enlarging the island to radius 12–14 (`IslandGenerator` honours a
  per-band shape). Inert without BWG (biome gate); golden-master gametest both suites.
- **Rugged fossil — recorded, NOT built (a considered call).** Unlike the others, BWG's `rugged_fossil` pool is six
  pieces with **`projection: terrain_matching`** (they sink/adapt to *natural* badlands terrain), which doesn't read
  right stamped on a flat island pad; it's minor scenery (bone blocks); and Skyseed already ships an authored
  [structure/fossil_dig](src/main/resources/data/skyseed/structure/fossil_dig) island covering that niche. Building it
  would be a placement-tuning spike for near-zero value, so it stays recorded per the plan's "fine to leave recorded."
  *(If ever wanted: a badlands-seed override over `rugged_badlands` with a rigid re-projection + sink tuning.)*

---

## Phase E — #60: dedicated structure seeds — SUPERSEDED (route 1 shipped)

The manor/bog-trial/prairie-house all shipped as **biome-keyed variants of existing seeds** (Phases B/D), and the
throw-test confirmed that carries them, so the dedicated-seed fallback is **not needed and not built**. Kept only as a
recorded escape hatch: if some future BWG structure should be a distinct player *goal* with its own onboarding, it would
get a new seed item + recipe + advancement + guide + icon + `structure_seeds` tag, exactly like the mansion/trial/village
seeds. (The standing dedicated-seed bar in the PLANOFPLANS Decisions log is about *biome/farm* families, not structures,
so it doesn't forbid this — it's purely a delivery choice.)

## Phase F — #68: net-new bespoke structures (record, don't schedule)

The long-tail *want* beyond resurrecting BWG's set — captured so it isn't lost, **not** part of this pass. Already
partly scoped by the shipped VARIETYSTRUCTUREPLAN 5%-surprise engine (Band 3 is where these land). Candidates:
- Little **Create sheds** (a token contraption on a growable pad).
- **Abandoned Inferium farmlands** (derelict MA plots — ruined farmland + a few essence crops to reclaim).
- …open-ended flavour builds. Each would take the same variant/spike vehicle + no-boxes + gametest + version-bump
  discipline. Tracked as **#68** in PLANOFPLANS' long tail / VARIETYSTRUCTUREPLAN Band 3.

---

## Sequencing & effort

| Phase | Items | Effort | Gate |
|---|---|---|---|
| ~~**A** spike~~ ✅ done | #26/#27 vehicle | small | **route 1 LOCKED** for both (throw-test passed 2026-07-05) |
| ~~**B** build manor + bog trial~~ ✅ done (v0.226.0) | #26 #27 + riders #28 #29 #30 | small each (data-only overrides) | both nodes green (214/216); **in-game throw-test over `aspen_boreal`/`pale_bog` left** |
| ~~**C** trial variants~~ ✅ done (v0.227.0) | #33 (×3 pieces) | medium | both nodes green (217/219); in-game feel folds into #61 |
| ~~**D** prairie house~~ ✅ done (v0.228.0); fossil recorded | #49 | small | both nodes green (218/220); in-game throw-test over `prairie` left |
| **E** dedicated seeds | #60 | — | fallback, superseded — route 1 shipped |
| **F** net-new bespoke | #68 | — | recorded want, not scheduled |

**Phase B is now smaller than first scoped** — route 1 is pure data (two `theme_override` JSONs + one wrapper pool +
gametests), no authored templates and **no regen dance**, since we assemble BWG's own committed `.nbt`.

Riders **#28 / #29 / #30** are not separate phases — they fold into every B/C/D build step.

## Risks / unknowns

- ~~**BWG pool internals (#29).**~~ **Resolved by the spike** — the aspen manors + bog trial assembled clean on the pad,
  no processor scarring. Still CI-guarded by the #29 assembly gametest.
- ~~**Vertical bounding-box collision (#28).**~~ **De-risked** — all three BWG pools are **single-piece surface** builds
  (not multi-storey vertical jigsaws), and they seated cleanly. Still add the #28 placement gametest.
- ~~**Inert-safety of a route-1 variant.**~~ **Not an issue for the biome-override delivery** — the override is gated on
  a BWG biome that can't exist without BWG, so it can never fire (or grow empty) without the mod; byte-identical golden
  master still asserted (#30).
- **`pale_bog` island shape vs the deepened trial island.** The trial-chamber base theme deepens its island for the deep
  warren (`sink 10`, radius 24–30); the bog trial is a shallow surface build seated at `sink 1`. It sits fine on top, but
  the deep island under it is wasted — an optional Phase-B tuning is to let the `pale_bog` override also flatten the shape.
- **Small-piece framing (#33).** The 5×5 cell / 3-wide corridor have little wall area for the 4-module mosaic; the
  alcove/cross pieces may need a smaller module or plainer treatment (same caveat the rollout already noted).
- **Regen blast radius.** Phase C touches shared `trial_chamber/*.nbt`; honour the staging trap + the `Blocks.*`
  cross-version gotcha, and regen on 1.21.1 only. *(Phase B has no regen — it's data-only.)*

## Open questions (small Phase-B build calls)

- **Aspen manor mob pack:** keep BWG's baked 2–4 **vindicators**, or reuse the mansion seed's **evoker → Totem of
  Undying** garrison? *Leaning evoker garrison* so the aspen manor keeps the mansion's signature reward — confirm at build.
- Should the manor *also* surface as a biome-appropriate **rare** inside `huge_forest` (it already hosts the vanilla
  mansion rare)? Deferred — the biome-override seed delivery is the committed path; this is an optional later add.
