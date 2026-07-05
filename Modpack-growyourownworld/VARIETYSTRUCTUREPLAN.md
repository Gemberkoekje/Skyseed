# Structure Variety — a weighted 5% surprise on every overworld seed (#68)

> **Status: Phase 0 engine + D5 flat-5% flatten + Phase 1 (ALL 12 Band-1 commons) SHIPPED, and the first three Phase-2
> Band-2 mod batches — **Create (B13–B16)**, **Immersive Engineering (B17–B18)** and **Applied Energistics 2 (B19)** —
> SHIPPED (both nodes green — 1.21.1: 195, 26.1.2: 197, 2026-07-05).**
> Every hot overworld `_large`/`huge_` tier is a weighted **5% gate** (the
> citadel's `huge_rocky` drops to ~1.4%), and **all ten overworld terrain families now carry 1–2 common vanilla builds**
> on their base + large tiers: Tool Shed + Ruined Farmstead (Meadow), Cairn (Rocky/Ancient), Graveyard + Hunter's Blind
> (Forest), Collapsed Cabin (Frozen), Fossil Dig + Prospector's Camp (Desert), Fossil Dig + Bandit Camp (Badlands),
> Overgrown Well (Lush/Mushroom), Fishing Camp (Aquatic), Mine Head (Rocky) — all authored no-boxes with per-building
> assembly gametests. The **Band 2 Create batch** (Broken Windmill, Derelict Watermill, Abandoned Train Shed, Rusted
> Drill Rig, `requires:["create"]`) and the **IE batch** (Dilapidated Factory + Fallen Powerline,
> `requires:["immersiveengineering"]`) and the **AE2 batch** (Gutted AE2 Lab, `requires:["ae2"]`, on Rocky/Ancient) are
> wired at w3 across all three tiers, authored via the `modNames` side-map (inert without the mod) with gate-safe scrap
> loot + GLMs — the AE2 lab shows an *empty controller pit* (no sky stone / controller / presses, D4). The **Iron's Spells
> batch** (Small Wizard's Hut, Wizard's Tower, Cursed Obelisk, `requires:["irons_spellbooks"]`) is wired at w3 across all
> three tiers of Forest+Meadow / Rocky+Ancient / Ancient+Badlands — all-vanilla architecture (no `modNames`; Iron's is a
> mob/item mod), gated by the mage `mobs` pack + gate-safe arcane-scrap loot (a token essence/ink GLM, no Upgrade Orb/named
> scroll, D4). The **Mystical Agriculture batch** (Abandoned Inferium Plot, `requires:["mysticalagriculture"]`, on
> Meadow/Hamlet) is a mostly-vanilla derelict farm — only the inferium farmland/crops/accelerator are modded (`modNames`
> over `FARMLAND`/`WHEAT`/`STONE` analogs, inert without the mod) — kept to tier-1 inferium with gate-safe essence/shard
> GLMs (no seeds/higher-tier essence/prosperity blocks, D4). The **Farmer's Delight batch** (Overgrown Cook's Homestead,
> `requires:["farmersdelight"]`, on Forest/Meadow) closes Band 2 — a vanilla cabin shell with modded kitchen fittings
> (`modNames` over `SMOKER`/`STONE`/`WHEAT` analogs) and flavour-only loot (FD has no progression gate). **Band 2 is now
> COMPLETE (B13–B24): eleven mod-gated rare surprise buildings across Create, IE, AE2, Iron's Spells, Mystical Agriculture
> and Farmer's Delight, each inert without its mod.** **Band 3 has begun:** the vanilla **Ruined Chapel** epic (B25) is
> wired w1 across Meadow + Rocky and floors the thin Lush/Mushroom themes' Explore seed — and with it the **weighted-gate
> theme migration is complete** (`huge_lush`/`huge_mushroom` were the last two legacy overworld themes; every overworld
> theme now runs the flat-5% weighted gate — **the D5 large/huge-rate question is settled: flat 5% on all sizes**). The
> first multi-mod epics have shipped too: the **Magitech Workshop** (B26, `requires:["create","irons_spellbooks"]`, Rocky) —
> a Create machine husk fused with a vanilla arcane study — and the **Automated Essence Farm** (B27,
> `requires:["create","mysticalagriculture"]`, Meadow) — a stalled Create harvester gantry over a derelict tier-1 inferium
> field. Both are D4-safe against both mods (Create → `andesite_alloy`; Iron's → `arcane_essence`/`blank_rune`; MA →
> tier-1 `inferium_essence`). A physical-review pass fixed a floating-beam class of bug across B24/B25/B26 and confirmed B27 clean.
> The **FE→ME Substation** (B28, `requires:["immersiveengineering","ae2"]`, Rocky) — the sharpest D4 case — has shipped too:
> an IE→AE2 conversion station over an **empty controller pit** (no sky stone / controller / press; fluix + wire-coil loot).
> The **Alchemist's Distillery** (B29, `requires:["immersiveengineering","irons_spellbooks"]`, Badlands) — an IE refinery
> reworked as a vanilla arcane still — has shipped as well (D4: IE plate + Iron's `arcane_essence`).
> **Left:** the last Band-3 combo (B30 Sky-Freight Depot) + the base-tier
> `huge_` mirrors for the small commons (scale permitting), and an in-game throw-test/tuning pass. This
> is the scoped build-out of PLANOFPLANS **#68** ("net-new bespoke structures beyond BWG's 17"). The goal is many more
> *common* and *rare* buildings so that every overworld seed has a **flat 5% chance** to germinate one that fits its
> type — which **dilutes the citadel** (and the other flagships) down to a genuine standout, and makes the exploration
> seed matter more. The roll-model engine (§2 A1–A3, A6) is **built and behavior-neutral** — it landed as a
> **backward-compatible opt-in** (a theme adopts the weighted-gate model by declaring `rare_structure_chance`; without
> it, the legacy per-entry roll is byte-identical), so nothing changed yet. What's left: the **per-theme migration
> (A4)** — now folded into the content phases (§6) — plus the catalog (§3) and wiring (§4). Child of
> [`STRUCTUREPLAN.md`](STRUCTUREPLAN.md) — reuses its hermetic string-id `.nbt` engine + the [[skyseed-structure-staging]]
> regen dance wholesale.

## 0. Goal, in one paragraph

Skyseed already grows a *surprise building on an otherwise ordinary island* — a theme's `rare_structures` roll
(`IslandGenerator.rollRare`, `IslandGenerator.java:236`) drops a jigsaw building at the island centre while the
island itself still generates its normal terrain/trees/ores. Today only a handful of these exist (igloo, abandoned
cottage, ocean ruin, evoker cell, vault cell, impaled boat, plus the biome-gated villages/temples). We want a **deep
bench** of them — humble vanilla ruins that show up often, single-mod curios that show up sometimes, and multi-mod
set-pieces that are genuinely rare — all sharing one **flat 5%-per-seed** budget so the odds of the amazing citadel
(and woodland mansion, ocean monument, end city, trial chamber, …) are *decreased*, making each flagship land feel
earned. A single structure can be listed on many themes and gated to many biomes, so the catalog is small relative
to the coverage.

## 1. Decisions (signed off 2026-07-04)

- **D1 — Roll model: SINGLE 5% GATE + WEIGHTED PICK.** One 5% roll per overworld seed; on a hit, weight-pick exactly
  one *eligible* fitting structure. "Common vs rare" is expressed as a **weight**, and the per-seed spawn probability
  stays a flat 5% no matter how many structures fit the theme. (The alternative — independent per-entry `chance` that
  must sum to ~5% — was rejected: it is order-biased by the first-hit-wins walk and drifts off 5% every time a
  structure is added or removed.) This is a small change to `rollRare` + the theme/`RareStructure` codecs (§2).
- **D2 — Rarity mix: BALANCED (~50% common / ~35% rare / ~15% epic).** Each theme's weighted table should land roughly
  half its probability mass on humble vanilla ruins, a third on single-mod curios, and a sixth on multi-mod / large
  set-pieces. Concretely: **common weight ≈ 6, rare weight ≈ 3, epic weight ≈ 1** per listed candidate, then compose
  each theme's list so the mass lands near 50/35/15 (§4 gives the per-theme tables).
- **D3 — The Explore seed has a REWARD FLOOR (added 2026-07-04).** The Explore Skyseed is expensive and its structure
  is *guaranteed*, not a 5% surprise (`IslandSeedEntity.pickFittingRare`, line 184, forces one fitting rare per throw).
  So it must **never** force a "complete junk" build — a loot-less vanilla shed is a bad payoff for a premium seed.
  Every structure carries an **`explorable`** flag (default `true`); pure-ambience / no-reward builds set it `false` and
  are excluded from the Explore force-pool while **still** appearing as normal-seed 5% flavour. This is decoupled from
  weight: a *common* build with real loot (a farmstead barrel, a well chest) stays Explore-eligible; a *common* build
  with no reward does not. See A6 — including the coverage invariant that keeps a premium throw from landing a *plain*
  island (which would be worse than a shed).
- **D4 — Mod structures stay DERELICT; their loot never breaks the mod's gating (added 2026-07-04).** A mod building's
  loot is scrap, flavour, hints, and at most *mid-game, non-gating* materials — **never** the mod's gate-keys,
  uncraftable/irreplaceable items, a working multiblock, or bulk endgame resources. The "all the interesting bits are
  missing" aesthetic **is** the balance mechanism, and it applies hardest to the multi-mod epics (§3 Band 3): a Gutted
  AE2 Lab must not hand over a complete AE2 setup. Reward-bearing (D3) and gate-safe are compatible — certus scraps +
  fluix make a fine Explore payoff without leaking sky stone or presses. Concrete per-mod allow/deny list in §5.
- **D5 — Flat 5% on every overworld seed, ALL sizes (added 2026-07-04).** A bigger seed's reward is **more resources**
  (bigger core → more ore), **not** a higher structure/loot chance — the guaranteed-structure niche belongs to the
  Explore seed. So every overworld tier's `rare_structure_chance` is `0.05`; the hot `_large`/`huge_` tables are
  flattened to a weighted 5% (relative rarity kept via `round(chance×100)` weights), the citadel's `huge_rocky` included.

**Still open (see §8):** the exact per-theme candidate lists (a review pass once the catalog is built), and whether
any *vanilla* build should be promoted into the "epic" band so a mod-free pack still occasionally gets a showpiece
(recommended: yes — the existing evoker cell / vault cell / ocean ruin already qualify).

## 2. Part A — the roll-model engine change

A handful of small, contained edits (A1–A6). All of them must preserve the **inert-without-the-mod /
determinism-parity invariant** (the same one the 2026-07-04 code review flagged for `MobPlanner`, PLANOFPLANS
engineering-debt): *a world generated without a given mod must consume an identical RNG stream to a world where that
mod's structures were never declared.*

### A1. Add `weight` to `RareStructure`, `rare_structure_chance` to the theme

- ✅ `RareStructure` (`worldgen/theme/RareStructure.java`) gained **`int weight`** (optional, default `1`),
  **`List<String> requires`** (A3), and **`boolean explorable`** (A6).
- ✅ `IslandTheme` gained **`Optional<Float> rareStructureChance`** — the single per-seed gate. Implemented as
  **opt-in**: *present* selects the weighted-gate model; *absent* keeps the legacy per-entry model, byte-identical
  (so the Nether/End + any unmigrated theme are untouched, and the engine landed with zero golden-master churn).
  Folded with `rare_structures` into one combined `Rares` codec slot to stay within DFU's 16-field group limit (the
  same trick the theme's `Carve` slot uses). `ThemeOverride.Patch` carries the base gate through (overrides append
  structures, not the rate); `BiomeOverride` doesn't carry `rare_structures`, so it needed no change.
