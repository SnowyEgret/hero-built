package ds.plato.item.spell;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

import ds.plato.Plato;

public enum Action {

	// TODO hardcode these
	UNDO(Keyboard.KEY_Z),
	NEXT(Keyboard.KEY_TAB),
	DELETE(Keyboard.KEY_DELETE),
	//DELETE(221),
	RESELECT(Keyboard.KEY_L),
	LEFT(Keyboard.KEY_LEFT),
	RIGHT(Keyboard.KEY_RIGHT),
	UP(Keyboard.KEY_UP),
	DOWN(Keyboard.KEY_DOWN),
	REINVOKE(Keyboard.KEY_PERIOD);

	public int keyCode;

	Action(int keyCode) {
		this.keyCode = keyCode;
	}

	public static Action fromKeyCode(int keyCode) {
		for (Action a : values()) {
			if (a.keyCode == keyCode) {
				return a;
			}
		}
		return null;
	}
}
