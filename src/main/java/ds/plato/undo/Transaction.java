package ds.plato.undo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

import ds.plato.player.IPlayer;
import ds.plato.player.Jumper;
import ds.plato.select.ISelect;

public class Transaction implements IUndoable, Iterable {

	public static final int MAX_SIZE = 99;
	private static final String SIZE_KEY = "s";
	protected List<IUndoable> undoables = Lists.newArrayList();
	private boolean isCached = false;
	private File cacheFile;

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

	public void deleteCache() {
		// System.out.println("path=" + Paths.get(filename));
		cacheFile.delete();
	}

	public boolean isEmpty() {
		return (undoables.size() == 0);
	}

	// IUndoable-----------------------------------------------------------------

	@Override
	public IUndoable dO(IPlayer player) {
		player.getUndoManager().addTransaction(this);
		//Jumper jumper = new Jumper(player);
		List<BlockPos> reselects = Lists.newArrayList();
		for (IUndoable u : undoables) {
			BlockPos pos = ((UndoableSetBlock) u).pos;
			if (!player.getBounds().contains(pos)) {
				u.dO(player);
				reselects.add(pos);
			}
			//jumper.setHeight(pos);
		}
		//jumper.jump();
		player.getSelectionManager().select(player, reselects);

		// TODO So far, this is doing nothing
		player.getWorld().update();
		// Ernio's suggestion in my post: http://www.minecraftforge.net/forum/index.php/topic,30991
		if (undoables.size() > MAX_SIZE) {
			cacheUndoables();
		}

		// String sound = "plato:" + StringUtils.toCamelCase(getClass());
		// TODO how to look up sound from state
		String sound = "ambient.weather.thunder";
		// Block b;
		// player.playSoundAtPlayer(sound);
		return this;
	}

	@Override
	public void undo(IPlayer player) {
		if (isCached) {
			unCacheUndoables();
		}
		for (IUndoable undoable : undoables) {
			undoable.undo(player);
		}
	}

	@Override
	public void redo(IPlayer player) {
		if (isCached) {
			unCacheUndoables();
		}
		for (IUndoable undoable : undoables) {
			undoable.redo(player);
		}
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(SIZE_KEY, undoables.size());
		int i = 0;
		for (IUndoable u : undoables) {
			// UndoableSetBlock implements toNBT();
			tag.setTag(String.valueOf(i), u.toNBT());
			i++;
		}
		return tag;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound tag) {
		int size = tag.getInteger(SIZE_KEY);
		for (int i = 0; i < size; i++) {
			NBTTagCompound t = tag.getCompoundTag(String.valueOf(i));
			// Can this be generic? So far we only have one type of IUndoable
			IUndoable u = new UndoableSetBlock();
			undoables = Lists.newArrayList();
			undoables.add(u.fromNBT(t));
		}
		return this;
	}

	// Iterable------------------------------------------------------------------

	@Override
	public Iterator iterator() {
		return undoables.iterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [undoables=");
		builder.append(undoables);
		builder.append(", isCached=");
		builder.append(isCached);
		builder.append("]");
		return builder.toString();
	}

	// Private------------------------------------------------------------------------

	private void cacheUndoables() {
		try {
			cacheFile = File.createTempFile(Integer.toHexString(hashCode()), ".undo");
			cacheFile.deleteOnExit();
			CompressedStreamTools.writeCompressed(toNBT(), new FileOutputStream(cacheFile));
			isCached = true;
			undoables = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void unCacheUndoables() {
		try {
			NBTTagCompound tag = CompressedStreamTools.readCompressed(new FileInputStream(cacheFile));
			System.out.println("tag=" + tag);
			fromNBT(tag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}