package org.snowyegret.mojo.item.spell.select;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.block.PrevStateTileEntity;
import org.snowyegret.mojo.item.spell.Spell;
import org.snowyegret.mojo.message.client.SpellMessage;
import org.snowyegret.mojo.player.Player;
import org.snowyegret.mojo.select.SelectionManager;
import org.snowyegret.mojo.world.IWorld;

import com.google.common.collect.Lists;

public class SpellCleanup extends Spell {

	private final int d = 100;
	private final Vec3i v = new Vec3i(d, d, d);

	public SpellCleanup() {
		super(1);
	}

	@Override
	public void invoke(Player player) {
		player.clearPicks();
		BlockPos p = player.getPosition();
		Iterable<BlockPos> allInBox = BlockPos.getAllInBox(p.subtract(v), p.add(v));
		List positions = Lists.newArrayList();
		IWorld w = player.getWorld();
		for (BlockPos pos : allInBox) {
			Block b = w.getState(pos).getBlock();
			if (b instanceof BlockSelected || b instanceof BlockPicked) {
				positions.add(pos);
			} else {
				// Check that a PrevStateTileEntity has not been left in world at this position
				TileEntity te = w.getTileEntity(pos);
				if (te != null && te instanceof PrevStateTileEntity) {
					te.invalidate();
					System.out.println("Found a PrevStateTileEntity at a position not occupied"
							+ " by a BlockSelected/Picked. Invalidating. te=" + te);
				}
			}
		}

		SelectionManager sm = player.getSelectionManager();
		sm.select(positions);
		int numSelected = sm.size();
		System.out.println("numSelected=" + numSelected);
		sm.clearSelections();
		// TODO internationalize
		//player.sendMessage(new SpellMessage("Cleaned up " + numSelected + " blocks in " + d + " block radius."));
		//player.sendMessage(new SpellMessage("item.spell_cleanup.message", numSelected));
		player.sendMessage(new SpellMessage("item.spell_cleanup.message", numSelected));
	}

}
