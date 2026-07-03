# QUARKPLAN — Quark integration (NeoForge 1.21.1)

> Child of [CONTENTPLAN §2](CONTENTPLAN.md) — PLANOFPLANS **#15** (jars + module curation) and **#43** (quest chapter).

## ✅ Shipped — see CHANGELOG

**Quark SHIPPED v0.182.0** — 4 jars in `mods.txt` (Quark 4.1-481, Zeta 1.1-40, Quark Oddities marker, QuarkPonders
1.5.1), modules curated in `overrides/config/quark-common.toml`, and the void-death **Totem of Holding** relocated to
a lit island shrine (`TotemShrineEvents`, no-ops without Quark Oddities). Island integrations are their own plan,
[QUARKISLANDPLAN.md](QUARKISLANDPLAN.md) (#71).

**Governing config invariant (do not regress):** the block-providing World modules — **New Stone Types, Corundum,
Blossom Trees** — must stay **ENABLED**. Disabling a Zeta module *unregisters its blocks*, which would make the #71
`theme_override`s resolve to nothing; their pure worldgen is already inert in a void world, so leaving it on costs
nothing. Only **Glimmering Weald** is disabled (it adds a biome that trips the 1.21.1 biome-cycle bug
[Quark#5340](https://github.com/VazkiiMods/Quark/issues/5340)). Overlap-with-shipped-mods modules are also off
(Oddities Backpack/Pipes/Crates, Pathfinder Maps).

## Open

- [x] **(#15)** **Smoke pass — ✅ signed off in-game 2026-07-02** (all works as intended).
- [ ] **(#43)** **Minimal quest chapter** (build last, now unblocked by the #15 sign-off): a short branch — *Sort Your Life Out*
  (sorting/chest QoL) · *Enter the Matrix* (Matrix Enchanting setup) · *Hold That Thought* (Totem of Holding recovery,
  the void fix already shipped). Keep it minimal per QUESTPLAN.
- **Partner-gated add-ons (install when the partner lands):** **Quark Engineering** (IE compat) when **IE #34** lands;
  **Farmer's Cutting: Quark** (FD cutting-board recipes) when **Farmer's Delight #16** lands.

**Caveat:** young NeoForge port — keep the pairing pinned (Quark 4.1-481 + Zeta 1.1-40) and watch patch notes before
quests point at Quark features.
