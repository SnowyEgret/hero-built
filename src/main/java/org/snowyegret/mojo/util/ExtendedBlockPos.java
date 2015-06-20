package org.snowyegret.mojo.util;

import java.util.Arrays;

import net.minecraft.util.BlockPos;

@Deprecated
public class ExtendedBlockPos {
	
	private BlockPos pos;
	private BlockPos[][][] exPos = new BlockPos[3][3][3];

	public ExtendedBlockPos(BlockPos pos) {
		super();
		this.pos = pos;
		for(int x = -1; x<= 1; x++) {
			for(int y = -1; y<= 1; y++) {
				for(int z = -1; z<= 1; z++) {
					exPos[x+1][y+1][z+1] = pos.add(x, y, z);
				}
			}
		}
	}
	
	public Iterable<BlockPos> all() {
		return BlockPos.getAllInBox(exPos[0][0][0], exPos[2][2][2]);
	}

	public Iterable<BlockPos> ground() {
		return BlockPos.getAllInBox(exPos[0][1][0], exPos[2][1][2]);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExtendedBlockPos [pos=");
		builder.append(pos);
		builder.append(", exPos=");
		builder.append(Arrays.deepToString(exPos));
		builder.append("]");
		return builder.toString();
	}

}
