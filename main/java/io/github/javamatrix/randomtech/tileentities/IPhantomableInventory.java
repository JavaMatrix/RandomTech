package io.github.javamatrix.randomtech.tileentities;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface IPhantomableInventory extends ISidedInventory {
    void setPhantomStack(int id, ItemStack stack);
}
