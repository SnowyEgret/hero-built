package org.snowyegret.mojo.item.staff;

import java.util.List;

import net.minecraft.init.Items;

import org.snowyegret.mojo.item.spell.Spell;

public class StaffTransform extends StaffPreset {

	public StaffTransform(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
