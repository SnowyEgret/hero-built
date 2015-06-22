package org.snowyegret.mojo.select;

import java.util.List;
import java.util.Set;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import org.snowyegret.mojo.player.IPlayer;

import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;

public interface ISelect {

	// Methods that message player------------------------------------------------

	public void select(IPlayer player, BlockPos pos);

	public void select(IPlayer player, Iterable<BlockPos> positions);

	// public void select(IPlayer player, List<IUndoable> undoables);

	public void clearSelections(IPlayer player);

	public void deselect(IPlayer player, BlockPos pos);

	public void deselect(IPlayer player, Iterable<BlockPos> positions);

	public void reselect(IPlayer player);

	// -----------------------------------------------------------------------------

	public Iterable<Selection> getSelections();

	public Selection getSelection(BlockPos pos);

	public void setReselects(List<BlockPos> reselects);

	public int size();

	public boolean isSelected(BlockPos pos);

	public List<Selection> getSelectionList();

	public Selection firstSelection();

	public Selection lastSelection();

	public Vec3 getCentroid();

	public IntegerDomain getDomain();

	// TODO Encapsulate this in selectionManager
	public VoxelSet voxelSet();

	// Below used only by AbstractSpellSelect -----------------------------
	// TODO Moveto AbstractSpellSelect

	//public List<BlockPos> getGrownSelections();
	public Set<BlockPos> getGrownSelections();

	// public void setGrownSelections(List<BlockPos> points);
	public void setGrownSelections(Set<BlockPos> positions);

	public void clearGrownSelections();

}
