package ds.plato.pick;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;

public class Pick {

	public int x, y, z;
	public Block block;
	public int metadata;
	public int side;

	public Pick(int x, int y, int z, Block block, int metadata, int side) {
		// super(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		this.metadata = metadata;
		this.side = side;
	}

	public Point3d point3d() {
		return new Point3d(x, y, z);
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
		Pick other = (Pick) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pick [block=");
		builder.append(block);
		builder.append(", metadata=");
		builder.append(metadata);
		builder.append(", side=");
		builder.append(side);
		builder.append(", x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", z=");
		builder.append(z);
		builder.append("]");
		return builder.toString();
	}

	public Point3i point3i() {
		return new Point3i(x, y, z);
	}
}
