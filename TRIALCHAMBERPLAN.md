# TRIALCHAMBERPLAN — capturing the vanilla trial-chamber *feel*

Owner of the trial-chamber "feel" rework (plannednotes **#61/#33**). The structure (a corridor warren) shipped
v0.194.0; this plan is about the **surface treatment** that makes it *read* as a vanilla trial chamber rather than
"rooms that happen to use the trial-chamber blocks." Grounded in a 2026-07-02 side-by-side (user screenshots of the
vanilla main-menu chamber vs. our atrium).

## The problem (why ours doesn't land)

Our walls/floors are filled by **`TrialChamberTemplates.mix(a,b)`** — a per-block hash `(a*7 + b*5) mod 12` over 8
blocks. That produces a **random speckle**: every block is an independent coin-flip between tuff / polished tuff /
chiseled tuff / oxidized copper / weathered copper / cut copper / grate / chiseled copper. It reads as **noise**.

Vanilla is the exact opposite — a **deliberate, tiled mosaic**:

- **Tuff-brick FRAMES** grid each wall into rectangular **panels** (the brown "grout" lattice).
- Each **panel is a patch of ONE copper tone** (weathered / oxidized / exposed) — the patina varies by *region*, not
  per block, so it reads as aged metal cladding, not static.
- **Panel centres carry MOTIFS**, placed at regular intervals: **chiseled copper** (the cross/X), **chiseled tuff**
  (the spiral/labyrinth), and **copper-grate** lattice "windows".
- A **plinth/base course**, a **cornice** at the top (we already have this), **recessed copper bulbs** in frames, and
  a **laid, tiled floor** (not speckle).
- Net: an aged decorated **temple**. The feeling is *framing + motifs + regional patina + symmetry*, NOT size.

**User notes (2026-07-02):** smaller is fine; **don't add free-standing pillars at this scale** (they're too much) —
the wall framing + corner posts should carry the structure instead.

## The fix — a wall/floor *texturer* replacing the random `mix()`

Add position-aware helpers and call them from every piece's fill loop (all pieces share them, so the whole warren is
coherent):

- **`wall(u, v)`** — decorated wall tile at in-wall coords (`u` = horizontal along the wall, `v` = height). A 4-block
  module: frame lines (`u%4==0 || v%4==0`) → **tuff bricks** (grout); each 3×3 panel → a patch of **one** copper tone
  chosen by panel index (oxidized / weathered / cut copper / tuff), with the panel **centre** a **motif** cycled by
  panel index (chiseled copper → chiseled tuff → grate). `v = y`, so belt-course frames land at y4/y8 and motifs at
  ~eye level (y2/y6).
- **`wallU(x, z, maxX, maxZ)`** — the along-wall coordinate (`±Z` walls run in x, `±X` walls run in z).
- **Corner posts** — the four vertical corner columns → a solid **cut-copper** post (definition without free-standing
  pillars).
- **`floorTile(x, z)`** — a laid floor: tuff-brick / polished-tuff checker with a **chiseled-copper stud** at grid
  nodes (`x%4==0 && z%4==0`). Reused for the ceiling.
- **Keep:** the cornice, copper-bulb chandeliers on chains, greebling, spawner/vault daises, doorways.

### Call-site change
Each piece loop currently does `mix(x, y+z)` (walls) / `mix(x, z)` (floor+ceiling). Replace with
`wall(wallU(...), y)` / `floorTile(x, z)`. Pieces: `hub`, `room`, `roomSmall`, `end`, `corridor`, `corner`,
`junction`, `descent` (descent keeps its stepped-floor substructure on a plain fill).

## Phasing (aesthetic-blind → iterate in-game)

1. **Atrium showcase** — apply the texturer to `hub` only, regen just the hub, and get the in-game look (the atrium is
   what the user judges). Cheapest way to validate the *direction* before touching every piece. **✅ v0.196.0** (texturer)
   **+ v0.198.0** (shape/level pass, from the in-game read): rectangular **11×13**, a **raised back dais** reached by a
   **stair step** (two floor levels — spawner on the lower arena, vault up on the dais), one **elevated passage exit**
   off the dais, **non-weathered (warm) copper** added to the wall palette, and a **mid-wall ridge** (cut-copper belt)
   breaking up the tall walls. Per the user: no free-standing pillars — decoration + shape + level changes carry it.
2. **Roll out** — apply to all remaining pieces for a coherent warren; regen all; both-node gametests. **✅ v0.203.0**:
   the framed-panel MOSAIC + laid-floor tiler + cut-copper corner posts now cover *every* piece (rooms, small cell,
   corridor, corner, junction, descent, end room). The small cell also picked up the cornice + greebling; the descent's
   treads became laid floor over a plain substructure. All 11 non-hub `.nbt` regenerated; a gametest asserts the end room
   carries the mosaic; both nodes green (154 / 163). **Learnings from the atrium/descent passes carried through the
   rollout (v0.196–v0.201):**
   - Wall MOSAIC (framed panels + motifs + oxidized *and* non-weathered copper) + the `floorTile` on every piece.
   - Level changes read best as a **2-block** raise (a 2-step stair), with a mid-wall **ridge** doubling as the doorway
     lintel; belt **corners use a top slab** (stairs can't hook cleanly at an inner corner).
   - Passages must be **roomy, not cramped** (≥5 tall interior on stairs).
   - **Branch-ends should land in a trial room, never a dead end** — the descent's exit draws the *rooms* pool. Consider
     the same for other terminal passages so "the last room is always a trial room" (user request).
   - No free-standing pillars at this scale — decoration + shape + level changes carry it.
3. **Tune** — module size (4?), motif frequency, patina mix, plinth, grate backing (a grate on an outer wall shows the
   island body behind — acceptable as a vent, or back it with a solid block / use sparingly). All in-game.

## Regen + verify
The wall/floor texture changes every piece, so **delete all `trial_chamber/*.nbt` and run the 2-build regen dance**
([[skyseed-structure-staging]]); regen on 1.21.1 only, validate 26.1.2 via DFU. A gametest can assert the atrium wall
contains the motif blocks (chiseled copper/tuff), but the *feel* is an in-game sign-off.

## Risks
- **Aesthetic-blind** — the exact module/motif tuning needs the user's eye each phase (why it's phased).
- **Small rooms** (5×5 cell, 3-wide corridors) have little wall area for a 4-module pattern — the framing may look
  cramped; may need a smaller module or plainer treatment for passages.
- **Regen blast radius** — every piece's `.nbt` changes; honour the staging trap.
