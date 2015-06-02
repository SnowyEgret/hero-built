package ds.plato.player;

import net.minecraft.block.state.IBlockState;

public class HotbarSlot {

	public IBlockState state;
	int slotNumber;
	
	public HotbarSlot(IBlockState state, int slotNumber) {
		super();
		this.state = state;
		this.slotNumber = slotNumber;
	}

	public HotbarSlot(IBlockState block) {
		this(block, 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (slotNumber != other.slotNumber)
			return false;
		return true;
	}
	
}
