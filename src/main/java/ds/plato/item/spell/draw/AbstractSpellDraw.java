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
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		selectionManager.clearSelections(player);
		pickManager.clearPicks(player);

		boolean isHollow = modifiers.isPressed(Modifier.SHIFT);
		boolean onSurface = modifiers.isPressed(Modifier.ALT);

		VoxelSet voxels = drawable.voxelize();

		if (drawable instanceof Solid && isHollow) {
			voxels = voxels.shell();
		}

		Jumper jumper = new Jumper(player);

		//Set<IUndoable> setBlocks = Sets.newHashSet();
		List<IUndoable> setBlocks = Lists.newArrayList();
		List<BlockPos> reselects = new ArrayList<>();
		IBlockState state = player.getHotbar().firstBlock();
		for (Point3i p : voxels) {
			BlockPos pos = new BlockPos(p.x, p.y, p.z);
			if (onSurface) {
				pos = pos.up();
			}
			jumper.setHeight(pos);
			setBlocks.add(new UndoableSetBlock(player.getWorld(), selectionManager, pos, state));
			reselects.add(pos);
		}

		jumper.jump();

		// Set the blocks inside an undoManager transaction
		Transaction t = undoManager.newTransaction();
		t.addAll(setBlocks);
//		for (UndoableSetBlock u : setBlocks) {
//			t.add(u.doIt());
//		}
		t.commit();

		// Select all transformed blocks
		selectionManager.select(player, reselects);

		// String sound = "plato:" + StringUtils.toCamelCase(getClass());
		// TODO how to look up sound from state
		String sound = "ambient.weather.thunder";
		//Block b;
		player.playSoundAtPlayer(sound);
	}

}
