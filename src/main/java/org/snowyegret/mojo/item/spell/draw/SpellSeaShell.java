package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;

import ds.geom.IDrawable;
import ds.geom.surface.SeaShell;

public class SpellSeaShell extends AbstractSpellDraw {

	public SpellSeaShell() {
		super(1);
	}

	@Override
	public void invoke(Player player) {
		Pick[] picks = player.getPicks();
		IDrawable d = new SeaShell(picks[0].point3d());
		draw(d, player, picks[0].side);
	}
	
}
