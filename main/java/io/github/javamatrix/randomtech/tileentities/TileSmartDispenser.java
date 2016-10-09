package io.github.javamatrix.randomtech.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Random;

public class TileSmartDispenser extends TileBase implements IInventory {

    protected String inventoryName;
    boolean isRoundRobin;
    int roundRobinIndex;
    private ItemStack[] inventory = new ItemStack[9];
    private Random rnd = new Random();

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null) {
            ItemStack itemstack;

            if (this.inventory[slot].stackSize <= amount) {
                itemstack = this.inventory[slot];
                this.inventory[slot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0) {
                    this.inventory[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.inventory[slot] != null) {
            ItemStack itemstack = this.inventory[slot];
            this.inventory[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public int getStack() {
        if (!isRoundRobin) {
            int i = -1;
            int j = 1;

            for (int k = 0; k < this.inventory.length; ++k) {
                if (this.inventory[k] != null && this.rnd.nextInt(j++) == 0) {
                    i = k;
                }
            }

            return i;
        } else {
            while (inventory[roundRobinIndex] == null) {
                roundRobinIndex++;
            }

            return roundRobinIndex++;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inventory[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public int putStack(ItemStack stack) {
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] == null
                    || this.inventory[i].getItem() == null) {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.inventoryName
                : "container.smartdispenser";
    }

    public void func_146018_a(String name) {
        this.inventoryName = name;
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return this.inventoryName != null;
    }

    public void readCustomNBT(NBTTagCompound nbt) {
        super.readCustomNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length) {
                this.inventory[j] = ItemStack
                        .loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if (nbt.hasKey("CustomName", 8)) {
            this.inventoryName = nbt.getString("CustomName");
        }

        if (nbt.hasKey("IsRoundRobin")) {
            isRoundRobin = nbt.getBoolean("IsRoundRobin");
        }

        if (nbt.hasKey("RoundRobinIndex")) {
            roundRobinIndex = nbt.getInteger("RoundRobinIndex");
        }
    }

    public void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound itemInSlot = new NBTTagCompound();
                itemInSlot.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(itemInSlot);
                nbttaglist.appendTag(itemInSlot);
            }
        }

        nbt.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.inventoryName);
        }

        nbt.setBoolean("IsRoundRobin", isRoundRobin);
        nbt.setInteger("RoundRobinIndex", roundRobinIndex);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
                                           this.zCoord) == this && player.getDistanceSq(
                (double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
                (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

}
