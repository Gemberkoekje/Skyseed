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

## The real work (#18) — bootstrap, and the press blocker

Two acquisition problems, one easy (like IE's ore island) and one that's the actual design decision (like IE's
Excavator):

**1. Certus + sky stone — the easy half (IE-ore-island pattern).** Certus/sky-stone normally come from **meteorite
worldgen**, a decoration-step feature — so it's **already dead in the Skyseed void** (`SkyseedVoidChunkGenerator`
skips decoration, same reason the IP oil reservoir is inert; see [skyseed-theme-override-inert-safety]). On-island
path = a "meteorite / sky-stone" island seed carrying, **all via the `ores` list** (the only inert-without-the-mod
channel — `OrePlanner` skips an unknown block id *before* the RNG roll, so the file stays byte-identical without
AE2). **IDs verified against `appliedenergistics2-19.2.17.jar` — modern AE2 has NO certus *ore* block; certus comes
from budding blocks, not an ore:**
  - `ae2:sky_stone_block` — the meteorite material and **the island's whole reason to exist**: the ME **Controller**
    is `smooth_sky_stone_block` + `ae2:fluix_crystal` + `ae2:engineering_processor` (smelt sky_stone_block → smooth),
    plus sky stone gates chests/tanks. **Effectively meteorite-only (verified):** the only sky-stone recipe smelts
    `ae2:sky_dust`, and sky_dust *only* comes from grinding `ae2:sky_stone_block` (inscriber) → fully circular, no
    independent source. This island is the sole sky-stone supply. Seed it as the island body/vein.
  - `ae2:flawless_budding_quartz` — a budding block for **theme/convenience**: it grows `small`/`medium`/
    `large_quartz_bud` + `quartz_cluster` on its exposed faces → `ae2:certus_quartz_crystal` (charge in a Charger for
    fluix), and the AE2 Things Crystal Growth Chamber automates it. ⚠ **Buds only grow on air/water-exposed faces** —
    place it in a small open pocket, not fully walled in the core. *Not load-bearing:* certus is already renewable
    off-island (rocky `quartz_block` deposit → transform), so this is flavor, not the gate.
  *(There is no `ae2:quartz_ore` — an earlier draft used that non-existent id; modern AE2 has no certus ore.)*
  This is a **dedicated `skyseed:meteorite` theme** (decided — see § Rarity), **not** an override on rocky/ancient:
  vanilla stone/deepslate body (so it degrades gracefully without AE2), sky stone + budding seeded via the `ores`
  list. Mirror to `_large` / `huge_` per [skyseed-mirror-island-tiers] (presence in all three tiers, base-density
  counts — `OrePlanner` scales per volume at runtime). Keep the sky-stone body **generous but finite** — enough for
  a real network, re-growable via another (expensive) seed when a build needs more.

**2. Inscriber presses — the hard blocker (the design decision).** The four presses (`ae2:calculation_processor_press`,
`ae2:engineering_processor_press`, `ae2:logic_processor_press`, `ae2:silicon_press`) gate *all* processor crafting.
**Verified in the jar:** they have **no from-scratch recipe** — the only sources are (a) breaking `ae2:mysterious_cube`
in a **meteorite**, which drops one random press from `#ae2:inscriber_presses`, or (b) *duplicating* a press you
already own in the Inscriber with an iron block (`recipe/inscriber/*_press.json`: press mold on top + iron block →
that press). The craftable `ae2:not_so_mysterious_cube` is a **red herring — it consumes all four presses + a
Controller**, so it can't bootstrap them. In the void there are no meteorites → **AE2 is hard-blocked.** This is
AE2's Excavator-equivalent. The gate is *one-time*: grant **one of each of the four presses once** and iron-block
duplication covers the rest forever. Options, in order of preference:
  1. **Guaranteed loot on the bootstrap structure** — one of each press in a chest inside a `rare_structure` on the
     sky-stone island. Fits the pack ethos ("everything from a grown island"), deterministic once found, and the
     one-time nature means a single chest suffices. Preferred.
  2. **KubeJS recipe granting the four presses** — deterministic, gate behind sky stone + certus + iron so they're
     earned, not free. Simplest/most robust; least "discovery" flavor. Good fallback.
  3. **The "Disable AE2 Meteors" datapack** — ready-made (disables meteors + adds press-bypass recipes), but its
     recipe balance isn't ours and it must load at world creation. Use only if (1)/(2) are more work than warranted.
  Decide this first — the island in (1) is moot until presses are solved.

