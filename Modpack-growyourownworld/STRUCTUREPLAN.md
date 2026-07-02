# Mod Structures in the Void — Audit + Resurrection Plan

> **Status (2026-07-01, mod 0.181.0):** the audit + triage are done, the scope decision is **FULL SCOPE** (see
> the **Scope decision** section below), and the highest-value item — the **BWG
> villages** — has **fully shipped** via its child plan [BWGVILLAGEPLAN.md](BWGVILLAGEPLAN.md)
> (v0.176.0–v0.181.0, all 6 styles × 3 tiers; only its in-game Phase-5 sign-off remains there). **Open here:**
> the **aspen manor (#26)** and **bog trial (#27)** resurrections with their vertical-jigsaw mitigation (#28)
> and on-pad assembly checks (#29), the per-step release-hygiene rider (#30), optional prairie houses / rugged
> fossil (#49), the deferred dedicated-seeds alternative (#60), and the recorded-only net-new-structures long
> tail (#68). Priorities in [`../PLANOFPLANS.md`](../PLANOFPLANS.md).

## Audit result: only BWG adds structures

A scan of every jar for `worldgen/structure_set` + `worldgen/structure`:

- **Oh The Biomes We've Gone** — the *only* mod with worldgen structures: **11 structure sets, 17 structure files**.
- Every other mod (Create + all submods, Mystical Agriculture / Botany, Sophisticated *, Silent Gear, Waystones, Xaero, JEI / Jade, libraries) adds **none**. (Waystones places its waystones via village jigsaw additions, not its own structure set — moot in the void anyway.)

## These do NOT leak — they're already suppressed

`SkyseedVoidChunkGenerator.createStructures(...)` is overridden to a **no-op** (both the 1.21.1 and 26.1.2 signatures): *"No structure starts ⇒ nothing places, any dimension, any mod."* The void is structurally immune to any biome/structure mod by construction, with the biome source left intact for theming.

So **there is no leak to fix** — the remaining structures never spawn. The consequence is that they are **unreachable content**, and the rest of this plan is resurrecting the worthwhile ones as growable Skyseed islands — exactly how Skyseed already reimplements the village, woodland mansion, trial chamber, etc. as seeds.

## Triage of BWG's 17 structures

**Worth resurrecting (jigsaw, real content):**

| BWG structure | start_pool | Skyseed analogue | Status |
|---|---|---|---|
| **village** — 6 styles | `biomeswevegone:village/<style>/…` | `village_center` / `hamlet` | ✅ shipped (BWGVILLAGEPLAN, v0.176.0–v0.181.0) |
| **aspen_manor_1 / _2** | `biomeswevegone:aspen_manor_1` / `_2` | `woodland_mansion` | **open — #26** |
| **bog_trial** | `biomeswevegone:bog_trial` | `trial_chamber` | **open — #27** |
| **prairie_house / abandoned_prairie_house** | `biomeswevegone:prairie_house` / `abandoned_prairie_house` | `hamlet` variety | open — #49 (optional polish) |
| **rugged_fossil** | `biomeswevegone:rugged_fossil` | (minor — fossil / bone) | open — #49 (optional polish) |

**Leave suppressed (pure scenery, no progression value):** `dripstone_arch`, `lush_arch`, `red_rock_arch`, `sharpened_rock`, `ironwood_gour_plateau`, `large_cold_lake`. Landscape garnish — nothing to grow, no reason to resurrect. No action needed; the void already drops them.

## Mechanism — Skyseed already assembles jigsaw pools

Skyseed island themes assemble a referenced jigsaw `pool` (the `rare_structures` field: `"jigsaw": { "pool": "skyseed:trail_ruins/ruins", "target": "minecraft:bottom", "depth": …, "pad": …, "sink": … }`). Two routes, and a third proven by the villages:

1. **Adapt BWG's own pools:** add `biome_overrides` so the existing structure seeds assemble the BWG
   `start_pool` when grown over the matching BWG biome — `woodland_mansion` over `aspen_boreal` → aspen manor;
   `trial_chamber` over a bog biome → bog trial. Inert without BWG (unknown pools skipped). Carries the
   foreign-pool-internals risk below.
2. **Dedicated seeds (#60):** only if a structure should be a *goal* in itself (cf. BWGPLAN Q3). Judged likely
   unnecessary — the mansion / trial seeds already exist. Kept as a recorded fallback.
3. **Author our OWN jigsaw set in BWG's palettes** — ⭐ the route the **villages actually took** (user's call:
   more control + variety; the hermetic BWG-block `.nbt` engine in BWGVILLAGEPLAN). **The manor/bog-trial
   vehicle choice (route 1 vs 3) is the first decision of #26/#27** — route 3 avoids the foreign-pool risk at
   the cost of authoring the buildings.

## Risks / unknowns

- **Vertical-jigsaw bounding boxes (#28).** Foreign multi-piece jigsaws (manors especially) can hit the bounding-box collision rule that bit the End City rebuild. Each resurrected pool needs correct `target` / `pad` / `sink`, headroom, and a placement gametest. *(Not a village risk — those are flat street-network jigsaws and have shipped.)*
- **Foreign pool internals (#29).** BWG pools may use processor lists / jigsaw-block targets that assume vanilla terrain; verify each assembles cleanly on a flat island pad. For the villages this was satisfied by the own-authored pools + assembly gametests (`bwg_*_assemble*`); it only still applies to the manor/trial **if** they take route 1 (foreign pools) — under route 3 it becomes "on-pad assembly gametest per structure".

## Sequencing

1. ✅ Prerequisite (BWGPLAN Step 1, wood biome adaptation) — shipped.
2. ✅ **Villages** — shipped via [BWGVILLAGEPLAN.md](BWGVILLAGEPLAN.md) (Phase-5 in-game sign-off tracked there).
3. **Manor + bog trial (#26/#27)** — decide the vehicle (route 1 vs 3 above), adapt/author, gametest each
   (the bounding-box risk), ship per-step.
4. **Prairie houses / rugged fossil (#49)** — optional polish (hamlet variety / a minor fossil island).

**Per-step release hygiene (#30, standing rule):** each structure step bumps `mod_version` + CHANGELOG, with an
inert golden-master gametest (byte-identical without BWG). Complied with for every shipped village step.

## Scope decision — ✅ RESOLVED (2026-07-01): FULL SCOPE

**Decision (user, 2026-07-01): resurrect everything — all 6 village styles, across all their biomes, AND the special
structures (aspen manor + bog trial).** No villages-only first pass; the manor and bog trial are in scope (they
carry the vertical-jigsaw risk, so each gets its own placement gametest per the Risks section). This resolved
PLANOFPLANS item #12 and put items #14 (villages — since shipped), #26/#27 (manor/trial), #28, #29 and #30 in
scope. Prairie houses / rugged fossil (#49) remain optional polish. Sequencing (villages → manor+trial →
optional polish) unchanged.

### Future want (record, don't schedule yet) — MORE structures than BWG originally shipped (#68)

Beyond resurrecting BWG's own 17, the eventual goal is **net-new bespoke structures that fit the modpack**, e.g.:

- **Little Create sheds** housing small working apparatus (a token contraption / kinetic gadget on a growable pad).
- **Abandoned Inferium farmlands** (derelict Mystical Agriculture plots — ruined farmland + a few essence crops to
  reclaim), tying structures to the MA loop.
- …and similar flavour builds (the list is open-ended).

These are **future / long-tail**, not part of the current full-scope pass — captured here so the ambition isn't lost.
Each would follow the same jigsaw-pool + biome-adaptation (or bespoke-seed) vehicle and the standing per-step
gametest + version-bump discipline. Tracked in PLANOFPLANS' long tail.
