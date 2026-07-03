# CONTENTPLAN — Skyseed: Grow your own world

Content-mod integration plan (NeoForge **1.21.1**). Companion to BEAUTIFYPLAN.md.

> **Status — the foundation AND the content-mod wave are shipped (see CHANGELOG):** the void ChunkGenerator, the
> **Create + Crafts & Additions + Flux Networks** power backbone, **Silent Gear**, the curated Create addons,
> **Mystical Agriculture** (MYSTICALPLAN.md), the **full BWG integration** (woods, flowers, all six village styles),
> the nine-chapter FTB quest spine — and now the whole wave: **Immersive Engineering + Petroleum + flight**
> (IEPLAN.md), **Applied Energistics 2 + the meteorite island** (AE2PLAN.md), **Farmer's Delight** (FARMERSDELIGHTPLAN.md),
> and **Quark** (QUARKPLAN.md). The tech backbone is **Immersive Engineering** (Mekanism dropped, §7). What remains is
> mostly in-game sign-offs plus a tail of flavor mods; priorities live in [`../PLANOFPLANS.md`](../PLANOFPLANS.md).

**What's left** (backlog #s = PLANOFPLANS):

- **✅ Shipped, in-game verify only:** **#34** Immersive Engineering (ore island + crude-oil pocket + **#35** Excavator
  config), **#18** Applied Energistics 2 (certus/sky-stone bootstrap + meteorite island Phases 1–3 + Meteorite-Core
  presses + quest), **#15** Quark (jars + curation + Totem void fix + island tie-ins **#71**), **#16** Farmer's Delight
  (wild crops + rice + dimension delights + quest), and **#39** the FE-flow (proven on paper). Each has its own plan.
- **The unbuilt flavor tail:** **#32** Productive Bees (starter bees/hives), **#31** Critters and Companions (spawn
  verification), **#36** Iron's Spells 'n Spellbooks (loot/mob injection) + **#37** its scope decision (§7).
- **#52** Verify the two uncertain Create addons — The Factory Must Grow, Extended Cogwheels — or drop them (§3).
- **#38** Per-future-mod call: bespoke ore island vs. lean on MA seeds (§7 — settled in practice for shipped mods).
- **#19 / #20** Rolling riders on every *future* integration: its quest chapter (§6) and its gated island tier (§5 phase 7).

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

## 1. Power & automation backbone — ✅ shipped (see CHANGELOG); #39 proof pending

The FE/RF grid is shipped: Create bridged to **Forge Energy** via **Crafts & Additions** (Alternator/Electric
Motor) + **Flux Networks** (wireless FE — a Flux Point per island shares one global network; no cables across the
void). IE (native FE) and AE2 (accepts FE) join directly when they land. Progression: Create rotation → Alternator
→ FE boots the first IE/AE2 machines → IE becomes the heavy generator → AE2 consumes for storage/automation.

