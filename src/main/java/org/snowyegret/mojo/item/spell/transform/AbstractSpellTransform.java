package org.snowyegret.mojo.item.spell.transform;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
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

	protected void transformSelections(IPlayer player, ITransform<Selection> transformer) {
		SelectionManager selectionManager = player.getSelectionManager();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player); 
		player.getPickManager().clearPicks(player);
		
		List<IUndoable> setBlocks = Lists.newArrayList();
		for (Selection s : selections) {
			for (Selection ss : transformer.transform(s)) {
				BlockPos pos = ss.getPos();
				IBlockState state = ss.getState();
				setBlocks.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
			}
		}

		Transaction t = new Transaction();
		t.addAll(setBlocks);
		t.dO(player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
