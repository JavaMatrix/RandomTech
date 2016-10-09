package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.util.PowerCards;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemPowerCard extends Item {

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        if (nbt.hasKey("cardName")) {
            return EnumChatFormatting.AQUA + nbt.getString("cardName")
                    + EnumChatFormatting.RESET;
        }
        return "Power Card";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean sneaking) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        if (nbt.hasKey("cardName")) {
            PowerCards.addToLore(nbt.getString("cardName"),
                                 nbt.getInteger("cardRank"), "", lore, true);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                             int x, int y, int z, int meta, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }

        Random r = new Random();
        int i = r.nextInt(PowerCards.powerCards.size());

        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        int j = 0;
        for (String name : PowerCards.powerCards.keySet()) {
            if (i == j) {
                nbt.setString("cardName", name);
                break;
            }
            j++;
        }
        nbt.setInteger("cardRank", r.nextInt(6));
        stack.setTagCompound(nbt);
        return true;
    }
}
