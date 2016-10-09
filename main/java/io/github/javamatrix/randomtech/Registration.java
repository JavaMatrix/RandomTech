package io.github.javamatrix.randomtech;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.javamatrix.randomtech.blocks.*;
import io.github.javamatrix.randomtech.entities.effect.EntityBike;
import io.github.javamatrix.randomtech.items.*;
import io.github.javamatrix.randomtech.items.hardtools.*;
import io.github.javamatrix.randomtech.projectiles.EntityFlareRocket;
import io.github.javamatrix.randomtech.projectiles.EntityRailgunBolt;
import io.github.javamatrix.randomtech.projectiles.EntityTractorBeam;
import io.github.javamatrix.randomtech.projectiles.EntityUnclipperBullet;
import io.github.javamatrix.randomtech.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Handles block, item, and TileEntity registration.
 *
 * @author JavaMatrix
 */
public class Registration {

    public static Potion potionStrider;
    // The creative tab for the mod.
    public static CreativeTabs randomTechCreativeTab = new CreativeTabs(
            "randomTechCreativeTab") {
        @Override
        public Item getTabIconItem() {
            return RandomTechItems.unclipper;
        }
    };

    /**
     * Registers all the items in the mod.
     */
    public static void registerItems() {
        RandomTechItems.itemDebugger = new ItemDebugger()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("itemDebugger")
                .setTextureName(RandomTech.modid + ":" + "itemDebugger");
        GameRegistry.registerItem(RandomTechItems.itemDebugger, "itemDebugger");

        RandomTechItems.sundial = new ItemSundial()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("sundial")
                .setTextureName(RandomTech.modid + ":" + "sundial");
        GameRegistry.registerItem(RandomTechItems.sundial, "sundial");

        // This tool material is used for the Sword of Mendragor.
        Item.ToolMaterial mendragorium = EnumHelper.addToolMaterial(
                "mendragorium", 0, 0, 0, Float.POSITIVE_INFINITY, 0);

        RandomTechItems.swordOfMendragor = new ItemSwordOfMendragor(
                mendragorium).setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("swordOfMendragor")
                .setTextureName(RandomTech.modid + ":mendragorSword");
        GameRegistry.registerItem(RandomTechItems.swordOfMendragor,
                                  "swordOfMendragor");

        // The armor values and the durability for the Death Pack.
        ItemArmor.ArmorMaterial energeticArmorMaterial = EnumHelper
                .addArmorMaterial("deathpack", 100 / 16,
                                  new int[]{2, 7, 5, 1}, 0);

        RandomTechItems.deathPack = new ItemDeathPack(energeticArmorMaterial)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("deathpack")
                .setTextureName(RandomTech.modid + ":deathpack");
        GameRegistry.registerItem(RandomTechItems.deathPack, "deathpack");

        RandomTechItems.unclipper = new ItemUnclipperGun()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("unclipper").setMaxDamage(1000)
                .setTextureName(RandomTech.modid + ":unclipper");
        GameRegistry.registerItem(RandomTechItems.unclipper, "unclipper");

        RandomTechItems.enthalpiteShard = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("enthalpiteShard")
                .setTextureName(RandomTech.modid + ":enthalpiteShard");
        GameRegistry.registerItem(RandomTechItems.enthalpiteShard,
                                  "enthalpiteShard");

        RandomTechItems.enthalpite = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("enthalpite")
                .setTextureName(RandomTech.modid + ":enthalpite");
        GameRegistry.registerItem(RandomTechItems.enthalpite, "enthalpite");

        RandomTechItems.moltenIngot = new Item()
                .setUnlocalizedName("moltenIngot")
                .setTextureName(RandomTech.modid + ":moltenIngot")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.moltenIngot, "moltenIngot");

