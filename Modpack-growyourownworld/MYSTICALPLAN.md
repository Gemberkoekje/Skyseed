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
> - **#51 — balance watch** (ongoing playtesting): see [Role & gating](#role--gating-keep-it-a-layer-not-a-bypass).
> - **#50 — optional dedicated Prosperity island** (pure polish): see below.

## Mods added (shipped — see `mods.txt`)

The MA family (Cucumber, MysticalAgriculture, MysticalAgradditions incl. the Insanium 5th tier + Tier-6 Crux crops,
MysticalCustomization) + the Botany Pots ecosystem (Bookshelf, Prickle, BotanyPots, BotanyPotsTiers, BotanyTrees,
MechanicalBotany, and the **BotanyPotsMystical** glue — MA crops grow in pots using MA farmland tiers as soil). All in.

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
