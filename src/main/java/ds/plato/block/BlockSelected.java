package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSelected extends Block implements ITileEntityProvider {

	public static final BlockSelectedProperty selectedBlockProperty = new BlockSelectedProperty();
	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockSelected");

	public BlockSelected() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
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
		PrevStateTileEntity tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
		IBlockState prevState = null;
		if (tileEntity != null) {
			prevState = tileEntity.getPrevState();
		} else {
			System.out.println("No tile entity on Block");
		}
		extendedState = extendedState.withProperty(selectedBlockProperty, prevState);
		return extendedState;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new PrevStateTileEntity();
	}

}
