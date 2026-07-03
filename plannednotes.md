# Planned Notes

Loose ideas + the trial-chamber tail that don't have their own plan doc. Shipped work is pointer-only — the detail
lives in `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md`.

## Open

- [ ] **(#70)** Waystone drop-compat — a compat that occasionally drops a waystone (decide + build; unscoped).
- [ ] **(#61)** Trial Chamber — capturing the vanilla *feel*. See **[TRIALCHAMBERPLAN.md](TRIALCHAMBERPLAN.md)**.
  - **Structure — corridor-warren SHIPPED v0.194.0** (atrium → passages → junctions → chambers). *In-game: does it
    wind/branch, stay inside the island? Knobs: hall/junction weights in `halls.json`, jigsaw `depth`.*
  - **Surface — decorated framed-panel wall MOSAIC** replacing the old random `mix()` speckle (the thing that made it
    read as "rooms using the tileset"). **Phase 1 SHIPPED v0.196.0 on the ATRIUM** (showcase); rolls out to the rest
    of the warren once the look is confirmed in-game. *(User: pillars too much at this scale — decoration is the lever,
    not size/pillars.)*
- [ ] **(#33)** Trial Chamber — more room/corridor variants. Partly done by the warren (corner/junction/cell/descent
  add variety); further options: cross-intersections, an alcove-corridor (chamber spur off a straight hall), bigger
  multi-cell chambers, vaulted ceilings. All in `TrialChamberTemplates.java`; new pieces trigger the **2-build regen
  dance** (see [[skyseed-structure-staging]]).

## Shipped (pointer-only)

- **Void worldgen enforcement** — custom `SkyseedVoidChunkGenerator` (per-dimension no-op `applyBiomeDecoration` /
  `createStructures`) makes Skyseed immune to biome/structure mods decorating the void floor while keeping the biome
  source + island theming intact. **✅ SHIPPED v0.165.0** (both nodes, in-game verified). See CHANGELOG.
- **Trial Chamber — vanilla-like redesign (#24 + #25)** — aged waxed-copper + tuff palette, copper-bulb lighting, a
  cut-copper cornice, a bigger 9×9 two-storey atrium / 7×7 chambers on a deepened island, multi-story **downward**
  (descent staircases dropping a storey + an ominous-vault end room), and greebling (cobwebs, candles, decorated pots,
  suspicious-gravel digs, moss). **✅ SHIPPED v0.189.0–v0.191.0** (both nodes green). Remaining: **#33** (more variants,
  above) and **#61** (in-game vanilla compare, above). See CHANGELOG.
