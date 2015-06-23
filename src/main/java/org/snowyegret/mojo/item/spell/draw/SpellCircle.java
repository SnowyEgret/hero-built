package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.geom.CircleXY;
import org.snowyegret.mojo.geom.CircleYZ;
import org.snowyegret.mojo.geom.Plane;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import ds.geom.IDrawable;
import ds.geom.curve.CircleXZ;

public class SpellCircle extends AbstractSpellDraw {

	public SpellCircle() {
		super(2);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		Pick[] picks = player.getPickManager().getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		
		Plane plane = Plane.getPlane(player, picks[0].getPos(), picks[1].getPos());
		System.out.println(plane);
		IDrawable d = null;
		switch (plane.getPlane()) {
		case XY:
			d = new CircleXY(p0, p1);
			break;
		case XZ:
			d = new CircleXZ(p0, p1);
			break;
		case YZ:
			d = new CircleYZ(p0, p1);
			break;
		default:
			d = new CircleXZ(p0, p1);
			break;
		}
		draw(d, player, plane);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
