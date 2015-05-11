package ds.plato.item.spell.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Spell;
import ds.plato.player.HotbarSlot;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.undo.Transaction;

public class SpellMengerSponge extends Spell {

	int level = 0;
	List<BlockPos> pointsToDelete = new ArrayList<>();

	public SpellMengerSponge(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(1, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot...slotEntries) {
		//TODO use enclosing cube
		recursivelySubtract(selectionManager.voxelSet());
		System.out.println("[SpellMengerSponge.invoke] pointsToDelete=" + pointsToDelete);
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		Transaction t = undoManager.newTransaction();
		for (BlockPos v : pointsToDelete) {
			t.add(new UndoableSetBlock(world, selectionManager, v, Blocks.air).set());
		}
		t.commit();
	}

	private void recursivelySubtract(VoxelSet voxels) {
		
		// Run through this set testing for containment in each sub domain. Depending on which domain it is contained
		// by, add it to the set of points to be set to air. Recurse on each sub voxel set.
		level++;
		System.out.println("[SpellMengerSponge.recursivelySubtract] level=" + level);
		Iterable<IntegerDomain> domains = voxels.divideDomain(3);
		System.out.println("[SpellMengerSponge.recursivelySubtract] domains=" + domains);
		List<Integer> domainsToDelete = Arrays.asList(4, 10, 12, 13, 14, 16, 22);
		for (Point3i p : voxels) {
			IntegerDomain domain = null;
			int i = 0;
			for (IntegerDomain d : domains) {
				if (d.contains(p)) {
					domain = d;
					domain.count = i;
					break;
				}
				i++;
			}
			if (domainsToDelete.contains(domain.count)) {
				pointsToDelete.add(new BlockPos(p.x, p.y, p.z));
			}
		}
		
		for (VoxelSet set : voxels.divide(3)) {
			if (set.size() >= 9) {
				recursivelySubtract(set);
			}
		}
		
		level--;
		System.out.println("[SpellMengerSponge.recursivelySubtract] level=" + level);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
