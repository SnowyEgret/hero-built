package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3i;

import org.lwjgl.input.Keyboard;

import ds.geom.matrix.ReflectionMatrix;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.Pick;

public class SpellMirror extends AbstractSpellMatrix {

	public SpellMirror(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(3, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		Pick[] picks = pickManager.getPicks();
		Point3i c = selectionManager.voxelSet().centroid();
		// FIXME
		Matrix4d matrix = new ReflectionMatrix(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		boolean deleteInitialBlocks = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		transformSelections(matrix, world, deleteInitialBlocks);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
