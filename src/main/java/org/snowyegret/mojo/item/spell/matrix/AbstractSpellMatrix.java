package org.snowyegret.mojo.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks) {
		super(numPicks);
	}
	

	protected void transformSelections(Player player, Matrix4d matrix) {

		boolean deleteInitialBlocks = player.getModifiers().isPressed(Modifier.CTRL);
		//Deletes must be done first
		List<IUndoable> deletes = Lists.newArrayList();
		List<IUndoable> undoables = Lists.newArrayList();

		Iterable<Selection> selections = player.getSelections();
		player.clearSelections();
		player.clearPicks();
		
		final IBlockState air = Blocks.air.getDefaultState();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(s.getPos(), player.getWorld().getState(s.getPos()), air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), s.getState()));
		}

		player.getTransactionManager().doTransaction(deletes, undoables);
	}

}
