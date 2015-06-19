package ds.plato.select;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import ds.geom.IntegerDomain;
import ds.geom.VoxelSet;
import ds.plato.player.IPlayer;

public interface ISelect {

	// Methods that message player------------------------------------------------

	public void select(IPlayer player, BlockPos pos);

	public void select(IPlayer player, Iterable<BlockPos> positions);

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

	public List<BlockPos> getGrownSelections();

	public void setGrownSelections(List<BlockPos> points);

	public void clearGrownSelections();

}
