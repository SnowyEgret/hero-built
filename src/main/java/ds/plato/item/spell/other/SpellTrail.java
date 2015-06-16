package ds.plato.item.spell.other;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.lwjgl.input.Keyboard;

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
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();
		
		//On LivingUpdateEvent selections are added when SpellTrail is in hand
		//boolean fill = modifiers.isPressed(Modifier.SHIFT);
		//boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
		Transaction transaction = undoManager.newTransaction();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		pickManager.clearPicks(player);
		for (Selection s : selections) {
			player.getWorld().setState(s.getPos(), player.getHotbar().firstBlock());
		}
		transaction.commit();
	}

}
