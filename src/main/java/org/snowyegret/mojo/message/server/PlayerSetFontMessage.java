package org.snowyegret.mojo.message.server;

import io.netty.buffer.ByteBuf;

import java.awt.Font;

import org.snowyegret.mojo.util.StringUtils;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerSetFontMessage implements IMessage {
	
	private Font font;
	
	public PlayerSetFontMessage() {
	}
	
	public PlayerSetFontMessage(Font font) {
		this.font = font;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, StringUtils.encodeFont(font));
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		font = Font.decode(ByteBufUtils.readUTF8String(buf));
	}
	
	public Font getFont() {
		return font;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayerSetFontMessage [font=");
		builder.append(font);
		builder.append("]");
		return builder.toString();
	}

}
