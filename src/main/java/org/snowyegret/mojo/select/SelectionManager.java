package org.snowyegret.mojo.select;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.network.SelectionMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;

public class SelectionManager {

	private Map<BlockPos, Selection> selMap = Maps.newLinkedHashMap();
	private Block blockSelected;
	private List<BlockPos> reselects;
	private Set<BlockPos> grownSelections = Sets.newHashSet();
	private Player player;

	public SelectionManager(Player player, Block blockSelected) {
		this.player = player;
		this.blockSelected = blockSelected;
	}

	public void select(Iterable<BlockPos> pos) {
		for (BlockPos p : pos) {
			select(player.getWorld(), p);
		}
		// player.getWorld().update();
		// Message client with selection info to display in overlay
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	// TODO
	public void select(BlockPos pos) {
		select(player.getWorld(), pos);
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	public void deselect(BlockPos pos) {
		deselect(player.getWorld(), pos);
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	public void deselect(Iterable<BlockPos> positions) {
		for (BlockPos pos : positions) {
			deselect(player.getWorld(), pos);
		}
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	public void reselect() {
		reselect(player.getWorld());
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	public void clearSelections() {
		clearSelections(player.getWorld());
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
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
		Block b = (state.getBlock());

		// Should not happen.
		// TODO Except that SpellDelete is reselecting on a delete key press
		if (b instanceof BlockAir) {
			System.out.println("BlockAir. Returning null.");
			return null;
		}

		// if (b instanceof BlockPicked) {
		// PrevStateTileEntity te = (PrevStateTileEntity) world.getTileEntity(pos);
		// if (te != null) {
		// state = te.getPrevState();
		// }
		// }

		if (b instanceof BlockSelected) {
			// Implementation of: Find a way to restore selected blocks to their previous state when they are left in
			// world after a crash #173
			// PrevStateTileEntity must call super.writeToNBT
			System.out.println("Selecting a BlockSelected");
			System.out.println("This Should only occur when a selection is left in world after a crash.");
			System.out.println("Looking for previous state on tile entity");
			// This should only be the case when a selection is left in the world after a crash.
			// If the BlockSelected has a tile entity it will render properly, if not as black/magenta block
			// Try to get its state from its tile entity so that a player can select it and restore its
			// previous state by clearing the selections.
			PrevStateTileEntity te = (PrevStateTileEntity) world.getTileEntity(pos);
			if (te != null) {
				state = te.getPrevState();
			}
		}

		if (isSelected(pos)) {
			System.out.println("Position is selected but not a BlockSelected. This should not happen.");
		}

		s = new Selection(pos, state);
		selMap.put(pos, s);

		world.setState(pos, blockSelected.getDefaultState());
		PrevStateTileEntity tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
		tileEntity.setPrevState(state);
		// Do I have to message client?
		// No, because PrevStateTileEntity overrides getDescriptionPacket and onDataPacket

		return s;
	}

	private void deselect(IWorld world, Selection selection) {
		world.setState(selection.getPos(), selection.getState());
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
