# Particular Reforged 1.21.1

## Multiloader port

- Ported to Fabric **and** NeoForge under a single Prism Gradle setup. The 1.21.1 source was NeoForge-only; Fabric is a brand-new platform target.
- Refactored the NeoForge-only `Main` into a shared `CommonClass` (logic) plus per-loader `Main` entrypoints.
- Particle registration now goes through a service-based `IParticleRegistry` (Fabric uses `Registry.register` + `FabricParticleTypes`, NeoForge uses `DeferredRegister`).
- `IrisCompat` switched from `ModList.get().isLoaded()` to a platform-abstracted `Services.PLATFORM.isModLoaded()`.

## Backports from 26.1

- **Lava splashes.** Entities falling into lava now produce splash particles (volumetric splash + foam + ring + landing-lava droplets) using the same engine as water splashes. New `lavaSplash` config toggle (default on).
- **Water splash min-fall-distance gate.** New `waterSplashMinFallDistance` config (default 0). Splashes only fire if the entity actually fell that far. Tracks both vanilla `fallDistance` and an accumulated y-velocity fallback for entities whose `fallDistance` was reset (boats, projectiles, etc).
- **Small splash droplets.** New `waterSplashSmallDroplets` config (default off). When enabled, slow water entries also emit a few falling-water droplets instead of just the foam ring.
- **Config screen lang.** Picked up the `particular.configuration.*` translations from 26.1 so the NeoForge `ConfigurationScreen` (and the Fabric mirror via Forge Config API Port) shows readable labels and tooltips instead of raw config paths.

## Bug fixes

- **Fireflies only spawned on the first in-game day.** Same bug as upstream — fixed by switching to `world.getDayTime() % 24000` for time-of-day checks.

## Dependencies

- **Fabric**: requires Fabric API and Forge Config API Port. ModMenu is recommended for opening the config screen but not required.
- **NeoForge**: no extra dependencies — config screen is built into NeoForge.

## Tooling

- Build system: Prism Gradle plugin, single branch produces both Fabric and NeoForge jars.
- Mappings: Parchment 2024.11.17 for 1.21.1.
