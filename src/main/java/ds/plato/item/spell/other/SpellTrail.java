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
import ds.plato.world.IWorld;

public class SpellTrail extends Spell {

	public SpellTrail() {
		super(2);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();
		
		//On LivingUpdateEvent selections are added when SpellTrail is in hand
		//boolean fill = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		//boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Transaction transaction = undoManager.newTransaction();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
		for (Selection s : selections) {
			world.setState(s.getPos(), player.getHotbar().firstBlock());
		}
		transaction.commit();
	}

}
