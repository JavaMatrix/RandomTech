package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.tileentities.TileAquaIra;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockAquaIra extends Block implements ITileEntityProvider {
    public BlockAquaIra() {
        // No push mobility or map visibility. Oddly, cake fits the bill.
        super(Material.cake);
        // We don't want it breakable either.
        setBlockUnbreakable();

        setBlockTextureName(RandomTech.modid + ":aquaIra");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileAquaIra();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase player, ItemStack stack) {
        TileAquaIra te = (TileAquaIra) createNewTileEntity(world,
                                                           world.getBlockMetadata(x, y, z));
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("Power")) {
                te.power = nbt.getInteger("Power");
            }
            if (nbt.hasKey("Target")) {
                te.target = (Block) Block.blockRegistry.getObject(nbt.getString("Target"));
            }
            if (nbt.hasKey("HasSpread")) {
                te.hasSpread = nbt.getBoolean("HasSpread");
            }
        }
        world.setTileEntity(x, y, z, te);
    }

    @Override
    public void getSubBlocks(Item i, CreativeTabs tab, List blocks) {
    }

    /**
     * Prevents the player from selecting this block.
     */
    @Override
    public boolean canCollideCheck(int meta, boolean hasBoat) {
        return false;
    }

    /**
     * Copied wholesale from BlockLiquid.
     */
    @Override
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y,
                                          int z) {
        int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int i1 = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int j1 = l & 255;
        int k1 = i1 & 255;
        int l1 = l >> 16 & 255;
        int i2 = i1 >> 16 & 255;
        return (j1 > k1 ? j1 : k1) | (l1 > i2 ? l1 : i2) << 16;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Cube: Yes. Opaque: No.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z,
                                        AxisAlignedBB bounds, List boxes, Entity who) {
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity e) {
        e.setFire(10);
    }
}
