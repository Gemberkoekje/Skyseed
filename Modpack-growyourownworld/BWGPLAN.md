# Oh The Biomes We've Gone + OTYG + Create-OTBWG — Integration Plan

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

## Step 1 — First-party BWG theme_override compat (Skyseed mod) ← the core

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

### Q1 — Step 1 scope (the immediate fork)
All signature **wood** biomes (~15, every plank reachable) or a **tight first pass** (5–6 marquee woods: aspen, baobab, cika, jacaranda, maple) to validate the loop before scaling? (× the 3 forest tiers either way.)

### Q2 — should other typed seeds respond to BWG biomes, not just forest?
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

### Q3 — which BWG biomes deserve their OWN seed?
Most BWG biomes are best as adaptations (a band on an existing seed). A few may be iconic enough for a **dedicated seed** — a new seed item + theme (×3 tiers) + advancement + recipe + quest, i.e. effectively a new mini-tier (cf. the "each content mod can become an island tier" roadmap). Reserve this for biomes that are (a) visually unforgettable, (b) carry unique signature blocks, and (c) feel rewarding as a *goal* rather than a random find. Candidates:

- **Enchanted** — `enchanted_tangle` / `forgotten_forest`, blue/green enchanted (glowing) wood. The marquee pick.
- **Spirit** — the ethereal `base_spirit_tree` woods (confirm the biome id in the tail).
- **Rainbow Eucalyptus / Sakura** — striking single-tree showpieces; could share one "ornamental" seed.
- **Baobab Savanna / Jacaranda Jungle** — bold silhouettes, but probably fine as plain adaptations.

Recommendation: ship **everything as adaptations first** (Steps 1–4), then promote **1–2** marquee biomes (Enchanted, maybe Spirit) to dedicated seeds as a follow-up tier — so the special biomes feel earned and Step 1 doesn't balloon into a dozen new seed items.
