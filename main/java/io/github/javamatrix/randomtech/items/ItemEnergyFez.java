package io.github.javamatrix.randomtech.items;

import cofh.api.energy.IEnergyContainerItem;
import io.github.javamatrix.randomtech.proxy.ClientProxy;
import io.github.javamatrix.randomtech.util.PlayerUtils;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEnergyFez extends ItemArmor implements IEnergyContainerItem {

    public static final int MAX_ENERGY = 200000;
    public static final int MAX_SEND_RECEIVE = 200;

    public ItemEnergyFez(ArmorMaterial material) {
        super(material, 1, 0);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("This hat was popular in");
        lore.add("ancient times but was");
        lore.add("abandoned for unknown");
        lore.add("reasons.");
        lore.add(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Charges items in your inventory.");
        lore.add(EnumChatFormatting.YELLOW + "Charge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot,
                                  String type) {
        return "randomtech:textures/model/armor/energy_fez_layer_1.png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (world.isRemote) {
            return;
        }

        receiveEnergy(stack, 5, false);

        ItemStack heldItem = PlayerUtils.getHolding(player);
        if (heldItem != null
                && heldItem.getItem() instanceof IEnergyContainerItem) {
            IEnergyContainerItem toCharge = (IEnergyContainerItem) heldItem
                    .getItem();
            int energyToSend = Math.min(getMaxReceive(),
                                        toCharge.receiveEnergy(heldItem, Integer.MAX_VALUE, true));
            energyToSend = Math.min(energyToSend, getEnergyStored(stack));
            extractEnergy(stack, energyToSend, false);
            toCharge.receiveEnergy(heldItem, energyToSend, false);
        }
    }

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

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving,
                                    ItemStack itemStack, int armorSlot) {
        return ClientProxy.getEnergyFezModel();
    }

    private int getMaxReceive() {

        return MAX_SEND_RECEIVE;
    }

    private int getMaxEnergy() {
        return MAX_ENERGY;
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
        int energyRemoved = Math.min(getMaxReceive(), maxExtract);

        if (!simulate) {
            NBTTagCompound tag = getNBT(container);
            tag.setInteger("StoredEnergy", getEnergyStored(container)
                    - energyRemoved);
        }

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
