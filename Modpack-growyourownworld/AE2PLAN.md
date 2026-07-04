# AE2PLAN — Applied Energistics 2 (NeoForge 1.21.1)

> Child of [CONTENTPLAN §2](CONTENTPLAN.md) — PLANOFPLANS **#18** (certus/sky-stone bootstrap), **#41** (AE2 quest
> chapter), **#39** (FE-flow proof — AE2 is now the second FE *consumer* after IE). AE2 is the **storage endgame**
> (CONTENTPLAN §5 row 4), deliberately **gated as a rare, expensive endgame reward** because it trivializes inventory
> management (§ Rarity). It **self-multiplies** after a one-time bootstrap (budding certus + crystal growth), so the
> integration is a *rare bootstrap*, not a full ore economy — with one sharp exception: the **inscriber presses**
> (§ The real work).

## ✅ Landed — jars in `overrides/mods/` (`mods.txt` @ 125 mods)

| Jar | Version | Role | Deps (all present) |
|---|---|---|---|
| `appliedenergistics2` | 19.2.17 | The mod. **Native FE consumer** — its Energy Acceptor converts the pack's Create → Crafts&Additions → Flux FE straight to AE; no bridge jar (same story as IE). | GuideME |
| `guideme` | 21.1.16 | AE2's **one required dependency** (the in-game guidebook engine, split out of AE2). Ships separately or AE2 won't boot. | none |
| `ae2wtlib` | 19.5.0 | AE2 Wireless Terminals — universal wireless terminal + wireless crafting/pattern access. Top QoL, zero worldgen. | AE2 |
| `AE2-Things` | 1.4.2-beta | **The thematically important addon for a skyblock.** Crystal Growth Chamber (bulk/automated certus+fluix growth → straight into the ME network — automates the self-multiply loop the bootstrap seeds), Advanced Inscriber, DISK cells. ⚠ **CurseForge-only** for NeoForge — the Modrinth `ae2things` is a *different, Fabric-only* mod. | AE2 |
| `megacells` | 4.11.0 | MEGA Cells — 1M–256M ME cells + MEGA crafting CPU + bulk/compression cells. The literal "storage endgame". Sits fine alongside Sophisticated Storage. | AE2 |
| `create_ae_generator` | 1.21.1-1.1 | **Create: AE Generator** — a "Kinetic Acceptor" block that converts Create **stress** directly into AE power (the direct kinetic→AE path, alongside the Create→FE→Energy-Acceptor route). CurseForge-only. | Create + AE2 |
| `createstockbridge` | 0.2.0 | **Create Stock Bridge** — links Create's Stock/logistics network to the AE2 ME network: Create stock items show as craftable in AE2, and Create packagers can pull from AE2 storage. | Create + AE2 |
| `rechiseledae` | 1.0.1 | **Rechiseled: AE2** — 100+ decorative chisel variants of AE2 blocks (connected textures). Pairs with the pack's `rechiseled` + `rechiseledcreate`. Cosmetic/QoL. | Rechiseled + AE2 |

## Additional AE2×pack bridges — available, not yet added

Evaluated against the installed set; add later if wanted (all NeoForge 1.21.1):

- **Immersive Energistics** (`immersive-energistics` 1.1.0-beta) — the **official** IE support addon for AE2: adds
  ME-capable wires (ME Connectors = 8 channels, Dense ME wires = 32) so AE2 channels run over IE-style wire
  connectors across gaps. On-theme with IE as the backbone; a *physical* island-to-island ME link alternative to
  `ae2wtlib`'s wireless/Quantum Bridge. Deps IE + AE2 (both present). **Recommended add.**
- **Applied Create** (`applied-create`) — a *deeper* Create×AE2: store Create rotational **stress** in ME cells
  (tiered like item/fluid cells), Stress P2P tunnels, and pattern providers that auto-feed Create Mechanical
  Crafters. Optional depth; partly overlaps the two small Create bridges above.

## Deliberately NOT added (the curation cuts)

