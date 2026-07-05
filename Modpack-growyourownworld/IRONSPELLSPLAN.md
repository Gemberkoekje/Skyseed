# Iron's Spells + Artifacts + Relics — the magic/exploration pillar — open items only

> Child of CONTENTPLAN (#36/#37). **Largely built in-branch, both nodes green:** the adaptive Explore seed (3 tiers,
> full onboarding) + loot layer (mod auto-inject + inert-safe `add_drop` GLMs) + biome-theme rares on every tier +
> magic-mob guardians + all Iron's own structures re-homed 1:1 (empty-target + `hasTemplatePool` inert-guard +
> auto-centre) + custom Impaled Boat + debug-seed coverage + the #46 quest chapter. See the changelogs + git.
> The three oversized-structure rebuilds live in `IRONSTRUCTUREREBUILDPLAN.md`. Trimmed to OPEN items.

## What's left (in-game / decision)
- In-game throw-test structure + rebuild fit & centering on both nodes (Frozen Warren, War Barrow, Mage's Sanctum,
  and the reused mod structures).
- In-game verify loot payout (mod auto-inject + `arcane_essence`/`upgrade_orb` GLMs) and Lootr per-player instancing.
- Load the #46 Magic & Exploration quest book in-game.
- Boot-verify the 14-jar set loads clean (watch the player-animation-lib vs Iron's version warning).
- **Decision:** resolve `createrelics` — confirm content/deps on boot, then keep or drop.
