# Changelog — Minecraft 26.1.2 build

Notable changes to the **26.1.2** Skyseed build. Skyseed is one codebase built for multiple Minecraft versions (see
`REFACTORPLAN.md`); the **1.21.1** build's history is in [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md). Both builds share
the version-number sequence, so a version can appear in one changelog and not the other.

> **Status: the multi-version refactor is COMPLETE — the 26.1.2 build compiles, builds, and passes its own gametest
> suite.** Both version nodes are green (`./gradlew chiseledBuild` / per-node `:1.21.1:` / `:26.1.2:` tasks; CI builds
> and gametests every node). Remaining repo-wide work is tracked in `PLANOFPLANS.md`. The per-feature build plans (the
> gametest harness, the recipe generator, and the Modonomicon guide) shipped and were retired into this changelog.

## [0.223.0] - 2026-07-05

### Added
- **Biome coverage pass — every overworld biome now grows a fitting island (→ [BIOMECOVERAGEPLAN.md](Modpack-growyourownworld/BIOMECOVERAGEPLAN.md)).**
  Audited every overworld biome (vanilla + Oh The Biomes We've Gone + Quark) against the two-layer biome→look resolution
  (`ExploreThemes` biome→theme + each theme's `biome_overrides`). Root cause of the gaps: snowy/desert bands key off
  explicit vanilla ids (no vanilla `#is_snowy`/`#is_desert` tag), so modded snowy/desert biomes fell through to a green
  Forest island. Eight fixes, all **inert without the mod** (byte-identical on the vanilla pack): F1 BWG deserts→Desert,
  F2 BWG frozen (snowy/icy)→Frozen + `frosted_*` snow bands, F3 vanilla `sparse_jungle` open band, F4 BWG plains→Meadow
  (activates the pre-authored BWG flower-field bands + new `pumpkin_valley`), F5 `cypress_wetlands`, F6 `howling_peaks`
  snow, F7 Quark `glimmering_weald`→Lush, F8 `orchard` forest band (adversarial-review find; also corrected a
  `crimson_tundra`→Frozen mis-route — it's a temperate grassland, temp 0.75). Both nodes green (1.21.1: 209, 26.1.2: 211).

## [0.222.0] - 2026-07-05

### Changed
- **Structure Variety Phase 4 — the weight-tuning pass (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §8), the last item on PLANOFPLANS #68.** An analytical pass over every overworld theme's weighted rare table.
  **Flagship dilution confirmed** — citadel 0.48%, woodland mansion 0.93%, ocean monument ~1.9%, ancient city 1.5% per
  fitting seed (genuine standouts). **Rebalanced the rare-heavy `huge_` tiers** by mirroring the base-tier small commons up
  (the user OK'd small structures on large islands): huge_meadow/badlands/desert/forest/rocky had lacked the Band-1 vanilla
  commons their base siblings carry, so they skewed rare/epic-heavy (huge_rocky was 0% common). After mirroring: huge_meadow
  26→51% common, huge_badlands 24→51%, huge_desert 38→64%, huge_forest 33→62%, huge_rocky 0→28% (still premium-dense but with
  ordinary variety), and the flagships diluted further (mansion 1.67→0.93%). Base + `_large` tiers were already balanced and
  untouched. **Theme-JSON only** — every added building is a pre-existing, already-reviewed common, so no structure geometry
  changed (no walking-path risk). Both nodes green (1.21.1: 209, 26.1.2: 211). **Closes PLANOFPLANS #68 bar subjective in-game
  feel-tuning.**

## [0.221.0] - 2026-07-05

### Added
- **Structure Variety Band 3 COMPLETE — the Sky-Freight Depot (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B30), the last build in the B1–B30 catalog.** A `requires:["create","immersiveengineering"]` epic (w1, Rocky) that
  only germinates where **both** Create and IE are installed: a freight yard where a Create train sits stalled at an IE
  loading dock — an open gravel-and-cobble pad with a vanilla rail siding carrying a stalled train (Create `small_bogey`
  bogeys under brass/copper/railway-casing car bodies + a loco cab), an IE loading platform (a conveyor line, stacked
  crates, a fuel barrel), and a vanilla cobblestone signal mast + lantern. Built to the floating-block rule the earlier
  review established (**no vanilla block rests on a modded one**), so the all-Create train + all-IE platform vanish cleanly
  together without their mods, leaving the vanilla shell. **D4:** Create → `andesite_alloy` (husks on a plain rail); IE →
  iron plate (conveyor/crate husks). Wired w1 across all three Rocky tiers; adversarial review clean. Both nodes green
  (1.21.1: 209, 26.1.2: 211). **Band 3 is now complete (B25–B30)** and the whole B1–B30 Structure Variety catalog has
  shipped — only the in-game weight-tuning pass is left on PLANOFPLANS #68.

## [0.220.0] - 2026-07-05

### Added
- **Structure Variety Band 3 — the Alchemist's Distillery (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B29).** A `requires:["immersiveengineering","irons_spellbooks"]` epic (w1, Badlands) that only germinates where **both**
  IE and Iron's Spells are installed: an IE refinery repurposed as an arcane still gone cold — a roofless rusty-sheetmetal
  shed around a lava-cauldron fire, a weathered-copper still-head + brewing stand, a weathered-copper **condenser column**
  with a lightning-rod vent, a water receiver, IE fluid tanks + a capacitor, and an amethyst focus / bookshelf study /
  essence-scrap chest. IE machinery via the `modNames` side-map; Iron's is a mob/item mod so the still + arcane fittings are
  all vanilla (the still stands on vanilla copper, so it doesn't float without IE — the gametest shell). **D4:** IE loot is an
  iron plate (husk still, no working multiblock); Iron's loot is `arcane_essence` only. Wired w1 across all three Badlands
  tiers. **Adversarial review (afterwards)** reworked the still off a full-width barrier into a left-side cluster so the
  central doorway axis stays a clear walkway to the loot. Both nodes green (1.21.1: 208, 26.1.2: 210).

## [0.219.0] - 2026-07-05

### Added
- **Structure Variety Band 3 — the FE→ME Substation (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B28), the sharpest D4 case in the plan.** A `requires:["immersiveengineering","ae2"]` epic (w1, Rocky) that only
  germinates where **both** IE and AE2 are installed: a derelict power-conversion station — a gutted roofless hall with an
  IE concrete/sheetmetal west end (wire-mast + post-transformer husk + capacitor), a certus-quartz/fluix AE2 east end, and
  a scorched **conversion column** (deepslate + a broken conduit antenna) between them, over the **empty controller pit** (a
  deepslate mount where the ME Controller was torn out). **D4 — sharpest gate:** NO `sky_stone_*` / `controller` / press /
  processor anywhere (the pit is empty); loot is fluix + a copper wire coil only. Both mods' machinery via the `modNames`
  side-map; the vanilla deepslate column/pit + end-rod antenna + chest + lantern are the gametest shell. Wired w1 across all
  three Rocky tiers. Both nodes green (1.21.1: 207, 26.1.2: 209).

### Fixed
- **FE→ME Substation stale-mod-id placement** (caught by the new gametest + the adversarial review): the scrap chest + the
  lantern sat on the back-wall line where the wall loop had already stamped a mod id, so the chest emitted as
  `ae2:quartz_block` — moved both to clear interior cells.

## [0.218.0] - 2026-07-05

### Added
- **Structure Variety Band 3 — the Automated Essence Farm (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B27).** A `requires:["create","mysticalagriculture"]` epic (w1, Meadow) that only germinates where **both** Create and
  Mystical Agriculture are installed: a mechanised inferium field seized up mid-harvest — a derelict essence plot (inferium
  farmland rows, some trampled, crops at mixed growth) straddled by a stalled harvester **gantry** (a vanilla oak frame of
  six posts → two side-rails → a cross-bridge, from which a **Create** `mechanical_drill` husk hangs with its casing body +
  a cogwheel drive), a hanging gantry lantern, a west control station (casing panel + scrap chest), and a derelict tier-1
  `growth_accelerator` + coolant cauldron behind a broken oak-fence rail. Create machinery + MA crops via the `modNames`
  side-map; the gantry frame is deliberately vanilla (the gametest shell). **D4 compounding gate:** Create → `andesite_alloy`
  (husk drill, no working contraption); MA → tier-1 `inferium_essence` only (no seeds/higher-tier essence/prosperity/infusion).
  Wired w1 across all three Meadow tiers; a new gametest per mirror. **Adversarial physical review clean** — the whole gantry
  is carried (nothing floats), the chest is openable, the field stays 2-blocks walkable. Both nodes green (1.21.1: 206,
  26.1.2: 208).

## [0.217.0] - 2026-07-05

### Fixed
- **Structure Variety — physical-playability review (floating blocks).** An adversarial pass over the surprise buildings
  authored this wave caught a repeated mistake — a soul-lantern/lantern hung from a roof beam that was itself unsupported,
  so both floated. Fixed by carrying the beam wall-to-wall in the **Ruined Chapel** (B25, two supported cross-beams) and
  the **Magitech Workshop** (B26), and by raising the **Cook's Homestead** (B24) kitchen lantern so it hangs from the ridge
  slab rather than from air. Rechecked the rest (Iron's spire lanterns on fence poles + the obelisk beacon on its solid tip
  are supported; chests stay openable; doorways are 2 tall). Both nodes green (1.21.1: 205, 26.1.2: 207).

## [0.216.0] - 2026-07-05

### Added
- **Structure Variety Band 3 — the Magitech Workshop (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B26), the first multi-mod epic.** A `requires:["create","irons_spellbooks"]` epic (w1, Rocky) that only germinates
  where **both** Create and Iron's Spells are installed: an arcane-forge workshop, *not a box* — an andesite-and-stone hall
  with a caved roof, a breached wall and a broken brick forge-flue, holding a **Create** machine husk (`andesite_casing`
  gearbox → cogwheel + shaft → a `mechanical_press`) beside the **arcane** half (an enchanting table ringed with amethyst
  and candles, a bookshelf study, a brewing nook). Create machinery via the `modNames` side-map; Iron's contributes the two
  mages (`archevoker` + `magehunter_vindicator`) and loot, so the vanilla arcane fittings are the gametest shell. **D4
  compounding gate:** Create loot tops out at `andesite_alloy` (husk press, no working contraption); Iron's loot is
  `arcane_essence` + an uninscribed `blank_rune` (inert without a gated Upgrade Orb) — never a spellbook/scroll/Orb. Wired
  w1 across all three Rocky tiers; a new gametest per mirror. Both nodes green (1.21.1: 205, 26.1.2: 207).

## [0.215.0] - 2026-07-05

### Added
- **Structure Variety Band 3 begins — the Ruined Chapel (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B25), plus the last theme migration.** An all-vanilla **epic** (w1) showpiece: a stone-brick chapel fallen to ruin —
  a caved nave open to the sky, arched shattered windows, a tall front bell-cote holding a hanging **bell**, and a chiseled
  chancel altar lit by a soul lantern behind a broken rose window with the reliquary chest (emeralds, an enchanted book, the
  odd diamond / golden apple). Wired **w1** across all three tiers of Meadow + Rocky, and — as the shared reward-bearing
  `explorable` Explore-floor — the thin Lush + Mushroom themes. **Finished the weighted-gate migration (D5):** `huge_lush`
  and `huge_mushroom` (the last two legacy overworld themes) were migrated to `rare_structure_chance: 0.05` + weighted
  entries; every overworld theme now runs the flat-5% weighted gate (only the Explore seed + Nether/End stay legacy by
  design). A new assembly gametest per mirror. Both nodes green (1.21.1: 204, 26.1.2: 206).

## [0.214.0] - 2026-07-05

### Added
- **Item-icon audit + full redraw pass (→ [ICONAUDITPLAN.md](ICONAUDITPLAN.md)).** Reviewed all 93 item textures and
  rebuilt the outliers so every icon reads at 16×16 and stays inside the family's visual language (silhouette = size
  tier, colour = theme). New dedicated **Large Forest** art (`island_seed_forest_large`, its model repointed off the
  shared generic disc). 30 textures redrawn/tweaked + 1 new + 1 model repoint.
- **Crash-safe island grows — persist/resume + force-load reconciliation (→ [CRASHRESUMEPLAN.md](CRASHRESUMEPLAN.md)).**
  In-progress island grows now survive a hard crash: a new `PendingIsland` descriptor (re-plan inputs + progress) is
  written at germination (`IslandSeedEntity`) and for twins (`TwinPlacer`), persisted every tick by `GenerationJob`, and
  re-enqueued at its saved progress on server start by the new `CrashRecovery` handler (`GenerationJob.resume`).
  Force-loaded chunks are recorded in `SkyseedWorldData` at the ref-count transitions and any leftovers are un-forced on
  start, so a crash mid-grow no longer leaks a permanent `/forceload`. Both persistence paths (1.21.1 NBT + 26.1.2 Codec)
  carry the new optional collections, guarded by a `crashResumeStateRoundTrips` gametest in both suites. *(In-game
  hard-crash sign-off still pending.)*

### Changed
- **Icon consistency pass.** Farm seeds (stable/pasture/poultry/wool_farm) given distinct signatures; `nether_lava_large`
  regains its lava glow; `nether_forest`/`nether_rocky` pulled apart (teal-fleck crimson vs brick + quartz); structure
  seeds (hamlet, woodland_mansion, bastion, ocean_monument) unified onto the "island + structure silhouette" convention;
  Explore & Wild now encode tier by shape (ball → disc → cone) instead of a colour ring; lush/meadow separated; ancient
  contrast lifted; the four End-Portal Edges restyled as portal-frame blocks with a green ender-eye + per-variant gem.
- **Temple traps are opt-in.** Jigsaw structures only place trap blocks when the theme entry sets `"traps": true`
  (`JigsawConfig`); enabled on the desert-temple, jungle-temple and badlands chambers so traps stop leaking into reused
  pools that shouldn't have them.
- **Questline starter-seed icons reverted Wild → Forest** in the introduction and skyseed chapters.

### Fixed
- **Crash-robustness review — 7 findings + observability (#67).** Both `MobPlanner` inert-safety fixes, the `traps`
  opt-in gate, per-candidate `findClearSpot` re-validation, a seed-derived `StartIsland` oak, the double dimension-reset
  backup guard, and a concurrent force-load ref-count (two islands sharing a chunk column no longer un-force each
  other's chunks). Adds a shutdown drain-cap warning and force-load acquire/release logging. Both nodes green
  (26.1.2: 204/204).

## [0.213.0] - 2026-07-05

### Added
- **Structure Variety Band 2 COMPLETE — the Farmer's Delight batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B24).** The last single-mod Band 2 build — a `requires`-gated rare surprise building that germinates on an ordinary
  island only when **Farmer's Delight** is installed: the **Overgrown Cook's Homestead** (Forest / Meadow, no mobs) — a
  kitchen cottage abandoned to the weeds, with a 5×5 log-and-cobble cabin, a caved roof, a breached wall where a cold
  campfire hearth vents up an external cobblestone chimney jutting broken above the ridge, the Farmer's Delight kitchen
  inside (a cold stove + cooking pot, skillet, cutting board, cabinets, the scrap chest), and a weed-choked front garden of
  FD crops on vanilla farmland with a rotting stack of produce crates. Vanilla shell with modded fittings (authored via the
  `modNames` side-map over `SMOKER`/`STONE`/`WHEAT` analogs, so they resolve to air without the mod and the vanilla shell is
  the gametest anchor). Loot is flavour-only (FD has no progression gate): a vanilla larder-scrap chest + two inert
  `add_drop` GLMs layering a little raw produce (onion) and rope. Wired **w3** across all three tiers of Forest + Meadow; an
  adversarial review before the regen fixed two corner-post overwrites. Both nodes green (1.21.1: 200, 26.1.2: 202).
  **Band 2 is now complete (B13–B24)** — eleven mod-gated rare surprise buildings across Create, Immersive Engineering,
  Applied Energistics 2, Iron's Spells, Mystical Agriculture and Farmer's Delight, each inert without its mod.

## [0.212.0] - 2026-07-05

### Added
- **Structure Variety Band 2 — the Mystical Agriculture batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B23).** One more `requires`-gated rare surprise building that germinates on an ordinary island only when **Mystical
  Agriculture** is installed: the **Abandoned Inferium Plot** (Meadow / Hamlet) — an essence farm gone to seed, with an
  irregular tilled bed of inferium farmland (rows trampled to coarse dirt, several bare) around a water-cauldron trough, a
  scatter of tier-1 essence crops, a lop-sided carved-pumpkin scarecrow, a caved tool lean-to over the scrap chest, a
  toppled inferium growth accelerator, hay straw and a broken oak-fence perimeter with a swung-open gate. Mostly-vanilla:
  only the farmland/crops/accelerator are modded (authored via the `modNames` side-map over `FARMLAND`/`WHEAT`/`STONE`
  analogs, so they resolve to air without the mod and the vanilla shell is the gametest anchor). Loot is gate-safe (D4/§5):
  a vanilla farm-scrap chest + two inert `add_drop` GLMs layering a little tier-1 Inferium Essence / Prosperity Shard, never
  a seed, a higher-tier essence or a prosperity/infusion component. Wired **w3** across all three tiers of Meadow + Hamlet;
  an adversarial review before the regen fixed three placement bugs. Both nodes green (1.21.1: 199, 26.1.2: 201).

## [0.211.0] - 2026-07-05

### Added
- **Structure Variety Band 2 — the Iron's Spells & Spellbooks batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B20–B22).** Three more `requires`-gated rare surprise buildings that germinate on an ordinary island only when
  **Iron's Spells** is installed: a **Small Wizard's Hut** (Forest / Meadow) under a pointed witch-hat spire, a two-storey
  **Wizard's Tower** (Rocky / Ancient) with a laddered shaft, arrow-slits and a jutting balcony, and a **Cursed Obelisk**
  (Ancient / Badlands) — a tapering blackstone/deepslate spire over a soul-lantern altar with a wither rose and a carved
  necromancer alcove. All three are **all-vanilla** architecture (Iron's is a mob/item mod, so there is no `modNames`
  side-map — the flavour is the `requires: ["irons_spellbooks"]` gate, the mage `mobs` pack, and the loot). Loot is
  gate-safe (D4/§5): a vanilla arcane-scrap chest + two inert `add_drop` GLMs layering a token Arcane Essence / Common Ink,
  never an Upgrade Orb or named scroll. Wired **w3** across all three tiers of Forest+Meadow (Hut), Rocky+Ancient (Tower),
  Ancient+Badlands (Obelisk); an adversarial review before the regen fixed three placement bugs. Both nodes green
  (1.21.1: 198, 26.1.2: 200).

## [0.210.0] - 2026-07-05

### Added
- **Structure Variety Band 2 — the Applied Energistics 2 batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B19).** The AE2-gated **Gutted AE2 Lab** (Rocky/Ancient) ships to this node too (shared authoring code + the
  1.21.1-authored `.nbt` via DataFixerUpper). Germinates only with Applied Energistics 2 (`requires: ["ae2"]`, filtered
  before any RNG) and is inert-safe without it: the `ae2:` certus/fluix quartz blocks resolve to air, the vanilla ruin
  shell + gate-safe scrap loot load clean. **D4-safe** — no sky stone / controller / press (the meteorite-island gate).
  Wired w3 across all three tiers of the two families. One new assembly gametest on this suite mirror. Gametests green (197).

## [0.209.0] - 2026-07-05

### Added
- **Structure Variety Band 2 — the Immersive Engineering batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B17–B18).** The two IE-gated rare buildings — **Dilapidated IE Factory** (Rocky/Badlands) and **Fallen Powerline**
  (Meadow/Desert) — ship to this node too (shared authoring code + the 1.21.1-authored `.nbt` via DataFixerUpper). Each
  germinates only with Immersive Engineering (`requires: ["immersiveengineering"]`, filtered before any RNG) and is
  inert-safe without it: the `immersiveengineering:` machinery resolves to air, the vanilla ruin shell + gate-safe scrap
  loot load clean. Wired w3 across all three tiers of the four families. Two new assembly gametests on this suite mirror.
  Gametests green (196).

## [0.208.0] - 2026-07-04

### Added
- **Structure Variety Band 2 — the Create batch (→ [VARIETYSTRUCTUREPLAN.md](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md)
  §3 B13–B16).** The four Create-gated rare surprise buildings — **Broken Windmill**, **Derelict Watermill**, **Abandoned
  Train Shed**, **Rusted Drill Rig** — ship to this node too: shared authoring code + the 1.21.1-authored structure `.nbt`
  loaded here via DataFixerUpper. Each germinates only when Create is installed (`requires: ["create"]`, filtered before
  any RNG) and is inert-safe without it — the `create:` machinery resolves to air and the gate-safe scrap loot loads
  clean. Wired at weight 3 across all three tiers of Meadow/Aquatic/Rocky/Badlands/Desert + the Hamlet windmill. Five new
  gametests on this suite mirror (four on-pad assembly tests + a `requires`-gate inert test). Gametests green (194).

## [0.207.0] - 2026-07-04

### Added
- **Iron's Spells exploration pillar — the mod side (→ [IRONSCONTENTGAPPLAN.md](Modpack-growyourownworld/IRONSCONTENTGAPPLAN.md)).**
  The "Explore" seed tiers + `explore` theme, five home-curated structure rebuilds (Frozen Warren, War Barrow, Citadel,
  Impaled Boat, and the new grand **Catacombs** — a descending deepslate crypt that restores the **Dead King** boss via
  a baked dormant `dead_king_corpse` + `catacombs_zombie` spawners), **mithril ore** on the Rocky/Ancient families
  (`deep_core`), the missing casters/bosses homed onto structure mob-packs, and the Iron's arcane-essence/upgrade-orb
  loot modifiers all ship to this node (shared code + the 1.21.1-authored structure `.nbt` loaded here via DFU). All
  inert-safe — the crypt's baked modded entities simply skip without the mod. Gametests green (177). (The modpack-side
  content — jars, quests, configs — is 1.21.1-only; see [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md).)

### Changed
- **Badlands `wooded` variant now reads badlands-first** — a coarse-dirt cap with sparse dark oaks instead of a grass
  forest, and rarer, across all three badlands tiers.

## [0.206.0] - 2026-07-04

### Added
- **Meteorite island family + Meteorite Core (the mod side of the Applied Energistics 2 integration, CONTENTPLAN #18 →
  [AE2PLAN.md](Modpack-growyourownworld/AE2PLAN.md)).** The `meteorite` / `meteorite_large` / `huge_meteorite` themes
  (a crater + sky-stone globe + a tiered **Meteorite Core** press source — skyseed's first custom block), the
  AE2-compat layer (seed + jar recipe present only with AE2), the 1 % wild-meteor `wildMeteorChance` common config, and
  the tiered/iron-tier press loot all ship to this node too. Gametests green (165). (The modpack-side content — jars,
  quests, KubeJS — is 1.21.1-only; see [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md).)

## [0.203.0] - 2026-07-03

### Changed
- **Trial chamber phase-2 rollout — the whole warren now shares the atrium's look (#61).** Applied the framed-panel
  MOSAIC wall texturer + laid-floor tiler to every remaining piece (rooms, small cell, corridor, corner, junction,
  descent, end room): tuff-brick-framed copper panels (warm + oxidized, chiseled/grate motifs), cut-copper corner posts,
  studded laid floors/ceilings — replacing the old per-block speckle. The cell gains the cornice + greebling; the
  descent's treads are laid floor over a plain substructure. Regenerated all 11 non-hub trial `.nbt` on 1.21.1, loaded
  here via DFU; a gametest now asserts the end room carries the mosaic. Both nodes green (154 / 163).

## [0.202.0] - 2026-07-03

### Changed
- **Ore now scales to island size at runtime — uniformly across every size, Y-band, and theme.** `OrePlanner` multiplies
  vein count by the island's core volume vs a baseline (~0.7× density on the biggest tier); Y-bands scale automatically
  (a band changes the shape/volume). The extra veins use a separate deterministic RNG, so normal-size islands stay
  byte-identical and downstream rolls (lava/decoration) are unaffected. All the per-tier hand-scaled counts (rocky/ancient
  large+huge + Create/MA/Quark compat) collapsed back to one base-density baseline. Both nodes green.

## [0.201.0] - 2026-07-03

### Fixed
- **Trial chamber — atrium/descent fixes (TRIALCHAMBERPLAN #61):** atrium dais back to a 2-block raise (exit sits under
  the ridge, not blocked by it); cornice/ridge corners are a clean top slab (stairs can't hook at an inner corner); the
  descent staircase is roomier and now draws the rooms pool, so a staircase down always lands in a trial room. Hub +
  descent `.nbt` regenerated on 1.21.1, loaded here via DFU; both nodes green.

## [0.200.0] - 2026-07-02

### Fixed
- **Mining islands now reliably carry their staple ores.** Each ore's `chance` is rolled once per island, so a fail
  dropped it entirely — the rocky/ancient staples were gated (coal 0.5–0.7 = often absent). Raised iron/copper/coal to
  always-present and gold to 0.55 (still deep) across all rocky/ancient tiers, so a stone island always has iron/coal
  like vanilla. Data-only, shared with 1.21.1; both nodes green. *(Deep/high/snowy Y-band overrides still use smaller
  counts — a follow-up.)*

## [0.199.0] - 2026-07-02

### Fixed
- **Trial atrium geometry fixes (TRIALCHAMBERPLAN #61):** the dais step is now a proper 3-step staircase facing the
  right way (south), the ladder moved off the corner onto the −X wall, and the cornice/ridge stairs face outward. Hub
  `.nbt` regenerated on 1.21.1, loaded here via DFU; both nodes green.

## [0.198.0] - 2026-07-02

### Changed
- **Trial atrium reshaped — rectangular 11×13 with a raised back dais + stair step (two floor levels), an elevated
  passage exit, warmer non-weathered copper in the wall mosaic, and a mid-wall ridge breaking up the tall walls
  (TRIALCHAMBERPLAN #61).** Hub `.nbt` regenerated on 1.21.1, loaded here via DFU; both nodes green.

## [0.197.0] - 2026-07-02

### Changed
- **Compat ore overrides now scale with island size too** (clears the v0.195.0 caveat): Create zinc, MA
  inferium/prosperity, and Quark stones on the large/huge rocky/ancient/lush tiers scaled by the same per-tier factors
  as the vanilla ores (huge ~0.7× normal density). MA nether-soul left as-is (tiny Nether islands). Data-only, shared
  with 1.21.1; both nodes green.

## [0.196.0] - 2026-07-02

### Changed
- **Trial atrium walls are now a decorated framed-panel MOSAIC, not random speckle (TRIALCHAMBERPLAN #61, phase 1).**
  A position-aware texturer replaces the per-block `mix()` hash: tuff-brick frames grid each wall into panels, each a
  patch of one copper patina with a chiseled/grate motif centre + cut-copper corner posts + a laid floor tile — the
  vanilla decorated-temple read. Atrium first (showcase); rolls out to the warren next. Hub `.nbt` regenerated (1.21.1),
  loaded here via DFU; both nodes green.

## [0.195.0] - 2026-07-02

### Changed
- **Ore scales with island size on the mining islands now** — a huge island had ~18× lower ore-by-volume than a normal
  one (ore `count` is fixed per theme and never grew with the ~72× volume). Rescaled `rocky_large`/`huge_rocky`/
  `ancient_large`/`huge_ancient` ore tables to core volume via bigger + more veins (large ≈ normal density, huge ≈ 0.7×).
  Data-only, shared with 1.21.1; both nodes green. *(Compat ore overrides — Create/MA/Quark — still under-scale on huge;
  a follow-up.)*

## [0.194.0] - 2026-07-02

### Changed
- **Trial chamber is now a corridor WARREN, not chambers bolted onto the atrium (plannednotes #61).** The jigsaw flow
  now reads like a real (smaller) vanilla trial dungeon: atrium edges open onto **passages** (new `halls` pool) that
  wind (`corridor`/`corner`) and branch at T-`junction`s, with spawner/treasure **chambers on junction spurs** (`rooms`
  pool) + a small 5×5 `cell` for size variety; the `descent` staircase drops the warren a storey. `gallery` retired,
  jigsaw `depth` 3→5 (sprawl capped to the existing island). NBTs regenerated on 1.21.1, loaded here via DFU; gametest
  `trialWarrenWiring` guards the flow; both nodes green.

## [0.193.0] - 2026-07-02

### Changed
- **More Quark stone on the mining islands (QUARKISLANDPLAN #71).** Limestone made abundant (Rocky chance 0.55→0.95,
  much bigger/more veins) + jasper/shale bumped on Rocky/Ancient across base/large/huge; corundum stays rare. Data-only,
  shared with 1.21.1; all six overrides updated together (base→large→huge mirror rule), gametest-guarded per tier.

## [0.192.0] - 2026-07-02

### Fixed
- **Quark island stones now appear at every throw depth (QUARKISLANDPLAN #71).** They were only on the top-level ore
  list, which a matched Y-band override replaces, so Quark stones only generated at Rocky Y 8–70 / Ancient Y 20–96.
  Added same-selector band patches across the six `quark_{rocky,ancient}{,_large,_huge}.json` overrides so the veins
  merge into every overworld Y-band (mirroring `create_rocky.json`'s deepslate-zinc patch). Data-only, shared with the
  1.21.1 build; ids verified against `Quark-4.1-481.jar`; gametest `quarkStonesReachYBands` guards it.

## [0.191.0] - 2026-07-02

### Added
- **Trial chamber greebling — the vanilla atmosphere layer (plannednotes #25, phase 3).** The atrium and chambers now
  carry deterministic set-dressing — **cobwebs** in ceiling corners, **lit candles** + **decorated pots** on the floor, a
  brushable **suspicious gravel** dig (`archaeology/trial_chambers` loot) and a **moss** patch — placed via
  `TrialChamberTemplates.greeble()` in `hub`/`room`/`end`, guarded so it can never bury a spawner/vault/lamp/ladder/
  connector/cornice. The hub gametest also asserts a decorated pot. NBTs regenerated on 1.21.1, loaded here via
  DataFixerUpper; both nodes green.

## [0.190.0] - 2026-07-02

### Added
- **Trial chamber is now multi-story, leaning downward (plannednotes #24, phase 2).** New **`descent`** staircase
  corridor whose exit (`chamber_edge`, redrawing the rooms pool) sits a storey (5 blocks) below its entrance
  (`room_door`) so the jigsaw seats the next piece a level down — its box sits beyond the parent's footprint so it never
  overlaps (STRUCTUREPLAN #28) — and a terminal **`end`** chamber capped with an **ominous vault** + breeze spawner.
  Both added to the `trial_chamber/rooms` pool; gametests `trialDescentDropsALevel` + `trialEndRoomHasOminousVault` guard
  them (NBTs regenerated on 1.21.1, loaded here via DataFixerUpper). *(Phase 3 greebling to follow; in-world read yours.)*

## [0.189.0] - 2026-07-02

### Changed
- **Trial chamber — vanilla aged-copper look, bigger & on a deeper island (plannednotes #24, phase 1 of the redesign).**
  `TrialChamberTemplates.mix()` is now tuff-brick dominant with the **waxed weathered/oxidized copper** family (copper +
  cut + chiseled + grate) for the signature green-teal patina; the hanging lanterns become lit **waxed copper bulbs on
  iron chains** (`iron_chain` on this node) with a cut-copper stair **cornice**; the hub is a 9×9 two-storey **atrium**
  (was 7×7), rooms are 7×7, the corridor 5×7. The purpose-built trial island grew to radius 24–30 with a deep uncapped
  underside + `sink`/`pad` 10/14 so the taller chamber fits under the surface. NBTs regenerated on the 1.21.1 node
  (loaded here via DataFixerUpper — incl. the `chain`→`iron_chain` rename); gametests green (atrium boss/vault +
  copper-bulb). *(Phase 2 multi-story descent + phase 3 greebling to follow; in-world vanilla-compare is the user's.)*

## [0.188.0] - 2026-07-02

### Added
- **Cypress swampland villages grow as a stilted overwater bayou (BWGSWAMPVILLAGEPLAN #73).** The three cypress village
  bands carve a broad **island-filling** shallow swamp (the #64 `pond` lever, `extent 0.9`) on a `mud` surface and stand
  the village on stilts + plank boardwalks over the open water. A stilted build **skips the levelled pad** (no dry
  footing disc displaces the water) and lifts the whole village by a data-driven **`stilt_height`** (`JigsawConfig`;
  cypress bands use 2 — the in-game floor-clearance knob). The pond is **`contained`** (new `Pond` flag) so it walls its
  un-backed rim like a river, staying hemmed in bar a few deliberate waterfalls. The **village-center island is huge-sized**
  (its own `shape`: radius 24–30, `top_dome` 2–3, `max_under_depth` 18; pond `radius` 40 so `extent` governs) instead of
  the base 3-island cluster. Engine in `PathSurfacer`: **`supportStilts`** hangs willow legs from a floor down *through
  the water* to the bed (never over pure void — no dangling stub / reach to another island); **`boardwalk`** lays a plank
  deck on sparse pier posts, **railed only along open-water edges** (never a lane tile or a solid block, so a rail can't
  overwrite a house or fence a road); and every resolved path tile has a stray tree trunk/canopy stripped above it
  (`clearCanopyAbove`) so **no village gets trees growing on its roads**. Legs/decks/rails use willow (oak fallback,
  BWG-free CI); a stilted island also clears its surface decoration. Gametests
  `pathSurfacerStiltsDescendThroughWater` + `pathSurfacerBoardwalksOverWater` (+ the resolve test) guard it. The other
  five styles are untouched (bar the shared tree-on-road fix); inert without BWG. *(Over-water look + exact
  `stilt_height`/`extent` are an in-world tuning pass.)*

## [0.187.0] - 2026-07-02

### Fixed
- **Village front doors now sit flush with the outside wall, not recessed inward (BWGVILLAGEPLAN #72).** A closed door
  with `FACING=NORTH` on a `z=0` front wall renders on the interior (`+Z`) edge of its cell, leaving a street-side
  recess — the "doors face inward" read from the Phase-5 sign-off. Flipped the front-door `FACING` `NORTH → SOUTH` (panel
  now on the exterior `−Z` edge) across the shared pattern: `BwgVillageTemplates` (`door()` + porch, all six BWG styles),
  `TradePostTemplates` (shop shell / great hall / blacksmith, all four variants), `VillageCenterTemplates` (four plaza
  halls) and `RareStructureTemplates` (evoker cell). `HamletTemplates` was already correct (it chooses the facing by
  wall) and untouched. The 163 affected door-bearing `.nbt` were regenerated on the 1.21.1 node (shared source; loaded
  here via DataFixerUpper); all 158 gametests green on this node, 149 on 1.21.1. *(In-world visual sign-off still yours.)*

## [0.186.0] - 2026-07-02

### Changed
- **Wet-wood islands: broad shallow swamp/marsh instead of a deep round pond (BWGPLAN #64).** The four AQUATIC wet-wood
  bands on all three tiers now carve a shallow (`depth: 2`), `slope`d marsh with a raised `extent` (0.6/0.62/0.68) so
  water sheets across the island (the Huge tier read as "not enough water"). Extent kept under 0.7 to leave a wooded rim.
- **Wet/semi forest biomes read as real forests (BWGPLAN #22).** Primary tree `tries` for the held wet/semi Forest bands
  (`flower_forest`, `cherry_grove`, `grove`, `mangrove_swamp`, `swamp`, `#is_river`, `mushroom_fields`, `bamboo_jungle`)
  lifted to the canonical per-tier forest density — **7 / 40 / 120** (base/large/huge) — matching the v0.170.0 pure-forest
  pass. Secondary trees / mushrooms / spacing / ponds / ground flora unchanged; genuinely open biomes stay scattered.

### Fixed
- **Wet-wood zero-tree floor (BWGPLAN #65).** `forceOneTree` now grades a real planting clearing (a 5×5 dirt pad under a
  tall air column) before its last-resort placement, giving the big NBT trees BWG uses (willow, cypress) room to grow —
  they were silently failing to fit and leaving small/huge wet-wood islands bare. Runs only when every normal site
  failed. Base-tier wet-wood tree `tries` lifted 4 → 6. Gametest `biomeswevegoneWetWoodPondsAreShallowMarshes` guards the
  shallow-marsh config; the tree floor still wants an in-game re-throw to confirm.

### Modpack (Grow-your-own-world)
- **Quest B602 "Prosperity Found" refreshed for the Lush ore source (MYSTICALPLAN #69)** — leads with the accessible Lush
  stone ores (v0.172.0), Ancient framed as the richer deepslate option.

## [0.185.0] - 2026-07-02

### Added
- **Quark island integration Phases 2 & 3 + myalite End (QUARKISLANDPLAN #71) — completes the Quark island work.**
  Blossom trees + saplings as a merged accent variant on the Forest tiers' snowy/swamp/savanna/plains/badlands bands
  (`quark_forest{,_large,_huge}.json`); Ancient Tomes loot extended to the Trial Chamber + Nether-fortress chests
  (config); myalite veins the rocky/ancient End form. Extras only, inert without Quark. Gametests
  `quarkBlossomBandsMergeOntoForestTiers` / `quarkMyaliteReachesEndForm`.

## [0.184.0] - 2026-07-02

### Added
- **Quark materials on the Rocky & Ancient mining islands (QUARKISLANDPLAN #71, Phase 1).** `theme_override`s add
  limestone/jasper (Rocky) and jasper/shale (Ancient) veins + a deep blue-corundum geode across base/large/huge tiers.
  Extras only, no seed; inert without Quark. Gametests `quarkStonesCompatTargetsRocky` / `quarkStonesCompatTargetsAncient`.

### Fixed
- **Exotic Woods guide: "Eleven wood families" → twenty** (`exotic_biomes.json`), matching the current Forest-seed set.

## [0.183.0] - 2026-07-02

### Fixed
- **The void-death Totem shrine only ADDS blocks now — never overwrites an island** (fell-straight-down-through-an-island
  case). Shrine fills air cells only; the totem rises to the first clear cell so it's never embedded. Shared with the
  1.21.1 build.

## [0.182.0] - 2026-07-02

### Added
- **Quark's Totem of Holding now works in the void (QUARKPLAN).** A void death used to drop the totem at the bottom of
  the world, unreachable without flight. A freshly-spawned `quark:totem` below y50 now keeps its x/z, is raised to the
  island band, and gets a small **lit shrine** built under it (3×3 stone-brick pad, glowing centre, four soul-lantern
  posts) — bridge-reachable and beacon-visible. Matched by entity id (no-ops without Quark Oddities); Skyseed worlds only.
  Shared handler with the 1.21.1 build.

## [0.181.0] - 2026-07-01

### Added
- **Every villager profession is now obtainable in village islands (vanilla + all six BWG styles).** Added the four
  missing shops — armorer/cleric/weaponsmith/leatherworker — so with the forge (toolsmith) all 13 professions can
  appear. Animal-pen trough → water basin (frees the cauldron for the leatherworker). Gametest
  `village_offers_every_profession`.

### Fixed
- **Tower-house loft ladder no longer breaks on glass** (back-wall column made solid + a mid-floor shaft hole); the
  same fix applied to the vanilla Hamlet cottage. **Smithy less cramped** — the smithing table moved out of the bed's
  column (BWG forge + vanilla blacksmith). Weaponsmith grindstone placed floor-attached.

## [0.180.0] - 2026-07-01

### Fixed
- **BWG village shrine hall (the "temple") no longer has a freestanding door in its open colonnade aisle.** Shared with
  the 1.21.1 build; the six `shrine_hall.nbt` were regenerated.

## [0.179.0] - 2026-07-01

### Added
- **BWG villages — a variety pass across all six styles (BWGVILLAGEPLAN Phase 4).** Shared with the 1.21.1 build: two
  new house shapes (porch cottage + longhouse), a HIP roof option on some shops, and three new decoration plots
  (animal pen, market stall, and a signature-flora grove), all palette-driven into the `fillers` pool. Gametest
  `bwg_village_decor_variety` on this node too.

## [0.178.0] - 2026-07-01

### Added
- **BWG villages — the Hamlet and Village-Center tiers, all six styles (BWGVILLAGEPLAN Phase 3).** Shared with the
  1.21.1 build: Hamlet + Village-Center seeds over a BWG village biome grow that style's hub / dense cluster village in
  its BWG blocks, reusing the per-style piece sets (no new templates). Wired via `biomeswevegone_hamlet.json` +
  `biomeswevegone_village_center.json` + per-style `hamlet_start` pools. Gametests `bwg_hamlet_and_center_bands_wired`
  + `bwg_hamlet_and_center_assemble` on this node too. Inert without BWG.

## [0.177.0] - 2026-07-01

### Added
- **BWG villages — all six styles at the Trade Post tier (BWGVILLAGEPLAN Phase 2).** Shared with the 1.21.1 build:
  Forgotten / Pumpkin Patch / Red Rock / Salem / Swamp join the Skyris pilot, each a full per-style piece set in that
  biome's real BWG 2.6.0 blocks (the hermetic mod-id engine mixes vanilla + BWG per cell), wired via
  `biomeswevegone_trade_post.json` bands. `Style` gained `bookshelf` + `flora` slots. Gametests
  `bwg_all_village_bands_wired_on_trade_post` + `bwg_village_styles_assemble` run on this node too. Inert without BWG.

## [0.176.0] - 2026-07-01

### Added
- **BWG villages, pilot: the Skyris style (BWGVILLAGEPLAN).** Shared with the 1.21.1 build: a Trade Post grown over
  `biomeswevegone:skyris_vale` assembles a Skyris-styled village (Skyseed's own jigsaw village mechanics in BWG's
  Skyris block palette). New `BwgVillageTemplates` authors the piece set; `StructureWriter` gained a `modNames`
  overload that writes `biomeswevegone:` block ids via a vanilla-analog state so the `.nbt` are authored with no BWG
  on the classpath. Inert without BWG. Gametests `bwg_skyris_village_band_on_trade_post` +
  `bwg_skyris_village_assembles` run on this node too. The remaining five styles + the Hamlet/Village-Center tiers
  follow.

## [0.175.0] - 2026-07-01

### Added
- **A light BWG-flower sprinkle on the exotic-wood Forest islands.** Shared datapack with the 1.21.1 build: the
  Forest-family BWG bands (`biomeswevegone_forest.json` + `_large`/`huge_`) gain a few-% ground-cover sprinkle of
  each biome's own signature BWG flower (orange daisy / iris / guzmania / rose / anemones / kovan flower / japanese
  orchid / california poppy / fairy slipper / foxglove / white sage / black rose / delphinium / protea, etc.) for
  colour — trees stay the focus. The two vanilla placeholders became authentic BWG blooms (enchanted → fairy
  slipper + cyan rose; florus → pink daffodil + angelica). Every flower verified as a real BWG 2.6.0 block; inert
  without BWG (`Lookup.hasBlock`). Ground flora is per-column, so the 3 tier files share identical bands.

## [0.174.0] - 2026-07-01

### Added
- **Millable BWG flowers now grow on islands (backlog #9).** Shared datapack with the 1.21.1 build: two new
  `theme_override` families place create-otbwg-millable BWG flowers as island ground cover — **Meadow**
  (`biomeswevegone_meadow.json` + `_large`/`huge_`, 8 floral-grassland biomes: alliums/amaranths/roses/tulips/
  anemones/daffodils/sages/poppy) and **Lush** (`biomeswevegone_lush.json` + `_large`/`huge_`, 3 jungle biomes:
  begonia/bistort/guzmania/incan-lily/lazarus-bellflower/richea/delphinium/protea). Deliberate Q2 multi-seed overlap:
  `tropical_rainforest`/`fragment_jungle` are trees-first on Forest, flora-first here. Every flower verified as a real
  BWG 2.6.0 block AND a `create-otbwg-compat-1.0` milling input; inert without BWG. Ground flora is per-column, so the
  3 tier files per family share identical bands. Mirrored `biomeswevegone_compat_places_meadow_flowers` +
  `_places_lush_flowers` gametests into the 26.1.2 native suite.

## [0.173.0] - 2026-07-01

### Added
- **Every BWG plank obtainable — last 5 growable woods shipped (backlog #63).** Shared datapack with the 1.21.1 build:
  new Forest-family bands (`biomeswevegone_forest.json` + `_large`/`_huge`, inert without BWG) for **florus**
  (`forgotten_forest` → `florus_trees`), **holly** (`dacite_ridges` → `holly_trees`), **pine** (`black_forest` →
  `pine_tree1` + `pine_tree2`, no aggregate exists), **mahogany** (`tropical_rainforest` → `mahogany_trees`) and
  **rainbow_eucalyptus** (`fragment_jungle` → `rainbow_eucalyptus_trees`) — each on its dedicated feature. Ids verified
  against BWG 2.6.0. `#skyseed:exotic_woods` reveal tag extended. Mirrored the extended
  `biomeswevegone_compat_prepends_forest_bands` assertions into the 26.1.2 native suite.

### Notes
- **fir intentionally non-growable** (BWG 2.6.0 has `fir_planks` but no configured fir tree feature) — excluded from the
  bands + tag; a gametest guards that no band references a `fir_*` feature. 24/25 planks are island-obtainable.
- **Spirit-band failure (#66) diagnosed, no change needed** — a matched `pale_bog` band replaces variants (emits only
  `spirit_trees`, never oak/birch), so the reported oak/birch result means the seed wasn't over `pale_bog` (a
  re-test/reachability item). Biome + feature both exist; spirit uses the same NBT feature type as the working siblings.

## [0.172.0] - 2026-07-01

### Added
- **Mystical Agriculture ore on the Lush island.** Shared datapack with the 1.21.1 build: new `mysticalagriculture_lush.json`
  (+ `_large`/`_huge`, inert without MA) adds the **stone** `inferium_ore`/`prosperity_ore` to the Lush stone core — the
  accessible bootstrap source MYSTICALPLAN intended, pairing with the **deepslate** variants that already ship on Ancient.
  Off-dimension forms stay clean (overworld-only theme → neutral empty ores in End/Nether). Mirrored
  `mystical_agriculture_compat_targets_lush` gametest added to the 26.1.2 native suite.

## [0.171.0] - 2026-07-01

### Added
- **BWG wet-woods + fantasy-woods islands finalized (ids verified against BWG 2.6.0).** Shared datapack with the 1.21.1
  build: the wet-woods bands on the **Aquatic** family (water-first: cypress, willow, white-mangrove, palm) and the
  fantasy-woods bands on the **Forest** family (trees-first: enchanted, skyris, spirit + a cypress multi-seed demo) ship
  for real, every `biomeswevegone:` id confirmed against `Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar`. Inert without BWG.

### Fixed
- **Corrected guessed BWG ids** (drafted before a jar was available): willow → `bayou` biome / `bayou_trees` feature
  (no `willow_trees` exists); white-mangrove → `white_mangrove_marshes` (not `pale_bog`); spirit is growable via
  `pale_bog` (no `spirit_woods` biome). And the `#skyseed:exotic_woods` reveal tag: dropped the non-existent
  `#biomeswevegone:planks`, `white_sakura_planks` → `sakura_planks`, `enchanted_planks` → `blue_enchanted_planks` +
  `green_enchanted_planks`.

### Tests
- Mirrored into the 26.1.2 native suite: new `biomeswevegone_compat_prepends_aquatic_bands` plus the extended
  forest-bands test that locks in the corrected ids.

## [0.170.0] - 2026-06-30

### Added
- **First-party Oh The Biomes We've Gone compat (ships with Skyseed, inert without BWG).** A `theme_override` adapts the
  **Forest** island (+ large/huge) to **11 BWG wood biomes** (aspen, baobab, cika, jacaranda, maple, ebony, redwood,
  zelkova, witch-hazel, sakura, ironwood) so a Forest seed thrown over one grows that biome's BWG trees. Inert without
  BWG (unknown ids never match) — byte-identical generation.
- **Optional / random water features (`pond.chance` + `pond.river`)** — a pond can be carved only `chance` of the time
  and, when it is, be a 50/50 pick between the pool and a `river`. The Huge Forest uses it for 25% lake / 25% river /
  50% dry. Plain ponds (chance 1, no river) are unchanged and consume no extra RNG.
- **Rivers are walled in and never sheer** — the planned river-to-rim follow-up: a river's banks always soften, and where
  it meets the island edge it is walled into a contained channel with only ~1-in-4 coarse rim stretches left open as
  deliberate waterfalls. Every river (forest / aquatic / huge forest); pond carving unchanged.

### Changed
- **`theme_override` biome bands now take precedence over the base theme's bands (prepend, not append).** Shared with the
  1.21.1 build: a patch band whose selector matches no base band is prepended so it wins the first-match over a base
  theme's vanilla `#is_*` catch-alls (BWG's biomes are transitively under `#is_forest` via `#biomeswevegone:forest`).
- **Auto debug seeds now cover `theme_override` biome bands** (`ThemeScanner` scans `theme_override/` too) — so the BWG
  wood biomes get debug seeds attributed to their `target` theme.
- **Denser forests on the large/huge tiers** — forest-character biomes (vanilla `#is_forest`/dark/birch/taiga/jungle +
  the BWG wood biomes) get much higher tree `tries` on the large/huge Forest islands; open biomes (plains/savanna/beach/
  desert) keep their scattered counts, and the BWG bands now scale per tier.
- **Rounded banks on the deep lakes** — the deep (depth ≥ 4) water pools (Huge Forest lake, Large Aquatic ponds, Large
  Lush pond) get `pond.slope: true` instead of a sheer drop; shallow base ponds and lava lagoons keep their steep edge.

## [0.166.0] - 2026-06-29

### Added
- **Theme overrides** — the `skyseed:theme_override` datapack merge layer (shared with the 1.21.1 build; see
  [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md) for the full description). Lets the modpack / other mods extend Skyseed
  islands by dropping a datapack patch (e.g. add `create:zinc_ore` to the rocky island). Pure codec/Java — no per-node
  `//?` needed; both nodes' gametests green.

## [0.165.0] - 2026-06-29

### Added
- **`skyseed:void` chunk generator** (shared with the 1.21.1 build — see [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md)
  for the full description). Suppresses biome-feature decoration in the void overworld/Nether and natural structures
  in every dimension, so biome mods (BYG/BWG/Terralith…) can't leak features at the void floor (~y=-64) and the
  "Generate Structures" toggle is moot. Only node difference: 26.1.2's `createStructures` takes a 6th
  `ResourceKey<Level>` param, handled with a `//?` guard (`applyBiomeDecoration` is identical across nodes).

## [0.164.0] - 2026-06-28

### Changed
- **Pale Garden is now a biome override, not a dedicated seed.** Dropped the 26.1.2-only Pale Garden Skyseed and folded
  its Creaking (`pale_oak_creaking`) into the `pale_garden` biome override — now on the **forest, forest_large, AND
  huge_forest** seeds (tree counts scaled per size). Throw any forest-line seed over a pale_garden biome to grow the full
  eerie pale variant: creaking pale oak, pale moss + carpet, eyeblossom, hanging-moss underside. Removed the whole
  modern-only-seed apparatus for it (the `//?`-gated `SEED_THEMES` entry, the recipe, the craft/gathered/reveal
  advancements, the `#skyseeds` tag entry, the guide entry, lang/model/texture). 26.1.2 seed items 70 → 69 (now matching
  1.21.1); the generic modern-only-content pattern stays wired (unused) for future node-only content.

## [0.163.0] - 2026-06-28

### Fixed
- **Guide entries are gated by their reveal advancement again** — only relevant/unlocked seeds show, instead of every
  entry appearing from the start. 0.158.0 wrongly dropped the entry-level `modonomicon:advancement` condition while
  chasing the premature "found it!"; that turned out to be the page-level checklist (fixed separately in 0.161.0), so
  the entry gating was correct all along. Restored it.

## [0.162.0] - 2026-06-28

### Changed
- **Trimmed the superfluous found-it explanation from "The Rare Catch" intro** (the "A green [x] turns up under each
  once you've gathered the makings" line) — players notice the checkmarks without being told. Removed from the shared
  Patchouli source, so both guide backends drop it.

## [0.161.0] - 2026-06-28

### Fixed
- **The Modonomicon guide really stops showing "found it!" now.** 0.158.0 dropped the entry-level reveal *condition*,
  but the false "found it!" was the page-level checklist in "The Rare Catch": each `[x] … found it!` page is gated by a
  page-level `advancement` that Patchouli hides until earned — and Modonomicon doesn't honour page gates, so they all
  showed. `generateGuide` now drops those gated progress-checklist pages from the Modonomicon book. (The Patchouli book
  keeps them; page gating works there.)

## [0.160.0] - 2026-06-28

1.21.1-only fix (see [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md)): the 0.157.0 guide-icon fix crashed the 1.21.1 client
at startup (`RegisterAdditional` rejected the `inventory` variant). **No 26.1.2 change** — its guide icon was never
affected (it uses the generated `items/guide.json` definition).

## [0.159.0] - 2026-06-28

### Fixed
- **The seed throw wind-up (raise-to-throw) animation plays again.** The port mapped 1.21.1's `UseAnim.SPEAR` to the
  literal `ItemUseAnimation.SPEAR`, but on 26.1.2 that enum split: the trident raise (what 1.21.1's SPEAR was) is now
  `ItemUseAnimation.TRIDENT`, while `SPEAR` is a new spear-weapon animation that shows no wind-up for a thrown item.
  The seed now returns `TRIDENT`. Visual only — throwing and landing already worked.

## [0.158.0] - 2026-06-28

### Fixed
- **The Modonomicon guide no longer shows "found it!" on entries nothing has been found for.** Patchouli's per-entry
  `advancement` (reveal-when-found) was translated to a `modonomicon:advancement` condition, which Modonomicon renders
  as an always-already-met "found it!" completion flag — worse than no gating. `generateGuide` no longer emits the
  condition, so Modonomicon entries are simply always visible. (The Patchouli book keeps reveal-on-found.)

## [0.157.0] - 2026-06-28

1.21.1-only fix (see [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md)): the Modonomicon guide-book icon. **No 26.1.2
change** — the book already renders the Skyfarer's Almanac there via its generated `items/guide.json` definition (0.156.0).

## [0.156.0] - 2026-06-28

The first real `:26.1.2:runClient` session surfaced runtime issues the headless gametests can't (no client model load, no integrated-server→client handshake). All fixed.

### Fixed — 26.1.2 runClient
- **`runClient` no longer hangs on "Loading terrain".** `test_instance` is a network-synced registry, so the client handshake (`RegistrySynchronization.packRegistry`) serializes every gametest — and the code-registered tests' codec threw. Registered a real `skyseed:gametest` codec in `TEST_INSTANCE_TYPE` and backed `SkyseedTest.codec()` with it (encode is all the handshake needs; decode → no-op). New gametest `every_test_instance_serializes_for_client_sync` guards it. Verified: the client reaches "joined the game".
- **All item icons render.** On 1.21.5+ every item needs an `assets/<ns>/items/<id>.json` definition or it renders as the missing-texture checkerboard; Skyseed shipped none (1.21.1 uses the old `models/item/` system + a bake hook, which has no base model to copy here). A new `generateItemModelDefinitions` task emits one per item — the committed seed/relic/edge/guide models **and** the generated debug-seed models (237 total). Missing-item-model warnings: ~230 → 0.
- **The Modonomicon guide book shows the Skyfarer's Almanac icon** (its `items/guide.json` definition + the book's `model: skyseed:guide` field), instead of the default brown book.
- **Dropped the obsolete global-loot-modifier index** (`data/neoforge/loot_modifiers/global_loot_modifiers.json`): on 1.21.5+ NeoForge loads each `loot_modifiers/` file as a codec GLM, so the legacy `{replace,entries}` index logged `ERROR: No key type`. The per-relic GLMs load directly and still drop.

## [0.155.0] - 2026-06-28

The whole 26.1.2 port landed under this version (1.21.1 stays byte-for-byte identical — the shared data additions below
are inert on 1.21.1, so its build is functionally unchanged).

### Added — the 26.1.2 build
- **Production code compiles + builds on 26.1.2** (NeoForge `26.1.2.76`, Java 25), driven entirely from `compat`
  directives. ~18 months of MC + NeoForge churn resolved: `ResourceLocation`→`Identifier` (the crux, 171×, via
  String-id codecs + the facade), the entity NBT rewrite (`ValueInput`/`ValueOutput`), `SavedData`→Codec, the recipe
  API, the `LootModifier` codec, GameRules→registry, the spawn/respawn API, the client model/key APIs, mob-class
  reorg, `MobSpawnType`→`EntitySpawnReason`, `registryOrThrow`→`lookupOrThrow`, and the scattered 1-offs. The void
  noise-settings gained `preliminary_surface_level` (shared JSON; verified by `void_worldgen_setup_loads_and_is_void`).
- **A native 26.1.2 gametest harness — 134 tests** (was *GAMETESTPLAN*). A separate `gametest_26_1_2` source set on the
  new `GameTestInstance` framework (the old `@GameTest`/`@GameTestHolder` annotations were removed), registered via
  `RegisterGameTestsEvent`. Covers all four phases (generation invariants, world-apply, structure, book/icon incl.
  recipe-resolution, loot, and a 26.1.2-captured golden master that is 4/5 byte-identical to the 1.21.1 suite). The
  1.21.1 suite stays frozen as the regression witness.
- **Golden-source recipe generation** (was *RECIPEGENPLAN*). Recipes are authored once as "golden" (modern string-
  ingredient form) under `recipes/`; the `generateRecipes` Gradle task emits version-correct JSON per node (26.1.2
  verbatim, 1.21.1 downgraded to `{item}`/`{tag}`). A `recipes/_modern_only/` subtree is version-gated out of the
  1.21.1 build.
- **The guide is now an optional Modonomicon book** (was *MODONOMICONPLAN*), preferred over Patchouli on every version
  (Patchouli kept as a first-class fallback; graceful if both are installed). The Modonomicon book is generated by
  `generateGuide` from the golden Patchouli content; `$(br)`/`$(li)` emit Markdown hard breaks so single line breaks
  render. `SkyseedGuide.book()` walks Modonomicon → Patchouli → written book.
- **All production stubs wired to real APIs:** `ThemeScanner` walks `IModFile.getContents().visitContent` (the 152 auto
  debug seeds regenerate); the bonus chest reads `server.getWorldGenSettings().options()`; `FMLEnvironment.isProduction()`;
  the auto-debug-seed icon hook uses `ModelEvent.ModifyBakingResult.getBakingResult().itemStackModels()`.
- **CI / multi-version build (Stage 3 start):** `chiseledBuild` + `chiseledRunGameTestServer` fan a task across all
  version nodes; the `build.yml` GitHub Actions job runs those chiseled tasks to build + gametest each node on its JDK (1.21.1→21, 26.1.2→25).

### Added — worldgen content (the 1.21.4 / 1.21.5 delta; inert on 1.21.1)
- **Pale Garden** — a `pale_garden` biome override on the Forest line (pale oak, pale moss, eyeblossom, hanging moss),
  plus a **dedicated 26.1.2-only Pale Garden seed** (pale-oak with creaking hearts → a Creaking at night). The seed is
  the template for modern-only content: `//?`-gated `SEED_THEMES` entry, modern-only recipe, tag-based advancements,
  `required:false` `#skyseeds` tag entry, and a guide-gen filter.
- **1.21.5 vegetation** — leaf litter, bush, firefly bush, wildflowers, golden dandelion (forest, meadow); short/tall
  dry grass, cactus flower (desert, badlands); and **fallen logs** (forest/taiga/jungle — a jar-diff caught these).
- **New mobs** — nautilus + zombie nautilus (aquatic), parched + camel husk (desert), happy ghast (huge meadow, a sky-
  mount reward), copper golem (the big village). The cow/pig/chicken biome-temperature variant defaults automatically
  through the existing spawn path (`finalizeMobSpawn` → biome selection) — verified, no change.
- A vanilla **jar diff confirmed 0 new structures / 0 new structure sets / 1 new biome** (pale_garden) between 1.21.1
  and 26.1.2; all 109 new blocks are obtainable.

### Added — bootstrap (Stage 2a, historical)
- The `26.1.2` node (NeoForge `26.1.2.76`, Java 25) added to the Stonecutter matrix, with per-node MC / NeoForge / Java
  / Parchment / Patchouli selection from the version-keyed root `gradle.properties`.
