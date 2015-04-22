package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import ds.plato.api.IWorld;

public class Shell implements Iterable<Point3i> {

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

	private List<Point3i> points = new ArrayList<>();
	private final Type type;

	public Shell(Type type, Point3i p0, IWorld w) {
		this.type = type;

		List<Point3i> noCorners = new ArrayList<>();
		noCorners.add(new Point3i(0, -1, 0));
		noCorners.add(new Point3i(0, 0, -1));
		noCorners.add(new Point3i(-1, 0, 0));
		noCorners.add(new Point3i(1, 0, 0));
		noCorners.add(new Point3i(0, 0, 1));
		noCorners.add(new Point3i(0, 1, 0));
		for (Point3i p : noCorners) {
			p.add(p0);
		}

		List<Point3i> allPoints = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					Point3i p = new Point3i(p0.x + x - 1, p0.y + y - 1, p0.z + z - 1);
					if (!p.equals(p0))
						allPoints.add(p);
				}
			}
		}

		List<Point3i> cross = new ArrayList<>();
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				for (int z = -1; z < 2; z++) {
					Point3i p = new Point3i(x, y, z);
					if (p.x == 0 || p.z == 0) {
						if (!(p.x == 0 && p.z == 0))
							cross.add(p);
					}
				}
			}
		}
		for (Point3i p : cross) {
			p.add(p0);
		}

		switch (type) {

		case ALL:
			points = allPoints;
			break;

		case HORIZONTAL:
			for (Point3i p : noCorners) {
				if (p.y == p0.y)
					points.add(p);
			}
			break;

		case ABOVE:
			for (Point3i p : noCorners) {
				if (p.y >= p0.y)
					points.add(p);
			}
			break;

		case ABOVE_ALL:
			for (Point3i p : allPoints) {
				if (p.y >= p0.y)
					points.add(p);
			}
			break;

		case BELLOW:
			for (Point3i p : noCorners) {
				if (p.y <= p0.y)
					points.add(p);
			}
			break;

		case TOP:
			for (Point3i p : noCorners) {
				if (p.y > p0.y)
					points.add(p);
			}
			break;

		case TOP_ALL:
			for (Point3i p : allPoints) {
				if (p.y > p0.y)
					points.add(p);
			}
			break;

		case TOP_CROSS:
			for (Point3i p : cross) {
				if (p.y > p0.y)
					points.add(p);
			}
			break;

		case BOTTOM:
			for (Point3i p : noCorners) {
				if (p.y < p0.y)
					points.add(p);
			}
			break;

		case BOTTOM_ALL:
			for (Point3i p : allPoints) {
				if (p.y < p0.y)
					points.add(p);
			}
			break;

		case XYZ:
			points = noCorners;
			break;

		case DOWN:
			for (Point3i p : noCorners) {
				if (p.z == p0.z && p.x == p0.x && p.y < p0.y)
					points.add(p);
			}
			break;

		case UP:
			for (Point3i p : noCorners) {
				if (p.z == p0.z && p.x == p0.x && p.y > p0.y)
					points.add(p);
			}
			break;

		case FLOOR:
			for (Point3i p : noCorners) {
				Block b = w.getBlock(p.x, p.y + 1, p.z);
				if (p.y == p0.y && b == Blocks.air)
					points.add(p);
			}
			break;

		case CEILING:
			for (Point3i p : noCorners) {
				Block b = w.getBlock(p.x, p.y - 1, p.z);
				if (p.y == p0.y && b == Blocks.air)
					points.add(p);
			}
			break;

		// FIXME leaks out though diagonal corner
		case EDGE:
			for (Point3i p : allPoints) {
				if (p.y == p0.y) {
					if (w.getBlock(p.x, p.y + 1, p.z) == Blocks.air) {
						// for (Point3i pointAbove : new Shell(Type.TOP_CROSS, p, w)) {
						for (Point3i pointAbove : new Shell(Type.TOP_ALL, p, w)) {
							Block b = w.getBlock(pointAbove.x, pointAbove.y, pointAbove.z);
							if (b != Blocks.air) {
								points.add(p);
								break;
							}
						}
					}
				}
			}
			break;

		case EDGE_UNDER:
			for (Point3i p : allPoints) {
				if (p.y == p0.y) {
					if (w.getBlock(p.x, p.y - 1, p.z) == Blocks.air) {
						for (Point3i pointBellow : new Shell(Type.BOTTOM_ALL, p, w)) {
							Block b = w.getBlock(pointBellow.x, pointBellow.y, pointBellow.z);
							if (b != Blocks.air) {
								points.add(p);
								break;
							}
						}
					}
				}
			}
			break;

		// case FLOOR_EDGE:
		// for (Point3i p : allPoints) {
		// if (p.y == p0.y) {
		// if (w.getBlock(p.x, p.y + 1, p.z) == Blocks.air) {
		// Shell s = new Shell(Type.ABOVE, p, w);
		// for (Point3i pp : s) {
		// if (pp.y > p.y && w.getBlock(pp.x, pp.y, pp.z) != Blocks.air) {
		// points.add(p);
		// break;
		// }
		// }
		// }
		// }
		// }
		// break;
		// case CEILING_EDGE:
		// for (Point3i p : allPoints) {
		// if (p.y == p0.y) {
		// if (w.getBlock(p.x, p.y - 1, p.z) == Blocks.air) {
		// Shell s = new Shell(Type.BELLOW, p, w);
		// for (Point3i pp : s) {
		// if (pp.y < p.y && w.getBlock(pp.x, pp.y, pp.z) != Blocks.air) {
		// points.add(p);
		// break;
		// }
		// }
		// }
		// }
		// }
		// break;
		case X:
			for (Point3i p : noCorners) {
				if (p.z == p0.z && p.y == p0.y)
					points.add(p);
			}
			break;

		case Y:
			for (Point3i p : noCorners) {
				if (p.z == p0.z && p.x == p0.x)
					points.add(p);
			}
			break;

		case Z:
			for (Point3i p : noCorners) {
				if (p.x == p0.x && p.y == p0.y)
					points.add(p);
			}
			break;

		case XY:
			for (Point3i p : noCorners) {
				if (p.z == p0.z)
					points.add(p);
			}
			break;

		case XZ:
			for (Point3i p : noCorners) {
				if (p.y == p0.y)
					points.add(p);
			}
			break;
		case YZ:
			for (Point3i p : noCorners) {
				if (p.x == p0.x)
					points.add(p);
			}
			break;
		default:
			break;
		}
	}

	// private static List<Point3i> initPoints() {
	// System.out.println("[Shell.initPoints]");
	// List<Point3i> pts = new ArrayList<>();
	// pts.add(new Point3i(0, -1, 0));
	// pts.add(new Point3i(0, 0, -1));
	// pts.add(new Point3i(-1, 0, 0));
	// pts.add(new Point3i(1, 0, 0));
	// pts.add(new Point3i(0, 0, 1));
	// pts.add(new Point3i(0, 1, 0));
	// return pts;
	// }

	@Override
	public String toString() {
		return "Shell [type=" + type + ", points=" + points + "]";
	}

	@Override
	public Iterator iterator() {
		return points.iterator();
	}

}
