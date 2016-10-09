package io.github.javamatrix.randomtech.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with the world.
 *
 * @author JavaMatrix
 */
public class WorldUtils {
    /**
     * Get a list of neighboring blocks.
     *
     * @param world            The world wherein the blocks can be found.
     * @param x                The X position of the origin block.
     * @param y                The Y position of the origin block.
     * @param z                The Z position of the origin block.
     * @param includeDiagonals Whether to include the 16 blocks diagonally adjacent to this
     *                         one.
     * @return
     */
    public static List<BlockPosition> buildNeighborList(IBlockAccess world,
                                                        int x, int y, int z, boolean includeDiagonals) {
        List<BlockPosition> neighbors = new ArrayList<BlockPosition>();

        // add the orthagonally adjacent blocks into the list.
        neighbors.add(new BlockPosition(x + 1, y, z));
        neighbors.add(new BlockPosition(x - 1, y, z));
        neighbors.add(new BlockPosition(x, y + 1, z));
        neighbors.add(new BlockPosition(x, y - 1, z));
        neighbors.add(new BlockPosition(x, y, z + 1));
        neighbors.add(new BlockPosition(x, y, z - 1));

        // Check that the player wants diagonals and corners.
        if (includeDiagonals) {
            // And now the diagonal ones (not the corners yet).
            neighbors.add(new BlockPosition(x + 1, y + 1, z));
            neighbors.add(new BlockPosition(x - 1, y + 1, z));
            neighbors.add(new BlockPosition(x, y + 1, z + 1));
            neighbors.add(new BlockPosition(x, y - 1, z - 1));
            neighbors.add(new BlockPosition(x + 1, y - 1, z));
            neighbors.add(new BlockPosition(x - 1, y - 1, z));
            neighbors.add(new BlockPosition(x, y - 1, z + 1));
            neighbors.add(new BlockPosition(x, y - 1, z - 1));

            // Corners!
            neighbors.add(new BlockPosition(x + 1, y + 1, z + 1));
            neighbors.add(new BlockPosition(x - 1, y + 1, z + 1));
            neighbors.add(new BlockPosition(x + 1, y + 1, z - 1));
            neighbors.add(new BlockPosition(x - 1, y + 1, z - 1));
            neighbors.add(new BlockPosition(x + 1, y - 1, z + 1));
            neighbors.add(new BlockPosition(x - 1, y - 1, z + 1));
            neighbors.add(new BlockPosition(x + 1, y - 1, z - 1));
            neighbors.add(new BlockPosition(x - 1, y - 1, z - 1));
        }

        return neighbors;
    }

    /**
     * Weeds out blocks from a list of blocks.
     *
     * @param toWeed The list to weed.
     * @param search The block to weed.
     * @param mode   True to remove all blocks of type search, false to remove all
     *               blocks that are not of type search.
     * @param world  The world that these blocks are in.
     * @return A weeded list of block positions that match the parameters.
     */
    public static List<BlockPosition> weedBlocks(IBlockAccess world,
                                                 Collection<BlockPosition> toWeed, Block search, boolean mode) {
        // A list to pop the good items into.
        List<BlockPosition> weeded = new ArrayList<BlockPosition>();
        for (BlockPosition position : toWeed) {
            // Fetch the block at the given position.
            Block atPos = position.getBlock(world);

            // Check that the block matches the search criteria.
            if ((atPos.equals(search) && mode)
                    || (!atPos.equals(search) && !mode)) {
                weeded.add(position);
            }
        }

        return weeded;
    }

    /**
     * Compares ItemStacks in a machine-friendly way.
     *
     * @param a
     * @param b
     * @return true if a ~= b && b.stackSize >= a.stackSize, false otherwise.
     */
    public static boolean matches(ItemStack a, ItemStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        boolean itemsEqual = a.getItem().equals(b.getItem());
        boolean metaEqual = a.getItemDamage() == b.getItemDamage();
        boolean amountsGood = b.stackSize >= a.stackSize;
        return itemsEqual && metaEqual && amountsGood;
    }
}
