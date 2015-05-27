package ds.plato.item.spell.other;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellDrop extends Spell {

	public SpellDrop(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
		info.addModifiers(Modifier.CTRL, Modifier.ALT, Modifier.SHIFT);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {

		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		boolean fill = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		// TODO check for all air or all non-air around block
		boolean raise = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		if (raise) {
			deleteOriginal = true;
		}

		List<UndoableSetBlock> setBlocks = new ArrayList();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		for (Selection s : selections) {
			if (raise) {
				setBlocks.addAll(raiseBurriedBlocks(world, s));
			} else {
				setBlocks.addAll(drop(world, s, fill));
			}
			if (deleteOriginal) {
				setBlocks.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air));
			}
		}

		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : setBlocks) {
			t.add(u.set());
		}
		t.commit();
	}

	private List<UndoableSetBlock> drop(IWorld world, Selection s, boolean fill) {
		List<UndoableSetBlock> setBlocks = new ArrayList();
		BlockPos pos = s.getPos();
		for (int distance = 1;; distance++) {
			Block b = world.getBlock(pos.down(distance));
			if (b == Blocks.air) {
				if (world.getBlock(pos.down(distance + 1)) != Blocks.air) {
					setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.down(distance), s.getBlock()));
					break;
				} else {
					if (fill) {
						setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.down(distance), s.getBlock()));
					}
				}
			}
		}
		return setBlocks;
	}

	private List<UndoableSetBlock> raiseBurriedBlocks(IWorld world, Selection s) {
		List<UndoableSetBlock> setBlocks = new ArrayList();
		BlockPos pos = s.getPos();
		for (int distance = 1;; distance++) {
			Block b = world.getBlock(pos.up(distance));
			if (b == Blocks.air) {
				setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.up(distance), s.getBlock()));
				break;
			}
		}
		return setBlocks;
	}

	@Override
	public Object[] getRecipe() {
		return null;
	}
}
