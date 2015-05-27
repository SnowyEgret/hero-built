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
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellHoleDrain extends Spell {

	//private Set<BlockPos> positions = new ConcurrentHashSet<>();
	private Set<BlockPos> positions = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos, Boolean>());
	private int numBlocksDrained = 0;
	private int maxBlocksDrained = 9999;
	private int positionsSize = 0;

	public SpellHoleDrain(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BTB", "   ", 'T', Items.ghast_tear, 'B', Items.bucket };
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slotEntries) {
		positions.clear();
		numBlocksDrained = 0;
		positionsSize = 0;
		BlockPos pos = pickManager.getPicks()[0].getPos();
		pickManager.clearPicks();

		// Pick is some block under water. Find the position of the top water block
		// int y = pos.getY();
		// while (true) {
		// y++;
		// Block b = world.getBlock(new BlockPos(pos.getX(), y, pos.getZ()));
		// if (b == Blocks.air) {
		// y--;
		// break;
		// }
		// }

		while (true) {
			Block b = world.getBlock(pos);
			if (b == Blocks.air) {
				break;
			}
			pos = pos.up();
		}

		// positions.add(new BlockPos(pos.getX(), y, pos.getZ()));
		positions.add(pos);
		recursivelyDrainWater(world);

		Transaction t = undoManager.newTransaction();
		for (BlockPos p : positions) {
			t.add(new UndoableSetBlock(world, selectionManager, p, Blocks.air).set());
		}
		t.commit();

		positions.clear();

	}

	private void recursivelyDrainWater(IWorld world) {

		if (positions.size() == positionsSize || numBlocksDrained > maxBlocksDrained) {
			return;
		}
		
		for (BlockPos pos : positions) {
			for (BlockPos p : Select.horizontal) {
				p = p.add(pos);
				Block b = world.getBlock(p);
				if (b == Blocks.water) {
					numBlocksDrained++;
					positions.add(p);
				}
			}
		}
		positionsSize = positions.size();

		// // To avoid concurrent modification
		// for (BlockPos center : Lists.newArrayList(positions)) {
		// Shell shell = new Shell(Shell.Type.HORIZONTAL, center, world);
		// for (BlockPos p : shell) {
		// Block b = world.getBlock(p);
		// if (b == Blocks.water) {
		// numBlocksDrained++;
		// positions.add(p);
		// }
		// }
		// }
		//
		// if (positions.size() > lastPointsSize && numBlocksDrained < maxBlocksDrained) {
		// lastPointsSize = positions.size();
		// recursivelyDrainWater(world);
		// }
	}

}
