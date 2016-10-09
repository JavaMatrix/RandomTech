package io.github.javamatrix.randomtech.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlareRocket extends EntityThrowable {

    public int life = 60;

    public EntityFlareRocket(World world) {
        super(world);
        // Make it faster.
        this.motionX *= 1.5;
        this.motionY *= 1.5;
        this.motionZ *= 1.5;
    }

    public EntityFlareRocket(World world, EntityLivingBase entity) {
        super(world, entity);
        // Make it faster.
        this.motionX *= 1.5;
        this.motionY *= 1.5;
        this.motionZ *= 1.5;
    }

    public EntityFlareRocket(World world, double x, double y, double z) {
        super(world, x, y, z);
        // Make it faster.
        this.motionX *= 1.5;
        this.motionY *= 1.5;
        this.motionZ *= 1.5;
    }

    @Override
    public float getGravityVelocity() {
        // No gravity here.
        return 0;
    }

    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            life--;
            System.out.println(life);
        }
        if (life < 0) {
            if (!worldObj.isRemote) {
                worldObj.createExplosion(this, posX, posY, posZ, 3, false);
                alertEntities(findClosestPlayer());
                this.kill();
            }
        }
    }

    private EntityPlayer findClosestPlayer() {
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(posX - 20, posY - 20,
                                                          posZ - 20, posX - 20, posY - 20, posZ - 20);
        List<EntityPlayer> playersInArea = worldObj.getEntitiesWithinAABB(
                EntityPlayer.class, aabb);

        EntityPlayer closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (EntityPlayer candidate : playersInArea) {
            double dist = candidate.getDistance(posX, posY, posZ);
            if (dist < closestDistance) {
                closest = candidate;
                closestDistance = dist;
            }
        }

        return closest;
    }

    private void alertEntities(Entity who) {

        if (who == null) {
            return;
        }

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(posX - 20, posY - 20,
                                                          posZ - 20, posX - 20, posY - 20, posZ - 20);
        List<EntityMob> mobsInArea = worldObj.getEntitiesWithinAABB(
                EntityMob.class, aabb);

        for (EntityMob mob : mobsInArea) {
            mob.setTarget(who);
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        life = 0;
    }

}
