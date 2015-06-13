package ds.plato.item.spell;

import java.util.HashMap;
import java.util.Map;

public class Modifiers {
	
	Map<Modifier, Boolean> modifiers = new HashMap<>();
	
	public Modifiers() {
		for (Modifier m : Modifier.values()) {
			modifiers.put(m, false);
		}
	}
	
	public boolean isPressed(Modifier modifier) {
		return modifiers.get(modifier);
	}
	
	public void setPressed(Modifier modifier, boolean isPressed) {
		modifiers.put(modifier, isPressed);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Modifiers [modifiers=");
		builder.append(modifiers);
		builder.append("]");
		return builder.toString();
	}
	
	
}
