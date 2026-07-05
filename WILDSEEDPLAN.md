# WILDSEEDPLAN — the Wild Skyseed becomes the default starter (adaptive, 5% builds)

> **Status: BUILT + VERIFIED 2026-07-04 — both nodes green** (`runGameTestServer`: 1.21.1 = 180/180, 26.1.2 = 182/182,
> both BUILD SUCCESSFUL). All parts below are implemented in the working tree (D1–D7 signed off 2026-07-04). Compiles on
> both nodes; the seed-coverage + resolver/`forcesRare` gametests pass (they now exercise the three Wild seeds). Remaining:
> an in-game throw-test (throw Wild over desert/mountains/snow → the right dedicated theme, builds only the ~5% of the
> time) and the user's git integration. Child-of-nothing — a self-contained feature that touches seeds/worldgen, recipes, advancements, both
> guide books, and the FTB questline. Reuses the **adaptive-seed engine already built for the Explore Skyseed**
> (`ExploreThemes` + the adaptive branch of `IslandSeedEntity.germinate`) and the per-theme rare-structure roll from
> [VARIETYSTRUCTUREPLAN](Modpack-growyourownworld/VARIETYSTRUCTUREPLAN.md).
>
> **Implemented:** ExploreThemes `WILD`/`WILD_LARGE`/`WILD_HUGE` markers + `resolveWildBase` (forest fallback) +
> `forcesRare`; the `germinate` force-gate; ModItems registration (Wild trio first) + `DEFAULT_SEED`; 4 recipes (Wild
> takes Forest's old planks+dirt; Forest→logs+dirt; nested Large/Huge with the no-iron kelp token); 10 advancements
> (incl. `gathered_forest`→logs and a new `reveal_forest` so Wild is the sole ungated root); 3 Patchouli entries + intro
> rewrite + Forest reframed/demoted; skyseeds tag; lang; 3 models + 3 self-drawn textures; the FTB seed-swap
> (skyseed.snbt/introduction.snbt tasks+icons, B106 lang); resolver + `forcesRare` + root-flip gametests in both suites.

## 0. Goal, in one paragraph

Today the cheapest, first-craftable seed is the **Forest Skyseed** (2×2 planks + dirt), and it *always* grows a
grass-and-trees island — even thrown over a desert it is a forest with sand scattered on top. The user wants the
default first seed to instead **read the biome it lands on and grow *that* biome's real island** — sand/cactus over
desert, stone over mountains, snow over snowfields — exactly like the **Explore Skyseed** does, but with only the
ordinary **~5% chance of a surprise building** instead of Explore's guaranteed one. That new seed is the **Wild
Skyseed**; it takes over the Forest seed's cheap recipe and becomes the new progression root. The **Forest Skyseed
stays** as a "force an always-wooded island" specialist, re-costed to **logs + dirt**. Large and Huge Wild Skyseeds
extend the same adaptive idea at the bigger tiers, with recipes that mix wood/dirt/sand/water to signal "this can
become anything."

## 1. Decisions (signed off 2026-07-04)

- **D1 — Name: "Wild Skyseed".** Theme sentinel `skyseed:wild` → item `skyseed:wild_skyseed`; tiers `skyseed:wild_large`
  ("Large Wild Skyseed") and `skyseed:huge_wild` ("Huge Wild Skyseed"). Fits the existing `<theme>_skyseed` naming with
  zero special-casing (unlike a bare `skyseed:skyseed`).
- **D2 — Behaviour: adaptive theme + *normal* rare-structure roll.** The Wild seed reuses the Explore resolver to grow
  the dedicated theme the local biome's own seed would grow, but does **not** force a build — it germinates through the
  standard path with `forcedRare = -1`, so each resolved theme's own `rare_structures` roll applies (~5% for overworld
  terrain themes per VARIETYSTRUCTUREPLAN **D5**). This is the *only* behavioural difference from Explore.
