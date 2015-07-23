package org.snowyegret.mojo.message.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MouseClickMessage implements IMessage {

	private int button;
	private BlockPos pos;
	private MovingObjectType typeOfHit;
	private boolean isDoubleClick = false;

	public MouseClickMessage() {
	}

	public MouseClickMessage(int button, BlockPos pos, MovingObjectType typeOfHit, boolean isDoubleClick) {
		this.button = button;
		this.pos = pos;
		this.typeOfHit = typeOfHit;
		this.isDoubleClick = isDoubleClick;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(button);
		buf.writeLong(pos.toLong());
		buf.writeInt(typeOfHit.ordinal());
		buf.writeBoolean(isDoubleClick);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		button = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
		int ordinal = buf.readInt();
		for (MovingObjectType t : MovingObjectType.values()) {
			if (t.ordinal() == ordinal) {
				typeOfHit = t;
				break;
			}
		}
		isDoubleClick = buf.readBoolean();
	}

	public BlockPos getPos() {
		return pos;
	}

	public int getButton() {
		return button;
	}

	public MovingObjectType getTypeOfHit() {
		return typeOfHit;
	}

	public boolean isDoubleClick() {
		return isDoubleClick;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MouseClickMessage [button=");
		builder.append(button);
		builder.append(", pos=");
		builder.append(pos);
		builder.append(", typeOfHit=");
		builder.append(typeOfHit);
		builder.append(", isDoubleClick=");
		builder.append(isDoubleClick);
		builder.append("]");
		return builder.toString();
	}

}
