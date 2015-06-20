package org.snowyegret.plato.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetBlockStateMessage implements IMessage {

	private static final int SIZE = 5;
	private int x;
	private int y;
	private int z;
	private int stateId;
	public SetBlockStateMessage() {
	}
	
	public SetBlockStateMessage(BlockPos pos, IBlockState state) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		stateId = Block.getStateId(state);
	}
	
	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public IBlockState getState() {
		return Block.getStateById(stateId);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, SIZE);
		ByteBufUtils.writeVarInt(buf, y, SIZE);
		ByteBufUtils.writeVarInt(buf, z, SIZE);
		//System.out.println("stateId=" + stateId);
		ByteBufUtils.writeVarInt(buf, stateId, SIZE);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, SIZE);
		y = ByteBufUtils.readVarInt(buf, SIZE);
		z = ByteBufUtils.readVarInt(buf, SIZE);
		int stateId = ByteBufUtils.readVarInt(buf, SIZE);
		//System.out.println("stateId=" + stateId);
		this.stateId = stateId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlockStateMessage [getPos()=");
		builder.append(getPos());
		builder.append(", getState()=");
		builder.append(getState());
		builder.append("]");
		return builder.toString();
	}

}
