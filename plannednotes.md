# Planned Notes

Loose ideas + the trial-chamber tail that don't have their own plan doc. Shipped work is pointer-only — the detail
lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md`.

## Open

- [ ] **(#70)** Waystone drop-compat — a compat that occasionally drops a waystone (decide + build; unscoped).
- [ ] **(#61)** Trial Chamber — capturing the vanilla *feel*. See **[TRIALCHAMBERPLAN.md](TRIALCHAMBERPLAN.md)**.
  - **Structure — corridor-warren SHIPPED v0.194.0** (atrium → passages → junctions → chambers). *In-game: does it
    wind/branch, stay inside the island? Knobs: hall/junction weights in `halls.json`, jigsaw `depth`.*
  - **Surface — decorated framed-panel wall MOSAIC** replacing the old random `mix()` speckle (the thing that made it
    read as "rooms using the tileset"). **SHIPPED to every piece v0.196.0–v0.203.0** (atrium showcase → full rollout,
    both nodes green). *In-game left: the vanilla feel/fit sign-off + tune (module size, motif frequency, patina mix).
    (User: pillars too much at this scale — decoration is the lever, not size/pillars.)*
- [x] **(#33)** Trial Chamber — more room/corridor variants. **BUILT v0.227.0** (STRUCTURELONGTAILPLAN Phase C): a 4-way
  **crossing**, an **alcove corridor** (chamber spur off a straight hall via an L-shaped side alcove), and a grand
  **multi-cell vaulted chamber** (stepped groin vault) — all in `TrialChamberTemplates.java`, woven into the halls/rooms
  pools, assembly-gametested, both nodes green (217/219). In-game feel/fit folds into **#61**. *(Was a 2-build regen
  dance, [[skyseed-structure-staging]].)*

## Shipped (pointer-only)

- **Void worldgen enforcement** — custom `SkyseedVoidChunkGenerator` (per-dimension no-op `applyBiomeDecoration` /
  `createStructures`) makes Skyseed immune to biome/structure mods decorating the void floor while keeping the biome
  source + island theming intact. **✅ SHIPPED v0.165.0** (both nodes, in-game verified). See CHANGELOG.
- **Trial Chamber — vanilla-like redesign (#24 + #25)** — aged waxed-copper + tuff palette, copper-bulb lighting, a
  cut-copper cornice, a bigger 9×9 two-storey atrium / 7×7 chambers on a deepened island, multi-story **downward**
  (descent staircases dropping a storey + an ominous-vault end room), and greebling (cobwebs, candles, decorated pots,
  suspicious-gravel digs, moss). **✅ SHIPPED v0.189.0–v0.191.0** (both nodes green). Remaining: **#33** (more variants,
  above) and **#61** (in-game vanilla compare, above). See CHANGELOG.
