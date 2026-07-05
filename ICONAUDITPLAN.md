# ICONAUDITPLAN — item-icon audit + the readability/consistency fixes it surfaced

> **Status: AUDIT COMPLETE 2026-07-05; BANDS A + B + C ALL BUILT 2026-07-05.** All 93 item textures under
> `src/main/resources/assets/skyseed/textures/item/` were reviewed at ×7.5 on grouped contact sheets plus a ×9.4
> "confusables" sheet with true-16px swatches. Every icon is a clean 16×16 with distinct pixels (no exact-duplicate
> art). The set rests on a strong three-part visual language; the defects were almost all *local deviations* from it.
> **All three bands are now implemented in the working tree** — 27 textures redrawn/tweaked + 1 new texture + 1 model
> repoint; validated: 93 model layer refs all resolve, every texture still 16×16. §5 Q1/Q2 signed off 2026-07-05.
> Left: an in-game hotbar eyeball and the user's git integration. Child-of-nothing — pure art/asset work, no worldgen or
> recipe changes. Tier art added here repoints its model and honours [[skyseed-mirror-island-tiers]] (base/large/huge
> stay a matched family).

## 0. Goal, in one paragraph

The icon set is large (93 textures) and mostly excellent, but a handful of icons either **can't be told apart at 16px**
(the four farm seeds), **break the family's own tier/theme convention** (nether_lava's large tier, the bastion/monument
seeds), or **have no dedicated art** (forest's large tier reuses the generic disc). This plan fixes the clear-cut
readability/consistency bugs first, flags the cross-family style questions that need a design call before redrawing, and
explicitly lists what to leave untouched so a future pass doesn't "fix" the parts that already work.

## 1. The visual system today (context — this is the thing to preserve)

Three conventions carry the whole set. They are good; keep them.

- **Skyseeds encode two axes at once.** *Silhouette = size tier:* round **ball** (base) → oval **disc** (large) →
  downward **cone** (huge). *Colour/texture = biome/theme.* So "large frozen" reads as an icy dome at a glance. This is
  the backbone — every fix below is judged against it.
- **Relics are one colour-coded gem.** A single diamond shape, one hue per structure (fortress=red, jungle=green,
  monument=teal, mansion=brown, outpost=grey, trial=orange, bastion=gold, desert=tan). Logical, tight, done.
- **Landmark seeds draw the actual structure** on a small island (end_city towers, trial_chamber vault, outpost tower,
  aquarium tank). This is the pattern the off-system structure seeds should migrate toward.

## 2. Audit method (reproducible)

- Tooling: **Python + Pillow** (installed 2026-07-05). Scripts live in the session scratchpad; the repo precedent for
  drawing icons is **PowerShell + `System.Drawing`** (WILDSEEDPLAN D7) — either is fine for the redraws.
- Built grouped ×7.5 contact sheets (overworld / nether / end+special / structures / relics+edges) and a ×9.4
  confusables sheet that also stamps the **true 16×16** swatch in each cell, to judge in-slot legibility.
- Verified no exact-duplicate pixel art (md5 over `img.tobytes()`), confirmed all 93 are 16×16, and cross-referenced
  every icon against its `lang/en_us.json` display name + its model's `layer0` to catch reused textures.

## 3. Findings scorecard

| Family | Individually | Consistent | Clear at a glance | Verdict |
|---|---|---|---|---|
| Relics (8) | Great | Excellent | Good | **Keep** |
| Overworld base balls | Good | Good | Good | Keep (2 tweaks) |
| Large discs | Good | Good | Good | Keep (1 gap: forest) |
| Huge cones | Good | Excellent | Good | **Keep** |
| Meteorite ball/disc/cone | Good | Excellent | Good | **Keep — reference set** |
| End / landmark scenes | Good | Good | Very good | Keep |
| Nether seeds | Mixed | Weak | Weak | **Change (A2, A3)** |
| Farm seeds (4) | Weak | *Too* uniform | **Poor** | **Change (A1 — top)** |
| Structure seeds | OK | Weak (mixed views) | OK | Change (B2) |
| Explore / Wild | OK | Off-system | OK | Change (B3) |
| Edges (4) | OK | Good | Weak (function) | Change (C4) |
| guide, portal_frame_shard, ladder | Good | n/a | Good | Keep |

## 4. Prioritized fix list

### Band A — clear-cut bugs (draw these; low risk, high value) — ✅ BUILT 2026-07-05

> All four redrawn from the existing templates (shared farm disc, base-ball masks, lava palette) so they stay in-family.
> Verified on regenerated ×8.75 + true-16px sheets: the four farms are now instantly separable; nether_lava base/large
> read as one theme; the four red nether spheres (mushroom/forest/rocky/fortress) are mutually distinct; forest_large no
> longer shares art with the generic disc. In-game hotbar check still pending.

**A1 — Farm seeds are not distinguishable at 16px. (highest priority)** ✅
Done: stable = gold hay bale; pasture = white cow + black patches; poultry = white bird + red comb + orange beak;
wool_farm = 5-colour rainbow wool bar. Shared brown disc kept identical across all four.
`island_seed_stable`, `island_seed_pasture`, `island_seed_poultry`, `island_seed_wool_farm` are four near-identical
brown discs with a green rim; the only difference is 1–3 pixels on top. In a real hotbar they read as the same item.
- **How:** give each a bold ≥4px signature that survives 16px, ideally recolouring the surface not just adding a dot —
  e.g. **stable** = hay-gold roof/fence; **pasture** = black-and-white cow patch filling the top; **poultry** = clear
  white bird + yellow; **wool_farm** = a wide multi-colour wool bar. Keep the disc silhouette (they are "large" tier).
- **Files:** the four `textures/item/island_seed_{stable,pasture,poultry,wool_farm}.png`. Models already point at these
  names — no model change.
- **Accept:** in a 2×2 true-16px comparison a first-time player names each correctly.

**A2 — `nether_lava_large` loses the lava read.** ✅ Done: recoloured to a glowing-orange lava top fading to dark
basalt underside, matching the base ball's ramp.
Base `island_seed_nether_lava` is glowing orange (excellent); `island_seed_nether_lava_large` was a dull rust-brown blob
with one orange fleck — the tier looked like a different theme.
- **How:** recolour the large disc to keep the glowing-orange crust (bright top surface + darker basalt underside),
  matching the base's palette. Nether tops out at large (no huge tier) — only these two must agree.
- **Files:** `textures/item/island_seed_nether_lava_large.png`.
- **Accept:** base and large sit side by side and read as the same theme at two sizes.

**A3 — `nether_forest` vs `nether_rocky` are twin red spheres.** ✅ Done: forest got warped-teal specks (base +
`_large`); rocky was darkened toward brick-red with white quartz specks + dark pits (base + `_large`). The four red
spheres are now mutually distinct.
Both were plain red balls; they also crowd `nether_fortress` (maroon) and overworld `mushroom` (red+spots). Four red
balls competed.
- **How:** push each to its signature — **nether_forest** = crimson red + a warped-teal fleck; **nether_rocky** =
  grey/dark speckled netherrack (pull it off pure red); leave fortress dark-brick maroon and mushroom white-spotted.
  Mirror any hue change onto each one's `_large` disc ([[skyseed-mirror-island-tiers]]).
- **Files:** `island_seed_nether_forest.png` (+`_large`), `island_seed_nether_rocky.png` (+`_large`).
- **Accept:** the four red seeds are individually nameable at 16px.

**A4 — `forest` has no dedicated large art.** ✅ Done: drew `island_seed_forest_large.png` (forest-green dome, snow
specks removed, small tree crown on top to match the forest family) and repointed the model.
`forest_large_skyseed.json` reused the **generic** `skyseed:item/island_seed_large` — the only overworld biome whose
large tier wasn't bespoke (confirmed: every sibling has its own `_large` texture; `island_seed_large` is otherwise unused
by a shipping item, and generic `island_seed` "?" is debug-only via `debug_streets_skyseed`).
- **How:** draw `island_seed_forest_large.png` (grass-top disc + dirt underside, small log/tree accent to match the base
  forest ball), then repoint `forest_large_skyseed.json` `layer0` → `skyseed:item/island_seed_forest_large`.
