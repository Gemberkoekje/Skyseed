# QUESTPLAN — Skyseed: Grow your own world (FTB Quests)

A **cosy, handholdy** quest line that explains every mod and how to use it. Authored *alongside* mod integration (one
chapter per mod as it lands), not last — so no end-of-project overflow. Engine: **FTB Quests** (installed:
`ftb-quests` / `ftb-library` / `ftb-teams` / `architectury`).

> **Status (2026-07-01):** everything authorable is authored — the six chapters (**Introduction · Skyseed ·
> Create · Mystical Agriculture · Tools & Travel · Storage**, 54 quests) plus the **BWG branch** (B701–B703
> under Tools & Travel) are shipped and committed under `overrides/config/ftbquests/quests/`. **Open:**
> 1. **(#3)** the in-game quest-book **test-load of the BWG branch** — chapter renders, B701's advancement task
>    (`skyseed:reveal_exotic_woods` / `has_exotic_wood`) resolves and auto-completes on an exotic plank, B703's
>    Skyfarer's Cache reward rolls.
> 2. The **future per-mod chapters** below (#41–#47) — each gated on its mod actually landing (CONTENTPLAN §2).
> 3. **(#19, rolling)** the standing rule: every new integration is followed by its chapter.

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
| Tools & Travel (`tools`) | 7 | Silent Gear · Waystones/Xaero travel · the **BWG branch** (B701–B703) |
| Create (`create`) | 16 | the tech chain incl. the Extras (rails, enchantment industry, deco, …) |
| Storage (`storage`) | 3 | Sophisticated Backpacks + Storage (**not** AE2) |
| Mystical Agriculture (`mysticalagriculture`) | 9 | the MA loop (B601–B609) |

**BWG branch record (BWGPLAN Step 5):** *Into the Wilds* (B701 — advancement task on the hidden
`skyseed:reveal_exotic_woods`, covering all 24 growable exotic planks via `#skyseed:exotic_woods`; dep B103),
*Mill the Blooms* (B702 — checkmark; deps B701 + B204 Millstone), *Grow Something Grand* (B703 — checkmark; dep
B701; reward: Skyfarer's Cache).

## Cross-chapter flow & gating (design rules for every future chapter)

- **Skyseed is the spine.** Tech needs materials, materials come from grown islands — so mod chapters unlock off
  Skyseed milestones, not at world start.
- **The one hard tie-in so far:** Create's **brass** ⇐ **zinc** ⇐ **grow a Rocky island** — the Create "Brass &
  Beyond" quest depends on the Skyseed "grow a rocky island" quest.
- Keep dependencies *soft* elsewhere (chapters openable in parallel) so the pack stays cosy, not on-rails.

## Future chapters (each gated on its mod landing — CONTENTPLAN §2)

| # | Chapter | Gate | Note |
|---|---|---|---|
| #45 | **Immersive Engineering** | IE integration (#34) | **promoted** — IE is now the tech backbone; same weave as Create (island/resource integration first, then the chapter) |
| #41 | **Applied Energistics 2** | AE2 integration (#18) | its own chapter (storage/automation endgame) |
| #42 | **Farmer's Delight** | FD integration (#16) | cosy, on-theme |
| #44 | **Productive Bees** | PB integration (#32) | overlaps shipped MA — low marginal value |
| #46 | **Iron's Spells** | Iron's integration (#36/#37) | loot/mob-gated, heaviest |
| #43 | **Quark** | Quark add (#15) | minimal coverage may suffice — 3-quest sketch in [QUARKPLAN.md](QUARKPLAN.md) |
| #47 | **"BYG content"** | — | ⚠ **scope unclear**: the installed biome mod is **BWG** and its branch (B701–B703) already shipped. Either this meant the separate predecessor mod BYG (not installed, not planned) or *deeper* BWG coverage beyond the 3-quest branch — decide before it's actionable. |

*(Mekanism's future chapter — old #40 — is **dropped**: Mekanism was cut from the pack on 2026-07-01, CONTENTPLAN §7.)*

## Mod coverage (triage refreshed 2026-07-01)

- **Quested:** skyseed · create + createaddition + create_better_motors · FluxNetworks · railways ·
  create-enchantment-industry · creategoggles · create_jetpack · the Create deco set · silent-gear ·
  sophisticated (backpacks/storage/core + integrations) · waystones (+ xaero compat) · veinmining · Xaero
  mini/world map · JEI · Jade · AppleSkin · **MysticalAgriculture + Botany Pots family** (MA chapter) ·
  **BWG + OTYG + create-otbwg-compat** (the BWG branch).
- **Invisible (no quests — libs / perf / aesthetic / backends):** embeddium, iris, monocle, EuphoriaPatcher,
  modernfix, ferritecore, entityculling, fpsreducer, ambientsounds, fallingleaves, fastleafdecay, sound-physics,
  fusion, EMF/ETF, balm, kotlinforforge, geckolib, corgilib, better_lib, framework, configured, catalogue,
  searchables, creativecore, supermartijn642 ×2, TerraBlender, Clumps, Controlling, MouseTweaks,
  AdvancedLootInfo, JustEnoughResources, silentgear jade/jei plugins, Cucumber, Patchouli, ftb-filter-system.
- **Not installed (so no chapter yet):** Applied Energistics 2, Immersive Engineering, Productive Bees,
  Farmer's Delight, Iron's Spells, Quark. *(Mekanism: dropped.)*
