package org.snowyegret.mojo.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
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

import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.Selection;

public class BlockSelected extends Block implements ITileEntityProvider {

	public static final PrevStateProperty prevStateProperty = new PrevStateProperty();

	public BlockSelected() {
		super(Material.clay);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		// Fix for Plants not rendering properly when selected #171
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { prevStateProperty });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		PrevStateTileEntity tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
		IBlockState prevState = null;
		if (tileEntity != null) {
			prevState = tileEntity.getPrevState();
		}
		// else {
		// System.out.println("tileEntity=" + tileEntity);
		// // world.getTileEntity(pos);
		// // Can not get it from selectionManager because we are on the client side
		// }
		return ((IExtendedBlockState) state).withProperty(prevStateProperty, prevState);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new PrevStateTileEntity();
	}

}
