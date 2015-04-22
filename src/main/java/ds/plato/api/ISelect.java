package ds.plato.api;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3i;

import ds.geom.VoxelSet;
import ds.plato.select.Selection;

public interface ISelect {

	public Iterable<Selection> getSelections();

	public Selection selectionAt(int x, int y, int z);

	public void select(IWorld world, int x, int y, int z);

	public void deselect(int x, int y, int z);

	public void deselect(Selection s);

	public void reselectLast();

	public void clearSelections();

	public Selection removeSelection(int x, int y, int z);

	public int size();

	//public Collection<? extends Point3i> selectedPoints();
	public Collection<Point3i> selectedPoints();

	public boolean isSelected(int x, int y, int z);

	//public ISelect setWorld(IWorld world);
	
	public Selection firstSelection();

	public Selection lastSelection();
	
	public VoxelSet voxelSet();

	public List<Selection> getSelectionList();

	public List<Point3i> getGrownSelections();

	public void setGrownSelections(List<Point3i> points);

	public void clearGrownSelections();

}
