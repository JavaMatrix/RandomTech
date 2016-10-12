package io.github.javamatrix.randomtech.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 * Created by JavaMatrix on 10/10/2016.
 * Licensed under Apache Commons 2.0.
 */
public abstract class GuiBase extends GuiContainer {

    public static final int LINE_HEIGHT = 11;

    public GuiBase(Container c) {
        super(c);
    }

    public void drawTooltip(String tip, int mouseX, int mouseY) {
        // Code modified from Zyin's at
        // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1437428-guide-1-7-2-how-to-make-button-tooltips

        String[] tooltipArray = new String[]{tip};

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
