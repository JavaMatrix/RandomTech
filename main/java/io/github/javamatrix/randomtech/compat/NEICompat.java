package io.github.javamatrix.randomtech.compat;

import codechicken.nei.api.ItemInfo;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import net.minecraft.item.ItemStack;

public class NEICompat {
    public static void load() {
        ItemInfo.hiddenItems.add(new ItemStack(RandomTechItems.moltenIngot));
    }
}
