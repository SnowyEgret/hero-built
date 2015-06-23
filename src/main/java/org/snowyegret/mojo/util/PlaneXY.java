package org.snowyegret.mojo.util;

import net.minecraft.util.BlockPos;

public class PlaneXY extends Hyperplane {
	private int z;

	public PlaneXY(int z) {
		this.z = z;
	}

	public boolean contains(BlockPos p) {
		return p.getZ() == z;
	}

}
