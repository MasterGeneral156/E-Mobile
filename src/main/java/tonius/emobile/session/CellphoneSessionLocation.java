package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class CellphoneSessionLocation extends CellphoneSessionBase {

    protected String unlocalizedLocation;
    protected int dimension;
    protected BlockPos blockpos;

    public CellphoneSessionLocation(EntityPlayerMP player, String unlocalizedLocation, int dimension,
                                    BlockPos block) {
        super(player);

        this.unlocalizedLocation = unlocalizedLocation;
        this.dimension = dimension;
        this.blockpos = block;

        ServerUtils.sendChatToPlayer(player, StringUtils.translate("chat.cellphone.start.location",
                StringUtils.translate(unlocalizedLocation)), TextFormatting.GOLD);
    }

    @Override
    public void tick() {
        if (!ServerUtils.isPlayerAlive(this.player)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isDimTeleportAllowed(this.player.dimension, this.dimension)) {
            ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                    "chat.cellphone.cancel.dimension",
                    this.player.worldObj.provider.getDimensionType().getName(),
                    this.player.mcServer.worldServerForDimension(this.dimension).provider.getDimensionType().getName()
            ), TextFormatting.RED);

            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.player);
            ServerUtils.sendDiallingParticles(this.dimension, blockpos);
        }

        super.tick();
    }

    @Override
    public void onCountdownFinished() {
        TeleportUtils.teleportPlayerToPos(this.player, this.dimension,
                this.blockpos.getX() + 0.5D, this.blockpos.getY() + 0.5D, this.blockpos.getZ() + 0.5D);

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                "chat.cellphone.success.location", StringUtils.translate(this.unlocalizedLocation)
        ), TextFormatting.GOLD);
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate("chat.cellphone.cancel"), TextFormatting.RED);
    }

}
