package io.github.javamatrix.randomtech.tileentities;

import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.util.BlockPosition;
import io.github.javamatrix.randomtech.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileSmithy extends TileBase {

    public static List<Material> heatSources = new ArrayList<Material>() {
        private static final long serialVersionUID = -1527238290724001695L;

        {
            add(Material.fire);
            add(Material.lava);
        }
    };

    // 0 = No ore, not doing anything.
    // 1 = Melting ore
    // 2 = Molten ingot, hammering.
    // 3 = Improving metal
    public int state = 0;

    // 0 = No metal
    // 1 = Iron (tough metal)
    // 2 = Gold (glittering metal)
    // 3 = Nullium
    public int metalType = 0;
    public int burnTime = 0;
    public int cooldown = 0;
    // Melting takes 100 progress ticks (5 seconds) to complete.
    // Molten ingots take 50 hits to turn to a usable ingot.
    // Every hit after that will improve metal quality.
    private float progress = 0;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float to) {
        progress = to;
        // Cause a redstone update.
        worldObj.func_147453_f(xCoord, yCoord, zCoord, RandomTechBlocks.smithy);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        nbt.setInteger("burnTime", burnTime);
        nbt.setInteger("state", state);
        nbt.setInteger("metalType", metalType);
        nbt.setFloat("progress", progress);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        burnTime = nbt.getInteger("burnTime");
        state = nbt.getInteger("state");
        metalType = nbt.getInteger("metalType");
        progress = nbt.getInteger("progress");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager netManager,
                             S35PacketUpdateTileEntity packet) {
        super.onDataPacket(netManager, packet);
        readFromNBT(packet.func_148857_g());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateEntity() {
        cooldown--;

        List<BlockPosition> neumannNeighbors = WorldUtils.buildNeighborList(
                worldObj, xCoord, yCoord, zCoord, false);

        boolean hasSource = false;

        for (BlockPosition pos : neumannNeighbors) {
            if (heatSources.contains(pos.getBlock(worldObj).getMaterial())) {
                hasSource = true;

                if (pos.getBlock(worldObj).getMaterial() == Material.lava) {
                    int chance = 6000;
                    List<BlockPosition> neighbors = WorldUtils
                            .buildNeighborList(worldObj, pos.x, pos.y, pos.z,
                                               true);
                    for (BlockPosition block : neighbors) {
                        if (block.getBlock(worldObj).getMaterial() == Material.lava) {
                            chance *= 1.2;
                        }
                    }
                    Random r = new Random(Sys.getTime());
                    if (r.nextInt(chance) == 314) {
                        pos.setBlock(worldObj, Blocks.cobblestone);
                    }
                }
            }
        }

        if (hasSource) {
            burnTime++;
        }

        if (state == 0 && hasSource) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord - 1.5,
                                                              yCoord - 1.5, zCoord - 1.5, xCoord + 1.5, yCoord + 1.5,
                                                              zCoord + 1.5);
            List<EntityItem> items = worldObj.getEntitiesWithinAABB(
                    Entity.class, aabb);
            for (int i = 0; i < items.size(); i++) {
                Entity e = items.get(i);
                System.out.println(e.getClass());
                if (!(e instanceof EntityItem)) {
                    continue;
                }
                EntityItem ei = (EntityItem) e;
                ItemStack s = ei.getEntityItem();
                System.out.println(s.getDisplayName());
                if (s.getItem().equals(Item.getItemFromBlock(Blocks.iron_ore))) {
                    state = 1;
                    metalType = 1;
                    ei.getEntityItem().stackSize--;
                    if (ei.getEntityItem().stackSize <= 0) {
                        // Kill the item.
                        ei.setDead();
                    }
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    break;
                } else if (s.getItem().equals(
                        Item.getItemFromBlock(Blocks.gold_ore))) {
                    state = 1;
                    metalType = 2;
                    ei.getEntityItem().stackSize--;
                    if (ei.getEntityItem().stackSize <= 0) {
                        // Kill the item.
                        ei.setDead();
                    }
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    break;
                } else if (s.getItem().equals(
                        Item.getItemFromBlock(RandomTechBlocks.nulliumOre))) {
                    state = 1;
                    metalType = 3;
                    ei.getEntityItem().stackSize--;
                    if (ei.getEntityItem().stackSize <= 0) {
                        // Kill the item.
                        ei.setDead();
                    }
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    break;
                }
            }
        } else if (state == 1) {
            if (burnTime > 0) {
                progress++;
                int maxProgress = 200;
                if (metalType == 3) {
                    // Nullium takes longer to melt.
                    maxProgress = 600;
                }
                if (progress >= maxProgress) {
                    state = 2;
                    progress = 0;
                }
            } else {
                if (progress > 0) {
                    progress--;
                } else {
                    pop();
                }
            }
        } else if (state == 2) {
            if (burnTime > 0) {
                int maxProgress = 50;
                if (metalType == 3) {
                    // Nullium takes longer.
                    maxProgress = 2000;
                }
                if (progress >= maxProgress) {
                    state = 3;

                    // Nullium doesn't have an improvement stage.
                    if (metalType == 3) {
                        pop();
                    }

                    progress = 0;
                }
            } else {
                pop();
            }
        } else if (state == 3) {
            if (burnTime <= 0) {
                pop();
            }
            if (progress >= 2000) {
                pop();
            }
        }

        if (burnTime > 0 || hasSource) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
            if (burnTime > 0) {
                burnTime--;
            }
        } else {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
        }

    }

    public void pop() {
        if (!worldObj.isRemote) {
            Item toPop = RandomTechItems.itemDebugger;
            int baseDurability = 0;
            if (state == 3) {
                if (metalType == 1) {
                    toPop = RandomTechItems.hardenedMetalIngot;
                    baseDurability = 85;
                } else if (metalType == 2) {
                    toPop = RandomTechItems.hardenedMagicalIngot;
                    baseDurability = 10;
                } else if (metalType == 3) {
                    toPop = RandomTechItems.nulliumIngot;
                    // Always 0 damage.
                    baseDurability = (int) -progress;
                }
            } else if (state == 1) {
                if (metalType == 1) {
                    toPop = Item.getItemFromBlock(Blocks.iron_ore);
                } else if (metalType == 2) {
                    toPop = Item.getItemFromBlock(Blocks.gold_ore);
                } else if (metalType == 3) {
                    toPop = Item.getItemFromBlock(RandomTechBlocks.nulliumOre);
                }
            } else {
                toPop = Items.flint;
            }
            EntityItem e = new EntityItem(worldObj, xCoord + 1.5, yCoord,
                                          zCoord, new ItemStack(toPop, 1, baseDurability
                    + (int) progress));
            e.motionX = 0;
            e.motionY = 0;
            e.motionZ = 0;
            worldObj.spawnEntityInWorld(e);
        }
        this.state = 0;
        this.metalType = 0;
        this.progress = 0;
        this.markDirty();
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
