package ds.plato.item.spell.other;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.select.Select;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellHoleDrain extends Spell {

	private Set<BlockPos> positions = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());
	private int positionsSize = 0;

	public SpellHoleDrain(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.bucket };
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		positions.clear();
		positionsSize = 0;
		BlockPos pos = pickManager.getPicks()[0].getPos();
		pickManager.clearPicks(world);

		while (true) {
			Block b = world.getBlock(pos.up());
			if (b == Blocks.air) {
				break;
			} else {
				pos = pos.up();
			}
		}

		positions.add(pos);
		drainWater(world);

		Transaction t = undoManager.newTransaction();
		for (BlockPos p : positions) {
			t.add(new UndoableSetBlock(world, selectionManager, p, Blocks.air.getDefaultState()).set());
		}
		t.commit();

		positions.clear();
	}

	private void drainWater(IWorld world) {

		for (BlockPos pos : positions) {
			for (BlockPos p : Select.horizontal) {
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
		drainWater(world);
	}
}
