package org.snowyegret.mojo.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.SelectionManager;

import org.snowyegret.geom.matrix.RotationMatrix;

public class SpellRotate extends AbstractSpellMatrix {

	public SpellRotate() {
		super(1);
		info.addModifiers(Modifier.SHIFT, Modifier.ALT, Modifier.X, Modifier.Y, Modifier.Z);
	}

	@Override
	public void invoke(Player player) {

		Modifiers modifiers = player.getModifiers();
		boolean rotateAboutCentroid = modifiers.isPressed(Modifier.SHIFT);
		boolean counterClockwise = modifiers.isPressed(Modifier.ALT);

		SelectionManager selectionManager = player.getSelectionManager();
		Pick[] picks = player.getPicks();
		player.clearPicks();
		if (selectionManager.size() == 0) {
			selectionManager.select(picks[0].getPos());
		}

		Point3d center = null;
		if (rotateAboutCentroid) {
			Vec3 c = player.getSelectionManager().getCentroid();
			// TODO getCentroid returns Point3d
			center = new Point3d(c.xCoord, c.yCoord, c.zCoord);
		} else {
			center = picks[0].point3d();
		}

		// First check for modifier keys, then use face to determine which plane to rotate in
		Vector3d axis = null;
		if (modifiers.isPressed(Modifier.X)) {
			axis = new Vector3d(1, 0, 0);
		} else if (modifiers.isPressed(Modifier.Y)) {
			axis = new Vector3d(0, 1, 0);
		} else if (modifiers.isPressed(Modifier.Z)) {
			axis = new Vector3d(0, 0, 1);
		} else {
			Vec3i d = picks[0].side.getDirectionVec();
			axis = new Vector3d(d.getX(), d.getY(), d.getZ());
		}
		double angle = Math.PI / 2;
		Matrix4d matrix = new RotationMatrix(center, axis, counterClockwise ? angle : -angle);

		// TODO
		transformSelections(player, matrix, picks[0].side);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
