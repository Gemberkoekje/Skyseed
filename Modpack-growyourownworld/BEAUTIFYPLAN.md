# BEAUTIFYPLAN — Skyseed: Grow your own world

Beautification pass for the modpack (NeoForge **1.21.1**, Embeddium-based).

> **Status (2026-07-01): SHIPPED.** The full stack is in `mods.txt` and wired: the shader stack (Iris + Monocle
> + Euphoria Patches + Complementary Reimagined, ON by default), Fresh Animations (+EMF/ETF), AmbientSounds 6
> (+CreativeCore), Falling Leaves, Fusion (+ Midnighttigger's Default CT pack), Sound Physics Remastered, and
> Better Clouds — plus the worldgen beautify wins (denser forests, rounded lakes, walled rivers). Resource packs
> enabled by default in `overrides/options.txt`; downloaded zips/shader folders gitignored with `mods.txt` +
> configs committed as the manifest.
>
> **What's left** (tracked in [`../PLANOFPLANS.md`](../PLANOFPLANS.md)):
> - **#21 — Distant Horizons (LOD), optional:** its stated blocker ("add after shaders are stable") has
>   cleared. If wanted: download the NeoForge 1.21.1 DH jar into `overrides/mods/`, verify shader compat with
>   the Iris + Monocle + Complementary/Euphoria stack (§1), re-run `gen-mods-txt.ps1`.
> - **#55 — shaderPack pin maintenance (standing rule, currently correct):** `overrides/config/iris.properties`
>   pins the patched folder's **versioned** name (`ComplementaryReimagined_r5.8.1 + EuphoriaPatches_1.9.3`) —
>   whenever Complementary or Euphoria Patches is updated, refresh that pin **and** `mods.txt`, or shaders
>   silently fail.
> - *(Deliberate drops, revivable — recipes kept below: **#53** Vanilla Tweaks (§2), **#54** standalone Skyseed
>   resource pack (§3).)*

---

## 1. Shaders — Complementary Reimagined + Euphoria Patches (reference for #21/#55)

Embeddium is **not** directly Iris-compatible on NeoForge. Verified working path for 1.21.1:

| Component | Location | Notes |
|---|---|---|
| **Iris** (NeoForge build) | `overrides/mods/` | Shader loader. https://modrinth.com/mod/iris |
| **Monocle** `0.2.3+` | `overrides/mods/` | Bridges Iris → our existing **Embeddium** renderer. Requires Iris + Embeddium. https://modrinth.com/mod/monocle-iris |
| **Euphoria Patches** (NeoForge 1.21.1) | `overrides/mods/` | Mod jar that patches the Complementary zip in `shaderpacks/`. https://modrinth.com/mod/euphoria-patches |
| **Complementary Reimagined** `.zip` | `overrides/shaderpacks/` | Base shaderpack (vanilla-faithful). https://modrinth.com/shader/complementary-reimagined |

> Oculus is dead for 1.21.1 — do **not** use it. Keep Embeddium; do not add Sodium.

## 2. Vanilla Tweaks (16×) — deliberately dropped (#53); revival recipe

- Generate a custom 16× pack at https://vanillatweaks.net/picks/resource-packs/ (borderless glass, alt ores,
  animated items, …). Keeps native resolution, so the world reads as vanilla.
- It's **site-generated, not on CurseForge** → can't be CF-linked. Drop the `.zip` in
  `overrides/resourcepacks/` (redistribution OK in modpacks with credit) and enable it in
  `overrides/options.txt`.
- Faithful 32× is **not used** (we moved off the higher-res look).

## 3. Standalone Skyseed resource pack — dropped for now (#54); re-scaffold recipe

At 16× the mod already ships matching icons, so there's no cohesion work to do, and the standalone
`Resourcepack-skyseed/` project was **removed** (never committed — gone, but quick to rebuild). If branding
(custom `pack.png`, themed splashes, a main-menu panorama) is wanted later: re-scaffold a standalone pack + a
small `build.ps1` (zip with `pack.mcmeta` at the root, forward-slash paths) and publish it as its own
CurseForge resource-pack project.
