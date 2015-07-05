package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpellMessage implements IMessage {

	private Double distance = -1d;
	private String message = "";

	public SpellMessage() {
	}

	public SpellMessage(String message) {
		this.message = message;
	}

	public SpellMessage(Double distance) {
		this.distance = distance;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(distance);
		ByteBufUtils.writeUTF8String(buf, message);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		distance = buf.readDouble();
		if (distance.intValue() == -1) {
			distance = null;
		}
		message = ByteBufUtils.readUTF8String(buf);
		if (message.isEmpty()) {
			message = null;
		}
	}

	public Double getDistance() {
		return distance;
	}

	public String getMessage() {
		return message;
	}

}
