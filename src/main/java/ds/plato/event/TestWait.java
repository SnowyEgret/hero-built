package ds.plato.event;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ds.plato.Plato;
import ds.plato.network.SetBlockStateMessage;

public class TestWait {
	public TestWait(World world, BlockPos pos) {
		Plato.network.sendToServer(new SetBlockStateMessage(pos, Blocks.dirt.getDefaultState()));
		//SetBlockStateMessage returns a message to the client which sets setBlockMessageDone true
		try {
			synchronized (this) {
				while (Plato.setBlockMessageDone == false) {
					wait(10);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Plato.setBlockMessageDone = false;
		System.out.println("state=" + world.getBlockState(pos));
	}

}
