# IEPLAN — Immersive Engineering + flight (NeoForge 1.21.1)

> Child of [CONTENTPLAN §2 / §7](CONTENTPLAN.md) — PLANOFPLANS **#34** (IE ore island), **#35** (Excavator),
> **#45** (IE quest chapter), **#39** (FE-flow proof). IE is the **decided tech backbone** (Mekanism dropped,
> CONTENTPLAN §7). Immersive Aircraft is a *separate* project (not an IE addon) but is tracked here because the
> `iaie` + Create crossovers bind it to the IE/Create backbone and it shipped in the same wave.

## ✅ Landed — jars in `overrides/mods/` (`mods.txt` @ 114 mods)

| Jar | Role | Deps (all present) |
|---|---|---|
| `ImmersiveEngineering-1.21.1-12.4.2-194.jar` | The tech backbone. Native **Forge Energy** — plugs into the shipped Create → Crafts&Additions → Flux Networks grid with no bridging | none (self-contained) |
| `ImmersivePetroleum-1.21.1-4.4.1-37.jar` | IE oil addon — crude oil → Distillation → diesel/kerosene/gasoline/lubricant; Pumpjack/Derrick/Distillation Tower (all FE-powered). **The premium aircraft-fuel tier** (see below) | IE |
| `Engineers Delight 1.21.1 neoforge R.1.8.jar` | **Farmer's Delight × IE** bridge (Bottling-Machine food integration: tomato sauce/melon juice → bottles/bowls). The `create-otbwg-compat`-style bridge jar | FD + IE |
| `immersive_aircraft-1.4.6+1.21.1-neoforge.jar` | Standalone flight — 7 aircraft. **Void-safe**: every part is craftable (Engine = boiler + cobble + blast furnace + pistons), no worldgen dependency | none |
| `iaie-1.0-1.21.1.jar` | **Immersive Aircraft × IE** — aircraft burn fuels tagged `iaie:fuel`; **IE biodiesel** by default; diesel/kerosene too **iff Immersive Petroleum is added** | IA + IE |
| `create_completeimmersiveaircraft-1.0.7-neoforge-1.21.1.jar` | **Create × Immersive Aircraft** integration | Create + IA |
| `kubejs-neoforge-2101.7.2-build.368.jar` + `rhino-2101.2.7-build.85.jar` | Scripting vehicle for recipe/tag tweaks (Excavator fallback, broken-FD-compat cleanup, gating). **Scripts not yet authored** — `overrides/kubejs/` is empty | Rhino |

*(QoL riders in the same batch, **not** IE-related: `Camera Zoom 1.0.6`, `visualhealth 2.0.2`.)*

