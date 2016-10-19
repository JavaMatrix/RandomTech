package io.github.javamatrix.randomtech.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class ItemFactory {
    public static final int ATTRIBUTE_OPERATION_INT_BOOST = 0;
    public static final int ATTRIBUTE_OPERATION_PERCENT_BOOST = 1;
    public static final int ATTRIBUTE_OPERATION_MULTIPLY = 2;
    public ItemStack item;

    public ItemFactory(Item type) {
        item = new ItemStack(type);
    }

    public ItemFactory(Block type) {
        item = new ItemStack(type);
    }

    public ItemFactory setStackSize(int stackSize) {
        item.stackSize = stackSize;
        return this;
    }

    public ItemFactory setItemName(String itemName) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        NBTTagCompound display = new NBTTagCompound();
        if (nbt.hasKey("display")) {
            display = nbt.getCompoundTag("display");
        }

        display.setString("Name", itemName);

        nbt.setTag("display", display);
        item.setTagCompound(nbt);

        return this;
    }

    public ItemFactory setItemLore(String[] lore) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        NBTTagCompound display = new NBTTagCompound();
        if (nbt.hasKey("display")) {
            display = nbt.getCompoundTag("display");
        }

        NBTTagList loreList = new NBTTagList();
        for (String line : lore) {
            loreList.appendTag(new NBTTagString(line));
        }
        display.setTag("Lore", loreList);

        nbt.setTag("display", display);
        item.setTagCompound(nbt);

        return this;
    }

    public ItemFactory addEnchantment(int enchantment, int level) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        NBTTagList enchantments = new NBTTagList();
        if (nbt.hasKey("ench")) {
            enchantments = nbt.getTagList("ench", 10); // 10 = NBTTagCompound
        }

        NBTTagCompound enchant = new NBTTagCompound();
        enchant.setInteger("id", enchantment);
        enchant.setInteger("lvl", level);

        enchantments.appendTag(enchant);

        nbt.setTag("ench", enchantments);
        item.setTagCompound(nbt);

        return this;
    }

    public ItemFactory addPotionEffect(int potion, int level, int duration) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        NBTTagList potionEffects = new NBTTagList();
        if (nbt.hasKey("CustomPotionEffects")) {
            potionEffects = nbt.getTagList("CustomPotionEffects", 10); // 10 = NBTTagCompound
        }

        NBTTagCompound effect = new NBTTagCompound();
        effect.setInteger("Id", potion);
        effect.setInteger("Amplifier", level - 1);
        effect.setInteger("Duration", duration);

        potionEffects.appendTag(effect);

        nbt.setTag("CustomPotionEffects", potionEffects);
        item.setTagCompound(nbt);

        return this;
    }

    public ItemFactory addAttributeMod(String name, String attributeName, int operation, double value) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        NBTTagList attributeMods = new NBTTagList();
        if (nbt.hasKey("AttributeModifiers")) {
            attributeMods = nbt.getTagList("AttributeModifiers", 10); // 10 = NBTTagCompound
        }

        NBTTagCompound mod = new NBTTagCompound();
        mod.setString("Name", name);
        mod.setString("AttributeName", attributeName);
        mod.setDouble("Amount", value);
        mod.setInteger("Operation", operation);
        mod.setLong("UUIDMost", 894654);
        mod.setLong("UUIDLeast", 2872);

        attributeMods.appendTag(mod);

        nbt.setTag("AttributeModifiers", attributeMods);
        item.setTagCompound(nbt);

        return this;
    }

    public ItemStack finish() {
        return item;
    }

    public void addTag(String name, NBTBase tag) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (item.hasTagCompound()) {
            nbt = item.getTagCompound();
        }

        nbt.setTag(name, tag);

        item.setTagCompound(nbt);
    }

    public ItemFactory setDamage(int damage) {
        item.setItemDamage(damage);
        return this;
    }
}
