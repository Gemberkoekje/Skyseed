# Mod Structures in the Void — Audit + Resurrection Plan

> **Status (PR #14): NOT STARTED — deferred to a later changeset.** This is a *child of BWGPLAN* (same biome-adaptation
> vehicle) and nothing here is urgent: the void generator already no-ops every mod structure, so BWG's structures
> **don't leak** — they're just unreachable content. Pick this up when there's appetite to resurrect villages / manors /
> the bog trial as growable islands. The audit + triage below are done and current.

> **Plan audit (2026-07-01):** the prerequisite (BWGPLAN Step 1 — wood biome adaptation) is marked ✅ shipped for the
> forest family, so this plan is now unblocked whenever it's picked up. **The scope decision is now made — FULL SCOPE**
> (all villages + manor + bog trial; see [Scope decision](#scope-decision--resolved-2026-07-01-full-scope)). The
> village↔biome mapping and the resurrection steps remain open — see [`../PLANOFPLANS.md`](../PLANOFPLANS.md) for priority.

## Audit result: only BWG adds structures

A scan of every jar for `worldgen/structure_set` + `worldgen/structure`:

- **Oh The Biomes We've Gone** — the *only* mod with worldgen structures: **11 structure sets, 17 structure files**.
- Every other mod (Create + all submods, Mystical Agriculture / Botany, Sophisticated *, Silent Gear, Waystones, Xaero, JEI / Jade, libraries) adds **none**. (Waystones places its waystones via village jigsaw additions, not its own structure set — moot in the void anyway.)

## These do NOT leak — they're already suppressed

`SkyseedVoidChunkGenerator.createStructures(...)` is overridden to a **no-op** (both the 1.21.1 and 26.1.2 signatures): *"No structure starts ⇒ nothing places, any dimension, any mod."* The void is structurally immune to any biome/structure mod by construction, with the biome source left intact for theming.

So **there is no leak to fix** — BWG's manors/villages/arches never spawn. The real consequence is that those 17 structures are **unreachable content**, locked behind worldgen that will never run. The opportunity (and the rest of this plan) is deciding which deserve **resurrection as growable Skyseed islands** — exactly how Skyseed already reimplements the village, woodland mansion, trial chamber, ocean monument, etc. as seeds.

## Triage of BWG's 17 structures

**Worth resurrecting (jigsaw, real content):**

| BWG structure | start_pool | Skyseed analogue |
|---|---|---|
| **village** — 6 styles: forgotten, pumpkin_patch, red_rock, salem, skyris, swamp | `biomeswevegone:village/<style>/…` | `village_center` / `hamlet` |
| **aspen_manor_1 / _2** | `biomeswevegone:aspen_manor_1` / `_2` | `woodland_mansion` |
| **bog_trial** | `biomeswevegone:bog_trial` | `trial_chamber` |
| **prairie_house / abandoned_prairie_house** | `biomeswevegone:prairie_house` / `abandoned_prairie_house` | `hamlet` variety |
| **rugged_fossil** | `biomeswevegone:rugged_fossil` | (minor — fossil / bone) |

**Leave suppressed (pure scenery, no progression value):** `dripstone_arch`, `lush_arch`, `red_rock_arch`, `sharpened_rock`, `ironwood_gour_plateau`, `large_cold_lake`. Landscape garnish — nothing to grow, no reason to resurrect. No action needed; the void already drops them.

## Mechanism — Skyseed already assembles jigsaw pools

Skyseed island themes assemble a referenced jigsaw `pool` (the `rare_structures` field: `"jigsaw": { "pool": "skyseed:trail_ruins/ruins", "target": "minecraft:bottom", "depth": …, "pad": …, "sink": … }`). Resurrecting a BWG structure = pointing a Skyseed jigsaw at BWG's `start_pool`. Two routes, mirroring BWGPLAN:

1. **Biome adaptation (preferred — shares BWGPLAN's vehicle):** add `biome_overrides` so the existing structure seeds assemble the BWG pool when grown over the matching BWG biome —
   - `village_center` / `hamlet` over a BWG biome → that biome's BWG village (red_rock village over the red-rock biome, swamp village over bayou/cypress, skyris village over skyris, pumpkin_patch over pumpkin_valley, …).
   - `woodland_mansion` over `aspen_boreal` → aspen manor.
   - `trial_chamber` over a bog biome → bog trial.
   The structures then fall out of the same "throw the right seed over the right biome" loop as the wood islands. Inert without BWG (unknown pools skipped).
2. **Dedicated seeds:** only if a structure should be a *goal* (cf. BWGPLAN Q3). Probably unnecessary here — the village / mansion / trial seeds already exist.

## Risks / unknowns

- **Vertical-jigsaw bounding boxes.** Foreign multi-piece jigsaws (manors especially) can hit the bounding-box collision rule that bit the End City rebuild. Each resurrected pool needs correct `target` / `pad` / `sink`, headroom, and a placement gametest.
- **Foreign pool internals.** BWG pools may use processor lists / jigsaw-block targets that assume vanilla terrain or a road/street context (villages); verify each assembles cleanly on a flat island pad.
- **Village style ↔ biome mapping.** Confirm which BWG biome each of the 6 village styles belongs to (red_rock / swamp / skyris / pumpkin_patch are guessable; **salem** and **forgotten** need checking) so the `biome_overrides` target the right biome.

## Sequencing — this is a child of BWGPLAN

Shares the biome-adaptation vehicle; land *after* BWGPLAN Step 1 (wood islands) proves the loop and confirms the BWG biome ids:

1. ✅ **BWGPLAN Step 1** — wood biome adaptation (establishes the pattern). **DONE** (shipped for the forest family: `theme_override/biomeswevegone_forest{,_large,_huge}.json`).
2. **Villages** — the highest-value resurrection; adapt `village_center` / `hamlet` to the 6 BWG village biomes.
3. **Manor + bog trial** — adapt `woodland_mansion` / `trial_chamber`; carries the bounding-box risk, so gametest each.
4. **Prairie houses / rugged fossil** — optional polish (hamlet variety / a minor fossil island).

Each mod step bumps `mod_version` + CHANGELOG, with an inert golden-master gametest (byte-identical without BWG), per the standing rules.

## Scope decision — ✅ RESOLVED (2026-07-01): FULL SCOPE

~~First pass — resurrect **just BWG villages**, or **villages + aspen manor + bog trial** together?~~

**Decision (user, 2026-07-01): resurrect everything — all 6 village styles, across all their biomes, AND the special
structures (aspen manor + bog trial).** No villages-only first pass; the manor and bog trial are in scope now (they
carry the vertical-jigsaw risk, so each gets its own placement gametest per the Risks section). This resolves
PLANOFPLANS item #12 and puts items #14 (villages), #26/#27 (manor/trial), #28 (vertical-jigsaw mitigation),
#29 (on-pad assembly verification) and #30 (per-step release hygiene) all in scope. Prairie houses / rugged fossil
(#49) remain optional polish. Sequencing (villages → manor+trial → optional polish) is unchanged — full scope means
we work the whole list, not that order stops mattering.

### Future want (record, don't schedule yet) — MORE structures than BWG originally shipped

Beyond resurrecting BWG's own 17, the eventual goal is **net-new bespoke structures that fit the modpack**, e.g.:

- **Little Create sheds** housing small working apparatus (a token contraption / kinetic gadget on a growable pad).
- **Abandoned Inferium farmlands** (derelict Mystical Agriculture plots — ruined farmland + a few essence crops to
  reclaim), tying structures to the MA loop.
- …and similar flavour builds (the list is open-ended).

These are **future / long-tail**, not part of the current full-scope pass — captured here so the ambition isn't lost.
Each would follow the same jigsaw-pool + biome-adaptation (or bespoke-seed) vehicle and the standing per-step
gametest + version-bump discipline. Tracked in PLANOFPLANS' long tail.
