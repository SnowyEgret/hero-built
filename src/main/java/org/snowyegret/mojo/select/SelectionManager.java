package org.snowyegret.mojo.select;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.snowyegret.geom.IntegerDomain;
import org.snowyegret.geom.VoxelSet;
import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockMaquette;
import org.snowyegret.mojo.block.BlockHighlight;
import org.snowyegret.mojo.block.BlockHightlightTileEntity;
import org.snowyegret.mojo.message.client.SelectionMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SelectionManager {

	private Map<BlockPos, Selection> selMap = Maps.newLinkedHashMap();
	private List<BlockPos> reselects;
	private Set<BlockPos> grownSelections = Sets.newHashSet();
	private Player player;

	public SelectionManager(Player player) {
		this.player = player;
	}

	public void select(Iterable<BlockPos> pos) {
		for (BlockPos p : pos) {
			select(player.getWorld(), p);
		}
		// player.getWorld().update();
		// Message client with selection info to display in overlay
		player.sendMessage(new SelectionMessage(this));
	}

	// TODO
	public void select(BlockPos pos) {
		Selection s = select(player.getWorld(), pos);
		// if (s != null) {
		player.sendMessage(new SelectionMessage(this));
		// }
	}

	public void deselect(BlockPos pos) {
		deselect(player.getWorld(), pos);
		player.sendMessage(new SelectionMessage(this));
	}

	public void deselect(Iterable<BlockPos> positions) {
		for (BlockPos pos : positions) {
			deselect(player.getWorld(), pos);
		}
		player.sendMessage(new SelectionMessage(this));
	}

	public void reselect() {
		reselect(player.getWorld());
		player.sendMessage(new SelectionMessage(this));
	}

	public void clearSelections() {
		clearSelections(player.getWorld());
		player.sendMessage(new SelectionMessage(this));
	}

	// ----------------------------------------------------------------------------

	// Returns a copy to avoid concurrent modification
	public Iterable<Selection> getSelections() {
		return Lists.newArrayList(selMap.values());
	}

	// Returns a copy to avoid concurrent modification
	public List<Selection> getSelectionList() {
		return Lists.newArrayList(selMap.values());
	}

	public Selection getSelection(BlockPos pos) {
		return selMap.get(pos);
	}

	public void setReselects(List<BlockPos> reselects) {
		this.reselects = reselects;
	}

	public int size() {
		return selMap.size();
	}

	public boolean isSelected(BlockPos pos) {
		return selMap.containsKey(pos);
	}

	public Selection firstSelection() {
		if (selMap.isEmpty()) {
			return null;
		}
		// TODO ConcurrentModificationException here in MP
		return selMap.values().iterator().next();
	}

	public Selection lastSelection() {
		if (selMap.isEmpty()) {
			return null;
		}
		return getSelectionList().get(selMap.size() - 1);
	}

	public IntegerDomain getDomain() {
		return voxelSet().getDomain();
	}

	public VoxelSet voxelSet() {
		VoxelSet set = new VoxelSet();
		for (BlockPos pos : selMap.keySet()) {
			set.add(new Point3i(pos.getX(), pos.getY(), pos.getZ()));
		}
		return set;
	}

	public Set<BlockPos> getGrownSelections() {
		if (grownSelections.isEmpty()) {
			grownSelections.addAll(selMap.keySet());
		}
		return grownSelections;
	}

	public void setGrownSelections(Set<BlockPos> positions) {
		grownSelections.clear();
		grownSelections.addAll(positions);
		// grownSelections = positions;
	}

	public void clearGrownSelections() {
		grownSelections.clear();
	}

	public Vec3 getCentroid() {
		Point3d c = voxelSet().centroid();
		return new Vec3(c.x, c.y, c.z);
	}

	@Override
	public String toString() {
		return "SelectionManager [selections=" + selMap.size() + "]";
	}

	// Private-----------------------------------------------------------------------

	private Selection select(IWorld world, BlockPos pos) {
		Selection s = null;
		IBlockState state = world.getActualState(pos);
		Block b = state.getBlock();

		if (b instanceof BlockAir) {
			// System.out.println("BlockAir. Returning null.");
			return null;
		}

		// Selecting a BlockSaved deletes it's tile entity.
		// No reason to select
		if (b instanceof BlockMaquette) {
			return null;
		}

		if (b instanceof BlockHighlight) {
			// Implementation of: Find a way to restore selected blocks to their previous state when they are left in
			// world after a crash #173
			// PrevStateTileEntity must call super.writeToNBT
			//System.out.println("Selecting a BlockHighlight");
			// System.out.println("This Should only occur when a selection is left in world after a crash.");
			// System.out.println("Looking for previous state on tile entity");
			// This should only be the case when a selection is left in the world after a crash.
			// If the BlockSelected has a tile entity it will render properly, if not as black/magenta block
			// Try to get its state from its tile entity so that a player can select it and restore its
			// previous state by clearing the selections.
			BlockHightlightTileEntity te = (BlockHightlightTileEntity) world.getTileEntity(pos);
			if (te != null) {
				state = te.getPrevState();
				// System.out.println("state=" + state);
			} else {
				System.out.println("Selected a BlockHighlight with no tile entity");
			}
		}

		if (isSelected(pos)) {
			//System.out.println("Position is already selected. pos=" + pos);
		}

		s = new Selection(pos, state);
		selMap.put(pos, s);

		world.setState(pos, MoJo.blockHighlight.getDefaultState());
		BlockHightlightTileEntity te = (BlockHightlightTileEntity) world.getTileEntity(pos);
		te.setPrevState(state);
		te.setColor(BlockHighlight.COLOR_SELECTED);

		return s;
	}

	private void deselect(IWorld world, Selection selection) {
		if (selection.getState() != null) {
			world.setState(selection.getPos(), selection.getState());
		} else {
			System.out.println("Could not set state. selection.getState()=null");
		}
		selMap.remove(selection.getPos());
	}

	private void deselect(IWorld world, BlockPos pos) {
		Selection s = getSelection(pos);
		if (s != null) {
			deselect(world, s);
		}
	}

	private void reselect(IWorld world) {
		clearSelections(world);
		if (reselects != null) {
			for (BlockPos pos : reselects) {
				select(world, pos);
			}
		}
	}

	private void clearSelections(IWorld world) {
		if (!selMap.isEmpty()) {
			reselects = Lists.newArrayList(selMap.keySet());
			// getSelections returns a copy so that it is not modified by deselect
			for (Selection s : getSelections()) {
				deselect(world, s);
			}
		}
		grownSelections.clear();
	}

}
