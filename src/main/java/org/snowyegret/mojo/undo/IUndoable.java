package org.snowyegret.mojo.undo;

import java.util.NoSuchElementException;

import org.snowyegret.mojo.player.Player;

import net.minecraft.nbt.NBTTagCompound;

public interface IUndoable {

	public boolean dO(Player player);

	public void undo(Player player) throws NoSuchElementException;

	public void redo(Player player) throws NoSuchElementException;

	public NBTTagCompound toNBT();

	public IUndoable fromNBT(NBTTagCompound tag);

}
