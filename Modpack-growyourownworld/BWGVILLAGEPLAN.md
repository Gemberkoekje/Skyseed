# BWG Villages → Growable Skyseed Islands — Build Plan

> **Owner of PLANOFPLANS items #13 (village↔biome mapping) and #14 (resurrect the 6 BWG village styles).**
> A child of [STRUCTUREPLAN.md](STRUCTUREPLAN.md) (full-scope decision, 2026-07-01) and a sibling of the BWG
> wood/flower [BWGPLAN.md](BWGPLAN.md). **Scope of THIS plan: villages only** — the aspen manor (#26) and bog
> trial (#27) are separate follow-on items and are *not* covered here.
>
> **Status: PHASES 1–4 SHIPPED (2026-07-01, v0.176–0.179).** Engine + **all six styles across all three tiers** (Hamlet
> / Trade Post / Village Center) + a variety pass (more houses/roofs + new decoration plots), both nodes green. Only
> Phase 5 (in-game sign-off) open. See [Phasing](#phasing).

## Goal

Throw a **hamlet / trade-post / village-center** seed over a BWG village biome and grow that biome's BWG-styled
village — six new visually-distinct village styles across all three village tiers, built as *our own* jigsaw set
**inspired by** BWG's villages (their block palettes and building vocabulary), reusing Skyseed's existing
vanilla-village jigsaw mechanics (`TradePostTemplates` palette system, `PathSurfacer` streets, the shop cap, bed→
villager spawning). **Variety is a first-class requirement** — more house and decoration shapes than the current
one-shell-recolour set.

Inert without BWG installed (unknown biomes are skipped; the BWG-block `.nbt` are only ever loaded when a village
assembles over a BWG biome, which cannot happen without BWG) — the same guarantee the shipped wood/flower bands give.

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

Each BWG `village` structure is `size 6`, `start_pool …/town_centers` — a full vanilla-style street jigsaw. Note
several of these biomes are *already* adapted by shipped wood/flower bands on **other** theme families
(`forgotten_forest`→florus, `cika_woods`→cika, `cypress_swamplands`→cypress, `skyris_vale`→skyris). No conflict:
those bands live on the `forest`/`aquatic`/`lush` themes; the village bands live on the `hamlet`/`trade_post`/
`village_center` themes. Same "same biome, different seed = different island" Q2 principle.

### The block palettes + aesthetic per style

Harvested by parsing every `.nbt` palette in each style (block-usage histogram; ids are therefore **real BWG 2.6.0
block ids**, not guesses). Target mapping onto Skyseed's `Palette` slots (wall / post / stairs+slab / door /
foundation / glass / fence / accent / **roof**) plus the signature decoration blocks:

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

### BWG's building vocabulary (what "don't cheap out on variety" looks like)

Each style ships **35–47 templates**: 3–4 `meeting_point` town centres, **6–8 `small_house` variants**, a broad
profession set (`butcher`, `cartographer`, `tool_smith`/`toolsmith`, `armorer`/`armorsmith`, `weaponsmith`, `mason`,
`shepherd`, `fletcher`, `fisher`, `forager`, `cleric`, `library`, `tannery`), `small`/`large_farm`, 2–3 `animal_pen`,
1–2 `temple`, `accessory`/`lamp` decor, and full `straight`/`corner`/`crossroad`/`turn`/`square` street + `terminator`
pools. **Our set must grow toward this** — several distinct house shapes and decorations per style, not one recoloured
shell.

## Architecture decision — hermetic string-id blocks (RECOMMENDED)

The problem: BWG blocks (`biomeswevegone:witch_hazel_planks`, …) are **not** available at compile time, and BWG is
**not** a build dependency (verified: `build.gradle` has no BWG/TerraBlender/Architectury; it's 1.21.1-only and would
break the 26.1.2 dev node). Our `.nbt` are authored **in code** at dev time by `DevStructureGenerator` via
`StructureWriter`, which today needs real `BlockState`s (`NbtUtils.writeBlockState`).

Two routes:

- **(A) BWG as a dev-runtime dependency** — resolve BWG ids → `BlockState` via the registry, reuse the whole existing
  `BlockState` path unchanged. ❌ Rejected: couples the build to external mods (BWG + TerraBlender + Architectury),
  breaks the 26.1.2 node (BWG is 1.21.1-only), and diverges from how every other BWG integration in this repo works.
- **(B) Hermetic string-id blocks** — write the palette compound (`{Name:"biomeswevegone:…", Properties:{…}}`)
  **directly**, no BWG on any classpath. ✅ **Chosen.** Consistent with the shipped wood/flower bands (pure string
  ids, inert-without-BWG, version-agnostic). The `.nbt` are generated on any machine/node; CI never needs BWG.

**Implementation of (B) — the "vanilla-analog rename":** author each BWG building with a **vanilla analog** block for
each material (e.g. `witch_hazel_planks`→`oak_planks`, `red_rock_stairs`→a `StairBlock`), so all the shape math,
adjacency (fence/stair connection) and **property serialisation** run exactly as today (BWG blocks subclass the vanilla
`StairBlock`/`SlabBlock`/`FenceBlock`/`DoorBlock`/… so the property *names* match). At write time the palette entry's
`Name` is substituted with the BWG id. Dedup keys on `(analogState, bwgId)` so two BWG blocks sharing an analog don't
collapse, and a mod block never collides with a literally-used vanilla block (salem uses *both* `witch_hazel_planks`
and real `oak_planks`).

Concretely:

1. **`BlockSpec`** — `Vanilla(BlockState)` | `Mod(BlockState analog, String id)`; `of(state)` / `mod(id, analog)`.
2. **`StructureWriter` spec overload** — palette by `BlockSpec`: `Vanilla`→`NbtUtils.writeBlockState`; `Mod`→ same,
   then set `Name` to `id`. Keys the palette on the spec, not the raw state.
3. **A BlockSpec-aware builder** (`BwgVillageTemplates`, with BlockSpec variants of the `gableRoof`/`linkFences`/`conn`/
   `anchor` helpers) so BWG buildings can use their own shapes and place BWG blocks in **roofs/fences** too (a plain
   analog-recolour of `StructureParts` would emit oak roofs on a witch-hazel house). The existing vanilla
   `TradePostTemplates` stays untouched.

> **Open decision for the user:** (B) is the recommendation. If you'd rather keep the code change small and accept a
> BWG dev-runtime dependency (A), say so — but (A)'s cross-version breakage makes (B) the clear default.

## Datapack wiring (mirrors the existing biome-styled trade post)

For each BWG style `<s>` and each village tier, a per-style **piece set** + **pools**, attached via a `theme_override`
band that points its jigsaw at that pool set — exactly how `trade_post.json` already swaps in `trade_post_desert` /
`_savanna` / `_spruce` per vanilla biome.

- **Pools** (generated per palette by `TradePostTemplates.generateInto`-style code, one dir per style):
  `data/skyseed/worldgen/template_pool/village_<s>/{start,start_dense,streets,streets_dense,lots,large_lots,fillers,
  fillers_void}.json` (+ any style-specific pools).
- **Piece `.nbt`**: `data/skyseed/structure/village_<s>/*.nbt`, dev-generated (writeIfAbsent), committed. Follows the
  **2-build regen dance** and the [[skyseed-structure-staging]] stale-`.nbt` trap rules.
- **Theme overrides** (3 files, inert without BWG — one per tier, *not* the ×3 base/large/huge multiplier the wood
  families need, because the village tiers ARE the progression):
  - `theme_override/biomeswevegone_hamlet.json` → 6 bands, each `{biomes:[…], surface:…, jigsaw:{pool:"skyseed:village_<s>/start", …cap…}, variants:[… BWG ground/decoration …]}`
  - `theme_override/biomeswevegone_trade_post.json` → 6 bands (depth 4, cap 2–4).
  - `theme_override/biomeswevegone_village_center.json` → 6 bands (`start_dense`, depth 6, cap 4–6, iron golem, anvil centerpiece).
  Bands **prepend** (win first-match over the base `#is_*`/vanilla catch-alls) via `ThemeOverride.Patch.mergeBands` —
  BWG biomes sit under vanilla catch-alls transitively, so an appended band would be shadowed (the lesson from the
  wood bands).
- **Debug seeds + lang**: auto debug seeds already cover `theme_override` bands (`ThemeScanner`) — each BWG village
  biome will appear in the Debug tab; add lang/model entries as the existing debug seeds need.

## Phasing

Each phase is its own commit(s): `mod_version` minor bump + per-node CHANGELOG entry + an inert golden-master/gametest,
both nodes compiling (the standing rules).

- **Phase 0 — Research.** ✅ DONE (this doc: biome map + real block palettes + building vocabulary).
- **Phase 1 — Engine + one style end-to-end.** `BlockSpec` + `StructureWriter` spec overload + BlockSpec builder
  helpers; prove **one** style (propose **salem** or **skyris** — clean, non-water) at the **trade_post** tier:
  town square + a couple of house shapes + shops + a field, its pools, one `theme_override` band, an assembly gametest
  (on a flat pad, BWG-id palette) + the inert golden-master. Locks the pattern.
- **Phase 2 — All six styles, core building set, trade_post tier.** ✅ DONE (v0.177.0). Each style is a `Style`
  palette (8 pools + 27 templates) reusing the shared building set (8 shops, cottage + tower homes, forge + shrine-hall
  landmarks, fields/garden/well/terminator/pier), plus a per-style `bookshelf` + signature `flora`; six
  `theme_override` trade_post bands (BWG trees + flora outdoors). Both nodes green (`bwg_all_village_bands_wired_on_trade_post`
  + `bwg_village_styles_assemble`). Red_rock/swamp use cattail-thatch roofs; forgotten/pumpkin_patch are largely vanilla
  blocks + a few BWG accents (the engine mixes per cell).
- **Phase 3 — Hamlet + Village-Center tiers.** ✅ DONE (v0.178.0). Datapack-only (the per-style `hamlet_hub` +
  `square_dense` already existed): a per-style `hamlet_start` pool + two `theme_override` files
  (`biomeswevegone_hamlet.json` / `biomeswevegone_village_center.json`, 6 bands each) wire the Hamlet tier (cap 1–2) and
  the Village-Center cluster tier (start_dense, cap 4–6, iron golem, anvil centerpiece). Both nodes green
  (`bwg_hamlet_and_center_bands_wired` + `bwg_hamlet_and_center_assemble`). No new `.nbt`.
- **Phase 4 — Variety pass ("don't cheap out").** ✅ DONE (v0.179.0). Palette-driven, so every style gains: a **HIP**
  roof (on fisherman/butcher) beside gable/flat/stepped; **two new house shapes** (a porch cottage + a 7×5 longhouse);
  and **three new decoration plots** — an **animal pen**, a **market stall**, and a **grove** (a dense bed of the
  style's signature `flora`: pumpkins / mushrooms / aloe / anemones / lily …), all in the surplus `fillers` pool. Both
  nodes green (`bwg_village_decor_variety` checks the fillers pool registers them — deterministic, not a noisy
  assembled-count). Further per-*building* signature decor (pumpkins *inside* pumpkin houses, wall vines, …) is a
  possible later polish.
- **Phase 5 — Verify + document.** Gametests green both nodes; **in-game** biome-reachability check for the four
  new-to-us biomes (`pumpkin_valley`, `red_rock_valley`, `weeping_witch_forest`, `cypress_wetlands`) + a
  throw-a-seed assembly sign-off per style; CHANGELOG/README; retire this plan's checklist into the CHANGELOG.

## Risks / unknowns

- **Biome reachability in the void multi-noise overworld.** `cika_woods`/`forgotten_forest`/`cypress_swamplands`/
  `skyris_vale` are confirmed reachable (shipped wood islands were thrown over them). `pumpkin_valley`,
  `red_rock_valley`, `weeping_witch_forest`, `cypress_wetlands` are **unverified** — if TerraBlender doesn't place one,
  that style is de-facto ungrowable and must be re-keyed to a reachable sibling biome (cf. BWG #66 spirit/`pale_bog`).
- **BWG block property serialisation.** The rename trick assumes BWG blocks share the vanilla property schema of their
  analog (true for stairs/slabs/fences/doors/logs). Verify a few odd ones (`cattail_thatch_stairs`, `dacite_pillar`,
  `pumpkin_burrow`, mushroom blocks) have the properties we serialise; default-state (no Properties) is the safe
  fallback.
- **Door/accent id gaps.** A few slot picks need an existence check against the jar (`florus_door`?, `baobab_door`?,
  `witch_hazel_door` ✓ seen, `cypress_door` ✓ seen, `skyris_door` ✓ seen). Fall back to a vanilla door or a
  trapdoor-shutter where a wood lacks a door.
- **Vertical / bounding-box.** Villages are street-network (flat) jigsaws — *not* the vertical-jigsaw bounding-box risk
  that bit the End City / that #28 tracks for the manor & trial. Standard `pad`/`reach` math (pad ≥ assembly extent,
  radius ≥ pad/0.75) from the existing trade post applies.
- **Swamp water.** The swamp style wants channels/stilts over water; on a floating island that's the pond/`PathSurfacer`
  bridge interaction — keep the first swamp pass dry-pad + thatch and add water in Phase 4 if it reads flat.

## Out of scope (tracked elsewhere)

- Aspen **manor** (#26) and **bog trial** (#27) — separate structures, separate plan items (vertical-jigsaw risk).
- Prairie houses / rugged fossil (#49) — optional polish.
- Net-new bespoke structures beyond BWG's 17 (#68) — long tail.
