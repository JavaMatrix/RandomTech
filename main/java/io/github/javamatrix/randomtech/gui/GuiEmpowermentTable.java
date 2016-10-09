package io.github.javamatrix.randomtech.gui;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.container.ContainerEmpowermentTable;
import io.github.javamatrix.randomtech.tileentities.TileEmpowermentTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiEmpowermentTable extends GuiContainer {
    public ResourceLocation empowermentTableGuiTextures = new ResourceLocation(
            RandomTech.modid.toLowerCase(), "textures/gui/empowermentTable.png");
    public TileEmpowermentTable te;

    public GuiEmpowermentTable(EntityPlayer player,
                               TileEmpowermentTable tile) {
        super(new ContainerEmpowermentTable(player, tile));
        this.te = tile;
        ySize = 197;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String string = I18n.format(te.getInventoryName());
        this.fontRendererObj.drawString(string,
                                        (this.xSize - this.fontRendererObj.getStringWidth(string)) / 2,
                                        6, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float deltaTimeMS,
                                                   int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(empowermentTableGuiTextures);
        int xMid = (this.width - this.xSize) / 2;
        int yMid = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xMid, yMid, 0, 0, this.xSize, this.ySize);
    }
}
