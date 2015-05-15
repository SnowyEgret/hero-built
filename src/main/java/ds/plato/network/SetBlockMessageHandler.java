package ds.plato.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

//TGG MBE60
public class SetBlockMessageHandler implements IMessageHandler<SetBlockMessage, IMessage> {

	@Override
	public IMessage onMessage(final SetBlockMessage message, MessageContext ctx) {
		//Println should verify that this is running in network thread
		System.out.println("message=" + message);
		if (ctx.side != Side.SERVER) {
			System.out.println("Message received on client side instead of server side. Returning null");
			return null;
		}
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	private void processMessage(SetBlockMessage message, EntityPlayerMP player) {
		// World world = MinecraftServer.getServer().worldServerForDimension(0);
		World world = player.worldObj;
		// TODO implement this!
		// world.setBlockState(new BlockPos(message.x, message.y, message.z), message.block);
	}
}
