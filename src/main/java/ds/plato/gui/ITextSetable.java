package ds.plato.gui;

import ds.plato.pick.IPick;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;

public interface ITextSetable {

	void setText(String text, ISelect selectionManager, IPick pickManager, IUndo undoManager);

}
