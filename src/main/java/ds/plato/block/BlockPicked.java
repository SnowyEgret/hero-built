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
import ds.plato.pick.IPick;
import ds.plato.pick.Pick;
import ds.plato.select.ISelect;

public class BlockPicked extends Block {

	public static final BlockPickedProperty pickedBlockProperty = new BlockPickedProperty();
	public static ModelResourceLocation modelResourceLocation = new ModelResourceLocation("plato:blockPicked");
	private IPick pickManager;
	//private ISelect selectionManager;

	public IPick getPickManager() {
		return pickManager;
	}

	public BlockPicked() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

	public void setPickManager(IPick pickManager) {
		this.pickManager = pickManager;
	}

//	public void setSelectionManager(ISelect selectionManager) {
//		this.selectionManager = selectionManager;
//	}

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
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		Pick pick = pickManager.getPick(pos);
		if (pick != null) {
			//Block pickedBlock = pick.getState().getBlock();
			// Commented out because could not reproduce bug it was trying to fix (infinite loop at
			// isAmbientOcclusion())
			// Handle case where pick is already selected
			//if (!(pickedBlock instanceof BlockPicked)) {
			//	pickedBlock = selectionManager.getSelection(pos).getBlock();
			//}
			IBlockState pickedBlockState = pick.getState();
			extendedState = extendedState.withProperty(pickedBlockProperty, pickedBlockState);
		} else {
			extendedState = extendedState.withProperty(pickedBlockProperty, null);
		}
		return extendedState;
	}

}
