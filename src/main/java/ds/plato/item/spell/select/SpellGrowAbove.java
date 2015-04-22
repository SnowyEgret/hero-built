package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellGrowAbove extends AbstractSpellSelect {

	public SpellGrowAbove(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.ABOVE, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
