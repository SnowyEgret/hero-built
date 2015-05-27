package ds.plato.item.spell.other;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Select;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellHoleFill extends Spell {

	public SpellHoleFill(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.water_bucket };
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {
		Transaction t = undoManager.newTransaction();
		for (Selection s : selectionManager.getSelections()) {
			// Shell.Type type = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? Shell.Type.HORIZONTAL : Shell.Type.BELLOW;
			BlockPos[] pos = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? Select.horizontal : Select.below;
			// Shell shell = new Shell(type, s.getPos(), world);
			// for (BlockPos p : shell) {
			for (BlockPos p : pos) {
				Block b = world.getBlock(p.add(s.getPos()));
				if (b == Blocks.air || b == Blocks.water) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						t.add(new UndoableSetBlock(world, selectionManager, p, slots[0].block).set());
					} else {
						t.add(new UndoableSetBlock(world, selectionManager, p, s.getBlock()).set());
					}
				}
			}
			selectionManager.deselect(world, s);
		}
		t.commit();
	}
}
