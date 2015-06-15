package ds.plato.item.spell.other;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Select;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public class SpellHoleFill extends Spell {

	public SpellHoleFill() {
		super(1);
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.water_bucket };
	}

	@Override
	public void invoke(IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		boolean isHorizontal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		boolean useBlockInHotbar = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		pickManager.clearPicks(player);
		Set<UndoableSetBlock> setBlocks = new HashSet();
		for (Selection s : selections) {
			BlockPos[] pos = isHorizontal ? Select.horizontal : Select.belowInclusive;
			for (BlockPos p : pos) {
				p = p.add(s.getPos());
				Block b = player.getWorld().getBlock(p);
				if (b == Blocks.air || b == Blocks.water) {
					if (useBlockInHotbar) {
						setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, p, player.getHotbar().firstBlock()));
					} else {
						setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, p, s.getState()));
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
