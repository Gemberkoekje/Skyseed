# QUESTPLAN — Skyseed: Grow your own world (FTB Quests)

A **cosy, handholdy** quest line that explains every mod and how to use it. Authored *alongside* mod integration (one
chapter per mod as it lands), not last — so no end-of-project overflow. Engine: **FTB Quests** (installed:
`ftb-quests` / `ftb-library` / `ftb-teams` / `architectury`).

> **Status (PR #14):** the six base chapters — Introduction · Skyseed · Create · Mystical Agriculture · Tools & Travel ·
> Storage — have shipped. The **BWG quest chapter** (Into the Wilds / Mill the Blooms / Grow Something Grand) is
> BWGPLAN Step 5, still **pending**.

> **Plan audit (2026-07-01):** 5 items below marked ✅ done — the **Tools**, **Create Extras**, and **Other** chapters all
> shipped (Extras folded into the Create chapter; Other + Tools merged into **"Tools & Travel"**), the Silent Gear
> placement question is **resolved**, and the **Mystical Agriculture** chapter shipped. The BWG chapter and the future
> per-mod chapters remain open — see [`../PLANOFPLANS.md`](../PLANOFPLANS.md).

## Approach

- **Tone — explain, don't gate.** Every quest carries a short plain-language description: what the mod/mechanic is, why
  you'd use it, the one key trick. The quests teach the *why*; lean on the mods' own in-game docs for the deep *how* —
  **Create's Ponder** (animated guides, the `W` key on a Create item) and the **Skyseed guide book** (Modonomicon /
  Patchouli). Link to them from the quest text.
- **Task style.** Mostly "obtain item X" or "complete advancement Y." Skyseed already defines `reveal_*` / `craft_*` /
  `gathered_*` advancements — FTB Quests can hook them directly, so the Skyseed chapter rides the mod's own progression.
  Use OR-tasks + optional quests so the line guides without straitjacketing.
- **Reward style.** Rewards bootstrap the *next* step (a few of the next ingredient, XP, a choice reward). Cosy = the
  player is never left wondering "what now?".
- **Authoring + version control.** Build chapters in-game (FTB Quests **edit mode** — far faster than hand-writing),
  which saves SNBT to `config/ftbquests/quests/`; copy that into `overrides/config/ftbquests/` so it ships with the pack
  and is committed (configs are tracked — see CONTENTPLAN §6). Author one chapter, test it, commit; repeat.

## Chapter map (your categories + Silent Gear)

```
[1 Introduction] ──▶ [2 Skyseed] ─┬─▶ [3 Tools (Silent Gear)]
   (onboarding)        (the spine) ├─▶ [4 Create] ──▶ [5 Create Extras]
                                   ├─▶ [6 Storage]
                                   └─▶ [7 Other]
```

| # | Chapter | Installed mods it covers | Role |
|---|---|---|---|
| 1 | **Introduction** | the void-skyblock idea; JEI, Jade, Xaero map, AppleSkin, Vein Mining, the Skyseed guide | onboard + QoL tour |
| 2 | **Skyseed** | `skyseed` (seed→island progression, relics/edges, overworld→nether→end) | the spine; gates the rest |
| 3 | ✅ **Tools** *(shipped — as the "Tools & Travel" chapter)* | Silent Gear (+ Jade tiers, JEI plugin) | early tools/armor |
| 4 | **Create** | `create`, Crafts & Additions (`createaddition`), Flux Networks | the main tech chain |
| 5 | ✅ **Create Extras** *(shipped — folded into the Create chapter)* | Steam'n'Rails, Enchantment Industry, Goggles, Jetpack, deco (Deco/Connected/Rechiseled/Bells&Whistles/Interiors/Design'n'Decor) | optional/advanced |
| 6 | **Storage** | Sophisticated Backpacks + Storage (+ their Create integrations) | storage QoL (**not** AE2) |
| 7 | ✅ **Other** *(shipped — merged into "Tools & Travel")* | Waystones (+ Xaero waystones compat); odds & ends | utility |

> **Silent Gear** wasn't in your list. It's a meaty tools/armor mod (your Tinkers replacement), so I'd give it a small
> **Tools** chapter rather than bury it in "Other." Alternative: fold its first quest into the Introduction's early game.
> ✅ **Decided (shipped):** it got its own dedicated Tools section — the "Tools" half of the "Tools & Travel" chapter.

## Per-chapter quest breakdown

### 1. Introduction — onboarding + QoL tour
- **Welcome to the Void** — the pitch (you grow your own world from seeds). Task: checkmark. Reward: starter kit + the guide book.
- **Your Almanac** — open the Skyseed guide (Modonomicon). Task: *have* the guide. Reward: XP.
- **Look It Up** — JEI: `R` = recipe, `U` = uses. Task: checkmark.
- **What's That?** — Jade: hover a block/mob for its info. Task: checkmark.
- **Find Your Way** — Xaero minimap + a waypoint. Task: checkmark.
- **Mine Smart** — Vein Mining: fell a whole vein at once. Task: mine an ore (or checkmark). Reward: food (AppleSkin tie-in).
- → unlocks the **Skyseed** chapter.

