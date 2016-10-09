package io.github.javamatrix.randomtech.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileGenerator extends TileBase implements
        IEnergyProvider {

    public EnergyStorage storage = new EnergyStorage(40000, 80);

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract,
                             boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return storage.getMaxEnergyStored();
    }

    public EnergyStorage getStorage() {
        return storage;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        storage.readFromNBT(nbt);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        storage.writeToNBT(nbt);
    }
}
