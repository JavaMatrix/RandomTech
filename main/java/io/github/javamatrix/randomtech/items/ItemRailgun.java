package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.projectiles.EntityRailgunBolt;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemRailgun extends PoweredItem {
    public static final int FIRE_ENERGY = 2000;
    public static final int MAX_ENERGY_STORED = 256000;
    public static final int MAX_RECEIVE = 200;

    @SuppressWarnings({"unchecked"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("Cuts through armor");
        lore.add("like it's not even there.");
        lore.add("Requires Railgun Bolts to fire.");
        lore.add("(Right-click to fire)");
        lore.add("\u00a7eCharge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    public ItemStack onItemRightClick(ItemStack stack, World world,
                                      EntityPlayer player) {
        if (getEnergyStored(stack) >= FIRE_ENERGY) {
            if (player.inventory.hasItem(RandomTechItems.railgunBolt) || player.capabilities.isCreativeMode) {
                EntityRailgunBolt bolt = new EntityRailgunBolt(world, player);
                if (!player.capabilities.isCreativeMode) {
                    player.inventory.consumeInventoryItem(RandomTechItems.railgunBolt);
                    drainEnergy(stack, FIRE_ENERGY);
                }
                world.spawnEntityInWorld(bolt);
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
