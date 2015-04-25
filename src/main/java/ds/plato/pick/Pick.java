package ds.plato.pick;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Pick {

	BlockPos pos;
	public Block block;
	public EnumFacing side;

	public Pick(BlockPos pos, Block block, EnumFacing side) {
		this.pos=pos;
		this.block = block;
		this.side = side;
	}

	public Point3d point3d() {
		return new Point3d(pos.getX(),pos.getY(), pos.getZ());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Pick other = (Pick) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pick [pos=");
		builder.append(pos);
		builder.append(", block=");
		builder.append(block);
		builder.append(", side=");
		builder.append(side);
		builder.append("]");
		return builder.toString();
	}

	public Point3i point3i() {
		return new Point3i(pos.getX(),pos.getY(), pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}
}
