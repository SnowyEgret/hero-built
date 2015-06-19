package ds.plato.undo;

import java.util.NoSuchElementException;

import ds.plato.player.IPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IUndoable {

	public IUndoable dO(IPlayer player);

	public void undo(IPlayer player) throws NoSuchElementException;

	public void redo(IPlayer player) throws NoSuchElementException;

	public NBTTagCompound toNBT();

	public IUndoable fromNBT(NBTTagCompound tag);

}
