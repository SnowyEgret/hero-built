package ds.plato.core;

import net.minecraft.block.Block;

public class HotbarSlot {

	public Block block;
	//public int metadata;
	int slotNumber;
	
	//public HotbarSlot(Block block, int metadata, int slotNumber) {
	public HotbarSlot(Block block, int slotNumber) {
		super();
		this.block = block;
		//this.metadata = metadata;
		this.slotNumber = slotNumber;
	}

	public HotbarSlot(Block block) {
		this(block, 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + slotNumber;
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
		HotbarSlot other = (HotbarSlot) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (slotNumber != other.slotNumber)
			return false;
		return true;
	}
	
}
