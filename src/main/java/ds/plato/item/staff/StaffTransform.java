package ds.plato.item.staff;

import java.util.List;

import net.minecraft.init.Items;
import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;

public class StaffTransform extends StaffPreset {

	public StaffTransform(IPick pickManager, List<Spell> spells) {
		super(pickManager, spells);
	}
	
	public Object[] getRecipe() {
		return new Object[] { "#  ", " # ", "  #", '#', Items.bone };
	}
}
