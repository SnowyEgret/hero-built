package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellGrowHorizontal extends AbstractSpellSelect {

	public SpellGrowHorizontal(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.HORIZONTAL, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}
}
