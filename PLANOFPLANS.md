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
  CODE_REVIEW follow-ups). *(2026-07-01: items #1, #4, #7, #8, #10 resolved/shipped — see the [Decisions log](#decisions-log).)*
- **31+ items were already shipped** and have been checked off in their plans — the docs read as far
  less finished than the repo actually is (CONTENTPLAN especially predates the void ChunkGenerator, the
  Create power backbone, Mystical Agriculture, and the six-chapter quest spine, all since shipped).
- The best value-per-effort sits entirely in the **top ~13 items** — cheap decisions and shipped-feature
  sign-offs that unblock everything downstream.

### Decisions log

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

## How to read this

- **Priority** = high / medium / low (unblock-value and player-facing ROI, not raw importance).
- **Effort** = small / medium / large.
- **Status** = `genuinely-open` (nothing built), `partially-done` (base shipped, follow-up remains),
  `unclear` (needs a prerequisite before it's even actionable).

## Snapshot by plan

| Plan | What it covers | Open | ✅ Checked off |
|---|---|---:|---:|
| [BWGPLAN.md](Modpack-growyourownworld/BWGPLAN.md) | Oh The Biomes We've Gone integration | 7 | 3 |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration levers | 17 | 11 |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | BWG structures → growable islands | 10 | 1 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | 9 | 5 |
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
3. **Build the BWG quest branch** (Into the Wilds / Mill the Blooms / Grow Something Grand) **under the Tools & Travel chapter**. *(QUESTPLAN / BWGPLAN Step 5 · small)* — the one explicitly-pending quest deliverable; its integration already shipped. Clean quick win.
4. ✅ **Fix CI-file doc drift** — DONE (2026-07-01): corrected `ci-skyseed.yml`→`build.yml` and the "matrix"→chiseled-fan-out description across README/REFACTORPLAN/CHANGELOG; the "add a version" recipe now correctly says no CI edit is needed. *(REFACTORPLAN · small)*
5. ✅ **Verify Mystical Agriculture in-game** — DONE (2026-07-01): ore spawn + bootstrap loop confirmed (deepslate ores on Ancient; essence→farmland→pot works). Surfaced follow-up #62 (stone ores on Lush). *(MYSTICALPLAN · small)*
6. ✅ **Smoke-test the void ChunkGenerator with BWG installed** — DONE (2026-07-01): no features at y≈-64, End central island intact. *(plannednotes · small)*
10. ✅ **Patchouli "Exotic Woods" entry** — DONE (2026-07-01): authored `entries/exotic_biomes.json` (basics category) describing the already-shipped Forest-over-BWG loop; the Modonomicon mirror auto-generates at build. *(BWGPLAN · small)*
11. **Decide: one tech backbone or two** (Mekanism vs. IE) — formalize the documented Mekanism-only lean; cascades to items 34/35/38. *(CONTENTPLAN · small)*
12. **Decide structure scope** — villages-only vs. villages + manor + bog trial. *(STRUCTUREPLAN · small)*

### Tier 2 — BWG content payoff & structures

Highest player-facing value once Q2 (1) and the village↔biome mapping (13) are settled.

7. ✅ **Wet-woods BWG bands** (cypress, willow, white-mangrove, palm) — DONE (2026-07-01, v0.171.0): ids verified vs BWG 2.6.0, willow/white-mangrove ids corrected. *(BWGPLAN · medium)*
8. ✅ **Fantasy-woods BWG bands** (enchanted, skyris, spirit) — DONE (2026-07-01, v0.171.0): spirit found growable via `pale_bog`. *(BWGPLAN · medium)*
9. **Verify create-otbwg milling recipes + place millable flowers on islands** — *(BWGPLAN · small, needs 1)*
13. **Confirm BWG village style ↔ biome mapping** (esp. `salem`, `forgotten`) — *(STRUCTUREPLAN · small)*
14. **Resurrect BWG villages** (6 styles) via biome adaptation — highest-value structure work. *(STRUCTUREPLAN · medium, needs 13 + 12)*
26–30. **Manor + bog-trial resurrection** with vertical-jigsaw gametests, on-pad assembly checks, and per-step golden-master discipline — only if scope (12) includes them; carries the bounding-box risk that broke the End City rebuild.

### Tier 3 — Content-mod wave & code follow-ups

Once the backbone decision (11) is committed, integrate in ROI order; each integration is followed by
its quest chapter and gated island tier.

15. **Curate Quark modules + add Zeta** — cheap building/QoL breadth. *(CONTENTPLAN · small)*
16. **Farmer's Delight** — wild crops on biome islands. *(CONTENTPLAN · medium)*
17. **Mekanism** — industrial ore island(s); the primary tech tier. *(CONTENTPLAN · large)*
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
(59), and the manual smoke-tests for the merged crash fixes. See the full table for all of them.

---

## Dependency chains & sequencing

1. **BWG content chain (most actionable value; prerequisites all shipped):**
   Q2 (1) → wet-woods (7) + fantasy-woods (8) → create-otbwg flower placement (9).
   OTYG sapling-growth verification (2) is an independent correctness gate — run it first/alongside Q2.
   The BWG quest chapter (3) and Patchouli entry (10) are already unblocked and can ship immediately.

2. **Tech / content-mod chain:** backbone decision (11, default Mekanism-only) → Quark (15) + Farmer's
   Delight (16) first (cheap), then Mekanism (17), then AE2 (18). Each integration is *followed by* its
   quest chapter (part of 19) and its gated gateway-island tier (20). FE cross-mod proof (39) is moot
   until a consumer mod lands.

3. **Structure resurrection chain (deferred child changeset, all NOT STARTED):** scope decision (12)
   gates everything; villages-only is the low-risk clear win. Villages (14) need the style↔biome
   mapping (13). Adding manor (26) + bog trial (27) pulls in the vertical-jigsaw mitigation (28) +
   on-pad assembly verification (29) + per-step release hygiene (30).

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
- [ ] **(#9) create-otbwg milling** — spot-check 2–3 milling recipes resolve against BWG ids, and confirm **BWG millable flowers actually spawn on islands** so the compat has inputs.

**Crash-fix smoke tests (CODE_REVIEW 5.1–5.3 — base fixes merged; confirm live behaviour):**

- [ ] **(5.1)** Deferred island-finalization state machine — grow an island; confirm the finalize pass (e.g. snow) is **not skipped or double-run**.
- [ ] **(5.2)** Drain-on-stop — throw a large-island seed, **immediately stop the server**; confirm the island completes + persists and `MAX_DRAIN_TICKS` is adequate.
- [ ] **(5.3)** Force-load twin islands — throw a twin seed; verify the **player-less Nether-side island fully populates** and the force-load ticket **releases** on completion.
- [ ] Confirm the merged **CI run (PR #15, both nodes) went green**.

**New BWG wet/fantasy wood content — ids now VERIFIED vs BWG 2.6.0 (2026-07-01, v0.171.0); these throw-a-seed sign-offs remain (#7/#8):**

- [ ] Throw an **Aquatic** seed over `cypress_swamplands` → confirm a **water-first** island (pond dominant, cypress trees secondary).
- [ ] Throw a **Forest** seed over the *same* `cypress_swamplands` → confirm a **trees-first** island (dense cypress, minimal water). *(This is the multi-seed / per-seed-priority model working.)*
- [ ] Grow the **wet woods** (cypress/willow/white-mangrove/palm) and **fantasy woods** (enchanted/skyris/spirit) → confirm each island generates with the right trees and **every BWG plank is now obtainable**.
- [ ] Confirm the drafts are **inert with BWG NOT installed** (no errors; generation unchanged).

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
| 3 | Build the BWG quest branch (Into the Wilds / Mill the Blooms / Grow Something Grand) **under the Tools & Travel chapter** (BWGPLAN Step 5) | QUESTPLAN.md / BWGPLAN.md | high | small | genuinely-open | The single explicitly-pending quest deliverable whose dependency (BWG island/resource integration) is already shipped, so it is immediately actionable. Lives under Tools & Travel (an explore-the-wilds line), not the Skyseed spine. |
| 4 | Reconcile plan/changelog CI-file references with the actual workflow (documentation drift) | REFACTORPLAN.md | medium | small | ✅ done (2026-07-01) | DONE — fixed `ci-skyseed.yml`→`build.yml` + "matrix"→chiseled-fan-out in README/REFACTORPLAN/CHANGELOG; "add a version" recipe now says no CI edit needed. |
| 5 | Verify the MA integration in-game (ore spawn + bootstrap loop) | MYSTICALPLAN.md | medium | small | ✅ done (2026-07-01) | VERIFIED: deepslate Inferium + Prosperity ore on Ancient; Inferium Seed + Inferium Farmland in an Elite Botany Pot grows as intended. Surfaced follow-up #62 (stone ores on Lush). |
| 6 | Runtime smoke test of void ChunkGenerator with BWG installed | plannednotes.md | medium | small | ✅ done (2026-07-01) | VERIFIED: with BWG installed, no decoration leak at the void floor (y≈-64) and the End central island still generates. |
| 7 | Add wet-woods BWG theme_override bands (cypress, willow, white-mangrove, palm) | BWGPLAN.md | medium | medium | ✅ done (2026-07-01, v0.171.0) | Ids VERIFIED vs BWG 2.6.0: willow → `bayou`/`bayou_trees`, white-mangrove → `white_mangrove_marshes`, palm kept on `rainbow_beach` by design. Aquatic gametest added; both nodes green. |
| 8 | Add fantasy-woods BWG theme_override bands (enchanted, skyris, spirit) | BWGPLAN.md | medium | medium | ✅ done (2026-07-01, v0.171.0) | Ids VERIFIED: enchanted_tangle/skyris_vale confirmed; **spirit IS growable via `pale_bog`** (no `spirit_woods` biome). Forest gametest extended to lock the ids. |
| 9 | Step 3 — Verify create-otbwg milling recipes and place BWG millable flowers on islands | BWGPLAN.md | medium | small | genuinely-open | The 94-recipe compat does nothing without inputs on islands. Recipe check is small, but real value is coupled to the lush/meadow band work that… |
| 10 | Step 4 — Patchouli guide: add 'Exotic Biomes' entry | BWGPLAN.md | medium | small | ✅ done (2026-07-01) | DONE — `entries/exotic_biomes.json` (basics category), **BWG-gated** via a `mod:biomeswevegone` flag + hidden `reveal_exotic_woods` advancement on the inert `#skyseed:exotic_woods` tag (hidden without BWG, revealed on growing a wood). Modonomicon mirror auto-generates. Tag plank ids VERIFIED vs BWG 2.6.0 (2026-07-01, v0.171.0): dropped non-existent `#biomeswevegone:planks`, fixed `sakura_planks` + split `enchanted` into blue/green. |
| 11 | Decide: one tech backbone or two (Mekanism vs. IE) | CONTENTPLAN.md | medium | small | partially-done | Near-free decision with a documented default that prevents duplicated large tech-tier effort and cascades to resolve the IE, Excavator, and… |
| 12 | Resolve open decision: villages-only vs. villages + manor + bog trial (STRUCTUREPLAN) | STRUCTUREPLAN.md | medium | small | genuinely-open | Gating decision for the entire (not-started) structure plan; cheap and high-leverage, sequences and bounds every downstream structure resurrection… |
| 13 | Confirm BWG village style ↔ biome mapping (esp. salem and forgotten) | STRUCTUREPLAN.md | medium | small | genuinely-open | Direct prerequisite to the villages resurrection — a wrong mapping targets the wrong biome and villages won't grow where intended. Small mechanical… |
| 14 | Resurrect BWG villages (6 styles) via biome adaptation | STRUCTUREPLAN.md | medium | medium | genuinely-open | Highest-value structure resurrection per the plan — 6 village styles via an existing low-risk mechanism, no bounding-box concern for street-network… |
| 15 | Curate Quark modules and add Zeta dependency | CONTENTPLAN.md | medium | small | genuinely-open | Low-effort additive building/QoL breadth; mostly config curation once jars are added, giving broad player-facing value cheaply. A good early content… |
| 16 | Integrate Farmer's Delight — wild crops on biome islands | CONTENTPLAN.md | medium | medium | genuinely-open | On-theme cozy content with a clear acquisition path; the void ChunkGenerator prerequisite is done. Higher marginal value than the overlapping… |
| 17 | Integrate Mekanism — Industrial ore island(s) | CONTENTPLAN.md | medium | large | genuinely-open | Designated primary tech backbone (per the Mekanism-only lean), so highest value of the tech tier — but large. Should follow the backbone decision… |
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
| 34 | Integrate Immersive Engineering — Bauxite/Aluminum ore island + Excavator decision | CONTENTPLAN.md | low | large | genuinely-open | Explicitly flagged redundant with Mekanism and likely dropped under the Mekanism-only lean; large effort for least value. Deferred/scope-down… |
| 35 | Decide: IE Excavator — drop recipe or leave installed-but-dead | CONTENTPLAN.md | low | small | partially-done | Essentially pre-resolved and contingent on IE being added at all; near-zero standalone value. |
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

---

## CODE_REVIEW.md follow-ups (open engineering debt)

All **21 code-review findings are fixed and merged to `main` via PR #15** — this doc treats them as
closed. What remains are two follow-ups the findings themselves deferred (the code documents each gap)
plus manual verification:

| Item | Priority | Effort | Status | Evidence |
|---|---|---|---|---|
| **5.3 follow-up** — reconcile force-load leak on server start: track forced regions in `SkyseedWorldData`, clear stale ones on `ServerStartedEvent` so a mid-grow crash doesn't leak a permanently force-loaded region | medium | medium | genuinely-open | `GenerationJob.java:170` documents the gap ("a hard crash mid-grow would leave them forced, recoverable with `/forceload remove`") |
| **5.2 follow-up** — full persist/resume of in-progress jobs across world loads: store `IslandPlan` + progress in `SkyseedWorldData`, re-enqueue on server start | medium | large | genuinely-open | `IslandGrowth.java:48` notes drain-on-stop was chosen over persist/resume deliberately |
| Manual smoke-tests for **5.1 / 5.2 / 5.3** + confirm the merged CI run went green | low | small | genuinely-open | Base fixes merged; no changelog/log artifact of a human running the germinate/quit/twin scenarios |

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
