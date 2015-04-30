package ds.plato.api;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IWorld {

	public void setBlock(BlockPos pos, Block block);

	public Block getBlock(BlockPos pos);

	public World getWorld();
}
