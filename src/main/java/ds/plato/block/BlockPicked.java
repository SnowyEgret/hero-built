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
import ds.plato.api.IPick;
import ds.plato.pick.Pick;

public class BlockPicked extends Block {
	
	public static final BlockPickedProperty pickedBlockProperty = new BlockPickedProperty();
	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockPicked");
	private IPick pickManager;

	public BlockPicked() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

	public void setPickManager(IPick pickManager) {
		this.pickManager = pickManager;
	}

	// Is this the default layer?
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.SOLID;
	}

	@Override
	protected BlockState createBlockState() {
		ExtendedBlockState state = new ExtendedBlockState(this, new IProperty[0],
				new IUnlistedProperty[] { pickedBlockProperty });
		System.out.println(state);
		return state;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		assert IExtendedBlockState.class.isAssignableFrom(state.getClass());
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		System.out.println(state);
		Pick pick = pickManager.getPick(pos);
		System.out.println("s=" + pick);
		if (pick != null) {
			Block pickedBlock = pick.getBlock();
			System.out.println("selectedBlock="+pickedBlock);
			//TODO TGG's example did this
			//IBlockState selectedBlockState = selectedBlock.getActualState(extendedState, world, pos);
			IBlockState pickedBlockState = pickedBlock.getDefaultState();
			System.out.println("selectedBlockState="+pickedBlockState);
			extendedState = extendedState.withProperty(pickedBlockProperty, pickedBlockState);
		} else {
			System.out.println("Block not selected at pos="+pos);
			extendedState = extendedState.withProperty(pickedBlockProperty, null);
		}
		System.out.println(extendedState.getUnlistedProperties());
		return extendedState;
	}

}
