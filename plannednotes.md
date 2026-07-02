# Planned Notes

- [ ] Consider adding a compat which occasionally drops a waystone

## Void worldgen enforcement — custom ChunkGenerator — ✅ SHIPPED (v0.165.0, both nodes; in-game verified 2026-07-01)

Design reference for `SkyseedVoidChunkGenerator` (the javadoc points here). One
`SkyseedVoidChunkGenerator extends NoiseBasedChunkGenerator` with a `skipDecoration` flag, referenced
per-dimension in `world_preset/skyblock.json` (each dim on its own noise settings: overworld
`skyseed:void`, nether `skyseed:void_nether`, End `skyseed:void_end`):

| Dim | `skipDecoration` | Behaviour |
|---|---|---|
| Overworld | true | no-op `applyBiomeDecoration` (kills biome-mod features) + no-op `createStructures` |
| Nether | true | same (future-proofs against Nether biome mods) |
| End | false | keeps decoration (the central island survives) but still no-op `createStructures` |

**Why it exists:** TerraBlender biome mods (BWG / BYG / Terralith / Incendium) inject biomes into the overworld
`multi_noise` source Skyseed keeps for island theming; those biomes' *features* then decorated at the void floor
(~y=-64). The biome source can't be swapped (`IslandGenerator` themes off `level.getBiome()`;
`WorldSetupEvents.findLandCenter` spirals the biome map), so the fix lives in the generator. Mixin-free
(codec/registry registration via `ModChunkGenerators`).

**Features and structures are SEPARATE levers** (don't conflate them): the `applyBiomeDecoration` no-op kills
**features**; the `createStructures` no-op kills **structures** (no starts ⇒ nothing places, all three dims —
the End keeps decoration, so its structures are stopped purely by the `createStructures` no-op). This also makes
the vanilla "Generate Structures" toggle moot — no structures generate in the void dims regardless.

Net: immune to every biome/structure mod; biome source + theming + start search intact. Verified in-game with
BWG: no features at y≈-64, End central island present.

## Trial Chamber rooms — aesthetic polish pass ← OPEN (#24 / #25 / #33 / #61)

Vanilla trial-chamber interiors look far richer than ours; do a visual pass to match. **Mainly aesthetic** — the
gameplay already works, including the reward vaults: `room_treasure` is a twin-vault room, the hub has an ominous
vault, each mob room a regular vault (gametest `trial_hub_has_boss_and_ominous_vault`, both nodes).

**Current assets:** `worldgen/template_pool/trial_chamber/{start,rooms}.json`, `TrialChamberTemplates.java`, and 7
NBTs in `structure/trial_chamber/`: `hub`, `gallery`, `room_{breeze,skeleton,spider,zombie,treasure}`.

**Current baseline (better than "flat fills"):** `TrialChamberTemplates.mix()` already lays a deterministic
4-block tuff-bricks / polished-tuff / chiseled-tuff / cut-copper masonry blend, and hub/rooms/gallery hang
lanterns. **The actual gap vs vanilla:** stairs/slabs/walls shapes, **copper bulbs**, copper grate, mud bricks,
chains — and the greebling layer (cobwebs, candles, decorated pots, suspicious gravel).

**⚠️ NBT staging trap:** editing `.nbt` room files silently won't reach the test/run classpath on incremental builds
(stale Stonecutter node copy wins `processResources`). Run clean or hand-sync `versions/<v>/src` and verify byte size
after each edit — this cost a whole session once.

- [ ] **(#24)** palette + lighting pass on the 5 room NBTs + `hub`/`gallery` (the vanilla-richness layer above), then regenerate the 7 NBTs
- [ ] **(#25)** the atmosphere/greebling half: decorated pots, cobwebs, candles, chains, suspicious gravel *(the vault half is ✅ done — see baseline)*
- [ ] **(#33)** add a few more room/corridor variants for layout variety (currently exactly 5 rooms + 1 gallery)
- [ ] **(#61)** rebuild clean (NBT staging trap) and eyeball against a vanilla trial chamber — gated on the three above
