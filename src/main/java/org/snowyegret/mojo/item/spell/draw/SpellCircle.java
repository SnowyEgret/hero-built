package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.IPick;
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
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		boolean onSurface = modifiers.isPressed(Modifier.SHIFT);
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		if (onSurface) {
			p0.y += 1;
			p1.y += 1;
		}
		IDrawable d = new CircleXZ(p0, p1);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
