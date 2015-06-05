package ds.plato.item.spell.transform;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.IPlantable;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform(IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
	}

	protected void transformSelections(IWorld world, IPlayer player, ITransform transformer) {
		// System.out.println("world="+world);
		if (selectionManager.getSelectionList().size() != 0) {
			Transaction t = undoManager.newTransaction();
			Iterable<Selection> selections = selectionManager.getSelections();
			//selectionManager.clearSelections(world);
			for (Selection s : selections) {
				s = transformer.transform(s);
				BlockPos pos = s.getPos();
				IBlockState state = s.getState();
				if (state.getBlock() instanceof IPlantable) {
					//TODO only plant if block beneath can sustain a plant
					//if (world.getBlockState(pos).getBlock()
					//		.canSustainPlant(world.getWorld(), pos, EnumFacing.UP, (IPlantable) state.getBlock())) {
						pos = pos.up();
					//} else {
					//	continue;
					//}
				}
				t.add(new UndoableSetBlock(world, selectionManager, pos, state).set());
			}
			t.commit();
		}
		pickManager.clearPicks(world);
		//TODO why is this happening?
		// Clear the selections because BlockSelected is still rendering with old state
		// Player can reselect last
		selectionManager.clearSelections(world);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
