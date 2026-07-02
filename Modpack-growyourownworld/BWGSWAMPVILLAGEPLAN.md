# BWGSWAMPVILLAGEPLAN — Cypress swampland villages as a stilted overwater bayou

Child of [BWGVILLAGEPLAN.md](BWGVILLAGEPLAN.md); PLANOFPLANS **#73**. This is the resolution of that plan's deferred
**Phase-5 "swamp read"** decision — the in-game sign-off (2026-07-02) confirmed the cypress villages assemble but read
as a *dry* pad, and the call is now made: build the intended **stilted bayou**. Grounded in a 2026-07-02 codebase pass.

> **Status: PLANNED.** The other five BWG village styles are done and stay as-is; this reworks the **swamp/cypress**
> style only, across all three tiers.

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
- **village_center:** give the cypress band its **own `shape`** (single island, e.g. `radius 12–15`, teardrop, no
  `cluster_offsets`) so it stops inheriting the cluster.
- **trade_post / hamlet:** already single — only the water/surface treatment (below) changes.

### 2. Water surface → a broad shallow swamp (data + the #64 mechanism)
Make the island top a shallow water flat with scattered mud/clay/peat shoals, so houses sit *over* water. This is the
**same water treatment as [BWGPLAN #64](BWGPLAN.md)** ("broad shallow swamp/marsh instead of the deep round pond") —
build/borrow that lever rather than a one-off. Candidate levers (decide in Phase 1): a wide shallow `pond` covering most
of the island vs. a wet palette (surface = water with a mud/`tuff`/`clay` bed a block below). Keep the shore walkable so
the village is reachable.

### 3. Stilts → swamp house pieces gain legs (code: `BwgVillageTemplates` + regen)
Author swamp-specific house variants whose floor sits a few blocks **above** the waterbed on `willow_log`/`cypress_log`
stilt posts (bake the legs into the `.nbt`, hermetic-Mat as usual). Two open approaches (Phase 2 decision):
- **(a) Fixed-height stilts baked into each piece** — simplest, works if the water flat is at a known depth.
- **(b) A terrain-aware stilt pass** (post columns down to the first solid block, like `PathSurfacer` railing over
  void) — robust to varying depth, more work. See the `PathSurfacer` / over-void bridge lever from SKYJIGSAWPLAN.

### 4. Wooden bridges → the swamp street pool (code/data: `village_swamp/streets*` + regen)
Replace the swamp style's dirt/grass street pieces with **willow/cypress plank bridges with fences as railings**, over
the water. Reuse the existing over-void self-railing bridge mechanism (`PathSurfacer`, v0.68.0 — SKYJIGSAWPLAN) adapted
to bridge over *water* rather than void. The `village_swamp/{streets,streets_dense}` pools point at the new bridge pieces.

## Scope (files)

- **Data:** the three `theme_override/biomeswevegone_{hamlet,trade_post,village_center}.json` cypress bands (shape +
  water); the `data/skyseed/worldgen/template_pool/village_swamp/{streets,streets_dense,lots,large_lots,…}` pools
  (point at the new bridge/stilt pieces).
- **Code + regen:** `BwgVillageTemplates` swamp house/street authoring (stilts + bridges) → **the 2-build regen dance**
  for the affected `village_swamp/*.nbt`, honouring the [[skyseed-structure-staging]] stale-`.nbt` trap (run clean,
  verify byte sizes; regen ONLY with the node-explicit `:1.21.1:runServer`).
- Only the **swamp** style changes; the other five styles' bands/pieces are untouched.

## Inert without BWG (unchanged guarantee)

The cypress bands are keyed to `biomeswevegone:*` biomes (skipped without BWG); the stilt/bridge pieces are authored
with the hermetic vanilla-analog rename (willow/cypress = analog logs/planks/fences), so nothing here needs BWG on the
classpath and CI stays BWG-free.

## Phasing

1. **Shape + water** (data-only): de-cluster village-center cypress band; give all three a single swamp-water island.
   Verify in-game the island reads as shallow bayou water and stays walkable. Coordinate with #64.
2. **Stilts** (pick approach a/b): author stilted swamp house pieces; regen; verify houses stand over water.
3. **Wooden bridges**: swamp street pool → plank bridges with railings; regen; verify the network spans the water.
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

## Open decisions

- **Water lever:** wide shallow `pond` vs. wet palette (surface=water + bed below) — settle in Phase 1 alongside #64.
- **Stilts:** fixed-height baked (3a) vs. terrain-aware post pass (3b).
- **Village-center single-island size:** keep the ~cluster footprint as one bigger island, or a tighter island (fewer
  shops spread over water)?
