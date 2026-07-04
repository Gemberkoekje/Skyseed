# FARMERSDELIGHTPLAN — Farmer's Delight integration (NeoForge 1.21.1)

> Child of [CONTENTPLAN §2](CONTENTPLAN.md) — PLANOFPLANS **#16** (crop islands) and **#42** (quest chapter).
> Grounded in the **actual jars** now in `overrides/mods/` (versions below, verified 2026-07-03).

## Status

**✅ SHIPPED (both nodes green); in-game verify left.** The curated 8-mod FD set is in `overrides/mods/` + `mods.txt`,
the wild crops are re-homed onto seed-grown islands via `theme_override` (20 files — dry crops on Forest/Meadow/Desert,
rice in Lush/Aquatic ponds, chorus succulent, the nether powdery-cane feature), all gametest-guarded and byte-identical
without FD, and the **#42 quest chapter** (A008) is authored. **Left: an in-game throw-test** (crops appear + the
harvest→replant loop + the nether-cane feature grows) **+ a quest-book load.** (Same re-homing pattern as the
Mystical Agriculture ores, [MYSTICALPLAN.md](MYSTICALPLAN.md), and the BWG flowers.) The detail below is retained as
the reference for the id map + the standing curation/worldgen rules; the build-order log is collapsed to a pointer.

**Why FD earns the slot:** cozy farming/cooking is the pack's soul; it's the highest-ROI mod left in the backlog.
It doesn't overlap what's installed — Mystical Agriculture is *resource/automation* crops, FD is *food* crops; the
two are complementary layers. AppleSkin (saturation HUD, already in the pack) and Botany Pots (FD crops grow
land-free in pots) both pair with it for free.

## The curated set — what shipped, and why (the standing decision)

The "Delight" ecosystem is ~60 addons on 1.21.1; **curation is the whole game**, like the Quark module pass and the
Mekanism drop. Eight jars earn a slot; the rest is deliberately skipped.

### ✅ Installed (`overrides/mods/`)

| Jar | Role | Island integration needed? |
|---|---|---|
| `FarmersDelight-1.21.1-1.3.2.jar` | **The anchor** — cooking pot / skillet / cutting board / knives / crops. | **Yes** — inject the 7 wild crops onto biome islands (see below). |
| `ends_delight-2.6.1+neoforge.1.21.1.jar` | End-dimension cooking (chorus, shulker, dragon-egg dishes). | **Yes, small** — `ends_delight:chorus_succulent` onto the Chorus Forest island. |
| `MyNethersDelight-1.21.1-1.10.2.jar` | Nether cooking (hoglin/strider dishes, propelplant). | **Yes, small** — `bullet_pepper` + `powdery_cane` onto Nether seeds. |
| `oceansdelight-neoforge-1.0.4-1.21.1.jar` | Ocean/fish dishes. | **No** — ships **no blocks** (food/items only); works off existing fish. |
| `AutochefsDelight-1.21.1-NeoForge-2.0.3.jar` | Stacked-cooking + recipe-match **perf/QoL**. | **No** — no worldgen. |
| `chefsdelight-1.0.5-neoforge-1.21.1.jar` | **Cook/Chef villager professions.** | **No worldgen**; optional village-jigsaw workstation tie-in (nice-to-have). |
| `farmersdelight_extended-1.21.1-0.2.2.jar` | **Create ↔ FD** recipe bridge; **no new crops/mobs**. | **No** — but ⚠ version-verify (see below). |
| `choppersdelight-neoforge-1.21.1-1.2.0.jar` | Cutting boards for **all wood types** (pairs with 24 BWG + Quark woods). | **No worldgen**; ⚠ version-verify (see below). |

### ❌ Deliberately skipped (the standing "don't add" call)

- **Compat for mods the pack doesn't run** — Aquaculture / Undergarden / Cataclysm / Twilight / Aether / Croptopia /
  Nature's Spirit / Vampirism / Cobblemon / Ars Nouveau delights. Inert or pointless without their base mod.
