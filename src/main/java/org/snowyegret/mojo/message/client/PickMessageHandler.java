package org.snowyegret.mojo.message.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.event.EventHandlerClient;

public class PickMessageHandler implements IMessageHandler<PickMessage, IMessage> {

	@Override
	public IMessage onMessage(final PickMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			public void run() {
				EventHandlerClient.pickInfo = message.getPickInfo();
			}
		});
		return null;
	}
}
