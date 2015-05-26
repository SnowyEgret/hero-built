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
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class SpellMirror extends AbstractSpellMatrix {

	public SpellMirror(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {

		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		// boolean mirrorAboutCentroid = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		// Vec3 c = selectionManager.getCentroid();

		Pick[] picks = pickManager.getPicks();
		EnumFacing side = picks[0].getSide();
		Vec3i d = side.getDirectionVec();
		Point3d p = picks[0].point3d();
		Point3d offset = new Point3d(.49, .49, .49);
		switch (side){
		case DOWN:
			p.sub(offset); //ok
			break;
		case EAST:
			p.add(offset); //ok
			break;
		case NORTH:
			p.sub(offset); //ok
			break;
		case SOUTH:
			p.add(offset); //ok
			break;
		case UP:
			p.add(offset); //ok
			break;
		case WEST:
			p.sub(offset); //ok
			break;
		default:
			break;
		}
		Matrix4d matrix = new ReflectionMatrix(p, new Vector3d(d.getX(), d.getY(), d.getZ()));
		transformSelections(matrix, world, deleteOriginal);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
