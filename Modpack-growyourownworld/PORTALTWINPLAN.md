# PORTALTWINPLAN — Aligned Ruined-Portal twins via vanilla linking (option C)

> **Separate PR.** Independent of the shipped Explore/loot/structure work. Goal: when you repair + light one side of a
> Ruined-Portal island pair, you emerge **in the twin frame**, not a couple blocks beside it facing the wrong way.
> Grounded in the actual code: [`TwinPlacer`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/TwinPlacer.java),
> [`RuinedPortalTemplates`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/structure/RuinedPortalTemplates.java),
> [`IslandSeedEntity.germinate`](../src/main/java/dev/gemberkoekje/skyseed/entity/IslandSeedEntity.java),
> [`GenerationJob`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/GenerationJob.java).

## Implemented — option B (matched rotation), not C

Shipped as **fallback (B)**, because a look at the actual seating showed C's heavy machinery is unnecessary here. The
`ruined_portal/portal` jigsaw is a single rigid piece whose `minecraft:bottom` anchor vanilla always seats at the piece
`origin` in XZ (`JigsawPlacement.addPieces`: `adjustedPosition = origin - rotate(anchor)`, so the anchor lands on `origin`
for *any* rotation), and `planStructure` sets that `origin.xz` to the **island centre**. The portal opening sits one
column above the anchor, so its XZ is *already* the island centre — the existing centre-based `TwinPlacer.linkedPortalPos`
is therefore block-exact in XZ (and within vanilla's own ×8 rounding on the Nether→OW return). So issues #1 (offset) and
#3 (extra forged portal) largely dissolve once the frames are centred; **only the independent random rotations (#2) had to
be fixed.**

What changed (no re-sequencing, no `PortalForcer`, no post-assembly callback, no `.nbt` rebuild):
- [`IslandPlan.JigsawSite`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandPlan.java) gained an
  `Optional<Rotation> rotation` (+ `withRotation`); empty everywhere but the portal keeps vanilla's random rotation.
- [`IslandGenerator.planIsland`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/IslandGenerator.java) forces a **fixed
  rotation** (`Rotation.NONE`) on the jigsaw of any twin-carrying island. Both the source and its twin are twin-flagged, so
  both pick up the identical rotation → the two frames share an axis. A fixed rotation stays matched even if a twin is
  nudged off the link spot.
- [`Jigsaw.placeSinglePiece`](../src/main/java/dev/gemberkoekje/skyseed/compat/Jigsaw.java) places the single fixed frame
  at an explicit rotation (vanilla's `addPieces` only rolls a random one), reproducing vanilla's start-piece seating.
  [`GenerationJob`](../src/main/java/dev/gemberkoekje/skyseed/worldgen/GenerationJob.java) routes the portal through it.
- Gametests (`ruinedPortalFrameSeatsOnCentre` on both nodes) stamp the frame at all four rotations and assert the opening
  seats over `origin` and stays a repairable ruin. The cross-dimension traversal remains a manual in-game verify.

The rest of this doc is the original option-C design, kept for context / if a future change needs a portal that *isn't* at
the island centre.

## The problem (why it doesn't line up today)

A Ruined-Portal island grows a **twin** at the vanilla 8:1 coordinate in the paired dimension so the two frames link
"for free" once repaired. Three things break the alignment:

1. **Twin placed by island CENTER, not the portal.** `TwinPlacer.spawnTwin` → `linkedPortalPos(islandCenter)` =
   `floorDiv(center, 8)` (OW→Nether) / `center*8` (Nether→OW). But vanilla links by the **lit portal block**, and the
   ruined frame sits ~1–2 blocks off the island centre inside the structure — an offset that **doesn't scale** across
   the divide, so the twin frame lands ≈ `offset·⁷⁄₈` from where vanilla expects.
2. **Independent random rotations.** The `ruined_portal/portal` jigsaw start piece is rotated by a position-seeded RNG,
   so the two sides get **different rotations** — the frames face different directions.
3. **Both frames spawn ruined (unlit).** Lighting one side makes vanilla **forge a fresh portal at the exact link
   coordinate** (an obsidian platform in the void) rather than reuse the ruined twin — so you arrive *beside* the twin.

## Goal

The two ruined frames sit at coordinates + orientation that **mutually link**, so after the player repairs and lights
both, travelling either way lands them in the paired frame with no drift and no vanilla-forged extra portal.

## Approach C — use the ACTUAL portal block + vanilla's linking

Stop deriving the twin from the island centre. Instead: know the **source portal block** (post-assembly), let vanilla
compute/validate the linked spot (it handles the Nether ceiling/lava-sea clamp and the void platform), and grow the
twin so its frame lands there with a **matched rotation**. Keep both frames ruined.

### Phases

1. **Record the source portal anchor.** When the `ruined_portal/portal` piece is stamped (in `Jigsaw.placeCapped` /
   `GenerationJob`), record the portal **inner block world-pos + axis + rotation** — e.g. into `IslandPlan` /
   `SkyseedWorldData`. (The frame shape is fixed in `RuinedPortalTemplates`, so the inner + axis are derivable from the
   piece origin + rotation; recording it at stamp time is cleaner than re-scanning the world.)
2. **Move twin placement to POST-ASSEMBLY.** Today `IslandSeedEntity.germinate` calls `TwinPlacer.spawnTwin` at
   germination — *before* the jigsaw stamps, so the portal position is unknown. Fire the twin step from a
   **`GenerationJob` completion callback** instead, keyed off the recorded portal anchor (Phase 1), not the centre.
3. **Vanilla-linked destination.** Compute the destination from the **portal block** (not centre) via
   `ServerLevel.getPortalForcer()` — `PortalForcer.findClosestPortalPosition` / `createPortal(BlockPos, Axis)` — reusing
   the 8:1 math in `linkedPortalPos` but fed the portal block, and letting the forcer clamp to a valid Y and find a
   valid spot. (In the void, `createPortal` builds an obsidian platform; see the open question below on island-vs-platform order.)
4. **Grow the twin island + frame at the linked spot**, with the frame's inner at the destination portal block and the
   **twin's rotation forced to match the source's** (see Rotation below). Both frames stay the **ruined** variant
   (`portal_nether` in the Nether, `portal` in the OW) — the player repairs them.
5. **(Optional) Activate-and-verify self-check.** Briefly complete + light the source, call the exit-portal lookup to
   confirm it resolves to the twin, then extinguish + re-ruin. A belt-and-braces guarantee; skip if Phases 1–4 are exact.

### Rotation matching

`Jigsaw.placeCapped` currently seeds the start-piece rotation from `(featureSeed, origin)`. For the twin to face the
source, either (a) record the source rotation (Phase 1) and pass it as an **explicit rotation** to the twin's
`placeCapped` (needs a new overload / `Rotation` param), or (b) place the ruined frame **without** the jigsaw (a direct
`StructureTemplate` place at the recorded rotation), since it's a single fixed piece anyway.

## Key APIs / touch points

- `ServerLevel.getPortalForcer()` → `PortalForcer` (**version-volatile** — the class + method names moved across 1.21.x
  → 26.1.x; isolate behind a `compat/` helper like `Jigsaw`/`Lookup`).
- `PortalShape` (frame detection/validation) if going the activate-and-verify route.
- `TwinPlacer.linkedPortalPos` — adapt to take the **portal block**; keep the Y-clamp.
- The `ruined_portal` jigsaw stamp site (record the anchor); `Jigsaw.placeCapped` (explicit-rotation overload).
- `GenerationJob` — add a **completion callback** so the twin fires after the source island lands.
- `SkyseedWorldData` — persist the pending twin (portal anchor + rotation) so a mid-grow crash still places it
  (ties into the existing 5.2 persist/resume follow-up).

## Risks / open questions

- **Sequencing is the big change** — the twin moves from germination to a post-assembly callback. Make it deterministic
  (same seed → same twin) and crash-safe (persist the pending twin).
- **Island vs. platform order in the void.** Decide: grow the twin island first then place the ruined frame at the exact
  linked block, OR let `PortalForcer` drop its platform and wrap the island around it. The former keeps full control;
  the latter reuses vanilla's valid-spot logic. Leaning **island-first + frame at the computed block** (skip actually
  creating a lit vanilla portal — we only need its *coordinate*, which is `portalBlock` scaled 8:1 + Y-clamp).
- **Rotation overload** to `Jigsaw.placeCapped`, or bypass the jigsaw for this one fixed piece.
- **Multi-version PortalForcer** rename — one compat helper.
- **CI can't test the cross-dim link** (in-game only, like the current twin). Gametests can cover the **coordinate math**
  (portal-block → linked block) and **frame placement + rotation match**; the actual traversal is a manual verify.

## Fallback ladder (if C proves too gnarly mid-PR)

- **(B) Center the portal block on the island + force matched rotation.** Rebuild `RuinedPortalTemplates` so the lit
  inner block sits at the piece/island centre, and fix the rotation → the *existing* centre-based math becomes exact
  (centre == portal), no re-sequencing. ~90 % of the benefit for a fraction of the code.
- **(A) Center only.** Positional line-up; frames may still face differently. Smallest change.

## Scope / non-goals

- Separate PR; does not touch the Explore seed / loot / structure work.
- Overworld ↔ Nether only (the twin already restricts to those). The End is out.
- The reward asymmetry stays (goodies on the OW frame, bare frame in the Nether — `RuinedPortalTemplates.portalNether`).
