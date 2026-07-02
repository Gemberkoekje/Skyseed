# SKYNETHERENDBIOMEPLAN — Per-biome Nether & End seed adaptation

**Goal (user, 2026-07-02):** existing seeds should look *different depending on the Nether / End biome they
germinate in* — the same "throw a Forest seed over cherry_grove → cherry island" richness the overworld
already has, extended into the Nether and the End. **No new seeds.** Plan-first; execution gated on sign-off
of the design fork in §3.

## 0. Premise correction — there are NO modded Nether/End biomes

This work was requested as "adapt seeds to the Oh-The-Biomes-We've-Gone Nether/End biomes." **BWG
(`biomeswevegone` 2.6.0) is an overworld-only biome mod** — verified against the jar: it registers a single
TerraBlender region (`BWGTerraBlenderRegion` → `TerrablenderOverworldBiomeBuilder`), no Nether/End region;
no `is_nether`/`is_end` tags; no crimson/warped/end biomes (the `soul_sand_valley` strings in a few BWG
biomes are just music refs). The modpack ships no other biome mod. *(The old "Oh The Biomes You'll **Go**"
1.16–1.19 had nether/end biomes; the "…We've **Gone**" 1.20+ rewrite dropped them.)*

**Therefore this pass targets the 5 vanilla Nether + 5 vanilla End biomes only.** It is core-mod content
authored **inline** in the base theme JSONs (exactly like the existing `forest.json` warped/crimson entries),
**not** a `biomeswevegone_*` / modpack `theme_override` band. Recorded in memory as
`bwg-overworld-only-no-nether-end`.

## 1. The 10 target biomes

| Nether (5) | End (5) |
|---|---|
| `nether_wastes` | `the_end` (central dragon-fight island) |
| `crimson_forest` | `end_highlands` |
| `warped_forest` | `end_midlands` |
| `soul_sand_valley` | `end_barrens` |
| `basalt_deltas` | `small_end_islands` |

**Reachability (Phase-0 check):** the skyblock preset uses "normal" Nether/End (multi-noise biome sources over
void noise settings), so all biomes exist in the biome map — but `basalt_deltas` / `soul_sand_valley` are
rarer, and the End's outer biomes only appear past the main island. Confirm each is standable-in via F3 before
sign-off (cf. the BWG-village reachability sign-off, PLANOFPLANS #14).

## 2. Current state (audited 2026-07-02, mod 0.181.0)

**Nether** — overworld seeds carry a `dimension:the_nether` form keyed by **seed identity, not biome**:

| Seed | Current Nether form | Biome-aware? |
|---|---|---|
| Forest | warped→teal fungal patch / else→crimson patch | **yes** (only one) |
| Rocky | netherrack mining rock (deep band + surface, Y-gated) | no |
| Desert | soul_sand / soul_soil / basalt | no |
| Badlands | basalt-deltas palette | no |
| Aquatic | lava lagoon | no |
| Ancient | haunted blackstone | no |
| Mushroom | nether mushroom island | no |
| Lush | vine grotto | no |
| Frozen / Meadow | *no nether form → fizzles* | n/a (by design) |

**End** — every overworld seed → a same-silhouette **bare end-stone** island (biome-agnostic). Only Forest &
Lush split: central `the_end` = empty building platform; outer biomes = small island + a little chorus. The
dedicated `chorus_forest` (chorus/purpur economy) and `end_city` (fizzle-gated to highlands/midlands) seeds are
the "real" End content. Per-end-biome re-skin was previously **deferred as low value** (void End keeps players
near the arrival island) — still true; the End pass here is deliberately **light** (§5).

**Tier scope:** dimension forms live on the **base + `_large`** themes only. `huge_*` themes have no
nether/end form (top overworld tier — out of scope). Structure/ladder themes out of scope.

## 3. ★ Design fork (needs sign-off before authoring)

Two coherent ways to make an island "biome-dependent" in the Nether:

- **(A) Seed = island identity, biome = flavor accents** *(recommended — mirrors the overworld).* A Rocky seed
  is *always* a netherrack mining rock, but its `surface_scatter` / ground plants / underside vines / mobs
  shift to the biome it's thrown in (crimson roots & shroomlight in `crimson_forest`; warped sprouts & twisting
  vines in `warped_forest`; soul soil & bone in `soul_sand_valley`; basalt & magma in `basalt_deltas`; sparse
  quartz in `nether_wastes`). Keeps each seed's ore/lava identity; maximizes variety (8 seeds × 5 biomes = 40
  distinct looks) from a small reusable "biome kit" set. Consistent with "Forest is always a forest."
