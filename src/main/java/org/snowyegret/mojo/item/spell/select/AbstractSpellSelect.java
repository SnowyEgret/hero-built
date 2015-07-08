package org.snowyegret.mojo.item.spell.select;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.item.spell.ICondition;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractSpellSelect extends Spell {

	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;

	public AbstractSpellSelect() {
		super(1);
		// this.growthPattern = growthPattern;
		// CTRL shrinks selection instead of grows
		// ALT ignores pattern block
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	public void select(Player player, BlockPos[] pattern, Iterable<ICondition> conditions) {

		Modifiers modifiers = player.getModifiers();
		boolean shrink = modifiers.isPressed(Modifier.CTRL);

		SelectionManager selectionManager = player.getSelectionManager();

		// Select the pick if there are no selections.
		// Either way the picks must be cleared.
		// FIXME Array out of bounds exception when picking with a selection spell after "Save and Quit to Title" but
		// not not "Quit Game" #194
		// Picks array is empty
		Pick firstPick = player.getPickManager().firstPick();
		player.clearPicks();
		if (selectionManager.size() == 0) {
			selectionManager.select(firstPick.getPos());
		}

		if (shrink) {
			shrinkSelections(player, pattern);
		} else {
			growSelections(player, pattern, conditions);
		}
	}

	// Private-------------------------------------------------------------------------------

	private void growSelections(Player player, BlockPos[] pattern, Iterable<ICondition> conditions) {

		SelectionManager selectionManager = player.getSelectionManager();
		Block patternBlock = selectionManager.firstSelection().getState().getBlock();
		Set<BlockPos> selections = Sets.newHashSet();
		// Grown selections must be on selectionManager and not on this spell so that it belongs to a player
		for (BlockPos center : selectionManager.getGrownSelections()) {
			for (BlockPos pos : pattern) {
				pos = pos.add(center);
				if (!applyConditions(conditions, player.getWorld(), pos, patternBlock)) {
					continue;
				}
				Block block = player.getWorld().getState(pos).getBlock();
				// Do not select blocks if they are air
				if (block instanceof BlockAir) {
					continue;
				}
				// Do not select blocks if they are already selected
				// Testing for BlockSelected and isSelected seems to perform equally
				if (block instanceof BlockSelected) {
					// System.out.println("Position already selected");
					// Select previously selected blocks if they have been left in world after a crash
					// TileEntity is saved to world and it not null.
					// If the prevState is null, reselect it.
					PrevStateTileEntity tileEntity = (PrevStateTileEntity) player.getWorld().getTileEntity(pos);
					if (tileEntity.getPrevState() != null) {
						// System.out.println("While growing selections, found a BlockSelected.");
						continue;
					} else {
						// TODO Why reselect it if state is null and when is state null?
						// If this never prints, delete this test
						System.out
								.println(">>>>>>>>>>>>>>>>>While growing selections, found a BlockSelected with null state.");
					}
					// continue;
				}

				// Set takes care of this
				// if (selectionManager.isSelected(p)) {
				// System.out.println("Position already selected but is not a BlockSelected");
				// continue;
				// }

				boolean anyBlock = player.getModifiers().isPressed(Modifier.ALT);
				if (anyBlock) {
					selections.add(pos);
				} else {
					if (block == patternBlock) {
						selections.add(pos);
					}
				}

			}
		}
		// System.out.println("grownSelections=" + grownSelections.size());
		selectionManager.select(selections);
		selectionManager.setGrownSelections(selections);
	}

	private void shrinkSelections(Player player, BlockPos[] pattern) {

		SelectionManager selectionManager = player.getSelectionManager();
		List<BlockPos> selections = Lists.newArrayList();
		for (Selection s : selectionManager.getSelections()) {
			for (BlockPos p : pattern) {
				p = p.add(s.getPos());
				Block b = player.getWorld().getBlock(p);
				if (!(b instanceof BlockSelected)) {
					selections.add(s.getPos());
					break;
				}
			}
		}
		selectionManager.deselect(selections);
		selectionManager.clearGrownSelections();
	}

	private boolean applyConditions(Iterable<ICondition> conditions, IWorld world, BlockPos pos, Block patternBlock) {
		if (conditions == null) {
			return true;
		}
		for (ICondition c : conditions) {
			if (!c.test(world, pos, patternBlock)) {
				return false;
			}
		}
		return true;
	}

}
