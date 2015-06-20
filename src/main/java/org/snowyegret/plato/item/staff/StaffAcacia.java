package org.snowyegret.plato.item.staff;

import org.snowyegret.plato.pick.IPick;

import net.minecraft.init.Items;

public class StaffAcacia extends Staff {

	@Override
	public Object[] getRecipe() {
		//TODO how to make recipe with acacia?
		return new Object[] { "#  ", " # ", "  #", '#', Items.apple};
	}

}
