package com.leclowndu93150.particular.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class EmberParticle extends TextureSheetParticle
{
	private static final int FULL_BRIGHT = 0xF000F0;
	private static final int FRAMES = 6;
	private static final int SPRITES = FRAMES * 2;
	private static final double WANDER = 0.004;
	private static final float FLICKER_SPEED = 0.9f;
	private static final float FLICKER_DEPTH = 0.15f;
	private static final float SHRINK_START = 0.7f;

	protected final SpriteSet provider;
	private final float phase;
	private boolean soul;
	private int delay;

	protected EmberParticle(ClientLevel clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteSet provider)
	{
		super(clientWorld, x, y, z);
		this.provider = provider;
		lifetime = 30 + random.nextInt(30);
		quadSize = 0.008f + random.nextFloat() * 0.004f;
		phase = random.nextFloat() * Mth.TWO_PI;
		gravity = 0f;
		friction = 0.96f;
		setParticleSpeed(velX, velY, velZ);
		setFrame();
	}

	public void configure(boolean soul, int delay, float gravity, int lifetime)
	{
		this.soul = soul;
		this.delay = delay;
		this.gravity = gravity;
		this.lifetime = lifetime;
		setFrame();
	}

	private void setFrame()
	{
		int frame = Math.min(FRAMES - 1, age * FRAMES / lifetime);
		setSprite(provider.get(frame + (soul ? FRAMES : 0), SPRITES - 1));
	}

	@Override
	public void tick()
	{
		if (delay > 0)
		{
			delay--;
			xo = x;
			yo = y;
			zo = z;
			return;
		}

		xd += (random.nextDouble() - 0.5) * WANDER;
		zd += (random.nextDouble() - 0.5) * WANDER;
		super.tick();
		setFrame();

		if (onGround)
		{
			remove();
		}
	}

	@Override
	public void render(VertexConsumer buffer, Camera camera, float partialTicks)
	{
		if (delay > 0) { return; }

		float flicker = 1f - FLICKER_DEPTH + FLICKER_DEPTH * Mth.sin((age + partialTicks) * FLICKER_SPEED + phase);
		setColor(flicker, flicker, flicker);
		super.render(buffer, camera, partialTicks);
	}

	@Override
	public float getQuadSize(float partialTicks)
	{
		float progress = Math.min(1f, (age + partialTicks) / lifetime);
		if (progress < SHRINK_START)
		{
			return quadSize;
		}

		float shrink = (progress - SHRINK_START) / (1f - SHRINK_START);
		return quadSize * (1f - shrink * shrink);
	}

	@Override
	protected int getLightColor(float partialTicks)
	{
		return FULL_BRIGHT;
	}

	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet provider;

		public Factory(SpriteSet provider)
		{
			this.provider = provider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ)
		{
			return new EmberParticle(world, x, y, z, velX, velY, velZ, provider);
		}
	}
}
