package org.snowyegret.mojo.select;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.snowyegret.geom.matrix.RotationMatrix;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import com.google.common.collect.Lists;

public class SelectionSet {

	private List<Selection> selections = Lists.newArrayList();

	public SelectionSet(Iterable<Selection> selections) {
		if (!selections.iterator().hasNext()) {
			throw new IllegalArgumentException("Selections cannot be empty");
		}
		for (Selection s : selections) {
			this.selections.add(s);
		}
	}

	public Vec3 getCentroid() {
		double x = 0, y = 0, z = 0;
		for (Selection s : selections) {
			BlockPos p = s.getPos();
			x += p.getX() + .5;
			y += p.getY() + .5;
			z += p.getZ() + .5;
		}
		int s = selections.size();
		return new Vec3(x / s, y / s, z / s);
	}

	public Iterable<Selection> getSelections() {
		return selections;
	}

	// Rotates selections in horizontal plane relative to south
	public void rotateHorizontal(EnumFacing facing) {
		if (facing.getAxis().isVertical()) {
			throw new IllegalArgumentException("Facing must be horizontal");
		}
		Vec3 c = getCentroid();
		Vec3i u = EnumFacing.UP.getDirectionVec();
		// Order is SWNE
		double angle = facing.getHorizontalIndex() * Math.PI / 2;
		Matrix4d matrix = new RotationMatrix(new Point3d(c.xCoord, c.yCoord, c.zCoord), new Vector3d(u.getX(),
				u.getY(), u.getZ()), angle);
		for (Selection s : selections) {
			Point3d p = s.point3d();
			matrix.transform(p);
			s.setPos(new BlockPos(p.x, p.y, p.z));
		}
	}

}
