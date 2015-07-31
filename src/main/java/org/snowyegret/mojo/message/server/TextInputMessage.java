package org.snowyegret.mojo.message.server;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TextInputMessage implements IMessage {

	public static final String CANCEL = "cancel";
	private String text;

	public TextInputMessage() {
	}

	public TextInputMessage(String text) {
		this.text = text;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextInputMessage [text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

}
