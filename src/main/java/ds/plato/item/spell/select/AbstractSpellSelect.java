package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.block.BlockSelected;
import ds.plato.core.HotbarSlot;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.Pick;
import ds.plato.select.Selection;

public abstract class AbstractSpellSelect extends Spell {

	protected Shell.Type shellType;
	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;

	public AbstractSpellSelect(Shell.Type type, IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
		this.shellType = type;
		//CTRL shrinks selection instead of grows
		//ALT (MENU) ignores pattern block
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot...hotbarSlots) {
		
		// Select the pick if there are no selections
		//Any way the pickManager must be cleared
		Pick p = pickManager.getPicks()[0];
		pickManager.clearPicks();
		if (selectionManager.size() == 0) {
			selectionManager.select(world, p.getPos());
		}
		
		// Shrink or grow selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			shrinkSelections(shellType, world);
		} else {
			Block patternBlock = selectionManager.firstSelection().getBlock();
			growSelections(shellType, world, patternBlock);
		}
	}

	protected void growSelections(Shell.Type shellType, IWorld world, Block patternBlock) {
		List<BlockPos> newGrownSelections = new ArrayList();
		for (BlockPos center : selectionManager.getGrownSelections()) {
			Shell shell = new Shell(shellType, center, world);
			for (BlockPos p : shell) {
				Block block = world.getBlock(p);
				if (!(block instanceof BlockAir) && !(block instanceof BlockSelected)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) { // Alt
						selectionManager.select(world, p);
						newGrownSelections.add(p);
					} else {
						if (block == patternBlock) {
							selectionManager.select(world, p);
							newGrownSelections.add(p);
						}
					}
				}
			}
		}
		selectionManager.setGrownSelections(newGrownSelections);
	}

	protected void shrinkSelections(Shell.Type shellType, IWorld world) {
		List<Selection> shrunkSelections = new ArrayList<>();
		for (Selection s : selectionManager.getSelections()) {
			Shell shell = new Shell(shellType, s.getPos(), world);
			for (BlockPos p : shell) {
				Block b = world.getBlock(p);
				if (!(b instanceof BlockSelected)) {
					shrunkSelections.add(s);
					break;
				}

			}
		}
		for (Selection s : shrunkSelections) {
			selectionManager.deselect(world, s);
		}
		selectionManager.clearGrownSelections();
	}
}
