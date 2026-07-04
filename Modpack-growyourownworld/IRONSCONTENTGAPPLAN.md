# IRONSCONTENTGAPPLAN — closing the exploration-mod content gaps

From the 2026-07-04 content audit: a normal overworld running Iron's Spells + Artifacts would expose **8 things a
Skyseed player currently can't reach** — **7 mobs** (2 bosses + 5 regular) and **1 worldgen block** (mithril ore, both
variants). Relics adds nothing missing (its "entities" are all spell/particle effects; its one block is a temporary
relic-spawn). This plan closes all 8, in priority order.

**Guiding rules (from the standing memories):**
- **Inert-safe.** Modded ids go in `ores` / mob `mobs` packs, which *skip* an unknown id — so every addition here is
  safe even if the mod is later removed. (theme-override-inert-safety)
- **Reuse the re-homed structures.** We already jigsaw-place the Iron's structures and hang guardian mob-packs on them;
  most gaps close by adding one entity to an existing pack — no new build.
- **Mirror across tiers.** Any theme change is applied to the base / `_large` / `huge_` variants together.
  (skyseed-mirror-island-tiers)
- **No NBT regen for datapack-only edits** (Phase 0 + 1 are pure JSON). Phase 2 touches a baked structure → regen dance.

---

> **Status (2026-07-04):** **ALL SHIPPED.** **Phase 0 ✅** (mithril in rocky ×3 + ancient ×3, deep_core). **Phase 1 ✅**
> (fire_boss→pyromancer tower; apothecarist→mangrove hut; priest+cursed_armor_stand×2+mimic→citadel). **Phase 2 ✅ — the
> dedicated grand Catacombs (option B)** is built + wired to `huge_ancient` (4.5%, alongside the Ancient City): restores
> the **Dead King** (baked dormant `dead_king_corpse`) + **catacombs_zombie** (dark spawners). Gametests green on both
> nodes (1.21.1: 175, 26.1.2: 177) — the crypt's baked modded entities skip inert-safely in the test env. Priest was
> moved off the village themes to the citadel after the hostility check (it's an `Enemy` that hunts villagers).
> **All 7 mobs + 1 block from the audit are now reachable.** New/changed: `CatacombsTemplates.java`,
> `template_pool/catacombs/crypt.json`, `structure/catacombs/crypt.nbt`, `DevStructureGenerator`, both gametest suites,
> `huge_ancient.json`.

## Phase 0 — Mithril ore *(trivial; do first)* ✅ SHIPPED

The only worldgen block the mod injects into a real overworld is mithril, via the `add_mithril_ore` biome-modifier
(`ore_mithril_placement`, count 7, Y −63…−38 = deepslate depth). That modifier never fires in our void chunk-gen, so
mithril is unobtainable — and it **gates the mid-tier spellbooks/armor**, so this is a progression hole, not just flavor.

Fix = add mithril to the deep cores of our stone islands' `ores` arrays. Two blocks, matched to core material:

| Block | Home themes (core) | `depth` |
|---|---|---|
| `irons_spellbooks:mithril_ore` (stone) | `rocky`, `rocky_large`, `huge_rocky` (stone core) | `deep_core` |
| `irons_spellbooks:deepslate_mithril_ore` | `ancient`, `ancient_large`, `huge_ancient` (deepslate core) + `huge_rocky`'s deepslate sub-core | `deep_core` |

**Tuning** (mid-tier metal — rarer than gold, commoner than diamond): `chance ~0.45`, `count {min:2,max:4}`,
`vein_size {min:2,max:3}`. Copy-paste entry:

```json
{ "block": "irons_spellbooks:deepslate_mithril_ore", "chance": 0.45, "count": { "min": 2, "max": 4 }, "vein_size": { "min": 2, "max": 3 }, "depth": "deep_core" }
```

- Datapack-only → `/reload`, no regen.
- **Optional quest hook:** one line in the Iron's chapter (or the IE/mining quest desc) telling players mithril lives
  deep in **Rocky / Ancient** islands — same treatment we gave bauxite. (QUESTPLAN follow-up.)

---

## Phase 1 — Easy mob adds *(one entity onto an existing guardian pack; no new structures)*

Every one of these already has a re-homed structure with a `mobs` pack we can extend. All inert-safe; spawn once at
germination (the citadel_keeper precedent). Mirror to each tier the structure appears on.

| Mob | Type | Home (existing structure → pack) | Change | Note |
|---|---|---|---|---|
| `irons_spellbooks:fire_boss` | **BOSS** | `huge_desert` → pyromancer_tower `{pyromancer, cultist}` | `+ fire_boss ×1` | It *is* the tower's intended boss (boss-bar + music). |
| `irons_spellbooks:apothecarist` | regular | mangrove_hut host (`lush_large`, +base if wired) → `{necromancer}` | `+ apothecarist ×1` | Potion-hut = perfect (its loot is potion ingredients). |
| `irons_spellbooks:priest` | regular | `huge_rocky` → citadel `{citadel_keeper}` | `+ priest ×1` | **Hostile** (see below) — a corrupt caster guarding the citadel, *not* a village resident. |
| `irons_spellbooks:cursed_armor_stand` | regular | `huge_rocky` → citadel `{citadel_keeper}` | `+ cursed_armor_stand ×2` | Animated-armor castle guardians. |
| `artifacts:mimic` | regular | `huge_rocky` → citadel vault pack | `+ mimic ×1` | Treasure guardian near the reward chest. |

**Hostility resolved (bytecode check 2026-07-04):**
- **`apothecarist`** — no `Enemy` marker, only `HurtByTarget` → *neutral*; safe in the mangrove potion hut. ✓
- **`priest`** — implements **`Enemy`** and carries 16 `Villager` refs → an **illager-style antagonist that hunts
  villagers**. It would butcher a peaceful trade-post's residents, so it is homed in the **citadel** (hostile guardian),
  *not* a village. (This is why priest moved off the village themes.)

**Still verify (playtest):**
- **Boss-at-germination** (`fire_boss`): confirm it idles + persists (doesn't despawn) until a player arrives — as
  `citadel_keeper` does. If it wanders/despawns, bake it as a trial-spawner instead.

---

## Phase 2 — Dead King + catacombs_zombie *(the last boss + its mob — needs a decision)*

Both live only in the mod's **Catacombs**, which we deliberately skipped (it digs straight down — unfit for an island).
The Dead King is doubly gated: its home is gone *and* the `dead_king_phylactery_shard` that summons it drops only from
catacombs spawners. Mechanic to exploit: **`dead_king_corpse`** is a *dormant statue* that awakens into the
`dead_king` fight on player approach — spawn-eggable, so a mob-pack can place it idle at germination; it activates when
the player arrives, and drops the Dead King loot on death. That makes a full vertical catacomb *optional*.

### ▸ Decision A — fold the Dead King into the **War Barrow** *(recommended; light)*
The War Barrow already is an undead barrow with a hollow crypt — a Dead King *rising from the barrow* is on-theme and
reuses existing work.
- Add `dead_king_corpse ×1` (boss) + `catacombs_zombie ×2` to the war_barrow pack → closes **both** remaining gaps.
- **Dependency (already-flagged):** MobPlanner spawns the pack at the structure **center**, which in the war_barrow is
  the **solid mound** → the boss/zombies spawn *buried*. Must spawn them in the open (hollow crypt or altar top). Fix
  via a structure open-center or a MobPlanner spawn-marker; this also resolves the earlier "War Barrow necromancer
  reliability" item in one go.
- Effort: edit `WarBarrowTemplates` (guarantee an open boss chamber) + the pack + the spawn-placement fix; regen the
  war_barrow `.nbt` (regen dance, 1.21.1 node).

### ▸ Decision B — a dedicated home-curated **Catacombs** *(optional stretch; grand)*
A bounded descending crypt (~19×19 × several floors) on `huge_ancient` (deepslate, deep — also where deepslate-mithril
lives), in the Citadel/Warren mould: catacombs_zombie spawners on the crypt floors, a dormant `dead_king_corpse` in the
bottom boss chamber, a necromancer, and the phylactery loot chain. New `CatacombsTemplates.java` + template pool +
gametest. Natural second home for `mimic` + `cursed_armor_stand`.

**Recommendation:** ship **A** now — it closes the last two gaps *and* fixes a known War Barrow bug for little cost.
Keep **B** on the shelf as a future grand dungeon if we later want a catacomb distinct from the barrow.

> **Owner decision (2026-07-04): build B — the dedicated grand Catacombs** (wants more structure variety; aware it's the
> bigger ask). Design law reaffirmed: **prevent squares, inside and out** (organic footprint + irregular interior, like
> the War Barrow / Frozen Warren / Citadel rebuilds). The War Barrow spawn-center bug is *not* pulled along; instead the
> new Catacombs must spawn its pack in an open chamber (learn from that bug), and `dead_king_corpse` sits dormant in the
> boss room until a player descends.

---

## Effort summary

| # | Item | Type | Effort | Closes |
|---|---|---|---|---|
| 0 | Mithril ore (2 blocks → rocky + ancient tiers) | datapack JSON | **XS** | 1 block |
| 1 | fire_boss · apothecarist · priest · cursed_armor_stand · mimic → existing packs | datapack JSON | **S** | 1 boss + 4 mobs |
| 2A | Dead King (+catacombs_zombie) folded into War Barrow | structure edit + regen + MobPlanner spawn fix | **M** | 1 boss + 1 mob |
| 2B | *(optional)* dedicated Catacombs grand dungeon | new structure (Templates + pool + gametest) | **L** | — (alt home) |

Phases 0 + 1 (**XS + S**, all datapack, no regen) close **1 block + 1 boss + 4 mobs** in one sitting. Phase 2A closes
the final **boss + mob** and retires the War Barrow spawn bug. Nothing is left uncovered.

## Verification checklist (all phases)
- [ ] apothecarist / priest hostility (neutral before peaceful placement)
- [ ] fire_boss / dead_king_corpse idle + persist at germination (citadel_keeper precedent)
- [ ] every theme edit mirrored across base / `_large` / `huge_` tiers
- [ ] Phase 0/1 in-game `/reload` smoke test; Phase 2A regen on the 1.21.1 node
- [ ] optional quest line: where to find mithril + the bosses
