package org.snowyegret.plato.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.select.Selection;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

public class SpellDrop extends AbstractSpellTransform {

	public SpellDrop() {
		info.addModifiers(Modifier.CTRL, Modifier.ALT, Modifier.SHIFT);
	}

	@Override
	public void invoke(final IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		final ISelect selectionManager = player.getSelectionManager();
		final boolean fill = modifiers.isPressed(Modifier.SHIFT);
		final boolean raise = modifiers.isPressed(Modifier.ALT);
		final boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL) || raise;
		final IBlockState air = Blocks.air.getDefaultState();

		transformSelections(player, new ITransform() {
			@Override
			public Iterable<Selection> transform(Selection s) {
				List<Selection> selections = Lists.newArrayList();
				if (raise) {
					selections.addAll(raiseBurriedBlocks(player.getWorld(), selectionManager, s));
				} else {
					selections.addAll(drop(player.getWorld(), selectionManager, s, fill));
				}
				if (deleteOriginal) {
					selections.add(new Selection(s.getPos(), air));
				}
				// Create a copy here because we don't want to modify the selectionManager's selection list.
				// If we don't create a copy undo doesn't work
				return selections;
			}

			private List<Selection> drop(IWorld world, ISelect selectionManager, Selection s, boolean fill) {
				System.out.println("fill=" + fill);
				List<Selection> selections = new ArrayList();
				BlockPos pos = s.getPos();
				for (int distance = 1;; distance++) {
					Block b = world.getBlock(pos.down(distance));
					if (b == Blocks.air) {
						if (world.getBlock(pos.down(distance + 1)) != Blocks.air) {
							selections.add(new Selection(pos.down(distance), s.getState()));
							break;
						} else {
							if (fill) {
								selections.add(new Selection(pos.down(distance), s.getState()));
							}
						}
					}
				}
				return selections;
			}

			private List<Selection> raiseBurriedBlocks(IWorld world, ISelect selectionManager, Selection s) {
				List<Selection> selections = new ArrayList();
				BlockPos pos = s.getPos();
				for (int distance = 1;; distance++) {
					Block b = world.getBlock(pos.up(distance));
					if (b == Blocks.air) {
						selections.add(new Selection(pos.up(distance), s.getState()));
						break;
					}
				}
				return selections;
			}
		});
	}
}

// extends Spell {
//
// public SpellDrop() {
// super(1);
// info.addModifiers(Modifier.CTRL, Modifier.ALT, Modifier.SHIFT);
// }
//
// @Override
// public void invoke(IPlayer player) {
//
// Modifiers modifiers = player.getModifiers();
// ISelect selectionManager = player.getSelectionManager();
// IPick pickManager = player.getPickManager();
// IUndo undoManager = player.getUndoManager();
//
// boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL);
// boolean fill = modifiers.isPressed(Modifier.SHIFT);
// // TODO check for all air or all non-air around block
// boolean raise = modifiers.isPressed(Modifier.SHIFT);
// if (raise) {
// deleteOriginal = true;
// }
//
// List<UndoableSetBlock> setBlocks = new ArrayList();
// Iterable<Selection> selections = selectionManager.getSelections();
// selectionManager.clearSelections(player);
// pickManager.clearPicks(player);
// for (Selection s : selections) {
// if (raise) {
// setBlocks.addAll(raiseBurriedBlocks(player.getWorld(), selectionManager, s));
// } else {
// setBlocks.addAll(drop(player.getWorld(), selectionManager, s, fill));
// }
// if (deleteOriginal) {
// setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, s.getPos(), Blocks.air.getDefaultState()));
// }
// }
//
// Transaction t = undoManager.newTransaction();
// for (UndoableSetBlock u : setBlocks) {
// t.add(u.set());
// }
// t.commit();
// }
//
// private List<UndoableSetBlock> drop(IWorld world, ISelect selectionManager, Selection s, boolean fill) {
// List<UndoableSetBlock> setBlocks = new ArrayList();
// BlockPos pos = s.getPos();
// for (int distance = 1;; distance++) {
// Block b = world.getBlock(pos.down(distance));
// if (b == Blocks.air) {
// if (world.getBlock(pos.down(distance + 1)) != Blocks.air) {
// setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.down(distance), s.getState()));
// break;
// } else {
// if (fill) {
// setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.down(distance), s.getState()));
// }
// }
// }
// }
// return setBlocks;
// }
//
// private List<UndoableSetBlock> raiseBurriedBlocks(IWorld world, ISelect selectionManager, Selection s) {
// List<UndoableSetBlock> setBlocks = new ArrayList();
// BlockPos pos = s.getPos();
// for (int distance = 1;; distance++) {
// Block b = world.getBlock(pos.up(distance));
// if (b == Blocks.air) {
// setBlocks.add(new UndoableSetBlock(world, selectionManager, pos.up(distance), s.getState()));
// break;
// }
// }
// return setBlocks;
// }
//
// @Override
// public Object[] getRecipe() {
// return null;
// }
//
// }
