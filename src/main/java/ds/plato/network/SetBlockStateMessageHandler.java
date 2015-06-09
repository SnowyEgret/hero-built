package ds.plato.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

//https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe60_network_messages
public class SetBlockStateMessageHandler implements IMessageHandler<SetBlockStateMessage, IMessage> {

	@Override
	public IMessage onMessage(final SetBlockStateMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		// player.getServerForPlayer().addScheduledTask(new Runnable() {
		// public void run() {
		processMessage(message, player);
		// }
		// });
		//Maybe something like this
		//if(message.isLast()) {
		return new SetBlockStateDoneMessage();
		//}
		//return null;
	}

	private void processMessage(SetBlockStateMessage message, EntityPlayerMP player) {
		// System.out.println("message=" + message);
		World world = player.worldObj;
		// world.setBlockState(new BlockPos(message.getX(), message.getY(), message.getZ()), message.getState());
		world.setBlockState(message.getPos(), message.getState());
	}
}