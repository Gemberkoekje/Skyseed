# Oh The Biomes We've Gone + OTYG + Create-OTBWG — Integration Plan

## Status — PR #14 (`feature/bwg-wood-islands`)

**Done & shipped in this changeset:**
- **Step 1 (the core) — DONE for the forest family.** First-party `theme_override` compat ships **11 forest-family woods** on `forest` / `forest_large` / `huge_forest`: aspen, baobab, cika, jacaranda, maple, ebony, redwood, zelkova, witch-hazel, sakura, ironwood. Inert-without-BWG, gametest-guarded.
- **Engine fix that made it work:** `theme_override` biome bands now **prepend** (win the first-match over the base `#is_*` catch-alls) — BWG biomes are transitively under `#is_forest` via `#biomeswevegone:forest`, so an appended band was silently shadowed.
- **Auto debug seeds now cover `theme_override` bands** (runtime `ThemeScanner` + the build-time model generator), so the BWG biomes show in the Skyseed Debug tab.
- **Forest density** raised on large/huge for the canonical forests (40 / 120 tries); open biomes kept scattered.
- **Water features:** deep lakes rounded (`pond.slope`); the Huge Forest rolls **25% lake / 25% river / 50% dry** (`pond.chance` + `pond.river`); **rivers walled + bank-softened** (the long-planned river-to-rim follow-up).
- **CI now runs the gametests** on every node. **Q1 RESOLVED** (tight first pass → scaled to the forest family). **Q3 RESOLVED** (rewritten below). Modpack: BWG/OTYG/create-otbwg + Better Clouds jars in, `mods.txt` refreshed.

**Left for a future return (NOT in this changeset):**
- **Q2 — concentrate vs distribute (UNDECIDED).** This gates the rest of the 25 planks: the **wet woods** (cypress/willow/white-mangrove/palm → aquatic family) and **fantasy woods** (enchanted/skyris/spirit) are **not yet added**.
- **Density follow-up:** lift the *held* wet/semi forest biomes (mangrove/swamp/riverside, cherry/grove/mushroom/bamboo/flower) to the agreed level — held pending the in-game density read, now confirmed good.
- **Step 2 (OTYG verification), Step 3 (create-otbwg verification), Step 4 (Patchouli "Exotic Biomes" entry), Step 5 (the light quest branch)** — all still to do.
- **STRUCTUREPLAN** (BWG village/manor/trial resurrection) — its own later child changeset.

## The three mods

| Mod | What it is | Skyblock reality |
|---|---|---|
| **Oh The Biomes We've Gone** (`biomeswevegone` 2.6.0) | **55 biomes**, **25 wood types** (aspen…zelkova), 88 saplings, a large flower/plant/block/mob catalog. Biomes injected via **TerraBlender**. | Biomes **are** in the void's biome layout (multi-noise overworld preset); terrain + features are suppressed by `skip_decoration: true`. Content reaches the player only through Skyseed islands. |
| **Oh The Trees You'll Grow** (`ohthetreesyoullgrow` 5.3.2) | A sapling-growth **framework** — saplings grow into larger, varied tree structures. Ships only `test` trees; **no items**. | Worldgen suppressed in the void; the *sapling-growth* behaviour is the value, and it works on islands / in Botany Pots. |
| **create-otbwg-compat** (1.0) | 94 entries, all `data/create/recipe/milling/*` — Create **milling** recipes turning BWG flowers/plants into petals / dyes / powders. | Pure recipe datapack. "Just works" once the BWG flowers are obtainable. |

Dependencies already present: **TerraBlender** 4.1.0.8, **Architectury** 13.0.8. No new deps.

## Why this is (mostly) a content-routing problem

Nothing generates in the void, so BWG's value — exotic woods, millable flowers, unique plants — is invisible unless an island carries it. Skyseed's theme engine is purpose-built for exactly this:

- `theme/*.json` islands carry a `biome_overrides` list; each entry matches the biome the seed is thrown over (by id or `#tag`) and supplies surface / `variants` / `trees` (configured-**features**) / `ground` plants / mobs.
- The void overworld keeps the **multi-noise overworld biome source** (verified in `world_preset/skyblock.json`), so **BWG biomes occupy the biome map** — you can stand in the void over `biomeswevegone:cika_woods`.
- Unknown ids in a theme are silently skipped (the version-inert `pale_garden` override proves this) → BWG entries are **byte-identical-inert without BWG installed**.
- `ThemeOverride` patches **append** `biome_overrides` (merge-by-selector, per `ThemeOverride.Patch.applyTo`) to a base theme — so BWG support ships as a first-party, opt-in compat datapack, exactly like the existing `mysticalagriculture_*` / `create_*` overrides.

