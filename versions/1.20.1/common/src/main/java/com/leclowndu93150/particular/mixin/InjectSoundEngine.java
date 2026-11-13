package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.utils.ScreenShake;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class InjectSoundEngine
{
	@Inject(
		method = "play",
		at = @At("HEAD"))
	private void shakeOnSound(SoundInstance sound, CallbackInfo ci)
	{
		ScreenShake.onSound(sound);
	}
}
