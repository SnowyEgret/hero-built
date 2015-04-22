package ds.plato.item.spell.select;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;

public class SpellGrowAll extends AbstractSpellSelect {

	public SpellGrowAll(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		super(Shell.Type.XYZ, undoManager, selectionManager, pickManager);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}

}
