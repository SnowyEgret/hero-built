package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.geom.IDrawable;
import ds.geom.curve.Line;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellLine extends AbstractSpellDraw {

	public SpellLine() {
		super(2);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Line(picks[0].point3d(), picks[1].point3d());
		draw(d, world, player);
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			pickManager.reset(2);
			pickManager.clearPicks(player);
			//TODO Can we pass null here?
			pickManager.pick(world, picks[1].getPos(), null);
		}
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
