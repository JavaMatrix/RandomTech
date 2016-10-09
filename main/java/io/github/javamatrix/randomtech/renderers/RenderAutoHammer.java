package io.github.javamatrix.randomtech.renderers;

import io.github.javamatrix.randomtech.model.ModelAutoHammer;
import io.github.javamatrix.randomtech.tileentities.TileAutoHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAutoHammer extends TileEntitySpecialRenderer {

    private static ModelAutoHammer model = new ModelAutoHammer();

    @Override
    public void renderTileEntityAt(TileEntity te, double x,
                                   double y, double z, float f) {
        // Cast down the TE.
        TileAutoHammer teah = (TileAutoHammer) te;

        // Bind the texture.
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        ResourceLocation texture = new ResourceLocation("randomtech",
                                                        "textures/blocks/blockAutoHammer.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, teah.getArmOutness());
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }

}