**Net design: throw a Forest seed over a BWG biome → grow a BWG-wood island.** This extends the existing rare-seed mechanic (forest-over-badlands) to 55 new biomes and turns BWG's biome map into a treasure map.

## Step 1 — First-party BWG theme_override compat (Skyseed mod) ← the core  ✅ DONE (forest family; wet/fantasy woods pending Q2)

Create `data/skyseed/skyseed/theme_override/biomeswevegone_forest.json` (+ `_forest_large`, `_huge_forest`; later `_lush`/`_meadow` for floral biomes), each appending BWG `biome_overrides` to the matching base theme. Per-entry shape:

```jsonc
{ "biomes": ["biomeswevegone:aspen_boreal"],
  "variants": [ { "weight": 1, "name": "aspen", "decoration": {
    "trees":  [ { "feature": "biomeswevegone:aspen_trees", "tries": 6, "spacing": 2 } ],
    "ground": [ { "block": "minecraft:short_grass", "chance": 0.30 },
                { "block": "biomeswevegone:<flower>", "chance": 0.05 } ] } } ] }
```

Starter wood → biome → feature map (biome names telegraph the wood; BWG ships a convenient `*_trees` aggregate feature):

| Wood | BWG biome | Tree feature |
|---|---|---|
| aspen | `aspen_boreal` | `biomeswevegone:aspen_trees` |
| baobab | `baobab_savanna` | `biomeswevegone:baobab_trees` |
| cika | `cika_woods` | `cika_trees` |
| ebony | `ebony_woods` | `ebony_trees` |
| jacaranda | `jacaranda_jungle` | `jacaranda_trees` |
| maple | `maple_taiga` | `maple_trees` |
| cypress | `cypress_swamplands` | `cypress_trees` |
| araucaria | `araucaria_savanna` | `araucaria_trees` |
| … | (55 biomes total — cover the signature wood biomes first) | |

Notes:
- **Verify** each `*_trees` configured-feature id + flower block ids against the 2.6.0 jar before use (the sample confirmed per-tree variants plus a `_trees` aggregate; confirm the aggregate exists per wood).
- Reuse the climate idioms already in `forest.json`: `surface_scatter` (podzol/coarse_dirt/sand), `snow: 1.0`, `pond` for swamplands.
- Add a **gametest** asserting inert resolution (golden-master byte-identical without BWG) — the standing rule for first-party compat.
- Bump `mod_version` (minor) + CHANGELOG entry per the standing rule.

Scope discipline: do **not** map all 55. Cover the signature **wood** biomes (so every BWG plank is reachable) + a couple of iconic floral biomes (to feed the milling compat). The rest land later.

## Step 2 — OTYG (sapling growth)

- **Verify** OTYG ships sapling→tree mappings for vanilla + BWG saplings, or whether it needs a Potion Studios tree-pack datapack (the jar carried only `test` trees). If BWG saplings don't grow under OTYG out of the box, add/enable the tree pack.
- Confirm OTYG's bigger-tree growth works **in Botany Pots** (the pack's tree-farming path) and on islands.
- `skip_decoration: true` already neutralises OTYG worldgen — no leak. Review `config/ohthetreesyoullgrow*` for growth/worldgen toggles.

## Step 3 — create-otbwg-compat (verify only)

- It only needs BWG flowers in hand. Spot-check 2–3 milling recipes resolve against BWG 2.6.0 ids.
- Ensure the BWG island `ground` lists actually place the millable flowers (allium_flower_bush, blue_rose_bush, glowcane, …) so the compat has inputs.

## Step 4 — Modpack wiring

- Curate `config/` for BWG, OTYG, create-otbwg-compat (like the MA pass). **Leave BWG biome injection ON** — it is the adaptation key.
- Regenerate `mods.txt`.
- Patchouli guide: a short **"Exotic Biomes"** entry (Forest seed over a BWG biome → exotic wood), mirroring the rare-seeds page.

## Step 5 — Quests (optional, light)

A small branch off the **Skyseed** chapter — not a new megachapter (keep it un-overwhelming):
- *Into the Wilds* — obtain any BWG plank (proof you grew an exotic island).
- *Mill the Blooms* — a create-otbwg milled output (ties BWG → Create).
- *Grow Something Grand* — an OTYG-grown tree.

## Build order

