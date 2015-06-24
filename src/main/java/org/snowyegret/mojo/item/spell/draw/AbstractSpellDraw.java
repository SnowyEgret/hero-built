package org.snowyegret.mojo.item.spell.draw;

import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.IUndoable;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;

import com.google.common.collect.Lists;

import ds.geom.IDrawable;
import ds.geom.VoxelSet;
import ds.geom.solid.Solid;

public abstract class AbstractSpellDraw extends Spell {

	public AbstractSpellDraw(int numPicks) {
		super(numPicks);
		info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

	protected void draw(IDrawable drawable, IPlayer player, EnumFacing side) {
		
		Modifiers modifiers = player.getModifiers();
		SelectionManager selectionManager = player.getSelectionManager();

		selectionManager.clearSelections();
		player.getPickManager().clearPicks();

		boolean isHollow = modifiers.isPressed(Modifier.SHIFT);
		boolean onSurface = modifiers.isPressed(Modifier.ALT);

		VoxelSet voxels = drawable.voxelize();

		if (drawable instanceof Solid && isHollow) {
			voxels = voxels.shell();
		}

		List<IUndoable> setBlocks = Lists.newArrayList();
		IBlockState state = player.getHotbar().firstBlock();
		for (Point3i p : voxels) {
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			// TODO conditional on plane and side of plane the player is positioned at.
			if (onSurface) {
				pos = pos.add(side.getDirectionVec());
			}
			setBlocks.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
		}
		
		player.doTransaction(setBlocks);

//		Transaction t = new Transaction();
//		t.addAll(setBlocks);
//		t.dO(player);
	}

}
