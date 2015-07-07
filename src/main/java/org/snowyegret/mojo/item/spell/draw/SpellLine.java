package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.player.Player;

import org.snowyegret.geom.IDrawable;
import org.snowyegret.geom.curve.Line;

public class SpellLine extends AbstractSpellDraw {

	public SpellLine() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(Player player) {
		Modifiers modifiers = player.getModifiers();
		boolean isPolyline = modifiers.isPressed(Modifier.CTRL);
		PickManager pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Line(picks[0].point3d(), picks[1].point3d());
		draw(d, player, picks[0].side);
		if (isPolyline) {
			pickManager.setNumPicks(2);
			pickManager.clearPicks();
			pickManager.pick(picks[1].getPos(), picks[0].side);
		}
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	
}
