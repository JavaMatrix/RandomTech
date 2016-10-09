package io.github.javamatrix.randomtech.renderers;

import io.github.javamatrix.randomtech.model.ModelEnergyFezItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderEnergyFezInventory implements IItemRenderer {
    ResourceLocation texture = new ResourceLocation("randomtech",
                                                    "textures/model/armor/energy_fez_layer_1.png");
    ModelEnergyFezItem model = new ModelEnergyFezItem();

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
                GL11.glTranslated(0.75, 1.0, 0.75);
                GL11.glScaled(2.0d, 2.0d, 2.0d);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.0, 1.0, 0.0);
                GL11.glScaled(2.0d, 2.0d, 2.0d);
                break;
            case INVENTORY:
                GL11.glTranslated(0.0, 0.25, 0.0);
                GL11.glScaled(2.0d, 2.0d, 2.0d);
                break;
            case ENTITY:
                GL11.glTranslated(0.0, 0.5, 0.0);
                GL11.glScaled(2.0d, 2.0d, 2.0d);
                break;
            default:
                GL11.glTranslated(0.0, 1, 0.0);
                break;
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}