- **(B) Biome = island type, seed = minor flavor.** The biome fully determines the island (throw *any* seed in
  `soul_sand_valley` → a soul-sand island). Fewer distinct results; collapses the seed's identity; contradicts
  the overworld precedent. **Not recommended.**

This plan is written for **(A)**. It also means the current "Desert seed == soul-sand-valley island by name"
becomes "Desert seed keeps a soul-sand base **and** picks up cross-biome accents" — slightly more variety,
same recipe/identity. Confirm (A) vs (B) before Phase 1.

## 4. Nether pass (model A) — the five "biome kits"

Each kit is a small, biome-signature bundle layered via a `biome_override`
(`dimension:minecraft:the_nether` + `biomes:[<one biome>]`, ordered specific-first, keeping the seed's base
`surface/fill/ores/lava`; unset fields fall back to the seed base since same-dimension). A kit sets only:
`surface_scatter` (per-column tint), one `variants` entry (`ground` + `underside`), and `mobs`.

| Kit | `surface_scatter` | `ground` accents | `underside` | `mobs` |
|---|---|---|---|---|
| **nether_wastes** | `nether_quartz_ore` 0.04, `glowstone` 0.02 | `nether_wart` 0.03 | — | zombified_piglin, magma_cube |
| **crimson_forest** | `crimson_nylium` 0.5 | `crimson_roots` 0.25, `crimson_fungus` 0.06, `shroomlight` 0.03 | `weeping_vines` 0.15 | hoglin, piglin, zombified_piglin |
| **warped_forest** | `warped_nylium` 0.5 | `warped_roots` 0.25, `warped_fungus` 0.06, `nether_sprouts` 0.1, `shroomlight` 0.03 | `twisting_vines` 0.15 | enderman |
| **soul_sand_valley** | `soul_soil` 0.4, `soul_sand` 0.2 | `bone_block` cluster 0.05 | — | skeleton, magma_cube |
| **basalt_deltas** | `basalt` 0.4, `blackstone` 0.2, `magma_block` 0.05 | `gilded_blackstone` 0.02 (accent) | — | magma_cube |

