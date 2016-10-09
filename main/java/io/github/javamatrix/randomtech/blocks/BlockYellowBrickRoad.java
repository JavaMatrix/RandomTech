package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import io.github.javamatrix.randomtech.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BlockYellowBrickRoad extends Block {
    public BlockYellowBrickRoad() {
        super(Material.rock);

        setHardness(6.0F);
        setStepSound(Block.soundTypeStone);
        setBlockName("yellowBrickRoad");
        setBlockTextureName(RandomTech.modid + ":yellowBrickRoad");
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        // Stolen wholesale from
        // https://github.com/SlimeKnights/TinkersConstruct/blob/80efde613ac73e98279e1ab8adb1107638f1a0e4/src/main/java/tconstruct/smeltery/blocks/SpeedBlock.java,
        // but that's licensed under CC0, so it's ok.
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase el = (EntityLivingBase) entity;
            // Speed IX for 0.5 seconds = ~5x speed
            el.addPotionEffect(new PotionEffect(1, 10, 8, true));
            // Strider I for 0.5 seconds = +1 step height.
            el.addPotionEffect(new PotionEffect(Registration.potionStrider.id,
                                                1, 0, true));
        }
    }
}
