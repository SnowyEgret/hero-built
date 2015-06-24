package org.snowyegret.mojo.item;

import net.minecraft.item.Item;

public abstract class ItemBase extends Item implements IItem {

	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public boolean hasRecipe() {
		return getRecipe() != null;
	}

}
