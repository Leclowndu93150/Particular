package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.Main;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DripParticle.FallAndLandParticle.class)
public abstract class InjectContinuousFalling extends TextureSheetParticle
{
	@Shadow @Final protected ParticleOptions landParticle;

	protected InjectContinuousFalling(ClientLevel clientWorld, double d, double e, double f)
	{
		super(clientWorld, d, e, f);
	}

	@Inject(
		method = "postMoveUpdate",
		at = @At("TAIL"))
	private void addRipples(CallbackInfo ci)
	{
		if (!ParticularConfig.waterDripRipples()) { return; }

		if (landParticle != ParticleTypes.SPLASH)
		{
			return;
		}

		BlockPos pos = BlockPos.containing(x, y, z);
		FluidState fluidState = level.getFluidState(pos);
		if (fluidState.is(FluidTags.WATER))
		{
			float yWater = pos.getY() + fluidState.getHeight(level, pos);
			if (y < yWater)
			{
				level.addParticle(Particles.WATER_RIPPLE.get(), x, yWater, z, 0, 0, 0);
			}
		}
	}
}