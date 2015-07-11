package org.snowyegret.mojo.message.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.event.EventHandlerClient;

public class SelectionMessageHandler implements IMessageHandler<SelectionMessage, IMessage> {

	@Override
	public IMessage onMessage(final SelectionMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			public void run() {
				//System.out.println("selectionInfo=" + message.getSelectionInfo());
				EventHandlerClient.selectionInfo = message.getSelectionInfo();
			}

		});
		return null;
	}
}
