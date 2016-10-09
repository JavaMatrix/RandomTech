package io.github.javamatrix.randomtech.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes.DefaultSynthesisRecipes;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes.ISynthesisRecipeHandler;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes.RecipeResult;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SynthesisMachineHandler extends TemplateRecipeHandler {
    @Override
    public String getRecipeName() {
        return "Synthesis Machine";
    }

    @Override
    public String getGuiTexture() {
        return "randomtech:textures/gui/neiSynthesis.png";
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 44);
    }

    @Override
    public void drawForeground(int recipe) {
        int rf = ((CachedSynthesisRecipe) arecipes.get(recipe)).rf;
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(rf
                                                                                 + " RF");
        Minecraft.getMinecraft().fontRenderer.drawString(rf + " RF",
                                                         (166 - width) / 2, 45, 0x555555);
        super.drawForeground(recipe);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {

        if (result.getItem() == null)
            return;

        if (result.getItem() == RandomTechItems.hardenedMetalPlate) {
            ItemStack is = new ItemStack(RandomTechItems.hardenedMetalIngot);
            NBTTagCompound nbt = new NBTTagCompound();
            if (is.hasTagCompound()) {
                nbt = is.getTagCompound();
            }
            nbt.setInteger("MoreThan", 100);
            is.setTagCompound(nbt);
            ItemStack[] in = new ItemStack[]{is, is, is, is};
            arecipes.add(new CachedSynthesisRecipe(in, new RecipeResult(result,
                                                                        8000)));
        }

        DefaultSynthesisRecipes dsr = null;
        for (ISynthesisRecipeHandler handler : SynthesisRecipes.instance().listeners) {
            if (handler instanceof DefaultSynthesisRecipes) {
                dsr = (DefaultSynthesisRecipes) handler;
                break;
            }
        }
        for (Entry<ItemStack[], RecipeResult> recipes : dsr.simpleRecipes
                .entrySet()) {
            if (recipes.getValue().getResult().getItem() == result.getItem()) {
                arecipes.add(new CachedSynthesisRecipe(recipes.getKey(),
                                                       recipes.getValue()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack used) {
        if (used.getItem() == null)
            return;

        if (used.getItem() == RandomTechItems.hardenedMetalIngot) {
            ItemStack is = new ItemStack(RandomTechItems.hardenedMetalIngot);
            NBTTagCompound nbt = new NBTTagCompound();
            if (is.hasTagCompound()) {
                nbt = is.getTagCompound();
            }
            nbt.setInteger("MoreThan", 100);
            ItemStack[] in = new ItemStack[]{is, is, is, is};
            arecipes.add(new CachedSynthesisRecipe(in, new RecipeResult(used,
                                                                        8000)));
        }

        DefaultSynthesisRecipes dsr = null;
        for (ISynthesisRecipeHandler handler : SynthesisRecipes.instance().listeners) {
            if (handler instanceof DefaultSynthesisRecipes) {
                dsr = (DefaultSynthesisRecipes) handler;
                break;
            }
        }
        for (Entry<ItemStack[], RecipeResult> recipes : dsr.simpleRecipes
                .entrySet()) {
            for (ItemStack is : recipes.getKey()) {
                if (is.getItem() == used.getItem()) {
                    arecipes.add(new CachedSynthesisRecipe(recipes.getKey(),
                                                           recipes.getValue()));
                    break;
                }
            }
        }
    }

    public class CachedSynthesisRecipe extends CachedRecipe {
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        public int rf;

        public CachedSynthesisRecipe(ItemStack[] input, RecipeResult result) {
            if (result == null || input.length == 0) {
                return;
            }

            inputs.add(new PositionedStack(input[0], 19, 9));
            if (input.length > 1)
                inputs.add(new PositionedStack(input[1], 37, 9));
            if (input.length > 2)
                inputs.add(new PositionedStack(input[2], 19, 27));
            if (input.length > 3)
                inputs.add(new PositionedStack(input[3], 37, 27));
            output = new PositionedStack(result.getResult(), 127, 19);
            rf = result.getWorkRequired();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return inputs;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

    }
}
