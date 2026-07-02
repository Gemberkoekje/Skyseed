# Oh The Biomes We've Gone + OTYG + Create-OTBWG — Integration Plan

**Status (2026-07-01, mod 0.181.0): shipped end-to-end.** All integration steps landed — the forest-family
wood bands (v0.170.0), the wet/fantasy wood bands with jar-verified ids (v0.171.0), the plank-coverage
close-out (v0.173.0, 24/25 — fir is the documented exception), the millable-flower islands + Forest flower
sprinkle (v0.174.0–v0.175.0, confirmed milling in-game), the BWG-gated "Exotic Woods" guide entry, and the
B701–B703 quest branch. History lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md`. The BWG **villages**
shipped separately via [BWGVILLAGEPLAN.md](BWGVILLAGEPLAN.md) (v0.176.0–v0.181.0).

**What's left in this plan** (backlog #s = [PLANOFPLANS.md](../PLANOFPLANS.md)):

- ~~**#64**~~ — ✅ **DONE (v0.186.0, signed off 2026-07-02):** the wet-wood water feature is a broad shallow swamp/marsh (`depth: 2`, `slope: true`, `extent` 0.6/0.62/0.68 base/large/huge) across the ×3 Aquatic tiers, gametest-guarded (`biomeswevegoneWetWoodPondsAreShallowMarshes`); the in-game re-throw confirmed the water reads as a marsh.
- ~~**#65**~~ — ✅ **DONE (v0.186.0, signed off 2026-07-02):** the engine's ≥1-tree guarantee (`GenerationJob.forceOneTree`) grades a real 5×5 planting clearing so the big NBT wet-wood trees can't silently fail to zero; base-tier `tries` lifted 4→6. In-game re-throw confirmed Small tiers grow ≥1 tree and **Huge Bayou grows willows** — the clearing was sufficient, so lever (c) was not needed.
- ~~**#66**~~ — ✅ **DONE (2026-07-02):** a forest seed grew **spirit trees** — band confirmed working (the earlier "no spirit" read was the mislabelled-biome test); no code change.
- ~~**#22**~~ — ✅ **DONE (v0.186.0):** the held wet/semi forest biomes (flower/cherry/grove/mangrove/swamp/riverside/mushroom/bamboo) had their **primary** tree `tries` lifted to the canonical per-tier forest density — **7 / 40 / 120** (base/large/huge) — in `forest{,_large}.json` + `huge_forest.json`, matching the v0.170.0 pure-forest pass (e.g. huge `mangrove_swamp` 40 → 120). Secondary trees / giant mushrooms / spacing / ponds / ground flora unchanged, so each biome keeps its character; genuinely open biomes (plains/savanna/beach/desert) stay scattered. Both nodes green.
- ~~**#23**~~ — ✅ **DONE (v0.186.0):** config coherence pass + `mods.txt` refresh — see the **Step 4** section below.
- **In-game sign-offs ✅ DONE (2026-07-02):** **#3** quest-book test-load and **#10** guide-reveal (both Patchouli +
  Modonomicon) confirmed — the BWG branch works as intended. **#10 fully closed:** the stale "Eleven wood families"
  flavour text was corrected to the current **twenty** (v0.184.0).

## The three mods

| Mod | What it is | Skyblock reality |
|---|---|---|
| **Oh The Biomes We've Gone** (`biomeswevegone` 2.6.0) | **55 biomes**, **25 wood types** (aspen…zelkova), 88 saplings, a large flower/plant/block/mob catalog. Biomes injected via **TerraBlender**. | Biomes **are** in the void's biome layout (multi-noise overworld preset); terrain + features are suppressed by `skip_decoration: true`. Content reaches the player only through Skyseed islands. |
| **Oh The Trees You'll Grow** (`ohthetreesyoullgrow` 5.3.2) | A sapling-growth **framework** — saplings grow into larger, varied tree structures. Ships only `test` trees; **no items**. | Worldgen suppressed in the void; the *sapling-growth* behaviour is the value, and it works on islands / in Botany Pots. |
| **create-otbwg-compat** (1.0) | 94 entries, all `data/create/recipe/milling/*` — Create **milling** recipes turning BWG flowers/plants into petals / dyes / powders. | Pure recipe datapack. "Just works" once the BWG flowers are obtainable (they are, since v0.174.0). |

Dependencies already present: **TerraBlender** 4.1.0.8, **Architectury** 13.0.8. No new deps.

## Why this is (mostly) a content-routing problem

Nothing generates in the void, so BWG's value — exotic woods, millable flowers, unique plants — is invisible unless an island carries it. Skyseed's theme engine is purpose-built for exactly this:

- `theme/*.json` islands carry a `biome_overrides` list; each entry matches the biome the seed is thrown over (by id or `#tag`) and supplies surface / `variants` / `trees` (configured-**features**) / `ground` plants / mobs. A **matched** override *replaces* the island's variants (`IslandGenerator.eff` returns them outright, not merged).
- The void overworld keeps the **multi-noise overworld biome source** (verified in `world_preset/skyblock.json`), so **BWG biomes occupy the biome map** — you can stand in the void over `biomeswevegone:cika_woods`.
- Unknown ids in a theme are silently skipped (the version-inert `pale_garden` override proves this) → BWG entries are **byte-identical-inert without BWG installed**.
- `ThemeOverride` patches **append** `biome_overrides` (merge-by-selector, per `ThemeOverride.Patch.applyTo`) to a base theme — and `theme_override` biome bands **prepend** (win the first-match over the base `#is_*` catch-alls; BWG biomes are transitively under `#is_forest` via `#biomeswevegone:forest`). BWG support ships as a first-party, opt-in compat datapack, exactly like the `mysticalagriculture_*` / `create_*` overrides.

**Net design: throw a Forest seed over a BWG biome → grow a BWG-wood island.** This extends the rare-seed mechanic (forest-over-badlands) to 55 new biomes and turns BWG's biome map into a treasure map.

Band-authoring recipe (used for the #64/#65/#22 edits; reuse for any future family set) — per-entry shape:

```jsonc
{ "biomes": ["biomeswevegone:aspen_boreal"],
  "variants": [ { "weight": 1, "name": "aspen", "decoration": {
    "trees":  [ { "feature": "biomeswevegone:aspen_trees", "tries": 6, "spacing": 2 } ],
    "ground": [ { "block": "minecraft:short_grass", "chance": 0.30 },
                { "block": "biomeswevegone:<flower>", "chance": 0.05 } ] } } ] }
```

Standing rules: verify every `*_trees` feature id + flower/plant block id against the BWG 2.6.0 jar first; budget the ×3 tier multiplier; ship an inert golden-master gametest per set with a `mod_version` + CHANGELOG bump.

## Step 3 — create-otbwg-compat (millable flowers) — ✅ CLOSED (v0.174.0–v0.175.0)

Shipped and confirmed in-game end-to-end: the **Meadow** (8 floral grasslands) and **Lush** (3 jungle biomes)
`theme_override` families place the millable BWG flowers as island ground cover — every flower cross-verified
as a real BWG 2.6.0 block AND a create-otbwg milling input — plus a biome-authentic flower sprinkle on the
Forest-family exotic-wood bands (v0.175.0, aesthetic only). Gametests
`biomeswevegone_compat_places_{meadow,lush}_flowers` on both nodes. The petal-blocks / glowcane / sand /
cactus milling inputs are intentionally **not** placed as meadow/lush ground (glowcane/cacti belong to
aquatic/desert families; petal-blocks are crafted, not grown).

## Step 4 — Modpack wiring — #23 ✅ DONE (v0.186.0)

Shipped: BWG/OTYG/create-otbwg/TerraBlender/Better Clouds jars in `mods.txt`; BWG config tracked with **biome
injection ON** (the adaptation key); the **"Exotic Woods"** guide entry (`entries/exotic_biomes.json`,
`skyseed:basics` category) with its BWG-gated reveal — a Patchouli `flag: "mod:biomeswevegone"` **plus** an
entry-level `advancement: "skyseed:reveal_exotic_woods"` that fires on obtaining any item in the inert
`#skyseed:exotic_woods` tag (empty without BWG → entry hidden on both backends; `generateGuide` maps the gate
onto Modonomicon too). All tag ids verified against the BWG 2.6.0 jar (v0.171.0, re-extended v0.173.0).

**#23 ✅ DONE (v0.186.0):**

- **Coherence pass complete** over the tracked `overrides/config/biomeswevegone/{misc,mob_spawn,trades,world_generation}.json`. Biome injection is **ON**, and every one of the **35 BWG biomes a Skyseed band targets is enabled** (`true`). The only disabled biome — `eroded_borealis: false` — is **targeted by no Skyseed band**, and holly (its supposed alternate host) is reachable via the enabled `dacite_ridges` (holly is keyed to `dacite_ridges` + its dedicated `holly_trees` feature in the forest bands), so its `false` is **intentional/coherent** — no change needed. (The other 19 disabled-nowhere BWG biomes aren't disabled; they're simply not yet targeted — the future Desert/Rocky/Frozen families per § Q2. If a future Frozen family targets `eroded_borealis`, flip it to `true` then.) `misc`/`mob_spawn`/`trades` are sane defaults. OTYG needs no config; create-otbwg is pure datapack.
- **`mods.txt` refreshed:** the bundled Skyseed jar bumped `skyseed-1.21.1_0.179.0.jar` → `skyseed-1.21.1_0.186.0.jar`.

**#10 ✅ DONE (2026-07-02):** the guide entry hides with BWG absent and appears once BWG is installed (both the
Patchouli book and the Modonomicon Almanac), AND the stale flavour text is fixed — `exotic_biomes.json` now reads
"Twenty wood families answer the Forest seed today" with the current list (aspen … rainbow eucalyptus + the enchanted
woods), verified against the forest wood bands. The stale text lived only in the Patchouli entry (the Modonomicon twin
did not carry it). Shipped v0.184.0.

## Step 5 — Quests — ✅ SHIPPED; sign-off open

Quests **B701–B703** under **Tools & Travel** (`overrides/config/ftbquests/quests/chapters/tools.snbt` +
`lang/en_us.snbt`): *Into the Wilds* (B701, advancement task on the hidden `skyseed:reveal_exotic_woods`,
depends on Skyseed B103), *Mill the Blooms* (B702, checkmark, depends on B701 + Create B204), *Grow Something
Grand* (B703, checkmark, depends on B701).

**★ Why an advancement, not a filter task.** First attempt used an `ftbfiltersystem:smart_filter` item task with `item_tag(skyseed:exotic_woods)` — **it does NOT work**: in-game FTB Quests treats the Smart Filter as a *literal item to obtain* (the "Valid items" screen shows only the filter), not as a tag expansion. Reusing the hidden reveal advancement (built for the Patchouli guide gate) is the robust fix — one hidden advancement drives both the quest and the guide entry. `ftb-filter-system` is therefore **not required** by this branch (harmless to keep as a general QoL mod). This is a standing trap for **all future tag-based quest tasks**.

**#3 ✅ DONE (2026-07-02):** in-game quest-book test-load passed — the B701–B703 branch works as intended (tasks/rewards resolve, incl. B703's Skyfarer's Cache roll).

## Plank coverage audit — 24 / 25 obtainable ✅ CLOSED (v0.173.0)

Ground truth for any "every BWG plank is obtainable" claim: BWG 2.6.0 ships **25 plank types**; **24 are
island-obtainable** via the Forest + Aquatic bands and `#skyseed:exotic_woods`. **fir is the single documented
exception** — BWG 2.6.0 ships `fir_planks`/`fir_sapling` but **no configured fir tree feature** exists, so fir
is non-growable by design; a gametest guards that no band references a `fir_*` feature.

## In-game test findings (2026-07-01, v0.171.0) — items #64–#66 (all ✅ RESOLVED: #64/#65 fixes SHIPPED v0.186.0 + re-throw signed off 2026-07-02; #66 resolved)

A user-run throw-a-seed pass over the shipped wet/fantasy wood bands. **Working:** enchanted ✅, skyris ✅,
white-mangrove ✅, palm ✅ (mangrove & palm grew plenty of trees); bands are **inert with BWG absent** (no
errors). Three groups of issues to address (do **not** fix inline — tracked as backlog items):

1. **Water feature too small / wrong shape (→ #64). ✅ DONE (v0.186.0, re-throw signed off 2026-07-02).**
   Every Aquatic wet-wood island (cypress, bayou/willow, white-mangrove, palm) and the Aquatic-cypress island read as
   "not enough water" — the Huge tier especially. A deep round `pond` was the wrong idiom for swamp/marsh woods. **Fix:**
   the four wet-wood bands on all three tiers (`biomeswevegone_aquatic{,_large}.json` + `biomeswevegone_huge_aquatic.json`)
   now carve a **broad, shallow swamp/marsh** — `depth: 2` with `slope: true` (basin floor eases to `1` at the shore) and
   a raised `extent` (0.6 / 0.62 / 0.68 for base / large / huge) so the water sheets across the island instead of sinking
   a deep well. `extent` is kept **under 0.7** on every tier so a wooded dry rim survives for the trees (#65). Guarded by
   the config gametest `biomeswevegoneWetWoodPondsAreShallowMarshes` (depth ≤ 2, `slope`, `extent` ≥ 0.6). **Re-throw
   sign-off:** confirm the water reads as a marsh in-game.
2. **Sparse / zero trees (→ #65). ✅ DONE (v0.186.0, re-throw signed off 2026-07-02 — Small grows ≥1 tree, Huge Bayou grows willows).**
   Small tiers grew **0 trees** (Aquatic cypress Small = 0; Forest cypress Small = 0) and **Huge Bayou grew 0 willow
   trees at all**. Observed cypress counts — Aquatic: Small 0 / Large 1 / Huge 5; Forest: Small 0 / Large ~5 / Huge ~8
   (the trees-first vs water-first priority *does* read — the Forest form is denser — it was the low/zero floor that was
   the problem).
   - **Root cause (confirmed by code audit):** in `DecorationPlanner.planDecoration` a `tries` loop picks a random
     surface column and defers each as a `TreeSite`; `GenerationJob` then calls `feature.place(...)` per site, and a
     **BWG tree that can't fit returns false and silently plants nothing**. The OTYG `tree_from_nbt_v1` trees are
     multi-block NBT structures (esp. **willow**/`bayou_trees` and the tall cypress) that need clear vertical + lateral
     room a tiny pad + a surface pond don't leave — so the floor was **placement failure**, not too-few `tries`.
   - **Fix shipped (lever b, generalised):** `GenerationJob` already had a `forceOneTree` last-resort guarantee, but it
     cleared only a single grass+air block — far too cramped for a big NBT tree, so it too failed and the island came up
     bare. `forceOneTree` now grades a real **5×5 dirt pad under a 24-tall air column** (`clearPlantingSpot`) at each
     candidate site before placing, stopping at the first success. It runs **only** when every normal placement already
     failed (`treesPlaced == 0`), so a healthy island is byte-identical. Base-tier wet-wood `tries` also lifted 4→6, and
     the shallower/broader-but-rim-preserving marsh (#64) leaves more flush plantable shore. This is a general engine fix
     (helps every theme, not just wet-woods).
   - **Willow-specific case (lever c) — NOT needed.** The 2026-07-02 re-throw confirmed the Huge Bayou now grows willows
     with the clearing in place, so `bayou_trees` *can* place on the Aquatic pad once given room — the fear that it needed
     water-adjacent mud didn't materialise. Lever (c) (smaller/water-adjacent willow variant) is left documented only as a
     fallback should a future change regress it.
3. **Spirit band (→ #66). ✅ RESOLVED (2026-07-02) — re-test passed: a forest seed grew spirit trees, band confirmed working (the earlier "no spirit" was the mislabelled-biome test). No code/data change.** A seed
   over `pale_bog` reportedly produced only **oak & birch, no spirit trees**. Root-cause audit against the jar + engine:
   `biomeswevegone:pale_bog` and `biomeswevegone:spirit_trees` both exist in 2.6.0; the band is present, prepended, and
   gametest-asserted; spirit uses the same `ohthetreesyoullgrow:tree_from_nbt_v1` feature type as the **working**
   enchanted/skyris. **Decisive:** a *matched* biome override **replaces** the island's variants, so a matched
   `pale_bog` band emits **only** `spirit_trees` and **can never produce oak/birch** — the seed was not over
   `pale_bog` (the same test pass self-contradicts with "pale_bog → white mangrove trees": a mislabelled biome).
   - **Action = re-test, not fix:** re-throw over a *confirmed* `pale_bog` (verify via F3/Debug tab first), and while
     there **confirm `pale_bog` is actually reachable** in the void multi-noise overworld (if TerraBlender doesn't place
     it, spirit would be de-facto ungrowable and would need re-keying to a reachable biome — the only remaining open
     sub-question). No band/code edit made.

## Tier coverage — base / large / huge (don't forget the tiers)

Every seed family is **three** themes: `forest` / `forest_large` / `huge_forest`, `desert` / `desert_large` / `huge_desert`, and so on. A `theme_override` targets exactly one theme, so each BWG adaptation set must be replicated across all three tier targets — the MA compat already does this (`mysticalagriculture_ancient` + `_ancient_large` + `_huge_ancient`). Author the `biome_overrides` content once, then emit one override file per tier target. **Budget the ×3 tier multiplier** when sizing any edit — as the shipped #64/#65/#22 work did.

## Design decisions (resolved — kept as the standing rules)

### Q2 — should other typed seeds respond to BWG biomes, not just forest? ✅ DISTRIBUTE (per typed seed, priority-ordered; 2026-07-01)

Each typed seed family gets its own BWG `theme_override` set adapting the BWG biomes in its climate — not everything piled on the forest generalist. Two refinements:

- **The same BWG biome may be adapted by more than one seed family, each with a different emphasis/priority.** The seed you throw decides the island's *character*. Example: `cypress_swamplands`/`bayou` grown from an **Aquatic** seed is **water-first** (ponds/rivers dominate, BWG trees secondary); the *same* biome from a **Forest** seed is **trees-first**. A **Lush** seed leans maximal/"extreme" nature; **Meadow** foregrounds the millable flowers; **Desert/Badlands** the arid palette; **Frozen** the cold palette; **Rocky** the stone/volcanic palette.
- **Per-seed "priority" = the ordering of what dominates the island** — surface/water vs trees vs ground flora vs plants — expressed through each override's `variants` decoration weights and `pond`/`surface_*` idioms.

**File convention** — one override set per participating seed family, ×3 tiers, mirroring the shipped sets and the MA/Create precedent:

```
biomeswevegone_<family>.json          (base tier   → theme <family>.json)
biomeswevegone_<family>_large.json    (large tier  → theme <family>_large.json)
biomeswevegone_huge_<family>.json     (huge tier   → theme huge_<family>.json)
```

`<family>` ∈ { `forest` ✅, `aquatic` ✅, `lush` ✅, `meadow` ✅, `desert`, `badlands`, `frozen`, `rocky`, `mushroom` } — add each remaining set as that family earns bands. Candidate biomes per family (confirm ids vs the jar when authoring): Desert/arid — `mojave_desert`, `atacama_outback`, `dead_sea` (golden_spined_cactus, sages); Rocky/volcanic — `basalt_barrera`, `dacite_ridges`, `dacite_shore`; Frozen/cold — `crimson_tundra`, `frosted_taiga`, `frosted_coniferous_forest`, `eroded_borealis`, `howling_peaks`, `canadian_shield`.

### Q3 — which BWG biomes deserve their OWN seed? ✅ Almost none

**The bar is non-growable, farm-worthy content.** A seed thrown over a BWG biome already grows that biome's island at every tier — and once you've chopped one tree you have its sapling and can regrow that wood anywhere. So a dedicated seed adds **nothing** for anything growable: wood, saplings, flowers and replantable plants are covered by adaptation + replanting. The only thing that earns a dedicated seed (item + recipe + advancement + quest) is content that **cannot be grown but is worth farming** — e.g. a biome-exclusive mob you'd build a spawner-island around. On the current content set nothing qualifies; check per biome if that changes.
