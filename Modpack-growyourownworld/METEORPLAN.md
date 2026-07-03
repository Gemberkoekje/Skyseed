# METEORPLAN — Meteor island redesign (AE2PLAN #18e)

> Child of [AE2PLAN.md](AE2PLAN.md) #18e. Replaces the current "charred rock ball" meteorite island with **a normal
> overworld island that a meteor crashed onto**: an overworld body + an impact crater holding a sky-stone globe with
> the Mysterious Cube (the press source) at its centre. User request, 2026-07-03.

## Vision

- The island reads as **a normal overworld island** (grass/dirt/stone, light tree/grass cover) — *not* a bespoke
  dark theme. It's "a regular island a meteor happened to hit."
- At the top-centre: an **impact crater** — a bowl lined with **obsidian** + scorched **glass / magma / basalt**.
- Seated in the crater: a **sky-stone globe** (`ae2:sky_stone_block` sphere), top exposed.
- Dead centre of the globe: the **`ae2:mysterious_cube`** — the only press source (breaking it drops all 4 presses;
  loot already shipped, AE2PLAN #18b).
- **Scaled by tier:** ~5×5 meteor on the base island → medium on large → big on huge.
- **Inert without AE2:** the globe + cube are AE2 blocks and skip (`Lookup.hasBlock`); the crater is vanilla, so a
  no-AE2 world just gets a scorched impact crater on a normal island — still a valid island.

The **seed recipe + gating are unchanged** (IE + certus, AE2PLAN #18c) — only the island's *appearance* and the
sky-stone *source* (a globe instead of ore veins) change.

## Architecture

### 1. The `meteor` theme field — via a codec refactor

`planIsland` only receives the `IslandTheme` (not its id), so the trigger must be a **theme field**, not an id check.
But `IslandTheme.CODEC` is already at **DFU's 16-field `group` limit** (shape…caves). Adding a 17th (`meteor`) needs
one combined slot. Only **one** site constructs `IslandTheme` (`ThemeOverride.applyTo`), so this is contained.

- New record `Meteor(IntRange radius)` + codec (mirrors `Caves`): `radius` = the sky-stone globe radius.
- `IslandTheme`: add a 17th field `Optional<Meteor> meteor` (+ `meteor()` accessor).
- `IslandTheme.CODEC`: fold the two terrain-carve optionals into **one** map-codec slot so the `group` stays ≤16
  (JSON keeps `caves` and `meteor` both top-level):
  ```java
  private record Carve(Optional<Caves> caves, Optional<Meteor> meteor) {
      static final MapCodec<Carve> CODEC = RecordCodecBuilder.mapCodec(c -> c.group(
          Caves.CODEC.optionalFieldOf("caves").forGetter(Carve::caves),
          Meteor.CODEC.optionalFieldOf("meteor").forGetter(Carve::meteor)
      ).apply(c, Carve::new));
  }
  ```
  The 16th group entry becomes `Carve.CODEC.forGetter(t -> new Carve(t.caves(), t.meteor()))`; the `apply` splits it
  back into the two constructor args. (Same "sub-record flattened via MapCodec" trick `ThemeOverride.Patch` already
  uses for the 16-field limit.)
- `ThemeOverride.applyTo`: pass `base.meteor()` through (overrides never patch the meteor — it's base-theme only).

### 2. `MeteorPlacer` — a carving pass (mirrors `CaveCarver`)

Same shape as `CaveCarver.carve(...)` — mutates the shared `blockMap` + `surfaceList` in place, hooked in
`IslandGenerator.planIsland` right **after** caves (line ~112) and **before** structure/decoration (so no trees grow
in the crater; a breached surface column is dropped from `surfaceList` like caves do):
```java
theme.meteor().ifPresent(m -> MeteorPlacer.place(blockMap, buffers.surfaceList(), center, baseRadius, m, random));
```
Block ids resolved via `Lookup.hasBlock` / `Lookup.blockState` (inert-safe skip), obsidian/glass via `Blocks.*`.

## The carve algorithm (`MeteorPlacer.place`)

Inputs: `blockMap`, `surfaceList`, `center`, `baseRadius`, `Meteor`, `random`.

1. **Sizes** from `meteor.radius`: `globeR = radius.sample()`; `craterR = round(globeR * 2)`; crater depth
   `≈ globeR`. Clamp so the bowl floor stays above the island underside (a safety margin like `CaveCarver`).
2. **Locate** the crater at the island top-centre: build a per-column `topY` from `surfaceList`; `surfaceY = topY` at
   the centre column. Globe centre = `(center.x, surfaceY − floor(globeR*0.4), center.z)` — seated so the top pokes
   out.
3. **Carve the bowl.** For each `(dx,dz)` with `d = √(dx²+dz²) ≤ craterR`:
   - bowl depth = `round((1 − (d/craterR)²) · craterDepth)` (paraboloid, deepest at centre);
   - `remove` blockMap cells from that column's top down to the bowl floor — **except** cells inside the globe sphere;
   - **line** the bowl floor + walls with **obsidian**, sprinkled (by chance) with **glass / black stained glass /
     magma_block / basalt** (scorched melt) and the odd **crying_obsidian**;
   - drop this column from `surfaceList` (no decoration in the crater).
4. **Place the globe.** For `dx²+dy²+dz² ≤ globeR²` around the globe centre: `sky_stone_block` (skip all of step 4–5
   if `!Lookup.hasBlock("ae2:sky_stone_block")`). *(Optional polish: outer shell `smooth_sky_stone_block`, inner
   `sky_stone_block` — a tunable.)*
5. **Place the cube** at the globe centre: `ae2:mysterious_cube`.
6. `supportFallingBlocks` (generator, runs after) is unaffected — obsidian/glass/sky-stone don't fall, and the bowl
   floor is solid, so nothing opens to the void.

## Theme JSON changes (`meteorite{,_large}` / `huge_meteorite`)

- **Palette** → overworld: `grass_block` / `dirt` / `stone` (a plains/meadow look), light `surface_scatter`.
- **Variants** → a normal-island variant: grass + sparse oak/birch trees + a few flowers (so it looks lived-in, not
  bespoke). Keep it distinct enough from the Meadow island.
- **`ores`** → **drop the AE2 veins** (`sky_stone_block` / `flawless_budding_quartz` / `quartz_block`) — the globe is
  the sky-stone source now, and certus lives on rocky/ancient (AE2PLAN #18d). Give it plain overworld ores
  (iron/coal/copper) so mining reads normal.
- **`meteor`** → `{ "radius": {"min":2,"max":2} }` base · `{3,4}` large · `{5,6}` huge (tunable).
- Keep `huge_meteorite`'s `caves` (interior caves + a surface meteor don't collide — meteor is centre-surface, caves
  ring the interior).

## Inert-safety (no AE2)

- Worldgen stays byte-safe: the globe/cube skip via `Lookup.hasBlock`; the crater is vanilla → a scorched impact
  crater on a normal overworld island (a fine, if pointless, island). No hard AE2 reference in code (ids are strings
  resolved at runtime, like every other modded block).
- The Patchouli entries stay `"flag": "mod:ae2"`-gated (unchanged).

## Gametests (both nodes — the 1.21.1 + 26.1.2 mirrors)

- **Revise `meteoriteIslandCarriesSkyStoneOnAllTiers`** — it currently asserts the theme's *ores* include
  `ae2:sky_stone_block` + budding; after the redesign those veins are gone, so that assertion must change or the test
  fails. New coverage:
  - `meteorIslandFormsCrater` — plan each tier; assert the block plan contains **obsidian** (the crater is vanilla, so
    this works in the AE2-less dev env) and the island builds (>N blocks). Guards the pass runs + carves on all three
    tiers.
  - The globe/cube can't be asserted in the dev env (no AE2 → skipped); instead assert the **theme resolves + carries
    a `meteor` config** (data-level), and that the base tier still builds (inert path — no crash without AE2).
- No structure `.nbt` → **no regen dance**; just the two gametest suites.

## Build order (checklist) — ✅ SHIPPED 2026-07-03 (both nodes green: 1.21.1 163, 26.1.2 165)

- [x] `Meteor` record + codec (`worldgen/theme/Meteor.java`).
- [x] `IslandTheme` — add `meteor` field + the `Carve` combined-slot codec; update `ThemeOverride.applyTo`.
- [x] `MeteorPlacer` (`worldgen/MeteorPlacer.java`) — the carve algorithm above.
- [x] Hook in `IslandGenerator.planIsland` (after caves).
- [x] Rewrite the 3 meteorite theme JSONs (overworld body + variants + drop AE2 ores + `meteor` field).
- [x] Update/replace the meteor gametest on **both** nodes.
- [x] Build + gametest both nodes (`:1.21.1:` JDK 21, `:26.1.2:` JDK 25) green.
- [x] Docs: tick AE2PLAN #18e; refresh the #18a island description (globe, not veins).

## Tunables / open decisions

- **Globe make-up:** solid `sky_stone_block` (simplest) vs. a `smooth_sky_stone_block` shell + `sky_stone_block` core
  (more vanilla-authentic). Default: solid; revisit after a throw-test.
- **Crater palette weights** (obsidian vs glass vs magma vs basatl) — tune for looks in-game.
- **Meteor sizes per tier** — first-pass {2}/{3,4}/{5,6}; throw-test and adjust so the base reads as "~5×5".
- **Does the meteorite island keep a budding-certus block?** Plan says no (certus is rocky/ancient + the transform).
  Add one back only if the on-island convenience is wanted.

---

# Phase 2 — AE2 compat layer, jar-side default recipe, and wild meteors

> User request, 2026-07-04. Phase 1 (above) shipped the visual. Phase 2 makes the meteorite a proper **Skyseed↔AE2
> compat feature** (no hard dep; invisible without AE2), moves the **default recipe into the jar** (craftable with
> just Skyseed + AE2), and adds a **1% wild-meteor** on overworld islands as the standalone bootstrap. The **modpack**
> keeps its KubeJS recipe override and **disables** the wild meteors. **✅ SHIPPED 2026-07-04 (both nodes green — 1.21.1: 163, 26.1.2: 165).**

## Why (the problem today)

- The jar ships a *vanilla* baseline recipe (blackstone+basalt) that's craftable **without AE2** and uses no AE2
  items — so it's neither a real default (AE2-gated) nor compat-clean, and the whole meteorite feature (seed, island)
  is visible/craftable even when AE2 is absent (only the Patchouli entries are `mod:ae2`-gated).
- **Principle:** Skyseed must have **no hard deps, only optional ones.** The Meteorite Skyseed should exist only with
  AE2, and craft from AE2 items — behind a compat layer.

## Target behaviour

| | Standalone (Skyseed + AE2) | Modpack |
|---|---|---|
| Meteorite seed | registered + craftable (AE2 present) | same |
| Default recipe | **jar**: sky stone + certus quartz (AE2-gated) | KubeJS **override**: IE steel + Create precision mechanism + certus (no sky stone) |
| Sky-stone bootstrap | **1% wild meteors** on overworld islands (small, sky-stone globe, no cube) | wild meteors **disabled** (config) |
| Certus bootstrap | the rocky/ancient `quartz_block` deposit (#18d, already in the jar, inert-safe) | same |
| Without AE2 | seed/recipe/entries all absent (no errors), wild meteors leave a plain vanilla crater | n/a (AE2 always present) |

**Standalone flow:** rocky/ancient certus + a lucky 1% wild meteor's sky stone → craft the Meteorite Skyseed →
the full meteor island (globe + **Mysterious Cube → all 4 presses**). The pack replaces the sky-stone recipe cost
with the IE gate (avoids the circular "need sky stone to craft the seed that gives sky stone").

## Pieces

1. **Compat gate the seed (conditional registration).** Add `compat/Ae2Compat.LOADED = ModList.get().isLoaded("ae2")`
   (matches the `compat/` pattern — ModonomiconCompat/PatchouliCompat). In `ModItems`, add the 3 `meteorite*` entries
   to `SEED_THEMES` **only when `Ae2Compat.LOADED`**. Without AE2: no seed item (not in the creative tab, not
   craftable, and the `everySeedRecipeAndBookEntryMatchesSeedKind` gametest skips it — the dev env has no AE2). The
   `meteorite*` **theme JSONs still load** (harmless dead data; the globe/cube already skip via `Lookup`).

2. **Default recipe in the jar, AE2-conditioned.** Rewrite `recipes/data/skyseed/recipe/meteorite_skyseed.json` to
   `neoforge:conditions: [{ "type": "neoforge:mod_loaded", "modid": "ae2" }]` + ingredients **sky stone + certus
   quartz** (e.g. a ring of `ae2:smooth_sky_stone_block` around an `ae2:certus_quartz_crystal`). Same condition on the
   `_large`/`huge_` recipes. Without AE2 the recipe is skipped (no error). ⚠ **Verify `build.gradle`'s
   `generateRecipes` preserves `neoforge:conditions`** through its JsonSlurper round-trip (it only rewrites
   `key`/`ingredients`, so it should — confirm). The modpack's `overrides/kubejs/data/skyseed/recipe/meteorite_skyseed.json`
   (IE+certus) still shadows this for the pack.

3. **Advancements with no AE2 hard-ref.** `gathered_`/`reveal_` currently list item ids; change their item criterion
   to a **skyseed tag** whose AE2 entries are `{"id": "ae2:…", "required": false}` (empty without AE2 → no error, and
   an empty predicate simply never triggers), OR drop the "has the makings" criterion and gate reveal on the `prereq`
   recipe alone. `craft_` advancements reference a **recipe id string** (`recipe_crafted`) — those are fine as-is.

4. **Wild meteors.** `MeteorPlacer.place(...)` gains a **`withCube`** flag (dedicated = true; wild = false → sky-stone
   globe, **no Mysterious Cube**). In `IslandGenerator.planIsland`:
   ```
   if (theme.meteor().isPresent())              MeteorPlacer.place(..., radius, /*withCube*/ true, random);
   else if (dim == overworld && naturalIsland && random.nextFloat() < wildMeteorChance)
                                                MeteorPlacer.place(..., /*small*/ 1-2, /*withCube*/ false, random);
   ```
   `dim` is already resolved; "naturalIsland" excludes structure/village themes (see Decision 5).

5. **Config toggle (mod common config).** Add a NeoForge **common** config `SkyseedCommonConfig` (mirror
   `SkyseedClientConfig`) with `wildMeteorChance` (default **0.01**), read in the generator. The modpack ships
   `overrides/config/skyseed-common.toml` with **`wildMeteorChance = 0.0`** (wild meteors off).

6. **Gametests (both nodes).** Keep `meteorIslandFormsCrater`. Add a direct `MeteorPlacer` no-cube smoke, or a wild
   roll with the chance forced high, asserting a sky-stone-less/obsidian crater and (with no AE2 in the dev env) no
   crash. Confirm the AE2-conditioned recipe + tag-based advancements **load without errors** when AE2 is absent (the
   gametest server load surfaces any).

## Open decisions (please confirm — my recommendations in **bold**)

1. **"without strange device" = no Mysterious Cube on wild meteors** (just a small sky-stone globe). **Yes.**
2. **Wild-meteor globe contents:** sky stone only, **or** sky stone + a little certus? **Sky stone only** — certus is
   already on rocky/ancient, and it keeps the wild find purely a sky-stone bootstrap.
3. **Default recipe shape/cost** (sky stone + certus): **8× `ae2:smooth_sky_stone_block` ring + 1× `ae2:certus_quartz_crystal`
   centre → the seed.** Cheap-ish, since getting the sky stone at all (a 1% meteor) is the real gate.
4. **Which themes get wild meteors** ("overworld-type"): **overworld dimension + a natural island body** (exclude
   structure/village/nether/end themes — e.g. skip any theme with a `jigsaw` or `rare_structures`, or gate on a small
   allowlist of the biome/rocky families). Confirm the exclusion rule.
5. **Config vs datapack for the 1% toggle:** a **mod common config** (simplest global switch; pack ships the toml).

## Build order (Phase 2) — ✅ SHIPPED 2026-07-04 (both nodes green)

- [x] `compat/Ae2Compat` (LOADED flag) + conditional `meteorite*` registration in `ModItems`.
- [x] Jar default recipe → AE2-conditioned, sky stone + certus (3 tiers); verify `generateRecipes` preserves conditions.
- [x] Advancements → tag-based (or prereq-only) so they don't hard-ref AE2 items; add the skyseed tag(s).
- [x] `MeteorPlacer` `withCube` flag; generator wild-meteor roll gated on `wildMeteorChance` + natural-overworld.
- [x] `SkyseedCommonConfig.wildMeteorChance` (default 0.01); read in the generator; pack `skyseed-common.toml = 0.0`.
- [x] Gametests both nodes (wild no-cube smoke + no-AE2 load-clean check).
- [x] Modpack: keep the KubeJS IE+certus override; add `overrides/config/skyseed-common.toml` (chance 0.0).
- [x] Build + gametest both nodes; docs (AE2PLAN #18b/#18c/#18e, this plan).

## Caveats

- Conditional seed registration runs at **mod construction** (ModList is available then) — build `SEED_THEMES`
  accordingly, don't mutate it later.
- The mod's `recipes/` are auto-downgraded for 1.21.1 by `generateRecipes`; a raw modpack datapack recipe is **not** —
  keep using object-form ingredients in the KubeJS override (already fixed).
- Without AE2, double-check nothing logs a load error: recipe (condition-skipped ✓), advancements (tag/empty ✓),
  Patchouli (`mod:ae2` ✓), theme (block ids are strings, skipped at gen ✓).

---

# Phase 3 — tiered press drops (small → 1, medium → 2 different, huge → 4)

> User request, 2026-07-04. Scale the press payout with the meteor tier without cramming multiple "strange devices"
> into one center. **Approach chosen: a custom Meteorite Core block (the recommended option).**
> **✅ SHIPPED 2026-07-04 (both nodes green — 1.21.1: 163, 26.1.2: 165).**

## The constraint (investigated)

- AE2's Mysterious Cube is a **property-less block** (`"variants": { "": … }`) with **one global loot table** — a block
  always drops the same loot regardless of which island it's in, and the meteor is a *carved shape*, not a registered
  structure to `location_check`. So a single cube **cannot** tier its own drop.
- The generator places **raw blockstates only** (no per-block NBT) → no "loot chest with tiered contents" without new
  plumbing. And **skyseed registers zero blocks today** (worldgen-only mod).
- ⇒ To tier the drop, the block at the center must **differ by tier** → skyseed owns a block.

## Design

- **New block `skyseed:meteorite_core`** (skyseed's first custom block) with an int blockstate **`tier` (0/1/2)**.
  Both nodes share the 2-arg `DeferredRegister.Blocks.registerBlock(name, factory)` — apply properties inside the
  factory, **no Stonecutter split**. No BlockItem (worldgen-only; the player mines it → presses, and it's gone).
  Model = `cube_all` over a **vanilla** texture (`minecraft:block/crying_obsidian`) so **no new PNG**. hardness ~3,
  **iron-tier** (`requiresCorrectToolForDrops` + `needs_iron_tool` + `mineable/pickaxe`, matching the AE2 sky-stone
  globe it sits in — so a harvest-tier readout appears, e.g. Silent Gear's Jade tier plugin shows "Tier 02 - Blaze
  Gold" like sky stone; and presses can't be lost since you can't reach the core through iron-tier sky stone without
  the tool to harvest it).
- **`MeteorPlacer`** replaces the Mysterious-Cube placement with the core, seated at the globe centre. Signature:
  `withCube` (bool) → **`coreTier` (int)**: `-1` = no core (wild meteor), `0/1/2` = core at that tier. Placed inside
  the existing `Lookup.hasBlock(SKY_STONE)` branch → still **AE2-only** (no stray cores without AE2). Retire the
  `overrides/kubejs/…/mysterious_cube.json` loot override (the cube is no longer placed).
- **`Meteor` record** gains `core_tier` (optional, default 0); the 3 theme JSONs set 0 / 1 / 2. `IslandGenerator`
  passes `m.coreTier()` (dedicated) or `-1` (wild).
- **Loot table `loot_table/blocks/meteorite_core.json`** — 3 pools, each gated by a
  `minecraft:block_state_property` on `tier`, **all tag-based so it's inert without AE2** (tag entries resolve empty,
  no load error — unlike item entries):
  - **tier 0** — `{ tag: #ae2:inscriber_presses, expand: true }`, rolls 1 → **1 random press**.
  - **tier 1** — six equally-weighted `{ tag: #skyseed:meteorite_pair_N, expand: false }` entries, rolls 1. A tag
    entry with `expand:false` drops **one of every item in the tag**, so each pair-tag = its 2 presses. Uniform over
    all **6 pairs**, always **2 distinct** — the clean "2 of 4, always different, no fixed brackets" the user wanted.
  - **tier 2** — `{ tag: #ae2:inscriber_presses, expand: false }` → **all 4 presses**.
- **6 pair-tags** `tags/item/meteorite_pair_0…5.json`, each two `{"id":"ae2:…_press","required":false}` (empty & clean
  without AE2). Presses: `calculation_processor_press`, `engineering_processor_press`, `logic_processor_press`,
  `silicon_press`.
- Block name lang + `mineable/pickaxe` block tag. Gametest: assert each tier's `Meteor.coreTier()` is 0/1/2 (data
  check — the block itself isn't placed in the AE2-less dev env, since there's no sky-stone globe).

## Build order (Phase 3) — ✅ SHIPPED 2026-07-04 (both nodes green)

- [x] `block/MeteoriteCoreBlock` (TIER 0..2) + `registry/ModBlocks` (createBlocks + 2-arg registerBlock) + register in `Skyseed`.
- [x] Blockstate + model (vanilla texture) + lang + `mineable/pickaxe` tag.
- [x] `loot_table/blocks/meteorite_core.json` (3 tier pools, tag-based) + 6 `meteorite_pair_N` item tags.
- [x] `Meteor` record `core_tier`; 3 theme JSONs set 0/1/2.
- [x] `MeteorPlacer` `withCube`→`coreTier`, place the core; `IslandGenerator` pass tier (dedicated) / `-1` (wild).
- [x] Retire the `mysterious_cube.json` KubeJS loot override.
- [x] Gametests both nodes (coreTier per tier); build + gametest both nodes; docs.
