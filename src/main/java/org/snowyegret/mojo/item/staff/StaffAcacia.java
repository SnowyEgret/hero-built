package org.snowyegret.mojo.item.staff;

import org.snowyegret.mojo.pick.IPick;

import net.minecraft.init.Items;

public class StaffAcacia extends Staff {

	@Override
	public Object[] getRecipe() {
		//TODO how to make recipe with acacia?
		return new Object[] { "#  ", " # ", "  #", '#', Items.apple};
	}

}
