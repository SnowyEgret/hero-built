package org.snowyegret.plato.item.spell.other;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;
import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.item.spell.Spell;
import org.snowyegret.plato.item.spell.select.Select;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.select.Selection;
import org.snowyegret.plato.undo.Transaction;
import org.snowyegret.plato.undo.UndoableSetBlock;
import org.snowyegret.plato.world.IWorld;

import ds.geom.IntegerDomain;

public class SpellThicken extends Spell {

	public SpellThicken() {
		super(1);
		// ctrl-inward, shift-outward, alt-within plane
		info.addModifiers(Modifier.CTRL, Modifier.SHIFT, Modifier.ALT);
	}

	@Override
	public void invoke(IPlayer player) {
		
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		
		Set<BlockPos> positions = new HashSet<>();
		IBlockState firstSelection = selectionManager.firstSelection().getState();
		IntegerDomain domain = selectionManager.getDomain();
		if (domain.isPlanar()) {
			thickenPlane(positions, modifiers, selectionManager, domain, player.getWorld());
		} else {
			thicken(positions, modifiers, selectionManager, player.getWorld());
		}

		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);

		Transaction t = new Transaction();
		for (BlockPos p : positions) {
			t.add(new UndoableSetBlock(p, player.getWorld().getState(p), firstSelection));
		}
		t.dO(player);
		
	}

	private void thicken(Set<BlockPos> positions, Modifiers modifiers, ISelect selectionManager, IWorld world) {
		
		boolean in = modifiers.isPressed(Modifier.CTRL);
		boolean out = modifiers.isPressed(Modifier.SHIFT);
		boolean noAir = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		
		final Vec3 c = selectionManager.getCentroid();
		for (Selection s : selectionManager.getSelections()) {
			double d = s.getPos().distanceSqToCenter(c.xCoord, c.yCoord, c.zCoord);
			for (BlockPos p : Select.ALL) {
				p = p.add(s.getPos());
				//Throw out this position if we don't want air and it is air
				if (noAir) {
					Block b = world.getBlock(p);
					//So as not to select plants when thickening a trail
					if (b instanceof BlockAir || !b.isNormalCube()) {
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

	private void thickenPlane(Set<BlockPos> points, Modifiers modifiers, ISelect selectionManager, IntegerDomain domain, IWorld world) {
		boolean withinPlane = modifiers.isPressed(Modifier.SHIFT);
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
