package io.github.javamatrix.randomtech.items.hardtools;

import io.github.javamatrix.randomtech.items.IHardenedItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

public class ItemHardenedMagicalHoe extends ItemHoe implements IHardenedItem {
    public ItemHardenedMagicalHoe() {
        super(EnumHelper.addToolMaterial("hardenedMagicalHoe", 3, 100, 9.0f, 2.5f,
                                         22));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("\u00a76Pointless, but magical!");
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
        nbt.setInteger("maxDurability", 4000);
        stack.setTagCompound(nbt);
        relevantItems.add(stack);
    }
}
