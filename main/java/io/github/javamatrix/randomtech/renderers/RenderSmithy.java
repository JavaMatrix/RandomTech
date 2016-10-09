package io.github.javamatrix.randomtech.renderers;

import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.model.ModelOreCube;
import io.github.javamatrix.randomtech.tileentities.TileSmithy;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tconstruct.tools.entity.FancyEntityItem;

public class RenderSmithy extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z,
                                   float f) {
        // Grab the cast-down entity.
        TileSmithy smithy = (TileSmithy) te;

        // First we'll render the metal.
        if (smithy.state == 1) {
            ResourceLocation texture = new ResourceLocation(
                    "This is just so Eclipse doesn't freak.");

            if (smithy.metalType == 1) {
                texture = new ResourceLocation("randomtech",
                                               "textures/blocks/oreCubeIron.png");
            } else if (smithy.metalType == 2) {
                texture = new ResourceLocation("randomtech",
                                               "textures/blocks/oreCubeGold.png");
            } else if (smithy.metalType == 3) {
                texture = new ResourceLocation("randomtech",
                                               "textures/blocks/oreCubeNullium.png");
            }

            ModelOreCube model = new ModelOreCube();

            boolean airAbove = smithy.getWorldObj()
                    .getBlock(smithy.xCoord, smithy.yCoord + 1, smithy.zCoord)
                    .getMaterial() == Material.air;

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F,
                              (float) z + 0.5F);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GL11.glPushMatrix();

            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            if (!airAbove) {
                GL11.glScalef(0.75f, 0.75f, 0.75f);
                GL11.glTranslatef(0.0f, 3.0f / 16.0f, 0.0f);
            }

            float maxProgress = 200.0f;

            if (smithy.metalType == 3) {
                // Nullium takes longer to melt.
                maxProgress = 600.0f;
            }

            float p = smithy.getProgress() / maxProgress;
            GL11.glColor3f(1, 1 - p / 1.5F, 1 - p);
            model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glColor3f(1, 1, 1);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        } else if (smithy.state == 2) {
            GL11.glPushMatrix();
            float var10 = (float) (x - 0.5F);
            float var11 = (float) (y - 0.5F);
            float var12 = (float) (z - 0.5F);
            GL11.glTranslatef(var10, var11 + 0.05f, var12);
            renderItem(smithy, new ItemStack(RandomTechItems.moltenIngot));
            GL11.glPopMatrix();
        } else if (smithy.state == 3) {
            GL11.glPushMatrix();
            float var10 = (float) (x - 0.5F);
            float var11 = (float) (y - 0.5F);
            float var12 = (float) (z - 0.5F);
            GL11.glTranslatef(var10, var11 + 0.05f, var12);
            if (smithy.metalType == 1) {
                renderItem(smithy, new ItemStack(
                        RandomTechItems.hardenedMetalIngot));
            } else if (smithy.metalType == 2) {
                renderItem(smithy, new ItemStack(
                        RandomTechItems.hardenedMagicalIngot));
            }
            GL11.glPopMatrix();
        }
    }

    // From Tinker's Construct, credit goes to SlimeKnights. Under the CC0
    // license.
    // Edited a bit by JavaMatrix to add heat animation.
    void renderItem(TileSmithy smithy, ItemStack stack) {
        FancyEntityItem entityitem = new FancyEntityItem(smithy.getWorldObj(), stack);
        entityitem.getEntityItem().stackSize = 1;
        entityitem.hoverStart = 0.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef(1F, 1.5F, 0.6F);
        GL11.glRotatef(90F, 1, 0F, 0F);
        GL11.glScalef(1.8F, 1.8F, 1.8F);
        if (stack.getItem() instanceof ItemBlock) {
            GL11.glRotatef(90F, -1, 0F, 0F);
            GL11.glTranslatef(0F, -0.1F, 0.2275F);
        }

        RenderItem.renderInFrame = true;
        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D,
                                                      0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;

        GL11.glPopMatrix();
    }
}
