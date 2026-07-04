# QUESTPLAN — Skyseed: Grow your own world (FTB Quests)

A **cosy, handholdy** quest line that explains every mod and how to use it. Authored *alongside* mod integration (one
chapter per mod as it lands), not last — so no end-of-project overflow. Engine: **FTB Quests** (installed:
`ftb-quests` / `ftb-library` / `ftb-teams` / `architectury`).

> **Status:** everything authorable is authored — **nine chapters** (**Introduction · Skyseed · Tools & Travel ·
> Create · Power · Immersive Engineering · Storage · Mystical Agriculture · Farmer's Delight**, ~106 quests) plus the
> **BWG branch** (B701–B703 under Tools & Travel) are shipped and committed under `overrides/config/ftbquests/quests/`;
> the in-game quest-book test-load (#3) signed off 2026-07-02 (the newer FD/IE/AE2 additions still want an in-game load).
> **Open:**
> 1. The remaining **future per-mod chapters** below — **#43** Quark, **#44** Productive Bees, **#46** Iron's Spells,
>    **#47** scope decision — each gated on its mod (CONTENTPLAN §2). *(#41 AE2, #42 FD, #45 IE have shipped.)*
> 2. **(#19, rolling)** the standing rule: every new integration is followed by its chapter.

## Approach (the authoring conventions — applies to every future chapter)

- **Tone — explain, don't gate.** Every quest carries a short plain-language description: what the mod/mechanic is, why
  you'd use it, the one key trick. The quests teach the *why*; lean on the mods' own in-game docs for the deep *how* —
  **Create's Ponder** (animated guides, the `W` key on a Create item) and the **Skyseed guide book** (Modonomicon /
  Patchouli). Link to them from the quest text.
- **Task style.** Mostly "obtain item X" or "complete advancement Y." Skyseed already defines `reveal_*` / `craft_*` /
  `gathered_*` advancements — FTB Quests can hook them directly, so the Skyseed chapter rides the mod's own progression.
  Use OR-tasks + optional quests so the line guides without straitjacketing.
  - **★ The smart-filter trap (standing warning for tag-based tasks):** an `ftbfiltersystem:smart_filter` item
    task (e.g. `item_tag(...)`) does **NOT** work — in-game FTB Quests treats the filter as a *literal item to
    obtain*, not a tag expansion. Use an **advancement task** on a hidden advancement instead (that's how B701
    works, via `skyseed:reveal_exotic_woods`).
- **Reward style.** Rewards bootstrap the *next* step (a few of the next ingredient, XP, a choice reward). Cosy = the
  player is never left wondering "what now?".
- **Authoring + version control.** Build chapters in-game (FTB Quests **edit mode** — far faster than hand-writing),
  which saves SNBT to `config/ftbquests/quests/`; copy that into `overrides/config/ftbquests/` so it ships with the pack
  and is committed (configs are tracked — see CONTENTPLAN §6). Author one chapter, test it, commit; repeat.

## Shipped chapters (for orientation)

| Chapter (snbt) | Quests | Covers |
|---|---|---|
| Introduction (`introduction`) | 3 | the void-skyblock pitch; QoL tour (JEI/Jade/Xaero/Vein Mining, the guide) |
| Skyseed (`skyseed`) | 16 | the spine — seed→island→relics→Nether→End; gates the rest |
| Tools & Travel (`tools`) | 12 | Silent Gear · Waystones/Xaero travel · the **BWG branch** (B701–B703) · the **flight line** (B916–B919 + the optional Aviation Fuel B920, moved here from IE) |
| Create (`create`) | 16 | the tech chain incl. the Extras (rails, enchantment industry, deco, …) |
| **Power (`power`)** | 8 | ⚡ the **FE backbone as one story** — generation (Alternator, IE Diesel Gen) → movement (C&A wires, **Flux** wireless across islands) → storage (capacitor, Flux bank) → consumption (**one grid feeds IE + AE2**, the #39 payoff). A deliberate *system* hub chapter (like Storage), gated off `B203` water wheel; keeps the per-mod power quests in place. |
| **Immersive Engineering (`immersiveengineering`)** | 15 | A009, B9xx — the tech backbone: Aluminium → Hammer/Manual → Windmill/LV → Coke Oven→Blast Furnace→Steel→Crusher/Excavator → Fluid Pump→Crude Oil→Distillation→Diesel Gen (#45) |
| Storage (`storage`) | 19 | Sophisticated Backpacks + Storage (B401–B403) **+ the AE2 endgame line** (B404–B419, #41) |
| Mystical Agriculture (`mysticalagriculture`) | 9 | the MA loop (B601–B609) |
| Farmer's Delight (`farmersdelight`) | 8 | the cosy cooking loop (B801–B808) — crops → pot/skillet/board → meal → rice → Nether/End delights |
| **Magic \& Exploration (`ironsspells`)** | 11 | A00A, BB01–BB0B — the Iron's Spells pillar: Explore-seed tiers → scroll/Arcane Essence loot → Copper Spellbook / Scroll Forge / Arcane Anvil → the rare **Upgrade Orb**; Relic + Artifact checkmark side-finds. Gated off the Skyseed structure-seed quest (B108). |

**BWG branch record:** *Into the Wilds* (B701 — advancement task on the hidden
`skyseed:reveal_exotic_woods`, covering all 24 growable exotic planks via `#skyseed:exotic_woods`; dep B103),
*Mill the Blooms* (B702 — checkmark; deps B701 + B204 Millstone), *Grow Something Grand* (B703 — checkmark; dep
B701; reward: Skyfarer's Cache).

## Chapter groups (sidebar organization — shipped)

Rather than merge mod chapters (which makes unwieldy 30-quest canvases and blurs progression tiers), the chapters are
sorted into **three collapsible sidebar groups** via `chapter_groups.snbt` (each chapter's `group:` field points at a
group id; within-group order is the chapter `order_index`). No quests move — every mod keeps its own focused chapter.

| Group (`chapter_groups.snbt` id) | Chapters (in order) |
|---|---|
| **Progression** (`…E001`) | Introduction · Skyseed · Tools & Travel |
| **Industry** (`…E002`) | Create · Power · Immersive Engineering · Storage (AE2) |
| **Farming** (`…E003`) | Mystical Agriculture · Farmer's Delight |
| **Magic \& Exploration** (`…E004`) | Iron's Spells (Explore seeds → magic loot → relics/artifacts) |

*Decision (declined the merge):* keep Create/IE and MA/FD as separate chapters; group them in the sidebar instead —
same thematic "tech together / farming together" feel, none of the size/churn/progression-clarity costs of merging.

## Cross-chapter flow & gating (design rules for every future chapter)

- **Skyseed is the spine.** Tech needs materials, materials come from grown islands — so mod chapters unlock off
  Skyseed milestones, not at world start.
- **The one hard tie-in so far:** Create's **brass** ⇐ **zinc** ⇐ **grow a Rocky island** — the Create "Brass &
  Beyond" quest depends on the Skyseed "grow a rocky island" quest.
- Keep dependencies *soft* elsewhere (chapters openable in parallel) so the pack stays cosy, not on-rails.

## Future chapters (each gated on its mod landing — CONTENTPLAN §2)

| # | Chapter | Gate | Note |
|---|---|---|---|
| ~~#45~~ | ~~**Immersive Engineering**~~ | — | ✅ **SHIPPED** — `chapters/immersiveengineering.snbt` (A009, 15 quests B9xx); the flight line moved to Tools & Travel. In-game book-load pending. |
| ~~#41~~ | ~~**Applied Energistics 2**~~ | — | ✅ **SHIPPED** — folded into the **Storage** chapter (`A005`) as 16 quests `B404`–`B419` (AE2 = the storage endgame), not a standalone chapter. Gated off `B104` (rocky→certus) + IE steel `B908`. Weaves the #39 FE-power step (Energy Acceptor) + Create bridges. In-game book-load pending. |
| ~~#42~~ | ~~**Farmer's Delight**~~ | — | ✅ **SHIPPED** — `chapters/farmersdelight.snbt` (A008, B801–B808), gated off the Skyseed spine at B103; Nether/End branches gated on B110/B113. In-game test-load pending. |
| #44 | **Productive Bees** | PB integration (#32) | overlaps shipped MA — low marginal value |
| ~~#46~~ | ~~**Iron's Spells**~~ | — | ✅ **SHIPPED** — `chapters/ironsspells.snbt` (A00A, 11 quests BB01–BB0B) in a new **Magic \& Exploration** sidebar group (E004). Gated off the Skyseed structure-seed quest B108. Explore-seed tiers → scroll/essence loot → spellbook/scroll-forge/arcane-anvil → the rare Upgrade Orb; Relic + Artifact as checkmark side-finds. In-game book-load pending. |
| #43 | **Quark** | Quark smoke pass (#15) | Quark shipped v0.182.0; minimal coverage may suffice — 3-quest sketch in [QUARKPLAN.md](QUARKPLAN.md) |
| #47 | **"BYG content"** | — | ⚠ **scope unclear**: the installed biome mod is **BWG** and its branch (B701–B703) already shipped. Either this meant the separate predecessor mod BYG (not installed, not planned) or *deeper* BWG coverage beyond the 3-quest branch — decide before it's actionable. |

*(Mekanism's future chapter — old #40 — is **dropped**: Mekanism was cut from the pack on 2026-07-01, CONTENTPLAN §7.)*

## Mod coverage (triage refreshed 2026-07-01)

- **Quested:** skyseed · create + createaddition + create_better_motors · FluxNetworks · railways ·
  create-enchantment-industry · creategoggles · create_jetpack · the Create deco set · silent-gear ·
  sophisticated (backpacks/storage/core + integrations) · waystones (+ xaero compat) · veinmining · Xaero
  mini/world map · JEI · Jade · AppleSkin · **MysticalAgriculture + Botany Pots family** (MA chapter) ·
  **BWG + OTYG + create-otbwg-compat** (the BWG branch) · **Farmer's Delight family** (base FD + End's / My Nether's /
  Ocean's / Autochef's / Chef's / Chopper's / FD Extended — the FD chapter A008).
- **Invisible (no quests — libs / perf / aesthetic / backends):** embeddium, iris, monocle, EuphoriaPatcher,
  modernfix, ferritecore, entityculling, fpsreducer, ambientsounds, fallingleaves, fastleafdecay, sound-physics,
  fusion, EMF/ETF, balm, kotlinforforge, geckolib, corgilib, better_lib, framework, configured, catalogue,
  searchables, creativecore, supermartijn642 ×2, TerraBlender, Clumps, Controlling, MouseTweaks,
  AdvancedLootInfo, JustEnoughResources, silentgear jade/jei plugins, Cucumber, Patchouli, ftb-filter-system.
- **Installed, chapter pending:** Quark (#43 — shipped v0.182.0; 3-quest sketch in QUARKPLAN.md, gated on the #15 smoke pass).
- **Not installed (so no chapter yet):** Productive Bees, Iron's Spells. *(Mekanism: dropped. Farmer's Delight:
  chapter A008. Immersive Engineering: chapter A009. **Applied Energistics 2: installed + the Storage-chapter AE2 line
  B404–B419 shipped (#41).**)*
