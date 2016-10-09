package io.github.javamatrix.randomtech.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHammer extends Item {
    public float efficiency = 0;

    public ItemHammer(float speed) {
        efficiency = speed;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z,
                                     EntityPlayer p) {
        // Don't smash blocks in creative mode. This way, we can play with
        // smithies in creative.
        super.onBlockStartBreak(stack, x, y, z, p);

        stack.damageItem(2, p);

        // Prevents the ghost hammer bug.
        if (p.getHeldItem().stackSize == 0) {
            p.inventory.setInventorySlotContents(p.inventory.currentItem, null);
        }

        return p.capabilities.isCreativeMode;
    }
}
