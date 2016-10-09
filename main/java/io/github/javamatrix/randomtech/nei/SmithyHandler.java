package io.github.javamatrix.randomtech.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class SmithyHandler extends TemplateRecipeHandler {

    @Override
    public String getRecipeName() {
        return "Smithy";
    }

    @Override
    public String getGuiTexture() {
        return "randomtech:textures/gui/neiSmithy.png";
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 44);
    }

    @Override
    public void drawForeground(int recipe) {
        String hitDesc = ((CachedSmithyRecipe) arecipes.get(recipe)).hitDesc;
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int width = fr.getStringWidth(hitDesc);
        fr.drawString(hitDesc, (166 - width) / 2, (44 - fr.FONT_HEIGHT) / 2
                - (fr.FONT_HEIGHT / 2) - 2, 0x555555);
        width = fr.getStringWidth("Hits");
        fr.drawString("Hits", (166 - width) / 2, (44 - fr.FONT_HEIGHT) / 2
                + (fr.FONT_HEIGHT / 2), 0x555555);
        super.drawForeground(recipe);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == RandomTechItems.hardenedMetalIngot) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(Blocks.iron_ore),
                                                "50 - 2000", new ItemStack(
                    RandomTechItems.hardenedMetalIngot)));
        } else if (result.getItem() == RandomTechItems.hardenedMagicalIngot) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(Blocks.gold_ore),
                                                "50 - 2000", new ItemStack(
                    RandomTechItems.hardenedMagicalIngot)));
        } else if (result.getItem() == RandomTechItems.nulliumIngot) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(
                    RandomTechBlocks.nulliumOre), "2000", new ItemStack(
                    RandomTechItems.nulliumIngot)));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack used) {
        if (used.getItem() == Item.getItemFromBlock(Blocks.iron_ore)) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(Blocks.iron_ore),
                                                "50 - 2000", new ItemStack(
                    RandomTechItems.hardenedMetalIngot)));
        } else if (used.getItem() == Item.getItemFromBlock(Blocks.gold_ore)) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(Blocks.gold_ore),
                                                "50 - 2000=", new ItemStack(
                    RandomTechItems.hardenedMagicalIngot)));
        } else if (used.getItem() == Item
                .getItemFromBlock(RandomTechBlocks.nulliumOre)) {
            arecipes.add(new CachedSmithyRecipe(new ItemStack(
                    RandomTechBlocks.nulliumOre), "2000 Hits", new ItemStack(
                    RandomTechItems.nulliumIngot)));
        }
    }

    public class CachedSmithyRecipe extends CachedRecipe {
        PositionedStack input;
        PositionedStack output;
        String hitDesc;

        public CachedSmithyRecipe(ItemStack input, String hitDesc,
                                  ItemStack output) {
            this.input = new PositionedStack(input, 9, 13);
            this.hitDesc = hitDesc;
            this.output = new PositionedStack(output, 138, 13);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return Arrays.asList(new PositionedStack[]{input});
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

    }
}
