# Particular Reforged 1.20.1

## 1.6.1

- **Update notice.** The one-time chat message about new effects now only shows in worlds that existed before the update, not in newly created ones.
- **Config.** New `enabledEffects.updateNotice` option to turn that message off.

## 1.6.0

- **Cascade foam overhaul.** Waterfall foam is now made of many small puffs instead of a few huge ones. Puffs vary in size and brightness, burst outward from the base of the fall, and grow then shrink away smoothly instead of popping.
- **Cascade mist.** A soft translucent mist now drifts off the base of waterfalls.
- **Height-based cascades.** The further the water falls, the bigger and denser the cascade. A one block drop makes a small splash of foam, a 16 block fall makes a large one.
- **Leaf snow.** Leaves with snow on top, or under falling snow, now shed clumps of snow that break into powder with small flecks drifting down. Toggle with `enabledEffects.leafSnow`, frequency in `advancedSettings.leafSnowSettings.spawnChance`.
- **Cold breath.** Players and mobs breathe out small puffs of vapour in biomes cold enough to snow. Undead, underwater and invisible creatures don't. Toggle with `enabledEffects.coldBreath`, tune in `advancedSettings.coldBreathSettings`.
- **Footstep dust.** Sprinting and landing on sand, gravel, dirt, grass and similar blocks now kicks up dust coloured after the block, replacing the vanilla sprint particles and landing burst there. The block list accepts blocks and tags in `advancedSettings.footstepDustSettings`. Toggle with `enabledEffects.footstepDust`.
- **Pollen and spores.** Flowers release drifting pollen during the day, moss and mushrooms release spores, and spores float through the air of lush caves. Toggles `enabledEffects.pollen` and `enabledEffects.spores`, frequency in `advancedSettings.pollenAndSporeSettings`.
- **Embers.** Campfires and lit furnaces throw off tiny glowing embers, blue ones for soul campfires. Toggle with `enabledEffects.embers`, tune in `advancedSettings.emberSettings`.
- **Tumbling bushes.** Dead bushes occasionally break loose and roll across dry biomes with the wind. Toggle with `enabledEffects.tumbleweeds`, tune in `advancedSettings.tumbleweedSettings`.
- **Rainbows.** After rain stops during the day a rainbow can appear opposite the sun, with a fainter secondary bow, and fade out over time. It stays in place in the distance as you move. Toggle with `enabledEffects.rainbows`, chance, duration and opacity in `advancedSettings.rainbowSettings`.
- **Screen shake.** The camera now shakes for loud nearby sounds: Warden roars and sonic booms, Ravager roars, Ender Dragon growls and explosions, fading with distance. Turn it off with `enabledEffects.screenShake`, or tune it in `advancedSettings.screenShakeSettings` with `shakeIntensity` and a `shakeSounds` list that accepts any sound ([#61](https://github.com/Leclowndu93150/Particular/issues/61)).
- **Cascade settings (config).** New `advancedSettings.cascadeSettings` section with `heightScaling`, `minIntensity`, `maxIntensity`, `maxFallHeight`, `foamDensity`, `puffSize` and `mistDensity`, plus an `enabledEffects.cascadeMist` toggle.
- **Update notice.** The first time you open each world after updating, a one-time chat message lists the new effects, in case another mod or resource pack already adds one of them.
- **API.** Other mods can now stop cascades at specific positions with `ParticularApi.addCascadeFilter((level, pos) -> ...)`. Added for Streams Reflowing compatibility ([#64](https://github.com/Leclowndu93150/Particular/issues/64)).
- **Fix.** Saved cascades and cascades being re-checked now go through the same checks as new ones, so a cascade another mod has blocked no longer comes back from the cache.
- **Fix.** Mobs and other players falling into water now make the big 3D splash reliably, sized to their fall, instead of only ripples most of the time.
