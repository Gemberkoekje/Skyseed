# BIOMECOVERAGEPLAN — biome → seed/override coverage audit

> **Status: AUDIT done 2026-07-05; ALL fixes F1–F7 IMPLEMENTED in-branch (both nodes green after F1–F3; F4–F7 build
> pending sign-off).** Full inventory of every overworld biome (vanilla + BWG + Quark) cross-checked against Skyseed's
> two-layer biome→look resolution. F1 (BWG deserts→Desert), F2 (BWG frozen→Frozen + frosted snow bands), F3 (vanilla
> `sparse_jungle`), F4 (BWG plains→Meadow + pumpkin_valley band), F5 (cypress_wetlands), F6 (howling_peaks snow) and F7
> (glimmering_weald→Lush) are all done, plus **F8** (orchard stranded-band fix, surfaced by the F1–F7 adversarial
> review). All fixes land in the **core repo** (`src/main/resources/data/skyseed/...`
> + `ExploreThemes.java`) and stay **byte-identical without the mod** (unknown `biomeswevegone:` ids/tags never match —
> [[skyseed-theme-override-inert-safety]]). Data pulled straight from the shipped jars
> (`Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar`, `Quark-4.1-481.jar`), not memory. Fits PLANOFPLANS BWG/biome polish.

## 1. How a biome gets a look (two layers)

1. **Which base theme an adaptive seed grows** — `ExploreThemes.RULES`
   ([`ExploreThemes.java:49`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/theme/ExploreThemes.java)) maps the
   germination biome → base theme. Wild/Explore (base/large/huge) seeds use this; an unmatched biome falls to `forest`
   (Wild) / `explore.json` (Explore).
2. **The per-biome band inside a theme** — a theme's `biome_overrides`, **first-match-wins**, matched by biome **id**
   or **`#tag`** ([`forest.json:43`](../src/main/resources/data/skyseed/skyseed/theme/forest.json)). Unmatched → the
   theme's top-level base config.

**Root cause of every gap below:** the forest/taiga/jungle/savanna/badlands/beach/mountain/ocean bands (and the matching
`ExploreThemes` rules) key off vanilla **`#minecraft:is_*` tags**, and **BWG injects its biomes into those tags** (its
`#biomeswevegone:forest` etc. are nested into `#minecraft:is_forest` etc.) — so those families auto-cover BWG for free.
But **snowy and desert have no vanilla `#is_*` tag**; Skyseed matches them by **explicit vanilla id**, so BWG's
snowy/desert biomes (and vanilla `sparse_jungle`, matched only by the broad `#is_jungle`) slip through to the wrong look.

## 2. Biome inventory

| Source | Count | Notes |
|---|---|---|
| Vanilla overworld | ~54 | +5 Nether, +5 End, +cave |
| BWG (`biomeswevegone`) | 55 | all overworld; folded into vanilla `#is_*` tags |
| Quark | 1 | `glimmering_weald` (underground); the rest is blossom-tree tags on vanilla biomes |
| Oh The Trees / TerraBlender / Botany Trees | 0 | add trees/engine, no biomes |

BWG's own family tags (verified from the jar) — this is the map the fixes target:

- **forest** (→ `#is_forest`): aspen_boreal, black_forest, canadian_shield, cika_woods, coniferous_forest, dacite_ridges, ebony_woods, enchanted_tangle, eroded_borealis, forgotten_forest, frosted_coniferous_forest, howling_peaks, orchard, overgrowth_woodlands, redwood_thicket, sakura_grove, skyris_vale, weeping_witch_forest, zelkova_forest
- **taiga** (→ `#is_taiga`): frosted_taiga, maple_taiga
- **jungle** (→ `#is_jungle`): crag_gardens, fragment_jungle, jacaranda_jungle, tropical_rainforest
- **savanna** (→ `#is_savanna`): araucaria_savanna, baobab_savanna, ironwood_gour
- **badlands** (→ `#is_badlands`): red_rock_peaks, red_rock_valley, rugged_badlands, sierra_badlands
- **beach** (→ `#is_beach`): basalt_barrera, dacite_shore, rainbow_beach
- **mountain** (→ `#is_mountain`): crag_gardens, howling_peaks
- **ocean** (→ `#is_ocean`): dead_sea, lush_stacks
- **swamp** (no vanilla tag): bayou, cypress_swamplands, cypress_wetlands, pale_bog, white_mangrove_marshes
- **plains** (no vanilla tag): allium_shrubland, amaranth_grassland, coconino_meadow, crimson_tundra, firecracker_chaparral, prairie, pumpkin_valley, rose_fields, temperate_grove
- **desert** (no vanilla tag; `#c:is_desert`): atacama_outback, mojave_desert, windswept_desert
- **snowy** (`#bwg:snowy` / `#c:is_snowy`): eroded_borealis, howling_peaks — **icy** (`#bwg:icy`): shattered_glacier

