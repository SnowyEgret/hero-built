package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KeyMessage implements IMessage {

	private int keyCode;
	private boolean keyState;
	private BlockPos cursorPos;

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
		buf.writeInt(keyCode);
		buf.writeBoolean(keyState);
		buf.writeLong(cursorPos.toLong());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		keyCode = buf.readInt();
		keyState = buf.readBoolean();
		cursorPos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeyMessage [keyCode=");
		builder.append(keyCode);
		builder.append(", keyState=");
		builder.append(keyState);
		builder.append(", cursorPos=");
		builder.append(cursorPos);
		builder.append("]");
		return builder.toString();
	}

}
