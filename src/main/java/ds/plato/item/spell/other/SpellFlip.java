package ds.plato.item.spell.other;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import ds.geom.GeomUtil;
import ds.geom.matrix.RotationMatrix;
import ds.geom.matrix.ScaleMatrix;
import ds.geom.matrix.TranslationMatrix;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.player.HotbarSlot;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

//Replaces SpellMirror in package spell.matrix until ReflectionMatrix is fixed 

@Deprecated
public class SpellFlip extends Spell {

	public SpellFlip(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(3, undoManager, selectionManager, pickManager);
	}

	@Override
	public void invoke(IWorld world, HotbarSlot... slots) {
		boolean deleteOriginal = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		Pick[] picks = pickManager.getPicks();
		//Vec3 c = selectionManager.getCentroid();

		List<UndoableSetBlock> deletes = new ArrayList<>();
		List<UndoableSetBlock> adds = new ArrayList<>();
		List<BlockPos> addedPos = new ArrayList<>();

		//Point3d d = new Point3d(.5,.5,.5);
		Point3d p1 = picks[0].point3d();
		//p1.add(d);
		Point3d p2 = picks[1].point3d();
		//p2.add(d);
		Point3d p3 = picks[2].point3d();
		//p3.add(d);

		IPlayer player = Player.getPlayer();
		Iterable<Selection> selections = selectionManager.getSelections();
		selectionManager.clearSelections(world);
		pickManager.clearPicks();
		for (Selection s : selections) {
			Point3d p = s.point3d();
			if (deleteOriginal) {
				deletes.add(new UndoableSetBlock(world, selectionManager, s.getPos(), Blocks.air));
			}
			//TODO pass 5 matrices which are constructed outside the loop
			GeomUtil.reflect(p, p1, p2, p3, true);
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			// TODO Move management of jump height from IPlayer to AbstractSpellMatrix.
			player.incrementJumpHeight(pos);
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
		player.jump();

		// Select all transformed blocks
		for (BlockPos pos : addedPos) {
			selectionManager.select(world, pos);
		}
	}

}
