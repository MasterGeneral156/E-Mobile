package tonius.emobile.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tonius.emobile.EMobile;

import java.util.ArrayList;
import java.util.List;

public class EMConfig {

    public static Configuration config;
    public static List<ConfigSection> configSections = new ArrayList<>();

    public static final ConfigSection sectionGeneral = new ConfigSection("General Settings", "general");
    public static final ConfigSection sectionTweaks = new ConfigSection("Tweaks Settings", "tweaks");

    // General settings
    public static boolean allowTeleportPlayers = true;
    public static boolean allowTeleportHome = true;
    public static boolean allowTeleportSpawn = true;
    public static int[] dimensionsBlacklist = new int[0];
    public static Boolean dimensionsWhitelist = false;
    public static String[] bedBlocks = new String[] {
            "com.carpentersblocks.block.BlockCarpentersBed"
    };

    // Tweaks
    public static ValueWithDefault<Integer> enderPearlStackSize = new ValueWithDefault<>(16);

    public static void preInit(FMLPreInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(new EMConfig());
        config = new Configuration(evt.getSuggestedConfigurationFile());

        EMobile.logger.info("Loading configuration file");
        syncConfig(false);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent evt) {
        if (evt.getModID().equals(EMobile.MODID)) {
            syncConfig(true);
        }
    }

    public static void syncConfig(boolean logUpdating) {
        if (logUpdating) {
            EMobile.logger.info("Updating configuration file");
        }

        try {
            processConfigOptions();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void processConfigOptions() {
        // General settings
        allowTeleportPlayers = config.get(sectionGeneral.name,
                "Allow teleporting to players", allowTeleportPlayers,
                "When enabled, the Ender Cellphone may be used to teleport to other players."
        ).getBoolean();

        allowTeleportHome = config.get(sectionGeneral.name,
                "Allow teleporting home", allowTeleportHome,
                "When enabled, the Ender Cellphone may be used by players to teleport to their beds."
        ).getBoolean();

        allowTeleportSpawn = config.get(sectionGeneral.name,
                "Allow teleporting to spawn", allowTeleportSpawn,
                "When enabled, the Ender Cellphone may be used to teleport to the world spawn."
        ).getBoolean();

        dimensionsBlacklist = config.get(sectionGeneral.name,
                "Dimensions Blacklist", dimensionsBlacklist,
                "List of IDs of dimensions that may NOT be teleported to or from using the Ender Cellphone. "
        ).getIntList();

        dimensionsWhitelist = config.get(sectionGeneral.name,
                "Dimensions Blacklist is Whitelist", dimensionsWhitelist,
                "If enabled, the dimensions blacklist will be treated as a whitelist instead. " +
                        "The dimensions will then be the ONLY dimensions that may be teleported to or from using " +
                        "the Ender Cellphone."
        ).getBoolean();

        bedBlocks = config.get(sectionGeneral.name,
                "Bed Blocks", bedBlocks,
                "A list of full class names of blocks that count as beds. Use this to add support for modded beds."
        ).getStringList();

        // Tweaks
        enderPearlStackSize.value = config.get(sectionTweaks.name,
                "Ender Pearl stack size", enderPearlStackSize.defaultValue,
                "This config option can be used to change the maximum stack size of Ender Pearls."
        ).setMinValue(1).setMaxValue(512).setRequiresMcRestart(true).getInt();
    }

    public static class ConfigSection {
        private final String name;
        private final String id;

        public ConfigSection(String name, String id) {
            this.name = name;
            this.id = id;
            EMConfig.configSections.add(this);
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    public static class ValueWithDefault<T> {
        private final T defaultValue;
        private T value;

        public ValueWithDefault(T defaultValue) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

}
