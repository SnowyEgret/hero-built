package ds.plato.undo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ds.plato.select.Selection;

public class Transaction implements IUndoable, Iterable {

	public static final int MAX_SIZE = 9999;
	protected Set<IUndoable> undoables = new HashSet<>();
	private final IUndo undoManager;

	public Transaction(IUndo undoManager) {
		this.undoManager = undoManager;
	}

	public void add(IUndoable undoable) {
		undoables.add(undoable);
	}

	@Override
	public void undo() {
		for (IUndoable undoable : undoables) {
			undoable.undo();
		}
	}

	@Override
	public void redo() {
		for (IUndoable undoable : undoables) {
			undoable.redo();
		}
	}

	@Override
	public Iterator iterator() {
		return undoables.iterator();
	}

	public boolean isEmpty() {
		return (undoables.size() == 0);
	}

	public void commit() {
		undoManager.addUndoable(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [undoManager=");
		builder.append(idOf(undoManager));
		builder.append(", undoables=");
		builder.append(undoables);
		builder.append("]");
		return builder.toString();
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}
}