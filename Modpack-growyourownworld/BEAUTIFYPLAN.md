# BEAUTIFYPLAN — Skyseed: Grow your own world

Beautification pass for the modpack (NeoForge **1.21.1**, Embeddium-based).
Locked decisions: **Complementary Reimagined** default shader · **16× / native-res** art baseline (Vanilla Tweaks) · all four extras.

> **Status (PR #14):** **Better Clouds** (volumetric clouds) added. Beautify wins from the BWG work also landed — denser
> forests on large/huge islands, rounded deep lakes, and contained (walled, bank-softened) rivers.

> **Plan audit (2026-07-01):** 9 items below marked ✅ already-shipped after a plan-vs-repo check (all present in `mods.txt`).
> See [`../PLANOFPLANS.md`](../PLANOFPLANS.md) for the full prioritized backlog of remaining work.

---

## 1. Shaders — Complementary Reimagined + Euphoria Patches

Embeddium is **not** directly Iris-compatible on NeoForge. Verified working path for 1.21.1:

| Component | Location | Notes |
|---|---|---|
| **Iris** (NeoForge build) | `overrides/mods/` | Shader loader. https://modrinth.com/mod/iris |
| **Monocle** `0.2.3+` | `overrides/mods/` | Bridges Iris → our existing **Embeddium** renderer. Requires Iris + Embeddium. https://modrinth.com/mod/monocle-iris |
| **Euphoria Patches** (NeoForge 1.21.1) | `overrides/mods/` | Mod jar that patches the Complementary zip in `shaderpacks/`. https://modrinth.com/mod/euphoria-patches |
| **Complementary Reimagined** `.zip` | `overrides/shaderpacks/` | Base shaderpack (vanilla-faithful). https://modrinth.com/shader/complementary-reimagined |

> Oculus is dead for 1.21.1 — do **not** use it. Keep Embeddium; do not add Sodium.

## 2. Vanilla resource pack — Vanilla Tweaks (16×)

- **Vanilla Tweaks** — generate a custom 16× pack at https://vanillatweaks.net/picks/resource-packs/
  (pick the tweaks you want: borderless glass, alt ores, animated items, etc.). Keeps native
  resolution, so the world reads as vanilla.
- It's **site-generated, not on CurseForge** → can't be CF-linked. Drop the `.zip` in
  `overrides/resourcepacks/` (redistribution OK in modpacks with credit).
- Faithful 32× is **not used** (we moved off the higher-res look). Native vanilla with no base
  pack is also a fine option if you'd rather Vanilla Tweaks stay purely additive.

## 3. Skyseed resource pack — dropped for now

At 16× the mod already ships matching icons, so there's no cohesion work to do, and the standalone
`Resourcepack-skyseed/` project has been **removed**. If branding (custom `pack.png`, themed splashes,
a main-menu panorama) is wanted later, re-scaffold a standalone pack + a small `build.ps1` (zip with
`pack.mcmeta` at the root, forward-slash paths) and publish it as its own CurseForge resource-pack
project then. (The removed folder was never committed, so it's gone — but it's quick to rebuild.)

## 4. Extras (all selected)

| Mod / pack | Location | Dependency notes |
|---|---|---|
| **Fresh Animations** (resource pack) | `overrides/resourcepacks/` | needs the two mods below |
| **Entity Model Features (EMF)** | `overrides/mods/` | https://modrinth.com/mod/entity-model-features |
| **Entity Texture Features (ETF)** | `overrides/mods/` | https://modrinth.com/mod/entitytexturefeatures |
| **AmbientSounds 6** | `overrides/mods/` | **requires CreativeCore** (add it too) · https://modrinth.com/mod/ambientsounds |
| **Falling Leaves** | `overrides/mods/` | pairs with existing FastLeafDecay · https://modrinth.com/mod/fallingleaves |
| **Fusion (Connected Textures)** | `overrides/mods/` | paired with **Midnighttigger's Default CT** pack (glass/panes/sandstone/bookshelves) · https://modrinth.com/resourcepack/mt-ct-d |

Optional / later (caveats): **Distant Horizons** (LOD; shader compat on 1.21.1 still finicky — add after shaders are stable), ✅ ~~**Sound Physics Remastered** (reverb; heavier)~~ — **shipped** (`sound-physics-remastered-neoforge-1.21.1-1.5.1.jar` + config dir present).

## 5. Wiring — done

- ✅ `mods.txt` regenerated from the overrides by `gen-mods-txt.ps1` (mods + shaderpacks + resourcepacks).
- ✅ Resource packs enabled by default in `overrides/options.txt`:
  `resourcePacks:["vanilla","file/Midnighttiggers-FCT-Default_1.20_V8.zip","file/FreshAnimations_v1.10.4.zip"]`
  (Vanilla Tweaks was dropped.)
- ✅ **Shaders ON by default** via `overrides/config/iris.properties` (`enableShaders=true`, Euphoria-patched
  Complementary selected). ⚠️ `shaderPack` pins the patched folder's versioned name — refresh it (and mods.txt)
  when Complementary / Euphoria Patches update.
- ✅ **Fusion** paired with **Midnighttigger's Default CT** pack so connected textures actually render.
- Downloaded/generated `.zip` packs + shader folders are gitignored; `mods.txt` + configs are committed.

## Build order

1. **Shaders** — Iris + Monocle + Euphoria Patches jars + Complementary Reimagined zip → launch with shaders on.
2. **Vanilla Tweaks** (16×) generated + wired + enabled.
3. **Extras** — Fresh Animations (+EMF/ETF), AmbientSounds (+CreativeCore), Falling Leaves, Fusion.

## Download checklist (I can't pull these through CurseForge/Cloudflare — grab the latest NeoForge 1.21.1 build of each)

- [x] Iris (NeoForge 1.21.1) ✅ shipped (`iris-neoforge-1.8.12+mc1.21.1.jar`)
- [x] Monocle 0.2.3+ ✅ shipped (`monocle-0.2.3.ms.jar`)
- [x] Euphoria Patches (NeoForge 1.21.1) ✅ shipped (`EuphoriaPatcher-1.9.3-r5.8.1-neoforge.jar`)
- [x] Complementary Reimagined (shaderpack zip) ✅ shipped (`ComplementaryReimagined_r5.8.1.zip`)
- [ ] Vanilla Tweaks (generate 16× pack at vanillatweaks.net — not a CF/Modrinth download) — *deliberately dropped, not shipped (see §2/§5)*
- [x] Fresh Animations (resourcepack zip) + EMF + ETF ✅ shipped (`FreshAnimations_v1.10.4.zip` + `entity_model_features` + `entitytexturefeatures`)
- [x] AmbientSounds 6 + CreativeCore ✅ shipped (`AmbientSounds_NEOFORGE_v6.3.8` + `CreativeCore_NEOFORGE_v2.13.41`)
- [x] Falling Leaves ✅ shipped (`fallingleaves-1.21.1-2.5.1.jar`)
- [x] Fusion (Connected Textures) ✅ shipped (`fusion-1.3.4-neoforge-mc1.21.1.jar` + Midnighttigger CT pack)
