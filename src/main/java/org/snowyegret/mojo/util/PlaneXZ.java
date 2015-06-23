package org.snowyegret.mojo.util;

import net.minecraft.util.BlockPos;

public class PlaneXZ extends Hyperplane {
	int y;

	public PlaneXZ(int y) {
		this.y = y;
	}

	public boolean contains(BlockPos p) {
		return p.getY() == y;
	}
}
