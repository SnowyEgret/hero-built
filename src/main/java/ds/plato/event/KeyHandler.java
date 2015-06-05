package ds.plato.event;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;
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
	private Map<String, KeyBinding> keyBindings = new HashMap<>();

	public KeyHandler(IUndo undo, ISelect select, IPick pick) {
		this.undoManager = undo;
		this.selectionManager = select;
		this.pickManager = pick;
		// TODO internationalize these strings
		keyBindings.put("undo", registerKeyBinding("Undo", Keyboard.KEY_Z));
		keyBindings.put("nextSpell", registerKeyBinding("Next spell", Keyboard.KEY_TAB));
		keyBindings.put("delete", registerKeyBinding("Delete", Keyboard.KEY_DELETE));
		keyBindings.put("lastSelection", registerKeyBinding("Last selection", Keyboard.KEY_L));
		keyBindings.put("left", registerKeyBinding("Move left", Keyboard.KEY_LEFT));
		keyBindings.put("right", registerKeyBinding("Move right", Keyboard.KEY_RIGHT));
		keyBindings.put("up", registerKeyBinding("Move up", Keyboard.KEY_UP));
		keyBindings.put("down", registerKeyBinding("Move down", Keyboard.KEY_DOWN));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		IPlayer player = Player.instance();
		IWorld world = player.getWorld();

		if (keyBindings.get("undo").isPressed()) {
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
		}

		if (keyBindings.get("nextSpell").isPressed()) {
			Staff staff = player.getStaff();
			if (staff != null) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					Plato.network.sendToServer(new PrevSpellMessage());
				} else {
					Plato.network.sendToServer(new NextSpellMessage());
				}
			}
		}

		if (keyBindings.get("delete").isPressed()) {
			new SpellDelete(undoManager, selectionManager, pickManager).invoke(world, null);
		}

		if (keyBindings.get("lastSelection").isPressed()) {
			selectionManager.reselect(world);
		}

		if (keyBindings.get("left").isPressed()) {
			copy(player, world, -1, 0);
		}

		if (keyBindings.get("right").isPressed()) {
			copy(player, world, 1, 0);
		}

		if (keyBindings.get("up").isPressed()) {
			// This should be alt so that control can be deleteOriginal
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, world, 1);
			} else {
				copy(player, world, 0, -1);
			}
		}

		if (keyBindings.get("down").isPressed()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, world, -1);
			} else {
				copy(player, world, 0, 1);
			}
		}

		if (event.isCancelable())
			event.setCanceled(true);
	}

	// Private ------------------------------------------------------------------------------------

	private void copy(IPlayer player, IWorld world, int leftRight, int upDown) {
		pickManager.reset(2);
		// Same old problem. reset is clearing the picks, but the state is not cleared yet in MP
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
		if (selectionManager.size() != 0) {
			// FIXME not reselecting in MP
			new SpellCopy(undoManager, selectionManager, pickManager).invoke(world, player);
		}
		pickManager.clearPicks(world);
	}

	private void copyVertical(IPlayer player, IWorld world, int upDown) {
		pickManager.reset(2);
		pickManager.pick(world, new BlockPos(0, 0, 0), null);
		pickManager.pick(world, new BlockPos(0, upDown, 0), null);
		// FIXME not reselecting in MP
		new SpellCopy(undoManager, selectionManager, pickManager).invoke(world, player);
		pickManager.clearPicks(world);
	}

	private KeyBinding registerKeyBinding(String name, int key) {
		KeyBinding b = new KeyBinding(name, key, Plato.NAME);
		ClientRegistry.registerKeyBinding(b);
		return b;
	}
}
