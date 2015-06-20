package org.snowyegret.mojo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;
import org.snowyegret.mojo.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

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

	@Override
	public String toString() {
		return getDistribution().toString();
	}

	private class HotbarDistribution {

		private List<HotbarSlot> slots = new ArrayList<>();
		private final List<Integer> indices = new ArrayList<>();
		private final Random random = new Random();
		private final Map<Integer, IBlockState> mapPercentBlock = new TreeMap<>();

		public HotbarDistribution(List<HotbarSlot> slots) {
			this.slots = slots;
			int j = 0;
			for (HotbarSlot s : slots) {
				for (int i = 0; i < s.getIndex(); i++) {
					indices.add(j);
				}
				j++;
			}
			if (!indices.isEmpty()) {
				for (int i = 0; i < slots.size(); i++) {
					int percentage = 100 * slots.get(i).getIndex() / indices.size();
					if (i == slots.size() - 1) {
						int sum = 0;
						for (Integer p : getPecentages()) {
							sum += p;
						}
						sum += percentage;
						int d = 100 - sum;
						percentage += d;
					}
					mapPercentBlock.put(percentage, slots.get(i).getState());
				}
			}
		}

		public Set<Integer> getPecentages() {
			return mapPercentBlock.keySet();
		}

		public HotbarSlot randomSlot() {
			int i = random.nextInt(indices.size() - 1);
			return slots.get(indices.get(i));
		}

		@Override
		public String toString() {
			List<String> tokens = new ArrayList();
			for (Entry<Integer, IBlockState> e : mapPercentBlock.entrySet()) {
				IBlockState state = e.getValue();
				String name = state.getBlock().getLocalizedName();
				if (name.startsWith("tile.")) {
					name = StringUtils.lastWordInCamelCase(state.getClass().getSimpleName());
				}
				ImmutableMap props = state.getProperties();
				for (Object prop : props.values()) {
					String adjective = prop.toString();
					name = adjective + " " + name;
				}
				name = WordUtils.capitalize(name);
				tokens.add(String.format("%s: %d%%", name, e.getKey()));
			}
			return Joiner.on(", ").join(tokens);
		}
	}

}
