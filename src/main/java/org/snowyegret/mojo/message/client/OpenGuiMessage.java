package org.snowyegret.mojo.message.client;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class OpenGuiMessage implements IMessage {

	private int id;

	public OpenGuiMessage() {
	}

	public OpenGuiMessage(int id) {
		this.id = id;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
	}

	public int getId() {
		return id;
	}

}
