# Multi-version build (Stonecutter) — open items only

> **The multi-version refactor is COMPLETE** — one codebase builds for 1.21.1 and 26.1.2 via Stonecutter; both nodes
> compile, build, and pass their own native gametest suites; the chiseled CI fan-out + the compat facade are in.
> See `CHANGELOG_1.21.1.md` / `CHANGELOG_26.1.md` + git. Trimmed to OPEN items.

## What's left
- **#56** (open, deprioritized) — route the residual direct API calls in the two gametest suites through the compat
  facade (`SkyseedGameTests.java` / `gametest_26_1_2/SkyseedTests.java` still use `BuiltInRegistries` /
  `ResourceLocation` / registry access directly).
- **#59** (discretionary) — add further version nodes: no third target chosen yet (26.2 was still beta when checked).
  The "how to add a version node" recipe is ready.
- **#57** (contingency) — a version-keyed golden-master fingerprint map: build ONLY if a shared gametest suite ever
  replaces the current per-node suites.
- **#58** (contingency) — a per-version data variant: build ONLY when a vanilla block-id rename actually breaks the
  tolerant skip-on-unknown data (zero demand today).

## Keep as reference
- The "How to add a version node" recipe; the compat-facade surface list; the §2.7 26.1.2 error-map catalog; the
  modern-only content-gating pattern (1.21.4/1.21.5 content shipped to both nodes, inert on 1.21.1).
