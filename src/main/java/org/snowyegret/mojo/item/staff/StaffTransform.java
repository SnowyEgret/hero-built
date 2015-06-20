package org.snowyegret.mojo.item.staff;

import java.util.List;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.pick.IPick;

import net.minecraft.init.Items;

public class StaffTransform extends StaffPreset {

	public StaffTransform(List<Spell> spells) {
		super(spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
