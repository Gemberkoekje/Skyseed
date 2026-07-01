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
- **Q2 — concentrate vs distribute → ✅ RESOLVED: DISTRIBUTE (per typed seed, priority-ordered — see Q2 below).** The **wet woods** (cypress/willow/white-mangrove/palm) and **fantasy woods** (enchanted/skyris/spirit) are now **DRAFTED (2026-07-01), pending id verification**:
  - **Wet woods → Aquatic family (water-first):** new `biomeswevegone_aquatic.json` (+ `_large`, `huge_`) — pond-dominant bands with the BWG wet-wood trees as the secondary layer.
  - **Fantasy woods → Forest family (trees-first):** appended to `biomeswevegone_forest.json` (+ `_large`, `huge_`). The same files also gained a **trees-first `cypress` overlap** as the deliberate multi-seed demo (cypress is water-first on Aquatic, trees-first on Forest).
  - **✅ SHIPPED (2026-07-01, v0.171.0).** All ids **verified against the BWG 2.6.0 jar** and corrected: willow → `bayou` biome / `bayou_trees` feature (no `willow_trees` exists), white_mangrove → `white_mangrove_marshes` (not `pale_bog`), **spirit IS growable** via `pale_bog` (there is no `spirit_woods` biome), palm kept on the sandy `rainbow_beach` by design (BWG injects `palm_trees` into vanilla beach). `feature` resolves against the **configured**-feature registry. Added `biomeswevegone_compat_prepends_aquatic_bands` + extended the forest gametest (both nodes); `mod_version`/CHANGELOG bumped. Tree tries / pond sizes remain tunable in-game.
- **Density follow-up:** lift the *held* wet/semi forest biomes (mangrove/swamp/riverside, cherry/grove/mushroom/bamboo/flower) to the agreed level — held pending the in-game density read, now confirmed good.
- **Step 2 (OTYG verification) ✅ DONE (2026-07-01)**, **Step 4 (Patchouli "Exotic Woods" entry) ✅ DONE**, and **Step 5 (the light quest branch under Tools & Travel) ✅ DONE (2026-07-01)**. **Step 3 (create-otbwg live-mill spot-check)** and **the rest of Step 4 (config curation + `mods.txt` regen)** — still to do.
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

## Step 1 — First-party BWG theme_override compat (Skyseed mod) ← the core  ✅ DONE (forest family; wet/fantasy woods decided per Q2 = distribute, authoring pending)

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

## Step 2 — OTYG (sapling growth) — ✅ VERIFIED (2026-07-01)

- ✅ **Verified in-game:** vanilla **oak** and BWG **Zelkova** saplings both grow to trees. **BWG saplings grow out of the box** — the Potion Studios tree-pack datapack was **not** needed.
- ✅ **Botany Pots path works:** Elite Botany Pot + dirt + Zelkova sapling → 2 zelkova logs; + oak sapling → 3 oak logs. Growth works both on islands and in pots.
- `skip_decoration: true` already neutralises OTYG worldgen — no leak. *(Config review of `config/ohthetreesyoullgrow*` for growth/worldgen toggles rolls into the Step 4 config-curation pass; nothing needed for growth to work.)*

## Step 3 — create-otbwg-compat (millable flowers) — ✅ INPUTS PLACED (v0.174.0)

