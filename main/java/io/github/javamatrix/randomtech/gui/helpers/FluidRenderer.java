package io.github.javamatrix.randomtech.gui.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by JavaMatrix on 10/10/2016.
 * Licensed under Apache Commons 2.0.
 */
public class FluidRenderer {
    public static void renderFluidRect(Fluid fluid, int x, int y, int w, int h) {
        Tessellator tessellator = Tessellator.instance;

        IIcon icon = fluid.getIcon();

        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        for (int slicedBarY = y + h; slicedBarY > y; slicedBarY -= 16) {
            int slicedBarHeight = (int) Math.min(slicedBarY - y, 16.0f);
            for (int slicedBarX = x; slicedBarX < x + w; slicedBarX += 16) {
                int slicedBarWidth = (int) Math.min(slicedBarX - x, 16.0f);
                tessellator.startDrawingQuads();
                {
                    tessellator.addVertexWithUV(slicedBarX, slicedBarY, 0, minU, minV + (maxV - minV) * slicedBarHeight / 16.0f);
                    tessellator.addVertexWithUV(slicedBarX + slicedBarWidth, slicedBarY, 0, maxU - (maxU - minU) * slicedBarWidth / 16.0f, minV + (maxV - minV) * slicedBarHeight / 16.0f);
                    tessellator.addVertexWithUV(slicedBarX + slicedBarWidth, slicedBarY - slicedBarHeight, 0, maxU - (maxU - minU) * slicedBarWidth / 16.0f, minV);
                    tessellator.addVertexWithUV(slicedBarX, slicedBarY - slicedBarHeight, 0, minU, minV);
                }
                tessellator.draw();
            }
        }
    }
}
