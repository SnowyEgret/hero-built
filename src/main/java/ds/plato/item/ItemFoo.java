package ds.plato.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoo extends Item {

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World w, int x, int y, int z, int s, float sx, float sy,
			float sz) {
		System.out.println("[ItemFoo.onItemUse] w=" + w);
		w.setBlock(x, y, z, Blocks.dirt, 0, 3);
		return true;
	}

}
