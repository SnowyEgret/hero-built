package ds.plato.network;

import ds.plato.item.spell.Modifier;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KeyMessage implements IMessage {

	private int keyCode;
	private boolean keyState;
	private static final int SIZE = 5;

	public KeyMessage() {
	}

	public KeyMessage(int keyCode, boolean keyState) {
		this.keyCode = keyCode;
		this.keyState = keyState;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean getKeyState() {
		return keyState;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		keyCode= ByteBufUtils.readVarInt(buf, SIZE);
		int i = ByteBufUtils.readVarInt(buf, SIZE);
		keyState = (i == 0) ? true : false;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, keyCode, SIZE);
		ByteBufUtils.writeVarInt(buf, keyState ? 0 : 1, SIZE);
	}

}
