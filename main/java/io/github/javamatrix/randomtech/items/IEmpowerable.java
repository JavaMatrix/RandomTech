package io.github.javamatrix.randomtech.items;

import net.minecraft.item.ItemStack;

public interface IEmpowerable {
    boolean canAddPC(ItemStack stack, String cardName, int cardRank);

    boolean addPC(ItemStack stack, String cardName, int cardRank);

    boolean hasPC(ItemStack stack, String cardName);

    boolean removePC(ItemStack stack, String cardName, int cardRank);

    String[][] getInstalledCards(ItemStack stack);
}
