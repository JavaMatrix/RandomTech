package io.github.javamatrix.randomtech.gui;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.container.ContainerEnergetic;
import io.github.javamatrix.randomtech.tileentities.TileEnergetic;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiEnergetic extends GuiContainer {
    public static final int LINE_HEIGHT = 11;
    public ResourceLocation energeticGuiTextures = new ResourceLocation(
            RandomTech.modid.toLowerCase(), "textures/gui/genericEnergetic.png");
    public TileEnergetic te;

    public GuiEnergetic(EntityPlayer player,
                        TileEnergetic tile) {
        super(new ContainerEnergetic(player, tile));
        this.te = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String string = I18n.format(te.getInventoryName());
        this.fontRendererObj.drawString(string,
                                        (this.xSize - this.fontRendererObj.getStringWidth(string)) / 2,
                                        6, 0x404040);
        if (isRFHovered(mouseX, mouseY)) {
            drawRFTooltip(mouseX, mouseY);
        }
    }

    private boolean isRFHovered(int mouseX, int mouseY) {
        int xMid = (this.width - this.xSize) / 2;
        int yMid = (this.height - this.ySize) / 2;
        mouseX -= xMid;
        mouseY -= yMid;
        return mouseX >= 79 && mouseX <= 96 && mouseY >= 7 && mouseY <= 76;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float deltaTimeMS,
                                                   int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(energeticGuiTextures);
        int xMid = (this.width - this.xSize) / 2;
        int yMid = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xMid, yMid, 0, 0, this.xSize, this.ySize);
        int rfPixels = getRFPixels();
        this.drawTexturedModalRect(xMid + 80, yMid + 8 + (68 - rfPixels), 176,
                                   68 - rfPixels, 16, rfPixels);
    }

    private int getRFPixels() {
        float energyF = (float) te.getEnergyStored(ForgeDirection.UNKNOWN)
                / (float) te.getMaxEnergyStored(ForgeDirection.UNKNOWN);
        return (int) (energyF * 68);
    }

    public void drawRFTooltip(int mouseX, int mouseY) {
        // Code modified from Zyin's at
        // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1437428-guide-1-7-2-how-to-make-button-tooltips
        String[] tooltipArray = new String[]{TextUtils.formatRF(te
                                                                        .getEnergyStored(ForgeDirection.UNKNOWN))};

        int tooltipWidth = mc.fontRenderer.getStringWidth(tooltipArray[0]);
        int tooltipHeight = mc.fontRenderer.FONT_HEIGHT - 2;

        int xMid = (this.width - this.xSize) / 2;
        int yMid = (this.height - this.ySize) / 2;

        int tooltipX = mouseX - xMid - tooltipWidth;
        int tooltipY = mouseY - yMid;

        if (tooltipX > width - tooltipWidth - 7)
            tooltipX = width - tooltipWidth - 7;
        if (tooltipY > height - tooltipHeight - 8)
            tooltipY = height - tooltipHeight - 8;

        // render the background inside box
        int innerAlpha = -0xFEFFFF0; // very very dark purple
        drawGradientRect(tooltipX, tooltipY - 1, tooltipX + tooltipWidth + 6,
                         tooltipY, innerAlpha, innerAlpha);
        drawGradientRect(tooltipX, tooltipY + tooltipHeight + 6, tooltipX
                                 + tooltipWidth + 6, tooltipY + tooltipHeight + 7, innerAlpha,
                         innerAlpha);
        drawGradientRect(tooltipX, tooltipY, tooltipX + tooltipWidth + 6,
                         tooltipY + tooltipHeight + 6, innerAlpha, innerAlpha);
        drawGradientRect(tooltipX - 1, tooltipY, tooltipX, tooltipY
                + tooltipHeight + 6, innerAlpha, innerAlpha);
        drawGradientRect(tooltipX + tooltipWidth + 6, tooltipY, tooltipX
                                 + tooltipWidth + 7, tooltipY + tooltipHeight + 6, innerAlpha,
                         innerAlpha);

        // render the background outside box
        int outerAlpha1 = 0x505000FF;
        int outerAlpha2 = (outerAlpha1 & 0xFEFEFE) >> 1 | outerAlpha1
                & -0x1000000;
        drawGradientRect(tooltipX, tooltipY + 1, tooltipX + 1, tooltipY
                + tooltipHeight + 6 - 1, outerAlpha1, outerAlpha2);
        drawGradientRect(tooltipX + tooltipWidth + 5, tooltipY + 1, tooltipX
                                 + tooltipWidth + 7, tooltipY + tooltipHeight + 6 - 1,
                         outerAlpha1, outerAlpha2);
        drawGradientRect(tooltipX, tooltipY, tooltipX + tooltipWidth + 3,
                         tooltipY + 1, outerAlpha1, outerAlpha1);
        drawGradientRect(tooltipX, tooltipY + tooltipHeight + 5, tooltipX
                                 + tooltipWidth + 7, tooltipY + tooltipHeight + 6, outerAlpha2,
                         outerAlpha2);

        // render the foreground text
        int lineCount = 0;
        for (String s : tooltipArray) {
            mc.fontRenderer.drawStringWithShadow(s, tooltipX + 2, tooltipY + 2
                    + lineCount * LINE_HEIGHT, 0xFFFFFF);
            lineCount++;
        }
    }
}
