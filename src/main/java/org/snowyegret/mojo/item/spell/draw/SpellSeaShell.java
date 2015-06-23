package org.snowyegret.mojo.item.spell.draw;

import org.lwjgl.input.Keyboard;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.undo.IUndo;

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
		draw(d, player, null);
	}
}
