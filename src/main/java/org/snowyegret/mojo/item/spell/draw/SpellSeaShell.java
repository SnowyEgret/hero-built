package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;

import org.snowyegret.geom.IDrawable;
import org.snowyegret.geom.surface.SeaShell;

public class SpellSeaShell extends AbstractSpellDraw {

	public SpellSeaShell() {
		super(2);
	}

	@Override
	public void invoke(Player player) {
		Pick[] picks = player.getPicks();
		//TODO
		// origin
		// how many times it goes around
		// offset (the ratio of a to b) 5 / 2 is ideal
		IDrawable d = new SeaShell(picks[0].point3d(), picks[1].point3d());
		draw(d, player, picks[0].side);  
	}

}
