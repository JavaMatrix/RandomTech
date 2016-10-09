package io.github.javamatrix.randomtech;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.javamatrix.randomtech.compat.NEICompat;
import io.github.javamatrix.randomtech.gui.GUIHandler;
import io.github.javamatrix.randomtech.potions.PotionStrider;
import io.github.javamatrix.randomtech.proxy.CommonProxy;
import io.github.javamatrix.randomtech.util.PowerCards;
import net.minecraftforge.common.MinecraftForge;
import tconstruct.tools.entity.FancyEntityItem;

/**
 * The main mod class - it defines all of the items, blocks, and tileentities,
 * as well as plugging in hooks (to mix metaphors), adding recipes, and the
 * like.
 *
 * @author JavaMatrix
 */
@Mod(modid = RandomTech.modid, version = RandomTech.version, name = RandomTech.name)
public class RandomTech {

    // Basic mod data.
    public static final String modid = "RandomTech";
    public static final String version = "1.7b4";
    public static final String name = "RandomTech";
    public static final boolean researchDisabled = true;

    // Where configs are stored.
    public static String configPath = "";

    // The proxy that handles rendering stuff.
    @SidedProxy(serverSide = "io.github.javamatrix.randomtech.proxy.CommonProxy", clientSide = "io.github.javamatrix.randomtech.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Instance("RandomTech")
    public static RandomTech instance;

    /**
     * Happens before initialization. Used to fetch the config path, register
     * blocks/items, and initialize the GuiHandler and the proxy.
     *
     * @param event The event being handled.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Grab the config path.
        configPath = event.getModConfigurationDirectory().getAbsolutePath();

        // Register items and blocks and entities.
        Registration.registerItems();
        Registration.registerBlocks();
        Registration.registerEntities();

        // Register our one potion effect.
        Registration.potionStrider = new PotionStrider();

        // Init the GuiHandler.
        // TODO GuiHandler.init();

        // Register any custom splashes with Neptune, if it's installed.
        Registration.registerNeptuneSplashes();

        // Register the items.
        proxy.registerRenderers();
    }

    /**
     * Called as FML initializes Minecraft. Used to make recipes and register
     * event hooks.
     *
     * @param event The event being handled.
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        RandomTechHooks rth = new RandomTechHooks();
        MinecraftForge.EVENT_BUS.register(rth);
        FMLCommonHandler.instance().bus().register(rth);
        GameRegistry.registerWorldGenerator(rth, 0);

        // Register the furnace fuel handler.
        GameRegistry.registerFuelHandler(new RandomTechHooks());

        // Hand over recipe stuff to Registration.
        Registration.registerRecipes();

        // Soft depends for NEI
        if (Loader.isModLoaded("NotEnoughItems")) {
            NEICompat.load();
        }

        // Set up the GUI Handler.
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

        // Register the FancyEntityItem entity.
        EntityRegistry.registerModEntity(FancyEntityItem.class, "Fancy Item",
                                         0, this, 32, 5, true);

        // Initialize all power cards.
        PowerCards.init();
    }
}
