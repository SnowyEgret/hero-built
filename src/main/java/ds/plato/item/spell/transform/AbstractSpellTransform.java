package ds.plato.item.spell.transform;

import ds.plato.api.IPick;
import ds.plato.api.ISelect;
import ds.plato.api.IUndo;
import ds.plato.api.IWorld;
import ds.plato.item.spell.Spell;
import ds.plato.select.Selection;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.undo.Transaction;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform(IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
	}

	protected void transformSelections(IWorld world, ITransform transformer) {
		if (selectionManager.getSelectionList().size() != 0) {
			Transaction t = undoManager.newTransaction();
			for (Selection s : selectionManager.getSelections()) {
				t.add(new UndoableSetBlock(world, selectionManager, transformer.transform(s)).set());
			}
			t.commit();
			selectionManager.clearSelections(world);
		}
		pickManager.clearPicks();
	}

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
