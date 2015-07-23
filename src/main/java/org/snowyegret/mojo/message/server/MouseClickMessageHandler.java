package org.snowyegret.mojo.message.server;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.item.spell.Modifier;
import org.snowyegret.mojo.item.spell.Modifiers;
import org.snowyegret.mojo.item.spell.select.Select;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MouseClickMessageHandler implements IMessageHandler<MouseClickMessage, IMessage> {

	private int lastSize;

	@Override
	public IMessage onMessage(final MouseClickMessage message, MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		WorldServer server = player.getServerForPlayer();
		server.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, player);
			}
		});
		return null;
	}

	// Private------------------------------------------------------------

	private void processMessage(MouseClickMessage message, EntityPlayerMP playerIn) {
		System.out.println("message=" + message);
		Player player = new Player(playerIn);
		SelectionManager selectionManager = player.getSelectionManager();
		MovingObjectType typeOfHit = message.getTypeOfHit();
		int button = message.getButton();
		// System.out.println("button=" + button);
		switch (typeOfHit) {
		case BLOCK:
			if (button == 0) {
				select(message.getPos(), player, message.isDoubleClick());
			}
			break;
		case ENTITY:
			break;
		case MISS:
			if (button == 0) {
				selectionManager.clearSelections();
			} else if (button == 1) {
				player.getPickManager().clearPicks();
			}
			break;
		default:
			break;
		}
	}

	private void select(BlockPos pos, Player player, boolean isDoubleClick) {

		Modifiers modifiers = player.getModifiers();
		boolean selectRegion = modifiers.isPressed(Modifier.SHIFT);
		boolean matchFirstSelection = modifiers.isPressed(Modifier.ALT);
		boolean addToSelection = modifiers.isPressed(Modifier.CTRL);
		SelectionManager sm = player.getSelectionManager();

		if (isDoubleClick) {
			lastSize = 0;
			Block block = player.getWorld().getBlock(pos);
			if (block instanceof BlockSelected) {
				PrevStateTileEntity te = (PrevStateTileEntity) player.getWorld().getTileEntity(pos);
				block = te.getPrevState().getBlock();
				// block = sm.getSelection(pos).getState().getBlock();
			} else {
				System.out.println("Expected a BlockSelected");
				return;
			}
			Set<BlockPos> positions = Sets.newHashSet();
			recursivelySelect(positions, Sets.newHashSet(pos), player.getWorld(), block);
			//System.out.println("size=" + positions.size());
			sm.select(positions);
			return;
		}

		// Shift replaces the current selections with a region.
		if (selectRegion && sm.size() != 0) {
			BlockPos lastPos = sm.lastSelection().getPos();
			IBlockState firstState = sm.firstSelection().getState();
			sm.clearSelections();

			Iterable<BlockPos> allInBox = BlockPos.getAllInBox(lastPos, pos);
			ArrayList<BlockPos> positions = Lists.newArrayList();
			for (BlockPos p : allInBox) {
				IBlockState state = player.getWorld().getActualState(p);
				if (state.getBlock() == Blocks.air) {
					continue;
				}
				if (state.getBlock().getMaterial().isLiquid()) {
					continue;
				}
				if (matchFirstSelection) {
					if (state == firstState) {
						positions.add(p);
					}
				} else {
					positions.add(p);
				}
			}
			sm.select(positions);
			return;
		}

		// Control adds or subtracts a selection to the current selections
		if (addToSelection) {
			if (sm.isSelected(pos)) {
				sm.deselect(pos);
			} else {
				sm.select(pos);
			}
			return;
		}

		// No modifier replaces the current selections with a new selection
		sm.clearSelections();
		sm.select(pos);
	}

	private void recursivelySelect(Set<BlockPos> positions, Set<BlockPos> newPositions, IWorld world, Block block) {
		Set<BlockPos> newNewPositions = Sets.newHashSet();
		for (BlockPos pos : newPositions) {
			for (BlockPos p : Select.ALL) {
				p = p.add(pos);
				Block b = world.getBlock(p);
				if (b == block) {
					positions.add(p);
					newNewPositions.add(p);
				}
			}
		}
		newPositions = newNewPositions;
		// newPositions.clear();
		// newPositions.addAll(newNewPositions);
		int size = positions.size();
		if (size < 1000 && size > lastSize) {
			lastSize = size;
			recursivelySelect(positions, newPositions, world, block);
		}
	}

}
