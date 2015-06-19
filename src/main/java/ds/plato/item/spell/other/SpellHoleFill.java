package ds.plato.item.spell.other;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Sets;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Select;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndoable;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

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
		ISelect selectionManager = player.getSelectionManager();

		boolean isHorizontal = modifiers.isPressed(Modifier.CTRL);
		boolean useBlockInHotbar = modifiers.isPressed(Modifier.SHIFT);

		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);
		
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

		Transaction t = new Transaction();
		t.addAll(setBlocks);
		t.dO(player);
	}
}
