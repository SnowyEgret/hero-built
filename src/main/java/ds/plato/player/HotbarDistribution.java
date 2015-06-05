package ds.plato.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import com.google.common.base.Joiner;

import ds.plato.util.StringUtils;

public class HotbarDistribution {

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
			//TODO get color of BlockColored for example Wool
			//int color = b.getBlockColor();
			String name = state.getBlock().getLocalizedName();
			if (name.startsWith("tile.")) {
				name = StringUtils.lastWordInCamelCase(state.getClass().getSimpleName());
			}
			//props = state.getProperties()
			tokens.add(String.format("%s: %d%%", name, e.getKey()));
		}
		return Joiner.on(", ").join(tokens);
	}
}
