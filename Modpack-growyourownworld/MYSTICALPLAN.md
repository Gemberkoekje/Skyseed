# MYSTICALPLAN — Mystical Agriculture + Botany Pots integration

Grounded in the actual jars (MA `8.0.27`, Agradditions `8.0.13`, Botany Pots `21.1.44`, Botany Pots Mystical `21.1.12`).

> **Status: SHIPPED (verified in-game 2026-07-01).** The first-party MA `theme_override` compat is complete —
> **stone** inferium/prosperity ores on **Lush** (`mysticalagriculture_lush{,_large}.json` +
> `mysticalagriculture_huge_lush.json`, v0.172.0, item #62), the **deepslate** variants on **Ancient**, and
> soulium on **Nether Soul** — each with gametests on both nodes. The 9-quest MA chapter shipped incl. its
> in-game test-load, and the in-game bootstrap-loop check passed (ore on Ancient; essence → Inferium Farmland →
> seed → Elite Botany Pot grows as intended).
>
> **What's left** (tracked in [`../PLANOFPLANS.md`](../PLANOFPLANS.md)):
> - ~~**#69 — refresh quest B602 "Prosperity Found"**~~ ✅ **DONE (v0.186.0):** the description in
>   `overrides/config/ftbquests/quests/lang/en_us.snbt` (B602 `quest_desc`) no longer says the MA ores "come from
>   one place" / directs only to an **Ancient** island — it now **leads with the accessible Lush stone ores** (added
>   v0.172.0) and frames Ancient as the *richer* deepslate option. **Gating left as-is** (B602 still depends on B601
>   Botany Pots + B105 "Into the Deep"): the player reaches B602 having already done the deep dive, so presenting Lush
>   as the easy alternative reads fine; loosening the B105 dependency is a bigger quest-graph call, deferred as optional.
> - **#51 — balance watch** (ongoing playtesting): see [Role & gating](#role--gating-keep-it-a-layer-not-a-bypass).
> - **#50 — optional dedicated Prosperity island** (pure polish): see below.

## Mods added (roles + deps)
**Mystical Agriculture family (BlakeBr0):**
- `Cucumber` — required library.
- `MysticalAgriculture` — core: grow resources as crops via the Inferium → Prudentium → Tertium → Imperium → Supremium essence tiers.
- `MysticalAgradditions` — endgame: the **Insanium** 5th tier + **Tier-6 crops** (Nether Star, Dragon Egg, Neutronium, Gaia Spirit, Nitro Crystal, Awakened Draconium) via Crux blocks.
- `MysticalCustomization` — datapack/config tool to add or re-gate crops/tiers (ships no content itself).

**Botany Pots ecosystem (Darkhax et al.):**
- `Bookshelf`, `Prickle` — required libraries.
- `BotanyPots` — grow crops/saplings in pots with **no farmland/land**. Soils are tag-based (dirt/sand/nether/end/…).
- `BotanyPotsTiers` — faster tiered pots. `BotanyTrees` — grow trees (renewable wood) in pots. `MechanicalBotany` — powered/auto-harvest pots.
- `BotanyPotsMystical` — the glue: MA crops grow in pots using **MA farmland tiers as the soil** (`inferium`→`supremium`, Agradditions `insanium` + the Tier-6 **Crux** blocks).

## Compatibility / worldgen (standing trap for any future MA work)
- MA ores place via NeoForge **biome modifiers** (`mysticalagriculture:inferium_ore` & `prosperity_ore` → `#is_overworld`; `soulium_ore` → nether/soulstone). The `skyseed:void` ChunkGenerator suppresses overworld/nether biome decoration, so **MA ores never generate naturally** — any MA ore source (including a future Prosperity island, #50) must carry its ores via the theme system, never natural generation.
- Botany Pots / Trees / Tiers / Mechanical Botany: item/block + automation only, no worldgen.

## The bootstrap gap (why the ore islands exist — grounding for #50)
MA progression starts from two items: **Inferium Essence** (drops from hostile mobs and Inferium Ore) and
**Prosperity Shard** (**only from Prosperity Ore — no mob drop**; required for the Prosperity Seed Base that
every crafting seed needs). Without Prosperity Ore the whole mod is hard-blocked — hence the shipped ore
overrides. **The gap is solved**: Lush = the accessible stone tier, Ancient = the deep richer deepslate tier
("throw it low"), Nether Soul = soulium. A dedicated Prosperity island (#50) is therefore **optional polish**
for progression clarity, not a blocker — if ever built: seed recipe + theme json + gametest + quest hook.

## Role & gating (keep it a layer, not a bypass)
- Position MA as the **mid/late automation layer**: Skyseed islands stay "first of each resource"; MA is "now scale/automate it." Don't let an Iron seed undercut the Rocky island on day one.
- The Lush(+MA-ore) island is gated mid-game in the quest line; the deepslate ore on Ancient reinforces the existing "throw low" mechanic.
- **Agradditions Tier-6** = far endgame — let it sit past the Dragon Trophy.
- **Balance knobs (#51, watch during normal play):** keep mob Inferium drops on (gentle early alt source); watch **Growth Accelerator stacking** and crop speed — tune the MA / Botany Pots Tiers configs only if playtesting surfaces a problem (no overrides exist yet, deliberately).

## The skyblock farming loop (reference)
1. Mine a little Prosperity + Inferium ore off the **Lush** island (or the richer deepslate off **Ancient**).
2. Craft **Inferium Farmland** (dirt + Inferium Essence) and a **Prosperity Seed Base** → resource crafting seeds.
3. Drop the farmland into a **Botany Pot**, plant the seed — grows **land-free**, no tilled soil needed.
4. **Mechanical Botany** (or a Create deployer/harvester) auto-harvests; **Sophisticated** storage handles output.
- **Botany Trees** covers renewable wood in pots; **Botany Pots Tiers** speeds it up.
