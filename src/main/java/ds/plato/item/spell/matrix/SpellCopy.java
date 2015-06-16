package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.lwjgl.input.Keyboard;

import ds.geom.matrix.TranslationMatrix;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;

public class SpellCopy extends AbstractSpellMatrix {

	public SpellCopy() {
		super(2);
		info.addModifiers(Modifier.SHIFT);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		Pick[] picks = pickManager.getPicks();
		Point3d from = picks[0].point3d();
		Point3d to = picks[1].point3d();
		Vector3d v = new Vector3d();
		v.sub(to, from);
		Matrix4d matrix = new TranslationMatrix(v);
		boolean deleteOriginal = modifiers.isPressed(Modifier.SHIFT);
		transformSelections(player, matrix, deleteOriginal);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
