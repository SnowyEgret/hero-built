package ds.plato.api;

import ds.plato.undo.Transaction;

public interface IUndo extends IUndoable {

	public Transaction newTransaction();

	public void addUndoable(IUndoable undoable);

}
