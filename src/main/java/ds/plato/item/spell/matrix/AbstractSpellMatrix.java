package ds.plato.item.spell.matrix;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import ds.plato.api.IPick;
import ds.plato.api.IPlayer;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Spell;
import ds.plato.player.Player;
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

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		BlockPos playerPos = player.getPosition();
		int jump = 0;
		Iterable<Selection> selections = selectionManager.getSelections();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteInitialBlocks) {
				deletes.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air));
			}
			matrix.transform(p);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			//Move player up if is going to collide with new blocks
			int dx = pos.getX() - playerPos.getX();
			int dy = pos.getY() - playerPos.getY();
			int dz = pos.getZ() - playerPos.getZ();
			if (dx == 0 && dz == 0 && dy > 0) {
				if (dy > jump) {
					jump = dy;
				}
			}
			if (jump != 0) {
				System.out.println("jump=" + jump);
				player.moveEntity(0, jump+2, 0);
			}
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

		// IPlayer p = Player.getPlayer();
		// EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		// if (selectionManager.isSelected(p.playerLocation)) {
		// p.
		// }
	}

}
