package io.github.javamatrix.randomtech.container;

import io.github.javamatrix.randomtech.tileentities.TileEnergetic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnergetic extends Container {
    TileEnergetic te;
    EntityPlayer player;

    public ContainerEnergetic(EntityPlayer who, TileEnergetic what) {
        player = who;
        te = what;

        // Add the player inventory
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlotToContainer(new Slot(player.inventory, k + i * 9
                        + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        // And the quickbar
        for (int j = 0; j < 9; j++) {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18,
                                             142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) <= 64.0;
    }

    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        return null;
    }
}
