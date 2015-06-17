package ds.plato.item.spell.transform;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.IPlantable;

import com.google.common.collect.Lists;

import ds.plato.item.spell.Spell;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndoable;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform() {
		super(1);
	}

	protected void transformSelections(IPlayer player, ITransform<Selection> transformer) {
		ISelect selectionManager = player.getSelectionManager();
		if (selectionManager.getSelectionList().size() == 0) {
			return;
		}

		Jumper jumper = new Jumper(player);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player); 
		player.getPickManager().clearPicks(player);
		List<IUndoable> setBlocks = Lists.newArrayList();
		List<BlockPos> reselects = Lists.newArrayList();
		for (Selection s : selections) {
			for (Selection sel : transformer.transform(s)) {
				// s = transformer.transform(s);
				BlockPos pos = sel.getPos();
				IBlockState state = sel.getState();
				if (state.getBlock() instanceof IPlantable) {
					// TODO only plant if block beneath can sustain a plant
					// if (world.getBlockState(pos).getBlock()
					// .canSustainPlant(world.getWorld(), pos, EnumFacing.UP, (IPlantable) state.getBlock())) {
					pos = pos.up();
					// } else {
					// continue;
					// }
				}
				jumper.setHeight(pos);
				setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, pos, state));
				reselects.add(pos);
			}
		}

		jumper.jump();

		Transaction t = player.getUndoManager().newTransaction();
		t.addAll(setBlocks);
		t.commit();

		selectionManager.select(player, reselects);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
