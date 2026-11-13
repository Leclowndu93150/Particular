package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.utils.ScreenShake;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class InjectCamera
{
	@Shadow
	private float xRot;
	@Shadow
	private float yRot;
	@Shadow
	private Vec3 position;

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Inject(
		method = "alignWithEntity",
		at = @At("TAIL"))
	private void applyScreenShake(float partialTick, CallbackInfo ci)
	{
		ScreenShake.update(position, partialTick);
		float yaw = ScreenShake.yawOffset();
		float pitch = ScreenShake.pitchOffset();
		if (yaw != 0f || pitch != 0f)
		{
			setRotation(yRot + yaw, xRot + pitch);
		}
	}
}
