package io.github.javamatrix.randomtech.items;

import io.github.javamatrix.randomtech.RandomTech;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemSuperFuel extends Item {

    IIcon[] icons = new IIcon[6];
    String[] names = new String[]{"fieryShard", "fieryCrystal",
            "burningCrystal", "blazingCrystal", "blazingGem", "infernoFuel"};

    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        icons[0] = reg.registerIcon(RandomTech.modid + ":fieryShard");
        icons[1] = reg.registerIcon(RandomTech.modid + ":fieryCrystal");
        icons[2] = reg.registerIcon(RandomTech.modid + ":burningCrystal");
        icons[3] = reg.registerIcon(RandomTech.modid + ":blazingCrystal");
        icons[4] = reg.registerIcon(RandomTech.modid + ":blazingGem");
        icons[5] = reg.registerIcon(RandomTech.modid + ":infernoFuel");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + names[stack.getItemDamage()];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < 6; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
