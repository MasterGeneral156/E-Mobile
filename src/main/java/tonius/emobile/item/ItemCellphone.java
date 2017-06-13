package tonius.emobile.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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

    public ItemCellphone(String name, String modid) {
    	super(name, modid);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("emobile.cellphone");
        this.setCreativeTab(CreativeTabs.TOOLS);
    }
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if (!worldIn.isRemote) 
    	{
    		playerIn.openGui(EMobile.instance, EMGuiHandler.CELLPHONE_PEARL, worldIn, 0, 0, 0);
    		return new ActionResult(EnumActionResult.PASS, itemStackIn);
    	}
    	return new ActionResult(EnumActionResult.FAIL, itemStackIn);
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
