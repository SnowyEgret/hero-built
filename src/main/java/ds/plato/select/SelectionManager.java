package ds.plato.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;

import com.google.common.collect.Lists;

import ds.plato.api.ISelect;
import ds.plato.api.IWorld;
import ds.geom.VoxelSet;

public class SelectionManager implements ISelect {

	private final Map<Point3i, Selection> selections = new ConcurrentHashMap<>();
	// private final Map<Point3i, Selection> selections = new HashMap<>();
	private IWorld world;
	private Block blockSelected;
	private List<Point3i> lastSelections;
	private List<Point3i> grownSelections = new ArrayList<>();

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

	// @Override
	// public Iterable<Selection> getSelections() {
	// return selections.values();
	// }

	@Override
	public Selection selectionAt(int x, int y, int z) {
		return selections.get(new Point3i(x, y, z));
	}

	@Override
	public void select(IWorld world, int x, int y, int z) {
		Block prevBlock = world.getBlock(x, y, z);
		int metadata = world.getMetadata(x, y, z);
		world.setBlock(x, y, z, blockSelected, 0);
		Selection s = new Selection(x, y, z, prevBlock, metadata);
		selections.put(s.point3i(), s);
		this.world = world;
	}

	@Override
	public void deselect(int x, int y, int z) {
		deselect(selectionAt(x, y, z));
	}

	@Override
	public void deselect(Selection s) {
		selections.remove(new Point3i(s.x, s.y, s.z));
		world.setBlock(s.x, s.y, s.z, s.block, s.metadata);
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
	public boolean isSelected(int x, int y, int z) {
		return selections.containsKey(new Point3i(x, y, z));
	}

	@Override
	public Collection<Point3i> selectedPoints() {
		return selections.keySet();
	}

	@Override
	public Selection removeSelection(int x, int y, int z) {
		return selections.remove(new Point3i(x, y, z));
	}

	@Override
	public VoxelSet voxelSet() {
		return new VoxelSet(selections.keySet());
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
			for (Point3i p : lastSelections) {
				select(world, p.x, p.y, p.z);
			}
		}
	}

	@Override
	public List<Point3i> getGrownSelections() {
		if (grownSelections.isEmpty()) {
			grownSelections.addAll(selections.keySet());
		}
		return grownSelections;
	}

	@Override
	public void setGrownSelections(List<Point3i> points) {
		grownSelections = points;
	}

	@Override
	public void clearGrownSelections() {
		grownSelections.clear();
	}

	// TODO Used only by Test class. Make default when test class is in same package.
	public void addSelection(Selection s) {
		selections.put(s.point3i(), s);
	}

	@Override
	public String toString() {
		return "SelectionManager [world=" + idOf(world) + ", selections=" + selections.size() + "]";
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}

}
