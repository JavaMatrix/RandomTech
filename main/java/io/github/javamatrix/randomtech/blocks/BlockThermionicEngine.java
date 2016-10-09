package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.tileentities.TileThermionicEngine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockThermionicEngine extends BlockContainer {

    public BlockThermionicEngine() {
        super(Material.iron);
        setHardness(10.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileThermionicEngine();
    }
}
