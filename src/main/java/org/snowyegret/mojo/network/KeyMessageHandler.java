package org.snowyegret.mojo.network;

import java.util.NoSuchElementException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.spell.Action;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.matrix.SpellCopy;
import org.snowyegret.mojo.item.spell.transform.SpellDelete;
import org.snowyegret.mojo.item.staff.Staff;
import org.snowyegret.mojo.pick.PickManager;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.undo.Transaction;
import org.snowyegret.mojo.undo.UndoableSetBlock;
import org.snowyegret.mojo.world.IWorld;

public class KeyMessageHandler implements IMessageHandler<KeyMessage, IMessage> {

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

		Player player = Player.instance(playerIn);
		IWorld world = player.getWorld();
		Modifiers modifiers = player.getModifiers();
		SelectionManager selectionManager = player.getSelectionManager();
		PickManager pickManager = player.getPickManager();
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
						player.getTransactionManager().redo(player);
					} else {
						selectionManager.clearSelections();
						player.getTransactionManager().undo(player);
					}
				}
				selectionManager.reselect();
			} catch (NoSuchElementException e) {
				// TODO Log to overlay. Create info line in overlay
				//MoJo.network.sendTo(new SpellMessage(p0.distance(p1)), (EntityPlayerMP) player.getPlayer());
				System.out.println(e.getMessage());
			}
			break;

		case NEXT:
			stack = player.getHeldItemStack();
			Staff staff = player.getStaff();
			if (staff != null) {
				Spell spell = null;
				if (modifiers.isPressed(Modifier.CTRL)) {
					spell = staff.prevSpell(stack);
				} else {
					spell = staff.nextSpell(stack);
				}
				player.resetPicks(spell);
			}
			break;

		case DELETE:
			new SpellDelete().invoke(player);
			break;

		case RESELECT:
			selectionManager.reselect();
			break;

		case LEFT:
			copy(player, -1, 0);
			break;

		case RIGHT:
			copy(player, 1, 0);
			break;

		case UP:
			if (modifiers.isPressed(Modifier.SHIFT)) {
				copyVertical(player, 1);
			} else {
				copy(player, 0, -1);
			}
			break;

		case DOWN:
			// TODO Modifier constructor with mulitple keys for right shift
			if (modifiers.isPressed(Modifier.SHIFT)) {
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
				selectionManager.clearSelections();
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

	private void copy(Player player, int leftRight, int upDown) {
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
		SpellCopy s = new SpellCopy();
		// So far this is the only place this is being used
		// Reslects will be selected in Transation.dO
		player.setLastInvokedSpell(s);
		s.invoke(player, new BlockPos(0, 0, 0), to);
	}

	private void copyVertical(Player player, int upDown) {
		SpellCopy s = new SpellCopy();
		player.setLastInvokedSpell(s);
		s.invoke(player, new BlockPos(0, 0, 0), new BlockPos(0, upDown, 0));
	}

}
