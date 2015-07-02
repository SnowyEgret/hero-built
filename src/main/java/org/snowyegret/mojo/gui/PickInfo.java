package org.snowyegret.mojo.gui;

import net.minecraft.util.BlockPos;

public class PickInfo {

	private boolean isPicking = false;
	private boolean isFinishedPicking = false;
	private BlockPos lastPos;

	public PickInfo() {
	}

	public PickInfo(boolean isPicking, boolean isFinishedPicking, BlockPos lastPos) {
		this.isPicking = isPicking;
		this.isFinishedPicking = isFinishedPicking;
		this.lastPos = lastPos.distanceSq(new BlockPos(0, 0, 0)) < .0001 ? null : lastPos;
	}

	public BlockPos getLastPos() {
		return lastPos;
	}

	public boolean isFinishedPicking() {
		return isFinishedPicking;
	}

	public boolean isPicking() {
		return isPicking;
	}

}
