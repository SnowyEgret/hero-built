package org.snowyegret.mojo.geom;

import javax.vecmath.Point3d;

import org.apache.commons.lang3.Range;

import org.snowyegret.geom.curve.Curve;

public class CircleXY extends Curve {

	private final double radius;

	public CircleXY(Point3d origin, Point3d pointOnEdge) {
		super(origin);
		radius = Math.abs(origin.distance(pointOnEdge));
		rT = Range.between(0d, 2 * Math.PI);
	}

	@Override
	public Point3d pointAtParameter(double t) {
		Point3d p = new Point3d();
		p.x = p0.x + radius * Math.sin(t);
		p.y = p0.y + radius * Math.cos(t);
		p.z = p0.z;
		return p;
	}

	@Override
	public boolean contains(Point3d p) {
		return Math.pow((p.x - p0.x), 2) + Math.pow((p.y - p0.y), 2) - radius * radius < epsilon;
	}
}