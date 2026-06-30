# MYSTICALPLAN — Mystical Agriculture + Botany Pots integration

Grounded in the actual jars (MA `8.0.27`, Agradditions `8.0.13`, Botany Pots `21.1.44`, Botany Pots Mystical `21.1.12`).
Skyseed is now `0.167.0`.

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
- `mysticalagriculture:prosperity_ore` + `inferium_ore` on a fertile island — **Lush** is the natural home (green + ore-cave vibe), gated mid-game.
- Their **deepslate** variants (`deepslate_prosperity_ore`/`deepslate_inferium_ore`) in a deep (≤y8) band → "throw it low for the richer ore," consistent with the zinc deep-band. *(Confirm at build time whether Lush has a deep band; if not, put the deepslate variants on the Ancient island instead.)*
- `mysticalagriculture:soulium_ore` on a **Nether** island (`nether_soul` — soulstone is nether).
- Files: `mysticalagriculture_lush.json` (+ `_large`/`_huge`), `mysticalagriculture_nether_soul.json`. Add a gametest like `createZincReachesRockyDeepBand`, bump version + CHANGELOG.
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
1. **Skyseed first-party MA theme-override** (ore → Lush + nether) + gametest + version bump. *(small, data-only)*
2. Verify in-game: ore appears on the island, and the MA bootstrap (essence → farmland → seed → pot crop) works.
3. **MA quest chapter** (after the pending in-game quest-book test-load).
4. *(Later, roadmap)* optional dedicated **Prosperity island** as its own Skyseed seed/tier.
