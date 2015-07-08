package org.snowyegret.mojo.geom;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

// Replaced by simply getting side of the pick.
// Might be useful.
@Deprecated
public class Plane {

	private int distanceFromOrigin;
	private EnumFacing direction;
	private EnumPlane plane;

	// This class is not necessary.
	public Plane(int distanceFromOrigin, EnumFacing direction) {
		this.distanceFromOrigin = distanceFromOrigin;
		this.direction = direction;
		switch (direction) {
		case UP:
			plane = EnumPlane.HORIZONTAL_XZ;
			break;
		case DOWN:
			plane = EnumPlane.HORIZONTAL_XZ;
			break;
		case EAST:
			plane = EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
			break;
		case WEST:
			plane = EnumPlane.VERTICAL_YZ_NORTH_SOUTH;
			break;
		case NORTH:
			plane = EnumPlane.VERTICAL_XY_EAST_WEST;
			break;
		case SOUTH:
			plane = EnumPlane.VERTICAL_XY_EAST_WEST;
			break;
		}
	}

	public boolean contains(BlockPos p) {
		switch (plane) {
		case VERTICAL_XY_EAST_WEST:
			return p.getZ() == distanceFromOrigin;
		case HORIZONTAL_XZ:
			return p.getY() == distanceFromOrigin;
		case VERTICAL_YZ_NORTH_SOUTH:
			return p.getX() == distanceFromOrigin;
		default:
			return false;
		}
	}

	public Vec3i normal() {
		return direction.getDirectionVec();
	}

	public EnumFacing getDirection() {
		return direction;
	}

	public EnumPlane getPlane() {
		return plane;
	}

	public static Plane getPlane(Player player, BlockPos p1, BlockPos p2) {

		int dx = p1.getX() - p2.getX();
		int dy = p1.getY() - p2.getY();
		int dz = p1.getZ() - p2.getZ();

		int pdx = player.getPosition().getX() - p1.getX();
		int pdy = player.getPosition().getY() - p1.getY();
		int pdz = player.getPosition().getZ() - p1.getZ();

		List<Plane> planes = Lists.newArrayList();
		if (dx == 0) {
			planes.add(new Plane(p1.getX(), pdx > 0 ? EnumFacing.EAST : EnumFacing.WEST));
		}
		if (dy == 0) {
			planes.add(new Plane(p1.getY(), pdy > 0 ? EnumFacing.UP : EnumFacing.DOWN));
		}
		if (dz == 0) {
			planes.add(new Plane(p1.getZ(), pdz > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH));
		}

		// If points do not lie in a plane, return ground plane and CircleXZ with take care of it.
		if (planes.size() == 0) {
			return new Plane(p1.getZ(), EnumFacing.UP);
		}

		if (planes.size() == 1) {
			return planes.get(0);
		}

		// Two positions can lie in two planes, but not in three
		assert planes.size() == 2 : "Expected 2 hyperplanes. Got " + planes.size();

		// Choose the best one based on the air blocks surrounding the first position
		List<Integer> numAirs = Lists.newArrayList();
		for (Plane h : planes) {
			int numAir = 0;
			for (BlockPos p : Select.ALL) {
				p = p.add(p1);
				if (h.contains(p) && player.getWorld().getBlock(p) == Blocks.air) {
					numAir++;
				}
			}
			numAirs.add(numAir);
		}
		return numAirs.get(0) >= numAirs.get(1) ? planes.get(1) : planes.get(0);
	}
}
