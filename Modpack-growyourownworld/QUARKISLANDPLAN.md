# QUARKISLANDPLAN — Quark × Skyseed island integration (NeoForge 1.21.1)

Child of [QUARKPLAN.md](QUARKPLAN.md); PLANOFPLANS **#71**. Grounded in a 2026-07-02 codebase + jar pass (block ids
harvested from `Quark-4.1-481.jar`, mechanics read from the live theme/loot code). Turns QUARKPLAN's "island
integration opportunities" into committed work: **the more Quark content we route through islands, the better.**

> **Status: COMPLETE — all phases shipped (v0.184.0–v0.185.0), both nodes green.** Phase 1 stones+corundum (+ the
> myalite End remnant), Phase 2 blossoms across all three forest tiers, Phase 3 Ancient-Tome loot. Only the in-game
> look/loot sign-off remains (blossoms render, tomes actually drop). Quark itself is shipped + curated (see QUARKPLAN).
>
> **Design decision (2026-07-02): Quark gets NO dedicated seeds / island tiers.** All Quark material is an *optional
> extra* layered onto **existing** islands via `theme_override` (ores / decoration / scatter) — never its own seed.
> This resolves the Phase-4 corundum question below in favour of an enhancement, and keeps every phase a light,
> inert-without-Quark add-on. (Auto debug seeds from `ThemeScanner` are dev-only creative-tab items, not player seeds —
> unaffected by this call.)

## The lever (how Skyseed reuses a content mod's blocks)

Skyseed islands are data-driven `skyseed:theme` records, patched by `skyseed:theme_override` datapack entries — the
exact mechanism the **Create** (`create_rocky.json` → zinc) and **Mystical Agriculture** (`mysticalagriculture_ancient.json`
→ inferium/prosperity) first-party compat overrides already use. A `theme_override` names a `target` theme and adds
`ores` / `variants` / `biome_overrides` / palette on top of it; multiple patches stack (id order). New content also
gets **auto debug seeds** for free — `ThemeScanner` derives one per added `biome_overrides`/`rare_structures` entry, so
no seed list to hand-maintain.

### ⚠ The inert-without-mod rule (decides every design choice below)

Skyseed ships in **both** packs (full = has Quark; vanilla-like = no content mods), so an override that references a
Quark block must degrade cleanly when Quark is absent. The resolvers do **not** all behave the same — verified in
[IslandGenerator.java](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGenerator.java) / `OrePlanner`:

| Field | Unknown-block behaviour | Safe for modded blocks? |
|---|---|---|
| `ores` | **skipped** before any RNG (byte-identical) | ✅ yes — the Create/MA pattern |
| `decoration.ground` (surface plants) | **skipped** | ✅ yes |
| `decoration.trees` (configured feature) | **skipped** | ✅ yes |
| `surface_scatter`, `fill_bands` | **skipped** | ✅ yes |
| `jigsaw.centerpiece` | **skipped** (`filter(hasBlock)`) | ✅ yes |
| `palette` surface/fill/core (& variant overrides) | **falls back to grass/dirt/stone** + logs a warning | ⚠ generates, but NOT byte-identical, and warn-spams the no-Quark pack |

**Rule:** prefer `ores` / `decoration` / `surface_scatter` (truly inert). Use a modded **palette / full-body variant**
only when a distinct look is worth the vanilla-pack fallback (island still builds, just in stone, with a warning).

## Confirmed Quark ids (harvested 2026-07-02)

- **Stones** (`New Stone Types`, kept ON): `quark:limestone`, `quark:jasper`, `quark:shale`*, `quark:myalite` — each
  with full `_bricks`/`_slab`/`_stairs`/`_wall`/`_pillar`/`polished_*` sets (players craft those from the raw block).
  Myalite is End-flavoured (`quark:dusky_myalite`, `quark:myalite_crystal`). *(\*verify `quark:shale` base id + the
  `quark:slate` alias the config uses in `big_stone_clusters` — harvest from the jar as the MA/Create overrides were.)*
