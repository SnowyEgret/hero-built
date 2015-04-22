package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellGrowDown extends AbstractSpellSelect {

	public SpellGrowDown(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.DOWN, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", " A ", " B ", 'A', ingredientA, 'B', ingredientB };
	}
}
