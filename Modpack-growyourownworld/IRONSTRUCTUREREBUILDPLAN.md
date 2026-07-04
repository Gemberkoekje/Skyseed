# IRONSTRUCTUREREBUILDPLAN — home-curated, island-friendly versions of the oversized Iron's structures

> Child of [IRONSPELLSPLAN.md](IRONSPELLSPLAN.md). Grew out of the **first in-game throw-test** of the 1:1 Iron's Spells
> structures (owner playtest, 2026-07-04). Several are simply too big / too sprawling for a floating island. This plan
> is the disposition of all of them, and the design of the three that need a **home-curated rebuild** — the same move we
> already made twice: the **[Trial Chamber](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/TrialChamberTemplates.java)**
> (a modular jigsaw dungeon at island scale) and the **[Impaled Boat](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/RareStructureTemplates.java)**
> (a small custom wreck standing in for the too-big mod ship).
>
> **Goal (owner's words):** *"similar to the trial rebuild we did, capture the same grand feeling, but on a smaller
> scale."* Not a shrink-ray on the mod's NBT — a **hand-built skyseed jigsaw** that reads as the same place, sized to sit
> on (and inside) an island we can actually grow.

## The proven pattern (what "rebuild" means here)

A `*Templates.java` builder writes code-authored `.nbt` pieces ([`Built`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/Built.java) →
[`StructureParts`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/StructureParts.java) → `StructureWriter`), a
`template_pool/*.json` wires the pieces into a jigsaw with a `minecraft:bottom` start anchor, and the host theme's
`rare_structures` entry points at the **skyseed** pool (always present — no mod dependency; the mod's *loot and mobs*
still flow in via the GLM + the guardian packs). Debug seeds are auto-derived by `ThemeScanner`; add an `xAssembles`
gametest to **both** suites. Regen `.nbt` on the 1.21.1 node (see [[skyseed-structure-staging]]) — remember the
**two-build dance** for a brand-new structure (first run writes the `.nbt`, second run can load it).

## Disposition of every reused Iron's structure

| Structure | Home | Playtest verdict | Action |
|---|---|---|---|
| **Impaled Icebreaker** | `huge_aquatic` (cold) | overhung `aquatic_large` | ✅ **done** — moved to huge cold-ocean + custom [Impaled Boat](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/RareStructureTemplates.java) covers `_large` |
| **Mountain Tower** (wizard) | ~~`rocky_large`~~ → `huge_rocky` | "slightly outgrew its island" | ✅ **done** — moved up a tier + archmage guard pack |
| **Mangrove Hut** | ~~`lush`~~ → `lush_large` | "lands outside the island, way too small" | ✅ **done** — moved up a tier + necromancer; **watch:** may still need `huge_lush` |
| **Evoker Fort** | `huge_forest` | "could be centred a bit more, but pretty good" | ✅ guard pack added; **needs** the centring offset (Enabler A) |
| **Pyromancer Tower** | `huge_desert` | "needs to be centred; go 1 deeper but don't bury the courtyard in sand" | ✅ auto-centred (§A) + guard pack + seated 1 into the ground via **`excavate`** (§D — a plain `sink` would bury its open courtyard) |
| **Ice Spider Den** | `huge_frozen` | "huge area under the island… recreate entirely to control sprawl; needs a deeper island" | ✅ **REBUILT** — Frozen Warren (§Den) |
| **Ancient Battleground** | `huge_badlands` | "way too large… home-curate to be island-friendly" | ✅ **REBUILT** — War Barrow (§Battleground) |
| **Citadel** | `huge_rocky` | "absolutely amazing… but too big; settle for a smaller, less epic version" | ✅ **REBUILT** — Mage's Sanctum (§Citadel) |

## Shared enablers (build before / alongside the rebuilds)

### A. Auto-centre reused mod structures — ✅ SHIPPED (2026-07-04)
Several mod structures assemble off-centre because their start jigsaw sits at a **corner/edge** of the build, not the
middle — so our pad-centred origin lands them lopsided, hanging off an island edge (evoker fort, pyromancer tower, wizard
tower). The mod NBT is fixed, but the positions we stamp aren't. Rather than a hand-tuned per-structure offset (a value to
measure in-game for each), [`Jigsaw.placeCapped`](../../src/main/java/dev/gemberkoekje/skyseed/compat/Jigsaw.java) now
**auto-centres** any **blank-target** placement: after the jigsaw assembles, `recentreOnOrigin` measures the union
footprint of all pieces and counter-shifts them so the footprint centre sits on the island centre (X/Z only — the start
piece's Y seating on the pad is preserved). Blank target == reused mod pool == corner anchor, so this fires for exactly
those and skips our own skyseed pools (`target: minecraft:bottom`, already centred by their central anchor). No
per-structure values; works for multi-piece forts too. Guarded by the extended `emptyTargetJigsawUsesPoolDefaultStart`
gametest (placed-block centroid near origin), both nodes. **Verify in-game** on the mod-structure debug seeds — it centres
**horizontally**; the Ice Spider Den's *downward* sprawl still needs its rebuild (§Den).

### B. Guardians — the mod structures spawn **no enemies** on our copies
Confirmed: none of the 1:1 rares carried a `mobs` pack, and the mod's own casters spawn via its **structure-spawn
config**, which doesn't fire on our jigsaw-placed copies (our islands aren't registered as the mod's MC structures; any
spawner *baked into* the NBT would still fire, but these have none). Fix = a theme-level `mobs` pack (spawned at island
centre, `MobPlanner` — **inert-safe**: an unknown id is warned-and-skipped, so `irons_spellbooks:*` guardians ship in the
base mod and no-op in CI). Verified entity ids (from `irons_spellbooks-1.21.1-3.16.1.jar`):
`archevoker`, `magehunter_vindicator`, `pyromancer`, `cryomancer`, `ice_spider`, `necromancer`, `cultist`, `priest`,
`citadel_keeper`, `dead_king`, `apothecarist`, `fire_boss`, `frozen_humanoid`. **Done this pass** for the five keepers
(wizard→archevoker+vindicator, mangrove→necromancer, evoker fort→archevoker+2 vindicators, pyromancer→pyromancer+2
cultists, icebreaker→cryomancer+2 ice_spiders). The three rebuilds carry their own (baked spawners **and** a mob pack).

