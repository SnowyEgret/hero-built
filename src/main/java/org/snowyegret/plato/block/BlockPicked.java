package org.snowyegret.plato.block;

import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;

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

public class BlockPicked extends Block implements ITileEntityProvider {

	public static final BlockPickedProperty pickedBlockProperty = new BlockPickedProperty();
	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockPicked");

	public BlockPicked() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

	// Is this the default layer?
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.SOLID;
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { pickedBlockProperty });
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		PrevStateTileEntity tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
		IBlockState prevState = null;
		if (tileEntity != null) {
			prevState = tileEntity.getPrevState();
		} else {
			System.out.println("No tile entity on Block");
		}
		extendedState = extendedState.withProperty(pickedBlockProperty, prevState);
		return extendedState;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new PrevStateTileEntity();
	}

}
