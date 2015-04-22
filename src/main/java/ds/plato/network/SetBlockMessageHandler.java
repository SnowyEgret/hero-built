package ds.plato.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetBlockMessageHandler implements IMessageHandler<SetBlockMessage, IMessage> {

	@Override
	public IMessage onMessage(SetBlockMessage message, MessageContext ctx) {
		System.out.println("[SetBlockMessageHandler.onMessage] message=" + message);
		//World world = MinecraftServer.getServer().worldServerForDimension(0);
		World world = Minecraft.getMinecraft().theWorld;
		world.setBlock(message.x, message.y, message.z, message.block, message.metadata, 3);
		return message;
	}

}
