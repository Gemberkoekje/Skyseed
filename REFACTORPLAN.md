# Skyseed — Multi-Version Refactor (NeoForge + Stonecutter)

**Goal.** Build Skyseed against multiple **Minecraft / NeoForge versions** from one codebase, by (1) isolating the
version-volatile API calls behind a thin **facade** so the algorithm stays version-stable, and (2) using
**Stonecutter** to manage the per-version build matrix. **NeoForge-only. No new runtime dependency.**

**Out of scope.** Fabric / cross-loader. If that ever happens it gets its own plan (that's what Architectury is for);
Architectury solves the *loader* split and does nothing for the version axis, so it's deliberately not used here.

> **Status (2026-07-01): Stages 0–2 COMPLETE, Stage 3 essentially complete.** Both nodes (1.21.1 + 26.1.2) build
> and pass their own native gametest suites; the CI fan-out (`chiseledBuild` + `chiseledRunGameTestServer` via
> `.github/workflows/build.yml`) covers every node with no per-version workflow edit; the "how to add a version"
> recipe is written below. **Open items** (tracked in [`PLANOFPLANS.md`](PLANOFPLANS.md)):
>
> - **#59 — add further version nodes as wanted.** Discretionary; no third target chosen (26.2 was still beta
>   when checked). Everything is in place — follow [the recipe](#how-to-add-a-version-node-the-recipe).
> - **#56 — route the gametest suites' residual direct API calls through `compat`** (the Stage-1 leftover:
>   `BuiltInRegistries`/`ResourceLocation`/`registryOrThrow` in the 1.21.1 suite, `Identifier` in the 26.1.2
>   suite). Deprioritized — buys tidiness, not cross-suite reuse, and touching the golden-master witness
>   carries risk.
> - **#57 / #58 — contingencies, nothing to build now:** a version-keyed in-test fingerprint map only if a
>   *shared* suite ever exists (each node currently has its own suite with its own GOLDEN map, which already
>   delivers the per-version regression guarantee), and a per-version data variant only when a vanilla block-id
>   rename actually bites (tolerant skip-on-unknown data + Java-side `//?` has absorbed every rename so far).

---

## How Stonecutter works (so the plan is self-contained)

- A Gradle **settings plugin** declares a list of versions — each a "node" pinning its MC version, NeoForge version,
  Parchment mappings, etc. Stonecutter creates one build per node.
- Source is preprocessed with **comment directives** — `//? if >=1.21.4 {` … `//?}` (and `/*? if … {*/ … /*?}*/` to
  swap an expression) — so a line/block is active only for matching versions and compiles against that version's API.
- `./gradlew build` builds the **active** version (set by `stonecutter.active`, switchable for the IDE);
  `./gradlew chiseledBuild` builds **all** versions → one jar each.
- The actual MC/NeoForge dependency resolution stays with the existing build plugin (**ModDevGradle**); Stonecutter
  wraps it and feeds each node its versions. **Build-time only — players install nothing extra.**

---

## The version-volatile surface (what actually changes between versions)

The bulk of Skyseed is version-stable: the generation math, the planners, the codec data model, and the structure
`.nbt` builders. The churn concentrates in registry access, id-type construction, `JigsawPlacement.generateJigsaw`,
and the NeoForge glue (registration, events, network). **Strategy:** route it through the **`compat` facade**
(`Ids` / `Id` / `Lookup` / `Jigsaw` / `Entities` / `Players`) with stable internal signatures, plus per-file `//?`
directives for the residual that a facade can't hide (entity NBT, SavedData, recipes, client APIs).

## Migration stages

0. **Stonecutter skeleton + build proof — ✅ DONE.** Stonecutter wraps ModDevGradle; `versions/` + `.stonecutter/`
   gitignored.
