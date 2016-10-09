package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.items.ItemHammer;
import io.github.javamatrix.randomtech.tileentities.TileSmithy;
import io.github.javamatrix.randomtech.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSmithy extends Block implements ITileEntityProvider {

    public IIcon[] icons = new IIcon[3];

    public BlockSmithy() {
        super(Material.iron);
        setHardness(5.0f);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register.registerIcon("randomtech:blockMachine");
        icons[1] = register.registerIcon("randomtech:blockSmithySidesUnlit");
        icons[2] = register.registerIcon("randomtech:blockSmithySides");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return icons[0];
        } else {
            return icons[meta + 1];
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int meta, float what, float these, float are) {
        if (player.isSneaking()) {
            return false;
        }

        TileSmithy smithy = (TileSmithy) world.getTileEntity(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);

        if (PlayerUtils.isHolding(player, Items.flint_and_steel)
                && !player.capabilities.isCreativeMode) {
            smithy.burnTime += 60;
            player.getHeldItem().damageItem(10, player);
            return true;
        } else if (PlayerUtils.isHolding(player,
                                         Item.getItemFromBlock(Blocks.iron_ore))
                && metadata == 1) {
            if (smithy.state == 0) {
                smithy.state = 1;
                smithy.metalType = 1;
                player.getHeldItem().stackSize--;
            }
            return true;
        } else if (PlayerUtils.isHolding(player,
                                         Item.getItemFromBlock(Blocks.gold_ore))
                && metadata == 1) {
            if (smithy.state == 0) {
                smithy.state = 1;
                smithy.metalType = 2;
                player.getHeldItem().stackSize--;
            }
            return true;
        } else if (PlayerUtils.isHolding(player,
                                         Item.getItemFromBlock(RandomTechBlocks.nulliumOre))
                && metadata == 1) {
            if (smithy.state == 0) {
                smithy.state = 1;
                smithy.metalType = 3;
                player.getHeldItem().stackSize--;
            }
            return true;
        } else if (PlayerUtils.isHolding(player, RandomTechItems.itemDebugger)) {
            smithy.state = 0;
            smithy.metalType = 0;
        } else if (smithy.state == 3) {
            smithy.pop();
        }

        return false;
    }

    @Override
    public boolean addHitEffects(World world, MovingObjectPosition target,
                                 EffectRenderer effectRenderer) {
        return (target.sideHit == 1);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z,
                               EntityPlayer player) {
        if (player.getHeldItem() != null && player.getHeldItem().stackSize > 0) {
            if (player.getHeldItem().getItem() instanceof ItemHammer) {
                TileSmithy smithy = (TileSmithy) world.getTileEntity(x, y, z);
                if (smithy.state > 1 && smithy.cooldown <= 0) {
                    smithy.cooldown = (int) (20 / ((EnchantmentHelper
                            .getEnchantmentLevel(
                                    Enchantment.efficiency.effectId,
                                    player.getHeldItem()) / 2.0f) + 1));
                    smithy.setProgress(smithy.getProgress()
                                               + ((ItemHammer) player.getHeldItem().getItem()).efficiency
                                               + EnchantmentHelper.getEnchantmentLevel(
                            Enchantment.fortune.effectId,
                            player.getHeldItem()));
                    world.playSoundAtEntity(player,
                                            RandomTech.modid + ":forge",
                                            1.0f + (float) Math.random(), 1.0f);
                    if (!player.capabilities.isCreativeMode) {
                        player.getHeldItem().damageItem(1, player);
                    }

                    if (smithy.state == 2) {
                        int particles = (int) (Math.random() * 15) + 10;
                        for (int i = 0; i < particles; i++) {
                            world.spawnParticle(
                                    "blockcrack_"
                                            + Block.getIdFromBlock(Blocks.lava)
                                            + "_0", x + 0.5
                                            + (Math.random() - 0.5), y + 1, z
                                            + 0.5 + (Math.random() - 0.5),
                                    4 * (Math.random() - 0.5),
                                    Math.random() * 4,
                                    4 * (Math.random() - 0.5));
                        }
                    }
                } else if (smithy.cooldown > 0) {
                    if (smithy.cooldown > 20
                            && !(EnchantmentHelper.getEnchantmentLevel(
                            Enchantment.silkTouch.effectId,
                            player.getHeldItem()) > 0)) {
                        for (int i = 0; i < 25; i++) {
                            String particleName = "";
                            if (smithy.state == 2) {
                                particleName = "iconcrack_"
                                        + Item.getIdFromItem(RandomTechItems.moltenIngot)
                                        + "_0";
                            } else if (smithy.state == 3) {
                                if (smithy.metalType == 1) {
                                    particleName = "iconcrack_"
                                            + Item.getIdFromItem(RandomTechItems.hardenedMetalIngot)
                                            + "_0";
                                } else {
                                    particleName = "iconcrack_"
                                            + Item.getIdFromItem(RandomTechItems.hardenedMagicalIngot)
                                            + "_0";
                                }
                            }
                            world.spawnParticle(particleName,
                                                x + 0.5 + (Math.random() - 0.5), y + 1, z
                                                        + 0.5 + (Math.random() - 0.5),
                                                4 * (Math.random() - 0.5),
                                                Math.random() * 4,
                                                4 * (Math.random() - 0.5));
                        }
                        world.playSoundAtEntity(player,
                                                "minecraft:random.break", 1.0f, 1.0f);
                        smithy.state = 0;
                        smithy.metalType = 0;
                        smithy.setProgress(0);
                        smithy.cooldown = -5;
                    }
                    smithy.cooldown += 5;
                }

                world.markBlockForUpdate(x, y, z);
                smithy.markDirty();
            }
        }
        if (player.getHeldItem() != null && player.getHeldItem().stackSize == 0) {
            player.inventory.setInventorySlotContents(
                    player.inventory.currentItem, null);
        }
        PlayerUtils.updateHeldItem(player);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 15 * world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z,
                                      Block neighbor) {
        if (!world.isRemote && world.isBlockIndirectlyGettingPowered(x, y, z)) {
            TileSmithy smithy = (TileSmithy) world.getTileEntity(x, y, z);
            if (smithy.state == 3) {
                smithy.pop();
                world.markBlockForUpdate(x, y, z);
                smithy.markDirty();
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z,
                                          int meta) {
        TileSmithy smithy = (TileSmithy) world.getTileEntity(x, y, z);
        float progressPercent = smithy.getProgress() / 2000.0f;
        return (int) Math.floor(progressPercent * 15);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileSmithy();
    }
}
