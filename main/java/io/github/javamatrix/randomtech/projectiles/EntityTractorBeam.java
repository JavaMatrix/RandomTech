package io.github.javamatrix.randomtech.projectiles;

import io.github.javamatrix.randomtech.items.ItemUnclipperGun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;

/**
 * Unclips entities from the world. Shot from the {@link ItemUnclipperGun}.
 *
 * @author JavaMatrix
 */
public class EntityTractorBeam extends EntityThrowable {

    double launcherX = 0;
    double launcherY = 0;
    double launcherZ = 0;
    boolean pull = false;

    public EntityTractorBeam(World world) {
        super(world);
        // Make it much faster.
        this.motionX *= 30;
        this.motionY *= 30;
        this.motionZ *= 30;
    }

    public EntityTractorBeam(World world, EntityLivingBase entity) {
        super(world, entity);
        // Make it much faster.
        this.motionX *= 30;
        this.motionY *= 30;
        this.motionZ *= 30;
        this.launcherX = entity.posX;
        this.launcherY = entity.posY + 1;
        this.launcherZ = entity.posZ;
        pull = entity.isSneaking();
    }

    public EntityTractorBeam(World world, double x, double y, double z) {
        super(world, x, y, z);
        // Make it much faster.
        this.motionX *= 30;
        this.motionY *= 30;
        this.motionZ *= 30;
        this.launcherX = x;
        this.launcherY = y + 1;
        this.launcherZ = z;
    }

    @Override
    public float getGravityVelocity() {
        // No gravity here.
        return 0;
    }

    protected void onImpact(MovingObjectPosition mop) {
        // We must hit an entity to unclip it.
        if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY
                && !worldObj.isRemote) {
            System.out.println("working");
            Vector3d vec = new Vector3d(mop.entityHit.posX - launcherX,
                                        mop.entityHit.posY - launcherY, mop.entityHit.posZ
                                                - launcherZ);
            vec.normalize();
            if (pull) {
                vec.negate();
            }
            vec.scale(0.5);
            vec.y += 0.1;
            mop.entityHit.motionX += vec.x;
            mop.entityHit.motionY += vec.y;
            mop.entityHit.motionZ += vec.z;
            System.out.println(vec.length());
        }

        // Set the projectile to dead.
        if (!worldObj.isRemote) {
            setDead();
        }
    }
}
