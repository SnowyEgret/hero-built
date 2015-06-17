package ds.plato.item.spell;

import org.lwjgl.input.Keyboard;

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
	CUT(Keyboard.KEY_X),
	COPY(Keyboard.KEY_C),
	PASTE(Keyboard.KEY_V),
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
