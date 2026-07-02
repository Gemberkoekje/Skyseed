# Changelog — Minecraft 26.1.2 build

Notable changes to the **26.1.2** Skyseed build. Skyseed is one codebase built for multiple Minecraft versions (see
`REFACTORPLAN.md`); the **1.21.1** build's history is in [CHANGELOG_1.21.1.md](CHANGELOG_1.21.1.md). Both builds share
the version-number sequence, so a version can appear in one changelog and not the other.

> **Status: the multi-version refactor is COMPLETE — the 26.1.2 build compiles, builds, and passes its own gametest
> suite.** Both version nodes are green (`./gradlew chiseledBuild` / per-node `:1.21.1:` / `:26.1.2:` tasks; CI builds
> and gametests every node). Remaining repo-wide work is tracked in `PLANOFPLANS.md`. The per-feature build plans (the
> gametest harness, the recipe generator, and the Modonomicon guide) shipped and were retired into this changelog.

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
