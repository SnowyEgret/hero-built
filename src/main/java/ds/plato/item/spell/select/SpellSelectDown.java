package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectDown extends AbstractSpellSelect {

	public SpellSelectDown(IUndo undo, ISelect select, IPick pick) {
		super(Select.down, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
}
