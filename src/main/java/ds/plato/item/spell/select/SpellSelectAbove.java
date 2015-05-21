package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectAbove extends AbstractSpellSelect {

	public SpellSelectAbove(IUndo undo, ISelect select, IPick pick) {
		super(Select.aboveInclusive, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
