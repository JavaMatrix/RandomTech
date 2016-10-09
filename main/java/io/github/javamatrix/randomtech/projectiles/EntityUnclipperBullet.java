package io.github.javamatrix.randomtech.projectiles;

import io.github.javamatrix.randomtech.items.ItemUnclipperGun;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

/**
 * Unclips entities from the world. Shot from the {@link ItemUnclipperGun}.
 *
 * @author JavaMatrix
 */
public class EntityUnclipperBullet extends EntityThrowable {

    public EntityUnclipperBullet(World world) {
        super(world);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public EntityUnclipperBullet(World world, EntityLivingBase entity) {
        super(world, entity);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public EntityUnclipperBullet(World world, double x, double y, double z) {
        super(world, x, y, z);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    @Override
    public float getGravityVelocity() {
        // No gravity here.
        return 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        // Spawn a pretty particle trail.
        for (int i = 0; i < 40; ++i) {
            this.worldObj.spawnParticle("magicCrit", this.posX + this.motionX
                                                * (double) i / 40.0D, this.posY + this.motionY * (double) i
                                                / 40.0D, this.posZ + this.motionZ * (double) i / 40.0D, 0,
                                        0, 0);
        }
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    protected void onImpact(MovingObjectPosition mop) {
        // We must hit an entity to unclip it.
        if (mop.typeOfHit == MovingObjectType.ENTITY) {
            // Get the entity we hit.
            Entity hit = mop.entityHit;
            // Has to be a creature.
            if (hit instanceof EntityCreature) {
                // Can't be a player.
                if (!(hit instanceof EntityPlayer)) {
                    // Give it no-clip xD
                    hit.noClip = true;
                    // Set the speed to 0 so it can't climb out of the earth.
                    ((EntityCreature) hit)
                            .getAttributeMap()
                            .getAttributeInstance(
                                    SharedMonsterAttributes.movementSpeed)
                            .setBaseValue(0.0);
                    // Turn off Entity tasks that may negatively impact the
                    // effect.
                    ((EntityCreature) hit).tasks.taskEntries.clear();
                    ((EntityCreature) hit).targetTasks.taskEntries.clear();
                    // Make it peaceful towards the player so spiders don't
                    // climb out and things like that.
                    ((EntityCreature) hit).setTarget(null);
                    // Spawn some hit particles.
                    for (int l = 0; l < 4; ++l) {
                        this.worldObj.spawnParticle("crit", this.posX,
                                                    this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }

        // Set the projectile to dead.
        if (!worldObj.isRemote) {
            setDead();
        }
    }
}
