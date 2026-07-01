# Planned Notes

- [ ] Consider adding a compat which occasionally drops a waystone

## Void worldgen enforcement — custom ChunkGenerator

Grounds out the BWG/TerraBlender leak (and pre-empts it for every future biome/structure mod) + answers
"disable structures entirely". **Not yet implemented.**

**Problem (confirmed on a test world, BWG installed):** TerraBlender biome mods (BWG / BYG / Terralith / Incendium)
inject biomes into the overworld `multi_noise` source Skyseed uses; those biomes' *features* then decorate at the
void floor (~y=-64), creating stray "islands". Base terrain stays void (noise settings hold) and the Nether/End are
clean — it's only the **decoration** step leaking in.

**Why we can't just swap the biome source:** `IslandGenerator` themes islands off `level.getBiome()` (matching
`BiomeOverride`), and `WorldSetupEvents.findLandCenter` spirals the biome map for a land start — both need the
vanilla `multi_noise` overworld preset to stay. That preset is exactly what TerraBlender hooks, so the fix has to
live in the generator, not the biome source.

**Grounding facts (from source):**
- Void = `skyseed:void` noise settings (`final_density 0.0`, `default_block air`) + `multi_noise` overworld preset
  — `worldgen/world_preset/skyblock.json` + `worldgen/noise_settings/void.json`.
- Exactly ONE Skyseed biome modifier exists: `neoforge/biome_modifier/central_end_island.json` → adds the central
  island feature to `minecraft:the_end` (step `raw_generation`). **Overworld + Nether use NO decoration** (100%
  seed-driven via `IslandGenerator`), so stripping their decoration is safe; the End must keep it.
- No mixins in the mod — keep it that way (codec/registry approach).

**Solution:** one `SkyseedVoidChunkGenerator extends NoiseBasedChunkGenerator` with a `skipDecoration` flag,
referenced per-dimension in `skyblock.json`:

| Dim | `skipDecoration` | Behaviour |
|---|---|---|
| Overworld | true | no-op `applyBiomeDecoration` (kills biome-mod features) + no-op `createStructures` + no-op `applyCarvers` |
| Nether | true | same (clean today; future-proofs against a Nether biome mod) |
| End | false | keep decoration (central island survives) but still no-op `createStructures` |

Net: immune to every biome/structure mod; biome source + theming + start search intact; mixin-free.

**Features and structures are SEPARATE levers** (don't conflate them):
- `applyBiomeDecoration` no-op → kills **features** (the biome-mod rocks/plants at the void floor).
- `createStructures` no-op → kills **structures** (no starts ⇒ nothing places, even though MC places structure
  *pieces* during `applyBiomeDecoration`). Applied to **all three dims** — the End keeps decoration for its central
  island, so its structures are stopped *purely* by the `createStructures` no-op. Verified: villages/etc. that
  appear with "Generate Structures" ON are structures, not decoration → the `createStructures` no-op is what removes them.

**Also answers "disable structures entirely":** the vanilla "Generate Structures" checkbox can't be removed per
world-preset (it's global vanilla UI tied to `WorldOptions`), but `createStructures` being a no-op makes it moot —
no structures generate in the void dims regardless of the toggle. Better than relying on the player setting it.

**Checklist:**
- [x] `SkyseedVoidChunkGenerator` — overrides `createStructures` (always) + `applyBiomeDecoration` (gated by flag). Carvers left alone (a void has nothing to carve). **1.21.1 done & compiling.**
- [x] register its `MapCodec` via `ModChunkGenerators` (`Registries.CHUNK_GENERATOR`), wired in `Skyseed` constructor.
- [x] `world_preset/skyblock.json` → all three dims on `skyseed:void`; overworld + nether `skip_decoration: true`, End `false` (keeps central island).
- [x] verify TerraBlender only injects biomes (doesn't swap the generator) — confirmed: base terrain stays void, only decoration leaked.
- [x] regression check: both nodes compile green; **all 126 gametests pass** (End/dragon included).
- [x] **26.1.2:** done — `applyBiomeDecoration` is identical across nodes (unguarded); only `createStructures` (gained a 6th `ResourceKey<Level>` param) needs a `//?` guard. Both nodes green; gametests pass (1.21.1: 126, 26.1.2: 135).
- [x] **runtime smoke test** (user, 2026-07-01): new skyblock world + BWG → **confirmed no features at y≈-64 and the End central island still present**. (A pipeline gametest is awkward in the framework — it tests in-structure, not chunk-gen.)

## Trial Chamber rooms — aesthetic polish pass

Vanilla trial-chamber interiors look far richer than ours; do a visual pass to match. **Mainly aesthetic** — the
gameplay (per-mob trial spawners, treasure room) already works; the gap is detail/atmosphere density.

**Current assets:** `worldgen/template_pool/trial_chamber/{start,rooms}.json`, `TrialChamberTemplates.java`, and 7
NBTs in `structure/trial_chamber/`: `hub`, `gallery`, `room_{breeze,skeleton,spider,zombie,treasure}`.

**Adopt from vanilla:**
- Palette variety — tuff / tuff bricks / polished + chiseled tuff (+ stairs/slabs/walls), cut copper, copper grate,
  mud bricks — instead of flat single-block fills.
- Lighting — **copper bulbs** (warm glow) + chains/lanterns; lean on the trial-spawner glow.
- Atmosphere/greebling — cobwebs, chains, candles, decorated pots, suspicious gravel, copper-grate windows.
- Reward — confirm **vaults** (normal + ominous) are present; keep the trial spawner as each room's centerpiece.
- Layout variety — more room/corridor/intersection variants (vanilla has many; we have 5 rooms + 1 gallery).

**⚠️ NBT staging trap:** editing `.nbt` room files silently won't reach the test/run classpath on incremental builds
(stale Stonecutter node copy wins `processResources`). Run clean or hand-sync `versions/<v>/src` and verify byte size
after each edit — this cost a whole session once.

- [ ] palette + lighting pass on the 5 room NBTs + `hub`/`gallery`
- [ ] add vaults/decorated pots/atmosphere; confirm reward vault in `room_treasure`
- [ ] add a few more room/corridor variants for layout variety
- [ ] rebuild clean (NBT staging trap) and eyeball against a vanilla trial chamber
