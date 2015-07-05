package org.snowyegret.mojo.item.spell.transform;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform() {
		super(1);
	}

	protected void transformSelections(Player player, ITransform<Selection> transformer) {
		Iterable<Selection> selections = player.getSelections();
		player.clearSelections(); 
		player.clearPicks();
		
		List<IUndoable> undoables = Lists.newArrayList();
		for (Selection s : selections) {
			for (Selection ss : transformer.transform(s)) {
				BlockPos pos = ss.getPos();
				IBlockState state = ss.getState();
				undoables.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
			}
		}

		player.getTransactionManager().doTransaction(undoables);
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
