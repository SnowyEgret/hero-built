package ds.plato.item.staff;

import net.minecraft.init.Items;
import ds.plato.api.IPick;

public class StaffAcacia extends Staff {

	public StaffAcacia(IPick pickManager) {
		super(pickManager);
	}

	@Override
	public Object[] getRecipe() {
		//TODO how to make recipe with acacia?
		return new Object[] { "#  ", " # ", "  #", '#', Items.apple};
	}

}
