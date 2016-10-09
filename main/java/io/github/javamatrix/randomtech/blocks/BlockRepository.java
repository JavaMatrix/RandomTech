package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.tileentities.TileRepository;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRepository extends BlockContainer {

    protected Random repoRand = new Random();
    protected IIcon side_icon;
    protected IIcon top_icon;

    public BlockRepository() {
        super(Material.iron);
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        side_icon = registry.registerIcon(RandomTech.modid.toLowerCase() + ":repository_sides");
        top_icon = registry.registerIcon(RandomTech.modid.toLowerCase() + ":repository_verticals");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side < 2) {
            return top_icon;
        } else {
            return side_icon;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileRepository();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block,
                           int meta) {
        TileRepository tepd = (TileRepository) world
                .getTileEntity(x, y, z);
        // Null-check just for safety, logically this should never be null.
        if (tepd != null) {
            for (int i = 0; i < tepd.getSizeInventory(); i++) {
                ItemStack stack = tepd.getStackInSlot(i);
                if (stack != null) {
                    // This will differ from furnace code, since we'll just
                    // throw entire stacks at once.
                    float dx = (float) (0.2 + (repoRand.nextDouble() * 0.6));
                    float dy = (float) (0.2 + (repoRand.nextDouble() * 0.6));
                    float dz = (float) (0.2 + (repoRand.nextDouble() * 0.6));

                    EntityItem e = new EntityItem(world, x + dx, y + dy,
                                                  z + dz, stack);

                    float vx = (float) (repoRand.nextGaussian() * 0.025);
                    float vy = (float) (repoRand.nextGaussian() * 0.025 + 0.1);
                    float vz = (float) (repoRand.nextGaussian() * 0.025);
                    e.setVelocity(vx, vy, vz);
                    world.spawnEntityInWorld(e);
                }
            }
            // Causes a block update.
            world.func_147453_f(x, y, z, block);
        }
    }
}
