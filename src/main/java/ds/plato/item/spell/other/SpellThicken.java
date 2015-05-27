package ds.plato.item.spell.other;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import ds.geom.IntegerDomain;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.select.Select;
import ds.plato.item.spell.transform.AbstractSpellTransform;
import ds.plato.pick.IPick;
import ds.plato.player.HotbarSlot;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellThicken extends AbstractSpellTransform {

	public SpellThicken(IUndo undo, ISelect select, IPick pick) {
		super(undo, select, pick);
		// ctrl-inward, shift-outward, alt-within plane
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(final IWorld world, HotbarSlot... slots) {
		Set<BlockPos> positions = new HashSet<>();
		Selection first = selectionManager.firstSelection();
		IntegerDomain domain = selectionManager.getDomain();
		if (domain.isPlanar()) {
			thickenPlane(positions, domain, world);
		} else {
			thicken(positions, world);
		}

		selectionManager.clearSelections(world);
		pickManager.clearPicks();

		Transaction t = undoManager.newTransaction();
		for (BlockPos p : positions) {
			t.add(new UndoableSetBlock(world, selectionManager, p, first.getBlock()).set());
		}
		t.commit();
	}

	private void thicken(Set<BlockPos> positions, IWorld world) {
		boolean in = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		boolean out = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		//TODO
		//boolean noAir = Modifiers.isPressed(Modifier.RSHIFT);
		boolean noAir = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		final Vec3 c = selectionManager.getCentroid();
		for (Selection s : selectionManager.getSelections()) {
			double d = s.getPos().distanceSqToCenter(c.xCoord, c.yCoord, c.zCoord);
			for (BlockPos p : Select.all) {
				p = p.add(s.getPos());
				//Throw out this position if we don't want air and it is air
				if (noAir) {
					Block b = world.getBlock(p);
					if (b instanceof BlockAir) {
						continue;
					}
				}
				double dd = p.distanceSqToCenter(c.xCoord, c.yCoord, c.zCoord);
				if ((in && dd < d) || (out && dd > d) || (!in && !out)) {
					if (!selectionManager.isSelected(p)) {
						positions.add(p);
					}
				}
			}
		}
	}

	private void thickenPlane(Set<BlockPos> points, IntegerDomain domain, IWorld world) {
		boolean withinPlane = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
		BlockPos[] select = null;
		switch (domain.getPlane()) {
		case XY:
			select = withinPlane ? Select.XY : Select.Z;
			break;
		case XZ:
			select = withinPlane ? Select.XZ : Select.Y;
			break;
		case YZ:
			select = withinPlane ? Select.YZ : Select.X;
			break;
		}
		for (Selection s : selectionManager.getSelections()) {
			for (BlockPos p : select) {
				p = p.add(s.getPos());
				if (!selectionManager.isSelected(p)) {
					points.add(p);
				}
			}
		}
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

}
