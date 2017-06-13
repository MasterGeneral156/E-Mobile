package tonius.emobile.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackUtils {
    
    public static NBTTagCompound getNBT(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            NBTTagCompound itemstack = new NBTTagCompound();
        }
        return itemStack.getTagCompound();
    }
    
    public static ItemStack decrementStack(ItemStack itemStack) {
        if (--itemStack.stackSize <= 0) {
            itemStack = null;
        }
        return itemStack;
    }
}
