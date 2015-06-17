package ds.plato.item.spell.matrix;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndoable;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks) {
		super(numPicks);
	}

	protected void transformSelections(IPlayer player, Matrix4d matrix, boolean deleteInitialBlocks) {

		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();

		List<IUndoable> deletes = Lists.newArrayList();
		List<IUndoable> setBlocks = Lists.newArrayList();
		List<BlockPos> reselects = Lists.newArrayList();

		Jumper jumper = new Jumper(player);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);
		IBlockState air = Blocks.air.getDefaultState();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(player.getWorld(), s.getPos(), air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			jumper.setHeight(pos);
			setBlocks.add(new UndoableSetBlock(player.getWorld(), pos, s.getState()));
			reselects.add(pos);
		}

		jumper.jump();

		Transaction t = player.getUndoManager().newTransaction();
		t.addAll(deletes);
		t.addAll(setBlocks);
		t.commit();

		selectionManager.select(player, reselects);
	}

}
