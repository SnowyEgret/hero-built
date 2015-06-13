package ds.plato.item.spell.other;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.item.spell.Modifiers;
import ds.plato.item.spell.Spell;
import ds.plato.network.SetBlockStateMessage;
import ds.plato.pick.IPick;
import ds.plato.player.IPlayer;
import ds.plato.select.ISelect;
import ds.plato.undo.IUndo;
import ds.plato.undo.Transaction;
import ds.plato.undo.UndoableSetBlock;
import ds.plato.world.IWorld;

public class SpellTest extends Spell {

	public SpellTest() {
		super(1);
	}

	@Override
	public void invoke(IWorld world, IPlayer player) {
		Modifiers modifiers = player.getModifiers();
		ISelect selectionManager = player.getSelectionManager();
		IPick pickManager = player.getPickManager();
		IUndo undoManager = player.getUndoManager();

		pickManager.clearPicks(world);
		System.out.println("isRemote=" + world.getWorld().isRemote);
		BlockPos pos = player.getPosition().down();
		// world.setBlockState(pos, Blocks.dirt.getDefaultState());
		Transaction t = undoManager.newTransaction();
		for (int i = 0; i < 10; i++) {
			t.add(new UndoableSetBlock(world, selectionManager, pos.east(i), Blocks.dirt.getDefaultState()).set());
		}
		// Plato.network.sendToServer(new SetBlockStateMessage(pos, Blocks.dirt.getDefaultState()));
		t.commit();
		try {
			synchronized (this) {
				while (Plato.setBlockMessageDone == false) {
					wait(10);
				}
				System.out.println("state=" + world.getState(pos));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Plato.setBlockMessageDone = false;
	}
}
