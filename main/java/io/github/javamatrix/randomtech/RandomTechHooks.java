package io.github.javamatrix.randomtech;

import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.ItemHammer;
import io.github.javamatrix.randomtech.items.ItemSuperFuel;
import io.github.javamatrix.randomtech.tileentities.TileEnergetic;
import io.github.javamatrix.randomtech.util.PlayerUtils;
import io.github.javamatrix.randomtech.util.PowerCards;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Listens for various events within Minecraft and implements custom behaviors
 * when they happen.
 *
 * @author JavaMatrix
 */
public class RandomTechHooks implements IFuelHandler, IWorldGenerator {

    public static final long WHACK_DELAY = 1000;
    public static Random rand = new Random();
    public long lastWhackTime = 0;
    List<Entity> striders = new ArrayList<Entity>();

    /**
     * Used to cancel "whack" events while holding the Sword of Mendragor.
     *
     * @param e The event being handled.
     */
    @SubscribeEvent
    public void whackEvent(PlayerInteractEvent e) {
        if (e.action == Action.LEFT_CLICK_BLOCK) {
            if (PlayerUtils.isHolding(e.entityPlayer,
                                      RandomTechItems.swordOfMendragor)) {
                if (e.isCancelable()) {
                    e.setCanceled(true);
                }
            } else if (PlayerUtils.getHolding(e.entityPlayer) != null
                    && PlayerUtils.getHolding(e.entityPlayer).getItem() instanceof ItemHammer
                    && !(e.entityPlayer instanceof FakePlayer)) {
                TileEntity te = e.world.getTileEntity(e.x, e.y, e.z);
                if (te != null
                        && te instanceof IEnergyHandler
                        && (System.currentTimeMillis() - lastWhackTime) > WHACK_DELAY) {
                    ForgeDirection dirFrom = ForgeDirection.UNKNOWN;
                    if (!e.world.isRemote) {
                        ((IEnergyHandler) te).receiveEnergy(dirFrom,
                                                            rand.nextInt(40), false);
                        te.markDirty();
                    }
                    e.world.playSoundAtEntity(e.entityPlayer, RandomTech.modid
                            + ":forge", 1.0f + (float) Math.random(), 1.0f);
                    lastWhackTime = System.currentTimeMillis();
                    if (!e.entityPlayer.capabilities.isCreativeMode) {
                        // Like using it 5-10 times for legitimate purposes.
                        e.entityPlayer.getHeldItem().damageItem(
                                rand.nextInt(5) + 5, e.entityPlayer);
                    }
                }

                // Prevents the ghost hammer bug.
                if (e.entityPlayer.getHeldItem().stackSize == 0) {
                    e.entityPlayer.inventory.setInventorySlotContents(
                            e.entityPlayer.inventory.currentItem, null);
                }
            } else if (PlayerUtils.isHolding(e.entityPlayer,
                                             RandomTechItems.itemDebugger)
                    && e.entityPlayer.isSneaking()) {

                TileEntity te = e.world.getTileEntity(e.x, e.y, e.z);
                if (te != null
                        && te instanceof TileEnergetic
                        && (System.currentTimeMillis() - lastWhackTime) > WHACK_DELAY) {
                    ((TileEnergetic) te).setEnergyStored(((TileEnergetic) te)
                                                                 .getMaxEnergyStored(ForgeDirection.UNKNOWN));
                    e.world.markBlockForUpdate(e.x, e.y, e.z);
                    te.markDirty();
                }
            }
        }
    }

