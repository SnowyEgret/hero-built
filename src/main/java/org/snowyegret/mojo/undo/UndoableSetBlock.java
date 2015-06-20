package org.snowyegret.mojo.undo;

import org.snowyegret.mojo.player.IPlayer;
import org.snowyegret.mojo.world.IWorld;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class UndoableSetBlock implements IUndoable {

	BlockPos pos;
	IBlockState prevState, state;
	private static final String POS_KEY = "a";
	private static final String PREV_STATE_KEY = "b";
	private static final String STATE_ID_KEY = "c";

	public UndoableSetBlock() {
	}

	public UndoableSetBlock(BlockPos pos, IBlockState prevState, IBlockState state) {
		this.pos = pos;
		this.prevState = prevState;
		this.state = state;
	}

	// IUndoable--------------------------------------------------------------------------

	public UndoableSetBlock dO(IPlayer player) {
		player.getWorld().setState(pos, state);
		return this;
	}

	@Override
	public void undo(IPlayer player) {
		player.getWorld().setState(pos, prevState);
	}

	@Override
	public void redo(IPlayer player) {
		player.getWorld().setState(pos, state);
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong(POS_KEY, pos.toLong());
		tag.setInteger(PREV_STATE_KEY, Block.getStateId(prevState));
		tag.setInteger(STATE_ID_KEY, Block.getStateId(state));
		return tag;
	}

	@Override
	public IUndoable fromNBT(NBTTagCompound tag) {
		pos = BlockPos.fromLong(tag.getLong(POS_KEY));
		prevState = Block.getStateById(tag.getInteger(PREV_STATE_KEY));
		state = Block.getStateById(tag.getInteger(STATE_ID_KEY));
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
