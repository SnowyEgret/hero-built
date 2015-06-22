package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.item.spell.Modifier;
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
		
		// TODO test for plane
		IDrawable d = null;
		if (modifiers.isPressed(Modifier.X)) {
			//d = new CircleYZ(p0, p1);
		} else if (modifiers.isPressed(Modifier.Y)) {
			d = new CircleXZ(p0, p1);
		} else if (modifiers.isPressed(Modifier.Z)) {
			//d = new CircleXY(p0, p1);
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
