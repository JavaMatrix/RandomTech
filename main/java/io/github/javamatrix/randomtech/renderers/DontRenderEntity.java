package io.github.javamatrix.randomtech.renderers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class DontRenderEntity extends Render {

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_,
                         double p_76986_4_, double p_76986_6_, float p_76986_8_,
                         float p_76986_9_) {
        // Do nothing.

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        // No texture needed to do nothing.
        return null;
    }

}
