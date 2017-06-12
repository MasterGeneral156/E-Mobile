package tonius.emobile.network.message.toclient;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.EMobile;
import tonius.emobile.config.EMConfig;

public class MessageConfigSync implements IMessage, IMessageHandler<MessageConfigSync, IMessage> {

    public boolean allowTeleportPlayers;
    public boolean allowTeleportHome;
    public boolean allowTeleportSpawn;

    @Override
    public void fromBytes(ByteBuf buf) {
        this.allowTeleportPlayers = buf.readBoolean();
        this.allowTeleportHome = buf.readBoolean();
        this.allowTeleportSpawn = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(EMConfig.allowTeleportPlayers);
        buf.writeBoolean(EMConfig.allowTeleportHome);
        buf.writeBoolean(EMConfig.allowTeleportSpawn);
    }

    @Override
    public IMessage onMessage(MessageConfigSync msg, MessageContext ctx) {
        EMConfig.allowTeleportPlayers = (msg.allowTeleportPlayers);
        EMConfig.allowTeleportHome = (msg.allowTeleportHome);
        EMConfig.allowTeleportSpawn = (msg.allowTeleportSpawn);

        EMobile.logger.info("Received server configuration");
        return null;
    }

}
