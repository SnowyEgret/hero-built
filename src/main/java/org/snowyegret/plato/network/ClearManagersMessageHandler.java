package org.snowyegret.plato.network;

import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.player.Player;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClearManagersMessageHandler implements IMessageHandler<ClearManagersMessage, IMessage> {

	@Override
	public IMessage onMessage(final ClearManagersMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	// Private------------------------------------------------------------

	private void processMessage(ClearManagersMessage message, EntityPlayerMP playerIn) {
		IPlayer player = Player.instance(playerIn);
		player.getSelectionManager().clearSelections(player);
		player.getPickManager().clearPicks(player);	
	}
}
