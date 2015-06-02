package ds.plato.select;

import javax.vecmath.Point3d;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class Selection {

	private BlockPos pos;
	@Deprecated
	private Block block;
	private IBlockState state;

	@Deprecated
	public Selection(BlockPos pos, Block block) {
		this.pos = pos;
		this.block = block;
	}

	public Selection(BlockPos pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
	}

	public Point3d point3d() {
		return new Point3d(pos.getX(), pos.getY(), pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}

	@Deprecated
	public void setBlock(Block block) {
		this.block = block;
	}

	@Deprecated
	public Block getBlock() {
		return block;
	}

	public void setState(IBlockState state) {
		this.state = state;
	}

	public IBlockState getState() {
		return state;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Selection [pos=");
		builder.append(pos);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	//TODO check that state.equals is functional
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Selection other = (Selection) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

}
