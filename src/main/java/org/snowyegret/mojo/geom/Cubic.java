package org.snowyegret.mojo.geom;

//http://mrl.nyu.edu/~perlin/cubic/Cubic_java.html
public class Cubic {

	public static final double[][] BEZIER = { // Bezier basis matrix
	{ -1, 3, -3, 1 }, { 3, -6, 3, 0 }, { -3, 3, 0, 0 }, { 1, 0, 0, 0 } };
	public static final double[][] BSPLINE = { // BSpline basis matrix
	{ -1. / 6, 3. / 6, -3. / 6, 1. / 6 }, { 3. / 6, -6. / 6, 3. / 6, 0. }, { -3. / 6, 0., 3. / 6, 0. },
			{ 1. / 6, 4. / 6, 1. / 6, 0. } };
	public static final double[][] CATMULL_ROM = { // Catmull-Rom basis matrix
	{ -0.5, 1.5, -1.5, 0.5 }, { 1, -2.5, 2, -0.5 }, { -0.5, 0, 0.5, 0 }, { 0, 1, 0, 0 } };
	public static final double[][] HERMITE = { // Hermite basis matrix
	{ 2, -2, 1, 1 }, { -3, 3, -2, -1 }, { 0, 0, 1, 0 }, { 1, 0, 0, 0 } };

	double a, b, c, d; // cubic coefficients vector

	public Cubic(double[][] M, double[] G) {
		a = b = c = d;
		for (int k = 0; k < 4; k++) { // (a,b,c,d) = M G
			a += M[0][k] * G[k];
			b += M[1][k] * G[k];
			c += M[2][k] * G[k];
			d += M[3][k] * G[k];
		}
	}

	public double eval(double t) {
		return t * (t * (t * a + b) + c) + d;
	}

}
