package com.leclowndu93150.particular.mixin;

import com.leclowndu93150.particular.CommonClass;
import com.leclowndu93150.particular.Particles;
import com.leclowndu93150.particular.ParticularConfig;
import com.leclowndu93150.particular.utils.CustomFireflySupport;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class InjectClientLevelAnimateTick {

    @Inject(
            method = "doAnimateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void particular$animateTick(
            int i, int j, int k, int l, RandomSource random, Block ignored, BlockPos.MutableBlockPos pos,
            CallbackInfo ci,
            @Local BlockState state) {
        Level world = (Level) (Object) this;
        Block block = state.getBlock();

        if (ParticularConfig.fireflies()) {
            double val = random.nextDouble();
            if ((block == Blocks.GRASS_BLOCK && val < ParticularConfig.COMMON.fireflyGrassFrequency.get()) ||
                    (block == Blocks.TALL_GRASS && val < ParticularConfig.COMMON.fireflyTallGrassFrequency.get()) ||
                    (block instanceof FlowerBlock && val < ParticularConfig.COMMON.fireflyFlowersFrequency.get()) ||
                    (block instanceof TallFlowerBlock && val < ParticularConfig.COMMON.fireflyTallFlowersFrequency.get()) ||
                    (val < ParticularConfig.COMMON.fireflyCustomBlockFrequency.get() && CustomFireflySupport.isFireflySpawnBlock(state))) {
                CommonClass.spawnFirefly(world, pos, random);
                return;
            }
        }

        if (ParticularConfig.caveDust()) {
            if (block == Blocks.AIR || block == Blocks.CAVE_AIR) {
                if (random.nextInt(ParticularConfig.COMMON.caveDustSpawnChance.get()) == 0 && pos.getY() < world.getSeaLevel() && particular$isValidBiomeForDust(world, pos)) {
                    float lightChance = 1f - Math.min(8, world.getBrightness(LightLayer.SKY, pos)) / 8f;
                    float depthChance = Math.min(1f, (world.getSeaLevel() - pos.getY()) / 96f);

                    if (random.nextFloat() < lightChance * depthChance) {
                        double x = (double) pos.getX() + random.nextDouble();
                        double y = (double) pos.getY() + random.nextDouble();
                        double z = (double) pos.getZ() + random.nextDouble();
                        world.addParticle(Particles.CAVE_DUST(), x, y, z, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    private static boolean particular$isValidBiomeForDust(Level world, BlockPos pos) {
        var key = world.getBiome(pos).unwrapKey();
        return key.map(k -> !ParticularConfig.COMMON.caveDustExcludeBiomes.get().contains(k.identifier())).orElse(true);
    }
}
