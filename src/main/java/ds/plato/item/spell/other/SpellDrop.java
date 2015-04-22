package ds.plato.item.spell.other;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.select.Selection;
import ds.plato.undo.SetBlock;
import ds.plato.undo.Transaction;

public class SpellDrop extends Spell {


	public SpellDrop(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		boolean fill = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Transaction transaction = undoManager.newTransaction();
		for (Selection s : selectionManager.getSelections()) {
			Block b = world.getBlock(s.x, s.y - 1, s.z);
			int drop = 0;
			while (b == Blocks.air) {
				drop++;
				Block nextBlockDown = world.getBlock(s.x, s.y - drop - 1, s.z);
				if (fill || nextBlockDown != Blocks.air) {
					transaction.add(new SetBlock(world, selectionManager, s.x, s.y - drop, s.z, s.block, s.metadata)
							.set());
				}
				b = nextBlockDown;
			}
			selectionManager.deselect(s);
			if (deleteOriginal) {
				transaction.add(new SetBlock(world, selectionManager, s.x, s.y, s.z, Blocks.air, 0).set());
			}
		}
		transaction.commit();
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}

//	@Override
//	public IModelCustom getModel() {
//		return model;
//	}

}
