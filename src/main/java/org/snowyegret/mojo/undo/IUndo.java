package org.snowyegret.mojo.undo;


public interface IUndo extends IUndoable {

	//public Transaction newTransaction();

	public void addTransaction(Transaction transaction);

	public void clear();
	
	public int size();

	//int indexOf(IUndoable node);

}
