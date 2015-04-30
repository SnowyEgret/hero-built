package ds.plato.undo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import ds.plato.api.ISelect;
import ds.plato.api.IUndoable;
import ds.plato.api.IWorld;
import ds.plato.select.Selection;
import ds.plato.util.StringUtils;

public class UndoableSetBlock implements IUndoable {

	IWorld world;
	ISelect selectionManager;
	BlockPos pos;
	Block block, prevBlock;

	public UndoableSetBlock(IWorld world, ISelect selectionManager, Selection s) {
		this(world, selectionManager, s.getPos(), s.getBlock());
	}

	public UndoableSetBlock(IWorld world, ISelect selectionManager, BlockPos pos, Block block) {
		this.world = world;
		this.selectionManager = selectionManager;
		this.pos = pos;
		this.block = block;
		prevBlock = world.getBlock(pos);
	}

	public UndoableSetBlock set() {

		Selection s = selectionManager.getSelection(pos);
		if (s != null) {
			prevBlock = s.getBlock();
		}
		world.setBlock(pos, block);

		//FIXME Unit test fails.
		if (block instanceof BlockAir) {
			//We do not want a selection pointing to a newly set air block.
			//This is the only place removeSelection is called
			//selectionManager.removeSelection(pos);
			System.out.println("Should we have selected block air?");
		} else {
			selectionManager.select(world, pos);
		}
		return this;
	}

	@Override
	public void undo() {
		selectionManager.clearSelections(world);
		//TODO commented out for now
		//pickManager.clearPicks();
		world.setBlock(pos, prevBlock);
	}

	@Override
	public void redo() {
		world.setBlock(pos, block);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result
				+ ((prevBlock == null) ? 0 : prevBlock.hashCode());
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
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (prevBlock == null) {
			if (other.prevBlock != null)
				return false;
		} else if (!prevBlock.equals(other.prevBlock))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlock [pos=");
		builder.append(pos);
		builder.append(", block=");
		builder.append(block);
		builder.append(", prevBlock=");
		builder.append(prevBlock);
		builder.append("]");
		return builder.toString();
	}
}
