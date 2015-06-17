package ds.plato.undo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.world.IWorld;

public class UndoableSetBlock implements IUndoable {

	IWorld world;
	ISelect selectionManager;
	BlockPos pos;
	IBlockState state, prevState;

	public UndoableSetBlock(IWorld world, ISelect selectionManager, BlockPos pos, IBlockState state) {
		this.world = world;
		this.selectionManager = selectionManager;
		this.pos = pos;
		this.state = state;
		prevState = world.getState(pos);
	}

	public UndoableSetBlock doIt() {

		Selection s = selectionManager.getSelection(pos);
		if (s != null) {
			prevState = s.getState();
		}
		world.setState(pos, state);
		return this;
	}

	@Override
	public void undo() {
		//selectionManager.deselect(world, pos);
		world.setState(pos, prevState);
	}

	@Override
	public void redo() {
		world.setState(pos, state);
	}

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
