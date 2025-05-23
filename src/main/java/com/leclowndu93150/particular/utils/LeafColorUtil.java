package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.mixin.NativeImageAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelDataManager;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.List;

public class LeafColorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("LeafColorUtil");
    private static final RandomSource renderRandom = RandomSource.create();

    /**
     * Calculate average color from a texture, only considering non-transparent pixels
     */
    public static double[] averageColor(NativeImage image) {
        if (image.format() != NativeImage.Format.RGBA) {
            LOGGER.error("RGBA image required, was {}", image.format());
            return new double[] {1, 1, 1};
        }

        // Use the mixin to safely access pixels
        long pixels = ((NativeImageAccessor)(Object)image).getPixels();

        if (pixels == 0) {
            LOGGER.error("Image is not allocated");
            return new double[] {1, 1, 1};
        }

        double r = 0;
        double g = 0;
        double b = 0;
        int n = 0;

        int width = image.getWidth();
        int height = image.getHeight();

        // Add up all opaque color values
        for (int i = 0; i < width * height; i++) {
            int c = MemoryUtil.memGetInt(pixels + 4L * i);

            // RGBA format
            int cr = (c       & 255);
            int cg = (c >> 8  & 255);
            int cb = (c >> 16 & 255);
            int ca = (c >> 24 & 255);

            if (ca != 0) {
                r += cr;
                g += cg;
                b += cb;
                n++;
            }
        }

        if (n == 0) return new double[] {1, 1, 1};

        return new double[] {
                (r / n) / 255.0,
                (g / n) / 255.0,
                (b / n) / 255.0
        };
    }

    /**
     * Get the texture color from a block state
     */
    public static double[] getBlockTextureColor(BlockState state, Level world, BlockPos pos) {
        Minecraft client = Minecraft.getInstance();
        BlockStateModel model = client.getBlockRenderer().getBlockModel(state);
        ModelDataManager modelDataManager = world.getModelDataManager();
        ModelData modelData = modelDataManager.getAt(pos);

        List<BlockModelPart> parts = model.collectParts(world, pos, state, renderRandom);
        renderRandom.setSeed(state.getSeed(pos));
        List<BakedQuad> quads = new ObjectArrayList<>();
        for (BlockModelPart part : parts) {
            quads.addAll(part.getQuads(Direction.DOWN));
        }

        TextureAtlasSprite sprite;
        boolean shouldColor;

        // Read data from the first bottom quad if possible
        if (!quads.isEmpty()) {
            BakedQuad quad = quads.get(0);
            sprite = quad.sprite();
            shouldColor = quad.isTinted();
        } else {
            // Fall back to block breaking particle
            sprite = model.particleIcon();
            shouldColor = true;
        }

        SpriteContents contents = sprite.contents();
        ResourceLocation spriteId = contents.name();
        NativeImage texture = contents.byMipLevel[0]; // directly extract texture
        int blockColor = (shouldColor ? client.getBlockColors().getColor(state, world, pos, 0) : -1);

        return calculateLeafColor(spriteId, texture, blockColor);
    }

    /**
     * Combines texture and biome colors
     */
    private static double[] calculateLeafColor(ResourceLocation spriteId, NativeImage texture, int blockColor) {
        // Use texture cache for performance
        double[] textureColor = TextureCache.INST.computeIfAbsent(spriteId, (loc) -> {
            double[] doubles = averageColor(texture);
            return new TextureCache.Data(doubles);
        }).getColor();

        if (blockColor != -1) {
            // Multiply texture and block color RGB values (in range 0-1)
            textureColor[0] *= (blockColor >> 16 & 255) / 255.0;
            textureColor[1] *= (blockColor >> 8  & 255) / 255.0;
            textureColor[2] *= (blockColor       & 255) / 255.0;
        }

        return textureColor;
    }

    /**
     * Returns an AWT Color from the texture color values
     */
    public static Color getColorFromValues(double[] values) {
        return new Color(
                (float) Math.max(0, Math.min(1, values[0])),
                (float) Math.max(0, Math.min(1, values[1])),
                (float) Math.max(0, Math.min(1, values[2]))
        );
    }
}