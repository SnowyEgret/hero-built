package org.snowyegret.mojo.gui;

import net.minecraft.util.BlockPos;

public class SelectionInfo {

	private int size = 0;
	private BlockPos firstPos;
	private BlockPos lastPos;
	private BlockPos centroid;

	public SelectionInfo(int size, BlockPos firstPos, BlockPos lastPos, BlockPos centroid) {
		this.size = size;
		this.firstPos = lastPos.distanceSq(new BlockPos(0,0,0)) < .0001 ? null: firstPos;
		this.lastPos = lastPos.distanceSq(new BlockPos(0,0,0)) < .0001 ? null: lastPos;
		this.centroid = centroid;
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

	public BlockPos getCentroid() {
		return centroid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SelectionInfo [size=");
		builder.append(size);
		builder.append(", firstPos=");
		builder.append(firstPos);
		builder.append(", lastPos=");
		builder.append(lastPos);
		builder.append(", centroid=");
		builder.append(centroid);
		builder.append("]");
		return builder.toString();
	}

}
