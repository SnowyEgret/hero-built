package ds.plato.undo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import ds.plato.api.ISelect;
import ds.plato.api.IUndoable;
import ds.plato.api.IWorld;
import ds.plato.select.Selection;
import ds.plato.util.StringUtils;

public class SetBlock implements IUndoable {

	IWorld world;
	ISelect selectionManager;
	public final int x, y, z;
	Block block, prevBlock;
	int metadata, prevMetadata;

	public SetBlock(IWorld world, ISelect selectionManager, Selection s) {
		this(world, selectionManager, s.x, s.y, s.z, s.block, s.metadata);
	}

	public SetBlock(IWorld world, ISelect selectionManager, int x, int y, int z, Block block, int metadata) {
		this.world = world;
		this.selectionManager = selectionManager;
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		prevBlock = world.getBlock(x, y, z);
		this.metadata = metadata;
		prevMetadata = world.getMetadata(x, y, z);
	}

	public SetBlock set() {

		Selection s = selectionManager.selectionAt(x, y, z);
		if (s != null) {
			prevBlock = s.block;
			prevMetadata = s.metadata;
		}
		world.setBlock(x, y, z, block, metadata);

		//FIXME Unit test fails.
		if (block instanceof BlockAir) {
			//We do not want a selection pointing to a newly set air block.
			selectionManager.removeSelection(x, y, z);
		} else {
			selectionManager.select(world, x, y, z);
		}
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		SetBlock other = (SetBlock) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public void undo() {
		selectionManager.clearSelections();
		//TODO commented out for now
		// pickManager.clearPicks();
		world.setBlock(x, y, z, prevBlock, prevMetadata);
	}

	@Override
	public void redo() {
		world.setBlock(x, y, z, block, metadata);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetBlock [x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", z=");
		builder.append(z);
		builder.append(", block=");
		builder.append(StringUtils.lastWordInCamelCase(block.getClass().getSimpleName()));
		builder.append(", prevBlock=");
		builder.append(StringUtils.lastWordInCamelCase(prevBlock.getClass().getSimpleName()));
		builder.append(", metadata=");
		builder.append(metadata);
		builder.append(", prevMetadata=");
		builder.append(prevMetadata);
		builder.append("]");
		return builder.toString();
	}

}
