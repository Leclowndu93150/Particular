# Particular Reforged 1.20.1

## Multiloader port

- Ported to Fabric **and** Forge under a single Prism Gradle setup. The 1.20.1 source was Forge-only; Fabric is a brand-new platform target.
- Refactored the Forge-only `Main` into a shared `CommonClass` (logic) plus per-loader `Main` entrypoints.
- Particle registration now goes through a service-based `IParticleRegistry` (Fabric uses `Registry.register` + `FabricParticleTypes`, Forge uses `DeferredRegister`).
- TerraFirmaCraft compat mixins (`TFCMixingFluidMixin`, `TFCWaterMixin`) and the legacy `NetworkHandler` stay Forge-only.

## Backports from 26.1

- **Lava splashes.** Entities falling into lava now produce splash particles (volumetric splash + foam + ring + landing-lava droplets) using the same engine as water splashes. New `lavaSplash` config toggle (default on).
- **Water splash min-fall-distance gate.** New `waterSplashMinFallDistance` config (default 0). Splashes only fire if the entity actually fell that far. Tracks both vanilla `fallDistance` and an accumulated y-velocity fallback for entities whose `fallDistance` was reset (boats, projectiles, etc).
- **Small splash droplets.** New `waterSplashSmallDroplets` config (default off). When enabled, slow water entries also emit a few falling-water droplets instead of just the foam ring.

## Bug fixes

- **Fireflies only spawned on the first in-game day.** Both the daily-frequency re-roll and the time-of-day range check compared against `world.getDayTime()`, which is absolute world time and keeps incrementing forever. After day 1 it never matched the configured firefly window again. Now uses `world.getDayTime() % 24000`.
- **Per-tick allocation in water-splash velocity tracking.** The `LinkedList<Double>` queue allocated on every entity tick. Replaced with a fixed 4-element `double[]` and a rolling index. (Backport of upstream PR #38.)

## Dependencies

- **Fabric**: requires Fabric API and Forge Config API Port. ModMenu is recommended for opening the config screen but not required.
- **Forge**: no extra dependencies. TerraFirmaCraft is supported when present.

## Tooling

- Build system: Prism Gradle plugin, single branch produces both Fabric and Forge jars.
- Mappings: Parchment 2023.09.03 for 1.20.1.
