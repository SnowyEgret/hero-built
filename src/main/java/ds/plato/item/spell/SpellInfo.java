package ds.plato.item.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.I18n;

import com.google.common.base.Joiner;

import ds.plato.api.ISpell;
import ds.plato.util.StringUtils;

public class SpellInfo {

	private String name;
	private String description;
	private String root;
	private List<String> picks = new ArrayList<>();
	private Map<String, String> modifiers = new HashMap<>();

	public SpellInfo(ISpell spell) {
		root = "item." + StringUtils.toCamelCase(spell.getClass()) + ".";
		name = format("name");
		description = format("description");
		addPicks(spell.getNumPicks());
	}

	private String format(String string) {
		return I18n.format(root + string);
	}

	private void addPicks(int numPicks) {
		for (int i = 0; i < numPicks; i++) {
			String s = format("pick." + picks.size());
			//To avoid repeating [].pick.0=anywhere in the lang file it can be left out
			//
			if (s.startsWith("item.")) {
				s = I18n.format("pick.anywhere");
			}
			picks.add(s);
		}
	}

	public void addModifiers(Modifier... modifiers) {
		for (Modifier modifier : modifiers) {
			switch (modifier) {
			case CTRL:
				this.modifiers.put("ctrl", format("modifier.ctrl"));
				break;
			case ALT:
				this.modifiers.put("alt", format("modifier.alt"));
				break;
			case SHIFT:
				this.modifiers.put("shift", format("modifier.shift"));
				break;
			case X:
				this.modifiers.put("x", format("modifier.x"));
				break;
			case Y:
				this.modifiers.put("y", format("modifier.y"));
				break;
			case Z:
				this.modifiers.put("z", format("modifier.z"));
				break;
			default:
				break;
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPicks() {
		List l = new ArrayList();
		int i = 0;
		for (String p : picks) {
			l.add(String.format("<pick%d> %s", ++i, p));
		}
		return Joiner.on(", ").join(l);
	}

	public String getModifiers() {
		List<String> l = new ArrayList();
		for (Map.Entry p : modifiers.entrySet()) {
			l.add(String.format("<%s>", p.getKey()) + " " + p.getValue());
		}
		return Joiner.on(", ").join(l);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SpellInfo [name=");
		builder.append(getName());
		builder.append(", description=");
		builder.append(getDescription());
		builder.append(", picks=");
		builder.append(getPicks());
		builder.append(", modifiers=");
		builder.append(getModifiers());
		builder.append("]");
		return builder.toString();
	}

}
