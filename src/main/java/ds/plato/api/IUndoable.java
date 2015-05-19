package ds.plato.api;

import java.util.NoSuchElementException;

public interface IUndoable {
	
	public void undo() throws NoSuchElementException;
	public void redo() throws NoSuchElementException;

}
