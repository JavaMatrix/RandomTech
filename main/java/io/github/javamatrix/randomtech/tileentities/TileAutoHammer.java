package io.github.javamatrix.randomtech.tileentities;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileAutoHammer extends TileEnergetic {
    public static final int ENERGY_PER_TICK = 20;
    public int outness = 0;
    public boolean justHit = false;
    public List<State> possibleStates = new ArrayList<State>() {
        private static final long serialVersionUID = 4210470561536545203L;

        {
            add(State.GOING_UP);
            add(State.GOING_DOWN);
            add(State.AT_TOP);
            add(State.AT_BOTTOM);
        }
    };
    public State currentState = State.AT_TOP;
    public int currentStateTimeLeft = 0;

    public int getArmOutness() {
        return outness;
    }

    public void doMovement() {
        if (currentState == State.GOING_DOWN) {
            outness = Math.min(outness + 4, 7);
            if (outness == 7) {
                currentState = State.AT_BOTTOM;
                currentStateTimeLeft = 5;
                justHit = true;
            }
        } else if (currentState == State.GOING_UP) {
            outness = Math.max(outness - 2, 0);
            if (outness == 0) {
                currentState = State.AT_TOP;
                currentStateTimeLeft = 20;
            }
        } else if (currentState == State.AT_BOTTOM && currentStateTimeLeft <= 0) {
            currentState = State.GOING_UP;
        } else if (currentState == State.AT_TOP && currentStateTimeLeft <= 0) {
            currentState = State.GOING_DOWN;
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);
        nbt.setInteger("Outness", outness);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        super.readCustomNBT(nbt);
        outness = nbt.getInteger("Outness");
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        boolean hasSmithy = worldObj.getBlock(xCoord, yCoord - 1, zCoord)
                .equals(RandomTechBlocks.smithy);
        // Make sure that a valid operation is taking place.
        hasSmithy = hasSmithy
                && ((TileSmithy) worldObj.getTileEntity(xCoord, yCoord - 1,
                                                        zCoord)).state >= 2;

        boolean active = true;

        if (!hasSmithy || storage.getEnergyStored() < ENERGY_PER_TICK) {
            active = false;
            if (getArmOutness() > 0) {
                currentState = State.GOING_UP;
            } else {
                currentState = State.AT_TOP;
            }
        }

        doMovement();

        if (!active) {
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return;
        }

        // Take RF for operation.
        extractEnergy(ForgeDirection.UNKNOWN, ENERGY_PER_TICK, false);

        // Decrement the state counter.
        currentStateTimeLeft--;

        // And hit that smithy, if we just hit bottom.
        if (!justHit) {
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return;
        }

        justHit = false;

        TileSmithy smithy = (TileSmithy) worldObj.getTileEntity(xCoord,
                                                                yCoord - 1, zCoord);

        smithy.cooldown = 20;
        smithy.setProgress(smithy.getProgress() + 15.0f);
        worldObj.playSound(xCoord, yCoord, zCoord, RandomTech.modid + ":forge",
                           1.0f + (float) Math.random(), 1.0f, false);

        if (smithy.state == 2) {
            int particles = (int) (Math.random() * 15) + 10;
            for (int i = 0; i < particles; i++) {
                worldObj.spawnParticle(
                        "blockcrack_" + Block.getIdFromBlock(Blocks.lava)
                                + "_0", xCoord + 0.5 + (Math.random() - 0.5),
                        yCoord, zCoord + (Math.random() - 0.5),
                        4 * (Math.random() - 0.5), Math.random() * 4,
                        4 * (Math.random() - 0.5));
            }
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public enum State {
        GOING_UP, GOING_DOWN, AT_TOP, AT_BOTTOM
    }

}
