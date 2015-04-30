package ds.plato.api;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.util.BlockPos;
import ds.geom.VoxelSet;
import ds.plato.select.Selection;

public interface ISelect {

	public Selection select(IWorld world, BlockPos pos);

	public void deselect(IWorld world, BlockPos pos);
	
	public void deselect(IWorld world, Selection selection);

	public Iterable<Selection> getSelections();

	public Selection getSelection(BlockPos pos);

	public void reselect(IWorld world);

	public void clearSelections(IWorld world);

	public Selection removeSelection(BlockPos pos);

	public int size();

	public boolean isSelected(BlockPos pos);
	
	public List<Selection> getSelectionList();
	
	public Selection firstSelection();

	public Selection lastSelection();
	
	//TODO this is messy.
	public VoxelSet voxelSet();

	//Below used only by AbstractSpellSelect -----------------------------
	//TODO Moveto AbstractSpellSelect

	public List<BlockPos> getGrownSelections();

	public void setGrownSelections(List<BlockPos> points);

	public void clearGrownSelections();

}
