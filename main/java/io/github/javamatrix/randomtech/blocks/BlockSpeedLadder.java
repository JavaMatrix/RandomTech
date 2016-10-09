package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import net.minecraft.block.BlockLadder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockSpeedLadder extends BlockLadder {

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z,
                                          Entity e) {
        if (e.motionY > 0.1f) {
            if (y < world.getHeight() - 1
                    && world.getBlock(x, y + 1, z).equals(
                    RandomTechBlocks.speedLadder)
                    && world.getBlock(x, y + 2, z).equals(
                    RandomTechBlocks.speedLadder)) {
                e.setPosition(e.posX, e.posY + 0.15, e.posZ);
            }
        }
        if (e.motionY < -0.1f) {
            if (y > 1
                    && world.getBlock(x, y - 1, z).equals(
                    RandomTechBlocks.speedLadder)
                    && world.getBlock(x, y - 2, z).equals(
                    RandomTechBlocks.speedLadder)) {
                e.setPosition(e.posX, e.posY - 0.15, e.posZ);
            }
        }
    }
}
