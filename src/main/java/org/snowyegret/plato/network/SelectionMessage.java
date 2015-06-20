package org.snowyegret.plato.network;

import org.snowyegret.plato.gui.SelectionInfo;
import org.snowyegret.plato.select.ISelect;
import org.snowyegret.plato.select.Selection;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SelectionMessage implements IMessage {

	private static final int SIZE = 5;
	private int size, fx, fy, fz, lx, ly, lz;

	public SelectionMessage() {
	}

	public SelectionMessage(ISelect selectionManager) {
		size = selectionManager.size();
		Selection s = selectionManager.firstSelection();
		if (s != null) {
			BlockPos first = s.getPos();
			fx = first.getX();
			fy = first.getY();
			fz = first.getZ();
		}
		s = selectionManager.lastSelection();
		if (s != null) {
			BlockPos last = s.getPos();
			fx = last.getX();
			fy = last.getY();
			fz = last.getZ();
		}
	}

	public SelectionInfo getSelectionInfo() {
		BlockPos firstPos = new BlockPos(fx, fy, fz);
		BlockPos lastPos = new BlockPos(lx, ly, lz);
		return new SelectionInfo(size, firstPos, lastPos);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, size, SIZE);
		ByteBufUtils.writeVarInt(buf, fx, SIZE);
		ByteBufUtils.writeVarInt(buf, fy, SIZE);
		ByteBufUtils.writeVarInt(buf, fz, SIZE);
		ByteBufUtils.writeVarInt(buf, lx, SIZE);
		ByteBufUtils.writeVarInt(buf, ly, SIZE);
		ByteBufUtils.writeVarInt(buf, lz, SIZE);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		size = ByteBufUtils.readVarInt(buf, SIZE);
		fx = ByteBufUtils.readVarInt(buf, SIZE);
		fy = ByteBufUtils.readVarInt(buf, SIZE);
		fz = ByteBufUtils.readVarInt(buf, SIZE);
		lx = ByteBufUtils.readVarInt(buf, SIZE);
		ly = ByteBufUtils.readVarInt(buf, SIZE);
		lz = ByteBufUtils.readVarInt(buf, SIZE);
	}

}
