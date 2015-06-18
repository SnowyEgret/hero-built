package ds.plato.undo;

import java.util.NoSuchElementException;

import net.minecraft.nbt.NBTTagCompound;

public class UndoManager implements IUndo {

	private Node currentNode;
	private int maxLength = 0;
	private int size = 0;
	final static int DEFAULT_MAX_LENGTH = 10;

	public UndoManager(int maxLength) {
		currentNode = new Node();
		this.maxLength = maxLength;
	}

	public UndoManager() {
		this(DEFAULT_MAX_LENGTH);
	}

	// Interface IUndo -------------------------------------

	@Override
	public void addTransaction(Transaction transaction) {
		Node node = new Node(transaction);
		currentNode.right = node;
		node.left = currentNode;
		currentNode = node;
		if (size > maxLength) {
			removeLeftEnd();
		} else {
			size++;
		}
	}

	@Override
	public int indexOf(IUndoable node) {
		int index = 0;
		Node n = currentNode;
		while (n.left != null) {
			n = n.left;
		}
		while (n.right != null) {
			if (n.equals(node)) {
				return index;
			}
			n = n.right;
			index++;
		}
		return index;
	}
	

	@Override
	public void clear() {
		currentNode = new Node();
	}

	// Interface IUndoable -------------------------------------

	@Override
	public IUndoable dO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo() throws NoSuchElementException {
		if (currentNode.left == null) {
			throw new NoSuchElementException("Nothing left to undo.");
		}
		currentNode.undoable.undo();
		currentNode = currentNode.left;
	}

	@Override
	public void redo() throws NoSuchElementException {
		if (currentNode.right == null) {
			throw new NoSuchElementException("Nothing left to redo.");
		}
		currentNode = currentNode.right;
		currentNode.undoable.redo();
	}
	
	// Default for testing -----------------------------------------------------------

	// int size() {
	// return size;
	// // int size = 0;
	// // Node n = currentNode;
	// // while (n.right != null) {
	// // n = n.right;
	// // size++;
	// // }
	// // n = currentNode;
	// // while (n.left != null) {
	// // n = n.left;
	// // size++;
	// // }
	// // return size;
	// }

	@Override
	public NBTTagCompound toNBT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}

	// Private ---------------------------------------------------------

	private void removeLeftEnd() {
		Node n = currentNode;
		while (n.left != null) {
			n = n.left;
		}
		n = n.right;
		n.left = null;
	}

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
