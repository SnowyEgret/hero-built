package ds.plato.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MouseClickMessage implements IMessage {

	private static final int SIZE = 5;
	private int button;
	private int x, y, z;
	private MovingObjectType typeOfHit;

	public MouseClickMessage() {
	}

	public MouseClickMessage(int button, BlockPos pos, MovingObjectType typeOfHit) {
		this.button = button;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.typeOfHit = typeOfHit;

	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, button, SIZE);
		ByteBufUtils.writeVarInt(buf, x, SIZE);
		ByteBufUtils.writeVarInt(buf, y, SIZE);
		ByteBufUtils.writeVarInt(buf, z, SIZE);
		ByteBufUtils.writeVarInt(buf, typeOfHit.ordinal(), SIZE);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		button = ByteBufUtils.readVarInt(buf, SIZE);
		x = ByteBufUtils.readVarInt(buf, SIZE);
		y = ByteBufUtils.readVarInt(buf, SIZE);
		z = ByteBufUtils.readVarInt(buf, SIZE);
		int ordinal = ByteBufUtils.readVarInt(buf, SIZE);
		for (MovingObjectType t : MovingObjectType.values()) {
			if (t.ordinal() == ordinal) {
				typeOfHit = t;
				break;
			}
		}
	}

	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public int getButton() {
		return button;
	}

	public MovingObjectType getTypeOfHit() {
		return typeOfHit;
	}

}
