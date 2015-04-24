package ds.plato.item.spell.draw;

import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.IDrawable;
import ds.geom.solid.Box;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;

public class SpellBox extends AbstractSpellDraw {

	public SpellBox(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		selectionManager.clearSelections(world);
		Pick[] picks = pickManager.getPicks();
		boolean isCube = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		if (onSurface) {
			p0.y += 1;
			p1.y += 1;
		}
		IDrawable d = new Box(p0, p1, isCube);
		boolean isHollow = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		draw(d, world, slotEntries[0].block, slotEntries[0].metadata, isHollow);
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
