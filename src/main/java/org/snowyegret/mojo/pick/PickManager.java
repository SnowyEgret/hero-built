package org.snowyegret.mojo.pick;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.network.PickMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.world.IWorld;

public class PickManager {

	private LinkedList<Pick> picks = new LinkedList<>();
	private LinkedList<Pick> lastPicks = new LinkedList<>();
	private int numPicks = 0;
	private Block blockPicked;
	private Player player;

	public PickManager(Player player, Block blockPicked) {
		this.player = player;
		this.blockPicked = blockPicked;
	}  

	public Pick pick(BlockPos pos, EnumFacing side) {
		Pick pick = pick(player.getWorld(), pos, side);
		MoJo.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
		return pick;
	}

	public void clearPicks() {
		clearPicks(player.getWorld());
		MoJo.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	public void repick() {
		repick(player.getWorld());
		MoJo.network.sendTo(new PickMessage(this), (EntityPlayerMP) player.getPlayer());
	}

	// -------------------------------------------------------------------------

	public Pick[] getPicks() {
		Pick[] array = new Pick[picks.size()];
		return picks.toArray(array);
	}

	public Pick getPick(BlockPos pos) {
		for (Pick p : picks) {
			if (pos.equals(p.getPos())) {
				return p;
			}
		}
		return null;
	}

	public boolean isPicking() {
		return picks.size() > 0 && !isFinishedPicking();
	}

	public boolean isFinishedPicking() {
		return (picks.size() == numPicks);
	}

	public void setNumPicks(int numPicks) {
		this.numPicks = numPicks;
	}

	public Pick firstPick() {
		return getPicks()[0];
	}

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
		builder.append("PickManager [numPicks=");
		builder.append(numPicks);
		builder.append(", picks=");
		builder.append(picks);
		builder.append("]");
		return builder.toString();
	}

	// Private -------------------------------------------------------------------------

	private Pick pick(IWorld world, BlockPos pos, EnumFacing side) {

		if (picks.size() == numPicks) {
			return null;
		}

		IBlockState prevState = world.getActualState(pos);
		PrevStateTileEntity tileEntity;
		if (prevState.getBlock() instanceof BlockSelected) {
			// System.out.println("prevState=" + prevState);
			tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
			prevState = tileEntity.getPrevState();
			// If the BlockSelected is left in the world after a crash, prevState will be null.
			// When selecting these blocks with a selection spell to delete or fill them, the previous state
			// can simply be BlockPick's default state
			if (prevState == null) {
				System.out
						.println("When clearing BlockSelecteds left in world after a crash, tileEntity.getPrevState() is null. Setting to BlockSelected default state.");
				prevState = MoJo.blockSelected.getDefaultState();
			}
		}
		Pick pick = new Pick(pos, prevState, side);
		picks.add(pick);
		world.setState(pos, blockPicked.getDefaultState());
		tileEntity = (PrevStateTileEntity) world.getTileEntity(pos);
		tileEntity.setPrevState(prevState);
		return pick;
	}

	private void clearPicks(IWorld world) {
		for (Pick pick : getPicks()) {
			IBlockState state = world.getActualState(pick.getPos());
			// When picking a BlockSelected in an AbstractSpellSelect when selecting BlockSelected left
			// in world after a crash, there is no state on the tile entity.
			world.setState(pick.getPos(), pick.getState());
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
}
