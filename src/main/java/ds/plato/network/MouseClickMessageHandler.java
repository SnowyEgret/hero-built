package ds.plato.network;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.google.common.collect.Lists;

import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;

public class MouseClickMessageHandler implements IMessageHandler<MouseClickMessage, IMessage> {

	@Override
	public IMessage onMessage(final MouseClickMessage message, MessageContext ctx) {
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

	private void processMessage(MouseClickMessage message, EntityPlayerMP playerIn) {
		IPlayer player = Player.instance(playerIn);
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		MovingObjectType typeOfHit = message.getTypeOfHit();
		int button = message.getButton();
		System.out.println("button=" + button);
		switch (typeOfHit) {
		case BLOCK:
			if (button == 0) {
				select(message.getPos(), player);
			}
			break;
		case ENTITY:
			break;
		case MISS:
			if (button == 0) {
				selectionManager.clearSelections(player);
			} else if (button == 1) {
				pickManager.clearPicks(player);
			}
			break;
		default:
			break;
		}
	}

	// Called by onPlayerInteractEvent on server side only.
	private void select(BlockPos pos, IPlayer player) {

		ISelect selectionManager = player.getSelectionManager();

		Modifiers modifiers = player.getModifiers();

		// Shift replaces the current selections with a region.
		if (modifiers.isPressed(Modifier.SHIFT) && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			IBlockState firstState = selectionManager.firstSelection().getState();
			selectionManager.clearSelections(player);

			Iterable<BlockPos> allInBox = BlockPos.getAllInBox(lastPos, pos);
			ArrayList<BlockPos> positions = Lists.newArrayList();
			for (BlockPos p : allInBox) {
				if (modifiers.isPressed(Modifier.ALT)) {
					IBlockState state = player.getWorld().getActualState(p);
					if (state == firstState) {
						positions.add(p);
					}
				} else {
					positions.add(p);
				}
			}
			selectionManager.select(player, positions);
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (modifiers.isPressed(Modifier.CTRL)) {
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(player, pos);
			} else {
				selectionManager.select(player, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections(player);
		selectionManager.select(player, pos);
	}

}