### C. A **deeper** frozen island for the Den
The Ice Spider Den is a *downward* dungeon (its pool goes start → cavern → dungeon → basement). Even a curated version
wants vertical room below the surface. `huge_frozen` already has `dome 2-4` + `max_under_depth 16`; the rebuild should
`sink` into that body, and we may deepen the underside (`max_under_depth`, a taller teardrop) so the cellar sits in
island, not void.

### D. `JigsawConfig` **excavate** — seat an open-courtyard mod structure into the ground — ✅ SHIPPED (2026-07-04)
`sink` is designed to *bury* a structure under the island surface (a temple roof hidden under the sand) — right for a
skyseed structure that carves its own air, but a reused **mod** structure with an **open courtyard** carries no explicit
courtyard air, so sinking it just fills the courtyard with the pad's surface layer (owner playtest: the Pyromancer Tower
courtyard buried in sand). New `"excavate": true` on [`JigsawConfig`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/theme/JigsawConfig.java)
makes [`IslandGenerator`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGenerator.java) drop the **pad**
itself to the sunk floor level (`gy - sink`) instead of levelling at the surface — so the structure is *set into the
ground* with its interior left clear, not buried. Default false (classic bury-sink, byte-identical for every existing
structure). Applied to the pyromancer (`sink 1`, `excavate true`); data-guarded in `modStructureRaresAreInertWithoutTheMod`
(the mod pool is inert in CI, so the pad behaviour is in-game-verify).

