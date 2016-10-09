package io.github.javamatrix.randomtech.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityRailgunBolt extends EntityThrowable {

    public static final float DAMAGE = 7.0f;

    public EntityRailgunBolt(World world) {
        super(world);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public EntityRailgunBolt(World world, EntityLivingBase entity) {
        super(world, entity);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public EntityRailgunBolt(World world, double x, double y, double z) {
        super(world, x, y, z);
        // Make it faster.
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        // We must hit an entity to unclip it.
        if (mop.typeOfHit == MovingObjectType.ENTITY) {
            // Get the entity we hit.
            Entity hit = mop.entityHit;
            // Has to be a creature.
            if (hit instanceof EntityCreature) {
                // Get its health.
                EntityCreature ec = (EntityCreature) hit;
                float health = ec.getHealth();
                // Add the damage.
                health -= DAMAGE;
                // And re-apply it to the entity. This process will bypass armor.
                ec.setHealth(health);

            }
        }

        // Set the projectile to dead.
        if (!worldObj.isRemote) {
            setDead();
        }
    }
}
