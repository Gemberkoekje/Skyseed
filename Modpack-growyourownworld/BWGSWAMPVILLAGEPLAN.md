# BWGSWAMPVILLAGEPLAN — Cypress swampland villages as a stilted overwater bayou

Child of [BWGVILLAGEPLAN.md](BWGVILLAGEPLAN.md); PLANOFPLANS **#73**. This is the resolution of that plan's deferred
**Phase-5 "swamp read"** decision — the in-game sign-off (2026-07-02) confirmed the cypress villages assemble but read
as a *dry* pad, and the call is now made: build the intended **stilted bayou**. Grounded in a 2026-07-02 codebase pass.

> **Status: IMPLEMENTED v0.188.0 (+ in-game round 1 fixes) — awaiting further sign-off.** Engine + data shipped (both
> nodes green): the three cypress bands carve the #64 swamp + lift onto stilts/boardwalks via a data-driven
> `stilt_height`; `PathSurfacer.supportStilts`/`resolveStilted` are the new terrain-aware passes (gametest-guarded).
> Approach note vs the original layer-3 plan: the data-driven **lift + terrain-aware legs** made per-piece baked raised
> floors unnecessary — **no `.nbt` regen** needed. The other five styles are untouched.
>
> **In-game round 1 (2026-07-02) surfaced two fixes, now applied:** (a) *not enough water* — the pond `radius` was the
> binding cap (`min(radius, baseRadius·extent)`), so `extent` did nothing; raised `radius` 6 → **24** and `extent`
> 0.72 → **0.9** so the water is island-filling (thin rim). (b) *a dry "footing" circle under the centre* was the
> levelled structure pad; a stilted build now **skips `levelStructurePad` entirely** (IslandGenerator.planStructure) so
> the village stands on stilts over open water instead of a solid disc.
>
> **In-game round 2 (2026-07-02):** the boardwalk railings were overwriting adjacent house blocks — `PathSurfacer.bridge`
> railed every exposed edge but only skipped other lane tiles, so a boardwalk tile bordering a house wrote a beam over
> its floor + a fence into its interior (never seen before because normal villages only bridge at the island edge, away
> from houses). Fixed: it now rails only into **empty** cells (skips any occupied neighbour); a boardwalk gametest
> assertion guards it.
>
> **In-game round 3 (2026-07-02) — two more:** (a) *fences still blocked the roads to the centre* — over an all-water
> swamp every lane borders open water, so any junction/gap irregularity became a blocking rail (dry roads never showed
> this — they're open dirt paths). Resolved by **dropping fence rails entirely on the over-water boardwalk**: a lane is
> now a plank deck on a sparse pier post (`PathSurfacer.boardwalk`), no rails — shallow water needs no fall-rail and the
> lanes stay open like dry paths. This also let the oak void-bridge revert to its original (byte-identical) form. (b) *a
> tree grew inside a house and killed its villager* — skipping the pad also dropped the pad's `surfaceList` clear, so
> decoration trees rooted on the surface grew up into the LIFTED houses. Fixed: a stilted build now clears the whole
> island's surface decoration (no trees/ground/ambient mobs planned under the elevated village; pond banks survive).
>
> **In-game round 4 (2026-07-02) — two more:** (a) *railings wanted back* (round 3 removed them all) — re-added, but as
> a rail run **only along the open-water edges** (`PathSurfacer.boardwalk`): a fence (on a sparse pier post) in the
> adjacent open cell, never on a lane tile or a solid block, and — unlike the earlier version — **no widening beam**, so
> no new walkable tile is created that could bridge a gap and get fenced. Rails therefore can't overwrite a house or
> fence a road. (b) *water no longer hemmed in* — raising `extent` to 0.9 pushed the pond to the island edge, and a
> plain pond leaves un-backed rim edges open (spilling everywhere). Added a **`contained`** `Pond` flag (the cypress
> bands set it) that walls the un-backed rim like a river does, leaving only a few `waterfallHere` stretches — hemmed in
> with a few deliberate falls.
>
> **In-game round 5 (2026-07-02) — three more:** (a) *trees on roads (all villages, e.g. weeping-witch)* — trees are
> placed BEFORE structures, so a lane laid across a tree's column leaves the trunk floating on the road; `PathSurfacer`
> now strips tree blocks above every resolved path tile (`clearCanopyAbove`), keeping trees in the gaps but off the
> roads. (Houses were already safe — the building overwrites its footprint.) (b) *village-center island → huge-sized* —
> the cypress VC `shape` now uses the huge-tier values (radius 24–30, `top_dome` 2–3, `max_under_depth` 18, `rim_noise`
> 0.42) and its pond `radius` is raised to 40 so `extent` still governs; the bigger island holds far more of the village
> on-island. (c) *no stilts dangling into the void* — `stiltDown` now places NO leg over pure void (no bed within the
> 8-block search); a stilt only ever rests on its own island, and can't dangle a stub or reach a different island (the
> boardwalk/floor floats there instead). Next in-world read: fine-tune `stilt_height` + `extent`.
>
> **Decisions resolved 2026-07-02** (were the "Open decisions" below):
> - **Water lever → reuse the #64 `pond`** (broad shallow swamp: `depth 2`, `slope: true`, raised `extent`). Not a
>   one-off wet-palette — the same [BWGPLAN #64](BWGPLAN.md) mechanism the Aquatic cypress band already ships.
> - **Stilts → terrain-aware post pass (approach 3b).** Legs column down to the first solid block (the bed) at
>   placement time, robust to varying water depth — adapt `PathSurfacer` (see [layer 3](#3-stilts--swamp-house-pieces-gain-legs-code-pathsurfacer-adaptation--regen)).
> - **Village-center → ONE bigger single island** (keep roughly the cluster footprint, not a tighter island), so the
>   dense street network + 4–6 shops still fit. Concrete `shape` in [layer 1](#1-island-shape--one-swamp-water-island-data-the-3-cypress-bands).

## Goal

Throw a cypress hamlet / trade-post / village-center seed over `biomeswevegone:cypress_swamplands` (or `cypress_wetlands`)
and grow a **single swamp-water island** whose houses stand on **willow/cypress stilts over the water**, linked by
**wooden plank bridges** — not the current dry grass pad, and (for the village-center tier) not the 3-island cluster.
The bayou aesthetic already documented in the BWGVILLAGEPLAN palette research ("stilted bayou on willow & cypress …
stilts over water … water channels") finally rendered.

## Current state (what the sign-off found)

- **Shape.** `trade_post` (r13–16) and `hamlet` (r9–11) already grow as a *single* island; **`village_center` is a
  3-island cluster** (`village_center.json` → `"cluster_offsets": [[0,0,19],[-16,0,-10],[16,0,-10]]`), which the
  cypress band inherits (it sets no shape). → de-cluster the cypress village-center only.
- **Surface.** All three cypress bands (`theme_override/biomeswevegone_{hamlet,trade_post,village_center}.json`, biomes
  `cypress_swamplands`+`cypress_wetlands`) set `surface: grass_block` + a podzol scatter — a **dry** pad. No water.
- **Pieces.** The `skyseed:village_swamp/*` piece set is the shared dry-pad house set (willow/cypress walls,
  `cattail_thatch` roofs) with grass-path streets — **no stilts, no bridges** (the deferred water work).

## The rework, in four layers

### 1. Island shape → one swamp-water island (data: the 3 cypress bands)
The base [`theme/village_center.json`](../../src/main/resources/data/skyseed/skyseed/theme/village_center.json) shape is a
**3-island cluster**: `"shape": { "radius": {min 10,max 12}, "rim_noise": 0.25, "underside": "teardrop", "top_dome":
{1,1}, "cluster_offsets": [[0,0,19],[-16,0,-10],[16,0,-10]] }` (spanning ~32×29). The cypress VC band inherits it (it
sets no `shape`; a band's `shape` REPLACES the base per `BiomeOverride.mergedWith`).
- **village_center (DECIDED — one bigger single island):** give the cypress band its **own `shape`** — same profile,
  **no `cluster_offsets`**, radius bumped to hold the dense net + 4–6 shops on one pad:
  ```json
  "shape": { "radius": { "min": 15, "max": 17 }, "rim_noise": 0.25, "underside": "teardrop", "top_dome": { "min": 1, "max": 1 } }
  ```
  (~32-block diameter ≈ the old cluster span; `reach: 64` already covers it). Tune radius in-game.
- **trade_post / hamlet:** already single-island — no `shape` change; only the water/surface below.

### 2. Water surface → a broad shallow swamp (data: reuse the #64 `pond`)
**DECIDED — reuse the [BWGPLAN #64](BWGPLAN.md) `pond` lever** (the Aquatic cypress band already ships it), NOT a
one-off wet palette. Add to all three cypress bands (and drop the dry `grass_block`+podzol surface for a wet `mud`
surface):
```json
"surface": "minecraft:mud",
"pond": { "block": "minecraft:water", "radius": 6, "depth": 2, "slope": true, "extent": 0.72,
  "plants": [ { "block": "minecraft:lily_pad", "chance": 0.16 }, { "block": "minecraft:seagrass", "chance": 0.10 } ],
  "bank":   [ { "block": "minecraft:sugar_cane", "chance": 0.50 } ] }
```
Difference vs the Aquatic band: **raise `extent` to ~0.72** ("near island-filling lake", per `Pond` javadoc) so the
water DOMINATES (a village over water) — the Aquatic band deliberately stayed ≤0.7 to keep a dry wooded rim for #65
trees; here we want the opposite. `slope: true` shallows the shore so the rim stays **walkable** (reachability). `depth`
stays 2 so the waterbed sits at a known-ish Y for the stilt legs.

### 3. Stilts → terrain-aware post pass (DECIDED: approach 3b — code: `PathSurfacer` + `GenerationJob`)
Add a `PathSurfacer` pass — call it **`supportStilts`** — modelled on the existing `supportTrestles`, invoked from
`GenerationJob.placeStructures` behind a new jigsaw flag (`"stilts": true`, mirroring the mineshaft `"trestles": true`).
For each house/lot floor tile over water/void, column a **`willow_log`/`cypress_log`** post **down through water** to the
first solid block (the bed), within a search depth, then `linkConnections` joins them.

> **The one change from the existing passes that makes this "terrain-aware over water":** `supportFloatingFloors` /
> `supportTrestles` stop at the first **non-air** block below — over water that is the water **surface**, so legs would
> float on top. `supportStilts` must treat a fluid as passable: descend while `state.isAir() || !state.getFluidState().isEmpty()`
> and stop only at the first solid (non-air, non-fluid) block. Post material is a `Mat` (hermetic willow/cypress log),
> not oak fence.

**Resolve FIRST (Phase-2 pre-step — the vertical-seating unknown):** houses must stand *above* the waterline, not awash.
How the jigsaw `target: minecraft:bottom` + `pad`/`sink` seat a piece over a carved `pond` is the key in-game unknown
(risks below). Two outcomes:
- **Pieces seat on the island top (above the pond):** floors are already clear of the water → `supportStilts` just fills
  the visible legs down into it. Best case, pieces unchanged.
- **Pieces seat at the water level (floors awash):** then the swamp house **pieces** also need a **baked raised floor**
  (living floor at piece y≈+2 with open legs, hermetic logs) for the vertical clearance — a hybrid of a baked floor-lift
  (clearance) + `supportStilts` (variable-depth legs below). Only author this if Phase-1's in-game read shows awash floors.

### 4. Wooden bridges → over-water plank bridges (code: extend `PathSurfacer.resolve/bridge`)
The swamp `streets`/`streets_dense` pieces already leave `PathSurfacer.MARKER`s (they are the connective marker pieces —
**confirm in Phase 3**; if they bake solid decks instead, re-author them as marker pieces). Extend the resolve pass:
today a deck tile is "over void" when `getBlockState(deck.below()).isAir()`; add an **over-water** branch — when the block
under the deck is a fluid, lay a **`willow_planks`/`cypress_planks`** deck + **`willow_fence`/`cypress_fence`** railings on
exposed sides (as `bridge()` does for void) AND drop a support **log** from each edge beam down to the bed, so the walkway
reads as trestled over the bayou rather than floating. Gate the material + water-awareness behind the same swamp
flag (or pool detection) so every other structure's bridges stay oak-over-void. This is a **code change, not new street
`.nbt`** — the pool data is largely untouched.

## Scope (files)

- **Data (Phase 1):** the three `theme_override/biomeswevegone_{hamlet,trade_post,village_center}.json` cypress bands
  (VC `shape` + all three `pond`/`surface`). No regen — pure JSON.
- **Code + regen (Phases 2–3):** `PathSurfacer` (`supportStilts` + over-water branch in `resolve`/`bridge`),
  `GenerationJob.placeStructures` (call the new pass behind the `stilts` flag), `JigsawConfig` (the `stilts` flag),
  and — only if the vertical-seating read forces it — `BwgVillageTemplates` baked raised-floor swamp house variants.
  Any piece re-authoring triggers **the 2-build regen dance** for the affected `village_swamp/*.nbt`, honouring the
  [[skyseed-structure-staging]] stale-`.nbt` trap (run clean, verify sizes; regen ONLY with the node-explicit
  `:1.21.1:runGameTestServer`).
- Only the **swamp** style changes; the other five styles' bands/pieces are untouched.

## Inert without BWG (unchanged guarantee)

The cypress bands are keyed to `biomeswevegone:*` biomes (skipped without BWG); the stilt/bridge pieces are authored
with the hermetic vanilla-analog rename (willow/cypress = analog logs/planks/fences), so nothing here needs BWG on the
classpath and CI stays BWG-free.

## Phasing

1. **Shape + water** (data-only, no regen): de-cluster the village-center cypress band (its own single-island `shape`);
   add the #64 `pond` + `mud` surface to all three cypress bands (layers 1–2 above have the exact JSON). Verify in-game
   the island reads as shallow bayou water and stays walkable — **and specifically read the vertical seating**: do the
   jigsaw pieces land above the waterline or awash? That answer decides layer 3 (posts-only vs baked-raised-floor+posts).
2. **Stilts** (approach 3b, terrain-aware): add `PathSurfacer.supportStilts` (descend through fluid to the bed) + the
   `stilts` jigsaw flag + the `GenerationJob` call; author baked raised-floor swamp house variants ONLY if step 1 read
   awash floors; regen if pieces change; verify houses stand over water.
3. **Wooden bridges**: extend `PathSurfacer.resolve`/`bridge` with the over-water branch (willow/cypress planks +
   fence railings + a log leg to the bed); confirm the swamp street pieces leave MARKERs (re-author if not); verify the
   network spans the water.
4. **Sign-off + retire** into the CHANGELOG (gametest that the cypress bands still wire + assemble on all three tiers,
   both nodes; the real BWG-block/over-water read is in-game only, as with the rest of BWGVILLAGEPLAN).

Per step: `mod_version` bump + per-node CHANGELOG entry + both-node build (standing rule).

## Risks / unknowns

- **Water depth vs. stilt height.** If the swamp water flat isn't a fixed Y, fixed-height stilts float or drown →
  favour approach 3(b) (terrain-aware posts) or pin the water to a known depth in the shape.
- **Jigsaw over water.** Pieces place at `target: minecraft:bottom`; confirm `sink`/`pad` behave over a water surface
  (the pieces should rest on the stilts/bed, not flood). May need a swamp-specific `sink`.
- **Bridge-over-water vs over-void.** `PathSurfacer` was built for void bridges; adapting it to stop at/over a water
  surface (and to lay planks, not stone) is the main code unknown.
- **Regen blast radius.** Re-authoring the swamp pieces regenerates many `village_swamp/*.nbt` — the staging trap and
  the node-explicit regen rule apply (a mixed-DataVersion regen corrupts `.nbt`).

## Decisions (resolved 2026-07-02)

- **Water lever → reuse the #64 `pond`** (broad shallow swamp), `extent` raised to ~0.72 so water dominates. *(Was: pond
  vs. wet palette.)*
- **Stilts → terrain-aware post pass (3b)**, `PathSurfacer.supportStilts` descending through fluid to the bed. *(Was:
  3a baked vs. 3b terrain-aware.)*
- **Village-center → one bigger single island** (radius ~15–17, no `cluster_offsets`), keeping the dense net + 4–6 shops.
  *(Was: bigger single island vs. tighter island.)*

## Still to settle in-game (Phase 1 read)

- **Vertical seating:** do jigsaw pieces land above the waterline or awash over a carved `pond`? Awash ⇒ layer 3 also
  needs baked raised-floor house variants (not posts alone). This is the one thing that can't be decided from the code.
- **Exact `extent` / VC radius / shore walkability:** tune from the first throw.
