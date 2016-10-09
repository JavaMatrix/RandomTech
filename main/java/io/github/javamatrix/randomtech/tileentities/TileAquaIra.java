package io.github.javamatrix.randomtech.tileentities;

import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.util.BlockPosition;
import io.github.javamatrix.randomtech.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class TileAquaIra extends TileBase {
    public static final int UPDATE_CHANCE_PER_TICK = 480;
    protected static Random rand = new Random();
    public int power = 10;
    public Block target = Blocks.water;
    public boolean hasSpread = false;

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Power")) {
            power = nbt.getInteger("Power");
        }
        if (nbt.hasKey("Target")) {
            target = (Block) Block.blockRegistry.getObject(nbt
                                                                   .getString("Target"));
        }
        if (nbt.hasKey("HasSpread")) {
            hasSpread = nbt.getBoolean("HasSpread");
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        nbt.setInteger("Power", power);
        nbt.setString("Target", Block.blockRegistry.getNameForObject(target));
        nbt.setBoolean("HasSpread", hasSpread);
    }

    @Override
    public void updateEntity() {
        // Spread to nearby blocks.
        if (!worldObj.isRemote && rand.nextInt(2 * UPDATE_CHANCE_PER_TICK / (power + 1)) == 0) {
            if (power > 0 && !hasSpread) {
                boolean spread = false;
                for (BlockPosition p : WorldUtils.buildNeighborList(worldObj,
                                                                    xCoord, yCoord, zCoord, true)) {
                    if (worldObj.getBlock(p.x, p.y, p.z)
                            .equals(target)) {
                        worldObj.setBlock(p.x, p.y, p.z,
                                          RandomTechBlocks.aquaIra);
                        TileAquaIra teThere = (TileAquaIra) worldObj
                                .getTileEntity(p.x, p.y, p.z);
                        teThere.target = target;
                        teThere.power = power - 1;
                        teThere.hasSpread = false;
                        spread = true;
                    }
                }
                hasSpread = true;
                if (spread) {
                    worldObj.playSoundEffect(xCoord, yCoord, zCoord,
                                             "minecraft:random.fizz", 1.0F, 1.0F);
                }
            }

            // Self destruct after spreading
            if (hasSpread || power <= 0) {
                worldObj.removeTileEntity(xCoord, yCoord, zCoord);
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }

            this.markDirty();
        }
    }
}
