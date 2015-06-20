package org.snowyegret.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;
import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;

import ds.geom.IDrawable;
import ds.geom.curve.Line;

public class SpellLine extends AbstractSpellDraw {

	public SpellLine() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Line(picks[0].point3d(), picks[1].point3d());
		draw(d, player);
		if (modifiers.isPressed(Modifier.CTRL)) {
			pickManager.reset(2);
			pickManager.clearPicks(player);
			pickManager.pick(player, picks[1].getPos(), null);
		}
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
