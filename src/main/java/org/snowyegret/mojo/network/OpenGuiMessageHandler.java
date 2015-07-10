package org.snowyegret.mojo.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.event.EventHandlerClient;
import org.snowyegret.mojo.player.Player;

public class OpenGuiMessageHandler implements IMessageHandler<OpenGuiMessage, IMessage>{

	@Override
	public IMessage onMessage(final OpenGuiMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			public void run() {
				new Player().openGui(message.getId());
			}

		});
		return null;
	}

}
