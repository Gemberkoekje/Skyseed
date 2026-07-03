# QUARKISLANDPLAN — Quark × Skyseed island integration

> Child of [QUARKPLAN.md](QUARKPLAN.md); PLANOFPLANS **#71**. **Design call (2026-07-02): Quark gets NO dedicated
> seeds / island tiers** — every Quark tie-in is an *optional extra* layered onto existing islands via
> `theme_override` (`ores` / `decoration` / `surface_scatter`), inert without Quark.

## ✅ Shipped — see CHANGELOG

All phases shipped v0.184.0–v0.185.0, both nodes green, each gametest-guarded and byte-identical without Quark:

- **Quark stones on the mining islands** — limestone/jasper (Rocky) + jasper/shale (Ancient) as core ore-veins, plus
  a deep **corundum** geode; **myalite** on the Rocky/Ancient **End** form.
- **Blossom groves** on the three Forest tiers — the biome's normal tree PLUS a Quark blossom + a harvestable sapling
  on snowy / swamp / savanna / plains / badlands bands (Quark's own biome map). Bands MERGE (append), not shadow.
- **Ancient Tomes** in structure-island loot — via Quark's own `[tools.ancient_tomes]` config (not a Skyseed GLM, so
  it stays inert in the no-Quark pack): existing dungeon/mansion/bastion/ancient-city tables + added **Trial Chamber**
  reward chests (`trial_chambers/reward,10` + `reward_ominous,12`) and re-enabled **Nether fortress** (`nether_bridge,8`).

## Open

### ✅ (#71) Quark stones don't appear on Rocky/Ancient — Y-band bug FIXED v0.192.0

In-game the user threw multiple Rocky/Ancient islands and saw **no Quark stones**. Root cause (ids were fine — all of
`quark:limestone/jasper/shale/myalite/blue_corundum` exist in `Quark-4.1-481.jar`): the veins were on each override's
**top-level `ores`**, which apply only when **no `biome_override` matches** (a matched band's `ores` *replaces* the
list — `IslandGenerator.eff`). The base themes band the whole Y range (rocky `max_y:8`/`min_y:70`/`min_y:130`/snowy;
ancient `max_y:20`/`min_y:96`), so the Quark stones survived **only in the un-banded gap** (rocky Y ≈ 8–70, ancient
Y ≈ 20–96) — and even there they're buried core veins.

**Fixed:** mirrored Create's deepslate-zinc trick — added same-selector `biome_override` band patches (matching each
base band's exact selector so they **merge/append** the veins rather than shadow) across all 6 files, giving every
overworld Y-band its Quark veins. Gametest `quarkStonesReachYBands` asserts every tier (base/large/huge) carries them.

**Amounts bumped (v0.193.0):** limestone is now abundant (Rocky chance 0.55→0.95, veins base 4–7 / large 12–18 / huge
16–24, size 8–14) with a matching jasper/shale bump on Rocky/Ancient; corundum stays a rare geode. All six files
updated together ([[skyseed-mirror-island-tiers]]).

### Remaining (in-game, after the fix)

- [ ] **Re-verify** at low / mid / high throws (mine into the core) that Quark stones now show at every depth, then
  **tune weights** (vein `chance`+`vein_size`; tome `[tools.ancient_tomes] "Loot Tables"` in `overrides/config/quark-common.toml`).
- [ ] Blossom groves render + drop saplings on the right biome islands; **Ancient Tomes** appear in the
  dungeon/mansion/bastion/ancient-city/**trial**/**fortress** island chests.
- *(Minor follow-up: the End form of `rocky_large`/`ancient_large` still has no myalite band — add if you want Quark
  stone on the large End islands too; the small Rocky/Ancient End forms already do.)*
