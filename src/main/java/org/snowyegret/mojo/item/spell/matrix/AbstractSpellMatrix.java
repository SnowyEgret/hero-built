package org.snowyegret.mojo.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.geom.EnumPlane;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks) {
		super(numPicks);
	}

	protected void transformSelections(Player player, Matrix4d matrix, EnumPlane plane) {

		boolean deleteInitialBlocks = player.getModifiers().isPressed(Modifier.CTRL);
		// Deletes must be done first
		List<IUndoable> deletes = Lists.newArrayList();
		List<IUndoable> undoables = Lists.newArrayList();

		Iterable<Selection> selections = player.getSelections();
		player.clearSelections();
		player.clearPicks();

		final IBlockState air = Blocks.air.getDefaultState();
		for (Selection sel : selections) {
			Point3d p = sel.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(sel.getPos(), player.getWorld().getState(sel.getPos()), air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			IBlockState state = sel.getState();
			ImmutableMap props = state.getProperties();
			for (Object k : props.keySet()) {
				if (k instanceof PropertyDirection) {
					PropertyDirection prop = (PropertyDirection) k;
					EnumFacing facing = (EnumFacing) props.get(prop);
					//System.out.println("facing=" + facing);
					EnumFacing newFacing = null;
					// if (plane == EnumFacing.EAST || plane == EnumFacing.WEST) {
					if (plane == EnumPlane.VERTICAL_YZ) {
						if (facing == EnumFacing.EAST) {
							newFacing = EnumFacing.WEST;
						}
						if (facing == EnumFacing.WEST) {
							newFacing = EnumFacing.EAST;
						}
					}
					// if (plane == EnumFacing.NORTH || plane == EnumFacing.SOUTH) {
					if (plane == EnumPlane.VERTICAL_XY) {
						if (facing == EnumFacing.NORTH) {
							newFacing = EnumFacing.SOUTH;
						}
						if (facing == EnumFacing.SOUTH) {
							newFacing = EnumFacing.NORTH;
						}
					}
					//System.out.println("newFacing=" + newFacing);
					if (newFacing != null) {
						state = state.withProperty(prop, newFacing);
					}
					break;
				}
			}
			undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
		}

		player.getTransactionManager().doTransaction(deletes, undoables);
	}

}
