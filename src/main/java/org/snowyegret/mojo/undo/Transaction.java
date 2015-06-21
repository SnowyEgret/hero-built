package org.snowyegret.mojo.undo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.IPlantable;

import org.snowyegret.mojo.item.spell.transform.AbstractSpellTransform;
import org.snowyegret.mojo.player.IPlayer;

import com.google.common.collect.Lists;

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
		List<BlockPos> reselects = Lists.newArrayList();
		// TODO undoables should be of type UndoableSetBlock. Parameterize Transaction constructor.
		// Transaction t = new Transaction<UndoableSetBlock>();
		// for (UndoableSetBlock u : undoables) {
		for (IUndoable u : undoables) {
			UndoableSetBlock setBlock = (UndoableSetBlock) u;
			BlockPos pos = setBlock.pos;

			// Set plantables on block above.
			// Do not set if the block below cannot sustain a plant
			Block plantable = setBlock.state.getBlock();
			if (plantable instanceof IPlantable) {
				Block b = player.getWorld().getBlock(pos);
				if (!b.canSustainPlant(player.getWorld().getWorld(), pos, EnumFacing.UP, (IPlantable) plantable)) {
					continue;
				}
				pos = pos.up();
				setBlock.pos = pos;
			}

			// Do not set blocks on top of player.
			// Player is expected to break his way out.
			// Because blocks are selected, when the player deselects the broken blocks will reappear.
			if (player.getBounds().contains(pos)) {
				continue;
			}

			reselects.add(pos);
			u.dO(player);
		}
		// FIXME not rendering properly for transform spells
		// Temporary fix.
		if (!(player.getSpell() instanceof AbstractSpellTransform)) {
			player.getSelectionManager().select(player, reselects);
		}

		// TODO So far, this is doing nothing
		// player.getWorld().update();
		// Ernio's suggestion in my post:
		// http://www.minecraftforge.net/forum/index.php/topic,30991
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