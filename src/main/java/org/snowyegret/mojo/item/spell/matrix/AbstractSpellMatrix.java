package org.snowyegret.mojo.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.block.BlockMaquette;
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

	protected void transformSelections(Player player, Matrix4d matrix, EnumFacing side) {

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
			
			// Rotate the facing property if it exists
			IBlockState state = sel.getState();
			// TODO something like this?
			// EnumFacing facing = (EnumFacing) state.getValue(BlockMaquette.FACING);
			ImmutableMap props = state.getProperties();
			for (Object key : props.keySet()) {
				if (key instanceof PropertyDirection) {
					PropertyDirection prop = (PropertyDirection) key;
					EnumFacing facing = (EnumFacing) props.get(prop);
					// System.out.println("facing=" + facing);
					EnumFacing newFacing = null;
					if (this instanceof SpellMirror) {
						if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
							if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
								newFacing = facing.getOpposite();
							}
						} else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
							if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
								newFacing = facing.getOpposite();
							}
						}
					}

					else if (this instanceof SpellRotate) {
						boolean counterClockwise = player.getModifiers().isPressed(Modifier.ALT);
						if (side == EnumFacing.UP) {
							// if (plane == EnumPlane.HORIZONTAL_XZ) {
							newFacing = counterClockwise ? facing.rotateYCCW() : facing.rotateY();
							// newFacing = facing.rotateYCCW();
						} else if (side == EnumFacing.DOWN) {
							newFacing = counterClockwise ? facing.rotateY() : facing.rotateYCCW();
						}
					}
					// System.out.println("newFacing=" + newFacing);
					if (newFacing != null) {
						state = state.withProperty(prop, newFacing);
					}
					break;
				}
			}
			
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
		}

		player.getTransactionManager().doTransaction(deletes, undoables);
	}

}
