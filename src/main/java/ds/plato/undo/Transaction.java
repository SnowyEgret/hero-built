package ds.plato.undo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.Lists;

import ds.plato.player.IPlayer;
import ds.plato.world.IWorld;

public class Transaction implements IUndoable, Iterable {

	public static final int MAX_SIZE = 9999;
	protected List<IUndoable> undoables = Lists.newArrayList();
	private final IUndo undoManager;
	private IWorld world;
	private boolean isCached = false;
	private String filename;

	public Transaction(IPlayer player) {
		world = player.getWorld();
		undoManager = player.getUndoManager();
	}

	public void add(IUndoable undoable) {
		undoables.add(undoable);
	}

	public void addAll(Iterable<IUndoable> setBlocks) {
		for (IUndoable u : setBlocks) {
			add(u);
		}
	}

	public List<IUndoable> getTransactions() {
		return undoables;
	}

	public boolean isEmpty() {
		return (undoables.size() == 0);
	}

	// IUndoable-----------------------------------------------------------------

	@Override
	public IUndoable dO() {
		commit();
		return this;
	}

	public void commit() {
		undoManager.addTransaction(this);
		for (IUndoable u : undoables) {
			u.dO();
		}
		if (world != null) {
			world.updateClient();
		}
		// Ernio's suggestion in my post: http://www.minecraftforge.net/forum/index.php/topic,30991
		if (undoables.size() > MAX_SIZE) {
			cacheUndoables();
		}
	}

	@Override
	public void undo() {
		if (isCached) {
			unCacheUndoables();
		}
		for (IUndoable undoable : undoables) {
			undoable.undo();
		}
	}

	@Override
	public void redo() {
		if (isCached) {
			unCacheUndoables();
		}
		for (IUndoable undoable : undoables) {
			undoable.redo();
		}
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		for (IUndoable u : undoables) {
			// UndoableSetBlock implements toNBT();
			tag.setTag("u", u.toNBT());
			u.toNBT();
		}
		return tag;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound nbt) {
		return null;
	}

	// Iterable------------------------------------------------------------------

	@Override
	public Iterator iterator() {
		return undoables.iterator();
	}

	// Private------------------------------------------------------------------------

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [undoables=");
		builder.append(undoables);
		builder.append(", undoManager=");
		builder.append(undoManager);
		builder.append(", world=");
		builder.append(world);
		builder.append(", isCached=");
		builder.append(isCached);
		builder.append(", filename=");
		builder.append(filename);
		builder.append("]");
		return builder.toString();
	}

	private void cacheUndoables() {
		filename = "undo" + undoManager.indexOf(this) + ".foo";
		try {
			CompressedStreamTools.writeCompressed(toNBT(), new FileOutputStream(filename));
			isCached = true;
			undoables = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void unCacheUndoables() {
		try {
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(filename));
			Transaction t = (Transaction) fromNBT(nbt);
			undoables = t.getTransactions();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}