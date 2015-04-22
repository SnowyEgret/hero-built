package ds.plato.core;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
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
	public Block getBlock(int x, int y, int z) {
		return world.getBlock(x, y, z);
	}

	@Override
	public int getMetadata(int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public void setBlock(int x, int y, int z, Block block, int metadata) {
		// TODO try this for preventing dropping
		// world.removeTileEntity(x, y, z);
		world.setBlock(x, y, z, block, metadata, 3);
		// System.out.println("[WorldWrapper.setBlock] sendPackets=" + sendPackets);
		if (sendPackets) {
			Plato.network.sendToServer(new SetBlockMessage(x, y, z, block, metadata));
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
