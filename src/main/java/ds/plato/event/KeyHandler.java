package ds.plato.event;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
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
import ds.plato.api.IPick;
import ds.plato.api.IPlayer;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.core.Player;
import ds.plato.item.spell.matrix.SpellCopy;
import ds.plato.item.spell.transform.SpellDelete;
import ds.plato.item.staff.Staff;

public class KeyHandler {

	private IUndo undoManager;
	private ISelect selectionManager;
	private IPick pickManager;
	Map<String, KeyBinding> keyBindings = new HashMap<>();


	public KeyHandler(IUndo undo, ISelect select, IPick pick) {
		this.undoManager = undo;
		this.selectionManager = select;
		this.pickManager = pick;
		// TODO internationalize these strings
		keyBindings.put("undo", registerKeyBinding("Undo", Keyboard.KEY_Z));
		keyBindings.put("redo", registerKeyBinding("Redo", Keyboard.KEY_Y));
		keyBindings.put("toggle", registerKeyBinding("Toggle", Keyboard.KEY_TAB));
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

		IPlayer player = Player.getPlayer();
		IWorld w = player.getWorld();

		if (keyBindings.get("undo").isPressed()) {
			try {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						undoManager.redo();
					} else {
						undoManager.undo();
					}
				}
			} catch (Exception e) {
				// TODO Log to chat
				Plato.log.info("[KeyInputEventHandler.onKeyInput]" + e.getMessage());
			}
		}

		if (keyBindings.get("redo").isPressed()) {
			try {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					undoManager.redo();
				}
			} catch (Exception e) {
				// TODO Log to overlay. Create info line in overlay
				Plato.log.info("[KeyInputEventHandler.onKeyInput]" + e.getMessage());
			}
		}

		if (keyBindings.get("toggle").isPressed()) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (stack != null) {
				Item i = stack.getItem();
				//1.8
//				if (i instanceof OldStaff) {
//					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
//						((OldStaff) i).previousSpell();
//					} else {
//						((OldStaff) i).nextSpell();
//					}
//				}
				if (i instanceof Staff) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
						((Staff) i).previousSpell(stack);
					} else {
						((Staff) i).nextSpell(stack);
					}
				}
			}
		}

		if (keyBindings.get("delete").isPressed()) {
			new SpellDelete(undoManager, selectionManager, pickManager).invoke(w, player.getHotbarSlots());
		}

		if (keyBindings.get("lastSelection").isPressed()) {
			selectionManager.reselectLast();
		}

		if (keyBindings.get("left").isPressed()) {
			copy(player, w, -1, 0);
		}

		if (keyBindings.get("right").isPressed()) {
			copy(player, w, 1, 0);
		}

		if (keyBindings.get("up").isPressed()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, w, 1);
			} else {
				copy(player, w, 0, -1);
			}
		}

		if (keyBindings.get("down").isPressed()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				copyVertical(player, w, -1);
			} else {
				copy(player, w, 0, 1);
			}
		}

		if (event.isCancelable())
			event.setCanceled(true);
	}

	private void copy(IPlayer player, IWorld w, int lr, int ud) {
		pickManager.clearPicks();
		pickManager.reset(2);
		//pickManager.pick(w, 0, 0, 0, 0);
		pickManager.pick(w, new BlockPos(0,0,0), null);
		switch (player.getDirection()) {
		case NORTH:
			//pickManager.pick(w, lr, 0, ud, 0);
			pickManager.pick(w, new BlockPos(lr,0,ud), null);
			break;
		case SOUTH:
			//pickManager.pick(w, -lr, 0, -ud, 0);
			pickManager.pick(w, new BlockPos(-lr,0,-ud), null);
			break;
		case EAST:
			//pickManager.pick(w, -ud, 0, lr, 0);
			pickManager.pick(w, new BlockPos(-ud,0,lr), null);
			break;
		case WEST:
			//pickManager.pick(w, ud, 0, -lr, 0);
			pickManager.pick(w, new BlockPos(ud,0,-lr), null);
			break;
		}
		if (selectionManager.size() != 0) {
			new SpellCopy(undoManager, selectionManager, pickManager).invoke(w, player.getHotbarSlots());
		}
		pickManager.clearPicks();
	}

	private void copyVertical(IPlayer player, IWorld w, int d) {
		pickManager.clearPicks();
		pickManager.reset(2);
		//1.8
		pickManager.pick(w, new BlockPos(0,0,0), null);
		pickManager.pick(w, new BlockPos(0,0,0), null);
		new SpellCopy(undoManager, selectionManager, pickManager).invoke(w, player.getHotbarSlots());
		pickManager.clearPicks();
	}

	private KeyBinding registerKeyBinding(String name, int key) {
		KeyBinding b = new KeyBinding(name, key, Plato.NAME);
		ClientRegistry.registerKeyBinding(b);
		return b;
	}
}
