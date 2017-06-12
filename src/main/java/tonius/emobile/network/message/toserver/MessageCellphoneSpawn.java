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
                ServerUtils.sendChatToPlayer(player.getName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimension(), player.mcServer.worldServerForDimension(0).provider.getDimension()), EnumChatFormatting.RED);
            } else {
                World world = player.mcServer.worldServerForDimension(0);
                BlockPos spawn = world.provider.getRandomizedSpawnPoint();
                if (player.worldObj.provider.canRespawnHere()) {
                    world = player.worldObj;
                    spawn = world.provider.getRandomizedSpawnPoint();
                }
                if (spawn != null) {
                    spawn.getY() = world.provider.getAverageGroundLevel();
                    Material mat = world.getBlockState(spawn).getMaterial();
                    Material mat2 = world.getBlockState(spawn).getMaterial();
                    if (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid()) {
                        do {
                            mat = world.getBlockState(spawn).getMaterial();
                            mat2 = world.getBlockState(spawn).getMaterial();
                            if (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid()) {
                                break;
                            }
                            spawn.getY()++;
                        } while (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid());
                    } else {
                        do {
                            mat = world.getBlockState(spawn).getMaterial();
                            mat2 = world.getBlockState(spawn).getMaterial();
                            if ((mat.isSolid() || mat.isLiquid()) && (mat2.isSolid() || mat2.isLiquid())) {
                                break;
                            }
                            spawn.getY()--;
                        } while (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid());
                    }
                    spawn.getY() += 0.2D;

                    if (!CellphoneSessionsManager.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getHeldItemMainhand();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).tryUseFuel(player)) {
                                ServerUtils.sendDiallingSound(player);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.spawn", player, 0, spawn.getX(), spawn.getY(), spawn.getZ());
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
