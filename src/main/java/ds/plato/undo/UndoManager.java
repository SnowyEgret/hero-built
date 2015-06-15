package ds.plato.undo;

import java.util.NoSuchElementException;

public class UndoManager implements IUndo {

	private Node currentNode;
	int maxLength = 0;
	final static int DEFAULT_MAX_LENGTH = 10;

	public UndoManager(int maxLength) {
		currentNode = new Node();
		this.maxLength = maxLength;
	}

	public UndoManager() {
		this(DEFAULT_MAX_LENGTH);
	}

	// Interface IUndo -------------------------------------

	public void addTransaction(Transaction transaction) {
		// TODO
		// Ernio's suggestion in my post: http://www.minecraftforge.net/forum/index.php/topic,30991
		// if (transaction.getSize() > Transaction.MAX_SIZE) {
		// CompressedStreamTools.writeCompressed(undoable.tooNBT(), new FileOutputStream(f));
		// }
		// Read it like this:
		// NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(f));
		// Filename has position of node in it so that it is rewritten
		Node node = new Node(transaction);
		currentNode.right = node;
		node.left = currentNode;
		currentNode = node;
		if (size() > maxLength)
			removeLeftEnd();
	}

	public Transaction newTransaction() {
		return new Transaction(this);
	}

	// Interface IUndoable -------------------------------------

	public void undo() throws NoSuchElementException {
		if (currentNode.left == null) {
			throw new NoSuchElementException("Nothing left to undo.");
		}
		currentNode.undoable.undo();
		currentNode = currentNode.left;
	}

	public void redo() throws NoSuchElementException {
		if (currentNode.right == null)
			throw new NoSuchElementException("Nothing left to redo.");
		currentNode = currentNode.right;
		currentNode.undoable.redo();
	}

	// Default for testing -----------------------------------------------------------

	int size() {
		int size = 0;
		Node n = currentNode;
		while (n.right != null) {
			n = n.right;
			size++;
		}
		n = currentNode;
		while (n.left != null) {
			n = n.left;
			size++;
		}
		return size;
	}

	void clear() {
		currentNode = new Node();
	}

	void removeLeftEnd() {
		Node n = currentNode;
		while (n.left != null) {
			n = n.left;
		}
		n = n.right;
		n.left = null;
	}

	// Private ---------------------------------------------------------

	private class Node {

		private Node left = null;
		private Node right = null;
		private final IUndoable undoable;

		public Node(IUndoable undoable) {
			this.undoable = undoable;
		}

		public Node() {
			undoable = null;
		}
	}
}
