package ds.plato.pick;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import ds.plato.Plato;
import ds.plato.block.BlockPicked;
import ds.plato.block.BlockSelected;
import ds.plato.block.PrevStateTileEntity;
import ds.plato.network.PickMessage;
import ds.plato.network.SelectionMessage;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.world.IWorld;

public class PickManager implements IPick {

	private LinkedList<Pick> picks = new LinkedList<>();
	private LinkedList<Pick> lastPicks = new LinkedList<>();
	private int maxPicks = 0;
	private Block blockPicked;

	public PickManager(Block blockPicked) {
		this.blockPicked = blockPicked;
	}

	@Override
	public Pick pick(IPlayer player, BlockPos pos, EnumFacing side) {
		Pick pick = pick(player.getWorld(), pos, side);
		Plato.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
		return pick;
	}

	@Override
	public void clearPicks(IPlayer player) {
		clearPicks(player.getWorld());
		Plato.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	@Override
	public void repick(IPlayer player) {
		repick(player.getWorld());
		Plato.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	// -------------------------------------------------------------------------

	@Override
	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	@Override
	public Pick getPick(BlockPos pos) {
		for (Pick p : picks) {
			if (pos.equals(p.getPos())) {
				return p;
			}
		}
		return null;
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
	public void reset(int maxPicks) {
		this.maxPicks = maxPicks;
	}

	@Override
	public Pick firstPick() {
		return getPicks()[0];
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

	// Private -------------------------------------------------------------------------

	private Pick pick(IWorld world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getActualState(pos);

		Pick pick = addPick(pos, state, side);
		if (pick != null) {
			world.setState(pos, blockPicked.getDefaultState());
			PrevStateTileEntity tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
			tileEntity.setPrevState(state);
			// Seems we don't have to do this
			// world.getWorld().markBlockForUpdate(pos);
		}
		return pick;
	}

	private void clearPicks(IWorld world) {
		for (Pick p : getPicks()) {
			IBlockState state = world.getActualState(p.getPos());
			world.setState(p.getPos(), p.getState());
		}
		lastPicks.clear();
		lastPicks.addAll(picks);
		picks.clear();
	}

	private void repick(IWorld world) {
		if (lastPicks != null) {
			for (Pick p : lastPicks) {
				pick(world, p.getPos(), p.side);
			}
		}
	}

	private Pick addPick(BlockPos pos, IBlockState state, EnumFacing side) {
		if (picks.size() < maxPicks) {
			Pick p = new Pick(pos, state, side);
			picks.add(p);
			return p;
		} else {
			System.out.println("Already reached maxPicks. Returning nulll");
			return null;
		}
	}

}
