package org.snowyegret.mojo.undo;

import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.world.IWorld;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class UndoableSetBlock implements IUndoable {

	BlockPos pos;
	IBlockState prevState, state;
	private static final String KEY_POS = "a";
	private static final String KEY_PREV_STATE = "b";
	private static final String KEY_STATE = "c";

	// For Transaction.fromNBT which can't be static if part of an interface (except in 1.8)
	// http://stackoverflow.com/questions/21817/why-cant-i-declare-static-methods-in-an-interface
	UndoableSetBlock() {
	}

	public UndoableSetBlock(BlockPos pos, IBlockState prevState, IBlockState state) {
		this.pos = pos;
		this.prevState = prevState;
		this.state = state;
	}

	// IUndoable--------------------------------------------------------------------------

	public boolean dO(Player player) {
		player.getWorld().setState(pos, state);
		return true;
	}

	@Override
	public void undo(Player player) {
		player.getWorld().setState(pos, prevState);
	}

	@Override
	public void redo(Player player) {
		player.getWorld().setState(pos, state);
	}

	// Implemented to save a large transaction to disk.
	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(KEY_POS, pos.toLong());
		tag.setInteger(KEY_PREV_STATE, Block.getStateId(prevState));
		tag.setInteger(KEY_STATE, Block.getStateId(state));
		return tag;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound tag) {
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		prevState = Block.getStateById(tag.getInteger(KEY_PREV_STATE));
		state = Block.getStateById(tag.getInteger(KEY_STATE));
		return new UndoableSetBlock(pos, prevState, state);
	}

	// --------------------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((prevState == null) ? 0 : prevState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UndoableSetBlock other = (UndoableSetBlock) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (prevState == null) {
			if (other.prevState != null)
				return false;
		} else if (!prevState.equals(other.prevState))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlock [pos=");
		builder.append(pos);
		builder.append(", state=");
		builder.append(state);
		builder.append(", prevState=");
		builder.append(prevState);
		builder.append("]");
		return builder.toString();
	}
}
