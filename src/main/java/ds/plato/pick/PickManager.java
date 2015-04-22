package ds.plato.pick;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import ds.plato.api.IPick;
import ds.plato.api.IWorld;
import ds.plato.block.BlockPicked;

public class PickManager implements IPick {

	private final LinkedList<Pick> picks = new LinkedList<>();
	private int maxPicks = 0;
	private IWorld world;
	private Block blockPicked;

	public PickManager(Block blockPicked) {
		this.blockPicked = blockPicked;
	}

	@Override
	public void pick(IWorld world, int x, int y, int z, int side) {
		// TODO: Handle case where location is already a selection
		Block block = world.getBlock(x, y, z);
		int metadata = world.getMetadata(x, y, z);
		world.setBlock(x, y, z, blockPicked, 0);
		addPick(x, y, z, block, metadata, side);
		this.world = world;
	}

	@Override
	public boolean isPicking() {
		return picks.size() > 0 && !isFinishedPicking();
	}

	@Override
	public boolean isFinishedPicking() {
		return (picks.size() == maxPicks);
	}

	@Override
	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	@Override
	public void reset(int maxPicks) {
		this.maxPicks = maxPicks;
		picks.clear();
	}

	@Override
	public void clearPicks() {
		for (Pick p : getPicks()) {
			Block block = world.getBlock(p.x, p.y, p.z);
			if (block instanceof BlockPicked) {
				world.setBlock(p.x, p.y, p.z, p.block, p.metadata);
			}
		}
		picks.clear();
	}

	@Override
	public Pick getPickAt(int x, int y, int z) {
		for (Pick p : picks) {
			if (p.equals(new Pick(x, y, z, null, 0, 0))) {
				return p;
			}
		}
		return null;
	}

	@Override
	public Pick lastPick() {
		try {
			return picks.getLast();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PickManager [maxPicks=");
		builder.append(maxPicks);
		builder.append(", picks=");
		builder.append(picks);
		builder.append("]");
		return builder.toString();
	}

	PickManager() {
	}

	Pick getPick(int i) {
		return picks.get(i);
	}

	int size() {
		return picks.size();
	}

	Pick addPick(int x, int y, int z, Block block, int metadata, int side) {
		if (picks.size() < maxPicks) {
			Pick p = new Pick(x, y, z, block, metadata, side);
			picks.add(p);
			return p;
		} else {
			return null;
		}
	}
}