- **Culture / novelty crossover packs** — Ramadan, Ube's, Gensokyo, Umapyoi, Greek, Slavic, Peruvian, Korean, Kacchi,
  Argentina's, Baldej, Easter's, Festive, Valheim, No Man's. Flavor bloat that each demand fresh crop injection.
- **The "more food" flood (pick ~0)** — More / Seed / Rustic / Veggies / Fruits / Pizza (×2) / Egg / Luncheon Meat /
  Corn / Dumplings / Roll / Sushi / Fizzy / Sauce / Coffee / Pineapple / Barbeque / Cuisine / Onion Onion / Vegan /
  Fright's / Dungeon's / Hearth & Harvest / Farmers Expanse / Oaks / Storage / Crate / Display Delight.
- **Wrong base/loader** — Farmer's Delight **TFC** (TerraFirmaCraft), **Refabricated** (Fabric).
- **Libraries/scripting** — Delight Lib, KubeJS Delight. *None of the 8 installed jars needs one* (deps checklist below).

*If FD ever feels thin after a playthrough, the one sanctioned depth pick is **Expanded Delight** or **ExtraDelight**
(more crops/workstations) — each adds injection work, so add at most one. Everything else stays out.*

## ⚠ Dependency & version verification — ✅ boot-verified 2026-07-03

All eight depend on **only** Farmer's Delight + NeoForge/Minecraft — **no extra library** is required. Three declared
version ranges that don't cleanly cover the installed **FD 1.3.2 / MC 1.21.1**; a boot + `latest.log` scan settled them:

| Jar | Declared range | Installed | Predicted risk → **actual** |
|---|---|---|---|
| `farmersdelight_extended-0.2.2` | `farmersdelight "1.21-1.2.8"`, `create [6.0.6,7.0.0)` | FD **1.3.2**, Create 6.0.10 | High → **loaded clean.** The unbracketed FD string is a *minimum*, not an upper bound, so NeoForge accepted it. No recipe/registry errors; only a missing `:farts` sound event (cosmetic). **Keep it**; eyeball its recipes in JEI once. |
| `choppersdelight-1.2.0` | `minecraft "[1.21,1.21.1)"` | MC **1.21.1** | Medium → **loaded clean.** No dependency-error screen, no errors. |
| `MyNethersDelight-1.10.2` | `farmersdelight "1.21-1.3"` | FD **1.3.2** | Low → **loaded clean.** No errors. |

**All eight mods loaded** (mod list confirmed) and the datapack reload threw **no error from any Delight mod** except
one bounded recipe casualty (below). NeoForge floor **≥ 21.1.219** is met by the pack.

### The one real issue (bounded, not from the flagged mods)
- **`farmersdelight:integration/silentgear/cutting/netherwood` fails to parse.** FD 1.3.2 ships its own Silent Gear
  cutting-board integration recipe using a `farmersdelight:tool_action` ingredient serializer that isn't registered in
  1.3.2 (an FD↔Silent Gear packaging drift, independent of the addons). **Impact:** you can't strip Silent Gear
  **netherwood** logs on a cutting board — one recipe, one modded wood; all 15,188 other recipes loaded. *Optional fix:*
  a tiny datapack that removes/rewrites that one recipe id. Not worth blocking on.

### Cosmetic warnings (no gameplay impact, optional)
- **Missing FD / FD-Extended sound events** (cooking-pot boil, stove crackle, skillet sizzle, `:farts`) — those actions
  play no sound. Likely a resourcepack override or FD packaging; harmless.
- **Ocean's Delight `guardian_soup`** renders a missing (purple/black) texture — its model references
  `farmersdelight:block/tray_pie_leftover`, removed/renamed in FD 1.3.2. Confirms Ocean's Delight 1.0.4 was built
  against older FD. One block, cosmetic; a resourcepack patch could supply the texture if it bothers.

