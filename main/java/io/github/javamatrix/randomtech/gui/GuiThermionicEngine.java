package io.github.javamatrix.randomtech.gui;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.container.ContainerThermionicEngine;
import io.github.javamatrix.randomtech.tileentities.TileThermionicEngine;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

public class GuiThermionicEngine extends GuiEnergetic {
    public ResourceLocation thermionicEngineGuiTextures = new ResourceLocation(
            RandomTech.modid.toLowerCase(), "textures/gui/thermionic.png");
    public TileThermionicEngine tete;

    public GuiThermionicEngine(EntityPlayer player,
                               TileThermionicEngine tile) {
        super(new ContainerThermionicEngine(player, tile), tile);
        this.tete = tile;
        ySize = 172;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(getResourceLocation());
        this.drawTexturedModalRect(26, 8, 192, 0, 16, 68);
        this.drawTexturedModalRect(134, 8, 208, 0, 16, 68);

        int rft = 0;
        if (tete.hot.getFluid() != null && tete.cold.getFluid() != null) {
            int tempDif = tete.hot.getFluid().getFluid().getTemperature() - tete.cold.getFluid().getFluid().getTemperature();
            rft = tempDif / 25;
        }
        String string = rft + " RF/t";

        this.fontRendererObj.drawString(string,
                                        (this.xSize - this.fontRendererObj.getStringWidth(string)) / 2,
                                        79, 0x404040);

        if (isColdHovered(mouseX, mouseY)) {
            if (tete.cold.getFluid() == null) {
                drawTooltip(I18n.format("text.noliquid"), mouseX, mouseY);
            } else {
                drawTooltip(TextUtils.formatFluid(tete.cold.getFluid()), mouseX, mouseY);
            }
        }

        if (isHotHovered(mouseX, mouseY)) {
            if (tete.hot.getFluid() == null) {
                drawTooltip(I18n.format("text.noliquid"), mouseX, mouseY);
            } else {
                drawTooltip(TextUtils.formatFluid(tete.hot.getFluid()), mouseX, mouseY);
            }
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    protected boolean isColdHovered(int mouseX, int mouseY) {
        mouseX -= xMid;
        mouseY -= yMid;
        return mouseX >= 26 && mouseX <= 42
                && mouseY >= 8 && mouseY <= 76;
    }

    protected boolean isHotHovered(int mouseX, int mouseY) {
        mouseX -= xMid;
        mouseY -= yMid;
        return mouseX >= 134 && mouseX <= 150
                && mouseY >= 8 && mouseY <= 76;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float deltaTimeMS,
                                                   int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(deltaTimeMS, mouseX, mouseY);

        GL11.glDisable(GL11.GL_LIGHTING);
        if (tete.cold.getFluid() != null) {
            int empty = (int) ((1 - ((double) tete.cold.getFluidAmount() / TileThermionicEngine.FLUID_AMOUNT)) * 68);
            drawFluidBar(tete.cold.getFluid().getFluid(), 26 + xMid, 42 + xMid, 8 + yMid + empty, 76 + yMid);
        }

        if (tete.hot.getFluid() != null) {
            int empty = (int) ((1 - ((double) tete.hot.getFluidAmount() / TileThermionicEngine.FLUID_AMOUNT)) * 68);
            drawFluidBar(tete.hot.getFluid().getFluid(), 134 + xMid, 150 + xMid, 8 + yMid + empty, 76 + yMid);
        }

        this.mc.getTextureManager().bindTexture(getResourceLocation());
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawFluidBar(Fluid fluid, int barMinX, int barMaxX, int barMinY, int barMaxY) {
        IIcon progressBarIcon = fluid.getIcon();

        double minU = progressBarIcon.getMinU();
        double minV = progressBarIcon.getMinV();
        double maxU = progressBarIcon.getMaxU();
        double maxV = progressBarIcon.getMaxV();

        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;

        // Draw the bar in 16-pixel slices from the bottom up.
        for (int slicedBarY = barMaxY; slicedBarY > 0; slicedBarY -= 16) {
            int slicedBarHeight = (int) Math.min(slicedBarY - barMinY, 16.0f);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(barMinX, slicedBarY, zLevel, minU, minV + (maxV - minV) * slicedBarHeight / 16.0f);
            tessellator.addVertexWithUV(barMaxX, slicedBarY, zLevel, maxU, minV + (maxV - minV) * slicedBarHeight / 16.0f);
            tessellator.addVertexWithUV(barMaxX, slicedBarY - slicedBarHeight, zLevel, maxU, minV);
            tessellator.addVertexWithUV(barMinX, slicedBarY - slicedBarHeight, zLevel, minU, minV);
            tessellator.draw();
        }
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return thermionicEngineGuiTextures;
    }
}