- **✅ DONE (v0.174.0, backlog #9):** the BWG island `ground` lists now place the millable flowers, so the compat has
  inputs. Two new `theme_override` families (inert without BWG):
  - **Meadow** (`biomeswevegone_meadow.json` + `_large` + `huge_`) — 8 floral **grasslands** → flower-field islands:
    `allium_shrubland`, `amaranth_grassland`, `rose_fields`, `coconino_meadow`, `orchard`, `prairie`, `temperate_grove`,
    `firecracker_chaparral`.
  - **Lush** (`biomeswevegone_lush.json` + `_large` + `huge_`) — 3 **jungle** biomes → flora-first tropical-bloom
    islands: `crag_gardens`, `tropical_rainforest`, `fragment_jungle` (the last two also grow trees-first on the Forest
    family — the Q2 multi-seed overlap).
  - **Verification method:** every placed flower was confirmed to be BOTH a real BWG 2.6.0 block (`assets/.../blockstates`)
    AND a `create-otbwg-compat-1.0` milling **input** (`data/create/recipe/milling/*`) — so each island flower feeds a
    real milling recipe. Ground flora is per-column (no `tries`), so the 3 tier files per family are identical bands.
    New `biomeswevegone_compat_places_{meadow,lush}_flowers` gametests (both nodes) assert each band places a
    `biomeswevegone:` flower.
- **Remaining (in-game):** spot-check 2–3 milling recipes actually resolve/run in a Create Millstone against a harvested
  BWG flower (the datapack side is verified; this is the live-recipe confirmation). The petal-blocks / glowcane / sand /
  cactus milling inputs are intentionally **not** placed as meadow/lush ground (glowcane/cacti belong to aquatic/desert
  families; petal-blocks are crafted, not grown).

## Step 4 — Modpack wiring

- Curate `config/` for BWG, OTYG, create-otbwg-compat (like the MA pass). **Leave BWG biome injection ON** — it is the adaptation key.
- Regenerate `mods.txt`.
- ✅ **DONE (2026-07-01)** — Patchouli guide: a short **"Exotic Woods"** entry (`entries/exotic_biomes.json`, `skyseed:basics` category, vanilla icon) describing the Forest-seed-over-BWG-biome loop, mirroring the rare-seeds page. The Modonomicon edition auto-generates at build via `generateGuide`.
  - **Only shows when BWG is installed** (the requested trick): the entry carries a Patchouli `flag: "mod:biomeswevegone"` **and** an entry-level `advancement: "skyseed:reveal_exotic_woods"`. That hidden advancement fires on obtaining any item in the new **inert** `#skyseed:exotic_woods` item tag (all `{id, required:false}` — empty without BWG, so it can never fire → entry hidden on both backends; with BWG, growing an exotic wood reveals it). `generateGuide` turns the entry `advancement` into a `modonomicon:advancement` condition, so the gate holds on the primary (Modonomicon) backend too.
  - ⚠ The tag's `biomeswevegone:*_planks` ids are best-guesses (same wood names as the band drafts) — **verify against the BWG 2.6.0 jar** (and confirm whether BWG ships a `#biomeswevegone:planks` tag, which would replace the explicit list). *(Remaining Step 4 work: the config-curation pass + `mods.txt` regen.)*

## Step 5 — Quests (optional, light) — ✅ SHIPPED (2026-07-01)

A small branch under the **Tools & Travel** chapter (an explore-the-wilds line — see QUESTPLAN) — not a new megachapter (kept it un-overwhelming). Authored as quests **B701–B703** in `overrides/config/ftbquests/quests/chapters/tools.snbt`, text in `lang/en_us.snbt`:
- *Into the Wilds* (B701) — obtain any BWG plank (proof you grew an exotic island). **Advancement task** hooking the already-shipped hidden `skyseed:reveal_exotic_woods` advancement (criterion `has_exotic_wood`, `inventory_changed` on `#skyseed:exotic_woods`) — so it auto-completes on picking up any exotic plank and stays inert without BWG (empty tag). Depends on Skyseed **B103** (grow a biome island). Reward: bone meal ×16.
- *Mill the Blooms* (B702) — mill a BWG bloom into dye/petals (ties BWG → Create). Checkmark; depends on **B701 + Create B204** (Millstone/Crushing Wheels). Reward: XP bottles ×6.
- *Grow Something Grand* (B703) — grow a tree from a sapling (OTYG). Checkmark; depends on **B701**. Reward: Skyfarer's Cache roll.

**★ Why an advancement, not a filter task.** First attempt used an `ftbfiltersystem:smart_filter` item task with `item_tag(skyseed:exotic_woods)` — **it does NOT work**: in-game FTB Quests treats the Smart Filter as a *literal item to obtain* (the "Valid items" screen shows only the filter), not as a tag expansion. Reusing the hidden reveal advancement (built for the Patchouli guide gate) is the robust fix — one hidden advancement now drives both the quest and the guide entry. `ftb-filter-system` is therefore **no longer required** by this branch (harmless to keep as a general QoL mod). The other two quests are checkmarks because a milled output / a grown tree aren't clean single item-ids. **Remaining:** in-game quest-book test-load (tasks/rewards resolve) — see PLANOFPLANS checklist #3.

## Plank coverage audit — 24 / 25 obtainable (2026-07-01) ✅ CLOSED (v0.173.0; fir is the documented exception)

**✅ DONE (v0.173.0).** The 5 addable woods shipped as dedicated-feature bands on the Forest family
(`biomeswevegone_forest.json` + `_large` + `_huge`): florus → `forgotten_forest`/`florus_trees`, holly →
`dacite_ridges`/`holly_trees`, pine → `black_forest`/`pine_tree1`+`pine_tree2` (no aggregate), mahogany →
`tropical_rainforest`/`mahogany_trees`, rainbow_eucalyptus → `fragment_jungle`/`rainbow_eucalyptus_trees`. Every id
re-verified against the 2.6.0 jar; `#skyseed:exotic_woods` extended; the forest gametest (both nodes) now asserts the
5 new biome→feature keys and guards that **no** band references a `fir_*` feature. **fir stays non-growable** (below).
So **24 of 25 planks are island-obtainable**; fir is the single documented exception. History below.

### (historical) 19 / 25 — the gap this section closed

Enumerated the plank roster straight from `overrides/mods/Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar`
(`assets/biomeswevegone/blockstates/*_planks.json`): **BWG 2.6.0 ships 25 plank types.** The shipped
Forest + Aquatic bands (and the `#skyseed:exotic_woods` tag) cover exactly **19**. Six planks have **no
island band and are absent from the tag**, so the "every BWG plank is now obtainable" goal is **not yet
met**. Five are trivially addable (each has a real configured tree feature + host biome); the sixth (fir)
appears to have no worldgen source at all.

**TODO — add bands for the 5 addable woods (×3 tiers each), extend `#skyseed:exotic_woods` + the gametests,
and settle fir.** Per the Q2 distribute rule, point each band at the *specific* configured feature (not the
biome-mixed `*_trees` selector) so it yields a single clean plank:

| Missing plank | Host biome(s) | Feature to target | Suggested family | Notes |
|---|---|---|---|---|
| florus | `forgotten_forest` | `florus_trees` (dedicated) | Forest | clean dedicated feature |
| holly | `dacite_ridges`, `eroded_borealis` | `holly_trees` (dedicated) | Forest / Rocky | clean dedicated feature |
| pine | `black_forest` (also `canadian_shield`, `red_rock_valley`/`red_rock_peaks`) | `pine_tree1` + `pine_tree2` | Forest (or Frozen) | **no** dedicated `pine_trees` aggregate — target `pine_tree1/2` directly (else it only appears mixed inside `black_forest_trees` / `canadian_shield_trees` / `red_rock_valley_trees`) |
| mahogany | `tropical_rainforest`, `fragment_jungle`, `crag_gardens` | `mahogany_trees` | Forest (jungle band) | in worldgen only mixed into `rainforest_trees` / `guiana_shield_trees` — target `mahogany_trees` directly for pure mahogany |
| rainbow_eucalyptus | `fragment_jungle`, `crag_gardens` | `rainbow_eucalyptus_trees` | Forest (jungle band) | only mixed into `fragment_jungle_trees` / `guiana_shield_trees` — target `rainbow_eucalyptus_trees` directly |
| **fir** | — none found — | **no `fir_tree`/`fir_trees` feature exists** | — | ships `fir_planks` + `fir_sapling`, but there is **no configured fir tree feature** and fir was **not** found in any biome tree-selector audited → appears **non-growable** in 2.6.0. Confirm, then either expose via a recipe or document as intentionally out of scope. |

Ship steps per the standing rules: inert golden-master gametest per set, `mod_version` + CHANGELOG bump.
Cross-refs: the in-game checklist line "every BWG plank is now obtainable" and backlog item **#63** in
[PLANOFPLANS.md](../PLANOFPLANS.md).

## In-game test findings (2026-07-01, v0.171.0) ← TODO: items #64–#66

A user-run throw-a-seed pass over the shipped wet/fantasy wood bands. **Working:** enchanted ✅, skyris ✅,
white-mangrove ✅, palm ✅ (mangrove & palm grew plenty of trees); bands are **inert with BWG absent** (no
errors). Three groups of issues to address (do **not** fix inline — tracked as backlog items):

1. **Water feature too small / wrong shape (→ #64).** Every Aquatic wet-wood island (cypress, bayou/willow,
   white-mangrove, palm) and the Aquatic-cypress island read as "not enough water" — the Huge tier especially.
   A deep round `pond` is the wrong idiom for swamp/marsh woods; implement a **broad, shallow swamp/marsh**
   water feature instead. (User's own suggestion.)
2. **Sparse / zero trees (→ #65).** Small tiers grow **0 trees** (Aquatic cypress Small = 0; Forest cypress
   Small = 0) and **Huge Bayou grew 0 willow trees at all**. Guarantee ≥1 tree on the Small tier (raise `tries`
   and/or the minimum island radius) and fix the Huge Bayou zero-tree case. Observed cypress counts —
   Aquatic: Small 0 / Large 1 / Huge 5; Forest: Small 0 / Large ~5 / Huge ~8 (the trees-first vs water-first
   priority *does* read — the Forest form is denser — it's the low/zero floor that's the problem).
   - **Code-side root-cause analysis (2026-07-01, needs in-game confirm):** in `DecorationPlanner.planDecoration` a
     `tries` loop picks a random surface column and defers each as a `TreeSite`; `GenerationJob` then calls
     `feature.place(...)` per site, and a **BWG tree that can't fit returns false and silently plants nothing**. So on a
     small island the zero-tree floor is likely **placement failure**, not too-few `tries` — the OTYG `tree_from_nbt_v1`
     trees are multi-block NBT structures (esp. **willow**/`bayou_trees` and the tall cypress) that need clear vertical +
     lateral room a tiny pad + a surface pond don't leave. Raising `tries` alone won't guarantee ≥1 if every site fails.
     Better levers to try in-game: **(a)** raise the wet-family **minimum island radius** (a bigger dry pad) and/or shrink
     the base-tier pond so trees have somewhere to stand; **(b)** as a floor, seed one **guaranteed** central tree
     (place the feature at the island centre first, before the pond carve, and retry a few offsets on failure); **(c)**
     for willow specifically, confirm `bayou_trees`/`willow_tree1..4` even *can* place on the Aquatic pad (it may need
     water-adjacent mud) — if not, pick a smaller willow variant for the Small/Huge floor. All three want the client loop
     to confirm the count actually moves off 0, so no speculative edit was shipped.
3. **Spirit band not resolving (→ #66). ✅ DIAGNOSED (2026-07-01) — no code/data change; a re-test item.** A seed
   over `pale_bog` reportedly produced only **oak & birch, no spirit trees**. Root-cause audit against the jar + engine:
   - `biomeswevegone:pale_bog` (biome) and `biomeswevegone:spirit_trees` (configured feature) **both exist** in 2.6.0.
   - The band is present, **prepended** ahead of the base catch-alls, and gametest-asserted (all three tiers).
   - Spirit uses the **same** `ohthetreesyoullgrow:tree_from_nbt_v1` feature type as its siblings enchanted/skyris,
     which **work** in-game — so it is not a feature-type/placement class problem.
   - **Decisive:** a *matched* biome override **replaces** the island's variants (`IslandGenerator.eff` returns the
     override's variants outright, not merged — confirmed in code), so a matched `pale_bog` band emits **only**
     `spirit_trees` and **can never produce oak/birch**. Oak/birch therefore means the band **did not match** →
     **the seed was not over `pale_bog`.** The same test pass self-contradicts ("pale_bog → white mangrove trees"),
     confirming a **mislabelled biome**.
   - **Action = re-test, not fix:** re-throw over a *confirmed* `pale_bog` (verify via F3/Debug tab that you're on
     `biomeswevegone:pale_bog` first), and while there **confirm `pale_bog` is actually reachable** in the void
     multi-noise overworld (if TerraBlender doesn't place it, spirit would be de-facto ungrowable and would need
     re-keying to a reachable biome — the only remaining open sub-question). No band/code edit made.

Also reconfirmed in this pass: **create-otbwg millable flowers (#9) are still not placed on any island** — the
compat has no inputs until the lush/meadow flower bands are authored.

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

### Q2 — should other typed seeds respond to BWG biomes, not just forest?  ✅ RESOLVED — DISTRIBUTE (per typed seed, priority-ordered; 2026-07-01)
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

**✅ Decision (2026-07-01) — DISTRIBUTE, per typed seed, priority-ordered.** Each typed seed family gets its own BWG `theme_override` set adapting the BWG biomes in its climate — not everything piled on the forest generalist. Two refinements beyond the plain fork:

- **The same BWG biome may be adapted by more than one seed family, each with a different emphasis/priority.** The seed you throw decides the island's *character*, not just whether it grows. Example: a wet, tree-bearing biome (`cypress_swamplands`, `bayou`) grown from an **Aquatic** seed is **water-first** (ponds/rivers dominate, its BWG trees are the secondary layer); the *same* biome grown from a **Forest** seed is **trees-first** (dense cypress/willow, water minimal). A **Lush** seed leans **maximal / "extreme" nature** (dense flora, plants, undergrowth); **Meadow** foregrounds the millable flowers; **Desert/Badlands** the arid palette; **Frozen** the cold palette; **Rocky** the stone/volcanic palette.
- **Per-seed "priority" = the ordering of what dominates the island** — surface/water vs trees vs ground flora vs plants — expressed through each override's `variants` decoration weights and `pond`/`surface_*` idioms. Author a biome's *content* once, then tune the per-family emphasis when it appears in more than one family's file.

**File convention (so downstream band work has a home).** One override set per participating seed family, ×3 tiers, mirroring the shipped forest set and the MA/Create precedent:

```
biomeswevegone_<family>.json          (base tier   → theme <family>.json)
biomeswevegone_<family>_large.json    (large tier  → theme <family>_large.json)
biomeswevegone_huge_<family>.json     (huge tier   → theme huge_<family>.json)
```

`<family>` ∈ { `forest` (✅ done), `aquatic`, `lush`, `meadow`, `desert`, `badlands`, `frozen`, `rocky`, `mushroom` } — add each set as that family earns bands. (Base names + tiers confirmed against `data/skyseed/skyseed/theme/`: base `<family>.json`, large `<family>_large.json`, huge `huge_<family>.json`.) A biome adapted by two families simply appears — with different `variants`/emphasis — in both families' files.

**What this unblocks now (the remaining 14 of 25 planks):** the **wet woods** (cypress/willow/white-mangrove/palm) land on the **aquatic** family (water-first) and, where the wood is the draw, may also ride the **forest** family (trees-first); the **fantasy woods** (enchanted/skyris/spirit) land on the **forest** family (forest-climate). Millable-flower biomes go on **lush/meadow** (feeds the create-otbwg compat, Step 3). Per the Step 1 rules: confirm every `*_trees` feature id + flower/plant block id against the BWG 2.6.0 jar first, budget the ×3 tier multiplier, and ship an inert golden-master gametest per set with a `mod_version` + CHANGELOG bump.

### Q3 — which BWG biomes deserve their OWN seed?  ✅ RESOLVED — almost none (see the rewritten answer)
**Almost none — the bar is non-growable, farm-worthy content.** A Forest seed thrown over a BWG biome already grows that biome's island at every tier (base/large/huge) with its trees, ground flora, and the biome itself — and once you've chopped one tree you have its sapling and can regrow that wood anywhere (island or Botany Pot). So a dedicated seed adds **nothing** for anything growable: wood, saplings, flowers and replantable plants are all covered by the adaptation + replanting.

The only thing that earns a dedicated seed (item + recipe + advancement + quest) is content that **cannot be grown but is worth farming** — e.g. a biome-exclusive **mob** you'd build a spawner-island around, or a resource that only regenerates in that biome's context and can't be replanted. Wood and plants never qualify; "the wood looks iconic" is not a reason — that's a *bespoke-island design* call (a unique shape/centerpiece), which is orthogonal to this content test.

So: ship **everything as adaptations** (Steps 1–4). Add a dedicated seed only after a biome passes that one test — non-growable **and** farm-worthy — checked per biome. On the current set (aspen … ironwood, plus enchanted/spirit/skyris) nothing does: it's all wood and plants.