## Open work

> **Shipped 2026-07-03 (both nodes green — 1.21.1: 163 tests, 26.1.2: 165 tests):** #18a meteorite theme + full
> seed integration, #18d certus deposit, #18c seed gate, #18b presses (recipe route). Details in the checklist.

- [x] **(#18a)** **Dedicated `skyseed:meteorite` theme (+ seed) — SHIPPED.** New base themes
  `meteorite{,_large}` / `huge_meteorite` (`data/skyseed/skyseed/theme/`): **overworld body (#18e redesign)** —
  ~~sky-stone/budding/quartz_block ore veins~~ **(superseded by #18e — sky stone comes from the meteor GLOBE, not
  veins)**. Full **seed integration** like every other seed — registered in
  `ModItems.SEED_THEMES`, `skyseeds` tag, lang, item models, textures (copied from the ancient family, repaintable),
  and the required companion set per tier: a **recipe** (`recipes/data/skyseed/recipe/…`), a **Patchouli entry**, and
  `gathered_`/`reveal_`/`craft_` **advancements**. Gametest-guarded (`meteorIslandFormsCrater`,
  both nodes) — resolves + carries a meteor config on all three tiers + a base crater build smoke.
  **Inert without AE2:** worldgen is byte-safe (AE2 blocks skipped before RNG — the build-smoke gametest runs with no
  AE2 and forms the island as a normal overworld island + a vanilla meteor crater); the three Patchouli entries are gated **`"flag": "mod:ae2"`**
  so they don't advertise sky stone/certus/presses without AE2 (precedent: `exotic_biomes.json` → `mod:biomeswevegone`).
  The seed itself stays craftable without AE2 (a valid vanilla rock island) — graceful, like every other seed.
- [x] **(#18e — meteor VISUAL redesign) — SHIPPED 2026-07-03 (both nodes green — 1.21.1: 163, 26.1.2: 165).** Built
  per [METEORPLAN.md](METEORPLAN.md): a `Meteor` theme field (via the caves+meteor codec-slot refactor), a
  `MeteorPlacer` carve pass (crater + sky-stone globe + Mysterious Cube, scaled per tier, inert-safe), the generator
  hook, and 3 rewritten **overworld** theme JSONs (grass body, normal ores, the AE2 ore veins dropped). Gametest
  `meteorIslandFormsCrater` on both nodes. Original request follows —
  make the island read as a **normal overworld island that a meteor crashed onto**, not a charred rock ball:
  overworld body (grass/dirt/stone) + an impact
  **crater** (obsidian / scorched glass / magma) with a **sky-stone globe** seated in it and the **`ae2:mysterious_cube`**
  at the globe's centre (the press source, #18b). **Scaled by tier** — a ~5×5 meteor on the base island up to a large
  one on huge. Plan: a new `MeteorPlacer` carving pass mirroring `CaveCarver` (mutates the `blockMap`/`surfaceList`;
  inert-safe via `Lookup.hasBlock` — no AE2 → just a vanilla crater). ⚠ **Constraint found:** `planIsland` doesn't get
  the theme id and `IslandTheme.CODEC` is already at **DFU's 16-field group limit**, so a clean `meteor` theme field
  needs a small codec refactor (combine `caves`+`meteor` into one slot) — it's a real feature, not a one-liner. When
  this lands, the theme JSONs drop the sky-stone/budding/quartz_block `ores` (the globe replaces the vein) and switch
  to the overworld palette; gametest on both nodes. **Loot already shipped** (cube → all 4 presses).
- [x] **(#18d — certus gate deposit) — SHIPPED.** Six `appliedenergistics2_{rocky,rocky_large,huge_rocky,ancient,ancient_large,huge_ancient}.json`
  overrides seed a small finite `ae2:quartz_block` vein (chance 0.45, vein 2–4) into the top level **and** every
  Y-band (so it doesn't vanish on low/high throws), inert-safe. Gametest-guarded
  (`appliedEnergisticsCertusReachesMiningTiers`, both nodes). **Tunable** — throw-test the 0.45 chance.
- [x] **(#18c — rarity gate) — SHIPPED (modpack).** `overrides/kubejs/data/skyseed/recipe/meteorite_skyseed.json`
  shadows the mod's **jar default recipe** with the IE-gated one: **2× `ae2:quartz_block` (certus) + 2×
  `immersiveengineering:ingot_steel` + 1× `create:precision_mechanism` + 4× blackstone**. Uncraftable before the IE
  backbone runs and before you've mined certus. **(Phase 2, 2026-07-04:** the jar default is now a proper
  **AE2-compat** recipe — `neoforge:mod_loaded ae2` + **8× smooth sky stone + 1× certus crystal** — craftable with just
  Skyseed + AE2; see METEORPLAN Phase 2. The pack override still pushes it to the IE gate.) ⚠ **Bugfix 2026-07-03:** the override first shipped
  with **1.21.1-invalid string ingredients** (`"B": "minecraft:blackstone"`), which the ingredient codec rejects — so
  it failed to load *and shadowed* the mod's baseline, leaving the **base seed with no recipe** (large/huge, un-
  overridden, were fine). Fixed to object form (`"B": { "item": "minecraft:blackstone" }`). **Any kubejs vanilla-
  crafting recipe here must use object-form ingredients** (the mod's `recipes/` get auto-downgraded by build.gradle;
  modpack datapack files do not).
- [x] **(#18b — presses) — REVISED to the Mysterious Cube (2026-07-03), then to a tiered Meteorite Core
  (METEORPLAN Phase 3, 2026-07-04).** The sky-stone press *recipes* were 1.21.1-invalid (string ingredients) and are
  **deleted** (`kubejs/data/gyow/` removed). Presses now come from a **`skyseed:meteorite_core`** at the meteor
  island's centre — a jar-side custom block (skyseed's first) that **replaces AE2's property-less Mysterious Cube** so
  the drop can **scale with the meteor tier** (a single block can't tier its own loot): **small → 1 random press,
  medium → 2 distinct (uniform over all 6 pairs via `expand:false` pair-tags), huge → all 4**. Loot is **tag-based →
  inert without AE2**. The old `mysterious_cube.json` loot override is **retired** (the cube is no longer placed). Also
  **removed MA's gate-bypass essence recipes**
  (`overrides/kubejs/server_scripts/remove_ae2_essence_bypasses.js`): the four
  `mysticalagriculture:essence/appliedenergistics2/*_press` **and `sky_stone`** (essence → sky stone bypassed the
  whole gate); kept certus/fluix essence (renewable, not the gate). The cube itself is placed by the meteor feature
  (#18e).
- [x] **Meteorite worldgen — RESOLVED (already inert; belt-and-suspenders shipped).** AE2 meteorites are a **worldgen
  structure** (`data/ae2/worldgen/structure{,_set}/meteorite.json`, gated by the `#ae2:has_meteorites` biome tag =
  `#minecraft:is_overworld`), placed on a `random_spread` grid at the `top_layer_modification` step. **Primary
  guarantee (verified in code):** `SkyseedVoidChunkGenerator` overrides `createStructures` to an **empty no-op**
  (`worldgen/SkyseedVoidChunkGenerator.java:79` — "No structure starts ⇒ nothing places, any dimension, any mod"), so
  meteorites can **never** generate in the Skyseed void — the same mechanism that kills stray villages. (This is why
  the `ae2-common.toml` config route was moot — structures aren't config-gated here, they're suppressed at the
  generator.) **Belt-and-suspenders shipped** for gate integrity + intent:
  `overrides/kubejs/data/ae2/tags/worldgen/biome/has_meteorites.json` = `{ "replace": true, "values": [] }` — empties
  the biome tag so the meteorite structure has no valid biomes even if structures were ever re-enabled. (Emptying the
  tag is side-effect-free — only the meteorite structure + the now-moot Meteorite Compass reference it.) No `_comment`
  in the tag file (tag codec) — documented here instead.
- [x] **MA certus seed — VERIFIED (native), 2026-07-04.** Mystical Agriculture ships `certus_quartz_seeds` +
  `certus_quartz_crop` + `certus_quartz_essence` out of the box (its AE2 compat). So the rocky/ancient certus deposit
  is *alongside*-MA as intended (deposit = first bootstrap certus; MA crop = renewable once you have essence) — nothing
  to add.
- [~] **(#39) FE-flow proof — proven on paper; in-game sign-off pending.** The chain is **type-compatible end to
  end** (every link speaks standard NeoForge Forge Energy / `IEnergyStorage`), verified from the jars:
  1. Create rotation → **C&A Alternator** (`createaddition:alternator`) → FE.
  2. FE → **Flux Plug** (`fluxnetworks:flux_plug`, input) → the wireless Flux Network → **Flux Point**
     (`fluxnetworks:flux_point`, output) on any island — no cables across the void (Flux is wireless; CONTENTPLAN §1).
  3. Flux Point → the consumer: **IE** natively, or AE2's **Energy Acceptor** (`ae2:energy_acceptor`, native FE→AE
     ≈2 FE : 1 AE — no bridge jar), or the direct **`create_ae_generator:kinetic_acceptor`** (rotation→AE).
  Since C&A emits standard FE, Flux moves standard FE, and both IE and the AE2 Energy Acceptor consume standard FE,
  the flow is mechanically sound. **In-game sign-off (the one thing left, needs a human):** (a) generator island —
  waterwheel/windmill → Alternator → FE → **Flux Plug**; (b) AE2 island — **Flux Point** (same network) → **Energy
  Acceptor** → **ME Controller**; confirm the Controller powers up and a terminal works. (c) Confirm IE draws the
  same network. (d) Optional: Create shaft → **Kinetic Acceptor** → AE. Then promote to a Tier-1 sign-off.
- [x] **Immersive Energistics — ADDED.** `Immersive-Energistics-1.1.0-beta.jar` is in the pack (the IE-native ME-wire
  addon), complementing `ae2wtlib` wireless for island-to-island ME.
- [x] **(#41 / rolling #19)** **AE2 quest chapter — SHIPPED** (into the **Storage** chapter `A005`, per the "AE2 = the
  storage endgame" call, not a new chapter). **16 quests, `B404`–`B419`** appended to `chapters/storage.snbt` + text in
  `quests/lang/en_us.snbt`. Gated off the Skyseed spine (`B104` rocky = certus) and **IE steel `B908`** (so the seed
  quest is post-IE). Tree: Certus → **Meteorite Seed** → Sky Stone → Presses ↘ / Charger → Fluix ↗ → Processors →
  **ME Controller** → Drive+Cell / Terminal / **Energy Acceptor (the #39 FE weave)** / Growth Accelerator
  (self-multiply) → Wireless (wtlib) → **MEGA Storage**; two **optional** Create-bridge quests (AE Stock Bridge,
  Kinetic Acceptor). Item ids jar-verified; SNBT brace/id-balanced. **Not gametested** (FTB isn't in the skyseed dev
  env), but **VERIFIED in-game 2026-07-04** — the chapter renders and deps resolve. The user repositioned some quest
  nodes so the dependency lines don't cross (an intentional layout edit to `chapters/storage.snbt` — do not revert).

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