- ✅ The per-entry **`chance`** field is now **optional** (`Codec.FLOAT.optionalFieldOf("chance", 0.05f)`). It is read
  **only** by the legacy path; the weighted-gate path reads `weight` instead. So a migrated entry drops `chance`, and
  a legacy (Nether) entry keeps it.

### A2. Rewrite `rollRare` — gate once, then weighted-pick ✅ built

`rollRare` now dispatches on the theme's gate: `rareStructureChance().isPresent()` → **`rollWeighted`**; absent →
**`rollLegacy`** (the old first-`chance`-hits loop, verbatim, so unmigrated themes stay byte-identical). Both share an
**`eligible(...)`** helper (dimension + biome + `requiresPresent()` + pool-registered), evaluated *before* any RNG.
`rollWeighted`:

1. Return early if the theme declares no rares.
2. **Gate roll (unconditional when the theme declares ≥1 rare):** draw **one** `random.nextFloat()`; miss if
   `≥ gate`. Rolling it whenever the theme *declares* rares (not whenever any are *eligible*) is what holds the
   determinism invariant: mod presence changes *which* candidates are eligible, never *whether* the gate float is drawn.
3. Build the eligible list + total weight (no RNG).
4. On a **miss**, return null (one float spent). On a **hit**, draw **one** selection float `nextFloat() * totalWeight`
   regardless of the eligible-set size (constant RNG cost → mod-independent), then walk the weights. Zero eligible on a
   hit → null (the build "would have" been there with the mod).

