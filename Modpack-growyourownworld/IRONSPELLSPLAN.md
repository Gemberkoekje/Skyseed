# IRONSPELLSPLAN — Iron's Spells + the Explore seed (NeoForge 1.21.1)

> Child of [CONTENTPLAN §2](CONTENTPLAN.md) — PLANOFPLANS **#36** (loot/mob injection), **#37** (scope decision),
> **#46** (quest chapter). Grounded in the **actual jars** now in `overrides/mods/` and the **actual engine**
> (`IslandGenerator`, `RareStructure`, `AddDropModifier`), verified 2026-07-03.

## Status

**Jars in. Phase 1 — the Explore seed — SHIPPED + gametested on both nodes; the magic-loot layer is next.**

- ✅ **The adaptive Explore Skyseed.** One `explore_skyseed` that, on germination, resolves the local biome to the
  dedicated theme its own seed would grow ([`ExploreThemes`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/theme/ExploreThemes.java))
  and **forces a biome-fitting rare structure** onto it ([`IslandSeedEntity`](../src/main/java/dev/gemberkoekje/skyseed/entity/IslandSeedEntity.java)
  — reuses the existing forced-rare path, weighted, `hasTemplatePool` inert-guarded). **Full seed onboarding, no
  shortcuts:** in `SEED_THEMES`; the plus-shaped **ring recipe** (`F/A/D/M` around `#skyseed:structure_seeds`, authored
  modern-form so the 1.21.1 downgrade + 26.1.2 both load); a real `explore.json` **fallback theme** (unmapped biomes);
  `craft`/`gathered`/`reveal_explore` advancements; a Patchouli entry that **auto-derives** the Modonomicon entry;
  item model + unique `island_seed_explore` icon; lang; `skyseeds` tag. Guarded by
  `exploreSeedResolvesBiomesToDedicatedThemes` on both nodes — **166 (1.21.1) / 164 (26.1.2) tests green.**
- Because it forces the biome themes' **existing** `rare_structures`, the exploration loop already works with the
  shipped skyseed structures (trail ruins, evoker cell, desert temple, igloo, ocean ruin, vault cell, …).
- ⏭ **Overworld-only** for now (thrown in the Nether/End it fizzles back, like the overworld biome seeds).
- ✅ **The loot layer (§Loot).** The mods **auto-inject** their loot into the vanilla chest tables the explore
  structures roll (breadth, free), plus a curated **inert-safe** `skyseed:add_drop` precision layer (Arcane Essence
  common / Upgrade Orb rare-in-deadly). `AddDropModifier` was made tolerant (item as an `Id`, resolved via
  `Lookup.hasItem`) so mod-item GLMs stay inert without the mod — CI-gametested on both nodes.

- ✅ **Biome-theme rares (base tiers).** The six base biome themes that lacked a rare now each carry a biome-fitting
  one (desert/badlands → desert temple, rocky/lush → trail ruins, meadow → evoker cell + evoker, mushroom → witch hut +
  witch; lush `suppress_pond`s its pond) — so a normal throw has the 5% surprise **and** the Explore seed forces one in
  those biomes. Placement gametested both nodes (`exploreBiomeThemesForceTheirRareStructure`).