1. **Step 1 (mod)** — BWG theme_override compat + gametest + version bump → its own branch/PR.
2. **Steps 2–3** — OTYG + compat verification (mostly config) → modpack branch.
3. **Step 4** — modpack config + mods.txt + guide entry.
4. **Step 5** — quest branch (optional).

## Tier coverage — base / large / huge (don't forget the tiers)

Every seed family is **three** themes: `forest` / `forest_large` / `huge_forest`, `desert` / `desert_large` / `huge_desert`, `frozen` / `_large` / `huge_`, and so on. A `theme_override` targets exactly one theme, so each BWG adaptation set must be replicated across all three tier targets — the MA compat already does this (`mysticalagriculture_ancient` + `_ancient_large` + `_huge_ancient`).

Author the BWG `biome_overrides` content once, then emit one override file per tier target. "5 marquee woods on the forest family" therefore means the same 5 bands across `biomeswevegone_forest.json` + `_forest_large.json` + `_huge_forest.json`. **Budget the ×3 tier multiplier** when sizing any step.

## Open design questions

### Q1 — Step 1 scope (the immediate fork)  ✅ RESOLVED — tight first pass, then scaled to the whole forest family (11 woods)
All signature **wood** biomes (~15, every plank reachable) or a **tight first pass** (5–6 marquee woods: aspen, baobab, cika, jacaranda, maple) to validate the loop before scaling? (× the 3 forest tiers either way.)

### Q2 — should other typed seeds respond to BWG biomes, not just forest?  ⬜ OPEN — gates the wet/fantasy woods (the next thing to decide on return)
The forest seed is the generalist (it already adapts to desert / savanna / taiga / ocean / …). But BWG biomes span every climate, and it is more coherent to let each typed seed recognise the BWG biomes in its own family — e.g. throw a **Desert** seed over BWG's `mojave_desert` / `atacama_outback` and get the right arid island instead of leaning on the forest seed for everything. Rough mapping (confirm ids against the full 55-biome roster — only ~40 were enumerated):

| Seed family | BWG biomes (examples — confirm ids) | Signature content |
|---|---|---|
| Forest (woods) | aspen_boreal, cika_woods, ebony_woods, jacaranda_jungle, maple_taiga, black_forest, coniferous_forest, + the redwood / sakura / skyris / willow / witch_hazel / zelkova biomes in the unseen tail | the 25 wood types |
| Desert / arid | mojave_desert, atacama_outback, dead_sea | golden_spined_cactus, sages |
| Rocky / volcanic | basalt_barrera, dacite_ridges, dacite_shore | (lean rocky, not desert) |
| Frozen / cold | crimson_tundra, frosted_taiga, frosted_coniferous_forest, eroded_borealis, howling_peaks, canadian_shield | |
| Aquatic / wet | bayou, cypress_wetlands, cypress_swamplands, rainbow_beach, pale_bog | cypress / willow, glowcane |
| Lush / meadow | allium_shrubland, amaranth_grassland, firecracker_chaparral, crag_gardens, prairie, orchard, coconino_meadow | the millable flowers (feeds the Create compat) |

**Concentrate** all bands on the forest generalist (one place, simplest, thematically loose) vs **distribute** by climate across the typed seeds (more files — and remember ×3 tiers each — but every seed grows the "right" island, the millable flowers naturally land on the lush/meadow seed, and the player picks the seed that matches the biome they found)? Distributing is the nicer loop; concentrating is the faster ship.

### Q3 — which BWG biomes deserve their OWN seed?  ✅ RESOLVED — almost none (see the rewritten answer)
**Almost none — the bar is non-growable, farm-worthy content.** A Forest seed thrown over a BWG biome already grows that biome's island at every tier (base/large/huge) with its trees, ground flora, and the biome itself — and once you've chopped one tree you have its sapling and can regrow that wood anywhere (island or Botany Pot). So a dedicated seed adds **nothing** for anything growable: wood, saplings, flowers and replantable plants are all covered by the adaptation + replanting.

The only thing that earns a dedicated seed (item + recipe + advancement + quest) is content that **cannot be grown but is worth farming** — e.g. a biome-exclusive **mob** you'd build a spawner-island around, or a resource that only regenerates in that biome's context and can't be replanted. Wood and plants never qualify; "the wood looks iconic" is not a reason — that's a *bespoke-island design* call (a unique shape/centerpiece), which is orthogonal to this content test.

So: ship **everything as adaptations** (Steps 1–4). Add a dedicated seed only after a biome passes that one test — non-growable **and** farm-worthy — checked per biome. On the current set (aspen … ironwood, plus enchanted/spirit/skyris) nothing does: it's all wood and plants.
