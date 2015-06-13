package ds.plato.item.spell.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellMengerSponge extends Spell {

	int level = 0;
	List<BlockPos> pointsToDelete = new ArrayList<>();

	public SpellMengerSponge() {
		super(1);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		//TODO use enclosing cube
		recursivelySubtract(selectionManager.voxelSet());
		System.out.println("pointsToDelete=" + pointsToDelete);
		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
		Transaction t = undoManager.newTransaction();
		for (BlockPos v : pointsToDelete) {
			t.add(new UndoableSetBlock(world, selectionManager, v, Blocks.air.getDefaultState()).set());
		}
		t.commit();
	}

	private void recursivelySubtract(VoxelSet voxels) {
		
		// Run through this set testing for containment in each sub domain. Depending on which domain it is contained
		// by, add it to the set of points to be set to air. Recurse on each sub voxel set.
		level++;
		System.out.println("level=" + level);
		Iterable<IntegerDomain> domains = voxels.divideDomain(3);
		System.out.println("domains=" + domains);
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
		System.out.println("level=" + level);
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
