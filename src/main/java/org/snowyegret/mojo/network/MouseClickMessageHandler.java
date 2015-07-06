package org.snowyegret.mojo.network;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.SelectionManager;

import com.google.common.collect.Lists;

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
		System.out.println("message=" + message);
		Player player = new Player(playerIn);
		SelectionManager selectionManager = player.getSelectionManager();
		MovingObjectType typeOfHit = message.getTypeOfHit();
		int button = message.getButton();
		//System.out.println("button=" + button);
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
				selectionManager.clearSelections();
			} else if (button == 1) {
				player.getPickManager().clearPicks();
			}
			break;
		default:
			break;
		}
	}

	// Called by onPlayerInteractEvent on server side only.
	private void select(BlockPos pos, Player player) {

		Modifiers modifiers = player.getModifiers();
		boolean selectRegion = modifiers.isPressed(Modifier.SHIFT);
		boolean matchFirstSelection = modifiers.isPressed(Modifier.ALT);
		boolean addToSelection = modifiers.isPressed(Modifier.CTRL);
		
		SelectionManager selectionManager = player.getSelectionManager();

		// Shift replaces the current selections with a region.
		if ( selectRegion && selectionManager.size() != 0) {
			BlockPos lastPos = selectionManager.lastSelection().getPos();
			IBlockState firstState = selectionManager.firstSelection().getState();
			selectionManager.clearSelections();

			Iterable<BlockPos> allInBox = BlockPos.getAllInBox(lastPos, pos);
			ArrayList<BlockPos> positions = Lists.newArrayList();
			for (BlockPos p : allInBox) {
				if (matchFirstSelection) {
					IBlockState state = player.getWorld().getActualState(p);
					if (state == firstState) {
						positions.add(p);
					}
				} else {
					positions.add(p);
				}
			}
			selectionManager.select(positions);
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (addToSelection) {
			if (selectionManager.isSelected(pos)) {
				selectionManager.deselect(pos);
			} else {
				selectionManager.select(player, pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		selectionManager.clearSelections();
		selectionManager.select(player, pos);
	}

}
