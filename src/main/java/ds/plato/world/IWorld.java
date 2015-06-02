package ds.plato.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IWorld {

	@Deprecated
	public void setBlock(BlockPos pos, Block block);

	@Deprecated
	public Block getBlock(BlockPos pos);

	public World getWorld();

	public IBlockState getBlockState(BlockPos pos);

	public void setBlockState(BlockPos pos, IBlockState state);
}
