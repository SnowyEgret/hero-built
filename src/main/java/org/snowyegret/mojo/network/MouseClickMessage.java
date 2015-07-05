package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MouseClickMessage implements IMessage {

	private int button;
	private BlockPos pos;
	private MovingObjectType typeOfHit;

	public MouseClickMessage() {
	}

	public MouseClickMessage(int button, BlockPos pos, MovingObjectType typeOfHit) {
		this.button = button;
		this.pos = pos;
		this.typeOfHit = typeOfHit;

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(button);
		buf.writeLong(pos.toLong());
		buf.writeInt(typeOfHit.ordinal());
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MouseClickMessage [button=");
		builder.append(button);
		builder.append(", pos=");
		builder.append(pos);
		builder.append(", typeOfHit=");
		builder.append(typeOfHit);
		builder.append("]");
		return builder.toString();
	}

}
