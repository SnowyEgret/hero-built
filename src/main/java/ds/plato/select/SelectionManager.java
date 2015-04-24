package ds.plato.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

import ds.geom.VoxelSet;
import ds.plato.api.ISelect;
import ds.plato.api.IWorld;

public class SelectionManager implements ISelect {

	private final Map<BlockPos, Selection> selections = new ConcurrentHashMap<>();
	// private final Map<Point3i, Selection> selections = new HashMap<>();
	private IWorld world;
	private Block blockSelected;
	private List<BlockPos> lastSelections;
	private List<BlockPos> grownSelections = new ArrayList<>();

	public SelectionManager(Block blockSelected) {
		this.blockSelected = blockSelected;
	}

	// Returns a copy to avoid concurrent modification
	@Override
	public Iterable<Selection> getSelections() {
		List<Selection> l = new ArrayList<>();
		l.addAll(selections.values());
		return l;
	}

	@Override
	//public Selection selectionAt(int x, int y, int z) {
	public Selection selectionAt(BlockPos pos) {
		return selections.get(pos);
	}

	@Override
	public void select(IWorld world, BlockPos pos) {
		Block prevBlock = world.getBlock(pos);
		//int metadata = world.getMetadata(pos);
		world.setBlock(pos, blockSelected);
		Selection s = new Selection(pos, prevBlock);
		selections.put(s.getPos(), s);
		this.world = world;
	}

	@Override
	public void deselect(BlockPos pos) {
		deselect(selectionAt(pos));
	}

	@Override
	public void deselect(Selection s) {
		selections.remove(s.getPos());
		world.setBlock(s.getPos(), s.getBlock() );
	}

	@Override
	public void clearSelections() {
		if (!selections.isEmpty()) {
			lastSelections = Lists.newArrayList(selections.keySet());
			for (Selection s : selections.values()) {
				deselect(s);
			}
		}
		grownSelections.clear();
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
	public Collection<BlockPos> selectedPoints() {
		return selections.keySet();
	}

	@Override
	public Selection removeSelection(BlockPos pos) {
		return selections.remove(pos);
	}

	@Override
	public VoxelSet voxelSet() {
		VoxelSet set = new VoxelSet();
		for (BlockPos pos : selections.keySet()) {
			set.add(new Point3i(pos.getX(), pos.getY(), pos.getZ()));
		}
		return set;
		///return new VoxelSet(selections.keySet());
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
		return getSelectionList().get(0);
	}

	@Override
	public Selection lastSelection() {
		if (selections.isEmpty()) {
			return null;
		}
		// FIXME going out of bounds using ConcurrentHashMap when the selections are being deleted
		// onDrawBlockHightlight using firstSelection() instead
		return getSelectionList().get(selections.size() - 1);
	}

	@Override
	public void reselectLast() {
		if (lastSelections != null) {
			for (BlockPos pos : lastSelections) {
				select(world, pos);
			}
		}
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

	// TODO Used only by Test class. Make default when test class is in same package.
	public void addSelection(Selection s) {
		selections.put(s.getPos(), s);
	}

	@Override
	public String toString() {
		return "SelectionManager [world=" + idOf(world) + ", selections=" + selections.size() + "]";
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}

}
