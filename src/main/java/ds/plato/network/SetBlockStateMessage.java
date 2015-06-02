package ds.plato.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetBlockStateMessage implements IMessage {

	private int x;
	private int y;
	private int z;
	private int stateId;
	private int size = 5;
	
	public SetBlockStateMessage() {
	}
	
	public SetBlockStateMessage(BlockPos pos, IBlockState state) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		stateId = Block.getStateId(state);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public int getStateId() {
		return stateId;
	}

	public IBlockState getState() {
		return Block.getStateById(stateId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, size);
		y = ByteBufUtils.readVarInt(buf, size);
		z = ByteBufUtils.readVarInt(buf, size);
		int stateId = ByteBufUtils.readVarInt(buf, size);
		//System.out.println("stateId=" + stateId);
		this.stateId = stateId;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, size);
		ByteBufUtils.writeVarInt(buf, y, size);
		ByteBufUtils.writeVarInt(buf, z, size);
		//System.out.println("stateId=" + stateId);
		ByteBufUtils.writeVarInt(buf, stateId, size);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlockStateMessage [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", z=");
		builder.append(z);
		builder.append(", state=");
		builder.append(getState());
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

}
