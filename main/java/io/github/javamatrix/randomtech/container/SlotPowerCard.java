package io.github.javamatrix.randomtech.container;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.IEmpowerable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPowerCard extends Slot {

    Slot otherSlot;

    public SlotPowerCard(IInventory inv, int id, int x, int y, Slot other) {
        super(inv, id, x, y);
        otherSlot = other;
    }

    public boolean isItemValid(ItemStack s) {
        // Only accepts power cards.
        return otherSlot.getHasStack()
                && s.getItem().equals(RandomTechItems.powerCard)
                && s.hasTagCompound()
                && s.getTagCompound().hasKey("cardName")
                && s.getTagCompound().hasKey("cardRank")
                && otherSlot.getStack().getItem() instanceof IEmpowerable
                && !((IEmpowerable) otherSlot.getStack().getItem()).hasPC(
                otherSlot.getStack(),
                s.getTagCompound().getString("cardName"))
                && otherSlot.getStack().hasTagCompound()
                && otherSlot.getStack().getTagCompound().hasKey("level")
                && otherSlot.getStack().getTagCompound().getInteger("level") > s.getTagCompound().getInteger("cardRank");
    }
}
