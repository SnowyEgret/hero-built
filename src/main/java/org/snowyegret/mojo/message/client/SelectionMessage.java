package org.snowyegret.mojo.message.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.snowyegret.mojo.event.EventHandlerClient;
import org.snowyegret.mojo.gui.SelectionInfo;
import org.snowyegret.mojo.select.Selection;
import org.snowyegret.mojo.select.SelectionManager;

public class SelectionMessage implements IMessage {

	private int size;
	private BlockPos firstPos = new BlockPos(0, 0, 0);
	private BlockPos lastPos = new BlockPos(0, 0, 0);
	private BlockPos centroid = new BlockPos(0, 0, 0);

	public SelectionMessage() {
	}

	public SelectionMessage(SelectionManager selectionManager) {
		size = selectionManager.size();
		Selection s = selectionManager.firstSelection();
		if (s != null) {
			firstPos = s.getPos();
		}
		s = selectionManager.lastSelection();
		if (s != null) {
			lastPos = s.getPos();
		}
		Vec3 c = selectionManager.getCentroid();
		if (c != null) {
			centroid = new BlockPos(c.xCoord, c.yCoord, c.zCoord);
		}
	}

	public SelectionInfo getSelectionInfo() {
		return new SelectionInfo(size, firstPos, lastPos, centroid);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(size);
		buf.writeLong(firstPos.toLong());
		buf.writeLong(lastPos.toLong());
		buf.writeLong(centroid.toLong());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		size = buf.readInt();
		firstPos = BlockPos.fromLong(buf.readLong());
		lastPos = BlockPos.fromLong(buf.readLong());
		centroid = BlockPos.fromLong(buf.readLong());
	}
}
