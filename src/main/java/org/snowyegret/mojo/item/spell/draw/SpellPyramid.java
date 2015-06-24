package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import ds.geom.IDrawable;
import ds.geom.solid.RectangularPyramid;

public class SpellPyramid extends AbstractSpellDraw {

	public SpellPyramid() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		boolean isSquare = modifiers.isPressed(Modifier.CTRL);
		Pick[] picks = player.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		IDrawable d = new RectangularPyramid(p0, p1, isSquare);
		draw(d, player, null);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
