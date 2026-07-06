# SIGNOFFPLAN — in-game throw-test findings (2026-07-06)

Tracks the results of the first human throw-test pass against the Tier-1 sign-off queue (PLANOFPLANS).
Each finding has a **root cause** (verified against the source where possible), a **proposed fix**, and a
**verify** step. Ordered: bugs first (by severity), then tuning, mod-curation, UX/config, and the confirmed-good list.

> Source session: Arie's throw-test, mod ~0.228.0. Positive confirmations are in **§7 Signed off** — do not re-test those.

---

## §1 Status board

| # | Item | Verdict | Owner area |
|---|---|---|---|
| B1a | Nether dirt pads | ✅ FIXED (`clearPlantingSpot` surface-aware) | `GenerationJob` |
| B1b | Powdery cane | ✅ FIXED — plant the `BonemealableBlock` + new `grow` field, bonemealed to varied stages | `GroundEntry`/`GenerationJob` + `mynethersdelight_*` |
| B2a | Blossom/"Trumpet" trees | ✅ signed off (swamp + plains confirmed) | `theme_override/quark_forest*` |
| B2b | Shale on ancient | ✅ found in-game; still maybe rare — optional tune | `quark_ancient.json` + `OrePlanner` |
| B3 | Snowy/frozen rocky islands lose base ores + deepslate | ✅ FIXED (code, pending recapture+test) | `mergeBands` selector match |
| B4 | Portal twin misaligned | ✅ FIXED — twins never nudge horizontally (opening stays on the 8:1 column) | `TwinPlacer` |
| T1 | Crude oil too rare / hidden | 🔧 tune (+ blocked by B3) | `theme_override/immersiveengineering_*` |
| M1 | Kinetic acceptor doubled — drop Create: AE Generator? | ❓ curation decision | `mods.txt` |
| U1 | `R` collides: aircraft dismount vs shader reload | 🔧 keybind config | client config |
| U2 | Aircraft fuel — item fuel (coal/blaze works); diesel/kerosene are fluids | ✅ resolved — not a bug | `config/immersive_aircraft.json` |
| S1 | Fastest creative energy for the Excavator | ❓ support (answered §6) | — |
| S2 | "FE power chain flows" — what it means | ❓ wording (answered §6) | PLANOFPLANS wording |

---

## §2 Bugs

### B1 — `forceOneTree` stamps an overworld dirt pad → Nether dirt pads AND no powdery cane  *(HIGH — breaks two features at once)*

**Your hypothesis was right.** Both symptoms are one bug.

**Root cause.** `GenerationJob.forceOneTree` (`GenerationJob.java:333`) is the "island is never bare" last resort: it
fires whenever `treesPlaced == 0`. It calls `clearPlantingSpot` (`GenerationJob.java:365`), which **unconditionally**
stamps a 5×5 `Blocks.DIRT` disc + a `GRASS_BLOCK` root cell (`FORCE_TREE_PAD_RADIUS = 2`) *before* it even tries to
place the feature.

On a Nether soul/forest island the powdery-cane feature (`mynethersdelight:patch_powdery_cane`) is the **only entry in
the `trees` list**. When it fails to place normally, `treesPlaced == 0` → `forceOneTree` lays the dirt pad → the cane
still can't grow (it needs soul-sand / nylium, not grass) → you're left with a **bare 5×5 overworld dirt pad and no
cane**. That's the "5×5 dirt pads" on the large Nether Wild island *and* the missing powdery cane, in one mechanism.

**Fix (recommend a + b):**
- **(a)** Make `clearPlantingSpot` dimension/theme-aware — pave the pad with the island's resolved **surface/soil**
  block (soul_sand on nether_soul, crimson/warped nylium on nether_forest, netherrack, end_stone, …) instead of
  hardcoded `DIRT` + `GRASS_BLOCK`. Then the cane can actually grow on the pad.
- **(b)** Only commit the pad if the feature actually places (try placement first, or roll the pad back on failure), so
  a feature that can never succeed can't litter bare pads.
- **(c, optional)** Don't let non-tree "plant" features (cane) trigger the bare-island tree guard at all.

**Also verify:** that `mynethersdelight:patch_powdery_cane` resolves in the installed MND jar — if the id is wrong it
never places, which *guarantees* the fallback fires every time.

**Verify:** throw `nether_soul` in a soul-sand-valley biome → powdery cane on soul sand, **no** dirt pad; throw
`wild_nether_large` across all 5 Nether biomes → no overworld dirt/grass pads anywhere.

