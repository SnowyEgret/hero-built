package ds.plato.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import ds.plato.player.IPlayer;
import ds.plato.player.Player;

public class MyEvent extends Event {

	private IPlayer player;
	private BlockPos pos;

	public MyEvent(EntityPlayerMP player, BlockPos pos) {
		this.player = Player.instance(player);
		this.pos = pos;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public BlockPos getPos() {
		return pos;
	}

}
