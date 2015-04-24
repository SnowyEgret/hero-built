package ds.plato.api;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IWorld {

	//public void setBlock(int x, int y, int z, Block block, int metadata);
	public void setBlock(BlockPos pos, Block block);

	//public Block getBlock(int x, int y, int z);
	public Block getBlock(BlockPos pos);

	//public int getMetadata(int x, int y, int z);
	//public int getMetadata(BlockPos pos);

	public World getWorld();


}
