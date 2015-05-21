package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectHorizontal extends AbstractSpellSelect {

	public SpellSelectHorizontal(IUndo undo, ISelect select, IPick pick) {
		super(Select.horizontal, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}
}
