package org.snowyegret.mojo.util;

import net.minecraft.util.BlockPos;

public class PlaneYZ extends Hyperplane {
	private int x;

	public PlaneYZ(int x) {
		this.x = x;
	}

	public boolean contains(BlockPos p) {
		return p.getX() == x;
	}
}