*(Everything else alarming in `latest.log` — `create_things_and_misc` bad texture filenames, `dndecor`/`railways`
"Unknown registry key" loot-table spam for absent wood mods, Embeddium mixin-taint, Sodium/EMI/Lootr ClassNotFound
probes, the VersionChecker JSON parse fail — is **pre-existing, non-FD** pack noise unrelated to this integration.)*

## Compatibility / worldgen trap (the standing rule for any FD work)

FD's wild crops place through **placed features assigned to biomes by NeoForge biome modifiers**
(`farmersdelight:patch_wild_*`). The `skyseed:void` ChunkGenerator suppresses overworld/nether biome decoration, so
**FD wild crops never generate naturally** — identical to the MA-ore situation. Any FD crop source therefore must
carry its blocks through the theme system, never natural generation.

- **Inert without the mod:** a `theme_override` block id resolves via `Lookup.hasBlock` — an unknown id is silently
  skipped, so the overrides are **byte-identical generation without FD installed** and safe if FD's ids ever drift.
- **Gametest-guard** the new ids on both nodes (as the MA/BWG overrides are), so a future FD update that renames a
  block is caught by CI, not a player.

## Injection design (#16) — verified block ids

**Verified against the jars (2026-07-03).** FD wild-crop blocks:

```
farmersdelight:wild_cabbages   farmersdelight:wild_tomatoes   farmersdelight:wild_onions
farmersdelight:wild_carrots    farmersdelight:wild_potatoes   farmersdelight:wild_beetroots
farmersdelight:wild_rice   (DoublePlantBlock — 2-tall, waterlogged; belongs in pond water, not dry ground)
```

Each wild crop **drops its seed/crop item**, which the player replants as an ordinary farmland crop
(`cabbages` / `tomatoes` / `onions` / `rice`) — so a single injected patch is a **renewable** bootstrap, not a
one-shot. (Confirm in-game that the crops bonemeal/regrow on island soil and that rice grows water-adjacent — base FD
behaviour, expected clean.)

