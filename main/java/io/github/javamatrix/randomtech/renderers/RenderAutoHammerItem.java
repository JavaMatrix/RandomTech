package io.github.javamatrix.randomtech.renderers;

import io.github.javamatrix.randomtech.model.ModelAutoHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderAutoHammerItem implements IItemRenderer {
    public static final int[] armAnimation = {0, 0, 0, 0, 0, 1, 3, 5, 7, 7, 7,
            7, 7, 6, 4, 2};

    ResourceLocation texture = new ResourceLocation("randomtech",
                                                    "textures/blocks/blockAutoHammer.png");
    ModelAutoHammer model = new ModelAutoHammer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
                                         ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        switch (type) {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.0, 1, 0.0);
                break;
            default:
                GL11.glTranslated(0.0, 1, 0.0);
                break;
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        int outness = armAnimation[(int) ((System.currentTimeMillis() / 50) % armAnimation.length)];
        model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F,
                     outness);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}