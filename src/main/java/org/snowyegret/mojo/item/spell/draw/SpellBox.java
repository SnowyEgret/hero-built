package org.snowyegret.mojo.item.spell.draw;

import javax.vecmath.Point3d;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;

import org.snowyegret.geom.IDrawable;
import org.snowyegret.geom.solid.Box;

public class SpellBox extends AbstractSpellDraw {

	public SpellBox() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(Player player) {
		Modifiers modifiers = player.getModifiers();
		boolean isCube = modifiers.isPressed(Modifier.CTRL);
		Pick[] picks = player.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		IDrawable d = new Box(p0, p1, isCube);
		draw(d, player, picks[0].side);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