## 3. Findings

### 3a. Vanilla gaps (Forest family)

| Biome | Currently grows | Severity |
|---|---|---|
| `sparse_jungle` | dense `#is_jungle` band (`jungle_tree` tries 5 + bushes) | **medium** — should be open/sparse |
| `old_growth_pine_taiga` / `old_growth_spruce_taiga` | generic `#is_taiga` spruce band | low — no mega-spruce distinction |
| `stony_peaks` / `stony_shore` | base config (no band) | low — falls through |

### 3b. BWG — thematically **wrong** (grows a green/non-snow island)

| BWG biome(s) | Tag membership | Adaptive seed → | Forest seed → | Should be | Severity |
|---|---|---|---|---|---|
| `atacama_outback`, `mojave_desert`, `windswept_desert` | `#c:is_desert` only (no vanilla tag) | **forest** (fallback) | grass base config | Desert | **high** |
| `eroded_borealis` | `#is_forest` + `#c:is_snowy` | **forest** (green oak/birch) | green oak/birch | Frozen | **high** |
| `shattered_glacier` | `#bwg:icy`+`#bwg:slope` (no vanilla tag) | **forest** (fallback) | grass base config | Frozen | **high** |
| `crimson_tundra` | `#bwg:plains` + `climate/cold` | **forest** (fallback) | grass base config | **Meadow** (temp 0.75 — a warm red grassland, NOT snowy despite the name) | medium |
| `frosted_taiga`, `frosted_coniferous_forest` | `#is_taiga` + `climate/cold` | forest (spruce, **no snow**) | spruce, no snow | snowy spruce | medium |
| `howling_peaks` | `#is_mountain` + `#c:is_snowy` + `#is_forest` | rocky (**no snow**) | oak/birch | snowy peak | low |
| `pumpkin_valley` | `#bwg:plains` | forest (fallback) | grass base config | its own pumpkin-plains look | medium |

### 3c. BWG — generic but acceptable (catch-all is a reasonable match)

araucaria_savanna (savanna), canadian_shield + coniferous_forest + overgrowth_woodlands (forest/taiga), rugged_badlands + sierra_badlands + red_rock_peaks + red_rock_valley (badlands), dacite_shore + basalt_barrera (beach), dead_sea + lush_stacks (ocean), cypress_wetlands (sibling of the handled cypress_swamplands; only village decor today).

### 3d. BWG — already well covered (32 biomes with a dedicated island band)

- **Forest override** (base/large/huge — [`biomeswevegone_forest.json`](../src/main/resources/data/skyseed/skyseed/theme_override/biomeswevegone_forest.json)): aspen_boreal, baobab_savanna, cika_woods, jacaranda_jungle, maple_taiga, ebony_woods, redwood_thicket, zelkova_forest, weeping_witch_forest, sakura_grove, ironwood_gour, enchanted_tangle, skyris_vale, pale_bog, cypress_swamplands, forgotten_forest, dacite_ridges, black_forest, tropical_rainforest, fragment_jungle
- **Meadow override**: allium_shrubland, amaranth_grassland, rose_fields, coconino_meadow, orchard, prairie, temperate_grove, firecracker_chaparral
- **Aquatic override**: bayou, white_mangrove_marshes, rainbow_beach (+ cypress_swamplands)
- **Lush override**: crag_gardens (+ tropical_rainforest, fragment_jungle)
- **Structure-decor only** (village_center/hamlet/trade_post — no island terrain band): pumpkin_valley, red_rock_valley, cypress_wetlands, skyris_vale, forgotten_forest, cika_woods, weeping_witch_forest

### 3e. Quark

`glimmering_weald` is underground (not in any surface tag) → adaptive seeds fall to Forest. Leave, or map to `lush`.

## 4. Proposed fixes (highest value first)

Ordered by "how wrong it looks today." Each is inert without BWG. Remember [[skyseed-mirror-island-tiers]] — every base
theme change mirrors to `_large` + `huge_`.

- [x] **F1 — BWG deserts → Desert theme (high). DONE.** Added `new Rule("#biomeswevegone:desert", "desert")` to
  `ExploreThemes.RULES` right after `minecraft:desert` (before the forest catch-alls); the `_large`/`huge_` resolvers
  follow via `family()`. This one clean tag holds exactly atacama_outback / mojave_desert / windswept_desert. *(Skipped
  the optional Forest-seed-on-desert companion band — the theme redirect is the win; a plain Forest seed on a BWG desert
  is an edge case, left for F-followup if wanted.)*
