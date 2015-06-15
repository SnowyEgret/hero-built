package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.curve.Rectangle;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

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
		boolean isSquare = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		IDrawable d = new Rectangle(picks[0].point3d(), picks[1].point3d(), isSquare);
		draw(d, player);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
