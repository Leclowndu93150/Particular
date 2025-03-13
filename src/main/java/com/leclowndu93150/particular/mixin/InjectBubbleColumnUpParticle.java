package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class InjectBubbleColumnUpParticle extends TextureSheetParticle
{
	protected InjectBubbleColumnUpParticle(ClientLevel clientWorld, double d, double e, double f)
	{
		super(clientWorld, d, e, f);
	}

	@Inject(
		method = "tick",
		at = @At("TAIL"), remap = false)
	private void releaseBubbles(CallbackInfo ci)
	{
		if (!ParticularConfig.poppingBubbles()) { return; }

		if (this.removed)
		{
			Particle bubble = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0, 0, 0);
			if (bubble != null)
			{
				((AccessorBillboardParticle) bubble).setQuadSize(this.quadSize * 2f);
			}

			//world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 1f, 1f, true);
		}
	}
}