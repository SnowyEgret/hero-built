package org.snowyegret.mojo.item.spell.draw;

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

import ds.geom.IDrawable;
import ds.geom.VoxelSet;
import ds.geom.matrix.RotationMatrix;
import ds.geom.solid.Solid;

public abstract class AbstractSpellDraw extends Spell {

	public AbstractSpellDraw(int numPicks) {
		super(numPicks);
		info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

	protected void draw(IDrawable drawable, IPlayer player, EnumFacing side) {

		Modifiers modifiers = player.getModifiers();
		boolean isHollow = modifiers.isPressed(Modifier.SHIFT);
		boolean onSurface = modifiers.isPressed(Modifier.ALT);

		player.clearSelections();
		player.clearPicks();

		// Get rotation matrix based on side
		Vec3i d = side.getDirectionVec();
		RotationMatrix matrix = new RotationMatrix(new Vector3d(0, 1, 0), new Vector3d(d.getX(), d.getY(), d.getZ()));
//		for (Point3d p : drawable.pointSet()) {
//			matrix.transform(p);
//		}

		VoxelSet voxels = drawable.voxelize();
		if (drawable instanceof Solid && isHollow) {
			voxels = voxels.shell();
		}

		List<IUndoable> undoables = Lists.newArrayList();
		IBlockState state = player.getHotbar().firstBlock();
		for (Point3i p : voxels) {
//			Point3d pp = new Point3d(p.x, p.y, p.z);
//			matrix.transform(pp);
//			BlockPos pos = new BlockPos(pp.x, pp.y, pp.z);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			if (onSurface) {
				pos = pos.add(side.getDirectionVec());
			}
			undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
		}

		player.getTransactionManager().doTransaction(undoables);
	}
}
