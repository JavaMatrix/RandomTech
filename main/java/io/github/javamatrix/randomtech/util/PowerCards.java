package io.github.javamatrix.randomtech.util;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerCards {
    public static Map<String, Data> powerCards = new HashMap<String, Data>();

    public static void init() {
        // Remember to sort by generation weight.
        addCard("Increased Vorpality", new Data(1, 1, 32,
                                                "+{amount}% to all damage done"));
        addCard("Excessive Force", new Data(1, 1, 16,
                                            "+{amount} knockback to all mobs"));
        addCard("Slice and Dice", new Data(1, 1, 16,
                                           "+{amount}% damage to zombie type mobs"));
        addCard("Volcanic Glaze", new Data(1, 1, 16, "+{amount} fire per hit"));
        addCard("Balanced Grip", new Data(1, 2, 8,
                                          "-{amount}% exhaustion from attacking"));
        addCard("Acupuncture", new Data(1, 2, 8,
                                        "+{amount}% critical hit damage"));
        addCard("Reach", new Data(1, 2, 8, "+{amount} blocks of reach"));
        addCard("Pedelepus",
                new Data(1, 2, 8, "+{amount}% critical hit chance"));
        addCard("Piercing Strikes", new Data(1, 3, 4,
                                             "-{amount} ticks of enemy invulnerability"));
        addCard("Defuser", new Data(1, 3, 4,
                                    "{amount}% chance to defuse Creepers"));
        addCard("Siphon", new Data(1, 4, 1,
                                   "Heal by {amount}% of damage done while sprinting."));
        addCard("Arlon's Touch", new Data(1, 4, 1,
                                          "{amount}% chance to inflict Wither on enemies"));
        addCard("Smash", new Data(1, 4, 1, "{amount}% of damage in AoE"));
        addCard("Aurora's Wrath", new Data(1, 4, 1,
                                           "+{amount}% damage during sunrise"));
        addCard("Power of Neptune", new Data(1, 4, 1,
                                             "+{amount}% damage in Ocean-type biomes."));
    }

    private static void addCard(String name, Data card) {
        powerCards.put(name, card);
    }

    public static void addToLore(String cardName, int rank, String prefix,
                                 List lore, boolean desc) {
        Data cardData = powerCards.get(cardName);

        if (rank > 0) {
            lore.add(prefix + EnumChatFormatting.RESET + "Rank " + rank);
        } else {
            lore.add(prefix + EnumChatFormatting.RESET + "Unranked");
        }

        if (desc) {
            String am = Float.toString(getAmount(cardName, rank));
            int dot = am.indexOf('.') + 1;
            // Trim long decimals.
            if (dot != 0) {
                if (am.substring(dot).length() > 2) {
                    am = am.substring(0, dot) + am.substring(dot, dot + 3);
                }
            }

            lore.add(prefix + EnumChatFormatting.RESET
                             + EnumChatFormatting.AQUA
                             + cardData.desc.replace("{amount}", am));
        }
        String compat = "Galatine";
        if (cardData.compatId == 2) {
            compat = "Battlesuit";
        }
        lore.add(prefix + EnumChatFormatting.RESET + "Compatible With: "
                         + EnumChatFormatting.AQUA + compat);
        String[] rarityNames = {"missingno",
                EnumChatFormatting.DARK_GRAY + "Common",
                EnumChatFormatting.GRAY + "Uncommon",
                EnumChatFormatting.GOLD + "Rare",
                EnumChatFormatting.BOLD + "Legendary"};
        lore.add(prefix + EnumChatFormatting.RESET + "Rarity: "
                         + rarityNames[cardData.rarity]);
    }

    public static float getAmount(String cardName, int rank) {
        if (rank < 0) {
            return 0;
        }

        if (cardName.equals("Slice and Dice")) {
            return (int) (30 + (70 * (rank / 5.0f)));
        } else if (cardName.equals("Balanced Grip")) {
            return (int) (25 + (65 * (rank / 5.0f)));
        } else if (cardName.equals("Increased Vorpality")) {
            return (int) (10 + (90 * rank / 5.0f));
        } else if (cardName.equals("Excessive Force")) {
            return (0.1f + (2.9f * rank / 5.0f));
        } else if (cardName.equals("Volcanic Glaze")) {
            return (int) (40 + (80 * rank / 5.0f));
        } else if (cardName.equals("Piercing Strikes")) {
            return (int) (3 + (15 * (rank / 5.0f)));
        } else if (cardName.equals("Siphon")) {
            return (int) (3 + (17 * (rank / 5.0f)));
        } else if (cardName.equals("Reach")) {
            return (0.5f + (2.5f * (rank / 5.0f)));
        } else if (cardName.equals("Arlon's Touch")) {
            return (int) (2 + (13.0f * (rank / 5.0f)));
        } else if (cardName.equals("Pedelepus")) {
            return (int) (1 + (9 * (rank / 5.0f)));
        } else if (cardName.equals("Acupuncture")) {
            return (int) (10 + (90 * (rank / 5.0f)));
        } else if (cardName.equals("Defuser")) {
            return (int) (10 + (90 * (rank / 5.0f)));
        } else if (cardName.equals("Smash")) {
            return (int) (25 + (50 * (rank / 5.0f)));
        } else if (cardName.equals("Aurora's Wrath")) {
            return (int) (5 + (45 * (rank / 5.0f)));
        } else if (cardName.equals("Power of Neptune")) {
            return (int) (5 + (45 * (rank / 5.0f)));
        }
        return 0;
    }

    public static ItemStack makeRandomCard() {
        NBTTagCompound nbt = new NBTTagCompound();
        ItemStack retVal = new ItemStack(RandomTechItems.powerCard);
        if (retVal.hasTagCompound()) {
            nbt = retVal.getTagCompound();
        }

        int totalWeight = 0;
        for (Data d : powerCards.values()) {
            totalWeight += d.generationWeight;
        }

        double backCounter = Math.random() * totalWeight;
        String chosen = null;
        for (String key : powerCards.keySet()) {
            backCounter -= powerCards.get(key).generationWeight;

            if (backCounter <= 0) {
                chosen = key;
                break;
            }
        }

        nbt.setString("cardName", chosen);
        nbt.setInteger("cardRank", 0);

        retVal.setTagCompound(nbt);
        return retVal;
    }

    public static class Data {
        /**
         * 1 = Galatine. 2 = Battlesuit
         */
        public int compatId = 0;

        /**
         * 1 = Common 2 = Uncommon 3 = Rare 4 = Legendary
         */
        public int rarity = 0;

        /**
         * Higher weights will generate more often.
         */
        public int generationWeight = 0;

        /**
         * A brief description of the power card.
         */
        public String desc = "missingno";

        public Data(int cI, int rar, int gW, String d) {
            compatId = cI;
            rarity = rar;
            generationWeight = gW;
            desc = d;
        }
    }
}
