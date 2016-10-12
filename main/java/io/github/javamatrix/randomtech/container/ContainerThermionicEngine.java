package io.github.javamatrix.randomtech.container;

import io.github.javamatrix.randomtech.tileentities.TileThermionicEngine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerThermionicEngine extends Container {
    TileThermionicEngine te;
    EntityPlayer player;

    public ContainerThermionicEngine(EntityPlayer who, TileThermionicEngine what) {
        player = who;
        te = what;

        // Add the player inventory
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlotToContainer(new Slot(player.inventory, k + i * 9
                        + 9, 8 + k * 18, 90 + i * 18));
            }
        }

        // And the quickbar
        for (int j = 0; j < 9; j++) {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18,
                                             148));
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
