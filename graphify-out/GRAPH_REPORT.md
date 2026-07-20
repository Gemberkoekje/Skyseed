# Graph Report - .  (2026-07-20)

## Corpus Check
- Large corpus: 1289 files · ~968,520 words. Semantic extraction will be expensive (many Claude tokens). Consider running on a subfolder.

## Summary
- 2400 nodes · 8373 edges · 108 communities (95 shown, 13 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 221 edges (avg confidence: 0.8)
- Token cost: 279,645 input · 0 output

## Community Hubs (Navigation)
- Theme Gametest Suite
- Nether Biome Adaptation Tests
- Jigsaw Structure Assembly
- BWG Village Templates
- Client Config & Events
- Trial Chamber Templates
- Island Generation Gametests
- Gametest Registration & Wiring
- Island Generator Core
- Trade Post Templates
- Meteorite Core Block
- Id & Lookup Utilities
- Caves & Fizzle Rules
- Ids & Resource Codecs
- Farmer's Delight Ruins
- Forced-Chunk Management
- End City Templates
- Frozen Warren Templates
- Jungle Temple Templates
- Skyseed Commands
- Pond & Water Carving
- Desert Temple & Dev Structures
- Modpack Design Plans
- Generation Job Scheduler
- Catacombs Templates
- Citadel Templates
- Dungeon Complex Templates
- Guide Recipe
- Generation & Crash-Resume Tests
- Common Ruins Templates
- Ore Planner
- Rare Structure Templates
- Rim Noise & Scatter
- Decoration Planner
- Portal Chamber Templates
- Loot Drop Modifier
- Path Surfacer
- Nether Fortress Templates
- Island Seed Entity
- Explore Themes & Mod Compat
- Bastion Templates
- Multi-Mod Ruins Templates
- Mod Entry Point
- Mob Planner
- War Barrow Templates
- Guide Book Compat
- World Setup Events
- Structure Content Tests
- Island Seed Data Accessors
- Forced Biome & Rare Picker
- Create Mod Ruins
- Mineshaft Templates
- Explore Theme Resolver
- Theme Scanner
- Vanilla Pack & Guide Docs
- Cave Carver
- Island Plan Model
- Debug Street Templates
- Crash Recovery
- Custom Trees
- Core Grow Loop & Themes
- Gametest Instance (26.1)
- Player Utilities
- Animal Pen Templates
- Immersive Engineering Ruins
- Iron's Spells Ruins
- Project Overview & Backlog
- Portal Twin Placer
- AE2 Compat & Items
- Lava & Int Range
- Start Island Builder
- AE2 Ruins Templates
- Ruined Portal Templates
- Biome Override Codec
- Island Placement Fit
- Modpack Manifest
- Island Growth Ticker
- Ancient City Templates
- Mystical Agriculture Ruins
- Jigsaw Config & Chunk Gen
- Entity Spawn Helper
- Rare Structure Roll Tests
- Pending Island State
- Dungeon Templates
- Network Payloads
- Entity Registry
- Ocean Monument Templates
- Ladder Shaft Planner
- Structure Traps
- Common Config
- Dev Structure & Epic Plan
- Worldgen Fixes & Notes
- Mod List & Client Config Docs
- Gradle Wrapper
- Key Mappings
- Portal Twin Notes
- Nether Fortress Note
- Ocean Monument Note
- End Light Pass Note

## God Nodes (most connected - your core abstractions)
1. `SkyseedTests` - 261 edges
2. `SkyseedGameTests` - 256 edges
3. `Built` - 173 edges
4. `Id` - 101 edges
5. `IslandTheme` - 55 edges
6. `IslandSeedEntity` - 45 edges
7. `GenerationJob` - 45 edges
8. `BwgVillageTemplates` - 41 edges
9. `BiomeOverride` - 39 edges
10. `SkyseedWorldData` - 37 edges

## Surprising Connections (you probably didn't know these)
- `Skyseed CurseForge description` --semantically_similar_to--> `Skyseed mod`  [INFERRED] [semantically similar]
  description.md → README.md
- `modNames inert-safety side-map` --semantically_similar_to--> `theme_override datapack registry`  [INFERRED] [semantically similar]
  EPICSTRUCTUREPLAN.md → README.md
- `Crimson/warped stem-tree feature` --semantically_similar_to--> `forceOneTree bare-island guard`  [INFERRED] [semantically similar]
  SKYNETHERENDBIOMEPLAN.md → SIGNOFFPLAN.md
- `Vanilla pack CurseForge description` --semantically_similar_to--> `Skyseed vanilla modpack`  [INFERRED] [semantically similar]
  modpack-vanilla/description.md → modpack-vanilla/README.md
- `Skyseed vanilla modpack` --references--> `Skyseed mod`  [EXTRACTED]
  modpack-vanilla/README.md → README.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Multi-version Stonecutter build + CI + dual changelogs** — readme_stonecutter, refactorplan_compat_facade, _github_workflows_build_chiseled_build, changelog_1_21_1_doc, changelog_26_1_doc [EXTRACTED 0.90]
- **The six grand epic structure builds (#74)** — epicstructureplan_grand_ruined_chapel, epicstructureplan_grand_magitech_workshop, epicstructureplan_grand_essence_farm, epicstructureplan_grand_substation, epicstructureplan_grand_distillery, epicstructureplan_grand_freight_depot [EXTRACTED 0.90]
- **Inert-without-the-mod compat mechanisms** — readme_theme_override_registry, epicstructureplan_mod_names_inert_safety, readme_dev_structure_generator [INFERRED 0.80]
- **Content-mod integration plan tree (CONTENTPLAN and children)** — modpack_growyourownworld_contentplan, modpack_growyourownworld_ieplan, modpack_growyourownworld_ironspellsplan, modpack_growyourownworld_quarkplan, modpack_growyourownworld_quarkislandplan, modpack_growyourownworld_ironstructurerebuildplan [EXTRACTED 0.90]
- **Plans implementing the inert-safe mod-gating pattern** — modpack_growyourownworld_varietystructureplan_inert_safe_gating, modpack_growyourownworld_quarkislandplan, modpack_growyourownworld_ieplan, modpack_growyourownworld_ironspellsplan, modpack_growyourownworld_varietystructureplan [INFERRED 0.85]
- **Every content-mod integration ships its FTB quest chapter (#19 standing rule)** — modpack_growyourownworld_questplan, modpack_growyourownworld_contentplan, modpack_growyourownworld_ieplan, modpack_growyourownworld_ironspellsplan, modpack_growyourownworld_quarkplan [INFERRED 0.85]

## Communities (108 total, 13 thin omitted)

### Community 0 - "Theme Gametest Suite"
Cohesion: 0.06
Nodes (5): GameTest, BlockPos, CompoundTag, GameTestHelper, SkyseedGameTests

### Community 3 - "Jigsaw Structure Assembly"
Cohesion: 0.06
Nodes (41): BiomeSource, BoundingBox, ChunkAccess, ChunkGeneratorStructureState, Feature, FeaturePlaceContext, NoneFeatureConfiguration, BlockPos (+33 more)

### Community 5 - "BWG Village Templates"
Cohesion: 0.15
Nodes (19): BwgVillageTemplates, Feature, BOOKS, NONE, WOOL, Block, BlockPos, BlockState (+11 more)

### Community 6 - "Client Config & Events"
Cohesion: 0.06
Nodes (33): BooleanValue, ByteBuf, CustomPacketPayload, InteractionHand, InteractionResultHolder, Item, ModifyBakingResult, RegisterAdditional (+25 more)

### Community 7 - "Trial Chamber Templates"
Cohesion: 0.16
Nodes (10): BlockPos, BlockState, CompoundTag, FrontAndTop, TrialChamberTemplates, BlockPos, BlockState, CompoundTag (+2 more)

### Community 9 - "Gametest Registration & Wiring"
Cohesion: 0.09
Nodes (3): Identifier, RegisterGameTestsEvent, SkyseedTests

### Community 10 - "Island Generator Core"
Cohesion: 0.14
Nodes (17): Decor, IslandGenerator, Biome, BlockPos, BlockState, Holder, RandomSource, Rotation (+9 more)

### Community 11 - "Trade Post Templates"
Cohesion: 0.15
Nodes (18): Feature, BOOKS, FORGE, HAY, NONE, WOOL, Block, BlockPos (+10 more)

### Community 12 - "Meteorite Core Block"
Cohesion: 0.09
Nodes (22): Block, DeferredBlock, EntityJoinLevelEvent, IntegerProperty, BlockState, Builder, Override, MeteoriteCoreBlock (+14 more)

### Community 13 - "Id & Lookup Utilities"
Cohesion: 0.10
Nodes (19): Reference, Id, Override, Nullable, Biome, Block, BlockState, ConfiguredFeature (+11 more)

### Community 14 - "Caves & Fizzle Rules"
Cohesion: 0.09
Nodes (25): Caves, Codec, FizzleRule, Biome, Codec, Holder, Carve, IslandTheme (+17 more)

### Community 15 - "Ids & Resource Codecs"
Cohesion: 0.07
Nodes (22): GameTestHolder, NewRegistry, PrefixGameTestTemplate, RegisterEvent, Codec, Ids, ResourceLocation, BlockState (+14 more)

### Community 16 - "Farmer's Delight Ruins"
Cohesion: 0.15
Nodes (12): Half, FarmersRuinsTemplates, BlockPos, BlockState, CompoundTag, Direction, BlockPos, BlockState (+4 more)

### Community 17 - "Forced-Chunk Management"
Cohesion: 0.09
Nodes (12): Factory, ListTag, PlayerLoggedInEvent, SavedData, SubscribeEvent, BlockPos, CompoundTag, MinecraftServer (+4 more)

### Community 18 - "End City Templates"
Cohesion: 0.22
Nodes (6): EndCityTemplates, BlockPos, BlockState, CompoundTag, Direction, FrontAndTop

### Community 19 - "Frozen Warren Templates"
Cohesion: 0.09
Nodes (14): FrozenWarrenTemplates, BlockPos, BlockState, HamletTemplates, Block, BlockState, Wood, Block (+6 more)

### Community 20 - "Jungle Temple Templates"
Cohesion: 0.12
Nodes (15): BlockPos, BlockState, CompoundTag, Direction, JungleTempleTemplates, Col, BlockPos, BlockState (+7 more)

### Community 21 - "Skyseed Commands"
Cohesion: 0.14
Nodes (14): CommandDispatcher, CommandSourceStack, RegisterCommandsEvent, ServerStoppedEvent, Component, CompoundTag, EventBusSubscriber, Level (+6 more)

### Community 22 - "Pond & Water Carving"
Cohesion: 0.19
Nodes (7): Water, BlockPos, BlockState, RandomSource, PondCarver, Codec, Pond

### Community 23 - "Desert Temple & Dev Structures"
Cohesion: 0.08
Nodes (9): FMLCommonSetupEvent, DesertTempleTemplates, DevStructureGenerator, DragonTrophyTemplates, BlockState, PiglinTradingPostTemplates, BlockState, ReturnPortalTemplates (+1 more)

### Community 24 - "Modpack Design Plans"
Cohesion: 0.11
Nodes (32): Modpack Visuals Plan (BEAUTIFYPLAN), Content-Mod Integration Umbrella Plan (CONTENTPLAN), Gated island-tier system, Skyseed: Grow Your Own World — Modpack Description, Immersive Engineering + Petroleum + Flight Plan (IEPLAN), Iron's Spells + Artifacts + Relics Plan (IRONSPELLSPLAN), Iron's Oversized-Structure Rebuild Plan, Island-friendly rebuilds (Frozen Warren / War Barrow / Mage's Sanctum) (+24 more)

### Community 25 - "Generation Job Scheduler"
Cohesion: 0.15
Nodes (9): GenerationJob, Block, BlockPos, BlockState, ChunkGenerator, Entity, Level, ResourceKey (+1 more)

### Community 26 - "Catacombs Templates"
Cohesion: 0.23
Nodes (5): CatacombsTemplates, BlockPos, BlockState, CompoundTag, Direction

### Community 27 - "Citadel Templates"
Cohesion: 0.23
Nodes (5): CitadelTemplates, BlockPos, BlockState, CompoundTag, Direction

### Community 28 - "Dungeon Complex Templates"
Cohesion: 0.29
Nodes (6): DungeonComplexTemplates, BlockPos, BlockState, CompoundTag, Direction, FrontAndTop

### Community 29 - "Guide Recipe"
Cohesion: 0.15
Nodes (15): CraftingBookCategory, CraftingInput, CustomRecipe, SimpleCraftingRecipeSerializer, GuideRecipe, ItemStack, Level, Override (+7 more)

### Community 31 - "Common Ruins Templates"
Cohesion: 0.27
Nodes (3): CommonRuinsTemplates, BlockPos, BlockState

### Community 32 - "Ore Planner"
Cohesion: 0.14
Nodes (16): BlockPos, BlockState, RandomSource, OrePlanner, getSerializedName(), Override, OreDepth, CORE (+8 more)

### Community 33 - "Rare Structure Templates"
Cohesion: 0.21
Nodes (5): BlockPos, BlockState, CompoundTag, Direction, RareStructureTemplates

### Community 34 - "Rim Noise & Scatter"
Cohesion: 0.15
Nodes (12): RandomSource, RimNoise, BlockState, Scatter, BlockPos, BlockState, RandomSource, Result (+4 more)

### Community 35 - "Decoration Planner"
Cohesion: 0.20
Nodes (12): DecorationPlanner, Block, BlockPos, BlockState, RandomSource, ServerLevel, Decoration, Codec (+4 more)

### Community 36 - "Portal Chamber Templates"
Cohesion: 0.32
Nodes (4): BlockPos, BlockState, Direction, PortalChamberTemplates

### Community 37 - "Loot Drop Modifier"
Cohesion: 0.16
Nodes (14): LootContext, LootItemCondition, LootModifier, ObjectArrayList, AddDropModifier, IGlobalLootModifier, ItemStack, MapCodec (+6 more)

### Community 38 - "Path Surfacer"
Cohesion: 0.30
Nodes (7): Block, BlockPos, BlockState, RandomSource, ServerLevel, PathSurfacer, Wood

### Community 39 - "Nether Fortress Templates"
Cohesion: 0.30
Nodes (6): BlockPos, BlockState, CompoundTag, Direction, FrontAndTop, NetherFortressTemplates

### Community 40 - "Island Seed Entity"
Cohesion: 0.12
Nodes (13): BlockHitResult, EntityDataAccessor, HitResult, Builder, EntityType, Item, ItemStack, Level (+5 more)

### Community 41 - "Explore Themes & Mod Compat"
Cohesion: 0.15
Nodes (20): Skyseed CurseForge description, Grand Alchemist's Distillery, Grand Automated Essence Farm, Grand Sky-Freight Depot, Grand Magitech Workshop, Grand FE→ME Substation, waterWheelRace spinning mover, Vanilla pack CurseForge description (+12 more)

### Community 42 - "Bastion Templates"
Cohesion: 0.20
Nodes (6): BastionTemplates, BlockPos, BlockState, CompoundTag, FrontAndTop, WitherArenaTemplates

### Community 43 - "Multi-Mod Ruins Templates"
Cohesion: 0.29
Nodes (5): Axis, BlockPos, BlockState, CompoundTag, MultiModRuinsTemplates

### Community 44 - "Mod Entry Point"
Cohesion: 0.17
Nodes (11): Blocks, CreativeModeTab, Logger, Mod, ModContainer, DeferredHolder, DeferredRegister, IEventBus (+3 more)

### Community 45 - "Mob Planner"
Cohesion: 0.23
Nodes (10): BlockPos, BlockState, EntityType, RandomSource, MobPlanner, AnimalPack, Entry, Codec (+2 more)

### Community 46 - "War Barrow Templates"
Cohesion: 0.30
Nodes (4): BlockPos, BlockState, CompoundTag, WarBarrowTemplates

### Community 47 - "Guide Book Compat"
Cohesion: 0.16
Nodes (7): Items, ItemStack, ModonomiconCompat, ItemStack, PatchouliCompat, ItemStack, SkyseedGuide

### Community 48 - "World Setup Events"
Cohesion: 0.22
Nodes (8): NoiseBasedChunkGenerator, BlockPos, EventBusSubscriber, MinecraftServer, ServerLevel, ServerStartedEvent, SubscribeEvent, WorldSetupEvents

### Community 51 - "Forced Biome & Rare Picker"
Cohesion: 0.29
Nodes (5): Biome, BlockPos, Holder, ServerLevel, DebugForce

### Community 52 - "Create Mod Ruins"
Cohesion: 0.33
Nodes (5): CreateRuinsTemplates, Axis, BlockPos, BlockState, CompoundTag

### Community 53 - "Mineshaft Templates"
Cohesion: 0.42
Nodes (6): BlockPos, BlockState, CompoundTag, FrontAndTop, MineshaftTemplates, Variant

### Community 54 - "Explore Theme Resolver"
Cohesion: 0.35
Nodes (4): ExploreThemes, Biome, Holder, Rule

### Community 55 - "Theme Scanner"
Cohesion: 0.25
Nodes (4): JsonArray, JsonObject, DebugSeedSpec, ThemeScanner

### Community 56 - "Vanilla Pack & Guide Docs"
Cohesion: 0.15
Nodes (16): Vanilla pack per-mod config notes, Vein Mining ore/log whitelist config, Jade, Just Enough Items (JEI), Curated QoL mod list, Patchouli, Vein Mining (QoL mod), Xaero's Minimap (+8 more)

### Community 57 - "Cave Carver"
Cohesion: 0.43
Nodes (4): CaveCarver, BlockPos, BlockState, RandomSource

### Community 58 - "Island Plan Model"
Cohesion: 0.30
Nodes (14): StructurePlan, AnimalSpawn, BlockPlacement, GrowSpot, IslandPlan, BlockPos, BlockState, ConfiguredFeature (+6 more)

### Community 59 - "Debug Street Templates"
Cohesion: 0.29
Nodes (5): DebugStreetTemplates, BlockPos, BlockState, CompoundTag, FrontAndTop

### Community 60 - "Crash Recovery"
Cohesion: 0.26
Nodes (6): CrashRecovery, EventBusSubscriber, MinecraftServer, ServerLevel, ServerStartedEvent, SubscribeEvent

### Community 61 - "Custom Trees"
Cohesion: 0.43
Nodes (4): CustomTrees, BlockPos, BlockState, RandomSource

### Community 62 - "Core Grow Loop & Themes"
Cohesion: 0.18
Nodes (14): modNames inert-safety side-map, biome_overrides, Oh The Biomes We've Gone (BWG compat), Skyseed grow-in core loop, Farmer's Delight family (mod compat), IslandGenerator (planIsland), IslandSeedItem throwable seed, IslandTheme codec (+6 more)

### Community 63 - "Gametest Instance (26.1)"
Cohesion: 0.29
Nodes (9): GameTestInstance, MutableComponent, GameTestHelper, Holder, MapCodec, Override, TestEnvironmentDefinition, SkyseedTest (+1 more)

### Community 64 - "Player Utilities"
Cohesion: 0.21
Nodes (7): ServerPlayer, Component, Player, ServerLevel, Players, EventBusSubscriber, PlayerEvents

### Community 65 - "Animal Pen Templates"
Cohesion: 0.38
Nodes (3): AnimalTemplates, BlockPos, BlockState

### Community 66 - "Immersive Engineering Ruins"
Cohesion: 0.38
Nodes (4): IeRuinsTemplates, BlockPos, BlockState, CompoundTag

### Community 67 - "Iron's Spells Ruins"
Cohesion: 0.32
Nodes (3): IronsRuinsTemplates, Block, BlockState

### Community 68 - "Project Overview & Backlog"
Cohesion: 0.27
Nodes (13): Dependabot version-update config, chiseledBuild version fan-out, CurseForge upload job, Build CI workflow, CHANGELOG — 1.21.1 build, CHANGELOG — 26.1.2 build, PLANOFPLANS prioritized backlog, Engineering debt (code-review findings) (+5 more)

### Community 69 - "Portal Twin Placer"
Cohesion: 0.37
Nodes (6): BlockPos, Level, ResourceKey, ServerLevel, TwinPlacer, TwinResult

### Community 70 - "AE2 Compat & Items"
Cohesion: 0.24
Nodes (6): DeferredItem, Ae2Compat, IEventBus, Item, ModItems, TagKey

### Community 71 - "Lava & Int Range"
Cohesion: 0.26
Nodes (6): IntRange, Codec, RandomSource, Codec, Lake, Lava

### Community 72 - "Start Island Builder"
Cohesion: 0.44
Nodes (5): BlockPos, BlockState, MutableBlockPos, ServerLevel, StartIsland

### Community 73 - "AE2 Ruins Templates"
Cohesion: 0.38
Nodes (4): AeRuinsTemplates, BlockPos, BlockState, CompoundTag

### Community 74 - "Ruined Portal Templates"
Cohesion: 0.33
Nodes (5): Built, BlockPos, BlockState, CompoundTag, RuinedPortalTemplates

### Community 75 - "Biome Override Codec"
Cohesion: 0.29
Nodes (6): BiomeOverride, Biome, Codec, Holder, MapCodec, Scalars

### Community 76 - "Island Placement Fit"
Cohesion: 0.31
Nodes (6): FunctionalInterface, Fit, IslandPlacement, BlockPos, Vec3, Occupancy

### Community 77 - "Modpack Manifest"
Cohesion: 0.18
Nodes (10): author, files, manifestType, manifestVersion, minecraft, modLoaders, version, name (+2 more)

### Community 78 - "Island Growth Ticker"
Cohesion: 0.25
Nodes (5): ServerStoppingEvent, IslandGrowth, EventBusSubscriber, Post, SubscribeEvent

### Community 79 - "Ancient City Templates"
Cohesion: 0.33
Nodes (5): AncientCityTemplates, BlockPos, BlockState, CompoundTag, Direction

### Community 80 - "Mystical Agriculture Ruins"
Cohesion: 0.38
Nodes (4): BlockPos, BlockState, CompoundTag, MysticalRuinsTemplates

### Community 81 - "Jigsaw Config & Chunk Gen"
Cohesion: 0.20
Nodes (10): Grand Ruined Chapel, Planned notes (open items), #70 Waystone drop-compat, End City structure, JigsawConfig jigsaw assembly, Trial Chamber structure, SkyseedVoidChunkGenerator, Woodland Mansion structure (+2 more)

### Community 82 - "Entity Spawn Helper"
Cohesion: 0.31
Nodes (5): Entities, Entity, EntityType, Nullable, ServerLevel

### Community 86 - "Dungeon Templates"
Cohesion: 0.47
Nodes (3): DungeonTemplates, BlockPos, BlockState

### Community 87 - "Network Payloads"
Cohesion: 0.32
Nodes (4): IPayloadContext, RegisterPayloadHandlersEvent, IEventBus, SkyseedNetwork

### Community 88 - "Entity Registry"
Cohesion: 0.39
Nodes (5): DeferredHolder, DeferredRegister, EntityType, IEventBus, ModEntities

### Community 90 - "Ladder Shaft Planner"
Cohesion: 0.43
Nodes (4): BlockPos, BlockState, RandomSource, ShaftPlanner

### Community 91 - "Structure Traps"
Cohesion: 0.43
Nodes (4): Block, BlockPos, ServerLevel, Traps

### Community 92 - "Common Config"
Cohesion: 0.40
Nodes (3): DoubleValue, ModConfigSpec, SkyseedCommonConfig

### Community 93 - "Dev Structure & Epic Plan"
Cohesion: 0.40
Nodes (6): Cross-wiring build reuse, D4 derelict loot gating principle, Grand epic structures (#74), GrandEpicTemplates class, StructureParts helpers, DevStructureGenerator (code-authored .nbt)

### Community 94 - "Worldgen Fixes & Notes"
Cohesion: 0.40
Nodes (6): GenerationJob tick-budget scheduler, clearPlantingSpot dirt-pad bug, forceOneTree bare-island guard, grow ground-cover field (bonemeal), Model-A Nether biome kits, Crimson/warped stem-tree feature

### Community 95 - "Mod List & Client Config Docs"
Cohesion: 0.40
Nodes (5): CurseForge Project IDs Registry (138 mods), Mods List (138 jars, mods.txt), Xaero's Minimap Info-Display Config, Xaero's HUD Module Config (xaerohud.txt), Minecraft options.txt (client settings + keybinds)

### Community 96 - "Gradle Wrapper"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **63 isolated node(s):** `version`, `modLoaders`, `manifestType`, `manifestVersion`, `name` (+58 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **13 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Id` connect `Id & Lookup Utilities` to `Village Jigsaw Gametests`, `Biome Override Resolution`, `Client Config & Events`, `Island Generator Core`, `Meteorite Core Block`, `Caves & Fizzle Rules`, `Ids & Resource Codecs`, `Pond & Water Carving`, `Ore Planner`, `Decoration Planner`, `Loot Drop Modifier`, `Island Seed Entity`, `Mob Planner`, `Guide Book Compat`, `Island Seed Data Accessors`, `Forced Biome & Rare Picker`, `Explore Theme Resolver`, `Theme Scanner`, `Island Plan Model`, `Portal Twin Placer`, `Biome Override Codec`?**
  _High betweenness centrality (0.099) - this node is a cross-community bridge._
- **Why does `GenerationJob` connect `Generation Job Scheduler` to `Theme Gametest Suite`, `Path Surfacer`, `Island Seed Entity`, `Island Growth Ticker`, `Ids & Resource Codecs`, `Forced-Chunk Management`, `Entity Spawn Helper`, `Forced Biome & Rare Picker`, `Pending Island State`, `Island Plan Model`, `Generation & Crash-Resume Tests`?**
  _High betweenness centrality (0.055) - this node is a cross-community bridge._
- **Why does `SkyseedTests` connect `Gametest Registration & Wiring` to `Village Jigsaw Gametests`, `Nether Biome Adaptation Tests`, `Biome Override Resolution`, `Island Generation Gametests`, `Ids & Resource Codecs`, `Island Seed Data Accessors`, `Rare Structure Roll Tests`, `Generation & Crash-Resume Tests`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **What connects `version`, `modLoaders`, `manifestType` to the rest of the system?**
  _63 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Theme Gametest Suite` be split into smaller, more focused modules?**
  _Cohesion score 0.060487038491751764 - nodes in this community are weakly interconnected._
- **Should `Village Jigsaw Gametests` be split into smaller, more focused modules?**
  _Cohesion score 0.05549450549450549 - nodes in this community are weakly interconnected._
- **Should `Nether Biome Adaptation Tests` be split into smaller, more focused modules?**
  _Cohesion score 0.08158508158508158 - nodes in this community are weakly interconnected._