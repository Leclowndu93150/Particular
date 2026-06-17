package com.leclowndu93150.particular.utils;

import java.util.Map;

import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

public record Color(float red, float green, float blue, float alpha) {
    public static final Color BLACK = ofRgb(0);
    public static final Color WHITE = ofRgb(16777215);
    public static final Color RED = ofRgb(16711680);
    public static final Color GREEN = ofRgb(65280);
    public static final Color BLUE = ofRgb(255);
    private static final Map<String, Color> NAMED_TEXT_COLORS = Map.ofEntries(
            Map.entry("black", ofRgb(0x000000)),
            Map.entry("dark-blue", ofRgb(0x0000AA)),
            Map.entry("dark-green", ofRgb(0x00AA00)),
            Map.entry("dark-aqua", ofRgb(0x00AAAA)),
            Map.entry("dark-red", ofRgb(0xAA0000)),
            Map.entry("dark-purple", ofRgb(0xAA00AA)),
            Map.entry("gold", ofRgb(0xFFAA00)),
            Map.entry("gray", ofRgb(0xAAAAAA)),
            Map.entry("dark-gray", ofRgb(0x555555)),
            Map.entry("blue", ofRgb(0x5555FF)),
            Map.entry("green", ofRgb(0x55FF55)),
            Map.entry("aqua", ofRgb(0x55FFFF)),
            Map.entry("red", ofRgb(0xFF5555)),
            Map.entry("light-purple", ofRgb(0xFF55FF)),
            Map.entry("yellow", ofRgb(0xFFFF55)),
            Map.entry("white", ofRgb(0xFFFFFF))
    );

    public Color(float red, float green, float blue) {
        this(red, green, blue, 1.0F);
    }

    public static Color ofArgb(int argb) {
        return new Color(
                (float)(argb >> 16 & 255) / 255.0F,
                (float)(argb >> 8 & 255) / 255.0F,
                (float)(argb & 255) / 255.0F,
                (float)(argb >>> 24) / 255.0F
        );
    }

    public static Color ofRgb(int rgb) {
        return new Color(
                (float)(rgb >> 16 & 255) / 255.0F,
                (float)(rgb >> 8 & 255) / 255.0F,
                (float)(rgb & 255) / 255.0F,
                1.0F
        );
    }

    public static Color ofHsv(float hue, float saturation, float value) {
        return ofRgb(Mth.hsvToRgb(hue - 5.0E-8F, saturation, value));
    }

    public static Color ofHsv(float hue, float saturation, float value, float alpha) {
        return ofArgb((int)(alpha * 255.0F) << 24 | Mth.hsvToRgb(hue - 5.0E-8F, saturation, value));
    }

    public static Color ofDye(@NotNull DyeColor dyeColor) {
        return ofArgb(dyeColor.getTextureDiffuseColor());
    }

    public static Color random() {
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

    public Color interpolate(Color next, float delta) {
        return new Color(
                Mth.lerp(delta, this.red, next.red),
                Mth.lerp(delta, this.green, next.green),
                Mth.lerp(delta, this.blue, next.blue),
                Mth.lerp(delta, this.alpha, next.alpha)
        );
    }

    public static Color fromHexString(String hexString) {
        if (!hexString.startsWith("#")) {
            Color color = NAMED_TEXT_COLORS.get(hexString);
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