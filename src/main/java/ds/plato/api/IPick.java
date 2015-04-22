package ds.plato.api;

import javax.vecmath.Point3i;

import ds.plato.pick.Pick;
import net.minecraft.block.Block;

public interface IPick {

	public void pick(IWorld world, int x, int y, int z, int side);

	public void clearPicks();

	public Pick[] getPicks();

	public boolean isFinishedPicking();

	public boolean isPicking();

	public Pick getPickAt(int x, int y, int z);

	public void reset(int numPicks);

//	public IPick setWorld(IWorld world);

	public Pick lastPick();
}