- **Applied Mekanistics** — Mekanism↔AE2 bridge; **Mekanism is dropped** (CONTENTPLAN §7). Nothing to bridge.
- **Applied Flux / AppFlux** — FE storage/cabling *into* AE2 is **redundant**: the Energy Acceptor already takes the
  pack's FE natively, and Flux Networks is the decided universal power backbone (CONTENTPLAN §1). Adding it just
  duplicates Flux.
- **Applied Botanics** — Botania integration; Botania isn't in the pack.
- **Create: Applied Kinetics** — another kinetic→AE energy mod; **overlaps `create_ae_generator`**. Skip.
- **AdvancedAE** — quantum/large crafting + utilities; overlaps MEGA, and pulls the extra **Glodium** lib. Cut in
  curation (chose Core + QoL + MEGA); revisit only if a specific feature is wanted.
- **Extended AE / Expanded AE / AE Additions** — more terminals/patterns/cells that overlap `ae2wtlib` + MEGA.
- **AE2 JEI Integration / Jade tie-ins** — AE2 19.x has native JEI/REI/EMI + Jade support; no bridge jar needed.

## Configs (to add under `overrides/config/`)

- **`ae2-common.toml`** — belt-and-suspenders: disable meteorite worldgen in `[worldGen]`. (Meteorites are already
  inert in the void — see below — but pinning the flag documents intent and guards a future non-void dim.) Also
  review `[worldGen] spawnChargedQuartz` and grinder/energy ratios; defaults are fine.
- The **"Disable AE2 Meteors" datapack** is a ready-made alternative (disables meteors *and* ships press-bypass
  recipes) — noted as a fallback for the press problem below; must load at world-gen time, not after.

## Governing invariants (do not regress)

- **FE→AE is native — no bridge jar for power.** AE2's Energy Acceptor consumes the pack's FE grid (Create → C&A →
  Flux) directly, exactly like IE. `create_ae_generator` (kinetic→AE) is an *addition*, not a requirement.
- **Storage is gated on sky stone + presses, NOT on certus.** Verified in the jar: **sky stone is effectively
  meteorite-only** (its only recipe smelts `ae2:sky_dust`, which itself only comes from grinding `ae2:sky_stone_block`
  → circular) and it gates the ME **Controller**; the **presses** are meteorite-only and gate **processors**. Certus,
  by contrast, is renewable early (`ae2:transform`: charged certus + `quartz_block` → `damaged_budding_quartz`). So
  **don't gate certus behind the island** — it's a mid-game resource; keep the endgame lock on sky stone + presses.
- **AE2 self-sustains after the bootstrap** — budding certus + crystal growth (or the AE2 Things Crystal Growth
  Chamber) multiplies certus/fluix indefinitely; the meteorite island only provides the *irreplaceable* sky stone +
  presses, never an ongoing supply.
