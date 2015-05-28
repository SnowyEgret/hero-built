package ds.plato.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Spell;
import ds.plato.item.staff.Staff;

public class NextSpellMessageHandler implements IMessageHandler<NextSpellMessage, IMessage> {

	@Override
	public IMessage onMessage(final NextSpellMessage message, MessageContext ctx) {
		System.out.println(message);
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}
	
	//Private------------------------------------------------------------

	private void processMessage(NextSpellMessage message, EntityPlayerMP player) {
		System.out.println(message);
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null) {
			Item i = stack.getItem();
			if (i instanceof Staff) {
				((Staff) i).nextSpell(stack);
			}
		}
	}
}
