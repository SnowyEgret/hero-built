package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpellTextMessage implements IMessage {

	private String text;

	public SpellTextMessage() {
	}

	public SpellTextMessage(String text) {
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

}
