package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.gui.GUI;
import io.github.javamatrix.randomtech.tileentities.TileEmpowermentTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEmpowermentTable extends BlockContainer {

    private IIcon[] icons = new IIcon[3];

    public BlockEmpowermentTable() {
        // Hey guys, look at all my sciency stuff!
        super(Material.iron);
        setHardness(10);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register
                .registerIcon("randomtech:blockEmpowermentTableBottom");
        icons[1] = register.registerIcon("randomtech:blockEmpowermentTableTop");
        icons[2] = register.registerIcon("randomtech:blockEmpowermentTableSides");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0) {
            return icons[0];
        } else if (side == 1) {
            return icons[1];
        } else {
            return icons[2];
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEmpowermentTable();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(RandomTech.instance, GUI.EMPOWERMENT_TABLE, world,
                           x, y, z);
        }
        return true;
    }
}
