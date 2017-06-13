package tonius.emobile.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tonius.emobile.EMobile;
import tonius.emobile.network.message.toclient.MessageConfigSync;
import tonius.emobile.network.message.toclient.MessageDiallingParticles;
import tonius.emobile.network.message.toclient.MessageDiallingSound;
import tonius.emobile.network.message.toclient.MessageTeleportParticles;
import tonius.emobile.network.message.toserver.*;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(EMobile.MODID);

    public static void preInit() {
        EMobile.logger.info("Registering network messages");
        INSTANCE.registerMessage(MessageCellphonePlayer.class, MessageCellphonePlayer.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageCellphoneAuthorize.class, MessageCellphoneAuthorize.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessageCellphoneSpawn.class, MessageCellphoneSpawn.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageCellphoneHome.class, MessageCellphoneHome.class, 3, Side.SERVER);
        INSTANCE.registerMessage(MessageCellphoneCancel.class, MessageCellphoneCancel.class, 4, Side.SERVER);
        INSTANCE.registerMessage(MessageConfigSync.class, MessageConfigSync.class, 5, Side.CLIENT);
        INSTANCE.registerMessage(MessageDiallingSound.class, MessageDiallingSound.class, 6, Side.CLIENT);
        //INSTANCE.registerMessage(MessageDiallingParticles.class, MessageDiallingParticles.class, 7, Side.CLIENT);
        INSTANCE.registerMessage(MessageTeleportParticles.class, MessageTeleportParticles.class, 8, Side.CLIENT);
    }
}
