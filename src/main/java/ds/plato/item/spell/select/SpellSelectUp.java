package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellSelectUp extends AbstractSpellSelect {

	public SpellSelectUp(IUndo undo, ISelect select, IPick pick) {
		super(Shell.Type.UP, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
