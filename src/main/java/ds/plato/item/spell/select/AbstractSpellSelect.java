package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import ds.plato.block.BlockSelected;
import ds.plato.item.spell.ICondition;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public abstract class AbstractSpellSelect extends Spell {

	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;
	protected BlockPos[] positions;
	private List<ICondition> conditions = new ArrayList<>();

	public AbstractSpellSelect(BlockPos[] positions, IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
		this.positions = positions;
		// CTRL shrinks selection instead of grows
		// ALT (MENU) ignores pattern block
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	public void setConditions(ICondition... conditions) {
		this.conditions.clear();
		for (ICondition c : conditions) {
			this.conditions.add(c);
		};
	}

	@Override
	public void invoke(IWorld world, final HotbarSlot... hotbarSlots) {

		// Select the pick if there are no selections.
		// Either way the pickManager must be cleared.
		Pick p = pickManager.getPicks()[0];
		pickManager.clearPicks();
		if (selectionManager.size() == 0) {
			selectionManager.select(world, p.getPos());
		}

		// Shrink or grow selections
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			shrinkSelections(world);
		} else {
			Block patternBlock = selectionManager.firstSelection().getState().getBlock();
			growSelections(world, patternBlock);
		}
	}

	// Private-------------------------------------------------------------------------------

	private void growSelections(IWorld world, Block patternBlock) {
		List<BlockPos> newGrownSelections = new ArrayList();
		for (BlockPos center : selectionManager.getGrownSelections()) {
			for (BlockPos p : positions) {
				p = p.add(center);
				if (!test(world, p)) {
					continue;
				}
				//Block block = world.getBlock(p);
				Block block = world.getState(p).getBlock();
				if (!(block instanceof BlockAir) && !(block instanceof BlockSelected)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
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

	private void shrinkSelections(IWorld world) {
		List<Selection> shrunkSelections = new ArrayList<>();
		for (Selection s : selectionManager.getSelections()) {
			for (BlockPos p : positions) {
				p = p.add(s.getPos());
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
	
	private boolean test(IWorld world, BlockPos pos) {
		for(ICondition c : conditions) {
			if(!c.test(world, pos)) return false;
		}
		return true;
	}

}
