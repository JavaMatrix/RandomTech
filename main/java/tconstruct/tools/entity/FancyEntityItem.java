package tconstruct.tools.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class FancyEntityItem extends EntityItem {
    public FancyEntityItem(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
    }

    public FancyEntityItem(World par1World, ItemStack par8ItemStack) {
        this(par1World, 0, 0, 0);
        this.setEntityItemStack(par8ItemStack);
        this.lifespan = (par8ItemStack.getItem() == null ? 6000 : par8ItemStack.getItem().getEntityLifespan(par8ItemStack, par1World));
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        return par1DamageSource.getDamageType().equals("outOfWorld");
    }
}