package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectBellow extends AbstractSpellSelect {

	public SpellSelectBellow(IUndo undo, ISelect select, IPick pick) {
		super(Select.belowInclusive, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
