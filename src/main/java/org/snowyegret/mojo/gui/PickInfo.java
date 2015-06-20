package org.snowyegret.mojo.gui;

import net.minecraft.util.BlockPos;

public class PickInfo {

	private BlockPos lastPos;
	private boolean isFinishedPicking = false;

	public PickInfo(boolean isFinishedPicking, BlockPos lastPos) {
		this.isFinishedPicking = isFinishedPicking;
		this.lastPos = lastPos;
	}

	public PickInfo() {
	}

	public BlockPos getLastPos() {
		return lastPos;
	}

	public boolean isFinishedPicking() {
		return isFinishedPicking;
	}

}
