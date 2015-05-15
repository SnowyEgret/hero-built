package ds.plato.network;

import org.lwjgl.input.Keyboard;

import ds.plato.item.staff.Staff;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NextSpellMessageHandler implements IMessageHandler<NextSpellMessage, IMessage> {

	@Override
	public IMessage onMessage(final NextSpellMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	private void processMessage(NextSpellMessage message, EntityPlayerMP player) {
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null) {
			Item i = stack.getItem();
			if (i instanceof Staff) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					((Staff) i).previousSpell(stack);
				} else {
					((Staff) i).nextSpell(stack);
				}
			}
		}
	}
}
