package tonius.emobile.network.message.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionPlayer;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class MessageCellphonePlayer implements IMessage, IMessageHandler<MessageCellphonePlayer, IMessage> {

    private String requesting;
    private String receiving;

    public MessageCellphonePlayer() {
    }

    public MessageCellphonePlayer(String requesting, String receiving) {
        this.requesting = requesting;
        this.receiving = receiving;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.requesting = ByteBufUtils.readUTF8String(buf);
        this.receiving = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.requesting);
        ByteBufUtils.writeUTF8String(buf, this.receiving);
    }

    @Override
    public IMessage onMessage(MessageCellphonePlayer msg, MessageContext ctx) {
        if (EMConfig.allowTeleportPlayers) {
            EntityPlayerMP requestingPlayer = ServerUtils.getPlayerOnServer(msg.requesting);
            EntityPlayerMP receivingPlayer = ServerUtils.getPlayerOnServer(msg.receiving);
            if (requestingPlayer == null) {
                return null;
            } else if (receivingPlayer == null) {
                ServerUtils.sendChatToPlayer(requestingPlayer, String.format(StringUtils.translate("chat.cellphone.tryStart.unknown"), msg.receiving));
            } else if (!TeleportUtils.isDimTeleportAllowed(requestingPlayer.dimension, receivingPlayer.dimension)) {
                ServerUtils.sendChatToPlayer(requestingPlayer, String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), requestingPlayer.worldObj.provider.getDimension(), receivingPlayer.worldObj.provider.getDimension()));
            } else {
                if (!CellphoneSessionsManager.isPlayerInSession(requestingPlayer)) {
                    if (CellphoneSessionsManager.isPlayerAccepted(receivingPlayer, requestingPlayer)) {
                        ItemStack heldItem = requestingPlayer.getActiveItemStack();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (requestingPlayer.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).tryUseFuel(requestingPlayer)) {
                                ServerUtils.sendDiallingSound(requestingPlayer);
                                new CellphoneSessionPlayer(requestingPlayer, receivingPlayer);
                            }
                        }
                    } else {
                        ServerUtils.sendChatToPlayer(requestingPlayer, String.format(StringUtils.translate("chat.cellphone.tryStart.unauthorized"), receivingPlayer.getName()));
                    }
                }
            }
        }

        return null;
    }

}
