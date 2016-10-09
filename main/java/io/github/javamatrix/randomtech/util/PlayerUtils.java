package io.github.javamatrix.randomtech.util;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class PlayerUtils {
    public static boolean isHolding(EntityPlayer p, Item i) {
        return p != null && p.getHeldItem() != null
                && p.getHeldItem().getItem() != null
                && p.getHeldItem().getItem().equals(i);
    }

    public static ItemStack getHolding(EntityPlayer p) {
        if (p == null) {
            return null;
        }
        return p.getHeldItem();
    }

    public static boolean hasDebugger(EntityPlayer p) {
        return RandomTechItems.itemDebugger != null
                && isHolding(p, RandomTechItems.itemDebugger);
    }

    public static void sendChat(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    public static boolean isEmptyHanded(EntityPlayer p) {
        return p != null && p.getHeldItem() == null;
    }

    public static void updateHeldItem(EntityPlayer player) {
        ItemStack heldItem = getHolding(player);
        if (heldItem != null) {
            heldItem.damageItem(0, player);
        }
    }
}
