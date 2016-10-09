package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.tileentities.TileAquaIra;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ItemAquaIraBottle extends Item {

    public static ArrayList<Block> IRA_WHITE_LIST = new ArrayList<Block>() {
        private static final long serialVersionUID = 1612873559455165785L;

        {
            add(Blocks.water);
            add(Blocks.soul_sand);
            add(Blocks.gravel);
            add(Blocks.leaves);
            add(Blocks.leaves2);
            add(Blocks.tallgrass);
            add(Blocks.double_plant);
        }
    };

    @Override
    public ItemStack onItemRightClick(ItemStack heldStack, World world,
                                      EntityPlayer whoDunnit) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(
                world, whoDunnit, true);

        if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            Block hit = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if (IRA_WHITE_LIST.contains(hit)) {
                world.setBlock(mop.blockX, mop.blockY, mop.blockZ,
                               RandomTechBlocks.aquaIra);
                TileAquaIra teai = (TileAquaIra) world
                        .getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                teai.target = hit;
                if (whoDunnit.capabilities.isCreativeMode) {
                    return heldStack;
                }
                return new ItemStack(Items.glass_bottle);
            }
        }

        return heldStack;
    }
}
