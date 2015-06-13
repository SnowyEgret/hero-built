package ds.plato.player;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Hotbar {

	private List<HotbarSlot> slots = new ArrayList<>();

	public Hotbar(InventoryPlayer inventory) {
		// TODO
		for (int i = 0; i < inventory.getHotbarSize(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null) {
				Item item = stack.getItem();
				int meta = item.getDamage(stack);
				Block b = null;
				if (item instanceof ItemBlock) {
					ItemBlock itemBlock = (ItemBlock) item;
					b = itemBlock.getBlock();
				} else if (item == Items.water_bucket) {
					b = Blocks.water;
				} else if (item == Items.lava_bucket) {
					b = Blocks.lava;
				}

				if (b != null) {
					IBlockState state = b.getStateFromMeta(meta);
					HotbarSlot slot = new HotbarSlot(state, i + 1);
					slots.add(slot);
				}
			}
		}
		if (slots.isEmpty()) {
			slots.add(new HotbarSlot(Blocks.dirt.getDefaultState()));
		}
	}

	public IBlockState firstBlock() {
		return slots.iterator().next().getState();
	}

	public HotbarDistribution getDistribution() {
		return new HotbarDistribution(slots);
	}

	public IBlockState randomBlock() {
		return new HotbarDistribution(slots).randomSlot().getState();
	}

	public IBlockState getBlock(int i) {
		return slots.get(i).getState();
	}

}
