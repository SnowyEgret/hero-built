package ds.plato.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.network.SetBlockStateMessage;

public class WorldWrapper implements IWorld {

	private World world;

	public WorldWrapper(World world) {
		this.world = world;
	}

	@Override
	public Block getBlock(BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		return b.getBlock();
	}

	@Override
	public void setState(BlockPos pos, IBlockState state) {
		// TODO This is wrong. Remove forceMessaging flag
		// if (world.isRemote || Plato.forceMessaging) {
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Sending SetBlockStateMessage");
		// Plato.network.sendToServer(new SetBlockStateMessage(pos, state));
		// } else {
		world.setBlockState(pos, state, 3);
		// }
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public IBlockState getState(BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	public IBlockState getActualState(BlockPos p) {
		IBlockState state = getState(p);
		return state.getBlock().getActualState(state, world, p);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WorldWrapper [world=");
		builder.append(world);
		builder.append("]");
		return builder.toString();
	}
}
