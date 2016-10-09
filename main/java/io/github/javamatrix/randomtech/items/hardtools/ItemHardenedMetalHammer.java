package io.github.javamatrix.randomtech.items.hardtools;

import io.github.javamatrix.randomtech.items.IHardenedItem;
import io.github.javamatrix.randomtech.items.ItemHammer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemHardenedMetalHammer extends ItemHammer implements
        IHardenedItem {

    public ItemHardenedMetalHammer() {
        super(2.0f);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("\u00a78Better than a precision sledgehammer!");
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 1;
        } else if (!stack.getTagCompound().hasKey("maxDurability")) {
            return 1;
        }

        return stack.getTagCompound().getInteger("maxDurability");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void getSubItems(Item i, CreativeTabs tab, List relevantItems) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        nbt.setInteger("maxDurability", 6000);
        stack.setTagCompound(nbt);
        relevantItems.add(stack);
    }
}
