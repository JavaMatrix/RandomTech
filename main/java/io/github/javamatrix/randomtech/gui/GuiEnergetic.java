package io.github.javamatrix.randomtech.gui;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.container.ContainerEnergetic;
import io.github.javamatrix.randomtech.tileentities.TileEnergetic;
import io.github.javamatrix.randomtech.util.TextUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiEnergetic extends GuiBase {
    public static final int RF_TEXTURE_WIDTH = 16;
    private static final int LINE_HEIGHT = 11;
    protected TileEnergetic te;
    protected int xMid;
    protected int yMid;
    private ResourceLocation energeticGuiTextures = new ResourceLocation(
            RandomTech.modid.toLowerCase(), "textures/gui/genericEnergetic.png");

    public GuiEnergetic(EntityPlayer player,
                        TileEnergetic tile) {
        super(new ContainerEnergetic(player, tile));
        this.te = tile;
    }

    public GuiEnergetic(Container container, TileEnergetic tile) {
        super(container);
        this.te = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String string = I18n.format(te.getInventoryName());
        this.fontRendererObj.drawString(string,
                                        (this.xSize - this.fontRendererObj.getStringWidth(string)) / 2,
                                        6, 0x404040);
        if (isRFHovered(mouseX, mouseY)) {
            drawTooltip(TextUtils.formatRF(te.getEnergyStored(ForgeDirection.UNKNOWN)), mouseX, mouseY);
        }
    }

    private boolean isRFHovered(int mouseX, int mouseY) {
        mouseX -= xMid;
        mouseY -= yMid;
        return mouseX >= getRFPositionX() && mouseX <= getRFPositionX() + RF_TEXTURE_WIDTH
                && mouseY >= getRFPositionY() && mouseY <= getRFPositionY() + getRFHeight();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float deltaTimeMS,
                                                   int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(getResourceLocation());
        xMid = (this.width - this.xSize) / 2;
        yMid = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xMid, yMid, 0, 0, this.xSize, this.ySize);
        int rfPixels = getRFPixels();
        this.drawTexturedModalRect(xMid + getRFPositionX(), yMid + getRFPositionY() + (getRFHeight() - rfPixels),
                                   getRFPositionU(), getRFPositionV() + getRFHeight() - rfPixels,
                                   RF_TEXTURE_WIDTH, rfPixels);
    }

    private int getRFPixels() {
        float energyF = (float) te.getEnergyStored(ForgeDirection.UNKNOWN)
                / (float) te.getMaxEnergyStored(ForgeDirection.UNKNOWN);
        return (int) (energyF * getRFHeight());
    }

    protected int getRFHeight() {
        return 68;
    }

    protected int getRFPositionX() {
        return 80;
    }

    protected int getRFPositionY() {
        return 8;
    }

    protected int getRFPositionU() {
        return 176;
    }

    protected int getRFPositionV() {
        return 0;
    }

    protected ResourceLocation getResourceLocation() {
        return energeticGuiTextures;
    }
}
