package ds.plato.item.spell.other;

import net.minecraft.block.state.IBlockState;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public class SpellTrail extends Spell {

	public SpellTrail() {
		super(2);
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();

		// On LivingUpdateEvent selections are added when SpellTrail is in hand
		// boolean fill = modifiers.isPressed(Modifier.SHIFT);
		// boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);
		IBlockState firstBlock = player.getHotbar().firstBlock();

		Transaction t = new Transaction();
		for (Selection s : selections) {
			t.add(new UndoableSetBlock(s.getPos(), player.getWorld().getState(s.getPos()), firstBlock));
		}
		t.dO(player);

	}

}
