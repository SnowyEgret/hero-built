package org.snowyegret.mojo.item.spell.transform;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class SpellHoleDrain extends AbstractSpellTransform {

	@Override
	public void invoke(final IPlayer player) {

		transformSelections(player, new ITransform() {

			@Override
			public Iterable<Selection> transform(Selection selection) {
				Set<BlockPos> positions = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());
				int positionsSize = 0;
				BlockPos pos = player.getPickManager().firstPick().getPos();
				player.getPickManager().clearPicks();
				IWorld world = player.getWorld();

				while (true) {
					Block b = world.getBlock(selection.getPos().up());
					if (b == Blocks.air) {
						break;
					} else {
						pos = pos.up();
					}
				}
				positions.add(pos);
				drainWater(world, positions, positionsSize);

				List<Selection> selections = Lists.newArrayList();
				for (BlockPos p : positions) {
					selections.add(new Selection(p, selection.getState()));
				}
				return selections;
			}

			private void drainWater(IWorld world, Set<BlockPos> positions, int positionsSize) {

				for (BlockPos pos : positions) {
					for (BlockPos p : Select.HORIZONTAL) {
						p = p.add(pos);
						Block b = world.getBlock(p);
						if (b == Blocks.water) {
							positions.add(p);
						}
					}
				}

				if (!(positions.size() > positionsSize)) {
					System.out.println("No more new water found. positions.size=" + positions.size());
					return;
				}
				if (positions.size() > Transaction.MAX_SIZE) {
					System.out.println("Transaction too large");
					return;
				}

				positionsSize = positions.size();
				drainWater(world, positions, positionsSize);
			}

		});

	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.bucket };
	}
}
// public class SpellHoleDrain extends Spell {
//
// private Set<BlockPos> positions = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());
// private int positionsSize = 0;
//
// public SpellHoleDrain() {
// super(1);
// }
//
// @Override
// public Object[] getRecipe() {
// return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.bucket };
// }
//
// @Override
// public void invoke(IPlayer player) {
// Modifiers modifiers = player.getModifiers();
// ISelect selectionManager = player.getSelectionManager();
// IPick pickManager = player.getPickManager();
// IUndo undoManager = player.getUndoManager();
//
// positions.clear();
// positionsSize = 0;
// BlockPos pos = pickManager.firstPick().getPos();
// pickManager.clearPicks(player);
//
// while (true) {
// Block b = player.getWorld().getBlock(pos.up());
// if (b == Blocks.air) {
// break;
// } else {
// pos = pos.up();
// }
// }
//
// positions.add(pos);
// drainWater(player.getWorld());
//
// Transaction t = undoManager.newTransaction();
// for (BlockPos p : positions) {
// t.add(new UndoableSetBlock(player.getWorld(), selectionManager, p, Blocks.air.getDefaultState()).set());
// }
// t.commit();
//
// positions.clear();
// }
//
// private void drainWater(IWorld world) {
//
// for (BlockPos pos : positions) {
// for (BlockPos p : Select.HORIZONTAL) {
// p = p.add(pos);
// Block b = world.getBlock(p);
// if (b == Blocks.water) {
// positions.add(p);
// }
// }
// }
//
// if (!(positions.size() > positionsSize)) {
// System.out.println("No more new water found. positions.size=" + positions.size());
// return;
// }
// if (positions.size() > Transaction.MAX_SIZE) {
// System.out.println("Transaction too large");
// return;
// }
//
// positionsSize = positions.size();
// drainWater(world);
// }
// }
