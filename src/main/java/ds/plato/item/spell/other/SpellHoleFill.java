package ds.plato.item.spell.other;

import java.util.HashSet;
import java.util.Set;

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
		boolean isHorizontal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		boolean useBlockInHotbar = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		Set<UndoableSetBlock> setBlocks = new HashSet();
		for (Selection s : selections) {
			BlockPos[] pos = isHorizontal ? Select.horizontal : Select.belowInclusive;
			for (BlockPos p : pos) {
				p = p.add(s.getPos());
				Block b = world.getBlock(p);
				if (b == Blocks.air || b == Blocks.water) {
					if (useBlockInHotbar) {
						setBlocks.add(new UndoableSetBlock(world, selectionManager, p, slots[0].block));
					} else {
						setBlocks.add(new UndoableSetBlock(world, selectionManager, p, s.getBlock()));
					}
				}
			}
		}
		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : setBlocks) {
			t.add(u.set());
		}
		t.commit();
	}
}
