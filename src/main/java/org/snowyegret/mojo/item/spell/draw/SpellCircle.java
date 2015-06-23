package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.geom.CircleXY;
import org.snowyegret.mojo.geom.CircleYZ;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.util.Hyperplane;
import org.snowyegret.mojo.util.MathUtils;
import org.snowyegret.mojo.util.PlaneXY;
import org.snowyegret.mojo.util.PlaneXZ;
import org.snowyegret.mojo.util.PlaneYZ;

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
		
		Hyperplane plane = MathUtils.getHyperplane(player.getWorld().getWorld(), picks[0].getPos(), picks[1].getPos());
		System.out.println(plane);
		IDrawable d = null;
		if (plane instanceof PlaneYZ) {
			d = new CircleYZ(p0, p1);
		} else if (plane instanceof PlaneXZ) {
			d = new CircleXZ(p0, p1);
		} else if (plane instanceof PlaneXY) {
			d = new CircleXY(p0, p1);
		} else {
			d = new CircleXZ(p0, p1);
		}

		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
