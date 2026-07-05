# EPICSTRUCTUREPLAN — Band 4: grand epic structures on huge islands (#74)

> **Status: PLAN-FIRST, unbuilt. Decisions D1–D2 locked with the user (2026-07-05); design + sequencing below are
> ready to build.** This is **VARIETYSTRUCTUREPLAN Band 4** — a strict superset of the shipped Band 1–3 surprise engine
> (same code-authored `.nbt`, `modNames` inert-safety, D4 loot gating, regen/gametest hygiene). Child of
> [`Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md`](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md) (#68) and a
> sibling of [`STRUCTURELONGTAILPLAN.md`](STRUCTURELONGTAILPLAN.md) (whose `theme_override` seed/biome vehicle this
> reuses). Tracked as **#74** in [`PLANOFPLANS.md`](PLANOFPLANS.md).

## Context

VARIETYSTRUCTUREPLAN Band 3 shipped six "epic" surprise buildings — the vanilla **Ruined Chapel** and five multi-mod
set-pieces (**Magitech Workshop**, **Automated Essence Farm**, **FE→ME Substation**, **Alchemist's Distillery**,
**Sky-Freight Depot**). The owner compared the Magitech Workshop screenshot to BWG's **Aspen Manor** and was blunt: the
Aspen Manor oozes epicness, the Workshop "looks like a boxy shed."

The screenshots don't lie, and neither do the numbers:

| | Footprint | Height | `pad` | Storeys |
|---|---|---|---|---|
| BWG Aspen Manor | 28×20 | 24 | 16 | multi |
| Skyseed citadel (`huge_rocky`) | ~30×30 | ~25 | 15 | multi |
| **The six "epics" today** | **~7×7 / 7×5 / 9×5** | **~5** | **5–6** | **1** |

A **huge island is radius 24–30** (Ø 48–60) and comfortably hosts the pad-15 citadel or the pad-16 Aspen Manor. The six
epics are pad-5/6 single-storey boxes with a hole in the roof — they use a fraction of the real estate. They read as
sheds because they *are* shed-sized.

**Goal:** author a **grand version** of each of the six, sized to the huge island (pad ~14, ~24×24, 2–3 storeys, 16–22
tall), and wire it on the **huge** tiers only. They must not be boxes: dramatic silhouette (towers, spires, gantries,
snapped pylons), real verticality (collapsed roofs that reveal an upper floor, caved floors that drop to an undercroft),
and a ruined/emptied/haunted mood. The three **Create** builds get a **genuinely-spinning water wheel** (fed by a
contained flowing-water race) so something actually moves.

**Folded-in second ask (owner, 2026-07-05):** *reuse* the builds across more seed/biome combinations — each structure
(and the wider Band 1/2/3 catalog) is a single `.nbt` that can be listed on many themes and biomes, so a handful of
builds should cover a lot of ground. Today the five mod epics are each wired to **one** terrain family only
(Magitech/Substation/Freight → Rocky; Distillery → Badlands; Essence Farm → Meadow), while the vanilla Ruined Chapel is
already spread across ~12 theme/tier combos. See **§ Cross-wiring**.

### Decisions locked (2026-07-05)
- **D1 — Scale = "big on huge + keep small."** Author a *new* grand build per structure, wired **huge-tier only**. The
  existing small builds stay exactly as-is on the base/large tiers (untouched). Net: 6 new `.nbt`, no edits to the 6
  shipped small ones.
- **D2 — Motion = a real spinning Create water wheel.** In Create, a `water_wheel` spins on its own from adjacent
  *flowing* water; a windmill needs player right-click assembly, so worldgen sails stay static. So every Create build
  (Magitech, Essence Farm, Freight Depot) gets a water wheel on a walled flowing-water race that actually rotates.
  Windmill sails, where used for silhouette, are decorative/static only.

---

## How the machinery already works (reused verbatim)

- **Code-authored `.nbt`.** Each build is a `Built` map assembled in a `*Templates` class and written by
  `writeIfAbsent` (only if absent — safe to re-run), registered in
  [DevStructureGenerator.onCommonSetup](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/DevStructureGenerator.java:45).
  Shared helpers live in
  [StructureParts.java](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/StructureParts.java): `anchor`
  (jigsaw seat at footprint centre — the portal-twin origin.xz rule), `lootChest`, `gableRoof`, `linkFences`,
  `mobSpawner`.
- **Inert-safe modded blocks.** Modded machinery is authored with the `modNames` side-map — a vanilla analog carries
  the property serialisation, the emitted palette `Name` is swapped to the real mod id. See the `set(...)`/`built(...)`
  helpers + the water-wheel precedent in
  [CreateRuinsTemplates.watermill](src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/CreateRuinsTemplates.java:143)
  (water_wheel → `END_ROD`, cogwheel/shaft → log, casing/press → `STONE`/`CARVED_PUMPKIN`). Without the mod, the id
  resolves to AIR, so each build carries a **vanilla shell** as the assertable gametest anchor.
- **Wiring.** A theme `rare_structures` entry names a `pool` + `pad` (+ optional `sink`), a `weight`, `requires` mod
  ids, and a `mobs` pack. The epics sit at `weight: 1`. See
  [huge_rocky.json:386](src/main/resources/data/skyseed/skyseed/theme/huge_rocky.json).
- **Pools.** One-line `data/skyseed/worldgen/template_pool/<dir>/<name>.json` (single `single_pool_element`) — e.g.
  [magitech_workshop/workshop.json](src/main/resources/data/skyseed/worldgen/template_pool/magitech_workshop/workshop.json).
- **Gametests.** Per build, an on-pad assembly test in **both** suites
  ([gametest/SkyseedGameTests](src/main/java/dev/gemberkoekje/skyseed/gametest/SkyseedGameTests.java:4216) +
  `gametest_26_1_2/SkyseedTests`) placing the pool on the 48×24×48 `big_region` pad and asserting the vanilla shell +
  a signature no-box feature. Mirror `magitechWorkshopAssembles` (line 4216).

---

## The shared grand-scale design toolkit

New helpers (add to `StructureParts` or the new templates class), reused across all six so the ruins read coherently:

- **Multi-storey + downward collapse.** The single biggest epicness lever. Every build has ≥2 levels: a caved roof that
  exposes an *upper* floor to the sky, and a caved floor section that drops to an *undercroft/cellar* (a second room +
  chest + mob nest). This is what separates "ruined cathedral" from "shed with a hole."
- **A dominant vertical (16–20 tall).** Each build has one tower/spire/pylon/stack/gantry/crane that shatters the
  rectangle on the skyline. Half-collapsed, leaning, or snapped — never a clean box.
- **Supported ruin geometry.** Barrel-vault ribs / fallen beams / buttress arches must rest on both ends (the
  floating-beam bug class the physical-review pass caught in B24–B26). Rubble drifts (cobblestone/gravel/mossy),
  shattered glass, hanging cobweb curtains, soul-lanterns, wither roses, scorch marks = the haunted/emptied mood.
- **`waterWheelRace(...)` — the mover (Create builds).** A walled stone race: a header source at the top → a waterfall
  column *beside* the wheel (falling water is permanently "flowing" → the wheel spins forever) → a **sealed, fully
  walled catch basin** where the flow spreads and dead-ends. The axle `shaft` runs from the wheel into the (dead)
  contraption so the motion visibly "drives" it. **Void-safety is mod-independent** (water is vanilla): the basin is
  walled on all sides and floor-sealed *inside the footprint* so nothing waterfalls off the island rim, with or without
  Create. Verified in-world (spin + zero leak) at throw-test.

**Sizing rule.** Target footprint ≤ 27×27 and height ≤ 22 (fits `pad 14` on a radius-24–30 huge island with rim
margin, and stays inside the 48×24×48 `big_region` for the gametest). Anchor the jigsaw at the footprint centre
(e.g. `(13,0,13)`), all coordinates ≥ 0 (NBT can't hold negatives — no inset overhangs past 0).

---

## The six grand builds

New class `GrandEpicTemplates.java` (all six for cohesion), `generateInto(base)` writing the six `.nbt`, registered in
`DevStructureGenerator` after `MultiModRuinsTemplates`. New pools as `template_pool/<existing-dir>/grand.json` →
`skyseed:<dir>/grand`, so the small build (`.../<name>`) and grand build (`.../grand`) coexist. Each reuses its existing
scrap loot table + GLMs (D4-safe) — the grand version can hold **2–3** chests (undercroft + main), same tables.

### A. Grand Ruined Chapel — vanilla (`ruined_chapel/grand`) → huge_meadow, huge_rocky, huge_lush, huge_mushroom
A fallen **cathedral**. A long nave with a **side-aisle colonnade** (arched arcade of pillars — internal, anti-box) and
a clerestory, a raised chancel with a **great rose window** (part-shattered stained glass) over a chiseled altar +
soul-lantern + candle banks (reliquary chest). A tall **bell tower** at the front corner (~18–20, belfry with the
`bell` + louvred arches, a broken tapering spire). **Flying-buttress** stubs off the aisle walls. The nave roof is a
**caved barrel vault** — a few surviving stone-brick arch ribs span wall-to-colonnade (both ends carried), the rest
open sky, keystones strewn below. A **collapsed crypt**: a nave-floor cave-in drops to an undercroft of tombs (a second
chest + a mob nest). Haunted undead pack, bumped to 3–4. All vanilla ⇒ the whole thing is the gametest shell.

### B. Grand Magitech Workshop — Create + Iron's Spells (`magitech_workshop/grand`) → huge_rocky · **MOVER**
A two-storey **arcane forge-hall**. Ground machine floor + a stair-reached **mezzanine library** (the showpiece:
two-storey bookshelf walls, lectern, enchanting table on a raised **amethyst dais** ringed with clusters + candles,
brewing alcove, glowstone). A **spinning water wheel** on a stone **aqueduct** drives a line of Create husks through the
wall (`cogwheel → shaft → gearbox → mechanical_press` over its bed) — motion "powers" the dead contraption. A
half-collapsed **brick smoke-flue/stack** (~16–18, leaning) is the vertical; a caved roof exposes the mezzanine, a
breached wall spills rubble. Iron's mages (bumped). **D4:** casing/cog/shaft/press + water_wheel husks (the wheel spins
but drives nothing); loot `magitech_scrap` + `andesite_alloy`/`arcane_essence`/`blank_rune` GLMs. Shell: enchanting
table, bookshelves, amethyst, the aqueduct water+stone, chest.

### C. Grand Automated Essence Farm — Create + Mystical (`essence_farm/grand`) → huge_meadow · **MOVER**
A large terraced **inferium field** (~17×13, irrigation channels) under a towering **harvester gantry** — a 2–3-storey
oak/andesite trestle carrying rails + a cross-bridge with the `mechanical_drill` husk. A **spinning water wheel** on the
irrigation aqueduct powers the gantry drive line (axle shaft → cogwheel up the frame) — the field's water-powered
harvester, seized. A stone **control silo/tower** (growth-accelerator husk + coolant cauldrons, caved top) breaks the
horizontal. A **toppled gantry bay** leans with a fallen rail + drill askew; trampled rows, broken fences, scarecrows,
cobwebs; zombies (bumped). **D4:** tier-1 inferium farmland/crops/accelerator + Create casing/drill/cog + water_wheel;
loot `essence_farm_scrap` + `andesite_alloy`/tier-1 `inferium_essence` GLMs. Shell: oak-log gantry, fences, aqueduct
water+stone, control chest, silo.

### D. Grand FE→ME Substation — IE + AE2 (`substation/grand`) → huge_rocky · (no mover — verticality)
A gutted two-level **power-conversion hall**, roofless, floors part-collapsed, IE-west / AE2-east. The vertical is a
**lattice transmission pylon** (~18–20 alu-scaffold/HV, cross-arms) with a whole **span snapped off draped across the
roofline** (Fallen Powerline at epic scale). A scorched **conversion tower** crowns the **empty controller pit** —
still **D4-empty** (NO sky stone / controller / press), broken conduit antenna on top, fluix/certus terraces around it.
A collapsed **transformer bay** (post-transformer + capacitor husks tumbled) with a cracked floor to a **cable
undercroft** (second chest). Scorch, hanging cables (end-rod/chain), cobwebs, a creeper (bumped). **D4:** IE
concrete/sheetmetal/alu_post/connector_hv/post_transformer/capacitor + AE2 quartz/fluix, no controller/press; loot
`substation_scrap` + `wire_copper`/`fluix_crystal` GLMs. Shell: deepslate column+pit, conduit antenna, pylon frame,
chest, lantern.

### E. Grand Alchemist's Distillery — IE + Iron's Spells (`distillery/grand`) → huge_badlands · (no mover — tower)
A multi-level **arcane refinery**: a ground still-floor + a catwalk gantry level, part-collapsed roof. The vertical is a
tall **weathered-copper condenser tower** (~16–18, oxidation-banded copper stairs/slabs, lightning-rod vent, half
collapsed). A **cascade of stills** (lava-cauldron fires under copper still-heads + brewing stands, glass "retort"
columns with candles, IE metal-barrel tanks + capacitor, water-cauldron receivers), an amethyst focus dais, a bookshelf
study + lectern. A **collapsed distillation vat** (a caved-floor basin of contained tinted glass + spill, rubble).
Badlands weathering, cobwebs, wither roses, a necromancer (bumped). **D4:** IE sheetmetal/metal_barrel/capacitor husks;
loot `distillery_scrap` + `plate_iron`/`arcane_essence` GLMs. Shell: copper condenser column, brewing-stand stills,
amethyst, chest.

### F. Grand Sky-Freight Depot — Create + IE (`freight_depot/grand`) → huge_rocky · **MOVER**
A large **freight yard**: a multi-track siding with a long stalled train (loco + several brass/copper/railway-casing
cars on `small_bogey` husks), an IE loading dock (conveyor line, crate stacks, fuel barrels). A towering **gantry
crane** straddles the tracks (IE/andesite frame + a hanging hook/pulley) — the vertical. A **water tower** on legs feeds
a **spinning water wheel** that drives the crane's (dead) hoist capstan via an axle shaft — the depot's water-powered
hoist, the mover. A **signal gantry** spans the tracks with lanterns; a **collapsed warehouse bay** (caved roof, spilled
crates, exposed rafters); a **derailed car** tilted off the rail into rubble. Two zombies (bumped). **D4:** Create
bogey/casing/water_wheel + IE conveyor/crate/barrel husks; loot `freight_scrap` + `andesite_alloy`/`plate_iron` GLMs.
Shell: rail siding, signal gantry, aqueduct water+stone, crane frame, chest.

---

## Wiring (huge tiers only — small builds untouched)

For each grand build, in the **huge** theme(s) where the small epic already rolls, **swap the epic entry's** `pool`
(`.../<name>` → `.../grand`) and `pad` (5–6 → **14**, add `sink` if a build wants to seat lower), and **bump the `mobs`
adults** (enemies ≈ loot, now that the reward is bigger — +1–2 adults, optionally a `mobSpawner` for haunt). Weight
stays `w1`; `requires` unchanged. Base + large tiers keep the small entries verbatim.

| Grand build | Huge theme file(s) to edit |
|---|---|
| Ruined Chapel | `huge_meadow.json`, `huge_rocky.json`, `huge_lush.json`, `huge_mushroom.json` (chapel wired on all four today) |
| Magitech Workshop | `huge_rocky.json` |
| Automated Essence Farm | `huge_meadow.json` |
| FE→ME Substation | `huge_rocky.json` |
| Alchemist's Distillery | `huge_badlands.json` |
| Sky-Freight Depot | `huge_rocky.json` |

Swapping a pool changes the placed blocks on hit-seeds ⇒ **re-capture the golden master** for each touched huge theme
on the **1.21.1 node only**. RNG draw-count is unchanged (weighted-gate draws the same floats), so no weight drift.

---

## Cross-wiring: reuse each build across more seeds & biomes

A structure is one `.nbt` + a one-line pool; **wiring it onto another theme costs only a theme entry + a golden-master
re-capture — no new build.** That's the whole economy of the surprise engine (VARIETYSTRUCTUREPLAN §4). The catalog is
under-reused today: the mod epics sit on a single terrain family each. Three vehicles already exist in the codebase:

1. **Inline `rare_structures` on a terrain theme** — the 5% surprise, with a `requires` mod gate + optional `biomes`
   sub-filter. This is where every Band 1/2/3 build lives (e.g.
   [huge_rocky.json:391](src/main/resources/data/skyseed/skyseed/theme/huge_rocky.json)). **Reuse = list the same pool
   on more terrain themes.** The `requires` gate keeps it inert without the mod; the biome filter narrows *where within*
   a theme it can roll. Cost per add: one entry + a golden-master re-capture of that theme.
2. **`theme_override` → `biome_overrides[].jigsaw` on a SEED theme** — the aspen-manor / bog-trial vehicle
   (STRUCTURELONGTAILPLAN): when a dedicated seed (woodland_mansion, trial_chamber, hamlet, …) is grown over a chosen
   biome, its structure is *replaced* by another pool. See
   [biomeswevegone_woodland_mansion.json](src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_woodland_mansion.json).
   Inert because the host biome can't exist without its mod. **Best for a build with a natural host biome** (the vanilla
   Chapel, or adapting more foreign structures) — the mod epics have no distinguishing biome, so they stay terrain rares.
