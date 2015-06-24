package org.snowyegret.mojo.item.spell.other;

import net.minecraft.block.state.IBlockState;

import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

public class SpellTrail extends Spell {

	public SpellTrail() {
		super(2);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		SelectionManager selectionManager = player.getSelectionManager();

		// On LivingUpdateEvent selections are added when SpellTrail is in hand
		// boolean fill = modifiers.isPressed(Modifier.SHIFT);
		// boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections();
		player.getPickManager().clearPicks();
		IBlockState firstBlock = player.getHotbar().firstBlock();

		Transaction t = new Transaction();
		for (Selection s : selections) {
			t.add(new UndoableSetBlock(s.getPos(), player.getWorld().getState(s.getPos()), firstBlock));
		}
		t.dO(player);

	}

}
