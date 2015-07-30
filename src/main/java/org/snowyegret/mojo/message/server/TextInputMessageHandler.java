package org.snowyegret.mojo.message.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.gui.ITextInput;
import org.snowyegret.mojo.player.Player;

public class TextInputMessageHandler implements IMessageHandler<TextInputMessage, IMessage> {

	@Override
	public IMessage onMessage(final TextInputMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				Player p = new Player(player);
				ITextInput s = (ITextInput) p.getSpell();
				String text = message.getText();
				if (text.equals(TextInputMessage.CANCEL)) {
					s.cancel(p);
				} else {
					s.setText(text, p);
				}
			}
		});
		return null;
	}

}
