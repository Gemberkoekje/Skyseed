# QUARKPLAN — Quark integration (NeoForge 1.21.1)

Child plan of [CONTENTPLAN §2 "Building / palette"](CONTENTPLAN.md) — PLANOFPLANS items **#15** (curate modules
+ add Zeta) and **#43** (future quest chapter). Grounded in a **2026-07-02 availability research pass**:
four parallel web-research threads, with the pivotal availability claims independently fact-checked twice
(GitHub releases / CurseForge file pages / the Violet Moon forum — the Modrinth API was proxy-blocked in the
research sandbox, so Modrinth listings were confirmed via their version pages; spot-check jars on download).

> **Status: PLANNED — nothing installed yet.** The blocking question ("does Quark even exist for NeoForge
> 1.21.1?") is **resolved: yes, officially.** This plan carries the jar list, the module curation for a void
> skyblock, the island-integration opportunities, and the partner-gated follow-ons. The work itself (#15) is
> still open.

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
| **QuarkPonders** | 1.4 (2026-03-19) — [Modrinth](https://modrinth.com/mod/quarkponders) | Third-party addon: Create-style **Ponder scenes for Quark features**. Players already know the Ponder UI from Create → Quark documents itself in-game for free. |

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

## Module curation (the actual #15 work)

Curate on first run, then commit the curated config from the dev instance into `overrides/config/`
(committed like `entityculling.json`; expected file `config/quark-common.toml` — verify the exact
Zeta-era filename on first run).

### Disable — void-dead worldgen (never fires; islands are the only worldgen)
**Glimmering Weald** underground biome (also dodges the known 1.21.1 biome-cycle bug
[Quark#5340](https://github.com/VazkiiMods/Quark/issues/5340)), **corundum clusters**, **new-stone-type veins**
(limestone/jasper/shale/myalite), **big dungeons**, **monster boxes**, **fallen logs**, **blossom trees**,
**fairy rings** — sweep the whole World category off by default; selected palettes come back via islands (below).
*(Aside: our `overrides/config/entityculling.json` already whitelists `quark:soul_bead` — a Weald mob drop;
harmless inherited default, no action.)*

### Disable — overlaps with shipped mods (duplicated features = config burden, not value)
- **Oddities Backpack** → Sophisticated Backpacks is the pack's backpack.
- **Oddities Pipes + Crates** → Create logistics + Sophisticated Storage are the pack's identity there.
- **Pathfinder Maps** → harmless but useless in a void world (they point into the empty noise field); Xaero's + Waystones own navigation.

### Keep — the value
- **Building/deco breadth:** vertical slabs, hedges, hollow logs, variant wood blocks (chests/bookshelves/ladders/posts),
  more brick types, framed glass, shingles/thatch, rope. Complements rather than duplicates Create Deco / Design 'n'
  Decor / Rechiseled (stone-and-industrial flavored) — this was the CONTENTPLAN's whole case for Quark.
- **Inventory sorting + chest-management buttons** — a real gap: Mouse Tweaks only drags, Sophisticated Storage only
  sorts its own containers.
- **Matrix Enchanting** (Oddities) — perfect for a skyblock where enchanted loot doesn't generate; pairs with
  Create: Enchantment Industry rather than fighting it (test the pairing in the smoke pass).
- **Totem of Holding** (Oddities) — death recovery in a pack where players fall into the void. ⚠ **Test a void death**
  to confirm where the totem spawns before relying on it in quest text.
- **Feed Trough / animal pen QoL** — synergizes with the shipped animal-pen structure islands.
- Client QoL by taste: usage ticker, auto-walk, camera mode, emotes, item sharing.

### Test-before-keep
- **Pistons Move Tile Entities** × Create contraptions — that pairing has a history of weirdness; disable if anything dupes.
- Quark inventory tweaks × Mouse Tweaks bindings — commonly run together, but confirm no double-handling.

## Island integration opportunities (the Skyseed lens — later, optional)

Worldgen-only content isn't dead weight here; it's island material (same lever as BWG/zinc/MA):

1. **Corundum → a crystal-island theme** — Quark's corundum clusters beg to be a bespoke island tier (or a
   rocky/ancient band) once we want another palette.
2. **Quark stones on rocky palettes** — limestone/jasper/shale via `theme_override` bands on Rocky/Ancient
   (myalite is End-flavored → End island). Data-only, inert without Quark, gametest per band — the MA/zinc pattern.
3. **Ancient Tomes → structure-island loot** — loot-gated, so either keep disabled or inject into the
   dungeon/mansion/trial loot tables (the exact lever already planned for Iron's Spells #36).
4. **Blossom trees** — grant saplings via a biome-island band or loot instead of worldgen, if wanted for deco.

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
3. **Hold That Thought** — die (or read about it) → recover via Totem of Holding *(only if the void-death test passes)*.

## Build order

1. **Add the 4 jars** (Quark 4.1-481, Zeta 1.1-40, Quark Oddities marker, QuarkPonders 1.4) → `./gen-mods-txt.ps1`
   to refresh `mods.txt`.
2. **First-run module curation** per the lists above → commit the curated config to `overrides/config/`.
3. **Smoke pass (in-game):** Pistons-Move-TEs × a Create contraption; a **void death** with Totem of Holding;
   sorting alongside Mouse Tweaks + Sophisticated; Matrix Enchanting × Enchantment Industry.
4. *(Later, optional)* island integrations: corundum crystal island / Quark stones on rocky bands / Ancient-Tome
   loot injection / blossom saplings.
5. *(Partner-gated)* **Quark Engineering** when IE (#34) lands; **Farmer's Cutting: Quark** when FD (#16) lands.
6. *(Last)* the minimal quest branch (#43).
