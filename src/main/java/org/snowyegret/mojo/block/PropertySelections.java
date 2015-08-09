package org.snowyegret.mojo.block;

import net.minecraftforge.common.property.IUnlistedProperty;

import org.snowyegret.mojo.select.Selection;

public class PropertySelections implements IUnlistedProperty<Iterable<Selection>> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isValid(Iterable<Selection> value) {
		return true;
	}

	@Override
	public Class getType() {
		return Iterable.class;
	}

	@Override
	public String valueToString(Iterable<Selection> value) {
		return value.toString();
	}

}
