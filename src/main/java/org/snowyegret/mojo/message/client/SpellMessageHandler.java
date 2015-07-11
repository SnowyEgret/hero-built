package org.snowyegret.mojo.message.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.event.EventHandlerClient;

public class SpellMessageHandler implements IMessageHandler<SpellMessage, IMessage> {

	@Override
	public IMessage onMessage(final SpellMessage message, MessageContext ctx) {
		 Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			public void run() {
				// TODO static field SpellInfo on EventHandlerClient
				EventHandlerClient.overlay.setDistance(message.getDistance());
				EventHandlerClient.overlay.setMessage(message.getMessage());
			}

		});
		return null;
	}
}
