package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellGrowBellow extends AbstractSpellSelect {

	public SpellGrowBellow(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.BELLOW, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "   ", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