    /**
     * Used to change vanilla drops when using the chunking pick.
     *
     * @param e
     */
    @SubscribeEvent
    public void onHarvested(HarvestDropsEvent e) {
        if (PlayerUtils.isHolding(e.harvester, RandomTechItems.chunkingPick)) {
            if (isSelfDroppingOre(e)) {
                ItemStack oldDrop = e.drops.get(0);
                ItemStack smeltingResult = FurnaceRecipes.smelting()
                        .getSmeltingResult(oldDrop);
                if (smeltingResult != null) {
                    smeltingResult.stackSize = fortuneMath(EnchantmentHelper
                                                                   .getFortuneModifier(e.harvester));
                    e.drops.remove(oldDrop);
                    e.drops.add(smeltingResult);
                    e.dropChance = 1.0F;
                }
            } else {
                for (ItemStack oldDrop : e.drops) {
                    ItemStack smeltingResult = FurnaceRecipes
                            .smelting()
                            .getSmeltingResult(
                                    new ItemStack(
                                            e.block.getItemDropped(
                                                    e.blockMetadata,
                                                    new Random(),
                                                    EnchantmentHelper
                                                            .getFortuneModifier(e.harvester)),
                                            e.block.quantityDropped(new Random()),
                                            e.block.damageDropped(e.blockMetadata)));
                    if (smeltingResult != null) {
                        smeltingResult.stackSize = oldDrop.stackSize;
                        e.drops.clear();
                        e.drops.add(smeltingResult);
                        e.dropChance = 1.0F;
                    } else {
                        e.drops.clear();
                        e.drops.add(oldDrop);
                        e.dropChance = 1.0F;
                    }
                }
            }
        }
    }

    private int fortuneMath(int fortune) {
        Random r = new Random(System.currentTimeMillis());
        int j = r.nextInt(fortune + 2) - 1;

        if (j < 0) {
            j = 0;
        }

        return (j + 1);
    }

