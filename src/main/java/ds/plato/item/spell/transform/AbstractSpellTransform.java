package ds.plato.item.spell.transform;

import ds.plato.item.spell.Spell;
import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public abstract class AbstractSpellTransform extends Spell {

	public AbstractSpellTransform(IUndo undo, ISelect select, IPick pick) {
		super(1, undo, select, pick);
	}

	protected void transformSelections(IWorld world, ITransform transformer) {
		//System.out.println("world="+world);
		if (selectionManager.getSelectionList().size() != 0) {
			Transaction t = undoManager.newTransaction();
			for (Selection s : selectionManager.getSelections()) {
				s = transformer.transform(s);
				t.add(new UndoableSetBlock(world, selectionManager, s.getPos(), s.getState()).set());
			}
			t.commit();
			//Clear the selections because BlockSelected is still rendering with old state
			//Player can reselect last
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
