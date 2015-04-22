package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.IDrawable;
import ds.geom.surface.SeaShell;
import ds.geom.surface.Sphere;
import ds.plato.pick.Pick;

public class SpellSeaShell extends AbstractSpellDraw {

	public SpellSeaShell(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		selectionManager.clearSelections();
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new SeaShell(picks[0].point3d());
		draw(d, world, slotEntries[0].block, slotEntries[0].metadata);
		pickManager.clearPicks();
	}

}
