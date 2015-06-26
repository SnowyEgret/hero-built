package org.snowyegret.mojo.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;

import ds.geom.matrix.ReflectionMatrix;

public class SpellMirror extends AbstractSpellMatrix {

	public SpellMirror() {
		super(1);
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		boolean mirrorAboutCentroid = modifiers.isPressed(Modifier.SHIFT);

		Pick[] picks = player.getPicks();
		EnumFacing side = picks[0].getSide();
		Vec3i d = side.getDirectionVec();
		Point3d p = picks[0].point3d();
		Point3d offset = null;
		//TODO Simplify this. Just trial and error. 
		if (mirrorAboutCentroid) {
			offset = new Point3d(-.01, -01, -.01);
		} else {
			offset = new Point3d(.49, .49, .49);
		}
		switch (side) {
		case UP:
			if (mirrorAboutCentroid) {
				offset.y += 1;
			} else {
				offset.y += .02;
			}
			p.add(offset); // okkk
			break;
		case DOWN:
			if (mirrorAboutCentroid) {
				offset.y += .5;
			}
			p.sub(offset); // okkk
			break;
		case EAST:
			offset.x += .02;
			p.add(offset); // okkk
			break;
		case WEST:
			p.sub(offset); // okkk
			break;
		case NORTH:
			p.sub(offset); // okkk
			break;
		case SOUTH:
			offset.z += .02;
			p.add(offset); // okkk
			break;
		default:
			break;
		}
		Matrix4d matrix = new ReflectionMatrix(p, new Vector3d(d.getX(), d.getY(), d.getZ()));
		transformSelections(player, matrix);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
	
}
