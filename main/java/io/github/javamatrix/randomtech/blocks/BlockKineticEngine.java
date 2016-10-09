package io.github.javamatrix.randomtech.blocks;

import cofh.api.energy.IEnergyReceiver;
import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.items.ItemHammer;
import io.github.javamatrix.randomtech.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.Map;

public class BlockKineticEngine extends Block {

    public static final int GENERATION_DELAY = 1000;
    public long lastHit = 0;
    public IIcon[] icons = new IIcon[2];

    public BlockKineticEngine() {
        super(Material.iron);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register.registerIcon("randomtech:blockMachine");
        icons[1] = register.registerIcon("randomtech:blockKineticGeneratorTop");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return icons[1];
        } else {
            return icons[0];
        }
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z,
                               EntityPlayer player) {
        if (PlayerUtils.getHolding(player) == null
                || !(PlayerUtils.getHolding(player).getItem() instanceof ItemHammer)
                || (System.currentTimeMillis() - lastHit) < GENERATION_DELAY) {
            return;
        }

        ItemHammer hammer = (ItemHammer) player.getHeldItem().getItem();

        if (!world.isRemote) {
            int rfGenerated = (int) (240 * hammer.efficiency);
            Map<IEnergyReceiver, ForgeDirection> neighborPowerables = new HashMap<IEnergyReceiver, ForgeDirection>();
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity te = world.getTileEntity(x + dir.offsetX, y
                        + dir.offsetY, z + dir.offsetZ);
                if (te != null && te instanceof IEnergyReceiver) {
                    neighborPowerables.put((IEnergyReceiver) te, dir);
                }
            }
            for (IEnergyReceiver receiver : neighborPowerables.keySet()) {
                receiver.receiveEnergy(neighborPowerables.get(receiver)
                                               .getOpposite(), rfGenerated
                                               / neighborPowerables.keySet().size(), false);
            }
            lastHit = System.currentTimeMillis();

            // Ding-zap!
            world.playSoundAtEntity(player, RandomTech.modid + ":forge",
                                    0.5f + (float) Math.random(), 1.0f);
            world.playSoundAtEntity(player, RandomTech.modid + ":zap",
                                    2.0f + (float) Math.random(),
                                    (float) Math.pow(2, Math.random()));
        }

        // Prevents the ghost hammer bug.
        if (player.getHeldItem().stackSize == 0) {
            player.inventory.setInventorySlotContents(
                    player.inventory.currentItem, null);
        }
    }
}
