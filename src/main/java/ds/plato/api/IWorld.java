package ds.plato.api;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IWorld {

	public void setBlock(int x, int y, int z, Block block, int metadata);

	public Block getBlock(int x, int y, int z);

	public int getMetadata(int x, int y, int z);

	public World getWorld();


}
