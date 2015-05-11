package ds.plato.block;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyPreviousBlock implements IUnlistedProperty<IBlockState> {

	@Override
	public String getName() {
		return "PropertyPreviousBlock";
	}

	@Override
	public boolean isValid(IBlockState value) {
		return true;
	}

	@Override
	public Class<IBlockState> getType() {
		return IBlockState.class;
	}

	@Override
	public String valueToString(IBlockState value) {
		return value.toString();
	}

}
