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
import ds.geom.surface.Cone;

public class SpellCone extends AbstractSpellDraw {

	public SpellCone() {
		super(3);
	}

	@Override
	public void invoke(IPlayer player) {
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Cone(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
