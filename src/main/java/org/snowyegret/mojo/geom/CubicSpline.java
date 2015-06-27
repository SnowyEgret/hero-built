package org.snowyegret.mojo.geom;

import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.lang3.Range;
import org.snowyegret.mojo.pick.Pick;

import com.google.common.collect.Lists;

import ds.geom.curve.Curve;

public class CubicSpline extends Curve {

	private List<Cubic> cubics = Lists.newArrayList();

	// public CubicSpline(Point3d... points) {
	// super(points[0]);
	// for (Point3d p : points) {
	// cubics.add(new Cubic(Cubic.BEZIER, new double[]{1, 2, 3, 4}));
	// }
	// rT = Range.between(0d, 1d);
	// }

	public CubicSpline(Pick[] picks) {
		super(picks[0].point3d());
		for (Pick p : picks) {
			Point3d pt = picks[0].point3d();
			cubics.add(new Cubic(Cubic.BEZIER, new double[]{pt.x, pt.y, pt.z, .5}));
		}
		rT = Range.between(0d, 1d);
	}

	@Override
	public Point3d pointAtParameter(double t) {
		Point3d p = new Point3d();
		p.x = cubics.get(0).eval(t);
		p.y = 0;
		p.z = cubics.get(1).eval(t);
		p.add(p0);
		return p;
	}

	@Override
	public boolean contains(Point3d arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
