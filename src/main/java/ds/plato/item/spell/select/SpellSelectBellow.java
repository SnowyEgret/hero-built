package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow(IUndo undo, ISelect select, IPick pick) {
		super(Select.belowInclusive, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
