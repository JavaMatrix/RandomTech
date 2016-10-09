package io.github.javamatrix.randomtech.items;

import cpw.mods.fml.relauncher.ReflectionHelper;
import io.github.javamatrix.randomtech.util.PowerCards;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

public class ItemGalatine extends ItemSword implements IEmpowerable {

    Random galRand = new Random();

    public ItemGalatine(Item.ToolMaterial material) {
        super(material);
    }

    private double getMaxXP(int currentLevel) {
        return 100000.0 * Math.pow(1.1, currentLevel);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore,
                               boolean f3h) {
        lore.add("Completely unbreakable.");
        lore.add("Empowerable in an Empowerment Table.");

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        if (!nbt.hasKey("level")) {
            nbt.setInteger("level", 1);
        }
        if (!nbt.hasKey("xp")) {
            nbt.setDouble("xp", 0);
        }

        stack.setTagCompound(nbt);

        int level = stack.getTagCompound().getInteger("level");
        double xp = stack.getTagCompound().getDouble("xp");
        double maxXP = getMaxXP(level);
        String levelStr;
        if (level == 6) {
            levelStr = EnumChatFormatting.AQUA + "Max Rank";
        } else {
            double percent = (100.0 * xp / maxXP);
            int percentInt = (int) (100 * percent);
            percent = percentInt / 100.0;
            levelStr = EnumChatFormatting.AQUA + "Rank " + level + " (" + percent + "% to ";
            if (level == 5) {
                levelStr += "Max Rank";
            } else {
                levelStr += "Rank " + (level + 1);
            }
            levelStr += ")";
        }
        lore.add(levelStr);

        if (GuiScreen.isShiftKeyDown()) {
            for (String[] cardData : getInstalledCards(stack)) {
                lore.add(EnumChatFormatting.RESET + ""
                                 + EnumChatFormatting.AQUA + cardData[0]
                                 + EnumChatFormatting.RESET + " [Rank " + cardData[1]
                                 + "]");
            }
        } else {
            lore.add(EnumChatFormatting.AQUA
                             + "Hold Shift for Installed Powers.");
        }

        if (GuiScreen.isCtrlKeyDown()) {
            float damage = 5.0f * (1.0f + (PowerCards.getAmount(
                    "Increased Vorpality",
                    getPCRank(stack, "Increased Vorpality")) / 100.0f));
            lore.add(EnumChatFormatting.BLUE + "+5.0 Base Damage");
            if (hasPC(stack, "Increased Vorpality")) {
                lore.add(EnumChatFormatting.BLUE
                                 + String.format("+%.1f Bonus Damage", damage - 5.0f));
            }

            if (hasPC(stack, "Slice and Dice")) {
                float slashDmg = damage
                        * (PowerCards.getAmount("Slice and Dice",
                                                getPCRank(stack, "Slice and Dice")) / 100.0f);
                lore.add(EnumChatFormatting.BLUE
                                 + String.format("+%.1f Slash Damage", slashDmg));

                damage += slashDmg;
            }

            float critChance = 1.0f;
            if (hasPC(stack, "Pedelepus")) {
                critChance += PowerCards.getAmount("Pedelepus",
                                                   getPCRank(stack, "Pedelepus"));
            }
            float critDmg = damage * 0.5f;
            if (hasPC(stack, "Acupuncture")) {
                critDmg *= 1.0 + PowerCards.getAmount("Acupuncture",
                                                      getPCRank(stack, "Acupuncture")) / 100.0f;
            }
            lore.add(EnumChatFormatting.BLUE
                             + String.format("%.1f%% Chance for %.1f extra damage.",
                                             critChance, critDmg));
            if (hasPC(stack, "Smash")) {
                float aoeDmg = damage
                        * PowerCards.getAmount("Smash",
                                               getPCRank(stack, "Smash")) / 100.0f;
                lore.add(EnumChatFormatting.BLUE
                                 + String.format("+%.1f AoE Damage", aoeDmg));
            }
            if (hasPC(stack, "Aurora's Wrath")) {
                float dawnDamage = damage
                        * PowerCards.getAmount("Aurora's Wrath",
                                               getPCRank(stack, "Aurora's Wrath")) / 100.0f;

                lore.add(EnumChatFormatting.BLUE
                                 + String.format("+%.1f Damage at Dawn", dawnDamage));
            }
            if (hasPC(stack, "Power of Neptune")) {
                float dawnDamage = damage
                        * PowerCards.getAmount("Power of Neptune",
                                               getPCRank(stack, "Power of Neptune")) / 100.0f;

                lore.add(EnumChatFormatting.BLUE
                                 + String.format("+%.1f Damage in Ocean-type Biomes",
                                                 dawnDamage));
            }
        } else {
            lore.add(EnumChatFormatting.BLUE
                             + "Hold Ctrl for Damage Breakdown.");
        }

    }

