package org.snowyegret.mojo.item.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.snowyegret.mojo.util.StringUtils;

import com.google.common.base.Joiner;

public class SpellInfo {

	private String root;
	private String name;
	private String description;
	private List<String> picks = new ArrayList<>();
	private Map<String, String> modifiers = new HashMap<>();

	public SpellInfo(Spell spell) {
		//root = "item." + StringUtils.nameFor(spell.getClass()) + ".";
		root = "item." + StringUtils.underscoreNameFor(spell.getClass()) + ".";
		name = format("name");
		description = format("description");
		addPicks(spell.getNumPicks());
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
			case SPACE:
				this.modifiers.put("space", format("modifier.space"));
				break;
			default:
				break;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public String getName() {
		return net.minecraft.client.resources.I18n.format(name);
	}

	@SideOnly(Side.CLIENT)
	public String getDescription() {
		return net.minecraft.client.resources.I18n.format(description);
	}

	@SideOnly(Side.CLIENT)
	public String getPicks() {
		List l = new ArrayList();
		int i = 0;
		for (String p : picks) {
			String localizedString = net.minecraft.client.resources.I18n.format(p);
			// Do not display a pick with an empty string in overlay #228
			// Not sure this is an improvement
			// if (!localizedString.isEmpty()) {
			l.add(String.format("<pick%d> %s", ++i, localizedString));
			// }
		}
		return Joiner.on(", ").join(l);
	}

	@SideOnly(Side.CLIENT)
	public String getModifiers() {
		List<String> l = new ArrayList();
		for (Map.Entry<String, String> m : modifiers.entrySet()) {
			String localizedString = net.minecraft.client.resources.I18n.format(m.getValue());
			if (!localizedString.isEmpty()) {
				l.add(String.format("<%s>", m.getKey()) + " " + localizedString);
			}
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

	private String format(String string) {
		return root + string;
	}

	private void addPicks(int numPicks) {
		for (int i = 0; i < numPicks; i++) {
			String s = format("pick." + picks.size());
			picks.add(s);
		}
	}

}
