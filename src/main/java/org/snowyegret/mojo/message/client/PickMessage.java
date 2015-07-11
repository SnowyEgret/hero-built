package org.snowyegret.mojo.message.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import org.snowyegret.mojo.gui.PickInfo;
import org.snowyegret.mojo.pick.Pick;
import org.snowyegret.mojo.pick.PickManager;

public class PickMessage implements IMessage {

	private boolean isPicking, isFinishedPicking;
	// Must not be null or toBytes will crash
	BlockPos lastPos = new BlockPos(0, 0, 0);

	public PickMessage() {
	}

	public PickMessage(PickManager pickManager) {
		isPicking = pickManager.isPicking();
		isFinishedPicking = pickManager.isFinishedPicking();
		Pick pick = pickManager.lastPick();
		if (pick != null) {
			lastPos = pick.getPos();
		}
	}

	public PickInfo getPickInfo() {
		return new PickInfo(isPicking, isFinishedPicking, lastPos);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isPicking);
		buf.writeBoolean(isFinishedPicking);
		buf.writeLong(lastPos.toLong());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		isPicking = buf.readBoolean();
		isFinishedPicking = buf.readBoolean();
		lastPos = BlockPos.fromLong(buf.readLong());
	}

}
