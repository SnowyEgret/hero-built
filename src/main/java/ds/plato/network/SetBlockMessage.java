package ds.plato.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetBlockMessage implements IMessage {

	public int x;
	public int y;
	public int z;
	//public BlockPos pos;
	public Block block;
	public int metadata;
	int size = 5;
	
	public SetBlockMessage(BlockPos pos, Block block) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		//this.pos = pos;
		//1.8
		//TODO commented out for now - runtime exception on cast
		//this.metadata = block.getMetaFromState((IBlockState) block.getBlockState());
		this.block = block;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, size);
		y = ByteBufUtils.readVarInt(buf, size);
		z = ByteBufUtils.readVarInt(buf, size);
		int blockId = ByteBufUtils.readVarInt(buf, size);
		System.out.println("blockId=" + blockId);
		block = Block.getBlockById(blockId);
		System.out.println("block=" + block);
		metadata = ByteBufUtils.readVarInt(buf, 1);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, size);
		ByteBufUtils.writeVarInt(buf, y, size);
		ByteBufUtils.writeVarInt(buf, z, size);
		ByteBufUtils.writeVarInt(buf, Block.getIdFromBlock(block), size);
		ByteBufUtils.writeVarInt(buf, metadata, 1);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlockMessage [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", z=");
		builder.append(z);
		builder.append(", block=");
		builder.append(block);
		builder.append(", metadata=");
		builder.append(metadata);
		builder.append("]");
		return builder.toString();
	}

	//TODO 
//	public BlockPos getPos() {
//		return pos;
//	}

}
