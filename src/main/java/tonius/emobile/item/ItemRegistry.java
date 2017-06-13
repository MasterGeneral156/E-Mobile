package tonius.emobile.item;

import tonius.emobile.EMobile;

import com.themastergeneral.ctdcore.item.RegisterItem;

public class ItemRegistry extends RegisterItem 
{
	public static ItemCellphone cellphone = null;
	public static void register()
	{
		cellphone = register(new ItemCellphone("cellphone", EMobile.MODID));
	}
}
