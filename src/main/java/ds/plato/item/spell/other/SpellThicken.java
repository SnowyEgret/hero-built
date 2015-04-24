package ds.plato.item.spell.other;

import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.geom.GeomUtil;
import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.select.Shell;
import ds.plato.item.spell.transform.AbstractSpellTransform;
import ds.plato.select.Selection;
import ds.plato.undo.SetBlock;
import ds.plato.undo.Transaction;

public class SpellThicken extends AbstractSpellTransform {

	public SpellThicken(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(final IWorld world, HotbarSlot... slotEntries) {
		Set<BlockPos> points = new HashSet<>();
		Selection first = selectionManager.getSelections().iterator().next();
		VoxelSet voxels = selectionManager.voxelSet();
		IntegerDomain domain = selectionManager.voxelSet().getDomain();
		if (domain.isPlanar()) {
			thickenPlane(points, domain, world);
		} else {
			thicken(points, voxels, world);
		}

		selectionManager.clearSelections(world);
		Transaction t = undoManager.newTransaction();
		for (BlockPos p : points) {
			t.add(new SetBlock(world, selectionManager, p, first.getBlock()).set());
		}
		t.commit();
		pickManager.clearPicks();
	}

	private void thicken(Set<BlockPos> points, VoxelSet voxels, IWorld world) {
		final Point3d centroid = GeomUtil.toPoint3d(voxels.centroid());
		for (Selection s : selectionManager.getSelections()) {
			double d = s.point3d().distance(centroid);
			//double d = s.getPos().distanceSq(centroid);
			Shell shell = new Shell(Shell.Type.XYZ, s.getPos(), world);
			for (BlockPos p : shell) {
				//double dd = GeomUtil.toPoint3d(p).distance(centroid);
				double dd = new Point3d(p.getX(), p.getY(), p.getZ()).distance(centroid);
				boolean in = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
				boolean out = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
				if ((in && dd < d) || (out && dd > d) || (!in && !out)) {
					//if (!selectionManager.isSelected(p.x, p.y, p.z)) {
					if (!selectionManager.isSelected(p)) {
						points.add(p);
					}
				}
			}
		}
	}

	private void thickenPlane(Set<BlockPos> points, IntegerDomain domain, IWorld world) {
		boolean withinPlane = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		Shell.Type shellType = null;
		System.out.println("[SpellThicken.thickenPlane] domain.getPlane()=" + domain.getPlane());
		switch (domain.getPlane()) {
		case XY:
			shellType = withinPlane ? Shell.Type.XY : Shell.Type.Z;
			break;
		case XZ:
			System.out.println("[SpellThicken.thickenPlane] shellType=" + shellType);
			shellType = withinPlane ? Shell.Type.XZ : Shell.Type.Y;
			break;
		case YZ:
			shellType = withinPlane ? Shell.Type.YZ : Shell.Type.X;
			break;
		}
		System.out.println("[SpellThicken.thickenPlane] shellType=" + shellType);
		for (Selection s : selectionManager.getSelections()) {
			Shell shell = new Shell(shellType, s.getPos(), world);
			for (BlockPos p : shell) {
				if (!selectionManager.isSelected(p)) {
					points.add(p);
				}
			}
		}
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