Net RNG cost per seed under the weighted model: **one gate float** (miss), **two floats** (hit). The debug-force path
(`forcedRare >= 0`) is unchanged — it still bypasses both rolls and pins an index.

> **Determinism note:** because the model is opt-in, a theme only shifts its RNG stream **when it is migrated** (A4).
> The engine landing itself is byte-identical (no theme carries a gate yet) — both nodes green with **no** golden-master
> re-capture. Each theme's re-capture happens as it's migrated, alongside its content batch.

### A3. Add a `requires` mod-presence gate to `RareStructure` ✅ built

`RareStructure` gained an optional **`List<String> requires`** (mod ids; default empty) + a **`requiresPresent()`**
helper. A candidate with a `requires` entry whose mod is **not loaded** is filtered out in `eligible(...)` — *before*
the gate roll, so it costs no RNG (the inert-safety invariant). This is what lets a Create windmill / AE2 lab / IE
factory / multi-mod set-piece be listed on a plain terrain theme yet stay completely inert (and byte-identical)
without its mod(s) — the analogue of the Patchouli `flag: mod:<modid>` gate ([[skyseed-theme-override-inert-safety]]
rule 3) for worldgen.

- ✅ Mod-loaded check added as **`Lookup.modLoaded(String)`** (`compat/Lookup.java`, `ModList.get().isLoaded(id)`,
  mirroring `Ae2Compat`) — the one central place, so it's version-agnostic across both nodes.
- Biome-gated structures (villages/temples over BWG biomes) still lean on the biome gate as before and need no
  `requires`; `requires` is for structures with **no** distinguishing biome (Create/AE2/IE/magitech).

### A4. Migrate the existing `rare_structures`

Data-only, and **per-theme opt-in** — a theme flips to the new model the moment it declares `rare_structure_chance`.
Because the engine is backward-compatible, this is **folded into the content phases (§6)**: migrate a theme when we
build its weighted table + new structures, re-capturing just that theme's golden master. Nothing is migrated in the
Phase-0 engine landing.

> **What the audit found (2026-07-04, ~39 files).** The existing tables are richer than "a lone `chance: 0.05`": many
> `_large`/`huge_` themes **deliberately stack** several structures at **graduated** per-entry chances (e.g.
> `ancient_large` = 10% trail ruins + 5% dungeon + 1% ruined portal ≈ **15% total**; `huge_rocky` stacks Iron's towers
> at 4% each; `meadow_large` = 6% + 6%). The blast radius also includes **out-of-scope Nether themes** (`nether_*_large`,
> blaze-room/bastion at 5% each). The opt-in design is what lets us leave all of that alone until we choose to touch it.

Per migrated theme:

- Set `rare_structure_chance: 0.05` (D5 — flat, every tier).
- Convert each entry's `chance` → a `weight` = `round(chance × 100)` (min 1), which **preserves the theme's relative
  rarity** (the 1% portal → w1 stays rarer than the 6% outpost → w6) while the gate caps the total at 5%. Remove the
  now-unused `chance`. New builds slot in per D2's bands.
