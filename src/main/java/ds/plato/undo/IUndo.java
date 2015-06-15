package ds.plato.undo;

public interface IUndo extends IUndoable {

	public Transaction newTransaction();

	// TODO change to addTransaction(Transaction transaction);
	public void addTransaction(Transaction transaction);

}
