# BWG Villages → Growable Skyseed Islands — Build Plan

> **Owner of PLANOFPLANS items #13 (village↔biome mapping, ✅ done) and #14 (resurrect the 6 BWG village styles).**
> A child of [STRUCTUREPLAN.md](STRUCTUREPLAN.md) (full-scope decision, 2026-07-01) and a sibling of the BWG
> wood/flower [BWGPLAN.md](BWGPLAN.md). **Scope of THIS plan: villages only** — the aspen manor (#26) and bog
> trial (#27) are separate follow-on items and are *not* covered here.
>
> **Status: PHASES 0–4 SHIPPED (v0.176.0–v0.179.0) + follow-up fixes (v0.180.0–v0.181.0).** All six styles
> across all three tiers (Hamlet / Trade Post / Village Center), a variety pass, the shrine-hall door fix,
> and the four missing profession shops (armorer/cleric/weaponsmith/leatherworker — all 13 professions now
> obtainable). Both nodes green. **Only Phase 5 (in-game sign-off) is open** — see [Phasing](#phasing).

## Goal

Throw a **hamlet / trade-post / village-center** seed over a BWG village biome and grow that biome's BWG-styled
village — six visually-distinct village styles across all three village tiers, built as *our own* jigsaw set
**inspired by** BWG's villages (their block palettes and building vocabulary), reusing Skyseed's existing
vanilla-village jigsaw mechanics. Inert without BWG installed (unknown biomes are skipped; the BWG-block `.nbt`
are only ever loaded when a village assembles over a BWG biome, which cannot happen without BWG).

## Research — the six styles (harvested from `Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar`)

### Item #13 RESOLVED — village style ↔ biome mapping

Read straight from `data/biomeswevegone/tags/worldgen/biome/has_structure/village_<style>.json`:

| Style | BWG biome(s) | Notes |
|---|---|---|
| **forgotten** | `forgotten_forest` | (the two flagged unknowns are resolved) |
| **pumpkin_patch** | `cika_woods`, `pumpkin_valley` | |
| **red_rock** | `red_rock_valley` | |
| **salem** | `weeping_witch_forest` | salem ⇒ weeping-witch, confirmed |
| **skyris** | `skyris_vale` | |
| **swamp** | `cypress_swamplands`, `cypress_wetlands` | |

Several of these biomes are *already* adapted by shipped wood/flower bands on **other** theme families
(`forgotten_forest`→florus, `cika_woods`→cika, `cypress_swamplands`→cypress, `skyris_vale`→skyris). No conflict:
those bands live on the `forest`/`aquatic`/`lush` themes; the village bands live on the `hamlet`/`trade_post`/
`village_center` themes. Same "same biome, different seed = different island" Q2 principle.

### The block palettes + aesthetic per style

The acceptance criteria for the Phase-5 per-style in-game sign-off — what each style must read as. Harvested by
parsing every `.nbt` palette in each BWG style (block-usage histogram; ids are **real BWG 2.6.0 block ids**):

- **forgotten** — *overgrown ruin reclaimed by nature.* `mossy_stone_bricks` (+ wall/slab/stairs) walls, `rocky_stone`/
  `gravel`/`tuff` footing, `florus_*` wood accents (`florus_fence`/`_trapdoor`/`_slab`/`_stairs`/`_bookshelf`/
  `stripped_florus_wood`), lime/green/yellow `terracotta`, `moss_block`/`moss_carpet`, `white_stained_glass_pane`.
  Decoration: `vine`, `cave_vines`, `poison_ivy`, `cobweb`, `tall_grass`, `cattail_thatch`, oxidised/weathered
  `cut_copper` trim.
- **pumpkin_patch** — *autumnal dark-oak & dacite, pumpkins everywhere.* `dark_oak_planks` walls, `dark_oak_log`/
  `stripped_dark_oak_log` posts, `dark_oak_stairs`/`_slab` roof, `dark_oak_door`, `dacite_cobblestone` (+`dacite_bricks`/
  `dacite_pillar`) footing, `dark_oak_fence`, `orange_terracotta` accent, `lush_dirt_path` streets. Decoration:
  `pumpkin`/`carved_pumpkin`/`pumpkin_stem`, `pumpkin_burrow`, `lantern`, `candle`, `bell`, `bookshelf`.
- **red_rock** — *southwestern adobe pueblo with thatch roofs.* `red_rock_bricks` (+`chiseled_`/`cracked_`) walls,
  `red_rock`/`red_sandstone`/`red_sand` footing, `baobab_log`/`_planks`/`_fence`/`_stairs`/`_slab`/`_trapdoor` +
  `palo_verde_wood` timber, **`cattail_thatch` roofs** (`_stairs`/`_slab`), `acacia_fence`, `orange_terracotta`/
  `brown_glazed_terracotta` accent. Decoration: `blooming_aloe_vera`, `palo_verde_leaves`/`flowering_palo_verde_leaves`,
  `cattail`, `torch`, `candle`, wheat farms.
- **salem** — *colonial / gothic New-England.* `witch_hazel_planks`+`oak_planks` walls, `witch_hazel_log`/
  `stripped_oak_log` posts, `witch_hazel_stairs`/`_slab` roof, `witch_hazel_door`, `stone`/`cobblestone`/`mossy_stone`/
  `andesite` footing, **`gray_stained_glass_pane`** windows (signature), `witch_hazel_fence`, `witch_hazel_leaves`/
  `blooming_witch_hazel_leaves`, `peat`, `coarse_dirt`. Decoration: `pink_anemone`, `white_anemone`, `lantern`, wheat.
- **skyris** — *elegant elevated masonry temple-town.* `skyris_planks` + `white_dacite_bricks` walls, `skyris_log`/
  `stripped_skyris_log`/`white_dacite_pillar` posts, `skyris_stairs`/`dark_prismarine_stairs` roofs, `skyris_door`,
  `polished_andesite`/`white_dacite`/`white_dacite_cobblestone` footing, **`light_blue_stained_glass_pane`** windows
  (signature), `skyris_fence`, `dark_prismarine`/`white_wool`/`white_dacite_tiles` accent. Decoration: `skyris_vine`,
  `white_wool`, `podzol`, `lantern`.
- **swamp** — *stilted bayou on willow & cypress.* `willow_planks`+`cypress_planks` walls, `willow_log`/
  `stripped_willow_wood`/`cypress_log` posts (and **stilts over water**), `willow_stairs`/`cattail_thatch` roofs,
  `willow_door`/`cypress_door`, `tuff`/`mud`/`clay` footing, `willow_fence`/`cypress_fence`, `cattail_thatch`/`peat`/
  green+cyan `terracotta` accent. Decoration: `green_mushroom_block`/`wood_blewit_mushroom_block`/
  `weeping_milkcap_mushroom_block`/red+brown mushroom blocks, `cattail`, `seagrass`, `podzol`, `water` channels.

## Architecture — ✅ shipped: hermetic string-id blocks (route B)

BWG is **not** a build dependency (it would break the 26.1.2 node), so the `.nbt` are authored with **no BWG on
any classpath** via the "vanilla-analog rename": each BWG material is authored as a vanilla analog block (so all
shape math, adjacency and property serialisation run as today — BWG blocks subclass the vanilla
`StairBlock`/`SlabBlock`/`FenceBlock`/`DoorBlock`), and at write time the palette entry's `Name` is substituted
with the BWG id. As shipped (v0.176.0): `BwgVillageTemplates.Mat` (record of analog + id; `Mat.v`/`Mat.bwg`), a
`modNames` side-map passed to `StructureWriter.write(blocks, Map<BlockPos,String> modNames, …)`, and palette
dedup keyed on `PaletteKey(state, modName)` so two BWG blocks sharing an analog don't collapse and a mod block
never collides with a literally-used vanilla block. CI never needs BWG.

## Datapack wiring (mirrors the existing biome-styled trade post)

What exists per style `<s>` (six styles): pool dir `data/skyseed/worldgen/template_pool/village_<s>/`
(`{start,start_dense,hamlet_start,streets,streets_dense,lots,large_lots,fillers,fillers_void}.json` — 9 pools)
plus 36 committed `.nbt` under `data/skyseed/structure/village_<s>/`; three `theme_override` files
(`data/skyseed/skyseed/theme_override/biomeswevegone_{hamlet,trade_post,village_center}.json`, 6 bands each,
inert without BWG — one per village tier, *not* the ×3 base/large/huge multiplier, because the village tiers ARE
the progression). Bands **prepend** (win first-match over the base catch-alls). Auto debug seeds cover the bands
via `ThemeScanner`.

**Operational traps (apply whenever village `.nbt` are regenerated — last done v0.180.0 for `shrine_hall`):**
the piece `.nbt` are dev-generated (`writeIfAbsent`) and committed, which needs the **2-build regen dance**, and
the [[skyseed-structure-staging]] stale-`.nbt` trap applies — a stale Stonecutter node copy wins
`processResources` on incremental builds, so run clean and verify byte sizes after regenerating.

## Phasing

Phases 0–4 shipped (each with `mod_version` bump + per-node CHANGELOG entry + gametests, both nodes):

- **Phase 0 — Research.** ✅ (this doc: biome map + real block palettes + building vocabulary).
- **Phase 1 — Engine + Skyris pilot at trade_post tier.** ✅ v0.176.0 (`bwg_skyris_village_band_on_trade_post`, `bwg_skyris_village_assembles`).
- **Phase 2 — All six styles, core building set, trade_post tier.** ✅ v0.177.0 (`bwg_all_village_bands_wired_on_trade_post`, `bwg_village_styles_assemble`).
- **Phase 3 — Hamlet + Village-Center tiers.** ✅ v0.178.0 (`bwg_hamlet_and_center_bands_wired`, `bwg_hamlet_and_center_assemble`; added the per-style `hamlet_start` pool).
- **Phase 4 — Variety pass.** ✅ v0.179.0 (HIP roof, porch cottage + longhouse, animal-pen / market-stall / grove plots; `bwg_village_decor_variety`). Follow-up fixes: v0.180.0 (shrine-hall freestanding door removed), v0.181.0 (armorer/cleric/weaponsmith/leatherworker shops — 12 `shop_*.nbt`, all 13 professions obtainable; loft-ladder + smithy fixes; `village_offers_every_profession`).
- **Phase 5 — Verify + document. ← THE OPEN PHASE (in-game, needs BWG installed):**
  1. **Biome-reachability check** for the four new-to-us biomes: `pumpkin_valley`, `red_rock_valley`,
     `weeping_witch_forest`, `cypress_wetlands` (confirm via F3/Debug tab that TerraBlender actually places each
     in the void multi-noise overworld). If one never places, re-key that style's bands to a reachable sibling
     biome (cf. the BWGPLAN #66 `pale_bog` precedent).
  2. **Throw-a-seed assembly sign-off per style** (all 6 styles × the tiers you care about): real BWG blocks
     render, roofs/fences/doors correct — this is also the only place the **BWG property-serialisation risk**
     is finally proven (gametests run without BWG on the classpath, so they can't check the real blocks).
     Includes the standing **swamp read**: if the swamp style reads flat/dry, decide whether to add the deferred
     water channels/stilts.
  3. Retire this plan into the CHANGELOG once signed off.

**Possible later polish (not committed):** per-*building* signature decor — pumpkins *inside* pumpkin houses,
wall vines, etc. (v0.179.0's decor is plot-level: grove/market-stall/animal-pen in the `fillers` pool.)

## Risks / unknowns (live until Phase 5 closes)

- **Biome reachability in the void multi-noise overworld.** `cika_woods`/`forgotten_forest`/`cypress_swamplands`/
  `skyris_vale` are confirmed reachable (shipped wood islands were thrown over them). `pumpkin_valley`,
  `red_rock_valley`, `weeping_witch_forest`, `cypress_wetlands` are **unverified** → Phase 5 step 1.
- **BWG block property serialisation.** The rename trick assumes BWG blocks share the vanilla property schema of
  their analog (true for stairs/slabs/fences/doors/logs). A few odd ones (`cattail_thatch_stairs`,
  `dacite_pillar`, `pumpkin_burrow`, mushroom blocks) are only provable in-game → Phase 5 step 2;
  default-state (no Properties) is the safe fallback.
- **Swamp water.** The swamp style wants channels/stilts over water; shipped templates are dry-pad + thatch
  (water only in field/well/animal-pen basins) → decide at the Phase 5 swamp read.
- **Vertical / bounding-box.** NOT a risk here — villages are street-network (flat) jigsaws; the vertical-jigsaw
  risk lives with the manor & trial (#26–#28 in STRUCTUREPLAN).

## Out of scope (tracked elsewhere)

- Aspen **manor** (#26) and **bog trial** (#27) — separate structures, separate plan items (vertical-jigsaw risk).
- Prairie houses / rugged fossil (#49) — optional polish.
- Net-new bespoke structures beyond BWG's 17 (#68) — long tail.
