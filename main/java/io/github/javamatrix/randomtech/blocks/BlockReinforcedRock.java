package io.github.javamatrix.randomtech.blocks;

import io.github.javamatrix.randomtech.RandomTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockReinforcedRock extends Block {

    public BlockReinforcedRock() {
        super(Material.rock);
        // The entity argument isn't used, so we'll pass in null.
        // The actual resistance is divided by 5 in the function.
        setResistance(Blocks.obsidian.getExplosionResistance(null) * 5.0f);
        setHardness(6.0F);
        setStepSound(Block.soundTypeStone);
        setBlockName("reinforcedRock");
        setBlockTextureName(RandomTech.modid + ":blockReinforcedRock");
    }
}
