package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CascadeCache {

    private static final ExecutorService IO = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "particular-cascade-cache");
        t.setDaemon(true);
        return t;
    });

    private static final Map<Identifier, Map<BlockPos, Integer>> cache = new ConcurrentHashMap<>();
    private static final Set<Identifier> dirty = ConcurrentHashMap.newKeySet();
    private static String worldKey = null;

    public static void init(Level world) {
        worldKey = resolveWorldKey();
        Identifier dim = world.dimension().identifier();
        if (!cache.containsKey(dim)) {
            load(dim);
        }
    }

    public static void add(Level world, BlockPos pos, int strength) {
        Identifier dim = world.dimension().identifier();
        cache.computeIfAbsent(dim, k -> new ConcurrentHashMap<>()).put(pos.immutable(), strength);
        dirty.add(dim);
        scheduleSave(dim);
    }

    public static void remove(Level world, BlockPos pos) {
        Identifier dim = world.dimension().identifier();
        Map<BlockPos, Integer> dimCache = cache.get(dim);
        if (dimCache != null) {
            dimCache.remove(pos.immutable());
            dirty.add(dim);
            scheduleSave(dim);
        }
    }

    public static Map<BlockPos, Integer> getChunk(Level world, ChunkPos chunk) {
        Identifier dim = world.dimension().identifier();
        Map<BlockPos, Integer> dimCache = cache.get(dim);
        if (dimCache == null) return Map.of();

        Map<BlockPos, Integer> result = new HashMap<>();
        int minX = chunk.getMinBlockX();
        int maxX = chunk.getMaxBlockX();
        int minZ = chunk.getMinBlockZ();
        int maxZ = chunk.getMaxBlockZ();
        for (Map.Entry<BlockPos, Integer> entry : dimCache.entrySet()) {
            BlockPos p = entry.getKey();
            if (p.getX() >= minX && p.getX() <= maxX && p.getZ() >= minZ && p.getZ() <= maxZ) {
                result.put(p, entry.getValue());
            }
        }
        return result;
    }

    public static void onDimensionChange(Level world) {
        Identifier dim = world.dimension().identifier();
        flushAll();
        cache.clear();
        dirty.clear();
        worldKey = resolveWorldKey();
        load(dim);
    }

    public static void onLevelUnload() {
        flushAll();
        cache.clear();
        dirty.clear();
        worldKey = null;
    }

    public static void flushAll() {
        for (Identifier dim : new HashSet<>(dirty)) {
            saveDim(dim);
        }
        dirty.clear();
    }

    private static void scheduleSave(Identifier dim) {
        IO.submit(() -> saveDim(dim));
    }

    private static void saveDim(Identifier dim) {
        if (worldKey == null) return;
        Map<BlockPos, Integer> dimCache = cache.get(dim);
        if (dimCache == null) return;
        try {
            Path file = cacheFile(dim);
            Files.createDirectories(file.getParent());
            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
                for (Map.Entry<BlockPos, Integer> entry : dimCache.entrySet()) {
                    BlockPos p = entry.getKey();
                    out.writeInt(p.getX());
                    out.writeInt(p.getY());
                    out.writeInt(p.getZ());
                    out.writeInt(entry.getValue());
                }
            }
            dirty.remove(dim);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save cascade cache for {}", dim, e);
        }
    }

    private static void load(Identifier dim) {
        if (worldKey == null) return;
        Map<BlockPos, Integer> dimCache = new ConcurrentHashMap<>();
        cache.put(dim, dimCache);
        Path file = cacheFile(dim);
        if (!Files.exists(file)) return;
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            while (in.available() >= 16) {
                int x = in.readInt();
                int y = in.readInt();
                int z = in.readInt();
                int strength = in.readInt();
                dimCache.put(new BlockPos(x, y, z), strength);
            }
            Constants.LOG.debug("Loaded {} cascade positions for {}", dimCache.size(), dim);
        } catch (IOException e) {
            Constants.LOG.error("Failed to load cascade cache for {}", dim, e);
        }
    }

    private static Path cacheFile(Identifier dim) {
        return Minecraft.getInstance().gameDirectory.toPath()
                .resolve("particular_cache")
                .resolve(sanitize(worldKey))
                .resolve("cascades")
                .resolve(sanitize(dim.getNamespace()))
                .resolve(sanitize(dim.getPath()) + ".dat");
    }

    private static String resolveWorldKey() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() != null) {
            return "sp_" + mc.getSingleplayerServer().getWorldData().getLevelName();
        }
        var server = mc.getCurrentServer();
        if (server != null) {
            return "mp_" + server.ip;
        }
        return "unknown";
    }

    private static String sanitize(String s) {
        return s.replaceAll("[^a-zA-Z0-9_\\-.]", "_");
    }
}
