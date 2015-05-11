package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ds.plato.api.ISelect;
import ds.plato.select.Selection;
import ds.plato.select.SelectionManager;

public class BlockSelected extends Block {

	public static final PropertyPreviousBlock prevBlockProperty = new PropertyPreviousBlock();
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
				new IUnlistedProperty[] { prevBlockProperty });
		System.out.println(state);
		return state;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		assert IExtendedBlockState.class.isAssignableFrom(state.getClass());
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		System.out.println(state);
		Selection s = selectionManager.getSelection(pos);
		System.out.println("s=" + s);
		if (s != null) {
			Block selectedBlock = s.getBlock();
			System.out.println("selectedBlock="+selectedBlock);
			//IBlockState selectedBlockState = selectedBlock.getActualState(extendedState, world, pos);
			IBlockState selectedBlockState = selectedBlock.getDefaultState();
			System.out.println("selectedBlockState="+selectedBlockState);
			extendedState = extendedState.withProperty(prevBlockProperty, selectedBlockState);
		} else {
			System.out.println("Block not selected at pos="+pos);
			extendedState = extendedState.withProperty(prevBlockProperty, null);
		}
		System.out.println(extendedState.getUnlistedProperties());
		return extendedState;
	}

}
