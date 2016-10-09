package io.github.javamatrix.randomtech.renderers;

import io.github.javamatrix.randomtech.entities.effect.EntityBike;
import io.github.javamatrix.randomtech.model.ModelBike;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBike extends Render {

    public void doRender(EntityBike entity, double x, double y, double z, float f1,
                         float f2) {
        // Bind the texture.
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        ResourceLocation texture = new ResourceLocation("randomtech",
                                                        "textures/entity/bike.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        ModelBike model = ModelBike.getInstance();

        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        System.out.println("Rendering bicycle. Yay.");
    }

    protected ResourceLocation getEntityTexture(EntityBike entity) {
        return new ResourceLocation("randomtech", "textures/entity/bike.png");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return getEntityTexture((EntityBike) entity);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f1,
                         float f2) {
        doRender((EntityBike) entity, x, y, z, f1, f2);
    }

}
