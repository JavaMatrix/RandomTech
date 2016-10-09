package io.github.javamatrix.randomtech.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionStrider extends Potion {

    private static final ResourceLocation customIcons = new ResourceLocation(
            "randomtech", "textures/gui/customPotionIcons.png");

    public PotionStrider() {
        super(getNextAvailableID(), false, 0xFF9900);
        setIconIndex(0, 0);
        setPotionName("potion.strider");
    }

    public static int getNextAvailableID() {
        int id;
        for (id = 0; id < Potion.potionTypes.length; id++) {
            if (Potion.potionTypes[id] == null) {
                break;
            }
        }
        return id;
    }

    @Override
    public boolean isReady(int i, int i2) {
        return i > 0;
    }

    @Override
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().renderEngine.bindTexture(customIcons);
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amp) {
        entity.stepHeight = (amp + 1);
        super.performEffect(entity, amp);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity,
                                                 BaseAttributeMap attributes, int level) {
        entity.stepHeight = (level + 1);
        entity.getEntityData();
        super.applyAttributesModifiersToEntity(entity, attributes, level);
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity,
                                                    BaseAttributeMap attributes, int level) {
        entity.stepHeight = 0.0f;
        super.applyAttributesModifiersToEntity(entity, attributes, level);
    }
}
