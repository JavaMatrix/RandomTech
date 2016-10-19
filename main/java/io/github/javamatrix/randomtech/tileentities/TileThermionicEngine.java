package io.github.javamatrix.randomtech.tileentities;

import cofh.api.energy.EnergyStorage;
import io.github.javamatrix.randomtech.util.WorldUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import static java.lang.Math.*;

/**
 * Created by JavaMatrix on 7/20/2015.
 * Licensed under Apache Commons 2.0.
 */
public class TileThermionicEngine extends TileEnergetic implements IFluidHandler {

    public static final int FLUID_AMOUNT = 24000;
    public static final double SECOND_ORDER_COEFFICIENT = 3.666666666E-5;
    public static final double FIRST_ORDER_COEFFICIENT = 3.333333333E-3;
    private static final int MAX_ENERGY_STORED = 400000;
    private static final double TEMPERATURE_TO_WORK_FACTOR = 0.001;
    public FluidTank hot = new FluidTank(FLUID_AMOUNT);
    public FluidTank cold = new FluidTank(FLUID_AMOUNT);

    public TileThermionicEngine() {
        super();
        hot.setCapacity(FLUID_AMOUNT);
        cold.setCapacity(FLUID_AMOUNT);
        storage = new EnergyStorage(MAX_ENERGY_STORED, 0, 240);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);

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
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Hot")) {
            hot = new FluidTank(hot.getFluid(), 0);
            hot.readFromNBT(nbt.getCompoundTag("Hot"));
        }
        if (nbt.hasKey("Cold")) {
            cold = new FluidTank(cold.getFluid(), 0);
            cold.readFromNBT(nbt.getCompoundTag("Cold"));
        }

        super.readCustomNBT(nbt);
    }

    @Override
    public void updateEntity() {
        if (hot.getFluid() != null && cold.getFluid() != null) {
            int temperatureDifference = hot.getFluid().getFluid().getTemperature() -
                    cold.getFluid().getFluid().getTemperature();
            int work = (int) (temperatureDifference * TEMPERATURE_TO_WORK_FACTOR);
            int generated = (int) (SECOND_ORDER_COEFFICIENT * temperatureDifference * temperatureDifference +
                    FIRST_ORDER_COEFFICIENT * temperatureDifference);
            generated = min(MAX_ENERGY_STORED - storage.getEnergyStored(), generated);

            // Work is both the amount of energy produced and the amount of each fluid (in mB) that is drained from the
            // internal tanks.
            work = max(work, 1);
            double mul = min(1, min((double) cold.getFluidAmount() / work,
                                    (double) hot.getFluidAmount() / work));
            generated = (int) (mul * generated);
            storage.setEnergyStored(min(storage.getEnergyStored() + generated, MAX_ENERGY_STORED));
            hot.drain((int) ceil(work * mul), true);
            cold.drain((int) ceil(work * mul), true);
        }

        if (hot.getFluidAmount() == 0) {
            hot.setFluid(null);
        }

        if (cold.getFluidAmount() == 0) {
            cold.setFluid(null);
        }

        WorldUtils.pushEnergy(worldObj, xCoord, yCoord, zCoord);
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
        if (hot.getFluid() != null && resource.getFluid().equals(hot.getFluid().getFluid()) ||
                (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() > 800)) {
            int amount = min(FLUID_AMOUNT - hot.getFluidAmount(), resource.amount);
            if (doFill) {
                hot.setFluid(new FluidStack(resource.getFluid(), hot.getFluidAmount() + amount));
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return amount;
        } else if (cold.getFluid() != null && resource.getFluid().equals(cold.getFluid().getFluid()) ||
                (cold.getFluidAmount() == 0 && resource.getFluid().getTemperature() <= 800)) {
            int amount = min(FLUID_AMOUNT - cold.getFluidAmount(), resource.amount);
            if (doFill) {
                cold.setFluid(new FluidStack(resource.getFluid(), cold.getFluidAmount() + amount));
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
            int amount = min(8000 - hot.getFluidAmount(), resource.amount);
            if (doDrain) {
                hot.setFluid(new FluidStack(hot.getFluid(), hot.getFluidAmount() + amount));
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return new FluidStack(hot.getFluid().getFluid(), amount);
        } else if (resource.getFluid().equals(cold.getFluid().getFluid()) || (hot.getFluidAmount() == 0 && resource.getFluid().getTemperature() <= 0)) {
            int amount = min(8000 - cold.getFluidAmount(), resource.amount);
            if (doDrain) {
                cold.setFluid(new FluidStack(cold.getFluid(), cold.getFluidAmount() + amount));
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return new FluidStack(cold.getFluid().getFluid(), amount);
        } else {
            return null;
        }
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p/>
     * This method is not Fluid-sensitive.
     * <p/>
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
     * <p/>
     * More formally, this should return true if fluid is able to enter from the given direction.
     *
     * @param from  The side to query fill possibility from.
     * @param fluid The fluid to query fill possibility about.
     */
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    /**
     * Returns true if the given fluid can be extracted from the given direction.
     * <p/>
     * More formally, this should return true if fluid is able to leave from the given direction.
     *
     * @param from  The side to query drain possibility from.
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
        return new FluidTankInfo[]{hot.getInfo(), cold.getInfo()};
    }
}
