package org.snowyegret.mojo.message.client;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpellMessage implements IMessage {

	private Double doubleValue = -1d;
	private Integer intValue = -1;
	private String message = "";

	public SpellMessage() {
	}

	public SpellMessage(String message) {
		this.message = message;
	}

//	public SpellMessage(Double doubleValue) {
//		this.doubleValue = doubleValue;
//	}

	public SpellMessage(String message, Double doubleValue) {
		this.message = message;
		this.doubleValue = doubleValue;
	}

	public SpellMessage(String message, Integer intValue) {
		this.message = message;
		this.intValue = intValue;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(doubleValue);
		buf.writeInt(intValue);
		ByteBufUtils.writeUTF8String(buf, message);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		doubleValue = buf.readDouble();
		if (doubleValue.intValue() == -1) {
			doubleValue = null;
		}
		intValue = buf.readInt();
		if (intValue == -1) {
			intValue = null;
		}
		message = ByteBufUtils.readUTF8String(buf);
		if (message.isEmpty()) {
			message = null;
		}
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public String getMessage() {
		return message;
	}

}
