package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3i;

import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import ds.geom.matrix.ReflectionMatrix;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellMirror extends AbstractSpellMatrix {

	public SpellMirror(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(3, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {

		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		// boolean mirrorAboutCentroid = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Pick[] picks = pickManager.getPicks();
		Vec3 c = selectionManager.getCentroid();
		Matrix4d matrix = new ReflectionMatrix(picks[0].point3d(), picks[1].point3d(), picks[2].point3d());
		transformSelections(matrix, world, deleteOriginal);
		//pickManager.repick();
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
