package ds.plato.item.spell.matrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import org.lwjgl.input.Keyboard;

import ds.geom.matrix.ReflectionMatrix;
import ds.plato.item.spell.Modifier;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellMirror extends AbstractSpellMatrix {

	public SpellMirror(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {

		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		boolean mirrorAboutCentroid = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		// boolean mirrorAboutCentroid = Modifiers.isPressed(Modifier.ALT);
		// Vec3 c = selectionManager.getCentroid();

		Pick[] picks = pickManager.getPicks();
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
		transformSelections(world, player, matrix, deleteOriginal);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
	
}
