package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.geom.CubicSpline;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import ds.geom.IDrawable;

public class SpellSpline extends AbstractSpellDraw {

	public SpellSpline() {
		super(3);
	}

	@Override
	public void invoke(IPlayer player) {
		Pick[] picks = player.getPicks();
		IDrawable d = new CubicSpline(picks);
		draw(d, player, picks[0].side);
	}


}
