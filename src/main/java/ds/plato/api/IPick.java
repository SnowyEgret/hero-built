package ds.plato.api;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.pick.Pick;

public interface IPick {

	public Pick pick(IWorld w, BlockPos pos, EnumFacing side);

	public Pick[] getPicks();

	public Pick getPick(BlockPos pos); 
	
	public void clearPicks();

	public boolean isPicking();

	public boolean isFinishedPicking();

	public void reset(int numPicks);

	public Pick lastPick();
}
