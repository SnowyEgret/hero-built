package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;

import org.snowyegret.geom.IDrawable;
import org.snowyegret.geom.curve.Rectangle;

public class SpellRectangle extends AbstractSpellDraw {

	public SpellRectangle() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(Player player) {
		Modifiers modifiers = player.getModifiers();
		Pick[] picks = player.getPicks();
		boolean isSquare = modifiers.isPressed(Modifier.CTRL);
		IDrawable d = new Rectangle(picks[0].point3d(), picks[1].point3d(), isSquare);
		draw(d, player, picks[0].side);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

	
}