### 2. Skyseed — the spine (mirrors the guide's progression; hooks the mod's advancements)
- **Your First Seed** → **Grow Your World** (throw it, watch it germinate) → **A Greener Start** (biome seeds: forest/desert/…).
- **Reap the Relics** (gather relics/edges off grown islands) → **Bigger Worlds** (large seeds) → **Huge Horizons** (huge tier).
- **Through the Portal** (portal-frame shards / ruined-portal seed) → **Nether Bound** (nether islands → blaze, fortress, bastion).
- **Eye of the End** (end-portal seed → reach the End) → **The Dragon's Trophy** (capstone).
- *Tasks key off Skyseed's existing `reveal_*` / `craft_*` / `gathered_*` advancements; structure follows the guide-book chapters so the two stay in lockstep.*

### 3. Tools — Silent Gear ✅ *(shipped as the "Tools & Travel" chapter)*
- **Forge a Tool** (rod + head → your first Silent Gear tool) → **Make It Yours** (material traits, blueprints) →
  **Keep It Sharp** (repair kits, tips/grips) → **Tiered Up** (better materials — e.g. nether Crimson Iron later).

### 4. Create — the main tech chain (point each quest to its Ponder)
- **First Rotation** (Andesite Alloy + a water wheel/windmill) → **Cased In** (Andesite Casing).
- **Press It** (Mechanical Press → plates) → **Double Your Ore** (Millstone/Crushing Wheels — *ore doubling*, the skyblock win).
- **Brass & Beyond** (Mixer → Brass). ⭐ **Gated on Skyseed:** brass needs **zinc → grow a Rocky island** (the theme-override
  zinc we just shipped). This quest *depends on* the Skyseed chapter's "grow a rocky island."
- **Automate It** (Deployer, Mechanical Arm, Mechanical Crafter) → **Power Up** (Crafts & Additions: Electric Motor /
  Alternator — rotation ↔ FE) → **Wireless Watts** (Flux Networks: one FE grid across your islands, no cables over the void).

### 5. Create Extras — optional/advanced ✅ *(shipped — folded into the Create chapter)*
- **All Aboard** (Steam'n'Rails: track + station + a train to link islands) · **Enchanted Industry** (automate XP/enchanting) ·
  **Goggles On** (Create Goggles) · **Take Flight** (Create Jetpack) · **Dress It Up** (the decoration addons).

### 6. Storage — Sophisticated (not AE2)
- **Pack It Up** (Sophisticated Backpacks + upgrades: pickup, magnet, stack) → **Barrels & Boxes** (Sophisticated Storage:
  barrels/chests, upgrade slots, the controller) → **Hooked to Create** (the Storage↔Create integration).

### 7. Other — utility ✅ *(shipped — merged into "Tools & Travel")*
- **Set Your Home** (Waystones: place + attune + teleport between islands — big QoL for a multi-island world) ·
  **On the Map** (Xaero waystones compat) · *odds & ends as they arrive.*

## Cross-chapter flow & gating
- **Skyseed is the spine.** Tech needs materials, materials come from grown islands — so chapters 3–7 unlock off Skyseed
  milestones, not at world start.
- **The one hard tie-in so far:** Create's **brass** ⇐ **zinc** ⇐ **grow a Rocky island**. The Create "Brass & Beyond" quest
  takes the Skyseed "grow a rocky island" quest as a dependency. (This is exactly why we did the zinc theme-override first.)
- Keep dependencies *soft* elsewhere (chapters openable in parallel) so the pack stays cosy, not on-rails.

## Scope & future chapters
- **Covered now:** Skyseed, Create (+ extras), Sophisticated storage, Silent Gear, Waystones, the QoL/nav mods.
- **Future chapters (mods to integrate later, each gets its own):** **Applied Energistics 2** (its own chapter, as you
  noted), **Mekanism**, **Immersive Engineering**, ✅ ~~**Mystical Agriculture**~~ *(shipped)*, **Productive Bees**, **Farmer's Delight**,
  **Iron's Spells**, **Quark**, and **BYG** content. Each lands with its island/resource integration (+ a theme-override
  patch where it needs ores), then its quest chapter — same weave as Create.

## Mod coverage (current jars)
- **Quested:** skyseed · create + createaddition + create_better_motors · FluxNetworks · railways · create-enchantment-industry ·
  creategoggles · create_jetpack · (deco) createdeco/create_connected/rechiseledcreate/bellsandwhistles/interiors/Design-n-Decor ·
  create content (aquatic_ambitions/things_and_misc/DragonsPlus/Encased — fold into Extras) · silent-gear · sophisticated(backpacks/storage/core + integrations) ·
  waystones (+ xaero compat) · veinmining · Xaero mini/world map · JEI · Jade · AppleSkin.
- **Invisible (no quests — libs / perf / aesthetic):** embeddium, iris, monocle, EuphoriaPatcher, modernfix, ferritecore,
  entityculling, fpsreducer, ambientsounds, fallingleaves, fastleafdecay, sound-physics, fusion, EMF/ETF, balm, kotlinforforge,
  geckolib, corgilib, better_lib, framework, configured, catalogue, searchables, creativecore, supermartijn642 ×2,
  TerraBlender, Clumps, Controlling, MouseTweaks, AdvancedLootInfo, JustEnoughResources, silentgear jade/jei plugins.
- **Not installed (so no chapter yet):** Applied Energistics 2, Mekanism, Immersive Engineering, ~~Mystical Agriculture~~ *(✅ now installed + chaptered)*,
  Productive Bees, Farmer's Delight, Iron's Spells, Quark.
