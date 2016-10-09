package io.github.javamatrix.randomtech.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author JavaMatrix
 */
public class ItemSundial extends Item {

    int delay = -1;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("A handy tool for the");
        lore.add("short-sighted - as long as");
        lore.add("they are outdoors.");
        lore.add("(Right-click outdoors to use)");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world,
                                      EntityPlayer player) {

        // Keep a delay of 2 seconds between queries.
        if (delay > 0) {
            return stack;
        }

        // If the player is above ground, then tell them an approximation of the
        // time.
        // Otherwise, just tell them it doesn't work underground.
        if (world.canBlockSeeTheSky(MathHelper.floor_double(player.posX),
                                    MathHelper.floor_double(player.posY),
                                    MathHelper.floor_double(player.posZ))) {
            long time = world.getWorldTime() % 24000;

            String s;

            if (time >= 0 && time <= 1000) {
                s = "The sun is rising.";
            } else if (time > 1000 && time <= 10000) {
                s = "The sun is in the sky.";
            } else if (time > 10000 && time <= 11000) {
                s = "The sun will start setting soon.";
            } else if (time > 11000 && time <= 12000) {
                s = "The sun is setting.";
            } else if (time > 12000 && time <= 13000) {
                s = "The moon is rising.";
            } else if (time > 13000 && time <= 23000) {
                s = "The moon is in the sky.";
            } else {
                s = "The moon is setting.  The sun will rise soon.";
            }

            player.addChatMessage(new ChatComponentText(s));
        } else {
            player.addChatMessage(new ChatComponentText(
                    "The sundial is useless underground and even under trees.  You'll have to craft something better when you can."));
        }

        // Set the delay all the way up.
        delay = 40;

        return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4,
                         boolean par5) {
        // Decrement the delay by a tick.
        delay--;
    }
}
