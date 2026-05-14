# Particular Reforged 1.20.1

Fabric now has feature parity with Forge — everything the loader version did, the Fabric version now does too.

## What's new

- **Reworked cascade waterfalls.** New textures, gentler motion, and bigger waterfalls now spawn proportionally more spray. Looks more natural and less chaotic.
- **Cuboid fluid particles.** Tiny 1-voxel cube particles for splash droplets and (optionally) waterfall spray. On by default for splashes — turn on `cuboidWaterfallSpray` in the config to extend it to waterfalls. Lava splashes get cuboid droplets too.
- **Soft water entries.** Stepping into water no longer triggers a full splash. Gentle entries now create a small ring of bubbles and droplets instead, while real falls still produce the big splash you're used to.
- **Splash transparency.** Splash particles are now translucent (75% opacity by default), adjustable in the config. Waterfalls and lava splashes feel less opaque and blend in better.
- **Waterfalls remember themselves.** Cascade spray used to take a few seconds to appear when you walked up to a waterfall, because the game only "noticed" them when you were right next to them. Now they're saved per world and dimension, so the next time you load that area they show up instantly. Each remembered waterfall is rechecked once on load to make sure the water is still there before re-enabling it. Works in both singleplayer and multiplayer.
