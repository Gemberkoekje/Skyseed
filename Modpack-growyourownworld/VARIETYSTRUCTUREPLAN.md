# Structure Variety — a weighted 5% surprise on every overworld seed (#68) — open items only

> **The whole B1–B30 catalog SHIPPED and #68 is closed** (commit "closes PLANOFPLANS #68", both nodes green):
> the Phase-0 weighted-gate engine (`rollRare` + `weight`/`requires`/`explorable` fields + `Lookup.modLoaded` +
> the Explore reward-floor filter, a backward-compatible opt-in), Band 1 (12 vanilla commons), Band 2 (11 mod-gated
> rares across Create/IE/AE2/Iron's/MA/FD, each inert without its mod), and Band 3 (Ruined Chapel + the 5 multi-mod
> epics). D1–D5 all decided; **D5 large/huge rate settled = flat 5% on all sizes**. See the changelogs + git.
> Trimmed to OPEN items.

## What's left
- **In-game tuning pass** — throw across tiers, confirm the 5% rate feels right, no floating/void-leak/burying,
  chests open; tune weights.
- **Band 4 — grand epic structures (#74)** — the six Band-3 "epics" are pad-5/6 sheds; the huge tier can host grand
  versions, plus a catalog cross-wiring pass. **Now owned by `../EPICSTRUCTUREPLAN.md`** (plan-first, unbuilt).

## Keep as reference (the engine future work builds on)
- The weighted-gate model: `rare_structure_chance` present → weighted pick over `weight`/`requires`/`explorable`;
  absent → legacy byte-identical. D3 Explore reward-floor (the premium seed never forces a loot-less build);
  D4 mod builds stay derelict (loot never leaks a gate-key — AE2 sky stone/presses, MA prosperity, Upgrade Orb).