## §Den — Ice Spider Den → "Frozen Warren" — ✅ BUILT (2026-07-04)
[`FrozenWarrenTemplates`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/FrozenWarrenTemplates.java) —
**one deliberate piece** with a known ~13×13 footprint and ~15-tall profile, so the depth is fully controlled (no
runaway under-island sprawl): a **surface ice-fort mouth** with a doorway + ice spikes (echoing the mod's entrance), a
**ladder shaft** down, and a **single ice cavern** carved into the island body — explicit `air` interior with an organic
jittered radius, lined by an ice shell (so it's enclosed even where the island body thins), ice pillars + cobweb nests +
a cave-spider spawner, and an ice-tomb **dais with an `underwater_ruin_big` chest**. Host rare on `huge_frozen`:
`skyseed:frozen_warren/warren`, `sink 10` (fort floor → surface, cavern buried y1–9), `pad 8`, `mobs` = cryomancer +
ice-spiders (§B, inert-safe). §C applied — `huge_frozen` `max_under_depth` deepened **16 → 22** so the cellar sits in
island, not void. Assembly gametested both nodes (`frozenWarrenAssembles`: ice shell + ladder shaft + chest + spawner).
Debug seed `debug_huge_frozen_frozen_warren` (auto). **In-game throw-test** the sink/fit + no-box feel.
*(Follow-up if wanted: a deeper sub-vault below the cavern, or a modular top so the fort varies.)*

## §Battleground — Ancient Battleground → "War Barrow" — ✅ BUILT (2026-07-04)
[`WarBarrowTemplates`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/WarBarrowTemplates.java) — one
bounded ~19×19 **surface** piece for `huge_badlands` (no carving; a scattered open-air composition, naturally anti-box):
an earthen **barrow mound** (coarse dirt / packed mud, a rounded dome) with a hollow **crypt** inside — the "inside" — a
skeleton spawner + a `buried_treasure` coffin chest reached through a doorway in the mound face; a stepped
**necromancer's altar** on the cap (polished-blackstone dais, soul-lantern corner posts, lit black candles, a skull
plinth, the `pillager_outpost` reward chest); a ragged ring of **gravestones** (varied mossy/deepslate walls + caps),
two **A-frame war tents** (one caved in), a broken **spruce palisade** with felled logs, bone piles, dark-oak **skull
pikes**, a doused soul-campfire and dead bushes over trampled ground. Host rare on `huge_badlands`:
`skyseed:war_barrow/barrow`, `pad 10`, surface (no sink), `mobs` = necromancer + cultists (§B, inert-safe). Assembly
gametested both nodes (`warBarrowAssembles`, on BIG_REGION — earthworks + altar lantern + reward chest + crypt spawner).
Debug seed `debug_huge_badlands_war_barrow` (auto). **In-game throw-test** the fit + graveyard feel.

## §Citadel — Citadel → "Mage's Sanctum" — ✅ BUILT, then enlarged v2 (2026-07-04)
**v2 (owner playtest push):** grown to a **21×21** keep with taller floors and a much grander **double-height library**
(bookshelf walls + a **two-wide mezzanine** balcony around a central void + four chandeliers + lecterns that sit on real
floor). Fixes from the first pass: **one boss** only — `citadel_keeper` (the old `dead_king` + `necromancer` fought each
other, and the dead king was oversized and shattered the floor); the **ladder core** now climbs one course into the
keeper's chamber so there's a full 2-block step-off (was a 1-block crawl); the mezzanine lectern sits on an actual
balcony cell (was floating); the **vault** candles moved to wall sconces off the walk-path (a candle was blocking the
way down). **v2.1:** mezzanine railing now traces only the void boundary (was ringing the landing shut) with a gap where
the ladder lands, so the balcony is reachable + walkable; and the square keep no longer perches on the round island —
`pad 15` (covers the footprint corners) + the ramparts/turrets get a **3-block foundation skirt** footed into the island.
`sink 5`. The v1 spec follows for reference:

[`CitadelTemplates`](../../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/CitadelTemplates.java) — the
flagship. One deliberate ~15×15 × ~25-tall keep for `huge_rocky` (a single piece for full control of the verticality),
keeping what made the mod's grand: **verticality + the library**. Levels, bottom-up:
- **Buried vault** (y0-3, `sink 4`): a trial **Vault** + a `woodland_mansion` chest, deepslate + candlelight.
- **Entrance hall** (y4-7): a pillared ground floor with a grand arched front gate + a hearth.
- **The library** (y8-14, double-height — the showpiece): **bookshelf-lined walls** both storeys, a **mezzanine**
  balcony ring with a railing, two **hanging chandeliers** (chain + lanterns), lecterns, a carpet runner, arched windows.
- **Keeper's chamber** (y15-19, set back): a throne dais + soul-lantern, a `trial_chambers/reward_ominous` chest, banners.
- **Deepslate spire** (y20-25) tapering to a soul-lantern finial; a **crenellated rampart skirt** + corner turrets + a
  front gate wrap the base. A single **ladder core** threads vault→keeper. Buttressed/windowed/set-back massing = no box.
Host rare on `huge_rocky`: `skyseed:citadel/keep`, `pad 9`, `sink 4`, `mobs` = **citadel_keeper + dead_king + necromancer**
(§B, inert-safe — a real flagship fight). Both chests + the Vault hit Upgrade-Orb-tier GLM tables. Assembly gametested
both nodes (`citadelKeepAssembles`: library shelves + Vault + chests + chandelier + ladder core; seated low in BIG_REGION
as the keep out-tops the 24-tall test region). Debug seed `debug_huge_rocky_citadel` (auto). **In-game throw-test** the
fit + the grand feel. *(A giant `huge_citadel` tier to host the real mod citadel is still the open stretch, below.)*

## Loot & onboarding (all three)
Chests bind **vanilla** tables the GLM already injects (Arcane Essence / Upgrade Orb / relics) — no new GLM entries
needed: Den → `chests/igloo`+`underwater_ruin_big`; Battleground → `chests/pillager_outpost`+`buried_treasure`; Citadel
→ `chests/woodland_mansion`+`trial_chambers/reward_ominous`. Each rebuild is a plain skyseed rare on its huge theme:
`ThemeScanner` gives it a debug seed for free; add `xAssembles` to both gametest suites.

## Sequencing
1. **Enabler A (offset)** + confirm the five keepers' guardians in-game (this pass) — smallest, unblocks evoker/pyromancer/wizard.
2. **Den** — ✅ done (Frozen Warren; single controlled piece, validated §C deeper island).
3. **Battleground** — ✅ done (War Barrow; surface graveyard-camp, bounded ~19×19).
4. **Citadel** — ✅ done (Mage's Sanctum; the flagship — bounded ~15×15 × ~25-tall keep, library showpiece). **All three rebuilds shipped.**

## Open questions
- Can `huge_rocky` be grown big enough to host the **real** citadel instead of a rebuild? (Owner asked.) Radius is
  24-30; the mod citadel wants more. A one-off `huge_citadel`-tier theme (radius ~40+) is possible but stresses the
  tick-budgeted `GenerationJob` and the pad math — the rebuild is the safer bet, with a giant tier as a stretch.
- Mangrove Hut: if `lush_large` still clips, promote to `huge_lush` (already exists) rather than rebuild — it's small.
