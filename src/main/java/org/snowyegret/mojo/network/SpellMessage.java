package org.snowyegret.mojo.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpellMessage implements IMessage {

	private double distance = -1;

	public SpellMessage() {
	}

	public SpellMessage(double distance) {
		this.distance = distance;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(distance);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		distance = buf.readDouble();
	}

	public double getDistance() {
		return distance;
	}

}