- **D3 — Tier recipes: nested, huge keeps the ender-pearl + blaze-powder gate.** Large = the base Wild Skyseed in the
  centre ringed by mixed raw mats; Huge = the Large Wild Skyseed in the centre with the **ender pearl + blaze powder**
  the other huge seeds require. Mirrors how every family tiers up (e.g. `forest_large` centres a `forest` seed;
  `huge_forest` = `TTT/ELP/TTT`).
- **D4 — Forest stays as a specialist; Wild is the new root.** The Wild Skyseed becomes the questline/book root and the
  first seed the intro points to. The Forest Skyseed keeps existing (re-costed to **logs + dirt**) as the "always grow a
  wooded/grassy island regardless of biome" seed, shown as a small follow-on entry. Forest is *not* removed — it stays
  genuinely distinct (grass surface + trees everywhere; the Wild seed instead grows the biome's real theme).
- **D5 — "Water" in the Large/Huge recipes is a no-iron token.** The mixed-mats recipes use a no-iron water-flavoured
  item (`minecraft:kelp`; `minecraft:clay_ball` the alt), **not** a `water_bucket` — so the Large tier isn't gated
  behind iron. (Resolved §12.1.)
- **D6 — FTB questline: swap, don't add nodes.** The chapter keeps its current shape; each forest-seed quest task/icon
  is simply repointed to the matching **Wild** seed at its tier — base `forest_skyseed` → `wild_skyseed`, large
  `forest_large_skyseed` → `wild_large_skyseed`, huge `huge_forest_skyseed` → `huge_wild_skyseed`. **No new Forest quest
  node** — Forest stays a *book* specialist, not a quest gate. (Resolved §12.2.)
- **D7 — Texture: bespoke, self-drawn motif.** The three Wild icons are bespoke — a small "wild/mixed" motif drawn
  programmatically via **PowerShell + `System.Drawing`** (16×16 PNG), not a placeholder or reused icon. (Resolved §12.3.)

## 2. How Wild differs from the two seeds it sits between

| | grows what | structure | fallback (unmapped/modded biome) |
|---|---|---|---|
| **Forest Skyseed** | *always* a grass+trees island; biome only tweaks tree species / sand-scatter (`forest.json` `biome_overrides`) | normal ~5% roll | n/a (fixed theme) |
| **Wild Skyseed** *(new)* | the **dedicated theme** the biome's own seed grows — `desert.json`, `rocky.json`, `frozen.json`, … | **normal ~5% roll** | **`forest`** (base) / `forest_large` / `huge_forest` — a plain wooded island, *no* forced build |
| **Explore Skyseed** | same dedicated theme as Wild | **forced** (guaranteed build, `pickFittingRare`) | **`explore.json`** (base, forces `chance:1.0` trail ruins) / `forest_large` / `huge_forest` |

The Wild and Explore seeds share the biome→theme resolution wholesale (`ExploreThemes.RULES`). They differ in exactly
two places: **whether a build is forced** (D2), and **the base-tier fallback theme** for an unmapped biome (Wild →
`forest`, so it never forces a build; Explore → `explore.json`, which does).

## 3. Part A — code (small, contained)

All in the existing adaptive-seed machinery. No new subsystem.

### A1. `ExploreThemes` — add the three Wild markers + a "forces a build?" predicate + a Wild base fallback

`worldgen/theme/ExploreThemes.java` (kept under its current name — it already documents itself as the adaptive
resolver; a rename to `AdaptiveThemes` is an *optional* cleanup that would touch `IslandSeedEntity` + both gametest
mirrors, not required):

- Add `WILD = Id.of("skyseed:wild")`, `WILD_LARGE = Id.of("skyseed:wild_large")`, `WILD_HUGE = Id.of("skyseed:huge_wild")`.
- `isAdaptive(Id)` → also `true` for the three Wild markers (so the entity takes the adaptive branch for them).
- `resolveFor(Id marker, Holder<Biome>)` → dispatch all six markers. Large/Huge Wild reuse `resolveLarge` /
  `resolveHuge` **as-is** (same `<family>_large` / `huge_<family>`, Forest as the unmapped fallback — identical to
  Explore's large/huge). Base Wild uses a **new** `resolveWildBase(biome)`:
  `Id.of("skyseed:" + (family(biome) == null ? "forest" : family(biome)))` — i.e. the dedicated theme, or **`forest`**
  (not `explore.json`) when nothing matches, so an unmapped biome grows a plain wooded island with the normal roll and
  no guaranteed build.
- Add `forcesRare(Id marker)` → `true` only for `MARKER` / `MARKER_LARGE` / `MARKER_HUGE` (Explore); `false` for the
  Wild markers.

### A2. `IslandSeedEntity.germinate` — gate the forcing on `forcesRare`

`entity/IslandSeedEntity.java:300` (the `if (adaptive)` block). Today it unconditionally resolves the theme *and*
forces a rare via `pickFittingRare`. Change to:

```java
if (adaptive) {
    theme = Themes.resolve(level.registryAccess(), ExploreThemes.resolveFor(getTheme(), biome));
    if (theme == null) { /* warn + fizzle, unchanged */ }
    force = ExploreThemes.forcesRare(getTheme())
            ? DebugForce.rare(pickFittingRare(level, theme, biome, base))   // Explore: guaranteed build
            : debugForce();                                                 // Wild: normal ~5% roll (forcedRare = -1)
}
```

`debugForce()` already carries `forcedRare = -1`, so the Wild seed falls straight through to the standard
`IslandGenerator.planIsland` → `rollRare` path. **No RNG-parity concern**: the Wild seed consumes the identical stream
any dedicated biome seed does for the resolved theme (it *is* that theme's normal germination).

### A3. `ModItems` — register the three seeds; make Wild the catalogue-leading default

`registry/ModItems.java`:

- Add `"wild", "wild_large", "huge_wild"` to `BASE_SEED_THEMES` — at the **front** of the list (before `forest`), so the
  creative tab / JEI show the Wild seed first as the primary starter. (List order is cosmetic for the auto-debug scan
  and coverage gametest, which iterate it.)
- Optionally repoint `DEFAULT_SEED = SEEDS.get("wild")` (the projectile's fallback display item) — thematically the Wild
  seed is now the canonical one. Low-stakes; the field is only a cosmetic fallback for a themeless thrown entity.

### A4. Item tag + no new theme JSON

- Add the three ids to the **`skyseeds` item tag** (so the guide recipe accepts them and any `#skyseed:skyseeds` logic
  includes them) — mirror wherever `explore` is listed.
- **No `wild.json` theme file** is needed: the Wild base falls back to the shipping `forest` theme, and large/huge to
  `forest_large` / `huge_forest`. (Explore needs `explore.json` only because *its* fallback forces a build; Wild's does
  not.) This is strictly less data than Explore.

## 4. Part B — recipes

Four recipe files under `recipes/data/skyseed/recipe/`. **The exact ingredient mixes for the Large/Huge tiers are the
one open review item (§8);** the shapes below are the proposal.

- **`wild_skyseed.json` — takes over Forest's *old* recipe** (2×2, inventory-craftable from the starter island):
  ```
  PP      P = #minecraft:planks
  DD      D = minecraft:dirt        → skyseed:wild_skyseed
  ```
- **`forest_skyseed.json` — re-costed to logs + dirt** (D4; still 2×2, still craftable off the start island's oak):
  ```
  LL      L = #minecraft:logs_that_burn   (matches forest_large's key)
  DD      D = minecraft:dirt              → skyseed:forest_skyseed
  ```
  *(No conflict with `wild_skyseed`: `#planks` and `#logs_that_burn` are disjoint tags.)*
- **`wild_large_skyseed.json` — nested, mixed mats** ("can be anything"):
  ```
  PSP     P = #minecraft:planks   (wood)
  WGD     S = minecraft:sand
  PSP     W = minecraft:kelp       (water token — no iron gate, D5; clay_ball the alt)
          D = minecraft:dirt
          G = skyseed:wild_skyseed      → skyseed:wild_large_skyseed
  ```
  Includes wood + sand + water + dirt around the base seed. No iron in the recipe (D5), so it's reachable as soon as
  you've grown an aquatic island for the kelp.
- **`huge_wild_skyseed.json` — nested, with the huge ender-pearl + blaze-powder gate** (D3; mirrors `huge_forest`'s
  `TTT/ELP/TTT`, ring diversified):
  ```
  DSD     D = minecraft:dirt
  EGB     S = minecraft:sand
  DWD     E = minecraft:ender_pearl
          G = skyseed:wild_large_skyseed
          B = minecraft:blaze_powder
          W = minecraft:kelp        (water token, matched to wild_large — no iron gate, D5)
                                        → skyseed:huge_wild_skyseed
  ```

## 5. Part C — advancements

Under `src/main/resources/data/skyseed/advancement/`. These drive the book's reveal/craft gating (mirror the
`forest` / `explore` advancement set).

- **New:** `gathered_wild.json` (inventory has `#minecraft:planks` + `minecraft:dirt` — i.e. **exactly the old
  `gathered_forest.json`**), `craft_wild.json` (`recipe_crafted skyseed:wild_skyseed`), plus `craft_wild_large.json` and
  `craft_huge_wild.json`. Add `gathered_wild_large` / `gathered_huge_wild` only if the book pages want the
  "hold-the-makings" reveal (match how forest_large's entry is gated).
- **Change:** `gathered_forest.json` must flip from planks+dirt to **`#minecraft:logs_that_burn` + `minecraft:dirt`** to
  match the re-costed recipe — otherwise the Forest crafting page reveals off the wrong items.
- **Optional consistency:** `reveal_explore.json`'s `prereq` (currently `recipe_crafted forest_skyseed`) could move to
  `wild_skyseed` now that Wild is the first seed; harmless to leave as-is (Forest is still craftable).

## 6. Part D — books (Patchouli source; Modonomicon auto-derives)

Only **Patchouli** is hand-authored (`src/main/resources/assets/skyseed/patchouli_books/guide/en_us/`); the
**Modonomicon** edition is regenerated from it by the `build.gradle` guide task (`patchouli:* → modonomicon:*`), so
**no Modonomicon files are edited** — just rebuild.

- **New entries** in `categories/overworld` (icon e.g. `minecraft:grass_block` or a compass-y icon): `wild_island.json`
  (sortnum `0`, the new lead entry, always-visible like the intro — `gathered_wild` on the crafting page, `craft_wild`
  on the field-notes pages), `large_wild_island.json`, `huge_wild_island.json`. Copy the structure of
  `forest_island.json` / `large_forest_island.json` / `huge_forest_island.json`, describing the **biome-matching**
  behaviour and the **~5% surprise building** (contrast: "unlike the Explore seed, the building is a lucky find, not a
  promise").
- **Rewrite `introduction.json`** ("Welcome, Skyfarer"): change "Start with the **Forest** Skyseed" → "**Wild**
  Skyseed", and reframe the "A living world" page — the Wild seed *becomes* the biome (desert→sand, mountains→rock),
  where the Forest seed is now the "always-wooded" choice.
- **Re-frame `forest_island.json`**: drop "The first seed you can make"; describe Forest as the specialist that forces a
  wooded island anywhere, and update its crafting page/`sortnum` to sit **after** Wild. Update its recipe reference
  (already `skyseed:forest_skyseed`, now logs+dirt — the `patchouli:crafting` page auto-renders the new recipe).
- Bump the `sortnum`s of the other overworld entries as needed so Wild leads.

## 7. Part E — FTB Quests (`Modpack-growyourownworld/overrides/config/ftbquests/quests/`)

The `chapters/skyseed.snbt` chapter is **already titled "Skyseed"** — re-rooting it on the Wild seed reads perfectly.
Per **D6, the questline keeps its exact current shape** — no new nodes; each forest-seed task/icon is repointed to the
matching Wild seed at its tier:

- **Swap the task items** in `skyseed.snbt`:
  - **B101** (`5C0000000000B101`, the root) task item `skyseed:forest_skyseed` → **`skyseed:wild_skyseed`**.
  - **B106** (`5C0000000000B106`) task item `skyseed:forest_large_skyseed` → **`skyseed:wild_large_skyseed`**.
  - **B116** (`5C0000000000B116`) task item `skyseed:huge_forest_skyseed` → **`skyseed:huge_wild_skyseed`**.
- **Swap the icons** that show a forest seed to the matching Wild seed: the chapter icon (`forest_large_skyseed` →
  `wild_large_skyseed`), the B102 icon (`forest_skyseed` → `wild_skyseed`), the B103 icon (`forest_large_skyseed` →
  `wild_large_skyseed`), and in `introduction.snbt` the chapter icon + root quest **B001** icon (`forest_skyseed` →
  `wild_skyseed`).
- **No Forest quest node** (D6): Forest is no longer required by any quest — it lives on only as a *book* specialist
  entry (§6). Nothing else in the chapter's dependency graph moves.
- **Lang** (`lang/en_us.snbt`): the chapter title stays "Skyseed". The descriptive lines that name the "Forest" seed
  (e.g. line 47 "different seeds grow different islands"; line 69 "the Large Forest comes from a stack of logs" —
  **still true**, Forest-large is unchanged) and the game-mechanic references (dark-forest evoker/Totem, wild crops on
  Forest/Meadow, BWG woods, chorus) **stay valid** — Forest still exists. No new node titles are needed since no nodes
  were added; optionally mention the Wild seed where natural.

## 8. Part F — assets (models / textures / lang)

- **Models** (`assets/skyseed/models/item/`): `wild_skyseed.json`, `wild_large_skyseed.json`, `huge_wild_skyseed.json`,
  each `parent: minecraft:item/generated` with `layer0` → a Wild texture (mirror the Explore family's per-tier textures:
  `island_seed_explore` / `_explore_large` / `island_seed_huge_explore`).
- **Textures** (`assets/skyseed/textures/item/`): bespoke icons (D7) — `island_seed_wild.png` + `_wild_large` +
  `island_seed_huge_wild`, a small "wild/mixed" motif suggesting "any biome" (e.g. a seed/sprout over a quartered
  patch of grass/sand/stone/water, tier-differentiated by size or a ring). Drawn **programmatically with PowerShell +
  `System.Drawing`** (`[System.Drawing.Bitmap] 16×16` → set pixels / draw shapes → `.Save(path, Png)`), so no external
  art tool is needed. Keep them readable at 16px and consistent with the existing seed icons' palette.
- **Lang** (`assets/skyseed/lang/en_us.json`): `item.skyseed.wild_skyseed = "Wild Skyseed"`,
  `item.skyseed.wild_large_skyseed = "Large Wild Skyseed"`, `item.skyseed.huge_wild_skyseed = "Huge Wild Skyseed"`.

## 9. Part G — gametests (both mirrors, per [[skyseed-structure-staging]])

Add to **both** `gametest/SkyseedGameTests` and `gametest_26_1_2/SkyseedTests`:

- **Seed-coverage:** the coverage test must treat `wild` / `wild_large` / `huge_wild` as **adaptive** (no fixed theme
  JSON of their own) exactly like `explore*` — find where the three Explore markers are excluded from the
  "every seed has a theme file" check and add the three Wild ids. They **do** need recipe + lang + model coverage (add
  those files, §4/§8), so they pass the rest of the sweep.
- **Resolver test** (extend `exploreSeedResolvesBiomesToDedicatedThemes` / add `wildSeedResolvesBiomesToDedicatedThemes`):
  assert `ExploreThemes.resolveFor(WILD, desert) == skyseed:desert`, `snowy_plains → frozen`, an **unmapped biome →
  `skyseed:forest`** (not `explore`), and `resolveFor(WILD_LARGE, desert) == desert_large`,
  `resolveFor(WILD_HUGE, desert) == huge_desert`.
- **No-force test:** assert `ExploreThemes.forcesRare(WILD*) == false` and `forcesRare(explore*) == true` — the one-line
  guarantee that the Wild seed leaves the ~5% roll alone.

## 10. Sequencing & release hygiene

Ship in one small PR (this is a modest, self-contained change), **per-step version bump + CHANGELOG entry** (the
standing #30 rule), both nodes green:

1. **Code (A1–A4)** — the resolver markers/predicate + the `germinate` gate + registration + tag. Add the resolver /
   no-force gametests here (they don't need `.nbt` regen).
2. **Data (B–F)** — recipes, advancements (incl. the `gathered_forest` flip), Patchouli entries + intro/forest rewrites,
   models/textures/lang. Rebuild to regenerate the Modonomicon edition.
3. **Quests (E)** — re-root `skyseed.snbt`, add the Forest specialist node + Wild-tier nodes, lang.
4. **In-game sign-off** (not runnable headless): throw a Wild seed over desert / mountains / snow / plains → the right
   dedicated theme grows, buildings are the occasional ~5% surprise (not every throw); the Large/Huge recipes craft; the
   book opens on the Wild entry and the questline roots on it.

No structure `.nbt` work is involved (no new buildings), so the [[skyseed-structure-staging]] regen dance is **not**
needed — only a normal rebuild for the Modonomicon derive + a golden-master re-run if the coverage test's seed list is
asserted anywhere.

## 11. Risks

- **RNG parity:** none new — the Wild seed germinates through the *exact* path a dedicated biome seed does (forcedRare
  = -1). It does not touch `pickFittingRare`. Confirm the `germinate` edit keeps the `else`-branch reaching `debugForce()`
  (which is `-1`), not accidentally forcing.
- **Fallback correctness:** the base-tier unmapped fallback **must** be `forest`, not `explore.json` — otherwise a Wild
  throw in a modded biome forces a guaranteed trail-ruins build (Explore's behaviour), defeating the "5% surprise"
  point. The resolver test's unmapped-biome assertion is the guard.
- **Coverage-test blind spot:** if the three Wild ids aren't added to the adaptive-exclusion list, the coverage sweep
  fails demanding a `wild.json` theme that shouldn't exist. Mirror `explore` precisely.
- **`gathered_forest` drift:** forgetting to flip it to logs leaves the Forest crafting page revealing off planks —
  cosmetic but wrong. Tie it to the recipe change in the same commit.
- **Recipe balance:** the water-bucket iron gate on Large/Huge (§4) may feel heavier than the Forest-large logs-only
  recipe. Decide the "water" token in the §8 review.

## 12. Open decisions — all resolved 2026-07-04

1. **Large/Huge recipe "water" token** → **RESOLVED (D5):** a no-iron token, `minecraft:kelp` (`clay_ball` the alt), not
   a water bucket. See §4.
2. **Wild-tier quest depth** → **RESOLVED (D6):** no new nodes — repoint the existing base/large/huge forest-seed
   tasks + icons to the matching Wild seed. See §7.
3. **Wild seed texture** → **RESOLVED (D7):** bespoke self-drawn "wild/mixed" motif, generated via PowerShell +
   `System.Drawing`. See §8.

Nothing outstanding — the plan is ready to build.
