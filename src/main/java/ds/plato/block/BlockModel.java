package ds.plato.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockModel extends BlockContainer  {

	public BlockModel() {
		super(Material.clay);
	}

	@Override
	public int getRenderType() {
		return BlockModelRenderer.id;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new BlockModelTileEntity();
	}

}
