package ds.plato.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import ds.plato.util.StringUtils;
import net.minecraft.block.Block;

public class HotbarDistribution {

	private final HotbarSlot[] slots;
	private final List<Integer> indices = new ArrayList<>();
	private final Random random = new Random();
	private final Map<Integer, Block> mapPercentBlock = new TreeMap<>();

	public HotbarDistribution(HotbarSlot... slots) {
		this.slots = slots;
		int j = 0;
		for (HotbarSlot s : slots) {
			for (int i = 0; i < s.slotNumber; i++) {
				indices.add(j);
			}
			j++;
		}
		if (!indices.isEmpty()) {
			for (int i = 0; i < slots.length; i++) {
				int percentage = 100 * slots[i].slotNumber / indices.size();
				if (i == slots.length - 1) {
					int sum = 0;
					for (Integer p : getPecentages()) {
						sum += p;
					}
					sum += percentage;
					int d = 100 - sum;
					percentage += d;
				}
				mapPercentBlock.put(percentage, slots[i].block.getBlock());
			}
		}
	}

	public Set<Integer> getPecentages() {
		return mapPercentBlock.keySet();
	}

	public HotbarSlot randomSlot() {
		int i = random.nextInt(indices.size() - 1);
		return slots[indices.get(i)];
	}

	@Override
	public String toString() {
		List<String> tokens = new ArrayList();
		for (Entry<Integer, Block> e : mapPercentBlock.entrySet()) {
			Block b = e.getValue();
			//TODO get color of BlockColored for example Wool
			//int color = b.getBlockColor();
			//System.out.println("[SlotDistribution.toString] color=" + color);
			String name = b.getLocalizedName();
			if (name.startsWith("tile.")) {
				name = StringUtils.lastWordInCamelCase(b.getClass().getSimpleName());
			}
			tokens.add(String.format("%s: %d%%", name, e.getKey()));
		}
		return Joiner.on(", ").join(tokens);
	}
}
