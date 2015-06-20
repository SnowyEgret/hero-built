package org.snowyegret.plato.pick;

import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IPick {

	public void clearPicks(IPlayer player);

	public void repick(IPlayer player);

	public Pick pick(IPlayer player, BlockPos pos, EnumFacing side);

	public Pick[] getPicks();

	public Pick getPick(BlockPos pos);

	public boolean isPicking();

	public boolean isFinishedPicking();

	public void reset(int numPicks);

	public Pick firstPick();

	public Pick lastPick();

}
