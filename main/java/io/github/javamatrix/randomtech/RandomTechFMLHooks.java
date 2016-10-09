package io.github.javamatrix.randomtech;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.IHardenedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RandomTechFMLHooks {

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent ev) {
        if (ev.crafting.getItem() instanceof IHardenedItem) {
            int durability = 0;
            for (int i = 0; i < ev.craftMatrix.getSizeInventory(); i++) {
                if (ev.craftMatrix.getStackInSlot(i) != null) {
                    ItemStack slot = ev.craftMatrix.getStackInSlot(i);
                    int hardness = slot.getItemDamage();
                    slot.setItemDamage(0);
                    if (slot.getItem().equals(
                            RandomTechItems.hardenedMetalIngot)
                            || slot.getItem().equals(
                            RandomTechItems.hardenedMagicalIngot)) {
                        durability += hardness;
                    }
                }
            }
            // ev.crafting.setItemDamage(durability);
            NBTTagCompound nbt = new NBTTagCompound();
            if (ev.crafting.hasTagCompound()) {
                nbt = ev.crafting.getTagCompound();
            }
            nbt.setInteger("maxDurability", durability);
            ev.crafting.setTagCompound(nbt);
        }
    }
}
