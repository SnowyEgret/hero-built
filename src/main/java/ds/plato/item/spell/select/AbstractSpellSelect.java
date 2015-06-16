package ds.plato.item.spell.select;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import ds.plato.block.BlockSelected;
import ds.plato.item.spell.ICondition;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public abstract class AbstractSpellSelect extends Spell {

	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;
	protected BlockPos[] positions;
	private List<ICondition> conditions = new ArrayList<>();

	public AbstractSpellSelect(BlockPos[] positions) {
		super(1);
		this.positions = positions;
		// CTRL shrinks selection instead of grows
		// ALT ignores pattern block
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	public void setConditions(ICondition... conditions) {
		this.conditions.clear();
		for (ICondition c : conditions) {
			this.conditions.add(c);
		};
	}

	@Override
	public void invoke(IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		// Select the pick if there are no selections.
		// Either way the pickManager must be cleared.
		Pick p = pickManager.getPicks()[0];
		pickManager.clearPicks(player);
		if (selectionManager.size() == 0) {
			selectionManager.select(player, p.getPos());
		}

		// Shrink or grow selections
		if (modifiers.isPressed(Modifier.CTRL)) {
			shrinkSelections(player, selectionManager);
		} else {
			Block patternBlock = selectionManager.firstSelection().getState().getBlock();
			growSelections(player, modifiers, selectionManager, patternBlock);
		}
	}

	// Private-------------------------------------------------------------------------------

	private void growSelections(IPlayer player, Modifiers modifiers, ISelect selectionManager, Block patternBlock) {
		List<BlockPos> newGrownSelections = new ArrayList();
		for (BlockPos center : selectionManager.getGrownSelections()) {
			for (BlockPos p : positions) {
				p = p.add(center);
				if (!test(player.getWorld(), p)) {
					continue;
				}
				Block block = player.getWorld().getState(p).getBlock();
				if (!(block instanceof BlockAir) && !(block instanceof BlockSelected)) {
					if (modifiers.isPressed(Modifier.SHIFT)) {
						newGrownSelections.add(p);
					} else {
						if (block == patternBlock) {
							newGrownSelections.add(p);
						}
					}
				}
			}
		}
		selectionManager.select(player, newGrownSelections);
		selectionManager.setGrownSelections(newGrownSelections);
	}

	private void shrinkSelections(IPlayer player, ISelect selectionManager) {
		List<BlockPos> shrunkSelections = new ArrayList<>();
		for (Selection s : selectionManager.getSelections()) {
			for (BlockPos p : positions) {
				p = p.add(s.getPos());
				Block b = player.getWorld().getBlock(p);
				if (!(b instanceof BlockSelected)) {
					shrunkSelections.add(s.getPos());
					break;
				}
			}
		}
		selectionManager.deselect(player, shrunkSelections);
		selectionManager.clearGrownSelections();
	}
	
	private boolean test(IWorld world, BlockPos pos) {
		for(ICondition c : conditions) {
			if(!c.test(world, pos)) return false;
		}
		return true;
	}

}
