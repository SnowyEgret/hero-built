package ds.plato.network;

import java.util.NoSuchElementException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ds.plato.item.spell.Action;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.Modifier;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.SpellInvoker;
import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.item.staff.Staff;
import ds.plato.pick.IPick;
import ds.plato.pick.PickManager;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.select.SelectionManager;
import ds.plato.undo.IUndo;
import ds.plato.undo.UndoManager;
import ds.plato.world.IWorld;

public class KeyMessageHandler implements IMessageHandler<KeyMessage, IMessage> {

	//private IUndo undoManager;
	private ISelect selectionManager;
	private IPick pickManager;
	public static SpellInvoker lastSpell;

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
		//undoManager = player.getUndoManager();
		selectionManager = player.getSelectionManager();
		pickManager = player.getPickManager();
		int keyCode = message.getKeyCode();
		boolean keyState = message.getKeyState();

		Modifier modifier = Modifier.fromKeyCode(keyCode);
		if (modifier != null) {
			modifiers.setPressed(modifier, keyState);
			//System.out.println("modifiers=" + modifiers);
		}

		//System.out.println("keyCode=" + keyCode);
		Action action = Action.fromKeyCode(keyCode);
		//System.out.println("action=" + action);

		if (action == null || !keyState) {
			return;
		}

		ItemStack stack;
		switch (action) {

		case UNDO:
			try {
				if (modifiers.isPressed(Modifier.CTRL)) {
					if (modifiers.isPressed(Modifier.SHIFT)) {
						player.getUndoManager().redo();
					} else {
						player.getUndoManager().undo();
					}
				}
				if (lastSpell.getSpell() instanceof SpellCopy) {
					selectionManager.reselect(player);
				}
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

		case REINVOKE:
			lastSpell.invoke();
			break;

		case DELETE:
			new SpellDelete().invoke(world, player);
			break;

		case RESELECT:
			selectionManager.reselect(player);
			break;

		case LEFT:
			copy(player, world, -1, 0);
			break;

		case RIGHT:
			copy(player, world, 1, 0);
			break;

		case UP:
			// This should be alt so that control can be deleteOriginal
			if (modifiers.isPressed(Modifier.CTRL)) {
				copyVertical(player, world, 1);
			} else {
				copy(player, world, 0, -1);
			}
			break;

		case DOWN:
			// This should be alt so that control can be deleteOriginal
			// TODO Modifier constructor with mulitple keys
			if (modifiers.isPressed(Modifier.CTRL)) {
				copyVertical(player, world, -1);
			} else {
				copy(player, world, 0, 1);
			}
			break;

		default:
			return;
		}
	}

	private void copy(IPlayer player, IWorld world, int leftRight, int upDown) {
		// Method reset clears picks
		pickManager.reset(2);
		// Same old problem. reset is clearing the picks, but the state is not cleared yet in MP
		// Pick at BlockPos(0, 0, 0) is still BlockSelected but PickManager is already cleared.
		// Check for picking a BlockSelected in PickManager.pick returns null and array is out of bounds
		// in both MP and SP
		pickManager.pick(world, new BlockPos(0, 0, 0), null);
		switch (player.getDirection()) {
		case NORTH:
			pickManager.pick(world, new BlockPos(leftRight, 0, upDown), null);
			break;
		case SOUTH:
			pickManager.pick(world, new BlockPos(-leftRight, 0, -upDown), null);
			break;
		case EAST:
			pickManager.pick(world, new BlockPos(-upDown, 0, leftRight), null);
			break;
		case WEST:
			pickManager.pick(world, new BlockPos(upDown, 0, -leftRight), null);
			break;
		}
		ISpell spell = new SpellCopy();
		lastSpell = new SpellInvoker(pickManager, spell, world, player);
		spell.invoke(world, player);
	}

	private void copyVertical(IPlayer player, IWorld world, int upDown) {
		pickManager.reset(2);
		pickManager.pick(world, new BlockPos(0, 0, 0), null);
		pickManager.pick(world, new BlockPos(0, upDown, 0), null);
		new SpellCopy().invoke(world, player);
	}

}
