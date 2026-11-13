package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.particles.TumbleweedParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class Tumbleweeds {
    private static final int MAX_ALIVE = 12;
    private static final int MAX_HEIGHT_DIFFERENCE = 16;
    private static final double MIN_DISTANCE = 8.0;
    private static final float MIN_TEMPERATURE = 0.25f;
    private static final float BARE_GROUND_CHANCE = 0.1f;

    private static String parsedTag;
    private static TagKey<Block> groundTag;

    public static void tick(ClientLevel level) {
        if (!ParticularConfig.tumbleweeds()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;
        if (mc.options.particles().get() == ParticleStatus.MINIMAL) return;

        boolean storm = level.isRaining();
        if (!storm && ParticularConfig.COMMON.tumbleweedStormsOnly.get()) return;

        RandomSource random = level.getRandom();
        double chance = ParticularConfig.COMMON.tumbleweedFrequency.get() * (storm ? ParticularConfig.COMMON.tumbleweedStormBoost.get() : 1.0);
        if (random.nextDouble() >= chance || TumbleweedParticle.alive() >= MAX_ALIVE) return;

        TagKey<Block> ground = groundTag();
        if (ground == null) return;

        double angle = random.nextDouble() * Mth.TWO_PI;
        if (Math.cos(angle) + Math.sin(angle) > 0.0) {
            angle += Mth.PI;
        }
        double distance = MIN_DISTANCE + random.nextDouble() * (TumbleweedParticle.RADIUS - MIN_DISTANCE - 1);
        int x = Mth.floor(mc.player.getX() + Math.cos(angle) * distance);
        int z = Mth.floor(mc.player.getZ() + Math.sin(angle) * distance);
        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        if (Math.abs(top - mc.player.getY()) > MAX_HEIGHT_DIFFERENCE) return;

        BlockPos pos = new BlockPos(x, top, z);
        if (!level.getBlockState(pos.below()).is(ground)) return;

        Biome biome = level.getBiome(pos).value();
        if (biome.getPrecipitationAt(pos, level.getSeaLevel()) != Biome.Precipitation.NONE || biome.getBaseTemperature() <= MIN_TEMPERATURE) return;

        BlockState plant = level.getBlockState(pos);
        float red = 1f;
        float green = 1f;
        float blue = 1f;
        if (plant.is(BlockTags.SWORD_EFFICIENT) && !plant.is(BlockTags.CROPS)) {
            BlockTintSource tintSource = mc.getBlockColors().getTintSource(plant, 0);
            if (tintSource != null) {
                int tint = tintSource.colorAsTerrainParticle(plant, level, pos);
                red = (tint >> 16 & 0xFF) / 255f;
                green = (tint >> 8 & 0xFF) / 255f;
                blue = (tint & 0xFF) / 255f;
            }
        } else {
            if (random.nextFloat() >= BARE_GROUND_CHANCE) return;
            plant = Blocks.DEAD_BUSH.defaultBlockState();
        }

        TextureAtlasSprite sprite = mc.getModelManager().getBlockStateModelSet().getParticleMaterial(plant).sprite();
        mc.particleEngine.add(new TumbleweedParticle(level, x + random.nextDouble(), top, z + random.nextDouble(), sprite, red, green, blue));
    }

    private static TagKey<Block> groundTag() {
        String configured = ParticularConfig.COMMON.tumbleweedGroundTag.get();
        if (!configured.equals(parsedTag)) {
            Identifier id = Identifier.tryParse(configured.startsWith("#") ? configured.substring(1) : configured);
            groundTag = id == null ? null : TagKey.create(Registries.BLOCK, id);
            parsedTag = configured;
        }
        return groundTag;
    }
}
