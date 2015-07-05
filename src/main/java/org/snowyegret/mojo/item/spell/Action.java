package org.snowyegret.mojo.item.spell;

public enum Action {

	// UNDO(Keyboard.KEY_Z),
	// NEXT(Keyboard.KEY_TAB),
	// DELETE(Keyboard.KEY_DELETE),
	// RESELECT(Keyboard.KEY_L),
	// LEFT(Keyboard.KEY_LEFT),
	// RIGHT(Keyboard.KEY_RIGHT),
	// UP(Keyboard.KEY_UP),
	// DOWN(Keyboard.KEY_DOWN),
	// CUT(Keyboard.KEY_X),
	// COPY(Keyboard.KEY_C),
	// PASTE(Keyboard.KEY_V);

	UNDO(44),
	NEXT(15),
	DELETE(211),
	RESELECT(38),
	LEFT(203),
	RIGHT(205),
	UP(200),
	DOWN(208),
	CUT(45),
	COPY(46),
	PASTE(47);

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
