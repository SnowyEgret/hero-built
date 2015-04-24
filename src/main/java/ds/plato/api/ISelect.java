package ds.plato.api;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.util.BlockPos;
import ds.geom.VoxelSet;
import ds.plato.select.Selection;

public interface ISelect {

	public Iterable<Selection> getSelections();

	public Selection selectionAt(BlockPos pos);

	public void select(IWorld world, BlockPos pos);

	public void deselect(BlockPos pos);

	public void deselect(Selection s);

	public void reselectLast();

	public void clearSelections();

	public Selection removeSelection(BlockPos pos);

	public int size();

	public Collection<BlockPos> selectedPoints();

	public boolean isSelected(BlockPos pos);
	
	public Selection firstSelection();

	public Selection lastSelection();
	
	public VoxelSet voxelSet();

	public List<Selection> getSelectionList();

	public List<BlockPos> getGrownSelections();

	public void setGrownSelections(List<BlockPos> points);

	public void clearGrownSelections();

}
