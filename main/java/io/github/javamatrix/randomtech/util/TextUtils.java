package io.github.javamatrix.randomtech.util;

import net.minecraft.util.EnumChatFormatting;

// This class contains jokes that IntelliJ is taking way too literally.
@SuppressWarnings("ConstantConditions")
public class TextUtils {
    public static String formatRF(int rf) {
        if (rf < 1000) {
            return EnumChatFormatting.YELLOW + "" + rf + " RF";
        } else if (rf < 1000000) {
            return String.format(EnumChatFormatting.YELLOW + "%.2fk RF", rf / 1000.0);
        } else if (rf < 10e9) {
            return String.format(EnumChatFormatting.YELLOW + "%.2fM RF", rf / 1000000.0);
        } else {
            return String.format(EnumChatFormatting.YELLOW + "%.2fG RF <WARNING: POWER CREEP DETECTED;" +
                                         "CONTACT JAVAMATRIX ASAP>", rf / 10.0e9);
        }
    }
}
