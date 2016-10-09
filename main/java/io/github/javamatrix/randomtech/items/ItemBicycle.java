package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.entities.effect.EntityBike;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

// Currently broken, but will return someday.
@SuppressWarnings("unused")
public class ItemBicycle extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world,
                                      EntityPlayer player) {
        if (!world.isRemote) {
            EntityBike bike = new EntityBike(world, player.posX, player.posY, player.posZ);
            bike.posX = player.posX + 1;
            bike.posX = player.posY;
            bike.posX = player.posZ;
            world.spawnEntityInWorld(bike);
        }
        stack.stackSize--;
        return stack;
    }
}
