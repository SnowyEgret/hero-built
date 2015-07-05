package org.snowyegret.mojo.item.spell.transform;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class SpellDrop extends AbstractSpellTransform {

	public SpellDrop() {
		info.addModifiers(Modifier.CTRL, Modifier.ALT, Modifier.SHIFT);
	}

	@Override
	public void invoke(final Player player) {

		Modifiers modifiers = player.getModifiers();
		final boolean fill = modifiers.isPressed(Modifier.SHIFT);
		final boolean raise = modifiers.isPressed(Modifier.ALT);
		final boolean deleteOriginal = modifiers.isPressed(Modifier.CTRL) || raise;
		
		final SelectionManager selectionManager = player.getSelectionManager();
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

			private List<Selection> drop(IWorld world, SelectionManager selectionManager, Selection s, boolean fill) {
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

			private List<Selection> raiseBurriedBlocks(IWorld world, SelectionManager selectionManager, Selection s) {
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