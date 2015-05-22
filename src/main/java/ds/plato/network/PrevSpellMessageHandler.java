package ds.plato.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ds.plato.item.staff.Staff;

public class PrevSpellMessageHandler implements IMessageHandler<PrevSpellMessage, IMessage> {

	@Override
	public IMessage onMessage(final PrevSpellMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	private void processMessage(PrevSpellMessage message, EntityPlayerMP player) {
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null) {
			Item i = stack.getItem();
			if (i instanceof Staff) {
				((Staff) i).prevSpell(stack);
			}
		}
	}
}
