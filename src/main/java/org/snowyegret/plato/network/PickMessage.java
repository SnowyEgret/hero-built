package org.snowyegret.plato.network;

import org.snowyegret.plato.gui.PickInfo;
import org.snowyegret.plato.pick.IPick;
import org.snowyegret.plato.pick.Pick;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PickMessage implements IMessage {

	private static final int SIZE = 5;
	private int x, y, z, isFinishedPicking;

	public PickMessage() {
	}

	public PickMessage(IPick pickManager) {
		isFinishedPicking = pickManager.isFinishedPicking() ? 0 : 1;
		Pick pick = pickManager.lastPick();
		if (pick != null) {
			BlockPos p = pick.getPos();
			x = p.getX();
			y = p.getY();
			z = p.getZ();
		}
	}

	public PickInfo getPickInfo() {
		BlockPos lastPos = new BlockPos(x, y, z);
		boolean finished = (isFinishedPicking == 0) ? true : false;
		return new PickInfo(finished, lastPos);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, x, SIZE);
		ByteBufUtils.writeVarInt(buf, y, SIZE);
		ByteBufUtils.writeVarInt(buf, z, SIZE);
		ByteBufUtils.writeVarInt(buf, isFinishedPicking, SIZE);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = ByteBufUtils.readVarInt(buf, SIZE);
		y = ByteBufUtils.readVarInt(buf, SIZE);
		z = ByteBufUtils.readVarInt(buf, SIZE);
		isFinishedPicking = ByteBufUtils.readVarInt(buf, SIZE);
	}

}