    @Override
    public String[][] getInstalledCards(ItemStack stack) {
        if (!stack.hasTagCompound()
                || !stack.getTagCompound().hasKey("installed")) {
            return new String[0][0];
        }
        // 10 = NBTTagCompound.
        NBTTagList installedCardsNBT = stack.getTagCompound().getTagList(
                "installed", 10);

        // Dimension 1 is the array of cards, dimension two is the separate data
        // pieces.
        String[][] cards = new String[installedCardsNBT.tagCount()][2];

        // Simply read the nbt into the structure.
        for (int i = 0; i < installedCardsNBT.tagCount(); i++) {
            NBTTagCompound cmpd = installedCardsNBT.getCompoundTagAt(i);
            cards[i] = new String[]{cmpd.getString("name"),
                    Integer.toString(cmpd.getInteger("rank"))};
        }
        return cards;
    }

    @Override
    public boolean canAddPC(ItemStack stack, String cardName, int cardRank) {
        // Load up the cards.
        String[][] installedCards = getInstalledCards(stack);

        // First, check that there's room.
        if (installedCards.length >= 9) {
            return false;
        }

        // Now just loop through checking for the card name - cards are uniquely
        // identified by name.
        for (int i = 0; i < installedCards.length; i++) {
            if (installedCards[i][0].equals(cardName)) {
                return false;
            }
        }

        // Since none has been found, the card can be added.
        return true;
    }

    @Override
    public boolean addPC(ItemStack stack, String cardName, int cardRank) {
        // First check that we can add the card.
        if (!canAddPC(stack, cardName, cardRank)) {
            return false;
        }

        // Next, load up the NBTTagList.
        NBTTagCompound stackNBT = new NBTTagCompound();

        if (stack.hasTagCompound()) {
            stackNBT = stack.getTagCompound();
        }

        // 10 = NBTTagCompound.
        NBTTagList installedCardsNBT = new NBTTagList();

        if (stackNBT.hasKey("installed")) {
            installedCardsNBT = stackNBT.getTagList("installed", 10);
        }

        // Now, create the compound tag for the item.
        NBTTagCompound cardNBT = new NBTTagCompound();
        cardNBT.setString("name", cardName);
        cardNBT.setInteger("rank", cardRank);

        // Add the card to the installed list.
        installedCardsNBT.appendTag(cardNBT);

        // Apply the list to the data.
        stackNBT.setTag("installed", installedCardsNBT);

        // And apply the data to the stack.
        stack.setTagCompound(stackNBT);

        return true;
    }

