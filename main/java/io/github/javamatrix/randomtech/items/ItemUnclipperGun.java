package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.projectiles.EntityUnclipperBullet;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Unclips entities from the world.  It's fun.
 *
 * @author JavaMatrix
 */
public class ItemUnclipperGun extends PoweredItem {

    public static final int FIRE_ENERGY = 20000;
    public static final int MAX_ENERGY_STORED = 2560000;
    public static final int MAX_RECEIVE = 2000;

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("Enemies tend to resist");
        lore.add("less when they don't");
        lore.add("collide with the ground.");
        lore.add("(Right-click to fire)");
        lore.add("\u00a7eCharge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // Don't shoot if it's out of energy.
        if (getEnergyStored(stack) <= FIRE_ENERGY) {
            return stack;
        }

        // Remove energy first.
        drainEnergy(stack, FIRE_ENERGY);

        // Shoot a bullet!
        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityUnclipperBullet(world, player));
        }

        return stack;
    }

    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY_STORED;
    }

    @Override
    public int getMaxReceive() {
        return MAX_RECEIVE;
    }
}
