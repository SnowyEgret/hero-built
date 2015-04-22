package ds.plato.item.staff;

import ds.plato.api.IPick;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.draw.AbstractSpellDraw;

@Deprecated
public class OldStaffDraw extends OldStaff {

	public OldStaffDraw(IPick pickManager) {
		super(pickManager);
	}

	@Override
	public void addSpell(Spell spell) {
		assert spell instanceof AbstractSpellDraw;
		super.addSpell(spell);
	}

	@Override
	public boolean hasRecipe() {
		return false;
	}
}
