package org.snowyegret.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.undo.IUndo;

import ds.geom.IDrawable;
import ds.geom.surface.SeaShell;
import ds.geom.surface.Sphere;

public class SpellSeaShell extends AbstractSpellDraw {

	public SpellSeaShell() {
		super(1);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new SeaShell(picks[0].point3d());
		draw(d, player);
	}
}
