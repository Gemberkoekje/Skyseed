# MYSTICALPLAN — Mystical Agriculture + Botany Pots integration

Grounded in the actual jars (MA `8.0.27`, Agradditions `8.0.13`, Botany Pots `21.1.44`, Botany Pots Mystical `21.1.12`).
Skyseed is now `0.167.0`.

> **Status: DONE — shipped.** The first-party MA `theme_override` compat and the Mystical Agriculture quest chapter both
> merged. The ore split is now complete: **stone** inferium/prosperity on the **Lush** island (`mysticalagriculture_lush{,_large,_huge}.json`,
> v0.172.0 — the original §Fix intent) **and** the **deepslate** variants on **Ancient**, plus soulium on Nether Soul.
> Item #62 (the Lush stone ores) closed 2026-07-01. Only soft follow-ups remain (balance watch, optional Prosperity island).

> **Plan audit (2026-07-01):** the pending in-game quest-book test-load (Build order §3) is ✅ complete, and the
> **in-game bootstrap-loop sanity check (Build order §2) is now ✅ done** — deepslate Inferium/Prosperity ore confirmed on
> Ancient and the Inferium Seed + Inferium Farmland in an Elite Botany Pot grows as intended. That test surfaced **#62**
> (stone MA ores on Lush — both stone ores exist in MA 8.0.27). Remaining soft follow-ups (balance-tuning watch, the
> optional dedicated Prosperity island) are tracked in [`../PLANOFPLANS.md`](../PLANOFPLANS.md).

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

## Compatibility / worldgen
- All dependencies present; no load conflicts expected.
- MA ores place via NeoForge **biome modifiers** (`mysticalagriculture:inferium_ore` & `prosperity_ore` → `#is_overworld`; `soulium_ore` → nether/soulstone). The `skyseed:void` ChunkGenerator already suppresses overworld/nether biome decoration, so **MA ores never generate naturally** — exactly the BWG/zinc situation. No config wiring needed; we add them deliberately via theme-override (below).
- Botany Pots / Trees / Tiers / Mechanical Botany: item/block + automation only, no worldgen.

## ⭐ The one thing that MUST be solved: the bootstrap gap
MA progression starts from two items:
- **Inferium Essence** — drops from hostile mobs (default) **and** Inferium Ore. (Has an alt source.)
- **Prosperity Shard** — **only from Prosperity Ore. No mob drop.** It's required for **Prosperity Seed Base**, which every crafting seed needs.

So without Prosperity Ore the whole mod is hard-blocked — and MA ores don't generate in the void. **The player needs Prosperity + Inferium ore from a Skyseed island.**

### Fix — first-party theme-override (mirrors the Create-zinc compat)
Ship a Skyseed theme-override (data-only, inert without MA, like `data/skyseed/skyseed/theme_override/create_rocky.json`):
- `mysticalagriculture:prosperity_ore` + `inferium_ore` (the **stone** variants) on a fertile island — **Lush** is the natural home (green + ore-cave vibe), gated mid-game. **✅ SHIPPED (v0.172.0)** — `mysticalagriculture_lush.json` (+ `_large`/`_huge`), stone ores at `core`, inert without MA, `mystical_agriculture_compat_targets_lush` gametest on both nodes.
- Their **deepslate** variants (`deepslate_prosperity_ore`/`deepslate_inferium_ore`) in a deep (≤y8) band → "throw it low for the richer ore," consistent with the zinc deep-band. **✅ SHIPPED** — placed on the **Ancient** island (`mysticalagriculture_ancient.json` + `_large`/`_huge`; Lush had no deep band, so the fallback was taken).
- `mysticalagriculture:soulium_ore` on a **Nether** island (`nether_soul` — soulstone is nether). **✅ SHIPPED.**
- Files: **✅ shipped** `mysticalagriculture_ancient.json` (+ `_large`/`_huge`) + `mysticalagriculture_nether_soul.json` (+ `_large`), with the `mysticalAgricultureCompatTargetsAncient`/`…NetherSoul` gametests. **⏳ #62:** add `mysticalagriculture_lush.json` (+ `_large`/`_huge`) with the **stone** ores on a surface/shallow band + a gametest + version bump. *(In-game test 2026-07-01 confirmed the deepslate ores + the bootstrap loop work; #62 adds the more-accessible stone source the plan first intended.)*
- *(Alternative: a modpack-side datapack via a global-pack loader — but first-party matches the Create precedent and needs no extra mod.)*

## The skyblock farming loop (why Botany Pots is the perfect pairing)
1. Mine a little Prosperity + Inferium ore off the Lush island.
2. Craft **Inferium Farmland** (dirt + Inferium Essence) and a **Prosperity Seed Base** → resource crafting seeds.
3. Drop the farmland into a **Botany Pot**, plant the seed — grows **land-free**, no tilled soil needed.
4. **Mechanical Botany** (or a Create deployer/harvester) auto-harvests; **Sophisticated** storage already handles output.
- **Botany Trees** covers renewable wood in pots; **Botany Pots Tiers** speeds it up.

## Role & gating (keep it a layer, not a bypass)
- Position MA as the **mid/late automation layer**: Skyseed islands stay "first of each resource"; MA is "now scale/automate it." Don't let an Iron seed undercut the Rocky island on day one.
- Gate the Lush(+MA-ore) island mid-game in the quest line (after the basic + rocky/ancient islands). The deepslate ore in the deep band reinforces the existing "throw low" mechanic.
- **Agradditions Tier-6** (Nether Star / Dragon Egg / Neutronium / Gaia / Nitro / Awakened Draconium) = far endgame — let it sit past the Dragon Trophy.
- Balance knobs: keep mob Inferium drops on (gentle early alt source); watch Growth Accelerator stacking if crops get too fast.

## Quest chapter (build later, same weave as Create)
A "Mystical Agriculture" chapter, gated off the Lush/Prosperity island:
1. **Pot It Up** — Botany Pots: land-free farming + soils.
2. **Prosperity Found** — grow the Lush island, mine Prosperity + Inferium ore.
3. **First Essence** — Inferium Essence → Inferium Farmland → Prosperity Seed Base.
4. **Sow a Seed** — craft a resource seed, grow it in a pot on Inferium farmland.
5. **Climb the Tiers** — Prudentium → Supremium farmland; Infusion Altar + Infusion Crystal.
6. **Hands-Free Harvest** — Mechanical Botany / Create auto-pots.
7. **Branches** — Botany Trees (wood), Soulium Dagger (mob seeds), Insanium / Tier-6 (endgame).

## Build order
1. **Skyseed first-party MA theme-override** — ✅ DONE: deepslate ore → Ancient, **stone ore → Lush (#62, v0.172.0)**, soulium → nether; all with gametests + version bumps.
2. ✅ **Verify in-game (2026-07-01):** deepslate Inferium/Prosperity ore appears on Ancient, and the MA bootstrap (essence → Inferium Farmland → seed → Elite Botany Pot crop) works. *(Surfaced #62: add the stone ores to Lush.)*
3. ✅ **MA quest chapter** — shipped; the previously-pending in-game quest-book test-load is **complete**.
4. *(Later, roadmap)* optional dedicated **Prosperity island** as its own Skyseed seed/tier.