    public boolean isSelfDroppingOre(HarvestDropsEvent e) {
        if (e.drops.size() == 1) {
            // Check if the block is dropping itself.
            if (e.drops.get(0).getItem().equals(Item.getItemFromBlock(e.block))) {
                int[] ore_ids = OreDictionary.getOreIDs(e.drops.get(0));
                if (ore_ids.length != 0) {
                    for (int i : ore_ids) {
                        if (OreDictionary.getOreName(i).startsWith("ore")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Called when a tooltip for an item is created.
     *
     * @param ev The event related to the tooltip.
     */
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent ev) {
        if (ev.itemStack.getItem().equals(RandomTechItems.enthalpite)) {
            ev.toolTip.add("How can a crystal be sinister? And");
            ev.toolTip.add("yet, this one is.");
        } else if (ev.itemStack.getItem().equals(
                RandomTechItems.enthalpiteShard)) {
            ev.toolTip.add("Only a tiny bit evil, but it emanates");
            ev.toolTip.add("a desire to be united with others like");
            ev.toolTip.add("it. But is that really a good idea?");
        } else if (ev.itemStack.getItem().equals(RandomTechItems.itemDebugger)) {
            ev.toolTip.add("Don't look directly at the bugs!");
        } else if (ev.itemStack.getItem().equals(
                Item.getItemFromBlock(RandomTechBlocks.smithy))) {
            ev.toolTip.add("Powered by fire or by lava.");
            ev.toolTip.add("Right-click me with ore, and hit");
            ev.toolTip.add("me with a hammer.");
        } else if (ev.itemStack.getItem().equals(
                Item.getItemFromBlock(RandomTechBlocks.nulliumOre))) {
            ev.toolTip.add("It seems unaffected by the");
            ev.toolTip.add("heat of a furnace. Perhaps a");
            ev.toolTip.add("more advanced form of smithing");
            ev.toolTip.add("is necessary.");
        } else if (ev.itemStack.getItem().equals(RandomTechItems.galatine)) {
            int delpos = -1;
            for (int i = 0; i < ev.toolTip.size(); i++) {
                if (ev.toolTip.get(i).contains("c-1")) {
                    delpos = i;
                }
            }
            if (delpos != -1) {
                // Get the blank line above it, too.
                ev.toolTip.remove(delpos - 1);
                // Which shifts the line, so...
                ev.toolTip.remove(delpos - 1);
            }
        } else if (ev.itemStack.getItem().equals(
                Item.getItemFromBlock(RandomTechBlocks.arthropodicDisruptor))) {
            ev.toolTip.add("AKA The Spider No-Climbatron 3000!");
            ev.toolTip.add("Emits sonic waves that inhibit spiders'");
            ev.toolTip.add("ability to climb.");
        } else if (ev.itemStack.getItem().equals(Items.reeds)) {
            ev.toolTip.add("Papercanes!");
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent lde) {
        Entity source = lde.source.getEntity();
        if (!lde.entity.worldObj.isRemote
                && lde.entity.getClass().equals(EntitySilverfish.class)
                && source != null && (source instanceof EntityPlayer)
                && (!(source instanceof FakePlayer)) && Math.random() > 0.99) {
            ItemStack mod = PowerCards.makeRandomCard();

            EntityItem ei = new EntityItem(lde.entity.worldObj,
                                           lde.entity.posX, lde.entity.posY, lde.entity.posZ, mod);

            lde.entity.worldObj.spawnEntityInWorld(ei);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (striders.contains(entity)) {
            if (entity.getActivePotionEffect(Registration.potionStrider) == null) {

            }
        }
    }

    @SubscribeEvent
    public void onAnvilEvent(AnvilUpdateEvent aue) {
        if (aue.left == null || aue.right == null || aue.left.getItem() == null
                || aue.right.getItem() == null) {
            return;
        }

        if (aue.left.getItem() == RandomTechItems.powerCard
                && aue.right.getItem() == RandomTechItems.powerCard) {
            if (!aue.left.hasTagCompound() || !aue.right.hasTagCompound()) {
                return;
            }
            NBTTagCompound leftNBT = aue.left.getTagCompound();
            NBTTagCompound rightNBT = aue.right.getTagCompound();
            String cardType = leftNBT.getString("cardName");

            if (!rightNBT.getString("cardName").equals(cardType)) {
                return;
            }

            int leftLevel = leftNBT.getInteger("cardRank");
            int rightLevel = rightNBT.getInteger("cardRank");
            if (rightLevel < leftLevel || rightLevel == 5 || leftLevel == 5) {
                return;
            }

            int newLevel = leftLevel + 1;
            aue.cost = Math.max(1,
                                (leftLevel * rightLevel * (PowerCards.powerCards
                                        .get(cardType).rarity + 1)) / 2);

            ItemStack result = new ItemStack(RandomTechItems.powerCard);
            NBTTagCompound nbt = result.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            nbt.setString("cardName", cardType);
            nbt.setInteger("cardRank", newLevel);
            result.setTagCompound(nbt);

            aue.output = result;
        }
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        // Ticks per item.
        int items = 200;

        if (fuel.getItem() instanceof ItemSuperFuel) {
            switch (fuel.getItemDamage()) {
                case 0:
                    return 50 * items;
                case 1:
                    return 210 * items;
                case 2:
                    return 218 * items;
                case 3:
                    return 226 * items;
                case 4:
                    return 1000 * items;
                case 5:
                    return 2500 * items;
            }
        }
        return 0;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        chunkX *= 16;
        chunkZ *= 16;
        if (random.nextInt(256) == 177) {
            int x = chunkX + random.nextInt(16);
            int y = random.nextInt(32) + 16;
            int z = chunkZ + random.nextInt(16);
            (new WorldGenMinable(RandomTechBlocks.nulliumOre,
                                 32 + random.nextInt(16))).generate(world, random, x, y, z);
        }

        // for (int x = 0; x < 16; x++) {
        // for (int z = 0; z < 16; z++) {
        // for (int y = 0; y < 128; y++) {
        // if (world.getBlock(x + chunkX, y, z + chunkZ).isSideSolid(
        // world, x, y, z, ForgeDirection.UP)
        // && !world.getBlock(x + chunkX, y + 1, z + chunkZ)
        // .isSideSolid(world, x, y, z,
        // ForgeDirection.UP)) {
        // world.setBlock(x + chunkX, y, z + chunkZ,
        // RandomTechBlocks.yellowBrickRoad, 0, 6);
        // }
        // }
        // }
        // }
    }
}