- **MA likely auto-seeds certus** — Mystical Agriculture mints seeds for registered crystals/gems the same way it
  does IE metals ([IEPLAN invariant](IEPLAN.md)); **verify** a certus-quartz seed exists (and note it would only add
  a *second* certus path — still no sky stone/presses, so it doesn't break the gate).
- **Presses are the exception to "self-sustains"** — see below; they're the one non-renewable, non-craftable gate.
- **AE2 is a rare, endgame reward** — it trivializes inventory management, so the *bootstrap* is deliberately hard
  to reach and never leaks into common throws (see § Rarity below). Don't regress it into an early/incidental grab.

## Rarity & progression gating (design intent)

AE2 **trivializes inventory management**, so it is deliberately an **endgame** reward: the *bootstrap* must be
**genuinely rare** to reach, while *expansion* afterwards may be easier but stays **expensive**. The pack's lever is
**seed acquisition** — each theme is its own `IslandSeedItem`, obtained by recipe/quest (see its javadoc) — so
rarity is tuned *there*, not by frustrating on-island RNG. Firm decisions:

- **The endgame unlocks live on a dedicated gated `skyseed:meteorite` theme + its own seed — NOT on a rocky/ancient
  override.** What the island *uniquely* provides is **sky stone** (→ the ME Controller) **+ the four inscriber
  presses** (→ processors) — the two things that are effectively meteorite-only (verified below) and that ME
  *storage* is gated on. Its own theme, its own seed; leaking sky stone / presses into common rocky throws would hand
  AE2's endgame out by accident. Settles the open `#38/#20` question — for AE2, dedicated, not shared. (The island
  still carries certus growths for theme, but certus isn't the gate — see the gate-ingredient bullet above.)
- **Certus is the gate ingredient — a small *finite* deposit on rocky/ancient (user call, 2026-07-03).** Seed a rare,
  finite certus deposit on the rocky + ancient mining islands (an override) and make it a **required ingredient in
  the meteorite-seed recipe**, so you must have mined certus in normal play before you can grow the AE2 island. **Use
  `ae2:quartz_block`** — verified in the jar as the ideal deposit: a **full solid cube** (so no attachment/placement
  caveat, unlike the amethyst-style `quartz_cluster`), completely **inert** (it's the terminal block a budding block
  decays into — grows nothing), it drops itself, and a shapeless **deconstruction** recipe turns 1 block → **4×
  `ae2:certus_quartz_crystal`**. Seed it via the `ores` list; keep chance/vein_size **small**; mirror to all six
  `*_rocky{,_large}` / `huge_*` + `*_ancient…` tier files per [skyseed-mirror-island-tiers].
  - *Why this doesn't undercut the endgame:* certus is **not** what AE2 *storage* is gated on. Even the renewable
    certus loop is reachable early — `ae2:charged_certus_quartz_crystal` + `ae2:quartz_block` → `ae2:transform` →
    `ae2:damaged_budding_quartz` (verified recipe), so certus + a Charger self-sustains without a meteorite. But an ME
    network needs a **Controller (sky stone)** and **processors (presses)**, and **both sky stone and the presses are
    effectively meteorite-only** (verified below). So rocky certus only unlocks *low-tier dabbling* (Charger,
    Inscriber, quartz tools) — never storage. The endgame lock is the meteorite island, not the certus.
- **The seed is the rarity gate: expensive + late, payload guaranteed.** Gate the `skyseed:meteorite` seed behind the
  **IE backbone** (uncraftable until IE steel + a Create precision mechanism / electron tubes are on tap) and place
  its quest **after** the Storage + IE chapters (endgame, #41). Put all the rarity in *getting the seed*; once grown,
  the four presses + one budding block are **guaranteed** — no double-RNG on the island. You only ever need **one**
  bootstrap, so a very expensive seed is entirely fine.
- **Expansion: easier, still expensive — don't cheapen it.** After the bootstrap, certus self-multiplies (budding →
  buds → Crystal Growth Chamber) so *raw material* is cheap, but the value-add stays costly and must not be shortcut:
  - keep AE2 + MEGA **native recipes** (processors, cells, MEGA tiers) — ship **no** KubeJS recipe that lowers
    cell/processor cost;
  - presses duplicate one-at-a-time at an **iron block** each (native) — cheap per copy, never free;
  - a live network is an **ongoing FE draw** on the Create → Flux grid;
  - keep the seed **repeatable-but-expensive** so more sky stone is obtainable when a build needs it, without it ever
    being cheap. Optionally tier the MEGA cells behind extra quest steps.

## The real work (#18) — ✅ SHIPPED end-to-end (both nodes green)

Both acquisition problems are solved and shipped; full detail lives in `CHANGELOG_1.21.1.md` + git (the meteor-island
design was in the now-retired `METEORPLAN.md`). The design rationale that still governs is in **Governing invariants**
and **Rarity & progression gating** above; the shipped shape:

- **Certus + sky stone (the easy half).** A dedicated `skyseed:meteorite{,_large}` / `huge_meteorite` theme (#18a) —
  a **normal overworld island a meteor crashed onto** (#18e visual redesign): grass body + an obsidian/scorched
  **crater** holding an `ae2:sky_stone_block` **globe** (the sky-stone source; the old ore veins were dropped). Full
  seed integration across all three tiers (recipe, Patchouli entry `mod:ae2`-gated, advancements), inert-safe (AE2
  blocks skipped *before* the RNG roll → byte-identical without AE2). The **certus gate deposit** (#18d) is a small
  finite `ae2:quartz_block` vein on the six `appliedenergistics2_{rocky,ancient}{,_large,huge_}` overrides, merged
  into every Y-band. Gametests `meteorIslandFormsCrater` + `appliedEnergisticsCertusReachesMiningTiers`, both nodes.
- **Inscriber presses (the hard blocker) — solved by a tiered Meteorite Core (#18b).** The four presses have no
  from-scratch recipe (meteorite-loot / Inscriber-duplication only) and a single block can't tier its own loot — so
  skyseed's **first custom block `skyseed:meteorite_core`** replaces AE2's property-less Mysterious Cube at the globe
  centre and scales the drop by tier: **small → 1 random press · medium → 2 distinct (uniform over 6 `expand:false`
  pair-tags) · huge → all 4** — tag-based loot (inert without AE2), iron-tier harvest. The MA essence→press/sky-stone
  bypass recipes were removed so the gate holds.
- **Rarity gate (#18c).** The pack's KubeJS override pushes the seed recipe behind the **IE backbone** (steel + a
  Create precision mechanism + certus); the jar default is an AE2-conditioned sky-stone + certus recipe (craftable
  with just Skyseed + AE2). A **1% wild meteor** (globe, no core) on natural overworld islands is the standalone
  sky-stone bootstrap, toggled off in the pack via `wildMeteorChance` (`skyseed-common.toml = 0.0`).
- **Meteorite worldgen** is already dead in the void (`SkyseedVoidChunkGenerator.createStructures` no-op); a
  belt-and-suspenders empty `#ae2:has_meteorites` tag documents intent. **MA certus seed** verified native — nothing to add.
- **Quest chapter (#41)** — 16 quests `B404`–`B419` in the **Storage** chapter (AE2 = the storage endgame), gated off
  `B104` (rocky→certus) + IE steel `B908`; weaves the #39 Energy-Acceptor step. **In-game-verified 2026-07-04**
  (renders + deps resolve; the user repositioned nodes so lines don't cross — do not revert).

## Left to do (in-game sign-off only)

- [~] **(#39) FE-flow proof — proven on paper, in-game sign-off pending.** The chain is type-compatible end to end
  (Create rotation → C&A `alternator` → Flux `flux_plug`/`flux_point` wireless → IE native FE / AE2 `energy_acceptor`,
  all standard NeoForge FE — no bridge jar). **The one thing left needs a human:** (a) generator island → Flux Plug;
  (b) AE2 island → Flux Point → Energy Acceptor → ME Controller powers up + a terminal works; (c) IE draws the same
  network; (d) optional Create shaft → Kinetic Acceptor → AE. Then promote to a Tier-1 sign-off. *(shared with IEPLAN #39)*
- [ ] **Meteor island throw-test + tuning** (the retired METEORPLAN's residual — the only meteor work left): confirm
  the crater/globe/core read right at each tier; tune meteor sizes (first-pass `{2}`/`{3,4}`/`{5,6}`) and the crater
  palette weights (obsidian vs glass vs magma vs basalt) in-game. Optional polish: a `smooth_sky_stone_block` globe
  shell; whether the meteorite island keeps a budding-certus block for on-island convenience.

## Caveats

- **Presses are the whole ballgame** — everything downstream is gated on them. ✅ **RESOLVED (2026-07-04):** press
  acquisition ships as the tiered **Meteorite Core** (small→1 / medium→2 distinct / huge→4, iron-tier harvest, MA
  essence-press bypass removed), and the quest chapter is authored + in-game-verified. The only AE2 item still open is
  the **#39 FE power-chain in-game sign-off** (proven on paper; needs one human "it powered up").
- Youngish crossover ports (`create_ae_generator`, `createstockbridge`, `rechiseledae`, and any future
  `immersive-energistics`/`applied-create`) — keep versions pinned and watch patch notes before quests point at
  their content.
- `AE2-Things` is a **beta** (1.4.2-beta) and CurseForge-only for NeoForge — watch it across AE2 updates; the Crystal
  Growth Chamber is load-bearing for the self-multiply story, so verify it survives AE2 bumps.
