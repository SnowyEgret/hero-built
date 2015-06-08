package ds.plato.item.spell.matrix;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.Plato;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public abstract class AbstractSpellMatrix extends Spell {

	public AbstractSpellMatrix(int numPicks, IUndo undo, ISelect select, IPick pick) {
		super(numPicks, undo, select, pick);
	}

	protected void transformSelections(IWorld world, IPlayer player, Matrix4d matrix, boolean deleteInitialBlocks) {

		List<UndoableSetBlock> deletes = new ArrayList<>();
		List<UndoableSetBlock> setBlocks = new ArrayList<>();
		List<BlockPos> reselects = new ArrayList<>();

		Jumper jumper = new Jumper(player);
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks(world);
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air.getDefaultState()));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			jumper.setHeight(pos);
			setBlocks.add(new UndoableSetBlock(world, selectionManager, pos, s.getState()));
			reselects.add(pos);
		}

		jumper.jump();

		Transaction t = undoManager.newTransaction();
		for (UndoableSetBlock u : deletes) {
			t.add(u.set());
		}
		for (UndoableSetBlock u : setBlocks) {
			t.add(u.set());
		}
		t.commit();

		// Select all transformed blocks but only when not messaging between client and server
		// Fix for MultiPlayer: State incorrect when reselecting after applying a draw or matrix spell #93
		if (Minecraft.getMinecraft().isSingleplayer() && !Plato.forceMessaging) {
			for (BlockPos pos : reselects) {
				// FIXME in MP select is rejecting these even though clearSelections
				// should have removed BlockSelected from world.
				// Same problem as when first corner of a region was left unselected
				selectionManager.select(world, pos);
			}
		} else {
			selectionManager.setReselects(reselects);
		}
	}

}
