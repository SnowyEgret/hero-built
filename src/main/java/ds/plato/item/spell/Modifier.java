package ds.plato.item.spell;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public enum Modifier {

	// Use ints to avoid Keyboard class on server side
	// SHIFT(16),
	// CTRL(17),
	// ALT(18),
	// X(88),
	// Y(89),
	// Z(90),
	SHIFT(42),
	CTRL(29),
	ALT(56),
	X(45),
	Y(21),
	Z(44),
	SPACE(57);

	public int key;

	private Modifier(int key) {
		this.key = key;
	}

	public static Modifier fromKey(int key) {
		for (Modifier m : values()) {
			if (m.key == key) {
				return m;
			}
		}
		return null;
	}

	// Convenience method for client side only
	@SideOnly(Side.CLIENT)
	public boolean isPressed() {
		return Keyboard.isKeyDown(this.key);
	}
}