- **Files:** new `textures/item/island_seed_forest_large.png`; edit `models/item/forest_large_skyseed.json`.
- **Accept:** Large Forest no longer shares art with any other item; base/large/huge forest read as one family.

### Band B — consistency (per §5 Q1/Q2) — ✅ BUILT 2026-07-05

**B1 + B2 — Structure seeds unified on "island + structure silhouette".** ✅ Redrew the four outliers as an island
disc + a legible structure on top, matching the trade_post/outpost/aquarium reference: **hamlet** = red-roof cottage on
a grass island; **woodland_mansion** = dark-oak manor with lit windows on a grass island; **bastion** = crenellated
blackstone rampart with gold on a basalt island; **ocean_monument** = prismarine ziggurat with a sea-lantern glow on a
watery island. The former plain gold/teal spheres are gone.
- **Files:** `island_seed_{hamlet,woodland_mansion,bastion,ocean_monument}.png`.

**B3 — Explore & Wild conformed to shape-tier (ball → disc → cone).** ✅ **Wild** keeps its 4-biome patchwork + sprout
identity, now with a proper disc (`wild_large`) and cone (`huge_wild`) silhouette instead of "same ball + colour ring".
**Explore** keeps its iconic base eye (already circular = ball tier) unchanged; `explore_large` = teal disc and
`huge_explore` = teal cone, each carrying a concentric eye/target motif so the family reads as one, tier by silhouette.
- **Files:** `island_seed_{wild_large,huge_wild,explore_large,huge_explore}.png` (`wild`, `explore` bases unchanged).

