package io.github.javamatrix.randomtech.items.hardtools;

import io.github.javamatrix.randomtech.items.IHardenedItem;
import io.github.javamatrix.randomtech.items.ItemHammer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemHardenedMagicalHammer extends ItemHammer implements
        IHardenedItem {

    public ItemHardenedMagicalHammer() {
        super(4.5f);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("\u00a76Mjolnir, to me!");
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
