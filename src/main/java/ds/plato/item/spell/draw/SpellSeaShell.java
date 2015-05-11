package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.geom.IDrawable;
import ds.geom.surface.SeaShell;
import ds.geom.surface.Sphere;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;

public class SpellSeaShell extends AbstractSpellDraw {

	public SpellSeaShell(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		selectionManager.clearSelections(world);
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new SeaShell(picks[0].point3d());
		draw(d, world, slotEntries[0].block);
		pickManager.clearPicks();
	}
}
