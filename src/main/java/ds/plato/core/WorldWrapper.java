package ds.plato.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.api.IWorld;
import ds.plato.network.SetBlockMessage;

public class WorldWrapper implements IWorld {

	private World world;
	private boolean sendPackets;

	public WorldWrapper(World world) {
		this.world = world;
		if (world instanceof WorldClient) {
			sendPackets = true;
		}
	}

	@Override
	//public Block getBlock(int x, int y, int z) {
	public Block getBlock(BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		//return world.getBlock(x, y, z);
		return b.getBlock();
	}

//	@Override
	//public int getMetadata(int x, int y, int z) {
//	public int getMetadata(BlockPos pos) {
//		//return world.getBlockMetadata(x, y, z);
//		//getBlock(pos).getgetStateFromMeta(pos);
//		//TODO return metadata
//		return 0;
//	}

	@Override
	//public void setBlock(int x, int y, int z, Block block, int metadata) {
	public void setBlock(BlockPos pos, Block block) {
		// TODO try this for preventing dropping
		// world.removeTileEntity(x, y, z);
		//world.setBlock(x, y, z, block, metadata, 3);
		IBlockState state = (IBlockState) block.getBlockState();
		world.setBlockState(pos, state, 3);
		if (sendPackets) {
			Plato.network.sendToServer(new SetBlockMessage(pos, block));
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WorldWrapper [world=");
		builder.append(world);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public World getWorld() {
		return world;
	}

}
