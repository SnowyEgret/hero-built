package ds.plato.undo;

public interface IUndo extends IUndoable {

	public Transaction newTransaction();

	public void addTransaction(Transaction transaction);

}
