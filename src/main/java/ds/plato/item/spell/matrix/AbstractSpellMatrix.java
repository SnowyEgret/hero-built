package ds.plato.item.spell.matrix;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Spell;
import ds.plato.select.Selection;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks, IUndo undo, ISelect select, IPick pick) {
		super(numPicks, undo, select, pick);
	}

	protected void transformSelections(Matrix4d matrix, IWorld world, boolean deleteInitialBlocks) {

		List<UndoableSetBlock> deletes = new ArrayList<>();
		List<UndoableSetBlock> adds = new ArrayList<>();
		List<BlockPos> addedPos = new ArrayList<>();

		Iterable<Selection> selections = selectionManager.getSelections();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			adds.add(new UndoableSetBlock(world, selectionManager, pos, s.getBlock()));
			addedPos.add(pos);
		}

		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : deletes) {
			t.add(u.set());
		}
		for (UndoableSetBlock u : adds) {
			t.add(u.set());
		}
		t.commit();

		selectionManager.clearSelections(world);
		for (BlockPos pos : addedPos) {
			selectionManager.select(world, pos);
		}
	}

}
