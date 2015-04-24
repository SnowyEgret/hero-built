package ds.plato.api;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.pick.Pick;

public interface IPick {

	//public void pick(IWorld world, int x, int y, int z, int side);
	public void pick(IWorld w, BlockPos pos, EnumFacing side);

	public void clearPicks();

	public Pick[] getPicks();

	public boolean isFinishedPicking();

	public boolean isPicking();

	//public Pick getPickAt(int x, int y, int z);
	public Pick getPickAt(BlockPos pos); 
	public void reset(int numPicks);

	public Pick lastPick();
}
