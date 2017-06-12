package tonius.emobile.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tonius.emobile.EMobile;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.inventory.InventoryCellphone;
import tonius.emobile.util.StringUtils;

import javax.annotation.Nullable;

import com.themastergeneral.ctdcore.item.CTDItem;

public class ItemCellphone extends CTDItem {

    public ItemCellphone(String modid, String name) {
    	super(name, modid);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("emobile.cellphone");
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public ItemStack onItemRightClick(ItemStack cellphone, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(EMobile.instance, EMGuiHandler.CELLPHONE_PEARL, world, 0, 0, 0);
        }

        return cellphone;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
        list.add(String.format(StringUtils.translate("tooltip.cellphone.pearls"), new InventoryCellphone(itemStack).getStoredPearls()));
    }

    public static boolean tryUseFuel(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack == null || !(stack.getItem() instanceof ItemCellphone)) {
            return false;
        }
        stack = player.getHeldItemOffhand();
        if (stack == null || !(stack.getItem() instanceof ItemCellphone)) {
            return false;
        }

        return player.isCreative() || new InventoryCellphone(stack).useFuel();
    }

}
