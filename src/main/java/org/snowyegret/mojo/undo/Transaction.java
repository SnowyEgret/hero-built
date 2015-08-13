package org.snowyegret.mojo.undo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.item.spell.matrix.SpellCopy;
import org.snowyegret.mojo.item.spell.matrix.SpellRotate;
import org.snowyegret.mojo.player.Player;

import com.google.common.collect.Lists;

public class Transaction implements IUndoable, Iterable {

	public static final int MAX_SIZE = 9999;
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
	public boolean dO(Player player) {
		World world = player.getWorld().getWorld();
		player.getTransactionManager().addTransaction(this);
		List<BlockPos> reselects = Lists.newArrayList();

		for (IUndoable u : undoables) {
			
			// TODO call u.dO(player) and put all this in UndoableSetBlock.dO
			u.dO(player);
			
			// TODO try to find a more appropriate place for this.
			if (u instanceof UndoableSetBlock) {
				reselects.add(((UndoableSetBlock)u).pos);
			}
			
		}

		// Issue #99: Transform spells not reselecting with proper state
		// FIXME not rendering properly for transform spells
		// Temporary fix. Player can relelect with Action.LAST key
		Spell s = player.getSpell();
		// if (!(s instanceof SpellFill || s instanceof SpellFillChecker || s instanceof SpellFillRandom)) {
		// // TODO do not reselect after spellDelete or pressing delete key
		// // Can not test for SpellDelete on delete key press because player does not have DeleteSpell in hand
		// if (!(s instanceof SpellDelete)) {
		// player.getSelectionManager().select(reselects);
		// }
		// System.out.println("lastInvokedSpell=" + player.getLastInvokedSpell());
		if (s instanceof SpellCopy || player.getLastInvokedSpell() instanceof SpellCopy || s instanceof SpellRotate) {
			player.getSelectionManager().select(reselects);
		} else {
			player.getSelectionManager().setReselects(reselects);
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
		return true;
	}

	@Override
	public void undo(Player player) {
		if (isCached) {
			unCacheUndoables();
		}
		for (IUndoable undoable : undoables) {
			undoable.undo(player);
		}
	}

	@Override
	public void redo(Player player) {
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
		undoables = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			NBTTagCompound t = tag.getCompoundTag(String.valueOf(i));
			// Can this be generic? So far we only have one type of IUndoable
			IUndoable u = new UndoableSetBlock();
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