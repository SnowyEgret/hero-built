package ds.plato.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ds.plato.Plato;
import ds.plato.event.ForgeEventHandler;

public class SelectionMessageHandler implements IMessageHandler<SelectionMessage, IMessage> {

	@Override
	public IMessage onMessage(final SelectionMessage message, MessageContext ctx) {
		Minecraft minecraft = Minecraft.getMinecraft();
		final WorldClient worldClient = minecraft.theWorld;
		minecraft.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(worldClient, message);
			}

		});

		return null;
	}

	// Private------------------------------------------------------------
	
	private void processMessage(WorldClient worldClient, SelectionMessage message) {
		Plato.instance.setSelectionInfo(message.getSelectionInfo());		
	}

}
