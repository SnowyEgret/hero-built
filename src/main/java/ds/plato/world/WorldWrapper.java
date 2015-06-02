package ds.plato.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.network.SetBlockMessage;
import ds.plato.network.SetBlockStateMessage;

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
	public Block getBlock(BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		return b.getBlock();
	}

	@Override
	public void setBlock(BlockPos pos, Block block) {
		System.out.println("world="+world);
		if (world.isRemote) {
			// if (sendPackets) {
			Plato.network.sendToServer(new SetBlockMessage(pos, block));
		} else {
			IBlockState state = block.getBlockState().getBaseState();
			world.setBlockState(pos, state, 3);
		}
	}

	@Override
	public void setBlockState(BlockPos pos, IBlockState state) {
		System.out.println("world="+world);
		// if (sendPackets) {
		if (world.isRemote) {
			Plato.network.sendToServer(new SetBlockStateMessage(pos, state));
		} else {
			world.setBlockState(pos, state, 3);
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

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		return world.getBlockState(pos);
	}
}
