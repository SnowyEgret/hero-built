package ds.plato.pick;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.player.IPlayer;
import ds.plato.world.IWorld;

public interface IPick {

	public void clearPicks(IPlayer player);

	// --------------------------------------------------------

	public Pick pick(IWorld world, BlockPos pos, EnumFacing side);

	public Pick[] getPicks();

	public Pick getPick(BlockPos pos);

	public void clearPicks(IWorld world);

	public boolean isPicking();

	public boolean isFinishedPicking();

	public void reset(int numPicks);

	public Pick firstPick();

	public Pick lastPick();

	public void repick(IWorld world);

}
