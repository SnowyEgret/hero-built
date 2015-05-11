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
	public Block getBlock(BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		return b.getBlock();
	}

	@Override
	public void setBlock(BlockPos pos, Block block) {
		// TODO try this for preventing dropping
		// world.removeTileEntity(x, y, z);
		IBlockState state = block.getBlockState().getBaseState();
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
