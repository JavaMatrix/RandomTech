package io.github.javamatrix.randomtech.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPosition {
    public int x;
    public int y;
    public int z;

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock(IBlockAccess world) {
        return world.getBlock(x, y, z);
    }

    public TileEntity getTE(IBlockAccess world) {
        return world.getTileEntity(x, y, z);
    }

    public void setBlock(World world, Block toSet) {
        world.setBlock(x, y, z, toSet);
    }

    public Block getTop(IBlockAccess worldObj) {
        return worldObj.getBlock(x, y + 1, z);
    }

    public void deleteBlock(World world) {
        setBlock(world, Blocks.air);
        world.removeTileEntity(x, y, z);
    }

    @Override
    public boolean equals(Object b) {
        if (!(b instanceof BlockPosition)) {
            return false;
        }
        BlockPosition other = (BlockPosition) b;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}
