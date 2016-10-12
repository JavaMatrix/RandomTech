package io.github.javamatrix.randomtech.blocks;

import codechicken.core.fluid.FluidUtils;
import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.gui.GUI;
import io.github.javamatrix.randomtech.tileentities.TileThermionicEngine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockThermionicEngine extends BlockContainer {

    private IIcon[] icons = new IIcon[2];

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
}
