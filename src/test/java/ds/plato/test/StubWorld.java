package ds.plato.test;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.api.IWorld;

public class StubWorld implements IWorld {

	Map<String, Block> blocks = new HashMap<>();
	//Map<String, Integer> metadata = new HashMap<>();
	Block dirt;
	
	public StubWorld(Block dirt) {
		this.dirt = dirt;
	}

	@Override
	public void setBlock(BlockPos pos, Block block) {
		blocks.put(encode(pos), block);
		//this.metadata.put(encode(pos), Integer.valueOf(metadata));
	}

	@Override
	public Block getBlock(BlockPos pos) {
		Block b =  blocks.get(encode(pos));
		if (b == null) {
			return dirt;
		} else {
			return b;
		}
	}

//	@Override
//	public int getMetadata(int x, int y, int z) {
//		Integer m = metadata.get(encode(x, y, z));
//		if (m == null) {
//			return 0;
//		} else {
//			return m;
//		}
//	}

	@Override
	public String toString() {
		return "StubWorld" + Integer.toHexString(hashCode()) + " [blocks=" + blocks + "]";
	}

	//private String encode(int x, int y, int z) {
	private String encode(BlockPos pos) {
		return "" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ();
	}

	@Override
	public World getWorld() {
		return null;
	}

//	@Override
//	public int getMetadata(BlockPos pos) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

}
