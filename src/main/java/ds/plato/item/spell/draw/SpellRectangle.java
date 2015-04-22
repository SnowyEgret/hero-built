package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.IDrawable;
import ds.geom.curve.Rectangle;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;

public class SpellRectangle extends AbstractSpellDraw {

	public SpellRectangle(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		selectionManager.clearSelections();
		Pick[] picks = pickManager.getPicks();
		boolean isSquare = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		IDrawable d = new Rectangle(picks[0].point3d(), picks[1].point3d(), isSquare);
		draw(d, world, slotEntries[0].block, slotEntries[0].metadata);
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
