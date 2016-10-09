package io.github.javamatrix.randomtech.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEnergyFezItem extends ModelBase {
    //fields
    ModelRenderer tassle1;
    ModelRenderer FezStrand;
    ModelRenderer Fez;
    ModelRenderer tassle2;

    public ModelEnergyFezItem() {
        textureWidth = 64;
        textureHeight = 32;

        tassle1 = new ModelRenderer(this, 32, 10);
        tassle1.addBox(-0.5F, -1F, -0.5F, 1, 1, 1);
        tassle1.setRotationPoint(0F, 0F, 0F);
        tassle1.setTextureSize(64, 32);
        tassle1.mirror = true;
        setRotation(tassle1, 0F, 0F, 0F);
        FezStrand = new ModelRenderer(this, 32, 10);
        FezStrand.addBox(2F, 1F, 2.5F, 1, 3, 1);
        FezStrand.setRotationPoint(0F, 0F, 0F);
        FezStrand.setTextureSize(64, 32);
        FezStrand.mirror = true;
        setRotation(FezStrand, 0F, 0F, 0F);
        Fez = new ModelRenderer(this, 32, 0);
        Fez.addBox(-2.5F, 0F, -2.5F, 5, 5, 5);
        Fez.setRotationPoint(0F, 0F, 0F);
        Fez.setTextureSize(64, 32);
        Fez.mirror = true;
        setRotation(Fez, 0F, 0F, 0F);
        tassle2 = new ModelRenderer(this, 32, 10);
        tassle2.addBox(0.5F, -1F, 0.5F, 1, 1, 1);
        tassle2.setRotationPoint(0F, 0F, 0F);
        tassle2.setTextureSize(64, 32);
        tassle2.mirror = true;
        setRotation(tassle2, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        tassle1.render(f5);
        FezStrand.render(f5);
        Fez.render(f5);
        tassle2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
