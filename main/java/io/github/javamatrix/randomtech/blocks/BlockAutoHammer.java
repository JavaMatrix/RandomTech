package io.github.javamatrix.randomtech.blocks;

import cpw.mods.fml.client.registry.RenderingRegistry;
import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.gui.GUI;
import io.github.javamatrix.randomtech.tileentities.TileAutoHammer;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAutoHammer extends Block implements ITileEntityProvider {

    public static final int RENDER_ID = RenderingRegistry
            .getNextAvailableRenderId();

    public BlockAutoHammer() {
        super(Material.iron);
        setHardness(10.0f);
        setBlockTextureName(RandomTech.modid.toLowerCase() + ":autoHammerDummy");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileAutoHammer();
    }

    @Override
    public int getRenderType() {
        // Just grab the custom-generated render id.
        return RENDER_ID;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y,
                                        int z, int meta) {
        // Don't render sides, this is a custom-rendered block.
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(RandomTech.instance, GUI.ENERGETIC, world, x, y, z);
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        // Not a cube.
        return false;
    }
}
