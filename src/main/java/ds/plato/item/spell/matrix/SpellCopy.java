package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.lwjgl.input.Keyboard;

import ds.geom.matrix.TranslationMatrix;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellCopy extends AbstractSpellMatrix {

	public SpellCopy(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Pick[] picks = pickManager.getPicks();
		Point3d from = picks[0].point3d();
		Point3d to = picks[1].point3d();
		Vector3d v = new Vector3d();
		v.sub(to, from);
		Matrix4d matrix = new TranslationMatrix(v);
		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		transformSelections(world, player, matrix, deleteOriginal);
		// Fix for MultiPlayer: Infinite loop when SpellCopy repicks. #95
		// Solution is a generic way to repeat last spell with same input
		// Can repeat with arrow keys in MP but only for orthogonal copies
		// pickManager.repick(world);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
