package org.snowyegret.mojo.item.spell.draw;

import org.lwjgl.input.Keyboard;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.undo.IUndo;

import ds.geom.IDrawable;
import ds.geom.curve.Rectangle;

public class SpellRectangle extends AbstractSpellDraw {

	public SpellRectangle() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		boolean isSquare = modifiers.isPressed(Modifier.CTRL);
		IDrawable d = new Rectangle(picks[0].point3d(), picks[1].point3d(), isSquare);
		draw(d, player, null);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
