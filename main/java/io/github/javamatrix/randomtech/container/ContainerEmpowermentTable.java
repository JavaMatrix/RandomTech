package io.github.javamatrix.randomtech.container;

import io.github.javamatrix.randomtech.tileentities.TileEmpowermentTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEmpowermentTable extends Container {
    TileEmpowermentTable te;
    EntityPlayer player;

    public ContainerEmpowermentTable(EntityPlayer who, TileEmpowermentTable what) {
        player = who;
        te = what;

        // Add the weapon slot.
        Slot weaponSlot;
        this.addSlotToContainer(weaponSlot = new Slot(te, 0, 80, 17));

        // Add the power card slots.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlotToContainer(new SlotPowerCard(te, i * 3 + j + 1,
                                                          62 + i * 18, 48 + j * 18, weaponSlot));
            }
        }

        // Add the player inventory
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlotToContainer(new Slot(player.inventory, k + i * 9
                        + 9, 8 + k * 18, 115 + i * 18));
            }
        }

        // And the quickbar
        for (int j = 0; j < 9; j++) {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18,
                                             173));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return te.isUseableByPlayer(player);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or
     * you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack origStack = slot.getStack();
            copyStack = origStack.copy();

            if (slotId <= 9) {
                if (!this.mergeItemStack(origStack, 10, 46, true)) {
                    return null;
                }

                slot.onSlotChange(origStack, copyStack);
            } else if (!this.mergeItemStack(origStack, 10, 46, false)) {
                return null;
            }

            if (origStack.stackSize <= 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (origStack.stackSize == copyStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, origStack);
        }

        return copyStack;
    }
}
