package tonius.emobile.network.message.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionLocation;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class MessageCellphoneSpawn implements IMessage, IMessageHandler<MessageCellphoneSpawn, IMessage> {

    private String player;

    public MessageCellphoneSpawn() {
    }

    public MessageCellphoneSpawn(String player) {
        this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.player);
    }

    @Override
    public IMessage onMessage(MessageCellphoneSpawn msg, MessageContext ctx) {
        if (EMConfig.allowTeleportSpawn) {
            EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
            if (player == null) {
                return null;
            } else if (!TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                ServerUtils.sendChatToPlayer(player, String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimension(), player.mcServer.worldServerForDimension(0).provider.getDimension()));
            } else {
                World world = player.mcServer.worldServerForDimension(0);
                BlockPos spawn = world.provider.getRandomizedSpawnPoint();
                if (player.worldObj.provider.canRespawnHere()) {
                    world = player.worldObj;
                    spawn = world.provider.getRandomizedSpawnPoint();
                }
                    if (!CellphoneSessionsManager.isPlayerInSession(player)) 
                    {
                        ItemStack heldItem = player.getHeldItemMainhand();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) 
                        {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).tryUseFuel(player)) 
                            {
                                ServerUtils.sendDiallingSound(player);
                                player.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
                                new CellphoneSessionLocation(player, "chat.cellphone.location.spawn", 0, spawn);
                            }
                        }
                    }
                }
            }
        return null;
    }
}
