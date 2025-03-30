package com.leclowndu93150.particular.utils;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import org.antlr.v4.runtime.misc.NotNull;

public record OwOColor(float red, float green, float blue, float alpha) {
    public static final OwOColor BLACK = ofRgb(0);
    public static final OwOColor WHITE = ofRgb(16777215);
    public static final OwOColor RED = ofRgb(16711680);
    public static final OwOColor GREEN = ofRgb(65280);
    public static final OwOColor BLUE = ofRgb(255);
    private static final Map<String, OwOColor> NAMED_TEXT_COLORS = Stream.of(ChatFormatting.values())
            .filter(ChatFormatting::isColor)
            .collect(Collectors.toMap(
                    formatting -> formatting.getName().toLowerCase(Locale.ROOT).replace("_", "-"),
                    OwOColor::ofFormatting
            ));

    public OwOColor(float red, float green, float blue) {
        this(red, green, blue, 1.0F);
    }

    public static OwOColor ofArgb(int argb) {
        return new OwOColor(
                (float)(argb >> 16 & 255) / 255.0F,
                (float)(argb >> 8 & 255) / 255.0F,
                (float)(argb & 255) / 255.0F,
                (float)(argb >>> 24) / 255.0F
        );
    }

    public static OwOColor ofRgb(int rgb) {
        return new OwOColor(
                (float)(rgb >> 16 & 255) / 255.0F,
                (float)(rgb >> 8 & 255) / 255.0F,
                (float)(rgb & 255) / 255.0F,
                1.0F
        );
    }

    public static OwOColor ofHsv(float hue, float saturation, float value) {
        return ofRgb(Mth.hsvToRgb(hue - 5.0E-8F, saturation, value));
    }

    public static OwOColor ofHsv(float hue, float saturation, float value, float alpha) {
        return ofArgb((int)(alpha * 255.0F) << 24 | Mth.hsvToRgb(hue - 5.0E-8F, saturation, value));
    }

    public static OwOColor ofFormatting(@NotNull ChatFormatting formatting) {
        Integer colorValue = formatting.getColor();
        return ofRgb(colorValue == null ? 0 : colorValue);
    }

    /* @Reason: Useless and can't bother fixing it
    public static OwOColor ofDye(@NotNull DyeColor dyeColor) {
        return ofArgb(dyeColor.getTextureDiffuseColor());
    }
     */

    public static OwOColor random() {
        return ofArgb((int)(Math.random() * (double)1.6777215E7F) | -16777216);
    }

    public int rgb() {
        return (int)(this.red * 255.0F) << 16 | (int)(this.green * 255.0F) << 8 | (int)(this.blue * 255.0F);
    }

    public int argb() {
        return (int)(this.alpha * 255.0F) << 24 | (int)(this.red * 255.0F) << 16 | (int)(this.green * 255.0F) << 8 | (int)(this.blue * 255.0F);
    }

    public float[] hsv() {
        float cmax = Math.max(Math.max(this.red, this.green), this.blue);
        float cmin = Math.min(Math.min(this.red, this.green), this.blue);
        float saturation;
        if (cmax != 0.0F) {
            saturation = (cmax - cmin) / cmax;
        } else {
            saturation = 0.0F;
        }

        float hue;
        if (saturation == 0.0F) {
            hue = 0.0F;
        } else {
            float redc = (cmax - this.red) / (cmax - cmin);
            float greenc = (cmax - this.green) / (cmax - cmin);
            float bluec = (cmax - this.blue) / (cmax - cmin);
            if (this.red == cmax) {
                hue = bluec - greenc;
            } else if (this.green == cmax) {
                hue = 2.0F + redc - bluec;
            } else {
                hue = 4.0F + greenc - redc;
            }

            hue /= 6.0F;
            if (hue < 0.0F) {
                ++hue;
            }
        }

        return new float[]{hue, saturation, cmax, this.alpha};
    }

    public String asHexString(boolean includeAlpha) {
        return includeAlpha ? String.format("#%08X", this.argb()) : String.format("#%06X", this.rgb());
    }

    public OwOColor interpolate(OwOColor next, float delta) {
        return new OwOColor(
                Mth.lerp(delta, this.red, next.red),
                Mth.lerp(delta, this.green, next.green),
                Mth.lerp(delta, this.blue, next.blue),
                Mth.lerp(delta, this.alpha, next.alpha)
        );
    }

    public static OwOColor fromHexString(String hexString) {
        if (!hexString.startsWith("#")) {
            OwOColor color = NAMED_TEXT_COLORS.get(hexString);
            if (color != null) {
                return color;
            } else {
                throw new IllegalArgumentException("Invalid color value '" + hexString + "', expected hex color of format #RRGGBB or #AARRGGBB or named text color");
            }
        } else if (hexString.matches("#([A-Fa-f\\d]{2}){3,4}")) {
            return hexString.length() == 7
                    ? ofRgb(Integer.parseUnsignedInt(hexString.substring(1), 16))
                    : ofArgb(Integer.parseUnsignedInt(hexString.substring(1), 16));
        } else {
            throw new IllegalArgumentException("Invalid color value '" + hexString + "', expected hex color of format #RRGGBB or #AARRGGBB or named text color");
        }
    }
}