- **Mirror to all three tiers** (`<x>`, `<x>_large`, `huge_<x>`) per [[skyseed-mirror-island-tiers]].
- **Leave Nether/End themes on the legacy path** (don't add `rare_structure_chance`) — out of scope, stays byte-identical.

> **✅ DECIDED (D5, user 2026-07-04) — FLAT 5% ON EVERY OVERWORLD SEED, ALL SIZES.** The reward for a bigger seed is
> **more resources** (a larger core = more ore, per `OrePlanner`'s volume scaling), **not** a higher structure/loot
> chance — the guaranteed-structure niche is exactly what the **Explore seed** is for. So every overworld tier's gate
> is `0.05`; the hot `_large`/`huge_` tables (today 6–17%) are flattened to a weighted 5%, relative rarity preserved
> via the `round(chance×100)` weights. This is max flagship dilution and it applies to `huge_rocky` (the citadel) too.

### A5. Debug + gametests

- ✅ `DebugForce` / `rollRare`'s `forcedRare` bypass is unchanged (still indexes `rareStructures()` and pins an index,
  ahead of both models) — verified green on both nodes. A debug alias to force *by pool name* is still a nice-to-have.
- **New-model gametests land with the first migrated theme** (they need a theme carrying `rare_structure_chance`):
  Gametests (both mirrors — `gametest/SkyseedGameTests` **and** `gametest_26_1_2/SkyseedTests`, per
  [[skyseed-structure-staging]]): (a) a **weighted-pick determinism** test (fixed seed → fixed structure), (b) a
  **mod-gate inert** test (a theme listing a `requires` build with the mod absent generates byte-identically to the
  same theme without the entry), (c) an **on-pad assembly** test per *new* building (it assembles with no floating/
  missing pieces — the STRUCTUREPLAN #29 discipline), reusing the `gametest/region` and `big_region` pads.

### A6. The Explore-seed reward floor (D3)

The Explore Skyseed does **not** use the 5% gate — `IslandSeedEntity.pickFittingRare` (line 184) resolves the biome to
a terrain theme and **weighted-picks one fitting rare to force, guaranteed** (weighting by `rs.chance()` today). That
guarantee is the whole value proposition of an expensive seed, and it is exactly why "complete junk" must not be in
its pool. Three edits, all data-safe:

1. **New field `boolean explorable` on `RareStructure`** (optional, **default `true`**). A build set `explorable:
   false` is *pure ambience / no reward* — it can still appear as a normal-seed 5% surprise but is barred from the
   Explore force-pool. Kept separate from `weight` on purpose: reward-bearing ≠ rare (a common farmstead with a barrel
   is a fine Explore payoff; a loot-less shed is not, at any weight).
2. **`pickFittingRare` migrates + filters.** Weight the pick by the new **`weight`** (not the deprecated `chance`, A1),
   and **skip any `!explorable`** candidate when building the `fitting` list (line 188–197). Everything else (the
   dimension / biome / `hasTemplatePool` gates, and — add it here too — the new `requires` mod gate, A3) is unchanged,
   so an absent mod's build stays out of the Explore pool as well.
3. **Coverage invariant + gametest.** A premium throw that forces *nothing* (a plain biome island) is **worse** than a
   shed, so every Explore-resolvable theme must keep **≥1 explorable, reward-bearing** structure for each biome it can
   grow — across **all three tiers** (`ExploreThemes.resolve` / `resolveLarge` / `resolveHuge`) and the `explore.json`
   fallback (which already forces the loot-bearing trail ruins at `chance: 1.0`). Add a gametest that walks every
   `ExploreThemes` rule × tier and asserts `pickFittingRare` returns a valid, `explorable` index (never `-1`) for a
   representative biome. When a theme is thin on eligible builds, this test is the alarm to add one (or widen a biome
   filter) before shipping that batch.

Define the reward floor concretely so the flag is applied consistently: **`explorable = true` iff the build carries a
real reward** — a loot chest / barrel, a brushable (archaeology), a spawner-or-trial-plus-vault, or a mob whose death
drops something notable (an evoker's totem, a mage's scroll). Decorative-only builds (an empty shed, a bare cairn, a
fallen chimney) are `explorable: false`.

## 3. Part B — the structure catalog (the creative menu)

Build order is by batch (§6); rarity band drives the weight (D2). Every building is a single-piece jigsaw authored in
a `*Templates.java` → `.nbt` + a one-line `worldgen/template_pool/<dir>/<name>.json` (see the existing
`template_pool/abandoned/cottage.json`). Loot rides vanilla/GLM tables so it's inert-safe; mobs come from the entry's
`mobs` pack (spawned at centre), sized so **enemies ≈ loot** (the user's rule of thumb). ✅ = already shipped.
Every catalogued build below **carries a reward** and is thus `explorable: true` — they are all fair Explore payoffs.
The `explorable: false` cases (D3 / A6) are the deliberately-empty *filler* builds authored for normal-seed variety —
see the **Filler** note under Band 1; keep new ambience-only builds out of the Explore pool the same way.

**Design rule — NO BOXES, inside and outside** ([[skyseed-no-boxes]], user call 2026-07-04): even a small "common"
build must not read as a plain walls-and-a-lid rectangle. Break the box on **both** faces — an irregular silhouette
(lean-to / bump-out / offset) + a pitched/broken roof + real ruin (missing planks, caved roof, a snapped post,
exterior clutter that spills past the walls) outside; varied ceiling + relief + clutter + cobwebs inside. `abandonedCottage`
is the reference. The assembly gametest asserts a signature non-box feature landed (the Tool Shed test checks the
lean-to fence), not just the loot.

### Band 1 — Common, all-vanilla (weight ≈ 6; fit the widest set of themes)

| # | Building | Footprint | Contents / loot | Mobs | Themes / biomes |
|---|---|---|---|---|---|
| ✅ | **Abandoned Cottage** | 7×7 | village-house chest, cobwebs, no bed | 1 zombie villager | Forest, Meadow, Hamlet |
| ✅ B1 | **Tool Shed** | 5×4 + lean-to | chest (`village_toolsmith`), barrel, crafting table, dead potted plant, floor lantern; **not a box** — caved roof, fallen wall planks, a snapped corner post, an open lean-to woodshed + chopping stump breaking the silhouette | 1 spider | any grass theme — **wired into base Meadow** (weighted-gate, w6); `_large`/`huge_` pending the rate call |
| ✅ B2 | **Collapsed Cabin** | 7×6 spruce | **caved-in back corner** (breached walls/roof, fallen timbers + snow blown in), a cobblestone chimney stack past the eave, hearth furnace, frosted windows, snowy-village chest | 1 stray | **wired into Frozen** (base+large, w6) |
| ✅ B3 | **Ruined Farmstead** | 7×6 | a broken split-rail pen (fallen rails), overgrown wheat on farmland + dead patches, a leaning carved-pumpkin scarecrow, a toppled hay stack, a stores chest | 1 cow | **wired into Meadow** (base+large, w6) |
| ✅ B4 | **Hunter's Blind** | 5×5, raised | a plank deck up on four log legs (elevated = anti-box), an access ladder, a fence railing with a gap, a slab lean-to canopy, a fletcher's chest, a ground campfire + barrel | 1 skeleton | **wired into Forest** (base+large, w6) |
| ✅ B5 | **Overgrown Well** | 5×5 | a two-course cobble curb holding a walled water pool, a stripped-log beam + slab canopy on posts, a hanging winch lantern, moss + cobwebs, a traveller's chest; self-contained water | 1–2 drowned | **wired into Lush + Mushroom** (base+large, w6) |
| ✅ B6 | **Roadside Shrine / Cairn** | 5×5 | mossy-cobble apron + stepped approaches, a lantern-topped altar with lit candles, **four broken columns of uneven height**, moss + cobwebs, a `village_temple` offering chest | none | **wired into Rocky + Ancient** (base+large, w6) |
| ✅ B7 | **Beached Fishing Camp** | 7×5 | a beached spruce rowboat (raked gunwale stairs, prow post, thwart seat) on a sandy beach, a dead campfire ringed by log seats, a lantern post, a dried-kelp bale, a decorative barrel, the fisher's chest | 1 drowned | **wired into Aquatic** (base+large, w6) |
| ✅ B8 | **Prospector's Camp** | 7×5 | a wool bedroll, a cold cobble-ringed campfire + log-stump seats, a coal-ore/cobble/gravel spoil heap, a lantern post, a tools chest, dead bushes | 1 husk | **wired into Desert** (base+large, w6) |
| ✅ B9 | **Bandit Camp** | 7×5 | two wool bedrolls, a cold cobble-ringed campfire + log seats, a crude black/red-wool banner on a pole, a bone-block carcass + cobwebs, a `pillager_outpost` spoils chest | 2 pillagers | **wired into Badlands** (base+large, w6) |
| ✅ B10 | **Graveyard Corner** | 7×5 | scattered headstone wall-posts over podzol mounds, a broken fence rail with a gap, a bare dead tree, a wither rose + cobwebs, a stone-brick crypt hiding the coffin chest | 2 zombies | **wired into Forest** (base+large, w6) |
| ✅ B11 | **Collapsed Mine Head** | 7×5 | a timbered adit mouth (log frame + lintel over a dark deepslate opening), a rail stub out to a derelict chest-minecart (`abandoned_mineshaft`), cobble/coal-ore/gravel spoil heaps, a hanging lantern | 1 cave spider | **wired into Rocky** (base+large, w6) |
| ✅ B12 | **Fossil Dig** | 7×5 | a part-excavated bone skeleton (broken spine + arcing ribs + skull) in sand, brushable suspicious sand/gravel (`archaeology`), a scaffold dig-frame + lantern, a mason's tools chest, fallen beam + dead bushes | 1 husk | **wired into Desert + Badlands** (base+large, w6) |

**Filler (`explorable: false`) — normal-seed variety only, barred from the Explore pool (D3/A6):** empty tool shed
(the no-barrel variant), bare cairn / stone marker, fallen chimney stack, collapsed fence line + trough, hay-bale
pile, dead-tree snag. These satisfy the "with-or-without loot" texture the user asked for on ordinary throws, but a
premium Explore seed never lands one. Give them a modest weight so they don't crowd out the reward-bearing commons.

### Band 2 — Rare, single-mod (weight ≈ 3; `requires: [modid]`)

| # | Building | Mod | Contents / loot | Mobs | Themes |
|---|---|---|---|---|---|
| ✅ B13 | **Broken Windmill** | Create | caved stone-brick mill house + a broken four-armed wool-clad sail (millstone/cogwheel/casing at the base); `create_scrap` loot | 1 zombie | **wired Meadow + Hamlet** (all 3 tiers, w3) |
| ✅ B14 | **Derelict Watermill** | Create | a `water_wheel` over a self-contained walled water channel + a `mechanical_press` husk on the ruined mill housing (`suppress_pond`) | 1 drowned | **wired Aquatic** (all 3 tiers, w3) |
| ✅ B15 | **Abandoned Train Shed** | Create | an open-sided timber shed (caved roof) over a rail stub + a half-finished casing train on `shaft` bogies; brass/copper scrap | 1 spider | **wired Rocky** (all 3 tiers, w3) |
| ✅ B16 | **Rusted Drill Rig** | Create | a scaffold derrick over a drilled deepslate shaft, a `mechanical_drill` + gearbox husk, a cauldron fluid-works, spoil heaps | 1 husk | **wired Badlands + Desert** (all 3 tiers, w3) |
| ✅ B17 | **Dilapidated IE Factory** | Immersive Eng. | a concrete-slab hall on uneven part-collapsed pillars, breached treated-wood/sheetmetal walls + a caved sheetmetal roof, a crusher/kiln husk, spilled crates, an external HV wire-post jutting past the roofline; `ie_scrap` loot | 2 zombies | **wired Rocky + Badlands** (all 3 tiers, w3) |
| ✅ B18 | **Fallen Powerline** | Immersive Eng. | a lattice alu-scaffold pylon (cross-arms of HV connectors + razor wire, a transformer husk) with a whole span snapped off lying across the ground; `ie_scrap` wire/redstone loot | 1 creeper | **wired Meadow + Desert** (all 3 tiers, w3) |
| ✅ B19 | **Gutted AE2 Lab** | AE2 | a certus-quartz/fluix room stripped for parts (breached walls, caved ceiling), end-rod conduit stubs + a snapped roof antenna, and an **empty controller pit** (a scorched-deepslate mount — no controller/sky-stone placed, D4); `ae2_scrap` + token certus/fluix loot | 1 zombie | **wired Rocky + Ancient** (all 3 tiers, w3) |
| ✅ B20 | **Small Wizard's Hut** | Iron's Spells | a cobble+oak hut under a pointed witch-hat spruce spire (lantern beacon), a bookshelf wall, a brewing stand over a cauldron, a lectern + amethyst focus + candles; vanilla arcane-scrap + token essence/ink GLM (inert-safe) | 1 apprentice mage | **wired Forest + Meadow** (all 3 tiers, w3) |
| ✅ B21 | **Wizard's Tower** | Iron's Spells | a 5×5 stone-brick tower, two storeys joined by a ladder, arrow-slit windows + a jutting balcony + a spruce-stair spire; a bookshelf study below, the alchemy floor above; arcane-scrap loot | 2 mages | **wired Rocky + Ancient** (all 3 tiers, w3) |
| ✅ B22 | **Cursed Obelisk** | Iron's Spells | a tapering blackstone/deepslate/basalt spire over a soul-lantern altar with black candles, bones + a wither rose; a carved base alcove hosts the necromancer; necrotic scrap loot | 1 necromancer | **wired Ancient + Badlands** (all 3 tiers, w3) |
| ✅ B23 | **Abandoned Inferium Plot** | Mystical Agri. | an irregular derelict inferium-farmland bed (rows trampled to coarse dirt) around a water-cauldron trough, a scatter of tier-1 essence crops, a lop-sided carved-pumpkin scarecrow, a caved tool lean-to over the chest, a toppled inferium growth accelerator; vanilla farm-scrap + inert inferium-essence/prosperity-shard GLMs (D4) | 1 zombie | **wired Meadow + Hamlet** (all 3 tiers, w3) |
| ✅ B24 | **Overgrown Cook's Homestead** | Farmer's Delight | a caved 5×5 log-and-cobble cabin, an external broken cobblestone hearth-chimney over a cold campfire, the FD kitchen inside (cold stove + cooking pot, skillet, cutting board, cabinets, chest), a weed-choked front garden of FD crops on vanilla farmland + a rotting produce-crate stack; vanilla larder-scrap + inert produce/rope GLMs (D4 — FD has no gate) | none | **wired Forest + Meadow** (all 3 tiers, w3) |

### Band 3 — Epic (weight ≈ 1; multi-mod set-pieces + a few big vanilla showpieces)

**All Band 2/3 mod builds obey D4 (§5): derelict, gate-safe loot only — scrap and flavour, never the mod's gate-keys
or a working setup.** The "Contents" below name the *fiction*; every machine/controller/forge is an empty **husk**
and every chest is a scrap pool.

| # | Building | Requires | Contents (derelict — D4/§5 loot) | Mobs | Themes |
|---|---|---|---|---|---|
| ✅ | **Evoker Cell** (mini mansion) | — (vanilla) | woodland-mansion chest, bookshelves | 1 evoker (+ Totem) | dark-forest Forest |
| ✅ | **Vault Cell** (buried trial) | — (vanilla) | 2 trial spawners + a vault | trial waves | Ancient |
| ✅ | **Ocean Ruin** (flooded basin) | — (vanilla) | archaeology + underwater-ruin chest | — | Aquatic |
| ✅ B25 | **Ruined Chapel** | — (vanilla) | a caved-roof stone-brick chapel (open nave, fallen rubble, shattered arched windows), a front bell-cote with a hanging **bell** over a broken door, a chiseled chancel altar lit by a soul lantern behind a broken stained-glass rose window, the **reliquary chest** (emeralds/enchanted book/diamond/golden apple — a real epic reward) | 2–3 undead | **wired Meadow + Rocky** (all 3 tiers, w1) **+ Lush + Mushroom** (thin-theme Explore-floor) |
| ✅ B26 | **Magitech Workshop** | Create + Iron's Spells | an andesite/stone arcane-forge hall (caved roof, breached wall, broken brick forge-flue); a Create machine husk (casing gearbox → cogwheel + shaft → a `mechanical_press`) beside the arcane half (enchanting table + amethyst + candles, a bookshelf study, a brewing nook); vanilla scrap + Create `andesite_alloy` / Iron's `arcane_essence` + `blank_rune` GLMs (D4 both mods) | 2 mages (archevoker + magehunter) | **wired Rocky** (all 3 tiers, w1) |
| ✅ B27 | **Automated Essence Farm (ruin)** | Create + Mystical | a derelict inferium field (farmland rows + tier-1 crops, some trampled) under a stalled harvester **gantry** (vanilla oak posts → rails → cross-bridge carrying a Create `mechanical_drill` husk + cogwheel drive), a west control-chest station, a broken tier-1 `growth_accelerator` + coolant cauldron; vanilla scrap + Create `andesite_alloy` / MA tier-1 `inferium_essence` GLMs (D4 both mods) | 1–2 zombies | **wired Meadow** (all 3 tiers, w1) |
| ✅ B28 | **FE→ME Substation** | IE + AE2 | a gutted roofless hall — an IE concrete/sheetmetal west end (wire-mast + post-transformer husk + capacitor), a certus/fluix AE2 east end, a scorched deepslate **conversion column** (broken conduit antenna) over the **empty controller pit** (⛔ no sky stone / controller / press — the pit is empty); vanilla scrap + IE `wire_copper` / AE2 `fluix_crystal` GLMs | 1 creeper | **wired Rocky** (all 3 tiers, w1) |
| ✅ B29 | **Alchemist's Distillery** | IE + Iron's Spells | a roofless sheetmetal shed around an arcane still (lava-cauldron fire, weathered-copper still-head + brewing stand, a copper condenser column + lightning-rod vent, a water receiver, IE metal-barrel tanks + capacitor) with an amethyst focus + bookshelf study; vanilla brewing scrap + IE `plate_iron` / Iron's `arcane_essence` GLMs (D4) | 1 necromancer | **wired Badlands** (all 3 tiers, w1) |
| B30 | **Sky-Freight Depot** | Create + IE | a stalled train + conveyor husks + crates; cog/ingot scrap | 2 zombies | Rocky |

The catalog is deliberately open-ended — treat B1–B30 as the **starter set**, not a ceiling. New buildings slot in
by the same recipe (template → pool json → theme wiring), so the bench can keep growing after the first batches ship.

## 4. Part C — per-theme wiring matrix

Each terrain family gets a weighted table composed to ~50/35/15 (D2). One structure appears on multiple themes; a
theme's biome filter (`biomes: ["#minecraft:is_taiga", …]`) narrows *where within that theme* it can roll (unchanged
semantics). **Every row is written to all three tiers** (base / `_large` / `huge_`) per [[skyseed-mirror-island-tiers]].

| Theme family | Common (w6) | Rare (w3) | Epic (w1) |
|---|---|---|---|
| **Forest** | Cottage, Tool Shed, Hunter's Blind, Graveyard | Wizard's Hut, Cook's Homestead | Evoker Cell (dark-forest), Ruined Chapel |
| **Meadow** | Cottage, Farmstead, Well, Bandit Ring, Inferium Plot(→rare) | Windmill, Wizard's Hut | Automated Essence Farm, Ruined Chapel |
| **Rocky** | Cairn, Mine Head, Tool Shed | Train Shed, IE Factory, AE2 Lab, Wizard's Tower | Magitech Workshop, FE→ME Substation, Sky-Freight Depot |
| **Ancient** | Cairn, Graveyard, Fossil | AE2 Lab, Wizard's Tower, Cursed Obelisk | Vault Cell, Cursed Obelisk(→epic on huge) |
| **Desert** | Prospector's Camp, Fossil, Well | Drill Rig | (buried temple already exists as a seed) |
| **Badlands** | Prospector's Camp, Mine Head, Fossil | IE Factory, Drill Rig, Cursed Obelisk | Alchemist's Distillery |
| **Aquatic** | Jetty+Rowboat, Well | Watermill | Ocean Ruin |
| **Frozen** | Collapsed Cabin, Well | — | (Impaled Boat lives on huge_aquatic) |
| **Lush** | Well, Shrine, Graveyard | — | Ruined Chapel |
| **Mushroom** | Shrine, Well | — | — |
| **Hamlet / villages** | Cottage, Farmstead, Tool Shed | Windmill, Wizard's Hut | Ruined Chapel |

(Cells are indicative — the exact lists are the §8 review pass once the buildings exist. Frozen/Lush/Mushroom are
thin on mod content today; they lean common + the shared vanilla epics, which is fine.)

## 5. Inert-safety & the string-id `.nbt` engine (reused verbatim)

- **Modded blocks with no mod on the classpath:** author each mod building with vanilla-**analog** blocks + the
  `StructureWriter` `modNames` side-map (`StructureWriter.java:63`), exactly as the BWG villages do — the emitted
  `.nbt` carries real `create:` / `ae2:` / `immersiveengineering:` / `irons_spellbooks:` ids but no mod is ever on
  the build classpath, so CI stays clean. Verify the analog's property schema matches the mod block's (stairs/slab/
  fence subclasses match; a machine block usually maps to a plain full-cube analog).
- **Two gates keep it inert in-world:** the **`requires` mod gate (A3)** filters the candidate out (no RNG, no pad)
  when the mod is absent; and where the building also has a natural biome, the biome gate is a second belt. A building
  that is filtered out is never placed, so its modded-id `.nbt` is never parsed without the mod.
- **Loot is inert by construction:** chests bind vanilla loot-table ids (or the mod's, reached via an inert-safe
  `add_drop` GLM like the Iron's `underwater_ruin_big` injection) — a string id renders without the mod and simply
  yields nothing modded. Mobs go through the `mobs` pack, which already resolve-then-skips unknown ids (fix the
  `MobPlanner` roll-before-resolve determinism bug *first* if a modded mob is ever listed — PLANOFPLANS eng-debt).

### Progression-safe loot (D4) — the derelict-loot allow/deny list

Point mod-structure chests at **scrap / low-tier** pools — a custom `skyseed:` loot table, or a vanilla-ish table + a
token modded scrap via an inert-safe `add_drop` GLM — **never** a mod's own rich structure/dungeon loot table.
Quantities stay small. The gate-keys in the deny column are hard-banned:

| Mod | ✅ OK (scrap / non-gating) | ⛔ Banned (breaks gating) |
|---|---|---|
| **AE2** | a few certus crystals/dust, some fluix, quartz-fibre / ME-cable *scraps*, empty decorative casings, a low-tier component | **sky stone / ME Controller, the 4 inscriber presses, processors, a working setup** — the meteorite-island gate (AE2PLAN: leaking these "hands the endgame away") |
| **Immersive Eng.** | a handful of ingots/plates, wire coils, treated wood, an early blueprint, a machine *husk* (decorative) | a complete multiblock, high-tier blueprints (railgun/excavator), bulk metals that skip the IE ore-island loop |
| **Create** | a few cogs/shafts, andesite casing/alloy, a token kinetic gadget as a trophy | a working contraption, bulk brass / precision mechanisms / electron tubes that skip the andesite→brass grind |
| **Iron's Spells** | low-tier scrolls, arcane essence/ink, a common spellbook (the intended Explore reward via the existing GLM) | top-tier spellbooks/scrolls, Upgrade Orbs, bulk essence that skips the tier climb |
| **Mystical Agri.** | a little inferium / low-tier essence, a broken growth-accelerator part | tier-4/5 essences, prosperity blocks, a full tier-skipping seed set |
| **Multi-mod epics** | the *union* of the OK columns above (still small) | the *union* of the deny columns — the compounding risk: e.g. **FE→ME Substation** must break neither the AE2 press gate nor hand a finished IE power setup |

Pairs with D3: a mod build stays reward-bearing (`explorable: true`) — gate-safety constrains *what* the reward is, not
*whether* there is one. When in doubt, err toward "the interesting bits are missing" — that reads as intended flavour.

## 6. Sequencing & release hygiene

Ship in batches, **per-step version bump + CHANGELOG + inert golden-master gametest** (the standing #30 rule):

1. ✅ **Phase 0 — engine (§2).** A1–A3, A6 built as a **backward-compatible opt-in**; both nodes green
   (1.21.1: 175, 26.1.2: 177), zero behavior change, no golden-master churn. **A4 migration + the A5/A6 new-model
   gametests are deferred into the content phases** (a theme migrates with its content batch), and the large/huge-rate
   open decision (A4) is settled per family as it's touched. *This was the only code-heavy step.*
2. ✅ **Phase 1 — vanilla common (Band 1, B1–B12) SHIPPED** (both nodes green, 1.21.1: 187 / 26.1.2: 189). All 12
   authored no-boxes in `CommonRuinsTemplates`, each with a base+large theme wiring (all 10 terrain families now carry
   1–2 commons) and a per-building assembly gametest on both suite mirrors. **Left within Phase 1:** the `huge_` mirrors
   for the small commons (scale permitting) + the in-game throw-test/tuning pass. Biggest single lift on the
   citadel-dilution goal — done.
3. **Phase 2 — single-mod rare (Band 2, B13–B24).** One mod each; `requires` gate + string-id `.nbt`. Group by mod
   (Create batch, IE batch, AE2 batch, Iron's batch) so each ships with its own inert test. **Per build, run the D4
   loot audit (§5): no chest/mob may drop a deny-column item.**
   - ✅ **Create batch (B13–B16) SHIPPED** (v0.208.0; both nodes green 192/194). Windmill/Watermill/Train Shed/Drill Rig
     authored in `CreateRuinsTemplates` via the `modNames` side-map (all `create:` ids verified against the 6.0.10 jar),
     `requires:["create"]` at w3 on all three tiers of Meadow/Aquatic/Rocky/Badlands/Desert (+ Hamlet). Loot = the vanilla
     `skyseed:chests/create_scrap` table + two inert `add_drop` GLMs (Andesite Alloy / Cogwheel) — D4-clean. 5 gametests
     per mirror. A 4-dimension adversarial review caught + fixed 3 blocked-chest reward-floor bugs, an uncontained-water
     leak, a no-op caved-roof remove, and a clobbered shaft mouth before the green run.
   - ✅ **IE batch (B17–B18) SHIPPED** (v0.209.0; both nodes green 194/196). Dilapidated Factory + Fallen Powerline
     authored in `IeRuinsTemplates` via the `modNames` side-map (all `immersiveengineering:` ids verified against the
     12.4.2 jar), `requires:["immersiveengineering"]` at w3 on all three tiers of Rocky/Badlands (Factory) + Meadow/Desert
     (Powerline). Loot = the vanilla `skyseed:chests/ie_scrap` table + two inert `add_drop` GLMs — D4-clean. 2 gametests
     per mirror (each build carries a vanilla ruin shell to assert on, since IE blocks resolve to AIR without the mod).
     Adversarial review clean (chest-openability held; one stale-mod-id cobweb cleared with `set(...,null)`).
   - ✅ **AE2 batch (B19) SHIPPED** (v0.210.0; both nodes green 195/197). The Gutted AE2 Lab authored in `AeRuinsTemplates`
     via the `modNames` side-map (certus-quartz/fluix ids verified against the 19.2.17 jar), `requires:["ae2"]` at w3 on
     all three tiers of Rocky + Ancient. **D4 — the sharpest gate:** built only from certus/fluix cubes (mid-game, not
     gating) with an *empty controller pit* — **no** `sky_stone_*` (harvestable ⇒ a Controller), **no** `ae2:controller`,
     **no** press/processor in the loot (`skyseed:chests/ae2_scrap` vanilla scrap + inert certus/fluix `add_drop` GLMs).
     1 gametest per mirror (vanilla ruin-shell asserts). Adversarial review before the regen caught + fixed a mob-blocking
     conduit, added the anti-box roof antenna, and a prepend that had displaced `huge_ancient`'s pinned index-0.
   - ✅ **Iron's Spells batch (B20–B22) SHIPPED** (v0.211.0; both nodes green 198/200). The Small Wizard's Hut, Wizard's
     Tower and Cursed Obelisk — **all-vanilla** architecture (Iron's is a mob/item mod, so no `modNames` side-map; the
     "Iron's" flavour is the `requires:["irons_spellbooks"]` gate + the mage `mobs` pack + the loot). Wired at w3 on all
     three tiers of Forest+Meadow (Hut), Rocky+Ancient (Tower), Ancient+Badlands (Obelisk). **D4:** loot =
     `skyseed:chests/irons_wizard` (vanilla arcane scrap) + two inert `add_drop` GLMs layering a token Arcane Essence /
     Common Ink — never an Upgrade Orb, top-tier ink, or named scroll. 3 gametests per mirror. Adversarial review before the
     regen fixed a chest walled inside the obelisk core, a mob-cell buried in the spire (carved a base alcove), and a
     slab-in-wall balcony (made a proper doorway); the wiring preserved `huge_ancient`'s pinned index-0.
   - ✅ **Mystical Agriculture batch (B23) SHIPPED** (v0.212.0; both nodes green 199/201). The Abandoned Inferium Plot — a
     mostly-vanilla derelict essence farm authored in `MysticalRuinsTemplates`; only the inferium farmland/crops/accelerator
     are modded (`modNames` over `FARMLAND`/`WHEAT`/`STONE` analogs, ids verified against the 8.0.27 jar), so they resolve to
     air without the mod and the vanilla shell (scarecrow, trough, fence, chest) is the gametest anchor.
     `requires:["mysticalagriculture"]` at w3 on all three tiers of Meadow + Hamlet. **D4:** kept to tier-1 inferium shown
     derelict — loot = `skyseed:chests/inferium_plot` (vanilla farm scrap) + two inert `add_drop` GLMs layering a little
     Inferium Essence / Prosperity Shard (the mineable base materials), **no** seeds / higher-tier essence / prosperity
     block-or-ore / infusion component. 1 gametest per mirror. Adversarial review before the regen fixed a jigsaw anchor
     under a crop cell, a fence gate whose `FACING` wouldn't join its E/W run, and a silently-overwritten grass tuft.
   - ✅ **Farmer's Delight batch (B24) SHIPPED** (v0.213.0; both nodes green 200/202) — **Band 2 COMPLETE.** The Overgrown
     Cook's Homestead authored in `FarmersRuinsTemplates`; a vanilla log-and-cobble cabin shell with modded kitchen fittings
     (`modNames` over a `SMOKER` facing analog for the appliances/cabinets, a `STONE` analog for the crates, a `WHEAT` analog
     for the crops — ids verified against the 1.3.2 jar; ages kept ≤3 for tomatoes/rice). `requires:["farmersdelight"]` at w3
     on all three tiers of Forest + Meadow, **no mobs** (a peaceful homestead). **D4:** FD has no progression gate, so loot is
     flavour-only — `skyseed:chests/cook_homestead` (vanilla larder scrap) + two inert `add_drop` GLMs layering a little raw
     produce (onion) + rope. 1 gametest per mirror. Adversarial review before the regen fixed two corner-post overwrites (a
     garden weed and a mushroom).
   - **Phase 2 (Band 2) is DONE** — B13–B24, eleven mod-gated rares across Create / IE / AE2 / Iron's / Mystical / Farmer's.
4. **Phase 3 — epic (Band 3, B25–B30).** Multi-mod set-pieces + the vanilla showpieces; rarest weights. **D4 loot
   audit against BOTH required mods' deny-lists** (the compounding case — esp. the AE2 press gate on B28).
5. **Phase 4 — tuning pass (§8).** In-game throw-tests per theme; adjust weights to feel; confirm the citadel/mansion/
   monument now read as standouts.

Each new building follows the **regen dance** ([[skyseed-structure-staging]]): edit `*Templates.java`, delete only the
changed `.nbt` from repo-root src, regen on the **1.21.1 node** (`JAVA_HOME=…jdk-21… ./gradlew :1.21.1:runGameTestServer`),
then green the 26.1.2 node (loads the shared `.nbt` via DataFixerUpper — never regen there). Add each gametest to
**both** suite mirrors.

## 7. Risks

- **RNG re-capture (Phase 0).** The roll-model change shifts the shared stream on hit-seeds; every golden master that
  pins a rare-structure outcome must be re-captured on 1.21.1. Contained + one-time, but do it deliberately.
- **Inert-safety regressions.** A `requires`-gated building that consumes RNG *before* the gate (or a modded mob rolled
  before `resolveEntity`) breaks determinism parity. Mitigate: gather-then-gate in `rollRare` (A2), and fix the two
  `MobPlanner` findings before listing any modded mob.
- **Placement / bounding boxes.** Single-piece buildings on a levelled `pad` are the low-risk case (the villages'
  multi-piece jigsaws were the hard one), but each still needs an on-pad assembly gametest (#29) and a `pad`/`sink`
  that seats it flush — mind the door-flush geometry ([[skyseed-door-facing]]) and that NBT can't hold negative
  positions (inset overhangs).
- **Analog schema mismatch.** A mod machine block whose analog has the wrong property set serialises a broken state.
  Verify each analog in the on-pad test (the block reads as the intended mod block in-world with the mod present).
- **Weight drift.** As the bench grows, a theme's table can quietly slide off 50/35/15. The Phase 4 pass + a small
  "sum the mass per band" sanity check keeps it honest.
- **Explore reward-floor gaps (D3/A6).** Adding filler `explorable:false` builds, or gating rewarding ones behind an
  absent mod, can leave a theme/biome/tier with **no** eligible Explore build — so a premium seed silently grows a
  plain island. The A6 coverage gametest is the guard; run it per batch, not just at Phase 0, since Phase 1–3 keep
  changing each theme's eligible set.
- **Gating leaks (D4).** A mod build that drops a gate-key (AE2 sky stone/press the sharpest — it skips the whole
  meteorite pillar) quietly guts a progression the pack spent real effort locking. Mitigate: author mod loot from
  curated *scrap* pools per the §5 allow/deny list, never a mod's own rich table; and add a **loot-audit checklist**
  to each mod-build's PR step (§6 Phase 2/3) — "does this chest/mob drop anything in a deny column?" Watch the epics
  hardest, where two mods' deny-lists compound.

## 8. Open decisions

1. **Per-theme final lists (§4).** The matrix cells are indicative; lock them in the Phase 4 review once the buildings
   exist and can be thrown in-game.
2. **Vanilla in the epic band.** Recommend **yes** — promote the existing evoker/vault/ocean showpieces (and B25 Ruined
   Chapel) so a mod-light pack still occasionally gets a "wow" build. Confirm.
3. **Thin themes (Frozen/Lush/Mushroom).** Accept common-heavy tables, or author a couple of bespoke vanilla epics for
   them (a frozen crypt, a lush shrine)? These themes are also the ones most at risk of an **Explore reward-floor gap**
   (D3/A6) — if their only builds are filler, the Explore seed there grows a plain island. Recommend giving each thin
   theme at least one reward-bearing, `explorable` build (the shared vanilla epics — Ruined Chapel, a frozen crypt —
   cover this cheaply). Revisit fuller tables if they still feel empty.
4. **Should any theme run hotter/colder than 5%?** Default all overworld terrain themes to `rare_structure_chance:
   0.05`. The structure-seeds (dungeon/mineshaft/etc.) and Nether/End are out of scope here. (The Explore seed ignores
   this rate — it forces a build regardless, D3 — so the rate only tunes the *ordinary* per-seed surprise.)
