package org.snowyegret.plato.network;

import java.util.NoSuchElementException;

import org.snowyegret.plato.item.spell.Action;
import org.snowyegret.plato.item.spell.Modifier;
import org.snowyegret.plato.item.spell.Modifiers;
import org.snowyegret.plato.item.spell.matrix.SpellCopy;
import org.snowyegret.plato.item.spell.transform.SpellDelete;
import org.snowyegret.plato.item.staff.Staff;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;
import org.snowyegret.plato.player.IPlayer;
import org.snowyegret.plato.player.Player;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.select.Selection;
import org.snowyegret.plato.undo.Transaction;
import org.snowyegret.plato.undo.UndoableSetBlock;
import org.snowyegret.plato.world.IWorld;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyMessageHandler implements IMessageHandler<KeyMessage, IMessage> {

	private ISelect selectionManager;
	private IPick pickManager;

	// public static SpellInvoker lastSpell;

	@Override
	public IMessage onMessage(final KeyMessage message, MessageContext ctx) {
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

	private void processMessage(KeyMessage message, EntityPlayerMP playerIn) {

		IPlayer player = Player.instance(playerIn);
		IWorld world = player.getWorld();
		Modifiers modifiers = player.getModifiers();
		selectionManager = player.getSelectionManager();
		pickManager = player.getPickManager();
		int keyCode = message.getKeyCode();
		boolean keyState = message.getKeyState();

		Modifier modifier = Modifier.fromKeyCode(keyCode);
		if (modifier != null) {
			modifiers.setPressed(modifier, keyState);
			// System.out.println("modifiers=" + modifiers);
		}

		// System.out.println("keyCode=" + keyCode);
		Action action = Action.fromKeyCode(keyCode);
		// System.out.println("action=" + action);

		if (action == null || !keyState) {
			return;
		}

		ItemStack stack;
		switch (action) {

		case UNDO:
			try {
				if (modifiers.isPressed(Modifier.CTRL)) {
					if (modifiers.isPressed(Modifier.SHIFT)) {
						player.getUndoManager().redo(player);
					} else {
						selectionManager.clearSelections(player);
						player.getUndoManager().undo(player);
					}
				}
				// if (lastSpell != null && lastSpell.getSpell() instanceof SpellCopy) {
				selectionManager.reselect(player);
				// }
			} catch (NoSuchElementException e) {
				// TODO Log to overlay. Create info line in overlay
				System.out.println(e.getMessage());
			}
			break;

		case NEXT:
			stack = player.getHeldItemStack();
			Staff staff = player.getStaff();
			if (staff != null) {
				if (modifiers.isPressed(Modifier.CTRL)) {
					staff.prevSpell(stack, pickManager);
				} else {
					staff.nextSpell(stack, pickManager);
				}
			}
			break;

		// case REINVOKE:
		// lastSpell.invoke();
		// break;

		case DELETE:
			new SpellDelete().invoke(player);
			break;

		case RESELECT:
			selectionManager.reselect(player);
			break;

		case LEFT:
			copy(player, -1, 0);
			break;

		case RIGHT:
			copy(player, 1, 0);
			break;

		case UP:
			// This should be alt so that control can be deleteOriginal
			if (modifiers.isPressed(Modifier.CTRL)) {
				copyVertical(player, 1);
			} else {
				copy(player, 0, -1);
			}
			break;

		case DOWN:
			// This should be alt so that control can be deleteOriginal
			// TODO Modifier constructor with mulitple keys
			if (modifiers.isPressed(Modifier.CTRL)) {
				copyVertical(player, -1);
			} else {
				copy(player, 0, 1);
			}
			break;
		case COPY:
			if (modifiers.isPressed(Modifier.CTRL)) {
				BlockPos cursor = message.getCursorPos();
				player.getClipboard().setSelections(selectionManager.getSelections());
				player.getClipboard().setOrigin(cursor);
				selectionManager.clearSelections(player);
			}
			break;
		case CUT:
			if (modifiers.isPressed(Modifier.CTRL)) {
				BlockPos cursor = message.getCursorPos();
				player.getClipboard().setOrigin(cursor);
				player.getClipboard().setSelections(selectionManager.getSelections());
				new SpellDelete().invoke(player);
			}
			break;
		case PASTE:
			if (modifiers.isPressed(Modifier.CTRL)) {
				Iterable<Selection> selections = player.getClipboard().getSelections();
				BlockPos cursor = message.getCursorPos();
				BlockPos delta = cursor.subtract(player.getClipboard().getOrigin().down());

				Transaction t = new Transaction();
				for (Selection s : selections) {
					BlockPos p = s.getPos().add(delta);
					t.add(new UndoableSetBlock(p, player.getWorld().getState(p), s.getState()));
				}
				t.dO(player);
			}
			break;
		default:
			return;
		}
	}

	private void copy(IPlayer player, int leftRight, int upDown) {
		BlockPos to = null;
		switch (player.getDirection()) {
		case NORTH:
			to = new BlockPos(leftRight, 0, upDown);
			break;
		case SOUTH:
			to = new BlockPos(-leftRight, 0, -upDown);
			break;
		case EAST:
			to = new BlockPos(-upDown, 0, leftRight);
			break;
		case WEST:
			to = new BlockPos(upDown, 0, -leftRight);
			break;
		}
		new SpellCopy().invoke(player, new BlockPos(0,0,0), to);
	}

	private void copyVertical(IPlayer player, int upDown) {
		new SpellCopy().invoke(player, new BlockPos(0,0,0), new BlockPos(0, upDown, 0));
	}

}
