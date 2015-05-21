package ds.plato.item.staff;

import net.minecraft.init.Items;
import ds.plato.pick.IPick;

public class StaffOak extends Staff {

	public StaffOak(IPick pickManager) {
		super(pickManager);
	}

	@Override
	public Object[] getRecipe() {
		//TODO how to make recipe with oak?
		return new Object[] { "#  ", " # ", "  #", '#', Items.apple};
	}

}
