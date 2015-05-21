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
		keyBindings.put("redo", registerKeyBinding("Redo", Keyboard.KEY_Y));
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
				//When undoing a copy/move it is helpful to reselect.
				//If we had a last spell, we could do this conditionally
				//Last spell is not necessarily what is in hand
				//Comment out for now
				//selectionManager.reselect(w)
			} catch (NoSuchElementException e) {
				// TODO Log to overlay. Create info line in overlay
				System.out.println(e.getMessage());
			}
		}

		if (keyBindings.get("redo").isPressed()) {
			try {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					undoManager.redo();
				}
			} catch (NoSuchElementException e) {
				// TODO Log to overlay. Create info line in overlay
				System.out.println(e.getMessage());
			}
		}

		if (keyBindings.get("nextSpell").isPressed()) {
			//FIXME Is the stack tag only being updated on client side?
			//ItemStack stack = player.getHeldItemStack();
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
			ItemStack stack = p.getCurrentEquippedItem();
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
			Plato.network.sendToServer(new NextSpellMessage());
		}

		if (keyBindings.get("delete").isPressed()) {
			new SpellDelete(undoManager, selectionManager, pickManager).invoke(w, player.getHotbar());
		}

		if (keyBindings.get("lastSelection").isPressed()) {
			selectionManager.reselect(w);
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
	
	//Private ------------------------------------------------------------------------------------

	private void copy(IPlayer player, IWorld w, int leftRight, int upDown) {
		pickManager.clearPicks();
		pickManager.reset(2);
		pickManager.pick(w, new BlockPos(0,0,0), null);
		switch (player.getDirection()) {
		case NORTH:
			pickManager.pick(w, new BlockPos(leftRight,0,upDown), null);
			break;
		case SOUTH:
			pickManager.pick(w, new BlockPos(-leftRight,0,-upDown), null);
			break;
		case EAST:
			pickManager.pick(w, new BlockPos(-upDown,0,leftRight), null);
			break;
		case WEST:
			pickManager.pick(w, new BlockPos(upDown,0,-leftRight), null);
			break;
		}
		if (selectionManager.size() != 0) {
			new SpellCopy(undoManager, selectionManager, pickManager).invoke(w, player.getHotbar());
		}
		pickManager.clearPicks();
	}

	private void copyVertical(IPlayer player, IWorld w, int upDown) {
		pickManager.clearPicks();
		pickManager.reset(2);
		pickManager.pick(w, new BlockPos(0,0,0), null);
		pickManager.pick(w, new BlockPos(0,upDown,0), null);
		new SpellCopy(undoManager, selectionManager, pickManager).invoke(w, player.getHotbar());
		pickManager.clearPicks();
	}

	private KeyBinding registerKeyBinding(String name, int key) {
		KeyBinding b = new KeyBinding(name, key, Plato.NAME);
		ClientRegistry.registerKeyBinding(b);
		return b;
	}
}