---

### B2a — Blossom / "Trumpet" trees  *(✅ largely working — narrow re-verify)*

**Update (2026-07-06 retest):** the swamp override **works** — a `forest` seed over swamp grows the lavender variant,
which renders in-game as **"serene trumpet leaves."** So the earlier "blossoms never appear" was wrong.

**What was going on.** In this Quark version the **"Blossom Trees" module is the "Trumpet Trees"** — `quark-common.toml`
headers `[world.blossom_trees]` with `#(Trumpet Trees)`. The registry still uses the color keys we already target
(`blue`/`lavender`/`orange`/`yellow`/`red`), each mapped to the same biomes as our override:

| Config color | Biome tag | Our band |
|---|---|---|
| blue | `c:is_snowy` | snowy band |
| lavender | `c:is_swamp` | swamp band ✅ confirmed ("serene trumpet") |
| orange | `minecraft:is_savanna` | savanna band |
| yellow | `c:is_plains` | plains band ✅ confirmed ("sunny trumpet", ~9 throws) |
| red | badlands | desert/badlands band |

So `quark:lavender_blossom` **does** resolve (display name "serene trumpet leaves") — the merge and the blossom ids are
fine; they're just branded "trumpet trees" in this version's lang.

**Resolved (2026-07-06):** the plains/yellow variant ("sunny trumpet leaves") turned up after ~9 throws — so the earlier
0/5 was just variance on the 50/50 variant roll, not a bug. Swamp + plains both confirmed; **B2a signed off.** The
optional B2a data test (§9, force each blossom band) is still worth landing to guard the merge against future
regressions, but no fix is needed.

---

### B2b — Shale "not found" on `ancient`  *(⚠ not a bug — visibility/tuning)*

