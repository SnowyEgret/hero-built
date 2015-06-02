package ds.plato.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

//TGG MBE60
public class SetBlockStateMessageHandler implements IMessageHandler<SetBlockStateMessage, IMessage> {

	@Override
	public IMessage onMessage(final SetBlockStateMessage message, MessageContext ctx) {
		if (ctx.side != Side.SERVER) {
			System.out.println("Message received on client side instead of server side. Returning null");
			return null;
		}
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		player.getServerForPlayer().addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	private void processMessage(SetBlockStateMessage message, EntityPlayerMP player) {
		World world = player.worldObj;
		world.setBlockState(new BlockPos(message.getX(), message.getY(), message.getZ()), message.getState());
	}
}