- **Open (#39):** prove FE actually flows Create → IE/AE2 across islands — a one-time in-game check, only testable
  once the first FE **consumer** (IE #34 or AE2 #18) lands. Keep **Flux Networks** the universal backbone + one
  cable family for local runs (mixing cable *types* is fiddly).

---

## 2. Per-mod integration (the open wave)

### Tech (FE grid)
- **Immersive Engineering (#34)** — ✅ **the chosen tech backbone (2026-07-01).** Jars + configs **landed**
  (IE 12.4.2-194 + the flight cluster + KubeJS); ore island + quest (A009) + Excavator config all **shipped**, in-game
  verify left. **Own plan:
  [IEPLAN.md](IEPLAN.md).** Gateway **Bauxite/Aluminum** (+ uses Copper [vanilla], Lead, Silver, Nickel). ⚠️
  **Excavator** (#35) — reframed in IEPLAN: IE's veins are per-chunk *data*, not worldgen ore, so it likely works
  in the void and closes with config, not a mixin (§7).
- **Applied Energistics 2 (#18)** — ✅ **curated jar set landed** (`overrides/mods/`): AE2 19.2.17 + GuideME + the
  Wireless Terminals / AE2 Things / MEGA Cells addons + the Create bridges (Create: AE Generator, Create Stock
  Bridge) + Rechiseled: AE2. Gateway **Certus Quartz + Sky Stone** (normally from meteorites, inert in the void).
  AE2 self-multiplies via budding certus + crystal growth, so the island only **bootstraps** it — but the four
  **inscriber presses** are meteorite-loot-only and *not craftable*, so they're a hard progression blocker that
  needs a bespoke source. Because AE2 trivializes inventory management it's **gated as a rare, expensive endgame
  reward**: the endgame unlocks (**sky stone + presses**, both verified effectively meteorite-only) live on a
  **dedicated `skyseed:meteorite` theme with its own IE-gated seed**, while a small **finite certus deposit**
  (`ae2:quartz_block`) on rocky/ancient is the *gate ingredient* the seed recipe consumes (certus itself isn't
  storage-gating). Expansion afterwards is easier but stays expensive. **Full curation, versions, the press decision,
  the rarity design, and the bootstrap plan in its own plan: [AE2PLAN.md](AE2PLAN.md).**

### Renewable resource engines
- **Productive Bees (#32)** — renewable ingots/resources via apiaries. *Plan:* grant a few starter bees/hives
  via island or loot; breeding scales it. Pairs with the shipped Mystical Agriculture as the second renewable
  pillar (which lowers its marginal value — deferrable).

### Farming / cozy
- **Farmer's Delight (#16)** — ✅ **the curated jar set is IN** (`overrides/mods/`): base FD 1.3.2 + the three
  dimension delights (End's / My Nether's / Ocean's) + zero-worldgen QoL/compat (Autochef's, Chef's, Chopper's, FD
  Extended). ~50 ecosystem addons deliberately skipped. Gateway: starter crops (cabbage/tomato/onion/rice) normally
  from wild-crop worldgen + trades. *Plan:* inject FD wild crops as `ground`/pond features on biome islands
  (cabbage/tomato/onion → forest/meadow/desert; **rice → aquatic/lush pond**), chorus_succulent → Chorus Forest,
  bullet_pepper/powdery_cane → Nether seeds. Renewable once seeded (drops replantable seeds); perfectly on-theme.
  ✅ **SHIPPED** (20 override files + the #42 quest A008; in-game verify left). **Full detail + verified ids in its own
  plan: [FARMERSDELIGHTPLAN.md](FARMERSDELIGHTPLAN.md).**

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
  blossom) are committed as **#71** → [QUARKISLANDPLAN.md](QUARKISLANDPLAN.md). The #15 smoke-pass signed off 2026-07-02;
  #71's Y-band fix shipped v0.192.0 (in-game re-verify pending).

*(Shipped for reference: the biome-palette mod is **Oh The Biomes We've Gone** (BWG) 2.6.0 — overworld-focused,
55 biomes / 25 woods, fully integrated (shipped + signed off, see CHANGELOG). Its stray
void-floor features are handled pack-wide by the void ChunkGenerator (shipped v0.165.0), which makes **any**
TerraBlender/structure mod safe to add: biomes flow into island theming, nothing decorates, no structures
generate. Mystical Agriculture shipped via **ore islands** — deepslate on Ancient, stone on Lush, soulium on
Nether-Soul; see MYSTICALPLAN.md.)*

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

- ✅ **Shipped (see CHANGELOG / QUESTPLAN.md):** six chapters (Introduction, Skyseed, Create, Tools & Travel,
  Storage, Mystical Agriculture — 54 quests) plus the BWG branch (B701–B703), under `overrides/config/ftbquests/quests/`.
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
- **IE Excavator (#35) — ✅ RESOLVED via config** (IEPLAN reframe). The Excavator's mineral veins are a **per-chunk
  data layer**, not physical ore, so they exist over empty sky — the config is pinned in
  `overrides/defaultconfigs/immersiveengineering-server.toml` (`chance` 0.9→0.7). The island-aware mixin (former
  option 1) and the disable-it fallback (former option 2) are both unneeded. **Left: in-game confirm it yields veins.**
- **Iron's Spells scope (#37)** — how deep to wire the loot/mob injection (full discovery loop vs. crafted-only).
  Settle when #36 is picked up.
- **Mystical Agriculture vs. bespoke ore islands (#38)** — settled **in practice** for shipped mods (MA got its
  own ore islands: deepslate on Ancient, stone on Lush, soulium on Nether-Soul — bespoke islands ship
  *alongside* MA). Remaining: the per-mod call for each future integration (does IE/AE2 get a bespoke ore
  island or lean on MA seeds).
