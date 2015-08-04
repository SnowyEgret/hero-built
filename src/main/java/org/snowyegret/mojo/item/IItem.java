package org.snowyegret.mojo.item;

import net.minecraft.util.ResourceLocation;

public interface IItem {

	public Object[] getRecipe();

	public boolean hasRecipe();
}