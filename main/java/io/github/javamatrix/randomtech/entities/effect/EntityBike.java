package io.github.javamatrix.randomtech.entities.effect;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

// Currently back-burnered, but will return someday.
@SuppressWarnings("unused")
public class EntityBike extends EntityZombie {

    public EntityBike(World world, double x, double y,
                      double z) {
        super(world);
        posX = x;
        posY = y;
        posZ = z;
    }

    public EntityBike(World world) {
        super(world);
    }

    public EntityBike(World world, EntityPlayer player) {
        super(world);
        posX = player.posX;
        posY = player.posY;
        posZ = player.posZ;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
    }

}
