package ds.plato.item.spell;

import org.lwjgl.input.Keyboard;

public enum Modifier {
	CTRL(Keyboard.KEY_LCONTROL),
	ALT(Keyboard.KEY_LMENU),
	SHIFT(Keyboard.KEY_LSHIFT),
	X(Keyboard.KEY_X),
	Y(Keyboard.KEY_Y),
	Z(Keyboard.KEY_Z),
	SPACE(Keyboard.KEY_SPACE);
	
	public int key;

	private Modifier(int key) {
		this.key = key;
	}

	public static boolean isPressed(Modifier modifier) {
		return Keyboard.isKeyDown(modifier.key);
	}

	public static Modifier fromKey(int key) {
		for (Modifier m : values()) {
			if (m.key == key) {
				return m;
			}
		}
		return null;
	}
}
