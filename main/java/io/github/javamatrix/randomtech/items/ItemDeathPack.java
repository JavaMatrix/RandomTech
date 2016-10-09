package io.github.javamatrix.randomtech.items;

import cofh.api.energy.IEnergyContainerItem;
import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * Zaps nearby entities when worn by the player.
 *
 * @author JavaMatrix
 */
public class ItemDeathPack extends ItemArmor implements IEnergyContainerItem {

    public static final int MAX_ENERGY_STORED = 60000000;
    public static final int RF_PER_HEALTH = 15000;
    public static final int MAX_RECEIVE = 12000;
    /**
     * For debugging/future config purposes.
     */
    private static final boolean disableFeatures = false;

    public ItemDeathPack(ArmorMaterial armor) {
        super(armor, 1, 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean par4) {
        lore.add("This mighty weapon can be");
        lore.add("hooked to your back and");
        lore.add("will zap your enemies");
        lore.add("with lightning!");
        lore.add("\u00a7eCharge: " + TextUtils.formatRF(getEnergyStored(stack)));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot,
                                  String type) {
        return "randomtech:textures/model/armor/deathpack_layer_1.png";
    }

    @Override
    public int getDamage(ItemStack stack) {
        float powerRatio = 1 - ((float) getEnergyStored(stack) / (float) MAX_ENERGY_STORED);
        float damage = powerRatio * this.getMaxDamage();
        return (int) damage;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true;
    }


    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        float powerRatio = 1 - ((float) getEnergyStored(stack) / (float) MAX_ENERGY_STORED);
        return (double) powerRatio;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        // Don't do the features if we haven't been told to.
        if (disableFeatures) {
            return;
        }

        // Make a box around the player's position.
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX - 4,
                                                          player.posY - 4, player.posZ - 4, player.posX + 4,
                                                          player.posY + 4, player.posZ + 4);

        // List out the mobs and the wolves.
        List<EntityLiving> mobs = world.getEntitiesWithinAABB(EntityMob.class,
                                                              aabb);
        List<EntityWolf> wolves = world.getEntitiesWithinAABB(EntityWolf.class,
                                                              aabb);
        // For some reason, Slimes aren't recognized as monsters.
        mobs.addAll(world.getEntitiesWithinAABB(EntitySlime.class, aabb));

        // Kill all enemies in the radius with a health of less than 200 (to
        // exclude bosses).
        for (EntityLiving enemy : mobs) {
            if (enemy.getHealth() > 0 && !(enemy.getHealth() >= 200)) {
                murder(world, enemy, player, stack);
            }
        }

        // Kill the angry wolves too.
        for (EntityWolf wolf : wolves) {
            if (wolf.isAngry()) {
                if (wolf.getHealth() > 0) {
                    murder(world, wolf, player, stack);
                }
            }
        }
    }

    /**
     * Kills the entity using electricity from this pack.
     *
     * @param world  The world containing the player who wears this pack.
     * @param enemy  The enemy to murder.
     * @param player The player wearing this pack.
     * @param stack  The itemstack containing this DeathPack.
     */
    public void murder(World world, EntityLiving enemy, EntityPlayer player,
                       ItemStack stack) {
        // Don't do this if we're out of energy.
        if (this.getEnergyStored(stack) <= 0) {
            return;
        }

        // Determine the enemy's health.
        float damage = enemy.getHealth();
        // Kill the enemy, or do as much damage as possible.
        enemy.attackEntityFrom(DamageSource.inFire,
                               (this.getEnergyStored(stack)) / RF_PER_HEALTH);
        // Draw some lightning for 1/4 of a second.
        // EntityLightning lightning = new EntityLightning(world,
        // (float) player.posX, (float) player.posY, (float) player.posZ,
        // (float) enemy.posX, (float) enemy.posY, (float) enemy.posZ, 5);
        // world.spawnEntityInWorld(lightning);
        // Play dat ZAP sound!
        world.playSoundAtEntity(player, RandomTech.modid + ":zap",
                                1.0f + (float) Math.random(),
                                (float) Math.pow(2, Math.random()));

        // Damage the item if the player isn't in creative.
        if (!player.capabilities.isCreativeMode) {

            int power = (int) (damage) * RF_PER_HEALTH;
            drainEnergy(stack, power);
        }
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        int energyReceived = Math
                .min(MAX_ENERGY_STORED - getEnergyStored(container),
                     Math.min(maxReceive, MAX_RECEIVE));

        if (!simulate) {
            NBTTagCompound data = getNBT(container);
            data.setInteger("StoredEnergy", getEnergyStored(container)
                    + energyReceived);
            container.setTagCompound(data);
        }

        return energyReceived;
    }

    public NBTTagCompound getNBT(ItemStack stack) {
        NBTTagCompound data = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            data = stack.getTagCompound();
        }
        if (!data.hasKey("StoredEnergy")) {
            data.setInteger("StoredEnergy", MAX_ENERGY_STORED);
        }
        return data;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract,
                             boolean simulate) {
        // Can't extract energy from a DeathPack.
        return 0;
    }

    public int drainEnergy(ItemStack container, int toRemove) {
        int energyRemoved = Math.min(getEnergyStored(container), toRemove);
        NBTTagCompound tag = getNBT(container);
        tag.setInteger("StoredEnergy", getEnergyStored(container)
                - energyRemoved);
        container.setTagCompound(tag);
        return energyRemoved;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return getNBT(container).getInteger("StoredEnergy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return MAX_ENERGY_STORED;
    }
}
