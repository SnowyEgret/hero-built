package org.snowyegret.mojo.util;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.snowyegret.mojo.item.spell.select.Select;

import com.google.common.collect.Lists;

public class MathUtils {

	public static Hyperplane getHyperplane(IBlockAccess world, BlockPos p1, BlockPos p2) {

		int dx = p1.getX() - p2.getX();
		int dy = p1.getY() - p2.getY();
		int dz = p1.getZ() - p2.getZ();

		List<Hyperplane> planes = Lists.newArrayList();
		if (dx == 0) {
			planes.add(new PlaneYZ(p1.getX()));
		}
		if (dy == 0) {
			planes.add(new PlaneXZ(p1.getY()));
		}
		if (dz == 0) {
			planes.add(new PlaneXY(p1.getZ()));
		}

		// If points lie in no hyperplane, return xz and CircleXZ with take care of it.
		if (planes.size() == 0) {
			return new PlaneXY(p1.getZ());
		}

		if (planes.size() == 1) {
			return planes.get(0);
		}

		// Two positions can lie in two planes, but not in three
		assert planes.size() == 2 : "Expected 2 hyperplanes. Got " + planes.size();

		// Choose the best one based on the air blocks surrounding the first position
		List<Integer> numAirs = Lists.newArrayList();
		for (Hyperplane h : planes) {
			int numAir = 0;
			for (BlockPos p : Select.ALL) {
				p = p.add(p1);
				if (h.contains(p) && world.isAirBlock(p)) {
					numAir++;
				}
			}
			numAirs.add(numAir);
		}
		return numAirs.get(0) >= numAirs.get(1) ? planes.get(1) : planes.get(0);
	}
}
