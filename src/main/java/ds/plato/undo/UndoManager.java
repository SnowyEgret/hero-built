package ds.plato.undo;

import java.util.NoSuchElementException;

import ds.plato.player.IPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class UndoManager implements IUndo {

	private Node currentNode;
	private int maxLength = 0;
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
		if (size() > maxLength) {
			removeLeftEnd();
		}
		System.out.println("size=" + size());
	}

	// Private ---------------------------------------------------------
	
	public int size() {
		// return size;
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

//	@Override
//	// FIXME
//	public int indexOf(IUndoable undoable) {
//		Node n = currentNode;
//		//Move to left end
//		while (n.left != null) {
//			n = n.left;
//		}
//		int index = 1;
//		if (n.undoable == undoable) {
//			return index;
//		}
//		while (n.right != null) {
//			n = n.right;
//			index++;
//			if (n.undoable == undoable) {
//				System.out.println("Found undoable=" + undoable);
//				return index;
//			}
//		}
//		System.out.println("Could not find undoable=" + undoable);
//		return -1;
//	}

	@Override
	public void clear() {
		currentNode = new Node();
	}

	// Interface IUndoable -------------------------------------

	@Override
	public IUndoable dO(IPlayer player) {
		return null;
	}

	@Override
	public void undo(IPlayer player) throws NoSuchElementException {
		System.out.println("size=" + size());
		if (currentNode.left == null) {
			throw new NoSuchElementException("Nothing left to undo.");
		}
		currentNode.transaction.undo(player);
		currentNode = currentNode.left;
	}

	@Override
	public void redo(IPlayer player) throws NoSuchElementException {
		System.out.println("size=" + size());
		if (currentNode.right == null) {
			throw new NoSuchElementException("Nothing left to redo.");
		}
		currentNode = currentNode.right;
		currentNode.transaction.redo(player);
	}

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
		private Transaction transaction;

		public Node(Transaction transaction) {
			this.transaction = transaction;
		}

		public Node() {
			transaction = null;
		}
		
		public void finalize() { 
			transaction.clearCache();
		}
	}
}
