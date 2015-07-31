package org.snowyegret.mojo.message.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.player.Player;

public class PlayerSetFontMessageHandler implements IMessageHandler<PlayerSetFontMessage, IMessage> {

	@Override
	public IMessage onMessage(final PlayerSetFontMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				Player p = new Player(player);
				System.out.println("message=" + message);
				p.setFont(message.getFont());
			}
		});
		return null;
	}
}