3. **`theme_override` appending to a terrain theme** — mod-keyed patches. Today these only add **ores** (the
   `create_*`/`immersiveengineering_*`/`ae2_*` compat files); the structure-append path is exercised only by the BWG
   seed overrides in vehicle 2. Available but not needed here — the epics' own `requires` gate already makes an inline
   entry inert, so vehicle 1 is simpler.

### Current epic coverage vs. proposed spread (indicative — confirm the list, OD-4)

Small build → more **base/large** terrain themes; grand build → more **huge** themes. All thematically justified, all
inert without their mods, all `w1` (an epic barely moves a theme's 50/35/15 mass — so epics are cheap to spread).

| Epic | Wired today | Add small → base/large | Add grand → huge |
|---|---|---|---|
| **Ruined Chapel** (vanilla) | meadow, rocky, lush, mushroom (all tiers) | **forest, hamlet, ancient** | **huge_forest, huge_ancient** |
| **Magitech Workshop** (Create+Iron's) | rocky (all tiers) | **ancient** | **huge_ancient** |
| **FE→ME Substation** (IE+AE2) | rocky (all tiers) | **ancient** | **huge_ancient** |
| **Sky-Freight Depot** (Create+IE) | rocky (all tiers) | **meadow, badlands, desert** | **huge_meadow, huge_badlands, huge_desert** |
| **Alchemist's Distillery** (IE+Iron's) | badlands (all tiers) | **desert, rocky** | **huge_desert, huge_rocky** |
| **Automated Essence Farm** (Create+Mystical) | meadow (all tiers) | **hamlet** (single-tier seed) | — (already on huge_meadow) |

Rationale: arcane ruins (Magitech) and AE2/IE gear (Substation) already share Ancient with the Wizard's Tower / AE2 Lab;
rail yards (Freight) and refineries (Distillery) cross the arid/rocky terrains; the Chapel is a natural village/forest
church. The vanilla Chapel is also the prime candidate for **vehicle 2** — e.g. surface the grand chapel as a biome-keyed
build on a village or mansion seed — if we want a *guaranteed* cathedral, not just a 5% one.

### The rest of the catalog (lower priority, same mechanism)

A quick spread audit while the golden masters are already being re-captured: Band-2 rares that fit more than their
current 1–2 families — e.g. **Wizard's Tower**/**AE2 Lab** → also Badlands; **IE Factory** → also Ancient; **Drill
Rig** → also Rocky; **Windmill** → also Forest. Optional; fold in per theme only where it reads true. Not new builds —
just entries.

### Cost, safety, and where it lands

- **Cheap and safe.** No new `.nbt`; each add is a theme entry reusing the build's existing pool + loot + `requires`
  gate (inert-safe, determinism-parity preserved). The only cost is the per-theme golden-master re-capture (1.21.1).
- **Weight budget (D2 of VARIETYSTRUCTUREPLAN).** Keep each theme near 50/35/15 — epics are `w1`, so 1–2 per theme is
  fine; don't stack four epics on one theme. The `huge_` tables that Phase-4 filled with commons have room.
- **Folds into the batches, not a new phase.** When Batch A/B/C builds a grand epic, wire it to **all** its target huge
  themes at once, and spread its **small** sibling to the extra base/large themes in the same step (one golden-master
  pass per touched theme). This *is* the VARIETYSTRUCTUREPLAN §8 open-decision-#1 "per-theme final lists" review.

---

## Sequencing (batches — each = version bump + CHANGELOG + both nodes green)

Follows the standing #30 rule and the [[skyseed-structure-staging]] **regen dance** (edit `*Templates.java`, delete only
the new `.nbt` from repo-root `src`, regen on the **1.21.1** node `runGameTestServer`, then green 26.1.2 which loads the
shared `.nbt` via DataFixerUpper — never regen there). New `.nbt` only (`writeIfAbsent`) — the shipped small builds are
never touched.

