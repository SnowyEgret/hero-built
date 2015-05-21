package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectUp extends AbstractSpellSelect {

	public SpellSelectUp(IUndo undo, ISelect select, IPick pick) {
		super(Select.up, undo, select, pick);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { " B ", " A ", "   ", 'A', ingredientA, 'B', ingredientB };
	}

}
