package ds.plato.undo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import ds.plato.world.IWorld;

public class Transaction implements IUndoable, Iterable {

	public static final int MAX_SIZE = 9999;
	//protected Iterable<IUndoable> undoables = new HashSet<>();
	protected List<IUndoable> undoables = Lists.newArrayList();
	private final IUndo undoManager;
	private IWorld world;

	protected Transaction(IUndo undoManager) {
		this.world = null;
		this.undoManager = undoManager;
	}

	public Transaction(IWorld world) {
		this.world = world;
		undoManager = null;
	}

	public void add(IUndoable undoable) {
		undoables.add(undoable);
	}

	public void addAll(Iterable<IUndoable> setBlocks) {
		for (IUndoable u : setBlocks) {
			this.undoables.add(u);
		}
	}

	@Override
	public IUndoable dO() {
		return this;
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
		// TODO
		// Ernio's suggestion in my post: http://www.minecraftforge.net/forum/index.php/topic,30991
		// if (transaction.getSize() > Transaction.MAX_SIZE) {
		// CompressedStreamTools.writeCompressed(undoable.tooNBT(), new FileOutputStream(f));
		// }
		// Read it like this:
		// NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(f));
		// Filename has position of node in it so that it is rewritten
		if (undoManager != null) {
			undoManager.addTransaction(this);
		}
		for (IUndoable u : undoables) {
			u.dO();
		}
		if (world != null) {
			world.updateClient();
		}
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