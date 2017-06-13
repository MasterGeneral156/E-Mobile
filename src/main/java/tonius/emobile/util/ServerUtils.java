package tonius.emobile.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.toclient.MessageDiallingParticles;
import tonius.emobile.network.message.toclient.MessageDiallingSound;
import tonius.emobile.network.message.toclient.MessageTeleportParticles;

public class ServerUtils {

    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static EntityPlayerMP getPlayerOnServer(String name) {
        return getServer().getPlayerList().getPlayerByUsername(name);
    }

    public static boolean isPlayerAlive(EntityPlayerMP player) {
        return player != null && getServer().getPlayerList().getPlayerList().contains(player) && player.isEntityAlive();
    }

    public static void sendChatToPlayer(EntityPlayerMP player, String chat, TextFormatting color) {
        if (player != null) {
            TextComponentString component = new TextComponentString(chat);
            component.getStyle().setColor(color);
            player.addChatMessage(component);
        }
    }

    public static void sendChatToPlayer(EntityPlayerMP player, String chat) {
        sendChatToPlayer(player, chat, TextFormatting.WHITE);
    }

    public static void sendDiallingSound(EntityPlayer player) {
        PacketHandler.INSTANCE.sendToAllAround(
                new MessageDiallingSound(player.getEntityId()),
                new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 256)
        );
    }

    public static void sendDiallingParticles(int dimension, BlockPos block) {
        PacketHandler.INSTANCE.sendToAllAround(
                new MessageDiallingParticles(block),
                new NetworkRegistry.TargetPoint(dimension, block.getX(), block.getY(), block.getZ(), 256)
        );
    }

    public static void sendDiallingParticles(int dimension, double d, double e, double f) {
        sendDiallingParticles(dimension, d + 0.5D, e + 0.5D, f + 0.5D);
    }

    public static void sendDiallingParticles(EntityPlayer player) {
        sendDiallingParticles(player.dimension, player.posX, player.posY + 0.8D, player.posZ);
    }

    public static void sendTeleportEffect(EntityPlayer player) {
        player.worldObj.playSound(null, player.getPosition(),
                SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

        PacketHandler.INSTANCE.sendToAllAround(
                new MessageTeleportParticles(player.posX, player.posY + 0.8D, player.posZ),
                new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 256)
        );
    }

}
