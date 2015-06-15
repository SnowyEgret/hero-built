package ds.plato.gui;

import net.minecraft.util.BlockPos;

public class SelectionInfo {

	private int size = 0;
	private BlockPos firstPos;
	private BlockPos lastPos;

	public SelectionInfo(int size, BlockPos firstPos, BlockPos lastPos) {
		this.size = size;
		this.firstPos = firstPos;
		this.lastPos = lastPos;
	}

	public SelectionInfo() {
	}

	public int getSize() {
		return size;
	}

	public BlockPos getFirstPos() {
		return firstPos;
	}

	public BlockPos getLastPos() {
		return lastPos;
	}

}
