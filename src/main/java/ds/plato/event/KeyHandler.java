package ds.plato.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
import ds.plato.item.spell.ISpell;
import ds.plato.item.spell.SpellInvoker;
import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.item.staff.Staff;
import ds.plato.network.NextSpellMessage;
import ds.plato.network.PrevSpellMessage;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.world.IWorld;

public class KeyHandler {

	private IUndo undoManager;
	private ISelect selectionManager;
	private IPick pickManager;
	private Map<Action, KeyBinding> keyBindings = new HashMap<>();
	public static SpellInvoker lastSpell;

	private enum Action {

		UNDO(Keyboard.KEY_Z),
		NEXT(Keyboard.KEY_TAB),
		DELETE(Keyboard.KEY_DELETE),
		RESELECT(Keyboard.KEY_L),
		LEFT(Keyboard.KEY_LEFT),
		RIGHT(Keyboard.KEY_RIGHT),
		UP(Keyboard.KEY_UP),
		DOWN(Keyboard.KEY_DOWN),
		REINVOKE(Keyboard.KEY_PERIOD);

		private int keyCode;

		Action(int keyCode) {
			this.keyCode = keyCode;
		}
	}

	public KeyHandler(IUndo undo, ISelect select, IPick pick) {
		this.undoManager = undo;
		this.selectionManager = select;
		this.pickManager = pick;
		for (Action a : Action.values()) {
			String s = a.toString().toLowerCase();
			// TODO internationalize
			keyBindings.put(a, registerKeyBinding(s, a.keyCode));
		}
		//System.out.println(keyBindings);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		IPlayer player = Player.instance();
		IWorld world = player.getWorld();

		Action action = getAction();
		if (action == null) {
			return;
		}

		switch (action) {

		case UNDO:
			try {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						undoManager.redo();
					} else {
						undoManager.undo();
					}
				}
				// When undoing a copy/move it is helpful to reselect.
				// If we had a reference to the last spell, we could do this conditionally
				// Last spell is not necessarily what is in hand
				// Comment out for now
				// selectionManager.reselect(w)
			} catch (NoSuchElementException e) {
				// TODO Log to overlay. Create info line in overlay
				System.out.println(e.getMessage());
			}
			break;

		case NEXT:
			Staff staff = player.getStaff();
			if (staff != null) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					Plato.network.sendToServer(new PrevSpellMessage());
				} else {
					Plato.network.sendToServer(new NextSpellMessage());
				}
			}
			break;

		case REINVOKE:
			lastSpell.invoke();
			break;

		case DELETE:
			new SpellDelete(undoManager, selectionManager, pickManager).invoke(world, player);
			break;

		case RESELECT:
			selectionManager.reselect(world);
			break;

		case LEFT:
			copy(player, world, -1, 0);
			break;

		case RIGHT:
			copy(player, world, 1, 0);
			break;

		case UP:
			// This should be alt so that control can be deleteOriginal
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, world, 1);
			} else {
				copy(player, world, 0, -1);
			}
			break;

		case DOWN:
			// This should be alt so that control can be deleteOriginal
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, world, -1);
			} else {
				copy(player, world, 0, 1);
			}
			break;

		default:
			return;
		}

		if (event.isCancelable())
			event.setCanceled(true);
	}

	// Private ------------------------------------------------------------------------------------

	private Action getAction() {
		for (Entry<Action, KeyBinding> s : keyBindings.entrySet()) {
			if (s.getValue().isPressed()) {
				System.out.println("action="+s.getKey());
				return s.getKey();
			}
		}
		return null;
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
		ISpell spell = new SpellCopy(undoManager, selectionManager, pickManager);
		lastSpell = new SpellInvoker(pickManager, spell, world, player);
		spell.invoke(world, player);
	}

	private void copyVertical(IPlayer player, IWorld world, int upDown) {
		pickManager.reset(2);
		pickManager.pick(world, new BlockPos(0, 0, 0), null);
		pickManager.pick(world, new BlockPos(0, upDown, 0), null);
		new SpellCopy(undoManager, selectionManager, pickManager).invoke(world, player);
	}

	private KeyBinding registerKeyBinding(String name, int key) {
		KeyBinding b = new KeyBinding(name, key, Plato.NAME);
		ClientRegistry.registerKeyBinding(b);
		return b;
	}
}