**Diagnosis (2026-07-06, from the player's logs + source).** Shale **does** generate — the id-mismatch theory was wrong:
- `quark:shale` **registers** (log: `Registering to minecraft:block/item - quark:shale`).
- It's in the resolved ore pool — the gametest `quarkStonesCompatTargetsAncient` (`SkyseedGameTests.java:329`) is green,
  and `quark_ancient.json` appends it top-level + merges it into the `max_y:20` / `min_y:96` bands.
- **No gen-time warning** in the client log — `OrePlanner.java:54` prints `theme ore references unknown block '…'` for a
  skipped id, and there is no such line for shale. So it's recognised and placed.

**Why it's hard to see (two compounding factors):**
1. **Dark-on-dark camouflage.** `ancient`'s body is `tuff` fill over a **`deepslate` core** (`ancient.json:6-7`) and
   shale is placed at `depth: core` — near-black Quark shale inside dark deepslate. Limestone (cream) + jasper (pink)
   pop; shale doesn't, especially under shaders.
2. **Sparse on the smallest island.** `ancient` is the smallest theme (radius 6-9). Shale is *last* in a 16-entry ore
   list and `OrePlanner` places in list order (`OrePlanner.java:52-68`), depleting the small core with the 13 base
   deepslate ores first — so shale still places (~3-5 short veins) but few/small.

**Decision (pick one):**
- **Accept** — it's an inert-safe flavour "extra"; subtlety is arguably fine.
- **Make it read** — bump shale `vein_size`/`count`, and/or move the Quark ores earlier in the resolved list so they're
  not starved on small cores (⚠ reorder/count changes shift the RNG stream → **golden-master recapture** on every ancient
  tier), and/or scatter a little shale into the `tuff` fill (lighter body) instead of only the deepslate core.

**Verify (visibility):** `/give @s quark:shale` next to deepslate to gauge contrast; throw `huge_ancient` (bigger core →
more/larger veins) — shale should be clearly present.

---

### B3 — Snowy/frozen rocky islands lose their base ores (and never go deepslate)  *(HIGH — ✅ FIXED in code)*

**Symptom (Arie, huge_rocky over deep frozen ocean, germination ≈ −40):** the island came out plain stone/diorite/andesite
with **only** the mod ores (bauxite, lead, limestone, jasper, certus) and **none** of the base ores (no iron/coal/redstone/
diamond), and not deepslate despite the very low Y.

**Root cause (confirmed — Arie's hypothesis was right: "a mod override is screwing up the base setup").** The 3 base
rocky themes fold `biomeswevegone:howling_peaks` into their snowy band (BIOMECOVERAGEPLAN F6), but the **9 mod-override
snowy bands** (Quark / IE / AE2 × rocky / rocky_large / huge_rocky) list only the vanilla snowy biomes. `sameSelectorAs`
required a **byte-exact** biome-list match, so those bands failed to merge and were **prepended** as standalone bands.
A prepended snowy band (ore-only) then (a) wins the first-match for any snowy/frozen biome at **any Y** — shadowing the
`max_y:8` deepslate band — and (b) its `ores` list **replaces** the base ores, so the island keeps only mod ores and
loses its snow surface. Every snowy/frozen rocky/large/huge island was affected, not just low throws.

**Fix (code — the robust one Arie asked for):** `BiomeOverride.sameSelectorAs` → **`canAbsorb`**: a base band now absorbs
a patch band when their `min_y`/`max_y`/`dimension` match **and the base band's biomes are a superset of the patch's**
(⊇), instead of requiring exact equality. A third-party override can list the ordinary vanilla biomes and still merge —
it never needs to know we fold in `howling_peaks`. Backward-compatible (an exact match is a superset of itself).
`BiomeOverride.java` + `ThemeOverride.java` (`mergeBands`). **Compiles on both nodes.** No data files changed — the 9
overrides now merge as-is.

**Still needed:** ⚠ **golden-master recapture** (resolution changes for the snowy rocky bands — the recapture diff will
also surface any *other* override that was silently prepending) + a **guard gametest** (assert each rocky-tier snowy band
carries both a base ore and the mod ores after resolve — would have gone red before this fix).

**Part 2 (deepslate beats biome at low Y) — ✅ DONE (Arie confirmed "an island thrown at −30 should be deepslate,
independent of biome").** Moved the `max_y:8` deepslate band *ahead of* the snowy band in all 3 base themes
(`rocky.json` / `rocky_large.json` / `huge_rocky.json`; documented with a `_comment`). Now: low throw (y≤8) → deepslate
regardless of biome; mid/high over a frozen biome → snow. All override bands still merge (via `canAbsorb`) so the
resolved order follows the base order. JSON validated; folds into the same recapture.

**Verify:** throw `huge_rocky`/`rocky_large` over a snowy/frozen biome → snow-capped island with base ores **+** mod ores;
over a non-frozen biome at y≤8 → deepslate.

---

### B4 — Portal twin lands at the wrong location  *(MEDIUM/HIGH — screenshot)*

**Symptom.** Cross-dimension traversal does not arrive at the paired repaired frame; the destination location is wrong
(screenshot: two portal frames side by side).

**Context.** PORTALTWINPLAN shipped as option B — forced matched rotation, jigsaw anchor seated at `origin.xz` for a
block-exact centre-based twin link (ref [[skyseed-portal-twin-alignment]]). The link math (8:1 nether/overworld scale +
centre-based linking) is the place to look.

**Repro (Arie, 2026-07-06):** portal seed thrown **in the Nether**. Nether frame `151,68,-7 → 154,72,-7`; overworld
twin frame `1216,67,-66 → 1219,71,-66` (germination ≈ just below the seed).

**Analysis.** `linkedPortalPos` (`TwinPlacer.java:58`) maps nether→overworld as `×8` — correct, and the X roughly
follows (152×8 ≈ 1216). But **Z is ~10 off** (nether Z −7 ⇒ overworld −56 expected, frame is at −66) and X ~1 off. The
two frames **cannot** both be `origin + fixedOffset` / `origin×8 + fixedOffset` with an integer origin — the gaps
(X 1065, Z −59) aren't divisible by 7 — so there's a real asymmetry: either `placeTwinNear` nudged the twin off `linked`
(should be a no-op in the void), or the opening isn't seating on the centre anchor as `IslandGenerator.java:169` claims.

**Twin log result (both directions):** `nudge=(0,0,0)`, `grewAt == linked`, `linked == origin×8 / origin÷8`. So the
island **origins are block-exact 8:1** — `linkedPortalPos` + `placeTwinNear` are correct. The residual ~1-block-down +
~1–2-block-XZ drift Arie sees is **inside the frame placement**, not the twin coordinate.

**Static analysis (exhausted, all say "should be exact"):** `top_dome` is fixed at 1 in both the base + nether override
(not random); the two templates (`portal.nbt` / `portal_nether.nbt`, `RuinedPortalTemplates`) are **geometrically
identical** — same frame cells + same anchor `(1,0,1)` (only decor differs: chest/gold/extra lava on the overworld
one); `PORTAL_ROTATION == NONE` (no pivot); the single-piece seat (`Jigsaw.placeSinglePiece`) uses `placePos =
origin − anchor` (XZ) and a `−(box.minY + groundLevelDelta)` Y shift — both identical for the two templates. Yet the
frames drift. Portals still **link** (within vanilla's search radius), so it's an alignment/polish bug, not a hard break.

**Root cause (from the probe logs):** the **horizontal twin nudge**. In the O→N throw `placeTwinNear` found the linked
nether spot occupied and nudged the twin **+6 X** (`nudge=(6,0,0)`), so the opening linked 6 blocks off. In the N→O
throw (`nudge=(0,0,0)`) the openings are block-exact 8:1 (nether origin 169 → overworld 1352 = 169×8) — the seating math
is correct; `frame = origin−1` only offsets the frame *around* the opening, which is what vanilla links through.

**✅ FIXED (`TwinPlacer.twinSearchSpots`):** twins are now **vertical-only** — never nudged sideways, so the opening
always lands on the exact 8:1 XZ column; small vertical lifts still dodge a vertical obstruction (vanilla's portal search
spans the Y column, so a Y shift still links). If nothing vertical is clear, it grows right on the link. Compiles both
nodes; no golden-master impact (twin placement is runtime, not planned output).

**Verify:** throw a portal seed → both frames sit at the 8:1 coordinate and traversal lands in the paired frame; the
`twin B4` log's `nudge` should now read `(0,y,0)` (never horizontal). Then remove the `twin B4` + `portal B4` probes.

**Verify:** step through → land inside the paired repaired frame, correct rotation.

---

## §3 Tuning

### T1 — Crude oil is far too rare / hidden

Immersive Petroleum **is installed** (`ImmersivePetroleum-1.21.1-4.4.1-38.jar`), so the "not found" is rarity + location,
not a missing mod. Compared to its neighbours it's stacked against you:

| Ore (rocky IE island) | chance | band |
|---|---|---|
| aluminum | 0.85 | all bands |
| lead | 0.65 | all bands |
| nickel | 0.55 | all bands |
| **crude oil** | **0.15** | **only the `max_y: 8` deepslate band** |

So oil is ~4× rarer than the rarest metal **and** confined to the bottom ~8 blocks (which B3 may be preventing from
resolving at all on large tiers). **Fix:** bump to ~`0.40–0.50`, vein `3–5`, and/or add the oil vein to more than just
the deepslate band, in `immersiveengineering_rocky.json` + `_rocky_large` + `_huge_rocky`. Re-test **after** B3 so the
deepslate band actually resolves.

---

## §4 Mod-curation decision

### M1 — Kinetic acceptor doubled: drop `create_ae_generator`?

The pack ships **both** `create_ae_generator` (Create rotation → AE2 energy) **and** `createaddition` (Create rotation ↔
FE). With AE2's own Energy Acceptor (FE → ME) present, the path `Create rotation → createaddition FE → AE2 Energy
Acceptor → ME` already exists — which makes `create_ae_generator`'s kinetic acceptor the likely duplicate you're seeing.

**Recommend:** drop `create_ae_generator`, keep the `createaddition → AE2` route, verify the energy path still closes,
then regenerate `mods.txt` via `gen-mods-txt.ps1` ([[skyseed-modpack-mod-management]]). Your call — confirm nothing
else depends on it first.

---

## §5 UX / config

### U1 — `R` keybind collision
`R` is bound to both Immersive Aircraft **dismount** and Iris **shader reload**. Rebind one (client keybinds). Low effort.

### U2 — Immersive Aircraft won't accept diesel / kerosene  *(✅ resolved — not a config bug)*
**Investigated (`config/immersive_aircraft.json`):** the aircraft burns **item** fuel only — `fuelList` = `blaze_powder`,
plus `acceptVanillaFuel: true` (so coal/charcoal/any furnace fuel works). IP **diesel/kerosene are fluids**, so they
can't go in the item fuel slot — that's why "the fuel bit doesn't work." The `create_completeimmersiveaircraft` addon
only adds crafting recipes, no fluid-fuel engine. **So the plane is already fuelable (coal or blaze powder); there's no
fluid path to add.** If you want a tech-flavoured fuel, add a *solid* modded fuel item (e.g. an IE coke id) to `fuelList`
with a burn value — tell me the item + value and I'll add it. Otherwise this is answered, not a bug.

---

## §6 Support questions (answered)

- **S1 — Fastest creative energy for the Excavator.** No creative energy cell mod is bundled (no Powah/Mekanism). Fastest
  path: **Create `Creative Motor`** (infinite rotation) → a rotation-to-FE generator (`createaddition`, or the
  `create_ae_generator` acceptor) → wire straight into the Excavator; **Flux Networks** can ship it wirelessly if the
  Excavator is on another island. (Can confirm the exact generator block name in-pack if you want.)
- **S2 — "FE power chain flows across islands"** just means: energy generated on one island (Create/IE) can be carried to
  a machine on a *different* island (AE2/IE) — i.e. Forge Energy cabling / wireless works across the void gap. It's the
  #39 sign-off. Will reword that line in PLANOFPLANS to plain language.
- **Nether Soul "has no soul-sand-valley override"** — correct **by design**, not a bug: the base `nether_soul` theme
  *is* the soul-sand-valley island, so soul_sand_valley needs no override band. The missing cane is B1, not this.

---

## §7 Signed off (confirmed good — do not re-test)

- Quark **limestone**, **jasper**; **Ancient Tomes** in structure chests
- Quark **blossom / "Trumpet" trees** — swamp ("serene") + plains ("sunny") confirmed (B2a)
- Farmer's Delight **wild cabbage / carrot / onion**, **wild rice**
- Farmer's Delight **quest book** (renders perfectly)
- **Meteorites** — all tiers read correctly
- **Biome seeds**, **Wild**, **Explore**, **Iron's Spells structures** — all resolve correctly
- **Trial Chamber** — as intended
- **Crash-resume** — as intended

---

## §8 Next actions (suggested order)

1. **B1** ✅ fixed in code (`clearPlantingSpot` surface-aware). Needs golden-master recapture + the red→green nether-dirt test.
2. **B3** ✅ fixed in code (`canAbsorb` subset-merge) **+ Part 2 done** (deepslate band reordered ahead of snowy in the 3
   base themes → low throw = deepslate, any biome). Needs recapture + guard test.
3. **T1** oil — with B3 fixed, a deliberate low (y≤8) non-frozen throw now yields deepslate **+** oil. Decide whether to
   also broaden it (top-level / bump `0.15`) so it's not low-throw-only.
4. **B2b** shale — confirmed present; optional visibility tune (bump veins / scatter into `tuff`).
5. **B4** portal twin — *needs your repro (seed + coords)*.
6. **U1/U2/M1** — config + curation (independent, low-risk).
7. ✅ **Golden master verified — NO recapture needed.** B1a + B3 + Part 2 leave the 5 locked test themes
   (`gametest/island`·`water`·`features`·`structure`·`bad`) byte-identical: none has a `theme_override`, snowy/deepslate
   band, or `forceOneTree` path, so the changes can't move a fingerprint. **Proven by running both suites — 1.21.1: 219
   tests, 26.1.2: 221 tests, 0 failures**, including the new B3 guard, the updated MND cane test (now checks the ground
   crop), and all quark/AE2/create/BWG merge tests (canAbsorb + reorder didn't break them). Remaining release steps: a
   `mod_version` bump + CHANGELOG entry (owner), and in-game verifies of B1b (cane heights) + B4 (portal link) then pull
   the two diagnostic probes.

**Land a headless test alongside each generation fix** — see §9. ✅ **B3 guard test DONE** —
`rockySnowyBandsMergeModOresAndDeepslateWins` in both suites (asserts each rocky-tier snowy band is the `howling_peaks`
base band carrying base ore **+** all 3 mods' ores, and that the deepslate band precedes it). Compiles both nodes; will
run in the recapture's gametest pass. B1 (nether-dirt) test still to write (needs the E1/E2 harness bits).

---

## §9 Headless test coverage to add

Most of these "is it there or not" questions **can** be caught headless in the gametest suites
(`gametest/SkyseedGameTests.java` + `gametest_26_1_2/SkyseedTests.java`) — with one sharp limit.

### What headless can and cannot catch  *(read this first)*

- ✅ **Data-wiring** — an id/variant is present in the resolved theme, in the right Y-band, in the right variant, and
  the override merge didn't shadow it. Existing patterns already do this:
  `Themes.resolve(...)` → `resolved.ores().stream().anyMatch(o -> o.block().value().equals("id"))`
  (`SkyseedGameTests.java:312`), the Y-band check `bandHasOre(t, ov -> ov.maxY()...==8, "id")` (`:465`), and the
  decoration-namespace check `groundHasNamespace(band, "ns")` (`:448`). This catches **B1, B3, T1** and any merge/order
  regression.
- ❌ **Modded-id validity** — whether `quark:shale` / `mynethersdelight:patch_powdery_cane` / `quark:yellow_blossom` is a
  *real* registered id in the shipped modpack. The dev/test env doesn't load those mods, so a **wrong id is
  indistinguishable from a valid-but-inert one.** Proof: `quarkStonesCompatTargetsAncient` (`SkyseedGameTests.java:329`)
  already asserts `quark:shale` is in the resolved ores and is **green**, while the block is missing in-game. So B2b is
  the layer a gametest structurally can't reach — that residual stays an in-game (or id-manifest) check.

### Harness enhancements (unlock deterministic variant tests — your point)

- **E1 — force a decoration variant.** `DebugForce` (`DebugForce.java:11`) currently only pins `rareIndex` + `waterfall`;
  `pickVariant` (`IslandGenerator.java:205`) is still RNG. Add a `variantName`/`variantIndex` field so a test can pin the
  *exact* decoration variant (e.g. `powdery_valley`, `yellow_blossom`). This is what makes the "the variant that's
  supposed to have it, has it" tests deterministic instead of luck-of-the-roll.
- **E2 — a full-generation block-scan helper.** Most tests assert on the *plan* (`p.blocks()`), but `forceOneTree`'s dirt
  pad is stamped at run-time via `level.setBlock` during the tick drain — it isn't in the plan. Add a helper that runs
  the full `GenerationJob` into the test level (as the portal tests already inspect `level.getBlockState`, `:1477`) and
  asserts `containsNo(blockId)` / `contains(blockId)` over the finished island.

### Per-finding tests

| # | Test (type) | Asserts | Catches in dev env? |
|---|---|---|---|
| **B1** | `netherIslandHasNoOverworldDirt` (full-gen, needs E1+E2) | Force `powdery_valley` on `nether_soul` in the_nether, run full gen → island contains **no** `minecraft:dirt` / `minecraft:grass_block`. | ✅ **Fails today** — the cane is inert in dev → `treesPlaced==0` → `forceOneTree` stamps the dirt pad. Reproduces the bug now. |
| **B1** | `netherThemesCarryNoOverworldSoil` (data sweep) | Extend the existing Nether block-presence test (`:1099`) — every `nether_*` + `wild_nether*` theme's variants/scatter never reference `dirt`/`grass_block`. | ✅ |
| **B2a** | `quarkTrumpetVariantOnForestBands` (data, needs E1) | Force each blossom band (plains/swamp/…) on `forest`+`forest_large` → the selected variant's ground contains `quark:*_blossom_sapling` and trees contain `quark:*_blossom`; merge didn't shadow the base band. | ✅ (wiring only) |
| **B2b** | *(id-validity — see caveat)* | The existing `:329` test already covers the wiring. Real fix needs the **installed jar**: either an in-game `/give` check, or an **id-manifest parity test** (commit the expected modded-id list, diff against a registry dump from a modpack CI run). | ❌ not by gametest |
| **B3** | `rockyLargeDeepslateBandResolves` (data) | `Themes.resolve("skyseed:rocky_large")` → the `max_y:8` band has deepslate `core` + the deepslate ore set, **and** no broader override band (empty/all-Y selector) precedes/shadows it. | ✅ reproduces the regression if it's resolution/ordering |
| **T1** | `crudeOilReachesDeepslateBand` (data) | Mirror the AE2 test (`:458`): `immersiveengineering_rocky[_large/huge]` resolved → `immersivepetroleum:crudeoil_fluid_block` present in the `max_y:8` band via `bandHasOre`. (Rarity/tuning stays in-game.) | ✅ catches a merge drop |
| **B4** | `portalTwinLinkCoords` (compute) | Given a source frame, the twin's computed destination frame origin equals the expected centre-linked / 8:1-scaled coords. Extends the existing portal-rotation tests (`:1477`). | ✅ if it's a math bug |
| U1/U2/M1 | — | Runtime mod config / keybind — **out of scope** for gametests. | — |

**Net:** B1, B3, T1, B4, and the B2a wiring are all worth a headless test (and B1 likely goes red immediately, which is
the best kind). B2b is the one genuine gap — track it as an in-game sign-off or build the id-manifest check if we want
CI to guard modded-id renames going forward.
