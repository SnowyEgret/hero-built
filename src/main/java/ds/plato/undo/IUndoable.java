package ds.plato.undo;

import java.util.NoSuchElementException;

public interface IUndoable {
	
	public IUndoable dO();
	public void undo() throws NoSuchElementException;
	public void redo() throws NoSuchElementException;

}
