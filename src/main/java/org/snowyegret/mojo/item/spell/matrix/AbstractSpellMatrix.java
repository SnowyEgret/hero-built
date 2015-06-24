package org.snowyegret.mojo.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks) {
		super(numPicks);
	}

	protected void transformSelections(IPlayer player, Matrix4d matrix, boolean deleteInitialBlocks) {

		Modifiers modifiers = player.getModifiers();
		SelectionManager selectionManager = player.getSelectionManager();

		List<IUndoable> deletes = Lists.newArrayList();
		List<IUndoable> setBlocks = Lists.newArrayList();

		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections();
		player.getPickManager().clearPicks();
		
		IBlockState air = Blocks.air.getDefaultState();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(s.getPos(), player.getWorld().getState(s.getPos()), air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			setBlocks.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), s.getState()));
		}

		player.getTransactionManager().doTransaction(deletes, setBlocks);
	}

}