    @Override
    public boolean hasPC(ItemStack stack, String cardName) {
        // Just a basic search.
        for (String[] cardData : getInstalledCards(stack)) {
            if (cardData[0].equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    private int getPCRank(ItemStack stack, String cardName) {
        for (String[] cardData : getInstalledCards(stack)) {
            if (cardData[0].equals(cardName)) {
                return Integer.parseInt(cardData[1]);
            }
        }

        return -1;
    }

    @Override
    public boolean removePC(ItemStack stack, String cardName, int cardRank) {
        // First check that we can remove the card (i.e. it's there).
        if (!hasPC(stack, cardName)) {
            return false;
        }

        // Next, load up the NBTTagList.
        NBTTagCompound stackNBT = new NBTTagCompound();

        if (stack.hasTagCompound()) {
            stackNBT = stack.getTagCompound();
        }

        // 10 = NBTTagCompound.
        NBTTagList installedCardsNBT = new NBTTagList();

        if (stackNBT.hasKey("installed")) {
            installedCardsNBT = stackNBT.getTagList("installed", 10);
        }

        // Now parse through until we find the card we want.
        for (int i = 0; i < installedCardsNBT.tagCount(); i++) {
            if (installedCardsNBT.getCompoundTagAt(i).hasKey("name")
                    && installedCardsNBT.getCompoundTagAt(i).getString("name")
                    .equals(cardName)) {
                // And now delete it.
                installedCardsNBT.removeTag(i);
            }
        }

        // Apply the edited list to the data.
        stackNBT.setTag("installed", installedCardsNBT);

        // And apply the data to the stack.
        stack.setTagCompound(stackNBT);

        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,
                                     Entity entity) {

        if (!(entity instanceof EntityLiving)) {
            return false;
        }

        EntityLiving enemy = (EntityLiving) entity;

        // Base damage.
        float damage = 5.0f;

        if (hasPC(stack, "Slice and Dice") && entity instanceof EntityZombie) {
            float slicediceModifier = 1.0f + (PowerCards.getAmount(
                    "Slice and Dice", getPCRank(stack, "Slice and Dice")) / 100.0f);
            damage *= slicediceModifier;
        }

        if (hasPC(stack, "Balanced Grip")) {
            // Adding negative exhaustion undoes some of the exhaustion cost of
            // the action.
            player.addExhaustion(-0.3f
                                         * (getPCRank(stack, "Balanced Grip") / 100.0f));
        }

        // Increased Vorpality
        if (hasPC(stack, "Increased Vorpality")) {
            damage *= 1.0f + (PowerCards.getAmount("Increased Vorpality",
                                                   getPCRank(stack, "Increased Vorpality")) / 100.0f);
        }

        long time = player.worldObj.getWorldTime();
        time %= 24000;

        if (hasPC(stack, "Aurora's Wrath") && (time > 23000 || time < 1000)) {
            damage *= 1.0 + (PowerCards.getAmount("Aurora's Wrath",
                                                  getPCRank(stack, "Aurora's Wrath")) / 100.0f);
        }

        BiomeGenBase playerBiome = player.worldObj.getBiomeGenForCoords(
                (int) player.posX, (int) player.posZ);

        if (hasPC(stack, "Power of Neptune")
                && BiomeDictionary.isBiomeOfType(playerBiome,
                                                 BiomeDictionary.Type.OCEAN)) {
            damage *= 1.0 + (PowerCards.getAmount("Power of Neptune",
                                                  getPCRank(stack, "Power of Neptune")) / 100.0f);
        }

        // Random crits
        int critChance = 1;
        if (hasPC(stack, "Pedelepus")) {
            critChance += PowerCards.getAmount("Pedelepus",
                                               getPCRank(stack, "Pedelepus"));
        }

        if (galRand.nextInt(100) <= critChance - 1) {
            damage *= 1.5f;
            if (hasPC(stack, "Acupuncture")) {
                damage *= 1.0 + PowerCards.getAmount("Acupuncture",
                                                     getPCRank(stack, "Acupuncture")) / 100.0f;
            }
        }

        // Knockback
        if (hasPC(stack, "Excessive Force")) {
            knockback(
                    enemy,
                    player,
                    PowerCards.getAmount("Excessive Force",
                                         getPCRank(stack, "Excessive Force")));
        }

        if (hasPC(stack, "Volcanic Glaze")) {
            enemy.setFire((int) PowerCards.getAmount("Volcanic Glaze",
                                                     getPCRank(stack, "Volcanic Glaze")));
        }

        if (hasPC(stack, "Arlon's Touch")) {
            if (galRand.nextDouble() < (PowerCards.getAmount("Arlon's Touch",
                                                             getPCRank(stack, "Arlon's Touch")) / 100.0f)) {
                enemy.addPotionEffect(new PotionEffect(Potion.wither.id, 60, 0,
                                                       false));
            }
        }

        if (enemy instanceof EntityCreeper && hasPC(stack, "Defuser")) {
            if (galRand.nextInt(100) <= PowerCards.getAmount("Defuser",
                                                             getPCRank(stack, "Defuser")) - 1) {
                // 1 == timeSinceIgnited
                ReflectionHelper.setPrivateValue(EntityCreeper.class,
                                                 (EntityCreeper) enemy, 0, 1);
            }
        }

        // AoE with Smash
        if (hasPC(stack, "Smash")) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(enemy.posX - 2,
                                                              enemy.posY - 2, enemy.posZ - 2, enemy.posX + 2,
                                                              enemy.posY + 2, enemy.posZ + 2);
            List<EntityMob> mobs = enemy.worldObj.getEntitiesWithinAABB(
                    EntityMob.class, aabb);
            for (EntityCreature mob : mobs) {
                mob.attackEntityFrom(
                        DamageSource.causePlayerDamage(player),
                        Math.round(damage
                                           * PowerCards.getAmount("Smash",
                                                                  getPCRank(stack, "Smash")) / 100.0f));
            }
        }

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        if (!nbt.hasKey("level")) {
            nbt.setInteger("level", 1);
        }
        if (!nbt.hasKey("xp")) {
            nbt.setDouble("xp", 0);
        }
        stack.setTagCompound(nbt);

        int level = nbt.getInteger("level");
        double xp = nbt.getDouble("xp");
        xp += damage + Math.random();
        if (xp >= getMaxXP(level) && level != 6) {
            level++;
            xp = 0;
        }
        nbt.setInteger("level", level);
        nbt.setDouble("xp", xp);

        enemy.attackEntityFrom(DamageSource.causePlayerDamage(player),
                               Math.round(damage));

        if (hasPC(stack, "Siphon") && player.isSprinting()) {
            float heal = damage
                    * PowerCards
                    .getAmount("Siphon", getPCRank(stack, "Siphon"))
                    / 100.0f;
            player.heal(heal);
        }

        if (hasPC(stack, "Piercing Strikes")) {
            enemy.hurtResistantTime -= PowerCards.getAmount("Piercing Strikes",
                                                            getPCRank(stack, "Piercing Strikes"));
        }

        if (player.isSprinting()) {
            player.motionX *= 0.6;
            player.motionZ *= 0.6;
            player.setSprinting(false);
        }

        return false;
    }

    private void knockback(EntityLivingBase target, EntityPlayer user,
                           float amount) {
        if (user.isSprinting()) {
            amount++;
        }
        target.addVelocity(
                (double) (-MathHelper.sin(user.rotationYaw * (float) Math.PI
                                                  / 180.0F)
                        * amount * 0.5F),
                0.1D,
                (double) (MathHelper.cos(user.rotationYaw * (float) Math.PI
                                                 / 180.0F)
                        * amount * 0.5F));
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (hasPC(stack, "Reach") && entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(
                    player.worldObj, (EntityPlayer) entityLiving, false);
            double reach = player.theItemInWorldManager.getBlockReachDistance();
            double ext_reach = reach
                    + PowerCards.getAmount("Reach", getPCRank(stack, "Reach"));
            if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY) {
                if (mop.hitVec.lengthVector() >= reach
                        && mop.hitVec.lengthVector() <= ext_reach) {
                    this.onLeftClickEntity(stack, player, mop.entityHit);
                }
            }
        }
        return false;
    }
}
