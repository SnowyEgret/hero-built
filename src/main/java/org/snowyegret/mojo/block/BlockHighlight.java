package org.snowyegret.mojo.block;

import java.awt.Color;

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

public class BlockHighlight extends Block implements ITileEntityProvider {

	public static final IUnlistedProperty PROP_STATE = new PropertyState();
	public static final IUnlistedProperty PROP_COLOR = new PropertyColor();
	public static final int COLOR_SELECTED = new Color(200, 200, 255).getRGB();
	public static final int COLOR_PICKED = new Color(255, 200, 200).getRGB();
	public static final int COLOR_DEFAULT = new Color(200, 200, 200).getRGB();

	public BlockHighlight() {
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
		// return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PROP_STATE });
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PROP_STATE, PROP_COLOR });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		BlockHightlightTileEntity tileEntity = (BlockHightlightTileEntity) world.getTileEntity(pos);
		IBlockState prevState = null;
		int color = COLOR_DEFAULT;
		if (tileEntity != null) {
			prevState = tileEntity.getPrevState();
			color = tileEntity.getColor();
		} else {
			System.out.println("tileEntity=" + tileEntity);
			// For debugging
			// world.getTileEntity(pos);
			// Can not get it from selectionManager because we are on the client side
		}

		return ((IExtendedBlockState) state).withProperty(PROP_STATE, prevState).withProperty(PROP_COLOR, color);
	}

	// @Override
	// public IBlockState getStateFromMeta(int meta) {
	// System.out.println("meta=" + meta);
	// // TODO Auto-generated method stub
	// return super.getStateFromMeta(meta);
	// }
	//
	// @Override
	// public int getMetaFromState(IBlockState state) {
	// // TODO Auto-generated method stub
	// System.out.println("state=" + state);
	// return 2;
	// }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BlockHightlightTileEntity();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

}