- **Corundum** (`Corundum`, kept ON): `quark:<c>_corundum`, `quark:<c>_corundum_cluster`, `_lamp`, `_pane` for
  `c ∈ {white, orange, yellow, green, blue, indigo, violet, red, black}`. Clusters grow like amethyst.
- **Blossom** (`Blossom Trees`, kept ON): saplings `quark:<c>_blossom_sapling`, leaves `quark:<c>_blossom_leaves`,
  hedges/leaf-carpets, wood set (`quark:blossom_log`/`_planks`/…) for `c ∈ {blue, lavender, orange, yellow, red}`.
  Configured tree features (for `decoration.trees`): `quark:blue_blossom`, `quark:lavender_blossom`,
  `quark:orange_blossom`, `quark:yellow_blossom`, `quark:red_blossom`. Quark's own biome map (from its config) is our
  guide: blue→snowy, lavender→swamp, orange→savanna, yellow→plains, red→badlands.
- **Ancient Tomes** (`Ancient Tomes`, kept ON): item `quark:ancient_tome`; Quark injects it into vanilla structure
  loot tables via its **own config** (not a Skyseed GLM — see Phase 3).

---

## Phase 1 — Quark stones on the mining islands ✅ SHIPPED (v0.184.0)

**Goal:** limestone/jasper/shale become mineable on the Rocky/Ancient tiers; myalite on their End form.

> **Shipped (v0.184.0):** six `theme_override`s — `quark_{rocky,rocky_large,huge_rocky,ancient,ancient_large,huge_ancient}.json`
> — add limestone + jasper (Rocky) / jasper + shale (Ancient) as core ore-veins, plus a deep `blue_corundum` geode
> (Phase 4, folded in). Ore-vein route chosen (the additive, always-inert path). Gametests
> `quarkStonesCompatTargetsRocky`/`…Ancient` (both nodes). **Myalite End remnant ✅ SHIPPED (v0.185.0):** a
> `dimension: the_end` band on `quark_rocky`/`quark_ancient` selector-merges into the base End band (confirmed
> `useBase=false` off-home-dim means the End band must — and now does — supply its own ores). Gametest
> `quarkMyaliteReachesEndForm`.

**Mechanism:** `theme_override` files targeting the mining themes — mirror `create_rocky.json`. Two safe routes:
- **Default (clean in both packs):** stones as **`surface_scatter`** accents + **ore-style veins** (an `ores` entry
  whose "ore" is the plain stone block → limestone/jasper blobs in the body). Both skip cleanly without Quark.
- **Not chosen (per the "extras only" call):** a full-body **variant** (`surface/fill/core_override` → `quark:limestone`)
  would give a distinct limestone-island look, but it re-skins the island rather than adding to it and falls back to
  stone + warns in the no-Quark pack — skip unless you later decide you want it.

**Files (new):** `theme_override/quark_rocky.json`, `…_rocky_large.json`, `…_huge_rocky.json`, and the same for
`ancient*` (deepslate-bodied → jasper/shale read well there). Myalite via the rocky/ancient **End** `biome_override`
(`dimension: the_end`) as scatter/vein.

**Sketch** (`quark_rocky.json`, scatter+vein route):
```json
{ "_comment": "Quark stones on the rocky mining island. Inert without Quark (scatter + stone-vein 'ores' skip).",
  "target": "skyseed:rocky",
  "ores": [ { "block": "quark:limestone", "chance": 0.6, "count": {"min":2,"max":3}, "vein_size": {"min":4,"max":8}, "depth": "core" },
            { "block": "quark:jasper",    "chance": 0.4, "count": {"min":1,"max":2}, "vein_size": {"min":3,"max":6}, "depth": "core" } ] }
```
**Gametest:** one per band (auto debug seed appears via ThemeScanner); assert the stone block present with Quark, and
island byte-identical / no warning without Quark.

