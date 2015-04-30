package ds.plato.item.spell.draw;

import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.geom.IDrawable;
import ds.geom.solid.RectangularPyramid;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;

public class SpellPyramid extends AbstractSpellDraw {

	public SpellPyramid(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		selectionManager.clearSelections(world);
		boolean isSquare = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
//		boolean isHollow = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
//		boolean onSurface = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Pick[] picks = pickManager.getPicks();
		Point3d p0 = picks[0].point3d();
		Point3d p1 = picks[1].point3d();
		IDrawable d = new RectangularPyramid(p0, p1, isSquare);
		//draw(d, world, slotEntries[0].block, isHollow, onSurface);
		draw(d, world, slotEntries[0].block);
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
