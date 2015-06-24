package org.snowyegret.mojo.undo;

import java.util.NoSuchElementException;

import net.minecraft.nbt.NBTTagCompound;

import org.snowyegret.mojo.player.IPlayer;

public class TransactionManager implements IUndoable {

	private Node currentNode;
	private int maxLength = 0;
	final static int DEFAULT_MAX_LENGTH = 30;

	public TransactionManager(int maxLength) {
		currentNode = new Node();
		this.maxLength = maxLength;
	}

	public TransactionManager() {
		this(DEFAULT_MAX_LENGTH);
	}

	// Interface IUndo -------------------------------------

	public void addTransaction(Transaction transaction) {
		Node node = new Node(transaction);
		currentNode.right = node;
		node.left = currentNode;
		currentNode = node;
		if (size() > maxLength) {
			removeLeftEnd();
		}
		//System.out.println("size=" + size());
	}

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
		// System.out.println("size=" + size());
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

	// Private---------------------------------------------------------------------

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
			transaction.deleteCache();
		}
	}
}