        RandomTechItems.hammerStone = new ItemHammer(1.0f)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hammerStone")
                .setTextureName(RandomTech.modid + ":hammerStone")
                .setMaxStackSize(1).setMaxDamage(132);
        GameRegistry.registerItem(RandomTechItems.hammerStone, "hammerStone");

        RandomTechItems.hammerIron = new ItemHammer(1.5f)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hammerIron")
                .setTextureName(RandomTech.modid + ":hammerIron")
                .setMaxStackSize(1).setMaxDamage(251);
        GameRegistry.registerItem(RandomTechItems.hammerIron, "hammerIron");

        RandomTechItems.hammerGold = new ItemHammer(4.0f)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hammerGold")
                .setTextureName(RandomTech.modid + ":hammerGold")
                .setMaxStackSize(1).setMaxDamage(33);
        GameRegistry.registerItem(RandomTechItems.hammerGold, "hammerGold");

        RandomTechItems.hammerDiamond = new ItemHammer(2.0f)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hammerDiamond")
                .setTextureName(RandomTech.modid + ":hammerDiamond")
                .setMaxStackSize(1).setMaxDamage(1562);
        GameRegistry.registerItem(RandomTechItems.hammerDiamond,
                                  "hammerDiamond");

        RandomTechItems.hardenedMetalIngot = new ItemHardenedMetalIngot()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalIngot")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalIngot")
                .setMaxDamage(0).setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalIngot,
                                  "hardenedMetalIngot");

        RandomTechItems.hardenedMagicalIngot = new ItemHardenedMagicalIngot()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalIngot")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMagicalIngot")
                .setMaxDamage(0).setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalIngot,
                                  "hardenedMagicalIngot");

        RandomTechItems.hardenedMetalPickaxe = new ItemHardenedMetalPickaxe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalPickaxe")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMetalPickaxe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalPickaxe,
                                  "hardenedMetalPickaxe");

        RandomTechItems.hardenedMetalHoe = new ItemHardenedMetalHoe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalHoe")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalHoe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalHoe,
                                  "hardenedMetalHoe");

        RandomTechItems.hardenedMetalAxe = new ItemHardenedMetalAxe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalAxe")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalAxe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalAxe,
                                  "hardenedMetalAxe");

        RandomTechItems.hardenedMetalShovel = new ItemHardenedMetalShovel()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalShovel")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalShovel")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalShovel,
                                  "hardenedMetalShovel");

        RandomTechItems.hardenedMetalSword = new ItemHardenedMetalSword()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalSword")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalSword")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalSword,
                                  "hardenedMetalSword");

        RandomTechItems.hardenedMetalHammer = new ItemHardenedMetalHammer()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalHammer")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalHammer")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalHammer,
                                  "hardenedMetalHammer");

        RandomTechItems.hardenedMagicalPickaxe = new ItemHardenedMagicalPickaxe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalPickaxe")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMagicalPickaxe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalPickaxe,
                                  "hardenedMagicalPickaxe");

        RandomTechItems.hardenedMagicalHoe = new ItemHardenedMagicalHoe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalHoe")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMagicalHoe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalHoe,
                                  "hardenedMagicalHoe");

        RandomTechItems.hardenedMagicalAxe = new ItemHardenedMagicalAxe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalAxe")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMagicalAxe")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalAxe,
                                  "hardenedMagicalAxe");

        RandomTechItems.hardenedMagicalShovel = new ItemHardenedMagicalShovel()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalShovel")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMagicalShovel")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalShovel,
                                  "hardenedMagicalShovel");

        RandomTechItems.hardenedMagicalSword = new ItemHardenedMagicalSword()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalSword")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMagicalSword")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalSword,
                                  "hardenedMagicalSword");

        RandomTechItems.hardenedMagicalHammer = new ItemHardenedMagicalHammer()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMagicalHammer")
                .setTextureName(
                        RandomTech.modid
                                + ":hardenedMetal/hardenedMagicalHammer")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.hardenedMagicalHammer,
                                  "hardenedMagicalHammer");

        RandomTechItems.aquaIraBottle = new ItemAquaIraBottle()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("aquaIraBottle")
                .setTextureName(RandomTech.modid + ":aquaIraBottle")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.aquaIraBottle,
                                  "aquaIraBottle");

        RandomTechItems.bottledWrath = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("bottledWrath")
                .setTextureName(RandomTech.modid + ":bottledWrath")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.bottledWrath, "bottledWrath");

        RandomTechItems.chunkingPick = new ItemChunkingPickaxe()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("chunkingPick")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/chunkingPick")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.chunkingPick, "chunkingPick");

        RandomTechItems.railgunBolt = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("railgunBolt")
                .setTextureName(RandomTech.modid + ":railgunBolt");
        GameRegistry.registerItem(RandomTechItems.railgunBolt, "railgunBolt");

        RandomTechItems.railgun = new ItemRailgun()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("railgun")
                .setTextureName(RandomTech.modid + ":railgun")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.railgun, "railgun");

        RandomTechItems.hardenedMetalPlate = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("hardenedMetalPlate")
                .setTextureName(
                        RandomTech.modid + ":hardenedMetal/hardenedMetalPlate")
                .setMaxStackSize(16);
        GameRegistry.registerItem(RandomTechItems.hardenedMetalPlate,
                                  "hardenedMetalPlate");

        RandomTechItems.cpu = new Item().setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("cpu")
                .setTextureName(RandomTech.modid + ":cpu").setMaxStackSize(4);
        GameRegistry.registerItem(RandomTechItems.cpu, "cpu");

        RandomTechItems.flareGun = new ItemFlareGun()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("flareGun")
                .setTextureName(RandomTech.modid + ":flareGun")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.flareGun, "flareGun");

        RandomTechItems.energyFez = new ItemEnergyFez(energeticArmorMaterial)
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("energyFez")
                .setTextureName(RandomTech.modid + ":energyFez")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.energyFez, "energyFez");

        RandomTechItems.tractorBeam = new ItemTractorBeam()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("tractorBeam")
                .setTextureName(RandomTech.modid + ":tractorBeam")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.tractorBeam, "tractorBeam");

        RandomTechItems.superFuel = new ItemSuperFuel()
                .setCreativeTab(randomTechCreativeTab).setHasSubtypes(true)
                .setMaxDamage(0);
        GameRegistry.registerItem(RandomTechItems.superFuel, "superFuel");

        RandomTechItems.nulliumIngot = new Item()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("nulliumIngot")
                .setTextureName(RandomTech.modid + ":nulliumIngot");
        GameRegistry.registerItem(RandomTechItems.nulliumIngot, "nulliumIngot");

        RandomTechItems.galatine = new ItemGalatine(EnumHelper.addToolMaterial(
                "galatine", 0, 0, 8.0f, -5.0f, 0))
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("galatine")
                .setTextureName(RandomTech.modid + ":galatine");
        GameRegistry.registerItem(RandomTechItems.galatine, "galatine");

        RandomTechItems.powerCard = new ItemPowerCard()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("powerCard")
                .setTextureName(RandomTech.modid + ":powerCard")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.powerCard, "powerCard");

        RandomTechItems.bucketOfBurblingInfinity = new ItemBucketOfBurblingInfinity()
                .setCreativeTab(randomTechCreativeTab)
                .setUnlocalizedName("bucketOfBurblingInfinity")
                .setTextureName(RandomTech.modid + ":bucketOfBurblingInfinity")
                .setMaxStackSize(1);
        GameRegistry.registerItem(RandomTechItems.bucketOfBurblingInfinity,
                                  "bucketOfBurblingInfinity");
        ItemStack infinityBucket = new ItemStack(
                RandomTechItems.bucketOfBurblingInfinity);
        ItemStack emptyInfinityBucket = new ItemStack(
                RandomTechItems.bucketOfBurblingInfinity, 1, 1);
        FluidContainerRegistry
                .registerFluidContainer(new FluidStack(FluidRegistry.WATER,
                                                       1000), infinityBucket, emptyInfinityBucket);
    }

    /**
     * Registers all the blocks and TileEntities for the mod.
     */
    public static void registerBlocks() {
        // Again, these are pretty much all the same.
        // Make a new block,
        // Set it's name (camelCase of the English name),
        // Set the creative tab,
        // And register it.

        RandomTechBlocks.smithy = new BlockSmithy().setBlockName("smithy")
                .setCreativeTab(randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.smithy, "smithy");
        GameRegistry.registerTileEntity(TileSmithy.class, "teSmithy");

        RandomTechBlocks.repository = new BlockRepository().setBlockName(
                "repository").setCreativeTab(randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.repository, "repository");
        GameRegistry.registerTileEntity(TileRepository.class, "teRepository");

        RandomTechBlocks.reinforcedRock = new BlockReinforcedRock()
                .setBlockName("reinforcedRock").setCreativeTab(
                        randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.reinforcedRock,
                                   "reinforcedRock");

        RandomTechBlocks.yellowBrickRoad = new BlockYellowBrickRoad()
                .setBlockName("yellowBrickRoad").setCreativeTab(
                        randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.yellowBrickRoad,
                                   "yellowBrickRoad");

        RandomTechBlocks.aquaIra = new BlockAquaIra().setBlockName("aquaIra")
                .setCreativeTab(randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.aquaIra, "aquaIra");
        GameRegistry.registerTileEntity(TileAquaIra.class, "teAquaIra");

        RandomTechBlocks.blockGunpowder = new BlockSimple(Material.sand)
                .setBlockName("blockGunpowder")
                .setCreativeTab(randomTechCreativeTab).setHardness(0.1f)
                .setStepSound(Block.soundTypeSand)
                .setBlockTextureName(RandomTech.modid + ":blockGunpowder");
        GameRegistry.registerBlock(RandomTechBlocks.blockGunpowder,
                                   "blockGunpowder");
        Blocks.fire.setFireInfo(RandomTechBlocks.blockGunpowder, 100, 100);

        RandomTechBlocks.speedLadder = new BlockSpeedLadder()
                .setBlockName("speedLadder")
                .setCreativeTab(randomTechCreativeTab)
                .setBlockTextureName(RandomTech.modid + ":speedLadder")
                .setLightOpacity(0).setLightLevel(0.5F).setHardness(1.0f);
        GameRegistry.registerBlock(RandomTechBlocks.speedLadder, "speedLadder");

        RandomTechBlocks.synthesisMachine = new BlockSynthesisMachine()
                .setBlockName("synthesisMachine").setCreativeTab(
                        randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.synthesisMachine,
                                   "synthesisMachine");
        GameRegistry.registerTileEntity(TileSynthesisMachine.class,
                                        "teSynthesisMachine");

        RandomTechBlocks.kineticGenerator = new BlockKineticEngine()
                .setBlockName("kineticGenerator")
                .setCreativeTab(randomTechCreativeTab).setHardness(10.0f);
        GameRegistry.registerBlock(RandomTechBlocks.kineticGenerator,
                                   "kineticGenerator");

        RandomTechBlocks.machineCore = new BlockSimple(Material.iron)
                .setBlockName("machineCore")
                .setCreativeTab(randomTechCreativeTab).setHardness(5.0f)
                .setBlockTextureName(RandomTech.modid + ":blockMachine")
                .setStepSound(Block.soundTypeMetal);
        GameRegistry.registerBlock(RandomTechBlocks.machineCore, "machineCore");

        RandomTechBlocks.autoHammer = new BlockAutoHammer().setBlockName(
                "autoHammer").setCreativeTab(randomTechCreativeTab);
        GameRegistry.registerBlock(RandomTechBlocks.autoHammer, "autoHammer");
        GameRegistry.registerTileEntity(TileAutoHammer.class, "teAutoHammer");

        RandomTechBlocks.nulliumOre = new BlockSimple(Material.rock)
                .setBlockName("nulliumOre")
                .setBlockTextureName(RandomTech.modid + ":nulliumOre")
                .setCreativeTab(randomTechCreativeTab).setHardness(15.0f);
        RandomTechBlocks.nulliumOre.setHarvestLevel("pickaxe", 3);
        GameRegistry.registerBlock(RandomTechBlocks.nulliumOre, "nulliumOre");

        RandomTechBlocks.empowermentTable = new BlockEmpowermentTable()
                .setCreativeTab(randomTechCreativeTab).setBlockName(
                        "empowermentTable");
        GameRegistry.registerBlock(RandomTechBlocks.empowermentTable,
                                   "empowermentTable");
        GameRegistry.registerTileEntity(TileEmpowermentTable.class,
                                        "teEmpowermentTable");

        RandomTechBlocks.arthropodicDisruptor = new BlockArthropodicDisruptor()
                .setCreativeTab(randomTechCreativeTab).setBlockName(
                        "arthropodicDisruptor");
        GameRegistry.registerBlock(RandomTechBlocks.arthropodicDisruptor,
                                   "arthropodicDisruptor");
        GameRegistry.registerTileEntity(TileArthropodicDisruptor.class,
                                        "teArthropodicDisruptor");
    }

    /**
     * Registers all manner of recipes for the mod.
     */
    public static void registerRecipes() {

        // Register the recipe for a sundial.
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.sundial, 1), "s",
                               "W", 's', new ItemStack(Items.stick), 'W', new ItemStack(
                        Blocks.planks));

        // Add the smelting recipe for a Shard of Enthalpite.
        GameRegistry.addSmelting(Items.blaze_rod, new ItemStack(
                RandomTechItems.enthalpiteShard), 1);

        // Add the crafting/decrafting recipe for an Enthalpite Crystal.
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.enthalpite),
                               "eee", "eee", "eee", 'e', RandomTechItems.enthalpiteShard);
        GameRegistry.addShapelessRecipe(new ItemStack(
                RandomTechItems.enthalpiteShard, 9), new ItemStack(
                RandomTechItems.enthalpite));

        // Basic pickaxe recipe for the hardened metal pickaxe.
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMetalPickaxe), "hhh", " s ", " s ",
                               'h', new ItemStack(RandomTechItems.hardenedMetalIngot, 1,
                                                  OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic hoe recipe for the hardened metal hoe.
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hardenedMetalHoe),
                               "hh", " s", " s", 'h', new ItemStack(
                        RandomTechItems.hardenedMetalIngot, 1,
                        OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hardenedMetalHoe),
                               "hh", "s ", "s ", 'h', new ItemStack(
                        RandomTechItems.hardenedMetalIngot, 1,
                        OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic axe recipes for the hardened metal axe.
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hardenedMetalAxe),
                               "hh", "hs", " s", 'h', new ItemStack(
                        RandomTechItems.hardenedMetalIngot, 1,
                        OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hardenedMetalAxe),
                               "hh", "sh", "s ", 'h', new ItemStack(
                        RandomTechItems.hardenedMetalIngot, 1,
                        OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic shovel recipe for the hardened metal shovel
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMetalShovel), "h", "s", "s", 'h',
                               new ItemStack(RandomTechItems.hardenedMetalIngot, 1,
                                             OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic sword recipe for the hardened metal sword
        GameRegistry.addRecipe(
                new ItemStack(RandomTechItems.hardenedMetalSword), "h", "h",
                "s", 'h', new ItemStack(RandomTechItems.hardenedMetalIngot, 1,
                                        OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic pickaxe recipe for the hardened magical pickaxe.
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMagicalPickaxe), "hhh", " s ", " s ",
                               'h', new ItemStack(RandomTechItems.hardenedMagicalIngot, 1,
                                                  OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic hoe recipe for the hardened magical hoe.
        GameRegistry.addRecipe(
                new ItemStack(RandomTechItems.hardenedMagicalHoe), "hh", " s",
                " s", 'h', new ItemStack(RandomTechItems.hardenedMagicalIngot,
                                         1, OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(
                new ItemStack(RandomTechItems.hardenedMagicalHoe), "hh", "s ",
                "s ", 'h', new ItemStack(RandomTechItems.hardenedMagicalIngot,
                                         1, OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic axe recipes for the hardened magical axe.
        GameRegistry.addRecipe(
                new ItemStack(RandomTechItems.hardenedMagicalAxe), "hh", "hs",
                " s", 'h', new ItemStack(RandomTechItems.hardenedMagicalIngot,
                                         1, OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(
                new ItemStack(RandomTechItems.hardenedMagicalAxe), "hh", "sh",
                "s ", 'h', new ItemStack(RandomTechItems.hardenedMagicalIngot,
                                         1, OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic shovel recipe for the hardened magical shovel
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMagicalShovel), "h", "s", "s", 'h',
                               new ItemStack(RandomTechItems.hardenedMagicalIngot, 1,
                                             OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // Basic sword recipe for the hardened magical sword
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMagicalSword), "h", "h", "s", 'h',
                               new ItemStack(RandomTechItems.hardenedMagicalIngot, 1,
                                             OreDictionary.WILDCARD_VALUE), 's', Blocks.obsidian);

        // Nullium pick recipe for the fiery pick
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.chunkingPick),
                               "nnn", " s ", " s ", 'n', RandomTechItems.nulliumIngot, 's',
                               Items.stick);

        // Energy fez recipe
        ItemStack energyFez = new ItemStack(RandomTechItems.energyFez);
        NBTTagCompound nbt = new NBTTagCompound();
        if (energyFez.hasTagCompound()) {
            nbt = energyFez.getTagCompound();
        }
        nbt.setInteger("StoredEnergy", 0);
        energyFez.setTagCompound(nbt);
        GameRegistry.addRecipe(energyFez, "WWi", "WgW", "WRW", 'W',
                               Blocks.wool, 'i', Items.gold_nugget, 'g', Items.gold_ingot,
                               'R', Blocks.redstone_block);

        // Hammer recipes
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerStone),
                               " ii", "is ", " s ", 'i', Blocks.cobblestone, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerStone),
                               "ii ", " si", " s ", 'i', Blocks.cobblestone, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerIron),
                               " ii", "is ", " s ", 'i', Items.iron_ingot, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerIron),
                               "ii ", " si", " s ", 'i', Items.iron_ingot, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerGold),
                               " ii", "is ", " s ", 'i', Items.gold_ingot, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerGold),
                               "ii ", " si", " s ", 'i', Items.gold_ingot, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerDiamond),
                               " ii", "is ", " s ", 'i', Items.diamond, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.hammerDiamond),
                               "ii ", " si", " s ", 'i', Items.diamond, 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMagicalHammer), " ii", "is ", " s ",
                               'i', new ItemStack(RandomTechItems.hardenedMagicalIngot, 1,
                                                  OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMagicalHammer), "ii ", " si", " s ",
                               'i', new ItemStack(RandomTechItems.hardenedMagicalIngot, 1,
                                                  OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMetalHammer), " ii", "is ", " s ", 'i',
                               new ItemStack(RandomTechItems.hardenedMetalIngot, 1,
                                             OreDictionary.WILDCARD_VALUE), 's', Items.stick);
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechItems.hardenedMetalHammer), "ii ", " si", " s ", 'i',
                               new ItemStack(RandomTechItems.hardenedMetalIngot, 1,
                                             OreDictionary.WILDCARD_VALUE), 's', Items.stick);

        // The Aqua Ira production chain.
        ItemStack poison = new ItemStack(Items.potionitem, 1, 8196);
        GameRegistry.addRecipe(new ItemStack(RandomTechBlocks.blockGunpowder),
                               "gg", "gg", 'g', Items.gunpowder);
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.bottledWrath),
                               "gGg", "GpG", "gGg", 'g',
                               Item.getItemFromBlock(RandomTechBlocks.blockGunpowder), 'G',
                               Item.getItemFromBlock(Blocks.glowstone), 'p', poison);
        GameRegistry.addSmelting(RandomTechItems.bottledWrath, new ItemStack(
                RandomTechItems.aquaIraBottle), 16.0F);

        // Machine Core recipes
        GameRegistry.addRecipe(new ItemStack(RandomTechBlocks.machineCore),
                               "rir", "iri", "rir", 'r', Blocks.redstone_block, 'i',
                               Items.iron_ingot);

        // Smithy Recipe
        GameRegistry.addRecipe(new ItemStack(RandomTechBlocks.smithy), "iii",
                               "iAi", "SFS", 'i', Items.iron_ingot, 'A', Blocks.anvil, 'S',
                               Blocks.stone, 'F', Blocks.furnace);

        // Kinetic Generator Recipe
        GameRegistry.addRecipe(
                new ItemStack(RandomTechBlocks.kineticGenerator), "igi", "iMi",
                "iii", 'i', Items.iron_ingot, 'g',
                Blocks.light_weighted_pressure_plate, 'M',
                RandomTechBlocks.machineCore);

        // Synthesis Machine Recipe
        GameRegistry.addRecipe(
                new ItemStack(RandomTechBlocks.synthesisMachine), "iii", "rMr",
                "iRi", 'i', Items.iron_ingot, 'r', Items.redstone, 'M',
                RandomTechBlocks.machineCore, 'R', Blocks.redstone_block);

        // Super Fuel smelting
        GameRegistry.addSmelting(
                new ItemStack(RandomTechItems.superFuel, 1, 1), new ItemStack(
                        RandomTechItems.superFuel, 1, 2), 0.0f);
        GameRegistry.addSmelting(
                new ItemStack(RandomTechItems.superFuel, 1, 2), new ItemStack(
                        RandomTechItems.superFuel, 1, 3), 0.0f);

        // Auto-hammer recipe
        GameRegistry.addRecipe(new ItemStack(RandomTechBlocks.autoHammer),
                               "cMc", " P ", "hhh", 'c', RandomTechItems.cpu, 'M',
                               new ItemStack(RandomTechBlocks.machineCore), 'P',
                               new ItemStack(Blocks.piston), 'h',
                               RandomTechItems.hardenedMetalPlate);

        // Nullium > Nullium in a furnace </troll>
        GameRegistry.addSmelting(RandomTechBlocks.nulliumOre, new ItemStack(
                RandomTechBlocks.nulliumOre), 0.0f);

        // Galatine recipe.
        GameRegistry.addRecipe(new ItemStack(RandomTechItems.galatine), " n ",
                               " n ", " O ", 'n', RandomTechItems.nulliumIngot, 'O',
                               Blocks.obsidian);

        // Empowerment Table recipe.
        GameRegistry.addRecipe(
                new ItemStack(RandomTechBlocks.empowermentTable), "OnO", "nCn",
                "OnO", 'O', Blocks.obsidian, 'n', RandomTechItems.nulliumIngot,
                'C', Blocks.crafting_table);

        // Arthropodic Disruptor recipe.
        GameRegistry.addRecipe(new ItemStack(
                                       RandomTechBlocks.arthropodicDisruptor), "IdI", "iMi", "iJi",
                               'I', Blocks.iron_block, 'd', Items.record_13, 'i',
                               Items.iron_ingot, 'M', RandomTechBlocks.machineCore, 'J',
                               Blocks.jukebox);
    }

    public static void registerEntities() {
        int modEntityId = 0;
        EntityRegistry.registerModEntity(EntityUnclipperBullet.class,
                                         "UnclipperBullet", ++modEntityId, RandomTech.instance, 64, 10,
                                         true);
        EntityRegistry
                .registerModEntity(EntityFlareRocket.class, "FlareRocket",
                                   ++modEntityId, RandomTech.instance, 64, 10, true);
        EntityRegistry
                .registerModEntity(EntityTractorBeam.class, "TractorBeam",
                                   ++modEntityId, RandomTech.instance, 64, 10, true);
        EntityRegistry
                .registerModEntity(EntityRailgunBolt.class, "RailgunBolt",
                                   ++modEntityId, RandomTech.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityBike.class, "Bike",
                                         ++modEntityId, RandomTech.instance, 80, 3, true);
    }

    public static void registerNeptuneSplashes() {
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Fred was intelligent!");
        FMLInterModComms.sendMessage("neptune", "add-splash", "Very random!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Aqua is very Ira right now!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Repositories are bigger on the inside!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Follow the Yellow Brick Road!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Entire packs of death!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Finally, sundials!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Mendragor lives!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Fortunate Ingots!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Suddenly, he couldn't collide with the ground!");
        FMLInterModComms
                .sendMessage("neptune", "add-splash", "Hardened metal!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Also try Zyin's HUD!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Completely fused!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "The shadow to Excalibur's light!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "NulliumIngotException!");
        FMLInterModComms.sendMessage("neptune", "add-splash",
                                     "Now with Arthropodic Disruptors!");
    }

    public static class RandomTechItems {
        // All of the items to be registered.
        public static Item itemDebugger;
        public static Item sundial;
        public static Item swordOfMendragor;
        public static Item deathPack;
        public static Item unclipper;
        public static Item enthalpiteShard;
        public static Item enthalpite;
        public static Item moltenIngot;
        public static Item hammerStone;
        public static Item hammerIron;
        public static Item hammerGold;
        public static Item hammerDiamond;
        public static Item hardenedMetalIngot;
        public static Item hardenedMagicalIngot;
        public static Item hardenedMetalPickaxe;
        public static Item hardenedMetalHoe;
        public static Item hardenedMetalAxe;
        public static Item hardenedMetalShovel;
        public static Item hardenedMetalSword;
        public static Item hardenedMetalHammer;
        public static Item hardenedMagicalPickaxe;
        public static Item hardenedMagicalHoe;
        public static Item hardenedMagicalAxe;
        public static Item hardenedMagicalShovel;
        public static Item hardenedMagicalSword;
        public static Item hardenedMagicalHammer;
        public static Item dungeonChestMaker;
        public static Item aquaIraBottle;
        public static Item bottledWrath;
        public static Item chunkingPick;
        public static Item railgunBolt;
        public static Item railgun;
        public static Item hardenedMetalPlate;
        public static Item cpu;
        public static Item flareGun;
        public static Item energyFez;
        public static Item tractorBeam;
        public static Item superFuel;
        public static Item nulliumIngot;
        public static Item galatine;
        public static Item powerCard;
        public static Item bucketOfBurblingInfinity;
    }

    public static class RandomTechBlocks {
        // All of the blocks to be registered.
        public static Block smithy;
        public static Block potionDiffuser;
        public static Block repository;
        public static Block yellowBrickRoad;
        public static Block reinforcedRock;
        public static Block aquaIra;
        public static Block blockGunpowder;
        public static Block speedLadder;
        public static Block synthesisMachine;
        public static Block kineticGenerator;
        public static Block machineCore;
        public static Block autoHammer;
        public static Block nulliumOre;
        public static Block empowermentTable;
        // public static Block smartDispenser;
        public static Block arthropodicDisruptor;
    }
}
