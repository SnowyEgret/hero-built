package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.Cone;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

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
