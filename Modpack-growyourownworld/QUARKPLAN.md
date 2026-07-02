# QUARKPLAN — Quark integration (NeoForge 1.21.1)

Child plan of [CONTENTPLAN §2 "Building / palette"](CONTENTPLAN.md) — PLANOFPLANS items **#15** (curate modules
+ add Zeta) and **#43** (future quest chapter). Grounded in a **2026-07-02 availability research pass**:
four parallel web-research threads, with the pivotal availability claims independently fact-checked twice
(GitHub releases / CurseForge file pages / the Violet Moon forum — the Modrinth API was proxy-blocked in the
research sandbox, so Modrinth listings were confirmed via their version pages; spot-check jars on download).

> **Status: SHIPPED (v0.182.0) — jars in, modules curated, Totem-of-Holding void fix live.** The 4 jars are
> installed (see `mods.txt`), the curation below is applied to `overrides/config/quark-common.toml`, and the
> void-death Totem shrine (Skyseed relocation) works in-game. **Remaining:** the in-game smoke-pass sign-off (#15),
> the minimal quest chapter (#43), the partner-gated follow-ons, and the island integrations — now their own
> committed plan, [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md) (#71).

## The headline (research result, verified 2026-07-02)

Violet Moon shipped the **full public NeoForge 1.21.1 release on 2026-02-14** (Quark 4.1-473 + Zeta 1.1-38,
"Welcome to 1.21.1!") after a 2025 supporter-alpha phase, and has patched steadily since (4.1-475…481).
1.21.1 is the **newest** MC version with actual releases; the 1.21.1 line is **NeoForge-only** (1.20.1 and
earlier were Forge). No forks or unofficial ports exist — none are needed. Two modules were dropped in the
port (*Enchantments Be Gone*, *Enchantment Predicates* — per the [porting tracker](https://github.com/VazkiiMods/Quark/issues/5228));
everything else survived, including the Oddities set.

**Version pairing (from the official announcements):** Quark 4.1-473 ↔ Zeta 1.1-38; Quark 4.1-477…481 ↔ Zeta 1.1-40.

## Mods to add (4 jars)

| Mod | Version (2026-07-02) | Why |
|---|---|---|
| **Quark** | 4.1-481 (2026-06-29) — [Modrinth](https://modrinth.com/mod/quark) | The core: building blocks, decoration, QoL — ~200 features, all per-module toggleable via Zeta config. |
| **Zeta** | 1.1-40 (2026-04-24) — [Modrinth](https://modrinth.com/mod/zeta) | Hard dependency (Quark's extracted module framework, ARL successor). |
| **Quark Oddities** | 1.21.1 marker jar (2026-02-14) — [Modrinth](https://modrinth.com/mod/quark-oddities) | A 936-byte flag jar that enables the Oddities modules already inside Quark (Matrix Enchanting, Totem of Holding, Crates, Magnets, Pipes, Backpack). We want it for **Matrix Enchanting + the Totem**, then disable the overlapping modules (below). |
| **QuarkPonders** | **1.5.1 installed** (plan named 1.4) — [Modrinth](https://modrinth.com/mod/quarkponders) | Third-party addon: Create-style **Ponder scenes for Quark features**. Players already know the Ponder UI from Create → Quark documents itself in-game for free. |

## Add later (partner-gated) / skip

- **Quark Engineering** (`QuarkEngineering-1.21.1-5.10.29`, [CurseForge](https://www.curseforge.com/minecraft/mc-mods/quark-engineering)) —
  Quark × **Immersive Engineering** compat (IE sawmill recipes for Quark woods, variant bookshelves). Add when **IE (#34)**,
  the decided tech backbone, lands.
- **Farmer's Cutting: Quark** ([datapack](https://modrinth.com/datapack/farmers-cutting-quark), 1.21.1-1.0) — FD cutting-board
  recipes for Quark woods. Add when **Farmer's Delight (#16)** lands.
- ❌ **Quark Delight** — dormant, 1.20.1 ceiling. ❌ **Ascended Quark** — has a 1.21.1 build now, but it's Aether compat; no Aether in the pack.
- ❌ **Alternatives deliberately not taken:** the Supplementaries/Amendments (Moonlight) family and the single-feature ports
  (Nemo's Inventory Sorting, Bridging Mod, Connected Glass, …) are all confirmed on NeoForge 1.21.1 but would duplicate
  official Quark. Revisit **only if the port proves unstable in the smoke pass** — the research thread has the candidates.

## Module curation (#15) — APPLIED to `overrides/config/quark-common.toml`

Curated by hand in the committed config (Zeta reads/normalises the file but preserves values). Only well-justified
toggles were flipped — see the ⚠ revision below, which is the important correction to the original plan.

### Disabled — overlaps with shipped mods (applied)
- **Oddities Backpack** (`Backpack = false`) → Sophisticated Backpacks is the pack's backpack.
- **Oddities Pipes + Crates** (`Pipes = false`, `Crate = false`) → Create logistics + Sophisticated Storage own that.
- **Pathfinder Maps** (`Pathfinder Maps = false`) → useless in a void world (they point into empty noise and can offer
  confusing cartographer/wandering-trader trades); Xaero's + Waystones own navigation.

### Disabled — the one worldgen module that misbehaves (applied)
- **Glimmering Weald** (`Glimmering Weald = false`) — it adds a biome to the source, which trips the 1.21.1
  biome-cycle bug [Quark#5340](https://github.com/VazkiiMods/Quark/issues/5340); it's also void-dead. *(Aside: our
  `entityculling.json` whitelists `quark:soul_bead`, a Weald drop — a now-inert inherited default, no action.)*

### ⚠ REVISED — do NOT sweep the whole World category off
The earlier draft said to disable all of World (corundum, new stone types, blossom trees, fallen logs, fairy rings,
monster boxes, big stone clusters …). **That is wrong for the block-providing modules:** disabling a Zeta module
*unregisters its blocks*, so the `theme_override`s that reuse those blocks (#71) would resolve to nothing — the whole
island-integration idea depends on the blocks existing. And their *worldgen* is already inert in a void world (no
terrain to attach to), so leaving it on costs nothing. Therefore **keep New Stone Types, Corundum, and Blossom Trees
ENABLED** — their blocks are the raw material for [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md). The pure-worldgen features
simply never fire and need no toggle. Only Glimmering Weald is off (the bug above).

### Keep — the value
- **Building/deco breadth:** vertical slabs, hedges, hollow logs, variant wood blocks (chests/bookshelves/ladders/posts),
  more brick types, framed glass, shingles/thatch, rope. Complements rather than duplicates Create Deco / Design 'n'
  Decor / Rechiseled (stone-and-industrial flavored) — this was the CONTENTPLAN's whole case for Quark.
- **Inventory sorting + chest-management buttons** — a real gap: Mouse Tweaks only drags, Sophisticated Storage only
  sorts its own containers.
- **Matrix Enchanting** (Oddities) — perfect for a skyblock where enchanted loot doesn't generate; pairs with
  Create: Enchantment Industry rather than fighting it (test the pairing in the smoke pass).
- **Totem of Holding** (Oddities) — death recovery in a pack where players fall into the void. ✅ **Void fix shipped
  (v0.182.0):** a totem that spawns below y50 (a void plunge) is otherwise unreachable, so Skyseed keeps its x/z,
  raises it to the island band, and builds a small lit shrine under it (`TotemShrineEvents`, decoupled — no-ops
  without Quark Oddities). Confirmed in-game.
- **Feed Trough / animal pen QoL** — synergizes with the shipped animal-pen structure islands.
- Client QoL by taste: usage ticker, auto-walk, camera mode, emotes, item sharing.

### Test-before-keep
- **Pistons Move Tile Entities** × Create contraptions — that pairing has a history of weirdness; disable if anything dupes.
- Quark inventory tweaks × Mouse Tweaks bindings — commonly run together, but confirm no double-handling.

## Island integration opportunities → now [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md) (#71)

Worldgen-only content isn't dead weight here; it's island material (same lever as BWG/zinc/MA). All four are now
committed (the user asked to build them), so they moved out of "later, optional" into their own plan — all as
**optional extras on existing islands (NO new Quark seeds/tiers, decided 2026-07-02):** corundum geodes + Quark stones
on Rocky/Ancient · Ancient-Tome structure-island loot · blossom saplings on biome bands. The mechanics,
phasing, and the key findings (e.g. the structure-islands already roll the vanilla loot tables Quark's Ancient Tomes
inject into) live in [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md).

## Caveats

- **Young port** (public ~4.5 months at research time). Pin the exact pairing (4.1-481 + 1.1-40), watch patch notes,
  and run the smoke pass before quests point at Quark features.
- Research verification note: Modrinth API was unreachable from the research sandbox (egress proxy), so verification
  used CurseForge file pages, GitHub releases, and the Violet Moon forum — three agreeing sources; still, spot-check
  the downloaded jars.
- Supplementaries×Quark alpha-era incompat reports are irrelevant (we ship neither Supplementaries nor the alphas).

## Quest chapter (#43 — build last, keep minimal)

Per QUESTPLAN, Quark gets **minimal** coverage — a short branch (under Tools & Travel, or a mini-chapter), only
after the jars + curation ship and the smoke pass is green:

1. **Sort Your Life Out** — the sorting buttons / chest QoL (checkmark or use-once).
2. **Enter the Matrix** — build a Matrix Enchanting setup (gated on the enchanting basics).
3. **Hold That Thought** — die (or read about it) → recover from the Totem of Holding at its shrine *(void fix shipped v0.182.0 — cleared)*.

## Build order

1. ✅ **Added the 4 jars** (Quark 4.1-481, Zeta 1.1-40, Quark Oddities marker, QuarkPonders **1.5.1**) — in `mods.txt`.
2. ✅ **Module curation applied** to `overrides/config/quark-common.toml` (the lists above).
3. **Smoke pass (in-game)** — ✅ **void death with Totem of Holding** (shrine relocation, v0.182.0). Still to verify:
   Pistons-Move-TEs × a Create contraption; sorting alongside Mouse Tweaks + Sophisticated; Matrix Enchanting ×
   Enchantment Industry. *(This is the remaining #15 sign-off.)*
4. **Island integrations** — committed plan [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md) (#71): corundum crystal island /
   Quark stones on rocky bands / Ancient-Tome loot / blossom saplings.
5. *(Partner-gated)* **Quark Engineering** when IE (#34) lands; **Farmer's Cutting: Quark** when FD (#16) lands.
6. *(Last)* the minimal quest branch (#43).
