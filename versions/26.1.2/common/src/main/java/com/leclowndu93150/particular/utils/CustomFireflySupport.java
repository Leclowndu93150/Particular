package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CustomFireflySupport {
    private static Set<Identifier> biomes = Set.of();
    private static Set<Block> blocks = Set.of();
    private static Map<Identifier, Integer> biomeColors = Map.of();
    private static int[] colorPool = new int[0];
    private static int lastBiomeHash = -1;
    private static int lastBlockHash = -1;
    private static int lastBiomeColorHash = -1;
    private static int lastPoolHash = -1;

    private CustomFireflySupport() {}

    public static boolean isFireflyBiome(Holder<Biome> holder) {
        refresh();
        if (biomes.isEmpty()) return false;
        return holder.unwrapKey().map(ResourceKey::identifier).map(biomes::contains).orElse(false);
    }

    public static boolean isFireflySpawnBlock(BlockState state) {
        refresh();
        if (blocks.isEmpty()) return false;
        return blocks.contains(state.getBlock());
    }

    public static int resolveColor(Holder<Biome> holder, RandomSource random, int fallback) {
        refresh();
        if (!biomeColors.isEmpty()) {
            Integer c = holder.unwrapKey().map(ResourceKey::identifier).map(biomeColors::get).orElse(null);
            if (c != null) return c;
        }
        if (colorPool.length > 0) {
            return colorPool[random.nextInt(colorPool.length)];
        }
        return fallback;
    }

    private static void refresh() {
        List<? extends String> biomeList = ParticularConfig.COMMON.fireflyBiomes.get();
        int bh = biomeList.hashCode();
        if (bh != lastBiomeHash) {
            Set<Identifier> set = new HashSet<>();
            for (String id : biomeList) {
                Identifier rl = Identifier.tryParse(id);
                if (rl != null) set.add(rl);
            }
            biomes = set;
            lastBiomeHash = bh;
        }

        List<? extends String> blockList = ParticularConfig.COMMON.fireflySpawnBlocks.get();
        int blh = blockList.hashCode();
        if (blh != lastBlockHash) {
            Set<Block> set = new HashSet<>();
            for (String id : blockList) {
                Identifier rl = Identifier.tryParse(id);
                if (rl == null) continue;
                Block b = BuiltInRegistries.BLOCK.getValue(rl);
                if (b != null && b != Blocks.AIR) set.add(b);
            }
            blocks = set;
            lastBlockHash = blh;
        }

        List<? extends String> bcList = ParticularConfig.COMMON.fireflyBiomeColors.get();
        int bch = bcList.hashCode();
        if (bch != lastBiomeColorHash) {
            Map<Identifier, Integer> map = new HashMap<>();
            for (String entry : bcList) {
                String[] parts = entry.split("\\|");
                if (parts.length != 2) continue;
                Identifier rl = Identifier.tryParse(parts[0].trim());
                Integer rgb = parseHex(parts[1].trim());
                if (rl != null && rgb != null) map.put(rl, rgb);
            }
            biomeColors = map;
            lastBiomeColorHash = bch;
        }

        List<? extends String> poolList = ParticularConfig.COMMON.fireflyColorPool.get();
        int ph = poolList.hashCode();
        if (ph != lastPoolHash) {
            int[] arr = new int[poolList.size()];
            int n = 0;
            for (String hex : poolList) {
                Integer rgb = parseHex(hex.trim());
                if (rgb != null) arr[n++] = rgb;
            }
            colorPool = n == arr.length ? arr : java.util.Arrays.copyOf(arr, n);
            lastPoolHash = ph;
        }
    }

    private static Integer parseHex(String s) {
        try {
            return Integer.parseInt(s, 16) & 0xFFFFFF;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