**Deliberately NOT added** (matches the curation): Immersive Aircraft: Better Engines (optional depth),
AlmostUnifiedIE (IE is the sole aluminum/lead/silver/nickel/uranium source — nothing to unify), Engineered
Compatibility (near-zero overlap with the installed set today — revisit only if Iron's Spells lands).

## Configs present (committed under `overrides/config/`)

- **`immersiveengineering-startup.toml`** — two settings that matter:
  - `preferredOres = ["immersiveengineering", "minecraft"]` — governs **which mod the Excavator's ores and
    associationless crushing recipes resolve to**. Leave IE first.
  - `[compat] create = true` (also `theoneprobe`, `computercraft`) — **IE has *native* Create compat.** Confirms
    no Create↔IE bridge jar is needed; the two interoperate through this flag + the FE grid.
- **`immersiveengineering-client.toml`** — cosmetic.
- **`immersive_aircraft.json`** — `acceptVanillaFuel: true`, `fuelList { minecraft:blaze_powder: 1200 }`, all
  three `validDimensions` true → aircraft fly in the Skyseed void overworld/Nether/End.
- **`immersivepetroleum-server.toml`** — Pumpjack 1024 FE/t → 15 mB oil/t; Distillation/Coker/Hydrotreater;
  `autounlock_recipes = true` (no gating problem); Portable-Generator + Motorboat fuel lists. **No reservoir
  section** — reservoirs are data-driven (see the IP section below).
- **`immersiveengineering-server.toml`** — now on disk (still a **per-world** file: it belongs in
  `saves/<world>/serverconfig/`, or `overrides/defaultconfigs/` to ship pack-wide — **neither is wired yet**).
  Key values: `[machines.excavator] chance = 0.9` (per-chunk perlin, **not** tied to physical ore — works over
  void); `[ores.*]` bauxite/lead/silver/nickel/uranium are **physical worldgen** (dead in the void — the
  decoration step is skipped, so the ore island below is the hand-mining path); `preferredOres` IE-first.

## Governing invariants (do not regress)

- **Create ↔ IE needs no jar** — native `[compat] create = true` + the FE grid (C&A + Flux). Don't add a bridge.
- **MA auto-seeds IE metals** — Mystical Agriculture mints seeds for IE's registered ores/ingots automatically;
  bespoke IE ore islands ship *alongside* MA (the settled #38 convention), never instead of it.
- **Mekanism stays dropped** (CONTENTPLAN §7) — no osmium/second-backbone tier.
- **Aircraft are void-native** — keep all three `validDimensions` true; don't gate flight behind worldgen.

## Excavator (#35) — reframed: config, not a mixin

The plan assumed the Excavator was dead in the void because it "samples worldgen mineral veins that don't exist."
**The server config confirms otherwise:** `[machines.excavator] chance = 0.9` is a **perlin-noise threshold** —
mineral veins are a per-chunk data layer assigned on demand by `ExcavatorHandler`, *not* physical ore blocks, so
they exist over empty sky. The FTB wiki agrees: the Excavator *"will function in void worlds because Mineral
Deposits can be in all chunks."* (This is the opposite of IP's oil reservoir, which *is* a worldgen feature — see
below.)

Plan:
1. **Verify in-game first.** Build an Excavator on an island and confirm it yields veins.
2. If it works → **pin the serverconfig** into `overrides/defaultconfigs/immersiveengineering-server.toml`
   (`excavator_chance` > 0; ensure the Skyseed overworld dim is **not** in `excavator_dimBlacklist`, which by
   default only lists The End). **This closes #35 with config** — the island-aware mixin (former option 1) is
   demoted to a nice-to-have we don't need.
3. If it misbehaves → fallback (former option 2): **KubeJS-remove the Excavator recipe + hide it in JEI**, and
   supply aluminum/bauxite via the #34 ore island instead.

## Immersive Petroleum — integration status

**Fuel chain: ✅ already wired by the mods themselves — no work needed.** Traced through the jar datapacks:
`iaie:fuel` → `#immersiveengineering:drill_fuel` → `#neoforge:diesel` / `kerosene` / `diesel_sulfur` (IP ships all
of these fluid tags, and IE's own biodiesel merges into `drill_fuel`). **So with IP in, aircraft automatically
accept IE biodiesel + IP diesel/sulfur-diesel/kerosene as fuel.** The `iaie` fuel description ("diesel/kerosene
burn iff Immersive Petroleum is installed") is realized purely through tags. Nothing to author. ✅

**Oil acquisition: ⚠ BLOCKED out of the box — this is the real integration task.** Unlike the IE Excavator, IP's
oil reservoir is a **worldgen feature**: `data/immersivepetroleum/neoforge/biome_modifier/reservoir.json` adds
`immersivepetroleum:reservoir` at the `underground_ores` decoration step. But `SkyseedVoidChunkGenerator`
(`worldgen/SkyseedVoidChunkGenerator.java:59`) **overrides `applyBiomeDecoration` to return early when
`skipDecoration` is true** — which is the case for the void **overworld and Nether**. So the reservoir feature
never runs there. And the oil reservoir (`recipe/reservoirs/oil.json`) **blacklists `minecraft:the_end`**, the one
dim that keeps decoration. **Net: crude oil is unobtainable in a Skyseed world as shipped.** (Biome filter is
`isBlacklist:false, list:[]` = any biome — so biomes aren't the problem; the skipped decoration step is.)

Resolution — **✅ SHIPPED: crude oil as an uncommon deep-band ore pocket.** Per the design call (oil should be an
uncommon find on *existing* islands, no separate seed) and IP's own manual (*"a slick black substance found while
mining near bedrock"*), crude oil is seeded as a small `deep_core` vein of `immersivepetroleum:crudeoil_fluid_block`
source blocks in the **deep band** of the mining islands — `max_y:8` on the rocky family, `max_y:20` on the ancient
family (all six `immersiveengineering_*` overrides), `chance 0.15`. Why this shape:
- **Inert-safe** — the ore planner skips an unknown block *before* the RNG roll (`worldgen/OrePlanner.java:53`), so
  without IP the file is byte-identical. (A crude-oil *pond* was rejected: `resolveBlock` falls back to **water**
  for a missing fluid (`IslandGenerator.java:325`), so an oil pond would spawn water ponds on everyone's rocky
  islands without IP — not inert. Ore, not pond.)
- **Stays put** — `deep_core` (lower 40% of the core) leaves the oil walled by the stone body, placed with the
  no-neighbour-update grow-in flags so it doesn't flow until breached. Extract with the **IE Fluid Pump**
  (`placeCobble` keeps the drain clean). Distillation/refining downstream is unchanged.
- **Signposted** — "deep throw = deep resource" is an established pack idiom, and IP's manual points the player at
  near-bedrock oil; broaden to more themes (badlands/desert) later if it plays too rare.

⚠ **In-game verify** (like the excavator): confirm a statically-placed crude-oil source block (a) survives grow-in
without flowing away and (b) is pumpable by the IE Fluid Pump. If a placed `LiquidBlock` won't register as a pump
source, fall back to a KubeJS crude-oil recipe. Tune `chance` after a few throws.

## Open work — ✅ ALL BUILT (both nodes green); in-game verifies left

Shipped end-to-end (detail in `CHANGELOG_1.21.1.md` + git):

- **#34 IE metals ore island** — six inert-safe `immersiveengineering_*` overrides: rocky family aluminum/lead/nickel,
  ancient family deep aluminum/silver/uranium; veins merged into every base Y-band; gametest-guarded.
- **Crude oil (IP)** — an uncommon `deep_core` pocket of `immersivepetroleum:crudeoil_fluid_block` in the deep band of
  the six IE overrides (rocky `max_y:8`, ancient `max_y:20`, chance 0.15), pumped with the IE Fluid Pump; inert-safe.
- **#35 Excavator** — config **pinned** (`overrides/defaultconfigs/immersiveengineering-server.toml`, `chance` 0.9→0.7);
  the island-aware mixin is demoted to a nice-to-have we don't need (veins are per-chunk data, work over the void).
- **FD×IE compat** — FD 1.3.2's IE integration recipes verified byte-identical to IE's native ones; the 5 genuinely
  broken *bridge* recipes (Engineers Delight fermenter/bottling; `create_completeimmersiveaircraft` rotary_cannon/telescope) fixed via KubeJS overrides.
- **Flight fuel chain** — wired purely through tags: vanilla fuel / blaze powder → IE biodiesel → IP diesel/kerosene, all `iaie:fuel`.
- **Quark Engineering** — `QuarkEngineering-1.21.1-5.10.29.jar` added to `overrides/mods/` + `mods.txt` (IE sawmill/smelting recipes for Quark woods + IE raw-ore blocks).
- **#45 IE quest chapter** — `chapters/immersiveengineering.snbt` (A009, 15 quests B9xx): Aluminium → Hammer/Manual →
  Windmill/LV → Coke Oven→Blast Furnace→Steel→Crusher/Excavator → Fluid Pump→Crude Oil→Distillation→Diesel Gen. The
  flight line (B916–B919) moved to Tools & Travel (buildable/coal-fuelled without IE); an optional "Aviation Fuel"
  (B920) teaches the diesel upgrade without ever blocking flight.

**Left — in-game verifies only** (can't run headless):

- [ ] Excavator yields veins over the void under a grown island; fine-tune `chance`.
- [ ] The crude-oil source survives grow-in (doesn't flow away) + is IE-Fluid-Pump-extractable; tune `chance`. If a placed `LiquidBlock` won't register as a pump source, fall back to a KubeJS crude-oil recipe.
- [ ] All five IE metals appear at low/mid/high throws.
- [ ] A diesel-fuelled aircraft actually consumes it; both the IE + flight quest chapters load.
- [~] **(#39)** the FE-flow in-game sign-off (shared with [AE2PLAN.md](AE2PLAN.md) #39).

## Caveats

- Youngish NeoForge ports — keep versions pinned (esp. `iaie` and `create_completeimmersiveaircraft`) and watch
  patch notes before quests point at their content.
- Watch the `iaie:fuel` tag name across `iaie` updates before scripting fuel additions.
