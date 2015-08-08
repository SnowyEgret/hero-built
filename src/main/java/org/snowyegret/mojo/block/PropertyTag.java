package org.snowyegret.mojo.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyTag implements IUnlistedProperty<NBTTagCompound> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isValid(NBTTagCompound value) {
		return true;
	}

	@Override
	public Class<NBTTagCompound> getType() {
		return NBTTagCompound.class;
	}

	@Override
	public String valueToString(NBTTagCompound value) {
		return value.toString();
	}

}
