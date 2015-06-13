package ds.plato.item.staff;

import net.minecraft.init.Items;
import ds.plato.pick.IPick;

public class StaffBirch extends Staff {

	@Override
	public Object[] getRecipe() {
		//TODO how to make recipe with oak?
		return new Object[] { "#  ", " # ", "  #", '#', Items.baked_potato};
	}

}
