# Particular Reforged 26.2

## 1.5.5

- **Fix.** Fireflies now spawn on modded plants (TFC, etc.) that override `animateTick` without calling super. Switched the hook from `Block.animateTick` to `ClientLevel.doAnimateTick` so coverage is consistent across blocks.

## 1.5.4

- **Fix.** Cascade particles no longer accumulate while the game is paused (closes #43).


- **Custom water-like fluids (config).** New `customFluidsSettings.waterLikeFluids` list — fluid IDs treated like vanilla water for splash, rain ripples, and waterfall spray.
- **Custom cascades (config).** New `customFluidsSettings.cascadeFluidPairs` list — `"flowing,source"` pairs that produce cascade waterfalls for any mod fluid that has both variants.
- **Firefly biomes (config).** New `fireflySettings.biomes` list — biome IDs where fireflies always spawn (bypasses temperature check).
- **Firefly spawn blocks (config).** New `fireflySettings.spawnBlocks` list — extra blocks that spawn fireflies, plus a `customBlocks` frequency modifier.
- **Firefly colors (config).** New `fireflySettings.biomeColors` (per-biome RGB tint: `"biomeId|RRGGBB"`) and `fireflySettings.colorPool` (random RGB pool, format `"RRGGBB"`).
