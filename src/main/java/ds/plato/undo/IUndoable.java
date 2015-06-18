package ds.plato.undo;

import java.util.NoSuchElementException;

import net.minecraft.nbt.NBTTagCompound;

public interface IUndoable {

	public IUndoable dO();

	public void undo() throws NoSuchElementException;

	public void redo() throws NoSuchElementException;

	public NBTTagCompound toNBT();

	public IUndoable fromNBT(NBTTagCompound tag);

}
