package org.snowyegret.plato.item.spell.draw;

import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;
import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;

import ds.geom.IDrawable;
import ds.geom.solid.Box;

public class SpellBox extends AbstractSpellDraw {

	public SpellBox() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		boolean isCube = modifiers.isPressed(Modifier.CTRL);
		Pick[] picks = pickManager.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		IDrawable d = new Box(p0, p1, isCube);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
