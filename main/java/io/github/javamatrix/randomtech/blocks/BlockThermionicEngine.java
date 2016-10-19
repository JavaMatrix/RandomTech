package io.github.javamatrix.randomtech.blocks;

import codechicken.core.fluid.FluidUtils;
import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.Registration;
import io.github.javamatrix.randomtech.gui.GUI;
import io.github.javamatrix.randomtech.tileentities.TileThermionicEngine;
import io.github.javamatrix.randomtech.util.BlockPosition;
import io.github.javamatrix.randomtech.util.ItemFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockThermionicEngine extends BlockContainer {

    private IIcon[] icons = new IIcon[2];
    private Map<BlockPosition, EntityPlayer> lastPlayersToHarvest = new HashMap<>();


    public BlockThermionicEngine() {
        super(Material.iron);
        setHardness(10.0f);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register.registerIcon("randomtech:blockMachine");
        icons[1] = register
                .registerIcon("randomtech:blockThermionicEngineSides");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return icons[0];
        } else {
            return icons[1];
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileThermionicEngine();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        // Buckets override gui
        boolean success = FluidUtils.fillTankWithContainer((TileThermionicEngine) world.getTileEntity(x, y, z), player);

        if (!success && !world.isRemote) {
            player.openGui(RandomTech.instance, GUI.THERMIONIC_ENGINE, world,
                           x, y, z);
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        TileThermionicEngine tte = (TileThermionicEngine) world.getTileEntity(x, y, z);
        if (item.getTagCompound() != null && item.getTagCompound().getCompoundTag("storage") != null) {
            NBTTagCompound storageCompound = item.getTagCompound().getCompoundTag("storage");
            tte.storage.readFromNBT(storageCompound);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer who) {
        lastPlayersToHarvest.put(new BlockPosition(x, y, z), who);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        EntityPlayer lastPlayerToHarvest = lastPlayersToHarvest.get(new BlockPosition(x, y, z));
        lastPlayerToHarvest = (lastPlayerToHarvest == null) ? Minecraft.getMinecraft().thePlayer : lastPlayerToHarvest;
        if (!lastPlayerToHarvest.capabilities.isCreativeMode) {
            TileThermionicEngine tte = (TileThermionicEngine) world.getTileEntity(x, y, z);
            NBTTagCompound storageCompound = new NBTTagCompound();
            tte.storage.writeToNBT(storageCompound);
            final ItemFactory factory = new ItemFactory(Registration.RandomTechBlocks.thermionicEngine);
            factory.addTag("storage", storageCompound);
            EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, factory.finish());
            world.spawnEntityInWorld(item);
        }

        super.breakBlock(world, x, y, z, block, meta);
    }
}