**Verification caveat (from #65, wet-wood):** prefer **`ground`/`surface_scatter` block placement** over
configured *features* for the fungi/vegetation — some vanilla nether-veg features silently fail to place on
small floating pads. Nether **trees** (a lone crimson/warped stem+shroomlight cap) can be added later as a
hand-built custom feature if desired (out of scope for v1).

**Worked example — Rocky in `crimson_forest`** (added to `rocky.json`, before the generic nether bands):

```jsonc
{ "_comment": "Nether biome flavor — Crimson: netherrack rock dressed in crimson fungal growth.",
  "dimension": "minecraft:the_nether", "biomes": ["minecraft:crimson_forest"],
  "surface_scatter": [ { "block": "minecraft:crimson_nylium", "chance": 0.5 } ],
  "variants": [ { "weight": 1, "name": "crimson", "decoration": {
    "ground":   [ { "block": "minecraft:crimson_roots", "chance": 0.25 },
                  { "block": "minecraft:crimson_fungus", "chance": 0.06 },
                  { "block": "minecraft:shroomlight",    "chance": 0.03 } ],
    "underside":[ { "block": "minecraft:weeping_vines",  "chance": 0.15 } ] } } ],
  "mobs": [ { "entity": "minecraft:hoglin", "chance": 0.25, "count": {"min":1,"max":2} },
            { "entity": "minecraft:piglin", "chance": 0.25, "count": {"min":1,"max":2} } ] }
```

**Ordering per theme:** the 5 specific-biome overrides first, then the existing generic `dimension:the_nether`
fallback last (so an unlisted state still works). Forest already has warped+crimson — extend it to all 5 for
consistency (or leave its richer bespoke fungal-patch forms and only add soul/basalt/wastes kits).

**Frozen / Meadow:** stay fizzled in the Nether (design decision unchanged) — no kits.

## 5. End pass (light) — per-end-biome accents

The End stays low-value, so this is a texture pass, **not** a rebuild, and must **not** duplicate the
`chorus_forest` / `end_city` seeds. Generalize the Forest/Lush central-vs-outer split to **all** seeds:

| End biome | Accent on the seed's bare end-stone form |
|---|---|
| `the_end` (central) | **unchanged** — clean end-stone building platform, no chorus (keep the dragon-fight staging clear) |
| `end_highlands` | `purpur_block` surface_scatter 0.1 + sparse chorus (`chorus_plant` tries 2) + rare `end_rod`; rare shulker |
| `end_midlands` | `purpur_block` surface_scatter 0.08 + occasional `end_rod` |
| `end_barrens` | bare + rare `end_rod`; slightly reduced radius |
| `small_end_islands` | reduced radius, bare |
| all | sparse `enderman` mob |

Kept lighter than `chorus_forest` (which stays the bulk-chorus/purpur seed). This is the **lowest-priority**
part of the pass — reasonable to ship Nether first (§7) and treat End as a follow-up.

## 6. Engine check (no code changes expected)

`BiomeOverride` already supports everything above: `dimension`, `biomes`, `surface_scatter`, `variants`
(`decoration.ground` + `decoration.underside`), `mobs`, `shape`. `Decoration.underside`/`hangUnder` builds the
vine strands (same path lush cave-vines use). So this is **data-only** (theme JSON) — no Java. Confirm during
Phase 1 that `crimson_roots`/`warped_roots`/`nether_sprouts` place as `ground` blocks (they're plants needing a
solid block below — the ground loop already checks support).

## 7. Phasing & scope

| Phase | Work | Files |
|---|---|---|
| **0** | Sign off design fork §3 (A vs B); F3-confirm all 10 biomes reachable in the void Nether/End | — |
| **1** | Author the 5 Nether kits; apply to **base**-tier of the 8 adapting families | 8 theme JSONs |
| **2** | Replicate Nether kits to the **`_large`** tier | 8 theme JSONs |
| **3** | End light pass — **base** tier, all 10 families | ~10 theme JSONs |
| **4** | Replicate End pass to **`_large`** tier | ~10 theme JSONs |
| **5** | In-game sign-off: throw each seed across each Nether/End biome; verify accents + reachability | — |

~36 theme-file edits total, data-only. Per the standing rules: ship an **inert golden-master gametest** per
kit-set (assert the biome overrides parse + select correctly), and **bump `mod_version` (minor) + add the
per-node CHANGELOG entry in the same commit** (`bump-version-on-commit`). Reload gotcha applies — the
`skyseed:theme` datapack registry needs a **server restart** (not `/reload`) to re-read theme JSON.

## 8. Open decisions for the user

1. **Design fork §3: (A) seed-identity + biome-flavor [recommended] vs (B) biome-determines-type.**
2. **End scope:** do the light End pass (§5) now, or Nether-only and defer End (given its known low value)?
3. **Forest in the Nether:** keep its richer bespoke warped/crimson fungal-patch forms and only add the 3
   missing kits (soul/basalt/wastes), or normalize Forest to the shared kit model for all 5?
4. **Nether trees:** ship v1 as ground-scatter only, or invest in a hand-built crimson/warped "stem tree"
   custom feature (like the mangrove/azalea hand-builds)?
