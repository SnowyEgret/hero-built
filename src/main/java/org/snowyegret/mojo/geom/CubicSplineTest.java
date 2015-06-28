package org.snowyegret.mojo.geom;

import javax.vecmath.Point3d;

import org.junit.Test;

import ds.geom.PointSet;
import ds.geom.Primitive;

public class CubicSplineTest {

	@Test
	public void test() {
		Primitive p = new CubicSpline(new Point3d(0,0,0), new Point3d(3,0,3), new Point3d(6,0,0));
		PointSet points = p.pointSet();
//		new Viewer(points);
//		for (Point3d pt : points) {
//			assertThat(p.contains(pt), equalTo(true));
//		}
	}
}