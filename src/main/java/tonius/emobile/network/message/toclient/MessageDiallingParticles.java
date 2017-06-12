package tonius.emobile.network.message.toclient;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.EMobile;

public class MessageDiallingParticles implements IMessage, IMessageHandler<MessageDiallingParticles, IMessage> {

    public double posX;
    public double posY;
    public double posZ;
    
    public MessageDiallingParticles(BlockPos block) {
    }

    public MessageDiallingParticles(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
    }

    @Override
    public IMessage onMessage(MessageDiallingParticles msg, MessageContext ctx) {
        EMobile.proxy.showDiallingParticles(msg.posX, msg.posY, msg.posZ);
        return null;
    }

}