1. **Batch A — Grand Ruined Chapel (vanilla).** Lowest risk, no mods; builds the grand toolkit (multi-storey,
   collapse-to-undercroft, tower/spire, buttresses, barrel-vault ribs) that B/C reuse. Wire on its huge themes; regen;
   golden-master re-capture; assembly gametest both suites; throw-test.
2. **Batch B — the `waterWheelRace` mover + the 3 Create movers** (Magitech, Essence Farm, Freight Depot). Shared
   spinning-wheel race. Per build: D4 loot audit (no deny-column item), regen, golden master, gametest both suites.
3. **Batch C — Substation + Distillery** (IE + AE2 / IE + Iron's — verticals, no mover). Substation is the sharpest D4
   (keep the controller pit empty). Regen, golden master, gametests.

Within each batch, once a build is green, **also apply its § Cross-wiring spread** — add the extra theme entries for its
grand (huge) and small (base/large) siblings and re-capture those themes' golden masters — so coverage lands together
with the build instead of a separate pass.

---

## Verification

- **Assembly gametest per build (both suites).** Place `skyseed:<dir>/grand` on the `big_region` pad via
  `Jigsaw.placeCapped` (mirror `magitechWorkshopAssembles`), scan the 48×48 region **up to y≈22** (grand builds are
  taller than the old y≤12 loops), assert the vanilla shell **+ a signature grand feature** (tower/spire height, a
  buttress, the undercroft floor + second chest, the aqueduct water). This is the no-box + no-float guard in CI.
- **Inert golden-master gametest.** For each touched huge theme, assert byte-identical generation **without** the
  build's mods (the determinism-parity guard — the small epics already prove the pattern).
- **In-world throw-test (human sign-off), mods present, on a huge island.** Confirm: (a) seats on `pad 14` with rim
  margin, no floating/burying/clipping; (b) **the water wheel actually spins**; (c) **zero water escapes to the void**;
  (d) every chest opens (air-cell rule [[skyseed-structure-chest-openable]]); (e) it reads as *epic* — silhouette,
  verticality, haunt. Both nodes green before any commit.

---

## Risks

- **Water → void leak (the #1 risk on a sky island).** Flowing water off the pad edge waterfalls into the sky. The
  race/basin must be fully walled + floor-sealed inside the footprint, with vanilla blocks (mod-independent), verified
  in-world.
- **Wheel won't spin from still water.** Create wheels need *flowing* water — the design uses a permanent waterfall
  column, not a still tub. Verify rotation at throw-test; tune the race if needed.
- **Floating ruin geometry.** Ribs/beams/buttresses/gantry spans must rest on both ends (the B24–B26 floating-beam
  class). Assert supports in the gametest where feasible.
- **Height vs pad region / dome.** Keep ≤22 tall (big_region is 24; huge dome 12–20). A ~18–20 spire/pylon is fine.
- **Negative coords / chest jam / portal-twin anchor.** All coords ≥0; air cell above every chest; anchor at footprint
  centre.
- **Golden-master churn.** Re-capture only the touched huge themes, 1.21.1 only.

## Open decisions

1. **Chapel on thin huge themes.** Recommend keeping the grand chapel on `huge_lush`/`huge_mushroom` (the small one is
   wired there today, and it fits at huge radius) — preserves the thin-theme Explore reward floor.
2. **Mob bump amounts.** Recommend +1–2 adults per grand entry, plus an optional vanilla `mobSpawner` in the
   undercroft/haunted core for the "haunted" builds (Chapel, Distillery) — keeps enemies ≈ loot for the bigger reward.
3. **Cross-wiring target list (§ Cross-wiring).** Confirm the indicative spread table before wiring it — especially
   whether the mod epics should reach **Ancient** (Magitech/Substation) and the arid/rocky terrains (Freight/Distillery),
   and whether to also use **vehicle 2** (a biome-keyed seed override) to make the grand Chapel a *guaranteed* cathedral
   somewhere. Also confirm whether to include the lower-priority Band-2 spreads or leave them out of this pass.
