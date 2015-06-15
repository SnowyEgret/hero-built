package ds.plato.world;

import ds.plato.undo.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IWorld {

	public Block getBlock(BlockPos pos);

	public World getWorld();

	public IBlockState getState(BlockPos pos);

	public IBlockState getActualState(BlockPos pos);

	public void setState(BlockPos pos, IBlockState state);

	public void updateClient();
	
	//public abstract Transaction newTransaction();

}
