package io.github.javamatrix.randomtech.gui;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.container.ContainerSynthesisMachine;
import io.github.javamatrix.randomtech.recipes.SynthesisRecipes;
import io.github.javamatrix.randomtech.tileentities.TileSynthesisMachine;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSynthesisMachine extends GuiEnergetic {
    public ResourceLocation synthesisMachineGuiTextures = new ResourceLocation(
            RandomTech.modid.toLowerCase(), "textures/gui/synthesisMachine.png");
    public TileSynthesisMachine tesm;

    public GuiSynthesisMachine(EntityPlayer player,
                               TileSynthesisMachine tile) {
        super(new ContainerSynthesisMachine(player, tile), tile);
        this.tesm = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String string = I18n.format(te.getInventoryName());
        this.fontRendererObj.drawString(string,
                                        (this.xSize - this.fontRendererObj.getStringWidth(string)) / 2,
                                        6, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float deltaTimeMS,
                                                   int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(deltaTimeMS, mouseX, mouseY);
        mc.renderEngine.bindTexture(getResourceLocation());
        this.drawTexturedModalRect(xMid + 82, yMid + 32, 176, 80, getWorkProgress(), 22);
    }

    private int getWorkProgress() {
        SynthesisRecipes.RecipeResult recipe = SynthesisRecipes.instance().getResult(
                tesm);
        if (recipe == null) {
            return 0;
        }

        float progressF = (float) tesm.currentWorkDone
                / (float) recipe.getWorkRequired();
        return (int) (progressF * 54);
    }

    @Override
    protected int getRFHeight() {
        return 68;
    }

    @Override
    protected int getRFPositionX() {
        return 8;
    }

    @Override
    protected int getRFPositionY() {
        return 8;
    }

    @Override
    protected int getRFPositionU() {
        return 176;
    }

    @Override
    protected int getRFPositionV() {
        return 0;
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return synthesisMachineGuiTextures;
    }
}
