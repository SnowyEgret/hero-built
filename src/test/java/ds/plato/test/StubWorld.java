package ds.plato.test;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.world.IWorld;

public class StubWorld implements IWorld {

	// Map<String, Block> blocks = new HashMap<>();
	Map<String, IBlockState> states = new HashMap<>();
	Block dirt;

	public StubWorld(Block dirt) {
		this.dirt = dirt;
	}

	@Override
	// public void setBlock(BlockPos pos, Block block) {
	public void setState(BlockPos pos, IBlockState block) {
		states.put(encode(pos), block);
		// this.metadata.put(encode(pos), Integer.valueOf(metadata));
	}

	@Override
	public Block getBlock(BlockPos pos) {
		Block b = states.get(encode(pos)).getBlock();
		if (b == null) {
			return dirt;
		} else {
			return b;
		}
	}

	@Override
	public String toString() {
		return "StubWorld" + Integer.toHexString(hashCode()) + " [blocks=" + states + "]";
	}

	// private String encode(int x, int y, int z) {
	private String encode(BlockPos pos) {
		return "" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ();
	}

	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public void setBlock(BlockPos pos, Block block) {
		// TODO Auto-generated method stub		
	}

	@Override
	public IBlockState getState(BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBlockState getActualState(BlockPos p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isForceMessaging() {
		// TODO Auto-generated method stub
		return false;
	}
}
