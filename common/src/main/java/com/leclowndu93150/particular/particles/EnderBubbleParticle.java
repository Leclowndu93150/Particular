package com.leclowndu93150.particular.particles;

import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.mixin.AccessorBillboardParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class EnderBubbleParticle extends BubbleColumnUpParticle
{

	public EnderBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public void tick()
	{
		super.tick();

		if (!ParticularConfig.poppingBubbles()) { return; }

		if (this.removed)
		{
			Particle bubble = Minecraft.getInstance().particleEngine.createParticle(Particles.ENDER_BUBBLE_POP(), x, y, z, 0, 0, 0);
			if (bubble != null)
			{
				((AccessorBillboardParticle) bubble).setQuadSize(this.quadSize * 2f);
			}
		}
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i)
		{
			EnderBubbleParticle enderBubbleParticle = new EnderBubbleParticle(clientWorld, d, e, f, g, h, i);
			enderBubbleParticle.pickSprite(this.spriteProvider);
			return enderBubbleParticle;
		}
	}
}