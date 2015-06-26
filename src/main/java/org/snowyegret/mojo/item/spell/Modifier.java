package org.snowyegret.mojo.item.spell;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public enum Modifier {

	// Use ints to avoid Keyboard class on server side
	// http://www.minecraftforge.net/forum/index.php/topic,31059.msg161734.html#msg161734

	// http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
	// SHIFT(16),
	// CTRL(17),
	// ALT(18),
	// X(88),
	// Y(89),
	// Z(90),

	// This is what I was getting from printlns
	SHIFT(42),
	CTRL(29),
	ALT(56),
	X(45),
	Y(21),
	Z(44),
	SPACE(57);

	public int keyCode;
	public int keyCodeRight = -1;

	private Modifier(int keyCode) {
		this.keyCode = keyCode;
	}

	// No KeyInputEvent for right shift, ctl, alt
	private Modifier(int keyCode, int keyCodeRight) {
		this.keyCode = keyCode;
		this.keyCodeRight = keyCodeRight;
	}

	public static Modifier fromKeyCode(int keyCode) {
		for (Modifier m : values()) {
			if (m.keyCode == keyCode) {
				return m;
			}
		}
		return null;
	}

	// Convenience method for client side only
	@SideOnly(Side.CLIENT)
	public boolean isPressed() {
		return Keyboard.isKeyDown(this.keyCode);
	}
}
