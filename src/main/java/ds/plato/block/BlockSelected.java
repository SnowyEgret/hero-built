package ds.plato.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockSelected extends Block {
	
	public BlockSelected() {
		super(Material.clay);
		setHardness(-1F);
		setStepSound(soundTypeGravel);
	}

//Upgrade to 1.8
//	@Override
//	public boolean renderAsNormalBlock() {
//		return false;
//	}

//	@Override
//	public int getRenderType() {
//		return BlockSelectedRenderer.id;
//	}
}
