package org.snowyegret.mojo.block;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyColor implements IUnlistedProperty<Integer> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isValid(Integer value) {
		return true;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public String valueToString(Integer value) {
		return value.toString();
	}

}
