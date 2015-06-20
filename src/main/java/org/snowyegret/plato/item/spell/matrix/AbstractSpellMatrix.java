package org.snowyegret.plato.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.item.spell.Spell;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.player.Jumper;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.select.Selection;
import org.snowyegret.plato.undo.IUndoable;
import org.snowyegret.plato.undo.Transaction;
import org.snowyegret.plato.undo.UndoableSetBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks) {
		super(numPicks);
	}

	protected void transformSelections(IPlayer player, Matrix4d matrix, boolean deleteInitialBlocks) {

		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();

		List<IUndoable> deletes = Lists.newArrayList();
		List<IUndoable> setBlocks = Lists.newArrayList();

		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);
		
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

		Transaction t = new Transaction();
		t.addAll(deletes);
		t.addAll(setBlocks);
		t.dO(player);
	}

}
