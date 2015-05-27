package ds.plato.select;

import javax.vecmath.Point3d;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class Selection {

	private BlockPos pos;
	private Block block;
	private IBlockState state;

	public Selection(BlockPos pos, Block block) {
		this.pos = pos;
		this.block = block;
	}

	public Point3d point3d() {
		return new Point3d(pos.getX(), pos.getY(), pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Selection [pos=");
		builder.append(pos);
		builder.append(", block=");
		builder.append(block);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
		Selection other = (Selection) obj;
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
		return true;
	}

	public void setState(IBlockState state) {
		this.state = state;
	}

	public IBlockState getState() {
		return state;
	}
}
