package io.github.javamatrix.randomtech.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * A creative-only sword for admins, ops, and their kind.
 *
 * @author JavaMatrix
 */
public class ItemSwordOfMendragor extends ItemSword {

    public ItemSwordOfMendragor(ToolMaterial material) {
        super(material);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean sneaking) {
        // Add some fancay lorez.
        lore.add("\u00a74The sword of the mighty");
        lore.add("\u00a74mage \u00a7l\u00a76Mendragor\u00a7r\u00a74.");
        lore.add("\u00a74You can feel its");
        lore.add("\u00a7l\u00a71creative\u00a7r\u00a74 power surging");
        lore.add("\u00a74through your veins.");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,
                                     Entity target) {
        // First make them vulnerable.
        NBTTagCompound nbt = new NBTTagCompound();
        target.writeToNBT(nbt);
        nbt.setBoolean("Invulnerable", false);
        nbt.setInteger("Invul", 0);
        target.readFromNBT(nbt);
        // And do an impossibly large amount of damage.
        target.attackEntityFrom(DamageSource.causePlayerDamage(player),
                                Float.MAX_VALUE);
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        // Pulls it back like a bow.
        return EnumAction.bow;
    }

    @SuppressWarnings("unchecked")
    public void onPlayerStoppedUsing(ItemStack stack, World world,
                                     EntityPlayer entityPlayer, int time) {
        // Make a box around the player.
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
                entityPlayer.posX - 16, entityPlayer.posY - 16,
                entityPlayer.posZ - 16, entityPlayer.posX + 16,
                entityPlayer.posY + 16, entityPlayer.posZ + 16);

        // Get monsters in the radius.
        List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class,
                                                           aabb);

        // Kill 'em all!
        for (EntityMob target : mobs) {
            NBTTagCompound nbt = new NBTTagCompound();
            target.writeToNBT(nbt);
            nbt.setBoolean("Invulnerable", false);
            nbt.setInteger("Invul", 0);
            target.readFromNBT(nbt);
            target.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
        }
    }
}
