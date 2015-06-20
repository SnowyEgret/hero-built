package org.snowyegret.plato.item.staff;

import java.util.List;

import org.snowyegret.plato.item.spell.Spell;
import org.snowyegret.plato.pick.IPick;

import net.minecraft.init.Items;

public class StaffTransform extends StaffPreset {

	public StaffTransform(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
