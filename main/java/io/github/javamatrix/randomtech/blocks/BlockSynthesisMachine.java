package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.gui.GUI;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSynthesisMachine extends BlockContainer {

    private IIcon[] icons = new IIcon[2];

    public BlockSynthesisMachine() {
        // Made with science!
        super(Material.iron);
        setHardness(10);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register.registerIcon("randomtech:blockMachine");
        icons[1] = register
                .registerIcon("randomtech:blockSynthesisMachineSides");
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
        return new TileSynthesisMachine();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(RandomTech.instance, GUI.SYNTHESIS_MACHINE, world,
                           x, y, z);
        }
        return true;
    }
}
