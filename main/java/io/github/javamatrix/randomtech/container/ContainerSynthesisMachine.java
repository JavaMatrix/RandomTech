package io.github.javamatrix.randomtech.container;

import cofh.api.energy.IEnergyContainerItem;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine.Slots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ContainerSynthesisMachine extends Container {
    public static final int PACKET_RF_STORED = 0;
    public static final int PACKET_WORK_TIME = 1;
    TileSynthesisMachine te;
    EntityPlayer player;
    int rfStored = 0;
    int currentWorkDone = 0;

    public ContainerSynthesisMachine(EntityPlayer who, TileSynthesisMachine what) {
        player = who;
        te = what;

        // Add the battery and crafting slots.
        this.addSlotToContainer(new Slot(te, Slots.INPUT_TOP_LEFT.id(), 40, 26));
        this.addSlotToContainer(new Slot(te, Slots.INPUT_TOP_RIGHT.id(), 58, 26));
        this.addSlotToContainer(new Slot(te, Slots.INPUT_BOTTOM_LEFT.id(), 40,
                                         44));
        this.addSlotToContainer(new Slot(te, Slots.INPUT_BOTTOM_RIGHT.id(), 58,
                                         44));
        this.addSlotToContainer(new SlotFurnace(who, te, Slots.OUTPUT.id(),
                                                148, 36));

        // Add the player inventory
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlotToContainer(new Slot(player.inventory, k + i * 9
                        + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        // And the quickbar
        for (int j = 0; j < 9; j++) {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18,
                                             142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, PACKET_RF_STORED,
                                       te.getEnergyStored(ForgeDirection.UNKNOWN));
        crafting.sendProgressBarUpdate(this, PACKET_WORK_TIME,
                                       te.currentWorkDone);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (ICrafting listener : (List<ICrafting>) crafters) {
            if (this.rfStored != te.getEnergyStored(ForgeDirection.UNKNOWN)) {
                listener.sendProgressBarUpdate(this, PACKET_RF_STORED,
                                               te.getEnergyStored(ForgeDirection.UNKNOWN));
            }
            if (this.currentWorkDone != te.currentWorkDone) {
                listener.sendProgressBarUpdate(this, PACKET_WORK_TIME,
                                               currentWorkDone);
            }
        }

        this.rfStored = te.getEnergyStored(ForgeDirection.UNKNOWN);
        this.currentWorkDone = te.currentWorkDone;
    }

    @Override
    public void updateProgressBar(int packet_id, int value) {
        switch (packet_id) {
            case PACKET_RF_STORED:
                setRfStored(value);
                break;
            case PACKET_WORK_TIME:
                te.currentWorkDone = value;
                break;
        }
    }

    private void setRfStored(int value) {
        int rfDiff = value - te.getEnergyStored(ForgeDirection.UNKNOWN);
        if (rfDiff > 0) {
            te.receiveEnergy(ForgeDirection.UNKNOWN, rfDiff, false);
        } else if (rfDiff < 0) {
            te.extractEnergy(ForgeDirection.UNKNOWN, Math.abs(rfDiff), false);
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

            if (slotId == Slots.OUTPUT.id()) {
                if (!this.mergeItemStack(origStack, 6, 42, true)) {
                    return null;
                }

                slot.onSlotChange(origStack, copyStack);
            } else if (slotId != Slots.INPUT_TOP_LEFT.id()
                    && slotId != Slots.INPUT_TOP_RIGHT.id()
                    && slotId != Slots.INPUT_BOTTOM_LEFT.id()
                    && slotId != Slots.INPUT_BOTTOM_RIGHT.id()) {
                if (origStack.getItem() instanceof IEnergyContainerItem) {
                    if (!this.mergeItemStack(origStack, 0, 1, false)) {
                        return null;
                    }
                } else if (!isFull()) {
                    if (!this.mergeItemStack(origStack, 1, 5, false)) {
                        return null;
                    }
                } else if (slotId >= 6 && slotId < 33) {
                    if (!this.mergeItemStack(origStack, 33, 42, false)) {
                        return null;
                    }
                } else if (slotId >= 33 && slotId < 42
                        && !this.mergeItemStack(origStack, 6, 33, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(origStack, 6, 42, false)) {
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

    private boolean isFull() {
        return isSlotFull(1) && isSlotFull(2) && isSlotFull(3) && isSlotFull(4);
    }

    private boolean isSlotFull(int i) {
        return this.getSlot(i).getHasStack()
                && this.getSlot(i).getStack().stackSize >= this.getSlot(i)
                .getStack().getMaxStackSize();
    }
}
