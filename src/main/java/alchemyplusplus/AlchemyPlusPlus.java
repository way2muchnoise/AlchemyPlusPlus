package alchemyplusplus;

import alchemyplusplus.handler.GUIHandler;
import alchemyplusplus.handler.DropHandler;
import alchemyplusplus.handler.FluidHandler;
import alchemyplusplus.handler.ZombieHandler;
import alchemyplusplus.helper.PotionRegistryHelper;
import alchemyplusplus.network.MessageHandler;
import alchemyplusplus.proxy.CommonProxy;
import alchemyplusplus.reference.Settings;
import alchemyplusplus.registry.BlockRegistry;
import alchemyplusplus.registry.ItemRegistry;
import alchemyplusplus.registry.MetaDataRegistry;
import alchemyplusplus.registry.PotionRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = AlchemyPlusPlus.ID, name = AlchemyPlusPlus.NAME, version = AlchemyPlusPlus.VERSION_FULL, useMetadata = false, guiFactory = "alchemyplusplus.gui.GUIFactory", acceptedMinecraftVersions = "[1.7.10,)")
public class AlchemyPlusPlus
{

    // Internal name for referring to the mod
    public static final String ID = "alchemyplusplus";
    // Set channel name for use in NetworkMod
    public static final String CHANNEL_NAME = ID;

    // Human readible mod name
    public static final String NAME = "AlchemyPlusPlus";

    public static final String V_MAJOR = "@MAJOR@";
    public static final String V_MINOR = "@MINOR@";
    public static final String V_REVIS = "@REVIS@";
    public static final String V_BUILD = "@BUILD@";
    public static final String VERSION_FULL = V_MAJOR + "." + V_MINOR + "." + V_REVIS + "." + V_BUILD;

    // Texture basedir
    public static final String textureBase = "minechem:";

    // Instancing
    @Instance(value = CHANNEL_NAME)
    public static AlchemyPlusPlus INSTANCE;

    public static GUIHandler guiHandler = new GUIHandler();


    /*
     * Herbs addon loaded
     */
    public static boolean herbs = false;
    @Instance(value = "alchemyplusplus")
    public static AlchemyPlusPlus instance = new AlchemyPlusPlus();
    @SidedProxy(clientSide = "alchemyplusplus.proxy.ClientProxy", serverSide = "alchemyplusplus.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(AlchemyPlusPlus.ID)
    public static ModMetadata metadata;

    // Logging
    public static final Logger LOGGER = LogManager.getLogger(AlchemyPlusPlus.ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Settings.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new Settings());
        MessageHandler.init();
        PotionRegistry.init();
        PotionRegistryHelper.expandPotionRegistry(256);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        INSTANCE = this;
        AlchemyPlusPlus.metadata = MetaDataRegistry.init(metadata);

        // Register GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(DropHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FluidHandler.INSTANCE);
        if (Settings.zombieMode)
        {
            ZombieHandler.INSTANCE.register();
        }

        proxy.registerRenderers();
        proxy.registerTileEntitys();

        // Register blocks and items
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();

        // Register recipes
        BlockRegistry.registerBlockRecipes();
        ItemRegistry.registerItemRecipes();

        if (Settings.DebugMode)
        {
            //ItemRegistry.registerHardcoreRecipes();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }

}
