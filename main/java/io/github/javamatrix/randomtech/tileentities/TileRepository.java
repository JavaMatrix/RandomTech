package io.github.javamatrix.randomtech.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileRepository extends TileBase implements ISidedInventory {

    protected ItemStack[] inventory = new ItemStack[512];

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int position, int amount) {
        if (this.inventory[position] != null) {
            ItemStack stack;
            if (this.inventory[position].stackSize <= amount) {
                stack = this.inventory[position];
                this.inventory[position] = null;
                return stack;
            } else {
                stack = this.inventory[position].splitStack(amount);
                if (this.inventory[position].stackSize <= 0) {
                    this.inventory[position] = null;
                }
                return stack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int position) {
        // The vanilla version has a null check for some reason. It shouldn't be
        // necessary here.
        ItemStack stack = this.inventory[position];
        this.inventory[position] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int position, ItemStack stack) {
        this.inventory[position] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "Repository";
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
    public void readCustomNBT(NBTTagCompound nbt) {
        NBTTagList nbtItems = nbt.getTagList("Inventory", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbtItems.tagCount(); i++) {
            NBTTagCompound item = nbtItems.getCompoundTagAt(i);
            int pos = item.getInteger("Slot");

            if (pos > 0 && pos < this.getSizeInventory()) {
                this.inventory[pos] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        NBTTagList nbtItems = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (this.inventory[i] != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", i);
                this.inventory[i].writeToNBT(item);
                nbtItems.appendTag(item);
            }
        }
        nbt.setTag("Inventory", nbtItems);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        } else {
            return player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
                                        this.zCoord + 0.5) <= 64.0;
        }
    }

    @Override
    public void openInventory() {
        // The player can't actually open this.
    }

    @Override
    public void closeInventory() {
        // Therefore, they can't close it either.
    }

    @Override
    public boolean isItemValidForSlot(int position, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        // All slots from any side.
        int[] allSlots = new int[512];
        for (int i = 0; i < 512; i++) {
            allSlots[i] = i;
        }
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int position, ItemStack stack, int side) {
        return true;
    }

    @Override
    public boolean canExtractItem(int position, ItemStack stack, int side) {
        return true;
    }
}