## Phase 2 — Blossom saplings on the biome islands ✅ SHIPPED (v0.185.0)

**Goal:** players can obtain the five blossom saplings + get the decorative trees on the matching biome-island bands.

> **Shipped (v0.185.0):** `quark_forest{,_large,_huge}.json` append a blossom-grove **variant** (equal weight) to the
> Forest tiers' snowy / swamp / savanna / plains / badlands bands — Quark's own biome map (blue/lavender/orange/yellow/red).
> The variant grows the biome's normal tree PLUS the blossom (`decoration.trees` → `quark:<c>_blossom`) and scatters the
> harvestable sapling (`decoration.ground` → `quark:<c>_blossom_sapling`). Selectors are byte-identical to the base
> bands so they MERGE (append), not shadow — verified for all 5 biomes × 3 tiers by `quarkBlossomBandsMergeOntoForestTiers`
> (both nodes). Inert without Quark (unknown features/blocks skipped).

**Mechanism:** `decoration` on a biome band (see how `forest.json` places `minecraft:cherry` on `cherry_grove`):
- `decoration.trees`: `{ "feature": "quark:<c>_blossom", "tries": 3, "spacing": 3 }` — grows the tree (look + saplings
  from leaf breakage). Skips without Quark (same as the pale_oak "version-inert" trees).
- `decoration.ground`: `{ "block": "quark:<c>_blossom_sapling", "chance": 0.05 }` — a harvestable sapling on the grass.
  (Use `ground`, **not** `surface_scatter` — scatter *replaces* the surface block; a sapling needs grass beneath it.)

**Biome map → Skyseed themes** (via `theme_override` bands matching the biome, mirroring `create_rocky`'s deep-band
patch so it merges into the existing band rather than shadowing it):

| Blossom | Feature | Target band |
|---|---|---|
| blue | `quark:blue_blossom` | `frozen` / forest snowy bands |
| lavender | `quark:lavender_blossom` | `forest` swamp / `aquatic` swamp |
| orange | `quark:orange_blossom` | `#is_savanna` band |
| yellow | `quark:yellow_blossom` | `meadow` / plains band |
| red | `quark:red_blossom` | `#is_badlands` band |

**Open detail to verify at build time:** `BiomeOverride.mergedWith` variant/ground merge semantics (append vs replace)
— confirm the patch adds to the band's decoration rather than replacing it; adjust selector to match.

## Phase 3 — Ancient Tomes in structure-island loot ✅ SHIPPED (v0.185.0, config)

> **Shipped (v0.185.0):** `quark-common.toml` `[tools.ancient_tomes] "Loot Tables"` now adds the **Trial Chamber**
> reward chests (`trial_chambers/reward,10` + `reward_ominous,12`) and re-enables **Nether fortress** chests
> (`nether_bridge` `0`→`8`), so tomes drop in those structure islands too. In-game: verify tomes actually appear.

**Key finding:** Skyseed's structure islands fill their chests from the **vanilla** loot tables
(`minecraft:chests/simple_dungeon`, `woodland_mansion`, `bastion_treasure`, `nether_bridge`, `trial_chambers/reward`,
… — confirmed against the relic GLMs in `data/skyseed/loot_modifiers/`). Quark's **Ancient Tomes** config already
injects tomes into `simple_dungeon:20`, `woodland_mansion:15`, `bastion_treasure:25`, `ancient_city:4`,
`stronghold_library:20`, `quark:monster_box:5`. **So tomes already drop in the dungeon / mansion / bastion / ancient-city
islands today** — and because a re-grown island re-rolls the table, they're already "rare but farmable" (the SKYEND relic
philosophy).

