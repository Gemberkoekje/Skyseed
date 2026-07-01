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

- **~65 genuinely-open points** remain, consolidated into **61 ranked items** below (plus the
  CODE_REVIEW follow-ups).
- **28 items were already shipped** and have been checked off in their plans — the docs read as far
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

## How to read this

- **Priority** = high / medium / low (unblock-value and player-facing ROI, not raw importance).
- **Effort** = small / medium / large.
- **Status** = `genuinely-open` (nothing built), `partially-done` (base shipped, follow-up remains),
  `unclear` (needs a prerequisite before it's even actionable).

## Snapshot by plan

| Plan | What it covers | Open | ✅ Checked off |
|---|---|---:|---:|
| [BWGPLAN.md](Modpack-growyourownworld/BWGPLAN.md) | Oh The Biomes We've Gone integration | 10 | 0 |
| [CONTENTPLAN.md](Modpack-growyourownworld/CONTENTPLAN.md) | Content-mod integration levers | 17 | 11 |
| [STRUCTUREPLAN.md](Modpack-growyourownworld/STRUCTUREPLAN.md) | BWG structures → growable islands | 10 | 1 |
| [QUESTPLAN.md](Modpack-growyourownworld/QUESTPLAN.md) | FTB Quests line | 9 | 5 |
| [plannednotes.md](plannednotes.md) | Void ChunkGenerator, Trial Chamber, misc | 6 | 0 |
| [REFACTORPLAN.md](REFACTORPLAN.md) | Multi-version (NeoForge + Stonecutter) refactor | 5 | 1 |
| [BEAUTIFYPLAN.md](Modpack-growyourownworld/BEAUTIFYPLAN.md) | Modpack visual/aesthetic pass | 4 | 9 |
| [MYSTICALPLAN.md](Modpack-growyourownworld/MYSTICALPLAN.md) | Mystical Agriculture + Botany Pots | 3 | 1 |
| [CODE_REVIEW.md](CODE_REVIEW.md) | 21 review findings (all fixed + merged via PR #15) | ~5 | — |

---

## Priority tiers (the readable view)

### Tier 1 — Quick unblockers & shipped-feature sign-offs *(do these first)*

Cheap decisions and validations that either unblock whole chains or confirm a flagship feature works
before more is piled on top.

1. ✅ **Resolve Q2** — ~~concentrate BWG wood bands on the Forest seed vs. distribute across typed seeds~~ **DECIDED 2026-07-01: distribute, priority-ordered per seed** (see [Decisions log](#decisions-log)). Items 7, 8, 9 are now unblocked.
2. **Verify OTYG sapling→tree growth** for vanilla + BWG saplings. *(BWGPLAN · medium)* — correctness gate: if saplings don't grow OOTB the entire exotic-wood loop is silently broken.
3. **Build the BWG quest chapter** (Into the Wilds / Mill the Blooms / Grow Something Grand). *(QUESTPLAN / BWGPLAN Step 5 · small)* — the one explicitly-pending quest deliverable; its integration already shipped. Clean quick win.
4. **Fix CI-file doc drift** — the docs point contributors at a `ci-skyseed.yml` that never existed (real file is `build.yml`). *(REFACTORPLAN · small)*
5. **Verify Mystical Agriculture in-game** — ore spawn + the prosperity-ore bootstrap loop. *(MYSTICALPLAN · small)*
6. **Smoke-test the void ChunkGenerator with BWG installed** — confirm no features at y≈-64, End island intact. *(plannednotes · small)*
10. **Patchouli "Exotic Biomes" entry** — documents the already-shipped Forest-over-BWG loop. *(BWGPLAN · small)*
11. **Decide: one tech backbone or two** (Mekanism vs. IE) — formalize the documented Mekanism-only lean; cascades to items 34/35/38. *(CONTENTPLAN · small)*
12. **Decide structure scope** — villages-only vs. villages + manor + bog trial. *(STRUCTUREPLAN · small)*

### Tier 2 — BWG content payoff & structures

Highest player-facing value once Q2 (1) and the village↔biome mapping (13) are settled.

7. **Wet-woods BWG bands** (cypress, willow, white-mangrove, palm) — *(BWGPLAN · medium, needs 1)*
8. **Fantasy-woods BWG bands** (enchanted, skyris, spirit) — *(BWGPLAN · medium, needs 1)*
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

## Full ranked backlog

| # | Item | Plan | Priority | Effort | Status | Why (ranking rationale) |
|---|---|---|---|---|---|---|
| 1 | ✅ **RESOLVED** — Q2: **distribute** BWG bands across typed seeds, priority-ordered per seed (aquatic=water-first, forest=trees-first, lush=extreme nature); same biome may appear in multiple families. See BWGPLAN §Q2. | BWGPLAN.md | high | small | ✅ done (2026-07-01) | Was the top blocker; now decided — the wet/fantasy wood-band authoring (#7/#8) and millable-flower placement (#9) are unblocked. |
| 2 | Step 2 — Verify OTYG sapling→tree growth for vanilla + BWG saplings | BWGPLAN.md | high | medium | genuinely-open | OTYG's entire value is sapling growth; if BWG (or vanilla) saplings don't grow, the exotic-wood replanting loop that all the band work relies on is… |
| 3 | Build the BWG (Biomes We've Grown) quest chapter / light quest branch (BWGPLAN Step 5) | QUESTPLAN.md / BWGPLAN.md | high | small | genuinely-open | The single explicitly-pending quest deliverable whose dependency (BWG island/resource integration) is already shipped, so it is immediately… |
| 4 | Reconcile plan/changelog CI-file references with the actual workflow (documentation drift) | REFACTORPLAN.md | medium | small | genuinely-open | Actively misdirects the documented 'add a version node' workflow — the recipe tells contributors to edit a file that never existed and add an… |
| 5 | Verify the MA integration in-game (ore spawn + bootstrap loop) | MYSTICALPLAN.md | medium | small | partially-done | MA is fully shipped and is the pack's renewable pillar; a single quick playtest de-risks the release's core loop before shipping. Low cost,… |
| 6 | Runtime smoke test of void ChunkGenerator with BWG installed | plannednotes.md | medium | small | genuinely-open | Only remaining sign-off for the confirmed TerraBlender/BWG decoration-leak fix — the mod's central worldgen-correctness feature. Low effort, closes… |
| 7 | Add wet-woods BWG theme_override bands (cypress, willow, white-mangrove, palm) | BWGPLAN.md | medium | medium | 🟡 drafted — pending id verification | DRAFTED on the Aquatic family (water-first) in `biomeswevegone_aquatic{,_large,huge_}.json`. Remaining: verify BWG ids + gametest + version bump. |
| 8 | Add fantasy-woods BWG theme_override bands (enchanted, skyris, spirit) | BWGPLAN.md | medium | medium | 🟡 drafted — pending id verification | DRAFTED on the Forest family (trees-first), appended to `biomeswevegone_forest{,_large,huge_}.json` (+ a cypress multi-seed-demo overlap). Remaining: verify BWG ids (esp. whether a `spirit` biome exists) + gametest + version bump. |
| 9 | Step 3 — Verify create-otbwg milling recipes and place BWG millable flowers on islands | BWGPLAN.md | medium | small | genuinely-open | The 94-recipe compat does nothing without inputs on islands. Recipe check is small, but real value is coupled to the lush/meadow band work that… |
| 10 | Step 4 — Patchouli guide: add 'Exotic Biomes' entry | BWGPLAN.md | medium | small | genuinely-open | The BWG mechanic is invisible without docs; small self-contained content that ships independently and improves discovery of already-shipped bands.… |
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
