# CONTENTPLAN — Skyseed: Grow your own world

Content-mod integration plan (NeoForge **1.21.1**). Companion to BEAUTIFYPLAN.md.

> **Status (2026-07-01, mod 0.181.0) — the foundation is shipped:** the void ChunkGenerator (v0.165.0), the
> **Create + Crafts & Additions + Flux Networks** power backbone, **Silent Gear**, all seven curated Create
> addons, **Mystical Agriculture** (ore islands on Ancient/Lush/Nether-Soul + its quest chapter — see
> MYSTICALPLAN.md), the **full BWG integration** (woods, flowers, and all six village styles — see BWGPLAN.md /
> BWGVILLAGEPLAN.md), and the six-chapter FTB quest spine. The tech backbone is **decided: Immersive
> Engineering** (Mekanism dropped, see §7). What remains is the next content-mod wave below; priorities live in
> [`../PLANOFPLANS.md`](../PLANOFPLANS.md).

**What's left** (backlog #s = PLANOFPLANS):

- **#34** Immersive Engineering — bauxite/aluminum ore island + FE integration (§2), gated on **#35** the Excavator fix (§7).
- **#18** Applied Energistics 2 — certus + sky-stone bootstrap (§2).
- **#15** Quark — **SHIPPED** (4 jars in + curated + Totem void fix v0.182.0; smoke-pass sign-off open) (§2). **Own plan:
  [QUARKPLAN.md](QUARKPLAN.md)**; island integration is **#71** → [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md).
- **#16** Farmer's Delight — wild crops on biome islands (§2).
- **#32** Productive Bees — starter bees/hives (§2).
- **#31** Critters and Companions — spawn verification on biome islands (§2).
- **#36** Iron's Spells 'n Spellbooks — loot/mob injection (§2) + **#37** its scope decision (§7).
- **#52** Verify the two uncertain Create addons — The Factory Must Grow, Extended Cogwheels — or drop them (§3).
- **#38** Per-future-mod call: bespoke ore island vs. lean on MA seeds (§7 — settled in practice for shipped mods).
- **#39** Prove FE flows Create → IE/AE2 across islands (§1 — moot until a consumer mod lands).
- **#19 / #20** Rolling riders on every integration: its quest chapter (§6) and its gated island tier (§5 phase 7).

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

---

## 1. Power & automation backbone (the interop plan) — ✅ shipped; #39 proof pending

Standardize on **Forge Energy (FE/RF)** as the shared currency. Create is the one outlier and is bridged.

| Mod | Native power | Talks FE? | How it joins the grid |
|---|---|---|---|
| **Immersive Engineering** | Immersive Flux (= FE) | ✅ native | directly |
| **Applied Energistics 2** | AE (internal) | ✅ accepts FE input | directly |
| **Create** | Rotational **Stress Units** | ❌ | **Create: Crafts & Additions** — Alternator (rotation→FE @75%, needs ≥32 RPM) + Electric Motor (FE→rotation) |

