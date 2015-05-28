package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.select.ISelect;
import ds.plato.select.Selection;
import ds.plato.select.SelectionManager;

public class BlockSelected extends Block {

	public static final BlockSelectedProperty selectedBlockProperty = new BlockSelectedProperty();
	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockSelected");
	private ISelect selectionManager;

	public BlockSelected() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

	public void setSelectionManager(ISelect selectionManager) {
		this.selectionManager = selectionManager;
	}

	// Is this the default layer?
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.SOLID;
	}

	@Override
	protected BlockState createBlockState() {
		ExtendedBlockState state = new ExtendedBlockState(this, new IProperty[0],
				new IUnlistedProperty[] { selectedBlockProperty });
		System.out.println(state);
		return state;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		assert IExtendedBlockState.class.isAssignableFrom(state.getClass());
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		Selection s = selectionManager.getSelection(pos);
		if (s != null) {
			Block selectedBlock = s.getBlock();
			IBlockState selectedBlockState = s.getState();
			extendedState = extendedState.withProperty(selectedBlockProperty, selectedBlockState);
		} else {
			extendedState = extendedState.withProperty(selectedBlockProperty, null);
		}
		return extendedState;
	}

}
