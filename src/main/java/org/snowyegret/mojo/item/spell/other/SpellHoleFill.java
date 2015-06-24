package org.snowyegret.mojo.item.spell.other;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Sets;

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
		SelectionManager selectionManager = player.getSelectionManager();

		boolean isHorizontal = modifiers.isPressed(Modifier.CTRL);
		boolean useBlockInHotbar = modifiers.isPressed(Modifier.SHIFT);

		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections();
		player.getPickManager().clearPicks();
		
		Set<IUndoable> setBlocks = Sets.newHashSet();
		for (Selection s : selections) {
			for (BlockPos p : isHorizontal ? Select.HORIZONTAL : Select.BELOW_INCLUSIVE) {
				p = p.add(s.getPos());
				Block b = player.getWorld().getBlock(p);
				if (b == Blocks.air || b == Blocks.water) {
					IBlockState prevState = player.getWorld().getState(p);
					IBlockState state = useBlockInHotbar ? player.getHotbar().firstBlock() : s.getState();
					setBlocks.add(new UndoableSetBlock(p, prevState, state));
				}
			}
		}

		player.getTransactionManager().doTransaction(setBlocks);
	}
}
