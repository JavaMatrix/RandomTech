package io.github.javamatrix.randomtech.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSynthesisMachine extends TileEnergetic implements
        ISidedInventory {

    public int currentWorkDone = 0;
    protected ItemStack[] inventory = new ItemStack[6];
    protected int[] slotsBottom = new int[]{Slots.OUTPUT.id()};
    protected int[] slotsTop = new int[]{Slots.INPUT_TOP_LEFT.id(),
            Slots.INPUT_TOP_RIGHT.id(), Slots.INPUT_BOTTOM_LEFT.id(),
            Slots.INPUT_BOTTOM_RIGHT.id()};
    protected int[] slotsSides = new int[]{};
    SynthesisRecipes recipes = new SynthesisRecipes();

    public TileSynthesisMachine() {
        super();
        storage = new EnergyStorage(24000, 80);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        super.readCustomNBT(nbt);

        NBTTagList nbtInv = nbt.getTagList("Inventory", 10);

        for (int i = 0; i < nbtInv.tagCount(); i++) {
            NBTTagCompound slot = nbtInv.getCompoundTagAt(i);
            int pos = slot.getInteger("Slot");

            if (pos >= 0 && pos < getSizeInventory()) {
                this.setInventorySlotContents(pos,
                                              ItemStack.loadItemStackFromNBT(slot));
            }
        }

        currentWorkDone = nbt.getInteger("WorkTime");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);

        NBTTagList inventory = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); i++) {
            NBTTagCompound slot = new NBTTagCompound();
            slot.setInteger("Slot", i);
            if (getStackInSlot(i) != null) {
                getStackInSlot(i).writeToNBT(slot);
            }
            inventory.appendTag(slot);
        }

        nbt.setTag("Inventory", inventory);
        nbt.setInteger("WorkTime", currentWorkDone);
    }

    @Override
    public void updateEntity() {
        boolean hasChanged = false;

        // No operation if there's no energy.
        if (storage.getEnergyStored() >= 0) {
            // Or if there are no recipes to do.
            SynthesisRecipes.RecipeResult synthResult = SynthesisRecipes.instance()
                    .getResult(this);
            if (synthResult != null
                    && willFit(synthResult.getResult(), Slots.OUTPUT)) {

                // Now onto the actual synthesis bit.
                // First, we increment the smelting counter according to energy.
                int workToDo = Math.min(40, storage.getEnergyStored());
                currentWorkDone += workToDo;
                extractEnergy(ForgeDirection.UNKNOWN, workToDo, false);
                // Next, check if we've completed the action.
                if (currentWorkDone > synthResult.getWorkRequired()) {
                    // If so, it's time to output the item.
                    ItemStack currentOutputStack = getStackInSlot(Slots.OUTPUT);
                    if (currentOutputStack == null) {
                        setInventorySlotContents(Slots.OUTPUT, synthResult
                                .getResult().copy());
                    } else {
                        inventory[Slots.OUTPUT.id()].stackSize += synthResult
                                .getResult().stackSize;
                    }

                    // And consume input items.
                    for (int i = Slots.INPUT_TOP_LEFT.id(); i <= Slots.INPUT_BOTTOM_RIGHT
                            .id(); i++) {
                        decrStackSize(i, 1);
                    }

                    // DOH! Reset work done.
                    currentWorkDone = 0;

                    // Mark for an update, too.
                    hasChanged = true;
                }
            }
        } else {
            // Should fix the bug where tasks would sometimes instantaneously complete.
            currentWorkDone = 0;
        }

        if (hasChanged) {
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public void setInventorySlotContents(Slots slot, ItemStack stack) {
        setInventorySlotContents(slot.id(), stack);
    }

    private boolean willFit(ItemStack stack, Slots slot) {
        return stack == null || getStackInSlot(slot) == null || stack.isItemEqual(getStackInSlot(slot)) &&
                stack.isStackable() &&
                stack.stackSize + getStackInSlot(slot).stackSize <= stack.getMaxStackSize();
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    public ItemStack getStackInSlot(Slots slot) {
        return getStackInSlot(slot.id());
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        // There's nothing in the slot, so nothing can be taken out.
        if (inventory[slot] == null) {
            return null;
        }
        // There's not enough to remove, so just take out as much as we have.
        if (inventory[slot].stackSize <= amount) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }

        // Finally, the expected case. Remove <amount> items from the stack and
        // then return those items.
        ItemStack stack = inventory[slot].splitStack(amount);

        // This shouldn't happen, but just in case, this will stop ghost items
        // from appearing.
        if (inventory[slot].stackSize <= 0) {
            inventory[slot] = null;
        }

        markDirty();

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        // If there's nothing in the slot, nothing can be thrown on the ground.
        if (inventory[slot] == null) {
            return null;
        }

        // Otherwise, remove it from the slot and throw it on the ground.
        ItemStack stack = inventory[slot];
        inventory[slot] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        // First, set the item.
        inventory[slot] = stack;

        // Next, run some checks to prevent weirdness.
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.synthesisMachine.name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistance(xCoord, yCoord, zCoord) <= 8.0;

    }

    @Override
    public void openInventory() {
        // Nothing to do here.
    }

    @Override
    public void closeInventory() {
        // Or here.
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        switch (slot) {
            // Battery
            case 0:
                return stack.getItem() instanceof IEnergyContainerItem;
            // Crafting slots
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            // Output
            case 5:
                return false;
            default:
                return false;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        switch (side) {
            case 0:
                return slotsBottom;
            case 1:
                return slotsTop;
            default:
                return slotsSides;
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        // Only allow output extraction from the bottom side.
        return slot == Slots.OUTPUT.id() && side == 0;
    }

    public enum Slots {
        INPUT_TOP_LEFT(1), INPUT_TOP_RIGHT(2), INPUT_BOTTOM_LEFT(3), INPUT_BOTTOM_RIGHT(
                4), OUTPUT(5);

        private int value;

        Slots(int value) {
            this.value = value;
        }

        public int id() {
            return value;
        }
    }

}
