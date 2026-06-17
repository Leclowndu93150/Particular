package com.leclowndu93150.particular.mixin;


import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleParticle.class)
public abstract class InjectWaterBubbleParticle extends SingleQuadParticle
{
	protected InjectWaterBubbleParticle(ClientLevel clientWorld, double d, double e, double f, TextureAtlasSprite sprite)
	{
		super(clientWorld, d, e, f, sprite);
	}

	@Inject(
		method = "tick",
		at = @At("TAIL"))
	private void releaseBubbles(CallbackInfo ci)
	{
		if (!ParticularConfig.poppingBubbles()) { return; }

		if (this.removed || lifetime == 0)
		{
			Particle bubble = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0, 0, 0);
			if (bubble != null)
			{
				((AccessorBillboardParticle) bubble).setQuadSize(this.quadSize * 2f);
			}
		}
	}
}
