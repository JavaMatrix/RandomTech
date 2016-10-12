package io.github.javamatrix.randomtech.items;

import codechicken.core.fluid.FluidUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemBucketOfBurblingInfinity extends Item implements IFluidContainerItem {
    @Override
    public void onUpdate(ItemStack stack, World world, Entity holding,
                         int meta, boolean isAMystery) {
        stack.setItemDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        // The first two seconds out of every one thousand seconds.
        if (System.currentTimeMillis() % 100000 < 2000) {
            return StatCollector
                    .translateToLocal("item.bucketOfBurblingInfinity.2.name");
        }
        return StatCollector
                .translateToLocal("item.bucketOfBurblingInfinity.name");
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return FluidStack representing the fluid in the container, null if the container is empty.
     */
    @Override
    public FluidStack getFluid(ItemStack container) {
        return FluidUtils.water;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return Capacity of this fluid container.
     */
    @Override
    public int getCapacity(ItemStack container) {
        return Integer.MAX_VALUE;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param doFill    If false, the fill will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) filled into the
     * container.
     */
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param maxDrain  Maximum amount of fluid to be removed from the container.
     * @param doDrain   True to drain the fluid, false to simulate it.
     * @return Amount of fluid that was (or would have been, if simulated) drained from the
     * container.
     */
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return new FluidStack(FluidUtils.water, maxDrain);
    }
}
