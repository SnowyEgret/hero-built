package org.snowyegret.mojo.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.gui.ITextSetable;
import org.snowyegret.mojo.player.Player;

public class SpellTextMessageHandler  implements IMessageHandler<SpellTextMessage, IMessage>{

	@Override
	public IMessage onMessage(final SpellTextMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				Player p = new Player(player);
				ITextSetable s = (ITextSetable) p.getHeldItem();
				s.setText(message.getText(), p);
			}
		});
		return null;
	}

}
