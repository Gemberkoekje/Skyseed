# Per-biome Nether/End seed adaptation — open items only

> **Nether pass BUILT + VERIFIED (v0.224.0), both nodes green.** Model-A biome kits on the 5 `nether_*` families
> (base + `_large`); a hand-built crimson/warped stem-tree feature (`CustomTrees` + `DecorationPlanner`); a
> **Nether Wild** seed (base + `_large`) with a Nether biome→theme resolver in `ExploreThemes` + full onboarding;
> golden-master gametests in both suites. See the changelogs + git. Trimmed to OPEN items.

## What's left
- **End light pass (§5)** — DEFERRED (low value, not dropped): per-end-biome accents on each seed's bare end-stone
  form, base + `_large` (~20 theme edits — end_highlands purpur scatter + sparse chorus + rare end_rod, etc.).
- **End Wild seed** — DROPPED (§9c-6); revisit only if the End pass is ever picked up.
- **In-game throw-test (Nether only)** — throw every `nether_*` seed + Nether Wild across all 5 Nether biomes;
  confirm kits/accents/stem-trees resolve.
- **Ship-rules for the End pass when done:** an inert golden-master gametest per kit-set, a minor `mod_version` bump
  + per-node CHANGELOG entry in the same commit, server-side gen.
