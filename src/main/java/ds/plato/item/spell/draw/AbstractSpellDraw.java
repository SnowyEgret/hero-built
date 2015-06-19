package ds.plato.item.spell.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3i;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ds.geom.IDrawable;
import ds.geom.VoxelSet;
import ds.geom.solid.Solid;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.IUndoable;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;

public abstract class AbstractSpellDraw extends Spell {

	public AbstractSpellDraw(int numPicks) {
		super(numPicks);
		info.addModifiers(Modifier.SHIFT, Modifier.ALT);
	}

	protected void draw(IDrawable drawable, IPlayer player) {

		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();

		selectionManager.clearSelections(player);
		player.getPickManager().clearPicks(player);

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
			if (onSurface) {
				pos = pos.up();
			}
			setBlocks.add(new UndoableSetBlock(pos, player.getWorld().getState(pos), state));
		}

		Transaction t = new Transaction();
		t.addAll(setBlocks);
		t.dO(player);
	}

}
