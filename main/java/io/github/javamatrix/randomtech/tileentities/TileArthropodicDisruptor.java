package io.github.javamatrix.randomtech.tileentities;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileArthropodicDisruptor extends TileEnergetic {

    @Override
    public void updateEntity() {
        if (storage.getEnergyStored() >= 10) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord - 8,
                                                              yCoord - 16, zCoord - 8, xCoord + 8, yCoord + 16,
                                                              zCoord + 8);
            List<EntitySpider> spiders = worldObj.getEntitiesWithinAABB(
                    EntitySpider.class, aabb);
            for (EntitySpider es : spiders) {
                es.setBesideClimbableBlock(false);
                es.motionY = -Math.abs(es.motionY);
                storage.extractEnergy(10, false);
            }
        }
    }
}