1. **Concentrate the volatile surface into `compat` — ✅ DONE.** Behaviour-preserving (the `islandOutputIsStable`
   golden master stayed byte-identical). *(The gametest suites kept direct calls as the golden-master oracle —
   routing them is the open #56.)*
2. **Add the second version — MC `26.1.2` / NeoForge `26.1.2.76` — ✅ DONE.** Both nodes compile, build, and pass
   their own native suites. The full delta record is kept below as the **reference catalog for the next node**.
3. **Generalize + document — ✅ DONE except #59.** The **CI fan-out is wired**: `stonecutter.gradle.kts` registers
   `chiseledBuild` + `chiseledRunGameTestServer` (fanned across every node), and `.github/workflows/build.yml` runs
   those two chiseled tasks — so it builds + gametests **every** node with **no per-version workflow edit** (the
   version list lives only in `settings.gradle`; each node's JDK is auto-provisioned by the foojay resolver). The
   "how to add a version" recipe is written (below). Remaining: **adding further versions as wanted (#59)**.

---

## Stage 2 in detail — target MC 26.1.2 / NeoForge 26.1.2.76

> **✅ COMPLETE.** Kept as the working reference for adding the next node (#59): the toolchain traps (§2.1), where
> the directives go (§2.2), the data strategy (§2.4), and the API delta catalog (§2.6/§2.7). Figures came from
> **diffing the actual vanilla client jars**, not changelogs (the changelogs misled — Poplar/Cinnabar/Sulfur are
> 26.2, not 26.1.2).

### 2.1 Toolchain & build wiring

- Each node pins its own Java (`26.1.2` → Java 25; `1.21.1` → Java 21); Gradle toolchain matching is by exact major.
- **NeoForge** uses the 4-component `<mcMajor>.<mcMinor>.<mcPatch>.<build>` scheme → `26.1.2.76`.
- **Parchment / Patchouli / Modonomicon** are per-node properties; an unpublished one just skips (the build wraps
  them in `if (project.hasProperty("…_${mcv}"))`).
- ⚠ **Per-node version values can't live in `versions/<v>/gradle.properties`** — the whole `versions/` tree is
  gitignored and Stonecutter-regenerated. Keep them **version-keyed in the root `gradle.properties`**
  (`mc_<v>` / `neo_<v>` / `java_<v>` / …), selected in `build.gradle` by the node name.
- ⚠ **Sequencing:** adding a node makes ModDevGradle set up that version's NeoForm decompile, and the source will
  **not compile** until the `compat` directives exist — the node flip is the **start** of the compat work, not a
  free-standing bootstrap. Do it on a branch; keep the existing nodes' gametests green as the guard.

### 2.2 The version-volatile API surface (where the `//?` directives go)

- **`Ids` / `Id` / `Lookup` / `Jigsaw` / `Entities` / `Players`** — the facade files; re-verify their wrapped
  signatures per new node. *(The once-expected `compat/Registration`, `compat/Net`, `compat/Events`, `compat/*Props`
  facades were never needed — that churn was absorbed by per-file `//?` directives instead.)*
- **`world_gen_settings.dat` (26.1):** WorldGenSettings moved out of `level.dat` into
  `data/<world>/world_gen_settings.dat`. Two impacts: **(a)** the legacy `/emptynether` `/emptyend`
  level.dat-editing rescue commands are version-gated off on 26.1.2 (`RESET_SUPPORTED` gate in
  `SkyseedCommands`); **(b)** the void dimensions were re-verified to load and drive the baked dimension
  generator on 26.1.2 (gametest cites §2.2(b)). This is the standing `void_*` rule for any future node.

### 2.4 The single-codebase data strategy (the key lever)

An older build **must not reference an id that doesn't exist there** or its datapack fails to load. Rather than
per-version resource source-sets, **Skyseed's theme / decoration / biome-override / ore codecs are tolerant of
unknown block/biome/feature ids — skip-with-log instead of hard-fail** (all codecs store the raw id `String`, and
`Lookup.hasBlock` / `hasEntityType` / `biomeMatches` / `hasTemplatePool` gate resolution). The **same dataset**
ships to every node: active where the ids exist, **inert** where they don't (gametested by
`unknownThemeIdsFallBack`). The residual hard cases — a renamed vanilla id, a shifted worldgen codec — take a
guarded data variant or a `//?` directive; handle those when they bite (#58).

### 2.6 The 26.1.2 compile — the live API delta (reference catalog)

The decompile pipeline (NeoForm download → decompile → patch → recompile) runs cleanly under Stonecutter — the
integration risk is retired. The headline delta and how it was beaten:

| Old (1.21.1) | New (26.1.2) | Note |
|---|---|---|
| **`ResourceLocation`** | **`Identifier`** (pure rename) | was 171 occurrences across 31 files — the crux |
| `InteractionResultHolder<T>` | `InteractionResult` (merged) | `IslandSeedItem` |
| `UseAnim` | `ItemUseAnimation` | `IslandSeedItem` |
| `ThrowableItemProjectile` | moved into a subpackage; ctor gained an `ItemStack` arg | `IslandSeedEntity` |
| `@GameTest`/`@GameTestHolder` | removed → `GameTestInstance` + `RegisterGameTestsEvent` | own `gametest_26_1_2` source set |
| animal/npc classes | per-type subpackage reorg | `//?` import swaps |

**The rename strategy (reused for any future type rename):** Java has no import alias, so a renamed type used
everywhere can't hide behind one directive. (1) **Confine the type to the facade** — the codec records store the
raw id **`String`** (`Codec.STRING`), resolving via `Ids`/`Lookup` at use-time (this also directly enables §2.4);
(2) per-file `//?` import + usage swaps for the residual files where the id type genuinely lives.

### 2.7 The 26.1.2 shipped-code error map (the per-category fix catalog)

The ~120 shipped-code errors clustered into ~10 root deltas; each fix is a `//?` block (current branch stays
uncommented + Javadoc-free so the existing node keeps compiling). The catalog — what the next node (#59) should
expect and where the fixes live:

| Category | 26.1.2 resolution (as landed) |
|---|---|
| Mob class reorg | `animal.cow.Cow`, `npc.villager.Villager`, … — `//?` import swaps |
| `MobSpawnType`→`EntitySpawnReason` | static-import swap; `Entities.create` gained the reason arg |
| `registryOrThrow`→`lookupOrThrow` | funneled through `Lookup.registry(...)`, one `//?` |
| Entity NBT (the 1.21.5 rewrite) | `CompoundTag` direct access → `ValueInput`/`ValueOutput` method pairs — the one genuinely careful piece (per-version method bodies) |
| `SavedData` | became Codec-based (`SavedDataType`) — rewritten in `SkyseedWorldData` |
| Recipe API | `CustomRecipe`/serializer shapes → MapCodec/StreamCodec record |
| `LootModifier` | codec/ctor change in `AddDropModifier` |
| Commands / spawn API | `hasPermission`→`PermissionSet`, respawn→`RespawnConfig`/`RespawnData` |
| Client | `ModelResourceLocation` moved, `KeyMapping.Category`, overlay message — compile-checked only (no gametest coverage) |
| 1-offs | `Blocks.CHAIN`→`IRON_CHAIN`, `ChunkPos` ctor + `JigsawStructure.MaxDistance`, `dataVersion().version()`, GameRules→registry (note `RULE_DISABLE_RAIDS=true`→`RAIDS=false` **inverted**), … |

The once-pragmatic 26.1.2 stubs (worldGenOptions / icon hook / `findResource` / `FMLEnvironment.production`) were
all **re-wired to real 26.1.2 APIs** (`ThemeScanner` / `DevStructureGenerator` use real hooks). The NeoForge
sources jar in the gradle cache is the reference for NeoForge-specific deltas.

### 2.8 Stage 2d content progress — ✅ ALL COMPLETE (anchor labels kept: 2d-1…2d-4)

- **2b tolerant codecs** — every resolve path skips unknown ids; proven by `unknownThemeIdsFallBack`.
- **2d-1 Pale Garden** — a `pale_garden` biome override on the Forest line (all three tiers), v0.164.0; gametest
  `forest_over_pale_garden_grows_pale_variant`. *(A dedicated Pale Garden seed was built then folded into the
  override — see the gating pattern below.)*
- **2d-2 1.21.5 vegetation** — decoration on existing themes (forest/meadow/desert/badlands, + fallen logs from
  the post-completion jar-diff audit); gametests `new_vegetation_resolves_on_themes`, `forest_places_fallen_logs`.
- **2d-3 new-mob placements** — nautilus/zombie_nautilus → aquatic, parched/camel_husk → desert, happy_ghast →
  huge_meadow, copper_golem → village_center; mannequin skipped (display entity); gametest
  `new_mobs_resolve_on_themes`.
- **2d-4 cow/pig/chicken biome-temperature variants** — verified to default through the existing spawn path (no
  code change); gametest `farm_animals_default_to_biome_variant`.
- **2c block-completeness** — all 109 new 26.1.2 ids obtainable (new primary sources above; remainder craftable).

**The modern-only-content gating pattern** (proved by the since-folded Pale Garden seed, documented for the next
genuinely node-only seed): `//?`-gate the `SEED_THEMES` entry, recipes under `recipes/_modern_only/` (skipped on
legacy by `generateRecipes`), **tags** in advancements (a direct unknown item id breaks the legacy datapack load;
an unknown tag resolves to empty), `{id, required:false}` in `#skyseeds`, and a `generateGuide` filter for the
book entry. The machinery stays wired (currently unused).

---

## How to add a version node (the recipe)

Distilled from adding `26.1.2`. A new node is **mechanical config + per-delta `//?` directives** — the facade
absorbs most of it, and the data is already tolerant. Keep the existing nodes green at every step.

1. **Declare the node.** Add the version to `settings.gradle` (`stonecutter { create(rootProject) { versions("1.21.1",
   "26.1.2", "<new>"); … } }`). Add the per-node values to the **root** `gradle.properties` (the `versions/` tree is
   gitignored + regenerated, so values can't live there): `mc_<v>`, `neo_<v>`, `java_<v>`, and — only if published for
   that MC — `parchment_mc_<v>`/`parchment_<v>`, `patchouli_<v>`, `modonomicon_<v>`. `build.gradle` already selects them
   by `mcv = project.name`; Patchouli/Parchment are wrapped in `if (project.hasProperty("…_${mcv}"))` so an unpublished
   one just skips.
2. **Run the decompile.** `./gradlew :<v>:compileJava` triggers the NeoForm download + decompile (cached after). It
   won't compile until the directives below exist — that's expected; the node flip is the *start* of the compat work.
3. **Drive the compile errors into `compat`.** Recompile, read the per-symbol histogram (lift `-Xmaxerrs` if it caps at
   100), fix a cluster, repeat to zero. **Expected directive sites (almost all in `compat`):** `Ids` (the id type),
   `Lookup` (registry access), `Jigsaw`, `Entities` (create/place/spawn-reason), `Players`. Per-file residual: the
   entity NBT rewrite, `SavedData`→Codec, the recipe API, the `LootModifier` codec, GameRules→registry, the
   spawn/respawn API, the client model/key APIs, the mob-class subpackage reorg, and 1-offs — see the §2.7 catalog.
   Each is a `//? if >=<v> { /*new*/ //?} else { current //?}` block — **the current branch stays uncommented +
   Javadoc-free** so the existing node keeps compiling.
4. **Gametests.** If the framework changed (it did at 1.21.5: `@GameTest`/`@GameTestHolder` → `GameTestInstance` +
   `RegisterGameTestsEvent`), add a `gametest_<v>` source set and a per-node exclude in `build.gradle`; port bodies
   from the nearest suite (the `GameTestHelper` API is stable). Otherwise reuse the existing suite. **Each node's
   golden-master fingerprints stay frozen** as that node's regression witness (the suites themselves keep growing
   with content).
5. **Content / data.** Most ships to every node for free — the codecs store raw `String` ids and **skip-with-log**
   on an unknown id (proven by `unknownThemeIdsFallBack`). For content only meaningful on the new node, use the
   **modern-only gating pattern** (§2.8). The residual hard cases (a renamed vanilla id, a shifted worldgen codec)
   take a guarded data variant or a `//?`.
6. **Verify.** `:<v>:build` + `:<v>:runGameTestServer` green, **every other node still green**, then `./gradlew
   chiseledBuild` / `chiseledRunGameTestServer` for all nodes. **No CI edit is needed** — `.github/workflows/build.yml`
   runs the chiseled tasks over every node in `settings.gradle`, and the node's JDK is auto-provisioned (foojay), so the
   new node is picked up automatically. Capture a **per-node golden master** (the gametest logs
   `[golden] CAPTURE` when an entry is missing — lock the printed fingerprints).

---

## Risks & things to verify (the live ones)

- **NeoForge API churn is the real work** of any new node. The network/payload + registration APIs move meaningfully
  between NeoForge versions — expect most directives there; the `compat` concentration pays off before adding a
  version. *(The Stonecutter ↔ ModDevGradle integration risk is retired — proven on two nodes.)*
- **Mappings across versions.** Each node pins its own Parchment version; Mojmap names are stable, but Parchment
  **parameter** names can shift — keep the source on names common to all targets, or directive the rare diff.
- **Golden-master fingerprints are per-version (#57 — realized; contingency remains).** Each node has its own suite
  with its own GOLDEN fingerprint map, which is the per-version regression guarantee. Contingency: if a *shared*
  suite ever exists, it would need an in-test version-keyed expected map (directive or constant).
- **Per-version data (#58 — handle when it bites).** `.nbt` + datapack JSON are mostly version-stable, but a vanilla
  block-id rename in a future version would need a guarded per-version data variant. Zero demand so far — every
  rename encountered was absorbed by the tolerant data or a Java-side `//?` (e.g. `CHAIN`→`IRON_CHAIN`).

---

## Definition of done

- Stonecutter builds ≥2 versions; `chiseledBuild` yields a jar per version and each passes its gametests. ✅
- The version-volatile calls live in `compat` or in named per-file `//?` blocks; the **algorithm and the data model
  carry no version directives**. ✅ *(As-built: the facade absorbs the bulk; ~28 files carry accepted per-file
  residual directives — entity NBT, SavedData, recipes, client, structure templates.)*
- No new runtime dependency; still NeoForge-only. ✅
- Adding a version is "a Stonecutter node + a handful of `compat` directives", documented. ✅ (the recipe above)
- Open: routing the gametest suites through `compat` (#56) and any further nodes (#59).
