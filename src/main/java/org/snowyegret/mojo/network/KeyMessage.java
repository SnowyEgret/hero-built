package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KeyMessage implements IMessage {

	private int keyCode;
	private boolean keyState;
	private BlockPos cursorPos;
	private static final int SIZE = 5;

	public KeyMessage() {
	}

	public KeyMessage(int keyCode, boolean keyState, BlockPos cursorPos) {
		this.keyCode = keyCode;
		this.keyState = keyState;
		this.cursorPos = cursorPos;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean getKeyState() {
		return keyState;
	}

	public BlockPos getCursorPos() {
		return cursorPos;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, keyCode, SIZE);
		ByteBufUtils.writeVarInt(buf, keyState ? 0 : 1, SIZE);
		ByteBufUtils.writeVarInt(buf, cursorPos.getX(), SIZE);
		ByteBufUtils.writeVarInt(buf, cursorPos.getY(), SIZE);
		ByteBufUtils.writeVarInt(buf, cursorPos.getZ(), SIZE);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		keyCode= ByteBufUtils.readVarInt(buf, SIZE);
		int i = ByteBufUtils.readVarInt(buf, SIZE);
		keyState = (i == 0) ? true : false;
		int x = ByteBufUtils.readVarInt(buf, SIZE);
		int y = ByteBufUtils.readVarInt(buf, SIZE);
		int z = ByteBufUtils.readVarInt(buf, SIZE);
		cursorPos = new BlockPos(x, y, z);
	}

}
