package io.github.javamatrix.randomtech.tileentities;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.IEmpowerable;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine.Slots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEmpowermentTable extends TileBase implements ISidedInventory {

    protected ItemStack[] inventory = new ItemStack[10];

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
            setInventorySlotContents(slot, null);
            return null;
        }
        // There's not enough to remove, so just take out as much as we have.
        if (inventory[slot].stackSize <= amount) {
            ItemStack stack = inventory[slot];
            setInventorySlotContents(slot, null);
            return stack;
        }

        // Finally, the expected case. Remove <amount> items from the stack and
        // then return those items.
        ItemStack stack = inventory[slot].splitStack(amount);

        // This shouldn't happen, but just in case, this will stop ghost items
        // from appearing.
        if (inventory[slot].stackSize <= 0) {
            setInventorySlotContents(slot, null);
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

    public void setInventorySlotContentsInternal(int slot, ItemStack stack) {
        // First, set the item.
        inventory[slot] = stack;

        // Next, run some checks to prevent weirdness.
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) {
            for (int i = 1; i <= 9; i++) {
                setInventorySlotContentsInternal(i, null);
            }
            setInventorySlotContentsInternal(slot, stack);
            // Load up the cards from the stack.
            if (stack != null && stack.getItem() instanceof IEmpowerable) {
                String[][] cards = ((IEmpowerable) stack.getItem())
                        .getInstalledCards(stack);
                for (int i = 0; i < cards.length; i++) {
                    String[] cardData = cards[i];
                    ItemStack card = new ItemStack(RandomTechItems.powerCard);
                    NBTTagCompound nbt = new NBTTagCompound();
                    if (card.hasTagCompound()) {
                        nbt = card.getTagCompound();
                    }
                    nbt.setString("cardName", cardData[0]);
                    nbt.setInteger("cardRank", Integer.parseInt(cardData[1]));
                    card.setTagCompound(nbt);
                    setInventorySlotContentsInternal(i + 1, card);
                }
            }
        } else {
            // Power card slots already do most of our filtering for us, so we
            // can do this:
            // Remove the current card, if there is one.
            if (inventory[slot] != null) {
                ((IEmpowerable) inventory[0].getItem())
                        .removePC(
                                inventory[0],
                                inventory[slot].getTagCompound().getString(
                                        "cardName"),
                                inventory[slot].getTagCompound().getInteger(
                                        "cardRank"));
            }
            // And, if there's a new one, install it.
            if (stack != null) {
                ((IEmpowerable) inventory[0].getItem()).addPC(inventory[0],
                                                              stack.getTagCompound().getString("cardName"), stack
                                                                      .getTagCompound().getInteger("cardRank"));
            }

            // Don't forget to actually set the slot contents.
            setInventorySlotContentsInternal(slot, stack);
        }
    }

    @Override
    public String getInventoryName() {
        return "container.empowermentTable.name";
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
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
            return false;
        }

        return player.getDistance(xCoord, yCoord, zCoord) <= 8.0;
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
            // Weapon
            case 0:
                return stack.getItem().equals(RandomTechItems.galatine);
            // Power Card slots
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return inventory[0] != null;
            default:
                return false;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        // Not accessible from sides.
        return new int[]{};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        // Not insertable.
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        // No extraction.
        return false;
    }

}
