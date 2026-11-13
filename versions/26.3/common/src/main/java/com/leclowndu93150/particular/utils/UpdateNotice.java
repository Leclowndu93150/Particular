package com.leclowndu93150.particular.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpdateNotice {
    private static final String MARKER = "notice_1.6.0";

    private static boolean checked;

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (checked || mc.player == null) return;
        checked = true;

        Path marker = mc.gameDirectory.toPath()
                .resolve("particular_cache")
                .resolve(CascadeCache.sanitize(CascadeCache.resolveWorldKey()))
                .resolve(MARKER);
        if (Files.exists(marker)) return;

        mc.player.sendSystemMessage(Component.translatable("particular.notice.1_6_0"));
        try {
            Files.createDirectories(marker.getParent());
            Files.createFile(marker);
        } catch (IOException ignored) {
        }
    }

    public static void reset() {
        checked = false;
    }
}
