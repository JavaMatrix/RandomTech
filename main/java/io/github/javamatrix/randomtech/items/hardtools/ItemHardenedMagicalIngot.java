package io.github.javamatrix.randomtech.items.hardtools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemHardenedMagicalIngot extends Item {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("Tough as nails, and magical too!");
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("MoreThan")) {
            int amount = stack.getTagCompound().getInteger("MoreThan");
            lore.add("\u00a7eHardness: " + amount + "+ durons");
        } else if (stack.getItemDamage() != 0) {
            lore.add("\u00a7eHardness: " + stack.getItemDamage() + " durons");
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack s) {
        return 0.0;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void getSubItems(Item i, CreativeTabs tab, List relevantItems) {
        relevantItems.add(new ItemStack(this, 1, 14));
    }
}
