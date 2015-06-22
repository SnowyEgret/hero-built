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
import net.minecraft.world.IBlockAccess;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.network.SelectionMessage;
import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;

public class SelectionManager implements ISelect {

	private final Map<BlockPos, Selection> selMap = Maps.newLinkedHashMap();
	private Block blockSelected;
	private List<BlockPos> reselects;
	private Set<BlockPos> grownSelections = Sets.newHashSet();

	public SelectionManager(Block blockSelected) {
		this.blockSelected = blockSelected;
	}

	@Override
	public void select(IPlayer player, Iterable<BlockPos> pos) {
		for (BlockPos p : pos) {
			select(player.getWorld(), p);
		}
		// player.getWorld().update();
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void select(IPlayer player, BlockPos pos) {
		select(player.getWorld(), pos);
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void deselect(IPlayer player, BlockPos pos) {
		deselect(player.getWorld(), pos);
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void deselect(IPlayer player, Iterable<BlockPos> positions) {
		for (BlockPos pos : positions) {
			deselect(player.getWorld(), pos);
		}
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void reselect(IPlayer player) {
		reselect(player.getWorld());
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void clearSelections(IPlayer player) {
		clearSelections(player.getWorld());
		MoJo.network.sendTo(new SelectionMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	// ----------------------------------------------------------------------------

	// Returns a copy to avoid concurrent modification
	@Override
	public Iterable<Selection> getSelections() {
		return Lists.newArrayList(selMap.values());
	}

	// Returns a copy to avoid concurrent modification
	@Override
	public List<Selection> getSelectionList() {
		return Lists.newArrayList(selMap.values());
	}

	@Override
	public Selection getSelection(BlockPos pos) {
		return selMap.get(pos);
	}

	public void setReselects(List<BlockPos> reselects) {
		this.reselects = reselects;
	}

	@Override
	public int size() {
		return selMap.size();
	}

	@Override
	public boolean isSelected(BlockPos pos) {
		return selMap.containsKey(pos);
	}

	@Override
	public Selection firstSelection() {
		if (selMap.isEmpty()) {
			return null;
		}
		// TODO ConcurrentModificationException here in MP
		return selMap.values().iterator().next();
	}

	@Override
	public Selection lastSelection() {
		if (selMap.isEmpty()) {
			return null;
		}
		return getSelectionList().get(selMap.size() - 1);
	}

	@Override
	public IntegerDomain getDomain() {
		return voxelSet().getDomain();
	}

	@Override
	public VoxelSet voxelSet() {
		VoxelSet set = new VoxelSet();
		for (BlockPos pos : selMap.keySet()) {
			set.add(new Point3i(pos.getX(), pos.getY(), pos.getZ()));
		}
		return set;
	}

	@Override
	// public List<BlockPos> getGrownSelections() {
	public Set<BlockPos> getGrownSelections() {
		if (grownSelections.isEmpty()) {
			grownSelections.addAll(selMap.keySet());
		}
		return grownSelections;
	}

	@Override
	// public void setGrownSelections(List<BlockPos> points) {
	public void setGrownSelections(Set<BlockPos> positions) {
		grownSelections.clear();
		grownSelections.addAll(positions);
		// grownSelections = positions;
	}

	@Override
	public void clearGrownSelections() {
		grownSelections.clear();
	}

	@Override
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
		if (b instanceof BlockAir) {
			System.out.println("BlockAir. Returning null.");
			return null;
		}

		// if (b instanceof BlockSelected) {
		// //s = getSelection(pos);
		// System.out.println("BlockSelected. Returning " + s);
		// //return s;
		// }

		// if (isSelected(pos)) {
		// //s = getSelection(pos);
		// System.out.println("Position already selected. Returning " + s);
		// //return s;
		// }

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