- ✅ **Magic-mob guardians + the mod-structure pattern.** The evoker cells (forest/meadow) now spawn an Iron's
  **archevoker** beside the vanilla evoker (inert-safe via `MobPlanner`). `rollRare` gained the **`hasTemplatePool`
  inert guard** (an absent mod pool is skipped *before* the chance roll — no bald pad, no RNG shift), so Iron's own
  jigsaw structures can ship as rares 1:1: the first, **`mangrove_hut` on Lush**, is in as the pattern (experimental —
  needs an in-game fit check; move to `lush_large` if it's too big). Both gametested inert on both nodes.

- ✅ **Iron's own structures wired 1:1, size-gated (experimental).** All the mod's jigsaw structures now ship as
  inert-safe rares: `impaled_icebreaker`→`huge_aquatic` *(cold-ocean gated — moved off `aquatic_large`, see the fit fix below)*, `mountain_tower`→`rocky_large`, `mangrove_hut`→`lush`; the big
  dungeons gated to `huge_` — `evoker_fort`→`huge_forest`, `ice_spider_den`→`huge_frozen`, `pyromancer_tower`→
  `huge_desert`, `ancient_battleground`→`huge_badlands`, `citadel`→`huge_rocky`. Each points at the mod's own pool
  (`irons_spellbooks:*`), kept inert without the mod by the `rollRare` guard; every pool is asserted absent in CI by the
  extended inert test. **These are un-verifiable in CI (inert by design) and need in-game throw-tests** — per the
  owner's note most Iron's structures are large/underground, so expect to tune `depth`/`pad` or drop the ones that clip
  or punch into the void. **Playtest fix (2026-07-03):** a mod pool has no `minecraft:bottom` jigsaw (that's skyseed's
  own start-jigsaw convention), so leaving the default `target` made vanilla find no start piece and place *nothing*
  ("No starting jigsaw minecraft:bottom found in start pool …"). All 8 now use **`"target": ""`** (empty → the mod
  pool's own default start element, since no Iron's structure def sets a `start_jigsaw_name`); guarded by
  `modStructureRaresAreInertWithoutTheMod`. `catacombs` was deliberately left out (it digs straight down — impractical
  on a floating island without a bespoke pad).

- ✅ **Impaled Icebreaker fit fix + a custom small boat (2026-07-04).** First in-game throw-test result: the mod's
  **Impaled Icebreaker is too big for `aquatic_large`** (radius 11-14) — the ship overhung the pad and floated out over
  the void. Two-part fix, per the owner's "both tiers, keep the pond + add ice" call:
  - **Full ship → `huge_aquatic`** (radius 28-32, which holds it), **gated to frozen/cold oceans** (`frozen_ocean`,
    `deep_frozen_ocean`, `cold_ocean`, `deep_cold_ocean`) so it only germinates as a *frozen-sea* wreck ("cold/icy huge
    aquatic"). The pond is **kept** (no `suppress_pond`) so the ship sits over the big lake, its own blue-ice spikes +
    a new **cold-ocean biome override** (snow surface, ice spikes, cold water/fish, polar bears — ordered before the
    warm `is_ocean` override) supplying the ice. Still `"target": ""` (mod pool default start).
  - **Custom skyseed [Impaled Boat](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/RareStructureTemplates.java)** — our own hand-built small answer, a wrecked
    spruce longboat *beset in a packed/blue-ice floe* (with open water leads) and **impaled on a blue-ice spike**, a
    snapped mast + tattered sail, a soul-lantern, and an `underwater_ruin_big` chest (so Iron's essence reaches it via
    the GLM). Self-contained (brings its own ice + water), all vanilla blocks → ships in the base mod. Wired as a rare on
    **`aquatic_large`** (frozen/cold-ocean gated, replacing the removed full ship) and **`frozen_large`** (ungated).
    Pool `skyseed:impaled_boat/wreck`, `.nbt` dev-generated. Assembly + loot chest gametested both nodes
    (`impaledBoatAssembles`); `modStructureRaresAreInertWithoutTheMod` now asserts the icebreaker on `huge_aquatic`.

- ✅ **Large & Huge Explore seeds.** `explore_large_skyseed` + `huge_explore_skyseed` resolve the biome to its
  `_large` / `huge_` theme (Forest as the unmapped fallback), so the exploration seed reaches the big structures a
  small isle can't host (`ExploreThemes.resolveLarge`/`resolveHuge`; the entity dispatches by which sentinel the seed
  carries). Full onboarding — ring recipes over the tier's biome seeds (+ a progression gate: large reveals after a
  base explore, huge after a large), advancements, Patchouli→Modonomicon entries, lang, `skyseeds` tag — and **drawn
  compass icons** (teal `explore` / silver `explore_large` / gold `huge_explore`, via System.Drawing, replacing the
  placeholder). Resolver + onboarding gametested both nodes.

