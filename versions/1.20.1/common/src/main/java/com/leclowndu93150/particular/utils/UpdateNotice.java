package com.leclowndu93150.particular.utils;

import com.leclowndu93150.particular.ParticularConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpdateNotice {
    private static final String MARKER = "notice_1.6.0";
    private static final int SETTLE_TICKS = 40;
    private static final long NEW_WORLD_TICKS = 1200L;

    private static boolean checked;
    private static int waited;

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (checked || mc.player == null || mc.level == null) return;
        if (++waited < SETTLE_TICKS) return;
        checked = true;
        if (!ParticularConfig.COMMON.updateNotice.get()) return;

        Path marker = mc.gameDirectory.toPath()
                .resolve("particular_cache")
                .resolve(CascadeCache.sanitize(CascadeCache.resolveWorldKey()))
                .resolve(MARKER);
        if (Files.exists(marker)) return;

        if (mc.level.getGameTime() >= NEW_WORLD_TICKS) {
            mc.player.displayClientMessage(Component.translatable("particular.notice.1_6_0"), false);
        }
        try {
            Files.createDirectories(marker.getParent());
            Files.createFile(marker);
        } catch (IOException ignored) {
        }
    }

    public static void reset() {
        checked = false;
        waited = 0;
    }
}
