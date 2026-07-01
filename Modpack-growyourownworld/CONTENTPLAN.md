# CONTENTPLAN — Skyseed: Grow your own world

Content-mod integration plan (NeoForge **1.21.1**). Companion to BEAUTIFYPLAN.md.

> **Status (PR #14):** **Mystical Agriculture** is integrated (shipped) and **Oh The Biomes We've Gone** is integrated for
> the forest-family woods (a Forest seed over a BWG biome grows that wood island — see BWGPLAN; the wet/fantasy woods are
> still pending). Other content mods remain to be slotted in via the levers below.

> **Plan audit (2026-07-01):** this plan predates a lot of shipped work — **11 items are marked ✅ below**: the void
> ChunkGenerator (Order 0), the full Create + Crafts & Additions + Flux Networks power backbone, Silent Gear, all seven
> curated Create addons (§3), the Mystical Agriculture starter, and the `gen-mods-txt.ps1` manifest refresh. The remaining
> tech integrations (Mekanism / AE2 / …), Quark, Farmer's Delight, and the §7 decisions are still open — see
> [`../PLANOFPLANS.md`](../PLANOFPLANS.md) for their priority.

**The lens:** Skyseed is a *void* skyblock — nothing generates naturally; all content arrives via
seed-grown islands. So every mod's resources/structures need an on-island acquisition path. Effort is
*not* the gate (we'll happily build islands); the only real questions are **thematic fit** and **hard
mechanical conflicts** that islands/config can't solve.

**Integration levers (per mod):**
1. **Extend an existing island seed** — add a mod's ores/plants/blocks to a fitting biome/rocky/nether island.
2. **New island tier** — a bespoke island gated behind progression ("each content mod = a tier").
3. **Loot / spawn injection** — drop a mod's loot into existing structure-island loot tables; let its
   mobs spawn on grown biome islands.
4. **Config** — neuter a mod's own worldgen (mostly inert in the void anyway) so it can't fight Skyseed.

> **BYG replaces BetterNether/BetterEnd.** Officially maintained (no unofficial-port risk), and one mod
> gives Overworld + Nether + End **block/flora/wood/mob palettes** — exactly the building material we want
> for new island tiers. The worldgen is irrelevant (void); we mine it for content.

---

## 1. Power & automation backbone (the interop plan)

Standardize on **Forge Energy (FE/RF)** as the shared currency. Create is the one outlier and gets bridged.

| Mod | Native power | Talks FE? | How it joins the grid |
|---|---|---|---|
| **Immersive Engineering** | Immersive Flux (= FE) | ✅ native | directly |
| **Mekanism** | Joules (internal) | ✅ via Universal Cables + configurable Joule↔FE ratio | directly |
| **Applied Energistics 2** | AE (internal) | ✅ accepts FE input | directly |
| **Create** | Rotational **Stress Units** | ❌ | **Create: Crafts & Additions** — Alternator (rotation→FE @75%, needs ≥32 RPM) + Electric Motor (FE→rotation) |

- **Bridge mod (required for the ask):** [Create: Crafts & Additions](https://modrinth.com/mod/createaddition)
  (`createaddition`, NeoForge 1.21.1-1.6.0). This is what makes Create ↔ Mekanism/IE/AE2 power flow possible.
- **Inter-island transfer:** **Flux Networks** (wireless FE) — run no cables across the void; a Flux Point
  on each island shares one global FE network. Strongly recommended given the floating-island layout.
- **Cabling:** all three FE mods interconnect, but mixing cable *types* is fiddly. Use **Flux Networks** as the
  universal FE backbone, or pick one cable family (e.g. Mekanism Universal Cables) for local runs.

**Natural power progression** (fits the skyblock arc):
`Create water wheels / windmills (rotation, vanilla mats)` → `Crafts & Additions Alternator → FE` to boot
the first Mek/IE/AE2 machines → mid/late **Mekanism** (gas/fission) and **IE** (diesel/multiblocks) become
the heavy FE generators → **AE2** consumes FE for storage/automation.

---

## 2. Per-mod integration

### Tech (FE grid)
- **Mekanism** — ❌ **DROPPED (2026-07-01): the pack standardizes on Immersive Engineering instead** (Mekanism reads
  as too blocky/boring — aesthetic call, see §7). Detail kept for reference only: gateway **Osmium** (+ Tin, Lead;
  late: Uranium, Fluorite); *Plan (not pursued):* an Industrial ore island (Osmium/Tin/Lead) with a deeper huge/late
  island for Uranium/Fluorite.
- **Immersive Engineering** — ✅ **the chosen tech backbone (2026-07-01).** Gateway **Bauxite/Aluminum** (+ uses
  Copper [vanilla], Lead, Silver, Nickel). *Plan:* IE ore island (bauxite/silver/nickel) + FE integration. ⚠️
  **Excavator** samples worldgen mineral veins that don't exist in void — the one un-rehomable mechanic. **Two
  approaches to try (§7):** (1) patch it to derive its ore mix from the **island it's built on**, or (2) disable it
  entirely and supply aluminum via the ore island. Everything else works.
- **Applied Energistics 2** — gateway **Certus Quartz + Sky Stone** (normally from meteorites). AE2
  self-multiplies via budding certus + crystal growth, so we only **bootstrap**: a small "meteorite island"
  seed (sky stone + certus + a budding certus block) or a recipe; AE2 sustains itself after. Disable meteorite
  worldgen.

### Tools
- **Silent Gear** — *the Tinkers' Construct replacement* (no real TiC on 1.21.1). Playable on **vanilla
  materials immediately → zero integration to start**. Custom ores (Crimson Iron [Nether], Bort, Azure Silver)
  become optional nether/deep-island progression. Dep: **Silent Lib**.

### Renewable resource engines (de-grind the tech tiers)
- **Mystical Agriculture** — grow resources/ores **as crops**; the single most on-theme mod for "grow your
  own world." Inferium from mob drops/crafting → tiered essences → resource & **ore seeds** that renewably
  feed Mekanism/IE/AE2. *Plan:* provide starter prosperity/inferium via a seed or recipe; farming does the rest.
  Dep: **Cucumber Library**.
- **Productive Bees** — renewable ingots/resources via apiaries on your islands. *Plan:* grant a few starter
  bees/hives via island or loot; breeding scales it. Pairs with Mystical Agriculture as the second renewable
  pillar.

### Farming / cozy
- **Farmer's Delight** — gateway: starter crops (cabbage/tomato/onion/rice) normally from wild-crop worldgen +
  trades. *Plan:* add FD wild crops as features on biome islands (cabbage/tomato/onion → forest/meadow/beach;
  **rice → aquatic/lush island with water**), or grant starter seeds via a seed/loot. Renewable once seeded;
  perfectly on-theme.

### Magic
- **Iron's Spells 'n Spellbooks** — spell scrolls/gear are largely **loot-gated** (catacombs/structures) and
  some mobs spawn in worldgen biomes. *Plan:* inject spell scrolls/loot into existing **structure-island loot
  tables** (dungeon/mansion/trial/witch_hut), and spawn magic mobs on a themed island. Most progression work
  of the bunch, but fully doable. Dep: **GeckoLib**.

### Mobs / flavor
- **Critters and Companions** — biome-spawn passive animals. *Plan:* likely **spawn as-is** on grown biome
  islands (forest/lush/meadow/aquatic) if their spawn biomes match your island biome tags — verify on a test
  island; otherwise add spawn entries or grant spawn eggs via seeds. Pure flavor. Dep: **GeckoLib**.

### Building / palette
- **Quark** — building blocks, decoration, QoL tweaks (modular). *Plan:* mostly additive; **curate modules**
  (enable building/decoration/QoL, disable worldgen-dependent ones that won't fire in void). Dep: **Zeta**.
- **Oh The Biomes You'll Go (BYG)** — huge Overworld/Nether/End palette (woods, plants, stones, mobs). *Plan:*
  use as the **block/flora palette for new island tiers**; mobs spawn on matching biome islands. Replaces
  BetterNether/BetterEnd. Worldgen irrelevant (void) — its stray void-floor features are handled by the
  custom void ChunkGenerator (see §5 + mod `plannednotes.md`), so no per-mod config needed.

---

## 3. Create addon pack (1.21.1-confirmed)

Curated for a tech-skyblock; all verified present for NeoForge 1.21.1 / Create 6.x — ✅ **all shipped in `mods.txt`**:

| Addon | Why |
|---|---|
| ✅ **Create: Crafts & Additions** | **Required** — the FE ↔ rotation power bridge (see §1) |
| ✅ **Create: Steam 'n' Rails** | Trains/rail depth — thematically perfect for linking floating islands |
| ✅ **Create: Enchantment Industry** | Automate XP/enchanting — strong mid/late progression |
| ✅ **Create: Bells & Whistles** | Decoration/adornments for nicer builds |
| ✅ **Create: Connected** | QoL blocks that "should exist" in Create |
| ✅ **Rechiseled: Create** | Decorative block variants (build variety) |
| ✅ **Create: Better Motors** *(optional)* | Enhances Crafts & Additions motors |

*Verify before adding (1.21.1 status less certain):* Create: Deco, The Factory Must Grow, Extended Cogwheels.
Avoid piling on overlapping addons — keep it focused.

---

## 4. Dependencies checklist (don't forget the libs)

- Silent Gear → **Silent Lib**
- Mystical Agriculture → **Cucumber Library**
- Iron's Spells, Critters and Companions → **GeckoLib**
- Quark → **Zeta**
- Create addons → **Create** (6.x for 1.21.1)
- FTB Quests → **Architectury API** + **FTB Library** + **FTB Teams**
- (Mekanism / IE / AE2 / Farmer's Delight / Productive Bees / BYG / Flux Networks — standalone or self-bundled; confirm on download)

---

## 5. Build priority & order

### Priority — ROI (best value-per-effort first)

> Effort isn't a gate (we'll build it all), but doing high-ROI mods first means a fun, playable pack early. The
> **void ChunkGenerator** (mod `plannednotes.md`) is a prerequisite — it unblocks BYG. **Mystical Agriculture** is a
> force-multiplier: renewable ore crops lower the integration effort of every tech mod, so it goes early.

| Order | Mod | Value | Effort | Note |
|---|---|---|---|---|
| 0 | ✅ *Void ChunkGenerator* | — | Med | **DONE** (0.165.0). Prerequisite: unblocks BYG + all biome mods; structures-off. |
| 1 | ✅ Silent Gear | High | ~Zero | **DONE.** Vanilla-mat playable; fills the Tinkers gap. |
| 2 | ✅ Create (+ Crafts & Additions + Flux) | Very High | Low | **DONE.** The automation spine; only zinc needs a seed. |
| 3 | ✅ Mystical Agriculture | Very High | Med | **DONE.** Force-multiplier — renewable ores feed the tech tiers. |
| 4 | BYG | High | Low* | Partial — forest woods shipped; wet/fantasy pending (BWGPLAN). *after the void fix. |
| 5 | Quark | Med-High | Low | Building/QoL breadth via config curation. |
| 6 | Farmer's Delight | High | Med | Cozy, on-theme; crop injection onto biome islands. |
| 7 | Applied Energistics 2 | High | Med | Storage endgame; self-multiplies after a certus bootstrap. |
| 8 | ~~Mekanism~~ | — | — | ❌ **DROPPED (2026-07-01)** — IE is the tech backbone instead (aesthetic; see §7). |
| 9 | Critters & Companions | Low-Med | Low | Flavor; likely spawns on biome islands. |
| 10 | Productive Bees | Med | Med | Renewable, but overlaps Mystical Agriculture. |
| 11 | ✅ **Immersive Engineering** | **High** | High | **The chosen tech backbone (2026-07-01).** Excavator needs a fix — patch to island-based ore mix, or disable (§7). |
| 12 | Iron's Spells | Med | High | ⚠️ Loot/mob-gated → heavy injection; partial without it. |

- **Most value / least effort:** rows 1–6 — gets you a fun, automatable, good-looking pack fast.
- **Least value / most effort:** Iron's Spells — defer or scope down. *(Immersive Engineering was ranked here, but
  taste re-ranked it: it's now the chosen tech backbone over Mekanism — see §7. The "IE's aesthetic is a personal
  headline" case came true.)*
- **FTB Quests:** author *last*, over the final set (its effort scales with the mod count).
- Settles several §7 decisions: Myst Ag *first*; tech backbone = **Immersive Engineering** (Mekanism dropped — aesthetic); IE **Excavator** → patch-to-island-aware *or* disable (TODO, §7).

### Integration workflow (phases)

1. ✅ **Worldgen compat — resolved Skyseed-side (one fix for all biome/structure mods).** **DONE** — the void ChunkGenerator shipped (0.165.0). Tested with BWG:
   TerraBlender biome mods (BWG/BYG/Terralith/Incendium) inject biomes into Skyseed's overworld `multi_noise`
   source; their *features* decorate at the void floor (~y=-64). Base terrain + Nether/End stay void. The fix is a
   **custom void ChunkGenerator** in the mod that no-ops biome decoration + structures in the void dims (tracked in
   the mod's `plannednotes.md`). **Once shipped, any TerraBlender/structure mod is safe to add** — biomes flow into
   island theming, nothing decorates, and no structures generate regardless of the world-creation toggle. This
   supersedes the old per-mod "is its worldgen disablable?" check.
2. ✅ **Add mods + deps**, then `./gen-mods-txt.ps1` to refresh the manifest. **DONE** — `mods.txt` current at 92 mods.
3. ✅ **Power backbone** — Create + Crafts & Additions + Flux Networks **shipped**. *(Still open: prove FE flows Create→Mek/IE/AE2 across islands — moot until a consumer mod lands.)*
4. **Tech bootstraps** — **IE bauxite/aluminum island** (the tech backbone), AE2 certus/sky-stone bootstrap. *(Mekanism osmium island dropped — see §7.)*
5. **Renewable engines** — Mystical Agriculture + Productive Bees starters.
6. **Content & palette** — Silent Gear (works now), Farmer's Delight crops on biome islands, Critters spawns,
   Iron's Spells loot injection, Quark module curation, BYG palette for new tiers.
7. **Per-mod island tiers** — promote each mod's "gateway island" into a gated progression step.

---

## 6. Quests — FTB Quests (the progression spine)

**FTB Quests** (NeoForge `2101.x` for 1.21.1) — the modpack-author standard (206M+ downloads), with an
**in-game editor** (fastest path to "quests for everything") and quests saved as **SNBT** you can commit.
Deps: **Architectury API + FTB Library + FTB Teams**.

- **Why it fits Skyseed:** the seed→island loop *is* a quest line. Quests use Skyseed's own island-seed items
  as tasks/rewards, hook advancements, and surface the §2 content-mod tiers as one guided path.
- **Authoring workflow (version-controlled):** build quests in a dev world → FTB Quests writes them to
  `config/ftbquests/quests/` (SNBT) → copy that into `overrides/config/ftbquests/` so they ship with the pack.
  Configs are committed (not gitignored), so the quest line lives in the repo. Player *progress* is per-world/
  team and stays out of the pack.
- **Suggested chapter spine:** Getting Started (first seeds) → Overworld islands → Nether chapter → End chapter →
  Tools (Silent Gear) → Power & Automation (Create → FE bridge → Mek/IE) → Storage (AE2) → Renewables (Mystical
  Agriculture, Productive Bees) → Farming (Farmer's Delight) → Magic (Iron's Spells) → Endgame. One chapter per
  tier mirrors "each content mod = an island tier."
- **Author last:** build the quest spine after the content + island tiers exist, so tasks point at real items/seeds.

> Alternatives if you'd rather author in files: **Heracles** (datapack + web editor) or lighter JSON options
> (Questlog, Boundless Quests). FTB Quests' in-game editor + ecosystem is the productivity win for a big quest line.

## 7. Open design decisions

- **One tech backbone or two? ✅ DECIDED (2026-07-01) — Immersive Engineering, single backbone; Mekanism dropped.**
  Mekanism and IE overlap (power + ore-doubling); rather than ship both, the pack standardizes on **IE**. The
  reason is **aesthetic** — Mekanism's machines read as too blocky/boring, whereas IE's multiblock/diesel look is
  the headline the player wants. (This is exactly the "taste re-ranks these" case flagged in §5.) Consequence: the
  Mekanism osmium-island integration is **not** pursued; the tech-tier bootstrap is the **IE bauxite/aluminum island**.
- **IE Excavator ✅ SCOPED (2026-07-01) — must be handled now that IE is the backbone.** The Excavator samples
  **worldgen mineral veins that don't exist in the void**, so it's dead as shipped. Two approaches to try, in order
  of preference (**TODO — not yet implemented**):
  1. **Patch it to be island-aware** — make the Excavator's ore mix a function of the **island it's built on**
     (its Skyseed theme/biome) instead of the vanilla mineral-vein sample. Preferred: keeps the signature IE
     mechanic alive and on-theme. Would need a mixin/compat against IE's `ExcavatorHandler` / mineral-vein lookup.
  2. **Disable the Excavator entirely** for this pack — remove its recipe (and hide it in JEI) so players never
     expect it to work; supply aluminum/bauxite via the IE ore island instead. Simple fallback if (1) is too invasive.
- **Iron's Spells scope** — how deep to wire the loot/mob injection (full discovery loop vs. crafted-only).
- **Mystical Agriculture vs. bespoke ore islands** — renewable farming can *replace* most ore islands; decide
  whether ore islands are the *first* acquisition (then Myst Ag scales) or skip some islands entirely.
