package ds.plato.item.spell.other;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellTrail extends Spell {

	public SpellTrail(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(2, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {
		//On LivingUpdateEvent selections are added when SpellTrail is in hand
		//boolean fill = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		//boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Transaction transaction = undoManager.newTransaction();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		for (Selection s : selections) {
			world.setBlockState(s.getPos(), slots[0].state);
		}
		transaction.commit();
	}

}
