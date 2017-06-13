package tonius.emobile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Logger;

import tonius.emobile.config.EMConfig;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.item.ItemRegistry;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.toclient.MessageConfigSync;
import tonius.emobile.session.CellphoneSessionsManager;

@Mod(modid = EMobile.MODID, version = EMobile.VERSION,
        guiFactory = "tonius.emobile.config.ConfigGuiFactoryEM",
        dependencies = EMobile.DEPENDENCIES)
public class EMobile {

    public static final String MODID = "emobile";
    public static final String PREFIX = MODID + ".";
    public static final String VERSION = "@VERSION@";
    //Requiring CTD Core just for initial porting, up to original
    //author if he wishes to keep this requirement or not.
    public static final String DEPENDENCIES = "required-after:ctdcore@[1.0.2,]";

    @Mod.Instance(EMobile.MODID)
    public static EMobile instance;
    @SidedProxy(serverSide = "tonius.emobile.CommonProxy", clientSide = "tonius.emobile.client.ClientProxy")
    public static CommonProxy proxy;
    public static Logger logger;

    public static SoundEvent phoneSound = null;
    public static SoundEvent phoneCountdownSound = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting E-Mobile");

        EMConfig.preInit(evt);

        //noinspection deprecation
        if (EMConfig.enderPearlStackSize.getValue() != Items.ENDER_PEARL.getItemStackLimit()) {
            logger.info("Changing Ender Pearl stack size to %d", EMConfig.enderPearlStackSize);
            Items.ENDER_PEARL.setMaxStackSize(EMConfig.enderPearlStackSize.getValue());
        }

        logger.info("Registering items");
        ItemRegistry.register();

        logger.info("Registering sounds");
        phoneSound = this.registerSound("phone");
        phoneCountdownSound = this.registerSound("phonecountdown");

        logger.info("Registering handlers");
        proxy.registerHandlers();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new EMGuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        logger.info("Registering recipes");

        GameRegistry.addRecipe(new ShapedOreRecipe(ItemRegistry.cellphone,
                " IS",
                "IPI",
                "IRI",
                'S', "stickWood",
                'I', "ingotIron",
                'P', Items.ENDER_PEARL,
                'R', Items.REDSTONE
        ));
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent evt) {
        CellphoneSessionsManager.clearSessions();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        PacketHandler.INSTANCE.sendTo(new MessageConfigSync(), (EntityPlayerMP) evt.player);
    }

    private SoundEvent registerSound(String sound) {
        ResourceLocation location = new ResourceLocation(MODID, sound);
        SoundEvent event = new SoundEvent(location);
        GameRegistry.register(event, location);

        return event;
    }

}
