package io.github.javamatrix.randomtech.tileentities;

import cofh.api.energy.IEnergyProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by JavaMatrix on 7/20/2015.
 * Licensed under Apache Commons 2.0.
 */
public class TileThermionicEngine extends TileBase implements IEnergyProvider, IFluidHandler {

    public static final int MAX_ENERGY_STORED = 400000;
    public static final double TEMPERATURE_TO_WORK_FACTOR = 0.004;
    private FluidTank hot;
    private FluidTank cold;
    private int energyStored;

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        NBTTagCompound hotNBT = new NBTTagCompound();
        if (hot != null) {
            hot.writeToNBT(hotNBT);
        }
        nbt.setTag("Hot", hotNBT);

        NBTTagCompound coldNBT = new NBTTagCompound();
        if (cold != null) {
            cold.writeToNBT(coldNBT);
        }
        nbt.setTag("Cold", coldNBT);

        nbt.setInteger("Energy", energyStored);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Hot")) {
            hot = new FluidTank(hot.getFluid(), 0);
            hot.readFromNBT(nbt.getCompoundTag("Hot"));
        }
        if (nbt.hasKey("Cold")) {
            cold = new FluidTank(hot.getFluid(), 0);
            cold.readFromNBT(nbt.getCompoundTag("Hot"));
        }
        if (nbt.hasKey("Energy")) {
            energyStored = nbt.getInteger("Energy");
        }
    }

    @Override
    public void updateEntity() {
        int temperatureDifference = hot.getFluid().getFluid().getTemperature() - cold.getFluid().getFluid().getTemperature();
        // Work is both the amount of energy produced and the amount of each fluid (in mB) that is drained from the
        // internal tanks. It's based on a value of 40 work for lava (1300T) and water (300T) by default.
        int work = (int) (temperatureDifference * TEMPERATURE_TO_WORK_FACTOR);
        if (hot.getFluidAmount() > work && cold.getFluidAmount() > work) {
            energyStored = Math.min(energyStored + temperatureDifference / 400, MAX_ENERGY_STORED);
            hot.drain(work, true);
            cold.drain(work, true);
        }
    }

    /**
     * Remove energy from an IEnergyProvider, internal distribution is left entirely to the IEnergyProvider.
     *
     * @param from       Orientation the energy is extracted from.
     * @param maxExtract Maximum amount of energy to extract.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted.
     */
    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        int energyToExtract = Math.min(energyStored, maxExtract);

        if (!simulate) {
            energyStored -= energyToExtract;
        }

        return energyToExtract;
    }

    /**
     * Returns the amount of energy currently stored.
     *
     * @param from The side to query energy levels from.
     */
    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStored;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     *
     * @param from The side to query max energy levels from.
     */
    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return MAX_ENERGY_STORED;
    }

    /**
     * Returns TRUE if the TileEntity can connect on a given side.
     *
     * @param from The side to query connection ability from.
     */
    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return true;
    }

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param from     Orientation the Fluid is pumped in from.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param doFill   If false, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid().equals(hot.getFluid().getFluid()) || (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() > 0)) {
            int amount = Math.min(8000 - hot.getFluidAmount(), resource.amount);
            if (doFill) {
                hot.setFluid(new FluidStack(hot.getFluid(), hot.getFluidAmount() + amount));
            }
            return amount;
        } else if (resource.getFluid().equals(cold.getFluid().getFluid()) || (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() <= 0)) {
            int amount = Math.min(8000 - cold.getFluidAmount(), resource.amount);
            if (doFill) {
                cold.setFluid(new FluidStack(cold.getFluid(), cold.getFluidAmount() + amount));
            }
            return amount;
        } else {
            return 0;
        }
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param from     Orientation the Fluid is drained to.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource.getFluid().equals(hot.getFluid().getFluid()) || (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() > 0)) {
            int amount = Math.min(8000 - hot.getFluidAmount(), resource.amount);
            if (doDrain) {
                hot.setFluid(new FluidStack(hot.getFluid(), hot.getFluidAmount() + amount));
            }
            return new FluidStack(hot.getFluid().getFluid(), amount);
        } else if (resource.getFluid().equals(cold.getFluid().getFluid()) || (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() <= 0)) {
            int amount = Math.min(8000 - cold.getFluidAmount(), resource.amount);
            if (doDrain) {
                cold.setFluid(new FluidStack(cold.getFluid(), cold.getFluidAmount() + amount));
            }
            return new FluidStack(cold.getFluid().getFluid(), amount);
        } else {
            return new FluidStack(FluidRegistry.WATER, 0);
        }
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p>
     * This method is not Fluid-sensitive.
     * <p>
     * This particular implementation will first try to drain the full amount,
     * then any amount at all, preferring hot fluids in both cases.
     *
     * @param from     Orientation the fluid is drained to.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain  If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (hot.getFluidAmount() > maxDrain) {
            return drain(from, new FluidStack(hot.getFluid().getFluid(), hot.getFluidAmount()), doDrain);
        } else if (cold.getFluidAmount() > maxDrain) {
            return drain(from, new FluidStack(cold.getFluid().getFluid(), cold.getFluidAmount()), doDrain);
        } else if (hot.getFluidAmount() > 0) {
            return drain(from, new FluidStack(hot.getFluid().getFluid(), hot.getFluidAmount()), doDrain);
        } else if (cold.getFluidAmount() > 0) {
            return drain(from, new FluidStack(hot.getFluid().getFluid(), hot.getFluidAmount()), doDrain);
        }
        return new FluidStack(FluidRegistry.WATER, 0);
    }

    /**
     * Returns true if the given fluid can be inserted into the given direction.
     * <p>
     * More formally, this should return true if fluid is able to enter from the given direction.
     *
     * @param from The side to query fill possibility from.
     * @param fluid The fluid to query fill possibility about.
     */
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    /**
     * Returns true if the given fluid can be extracted from the given direction.
     * <p>
     * More formally, this should return true if fluid is able to leave from the given direction.
     *
     * @param from The side to query drain possibility from.
     * @param fluid The fluid to query drain possibility about.
     */
    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    /**
     * Returns an array of objects which represent the internal tanks. These objects cannot be used
     * to manipulate the internal tanks. See {@link FluidTankInfo}.
     *
     * @param from Orientation determining which tanks should be queried.
     * @return Info for the relevant internal tanks.
     */
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { hot.getInfo(), cold.getInfo() };
    }
}
