package io.github.javamatrix.randomtech.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import io.github.javamatrix.randomtech.Registration.RandomTechBlocks;
import io.github.javamatrix.randomtech.Registration.RandomTechItems;
import io.github.javamatrix.randomtech.entities.effect.EntityBike;
import io.github.javamatrix.randomtech.model.ModelEnergyFez;
import io.github.javamatrix.randomtech.projectiles.EntityFlareRocket;
import io.github.javamatrix.randomtech.projectiles.EntityRailgunBolt;
import io.github.javamatrix.randomtech.projectiles.EntityTractorBeam;
import io.github.javamatrix.randomtech.projectiles.EntityUnclipperBullet;
import io.github.javamatrix.randomtech.renderers.*;
import io.github.javamatrix.randomtech.tileentities.TileAutoHammer;
import io.github.javamatrix.randomtech.tileentities.TileSmithy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import tconstruct.tools.entity.FancyEntityItem;
import tconstruct.tools.model.FancyItemRender;

public class ClientProxy extends CommonProxy {

    private static ModelBiped modelEnergyFez = new ModelEnergyFez();

    public static ModelBiped getEnergyFezModel() {
        return modelEnergyFez;
    }

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSmithy.class,
                                                     new RenderSmithy());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoHammer.class,
                                                     new RenderAutoHammer());
        MinecraftForgeClient.registerItemRenderer(
                Item.getItemFromBlock(RandomTechBlocks.autoHammer),
                new RenderAutoHammerItem());
        MinecraftForgeClient.registerItemRenderer(RandomTechItems.energyFez,
                                                  new RenderEnergyFezInventory());
        RenderingRegistry.registerEntityRenderingHandler(FancyEntityItem.class,
                                                         new FancyItemRender());
        RenderingRegistry.registerEntityRenderingHandler(
                EntityUnclipperBullet.class, new DontRenderEntity());
        RenderingRegistry.registerEntityRenderingHandler(
                EntityRailgunBolt.class, new RenderRailgunBolt());
        RenderingRegistry.registerEntityRenderingHandler(
                EntityTractorBeam.class, new DontRenderEntity());
        RenderingRegistry.registerEntityRenderingHandler(
                EntityFlareRocket.class, new RenderFlareRocket());
        RenderingRegistry.registerEntityRenderingHandler(EntityBike.class,
                                                         new RenderBike());
    }
}