*(Mekanism's row is gone — dropped 2026-07-01, see §7.)*

- ✅ **Shipped:** Create + **Crafts & Additions** (`createaddition` 1.6.0) + **Flux Networks** (wireless FE — a
  Flux Point per island shares one global FE network; no cables across the void).
- **Open (#39):** prove FE actually flows Create → IE/AE2 across islands — a one-time in-game check, only
  testable once the first FE **consumer** (IE #34 or AE2 #18) lands.
- **Cabling guidance for then:** all FE mods interconnect, but mixing cable *types* is fiddly — keep **Flux
  Networks** as the universal backbone and one cable family for local runs.

**Natural power progression:** Create water wheels / windmills (rotation, vanilla mats) → Alternator → FE to
boot the first IE/AE2 machines → mid/late **IE** (diesel/multiblocks) becomes the heavy FE generator → **AE2**
consumes FE for storage/automation.

---

## 2. Per-mod integration (the open wave)

### Tech (FE grid)
- **Immersive Engineering (#34)** — ✅ **the chosen tech backbone (2026-07-01).** Gateway **Bauxite/Aluminum**
  (+ uses Copper [vanilla], Lead, Silver, Nickel). *Plan:* IE ore island (bauxite/silver/nickel) as a
  `theme_override` gateway + FE integration, then its quest chapter. ⚠️ **Excavator** samples worldgen mineral
  veins that don't exist in the void — the one un-rehomable mechanic; **gated on the #35 fix (§7)**.
- **Applied Energistics 2 (#18)** — gateway **Certus Quartz + Sky Stone** (normally from meteorites). AE2
  self-multiplies via budding certus + crystal growth, so we only **bootstrap**: a small "meteorite island"
  seed (sky stone + certus + a budding certus block) or a recipe; AE2 sustains itself after. Confirm meteorite
  worldgen is inert/disabled.

### Renewable resource engines
- **Productive Bees (#32)** — renewable ingots/resources via apiaries. *Plan:* grant a few starter bees/hives
  via island or loot; breeding scales it. Pairs with the shipped Mystical Agriculture as the second renewable
  pillar (which lowers its marginal value — deferrable).

### Farming / cozy
- **Farmer's Delight (#16)** — gateway: starter crops (cabbage/tomato/onion/rice) normally from wild-crop
  worldgen + trades. *Plan:* add FD wild crops as features on biome islands (cabbage/tomato/onion →
  forest/meadow/beach; **rice → aquatic/lush island with water** — those override families now exist to
  extend), or grant starter seeds via a seed/loot. Renewable once seeded; perfectly on-theme.

### Magic
- **Iron's Spells 'n Spellbooks (#36)** — spell scrolls/gear are largely **loot-gated** (catacombs/structures)
  and some mobs spawn in worldgen biomes. *Plan:* inject spell scrolls/loot into existing **structure-island
  loot tables** (dungeon/mansion/trial/witch_hut), and spawn magic mobs on a themed island. Most progression
  work of the bunch; scope decision first (**#37**, §7). Dep: **GeckoLib** (already in the pack).

### Mobs / flavor
- **Critters and Companions (#31)** — biome-spawn passive animals. *Plan:* likely **spawn as-is** on grown
  biome islands if their spawn biomes match the island biome tags — verify on a test island; otherwise add
  spawn entries or grant spawn eggs via seeds. Pure flavor. Dep: **GeckoLib** (already in the pack).

### Building / palette
- **Quark (#15)** — building blocks, decoration, QoL tweaks (modular). **SHIPPED:** 4 jars in (Quark 4.1-481 +
  Zeta 1.1-40 + Oddities marker + QuarkPonders 1.5.1), modules curated (overlaps + Glimmering Weald off; block-providing
  World modules kept ON for island reuse), and the **Totem of Holding void fix** is live (v0.182.0). **Own plan:
  [QUARKPLAN.md](QUARKPLAN.md)**; the four **island integrations** (corundum / Quark stones / Ancient-Tome loot /
  blossom) are committed as **#71** → [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md). Remaining on #15: the in-game smoke-pass.

*(Shipped for reference: the biome-palette mod is **Oh The Biomes We've Gone** (BWG) 2.6.0 — overworld-focused,
55 biomes / 25 woods, fully integrated per BWGPLAN.md + BWGVILLAGEPLAN.md. Its stray void-floor features are
handled pack-wide by the void ChunkGenerator (see the mod's `plannednotes.md` — shipped v0.165.0), which makes
**any** TerraBlender/structure mod safe to add: biomes flow into island theming, nothing decorates, no
structures generate. Mystical Agriculture shipped via **ore islands** — deepslate on Ancient, stone on Lush,
soulium on Nether-Soul — rather than a starter seed/recipe; see MYSTICALPLAN.md.)*

---

## 3. Create addon pack

The seven curated addons (Crafts & Additions, Steam 'n' Rails, Enchantment Industry, Bells & Whistles,
Connected, Rechiseled: Create, Better Motors) ✅ **all shipped in `mods.txt`** — along with several more added
since (Deco, Encased, Aquatic Ambitions, Jetpack, Goggles, Interiors, Design-n-Decor, …).

**Open (#52) — verify before adding (1.21.1 status less certain):** The Factory Must Grow, Extended Cogwheels —
verify each for NeoForge 1.21.1 / Create 6.x, or explicitly drop them. *(Create: Deco was on this list and has
since shipped.)*

---

## 4. Dependencies checklist (for the open wave)

- Quark (#15) → **Zeta** (1.1-40 for Quark 4.1-477…481 — see [QUARKPLAN.md](QUARKPLAN.md))
- Iron's Spells (#36), Critters and Companions (#31) → **GeckoLib** ✅ *already in the pack*
- IE / AE2 / Farmer's Delight / Productive Bees — standalone or self-bundled; confirm on download

---

## 5. Build priority & order

Rows 0–3 of the original ROI ranking (void ChunkGenerator, Silent Gear, Create+FE bridge, Mystical
Agriculture) and the BWG palette are ✅ shipped. Remaining, in ROI order:

| Order | Mod | Value | Effort | Note |
|---|---|---|---|---|
| 1 | Quark (#15) | Med-High | Low | Building/QoL breadth via config curation. **SHIPPED + curated → [QUARKPLAN.md](QUARKPLAN.md)**; island tie-ins #71 → [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md). |
| 2 | Farmer's Delight (#16) | High | Med | Cozy, on-theme; crop injection onto biome islands. |
| 3 | **Immersive Engineering (#34)** | **High** | High | **The chosen tech backbone.** Gated on the Excavator fix (#35, §7). |
| 4 | Applied Energistics 2 (#18) | High | Med | Storage endgame; self-multiplies after a certus bootstrap. |
| 5 | Critters & Companions (#31) | Low-Med | Low | Flavor; likely spawns on biome islands. |
| 6 | Productive Bees (#32) | Med | Med | Renewable, but overlaps Mystical Agriculture. |
| 7 | Iron's Spells (#36/#37) | Med | High | ⚠️ Loot/mob-gated → heavy injection; scope it first. |

### Integration workflow (phases)

Phases 1–3 (worldgen compat via the void ChunkGenerator, mods + `gen-mods-txt.ps1` manifest, power backbone)
are ✅ done. Remaining:

4. **Tech bootstraps** — **IE bauxite/aluminum island** (the tech backbone), AE2 certus/sky-stone bootstrap.
5. **Renewable engines** — Productive Bees starters (MA shipped).
6. **Content & palette** — Farmer's Delight crops on biome islands, Critters spawns, Iron's Spells loot
   injection, Quark module curation.
7. **Per-mod island tiers (#20)** — promote each mod's "gateway island" into a gated progression step. The
   pattern is proven for every installed mod (Create zinc on rocky/ancient, MA ores, the 15 `biomeswevegone_*`
   override files); apply it to each future integration as it lands.

---

## 6. Quests — FTB Quests (the progression spine)

**FTB Quests** (NeoForge `2101.x` for 1.21.1) — in-game editor, quests saved as **SNBT** and committed.
Deps: **Architectury API + FTB Library + FTB Teams** (all shipped).

- ✅ **Shipped:** six chapters (Introduction, Skyseed, Create, Tools & Travel, Storage, Mystical Agriculture —
  54 quests) plus the BWG branch (B701–B703), committed under `overrides/config/ftbquests/quests/`. See
  QUESTPLAN.md.
- **Authoring workflow (version-controlled):** build quests in a dev world → FTB Quests writes them to
  `config/ftbquests/quests/` (SNBT) → copy into `overrides/config/ftbquests/` so they ship with the pack.
  Configs are committed; player *progress* is per-world/team and stays out of the pack.
- **Rolling rule (#19):** each remaining integration (§2) is *followed by* its chapter — IE → Power &
  Automation extension, AE2 → Storage extension, FD → Farming, Iron's → Magic, plus an eventual Endgame
  chapter. Author each chapter right after its mod's island/tier integration lands, so tasks point at real
  items/seeds (the original "author last over the final set" is superseded by this incremental weave —
  that's how the six shipped chapters were built).

## 7. Open design decisions

- **One tech backbone or two? ✅ DECIDED (2026-07-01) — Immersive Engineering, single backbone; Mekanism
  dropped.** Mekanism and IE overlap (power + ore-doubling); rather than ship both, the pack standardizes on
  **IE**. The reason is **aesthetic** — Mekanism's machines read as too blocky/boring, whereas IE's
  multiblock/diesel look is the headline the player wants. Consequence: no Mekanism osmium island; the
  tech-tier bootstrap is the **IE bauxite/aluminum island** (#34).
- **IE Excavator (#35) — must be handled now that IE is the backbone.** The Excavator samples **worldgen
  mineral veins that don't exist in the void**, so it's dead as shipped. Two approaches to try, in order of
  preference (**TODO — not yet implemented**):
  1. **Patch it to be island-aware** — make the Excavator's ore mix a function of the **island it's built on**
     (its Skyseed theme/biome) instead of the vanilla mineral-vein sample. Preferred: keeps the signature IE
     mechanic alive and on-theme. Would need a mixin/compat against IE's `ExcavatorHandler` / mineral-vein lookup.
  2. **Disable the Excavator entirely** for this pack — remove its recipe (and hide it in JEI) so players never
     expect it to work; supply aluminum/bauxite via the IE ore island instead. Simple fallback if (1) is too invasive.
- **Iron's Spells scope (#37)** — how deep to wire the loot/mob injection (full discovery loop vs. crafted-only).
  Settle when #36 is picked up.
- **Mystical Agriculture vs. bespoke ore islands (#38)** — settled **in practice** for shipped mods (MA got its
  own ore islands: deepslate on Ancient, stone on Lush, soulium on Nether-Soul — bespoke islands ship
  *alongside* MA). Remaining: the per-mod call for each future integration (does IE/AE2 get a bespoke ore
  island or lean on MA seeds).
