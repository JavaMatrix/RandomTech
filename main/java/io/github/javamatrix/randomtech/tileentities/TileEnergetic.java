package io.github.javamatrix.randomtech.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A simple merge of TileEntityBase and TileEnergyHandler. Code by KingLemming
 * with very few modifications.
 *
 * @author JavaMatrix
 */
public class TileEnergetic extends TileBase implements IEnergyHandler {

    protected EnergyStorage storage = new EnergyStorage(32000);

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        storage.readFromNBT(nbt);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        storage.writeToNBT(nbt);
    }

    /* IEnergyConnection */
    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return true;
    }

    /* IEnergyReceiver */
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive,
                             boolean simulate) {
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return storage.receiveEnergy(maxReceive, simulate);
    }

    /* IEnergyProvider */
    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract,
                             boolean simulate) {
        markDirty();
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return storage.extractEnergy(maxExtract, simulate);
    }

    /* IEnergyReceiver and IEnergyProvider */
    @Override
    public int getEnergyStored(ForgeDirection from) {

        return storage.getEnergyStored();
    }

    public void setEnergyStored(int rf) {
        storage.setEnergyStored(rf);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return storage.getMaxEnergyStored();
    }

    public String getInventoryName() {
        return "";
    }
}
