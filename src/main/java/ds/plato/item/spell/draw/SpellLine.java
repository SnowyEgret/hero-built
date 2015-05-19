package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.geom.IDrawable;
import ds.geom.curve.Line;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;

public class SpellLine extends AbstractSpellDraw {

	public SpellLine(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Line(picks[0].point3d(), picks[1].point3d());
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			draw(d, world, slotEntries[0].block);
			pickManager.clearPicks();
			pickManager.reset(2);
			pickManager.pick(world, picks[1].getPos(), null);
		} else {
			draw(d, world, slotEntries[0].block);
		}
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

}
