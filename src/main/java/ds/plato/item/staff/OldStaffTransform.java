package ds.plato.item.staff;

import ds.plato.api.IPick;
import ds.plato.item.spell.Spell;
import ds.plato.item.spell.matrix.AbstractSpellMatrix;
import ds.plato.item.spell.transform.AbstractSpellTransform;

@Deprecated
public class OldStaffTransform extends OldStaff {

	public OldStaffTransform(IPick pickManager) {
		super(pickManager);
	}

	@Override
	public void addSpell(Spell spell) {
		assert (spell instanceof AbstractSpellTransform || spell instanceof AbstractSpellMatrix);
		super.addSpell(spell);
	}

	@Override
	public boolean hasRecipe() {
		return false;
	}
}