**Theme → crop map** (following FD's own wild-crop biome logic + [CONTENTPLAN §2](CONTENTPLAN.md)). Mirror every entry
to the `_large` and `huge_` tiers per the standing **mirror-island-tiers** rule — ground flora is per-column, so a
bigger island just grows proportionally more:

| Base theme(s) | Wild crops (as `ground` entries) | Notes |
|---|---|---|
| `forest`, `forest_large`, `huge_forest` | wild_onions, wild_potatoes, wild_carrots | Temperate/forest set. |
| `meadow` (+ tiers) | wild_onions, wild_carrots | Plains-like. |
| `desert` (+ tiers) | wild_tomatoes, wild_beetroots | Hot/dry set. |
| `badlands` (+ tiers) | wild_tomatoes | Hot; sparse. |
| `aquatic` (+ tiers) | wild_cabbages, wild_beetroots (sandy shore) **+ wild_rice in the pond** | Rice into the pond `plants`/`bank` list (waterlogged double plant — the engine already places both halves and waterlogs pond plants). |
| `lush` (+ tiers) | wild_carrots **+ wild_rice in the pond** | Has water for rice. |

Keep per-column `chance` low (≈0.02–0.05, matching the BWG flower fields) so crops read as a scattered wild find, not
a planted field — the player *makes* the field.

## Dimension delights (small, natural homes in existing chapters)

- **End's Delight → the Chorus Forest island.** Inject `ends_delight:chorus_succulent` (grows on end stone) onto
  `chorus_forest.json`. Everything else in the mod is cooked-food blocks (no worldgen).
- **My Nether's Delight → the Nether seeds.** Inject `mynethersdelight:bullet_pepper` (crop) and
  `mynethersdelight:powdery_cane` (nether reed) onto `nether_soul` / `nether_forest` (+ their `_large` tiers). The
  `crimson_fungus_colony` / `warped_fungus_colony` are configured features that can optionally hang off the crimson/
  warped seeds via a `trees`/feature entry. `resurgent_soil_farmland` is craftable — no worldgen.
- **Ocean's Delight → nothing to do.** Ships no blocks; its fish dishes work off existing aquatic life. Zero injection.
- **Chef's Delight → optional village tie-in.** The Cook/Chef professions attach to villagers the pack already spawns
  in Hamlet / Trade Post / Village Center. No worldgen required; *optionally* add their workstation to a village
  jigsaw pool so the profession is reachable in-world (nice-to-have, not a blocker).
- **Autochef's / Chopper's / FD Extended → no worldgen at all** (QoL / cutting boards / Create recipes).

## Quest chapter (#42, follows the integration per the rolling rule #19)

A **Farming & Cooking** FTB chapter, authored *after* the crops are seed-reachable so tasks point at real items
(the workflow the six shipped chapters used):

1. Find a wild crop on a biome island → get the first cabbage/tomato/onion/rice.
2. Craft the **cooking pot** + **skillet** → cook a first meal.
3. **Cutting board** (Chopper's, any wood) → knife recipes.
4. Advanced branches gated behind the chapters that unlock them: **My Nether's** dishes (post-portal),
   **End's** dishes (post-End), the **Create ↔ FD** recipes (FD Extended) as the automation tie-in.

Remember the standing **FTB tag-task trap**: smart-filter item tasks don't expand tags — use an advancement task.

## Build order — ✅ SHIPPED (both nodes green); detail in `CHANGELOG_1.21.1.md` + git

1. ✅ Boot smoke-test — all 8 load clean (one bounded netherwood cutting-recipe casualty + two cosmetic warns).
2. ✅ Base FD crop overrides — Forest/Meadow/Desert dry crops (9 files, 3 gametests) + a v0.2 discoverability tuning
   pass (bumped crop weights, added crop variants to the birch/dark/flower/taiga/jungle sub-biome bands that matched
   before the is_forest catch-all); rice on Lush (3 files) + Aquatic (3 files, incl. the `#is_river`/`swamp` band
   ponds), with a dedicated `wild_rice` case in `PondCarver.plantInPond` standing both halves at the water surface.
3. ✅ Dimension delights — chorus succulent on Chorus Forest (1 file); powdery-cane on Nether Soul + Nether Forest (4
   files) placed via its **configured feature** `mynethersdelight:patch_powdery_cane` (a raw ground block is the broken
   age-0 stub) — and the tree-feature `place()` path is now try/caught (`GenerationJob.placeFeatureSafely`, a defensive
   win for every modded feature). `bullet_pepper` intentionally skipped (no worldgen form). Ocean's/Chef's/Autochef's/
   Chopper's/FD Extended have no worldgen.
5. ✅ Quest chapter (#42) — `chapters/farmersdelight.snbt` (A008, B801–B808) + lang; gated off the Skyseed spine at
   B103, with a Rice branch + Nether/End branches (B110/B113); all `item` tasks on verified ids (no smart-filter tags).

**Left — in-game verify only:** throw Forest/Meadow/Aquatic/Lush, confirm crops appear + harvest→replant→regrow + rice
in the pond; confirm the nether powdery-cane **feature** grows a harvestable cane on both surfaces; quest-book load.
6. *(Optional)* Chef's Delight workstation into a village jigsaw pool.

## Open decisions

- **FD Extended keep/drop** — ✅ **keep.** Loaded clean on FD 1.3.2 (§ verification); the version-range flag was a
  false alarm. Just confirm its recipes look right in JEI during the first play.
- **Chef's Delight depth** — professions-only (zero work) vs. authoring the workstation into a village pool. Default to
  professions-only unless the trades feel unreachable in play.
- **Progression placement** — FD crops sit on the *early* biome islands (Forest/Meadow are starter-accessible), so the
  cozy layer is available from the start; the dimension delights gate naturally behind their chapters. No artificial
  tier needed (unlike the tech mods).
