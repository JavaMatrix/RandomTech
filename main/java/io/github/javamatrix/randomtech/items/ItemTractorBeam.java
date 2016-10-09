package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.projectiles.EntityTractorBeam;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemTractorBeam extends PoweredItem {
    public static final int FIRE_ENERGY = 2000;

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("Never leave spacedock");
        lore.add("without one.");
        lore.add("(Right-click to fire)");
        lore.add("\u00a7eCharge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity whodunbeholding,
                         int meta, boolean isInHand) {
        if (!world.isRemote) {
            NBTTagCompound nbt = new NBTTagCompound();
            if (stack.hasTagCompound()) {
                nbt = stack.getTagCompound();
            }
            if (!nbt.hasKey("Cooldown")) {
                nbt.setInteger("Cooldown", 0);
            }
            nbt.setInteger("Cooldown", nbt.getInteger("Compound") - 1);
            stack.setTagCompound(nbt);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world,
                                      EntityPlayer player) {

        // Don't shoot if it's out of energy or we're still cooling down.
        if ((getEnergyStored(stack) <= FIRE_ENERGY && !player.capabilities.isCreativeMode)
                || world.isRemote) {
            return stack;
        }

        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        if (!nbt.hasKey("Cooldown")) {
            nbt.setInteger("Cooldown", 0);
        }

        if (nbt.getInteger("Cooldown") > 0) {
            return stack;
        }

        // Remove energy first.
        if (!player.capabilities.isCreativeMode) {
            drainEnergy(stack, FIRE_ENERGY);
        }

        // Shoot a bullet!
        world.spawnEntityInWorld(new EntityTractorBeam(world, player));

        if (player.isSneaking()) {
            world.playSoundAtEntity(player, RandomTech.modid + ":tractor_beam_pull",
                                    1.0f, 1.0f);
        } else {
            world.playSoundAtEntity(player, RandomTech.modid + ":tractor_beam",
                                    1.0f, 1.0f);
        }

        // Set the cooldown.
        nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        }
        nbt.setInteger("Cooldown", 20);
        stack.setTagCompound(nbt);

        return stack;
    }

    @Override
    public int getMaxEnergy() {
        return 200000;
    }

    @Override
    public int getMaxReceive() {
        return 200;
    }
}
