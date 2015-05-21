package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.world.IWorld;

public class Shell implements Iterable<BlockPos> {

	public enum Type {
		ALL,
		XYZ,
		HORIZONTAL,
		UP,
		DOWN,
		TOP,
		TOP_CROSS,
		TOP_ALL,
		BOTTOM,
		BOTTOM_ALL,
		ABOVE,
		ABOVE_ALL,
		BELLOW,
		FLOOR,
		CEILING,
		X,
		Y,
		Z,
		XY,
		XZ,
		YZ,
		EDGE,
		EDGE_UNDER
	}

	private List<BlockPos> positions = new ArrayList<>();
	private final Type type;

	public Shell(Type type, BlockPos p0, IWorld w) {
		this.type = type;

		List<BlockPos> noCorners = new ArrayList<>();
		noCorners.add(new BlockPos(0, -1, 0));
		noCorners.add(new BlockPos(0, 0, -1));
		noCorners.add(new BlockPos(-1, 0, 0));
		noCorners.add(new BlockPos(1, 0, 0));
		noCorners.add(new BlockPos(0, 0, 1));
		noCorners.add(new BlockPos(0, 1, 0));
		for (BlockPos p : noCorners) {
			p.add(p0);
		}

		List<BlockPos> all = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					BlockPos p = new BlockPos(p0.add(x-1,y-1,x-1));
					//BlockPos p = p0.add(x-1,y-1,x-1);
					if (!p.equals(p0))
						all.add(p);
				}
			}
		}

		List<BlockPos> cross = new ArrayList<>();
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				for (int z = -1; z < 2; z++) {
					BlockPos p = new BlockPos(x, y, z);
					if (p.getX() == 0 || p.getZ() == 0) {
						if (!(p.getX() == 0 && p.getZ() == 0))
							cross.add(p);
					}
				}
			}
		}
		for (BlockPos p : cross) {
			p.add(p0);
		}

		switch (type) {

		case ALL:
			positions = all;
			break;

		case HORIZONTAL:
			for (BlockPos p : noCorners) {
				if (p.getY() == p0.getY())
					positions.add(p);
			}
			break;

		case ABOVE:
			for (BlockPos p : noCorners) {
				if (p.getY() >= p0.getY())
					positions.add(p);
			}
			break;

		case ABOVE_ALL:
			for (BlockPos p : all) {
				if (p.getY() >= p0.getY())
					positions.add(p);
			}
			break;

		case BELLOW:
			for (BlockPos p : noCorners) {
				if (p.getY() <= p0.getY())
					positions.add(p);
			}
			break;

		case TOP:
			for (BlockPos p : noCorners) {
				if (p.getY() > p0.getY())
					positions.add(p);
			}
			break;

		case TOP_ALL:
			for (BlockPos p : all) {
				if (p.getY() > p0.getY())
					positions.add(p);
			}
			break;

		case TOP_CROSS:
			for (BlockPos p : cross) {
				if (p.getY() > p0.getY())
					positions.add(p);
			}
			break;

		case BOTTOM:
			for (BlockPos p : noCorners) {
				if (p.getY() < p0.getY())
					positions.add(p);
			}
			break;

		case BOTTOM_ALL:
			for (BlockPos p : all) {
				if (p.getY() < p0.getY())
					positions.add(p);
			}
			break;

		case XYZ:
			positions = noCorners;
			break;

		case DOWN:
			for (BlockPos p : noCorners) {
				if (p.getZ() == p0.getZ() && p.getX() == p0.getX() && p.getY() < p0.getY())
					positions.add(p);
			}
			break;

		case UP:
			for (BlockPos p : noCorners) {
				if (p.getZ() == p0.getZ() && p.getX() == p0.getX() && p.getY() > p0.getY())
					positions.add(p);
			}
			break;

		case FLOOR:
			for (BlockPos p : noCorners) {
				//Block b = w.getBlock(p.getX(), p.getY() + 1, p.getZ());
				Block b = w.getBlock(p.up());
				if (p.getY() == p0.getY() && b == Blocks.air)
					positions.add(p);
			}
			break;

		case CEILING:
			for (BlockPos p : noCorners) {
				//Block b = w.getBlock(p.getX(), p.getY() - 1, p.getZ());
				Block b = w.getBlock(p.down());
				if (p.getY() == p0.getY() && b == Blocks.air)
					positions.add(p);
			}
			break;

		// FIXME leaks out though diagonal corner
		case EDGE:
			for (BlockPos p : all) {
				if (p.getY() == p0.getY()) {
					//if (w.getBlock(p.getX(), p.getY() + 1, p.getZ()) == Blocks.air) {
					if (w.getBlock(p.up()) == Blocks.air) {
						// for (BlockPos pointAbove : new Shell(Type.TOP_CROSS, p, w)) {
						for (BlockPos pointAbove : new Shell(Type.TOP_ALL, p, w)) {
							Block b = w.getBlock(pointAbove);
							if (b != Blocks.air) {
								positions.add(p);
								break;
							}
						}
					}
				}
			}
			break;

		case EDGE_UNDER:
			for (BlockPos p : all) {
				if (p.getY() == p0.getY()) {
					//TODO Does add modify p?
					//if (w.getBlock(p.getX(), p.getY() - 1, p.getZ()) == Blocks.air) {
					if (w.getBlock(p.down()) == Blocks.air) {
						for (BlockPos pointBellow : new Shell(Type.BOTTOM_ALL, p, w)) {
							Block b = w.getBlock(pointBellow);
							if (b != Blocks.air) {
								positions.add(p);
								break;
							}
						}
					}
				}
			}
			break;

		// case FLOOR_EDGE:
		// for (BlockPos p : allPoints) {
		// if (p.getY() == p0.getY()) {
		// if (w.getBlock(p.getX(), p.getY() + 1, p.getZ()) == Blocks.air) {
		// Shell s = new Shell(Type.ABOVE, p, w);
		// for (BlockPos pp : s) {
		// if (pp.getY() > p.getY() && w.getBlock(pp.getX(), pp.getY(), pp.getZ()) != Blocks.air) {
		// points.add(p);
		// break;
		// }
		// }
		// }
		// }
		// }
		// break;
		// case CEILING_EDGE:
		// for (BlockPos p : allPoints) {
		// if (p.getY() == p0.getY()) {
		// if (w.getBlock(p.getX(), p.getY() - 1, p.getZ()) == Blocks.air) {
		// Shell s = new Shell(Type.BELLOW, p, w);
		// for (BlockPos pp : s) {
		// if (pp.getY() < p.getY() && w.getBlock(pp.getX(), pp.getY(), pp.getZ()) != Blocks.air) {
		// points.add(p);
		// break;
		// }
		// }
		// }
		// }
		// }
		// break;
		case X:
			for (BlockPos p : noCorners) {
				if (p.getZ() == p0.getZ() && p.getY() == p0.getY())
					positions.add(p);
			}
			break;

		case Y:
			for (BlockPos p : noCorners) {
				if (p.getZ() == p0.getZ() && p.getX() == p0.getX())
					positions.add(p);
			}
			break;

		case Z:
			for (BlockPos p : noCorners) {
				if (p.getX() == p0.getX() && p.getY() == p0.getY())
					positions.add(p);
			}
			break;

		case XY:
			for (BlockPos p : noCorners) {
				if (p.getZ() == p0.getZ())
					positions.add(p);
			}
			break;

		case XZ:
			for (BlockPos p : noCorners) {
				if (p.getY() == p0.getY())
					positions.add(p);
			}
			break;
		case YZ:
			for (BlockPos p : noCorners) {
				if (p.getX() == p0.getX())
					positions.add(p);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "Shell [type=" + type + ", points=" + positions + "]";
	}

	@Override
	public Iterator iterator() {
		return positions.iterator();
	}

}
