package org.snowyegret.mojo.item.spell.transform;

import java.util.List;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.player.Jumper;
import org.snowyegret.mojo.select.ISelect;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.IPlantable;

import com.google.common.collect.Lists;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform() {
		super(1);
	}

	protected void transformSelections(IPlayer player, ITransform<Selection> transformer) {
		ISelect selectionManager = player.getSelectionManager();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player); 
		player.getPickManager().clearPicks(player);
		
		List<IUndoable> setBlocks = Lists.newArrayList();
		for (Selection s : selections) {
			for (Selection ss : transformer.transform(s)) {
				BlockPos pos = ss.getPos();
				IBlockState state = ss.getState();
				//TODO move to Transaction.dO
				// if (state.getBlock() instanceof IPlantable) {
				// // TODO only plant if block beneath can sustain a plant
				// // if (world.getBlockState(pos).getBlock()
				// // .canSustainPlant(world.getWorld(), pos, EnumFacing.UP, (IPlantable) state.getBlock())) {
				// pos = pos.up();
				// // } else {
				// // continue;
				// // }
				// }
				setBlocks.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
			}
		}

		Transaction t = new Transaction();
		t.addAll(setBlocks);
		t.dO(player);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
