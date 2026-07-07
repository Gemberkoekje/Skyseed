# MODPLACEMENTTESTPLAN.md — "actually placed" integration tests for optional-mod content

Skyseed's optional-mod compat is **data-driven and inert without the mod**: a theme / theme_override
references a modded block or liquid by id, and when the mod is absent the id doesn't resolve, so the
content is skipped (`OrePlanner.planOres` and `IslandGenerator.resolveBlock` both gate on
`Lookup.hasBlock`). The gametest suites verify this at the **resolved-data layer** (e.g.
`createZincCompatTargetsRocky` checks `create:zinc_ore` is in rocky's resolved ore list).

This plan tracks the complementary tests that load the **real mod** and assert the block/liquid is
**physically placed in a generated island** — the difference between "the id `create:zinc_ore` is in the
data" and "an actual Create zinc-ore block landed in the world".

**Precedent (done, CI-green):** Create — see `createZincOreActuallyPlaces` /
`createZincIsInertWithoutCreate` in `gametest/SkyseedGameTests.java`, the `-PwithCreate` profile in
`build.gradle` + `gradle.properties`, and the `create-integration` job in `.github/workflows/build.yml`
(PR #51). Everything below generalises that.

---

## 1. The three placement mechanisms (a test differs by which one)

Modded content reaches an island through three different passes, so the "look for the block" step of a
placement test depends on the content type:

| Mechanism | Pass | Block/liquid kinds | How to force/observe it |
| --- | --- | --- | --- |
| **Ore** | `OrePlanner` grows veins through the **core** | ores (Create zinc, IE ores, MA ores, Quark stones, Iron's mithril, AE2 certus) | Plan the relevant theme; scan `plan.blocks()` for the id. **The gametest region is at Y ≈ -60**, so rocky/ancient hit the `max_y:8` **deep band** → you get the *deepslate* variant. Match `create:*zinc*` etc., not the exact shallow id. |
| **Ground decoration** | surface/scatter pass | BWG flowers, FD wild crops, Quark blossom saplings, MND powdery cane, ED chorus succulent | Plan a theme whose surface band carries the decoration; scan `plan.blocks()` (or `scatterPositions`) for the id. Existing helpers `groundHasNamespace` / `topGroundHasNamespace` show the data side. |
| **Liquid** | pond / lava-lake pass (`PondCarver`) | `immersivepetroleum:crudeoil_fluid_block` | Plan the theme whose `pond`/lava band uses the mod fluid; scan `plan.blocks()` / `fluidTicks` for the fluid block. |

All three are observable the same way — plan an island and inspect `IslandPlan.blocks()` — only the
theme/seed selection and the id you match differ.

---

## 2. Reusable recipe to add a mod (generalised from Create)

For a mod `<mod>` published on a node (today: **1.21.1 only** — no content mod ships for 26.1.2 yet):

1. **Versions → `gradle.properties`** (keyed per node, easy to bump; they drift with releases):
   `<mod>_1.21.1=<ver>` plus one line per transitive dep.
2. **`build.gradle`** — under `if (project.hasProperty('with<Mod>') && mcv == "1.21.1")`:
   - add the mod's maven repo(s) in `repositories { … }` (guarded the same way, so a normal build never hits them);
   - `localRuntime` the mod **and every transitive dep** (runtime-only — Skyseed compiles against none of them).
     ⚠️ **Modrinth / CurseMaven jars carry NO transitive-dependency metadata**, so each required lib must be
     listed explicitly (this is why Create needed Flywheel + Ponder + Registrate spelled out). Prefer the mod's
     own maven when it has one — it publishes transitive info.
3. **Gametest(s)** in `gametest/SkyseedGameTests.java` (1.21.1 suite):
   - `<mod><Thing>ActuallyPlaces` — `if (!ModList.get().isLoaded("<mod>")) { helper.succeed(); return; }`, then plan
     the theme and assert the real block/liquid is placed. `@GameTest` methods are always discovered, so it must
     self-skip when the mod is absent (the normal/CI run).
   - optionally the verifiable-now companion `<mod><Thing>IsInertWithout<Mod>` — self-skips when the mod *is* loaded.
   - Match by **namespace + keyword** (`id.startsWith("<mod>:") && id.contains("<thing>")`) so a deep-band deepslate
     variant or an upstream id rename doesn't silently break it.
4. **Gate pre-existing "inert-without-the-mod" tests.** Loading a real mod flips assertions that assume it's
   absent. For Create this was exactly one test (`createRareGateIsInertWithoutMod`, a `requires:[create]` rare-gate);
   it now self-skips when `create` is loaded. **When adding a mod, run the full suite with it once and gate every
   newly-failing "inert" test the same way** — grep the suite for the mod id and for `requires:[<mod>]`.
5. **CI** — add a job like `create-integration` (or extend a shared one) running
   `./gradlew :1.21.1:runGameTestServer -Pwith<Mod> --stacktrace --no-daemon`. Keep it a **separate, PR-only job**
   so the mod's dependency graph can never gate jar publishing.

**Standing caveats:** 1.21.1 only until content mods publish for 26.1.2; the Y ≈ -60 deep-band rule for ores;
transitive deps must be explicit; heavy graphs (Create, AE2, BWG) slow CI — keep each behind its own `-Pwith<Mod>`
(or a combined `-PwithContentMods`) and its own job.

---

## 3. Inventory — every mod block & liquid referenced by the theme data

Extracted from `data/skyseed/skyseed/theme` + `theme_override`. **Liquids are called out; everything else is a
block.** "Kind" = which pass places it (§1). Coordinates are **starting points to verify** per mod (as Create's
were) — check the mod's "depending on…" docs / maven listing for exact current versions.

### 3.1 Create — ✅ DONE (CI-green)
- **Blocks:** `create:zinc_ore`, `create:deepslate_zinc_ore` (ore).
- **Themes:** `theme_override/create_{rocky,ancient,rocky_large,huge_rocky,ancient_large,huge_ancient}.json`.
- **Load:** `maven.createmod.net` (Create, Ponder, Flywheel) + `maven.ithundxr.dev/snapshots` (Registrate);
  deps `com.simibubi.create:create-1.21.1:slim` (transitive=false) + `net.createmod.ponder:ponder-neoforge` +
  `dev.engine-room.flywheel:flywheel-neoforge-1.21.1` + `com.tterrag.registrate:Registrate`.
- **Status:** implemented (`-PwithCreate`, `create-integration` job). Template for everything below.

### 3.2 Immersive Engineering — ore (low risk, mostly standalone)
- **Blocks:** `immersiveengineering:ore_{aluminum,lead,nickel}`,
  `immersiveengineering:deepslate_ore_{aluminum,lead,nickel,silver,uranium}` (ore).
- **Themes:** `theme_override/immersiveengineering_*` (6 files).
- **Load:** `https://maven.blamejared.com` → `blusunrize.immersiveengineering:ImmersiveEngineering:<1.21.1-ver>`.
  Few/no heavy transitive deps on NeoForge 1.21.1.
- **Test:** ore pass; deep band → deepslate variants. Plan `ancient`/`rocky`.

### 3.3 Immersive Petroleum — **LIQUID** (needs IE)
- **Liquid:** `immersivepetroleum:crudeoil_fluid_block` (pond/lava pass) — referenced in
  `theme_override/immersiveengineering_ancient.json`.
- **Load:** requires **Immersive Engineering** (§3.2) first; IP jar via `https://cursemaven.com`
  (`curse.maven:immersive-petroleum-<projId>:<fileId>`) or Modrinth maven. Verify a 1.21.1 IP build exists.
- **Test:** the one **liquid** case — plan the crude-oil theme and assert the fluid block is placed in the pond.

### 3.4 Applied Energistics 2 — ore/quartz (needs GuideME)
- **Block:** `ae2:quartz_block` (certus, ore pass).
- **Themes:** `theme_override/appliedenergistics2_*` (6 files) — note file prefix `appliedenergistics2_`, block ns `ae2:`.
- **Load:** `https://modmaven.dev` → `appeng:appliedenergistics2:<19.2.x>` (**transitive: GuideME is required** by
  recent AE2). Alt: Modrinth maven `maven.modrinth:ae2:<versionId>` — then add GuideME explicitly (no transitive metadata).
- **Test:** ore pass; the deep band may not carry certus, so plan the mining theme/band that does.

### 3.5 Mystical Agriculture — ore (needs Cucumber)
- **Blocks:** `mysticalagriculture:{inferium,prosperity,soulium}_ore`,
  `mysticalagriculture:deepslate_{inferium,prosperity}_ore` (ore).
- **Themes:** `theme_override/mysticalagriculture_*` (8 files).
- **Load:** BlameJared / CurseMaven → `com.blakebr0.mysticalagriculture:MysticalAgriculture:<ver>` +
  **`com.blakebr0.cucumber:Cucumber:<ver>`** (required lib).
- **Test:** ore pass; ancient (deepslate variants) per `mysticalAgricultureCompatTargetsAncient`.

### 3.6 Quark — stones + geodes + blossom saplings (needs Zeta) ⚠️ availability risk
- **Blocks:** `quark:{limestone,jasper,shale}` (stone/ore bodies), `quark:blue_corundum` (geode),
  `quark:myalite` (End form), `quark:{blue,orange,red,yellow,lavender}_blossom_sapling` (decoration).
- **Themes:** `theme_override/quark_*` (9 files).
- **Load:** `https://maven.blamejared.com` → `org.violetmoon.quark:Quark:<ver>` +
  **`org.violetmoon.zeta:Zeta:<ver>`**. ⚠️ **Quark 1.21.1 was alpha/supporter-only** at last check — confirm a
  public maven build exists before committing coordinates.
- **Test:** mixed — stones/geodes via the **ore** pass, blossom saplings via the **decoration** pass.

### 3.7 Iron's Spells 'n Spellbooks — ore (needs GeckoLib; **in base themes**)
- **Blocks:** `irons_spellbooks:mithril_ore`, `irons_spellbooks:deepslate_mithril_ore` (ore).
- **Themes:** referenced in **base** themes `theme/{rocky,rocky_large,huge_rocky,ancient,ancient_large,huge_ancient}.json`
  (not just overrides).
- **Load:** CurseMaven / Modrinth (`maven.modrinth:irons-spells-n-spellbooks:<versionId>`) +
  **GeckoLib** (`software.bernie.geckolib:geckolib-neoforge-1.21.1:<ver>`, cloudsmith/blamejared).
- **Test:** ore pass; deep band → `deepslate_mithril_ore`.

### 3.8 Farmer's Delight — wild crops / ground decoration (standalone)
- **Blocks:** `farmersdelight:wild_{beetroots,cabbages,carrots,onions,potatoes,rice,tomatoes}` (decoration/ground).
- **Themes:** `theme_override/farmersdelight_*` (15 files).
- **Load:** CurseMaven / Modrinth (`maven.modrinth:farmers-delight:<versionId>`) — standalone on NeoForge 1.21.1.
- **Test:** **decoration** pass — plan the forest/meadow tiers the wild crops merge onto (see
  `farmersDelightCropsMergeOntoForestTiers`, `…RiceReachesLushPonds`); scan for the ground block.

### 3.9 My Nether's Delight — powdery cane (needs FD)
- **Block:** `mynethersdelight:powdery_cane` (decoration; **bonemealable → `IslandPlan.GrowSpot`**).
- **Themes:** `theme_override/mynethersdelight_*` (4 files), incl. `mynethersdelight_nether_forest.json`.
- **Load:** requires **Farmer's Delight** (§3.8); jar via CurseMaven / Modrinth.
- **Test:** decoration/grow-spot pass on the nether seeds; also a chance to assert the `GrowSpot` advance.

### 3.10 End's Delight — chorus succulent (needs FD)
- **Block:** `ends_delight:chorus_succulent` (decoration).
- **Themes:** `theme_override/endsdelight_*` (1 file; block ns `ends_delight:`, file prefix `endsdelight_`).
- **Load:** requires **Farmer's Delight** (§3.8); jar via CurseMaven / Modrinth.
- **Test:** decoration pass on the chorus-forest / End seed (see `endsDelightSucculentReachesChorusForest`).

### 3.11 Oh The Biomes We've Gone (BWG) — ~49 flowers / ground decoration (large mod)
- **Blocks (decoration, ground pass):** ~49 flower/plant blocks — the allium family
  (`{pink,tall,tall_pink,tall_white,white}_allium`, `{pink,white}_allium_flower_bush`, `allium_flower_bush`),
  roses (`rose`, `black_rose`, `blue_rose_bush`, `cyan_rose`, `osiria_rose`), the amaranths
  (`{cyan,magenta,orange,purple}_amaranth`, `amaranth`), sages (`{blue,purple,white}_sage`), tulips/daffodils/anemones,
  and specials (`blooming_aloe_vera`, `fairy_slipper`, `firecracker_flower_bush`, `foxglove`, `guzmania`, `incan_lily`,
  `iris`, `japanese_orchid`, `kovan_flower`, `lollipop_flower`, `protea_flower`, `richea`, `silver_vase_flower`, …).
  Full set is derivable from `rg '"block": "biomeswevegone:' theme_override`.
- **Themes:** `theme_override/biomeswevegone_*` (18 files). NB BWG also contributes **trees (features)** and
  **villages/structures (jigsaw pools)** — those are separate concerns, **not** blocks/liquids, and out of scope here.
- **Load:** CurseMaven / Modrinth (`maven.modrinth:biomes-weve-gone:<versionId>`); large mod — verify its own libs.
- **Test:** decoration pass. One representative flower per band is enough to prove real placement; don't test all 49.

---

## 4. Suggested phasing (easiest / highest-value first)

| Phase | Mods | Why |
| --- | --- | --- |
| 0 ✅ | **Create** | done — the template + CI-green proof |
| 1 | **Immersive Engineering**, **Farmer's Delight** | standalone (no/thin transitive deps), clean ore + decoration exemplars |
| 2 | **Mystical Agriculture** (+Cucumber), **Iron's Spells** (+GeckoLib), **AE2** (+GuideME) | one extra lib each; all ores |
| 3 | **My Nether's Delight**, **End's Delight**, **Immersive Petroleum** | addons — need their base (FD / IE) from Phase 1 first; ED/MND cover decoration+grow-spot, IP covers the sole **liquid** |
| 4 | **Quark** (+Zeta), **BWG** | availability/size risk — Quark 1.21.1 maven may be alpha-only; BWG is large. Do last. |

Each phase is independent behind its own `-Pwith<Mod>` and CI job (or fold into one `-PwithContentMods` +
`content-integration` job once several are stable).

---

## 5. Open questions / caveats

- **26.1.2 node:** every content mod above is 1.21.1-only today. Revisit each as it publishes for 26.1.2; until then
  the placement tests stay `mcv == "1.21.1"`-gated and the 26.1.2 side keeps only the resolved-data tests.
- **Transitive metadata:** Modrinth/CurseMaven jars don't declare their deps — every lib must be explicit. A mod that
  fails to boot in the `*-integration` job almost always means a missing transitive lib (or a wrong version); the job
  log's "Missing or unsupported mandatory dependencies" / `NoClassDefFoundError` names it.
- **Deep band (Y ≈ -60):** ore tests must accept the deepslate variant (proven with Create). Decoration/liquid tests
  need a theme+seed that actually rolls the surface band / pond carrying the mod content.
- **Newly-failing inert tests:** budget one audit pass per mod (grep the suite for the mod id + `requires:[<mod>]`),
  as `createRareGateIsInertWithoutMod` needed for Create.
- **CI cost:** each mod adds a full 1.21.1 gametest run (~2 min once deps are cached). Prefer a single combined
  `content-integration` job over many once the set stabilises.