### Band C — polish — ✅ BUILT 2026-07-05

- **C1 — lush vs meadow split.** ✅ lush deepened to a richer blue-green with azalea-pink accents; meadow brightened to
  a yellower green keeping its multi-colour flowers. Mirrored to `_large` + huge (green darken/brighten transform +
  accent flecks). Files: `island_seed_{lush,meadow}` (+ `_large`/huge each).
- **C2 — ancient contrast.** ✅ greys lifted, amethyst brightened, cyan sculk glints added so it survives a dim slot.
  Files: `island_seed_ancient` (+ `ancient_large`/`huge_ancient`).
- **C3 — Edges → portal-frame motif.** ✅ all four restyled as an end-portal-frame block (sage endstone + green
  ender-eye) with a per-variant gem (camp=teal, grand=purple, nether=orange, temple=gold) — ties look to the
  "End-Portal Edge" function while staying mutually distinct. Files: the four `*_edge.png`.

## 5. Decisions (signed off 2026-07-05)

- **Q1 — Structure-seed convention: UNIFY ALL on "island disc/cone + small structure silhouette".** Migrate the outliers
  (hamlet + woodland_mansion elevation buildings; bastion + ocean_monument plain spheres) into the trade_post/outpost/
  aquarium language. Governs B1/B2.
- **Q2 — Tier encoding for Explore/Wild: CONFORM TO SHAPE-TIER** (ball → disc → cone) like the biomes and meteorite.
  Silhouette carries the tier; each theme keeps a legible surface identity (wild = biome patchwork + sprout; explore =
  eye/lens motif on the island). Governs B3.
- **Q3 — Batch scope: Band A shipped first (done 2026-07-05); Band B + C implemented in the same session after Q1/Q2.**

## 6. Do NOT touch (already working)

The **8 relics**, all **huge cones**, the **meteorite ball/disc/cone** set (use it as the reference for correct
tiering), and the readable landmark/utility icons: **chorus_forest, end_city, end_portal, dragon_trophy, trial_chamber,
outpost, aquarium, trade_post, guide, ladder (+large), portal_frame_shard, ruined_portal, return_portal**. Leave the
debug-only generic `island_seed.png` "?" and `island_seed_large.png` alone (A4 stops the latter being shipped-visible).

## 7. Verification

- After each redraw, regenerate the ×9.4 + true-16px confusables sheet and eyeball the affected cluster.
- No functional/gametest impact expected (textures + one model `layer0` string in A4). Sanity-run the client asset load;
  confirm no missing-texture (magenta/black) fallback for `forest_large_skyseed`.
- Once built, retire this plan into the changelog + a `PLANOFPLANS.md` backlog pointer per repo convention.
