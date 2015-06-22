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
import org.snowyegret.mojo.pick.IPick;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractSpellSelect extends Spell {

	protected Item ingredientA = Items.feather;
	protected Item ingredientB = Items.coal;
	private BlockPos[] growthPattern;
	private List<ICondition> conditions = Lists.newArrayList();

	public AbstractSpellSelect(BlockPos[] growthPattern) {
		super(1);
		this.growthPattern = growthPattern;
		// CTRL shrinks selection instead of grows
		// ALT ignores pattern block
		info.addModifiers(Modifier.CTRL, Modifier.ALT);
	}

	public BlockPos[] getGrowthPattern() {
		return growthPattern;
	}

	public void setGrowthPattern(BlockPos[] growthPattern) {
		this.growthPattern = growthPattern;
	}

	public void setConditions(ICondition... conditions) {
		this.conditions.clear();
		for (ICondition c : conditions) {
			this.conditions.add(c);
		}
	}

	@Override
	public void invoke(IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();

		// Select the pick if there are no selections.
		// Either way the pickManager must be cleared.
		Pick firstPick = pickManager.firstPick();
		pickManager.clearPicks(player);
		if (selectionManager.size() == 0) {
			selectionManager.select(player, firstPick.getPos());
		}

		boolean shrink = modifiers.isPressed(Modifier.CTRL);
		boolean anyBlock = modifiers.isPressed(Modifier.ALT);

		if (shrink) {
			shrinkSelections(player, selectionManager);
		} else {
			Block patternBlock = selectionManager.firstSelection().getState().getBlock();
			growSelections(player, anyBlock, selectionManager, patternBlock);
		}
	}

	// Private-------------------------------------------------------------------------------

	private void growSelections(IPlayer player, boolean anyBlock, ISelect selectionManager, Block patternBlock) {
		Set<BlockPos> grownSelections = Sets.newHashSet();
		for (BlockPos center : selectionManager.getGrownSelections()) {
			for (BlockPos p : growthPattern) {
				p = p.add(center);
				if (!applyConditions(player.getWorld(), p)) {
					continue;
				}
				Block block = player.getWorld().getState(p).getBlock();
				if (block instanceof BlockAir) {
					continue;
				}
				// Both of these tests perform equally
				if (block instanceof BlockSelected) {
					// if (selectionManager.isSelected(p)) {
					// System.out.println("Position already selected");

					// We only select previously selected blocks if they have been left in world after a crash
					PrevStateTileEntity tileEntity = (PrevStateTileEntity) player.getWorld().getTileEntity(p);
					if (tileEntity.getPrevState() != null) {
						continue;
					}
				}
				if (anyBlock) {
					grownSelections.add(p);
				} else {
					if (block == patternBlock) {
						grownSelections.add(p);
					}
				}

			}
		}
		selectionManager.select(player, grownSelections);
		selectionManager.setGrownSelections(grownSelections);
	}

	private void shrinkSelections(IPlayer player, ISelect selectionManager) {
		List<BlockPos> shrunkSelections = Lists.newArrayList();
		for (Selection s : selectionManager.getSelections()) {
			for (BlockPos p : growthPattern) {
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

	private boolean applyConditions(IWorld world, BlockPos pos) {
		for (ICondition c : conditions) {
			if (!c.test(world, pos)) {
				return false;
			}
		}
		return true;
	}

}
