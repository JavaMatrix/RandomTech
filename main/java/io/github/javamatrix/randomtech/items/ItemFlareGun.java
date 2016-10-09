package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.projectiles.EntityFlareRocket;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemFlareGun extends PoweredItem {
    public static final int FIRE_ENERGY = 2000;
    public static final int MAX_ENERGY_STORED = 256000;
    public static final int MAX_RECEIVE = 200;

    @SuppressWarnings({"unchecked"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("Shoots rockets that");
        lore.add("alert nearby enemies.");
        lore.add("Requires Fireworks to fire.");
        lore.add("(Right-click to fire)");
        lore.add("\u00a7eCharge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    public ItemStack onItemRightClick(ItemStack stack, World world,
                                      EntityPlayer player) {
        if (getEnergyStored(stack) >= FIRE_ENERGY) {
            if (player.inventory.hasItem(Items.fireworks)
                    || player.capabilities.isCreativeMode) {
                EntityFlareRocket rocket = new EntityFlareRocket(world, player);
                rocket.life = 3;

                if (!player.capabilities.isCreativeMode) {
                    player.inventory.consumeInventoryItem(Items.fireworks);
                    drainEnergy(stack, FIRE_ENERGY);
                }
                world.spawnEntityInWorld(rocket);
            }
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
