# PLANOFPLANS — Skyseed prioritized backlog

**A single prioritized view across every planning doc in the repo.** This is the "what do we work on
next" index that sits on top of the nine individual plans; each item links back to the plan that owns
the detail.

> **Generated 2026-07-01** by a full plan-vs-repo audit: every plan doc was read, its open points
> extracted, and each point **verified against the actual code, both changelogs, `mods.txt`, and the
> modpack configs** before being kept. Items the plans still described as "pending" but that had
> actually shipped were dropped from this backlog and **checked off in their source plans** (see
> [Already shipped](#already-shipped-checked-off-in-the-source-plans)).

## Headline

- **~62 genuinely-open points** remain, consolidated into the ranked items below (plus the
  CODE_REVIEW follow-ups). *(2026-07-01: items #1, #3, #4, #7, #8, #9, #10, #12, #62, #63 resolved/shipped and #66 diagnosed — #9 now confirmed in-game and #12 decided full-scope — see the [Decisions log](#decisions-log).)*
- **31+ items were already shipped** and have been checked off in their plans — the docs read as far
  less finished than the repo actually is (CONTENTPLAN especially predates the void ChunkGenerator, the
  Create power backbone, Mystical Agriculture, and the six-chapter quest spine, all since shipped).
- The best value-per-effort sits entirely in the **top ~13 items** — cheap decisions and shipped-feature
  sign-offs that unblock everything downstream.

### Decisions log

- **2026-07-01 — item #12 RESOLVED: FULL structure scope.** User's call: resurrect **everything** — all 6 BWG
  village styles (across all their biomes) **and** the special structures (aspen manor + bog trial), not a
  villages-only first pass. This puts items **#14** (villages), **#26/#27** (manor/bog trial), **#28** (vertical-jigsaw
  mitigation), **#29** (on-pad assembly verification) and **#30** (per-step release hygiene) all in scope; prairie
  houses / rugged fossil (#49) stay optional polish. Sequencing (villages → manor+trial → polish) is unchanged.
  Recorded a **future want** (long-tail, not scheduled): *more* structures than BWG shipped — little Create sheds with
  small apparatus, abandoned Inferium farmlands, and similar bespoke flavour builds. Detail in
  [STRUCTUREPLAN § Scope decision](Modpack-growyourownworld/STRUCTUREPLAN.md).
- **2026-07-01 — item #9 fully DONE: create-otbwg milling confirmed in-game.** The flower placement shipped in v0.174.0
  (Meadow + Lush families) and v0.175.0 added a Forest-family flower sprinkle; user has now **verified in-game** that
  the BWG flowers grow on islands **and** that the create-otbwg milling recipes are present/working. The last remaining
  live-mill spot-check is done → #9 is closed end-to-end (datapack + in-game). Detail in
  [BWGPLAN § Step 3](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — Q2 (item #1) RESOLVED: distribute.** BWG wood/flora bands are distributed across the
  typed seed families, **priority-ordered per seed** (aquatic = water-first + biome trees second, forest =
  trees-first, lush = maximal/"extreme" nature, meadow = millable flowers, etc.). The *same* BWG biome may
  be adapted by more than one family, each with a different emphasis. File convention + mapping recorded in
  [BWGPLAN.md § Q2](Modpack-growyourownworld/BWGPLAN.md). **This unblocks items #7, #8, and #9.**
- **2026-07-01 — items #7 & #8 DRAFTED (pending id verification).** Wet-wood bands authored on the Aquatic
  family (water-first): new `biomeswevegone_aquatic{,_large,huge_}.json`. Fantasy-wood bands + a trees-first
  `cypress` multi-seed-demo overlap appended to the Forest family: `biomeswevegone_forest{,_large,huge_}.json`.
  All `biomeswevegone:` biome/feature ids are best-guesses flagged in each file's `_verify` — **must be
  confirmed against the BWG 2.6.0 jar, then add a gametest + version bump** before merge. Inert without BWG.
- **2026-07-01 — items #4 & #10 DONE (no jar / no in-game needed).** #4: fixed the `ci-skyseed.yml`→`build.yml`
  doc drift + the "matrix"→chiseled-fan-out description and the "add a version" recipe across README / REFACTORPLAN /
  CHANGELOG_26.1. #10: authored the Patchouli **"Exotic Woods"** guide entry (`entries/exotic_biomes.json`, basics
  category) for the shipped Forest-over-BWG loop — the Modonomicon edition auto-generates at build via `generateGuide`.
- **2026-07-01 — item #10 made BWG-conditional (the "only show when BWG is installed" trick).** The entry is gated by a
  Patchouli `mod:biomeswevegone` flag **and** a hidden `skyseed:reveal_exotic_woods` advancement that fires on obtaining
  any item in the new **inert** `#skyseed:exotic_woods` tag (all `{id, required:false}` → empty without BWG, so the
  advancement can never fire and the entry stays hidden on both the Patchouli and Modonomicon backends; `generateGuide`
  maps the entry `advancement` → a `modonomicon:advancement` condition). With BWG, growing an exotic wood reveals it.
  ⚠ The tag's `biomeswevegone:*_planks` ids are best-guesses (like the band ids) — verify against the jar on desktop.
- **2026-07-01 — items #7, #8 & #10 DONE (ids VERIFIED vs the BWG 2.6.0 jar; v0.171.0).** The jar is in the repo
  (`Modpack-growyourownworld/overrides/mods/Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar`), so every guessed id was
  confirmed against it and the drafts finalized. Corrections: willow → `bayou` biome / **`bayou_trees`** feature (no
  `willow_trees` exists); white-mangrove → **`white_mangrove_marshes`** (not `pale_bog`); **spirit IS growable** via
  **`pale_bog`** (there is no `spirit_woods` biome); palm keeps `rainbow_beach` (BWG injects `palm_trees` into vanilla
  beach, so a sandy BWG biome hosts it by design). `TreeEntry` resolves the `feature` against the **configured**-feature
  registry (confirmed in `Lookup.configuredFeature`). Tag (`exotic_woods.json`): dropped the non-existent
  `#biomeswevegone:planks`, `white_sakura_planks` → `sakura_planks`, `enchanted_planks` → `blue_enchanted_planks` +
  `green_enchanted_planks`. Added a `biomeswevegone_compat_prepends_aquatic_bands` gametest + extended the forest one
  (both nodes); **all 134 1.21.1 gametests pass, both nodes compile.** Remaining for these is only the in-game
  throw-a-seed sign-off (see checklist).
- **2026-07-01 — in-game sign-offs #6, #2, #5 all PASS** (user-tested, BWG installed). #6: no void-floor decoration
  leak, End central island generates. #2: oak + Zelkova (BWG) saplings grow to trees on islands **and** in Elite Botany
  Pots — BWG saplings work OOTB (no Potion Studios tree pack needed). #5: deepslate Inferium/Prosperity ore on Ancient +
  the essence→farmland→pot loop works. **New follow-up #62:** #5 surfaced that only the *deepslate* MA ores on Ancient
  shipped — MYSTICALPLAN §Fix also wanted the **stone** `inferium_ore`/`prosperity_ore` on a **Lush** island (both stone
  ores exist in MA 8.0.27); added as backlog item #62.
- **2026-07-01 — item #62 DONE (v0.172.0).** Shipped `mysticalagriculture_lush{,_large,_huge}.json` — the **stone**
  `inferium_ore`/`prosperity_ore` on the Lush island's stone `core` (Ancient keeps the deepslate variants: Lush =
  accessible tier, Ancient = deep richer tier). Confirmed via `IslandGenerator.eff` that top-level override ores do **not**
  leak into the Lush End/Nether forms (off the overworld home dimension → neutral empty-ore default), so the End "clean
  platform" island stays empty. Added the `mystical_agriculture_compat_targets_lush` gametest to both suites (asserts the
  stone ores are added and the deepslate ones are not). All gametests pass; both nodes compile.
- **2026-07-01 — plank-coverage audit surfaced new backlog item #63.** Enumerating the plank roster straight
  from `Oh-The-Biomes-Weve-Gone-NeoForge-2.6.0.jar` found **BWG ships 25 plank types but only 19 are obtainable**
  via the shipped Forest + Aquatic bands (and `#skyseed:exotic_woods`). Six are uncovered: **florus, holly, pine,
  mahogany, rainbow_eucalyptus** (all addable — each has a real configured tree feature + host biome) and **fir**
  (no configured tree feature exists in 2.6.0 → appears non-growable). So the in-game checklist line "every BWG
  plank is now obtainable" (#7/#8 sign-off) is currently **false**. Added as item **#63**; per-wood target
  features + host biomes recorded in [BWGPLAN.md § Plank coverage audit](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — in-game test pass (user-run) checked off 5 sign-offs and surfaced 4 new items.** ✅ PASS:
  crash-fix **5.1** (snow added exactly once), **5.2** (island persists across stop/restart), **5.3** (twin
  Nether island populates), the **PR #15 CI** (both nodes green), and **BWG inert without the mod** (no errors).
  New backlog items from the BWG wet/fantasy wood throws: **#64** wet-wood water feature is too small / wrong
  shape (deep round pond vs. the swamp/marsh these woods want); **#65** wet-wood tree density — Small tiers grow
  0 trees and **Huge Bayou grew 0 willow**; **#66** the **spirit band fails in-game** (a `pale_bog` throw gave
  oak/birch, no spirit trees — the band is in the shared source, so runtime-match/build, not 26.1.2-only); **#67**
  crash-fix observability (no way to see `MAX_DRAIN_TICKS` timeout or force-load ticket release). **#9** (create-otbwg
  millable flowers) reconfirmed **NOT implemented** — still no flower inputs on islands. Detail in
  [BWGPLAN § In-game test findings](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — item #63 DONE (v0.173.0) + item #66 DIAGNOSED (no change).** #63: the 5 remaining **growable** BWG
  planks shipped as dedicated-feature Forest-family bands (florus/holly/pine/mahogany/rainbow_eucalyptus ×3 tiers), all
  ids re-verified vs the 2.6.0 jar; `#skyseed:exotic_woods` extended; the forest gametest (both nodes) now locks the 5
  new biome→feature keys **and** guards that no band references a `fir_*` feature. **fir is the one documented
  non-growable plank** (2.6.0 ships `fir_planks`/`fir_sapling` but no configured fir tree feature) → **24/25 planks are
  now island-obtainable.** #66 (spirit fails in-game): root-caused as a **test mislabel, not a defect** — a *matched*
  `pale_bog` band **replaces** variants (verified in `IslandGenerator.eff`) so it emits only `spirit_trees` and can
  **never** yield oak/birch; the biome + feature both exist and spirit shares the working siblings' NBT feature type, so
  the oak/birch result means the seed wasn't over `pale_bog` (the report self-contradicts with "pale_bog → white
  mangrove"). Re-test over a confirmed `pale_bog` + verify its reachability; no band/code edit. Detail in
  [BWGPLAN § Plank coverage audit / In-game test findings](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — item #9 DONE (v0.174.0): millable BWG flowers now grow on islands.** The create-otbwg compat (94
  milling recipes) had **zero inputs** because no BWG flowers spawned. Two new `theme_override` families place them as
  island ground cover: **Meadow** (`biomeswevegone_meadow{,_large,_huge}`) adapts 8 BWG floral **grasslands**
  (allium_shrubland/amaranth_grassland/rose_fields/coconino_meadow/orchard/prairie/temperate_grove/firecracker_chaparral)
  and **Lush** (`biomeswevegone_lush{,_large,_huge}`) adapts 3 BWG **jungle** biomes (crag_gardens/tropical_rainforest/
  fragment_jungle) flora-first — the last two being the deliberate Q2 multi-seed overlap with the Forest family's
  trees-first mahogany/rainbow_eucalyptus. **Verification discipline:** every placed flower was confirmed to be BOTH a
  real BWG 2.6.0 block **and** a `create-otbwg-compat-1.0` milling *input* (cross-checked against both jars), so each
  island flower feeds a real recipe. Ground flora is per-column (no `tries`), so the 3 tier files per family are identical
  bands. New `biomeswevegone_compat_places_{meadow,lush}_flowers` gametests (both nodes) pass — 137/137 + 146/146.
  Only the in-game live-mill spot-check remains. Detail in [BWGPLAN § Step 3](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — item #3 DONE: the BWG quest branch shipped.** Three quests (**B701 Into the Wilds / B702 Mill the
  Blooms / B703 Grow Something Grand**) authored under the **Tools & Travel** chapter (`chapters/tools.snbt` + `lang/en_us.snbt`).
  **Into the Wilds auto-completes on obtaining any of the 24 exotic planks** via an FTB Quests **advancement task** on the
  hidden `skyseed:reveal_exotic_woods` advancement (criterion `has_exotic_wood`, `inventory_changed` on `#skyseed:exotic_woods` —
  originally built for the Patchouli guide-reveal gate, now reused so one hidden advancement drives both). **First attempt
  used an `ftbfiltersystem:smart_filter` item task** (`item_tag(skyseed:exotic_woods)`, reverse-engineered from the jars) —
  **it did NOT work: in-game FTB Quests treats the Smart Filter as a literal item to obtain**, not a tag expansion (user
  screenshot). So `ftb-filter-system` is not needed by this branch. Mill the Blooms + Grow Something Grand are checkmarks
  (a milled output / a grown tree aren't clean single item-ids). Root gated on Skyseed **B103** (grow a biome island); B702
  also on Create **B204** (Millstone). SNBT brace-balanced, ids collision-free. **Remaining:** the in-game quest-book
  test-load (checklist #3). See [BWGPLAN § Step 5](Modpack-growyourownworld/BWGPLAN.md).
- **2026-07-01 — tech backbone DECIDED: Immersive Engineering, not Mekanism (aesthetic).** User's call — Mekanism
  reads as too blocky/boring; IE's multiblock/diesel aesthetic is the headline. Consequences: **#11 resolved**;
  **#17 (Mekanism integration) dropped**; **#34 (IE) promoted to the primary tech backbone** (tech bootstrap = the
  **IE bauxite/aluminum island**). The **IE Excavator (#35)** must now be handled — it samples nonexistent void
  worldgen veins — with **two approaches to try (TODO, not built):** (1) patch it so its ore mix derives from the
  **island it's built on** (preferred — keeps the mechanic, on-theme; needs a mixin vs IE's `ExcavatorHandler`),
  or (2) **disable it entirely** (remove recipe + hide in JEI) and supply aluminum via the ore island. Detail in
  [CONTENTPLAN §7 + §2](Modpack-growyourownworld/CONTENTPLAN.md).

## How to read this

- **Priority** = high / medium / low (unblock-value and player-facing ROI, not raw importance).
- **Effort** = small / medium / large.
- **Status** = `genuinely-open` (nothing built), `partially-done` (base shipped, follow-up remains),
  `unclear` (needs a prerequisite before it's even actionable).

## Snapshot by plan

| Plan | What it covers | Open | ✅ Checked off |
|---|---|---:|---:|
| [BWGPLAN.md](Modpack-growyourownworld/BWGPLAN.md) | Oh The Biomes We've Gone integration | 7 | 7 |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration levers | 15 | 12 |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | BWG structures → growable islands | 9 | 2 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | 8 | 6 |
| [plannednotes.md](plannednotes.md) | Void ChunkGenerator, Trial Chamber, misc | 6 | 0 |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version (NeoForge + Stonecutter) refactor | 5 | 1 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visual/aesthetic pass | 4 | 9 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture + Botany Pots | 2 | 3 |
| [CODE_REVIEW.md](CODE_REVIEW.md) | 21 review findings (all fixed + merged via PR #15) | ~5 | — |

---

## Priority tiers (the readable view)

### Tier 1 — Quick unblockers & shipped-feature sign-offs *(do these first)*

Cheap decisions and validations that either unblock whole chains or confirm a flagship feature works
before more is piled on top.

1. ✅ **Resolve Q2** — ~~concentrate BWG wood bands on the Forest seed vs. distribute across typed seeds~~ **DECIDED 2026-07-01: distribute, priority-ordered per seed** (see [Decisions log](#decisions-log)). Items 7, 8, 9 are now unblocked.
2. ✅ **Verify OTYG sapling→tree growth** — DONE (2026-07-01): oak + Zelkova saplings grow to trees on islands and in Botany Pots, BWG saplings work OOTB (no Potion Studios pack needed). *(BWGPLAN · medium)*
3. ✅ **Build the BWG quest branch** (Into the Wilds / Mill the Blooms / Grow Something Grand) **under the Tools & Travel chapter** — DONE (2026-07-01): quests B701–B703 in `chapters/tools.snbt` + `lang/en_us.snbt`; Into the Wilds auto-completes via an **advancement task** on the hidden `skyseed:reveal_exotic_woods` (a smart-filter item task was tried first but FTB Quests treats the filter as a literal item), the other two are checkmarks; cross-gated on B103 + B204. Only in-game test-load remains. *(QUESTPLAN / BWGPLAN Step 5 · small)*
4. ✅ **Fix CI-file doc drift** — DONE (2026-07-01): corrected `ci-skyseed.yml`→`build.yml` and the "matrix"→chiseled-fan-out description across README/REFACTORPLAN/CHANGELOG; the "add a version" recipe now correctly says no CI edit is needed. *(REFACTORPLAN · small)*
5. ✅ **Verify Mystical Agriculture in-game** — DONE (2026-07-01): ore spawn + bootstrap loop confirmed (deepslate ores on Ancient; essence→farmland→pot works). Surfaced follow-up #62 (stone ores on Lush). *(MYSTICALPLAN · small)*
6. ✅ **Smoke-test the void ChunkGenerator with BWG installed** — DONE (2026-07-01): no features at y≈-64, End central island intact. *(plannednotes · small)*
10. ✅ **Patchouli "Exotic Woods" entry** — DONE (2026-07-01): authored `entries/exotic_biomes.json` (basics category) describing the already-shipped Forest-over-BWG loop; the Modonomicon mirror auto-generates at build. *(BWGPLAN · small)*
11. ✅ **Decide: one tech backbone or two** (Mekanism vs. IE) — **DECIDED 2026-07-01: Immersive Engineering; Mekanism dropped** (aesthetic). Cascades: #17 dropped, #34 promoted, #35 (Excavator fix) now in scope. *(CONTENTPLAN · small)*
12. ✅ **Decide structure scope** — **DECIDED 2026-07-01: FULL SCOPE** (all villages + aspen manor + bog trial; not villages-only). Puts #14/#26/#27/#28/#29/#30 in scope. Future long-tail want recorded: *more* structures than BWG shipped (Create sheds, abandoned Inferium farmlands, …). *(STRUCTUREPLAN · small)*

### Tier 2 — BWG content payoff & structures

Highest player-facing value once Q2 (1) and the village↔biome mapping (13) are settled.

7. ✅ **Wet-woods BWG bands** (cypress, willow, white-mangrove, palm) — DONE (2026-07-01, v0.171.0): ids verified vs BWG 2.6.0, willow/white-mangrove ids corrected. *(BWGPLAN · medium)*
8. ✅ **Fantasy-woods BWG bands** (enchanted, skyris, spirit) — DONE (2026-07-01, v0.171.0): spirit found growable via `pale_bog`. *(BWGPLAN · medium)*
9. ✅ **Verify create-otbwg milling recipes + place millable flowers on islands** — DONE (2026-07-01): flowers grow on islands (Meadow/Lush + the v0.175.0 Forest sprinkle) **and** the milling recipes are confirmed present/working in-game. Closed end-to-end. *(BWGPLAN · small)*
13. **Confirm BWG village style ↔ biome mapping** (esp. `salem`, `forgotten`) — *(STRUCTUREPLAN · small)*
14. **Resurrect BWG villages** (6 styles) via biome adaptation — highest-value structure work. *(STRUCTUREPLAN · medium, needs 13; scope ✅ full per 12)*
26–30. **Manor + bog-trial resurrection** with vertical-jigsaw gametests, on-pad assembly checks, and per-step golden-master discipline — **now in scope** (12 decided full); carries the bounding-box risk that broke the End City rebuild, so each gets its own placement gametest.

### Tier 3 — Content-mod wave & code follow-ups

Once the backbone decision (11) is committed, integrate in ROI order; each integration is followed by
its quest chapter and gated island tier.

15. **Curate Quark modules + add Zeta** — cheap building/QoL breadth. *(CONTENTPLAN · small)*
16. **Farmer's Delight** — wild crops on biome islands. *(CONTENTPLAN · medium)*
17. ~~**Mekanism**~~ — ❌ dropped (2026-07-01); **Immersive Engineering (#34)** is the tech backbone instead (aesthetic). *(CONTENTPLAN · large)*
18. **Applied Energistics 2** — certus + sky-stone bootstrap. *(CONTENTPLAN · medium)*
24. **Trial Chamber palette + lighting pass**, then atmosphere greebling (25) — batch them (same NBT regeneration; mind the stale-NBT trap). *(plannednotes · medium)*
- **CODE_REVIEW follow-ups** (see [dedicated section](#code_reviewmd-follow-ups-open-engineering-debt) — these are real, code-documented gaps the auto-ranker missed):
  - **5.3 force-load leak reconciliation** on server start — self-contained crash-robustness fix. *(medium)*
  - **5.2 full persist/resume** of in-progress jobs across world loads. *(large)*

### Tier 4 — Long tail / optional / future

Lower-value flavor mods (Critters & Companions 31, Productive Bees 32), optional polish (Distant
Horizons 21, resource packs 53/54, more Trial Chamber variants 33), already-settled decisions awaiting
formal confirmation (35/38/52), the **7 future quest chapters (40–47)** — each auto-gated behind its
mod's integration — rare version-divergence safeguards (57/58), a discretionary third version node
(59), the **future net-new bespoke structures (68)** — Create sheds, abandoned Inferium farmlands, … beyond BWG's own — and the manual smoke-tests for the merged crash fixes. See the full table for all of them.

---

## Dependency chains & sequencing

1. **BWG content chain (most actionable value; prerequisites all shipped):**
   Q2 (1) → wet-woods (7) + fantasy-woods (8) → remaining 6 planks (63) + create-otbwg flower placement (9).
   OTYG sapling-growth verification (2) is an independent correctness gate — run it first/alongside Q2.
   The BWG quest chapter (3) and Patchouli entry (10) are already unblocked and can ship immediately.

2. **Tech / content-mod chain:** backbone decision (11 ✅ = **Immersive Engineering**) → Quark (15) + Farmer's
   Delight (16) first (cheap), then **IE (34)** — gated on the **Excavator fix (35)** — then AE2 (18). *(Mekanism
   (17) dropped.)* Each integration is *followed by* its quest chapter (part of 19) and its gated gateway-island
   tier (20). FE cross-mod proof (39) is moot until a consumer mod lands.

3. **Structure resurrection chain (deferred child changeset, all NOT STARTED):** scope decision (12) ✅
   **DECIDED full-scope** — all villages + manor + bog trial. Villages (14) are the low-risk first step and
   need the style↔biome mapping (13). Manor (26) + bog trial (27) then pull in the vertical-jigsaw mitigation
   (28) + on-pad assembly verification (29) + per-step release hygiene (30). Prairie houses / rugged fossil
   (49) remain optional polish. A future long-tail adds *net-new* bespoke structures (Create sheds, abandoned
   Inferium farmlands, …) beyond BWG's own.

4. **Trial-chamber polish:** palette+lighting (24) is the highest-value pass; atmosphere greebling (25)
   shares the same generator + rebuild workflow (batch them); more layout variants (33) is large and
   lowest-urgency; the clean-rebuild-and-compare sign-off (61) is gated on all three.

**Suggested first session:** the cheap unblockers/decisions (1, 4, 11, 12) + the three shipped-feature
sign-offs (2, 5, 6) — any of the sign-offs could surface a real bug and they de-risk the modpack
release. Then the BWG band chain (7, 8, 9) plus its quest + guide (3, 10).

---

## 🎮 In-game verification checklist (for you to test)

Items that need a running client/server — I can't do these from here. They're pulled out of the backlog
so you have one list to work through. Tick them off as you go; several could surface a real bug, so the
sign-offs are worth doing before the next content lands on top.

**Shipped features — sign-off (do soon):**

- [x] **(#6) Void ChunkGenerator + BWG** — ✅ VERIFIED (2026-07-01, BWG installed): **no decoration leak** at the void floor and the **End central island still generates**.
- [x] **(#2) OTYG sapling→tree growth** — ✅ VERIFIED (2026-07-01): oak *and* **Zelkova (BWG)** saplings both grow to trees on an island **and** in an **Elite Botany Pot** (dirt) — got 3 oak / 2 zelkova logs. BWG saplings grow **out of the box**; the Potion Studios tree pack was **not** needed.
- [x] **(#5) Mystical Agriculture loop** — ✅ VERIFIED (2026-07-01): Inferium + Prosperity ore found (deepslate, on the **Ancient** island); **Inferium Seed + Inferium Farmland in an Elite Botany Pot grows as intended**. ✅ **Follow-up #62 SHIPPED (v0.172.0):** the **stone** `inferium_ore`/`prosperity_ore` now generate on the **Lush** island (`mysticalagriculture_lush{,_large,_huge}.json`), pairing with the deepslate variants on Ancient. *(In-game throw-a-Lush-seed spot-check still welcome, but the override + gametest are in.)*
- [x] **(#9) create-otbwg milling** — ✅ **DONE end-to-end (2026-07-01).** Flower placement shipped v0.174.0 (Meadow: 8 grassland biomes + Lush: 3 jungle biomes) and v0.175.0 added a Forest-family flower sprinkle; every flower verified as a real BWG 2.6.0 block **and** a create-otbwg milling input, gametest-guarded on both nodes. **User has now confirmed in-game** that the flowers grow on islands **and** that the create-otbwg milling recipes are present/working (harvested flower → mills to its dye/petal output). Closed.

**Crash-fix smoke tests (CODE_REVIEW 5.1–5.3 — base fixes merged; confirm live behaviour):**

- [x] **(5.1)** Deferred island-finalization state machine — ✅ VERIFIED (2026-07-01 in-game): snow is added **exactly once** — the finalize pass is neither skipped nor double-run.
- [x] **(5.2)** Drain-on-stop — ✅ **island completes + persists** (2026-07-01 in-game): a large island was fully grown after a server stop + restart/relog. ⚠ **`MAX_DRAIN_TICKS` adequacy still unverified** — there's no visible signal for the *inadequate* case; needs a diagnostic (see **#67**).
- [x] **(5.3)** Force-load twin islands — ✅ the **player-less Nether-side island fully populates** (2026-07-01 in-game). ⚠ **force-load ticket release unverified** — user has no way to observe ticket state; needs a diagnostic / `/forceload query` (see **#67**).
- [x] Confirm the merged **CI run (PR #15, both nodes) went green** — ✅ VERIFIED (2026-07-01): all green, both nodes.

**New BWG wet/fantasy wood content — ids now VERIFIED vs BWG 2.6.0 (2026-07-01, v0.171.0); these throw-a-seed sign-offs remain (#7/#8):**

- [ ] **TESTED (2026-07-01) — issues found.** Throw an **Aquatic** seed over `cypress_swamplands` (water-first): cypress trees **Small 0 / Large 1 / Huge 5**, and the **Huge pond is too small**. Water-first priority reads, but tree counts are too low (Small = 0) and the water feature is undersized → **#64** (water feature) + **#65** (tree density).
- [ ] **TESTED (2026-07-01) — issue found.** Throw a **Forest** seed over the *same* `cypress_swamplands` (trees-first): cypress **Small 0 / Large ~5 / Huge ~8**. Multi-seed priority reads correctly (denser than the Aquatic form), but **Small = 0 trees** — should guarantee ≥1 (raise tries and/or the minimum island size) → **#65**.
- [ ] **TESTED (2026-07-01) — mostly works; 2 bugs + the plank gap.** Wet woods (cypress/willow/white-mangrove/palm) + fantasy woods (enchanted/skyris/spirit): **enchanted ✅, skyris ✅, white-mangrove ✅, palm ✅** (mangrove/palm grew plenty of trees). Open issues: **(a)** all wet-wood islands have **too little water** — a deep round pond is the wrong feature for swamp/marsh woods → **#64**; **(b)** **Huge Bayou (willow) grew 0 trees** and Small tiers grow 0 → **#65**; **(c)** **Spirit ✗** — a seed over `pale_bog` produced only **oak & birch, no spirit trees** → **#66** *(✅ 2026-07-01 DIAGNOSED — a matched `pale_bog` band replaces variants so it can only emit spirit_trees, never oak/birch; ∴ the seed wasn't over `pale_bog`. Re-test over a confirmed pale_bog; no code change)*. Plus **"every plank obtainable" was only 19/25** → **#63** *(✅ 2026-07-01 SHIPPED v0.173.0 — now 24/25; fir is the documented non-growable exception)*. *(Label note: user reported "Pale Bog → white mangrove" in the bulk test but "pale_bog → oak/birch" for spirit — confirms the mislabel; re-test each biome singly.)*
- [x] Confirm the drafts are **inert with BWG NOT installed** — ✅ VERIFIED (2026-07-01): no errors observed; generation unchanged.

**Quests — after authoring:**

- [ ] **(#3) BWG quest branch** (under Tools & Travel) loads in-game — quest-book test-load; tasks/rewards resolve.
- [ ] **(#10) "Exotic Woods" guide page** — confirm it is **hidden** on a world with **BWG absent**, and **appears** (in Getting Started) once BWG is installed and you've obtained a BWG plank (the hidden `skyseed:reveal_exotic_woods` advancement fires). Check both the Patchouli book and the Modonomicon Almanac. *(The `#skyseed:exotic_woods` tag plank ids are now verified vs BWG 2.6.0 — v0.171.0.)*

**Balance & polish — observe during a normal playthrough:**

- [ ] **(#51 / #45) MA balance** — watch **Growth Accelerator stacking** and **mob Inferium drop rates**; tune `mysticalagriculture-common.toml` if crops get too fast or drops too generous.
- [ ] **(#61) Trial Chamber** — after the palette/atmosphere pass, do a **clean rebuild and visual comparison to a vanilla trial chamber** (mind the stale-NBT Stonecutter trap).

**Future — blocked until the mod is installed:**

- [ ] **(#39)** Prove **FE flows Create → Mekanism/IE/AE2** across islands (only testable once an FE-consumer mod lands).

---

## Full ranked backlog

| # | Item | Plan | Priority | Effort | Status | Why (ranking rationale) |
|---|---|---|---|---|---|---|
| 1 | ✅ **RESOLVED** — Q2: **distribute** BWG bands across typed seeds, priority-ordered per seed (aquatic=water-first, forest=trees-first, lush=extreme nature); same biome may appear in multiple families. See BWGPLAN §Q2. | BWGPLAN.md | high | small | ✅ done (2026-07-01) | Was the top blocker; now decided — the wet/fantasy wood-band authoring (#7/#8) and millable-flower placement (#9) are unblocked. |
| 2 | Step 2 — Verify OTYG sapling→tree growth for vanilla + BWG saplings | BWGPLAN.md | high | medium | ✅ done (2026-07-01) | VERIFIED in-game: oak + Zelkova (BWG) saplings grow to trees on islands and in Elite Botany Pots (3 oak / 2 zelkova logs); BWG saplings grow OOTB — the Potion Studios tree pack was not needed. |
| 3 | Build the BWG quest branch (Into the Wilds / Mill the Blooms / Grow Something Grand) **under the Tools & Travel chapter** (BWGPLAN Step 5) | QUESTPLAN.md / BWGPLAN.md | high | small | ✅ done (2026-07-01) | SHIPPED: quests **B701–B703** in `chapters/tools.snbt` + text in `lang/en_us.snbt`. **Into the Wilds** (B701) auto-completes via an **advancement task** on the hidden `skyseed:reveal_exotic_woods` (criterion `has_exotic_wood`, on `#skyseed:exotic_woods`) — a `ftbfiltersystem:smart_filter` item task was tried first but FTB Quests treats the filter as a literal item to obtain (confirmed in-game). **Mill the Blooms** (B702, dep B701+B204 Millstone) + **Grow Something Grand** (B703, dep B701) are checkmarks. Root gated on Skyseed **B103**. SNBT balance-checked; ids collision-free. In-game test-load = checklist #3. |
| 4 | Reconcile plan/changelog CI-file references with the actual workflow (documentation drift) | REFACTORPLAN.md | medium | small | ✅ done (2026-07-01) | DONE — fixed `ci-skyseed.yml`→`build.yml` + "matrix"→chiseled-fan-out in README/REFACTORPLAN/CHANGELOG; "add a version" recipe now says no CI edit needed. |
| 5 | Verify the MA integration in-game (ore spawn + bootstrap loop) | MYSTICALPLAN.md | medium | small | ✅ done (2026-07-01) | VERIFIED: deepslate Inferium + Prosperity ore on Ancient; Inferium Seed + Inferium Farmland in an Elite Botany Pot grows as intended. Surfaced follow-up #62 (stone ores on Lush). |
| 6 | Runtime smoke test of void ChunkGenerator with BWG installed | plannednotes.md | medium | small | ✅ done (2026-07-01) | VERIFIED: with BWG installed, no decoration leak at the void floor (y≈-64) and the End central island still generates. |
| 7 | Add wet-woods BWG theme_override bands (cypress, willow, white-mangrove, palm) | BWGPLAN.md | medium | medium | ✅ done (2026-07-01, v0.171.0) | Ids VERIFIED vs BWG 2.6.0: willow → `bayou`/`bayou_trees`, white-mangrove → `white_mangrove_marshes`, palm kept on `rainbow_beach` by design. Aquatic gametest added; both nodes green. |
| 8 | Add fantasy-woods BWG theme_override bands (enchanted, skyris, spirit) | BWGPLAN.md | medium | medium | ✅ done (2026-07-01, v0.171.0) | Ids VERIFIED: enchanted_tangle/skyris_vale confirmed; **spirit IS growable via `pale_bog`** (no `spirit_woods` biome). Forest gametest extended to lock the ids. |
| 9 | Step 3 — Verify create-otbwg milling recipes and place BWG millable flowers on islands | BWGPLAN.md | medium | small | ✅ done (2026-07-01, v0.174.0) | SHIPPED the datapack half: new **Meadow** (`biomeswevegone_meadow{,_large,_huge}`, 8 floral grasslands) + **Lush** (`biomeswevegone_lush{,_large,_huge}`, 3 jungle biomes) families place BWG flowers as island ground cover. Every flower verified as a real BWG 2.6.0 block AND a create-otbwg-compat-1.0 milling input. Gametests (both nodes, 137/137 + 146/146) assert each band places a `biomeswevegone:` flower. v0.175.0 also added a Forest-family flower sprinkle. **✅ in-game confirmed (2026-07-01):** flowers grow on islands and the create-otbwg milling recipes are present/working. Closed end-to-end. |
| 10 | Step 4 — Patchouli guide: add 'Exotic Biomes' entry | BWGPLAN.md | medium | small | ✅ done (2026-07-01) | DONE — `entries/exotic_biomes.json` (basics category), **BWG-gated** via a `mod:biomeswevegone` flag + hidden `reveal_exotic_woods` advancement on the inert `#skyseed:exotic_woods` tag (hidden without BWG, revealed on growing a wood). Modonomicon mirror auto-generates. Tag plank ids VERIFIED vs BWG 2.6.0 (2026-07-01, v0.171.0): dropped non-existent `#biomeswevegone:planks`, fixed `sakura_planks` + split `enchanted` into blue/green. |
| 11 | Decide: one tech backbone or two (Mekanism vs. IE) | CONTENTPLAN.md | medium | small | ✅ done (2026-07-01) | RESOLVED — **Immersive Engineering**, single backbone; **Mekanism dropped** (aesthetic call). Cascades: #17 dropped, #34 promoted, #35 (Excavator) now must be handled. |
| 12 | Resolve open decision: villages-only vs. villages + manor + bog trial (STRUCTUREPLAN) | STRUCTUREPLAN.md | medium | small | ✅ done (2026-07-01) | **DECIDED: FULL SCOPE** — all 6 village styles + aspen manor + bog trial (not villages-only). Puts #14/#26/#27/#28/#29/#30 in scope; #49 stays optional. Future long-tail want recorded (more structures than BWG shipped: Create sheds, abandoned Inferium farmlands, …). See STRUCTUREPLAN § Scope decision. |
| 13 | Confirm BWG village style ↔ biome mapping (esp. salem and forgotten) | STRUCTUREPLAN.md | medium | small | genuinely-open | Direct prerequisite to the villages resurrection — a wrong mapping targets the wrong biome and villages won't grow where intended. Small mechanical… |
| 14 | Resurrect BWG villages (6 styles) via biome adaptation | STRUCTUREPLAN.md | medium | medium | genuinely-open | Highest-value structure resurrection per the plan — 6 village styles via an existing low-risk mechanism, no bounding-box concern for street-network… |
| 15 | Curate Quark modules and add Zeta dependency | CONTENTPLAN.md | medium | small | genuinely-open | Low-effort additive building/QoL breadth; mostly config curation once jars are added, giving broad player-facing value cheaply. A good early content… |
| 16 | Integrate Farmer's Delight — wild crops on biome islands | CONTENTPLAN.md | medium | medium | genuinely-open | On-theme cozy content with a clear acquisition path; the void ChunkGenerator prerequisite is done. Higher marginal value than the overlapping… |
| 17 | ~~Integrate Mekanism — Industrial ore island(s)~~ | CONTENTPLAN.md | — | large | ❌ dropped (2026-07-01) | **DROPPED** — backbone decision (#11) chose Immersive Engineering instead (Mekanism too blocky/boring). Not pursued unless the aesthetic call is reversed. |
| 18 | Bootstrap Applied Energistics 2 (certus + sky stone) | CONTENTPLAN.md | medium | medium | genuinely-open | Self-sustaining storage endgame; FE prerequisite already satisfied, so only the one-time certus bootstrap remains. Good value once the primary tech… |
| 19 | Author FTB Quests progression spine (incremental, per newly-landed mod) | QUESTPLAN.md / CONTENTPLAN.md | medium | large | partially-done | The spine is authored for everything installed; remaining work scales with and is gated by the not-yet-installed mods, so it rolls forward as each… |
| 20 | Promote per-mod gateway islands into gated progression tiers (phase 7) | CONTENTPLAN.md | medium | large | partially-done | Proven and applied for all installed mods; a rolling capstone that scales with the remaining integrations and the quest spine. Follows those items… |
| 21 | Optional/later: add Distant Horizons (LOD) | BEAUTIFYPLAN.md | medium | medium | genuinely-open | Its cited blocker (unstable shaders) has cleared since the shader stack shipped, so it is unblocked. Nice-to-have visual enhancement with lingering… |
| 22 | Density follow-up: lift held wet/semi forest biomes to agreed level | BWGPLAN.md | low | small | genuinely-open | Low-risk cosmetic-parity polish the plan says is already unblocked (density read confirmed good). Small numeric edits; a quick win but purely… |
| 23 | Step 4 — Curate modpack config for BWG/OTYG/create-otbwg and regenerate mods.txt | BWGPLAN.md | low | small | partially-done | mods.txt and BWG injection already in place; residual work is a small coherence pass. Low-risk cleanup that rides alongside the OTYG verification. |
| 24 | Trial Chamber: palette + lighting pass on room NBTs plus hub/gallery | plannednotes.md | medium | medium | genuinely-open | Highest-value of the trial-chamber polish set: closes the visible detail gap vs vanilla that players directly experience. Aesthetic-only (gameplay… |
| 25 | Trial Chamber: add vaults / decorated pots / atmosphere (reward vault already confirmed) | plannednotes.md | low | medium | partially-done | Core reward loop (normal + ominous vaults) already ships; remaining work is atmospheric density only. Overlaps the palette task (same regeneration). |
| 26 | Resurrect aspen manor via woodland_mansion adaptation | STRUCTUREPLAN.md | low | medium | genuinely-open | Real content but carries the vertical-jigsaw bounding-box risk that broke the End City rebuild, so it needs its own gametest. Only in scope if the… |
| 27 | Resurrect bog trial via trial_chamber adaptation | STRUCTUREPLAN.md | low | medium | genuinely-open | Progression/combat content but same vertical-jigsaw risk as the manor; conditional on structure scope. Sequenced with the manor as the higher-risk… |
| 28 | Mitigate vertical-jigsaw bounding-box risk with correct placement params + gametests | STRUCTUREPLAN.md | low | medium | genuinely-open | Required validation layer before manor/trial can ship, but only relevant if the scope decision includes them. Bundled with those steps rather than… |
| 29 | Verify foreign BWG pool internals assemble cleanly on a flat island pad | STRUCTUREPLAN.md | low | medium | genuinely-open | Per-structure assembly validation bundled with each resurrection; prevents broken/misplaced structures from foreign assumptions. Not standalone work. |
| 30 | Per-step: bump mod_version + CHANGELOG and add inert golden-master gametest | STRUCTUREPLAN.md | low | small | genuinely-open | Non-negotiable release hygiene per the plan, but only exists as work when a resurrection step is undertaken. Rides along each structure item. |
| 31 | Integrate Critters and Companions — spawns on biome islands | CONTENTPLAN.md | low | small | genuinely-open | Cheap flavor add with its dependency already in the pack, but flavor-only and below the content/tech pillars in value. |
| 32 | Integrate Productive Bees — starter bees/hives | CONTENTPLAN.md | low | medium | genuinely-open | A second renewable pillar that overlaps the already-shipped Mystical Agriculture, lowering marginal value; deferrable. |
| 33 | Trial Chamber: add more room/corridor/intersection variants for layout variety | plannednotes.md | low | large | partially-done | Reduces repetition but layout already varies (gallery shipped); volume-heavy and lowest urgency of the trial-chamber tasks. |
| 34 | Integrate Immersive Engineering — Bauxite/Aluminum ore island (**the chosen tech backbone**) | CONTENTPLAN.md | medium | large | genuinely-open | **PROMOTED (2026-07-01)** — #11 chose IE over Mekanism (aesthetic). Now the primary tech tier: IE ore island (bauxite/silver/nickel) + FE integration. Gated on the Excavator fix (#35). |
| 35 | Fix the IE Excavator for the void — try (1) patch its ore mix to be **island-based** (preferred) else (2) **disable it entirely** (remove recipe + hide in JEI) | CONTENTPLAN.md | medium | medium | genuinely-open | **Now in scope (2026-07-01)** since IE is the backbone (#34). Excavator samples worldgen mineral veins absent in the void. Option 1 keeps the mechanic on-theme (needs a mixin vs IE's `ExcavatorHandler`); option 2 is the simple fallback. |
| 36 | Integrate Iron's Spells 'n Spellbooks — loot/mob injection | CONTENTPLAN.md | low | large | genuinely-open | Heaviest injection on the list, partially blocked by un-started structure-island loot work; lowest ROI of the tech/magic set. |
| 37 | Decide: Iron's Spells scope (full discovery loop vs. crafted-only) | CONTENTPLAN.md | low | small | genuinely-open | Bounds the heaviest integration but only worth settling once that low-priority mod is picked up. |
| 38 | Decide: Mystical Agriculture vs. bespoke ore islands | CONTENTPLAN.md | low | small | partially-done | Low-stakes ongoing decision already settled in practice for shipped mods; only the future-mod island count remains. |
| 39 | Prove FE flows Create → Mekanism/IE/AE2 across islands | CONTENTPLAN.md | low | small | unclear | Cheap verification but moot until at least one FE consumer is added; naturally follows the Mekanism/AE2 integrations. |
| 40 | Future chapter: Mekanism (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Follows the Mekanism integration; the big back-loaded tech tier's quest coverage, gated on install. |
| 41 | Future chapter: Applied Energistics 2 (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Follows AE2 integration; storage/automation endgame onboarding, gated on install. |
| 42 | Future chapter: Farmer's Delight (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Follows FD integration; cozy on-theme coverage, gated on install. |
| 43 | Future chapter: Quark (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Additive mod may need minimal quest coverage; low value, follows the Quark add. |
| 44 | Future chapter: Productive Bees (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Overlaps shipped Mystical Agriculture; low marginal value, gated on install. |
| 45 | Future chapter: Immersive Engineering (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Explicitly least-value/most-effort and possibly dropped; lowest of the tech quest chapters. |
| 46 | Future chapter: Iron's Spells (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Loot/mob-gated, heavy injection, low value; among the lowest-priority future chapters. |
| 47 | Future chapter: BYG content (quest) | QUESTPLAN.md | low | unknown | genuinely-open | Large palette mod gated behind install + full theme integration; distant future work. |
| 48 | STRUCTUREPLAN — BWG village/manor/trial resurrection (later child changeset umbrella) | STRUCTUREPLAN.md | low | large | genuinely-open | Deliberately scoped to its own future child changeset; high long-term content value but explicitly not blocking woods/compat work. Tracked via its… |
| 49 | Resurrect prairie houses / rugged fossil (optional polish) | STRUCTUREPLAN.md | low | small | genuinely-open | Explicitly optional garnish reusing the hamlet mechanism; only worth doing after core villages ship. |
| 50 | Optional dedicated Prosperity island as its own Skyseed seed/tier (roadmap) | MYSTICALPLAN.md | low | medium | genuinely-open | Pure polish — the bootstrap gap is already solved by the Ancient override; improves progression clarity but has no functional blocker. |
| 51 | Balance tuning: watch Growth Accelerator stacking and mob Inferium drop rates | MYSTICALPLAN.md | low | small | genuinely-open | Ongoing watch knob dependent on the in-game verification pass; adjust only if playtesting surfaces a problem. |
| 52 | Verify uncertain Create addons (The Factory Must Grow, Extended Cogwheels; Deco done) | CONTENTPLAN.md | low | small | partially-done | Explicitly optional 'avoid piling on' extras; one already decided, the rest discretionary. |
| 53 | Generate Vanilla Tweaks 16x resource pack and wire/enable it | BEAUTIFYPLAN.md | low | medium | genuinely-open | Consciously dropped; reviving is optional polish, not a gap. |
| 54 | Optional: re-scaffold standalone Skyseed resource pack for branding | BEAUTIFYPLAN.md | low | medium | genuinely-open | Purely optional branding/cohesion polish, deferred by design. |
| 55 | Refresh shaderPack pin when Complementary / Euphoria Patches update | BEAUTIFYPLAN.md | low | small | genuinely-open | Standing maintenance note; the pin is currently correct, so nothing to do until the next update (silent shader failure risk if forgotten then). |
| 56 | Route the gametest suite's remaining direct API calls through compat (Stage-1 to-do) | REFACTORPLAN.md | low | medium | genuinely-open | Value dropped after Stage 2 shipped — buys Definition-of-Done tidiness, not cross-suite reuse, and touching the frozen golden-master witness carries… |
| 57 | Plan/implement version-keyed golden-master fingerprints if serialization shifts | REFACTORPLAN.md / CODE_REVIEW.md | low | small | partially-done | Regression guarantee already preserved via per-version GOLDEN maps; remaining value is a small doc fix plus a contingency for a future shared-suite… |
| 58 | Add a per-version data variant for a future vanilla block-id rename | REFACTORPLAN.md | low | small | genuinely-open | Zero current demand — skip-on-unknown + Java compat has covered every rename so far; premature machinery would be speculative. |
| 59 | Add further Minecraft/NeoForge version nodes as wanted | REFACTORPLAN.md | low | large | genuinely-open | Delivers the refactor's core promise but no third target is currently chosen and each node is a multi-session grind; discretionary. |
| 60 | (Deferred alternative) Dedicated structure seeds instead of biome adaptation | STRUCTUREPLAN.md | low | medium | genuinely-open | Both plans judge it likely unnecessary (adaptation + existing vanilla-analogue seeds cover it); lowest-priority fallback, not planned to be used. |
| 61 | Trial Chamber: clean rebuild and visual comparison against a vanilla trial chamber | plannednotes.md | low | small | genuinely-open | A gated human sign-off that cannot start until the upstream polish tasks (24, 25, 33) are attempted; verification, not dev work. Placed after them. |
| 62 | Add **stone** inferium/prosperity MA ores to the **Lush** island (new `mysticalagriculture_lush.json` + `_large`/`_huge`) | MYSTICALPLAN.md | medium | small | ✅ done (2026-07-01, v0.172.0) | SHIPPED: `mysticalagriculture_lush{,_large,_huge}.json` add the stone `inferium_ore`/`prosperity_ore` at `core` (Lush's core is stone); Ancient keeps the deepslate variants. Off-dimension safe (overworld-only theme → neutral empty ores in End/Nether). `mystical_agriculture_compat_targets_lush` gametest on both nodes; all gametests pass, both nodes compile. |
| 63 | Close the BWG plank gap — add bands for the uncovered growable planks + resolve fir; extend `#skyseed:exotic_woods` + the gametests. | BWGPLAN.md | medium | medium | ✅ done (2026-07-01, v0.173.0) | SHIPPED: the 5 growable woods as dedicated-feature Forest-family bands ×3 tiers — florus→`forgotten_forest`/`florus_trees`, holly→`dacite_ridges`/`holly_trees`, pine→`black_forest`/`pine_tree1`+`pine_tree2` (no aggregate), mahogany→`tropical_rainforest`/`mahogany_trees`, rainbow_eucalyptus→`fragment_jungle`/`rainbow_eucalyptus_trees`; ids re-verified vs the 2.6.0 jar. `#skyseed:exotic_woods` +5 planks. **fir = non-growable** (ships planks/sapling but no configured tree feature) → **24/25 obtainable**; a gametest guards no `fir_*` band. Forest gametest (both nodes) locks the 5 new keys — 135/135 + 144/144 pass. |
| 64 | Rework the **wet-wood (Aquatic-family) water feature** — the deep round `pond` is too small and the wrong shape for swamp/marsh woods (cypress/bayou/white_mangrove/palm); implement a **broad, shallow swamp/marsh** water feature instead (esp. the Huge tier, which read as under-watered). | BWGPLAN.md | medium | medium | genuinely-open | In-game (2026-07-01): all four wet-wood islands + the Aquatic-cypress island had "not enough water"; user suggests a shallow-swamp feature over a pond. Player-facing quality of the water-first islands. Detail in [BWGPLAN § In-game test findings](Modpack-growyourownworld/BWGPLAN.md). |
| 65 | Fix **wet-wood tree density / zero-tree cases** — guarantee **≥1 tree on the Small tier** (Aquatic cypress Small = 0, Forest cypress Small = 0) and fix **Huge Bayou growing 0 willow trees**; raise `tries` and/or the minimum island radius so small islands aren't barren. | BWGPLAN.md | medium | small | genuinely-open | In-game (2026-07-01): Small tiers produced 0 cypress; Huge Bayou produced 0 willow at all. A grown wood island with no trees defeats the purpose. |
| 66 | Investigate the **spirit band failing in-game** — a seed over `pale_bog` produced only oak & birch, no spirit. | BWGPLAN.md | medium | medium | ✅ diagnosed (2026-07-01) — re-test, no code change | ROOT-CAUSED as a **test mislabel, not a defect**: a *matched* biome override **replaces** variants (verified in `IslandGenerator.eff`), so a matched `pale_bog` band emits **only** `spirit_trees` and **can never** yield oak/birch. Biome `pale_bog` + feature `spirit_trees` both exist in the jar; the band is prepended + gametest-asserted; spirit shares the **same** `ohthetreesyoullgrow:tree_from_nbt_v1` type as the working enchanted/skyris. ∴ the oak/birch result means the seed **wasn't over `pale_bog`** (the report self-contradicts with "pale_bog → white mangrove"). **Action:** re-throw over a confirmed `pale_bog` + verify it's reachable in the void multi-noise. No band/code edit. |
| 67 | Add **crash-fix observability** for 5.2 / 5.3 — log a warning when a drain hits `MAX_DRAIN_TICKS` before completion, and log force-load ticket acquire/release (or document `/forceload query`), so the drain-adequacy and ticket-release sign-offs are actually checkable. | CODE_REVIEW.md | low | small | genuinely-open | In-game (2026-07-01): 5.2/5.3 *behaviour* passed but the user could not verify `MAX_DRAIN_TICKS` adequacy or ticket release — there's no surfaced signal. Small dev-experience/diagnostic add. |
| 68 | **Future — net-new bespoke structures** beyond BWG's own 17: little **Create sheds** with small working apparatus, **abandoned Inferium farmlands** (derelict MA plots), and similar flavour builds. Same jigsaw-pool + biome-adaptation (or bespoke-seed) vehicle; per-step gametest + version bump. | STRUCTUREPLAN.md | low | large | genuinely-open | User want (2026-07-01) recorded alongside the #12 full-scope decision — the *long-term* structure ambition is more content than BWG shipped, not just resurrecting it. Not scheduled; sits behind the full-scope resurrection (12/14/26–30). |

---

## CODE_REVIEW.md follow-ups (open engineering debt)

All **21 code-review findings are fixed and merged to `main` via PR #15** — this doc treats them as
closed. What remains are two follow-ups the findings themselves deferred (the code documents each gap)
plus manual verification:

| Item | Priority | Effort | Status | Evidence |
|---|---|---|---|---|
| **5.3 follow-up** — reconcile force-load leak on server start: track forced regions in `SkyseedWorldData`, clear stale ones on `ServerStartedEvent` so a mid-grow crash doesn't leak a permanently force-loaded region | medium | medium | genuinely-open | `GenerationJob.java:170` documents the gap ("a hard crash mid-grow would leave them forced, recoverable with `/forceload remove`") |
| **5.2 follow-up** — full persist/resume of in-progress jobs across world loads: store `IslandPlan` + progress in `SkyseedWorldData`, re-enqueue on server start | medium | large | genuinely-open | `IslandGrowth.java:48` notes drain-on-stop was chosen over persist/resume deliberately |
| Manual smoke-tests for **5.1 / 5.2 / 5.3** + confirm the merged CI run went green | low | small | ✅ done (2026-07-01) | User-run: **5.1** snow added exactly once; **5.2** island persists across restart; **5.3** twin Nether island populates; **PR #15 CI** green both nodes. Residual observability (drain timeout / ticket release not surfaced) spun off as **#67**. |

---

## Already shipped (checked off in the source plans)

The audit found **28 items** that the plans still listed as pending but which have actually shipped.
They've been marked ✅ in place in each plan. Summary:

- **CONTENTPLAN.md (11):** void ChunkGenerator (Order 0), the Create + Crafts & Additions + Flux
  Networks power backbone, Silent Gear, Create addons (Steam 'n' Rails, Enchantment Industry, Bells &
  Whistles, Connected, Rechiseled, Better Motors), the Mystical Agriculture starter, and the
  `gen-mods-txt.ps1` manifest refresh.
- **BEAUTIFYPLAN.md (9):** Iris, Monocle, Euphoria Patches, Complementary Reimagined, Fresh Animations
  (+EMF/ETF), AmbientSounds (+CreativeCore), Falling Leaves, Fusion (+Midnighttigger CT), and Sound
  Physics Remastered — all present in `mods.txt`. *(Vanilla Tweaks stays unchecked — it was
  deliberately dropped, not shipped.)*
- **QUESTPLAN.md (5):** the Tools chapter (shipped as "Tools & Travel"), Create Extras (folded into
  the Create chapter), the Other chapter (folded into Tools & Travel), the Silent Gear placement
  decision (resolved), and the Mystical Agriculture "future chapter" (shipped).
- **REFACTORPLAN.md (1):** the pragmatic 26.1.2 stubs (worldGenOptions / icon hook / findResource /
  FMLEnvironment.production) are re-wired to real 26.1.2 APIs.
- **MYSTICALPLAN.md (1):** the in-game quest-book test-load gate is complete.
- **STRUCTUREPLAN.md (1):** its prerequisite (BWGPLAN Step 1 — wood biome adaptation) has shipped for
  the forest family.
