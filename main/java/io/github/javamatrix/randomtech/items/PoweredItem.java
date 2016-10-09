package io.github.javamatrix.randomtech.items;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PoweredItem extends Item implements IEnergyContainerItem {

    public PoweredItem() {
        setMaxDamage(1);
    }

    public abstract int getMaxEnergy();

    public abstract int getMaxReceive();

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        int energyReceived = Math.min(getMaxEnergy()
                                              - getEnergyStored(container),
                                      Math.min(maxReceive, getMaxReceive()));

        if (!simulate) {
            NBTTagCompound data = getNBT(container);
            data.setInteger("StoredEnergy", getEnergyStored(container)
                    + energyReceived);
            container.setTagCompound(data);
        }

        return energyReceived;
    }

    public NBTTagCompound getNBT(ItemStack stack) {
        NBTTagCompound data = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            data = stack.getTagCompound();
        }
        if (!data.hasKey("StoredEnergy")) {
            data.setInteger("StoredEnergy", getMaxEnergy());
        }
        return data;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract,
                             boolean simulate) {
        // Can't extract energy from a DeathPack.
        return 0;
    }

    public int drainEnergy(ItemStack container, int toRemove) {
        int energyRemoved = Math.min(getEnergyStored(container), toRemove);
        NBTTagCompound tag = getNBT(container);
        tag.setInteger("StoredEnergy", getEnergyStored(container)
                - energyRemoved);
        container.setTagCompound(tag);
        return energyRemoved;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return getNBT(container).getInteger("StoredEnergy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getMaxEnergy();
    }

    @Override
    public int getDamage(ItemStack stack) {
        float powerRatio = 1 - ((float) getEnergyStored(stack) / (float) getMaxEnergy());
        float damage = powerRatio * this.getMaxDamage();
        return (int) damage;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        float powerRatio = 1 - ((float) getEnergyStored(stack) / (float) getMaxEnergy());
        return (double) powerRatio;
    }
}
