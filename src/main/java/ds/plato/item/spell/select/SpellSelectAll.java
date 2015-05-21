package ds.plato.item.spell.select;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public class SpellSelectAll extends AbstractSpellSelect {

	public SpellSelectAll(IUndo undoManager, ISelect selectionManager, IPick pickManager) {
		//super(Shell.Type.XYZ, undoManager, selectionManager, pickManager);
		super(Select.all, undoManager, selectionManager, pickManager);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { "BBB", "BAB", "BBB", 'A', ingredientA, 'B', ingredientB };
	}
}
