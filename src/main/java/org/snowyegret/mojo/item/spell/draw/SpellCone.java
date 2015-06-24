package org.snowyegret.mojo.item.spell.draw;

import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import ds.geom.IDrawable;
import ds.geom.surface.Cone;

public class SpellCone extends AbstractSpellDraw {

	public SpellCone() {
		super(3);
	}

	@Override
	public void invoke(IPlayer player) {
		Pick[] picks = player.getPicks();
		IDrawable d = new Cone(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		draw(d, player, null);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
