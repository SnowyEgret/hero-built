package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.Range;

import ds.plato.item.spell.select.Shell.Type;
import ds.plato.world.IWorld;

public class Select {

	public static final BlockPos[] above = pos(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1));
	public static final BlockPos[] aboveNoCorners = pos(Range.between(-1, 1), Range.between(1, 1), Range.between(-1, 1), true);
	public static final BlockPos[] aboveInclusive = pos(Range.between(-1, 1), Range.between(0, 1), Range.between(-1, 1));
	public static final BlockPos[] horizontal = pos(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1));
	public static final BlockPos[] horizontalNoCorners = pos(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1),
			true);
	public static final BlockPos[] below = pos(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1));
	public static final BlockPos[] belowNoCorners = pos(Range.between(-1, 1), Range.between(-1, -1), Range.between(-1, 1),
			true);
	public static final BlockPos[] belowInclusive = pos(Range.between(-1, 1), Range.between(0, -1), Range.between(-1, 1));
	public static final BlockPos[] eastWest = pos(Range.between(-1, 1), Range.between(-1, 1), Range.between(0, 0));
	public static final BlockPos[] northSouth = pos(Range.between(0, 0), Range.between(-1, 1), Range.between(-1, 1));
	public static final BlockPos[] up = pos(Range.between(0, 0), Range.between(1, 1), Range.between(0, 0));
	public static final BlockPos[] down = pos(Range.between(0, 0), Range.between(-1, -1), Range.between(0, 0));
	public static final BlockPos[] east = pos(Range.between(1, 1), Range.between(-1, 1), Range.between(-1, 1));
	public static final BlockPos[] west = pos(Range.between(-1, -1), Range.between(-1, 1), Range.between(-1, 1));
	public static final BlockPos[] north = pos(Range.between(-1, 1), Range.between(-1, 1), Range.between(1, 1));
	public static final BlockPos[] south = pos(Range.between(-1, 1), Range.between(-1, 1), Range.between(-1, -1));
	public static final BlockPos[] all = concat(above, horizontal, below);
	public static final BlockPos[] allNoCorners = concat(northSouth, eastWest, horizontal);
	public static final BlockPos[] upDownNorhEastSouthWest = concat(up, down, north, east, south, west);
	
	public static final BlockPos[] XY = pos(Range.between(-1, 1), Range.between(-1, 1), Range.between(0, 0));
	public static final BlockPos[] Z = pos(Range.between(0, 0), Range.between(0, 0), Range.between(-1, 1));
	public static final BlockPos[] XZ = pos(Range.between(-1, 1), Range.between(0, 0), Range.between(-1, 1));
	public static final BlockPos[] Y = pos(Range.between(0, 0), Range.between(-1, 1), Range.between(0, 0));
	public static final BlockPos[] YZ = pos(Range.between(0, 0), Range.between(-1, 1), Range.between(-1, 1));
	public static final BlockPos[] X = pos(Range.between(-1, 1), Range.between(0, 0), Range.between(0, 0));

	// public static boolean isEdgeOnGround(IWorld w, BlockPos position) {
	// for (BlockPos p : above) {
	// p = p.add(position);
	// if (w.getBlock(p) != Blocks.air) {
	// return false;
	// }
	// }
	// return false;
	// }

	public static BlockPos[] toSideOfPlane(EnumFacing side) {
		switch (side) {
		case UP:
			return above;
		case DOWN:
			return below;
		case EAST:
			return east;
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case WEST:
			return west;
		default:
			break;
		}
		return null;
	}

	public static BlockPos[] planeForSide(EnumFacing side) {
		switch (side) {
		case UP:
			return horizontal;
		case DOWN:
			return horizontal;
		case EAST:
			return northSouth;
		case WEST:
			return northSouth;
		case NORTH:
			return eastWest;
		case SOUTH:
			return eastWest;
		default:
			break;
		}
		return null;
	}

	// Private------------------------------------------------

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
