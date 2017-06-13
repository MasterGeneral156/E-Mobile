package tonius.emobile.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.util.StackUtils;

public class InventoryCellphone implements IInventory 
{
	private ItemStack cellphone;
	private ItemStack[] inv = new ItemStack[1];
    private String CustomName;
	public InventoryCellphone(ItemStack cellphone) 
	{
        if (cellphone == null || !(cellphone.getItem() instanceof ItemCellphone)) {
            throw new IllegalArgumentException("Invalid ItemStack when creating a " + this.getClass().getSimpleName() + " instance");
        }
        this.cellphone = cellphone;

        //this.readFromNBT(StackUtils.getNBT(cellphone));
    }
	
	public ItemStack getCellphone() 
	{
        this.markDirty();
        return this.cellphone;
    }
	public int getStoredPearls() 
	{
        ItemStack pearls = this.getStackInSlot(0);
        return pearls != null ? pearls.stackSize : 0;
    }

    public boolean useFuel() 
    {
        if (this.getStoredPearls() <= 0) 
        {
            return false;
        }
        this.getStackInSlot(0);
        this.decrStackSize(0, 1);
        this.markDirty();
        return true;
    }
	@Override
	public String getName() 
	{
		return this.hasCustomName() ? this.CustomName : "item.emobile.cellphone.name";
	}

	@Override
	public boolean hasCustomName() 
	{
		return this.CustomName != null && !this.CustomName.isEmpty();
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory() 
	{
		return this.inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) 
	{
		ItemStack itemStack = this.getStackInSlot(slot);
		if (itemStack != null) 
		{
            if (itemStack.stackSize <= amount) 
            {
                this.setInventorySlotContents(slot, null);
            } 
            else
            {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0)
                {
                    this.setInventorySlotContents(slot, null);
                }
            }
        }
        return itemStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return ItemStackHelper.getAndRemove(inv, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		boolean flag = stack != null && stack.isItemEqual(this.inv[index]) && ItemStack.areItemStackTagsEqual(stack, this.inv[index]);
        this.inv[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag)
        {
            this.markDirty();
        }
		
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return EMConfig.enderPearlStackSize;
	}

	@Override
	public void markDirty() 
	{
		NBTTagCompound tagCompound = this.cellphone.getTagCompound();
		this.writeToNBT(tagCompound);
		this.cellphone.setTagCompound(tagCompound);
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return stack != null && stack.getItem() instanceof ItemEnderPearl;
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inv = null;
		
	}
	public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.inv.length) {
                this.inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.inv.length; i++) {
            ItemStack stack = this.inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        if (itemList != null)
        {
        	tagCompound.setTag("Inventory", itemList);
        }
        if (this.hasCustomName())
        {
            tagCompound.setString("CustomName", this.CustomName);
        }

        return tagCompound;
    }
}