**Work:**
1. **Verify** in-game that tomes appear in those islands.
2. **Tune** Quark's `[tools.ancient_tomes] "Loot Tables"` in `overrides/config/quark-common.toml`: add the **Trial
   Chamber** reward tables (`minecraft:chests/trial_chambers/reward,10`, `…/reward_ominous,12` — Skyseed has a Trial
   Chamber island, and Quark's default omits it) and reconsider `nether_bridge` (currently weight `0`).
3. **Do NOT** use a Skyseed `AddDropModifier` GLM here — its item codec (`quark:ancient_tome`) would fail to load in the
   no-Quark vanilla-like pack. Quark's own config is the mod-present-only lever, which is exactly what we want.

## Phase 4 — Corundum as a crystal-geode extra on the mining islands ✅ SHIPPED (v0.184.0, folded into Phase 1)

**Decision applied (2026-07-02):** corundum is an *optional extra* on the existing **Rocky / Ancient** mining islands
(and their `_large`/`huge` tiers) — a crystal geode you find while mining, **not** a bespoke crystal island. It rides
the very same `theme_override` files as Phase 1 (`quark_rocky.json` / `quark_ancient.json`), so there's no new plumbing.

**Mechanism:** a deep **`ores`** vein of solid `quark:<c>_corundum` (a geode pocket, `depth: deep_core`), optionally
dressed with a low-chance cluster accent (`quark:<c>_corundum_cluster`) via the surface `decoration`/`ground` layer.
Corundum has **no worldgen feature**, so it's pure block placement — `ores` for the crystal blocks. Pick one hue per
island (seeded) so a geode reads as a single crystal type, not a rainbow.

**Sketch** (folded into `quark_rocky.json`'s `ores`):
```json
{ "block": "quark:blue_corundum", "chance": 0.30, "count": {"min":1,"max":2}, "vein_size": {"min":3,"max":6}, "depth": "deep_core" }
```
**Inert without Quark:** the corundum ids skip cleanly (Phase-1 rule) — byte-identical in the vanilla-like pack.

---

## Cross-cutting

- **Debug seeds:** free via `ThemeScanner` for every added band / rare structure — no list edits.
- **Gametests:** one per new band/theme, asserting (a) the Quark block/feature present with Quark and (b) inert
  (byte-identical, no warning) without it. Run both nodes with the node-explicit tasks.
- **Two version nodes:** all four phases are data-only (theme_override JSON + one config edit) — no `//?` code, so
  1.21.1 and 26.1.2 share the files unchanged.
- **Vanilla-like pack:** every phase is byte-identical there now — all additive `ores`/`decoration`/scatter, no new
  tier, so the seed/palette-fallback concerns are moot; Phase 3 doesn't touch that pack (no Quark).

## Build order

1. ✅ **Phase 1 + 4** (stones + corundum on Rocky/Ancient) — SHIPPED v0.184.0; **myalite on the End form** SHIPPED v0.185.0.
2. ✅ **Phase 3** (Ancient Tomes) — config tune SHIPPED v0.185.0 (Trial Chamber + Nether-fortress tables).
3. ✅ **Phase 2** (blossom) — SHIPPED v0.185.0 (all 3 forest tiers, merge verified).
4. Per-step: bump `mod_version`, both-node CHANGELOG entries, both-node build (standing rule). **All done.**

**Remaining: only the in-game sign-off** — blossoms render on the right biome islands + drop saplings; Ancient Tomes
actually appear in the dungeon/mansion/bastion/ancient-city/trial/fortress islands.

## Open decisions

- **Phase 4:** ✅ RESOLVED — corundum is an extra on Rocky/Ancient, no new tier/seed (the 2026-07-02 "no Quark seeds" call).
- **Phase 1:** ✅ RESOLVED — additive scatter + ore-veins only (no full-body re-skin variant, per the "extras only" call).
- **Phase 3:** ✅ RESOLVED — tome weights set (Trial Chamber `10`/`12`, Nether fortress `8`); tune after the in-game read.
