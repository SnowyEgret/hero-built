package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.IDrawable;
import ds.geom.curve.Line;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;

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
			draw(d, world, slotEntries[0].block, slotEntries[0].metadata);
			pickManager.clearPicks();
			pickManager.reset(2);
			//pickManager.pick(world, picks[1].x, picks[1].y, picks[1].z, 0);
			pickManager.pick(world, picks[1].getPos(), null);
		} else {
			selectionManager.clearSelections();
			// Best in this order
			pickManager.clearPicks();
			draw(d, world, slotEntries[0].block, slotEntries[0].metadata);
		}
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