- ✅ **First throw-test pass + guardians (2026-07-04).** Owner playtested all the 1:1 Iron's structures. Fit outcomes and
  the three that need home-curated rebuilds are tracked in **[IRONSTRUCTUREREBUILDPLAN.md](IRONSTRUCTUREREBUILDPLAN.md)**.
  Quick fixes landed this pass: **Mountain Tower** (wizard) moved `rocky_large`→`huge_rocky`; **Mangrove Hut** moved
  `lush`→`lush_large`; and — answering "does the wizard's tower have enemies?" (**it didn't** — nor did any of them; the
  mod spawns casters via its structure-spawn config, which doesn't fire on our jigsaw copies) — **guardian mob packs**
  added to the five keepers (wizard, mangrove, evoker fort, pyromancer, icebreaker) with verified `irons_spellbooks:*`
  caster ids, kept inert-safe by `MobPlanner`. **Rebuilds pending:** Ice Spider Den (sprawls under the island),
  Battleground (too large), Citadel (too big — the owner's favourite → a "grand feeling, smaller scale" rebuild).

- ✅ **Quest chapter #46 — Magic & Exploration (2026-07-04).** `overrides/config/ftbquests/quests/chapters/ironsspells.snbt`
  (A00A, 11 quests BB01–BB0B) in a new **Magic & Exploration** sidebar group (E004). Gated off the Skyseed structure-seed
  quest B108: Explore-seed tiers (base/large/huge) → scroll + Arcane Essence loot → Copper Spellbook / Scroll Forge /
  Arcane Anvil → the rare **Upgrade Orb**; Relic + Artifact as checkmark side-finds. Item ids verified against the jars;
  in-game book-load pending (like the other chapters — not runnable in the headless dev env).

**Left:** the whole pillar is now built — **in-game verifies only**: throw-test the structures/loot, and load the quest
book. The mod set is in `overrides/mods/` + `mods.txt` (regen via `gen-mods-txt.ps1`).

> **Note — no blanket `_large`/`huge_` rare mirror.** The tier themes already carry their own (tier-scaled) rares, so
> `explore_large`/`huge_explore` force a structure in every biome; copying the pad-4 base structures onto radius 24–32
> huge islands would look lost, so that's *not* done on purpose. ✅ The two tiers that had **no** `rare_structures`
> (`huge_mushroom`, `huge_lush`) are now filled — a buried dungeon complex / abandoned mineshaft (`pad 0`/`sink 10`, no
> bald pad, reused from `huge_rocky`), auto-covered by the debug-seed + coverage tests.

## Scope decision (#37) — SETTLED: the full exploration loop

The pack owner's call, overruling the earlier "crafted-only is the only sane route in a void" framing: **Skyseed is
an *exploration* game.** You throw a seed, grow an island, and a **rare building** germinates on it with **special
loot** inside — different seeds and biomes yield different structures and treasure. This is native to the engine, not
a workaround: the pack already ships a full jigsaw structure library (villages, trial chamber, temples, mansion,
monument…) that grows *on* islands, and `RareStructure` already germinates a chance-gated building **in place of** the
usual island. So Iron's Spells earns its slot as the **magic-and-loot exploration pillar**, delivered through
structure-islands + loot injection — the mod's own natural worldgen stays inert in the void, exactly like FD's wild
crops and MA's ores.

## The curated set — what shipped, and why

The Iron's Spells "spellbook" ecosystem is ~30 addons; most are gated on base mods this pack doesn't run (Cataclysm,
Ice & Fire, Apotheosis, Expanded Combat, Pufferfish's Skills) and are deliberately skipped. What earns a slot is the
magic core, the two exploration-loot pillars, and the Create bridge that ties magic into the pack's headline tech.

### ✅ Installed (`overrides/mods/`)

| Jar | Role | Integration needed? |
|---|---|---|
| `irons_spellbooks-1.21.1-3.16.1.jar` | **The anchor** — spells, scrolls, spellbooks, upgrade orbs, magic mobs + structures. | **Yes** — re-home its structures onto islands + inject its loot (see below). |
| `irons_lib-1.21.1-1.1.0.jar` | Iron's shared lib. | Dependency only. |
| `curios-neoforge-9.5.1+1.21.1.jar` | Curio slots (spellbook slot; also Relics/Artifacts slots). | Dependency only; nothing else in the pack used it before. |
| `player-animation-lib-forge-2.0.4+1.21.1.jar` | KosmX player-animation (casting anims). | Dependency only. |
| `irons_spells_js-4.0.3.jar` | **KubeJS ↔ Iron's Spells** — author custom spells/recipes/unlocks. | Tooling; used to bridge loot/recipe gaps. |
| `create_wizardry-1.21.1-0.5.0.jar` | **Create automation** of Iron's inks/essences/mana (fluids) + spell turret. | Ties magic into the Create backbone; JEI eyeball only. |
| `artifacts-neoforge-13.2.1.jar` | **Exploration curios** — whimsical trinkets; **loot-only here** (its campsite is code-gen, ships no data structures). | **Yes** — inject `artifacts:chests/campsite_chest` / specific artifacts; optional mimic entity. |
| `artifactdelight-1.1.4.jar` | Artifacts ↔ Farmer's Delight compat (vanilla-style). | None — pairs with the shipped FD; no worldgen. |
| `relics-1.21.1-0.10.7.8.jar` | **Exploration relics** — ~25 leveling RPG curios; **loot-only** (ships no structures; injects via its own config GLM). | **Yes** — point its loot config at our tables + curated relic GLMs. |
| `octolib-NEOFORGE-0.6.2+1.21.jar` | Relics' library dependency. | Dependency only. |
| `reliquified_irons_spells_and_spellbooks-1.21.1-0.2.7.jar` | **Relics × Iron's Spells** — Iron's-themed relics (flow through Relics' loot system + a mimic-loot tag). | Rides the Relics integration. |
| `createrelics-1.4.0.jar` | Create-themed relic items. | ⚠ **Confirm scope** (Create addon vs Relics bridge) + JEI eyeball; loot rides the Relics/GLM path. |
| `lootr-neoforge-1.21.1-1.11.37.121.jar` | **Per-player instanced loot** — every loot chest rolls per player. | Wraps our island chests automatically; **co-op exploration enabler**, ~zero integration. |
| `geckolib-neoforge-1.21.1-4.9.2.jar` | Model/animation lib (already in the pack). | Dependency (pre-existing). |

### ❌ Deliberately skipped (need a base mod the pack doesn't run)

Cataclysm: Spellbooks (L_Ender's Cataclysm) · Ice & Fire: Spellbooks · Iron's Apothic Invaders (Apotheosis) ·
EC Iron's Spells Compat (Expanded Combat) · Dynamic Skill Trees (Pufferfish's Skills) · T.O Tweaks (Cataclysm +
Alex's Caves). *If the magic pool ever feels thin, the sanctioned depth picks are the self-contained schools —
**Aeromancy Additions**, **GTBC's Geomancy Plus** — each adding scroll variety to scatter across islands.*

### ⚠ Dependencies & versions — to boot-verify (like the FD pass)

All deps are present: **GeckoLib** (pre-existing), **Curios**, **Iron's Lib**, **player-animation-lib**, **OctoLib**
(Relics). Confirm on first boot: (1) all load clean; (2) `createrelics` actual dependency + content; (3) player-
animation-lib 2.0.4 vs Iron's 3.16.1 (a historically fiddly pair — check for the KosmX version warning). Record the
result here as the FD plan did (§ verification).

## The void / worldgen trap (the standing rule)

Iron's Spells places its structures via **NeoForge structure sets + biome modifiers** and spawns its mobs in
worldgen biomes; Artifacts generates its campsite via a **code feature**. The `skyseed:void` ChunkGenerator
suppresses *all* natural decoration/structures/spawns, so **none of it generates naturally** — identical to the FD/MA
situation. Every source therefore rides the theme + loot systems:

- **Structures →** `RareStructure` entries on island themes (jigsaw pool assembled on a levelled pad by
  `GenerationJob`; see [`IslandGenerator.planStructure`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGenerator.java)).
- **Loot →** the proven `skyseed:add_drop` **Global Loot Modifier**
  ([`AddDropModifier`](../src/main/java/dev/gemberkoekje/skyseed/loot/AddDropModifier.java)), gated per loot table by a
  `neoforge:loot_table_id` condition — *"a re-grown structure island is a re-rollable source (rare but farmable)."*
- **Mobs →** theme `mobs` / a rare structure's `mobs` list (magic mobs on a themed island).

**Inert-safety (two guards):**
- **Loot item ids** resolve strictly (`BuiltInRegistries.ITEM.byNameCodec()`), so a GLM naming an Iron's/Artifacts
  item **fails to load without the mod**. Therefore all **mod-item loot** (GLMs, table references) ships in the
  **modpack `overrides/` datapack** (`overrides/kubejs/data/…`, where the mods are guaranteed present) — *not* the
  base mod (whose CI/gametests lack them). This mirrors where `meteorite_skyseed.json` lives.
- **Jigsaw pools** referencing `irons_spellbooks:*` need a new inert guard: `rollRare`/`planStructure` must **skip a
  rare structure whose `jigsaw.pool` isn't registered** (`Lookup.hasTemplatePool`, the primitive already used in
  `IslandGenerator.dimensionVariant`). With that guard, mod-pool `RareStructure` entries can safely ship in the base
  mod's themes and stay byte-identical/inert without the mod — matching the `Lookup.hasBlock` pattern.

## The Explore seed (headline design)

A **single adaptive seed** (`explore_skyseed`): thrown anywhere, it grows the island the local biome would grow **and
forces a biome-appropriate rare structure to germinate**. Throw it in a desert → a desert island with a desert
structure; in a dark forest → a forest island with a witch hut / mansion / evoker outpost. It reuses the existing
germination pipeline almost wholesale.

### a. Mechanism (small, additive engine work)

1. **Biome → base-theme resolver.** A tag-driven map (reusing the same `#is_*` biome tags the `biome_overrides`
   already key on) resolving the germination biome to a base theme: `#is_desert→desert`, `#is_forest`/`#is_taiga`/
   `#is_jungle→forest`, `#is_badlands→badlands`, `#is_ocean`/`#is_river→aquatic`, `swamp`/`#is_lush→lush`,
   `#is_plains`/`meadow/savanna→meadow`, `#is_hill`/mountains→rocky, snowy/frozen→frozen, `mushroom_fields→mushroom`;
   default → `meadow`. So the terrain is **identical to that biome's own seed**, no palette duplication.
2. **A production "force a fitting rare" flag.** Today `IslandGenerator.rollRare` takes a debug `forcedRare` *index*.
   Add a production path: collect every `RareStructure` where `rollsIn(dim) && matchesBiome(biome)` and pick one
   (weighted by `chance`, so repeat Explore throws in the same biome **vary**), bypassing the chance roll. Carried
   like the existing `DebugForce`, set by the entity when the seed is the Explore seed.
3. **Adaptive item.** `explore_skyseed` is an `IslandSeedItem` with no fixed theme; `IslandSeedEntity` resolves the
   theme from the biome (step 1) and sets force-fitting-rare (step 2) at germination — the one hook is
   [`IslandSeedEntity.planAt`](../src/main/java/dev/gemberkoekje/skyseed/entity/IslandSeedEntity.java).
4. **Guaranteed payoff (fallback).** Give each base theme one **biome-agnostic** catch-all rare (empty `biomes` = any;
   a small "arcane ruin" carrying magic loot) so the force path always has a candidate — the Explore seed never
   fizzles into a plain island.

### b. The recipe (the owner's ring idea)

A plus-shaped **ring of four biome essences around a structure-seed center**, as a `crafting_shaped` JSON in
`overrides/kubejs/data/skyseed/recipe/explore_skyseed.json` (same home + format as `meteorite_skyseed.json`):

```
 F         F = forest_skyseed
AXD        A = aquatic_skyseed   D = desert_skyseed
 M         M = meadow_skyseed
           X = center: any #skyseed:structure_seeds   (consumed — the rarity gate)
```

- **Center = a new `#skyseed:structure_seeds` item tag** (the dungeon/temple/mansion/monument/outpost/trial/witch_hut/
  fortress/bastion seeds). Any earned structure seed works and is *sacrificed*, so the Explore seed **inherits that
  seed's cost** — the owner's "sufficiently rare / relatively expensive" intent, kept flexible.
- The four biome seeds are **symbolic** (world's biomes + a ruin → an explorer's seed); they don't restrict where it's
  thrown. Yields **1** Explore seed per craft (consumable per throw).
- *(Optional future flavor:* a center witch_hut seed could bias toward witch-family structures via a recipe variant —
  deferred; the default keeps the center a generic cost and lets the **biome** pick the structure.)*

### c. Double duty + free debug coverage

The `RareStructure` entries added to the biome themes serve **both** roles at once:
- **Normal throw** of `forest`/`desert`/… → the mod/vanilla structure at **~5% chance** (the "lucky find").
- **Explore throw** → the same entry, **forced** to 100% (biome-appropriate pick).

`ThemeScanner` auto-derives a **debug seed per `rare_structures` entry** (forcing it) — so every new structure gets a
creative-tab test seed for free, no list edits. **Mirror every entry to the `_large`/`huge_` tiers** per the standing
mirror-island-tiers rule.

## Structures — pragmatic per-structure sourcing (the owner's call)

Iron's ships **65 jigsaw template pools + 236 NBTs** (reusable 1:1) but most are **large/underground dungeons** that
won't seat on a floating island. Decide per structure: **reuse 1:1** where it fits, **author a compact Skyseed room**
where it doesn't, **loot-only** where the mod ships no structure. Starter matrix (base overworld biomes; tune in play):

| Biome family | Structure | Source | Note |
|---|---|---|---|
| dark forest / dark oak | Witch Hut *(existing)* + Evoker outpost | **existing** + **reuse** Iron's `evoker_fort/guard_tower` | Surface fort piece seats on a pad; mansion → `huge_` only. |
| swamp | Witch Hut *(existing)* | **existing** | Iron's scrolls injected into its chest. |
| desert | Desert Temple *(existing)* | **existing** | Magic loot + Iron's `additional_treasure_loot`. |
| jungle | Jungle Temple *(existing)* | **existing** | — |
| badlands / savanna | Iron's **Battleground** (graveyard/piglin camp) | **reuse 1:1** (surface) | Best-fit reuse — it's a surface ruin. |
| frozen / snowy | Iron's **Ice Spider Den** (`huge_frozen`) + skyseed **Impaled Boat** (`frozen_large`) | **reuse** + **author** | Ice Spider Den size-gated huge; the small custom wreck is the `_large` frozen highlight. |
| ocean / beach | Ocean Monument *(existing)* + skyseed **Impaled Boat** (`aquatic_large`, cold) + Iron's full **Impaled Icebreaker** (`huge_aquatic`, cold) | **existing** + **author** + **reuse** | Full mod ship overhung `aquatic_large` → moved to the huge cold-ocean tier; our small custom boat covers `aquatic_large`/`frozen_large`. |
| plains / meadow | Outpost *(existing)* | **existing** | — |
| forest | Dungeon *(existing)* + small **arcane ruin** | **existing** + **author** | The author-own catch-all (also the fallback ruin). |
| rocky / mountains | Iron's **Citadel** or a mini-crypt | **author** (Citadel too big) / gate `huge_` | Or a small authored "mage tower". |

*Iron's **Catacombs** (62 KB origin, deep multi-room) is the clearest "author our own small crypt instead" case.*
Artifacts contributes **no** structure — it's pure loot into the above (plus an optional mimic mob for a combat twist).

## Loot — both paths (the mods do the breadth for free) — ✅ SHIPPED

**The "totem": all three mods already auto-inject into the vanilla chest tables the skyseed explore structures roll.**
The skyseed structures reuse vanilla chest loot tables (`minecraft:chests/desert_pyramid`, `jungle_temple`,
`simple_dungeon`, `igloo_chest`, `underwater_ruin_big`, `ruined_portal`, `pillager_outpost`, `woodland_mansion`,
`buried_treasure`, `trial_chambers/reward`, `ancient_city`, …) — and each mod ships its own GLMs against exactly those
ids: **Iron's** `chest_loot/*` (`append_loot`, with the `randomize_spell` quality dial built in) + its trial-chamber +
entity-drop modifiers; **Artifacts** `loot_modifiers/inject/chests/*` (`roll_loot_table`); **Relics'** config-driven
`relic_loot`. So a grown explore structure pays out spells/scrolls, artifacts, and relics **with no datapack work from
us** — the breadth is automatic (verify in-game).

**Precision (our controlled "stronger = rarer" dial) — SHIPPED, inert-safe:** two curated `skyseed:add_drop` GLMs in
the **base mod** ([`data/skyseed/loot_modifiers/`](../src/main/resources/data/skyseed/loot_modifiers/)):
`irons_arcane_essence` (the common mat, ~0.35 across the accessible explore tables) and `irons_upgrade_orb` (the scarce
upgrade item, ~0.04, only in the deadliest — trial-chamber reward, ancient city, mansion, bastion). Low-but-nonzero, so
a lucky player can still strike one on any throw. **Inert-safe (the totem of retrieval):** `AddDropModifier` now stores
the item as a raw `Id` and resolves it through `Lookup.hasItem` at apply time, so a GLM naming an absent mod's item
simply adds nothing instead of failing the datapack load — which is why these ship in the base mod (not overrides) and
are **CI-gametested** (`modItemLootModifiersAreInertWithoutTheMod`, both nodes). Add more curated marquee items the
same way (any `irons_spellbooks:` / `artifacts:` id, gated to a structure table with a `chance`).

## Progression & balance

- Explore seed sits **behind a structure seed** (recipe center) → it's a mid/late tool, not a starter.
- **Lootr** makes every island chest per-player — a rare structure-island rewards a **whole co-op team**, not the
  first opener.
- Trinket power creep (Artifacts early utility + Relics leveling): keep the strong relics on the **rarer/deadlier**
  structures via the GLM chances; let common Artifacts populate the easy huts.

## Build order

1. **Boot-verify** the set (§ dependencies); record results here.
2. **Engine:** the `Lookup.hasTemplatePool` inert-guard in `rollRare`/`planStructure`; the biome→theme resolver; the
   production force-fitting-rare flag on `DebugForce`/`rollRare`/`IslandSeedEntity`; register `explore_skyseed` +
   the `#skyseed:structure_seeds` tag. Gametest each (id/pool resolves; resolver maps sample biomes; force path
   always yields a rare).
3. **Structures:** add the `rare_structures` (chance ~0.05, biome-gated) to the biome themes + `_large`/`huge_` tiers,
   per the matrix — reuse mod pools where marked, author the small arcane ruin / crypt / mage-tower where marked.
   Gametest-guard every pool id on both nodes.
4. **Loot:** the mod-table references + Relics config in `overrides/`; the curated `skyseed:add_drop` GLMs; tune
   `chance`/`randomize_spell` quality per structure tier.
5. **Recipe:** `explore_skyseed.json` (the ring); optional shadow/tune of structure-seed costs.
6. **In-game verify:** throw the Explore seed in 4–5 biomes → correct terrain + a fitting structure each; open chests
   → magic/artifact/relic loot; confirm the 5% lucky-find on normal seeds; confirm Lootr per-player instancing.
7. **Quest chapter (#46):** a **Magic & Exploration** FTB chapter authored *after* the loot is reachable (rolling
   rule #19) — craft the Explore seed → find a structure → first scroll/spellbook → upgrade orb → a marquee relic.
   Remember the FTB tag-task trap (use advancement tasks, not smart-filter tag tasks).

## Open decisions

- **createrelics** — confirm what it actually adds + its deps on boot; keep or drop.
- **Explore-seed center** — the `#skyseed:structure_seeds` **tag** (recommended, flexible) vs a single fixed rare seed.
- **Catch-all fallback ruin** — build the small "arcane ruin" now (guarantees payoff + doubles as the forest
  structure) vs rely on per-biome coverage only.
- **Relics loot reach** — enable Relics' own config on `skyseed:chests/*` (breadth) + curated GLMs (marquee), or GLMs
  only for full control.

## Backlog wiring (after this lands)

Update [CONTENTPLAN §2](CONTENTPLAN.md) (#36/#37) and [PLANOFPLANS.md](../PLANOFPLANS.md) rows #36/#37/#46 to point
here; regen `mods.txt` via `gen-mods-txt.ps1`; add the Explore-seed line to the guide/almanac.
