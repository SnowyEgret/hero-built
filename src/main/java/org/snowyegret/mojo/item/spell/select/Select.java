package org.snowyegret.mojo.item.spell.select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.Range;

public class Select {

	public static final BlockPos[] ABOVE = pos(range(-1, 1), range(1, 1), range(-1, 1));
	public static final BlockPos[] ABOVE_NO_CORNERS = pos(range(-1, 1), range(1, 1), range(-1, 1), true);
	public static final BlockPos[] ABOVE_INCLUSIVE = pos(range(-1, 1), range(0, 1), range(-1, 1));
	public static final BlockPos[] HORIZONTAL = pos(range(-1, 1), range(0, 0), range(-1, 1));
	public static final BlockPos[] HORIZONTAL_NO_CORNERS = pos(range(-1, 1), range(0, 0), range(-1, 1), true);
	public static final BlockPos[] BELOW = pos(range(-1, 1), range(-1, -1), range(-1, 1));
	public static final BlockPos[] BELOW_NO_CORNERS = pos(range(-1, 1), range(-1, -1), range(-1, 1), true);
	public static final BlockPos[] BELOW_INCLUSIVE = pos(range(-1, 1), range(0, -1), range(-1, 1));
	public static final BlockPos[] EAST_WEST = pos(range(-1, 1), range(-1, 1), range(0, 0));
	public static final BlockPos[] NORTH_SOUTH = pos(range(0, 0), range(-1, 1), range(-1, 1));
	public static final BlockPos[] UP = pos(range(0, 0), range(1, 1), range(0, 0));
	public static final BlockPos[] DOWN = pos(range(0, 0), range(-1, -1), range(0, 0));
	public static final BlockPos[] EAST = pos(range(1, 1), range(-1, 1), range(-1, 1));
	public static final BlockPos[] WEST = pos(range(-1, -1), range(-1, 1), range(-1, 1));
	public static final BlockPos[] NORTH = pos(range(-1, 1), range(-1, 1), range(1, 1));
	public static final BlockPos[] SOUTH = pos(range(-1, 1), range(-1, 1), range(-1, -1));
	public static final BlockPos[] ALL = pos(range(-1, 1), range(-1, 1), range(-1, 1));
	public static final BlockPos[] ALL_NO_CORNERS = concat(NORTH_SOUTH, EAST_WEST, HORIZONTAL);
	public static final BlockPos[] UP_DOWN_NESW = concat(UP, DOWN, NORTH, EAST, SOUTH, WEST);

	public static final BlockPos[] XY = pos(range(-1, 1), range(-1, 1), range(0, 0));
	public static final BlockPos[] Z = pos(range(0, 0), range(0, 0), range(-1, 1));
	public static final BlockPos[] XZ = pos(range(-1, 1), range(0, 0), range(-1, 1));
	public static final BlockPos[] Y = pos(range(0, 0), range(-1, 1), range(0, 0));
	public static final BlockPos[] YZ = pos(range(0, 0), range(-1, 1), range(-1, 1));
	public static final BlockPos[] X = pos(range(-1, 1), range(0, 0), range(0, 0));

	public static final BlockPos[] XYZ = concat(X, Y, Z);

	public static BlockPos[] toSideOfPlane(EnumFacing side) {
		switch (side) {
		case UP:
			return ABOVE;
		case DOWN:
			return BELOW;
		case EAST:
			return EAST;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		default:
			break;
		}
		return null;
	}

	public static BlockPos[] planeForSide(EnumFacing side) {
		switch (side) {
		case UP:
			return HORIZONTAL;
		case DOWN:
			return HORIZONTAL;
		case EAST:
			return NORTH_SOUTH;
		case WEST:
			return NORTH_SOUTH;
		case NORTH:
			return EAST_WEST;
		case SOUTH:
			return EAST_WEST;
		default:
			break;
		}
		return null;
	}

	// Private------------------------------------------------

	private static Range<Integer> range(int from, int to) {
		return Range.between(from, to);
	}

	private static BlockPos[] pos(Range<Integer> rx, Range<Integer> ry, Range<Integer> rz) {
		return pos(rx, ry, rz, false);
	}

	private static BlockPos[] pos(Range<Integer> rx, Range<Integer> ry, Range<Integer> rz, boolean excludeCorners) {
		List<BlockPos> positions = new ArrayList<>();
		for (int x = rx.getMinimum(); x <= rx.getMaximum(); x++) {
			for (int y = ry.getMinimum(); y <= ry.getMaximum(); y++) {
				for (int z = rz.getMinimum(); z <= rz.getMaximum(); z++) {
					if (excludeCorners) {
						if ((Math.abs(x) == 1 && Math.abs(z) == 1) || (Math.abs(x) == 1 && Math.abs(y) == 1)
								|| (Math.abs(y) == 1 && Math.abs(z) == 1))
							continue;
					}
					// Exclude center
					// When excluded the picked block is not rendering as if it is not picked in a selection spell
					if (x == 0 && y == 0 && z == 0) {
						continue;
					}
					positions.add(new BlockPos(x, y, z));
				}
			}
		}
		BlockPos[] array = new BlockPos[positions.size()];
		return positions.toArray(array);
	}

	static BlockPos[] concat(BlockPos[]... posArrays) {
		Set positions = new HashSet();
		for (BlockPos[] posArray : posArrays) {
			for (BlockPos p : posArray) {
				positions.add(p);
			}
		}
		BlockPos[] array = new BlockPos[positions.size()];
		positions.toArray(array);
		return array;
	}

}
