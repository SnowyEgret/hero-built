package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.surface.SeaShell;
import ds.geom.surface.Sphere;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

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
