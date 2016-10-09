package io.github.javamatrix.randomtech.items;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBucketOfBurblingInfinity extends ItemBucket {
    public ItemBucketOfBurblingInfinity() {
        super(Blocks.water);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity holding,
                         int meta, boolean isAMystery) {
        stack.setItemDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        // The first two seconds out of every one thousand seconds.
        if (System.currentTimeMillis() % 100000 < 2000) {
            return StatCollector
                    .translateToLocal("item.bucketOfBurblingInfinity.2.name");
        }
        return StatCollector
                .translateToLocal("item.bucketOfBurblingInfinity.name");
    }
}
