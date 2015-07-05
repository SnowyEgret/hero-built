package org.snowyegret.mojo.item.spell.other;

import java.util.List;

import net.minecraft.block.state.IBlockState;

import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

public class SpellTrail extends Spell {

	public SpellTrail() {
		super(2);
	}

	@Override
	public void invoke(Player player) {
		Modifiers modifiers = player.getModifiers();
		SelectionManager selectionManager = player.getSelectionManager();

		// On LivingUpdateEvent selections are added when SpellTrail is in hand
		// boolean fill = modifiers.isPressed(Modifier.SHIFT);
		// boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections();
		player.getPickManager().clearPicks();
		IBlockState firstBlock = player.getHotbar().firstBlock();

		List<IUndoable> undoables = Lists.newArrayList();
		for (Selection s : selections) {
			undoables.add(new UndoableSetBlock(s.getPos(), player.getWorld().getState(s.getPos()), firstBlock));
		}
		player.getTransactionManager().doTransaction(undoables);

	}

}
