package ds.plato.item.spell.draw;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.geom.IDrawable;
import ds.geom.surface.Cone;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;

public class SpellCone extends AbstractSpellDraw {

	public SpellCone(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(3, undoManager, selectionManager, pickManager);
		//info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		Pick[] picks = pickManager.getPicks();
		IDrawable d = new Cone(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		draw(d, world, slotEntries[0].block);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
