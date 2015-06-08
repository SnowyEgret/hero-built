package ds.plato.select;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import com.google.common.collect.Lists;

import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;
import ds.plato.block.BlockSelected;
import ds.plato.world.IWorld;

public class SelectionManager implements ISelect {

	private final Map<BlockPos, Selection> selections = new LinkedHashMap<>();
	private Block blockSelected;
	private List<BlockPos> reselects;
	private List<BlockPos> grownSelections = new ArrayList<>();

	public SelectionManager(Block blockSelected) {
		this.blockSelected = blockSelected;
	}

	@Override
	public Selection select(IWorld world, BlockPos pos) {
		IBlockState state = world.getActualState(pos);
		Block b = (state.getBlock());
		if (b instanceof BlockAir) {
			System.out.println("Found BlockAir. Returning null.");
			return null;
		}
		if (b instanceof BlockSelected) {
			// getSelection is already null so we have no way of knowing what the original block was
			System.out.println("Found BlockSelected. Returning null.");
			return getSelection(pos);
		}
		Selection selection = new Selection(pos, state);
		selections.put(pos, selection);
		world.setState(pos, blockSelected.getDefaultState());
		return selection;
	}

	@Override
	public void deselect(IWorld world, Selection selection) {
		// Block b = selection.getBlock();
		// if (b instanceof BlockPicked) {
		// //Look up pick from pickManager
		// //b = ((BlockPicked)b).getPos())
		// }
		System.out.println("selection=" + selection);
		world.setState(selection.getPos(), selection.getState());
		selections.remove(selection.getPos());
	}

	@Override
	public void deselect(IWorld world, BlockPos pos) {
		Selection s = getSelection(pos);
		if (s != null) {
			deselect(world, s);
		}
	}

	// Returns a copy to avoid concurrent modification
	@Override
	public Iterable<Selection> getSelections() {
		List<Selection> l = new ArrayList<>();
		l.addAll(selections.values());
		return l;
	}

	@Override
	public Selection getSelection(BlockPos pos) {
		return selections.get(pos);
	}

	public void setReselects(List<BlockPos> reselects) {
		this.reselects = reselects;
	}

	@Override
	public void reselect(IWorld world) {
		clearSelections(world);
		if (reselects != null) {
			for (BlockPos pos : reselects) {
				select(world, pos);
			}
		}
	}

	@Override
	public void clearSelections(IWorld world) {
		if (!selections.isEmpty()) {
			reselects = Lists.newArrayList(selections.keySet());
			// getSelections returns a copy so that it is not modified by deselect
			for (Selection s : getSelections()) {
				deselect(world, s);
			}
		}
		grownSelections.clear();
	}

	// TODO Does not set a block so doesn't need world. Only called by UndoableSetBlock.set(). Is there another way?
	@Override
	public Selection removeSelection(BlockPos pos) {
		return selections.remove(pos);
	}

	@Override
	public int size() {
		return selections.size();
	}

	@Override
	public boolean isSelected(BlockPos pos) {
		return selections.containsKey(pos);
	}

	@Override
	public List<Selection> getSelectionList() {
		List<Selection> l = new ArrayList<>();
		l.addAll(selections.values());
		return l;
	}

	@Override
	public Selection firstSelection() {
		if (selections.isEmpty()) {
			return null;
		}
		// TODO ConcurrentModificationException here in MP
		return selections.values().iterator().next();
	}

	@Override
	public Selection lastSelection() {
		if (selections.isEmpty()) {
			return null;
		}
		return getSelectionList().get(selections.size() - 1);
	}

	@Override
	public IntegerDomain getDomain() {
		return voxelSet().getDomain();
	}

	@Override
	public VoxelSet voxelSet() {
		VoxelSet set = new VoxelSet();
		for (BlockPos pos : selections.keySet()) {
			set.add(new Point3i(pos.getX(), pos.getY(), pos.getZ()));
		}
		return set;
	}

	@Override
	public List<BlockPos> getGrownSelections() {
		if (grownSelections.isEmpty()) {
			grownSelections.addAll(selections.keySet());
		}
		return grownSelections;
	}

	@Override
	public void setGrownSelections(List<BlockPos> points) {
		grownSelections = points;
	}

	@Override
	public void clearGrownSelections() {
		grownSelections.clear();
	}

	@Override
	public String toString() {
		return "SelectionManager [selections=" + selections.size() + "]";
	}

	@Override
	public Vec3 getCentroid() {
		Point3d c = voxelSet().centroid();
		return new Vec3(c.x, c.y, c.z);
	}

}