- [x] **F2 — BWG frozen → Frozen theme (high). DONE.** Added `#biomeswevegone:snowy` + `#biomeswevegone:icy` → `frozen`
  rules **after** `#is_mountain` (so `howling_peaks` stays Rocky via its mountain tag and F6 snow-caps it there;
  `eroded_borealis` / `shattered_glacier`, both temp −0.5, reach the snowy/icy rules → Frozen).
  `frosted_taiga`/`frosted_coniferous_forest` stay on the Forest family but gained a
  `biomeswevegone_forest{,_large,_huge}` band with `"snow": 1.0` + spruce/podzol so they get a snow cap. **Adversarial
  review fix:** `crimson_tundra` was initially routed here on the strength of its name, but it's temp **0.75** — a warm
  red grassland, not frozen. Dropped that rule; it now falls to F4 (`plains → Meadow`).
- [x] **F3 — `sparse_jungle` sparse band (medium, core/vanilla). DONE.** Added a `minecraft:sparse_jungle` band in
  `forest.json` + `forest_large.json` + `huge_forest.json` **before** the `#minecraft:is_jungle` catch-all: scattered
  jungle trees mixed with oak over grass/fern + the odd melon (open, not a canopy). Pure-vanilla.
- [x] **F4 — `pumpkin_valley` → Meadow (medium). DONE.** Bigger than a lone band: added
  `new Rule("#biomeswevegone:plains", "meadow")` to `ExploreThemes` — the whole BWG grassland family was falling to the
  Forest fallback even though `biomeswevegone_meadow` already carried dedicated flower-field bands for 7 of them (a
  latent gap), so this activates those bands for the adaptive seed too. Added a new `pumpkin_valley` pumpkin-patch band
  to `biomeswevegone_meadow{,_large,_huge}`. Also catches `crimson_tundra` (a temperate red grassland, temp 0.75 — the
  adversarial review corrected its initial F2 Frozen mis-routing); it gets a generic Meadow island (no dedicated red
  band yet).
- [x] **F5 — `cypress_wetlands` terrain band (low). DONE.** Added `biomeswevegone:cypress_wetlands` to the existing
  `cypress_swamplands` bands in the forest overrides (×3) and aquatic overrides (×3) — same cypress look as its sibling.
- [x] **F6 — `howling_peaks` snow (low). DONE.** Added `biomeswevegone:howling_peaks` to the Rocky theme's snowy band
  (`rocky.json` + `rocky_large.json` + `huge_rocky.json`), which gives it the snow-capped *peak* shape (better than
  routing to flat Frozen). Direct id in the base band is fully inert without BWG (unknown id never matches).
- [x] **F7 — `glimmering_weald` → `lush` (optional). DONE.** Added `new Rule("quark:glimmering_weald", "lush")` to
  `ExploreThemes`. Inert without Quark.
- [x] **F8 — `orchard` stranded-band fix (found in the F1–F7 adversarial review; not in the original audit). DONE.**
  `orchard` is a temperate (0.8) BWG **fruit-tree wood** with a dedicated `orchard_trees` feature; it sits in
  `#biomeswevegone:forest` so the adaptive seed resolves it to the Forest theme — but the forest override had **no
  orchard band**, so it grew a generic oak/birch island and lost its trees, while the `orchard_bloom` band in
  `biomeswevegone_meadow` (flowers-only) was only reachable via an explicit Meadow seed. Added an `orchard` band to
  `biomeswevegone_forest{,_large,_huge}` (`orchard_trees` + the biome's own blossom flora at 6/40/120 tries). Kept the
  meadow flower-field band per the distribute-across-families convention (Meadow seed = flowers, Wild/Forest = the tree
  orchard). Inert without BWG.

**Remaining tail (low value, not done):** the BWG deserts still get only the theme redirect (no Forest-seed-on-desert
companion band); `bayou`'s aquatic band is still only reachable via an explicit Aquatic seed (adaptive → Forest fallback,
same pattern as cypress); and the generic-but-acceptable BWG biomes in §3c are left on their catch-alls by design.

## 5. Verification

- **Debug seeds are auto-derived** from each theme's `biome_overrides` by `ThemeScanner`
  ([`ThemeScanner.java:48`](../src/main/java/dev/gemberkoekje/skyseed/registry/ThemeScanner.java)) — every new band
  above surfaces a `debug_<theme>_<biome>` seed next launch, no list to hand-edit. Throw-test each in creative.
- Confirm the resolver redirects (F1/F2/F7) with a Wild seed thrown on the real BWG biome in-game (or a targeted
  gametest asserting `ExploreThemes.resolveFor(WILD, biome)` for a representative id).
- Both stonecutter nodes (1.21.1 + 26.1.2) must stay green; the 26.1.2-only ids (leaf_litter etc.) already gate via the
  tolerant resolvers, so BWG ids added here are equally inert on the vanilla pack.
