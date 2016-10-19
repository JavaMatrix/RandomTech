package io.github.javamatrix.randomtech.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This class contains jokes that IntelliJ is taking way too literally.
@SuppressWarnings("ConstantConditions")
public class TextUtils {
    private static List<Integer> intColors = new ArrayList<Integer>() {{
        add(0xBE0000);
        add(0xFE3F3F);
        add(0xD9A334);
        add(0xFEFE3F);
        add(0x00BE00);
        add(0x3FFE3F);
        add(0x3FFEFE);
        add(0x00BEBE);
        add(0x0000BE);
        add(0x3F3FFE);
        add(0xFE3FFE);
        add(0xBE00BE);
        add(0xFFFFFF);
        add(0xBEBEBE);
        add(0x3F3F3F);
        add(0x000000);
    }};
    private static List<String> chatColors = new ArrayList<String>() {{
        add(EnumChatFormatting.DARK_RED + "");
        add(EnumChatFormatting.RED + "");
        add(EnumChatFormatting.GOLD + "");
        add(EnumChatFormatting.YELLOW + "");
        add(EnumChatFormatting.DARK_GREEN + "");
        add(EnumChatFormatting.GREEN + "");
        add(EnumChatFormatting.AQUA + "");
        add(EnumChatFormatting.DARK_AQUA + "");
        add(EnumChatFormatting.DARK_BLUE + "");
        add(EnumChatFormatting.BLUE + "");
        add(EnumChatFormatting.LIGHT_PURPLE + "");
        add(EnumChatFormatting.DARK_PURPLE + "");
        add(EnumChatFormatting.WHITE + "");
        add(EnumChatFormatting.GRAY + "");
        add(EnumChatFormatting.DARK_GRAY + "");
        add(EnumChatFormatting.BLACK + "");
    }};

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

    public static String formatRFd(int rf) {
        String str = formatRF(Math.abs(rf)).substring(2) + "/t";
        if (rf < 0) {
            str = EnumChatFormatting.DARK_RED + "(-" + str + ")";
        } else if (rf == 0) {
            str = EnumChatFormatting.YELLOW + "(\u00b1" + str + ")";
        } else {
            str = EnumChatFormatting.DARK_GREEN + "(+" + str + ")";
        }
        return str;
    }

    private static String colorToChatColor(int color) {
        byte rc = (byte) (color & 0xFF0000 >> 16);
        byte gc = (byte) (color & 0x00FF00 >> 8);
        byte bc = (byte) (color & 0x0000FF);

        List<Integer> diffs = new ArrayList<>();
        for (int x : intColors) {
            byte rx = (byte) (x & 0xFF0000 >> 16);
            byte gx = (byte) (x & 0x00FF00 >> 8);
            byte bx = (byte) (x & 0x0000FF);

            int diff = Math.abs(rx - rc) + Math.abs(gx - gc) + Math.abs(bx - bc);
            diffs.add(diff);
        }

        int idx = diffs.indexOf(Collections.min(diffs));

        return chatColors.get(idx);
    }

    public static String formatFluid(FluidStack stack) {
        String color = colorToChatColor(stack.getFluid().getColor(stack));
        return String.format("%s%s: %d mB", color, stack.getLocalizedName(), stack.amount);
    }
}
