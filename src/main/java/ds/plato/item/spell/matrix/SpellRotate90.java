package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import ds.geom.GeomUtil;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;

public class SpellRotate90 extends AbstractSpellMatrix {

	public SpellRotate90() {
		super(1);
		info.addModifiers(Modifier.ALT, Modifier.X, Modifier.Y, Modifier.Z);
	}

	@Override
	public void invoke(IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		IPick pickManager = player.getPickManager();
		ISelect selectionManager = player.getSelectionManager();

		boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
		boolean rotateAboutCentroid = modifiers.isPressed(Modifier.SHIFT);
		Pick[] picks = pickManager.getPicks();
		Point3d center = null;
		if (rotateAboutCentroid) {
			Vec3 c = selectionManager.getCentroid();
			center = new Point3d(c.xCoord, c.yCoord, c.zCoord);
		} else {
			center = picks[0].point3d();
		}

		// TODO switch to RotationMatrix constructor with 2 vectors
		Matrix4d matrix;
		switch (Keyboard.getEventKey()) {
		case (Keyboard.KEY_X):
			matrix = GeomUtil.newRotX90Matrix(center);
			break;
		case (Keyboard.KEY_Y):
			matrix = GeomUtil.newRotY90Matrix(center);
			break;
		case (Keyboard.KEY_Z):
			matrix = GeomUtil.newRotZ90Matrix(center);
			break;
		default:
			matrix = GeomUtil.newRotY90Matrix(center);
		}

		transformSelections(player, matrix, deleteOriginal);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
